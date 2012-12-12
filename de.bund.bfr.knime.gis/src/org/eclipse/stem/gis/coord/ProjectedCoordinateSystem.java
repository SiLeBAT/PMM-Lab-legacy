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

import java.util.Properties;

public class ProjectedCoordinateSystem extends CoordinateSystem
{
	private GeographicCoordinateSystem gcs;
	private String projectionName;
	private Properties props;
	private double unitScalar;
	
	public ProjectedCoordinateSystem(String name, GeographicCoordinateSystem gcs,
			String projectionName, double unitScalar,
			 Properties props) {
		super(name);
		this.gcs = gcs;
		this.name = name;
		this.projectionName = projectionName;
		this.props = props;
		this.unitScalar = unitScalar;
	}

	public GeographicCoordinateSystem getGeoCoordinateSystem() {
		return gcs;
	}

	public String getProjectionName() {
		return projectionName;
	}
	
	public Properties getProperties() {
		return props;
	}

	public double getUnitScalar() {
		return unitScalar;
	}

	@Override
	public String toString() {
		return "ProjectedCoordinateSystem [name=" + name + ", gcs=" + gcs
				+ ", projectionName=" + projectionName + ", props=" + props
				+ ", unitScalar=" + unitScalar + "]";
	}
	

	
}
