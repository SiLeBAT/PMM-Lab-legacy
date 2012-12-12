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

import java.awt.geom.Point2D;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.io.input.SwappedDataInputStream;
import org.eclipse.stem.gis.coord.CoordinateSystem;
import org.eclipse.stem.gis.shp.type.Box;
import org.eclipse.stem.gis.shp.type.Part;
import org.eclipse.stem.gis.shp.type.Range;

public class ShpInputStream extends SwappedDataInputStream
{
	ShpHeader header;
	CoordinateSystem coordinateSystem;
	
	public ShpInputStream(InputStream input) throws IOException
	{
		this(input,null);
	}
	
	public ShpInputStream(InputStream input, CoordinateSystem cs) throws IOException
	{
		super(input);
		this.coordinateSystem = cs;
		readHeader();
	}
	
	
	private void readHeader() throws IOException
	{
		ShpHeader header = new ShpHeader();
		header.fileCode = EndianUtils.swapInteger(readInt()); // 0
		skipBytes(20); // 4-23 reserved
		header.fileLength = EndianUtils.swapInteger(readInt()); // 24
		header.version = readInt(); // 28
		header.shapeType = readInt(); // 32
		
		header.xyBounds = readBoundingBox(true); // 36-67
		
//		header.xMin = readDouble(); // 36
//		header.yMin = readDouble(); // 44
//		header.xMax = readDouble(); // 52
//		header.yMax = readDouble(); // 60

		
		header.zRange = readRange(); // 68-83
		header.mRange = readRange(); // 84-99

//		header.zMin = readDouble(); // 68
//		header.zMax = readDouble(); // 76
//		header.mMin = readDouble(); // 84
//		header.mMax = readDouble(); // 92
		
		this.header = header;
	}
	
	
	public ShpRecord readNextRecord() throws IOException
	{
		ShpRecord shp = null;
		
		try {
			// Get the record header
			int recordNumber = EndianUtils.swapInteger(readInt()); // 0
			int contentLength = EndianUtils.swapInteger(readInt()); // 4
			// Get the shape type
			int shpType = readInt(); // 8
			
			switch (shpType) {
			case ShpConstants.SHP_NULL_SHAPE: shp = readNullShape(); break;
			case ShpConstants.SHP_POINT: shp = readPoint(); break;
			case ShpConstants.SHP_POINT_M: shp = readPointM(); break;
			case ShpConstants.SHP_POINT_Z: shp = readPointZ(); break;
			case ShpConstants.SHP_MULTI_POINT: shp = readMultiPoint(); break;
			case ShpConstants.SHP_MULTI_POINT_M: shp = readMultiPointM(); break;
			case ShpConstants.SHP_MULTI_POINT_Z: shp = readMultiPointZ(); break;
			case ShpConstants.SHP_POLY_LINE: shp = readPolyLine(); break;
			case ShpConstants.SHP_POLY_LINE_M: shp = readPolyLineM(); break;
			case ShpConstants.SHP_POLY_LINE_Z: shp = readPolyLineZ(); break;
			case ShpConstants.SHP_POLYGON: shp = readPolygon(); break;
			case ShpConstants.SHP_POLYGON_M: shp = readPolygonM(); break;
			case ShpConstants.SHP_POLYGON_Z: shp = readPolygonZ(); break;
			case ShpConstants.SHP_MULTI_PATCH: shp = readMultiPatch(); break;
			default: shp = readUnsupportedShape(contentLength-2); break;
			}
			
			shp.setContentLength(contentLength);
			shp.setRecordNumber(recordNumber);
			
		} catch (EOFException eofe) {
			shp = null;
		}
		
		return shp;
	}

	public ShpHeader getHeader()
	{
		return header;
	}
	
