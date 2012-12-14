package de.bund.bfr.knime.gis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

public class GraphCanvas extends JPanel {

	private static final long serialVersionUID = 1L;

	public GraphCanvas(List<Node> nodes, List<Edge> edges) {
		Graph<Node, Edge> g = new SparseMultigraph<Node, Edge>();

		for (Node node : nodes) {
			g.addVertex(node);
		}

		for (Edge edge : edges) {
			g.addEdge(edge, edge.getN1(), edge.getN2());
		}

		Layout<Node, Edge> layout = new CircleLayout<Node, Edge>(g);

		layout.setSize(new Dimension(1000, 1000));

		VisualizationViewer<Node, Edge> vv = new VisualizationViewer<Node, Edge>(
				layout);

		vv.setPreferredSize(new Dimension(350, 350));
		// vv.getRenderContext().setVertexLabelTransformer(
		// new ToStringLabeller<Node>());
		// vv.getRenderContext().setEdgeLabelTransformer(
		// new ToStringLabeller<Edge>());

		DefaultModalGraphMouse<Integer, String> gm = new DefaultModalGraphMouse<Integer, String>();

		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(gm);

		setLayout(new BorderLayout());
		add(vv, BorderLayout.CENTER);
	}

	public static class Node {

		private String region;

		public Node(String region) {
			this.region = region;
		}

		public String getRegion() {
			return region;
		}

		@Override
		public String toString() {
			return "Node [region=" + region + "]";
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

		@Override
		public String toString() {
			return "Edge [value=" + value + "]";
		}
	}

}
