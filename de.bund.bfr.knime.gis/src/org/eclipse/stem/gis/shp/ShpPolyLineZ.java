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

public class ShpPolyLineZ extends ShpPolyLineM
{
	protected Range zRange;

	ShpPolyLineZ()
	{
		this(null,null,null,null);
	}
	
	public ShpPolyLineZ(Box boundingBox,Part[] parts, Range mRange, Range zRange)
	{
		super(boundingBox, parts, mRange);
		this.zRange = zRange;
	}

	public Range getZRange() {
		return zRange;
	}
	
	void setZRange(Range zRange) {
		this.zRange = zRange;
	}


	public int getType()
	{
		return ShpConstants.SHP_POLY_LINE_Z;
	}
	
}
