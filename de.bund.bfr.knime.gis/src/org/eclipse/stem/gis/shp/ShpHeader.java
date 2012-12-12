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
import org.eclipse.stem.gis.shp.type.Range;

public class ShpHeader
{
	int fileCode;
	int fileLength;
	int version;
	int shapeType;
	
	Box xyBounds;
	Range zRange;
	Range mRange;
	
	public int getFileCode() {
		return fileCode;
	}

	public int getFileLength() {
		return fileLength;
	}

	public int getVersion() {
		return version;
	}

	public int getShapeType() {
		return shapeType;
	}

	public Box getXYBoundingBox() {
		return xyBounds;
	}
	
	public Range getZRange() {
		return zRange;
	}

	public Range getMRange() {
		return mRange;
	}

	@Override
	public String toString() {
		return "ShpHeader [fileCode=" + fileCode + ", fileLength=" + fileLength
				+ ", version=" + version + ", shapeType=" + shapeType
				+ ", xyBounds=" + xyBounds + ", zRange=" + zRange + ", mRange="
				+ mRange + "]";
	}
}