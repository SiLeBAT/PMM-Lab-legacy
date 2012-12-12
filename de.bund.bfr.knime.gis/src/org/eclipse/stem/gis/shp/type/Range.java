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

public class Range
{
	protected final double min;
	protected final double max;
	
	public Range(double min, double max)
	{
		this.min = min;
		this.max = max;
	}

	@Override
	public String toString() 
	{
		return "Range [x=" + min + ", y=" + max + "]";
	}

	public double getMin() 
	{
		return min;
	}

	public double getMax() 
	{
		return max;
	}
	
	
}
