package de.bund.bfr.knime.gis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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

import de.bund.bfr.knime.gis.GraphCanvas.Node;
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
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;

public class GraphCanvas extends JPanel implements ActionListener,
		ItemListener, MouseListener, GraphMouseListener<Node> {

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
	private JMenuItem clearHighlightNodesItem;
	private JMenuItem highlightEdgesItem;
	private JMenuItem clearHighlightEdgesItem;

	private List<NodeSelectionListener> nodeSelectionListeners;
	private HighlightCondition nodesHighlightCondition;
	private HighlightCondition edgesHighlightCondition;

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
		nodesHighlightCondition = null;
		edgesHighlightCondition = null;

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

	public List<Node> getNodes() {
		return nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setSelectedNodes(Set<Node> selectedNodes) {
		for (Node node : nodes) {
			if (selectedNodes.contains(node)) {
				viewer.getPickedVertexState().pick(node, true);
			} else {
				viewer.getPickedVertexState().pick(node, false);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == layoutButton) {
			updateViewer();
		} else if (e.getSource() == nodeSizeButton) {
			try {
				viewer.getRenderContext().setVertexShapeTransformer(
						new NodeShapeTransformer(Integer.parseInt(nodeSizeField
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
					nodeProperties, nodesHighlightCondition);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);

			if (dialog.isSuccessful()) {
				nodesHighlightCondition = dialog.getHighlightCondition();

				Map<GraphElement, Double> highlightedElements = nodesHighlightCondition
						.getValues(new ArrayList<GraphElement>(nodes));
				Map<Node, Double> highlightedNodes = new LinkedHashMap<>();

				for (GraphElement element : highlightedElements.keySet()) {
					highlightedNodes.put((Node) element,
							highlightedElements.get(element));
				}

				viewer.getRenderContext().setVertexFillPaintTransformer(
						new NodeFillTransformer(viewer, highlightedNodes));
				viewer.repaint();
			}
		} else if (e.getSource() == clearHighlightNodesItem) {
			nodesHighlightCondition = null;

			Map<Node, Double> highlightedNodes = new LinkedHashMap<>();

			viewer.getRenderContext().setVertexFillPaintTransformer(
					new NodeFillTransformer(viewer, highlightedNodes));
			viewer.repaint();
		} else if (e.getSource() == highlightEdgesItem) {
			HighlightNodesDialog dialog = new HighlightNodesDialog(this,
					edgeProperties, edgesHighlightCondition);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);

			if (dialog.isSuccessful()) {
				edgesHighlightCondition = dialog.getHighlightCondition();

				Map<GraphElement, Double> highlightedElements = edgesHighlightCondition
						.getValues(new ArrayList<GraphElement>(edges));
				Map<Edge, Double> highlightedEdges = new LinkedHashMap<>();

				for (GraphElement element : highlightedElements.keySet()) {
					highlightedEdges.put((Edge) element,
							highlightedElements.get(element));
				}

				viewer.getRenderContext().setEdgeDrawPaintTransformer(
						new EdgeDrawTransformer(viewer, highlightedEdges));
				viewer.repaint();
			}
		} else if (e.getSource() == clearHighlightEdgesItem) {
			edgesHighlightCondition = null;

			Map<Edge, Double> highlightedEdges = new LinkedHashMap<>();

			viewer.getRenderContext().setEdgeDrawPaintTransformer(
					new EdgeDrawTransformer(viewer, highlightedEdges));
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

	@Override
	public void graphClicked(Node v, MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON2) {
			SingleNodePropertiesDialog dialog = new SingleNodePropertiesDialog(
					me.getComponent(), v);

			dialog.setLocation(me.getLocationOnScreen());
			dialog.setVisible(true);
		}
	}

	@Override
	public void graphPressed(Node v, MouseEvent me) {
	}

	@Override
	public void graphReleased(Node v, MouseEvent me) {
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
			viewer.addGraphMouseListener(this);
			viewer.setPreferredSize(size);
			viewer.setGraphMouse(mouseModel);
			viewer.getPickedVertexState().addItemListener(this);
			viewer.getRenderContext().setVertexFillPaintTransformer(
					new NodeFillTransformer(viewer,
							new LinkedHashMap<Node, Double>()));
			viewer.getRenderContext().setEdgeDrawPaintTransformer(
					new EdgeDrawTransformer(viewer,
							new LinkedHashMap<Edge, Double>()));
			viewer.getRenderContext().setVertexShapeTransformer(
					new NodeShapeTransformer(DEFAULT_NODESIZE));
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
		clearHighlightNodesItem = new JMenuItem("Clear Node Highlights");
		clearHighlightNodesItem.addActionListener(this);
		highlightEdgesItem = new JMenuItem("Highlight Edges");
		highlightEdgesItem.addActionListener(this);
		clearHighlightEdgesItem = new JMenuItem("Clear Edge Highlights");
		clearHighlightEdgesItem.addActionListener(this);

		popup = new JPopupMenu();
		popup.add(nodePropertiesItem);
		popup.add(edgePropertiesItem);
		popup.add(highlightNodesItem);
		popup.add(clearHighlightNodesItem);
		popup.add(highlightEdgesItem);
		popup.add(clearHighlightEdgesItem);
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
			listener.graphSelectionChanged(viewer.getPickedVertexState()
					.getPicked());
		}
	}

	public static interface GraphElement {

		public Map<String, Object> getProperties();
	}

	public static class Node implements GraphElement {

		private String id;
		private String region;
		private Map<String, Object> properties;

		public Node(String id, String region, Map<String, Object> properties) {
			this.id = id;
			this.region = region;
			this.properties = properties;
		}

		public String getRegion() {
			return region;
		}

		@Override
		public Map<String, Object> getProperties() {
			return properties;
		}

		@Override
		public int hashCode() {
			return id.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Node) {
				return id.equals(((Node) obj).id);
			}

			return false;
		}
	}

	public static class Edge implements GraphElement {

		private String id;
		private Node n1;
		private Node n2;
		private Map<String, Object> properties;

		public Edge(String id, Node n1, Node n2, Map<String, Object> properties) {
			this.id = id;
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

		@Override
		public Map<String, Object> getProperties() {
			return properties;
		}

		@Override
		public int hashCode() {
			return id.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Edge) {
				return id.equals(((Edge) obj).id);
			}

			return false;
		}
	}

	public static interface NodeSelectionListener {

		public void graphSelectionChanged(Set<Node> selectedNodes);
	}

	private static class NodeShapeTransformer implements
			Transformer<Node, Shape> {

		private int size;

		public NodeShapeTransformer(int size) {
			this.size = size;
		}

		@Override
		public Shape transform(Node n) {
			Ellipse2D circle = new Ellipse2D.Double(-size / 2, -size / 2, size,
					size);

			return circle;
		}
	}

	private static class NodeFillTransformer implements
			Transformer<Node, Paint> {

		private VisualizationViewer<Node, Edge> viewer;
		private Map<Node, Double> highlightedNodes;

		public NodeFillTransformer(VisualizationViewer<Node, Edge> viewer,
				Map<Node, Double> highlightedNodes) {
			this.viewer = viewer;
			this.highlightedNodes = highlightedNodes;
		}

		@Override
		public Paint transform(Node n) {
			if (viewer.getPickedVertexState().getPicked().contains(n)) {
				return Color.GREEN;
			} else if (highlightedNodes.containsKey(n)) {
				float alpha = highlightedNodes.get(n).floatValue();

				return new Color(1.0f, 1.0f - alpha, 1.0f - alpha);
			} else {
				return Color.WHITE;
			}
		}

	}

	private static class EdgeDrawTransformer implements
			Transformer<Edge, Paint> {

		private VisualizationViewer<Node, Edge> viewer;
		private Map<Edge, Double> highlightedEdges;

		public EdgeDrawTransformer(VisualizationViewer<Node, Edge> viewer,
				Map<Edge, Double> highlightedEdges) {
			this.viewer = viewer;
			this.highlightedEdges = highlightedEdges;
		}

		@Override
		public Paint transform(Edge edge) {
			if (viewer.getPickedEdgeState().getPicked().contains(edge)) {
				return Color.GREEN;
			} else if (highlightedEdges.containsKey(edge)) {
				float alpha = highlightedEdges.get(edge).floatValue();

				return new Color(alpha, 0.0f, 0.0f);
			} else {
				return Color.BLACK;
			}
		}

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

		private static final String LOGICAL_CONDITION = "Logical Condition";
		private static final String VALUE_CONDITION = "Value Condition";

		private String conditionType;

		private JComboBox<String> conditionTypeBox;
		private JPanel conditionPanel;
		private JButton okButton;
		private JButton cancelButton;

		private JComboBox<String> valuePropertyBox;
		private JComboBox<String> valueTypeBox;

		private List<JComboBox<String>> logicalAndOrBoxes;
		private List<JComboBox<String>> logicalPropertyBoxes;
		private List<JComboBox<String>> logicalTypeBoxes;
		private List<JTextField> logicalValueFields;
		private List<JButton> logicalAddButtons;
		private List<JButton> logicalRemoveButtons;

		private Map<String, Class<?>> nodeProperties;

		private HighlightCondition highlightCondition;
		private boolean successful;

		public HighlightNodesDialog(JComponent parent,
				Map<String, Class<?>> nodeProperties,
				HighlightCondition initialHighlightCondition) {
			super(JOptionPane.getFrameForComponent(parent),
					"Highlight Condition", true);
			this.nodeProperties = nodeProperties;
			highlightCondition = null;
			successful = false;

			conditionTypeBox = new JComboBox<>(new String[] {
					LOGICAL_CONDITION, VALUE_CONDITION });

			JPanel conditionTypePanel = new JPanel();

			conditionTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			conditionTypePanel.add(conditionTypeBox);

			if (initialHighlightCondition instanceof AndOrHighlightCondition) {
				conditionTypeBox.setSelectedItem(LOGICAL_CONDITION);
				conditionType = LOGICAL_CONDITION;
				conditionPanel = createLogicalPanel(initialHighlightCondition);
			} else if (initialHighlightCondition instanceof ValueHighlightCondition) {
				conditionTypeBox.setSelectedItem(VALUE_CONDITION);
				conditionType = VALUE_CONDITION;
				conditionPanel = createValuePanel(initialHighlightCondition);
			} else {
				conditionTypeBox.setSelectedItem(LOGICAL_CONDITION);
				conditionType = LOGICAL_CONDITION;
				conditionPanel = createLogicalPanel(null);
			}

			conditionTypeBox.addActionListener(this);

			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel bottomPanel = new JPanel();

			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(conditionTypePanel, BorderLayout.NORTH);
			add(conditionPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				highlightCondition = createCondition();
				successful = true;
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			} else if (e.getSource() == conditionTypeBox) {
				if (!conditionType.equals(conditionTypeBox.getSelectedItem())) {
					if (conditionTypeBox.getSelectedItem().equals(
							LOGICAL_CONDITION)) {
						conditionType = LOGICAL_CONDITION;
						remove(conditionPanel);
						conditionPanel = createLogicalPanel(null);
						add(conditionPanel, BorderLayout.CENTER);
						pack();
					} else if (conditionTypeBox.getSelectedItem().equals(
							VALUE_CONDITION)) {
						conditionType = VALUE_CONDITION;
						remove(conditionPanel);
						conditionPanel = createValuePanel(null);
						add(conditionPanel, BorderLayout.CENTER);
						pack();
					}
				}
			} else if (logicalAddButtons.contains(e.getSource())) {
				int index = logicalAddButtons.indexOf(e.getSource());
				List<List<LogicalHighlightCondition>> conditions = ((AndOrHighlightCondition) createCondition())
						.getConditions();
				LogicalHighlightCondition newCond = new LogicalHighlightCondition(
						nodeProperties.keySet().toArray(new String[0])[0],
						LogicalHighlightCondition.EQUAL_TYPE, "");
				boolean added = false;
				int count = 0;

				for (int i = 0; i < conditions.size(); i++) {
					for (int j = 0; j < conditions.get(i).size(); j++) {
						if (index == count) {
							conditions.get(i).add(j, newCond);
							added = true;
							break;
						}

						count++;
					}

					if (added) {
						break;
					}
				}

				if (!added) {
					conditions.get(conditions.size() - 1).add(newCond);
				}

				remove(conditionPanel);
				conditionPanel = createLogicalPanel(new AndOrHighlightCondition(
						conditions));
				add(conditionPanel, BorderLayout.CENTER);
				pack();
			} else if (logicalRemoveButtons.contains(e.getSource())) {
				int index = logicalRemoveButtons.indexOf(e.getSource());
				List<List<LogicalHighlightCondition>> conditions = ((AndOrHighlightCondition) createCondition())
						.getConditions();
				boolean removed = false;
				int count = 0;

				for (int i = 0; i < conditions.size(); i++) {
					for (int j = 0; j < conditions.get(i).size(); j++) {
						if (index == count) {
							conditions.get(i).remove(j);

							if (conditions.get(i).isEmpty()) {
								conditions.remove(i);
							}

							removed = true;
							break;
						}

						count++;
					}

					if (removed) {
						break;
					}
				}

				remove(conditionPanel);
				conditionPanel = createLogicalPanel(new AndOrHighlightCondition(
						conditions));
				add(conditionPanel, BorderLayout.CENTER);
				pack();
			}
		}

		public HighlightCondition getHighlightCondition() {
			return highlightCondition;
		}

		public boolean isSuccessful() {
			return successful;
		}

		private JPanel createLogicalPanel(HighlightCondition highlightCondition) {
			logicalAndOrBoxes = new ArrayList<>();
			logicalPropertyBoxes = new ArrayList<>();
			logicalTypeBoxes = new ArrayList<>();
			logicalValueFields = new ArrayList<>();
			logicalAddButtons = new ArrayList<>();
			logicalRemoveButtons = new ArrayList<>();

			JPanel logicalPanel = new JPanel();
			int row = 0;

			logicalPanel.setLayout(new GridBagLayout());

			if (highlightCondition != null) {
				AndOrHighlightCondition condition = (AndOrHighlightCondition) highlightCondition;

				for (int i = 0; i < condition.getConditions().size(); i++) {
					for (int j = 0; j < condition.getConditions().get(i).size(); j++) {
						LogicalHighlightCondition cond = condition
								.getConditions().get(i).get(j);

						JComboBox<String> propertyBox = new JComboBox<>(
								nodeProperties.keySet().toArray(new String[0]));
						JComboBox<String> typeBox = new JComboBox<>(
								LogicalHighlightCondition.TYPES);
						JTextField valueField = new JTextField();
						JButton addButton = new JButton("Add");
						JButton removeButton = new JButton("Remove");

						propertyBox.setSelectedItem(cond.getProperty());
						typeBox.setSelectedItem(cond.getType());
						valueField.setText(cond.getValue() + "");

						valueField.setPreferredSize(new Dimension(100,
								valueField.getPreferredSize().height));
						addButton.addActionListener(this);
						removeButton.addActionListener(this);

						if (row != 0) {
							JComboBox<String> andOrBox = new JComboBox<>(
									AndOrHighlightCondition.TYPES);

							if (j == 0) {
								andOrBox.setSelectedItem(AndOrHighlightCondition.OR_TYPE);
							} else {
								andOrBox.setSelectedItem(AndOrHighlightCondition.AND_TYPE);
							}

							logicalAndOrBoxes.add(andOrBox);
							logicalPanel.add(andOrBox,
									createConstraints(0, row));
						}

						logicalPropertyBoxes.add(propertyBox);
						logicalTypeBoxes.add(typeBox);
						logicalValueFields.add(valueField);
						logicalAddButtons.add(addButton);
						logicalRemoveButtons.add(removeButton);

						logicalPanel
								.add(propertyBox, createConstraints(1, row));
						logicalPanel.add(typeBox, createConstraints(2, row));
						logicalPanel.add(valueField, createConstraints(3, row));
						logicalPanel.add(addButton, createConstraints(4, row));
						logicalPanel.add(removeButton,
								createConstraints(5, row));

						row++;
					}
				}

				JButton addButton = new JButton("Add");

				addButton.addActionListener(this);
				logicalAddButtons.add(addButton);
				logicalPanel.add(addButton, createConstraints(4, row));
			} else {
				JComboBox<String> propertyBox = new JComboBox<>(nodeProperties
						.keySet().toArray(new String[0]));
				JComboBox<String> typeBox = new JComboBox<>(
						LogicalHighlightCondition.TYPES);
				JTextField valueField = new JTextField();
				JButton addButton = new JButton("Add");
				JButton removeButton = new JButton("Remove");

				valueField.setPreferredSize(new Dimension(50, valueField
						.getPreferredSize().height));
				addButton.addActionListener(this);
				removeButton.addActionListener(this);

				logicalPropertyBoxes.add(propertyBox);
				logicalTypeBoxes.add(typeBox);
				logicalValueFields.add(valueField);
				logicalAddButtons.add(addButton);
				logicalRemoveButtons.add(removeButton);

				logicalPanel.add(propertyBox, createConstraints(1, 0));
				logicalPanel.add(typeBox, createConstraints(2, 0));
				logicalPanel.add(valueField, createConstraints(3, 0));
				logicalPanel.add(addButton, createConstraints(4, 0));
				logicalPanel.add(removeButton, createConstraints(5, 0));

				JButton addButton2 = new JButton("Add");

				addButton2.addActionListener(this);
				logicalAddButtons.add(addButton2);
				logicalPanel.add(addButton2, createConstraints(4, 1));
			}

			return logicalPanel;
		}

		private JPanel createValuePanel(HighlightCondition highlightCondition) {
			List<String> numberProperties = new ArrayList<>();

			for (String property : nodeProperties.keySet()) {
				Class<?> type = nodeProperties.get(property);

				if (type == Integer.class || type == Double.class) {
					numberProperties.add(property);
				}
			}

			valuePropertyBox = new JComboBox<>(
					numberProperties.toArray(new String[0]));
			valueTypeBox = new JComboBox<>(ValueHighlightCondition.TYPES);

			if (highlightCondition != null) {
				ValueHighlightCondition condition = (ValueHighlightCondition) highlightCondition;

				valuePropertyBox.setSelectedItem(condition.getProperty());
				valueTypeBox.setSelectedItem(condition.getType());
			}

			JPanel valuePanel = new JPanel();

			valuePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			valuePanel.add(new JLabel("Property:"));
			valuePanel.add(valuePropertyBox);
			valuePanel.add(new JLabel("Type:"));
			valuePanel.add(valueTypeBox);

			return valuePanel;
		}

		private GridBagConstraints createConstraints(int x, int y) {
			return new GridBagConstraints(x, y, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(2, 2, 2, 2), 0, 0);
		}

		private HighlightCondition createCondition() {
			if (conditionType.equals(VALUE_CONDITION)) {
				return new ValueHighlightCondition(
						(String) valuePropertyBox.getSelectedItem(),
						(String) valueTypeBox.getSelectedItem());
			} else if (conditionType.equals(LOGICAL_CONDITION)) {
				List<List<LogicalHighlightCondition>> conditions = new ArrayList<>();
				List<LogicalHighlightCondition> andList = new ArrayList<>();

				for (int i = 0; i < logicalPropertyBoxes.size(); i++) {
					if (i != 0) {
						String operation = (String) logicalAndOrBoxes
								.get(i - 1).getSelectedItem();

						if (operation.equals(AndOrHighlightCondition.OR_TYPE)) {
							conditions.add(andList);
							andList = new ArrayList<>();
						}
					}

					String property = (String) logicalPropertyBoxes.get(i)
							.getSelectedItem();
					String type = (String) logicalTypeBoxes.get(i)
							.getSelectedItem();
					String value = logicalValueFields.get(i).getText();

					andList.add(new LogicalHighlightCondition(property, type,
							value));
				}

				conditions.add(andList);

				return new AndOrHighlightCondition(conditions);
			}

			return null;
		}
	}

	private static interface HighlightCondition {

		public Map<GraphElement, Double> getValues(List<GraphElement> elements);
	}

	private static class ValueHighlightCondition implements HighlightCondition {

		public static final String VALUE_TYPE = "Value";
		public static final String LOG_VALUE_TYPE = "Log Value";
		public static final String[] TYPES = { VALUE_TYPE, LOG_VALUE_TYPE };

		private String property;
		private String type;

		public ValueHighlightCondition(String property, String type) {
			this.property = property;
			this.type = type;
		}

		@Override
		public Map<GraphElement, Double> getValues(List<GraphElement> elements) {
			Map<GraphElement, Double> values = new LinkedHashMap<>();

			for (GraphElement element : elements) {
				Object value = element.getProperties().get(property);

				if (value instanceof Number) {
					double doubleValue = ((Number) value).doubleValue();

					if (!Double.isNaN(doubleValue)
							&& !Double.isInfinite(doubleValue)
							&& doubleValue >= 0.0) {
						values.put(element, doubleValue);
					} else {
						values.put(element, 0.0);
					}
				} else {
					values.put(element, 0.0);
				}
			}

			double max = Collections.max(values.values());

			if (max != 0.0) {
				for (GraphElement element : elements) {
					values.put(element, values.get(element) / max);
				}
			}

			if (type.equals(LOG_VALUE_TYPE)) {
				for (GraphElement element : elements) {
					values.put(element,
							Math.log10(values.get(element) * 9.0 + 1.0));
				}
			}

			return values;
		}

		public String getProperty() {
			return property;
		}

		public String getType() {
			return type;
		}
	}

	private static class AndOrHighlightCondition implements HighlightCondition {

		public static final String AND_TYPE = "And";
		public static final String OR_TYPE = "Or";
		public static final String[] TYPES = { AND_TYPE, OR_TYPE };

		private List<List<LogicalHighlightCondition>> conditions;

		public AndOrHighlightCondition(
				List<List<LogicalHighlightCondition>> conditions) {
			this.conditions = conditions;
		}

		@Override
		public Map<GraphElement, Double> getValues(List<GraphElement> elements) {
			List<List<Map<GraphElement, Double>>> valuesList = new ArrayList<>();

			for (List<LogicalHighlightCondition> andLists : conditions) {
				List<Map<GraphElement, Double>> v = new ArrayList<>();

				for (LogicalHighlightCondition condition : andLists) {
					v.add(condition.getValues(elements));
				}

				valuesList.add(v);
			}

			Map<GraphElement, Double> returnValues = new LinkedHashMap<>();

			for (GraphElement element : elements) {
				boolean allFalse = true;

				for (List<Map<GraphElement, Double>> andValues : valuesList) {
					boolean allTrue = true;

					for (Map<GraphElement, Double> values : andValues) {
						if (values.get(element) != 1.0) {
							allTrue = false;
							break;
						}
					}

					if (allTrue) {
						allFalse = false;
						break;
					}
				}

				if (allFalse) {
					returnValues.put(element, 0.0);
				} else {
					returnValues.put(element, 1.0);
				}
			}

			return returnValues;
		}

		public List<List<LogicalHighlightCondition>> getConditions() {
			return conditions;
		}

	}

	private static class LogicalHighlightCondition implements
			HighlightCondition {

		public static final String EQUAL_TYPE = "==";
		public static final String NOT_EQUAL_TYPE = "!=";
		public static final String GREATER_TYPE = ">";
		public static final String LESS_TYPE = "<";
		public static final String[] TYPES = { EQUAL_TYPE, NOT_EQUAL_TYPE,
				GREATER_TYPE, LESS_TYPE };

		private String property;
		private String type;
		private String value;
		private Double doubleValue;

		public LogicalHighlightCondition(String property, String type,
				String value) {
			this.property = property;
			this.type = type;
			this.value = value;

			try {
				doubleValue = Double.parseDouble(value);
			} catch (Exception e) {
				doubleValue = null;
			}
		}

		@Override
		public Map<GraphElement, Double> getValues(List<GraphElement> elements) {
			Map<GraphElement, Double> values = new LinkedHashMap<>();

			for (GraphElement element : elements) {
				Object nodeValue = element.getProperties().get(property);

				if (type.equals(EQUAL_TYPE)) {
					if (nodeValue != null && nodeValue instanceof Boolean) {
						if (value != null
								&& value.equalsIgnoreCase((boolean) nodeValue ? "true"
										: "false")) {
							values.put(element, 1.0);
						} else {
							values.put(element, 0.0);
						}
					} else {
						if (value != null && value.equals(nodeValue)) {
							values.put(element, 1.0);
						} else {
							values.put(element, 0.0);
						}
					}
				} else if (type.equals(NOT_EQUAL_TYPE)) {
					if (nodeValue != null && nodeValue instanceof Boolean) {
						if (value == null
								|| value.equalsIgnoreCase((boolean) nodeValue ? "false"
										: "true")) {
							values.put(element, 1.0);
						} else {
							values.put(element, 0.0);
						}
					} else {
						if (value != null && !value.equals(nodeValue)) {
							values.put(element, 1.0);
						} else {
							values.put(element, 0.0);
						}
					}
				} else if (type.equals(GREATER_TYPE)) {
					if (doubleValue != null && nodeValue instanceof Number) {
						if (((Number) nodeValue).doubleValue() > doubleValue) {
							values.put(element, 1.0);
						} else {
							values.put(element, 0.0);
						}
					} else {
						values.put(element, 0.0);
					}
				} else if (type.equals(LESS_TYPE)) {
					if (doubleValue != null && nodeValue instanceof Number) {
						if (((Number) nodeValue).doubleValue() < doubleValue) {
							values.put(element, 1.0);
						} else {
							values.put(element, 0.0);
						}
					} else {
						values.put(element, 0.0);
					}
				}
			}

			return values;
		}

		public String getProperty() {
			return property;
		}

		public String getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

	}

	private static class SingleNodePropertiesDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		public SingleNodePropertiesDialog(Component parent, Node node) {
			super(JOptionPane.getFrameForComponent(parent), "Properties", true);

			JPanel centerPanel = new JPanel();
			JPanel leftCenterPanel = new JPanel();
			JPanel rightCenterPanel = new JPanel();

			leftCenterPanel.setLayout(new GridLayout(node.getProperties()
					.size(), 1, 5, 5));
			rightCenterPanel.setLayout(new GridLayout(node.getProperties()
					.size(), 1, 5, 5));

			for (Map.Entry<String, Object> property : node.getProperties()
					.entrySet()) {
				JTextField field = new JTextField();

				if (property.getValue() != null) {
					field.setText(property.getValue().toString());
				}

				field.setEditable(false);
				leftCenterPanel.add(new JLabel(property.getKey() + ":"));
				rightCenterPanel.add(field);
			}

			centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			centerPanel.setLayout(new BorderLayout(5, 5));
			centerPanel.add(leftCenterPanel, BorderLayout.WEST);
			centerPanel.add(rightCenterPanel, BorderLayout.CENTER);

			JButton okButton = new JButton("OK");
			JPanel bottomPanel = new JPanel();

			okButton.addActionListener(this);
			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(okButton);

			setLayout(new BorderLayout());
			add(centerPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setResizable(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

}
