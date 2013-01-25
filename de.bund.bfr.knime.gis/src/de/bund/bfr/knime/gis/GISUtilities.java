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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.eclipse.stem.gis.shp.ShpPolygon;
import org.eclipse.stem.gis.shp.type.Part;

public class GISUtilities {

	private GISUtilities() {
	}

	public static Point2D.Double getCenter(ShpPolygon poly) {
		double largestArea = 0.0;
		Point2D.Double center = null;

		for (Part part : poly.getParts()) {
			int n;
			double[] xs;
			double[] ys;			

			if (part.getXs()[0] == part.getXs()[part.getPointCount() - 1]
					&& part.getYs()[0] == part.getYs()[part.getPointCount() - 1]) {
				n = part.getPointCount();
				xs = part.getXs();
				ys = part.getYs();
			} else {
				n = part.getPointCount() + 1;
				xs = new double[n];
				ys = new double[n];

				for (int i = 0; i < part.getPointCount(); i++) {
					xs[i] = part.getXs()[i];
					ys[i] = part.getYs()[i];
				}

				xs[n - 1] = xs[0];
				ys[n - 1] = ys[0];
			}

			double area = 0.0;
			double cx = 0.0;
			double cy = 0.0;

			for (int i = 0; i < n - 1; i++) {
				double mem = xs[i] * ys[i + 1] - xs[i + 1] * ys[i];

				area += mem;
				cx += (xs[i] + xs[i + 1]) * mem;
				cy += (ys[i] + ys[i + 1]) * mem;
			}

			area /= 2.0;
			cx /= 6 * area;
			cy /= 6 * area;
			area = Math.abs(area);

			if (area > largestArea) {
				largestArea = area;
				center = new Point2D.Double(cx, cy);
			}
		}

		return center;
	}

	public static Rectangle2D.Double getBoundingBox(ShpPolygon poly) {
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

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

		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}

	public static boolean containsPoint(ShpPolygon poly, Point2D.Double point) {
		for (Part part : poly.getParts()) {
			double x = point.x;
			double y = point.y;
			int n;
			double[] xs;
			double[] ys;

			if (part.getXs()[0] == part.getXs()[part.getPointCount() - 1]
					&& part.getYs()[0] == part.getYs()[part.getPointCount() - 1]) {
				n = part.getPointCount();
				xs = part.getXs();
				ys = part.getYs();
			} else {
				n = part.getPointCount() + 1;
				xs = new double[n];
				ys = new double[n];				

				for (int i = 0; i < part.getPointCount(); i++) {
					xs[i] = part.getXs()[i];
					ys[i] = part.getYs()[i];
				}

				xs[n - 1] = xs[0];
				ys[n - 1] = ys[0];
			}

			int hits = 0;
			double x1 = xs[0];
			double y1 = ys[0];

			for (int i = 1; i < n; i++) {
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
						double xProjection = (x2 - x1) / (y2 - y1) * (y - y1)
								+ x1;

						if (x < xProjection) {
							hits++;
						}
					}
				}

				x1 = x2;
				y1 = y2;
			}

			if (hits % 2 != 0) {
				return true;
			}
		}

		return false;
	}

}
