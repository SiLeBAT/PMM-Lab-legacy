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

public class ShpPointZ extends ShpPointM
{
	protected double z;
	
	ShpPointZ()
	{
		this(0.0,0.0,0.0,0.0);
	}
	
	public ShpPointZ(double x, double y, double m, double z) 
	{
		super(x,y,m);
	}
	
	void setZ(double z)
	{
		this.z = z;
	}
	
	public double getZ()
	{
		return z;
	}
	
	public int getType()
	{
		return ShpConstants.SHP_POINT_Z;
	}
	
}
