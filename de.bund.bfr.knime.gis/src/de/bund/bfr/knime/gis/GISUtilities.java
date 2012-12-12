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

import org.eclipse.stem.gis.shp.ShpPolygon;
import org.eclipse.stem.gis.shp.type.Part;

public class GISUtilities {

	private GISUtilities() {
	}

	public static Point2D.Double getCenter(ShpPolygon poly) {
		double largestArea = 0.0;
		Point2D.Double center = null;

		for (Part part : poly.getParts()) {
			double[] x;
			double[] y;
			int npoints;

			if (part.getXs()[0] == part.getXs()[part.getPointCount() - 1]
					&& part.getYs()[0] == part.getYs()[part.getPointCount() - 1]) {
				npoints = part.getPointCount();
				x = part.getXs();
				y = part.getYs();
			} else {
				npoints = part.getPointCount() + 1;
				x = new double[npoints];
				y = new double[npoints];

				for (int i = 0; i < part.getPointCount(); i++) {
					x[i] = part.getXs()[i];
					y[i] = part.getYs()[i];
				}

				x[npoints - 1] = x[0];
				y[npoints - 1] = y[0];
			}

			double area = 0.0;
			double cx = 0.0;
			double cy = 0.0;

			for (int i = 0; i < npoints - 1; i++) {
				double mem = x[i] * y[i + 1] - x[i + 1] * y[i];

				area += mem;
				cx += (x[i] + x[i + 1]) * mem;
				cy += (y[i] + y[i + 1]) * mem;
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

	public static boolean containsPoint(ShpPolygon poly, Point2D.Double point) {
		for (Part part : poly.getParts()) {
			int n = part.getPointCount();
			double[] xs = part.getXs();
			double[] ys = part.getYs();
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