	protected ShpPolyLine readPolyLine(ShpPolyLine shape) throws IOException
	{
		// Read bounding box (32 bytes)
		Box bbox = readBoundingBox(true);
		shape.setBoundingBox(bbox);
		
		// Read the part and point counts (8 bytes)
		int numParts = readInt();
		int numPoints = readInt();
		
		// Read the part boundary indices (numParts * 4 bytes)
		int[] partIndices = readPartBoundaries(numParts);

		int[] partTypes = null;
		if (shape instanceof ShpMultiPatch) {
			// Read the part boundary types (numParts * 4 bytes)
			partTypes = readPartTypes(numParts);
		}
		
		// Read the point values and partition into parts (numPoints * 16 bytes)
		Part[] parts = readParts(numPoints, partIndices, partTypes);
		shape.setParts(parts);
		
		if (shape instanceof ShpPolyLineZ) {
			// Read the Z range (zMin, zMax) - 16 bytes
			((ShpPolyLineZ)shape).setZRange(readRange());
	
			// Read the Z coordinates into the existing coordinates (16 bytes + numPoints * 8 bytes)
			readZCoordinates(parts);			
		}
		
		if (shape instanceof ShpPolyLineM) {
			// Read the measure range (mMin, mMax) - 16 bytes
			((ShpPolyLineM)shape).setMRange(readRange());
			
			// Read the measures into the existing coordinates (numPoints * 8 bytes)
			readMeasures(parts);
		}
		
		return shape;
	}

	protected ShpMultiPoint readMultiPoint(ShpMultiPoint shape) throws IOException
	{
		// Read bounding box (32 bytes)
		shape.setBoundingBox(readBoundingBox(true));
		
		// Read number points (4 bytes)
		int numPoints = readInt();

		// Read the point values (numPoints * 16 bytes)
		Part[] parts = readParts(numPoints, new int[]{0}, null);

		shape.setPart(parts[0]);
		
		if (shape instanceof ShpMultiPointZ) {
			// Read the Z range (zMin, zMax) - 16 bytes
			((ShpMultiPointZ)shape).setZRange(readRange());
			
			// Read the Z coordinates into the existing coordinates (numPoints * 8 bytes)
			readZCoordinates(parts);
		}

		if (shape instanceof ShpMultiPointM) {
			// Read the measure range (mMin, mMax) - 16 bytes
			((ShpMultiPointM)shape).setMRange(readRange());
			
			// Read the measures into the existing coordinates (numPoints * 8 bytes)
			readMeasures(parts);
		}
		
		return shape;
	}
	
	protected ShpPoint readPoint(ShpPoint shape) throws IOException
	{
		// Read X and Y coordinates (16 bytes)
		shape.setPoints(readDouble(), readDouble());
		
		if (shape instanceof ShpPointZ) {
			// Read measure (8 bytes)
			((ShpPointZ)shape).setZ(readDouble());
		}
		
		if (shape instanceof ShpPointM) {
			// Read measure (8 bytes)
			((ShpPointM)shape).setM(readDouble());
		}
		
		return shape;
	}
	
	protected ShpPolygon readPolygon() throws IOException
	{
		return (ShpPolygon)readPolyLine(new ShpPolygon());
	}
	
	protected ShpPolygonM readPolygonM() throws IOException
	{
		return (ShpPolygonM)readPolyLine(new ShpPolygonM());
	}
	
	protected ShpPolygonZ readPolygonZ() throws IOException
	{
		return (ShpPolygonZ)readPolyLine(new ShpPolygonZ());
	}
	
	protected ShpMultiPatch readMultiPatch() throws IOException
	{
		return (ShpMultiPatch)readPolyLine(new ShpMultiPatch());
	}
	
	protected ShpPolyLine readPolyLine() throws IOException
	{
		return readPolyLine(new ShpPolyLine());
	}
	
	protected ShpPolyLineM readPolyLineM() throws IOException
	{
		return (ShpPolyLineM)readPolyLine(new ShpPolyLineM());
	}
	
	protected ShpPolyLineZ readPolyLineZ() throws IOException
	{
		return (ShpPolyLineZ)readPolyLine(new ShpPolyLineZ());
	}
	
	protected ShpMultiPoint readMultiPoint() throws IOException
	{
		return readMultiPoint(new ShpMultiPoint());
	}
	
	protected ShpMultiPointM readMultiPointM() throws IOException
	{
		return (ShpMultiPointM)readMultiPoint(new ShpMultiPointM());
	}	
	
	protected ShpMultiPointZ readMultiPointZ() throws IOException
	{
		return (ShpMultiPointZ)readMultiPoint(new ShpMultiPointZ());
	}
	
