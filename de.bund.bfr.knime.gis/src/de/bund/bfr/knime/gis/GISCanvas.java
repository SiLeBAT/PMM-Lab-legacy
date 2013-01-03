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
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;

import org.eclipse.stem.gis.shp.ShpPolygon;
import org.eclipse.stem.gis.shp.type.Part;

public class GISCanvas extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_BORDER_ALPHA = 255;

	private GISComponent gisComponent;
	private JSlider borderAlphaSlider;
	private JButton borderAlphaButton;

	public GISCanvas(Map<String, ShpPolygon> shapes) {
		gisComponent = new GISComponent(shapes, DEFAULT_BORDER_ALPHA);
		borderAlphaSlider = new JSlider(0, 255, DEFAULT_BORDER_ALPHA);
		borderAlphaButton = new JButton("Apply");
		borderAlphaButton.addActionListener(this);

		JPanel borderAlphaPanel = new JPanel();

		borderAlphaPanel.setBorder(BorderFactory
				.createTitledBorder("Border Alpha"));
		borderAlphaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		borderAlphaPanel.add(borderAlphaSlider);
		borderAlphaPanel.add(borderAlphaButton);

		JPanel optionsPanel = new JPanel();

		optionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		optionsPanel.add(borderAlphaPanel);

		setLayout(new BorderLayout());
		add(gisComponent, BorderLayout.CENTER);
		add(optionsPanel, BorderLayout.SOUTH);
	}

	public void setRegionData(Map<String, Double> regionData) {
		gisComponent.setRegionData(regionData);
	}

	public void setEdgeData(Map<Edge, Double> edgeData) {
		gisComponent.setEdgeData(edgeData);
	}

	public void setHighlightedRegions(List<String> highlightedRegions) {
		gisComponent.setHighlightedRegions(highlightedRegions);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == borderAlphaButton) {
			gisComponent.setBorderAlpha((borderAlphaSlider.getValue()));
			gisComponent.repaint();
		}
	}

	public static class Edge {

		private String from;
		private String to;

		public Edge(String from, String to) {
			this.from = from;
			this.to = to;
		}

		public String getFrom() {
			return from;
		}

		public String getTo() {
			return to;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Edge other = (Edge) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.equals(other.to))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Edge [from=" + from + ", to=" + to + "]";
		}
	}

	private static class GISComponent extends JComponent implements
			ActionListener, MouseListener, MouseMotionListener,
			MouseWheelListener {

		private static final long serialVersionUID = 1L;
		private static final double ZOOMING_FACTOR = 1.1;

		private Map<String, ShpPolygon> shapes;
		private Map<String, Point2D.Double> shapeCenters;
		private Map<String, Rectangle2D.Double> shapeBoundingBoxes;
		private Map<String, Double> regionData;
		private Map<Edge, Double> edgeData;
		private List<String> highlightedRegions;
		private int borderAlpha;

		private boolean transformComputed;
		private double scaleX;
		private double scaleY;
		private double translationX;
		private double translationY;

		private boolean transformedShapesComputed;
		private Map<String, List<Polygon>> transformedShapes;
		private Map<String, Point> transformedShapeCenters;

		private boolean leftButtonPressed;
		private int lastX;
		private int lastY;
		private String lastContainingPolygon;

		private double gainFactor;
		private boolean logScale;

		private JPopupMenu popup;
		private JCheckBoxMenuItem logScaleItem;
		private JRadioButtonMenuItem gain01Item;
		private JRadioButtonMenuItem gain1Item;
		private JRadioButtonMenuItem gain10Item;

		public GISComponent(Map<String, ShpPolygon> shapes, int borderAlpha) {
			this.shapes = shapes;
			this.borderAlpha = borderAlpha;
			shapeCenters = new LinkedHashMap<String, Point2D.Double>();
			shapeBoundingBoxes = new LinkedHashMap<String, Rectangle2D.Double>();

			for (String id : shapes.keySet()) {
				shapeCenters.put(id, GISUtilities.getCenter(shapes.get(id)));
			}

			for (String id : shapes.keySet()) {
				shapeBoundingBoxes.put(id,
						GISUtilities.getBoundingBox(shapes.get(id)));
			}

			transformComputed = false;
			transformedShapesComputed = false;
			leftButtonPressed = false;
			lastContainingPolygon = null;

			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			createPopupMenu();

			gainFactor = 1.0;
			gain1Item.setSelected(true);
			logScale = false;
			logScaleItem.setSelected(false);
		}

		public void setRegionData(Map<String, Double> regionData) {
			this.regionData = regionData;
		}

		public void setEdgeData(Map<Edge, Double> edgeData) {
			this.edgeData = edgeData;
		}

		public void setHighlightedRegions(List<String> highlightedRegions) {
			this.highlightedRegions = highlightedRegions;
		}

		public void setBorderAlpha(int borderAlpha) {
			this.borderAlpha = borderAlpha;
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

			if (regionData != null) {
				double max = Collections.max(regionData.values());

				for (String id : regionData.keySet()) {
					List<Polygon> poly = transformedShapes.get(id);
					Double value = regionData.get(id);

					if (poly != null) {
						value /= max;
						value *= gainFactor;
						value = Math.max(0.0, value);
						value = Math.min(1.0, value);

						if (logScale) {
							value = Math.log10(value * 9.0 + 1.0);
						}

						float alpha = value.floatValue();

						if (alpha != 0.0) {
							g.setColor(new Color(1.0f, 1.0f - alpha,
									1.0f - alpha));

							for (Polygon part : poly) {
								g.fillPolygon(part);
							}
						}
					}
				}
			}

			if (highlightedRegions != null) {
				for (String id : highlightedRegions) {
					List<Polygon> poly = transformedShapes.get(id);

					if (poly != null) {
						g.setColor(Color.GREEN);

						for (Polygon part : poly) {
							g.fillPolygon(part);
						}
					}
				}
			}

			BufferedImage borderImage = new BufferedImage(getWidth(),
					getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics borderGraphics = borderImage.getGraphics();

			borderGraphics.setColor(Color.BLACK);

			for (List<Polygon> poly : transformedShapes.values()) {
				for (Polygon part : poly) {
					borderGraphics.drawPolygon(part);
				}
			}

			float[] scales = { 1f, 1f, 1f, (float) borderAlpha / 255.0f };
			float[] offsets = new float[4];

			((Graphics2D) g).drawImage(borderImage, new RescaleOp(scales,
					offsets, null), 0, 0);

			if (edgeData != null) {
				g.setColor(Color.BLACK);

				for (Edge edge : edgeData.keySet()) {
					Point center1 = transformedShapeCenters.get(edge.getFrom());
					Point center2 = transformedShapeCenters.get(edge.getTo());

					if (center1 != null && center2 != null) {
						g.drawLine(center1.x, center1.y, center2.x, center2.y);
					}
				}
			}
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
			transformedShapes = new LinkedHashMap<String, List<Polygon>>();
			transformedShapeCenters = new LinkedHashMap<String, Point>();

			for (String id : shapes.keySet()) {
				ShpPolygon poly = shapes.get(id);
				Point2D.Double center = shapeCenters.get(id);
				List<Polygon> transPoly = new ArrayList<Polygon>();
				Point transCenter = new Point();

				for (Part part : poly.getParts()) {
					int[] xs = new int[part.getPointCount()];
					int[] ys = new int[part.getPointCount()];

					for (int i = 0; i < part.getPointCount(); i++) {
						xs[i] = (int) (part.getXs()[i] * scaleX + translationX);
						ys[i] = (int) (part.getYs()[i] * scaleY + translationY);
					}

					transPoly.add(new Polygon(xs, ys, part.getPointCount()));
				}

				transCenter.x = (int) (center.x * scaleX + translationX);
				transCenter.y = (int) (center.y * scaleY + translationY);

				transformedShapes.put(id, transPoly);
				transformedShapeCenters.put(id, transCenter);
			}
		}

		private Rectangle2D.Double getPolygonsBounds() {
			Rectangle2D.Double bounds = null;

			for (String id : shapeBoundingBoxes.keySet()) {
				if (bounds == null) {
					bounds = shapeBoundingBoxes.get(id);
				} else {
					bounds = (Rectangle2D.Double) bounds
							.createUnion(shapeBoundingBoxes.get(id));
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
			logScaleItem = new JCheckBoxMenuItem("Logarithmic Scaling");
			logScaleItem.addActionListener(this);
			gain01Item = new JRadioButtonMenuItem("0.1");
			gain01Item.addActionListener(this);
			gain1Item = new JRadioButtonMenuItem("1.0");
			gain1Item.addActionListener(this);
			gain10Item = new JRadioButtonMenuItem("10.0");
			gain10Item.addActionListener(this);

			ButtonGroup gainGroup = new ButtonGroup();

			gainGroup.add(gain01Item);
			gainGroup.add(gain1Item);
			gainGroup.add(gain10Item);

			JMenu gainMenu = new JMenu("Gain Factor");

			gainMenu.add(gain01Item);
			gainMenu.add(gain1Item);
			gainMenu.add(gain10Item);

			popup = new JPopupMenu();
			popup.add(logScaleItem);
			popup.add(gainMenu);
		}

		private String getIdOfContainingPolygon(int x, int y) {
			Point2D.Double p = getInversedPoint(x, y);

			for (String id : shapes.keySet()) {
				if (shapeBoundingBoxes.get(id).contains(p)
						&& GISUtilities.containsPoint(shapes.get(id), p)) {
					return id;
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
			if (leftButtonPressed) {
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
			String id = getIdOfContainingPolygon(e.getX(), e.getY());

			if (id == null) {
				setToolTipText(null);
			} else if (!id.equals(lastContainingPolygon)) {
				Double value = regionData.get(id);
				String row1 = "<tr><td>ID</td><td>" + id + "</td></tr>";
				String row2 = "<tr><td>Value</td><td>" + value + "</td></tr>";

				setToolTipText("<html><table border=\"0\">" + row1 + row2
						+ "</table></html>");
			}

			lastContainingPolygon = id;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == logScaleItem) {
				if (logScaleItem.isSelected()) {
					logScale = true;
				} else {
					logScale = false;
				}

				repaint();
			} else if (e.getSource() == gain01Item) {
				gainFactor = 0.1;
				repaint();
			} else if (e.getSource() == gain1Item) {
				gainFactor = 1.0;
				repaint();
			} else if (e.getSource() == gain10Item) {
				gainFactor = 10.0;
				repaint();
			}
		}
	}

}
