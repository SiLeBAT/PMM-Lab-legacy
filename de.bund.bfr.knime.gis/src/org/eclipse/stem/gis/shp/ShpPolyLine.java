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

public class ShpPolyLine extends ShpRecord
{
	protected Box boundingBox;
	protected Part[] parts;
	
	
	ShpPolyLine()
	{
		this(null,null);
	}
	
	public ShpPolyLine(Box boundingBox, Part[] parts) 
	{
		super();
		this.boundingBox = boundingBox;
		this.parts = parts;
	}
	
	void setBoundingBox(Box boundingBox) {
		this.boundingBox = boundingBox;
	}

	void setParts(Part[] parts) {
		this.parts = parts;
	}

	public Box getBoundingBox() {
		return boundingBox;
	}

	public Part[] getParts() {
		return parts;
	}
	
	public int getType()
	{
		return ShpConstants.SHP_POLY_LINE;
	}
	

//	protected String partsToString()
//	{
//		StringBuilder sb = new StringBuilder();
//		for (int i=0; i<parts.length; i++) {
//			sb.append("Part ["+i+"]:");
//			Point[] points = parts[i].getPoints();
//			for (int j=0; j<points.length; j++) {
//				sb.append("("+points[j].getX()+","+points[j].getY()+"),");
//			}
//			sb.append("\n");
//		}
//		
//		return sb.toString();
//		
//	}
//
//	@Override
//	public String toString() {
//		final int maxLen = 10;
//		
//		String partStrings = partsToString();
//		
//		return "ShpPolyLine [boundingBox="
//				+ boundingBox
//				+ ", parts=\n"
//				+ partStrings
//				+ ", recordNumber=" + recordNumber + ", contentLength="
//				+ contentLength + ", shpType=" + shpType + "]";
//	}
	
	
}
