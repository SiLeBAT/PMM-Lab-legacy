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


public class PlateCareeProjection implements Projection
{

	@Override
	public Point2D project(double lon, double lat) 
	{
		return new Point2D.Double(lon,lat);
	}

	@Override
	public Point2D inverseProject(double x, double y) 
	{
		return new Point2D.Double(x,y);
	}

}
