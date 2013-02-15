package de.bund.bfr.knime.foodprocess.view;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.ui.RectangleEdge;

import de.bund.bfr.knime.pmm.common.chart.ColorAndShapeCreator;

public class MyAxis extends NumberAxis {

	private static final long serialVersionUID = 1L;

	private LinkedHashMap<Point2D.Double, String> ranges;

	public MyAxis(String label, LinkedHashMap<Point2D.Double, String> ranges) {
		super(label);
		this.ranges = ranges;
	}

	@Override
	protected void drawAxisLine(Graphics2D g2, double cursor,
			Rectangle2D dataArea, RectangleEdge edge) {
		Line2D axisLine = null;
		if (edge == RectangleEdge.TOP) {
			axisLine = new Line2D.Double(dataArea.getX(), cursor,
					dataArea.getMaxX(), cursor);
		} else if (edge == RectangleEdge.BOTTOM) {
			axisLine = new Line2D.Double(dataArea.getX(), cursor,
					dataArea.getMaxX(), cursor);
		} else if (edge == RectangleEdge.LEFT) {
			axisLine = new Line2D.Double(cursor, dataArea.getY(), cursor,
					dataArea.getMaxY());
		} else if (edge == RectangleEdge.RIGHT) {
			axisLine = new Line2D.Double(cursor, dataArea.getY(), cursor,
					dataArea.getMaxY());
		}
		g2.setPaint(getAxisLinePaint());
		g2.setStroke(getAxisLineStroke());
		g2.draw(axisLine);

		/* ------------------------------------------------------------------ */

		ColorAndShapeCreator colorCreator = new ColorAndShapeCreator(
				ranges.size());

	    g2.setFont(new Font("TimesRoman", Font.PLAIN, 8));
	    FontMetrics fm = g2.getFontMetrics();	    
	    int i = 0;
		for (Point2D.Double range : ranges.keySet()) {
			Line2D rangeLine = null;
			float xx1 = (float) valueToJava2D(range.x, dataArea, edge);
			float xx2 = (float) valueToJava2D(range.y, dataArea, edge);

			if (edge == RectangleEdge.TOP) {
				rangeLine = new Line2D.Double(xx1, cursor, xx2, cursor);
			} else if (edge == RectangleEdge.BOTTOM) {
				rangeLine = new Line2D.Double(xx1, cursor, xx2, cursor);
			} else if (edge == RectangleEdge.LEFT) {
				rangeLine = new Line2D.Double(cursor, xx1, cursor, xx2);
			} else if (edge == RectangleEdge.RIGHT) {
				rangeLine = new Line2D.Double(cursor, xx1, cursor, xx2);
			}

			g2.setPaint(colorCreator.getColorList().get(i));
			g2.setStroke(new BasicStroke(4.0f));
			g2.draw(rangeLine);
			String process = ranges.get(range);
		    Rectangle2D r = fm.getStringBounds(process, g2);
			g2.drawString(process, (float) ((xx2+xx1)/2 - r.getWidth() / 2), (float)cursor - (i%2 == 0 ? 5 : 15));
			i++;
		}

		/* ------------------------------------------------------------------ */

		boolean drawUpOrRight = false;
		boolean drawDownOrLeft = false;
		if (this.isPositiveArrowVisible()) {
			if (this.isInverted()) {
				drawDownOrLeft = true;
			} else {
				drawUpOrRight = true;
			}
		}
		if (this.isNegativeArrowVisible()) {
			if (this.isInverted()) {
				drawUpOrRight = true;
			} else {
				drawDownOrLeft = true;
			}
		}
		if (drawUpOrRight) {
			double x = 0.0;
			double y = 0.0;
			Shape arrow = null;
			if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
				x = dataArea.getMaxX();
				y = cursor;
				arrow = this.getRightArrow();
			} else if (edge == RectangleEdge.LEFT
					|| edge == RectangleEdge.RIGHT) {
				x = cursor;
				y = dataArea.getMinY();
				arrow = this.getUpArrow();
			}

			// draw the arrow...
			AffineTransform transformer = new AffineTransform();
			transformer.setToTranslation(x, y);
			Shape shape = transformer.createTransformedShape(arrow);
			g2.fill(shape);
			g2.draw(shape);
		}

		if (drawDownOrLeft) {
			double x = 0.0;
			double y = 0.0;
			Shape arrow = null;
			if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
				x = dataArea.getMinX();
				y = cursor;
				arrow = this.getLeftArrow();
			} else if (edge == RectangleEdge.LEFT
					|| edge == RectangleEdge.RIGHT) {
				x = cursor;
				y = dataArea.getMaxY();
				arrow = this.getDownArrow();
			}

			// draw the arrow...
			AffineTransform transformer = new AffineTransform();
			transformer.setToTranslation(x, y);
			Shape shape = transformer.createTransformedShape(arrow);
			g2.fill(shape);
			g2.draw(shape);
		}
	}

}