	protected ShpPoint readPoint() throws IOException
	{
		return readPoint(new ShpPoint());
	}
	
	protected ShpPointM readPointM() throws IOException
	{
		return (ShpPointM)readPoint(new ShpPointM());
	}
	
	protected ShpPointZ readPointZ() throws IOException
	{
		return (ShpPointZ)readPoint(new ShpPointZ());
	}
	
	protected ShpUnsupportedShape readUnsupportedShape(int words) throws IOException
	{
		skipBytes(words*2);
		
		return new ShpUnsupportedShape();
	}
	
	protected ShpNullShape readNullShape()
	{
		return new ShpNullShape();
	}
	
	protected int[] readIntArray(int size) throws IOException
	{
		int[] idxs = new int[size];
		for (int i=0; i<size; i++) {
			idxs[i] = readInt();
		}
		return idxs;
	}
	
	protected double[][] readAndWeavePoints(int rows, int pointCount) throws IOException
	{
		double[][] points = new double[rows][];

		int columns = pointCount; 
		for (int idx=0; idx<points.length; idx++) {
			points[idx] = new double[columns];
		}
		
		for (int column = 0; column < columns; column++) {
			for (int row = 0; row < rows; row+=2) {
				
				Point2D pt = readPointAndProject();
				points[row][column] = pt.getX();
				points[row+1][column] = pt.getY();
						
				//points[row][column] = readDouble();
			}
			
		}

		return points;
	}
	
	
	protected Point2D transform(double x, double y)
	{
		if (coordinateSystem != null) {
			return coordinateSystem.inverseProject(x, y);
		} else {
			return new Point2D.Double(x,y);
		}
	}
	
	protected Point2D readPointAndProject() throws IOException
	{
		double lon = readDouble();
		double lat = readDouble();
		
		return transform(lon,lat);
	}
	
	protected int[] readPartBoundaries(int partCount) throws IOException
	{
		return readIntArray(partCount);
	}
	
	protected int[] readPartTypes(int partCount) throws IOException
	{
		return readIntArray(partCount);
	}
	
	protected Part[] readParts(int pointCount, int[] partBoundaries, int[] partTypes) throws IOException
	{
		Part[] parts = new Part[partBoundaries.length];
		
		int partType = ShpConstants.SHP_PART_RING;
		for (int idx=0; idx<partBoundaries.length; idx++) {
			if (partTypes != null) {
				partType = partTypes[idx];
			}
			parts[idx] = new Part(partType,
					readAndWeavePoints(2, getPointCountForPart(idx, pointCount, partBoundaries)));
		}

		return parts;
	}
	
	protected Range readRange() throws IOException
	{
		double min = readDouble();
		double max = readDouble();
		
		return new Range(min,max);
	}
	
	protected Box readBoundingBox(boolean transform) throws IOException
	{
		// Bounding Box (4 * 8 bytes)
		double xMin = readDouble();
		double yMin = readDouble();
		double xMax = readDouble();
		double yMax = readDouble();
		
		if (transform && coordinateSystem != null) {
			Point2D xyMin = transform(xMin,yMin);
			Point2D xyMax = transform(xMax,yMax);

			xMin = xyMin.getX();
			yMin = xyMin.getY();
			xMax = xyMax.getX();
			yMax = xyMax.getY();
		}
		
		return new Box(xMin,yMin,xMax,yMax);
	}
	
	protected void readMeasures(Part[] parts) throws IOException
	{
		for (int idx=0; idx<parts.length; idx++) {
			double[][] pts = readAndWeavePoints(1, parts[idx].getPointCount());
			parts[idx].setMs(pts[0]);
		}
	}

	protected void readZCoordinates(Part[] parts) throws IOException
	{
		for (int idx=0; idx<parts.length; idx++) {
			double[][] pts = readAndWeavePoints(1, parts[idx].getPointCount());
			parts[idx].setZs(pts[0]);
		}
	}
	
	protected int getPointCountForPart(int currentIdx, int pointCount, int[] partBoundaries)
	{
		if (currentIdx+1 < partBoundaries.length) {
			return partBoundaries[currentIdx+1]-partBoundaries[currentIdx];
		} else {
			return pointCount-partBoundaries[currentIdx];
		}
	}
	
	
}
