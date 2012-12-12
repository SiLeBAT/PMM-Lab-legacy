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
package org.eclipse.stem.gis.coord;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import org.eclipse.stem.gis.GisUtils;
import org.eclipse.stem.gis.proj.ESRIProjectionFactory;

public class CoordinateSystemLoader 
{

	public static CoordinateSystem loadFromPrj(File file) throws IOException
	{
		InputStream is = null;
		try {
			is = new BufferedInputStream(
					new FileInputStream(file));
			return loadFromPrj(is);
		} finally {
			GisUtils.safeClose(is);
		}
	}
	
	public static CoordinateSystem loadFromPrj(InputStream is) throws IOException
	{
		PrjParser parser = new PrjParser(is);
		CoordinateSystemParseTree parseResults = parser.parse();
		return loadFromParser(parseResults);
	}
	
	
	private static CoordinateSystem loadFromParser(CoordinateSystemParseTree node)
	{
		if (node.getValue().equals("GEOGCS")) {
			return parseGeographicCoordinateSystem(node);
		} else if (node.getValue().equals("PROJCS")) {
			return parseProjectedCoordinateSystem(node);
		}
		return null;
	}
	
	private static Ellipsoid parseSpheroid(CoordinateSystemParseTree node)
	{
		String name = node.getChildren().get(0).getValue();
		double majorAxis = Double.valueOf(node.getChildren().get(1).getValue());
		double flattening = Double.valueOf(node.getChildren().get(2).getValue());
		
		Ellipsoid retVal = Ellipsoid.getForName(name);
		if (retVal == null) {
			retVal = new Ellipsoid(majorAxis, flattening);
		}
		
		return retVal;
	}
	
	private static Ellipsoid parseDatum(CoordinateSystemParseTree node)
	{
		Ellipsoid retVal = null;
		for (CoordinateSystemParseTree child : node.getChildren()) {
			if (child.getValue().equals("SPHEROID")) {
				retVal = parseSpheroid(child);
			}
		}
		
		
		return retVal;
	}
	
	private static ProjectedCoordinateSystem parseProjectedCoordinateSystem(CoordinateSystemParseTree node)
	{
		String name = node.getChildren().get(0).getValue();
		String projectionName = null;
		Properties props = new Properties();
		double unitScalar = 0.0;
		
		GeographicCoordinateSystem gcs = null;
		for (CoordinateSystemParseTree child : node.getChildren()) {
			if (child.getValue().equals("GEOGCS")) {
				gcs = parseGeographicCoordinateSystem(child);
			} else if (child.getValue().equals("PROJECTION")) {
				projectionName = child.getChildren().get(0).getValue();
			} else if (child.getValue().equals("UNIT")) {
				unitScalar = Double.valueOf(child.children.get(1).getValue());
			} else if (child.getValue().equals("PARAMETER")) {
				String paramName = child.getChildren().get(0).getValue();
				String paramValue = child.getChildren().get(1).getValue();
				
				props.put(paramName, paramValue);
			}
			
		}

		ProjectedCoordinateSystem projCS = new ProjectedCoordinateSystem(name,gcs, projectionName, unitScalar, props);
		projCS.setProjection(ESRIProjectionFactory.getForCoordinateSystem(projCS));
		
		return projCS;	
	}

	
	
	private static GeographicCoordinateSystem parseGeographicCoordinateSystem(CoordinateSystemParseTree node)
	{
		String name = node.getChildren().get(0).getValue();
		double primeMeridanOffset = 0.0;
		double unitScalar = 0.0;
		Ellipsoid datum = null;	
		
		for (CoordinateSystemParseTree child : node.getChildren()) {
			if (child.getValue().equals("DATUM")) {
				datum = parseDatum(child); 
			} else if (child.getValue().equals("PRIMEM")) {
				primeMeridanOffset = Double.valueOf(child.getChildren().get(1).getValue());
			} else if (child.getValue().equals("UNIT")) {
				unitScalar = Double.valueOf(child.getChildren().get(1).getValue());
			}
		}

		return new GeographicCoordinateSystem(name, datum, primeMeridanOffset, unitScalar);
	}
	

	/**
	 * Generates a parse tree representing an ESRI PRJ projection / coordinate
	 * system definition file.
	 *
	 */
	private static class PrjParser
	{
		private String inputData;
		
		public PrjParser(InputStream is) throws IOException
		{
			read(is);
			parse();
		}
		
		public CoordinateSystemParseTree parse()
		{
			return getParseTree();
		}

		private CoordinateSystemParseTree getParseTree()
		{
			String data = inputData;
			
			Stack<CoordinateSystemParseTree> history = new Stack<CoordinateSystemParseTree>();
			CoordinateSystemParseTree currentNode = null;
			
			StringBuilder currentFieldData = new StringBuilder();
			for (int idx=0; idx<data.length(); idx++) {
				char c = data.charAt(idx);
				if (c == '[') {
					if (currentNode != null) {
						history.push(currentNode);
					}
					
					currentNode = new CoordinateSystemParseTree(currentFieldData.toString());					
					currentFieldData = new StringBuilder();
				} else if (c == ']') {
					if (currentFieldData.length() > 0) {
						currentNode.addChild(cleanup(currentFieldData.toString()));
					}
					if (history.size() > 0) {
						CoordinateSystemParseTree tmp = history.pop();
						tmp.addChild(currentNode);
						currentNode = tmp;
					}
					currentFieldData = new StringBuilder();
					
				} else if (c == ',') {
					if (currentFieldData.length() > 0) {
						currentNode.addChild(cleanup(currentFieldData.toString()));
					}
					currentFieldData = new StringBuilder();
				} else {
					currentFieldData.append(c);
				}
			}

			return currentNode;
		}
		
		private String cleanup(String str)
		{
			return str.replaceAll("\"", "");
		}
		
		private void read(InputStream is) throws IOException
		{
			StringBuilder sb = new StringBuilder();
			
			byte[] buf = new byte[1024];
			int read = 0;
			
			while ((read = is.read(buf)) > 0) {
				sb.append(new String(buf, 0, read));
			}
			
			inputData = sb.toString().trim();
		}

	}
	
	private static class CoordinateSystemParseTree
	{
		private String value;
		private List<CoordinateSystemParseTree> children;
		
		CoordinateSystemParseTree(String name)
		{
			this.value = name;
		}
		void addChild(CoordinateSystemParseTree node)
		{
			if (children == null) {
				children = new ArrayList<CoordinateSystemParseTree>();
			}
			children.add(node);
		}
		void addChild(String name)
		{
			addChild(new CoordinateSystemParseTree(name));
		}
		String getValue()
		{
			return value;
		}
		List<CoordinateSystemParseTree> getChildren()
		{
			return children;
		}
		
	}
	
}
