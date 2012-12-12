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


public class ShpMultiPointM extends ShpMultiPoint
{
	protected Range mRange;
	
	ShpMultiPointM()
	{
		this(null,null,null);
	}
	
	public ShpMultiPointM(Box boundingBox, Part part, Range mRange) 
	{
		super(boundingBox, part);
		this.mRange = mRange;
	}
	
	public double[] getMs()
	{
		return part.getMs();
	}
	
	public Range getMRange()
	{
		return mRange;
	}
		
	void setMRange(Range mRange) 
	{
		this.mRange = mRange;
	}

	public int getType()
	{
		return ShpConstants.SHP_MULTI_POINT_M;
	}
}
