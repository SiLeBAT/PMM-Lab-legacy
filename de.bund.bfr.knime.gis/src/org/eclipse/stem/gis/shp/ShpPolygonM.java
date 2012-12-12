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

public class ShpPolygonM extends ShpPolyLineM
{
	ShpPolygonM()
	{
		this(null,null,null);
	}
	public ShpPolygonM(Box boundingBox, Part[] parts, Range mRange)
	{
		super(boundingBox, parts, mRange);
	}
	
	public int getType()
	{
		return ShpConstants.SHP_POLYGON_M;
	}
	
}
