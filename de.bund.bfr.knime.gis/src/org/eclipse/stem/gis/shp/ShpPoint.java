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

public class ShpPoint extends ShpRecord
{

	protected double x;
	protected double y;

	ShpPoint()
	{
		this(0.0, 0.0);
	}
	
	public ShpPoint(double x, double y)
	{
		super();
		setPoints(x,y);

	}
	
	void setPoints(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}

	public int getType()
	{
		return ShpConstants.SHP_POINT;
	}
	
//	@Override
//	public String toString() {
//		return "ShpPoint [point=" + point + "]";
//	}
	
}
