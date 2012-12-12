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
package org.eclipse.stem.gis.shp.type;

public class Box 
{
	protected final double xMin;
	protected final double yMin;
	protected final double xMax;
	protected final double yMax;
	
	public Box(double xMin, double yMin, double xMax, double yMax)
	{
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}
	
	public double getXMin() 
	{
		return xMin;
	}

	public double getYMin() 
	{
		return yMin;
	}

	public double getXMax() 
	{
		return xMax;
	}

	public double getYMax() 
	{
		return yMax;
	}

	@Override
	public String toString() {
		return "Box [xMin=" + xMin + ", yMin=" + yMin + ", xMax=" + xMax
				+ ", yMax=" + yMax + "]";
	}
	
	
}
