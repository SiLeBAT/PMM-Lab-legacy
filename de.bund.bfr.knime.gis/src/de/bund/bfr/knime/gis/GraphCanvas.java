package de.bund.bfr.knime.gis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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

public class GraphCanvas extends JPanel implements ActionListener,
		ItemListener, MouseListener {

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

	private static final String DEFAULT_LAYOUT = FR_LAYOUT;
	private static final int DEFAULT_NODESIZE = 10;
	private static final String DEFAULT_MODE = PICKING_MODE;

	private List<Node> nodes;
	private List<Edge> edges;
	private Map<String, Class<?>> nodeProperties;
	private Map<String, Class<?>> edgeProperties;
	private Map<Node, List<Edge>> connectingEdges;
	private VisualizationViewer<Node, Edge> viewer;

	private JComboBox<String> layoutBox;
	private JButton layoutButton;
	private JTextField nodeSizeField;
	private JButton nodeSizeButton;
	private JComboBox<String> modeBox;

	private JPopupMenu popup;
	private JMenuItem nodePropertiesItem;
	private JMenuItem edgePropertiesItem;
	private JMenuItem highlightNodesItem;
	private JMenuItem clearHighlightItem;

	private List<NodeSelectionListener> nodeSelectionListeners;
	private HighlightCondition highlightCondition;

	public GraphCanvas(List<Node> nodes, List<Edge> edges,
			Map<String, Class<?>> nodeProperties,
			Map<String, Class<?>> edgeProperties) {
		this.nodes = nodes;
		this.edges = edges;
		this.nodeProperties = nodeProperties;
		this.edgeProperties = edgeProperties;
		connectingEdges = new LinkedHashMap<>();

		for (Node node : nodes) {
			connectingEdges.put(node, new ArrayList<Edge>());
		}

		for (Edge edge : edges) {
			connectingEdges.get(edge.getN1()).add(edge);
			connectingEdges.get(edge.getN2()).add(edge);
		}

		nodeSelectionListeners = new ArrayList<NodeSelectionListener>();
		highlightCondition = null;

		createPopupMenu();
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
		} else if (e.getSource() == nodePropertiesItem) {
			List<Map<String, Object>> propertyValues = new ArrayList<>();

			for (Node node : viewer.getPickedVertexState().getPicked()) {
				propertyValues.add(node.getProperties());
			}

			PropertiesDialog dialog = new PropertiesDialog(propertyValues,
					nodeProperties);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		} else if (e.getSource() == edgePropertiesItem) {
			List<Map<String, Object>> propertyValues = new ArrayList<>();

			for (Edge edge : viewer.getPickedEdgeState().getPicked()) {
				propertyValues.add(edge.getProperties());
			}

			PropertiesDialog dialog = new PropertiesDialog(propertyValues,
					edgeProperties);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		} else if (e.getSource() == highlightNodesItem) {
			HighlightNodesDialog dialog = new HighlightNodesDialog(this,
					nodeProperties, highlightCondition);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);

			if (dialog.isSuccessful()) {
				highlightCondition = dialog.getHighlightCondition();

				Map<Node, Double> highlightedNodes = highlightCondition
						.getValues(nodes);

				viewer.getRenderContext().setVertexFillPaintTransformer(
						new FillTransformer(viewer, highlightedNodes));
				viewer.repaint();
			}
		} else if (e.getSource() == clearHighlightItem) {
			highlightCondition = null;

			Map<Node, Double> highlightedNodes = new LinkedHashMap<>();

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

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
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
					new FillTransformer(viewer,
							new LinkedHashMap<Node, Double>()));
			viewer.getRenderContext().setVertexShapeTransformer(
					new ShapeTransformer(DEFAULT_NODESIZE));
			viewer.addMouseListener(this);
		}
	}

	private void createPopupMenu() {
		nodePropertiesItem = new JMenuItem("Show Node Properties");
		nodePropertiesItem.addActionListener(this);
		edgePropertiesItem = new JMenuItem("Show Edge Properties");
		edgePropertiesItem.addActionListener(this);
		highlightNodesItem = new JMenuItem("Highlight Nodes");
		highlightNodesItem.addActionListener(this);
		clearHighlightItem = new JMenuItem("Clear Highlights");
		clearHighlightItem.addActionListener(this);

		popup = new JPopupMenu();
		popup.add(nodePropertiesItem);
		popup.add(edgePropertiesItem);
		popup.add(highlightNodesItem);
		popup.add(clearHighlightItem);
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

		JPanel panel1 = new JPanel();

		panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel1.add(layoutPanel);
		panel1.add(nodeSizePanel);
		panel1.add(modePanel);

		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panel1);

		return panel;
	}

	private void fireNodeSelectionChanged() {
		for (NodeSelectionListener listener : nodeSelectionListeners) {
			listener.selectionChanged(viewer.getPickedVertexState().getPicked());
		}
	}

	public static class Node {

		private String region;
		private Map<String, Object> properties;

		public Node(String region, Map<String, Object> properties) {
			this.region = region;
			this.properties = properties;
		}

		public String getRegion() {
			return region;
		}

		public Map<String, Object> getProperties() {
			return properties;
		}
	}

	public static class Edge {

		private Node n1;
		private Node n2;
		private Map<String, Object> properties;

		public Edge(Node n1, Node n2, Map<String, Object> properties) {
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

		public Map<String, Object> getProperties() {
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
		private Map<Node, Double> highlightedNodes;
		private double maxValue;

		public FillTransformer(VisualizationViewer<Node, Edge> viewer,
				Map<Node, Double> highlightedNodes) {
			this.viewer = viewer;
			this.highlightedNodes = highlightedNodes;

			if (!highlightedNodes.isEmpty()) {
				maxValue = Collections.max(highlightedNodes.values());
			} else {
				maxValue = Double.NaN;
			}
		}

		@Override
		public Paint transform(Node n) {
			if (viewer.getPickedVertexState().getPicked().contains(n)) {
				return Color.GREEN;
			} else if (highlightedNodes.containsKey(n)) {
				float alpha = (float) (highlightedNodes.get(n) / maxValue);

				return new Color(1.0f, 1.0f - alpha, 1.0f - alpha);
			} else {
				return Color.WHITE;
			}
		}

	}

	public static interface NodeSelectionListener {

		public void selectionChanged(Set<Node> selectedNodes);
	}

	private static class PropertiesDialog extends JFrame implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		public PropertiesDialog(List<Map<String, Object>> propertyValues,
				Map<String, Class<?>> properties) {
			super("Properties");
			List<String> columnNames = new ArrayList<>();
			List<Class<?>> columnTypes = new ArrayList<>();
			List<List<Object>> columnValueTuples = new ArrayList<>();

			for (Map.Entry<String, Class<?>> entry : properties.entrySet()) {
				columnNames.add(entry.getKey());
				columnTypes.add(entry.getValue());
			}

			for (Map<String, Object> propertyMap : propertyValues) {
				List<Object> tuple = new ArrayList<>();

				for (String property : columnNames) {
					tuple.add(propertyMap.get(property));
				}

				columnValueTuples.add(tuple);
			}

			JTable table = new JTable(new PropertiesTableModel(columnNames,
					columnTypes, columnValueTuples));

			table.setAutoCreateRowSorter(true);

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
			private List<Class<?>> columnTypes;
			private List<List<Object>> columnValueTuples;

			public PropertiesTableModel(List<String> columnNames,
					List<Class<?>> columnTypes,
					List<List<Object>> columnValueTuples) {
				this.columnNames = columnNames;
				this.columnTypes = columnTypes;
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

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes.get(columnIndex);
			}
		}
	}

	private static class HighlightNodesDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton okButton;
		private JButton cancelButton;

		private JComboBox<String> valuePropertyBox;
		private JComboBox<String> valueTypeBox;

		private HighlightCondition highlightCondition;
		private boolean successful;

		public HighlightNodesDialog(JComponent parent,
				Map<String, Class<?>> nodeProperties,
				HighlightCondition initialHighlightCondition) {
			super(JOptionPane.getFrameForComponent(parent),
					"Highlight Condition", true);
			highlightCondition = null;
			successful = false;

			List<String> numberProperties = new ArrayList<>();

			for (String property : nodeProperties.keySet()) {
				Class<?> type = nodeProperties.get(property);

				if (type == Integer.class || type == Double.class) {
					numberProperties.add(property);
				}
			}

			valuePropertyBox = new JComboBox<>(
					numberProperties.toArray(new String[0]));
			valueTypeBox = new JComboBox<>(new String[] {
					ValueHighlightCondition.VALUE_TYPE,
					ValueHighlightCondition.LOG_VALUE_TYPE });

			if (initialHighlightCondition != null) {
				if (initialHighlightCondition instanceof ValueHighlightCondition) {
					ValueHighlightCondition condition = (ValueHighlightCondition) initialHighlightCondition;

					valuePropertyBox.setSelectedItem(condition.getProperty());
					valueTypeBox.setSelectedItem(condition.getType());
				}
			}

			JPanel valuePanel = new JPanel();

			valuePanel.setLayout(new FlowLayout());
			valuePanel.add(new JLabel("Property:"));
			valuePanel.add(valuePropertyBox);
			valuePanel.add(new JLabel("Type:"));
			valuePanel.add(valueTypeBox);

			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel bottomPanel = new JPanel();

			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(valuePanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				highlightCondition = new ValueHighlightCondition(
						(String) valuePropertyBox.getSelectedItem(),
						(String) valueTypeBox.getSelectedItem());
				successful = true;
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			}
		}

		public HighlightCondition getHighlightCondition() {
			return highlightCondition;
		}

		public boolean isSuccessful() {
			return successful;
		}
	}

	private static interface HighlightCondition {

		public Map<Node, Double> getValues(List<Node> nodes);
	}

	public static class ValueHighlightCondition implements HighlightCondition {

		public static final String VALUE_TYPE = "Value";
		public static final String LOG_VALUE_TYPE = "Log Value";

		private String property;
		private String type;

		public ValueHighlightCondition(String property, String type) {
			this.property = property;
			this.type = type;
		}

		@Override
		public Map<Node, Double> getValues(List<Node> nodes) {
			Map<Node, Double> values = new LinkedHashMap<>();

			for (Node node : nodes) {
				Object value = node.getProperties().get(property);

				if (value instanceof Number) {
					double doubleValue = ((Number) value).doubleValue();

					if (!Double.isNaN(doubleValue)
							&& !Double.isInfinite(doubleValue)
							&& doubleValue >= 0.0) {
						values.put(node, doubleValue);
					} else {
						values.put(node, 0.0);
					}
				} else {
					values.put(node, 0.0);
				}
			}

			double max = Collections.max(values.values());

			if (max != 0.0) {
				for (Node node : nodes) {
					values.put(node, values.get(node) / max);
				}
			}

			if (type.equals(LOG_VALUE_TYPE)) {
				for (Node node : nodes) {
					values.put(node, Math.log10(values.get(node) * 9.0 + 1.0));
				}
			}

			return values;
		}

		protected String getProperty() {
			return property;
		}

		protected String getType() {
			return type;
		}
	}

}
