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
package org.eclipse.stem.gis.shp;

public class ShpPointM extends ShpPoint
{
	protected double m;
	
	ShpPointM()
	{
		this(0.0, 0.0, 0.0);
	}

	public ShpPointM(double x, double y, double m) 
	{
		super(x,y);
	}
	
	void setM(double m)
	{
		this.m = m;
	}
	
	public double getM()
	{
		return m;
	}
	
	public int getType()
	{
		return ShpConstants.SHP_POINT_M;
	}
}
