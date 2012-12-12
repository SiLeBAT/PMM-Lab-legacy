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
import org.eclipse.stem.gis.shp.type.Range;


public class ShpMultiPointZ extends ShpMultiPointM
{
	protected Range zRange;
	
	ShpMultiPointZ()
	{
		this(null,null,null,null);
	}
	
	public ShpMultiPointZ(Box boundingBox, Part part, Range mRange, Range zRange) 
	{
		super(boundingBox, part, mRange);
		this.zRange = zRange;
	}

	public Range getZRange()
	{
		return mRange;
	}
	
	public double[] getZs()
	{
		return part.getZs();
	}
	
	void setZRange(Range zRange) 
	{
		this.zRange = zRange;
	}

	
	public int getType()
	{
		return ShpConstants.SHP_MULTI_POINT_Z;
	}
}
