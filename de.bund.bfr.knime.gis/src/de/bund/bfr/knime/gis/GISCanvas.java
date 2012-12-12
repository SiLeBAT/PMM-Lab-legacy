package de.bund.bfr.knime.gis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import org.eclipse.stem.gis.shp.ShpPolygon;
import org.eclipse.stem.gis.shp.type.Part;

public class GISCanvas extends JComponent implements ActionListener,
		MouseListener, MouseMotionListener, MouseWheelListener {

	private static final long serialVersionUID = 1L;
	private static final double ZOOMING_FACTOR = 1.1;

	private List<ShpPolygon> shapes;
	private List<String> columnNames;
	private List<List<String>> dataRows;
	private String idColumn;
	private Map<String, Double> data;

	private boolean transformComputed;
	private double scaleX;
	private double scaleY;
	private double translationX;
	private double translationY;

	private boolean transformedShapesComputed;
	private List<List<Polygon>> transformedShapes;

	private boolean leftButtonPressed;
	private int lastX;
	private int lastY;
	private int lastContainingPolygon;

	private double gainFactor;
	private boolean logScale;

	private JPopupMenu popup;
	private JCheckBoxMenuItem logScaleItem;
	private JRadioButtonMenuItem gain01Item;
	private JRadioButtonMenuItem gain1Item;
	private JRadioButtonMenuItem gain10Item;

	public GISCanvas(List<ShpPolygon> shapes, List<String> columnNames,
			List<List<String>> dataRows, String idColumn) {
		this.shapes = shapes;
		this.columnNames = columnNames;
		this.dataRows = dataRows;
		this.idColumn = idColumn;

		transformComputed = false;
		transformedShapesComputed = false;
		leftButtonPressed = false;
		lastContainingPolygon = -1;

		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		createPopupMenu();

		gainFactor = 1.0;
		gain1Item.setSelected(true);
		logScale = false;
		logScaleItem.setSelected(false);
	}

	public void setData(Map<String, Double> data) {
		this.data = data;
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

		if (data != null) {
			double max = Collections.max(data.values());

			for (int i = 0; i < transformedShapes.size(); i++) {
				String id = dataRows.get(i).get(columnNames.indexOf(idColumn));
				Double value = data.get(id);

				if (value != null) {
					value /= max;
					value *= gainFactor;
					value = Math.max(0.0, value);
					value = Math.min(1.0, value);

					if (logScale) {
						value = Math.log10(value * 9.0 + 1.0);
					}

					float alpha = value.floatValue();

					if (alpha != 0.0) {
						g.setColor(new Color(1.0f, 1.0f - alpha, 1.0f - alpha));

						for (Polygon part : transformedShapes.get(i)) {
							g.fillPolygon(part);
						}
					}
				}
			}
		}

		g.setColor(Color.BLACK);

		for (List<Polygon> poly : transformedShapes) {
			for (Polygon part : poly) {
				g.drawPolygon(part);
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 600);
	}

	private void computeTransform() {
		Rectangle2D.Double polygonsBounds = getPolygonsBounds();
		Rectangle canvasBounds = getBounds();
		double widthRatio = canvasBounds.width / polygonsBounds.width;
		double heightRatio = canvasBounds.height / polygonsBounds.height;
		int canvasCenterX = canvasBounds.x + canvasBounds.width / 2;
		int canvasCenterY = canvasBounds.y + canvasBounds.height / 2;
		double polygonCenterX = polygonsBounds.getCenterX();
		double polygonCenterY = polygonsBounds.getCenterY();

		scaleX = Math.min(widthRatio, heightRatio);
		scaleY = -scaleX;
		translationX = canvasCenterX - polygonCenterX * scaleX;
		translationY = canvasCenterY - polygonCenterY * scaleY;
	}

	private void computeTransformedShapes() {
		transformedShapes = new ArrayList<List<Polygon>>();

		for (ShpPolygon poly : shapes) {
			List<Polygon> transPoly = new ArrayList<Polygon>();

			for (Part part : poly.getParts()) {
				int[] xs = new int[part.getPointCount()];
				int[] ys = new int[part.getPointCount()];

				for (int i = 0; i < part.getPointCount(); i++) {
					xs[i] = (int) (part.getXs()[i] * scaleX + translationX);
					ys[i] = (int) (part.getYs()[i] * scaleY + translationY);
				}

				transPoly.add(new Polygon(xs, ys, part.getPointCount()));
			}

			transformedShapes.add(transPoly);
		}
	}

	private Rectangle2D.Double getPolygonsBounds() {
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

		for (ShpPolygon poly : shapes) {
			for (Part part : poly.getParts()) {
				for (double x : part.getXs()) {
					minX = Math.min(minX, x);
					maxX = Math.max(maxX, x);
				}

				for (double y : part.getYs()) {
					minY = Math.min(minY, y);
					maxY = Math.max(maxY, y);
				}
			}
		}

		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
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

	private int getIndexOfContainingPolygon(int x, int y) {
		Point2D.Double p = getInversedPoint(x, y);

		for (int i = 0; i < shapes.size(); i++) {
			for (Part part : shapes.get(i).getParts()) {
				if (containsPoint(part, p)) {
					return i;
				}
			}
		}

		return -1;
	}

	private boolean containsPoint(Part poly, Point2D.Double point) {
		int n = poly.getPointCount();
		double[] xs = poly.getXs();
		double[] ys = poly.getYs();
		double x = point.x;
		double y = point.y;

		int hits = 0;
		double x1 = xs[n - 1];
		double y1 = ys[n - 1];

		for (int i = 0; i < n; i++) {
			double x2 = xs[i];
			double y2 = ys[i];

			if (y == y2) {
				if (x < x2) {
					double y3 = ys[(i + 1) % n];

					if (y > Math.min(y1, y3) && y < Math.max(y1, y3)) {
						hits++;
					}
				}
			} else {
				if (y > Math.min(y1, y2) && y < Math.max(y1, y2)) {
					double xProjection = (x2 - x1) / (y2 - y1) * (y - y1) + x1;

					if (x < xProjection) {
						hits++;
					}
				}
			}

			x1 = x2;
			y1 = y2;
		}

		return hits % 2 != 0;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			zoomIn(e.getX(), e.getY());
		} else if (e.getWheelRotation() > 0) {
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
		int index = getIndexOfContainingPolygon(e.getX(), e.getY());

		if (index != lastContainingPolygon) {
			if (index == -1) {
				setToolTipText(null);
			} else {
				String id = dataRows.get(index).get(
						columnNames.indexOf(idColumn));
				Double value = data.get(id);
				String row1 = "<tr><td>ID</td><td>" + id + "</td></tr>";
				String row2 = "<tr><td>Value</td><td>" + value + "</td></tr>";

				setToolTipText("<html><table border=\"0\">" + row1 + row2
						+ "</table></html>");
			}

			lastContainingPolygon = index;
		}
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
