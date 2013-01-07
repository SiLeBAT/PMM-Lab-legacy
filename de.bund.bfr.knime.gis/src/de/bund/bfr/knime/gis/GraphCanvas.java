package de.bund.bfr.knime.gis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;

public class GraphCanvas extends JPanel implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private static final String CIRCLE_LAYOUT = "Circle Layout";
	private static final String FR_LAYOUT = "FR Layout";
	private static final String FR_LAYOUT_2 = "FR Layout 2";
	private static final String ISOM_LAYOUT = "ISOM Layout";
	private static final String KK_LAYOUT = "KK Layout";
	private static final String SPRING_LAYOUT = "Spring Layout";
	private static final String SPRING_LAYOUT_2 = "Spring Layout 2";
	private static final String[] LAYOUTS = { CIRCLE_LAYOUT, FR_LAYOUT,
			FR_LAYOUT_2, ISOM_LAYOUT, KK_LAYOUT, SPRING_LAYOUT, SPRING_LAYOUT_2 };

	private static final String TRANSFORMING_MODE = "Transforming";
	private static final String PICKING_MODE = "Picking";
	private static final String[] MODES = { TRANSFORMING_MODE, PICKING_MODE };

	private static final String EQUAL = "==";
	private static final String NOT_EQUAL = "!=";
	private static final String GREATER = ">";
	private static final String LESS = "<";
	private static final String VALUE = "Value";
	private static final String[] CONDITIONS = { EQUAL, NOT_EQUAL, GREATER,
			LESS, VALUE };

	private static final String DEFAULT_LAYOUT = CIRCLE_LAYOUT;
	private static final int DEFAULT_NODESIZE = 10;
	private static final String DEFAULT_MODE = TRANSFORMING_MODE;

	private List<Node> nodes;
	private List<Edge> edges;
	private Map<Node, List<Edge>> connectingEdges;
	private Set<String> allNodeProperties;
	private VisualizationViewer<Node, Edge> viewer;

	private List<NodeSelectionListener> nodeSelectionListeners;

	private JComboBox<String> layoutBox;
	private JButton layoutButton;
	private JTextField nodeSizeField;
	private JButton nodeSizeButton;
	private JComboBox<String> modeBox;
	private JButton nodePropertiesButton;
	private JButton edgePropertiesButton;
	private JComboBox<String> conditionPropertyBox;
	private JComboBox<String> conditionTypeBox;
	private JTextField conditionField;
	private JButton conditionButton;

	public GraphCanvas(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
		connectingEdges = new LinkedHashMap<>();
		allNodeProperties = new LinkedHashSet<>();

		for (Node node : nodes) {
			connectingEdges.put(node, new ArrayList<Edge>());
			allNodeProperties.addAll(node.getProperties().keySet());
		}

		for (Edge edge : edges) {
			connectingEdges.get(edge.getN1()).add(edge);
			connectingEdges.get(edge.getN2()).add(edge);
		}

		nodeSelectionListeners = new ArrayList<NodeSelectionListener>();

		updateViewer();
		setLayout(new BorderLayout());
		add(viewer, BorderLayout.CENTER);
		add(createOptionsPanel(), BorderLayout.SOUTH);
	}

	public void addNodeSelectionListener(NodeSelectionListener listener) {
		nodeSelectionListeners.add(listener);
	}

	public void removeNodeSelectionListener(NodeSelectionListener listener) {
		nodeSelectionListeners.remove(listener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == layoutButton) {
			updateViewer();
		} else if (e.getSource() == nodeSizeButton) {
			try {
				viewer.getRenderContext().setVertexShapeTransformer(
						new ShapeTransformer(Integer.parseInt(nodeSizeField
								.getText())));
				viewer.repaint();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this,
						"Node Size must be Integer Value", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == modeBox) {
			DefaultModalGraphMouse<Integer, String> mouseModel = new DefaultModalGraphMouse<Integer, String>();
			String mode = (String) modeBox.getSelectedItem();

			if (mode.equals(TRANSFORMING_MODE)) {
				mouseModel.setMode(Mode.TRANSFORMING);
			} else if (mode.equals(PICKING_MODE)) {
				mouseModel.setMode(Mode.PICKING);
			}

			viewer.setGraphMouse(mouseModel);
		} else if (e.getSource() == nodePropertiesButton) {
			List<Map<String, String>> propertyValues = new ArrayList<Map<String, String>>();

			for (Node node : viewer.getPickedVertexState().getPicked()) {
				propertyValues.add(node.getProperties());
			}

			PropertiesDialog dialog = new PropertiesDialog(this, propertyValues);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		} else if (e.getSource() == edgePropertiesButton) {
			List<Map<String, String>> propertyValues = new ArrayList<Map<String, String>>();

			for (Edge edge : viewer.getPickedEdgeState().getPicked()) {
				propertyValues.add(edge.getProperties());
			}

			PropertiesDialog dialog = new PropertiesDialog(this, propertyValues);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		} else if (e.getSource() == conditionButton) {
			String property = (String) conditionPropertyBox.getSelectedItem();
			String type = (String) conditionTypeBox.getSelectedItem();
			String value = conditionField.getText();
			Set<Node> highlightedNodes = new LinkedHashSet<>();

			for (Node node : nodes) {
				if (node.getProperties().containsKey(property)) {
					String nodeValue = node.getProperties().get(property);

					if (type.equals(EQUAL)) {
						if (nodeValue.equals(value)) {
							highlightedNodes.add(node);
						}
					} else if (type.equals(NOT_EQUAL)) {
						if (!nodeValue.equals(value)) {
							highlightedNodes.add(node);
						}
					} else if (type.equals(GREATER)) {
						try {
							if (Double.parseDouble(nodeValue) > Double
									.parseDouble(value)) {
								highlightedNodes.add(node);
							}
						} catch (NumberFormatException ex) {
						}
					} else if (type.equals(LESS)) {
						try {
							if (Double.parseDouble(nodeValue) < Double
									.parseDouble(value)) {
								highlightedNodes.add(node);
							}
						} catch (NumberFormatException ex) {
						}
					}
				}
			}

			viewer.getRenderContext().setVertexFillPaintTransformer(
					new FillTransformer(viewer, highlightedNodes));
			viewer.repaint();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() instanceof Node) {
			Node node = (Node) e.getItem();

			if (e.getStateChange() == ItemEvent.SELECTED) {
				for (Edge edge : connectingEdges.get(node)) {
					Node otherNode = null;

					if (edge.getN1() == node) {
						otherNode = edge.getN2();
					} else if (edge.getN2() == node) {
						otherNode = edge.getN1();
					}

					if (viewer.getPickedVertexState().isPicked(otherNode)) {
						viewer.getPickedEdgeState().pick(edge, true);
					}
				}
			}

			fireNodeSelectionChanged();
		}
	}

	private void updateViewer() {
		Graph<Node, Edge> graph = new SparseMultigraph<Node, Edge>();
		Dimension size = null;
		String layoutType = null;
		Layout<Node, Edge> layout = null;

		for (Node node : nodes) {
			graph.addVertex(node);
		}

		for (Edge edge : edges) {
			graph.addEdge(edge, edge.getN1(), edge.getN2());
		}

		if (viewer != null) {
			size = viewer.getSize();
		} else {
			size = new Dimension(400, 600);
		}

		if (layoutBox != null) {
			layoutType = (String) layoutBox.getSelectedItem();
		} else {
			layoutType = DEFAULT_LAYOUT;
		}

		if (layoutType.equals(CIRCLE_LAYOUT)) {
			layout = new CircleLayout<Node, Edge>(graph);
		} else if (layoutType.equals(FR_LAYOUT)) {
			layout = new FRLayout<Node, Edge>(graph);
		} else if (layoutType.equals(FR_LAYOUT_2)) {
			layout = new FRLayout2<Node, Edge>(graph);
		} else if (layoutType.equals(ISOM_LAYOUT)) {
			layout = new ISOMLayout<Node, Edge>(graph);
		} else if (layoutType.equals(KK_LAYOUT)) {
			layout = new KKLayout<Node, Edge>(graph);
		} else if (layoutType.equals(SPRING_LAYOUT)) {
			layout = new SpringLayout<Node, Edge>(graph);
		} else if (layoutType.equals(SPRING_LAYOUT_2)) {
			layout = new SpringLayout2<Node, Edge>(graph);
		}

		layout.setSize(size);

		if (viewer != null) {
			viewer.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT).setTranslate(0.0, 0.0);
			viewer.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.VIEW)
					.setScale(1.0, 1.0, new Point2D.Double(0.0, 0.0));
			viewer.setGraphLayout(layout);
		} else {
			DefaultModalGraphMouse<Integer, String> mouseModel = new DefaultModalGraphMouse<>();

			if (DEFAULT_MODE.equals(TRANSFORMING_MODE)) {
				mouseModel.setMode(Mode.TRANSFORMING);
			} else if (DEFAULT_MODE.equals(PICKING_MODE)) {
				mouseModel.setMode(Mode.PICKING);
			}

			viewer = new VisualizationViewer<Node, Edge>(layout);
			viewer.setPreferredSize(size);
			viewer.setGraphMouse(mouseModel);
			viewer.getPickedVertexState().addItemListener(this);
			viewer.getRenderContext().setVertexFillPaintTransformer(
					new FillTransformer(viewer, new LinkedHashSet<Node>()));
			viewer.getRenderContext().setVertexShapeTransformer(
					new ShapeTransformer(DEFAULT_NODESIZE));
		}
	}

	private JPanel createOptionsPanel() {
		layoutBox = new JComboBox<String>(LAYOUTS);
		layoutBox.setSelectedItem(DEFAULT_LAYOUT);
		layoutButton = new JButton("Apply");
		layoutButton.addActionListener(this);
		nodeSizeField = new JTextField("" + DEFAULT_NODESIZE);
		nodeSizeField.setPreferredSize(new Dimension(50, nodeSizeField
				.getPreferredSize().height));
		nodeSizeButton = new JButton("Apply");
		nodeSizeButton.addActionListener(this);
		modeBox = new JComboBox<String>(MODES);
		modeBox.setSelectedItem(DEFAULT_MODE);
		modeBox.addActionListener(this);
		nodePropertiesButton = new JButton("Node Properties");
		nodePropertiesButton.addActionListener(this);
		edgePropertiesButton = new JButton("Edge Properties");
		edgePropertiesButton.addActionListener(this);
		conditionPropertyBox = new JComboBox<String>(
				allNodeProperties.toArray(new String[0]));
		conditionPropertyBox.setSelectedItem(null);
		conditionTypeBox = new JComboBox<String>(CONDITIONS);
		conditionTypeBox.setSelectedItem(EQUAL);
		conditionField = new JTextField();
		conditionField.setPreferredSize(new Dimension(50, conditionField
				.getPreferredSize().height));
		conditionButton = new JButton("Apply");
		conditionButton.addActionListener(this);

		JPanel layoutPanel = new JPanel();

		layoutPanel.setBorder(BorderFactory.createTitledBorder("Layout"));
		layoutPanel.setLayout(new FlowLayout());
		layoutPanel.add(layoutBox);
		layoutPanel.add(layoutButton);

		JPanel nodeSizePanel = new JPanel();

		nodeSizePanel.setBorder(BorderFactory.createTitledBorder("Node Size"));
		nodeSizePanel.setLayout(new FlowLayout());
		nodeSizePanel.add(nodeSizeField);
		nodeSizePanel.add(nodeSizeButton);

		JPanel modePanel = new JPanel();

		modePanel.setBorder(BorderFactory.createTitledBorder("Editing Mode"));
		modePanel.setLayout(new FlowLayout());
		modePanel.add(modeBox);

		JPanel selectionPanel = new JPanel();

		selectionPanel.setBorder(BorderFactory.createTitledBorder("Selection"));
		selectionPanel.setLayout(new FlowLayout());
		selectionPanel.add(nodePropertiesButton);
		selectionPanel.add(edgePropertiesButton);

		JPanel highlightPanel = new JPanel();

		highlightPanel.setBorder(BorderFactory
				.createTitledBorder("Highlight Nodes"));
		highlightPanel.setLayout(new FlowLayout());
		highlightPanel.add(conditionPropertyBox);
		highlightPanel.add(conditionTypeBox);
		highlightPanel.add(conditionField);
		highlightPanel.add(conditionButton);

		JPanel panel1 = new JPanel();

		panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel1.add(layoutPanel);
		panel1.add(nodeSizePanel);

		JPanel panel2 = new JPanel();

		panel2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel2.add(modePanel);
		panel2.add(selectionPanel);

		JPanel panel3 = new JPanel();

		panel3.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel3.add(highlightPanel);

		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panel1);
		panel.add(panel2);
		panel.add(panel3);

		return panel;
	}

	private void fireNodeSelectionChanged() {
		for (NodeSelectionListener listener : nodeSelectionListeners) {
			listener.selectionChanged(viewer.getPickedVertexState().getPicked());
		}
	}

	public static class Node {

		private String region;
		private Map<String, String> properties;

		public Node(String region, Map<String, String> properties) {
			this.region = region;
			this.properties = properties;
		}

		public String getRegion() {
			return region;
		}

		public Map<String, String> getProperties() {
			return properties;
		}
	}

	public static class Edge {

		private Node n1;
		private Node n2;
		private Map<String, String> properties;

		public Edge(Node n1, Node n2, Map<String, String> properties) {
			this.n1 = n1;
			this.n2 = n2;
			this.properties = properties;
		}

		public Node getN1() {
			return n1;
		}

		public Node getN2() {
			return n2;
		}

		public Map<String, String> getProperties() {
			return properties;
		}
	}

	private static class ShapeTransformer implements Transformer<Node, Shape> {

		private int size;

		public ShapeTransformer(int size) {
			this.size = size;
		}

		@Override
		public Shape transform(Node n) {
			Ellipse2D circle = new Ellipse2D.Double(-size / 2, -size / 2, size,
					size);

			return circle;
		}
	}

	private static class FillTransformer implements Transformer<Node, Paint> {

		private VisualizationViewer<Node, Edge> viewer;
		private Set<Node> highlightedNodes;

		public FillTransformer(VisualizationViewer<Node, Edge> viewer,
				Set<Node> highlightedNodes) {
			this.viewer = viewer;
			this.highlightedNodes = highlightedNodes;
		}

		@Override
		public Paint transform(Node n) {
			if (viewer.getPickedVertexState().getPicked().contains(n)) {
				return Color.GREEN;
			} else if (highlightedNodes.contains(n)) {
				return Color.RED;
			} else {
				return Color.WHITE;
			}
		}

	}

	public static interface NodeSelectionListener {

		public void selectionChanged(Set<Node> selectedNodes);
	}

	private static class PropertiesDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		public PropertiesDialog(Component parent,
				List<Map<String, String>> propertyValues) {
			super(JOptionPane.getFrameForComponent(parent), "Properties", true);

			Set<String> propertySet = new LinkedHashSet<String>();

			for (Map<String, String> propertyMap : propertyValues) {
				propertySet.addAll(propertyMap.keySet());
			}

			List<String> properties = new ArrayList<String>(propertySet);
			List<List<String>> propertyValueTuples = new ArrayList<List<String>>();

			for (Map<String, String> propertyMap : propertyValues) {
				List<String> tuple = new ArrayList<String>();

				for (String property : properties) {
					tuple.add(propertyMap.get(property));
				}

				propertyValueTuples.add(tuple);
			}

			JTable table = new JTable(new PropertiesTableModel(properties,
					propertyValueTuples));

			JButton okButton = new JButton("OK");
			JPanel bottomPanel = new JPanel();

			okButton.addActionListener(this);
			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(okButton);

			setLayout(new BorderLayout());
			add(new JScrollPane(table), BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}

		private class PropertiesTableModel extends AbstractTableModel {

			private static final long serialVersionUID = 1L;

			private List<String> columnNames;
			private List<List<String>> columnValueTuples;

			public PropertiesTableModel(List<String> columnNames,
					List<List<String>> columnValueTuples) {
				this.columnNames = columnNames;
				this.columnValueTuples = columnValueTuples;
			}

			@Override
			public int getColumnCount() {
				return columnNames.size();
			}

			@Override
			public int getRowCount() {
				return columnValueTuples.size();
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return columnValueTuples.get(rowIndex).get(columnIndex);
			}

			@Override
			public String getColumnName(int column) {
				return columnNames.get(column);
			}

		}
	}

}
