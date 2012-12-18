package de.bund.bfr.knime.gis;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.collections15.Transformer;

import de.bund.bfr.knime.gis.GraphCanvas.Node;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;

public class GraphCanvas extends JPanel implements ActionListener,
		GraphMouseListener<Node>, ItemListener {

	private static final long serialVersionUID = 1L;

	private static final String CIRCLE_LAYOUT = "Circle Layout";
	private static final String FR_LAYOUT = "FR Layout";
	private static final String FR_LAYOUT_2 = "FR Layout 2";
	private static final String ISOM_LAYOUT = "ISOM Layout";
	private static final String KK_LAYOUT = "KK Layout";
	private static final String[] LAYOUTS = { CIRCLE_LAYOUT, FR_LAYOUT,
			FR_LAYOUT_2, ISOM_LAYOUT, KK_LAYOUT };

	private static final String TRANSFORMING_MODE = "Transforming";
	private static final String PICKING_MODE = "Picking";
	private static final String[] MODES = { TRANSFORMING_MODE, PICKING_MODE };

	private static final String DEFAULT_LAYOUT = CIRCLE_LAYOUT;
	private static final int DEFAULT_NODESIZE = 10;
	private static final boolean DEFAULT_HIDE_NODES = true;
	private static final String DEFAULT_MODE = TRANSFORMING_MODE;

	private List<Node> nodes;
	private List<Edge> edges;
	private VisualizationViewer<Node, Edge> viewer;
	private DefaultModalGraphMouse<Integer, String> mouseModel;

	private List<NodeSelectionListener> nodeSelectionListeners;

	private List<Node> selectedNodes;

	private JComboBox<String> layoutBox;
	private JTextField nodeSizeField;
	private JCheckBox hideNodeBox;
	private JButton applyButton;
	private JComboBox<String> modeBox;

	public GraphCanvas(List<Node> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
		nodeSelectionListeners = new ArrayList<NodeSelectionListener>();
		selectedNodes = new ArrayList<Node>();
		mouseModel = new DefaultModalGraphMouse<Integer, String>();

		if (DEFAULT_MODE.equals(TRANSFORMING_MODE)) {
			mouseModel.setMode(Mode.TRANSFORMING);
		} else if (DEFAULT_MODE.equals(PICKING_MODE)) {
			mouseModel.setMode(Mode.PICKING);
		}

		viewer = createViewer(DEFAULT_LAYOUT, DEFAULT_NODESIZE,
				DEFAULT_HIDE_NODES);
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
				remove(viewer);
				viewer = createViewer((String) layoutBox.getSelectedItem(),
						Integer.parseInt(nodeSizeField.getText()),
						hideNodeBox.isSelected());
				add(viewer, BorderLayout.CENTER);
				revalidate();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this,
						"Node Size must be Integer Value", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == modeBox) {
			if (modeBox.getSelectedItem().equals(TRANSFORMING_MODE)) {
				mouseModel.setMode(Mode.TRANSFORMING);
			} else if (modeBox.getSelectedItem().equals(PICKING_MODE)) {
				mouseModel.setMode(Mode.PICKING);
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() instanceof Node) {
			Node node = (Node) e.getItem();

			if (e.getStateChange() == ItemEvent.SELECTED) {
				selectedNodes.add(node);
				fireNodeSelectionChanged();
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				selectedNodes.remove(node);
				fireNodeSelectionChanged();
			}
		}
	}

	@Override
	public void graphClicked(Node v, MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON3) {
			NodePropertiesDialog dialog = new NodePropertiesDialog(
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

	private VisualizationViewer<Node, Edge> createViewer(String layoutType,
			int nodeSize, boolean hideNodes) {
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
		}

		layout.setSize(size);

		VisualizationViewer<Node, Edge> vv = new VisualizationViewer<Node, Edge>(
				layout);

		vv.setPreferredSize(size);
		vv.setGraphMouse(mouseModel);
		vv.addGraphMouseListener(this);
		vv.getRenderContext().setVertexShapeTransformer(
				new ShapeTransformer(nodeSize));
		vv.getPickedVertexState().addItemListener(this);

		return vv;
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

		JPanel panel = new JPanel();

		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel.add(layoutPanel);
		panel.add(modePanel);

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
		private double value;

		public Edge(Node n1, Node n2, double value) {
			this.n1 = n1;
			this.n2 = n2;
			this.value = value;
		}

		public double getValue() {
			return value;
		}

		public Node getN1() {
			return n1;
		}

		public Node getN2() {
			return n2;
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

	private static class NodePropertiesDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		public NodePropertiesDialog(Component parent, Node node) {
			super(JOptionPane.getFrameForComponent(parent), "Properties", true);

			JPanel centerPanel = new JPanel();
			JPanel leftCenterPanel = new JPanel();
			JPanel rightCenterPanel = new JPanel();

			leftCenterPanel.setLayout(new GridLayout(node.getProperties()
					.size(), 1, 5, 5));
			rightCenterPanel.setLayout(new GridLayout(node.getProperties()
					.size(), 1, 5, 5));

			for (Map.Entry<String, String> property : node.getProperties()
					.entrySet()) {
				JTextField field = new JTextField(property.getValue());

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
