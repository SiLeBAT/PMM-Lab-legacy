package de.bund.bfr.knime.gis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

public class GraphCanvas extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String CIRCLE_LAYOUT = "Circle Layout";
	private static final String FR_LAYOUT = "FR Layout";
	private static final String[] LAYOUTS = { CIRCLE_LAYOUT, FR_LAYOUT };

	private static final String DEFAULT_LAYOUT = CIRCLE_LAYOUT;
	private static final int DEFAULT_NODESIZE = 10;

	private Graph<Node, Edge> graph;
	private VisualizationViewer<Node, Edge> viewer;

	private JComboBox<String> layoutBox;
	private JTextField nodeSizeField;
	private JButton applyButton;

	public GraphCanvas(List<Node> nodes, List<Edge> edges) {
		graph = new SparseMultigraph<Node, Edge>();

		for (Node node : nodes) {
			graph.addVertex(node);
		}

		for (Edge edge : edges) {
			graph.addEdge(edge, edge.getN1(), edge.getN2());
		}

		viewer = createViewer(DEFAULT_LAYOUT, DEFAULT_NODESIZE);
		setLayout(new BorderLayout());
		add(viewer, BorderLayout.CENTER);
		add(createOptionsPanel(), BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == applyButton) {
			try {
				remove(viewer);
				add(createViewer((String) layoutBox.getSelectedItem(),
						Integer.parseInt(nodeSizeField.getText())),
						BorderLayout.CENTER);
				revalidate();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this,
						"Node Size must be Integer Value", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private VisualizationViewer<Node, Edge> createViewer(String layoutType,
			int nodeSize) {
		Dimension size = null;
		Layout<Node, Edge> layout = null;

		if (viewer != null) {
			size = viewer.getSize();
		} else {
			size = new Dimension(400, 600);
		}

		if (layoutType.equals(CIRCLE_LAYOUT)) {
			layout = new CircleLayout<Node, Edge>(graph);
		} else if (layoutType.equals(FR_LAYOUT)) {
			layout = new FRLayout<Node, Edge>(graph);
			((FRLayout<Node, Edge>) layout).setMaxIterations(100);
		}

		layout.setSize(size);

		VisualizationViewer<Node, Edge> vv = new VisualizationViewer<Node, Edge>(
				layout);

		vv.setPreferredSize(size);
		vv.setGraphMouse(new DefaultModalGraphMouse<Integer, String>());
		vv.getRenderContext().setVertexShapeTransformer(
				new ShapeTransformer(nodeSize));

		return vv;
	}

	private JPanel createOptionsPanel() {
		layoutBox = new JComboBox<String>(LAYOUTS);
		layoutBox.setSelectedItem(DEFAULT_LAYOUT);
		layoutBox.addActionListener(this);
		nodeSizeField = new JTextField("" + DEFAULT_NODESIZE);
		nodeSizeField.setPreferredSize(new Dimension(50, nodeSizeField
				.getPreferredSize().height));
		applyButton = new JButton("Apply");
		applyButton.addActionListener(this);

		JPanel panel = new JPanel();

		panel.setLayout(new FlowLayout());
		panel.add(new JLabel("Layout Type:"));
		panel.add(layoutBox);
		panel.add(new JLabel("Node Size:"));
		panel.add(nodeSizeField);
		panel.add(applyButton);

		return panel;
	}

	public static class Node {

		private String region;

		public Node(String region) {
			this.region = region;
		}

		public String getRegion() {
			return region;
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

}
