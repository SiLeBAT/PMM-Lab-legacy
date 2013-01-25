/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.bund.bfr.knime.gis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;

import org.eclipse.stem.gis.shp.ShpPolygon;
import org.eclipse.stem.gis.shp.type.Part;

public class GISCanvas extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String TRANSFORMING_MODE = "Transforming";
	private static final String PICKING_MODE = "Picking";
	private static final String[] MODES = { TRANSFORMING_MODE, PICKING_MODE };

	private static final int DEFAULT_BORDER_ALPHA = 255;
	private static final int DEFAULT_EDGE_ALPHA = 255;
	private static final String DEFAULT_MODE = PICKING_MODE;

	private GISComponent gisComponent;
	private JButton layoutButton;
	private JSlider borderAlphaSlider;
	private JButton borderAlphaButton;
	private JSlider edgeAlphaSlider;
	private JButton edgeAlphaButton;
	private JComboBox<String> modeBox;

	public GISCanvas(List<Node> nodes, List<Edge> edges,
			List<String> nodeProperties, List<String> edgeProperties) {
		gisComponent = new GISComponent(nodes, edges, nodeProperties,
				edgeProperties, DEFAULT_BORDER_ALPHA, DEFAULT_EDGE_ALPHA,
				DEFAULT_MODE);
		layoutButton = new JButton("Reset");
		layoutButton.addActionListener(this);
		borderAlphaSlider = new JSlider(0, 255, DEFAULT_BORDER_ALPHA);
		borderAlphaSlider.setPreferredSize(new Dimension(100, borderAlphaSlider
				.getPreferredSize().height));
		borderAlphaButton = new JButton("Apply");
		borderAlphaButton.addActionListener(this);
		edgeAlphaSlider = new JSlider(0, 255, DEFAULT_EDGE_ALPHA);
		edgeAlphaSlider.setPreferredSize(new Dimension(100, edgeAlphaSlider
				.getPreferredSize().height));
		edgeAlphaButton = new JButton("Apply");
		edgeAlphaButton.addActionListener(this);
		modeBox = new JComboBox<String>(MODES);
		modeBox.setSelectedItem(DEFAULT_MODE);
		modeBox.addActionListener(this);

		JPanel layoutPanel = new JPanel();

		layoutPanel.setBorder(BorderFactory.createTitledBorder("Layout"));
		layoutPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		layoutPanel.add(layoutButton);

		JPanel borderAlphaPanel = new JPanel();

		borderAlphaPanel.setBorder(BorderFactory
				.createTitledBorder("Border Alpha"));
		borderAlphaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		borderAlphaPanel.add(borderAlphaSlider);
		borderAlphaPanel.add(borderAlphaButton);

		JPanel edgeAlphaPanel = new JPanel();

		edgeAlphaPanel
				.setBorder(BorderFactory.createTitledBorder("Edge Alpha"));
		edgeAlphaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		edgeAlphaPanel.add(edgeAlphaSlider);
		edgeAlphaPanel.add(edgeAlphaButton);

		JPanel modePanel = new JPanel();

		modePanel.setBorder(BorderFactory.createTitledBorder("Editing Mode"));
		modePanel.setLayout(new FlowLayout());
		modePanel.add(modeBox);

		JPanel optionsPanel = new JPanel();

		optionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		optionsPanel.add(layoutPanel);
		optionsPanel.add(borderAlphaPanel);
		optionsPanel.add(edgeAlphaPanel);
		optionsPanel.add(modePanel);

		setLayout(new BorderLayout());
		add(gisComponent, BorderLayout.CENTER);
		add(optionsPanel, BorderLayout.SOUTH);
	}

	public void setHighlightedRegions(List<String> highlightedRegions) {
		gisComponent.setHighlightedRegions(highlightedRegions);
		gisComponent.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == layoutButton) {
			gisComponent.reset();
			gisComponent.repaint();
		} else if (e.getSource() == borderAlphaButton) {
			gisComponent.setBorderAlpha(borderAlphaSlider.getValue());
			gisComponent.repaint();
		} else if (e.getSource() == edgeAlphaButton) {
			gisComponent.setEdgeAlpha(edgeAlphaSlider.getValue());
			gisComponent.repaint();
		} else if (e.getSource() == modeBox) {
			gisComponent.setMode((String) modeBox.getSelectedItem());
		}
	}

	public static interface GISElement {

		public Map<String, Double> getProperties();
	}

	public static class Node implements GISElement {

		private String id;
		private Map<String, Double> properties;

		private ShpPolygon polygon;
		private Point2D.Double center;
		private Rectangle2D.Double boundingBox;

		private List<Polygon> transformedPolygon;
		private Point transformedCenter;

		public Node(String id, Map<String, Double> properties,
				ShpPolygon polygon) {
			this.id = id;
			this.properties = properties;
			this.polygon = polygon;
			center = GISUtilities.getCenter(polygon);
			boundingBox = GISUtilities.getBoundingBox(polygon);
		}

		public String getId() {
			return id;
		}

		@Override
		public Map<String, Double> getProperties() {
			return properties;
		}

		public ShpPolygon getPolygon() {
			return polygon;
		}

		public Point2D.Double getCenter() {
			return center;
		}

		public Rectangle2D.Double getBoundingBox() {
			return boundingBox;
		}

		public List<Polygon> getTransformedPolygon() {
			return transformedPolygon;
		}

		public Point getTransformedCenter() {
			return transformedCenter;
		}

		public void setTransform(double translationX, double translationY,
				double scaleX, double scaleY) {
			transformedPolygon = new ArrayList<Polygon>();
			transformedCenter = new Point();

			for (Part part : polygon.getParts()) {
				int[] xs = new int[part.getPointCount()];
				int[] ys = new int[part.getPointCount()];

				for (int i = 0; i < part.getPointCount(); i++) {
					xs[i] = (int) (part.getXs()[i] * scaleX + translationX);
					ys[i] = (int) (part.getYs()[i] * scaleY + translationY);
				}

				transformedPolygon
						.add(new Polygon(xs, ys, part.getPointCount()));
			}

			transformedCenter.x = (int) (center.x * scaleX + translationX);
			transformedCenter.y = (int) (center.y * scaleY + translationY);
		}

		public boolean containsPoint(Point2D.Double point) {
			return boundingBox.contains(point)
					&& GISUtilities.containsPoint(polygon, point);
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

	public static class Edge implements GISElement {

		private Node from;
		private Node to;
		private Map<String, Double> properties;

		public Edge(Node from, Node to, Map<String, Double> properties) {
			this.from = from;
			this.to = to;
			this.properties = properties;
		}

		public Node getFrom() {
			return from;
		}

		public Node getTo() {
			return to;
		}

		@Override
		public Map<String, Double> getProperties() {
			return properties;
		}

		@Override
		public int hashCode() {
			String s = from + "->" + to;

			return s.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Edge) {
				return from.equals(((Edge) obj).from)
						&& to.equals(((Edge) obj).to);
			}

			return false;
		}
	}

	private static class GISComponent extends JComponent implements
			ActionListener, MouseListener, MouseMotionListener,
			MouseWheelListener {

		private static final long serialVersionUID = 1L;
		private static final double ZOOMING_FACTOR = 1.1;

		private List<Node> nodes;
		private List<Edge> edges;
		private List<String> nodeProperties;
		private List<String> edgeProperties;
		private int borderAlpha;
		private int edgeAlpha;
		private String mode;
		private List<String> highlightedRegions;

		private boolean transformComputed;
		private boolean transformedShapesComputed;
		private double scaleX;
		private double scaleY;
		private double translationX;
		private double translationY;

		private boolean leftButtonPressed;
		private int lastX;
		private int lastY;
		private Node lastContainingPolygon;

		private JPopupMenu popup;
		private JMenuItem highlightNodesItem;
		private JMenuItem clearHighlightNodesItem;
		private JMenuItem highlightEdgesItem;
		private JMenuItem clearHighlightEdgesItem;

		private ValueHighlightCondition nodesHighlightCondition;
		private ValueHighlightCondition edgesHighlightCondition;

		public GISComponent(List<Node> nodes, List<Edge> edges,
				List<String> nodeProperties, List<String> edgeProperties,
				int borderAlpha, int edgeAlpha, String mode) {
			this.nodes = nodes;
			this.edges = edges;
			this.nodeProperties = nodeProperties;
			this.edgeProperties = edgeProperties;
			this.borderAlpha = borderAlpha;
			this.edgeAlpha = edgeAlpha;
			this.mode = mode;

			transformComputed = false;
			transformedShapesComputed = false;
			leftButtonPressed = false;
			lastContainingPolygon = null;

			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			createPopupMenu();
			updateCursor();
		}

		public void setBorderAlpha(int borderAlpha) {
			this.borderAlpha = borderAlpha;
		}

		public void setEdgeAlpha(int edgeAlpha) {
			this.edgeAlpha = edgeAlpha;
		}

		public void setMode(String mode) {
			this.mode = mode;
			updateCursor();
		}

		public void setHighlightedRegions(List<String> highlightedRegions) {
			this.highlightedRegions = highlightedRegions;
		}

		public void reset() {
			transformComputed = false;
		}

		@Override
		protected void paintComponent(Graphics g) {
			if (!transformComputed) {
				computeTransform();
				transformComputed = true;
				computeTransformedShapes();
				transformedShapesComputed = true;
			}

			if (!transformedShapesComputed) {
				computeTransformedShapes();
				transformedShapesComputed = true;
			}

			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());

			/*
			 * ------------------------------------------------------------------
			 */

			if (nodesHighlightCondition != null) {
				Map<GISElement, Double> highlightedNodes = nodesHighlightCondition
						.getValues(new ArrayList<GISElement>(nodes));

				for (GISElement element : highlightedNodes.keySet()) {
					Node node = (Node) element;
					float alpha = highlightedNodes.get(element).floatValue();

					if (alpha != 0.0f) {
						g.setColor(new Color(1.0f, 1.0f - alpha, 1.0f - alpha));

						for (Polygon part : node.getTransformedPolygon()) {
							g.fillPolygon(part);
						}
					}
				}
			}

			/*
			 * ------------------------------------------------------------------
			 */

			if (highlightedRegions != null) {
				Set<String> highlightedSet = new LinkedHashSet<>(
						highlightedRegions);

				for (Node node : nodes) {
					if (highlightedSet.contains(node.getId())) {
						g.setColor(Color.GREEN);

						for (Polygon part : node.getTransformedPolygon()) {
							g.fillPolygon(part);
						}
					}
				}
			}

			/*
			 * ------------------------------------------------------------------
			 */

			BufferedImage borderImage = new BufferedImage(getWidth(),
					getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics borderGraphics = borderImage.getGraphics();

			borderGraphics.setColor(Color.BLACK);

			for (Node node : nodes) {
				for (Polygon part : node.getTransformedPolygon()) {
					borderGraphics.drawPolygon(part);
				}
			}

			float[] borderScales = { 1f, 1f, 1f, (float) borderAlpha / 255.0f };
			float[] borderOffsets = new float[4];

			((Graphics2D) g).drawImage(borderImage, new RescaleOp(borderScales,
					borderOffsets, null), 0, 0);

			/*
			 * ------------------------------------------------------------------
			 */

			BufferedImage edgeImage = new BufferedImage(getWidth(),
					getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics edgeGraphics = edgeImage.getGraphics();
			Map<GISElement, Double> highlightedEdges = new LinkedHashMap<>();

			if (edgesHighlightCondition != null) {
				highlightedEdges = edgesHighlightCondition
						.getValues(new ArrayList<GISElement>(edges));
			}

			for (Edge edge : edges) {
				if (highlightedEdges.containsKey(edge)) {
					float alpha = highlightedEdges.get(edge).floatValue();

					edgeGraphics.setColor(new Color(alpha, 0.0f, 0.0f));
				} else {
					edgeGraphics.setColor(Color.BLACK);
				}

				Point center1 = edge.getFrom().getTransformedCenter();
				Point center2 = edge.getTo().getTransformedCenter();

				edgeGraphics.drawLine(center1.x, center1.y, center2.x,
						center2.y);
			}

			float[] edgeScales = { 1f, 1f, 1f, (float) edgeAlpha / 255.0f };
			float[] edgeOffsets = new float[4];

			((Graphics2D) g).drawImage(edgeImage, new RescaleOp(edgeScales,
					edgeOffsets, null), 0, 0);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(400, 600);
		}

		private void computeTransform() {
			Rectangle2D.Double polygonsBounds = getPolygonsBounds();
			Rectangle canvasBounds = getVisibleRect();
			double widthRatio = canvasBounds.width / polygonsBounds.width;
			double heightRatio = canvasBounds.height / polygonsBounds.height;
			double canvasCenterX = canvasBounds.getCenterX();
			double canvasCenterY = canvasBounds.getCenterY();
			double polygonCenterX = polygonsBounds.getCenterX();
			double polygonCenterY = polygonsBounds.getCenterY();

			scaleX = Math.min(widthRatio, heightRatio);
			scaleY = -scaleX;
			translationX = canvasCenterX - polygonCenterX * scaleX;
			translationY = canvasCenterY - polygonCenterY * scaleY;
		}

		private void computeTransformedShapes() {
			for (Node node : nodes) {
				node.setTransform(translationX, translationY, scaleX, scaleY);
			}
		}

		private Rectangle2D.Double getPolygonsBounds() {
			Rectangle2D.Double bounds = null;

			for (Node node : nodes) {
				if (bounds == null) {
					bounds = node.getBoundingBox();
				} else {
					bounds = (Rectangle2D.Double) bounds.createUnion(node
							.getBoundingBox());
				}
			}

			return bounds;
		}

		private void zoomIn(int x, int y) {
			Point2D.Double p = getInversedPoint(x, y);

			translationX -= (ZOOMING_FACTOR - 1) * scaleX * p.x;
			translationY -= (ZOOMING_FACTOR - 1) * scaleY * p.y;
			scaleX *= ZOOMING_FACTOR;
			scaleY *= ZOOMING_FACTOR;
			transformedShapesComputed = false;
			repaint();

		}

		private void zoomOut(int x, int y) {
			Point2D.Double p = getInversedPoint(x, y);

			scaleX /= ZOOMING_FACTOR;
			scaleY /= ZOOMING_FACTOR;
			translationX += (ZOOMING_FACTOR - 1) * scaleX * p.x;
			translationY += (ZOOMING_FACTOR - 1) * scaleY * p.y;
			transformedShapesComputed = false;
			repaint();
		}

		private Point2D.Double getInversedPoint(int x, int y) {
			return new Point2D.Double((x - translationX) / scaleX,
					(y - translationY) / scaleY);
		}

		private void createPopupMenu() {
			highlightNodesItem = new JMenuItem("Highlight Nodes");
			highlightNodesItem.addActionListener(this);
			clearHighlightNodesItem = new JMenuItem("Clear Node Highlights");
			clearHighlightNodesItem.addActionListener(this);
			highlightEdgesItem = new JMenuItem("Highlight Edges");
			highlightEdgesItem.addActionListener(this);
			clearHighlightEdgesItem = new JMenuItem("Clear Edge Highlights");
			clearHighlightEdgesItem.addActionListener(this);

			popup = new JPopupMenu();
			popup.add(highlightNodesItem);
			popup.add(clearHighlightNodesItem);
			popup.add(highlightEdgesItem);
			popup.add(clearHighlightEdgesItem);
		}

		private void updateCursor() {
			if (mode.equals(TRANSFORMING_MODE)) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else if (mode.equals(PICKING_MODE)) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}

		private Node getIdOfContainingPolygon(int x, int y) {
			Point2D.Double p = getInversedPoint(x, y);

			for (Node node : nodes) {
				if (node.containsPoint(p)) {
					return node;
				}
			}

			return null;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() > 0) {
				zoomIn(e.getX(), e.getY());
			} else if (e.getWheelRotation() < 0) {
				zoomOut(e.getX(), e.getY());
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				leftButtonPressed = true;
				lastX = e.getX();
				lastY = e.getY();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				leftButtonPressed = false;
			} else if (e.getButton() == MouseEvent.BUTTON3) {
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
		public void mouseDragged(MouseEvent e) {
			if (leftButtonPressed && mode.equals(TRANSFORMING_MODE)) {
				int diffX = e.getX() - lastX;
				int diffY = e.getY() - lastY;

				lastX = e.getX();
				lastY = e.getY();

				if (diffX != 0 || diffY != 0) {
					translationX += diffX;
					translationY += diffY;
					transformedShapesComputed = false;
					repaint();
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Node node = getIdOfContainingPolygon(e.getX(), e.getY());

			if (node == null) {
				setToolTipText(null);
			} else if (!node.equals(lastContainingPolygon)) {
				String tooltip = "<html><table border=\"0\">";

				tooltip += "<tr><td>ID</td><td>" + node.getId() + "</td></tr>";

				for (Map.Entry<String, Double> entry : node.getProperties()
						.entrySet()) {
					tooltip += "<tr><td>" + entry.getKey() + "</td><td>"
							+ entry.getValue() + "</td></tr>";
				}

				tooltip += "</table></html>";

				setToolTipText(tooltip);
			}

			lastContainingPolygon = node;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == highlightNodesItem) {
				HighlightDialog dialog = new HighlightDialog(this,
						nodeProperties, nodesHighlightCondition);

				dialog.setLocationRelativeTo(this);
				dialog.setVisible(true);

				if (dialog.isSuccessful()) {
					nodesHighlightCondition = dialog.getHighlightCondition();
					repaint();
				}
			} else if (e.getSource() == clearHighlightNodesItem) {
				nodesHighlightCondition = null;
				repaint();
			} else if (e.getSource() == highlightEdgesItem) {
				HighlightDialog dialog = new HighlightDialog(this,
						edgeProperties, edgesHighlightCondition);

				dialog.setLocationRelativeTo(this);
				dialog.setVisible(true);

				if (dialog.isSuccessful()) {
					edgesHighlightCondition = dialog.getHighlightCondition();
					repaint();
				}
			} else if (e.getSource() == clearHighlightEdgesItem) {
				edgesHighlightCondition = null;
				repaint();
			}
		}
	}

	public static class ValueHighlightCondition {

		public static final String VALUE_TYPE = "Value";
		public static final String LOG_VALUE_TYPE = "Log Value";
		public static final String[] TYPES = { VALUE_TYPE, LOG_VALUE_TYPE };

		private String property;
		private String type;

		public ValueHighlightCondition(String property, String type) {
			this.property = property;
			this.type = type;
		}

		public Map<GISElement, Double> getValues(List<GISElement> elements) {
			Map<GISElement, Double> values = new LinkedHashMap<>();

			for (GISElement element : elements) {
				Double value = element.getProperties().get(property);

				if (value != null && !value.isNaN() && !value.isInfinite()
						&& value >= 0.0) {
					values.put(element, value);
				} else {
					values.put(element, 0.0);
				}
			}

			double max = Collections.max(values.values());

			if (max != 0.0) {
				for (GISElement element : elements) {
					values.put(element, values.get(element) / max);
				}
			}

			if (type.equals(LOG_VALUE_TYPE)) {
				for (GISElement element : elements) {
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

	private static class HighlightDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton okButton;
		private JButton cancelButton;

		private JComboBox<String> valuePropertyBox;
		private JComboBox<String> valueTypeBox;

		private List<String> properties;

		private ValueHighlightCondition highlightCondition;
		private boolean successful;

		public HighlightDialog(JComponent parent, List<String> properties,
				ValueHighlightCondition initialHighlightCondition) {
			super(JOptionPane.getFrameForComponent(parent),
					"Highlight Condition", true);
			this.properties = properties;
			highlightCondition = null;
			successful = false;

			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel bottomPanel = new JPanel();

			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(createValuePanel(initialHighlightCondition),
					BorderLayout.CENTER);
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
			}
		}

		public ValueHighlightCondition getHighlightCondition() {
			return highlightCondition;
		}

		public boolean isSuccessful() {
			return successful;
		}

		private JPanel createValuePanel(
				ValueHighlightCondition highlightCondition) {
			valuePropertyBox = new JComboBox<>(
					properties.toArray(new String[0]));
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

		private ValueHighlightCondition createCondition() {
			return new ValueHighlightCondition(
					(String) valuePropertyBox.getSelectedItem(),
					(String) valueTypeBox.getSelectedItem());
		}
	}

}
