package de.bund.bfr.knime.gis;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
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

	private static final String DEFAULT_LAYOUT = CIRCLE_LAYOUT;
	private static final int DEFAULT_NODESIZE = 10;
	private static final boolean DEFAULT_HIDE_NODES = true;
	private static final String DEFAULT_MODE = TRANSFORMING_MODE;

	private List<Node> nodes;
	private List<Edge> edges;
	private Map<Node, List<Edge>> connectingEdges;
	private VisualizationViewer<Node, Edge> viewer;
	private DefaultModalGraphMouse<Integer, String> mouseModel;

	private List<NodeSelectionListener> nodeSelectionListeners;

	private List<Node> selectedNodes;
	private List<Edge> selectedEdges;

	private JComboBox<String> layoutBox;
	private JTextField nodeSizeField;
	private JCheckBox hideNodeBox;
	private JButton applyButton;
	private JComboBox<String> modeBox;
	private JButton nodePropertiesButton;
	private JButton edgePropertiesButton;

	public GraphCanvas(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
		connectingEdges = new LinkedHashMap<>();

		for (Node node : nodes) {
			connectingEdges.put(node, new ArrayList<Edge>());
		}

		for (Edge edge : edges) {
			connectingEdges.get(edge.getN1()).add(edge);
			connectingEdges.get(edge.getN2()).add(edge);
		}

		nodeSelectionListeners = new ArrayList<NodeSelectionListener>();
		selectedNodes = new ArrayList<Node>();
		selectedEdges = new ArrayList<Edge>();

		mouseModel = null;
		updateMouseModel(DEFAULT_MODE);

		viewer = null;
		updateViewer(DEFAULT_LAYOUT, DEFAULT_NODESIZE, DEFAULT_HIDE_NODES);

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
		if (e.getSource() == applyButton) {
			try {
				updateViewer((String) layoutBox.getSelectedItem(),
						Integer.parseInt(nodeSizeField.getText()),
						hideNodeBox.isSelected());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this,
						"Node Size must be Integer Value", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == modeBox) {
			updateMouseModel((String) modeBox.getSelectedItem());
		} else if (e.getSource() == nodePropertiesButton) {
			List<Map<String, String>> propertyValues = new ArrayList<Map<String, String>>();

			for (Node node : selectedNodes) {
				propertyValues.add(node.getProperties());
			}

			PropertiesDialog dialog = new PropertiesDialog(this, propertyValues);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		} else if (e.getSource() == edgePropertiesButton) {
			List<Map<String, String>> propertyValues = new ArrayList<Map<String, String>>();

			for (Edge edge : selectedEdges) {
				propertyValues.add(edge.getProperties());
			}

			PropertiesDialog dialog = new PropertiesDialog(this, propertyValues);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() instanceof Node) {
			Node node = (Node) e.getItem();

			if (e.getStateChange() == ItemEvent.SELECTED) {
				selectedNodes.add(node);
				fireNodeSelectionChanged();

				for (Edge edge : connectingEdges.get(node)) {
					Node otherNode = null;

					if (edge.getN1() == node) {
						otherNode = edge.getN2();
					} else if (edge.getN2() == node) {
						otherNode = edge.getN1();
					}

					if (selectedNodes.contains(otherNode)
							&& !selectedEdges.contains(edge)) {
						viewer.getPickedEdgeState().pick(edge, true);
					}
				}
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				selectedNodes.remove(node);
				fireNodeSelectionChanged();
			}
		} else if (e.getItem() instanceof Edge) {
			Edge edge = (Edge) e.getItem();

			System.out.println(edge);

			if (e.getStateChange() == ItemEvent.SELECTED) {
				selectedEdges.add(edge);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				selectedEdges.remove(edge);
			}
		}
	}

	private void updateMouseModel(String mode) {
		if (mouseModel == null) {
			mouseModel = new DefaultModalGraphMouse<Integer, String>();
		}

		if (mode.equals(TRANSFORMING_MODE)) {
			mouseModel.setMode(Mode.TRANSFORMING);
		} else if (mode.equals(PICKING_MODE)) {
			mouseModel.setMode(Mode.PICKING);
		}
	}

	private void updateViewer(String layoutType, int nodeSize, boolean hideNodes) {
		Graph<Node, Edge> graph = new SparseMultigraph<Node, Edge>();
		Dimension size = null;
		Layout<Node, Edge> layout = null;
		Set<Node> usedNodes = new LinkedHashSet<Node>();

		for (Edge edge : edges) {
			graph.addEdge(edge, edge.getN1(), edge.getN2());
			usedNodes.add(edge.getN1());
			usedNodes.add(edge.getN2());
		}

		for (Node node : nodes) {
			if (!hideNodes || usedNodes.contains(node)) {
				graph.addVertex(node);
			}
		}

		if (viewer != null) {
			size = viewer.getSize();
		} else {
			size = new Dimension(400, 600);
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
			viewer.getRenderContext().setVertexShapeTransformer(
					new ShapeTransformer(nodeSize));
		} else {
			viewer = new VisualizationViewer<Node, Edge>(layout);
			viewer.setPreferredSize(size);
			viewer.setGraphMouse(mouseModel);
			viewer.getPickedVertexState().addItemListener(this);
			viewer.getRenderContext().setVertexShapeTransformer(
					new ShapeTransformer(nodeSize));
		}
	}

	private JPanel createOptionsPanel() {
		layoutBox = new JComboBox<String>(LAYOUTS);
		layoutBox.setSelectedItem(DEFAULT_LAYOUT);
		nodeSizeField = new JTextField("" + DEFAULT_NODESIZE);
		nodeSizeField.setPreferredSize(new Dimension(50, nodeSizeField
				.getPreferredSize().height));
		hideNodeBox = new JCheckBox("Hide Nodes without Links");
		hideNodeBox.setSelected(DEFAULT_HIDE_NODES);
		applyButton = new JButton("Apply");
		applyButton.addActionListener(this);
		modeBox = new JComboBox<String>(MODES);
		modeBox.setSelectedItem(DEFAULT_MODE);
		modeBox.addActionListener(this);
		nodePropertiesButton = new JButton("Node Properties");
		nodePropertiesButton.addActionListener(this);
		edgePropertiesButton = new JButton("Edge Properties");
		edgePropertiesButton.addActionListener(this);

		JPanel layoutPanel = new JPanel();

		layoutPanel.setBorder(BorderFactory.createTitledBorder("Layout"));
		layoutPanel.setLayout(new FlowLayout());
		layoutPanel.add(new JLabel("Type:"));
		layoutPanel.add(layoutBox);
		layoutPanel.add(new JLabel("Node Size:"));
		layoutPanel.add(nodeSizeField);
		layoutPanel.add(hideNodeBox);
		layoutPanel.add(applyButton);

		JPanel modePanel = new JPanel();

		modePanel.setBorder(BorderFactory.createTitledBorder("Editing Mode"));
		modePanel.setLayout(new FlowLayout());
		modePanel.add(modeBox);

		JPanel selectionPanel = new JPanel();

		selectionPanel.setBorder(BorderFactory.createTitledBorder("Selection"));
		selectionPanel.setLayout(new FlowLayout());
		selectionPanel.add(nodePropertiesButton);
		selectionPanel.add(edgePropertiesButton);

		JPanel upperPanel = new JPanel();

		upperPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		upperPanel.add(layoutPanel);

		JPanel lowerPanel = new JPanel();

		lowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		lowerPanel.add(modePanel);
		lowerPanel.add(selectionPanel);

		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(upperPanel);
		panel.add(lowerPanel);

		return panel;
	}

	private void fireNodeSelectionChanged() {
		for (NodeSelectionListener listener : nodeSelectionListeners) {
			listener.selectionChanged(selectedNodes);
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
		public Shape transform(Node i) {
			Ellipse2D circle = new Ellipse2D.Double(-size / 2, -size / 2, size,
					size);

			return circle;
		}
	}

	public static interface NodeSelectionListener {

		public void selectionChanged(List<Node> selectedNodes);
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
