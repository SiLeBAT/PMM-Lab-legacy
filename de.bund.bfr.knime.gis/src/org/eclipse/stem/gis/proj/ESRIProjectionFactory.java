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

import java.util.Properties;

import org.eclipse.stem.gis.Activator;
import org.eclipse.stem.gis.coord.ProjectedCoordinateSystem;

public class ESRIProjectionFactory 
{
	public static Projection getForCoordinateSystem(ProjectedCoordinateSystem cs)
	{
		if ("Transverse_Mercator".equals(cs.getProjectionName())) {
			return getTransverseMercatorProjection(cs);
		}
		
		Activator.logWarning("Warning: Projection "+cs.getProjectionName()+" in coordinate system "+ cs.getName() +" is not supported.", null);
		
		return new NullProjection();
	}
	
	private static Projection getTransverseMercatorProjection(ProjectedCoordinateSystem cs)
	{
		Properties projectionProperties = cs.getProperties();
		
		double falseNorthing = Double.valueOf(projectionProperties.getProperty("False_Northing", "0.0"));
		double falseEasting = Double.valueOf(projectionProperties.getProperty("False_Easting", "0.0"));
		double scale = Double.valueOf(projectionProperties.getProperty("Scale_Factor", "0.0"));
		double centralMeridianLongitude = Double.valueOf(projectionProperties.getProperty("Central_Meridian", "0.0"));
		//double originLatitude = Double.valueOf(projectionProperties.getProperty("Latitude_Of_Origin", "0.0"));

		return new TransverseMercatorProjection(falseEasting, falseNorthing, centralMeridianLongitude, scale, cs.getGeoCoordinateSystem().getDatum());
	}
		
}
