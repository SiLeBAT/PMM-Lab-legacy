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

import org.eclipse.stem.gis.shp.type.Box;
import org.eclipse.stem.gis.shp.type.Part;


public class ShpMultiPoint extends ShpRecord
{
	protected Box boundingBox;
	protected Part part;
	
	ShpMultiPoint()
	{
		this(null,null);
	}
	
	public ShpMultiPoint(Box boundingBox, Part part) 
	{
		super();
		this.boundingBox = boundingBox;
		this.part = part;
	}

	void setPart(Part part)
	{
		this.part = part;
	}
	
	void setBoundingBox(Box boundingBox) 
	{
		this.boundingBox = boundingBox;
	}

	public Box getBoundingBox() 
	{
		return boundingBox;
	}
	
	public double[] getXs()
	{
		return part.getXs();
	}
	
	public double[] getYs()
	{
		return part.getYs();
	}
	
	
	public int getType()
	{
		return ShpConstants.SHP_MULTI_POINT;
	}
	
}
