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

import org.eclipse.stem.gis.coord.Ellipsoid;

public class UTMProjection extends TransverseMercatorProjection 
{
	public static final double DEFAULT_FALSE_EASTING = 500000.0;
	public static final double DEFAULT_SCALE = 0.9996;
	
	public UTMProjection(int utmZoneNumber, boolean isNorthernHemisphere,
			Ellipsoid datum) {
		super(DEFAULT_FALSE_EASTING, // default false easting
				(isNorthernHemisphere ? 0.0 : 10000000.0), // default false northing
				getMeridianOffsetForUTMZone(utmZoneNumber), // central merdian offset
				DEFAULT_SCALE, // default scale
				datum);
	}

	
	public static double getMeridianOffsetForUTMZone(int zoneNumber)
	{
		return ((zoneNumber - 1) * 6 - 177);
	}
	

//	public static void main(String[] args)
//	{
//		UTMProjection proj = new UTMProjection(32, true, Ellipsoid.getDefaultEllipsoid());
//		System.out.println(proj.inverseProject(595000.0, 5740000.0));
//		System.out.println(proj.project(10.377804512727295, 51.802771341166256));
//	}
}
