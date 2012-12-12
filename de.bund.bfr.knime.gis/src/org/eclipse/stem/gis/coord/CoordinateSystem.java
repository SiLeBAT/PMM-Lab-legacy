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
package org.eclipse.stem.gis.coord;

import java.awt.geom.Point2D;

import org.eclipse.stem.gis.proj.Projection;

public class CoordinateSystem 
{
	protected Projection projection;
	protected String name;
	
	
	public CoordinateSystem(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	void setProjection(Projection projection)
	{
		this.projection = projection;
	}
	
	public Projection getProjection()
	{
		return projection;
	}
	
	public Point2D project(double lon, double lat)
	{
		if (projection != null) {
			return projection.project(lon,lat);
		} else {
			return new Point2D.Double(lon,lat);
		}
	}
	
	public Point2D inverseProject(double x, double y)
	{
		if (projection != null) {
			return projection.inverseProject(x,y);
		} else {
			return new Point2D.Double(x,y);
		}
	}
}
