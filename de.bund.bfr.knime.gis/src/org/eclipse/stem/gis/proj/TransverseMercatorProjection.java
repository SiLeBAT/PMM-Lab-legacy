/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.stem.gis.proj;

import java.awt.geom.Point2D;

import org.eclipse.stem.gis.coord.Ellipsoid;

/**
 * Implementation of the Transverse Mercator projection for ellipsoids.
 * 
 * Formulas are derived from United States Geological Survey (USGS)
 * Professional Paper 1395.
 * 
 * http://pubs.er.usgs.gov/djvu/PP/PP_1395.pdf
 * 
 */
public class TransverseMercatorProjection implements Projection 
{
	protected Ellipsoid datum;
	protected double falseEasting = 0.0;
	protected double falseNorthing = 0.0;
	protected double centralMeridianLongitude = 0.0;
	protected double k0;

	private double majorAxisRadius;
	private double eccentricitySquared;
	private double eccentricityPrimeSquared;
	private double e1;

	/**
	 * @param falseEasting
	 * @param falseNorthing
	 * @param centralMeridianLongitude
	 * @param k0
	 * @param datum
	 */
	public TransverseMercatorProjection(double falseEasting, double falseNorthing,
			double centralMeridianLongitude, double k0, Ellipsoid datum) {
		this.datum = datum;
		this.falseEasting = falseEasting;
		this.falseNorthing = falseNorthing;
		this.centralMeridianLongitude = centralMeridianLongitude;
		this.k0 = k0;

		precalculate();
	}

	protected void precalculate() {
		if (datum == null) {
			datum = Ellipsoid.getDefaultEllipsoid();
		}

		this.majorAxisRadius = datum.getMajorAxis();
		this.eccentricitySquared = datum.getEccentricitySquared();
		this.eccentricityPrimeSquared = (eccentricitySquared) / (1.0 - eccentricitySquared);

		this.e1 = (1.0 - Math.sqrt(1.0 - eccentricitySquared))
				/ (1.0 + Math.sqrt(1.0 - eccentricitySquared));
	}
	
	public Point2D inverseProject(double easting, double northing) 
	{
		double x = easting - falseEasting;
		double y = northing - falseNorthing;
		
		double a = this.majorAxisRadius;
		double ep2 = this.eccentricityPrimeSquared;
		double e2 = this.eccentricitySquared;
		double k0 = this.k0;
		double e1 = this.e1;
		
		double M = y/k0;
		double mu = M / (a * (1.0 - e2/4.0 - 3.0*e2*e2/64.0 - 5.0*e2*e2*e2/256.0));
		
		double phi1 = mu 
				+ (3.0*e1/2.0 - 27.0*e1*e1*e1/32.0) * Math.sin(2.0*mu) 
				+ (21.0*e1*e1/16.0 - 55.0*e1*e1*e1*e1/32.0) * Math.sin(4.0*mu)
				+ (151.0*e1*e1*e1/96.0) * Math.sin(6.*mu)
				+ (1097.0*e1*e1*e1*e1/512.0) * Math.sin(8.0*mu);

		double phi1tan = Math.tan(phi1);
		double phi1cos = Math.cos(phi1);
		double phi1sin = Math.sin(phi1);
		
		double C1 = ep2 * phi1cos * phi1cos;
		double N1 = a / Math.sqrt(1.0 - e2*phi1sin*phi1sin);
		double R1 = a*(1.0 - e2)/Math.pow(1.0 - e2*phi1sin*phi1sin,1.5);
		double T1 = phi1tan * phi1tan;
		double D = x / (N1 * k0);

		double phi = phi1 - 
				(N1 * phi1tan/R1) *
				(D*D/2.0 
				  - (5.0 + 3.0*T1 + 10.0*C1 - 4.0*C1*C1 - 9.0*ep2)*D*D*D*D/24.0
				  + (61.0 + 90.0*T1 + 298.0*C1 + 45.0*T1*T1 - 252.0*ep2 - 3.0*C1*C1)*D*D*D*D*D*D/720.0
				);
		
		double lambda =
				(D 
				- (1.0 + 2.0*T1 + C1)*D*D*D/6.0
				+ (5.0 - 2.0*C1 + 28.0*T1 - 3.0*C1*C1 + 8.0*ep2 + 24.0*T1*T1)*D*D*D*D*D / 120.0
				) / phi1cos;
		
		
		lambda = Math.toDegrees(lambda) + centralMeridianLongitude;
		phi = Math.toDegrees(phi);
		
		return new Point2D.Double(lambda,phi);

	}


	@Override
	public Point2D project(double lon, double lat) 
	{
		double phi = Math.toRadians(lat);
		double lambda = Math.toRadians(lon);
		double lambda0 = Math.toRadians(centralMeridianLongitude);

		double a = majorAxisRadius;
		double e2 = eccentricitySquared;
		double ep2 = eccentricityPrimeSquared;
		double k0 = this.k0;

		double N = a / Math.sqrt(1.0 - e2 * Math.sin(phi)*Math.sin(phi));
		double A = (lambda - lambda0) * Math.cos(phi);
		double T = Math.tan(phi) * Math.tan(phi);
		double C = ep2 * Math.cos(phi) * Math.cos(phi);
		double M = a * (
				  (1.0 - e2/4.0 - 3.0*e2*e2/64.0 - 5.0*e2*e2*e2/256.0) * phi
				- (3.0*e2/8 + 3.0*e2*e2/32.0 + 45.0*e2*e2*e2/1024.0) * Math.sin(2.0*phi)
				+ (15.0*e2*e2/256.0 + 45.0*e2*e2*e2/1024.0) * Math.sin(4.0*phi)
				- (35.0*e2*e2*e2/3072.0) * Math.sin(6.0*phi)
				);
		
		
		double x = k0*N*(A + (1.0-T+C)*A*A*A/6.0 + 
						(5.0-18.0*T + T*T + 72.0*C - 58.0*ep2*ep2)*A*A*A*A*A/120.0);
		
		double y =  k0 * (M + N*Math.tan(phi) * 
				  ( A*A/2.0 + (5.0 - T + 9.0*C + 4.0*C*C)*A*A*A*A/24.0 +
					(61.0 - 58.0*T*T*T + 600.0*C - 300.0*ep2*ep2)*A*A*A*A*A*A/720.0
				  ));
		
		x = Math.rint(x+falseEasting);
		y = Math.rint(y+falseNorthing);
		
		return new Point2D.Double(x, y);
	}

	
}
