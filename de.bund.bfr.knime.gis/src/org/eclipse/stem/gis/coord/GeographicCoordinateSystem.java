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

public class GeographicCoordinateSystem extends CoordinateSystem
{
	private Ellipsoid datum;
	private double primeMerdianOffset;
	private double unitScalar;
	
	public GeographicCoordinateSystem(String name, Ellipsoid geodesy,
			double primeMerdianOffset, double unitScalar) {
		super(name);
		this.datum = geodesy;
		this.primeMerdianOffset = primeMerdianOffset;
		this.unitScalar = unitScalar;
	}

	public Ellipsoid getDatum() {
		return datum;
	}

	public double getPrimeMerdianOffset() {
		return primeMerdianOffset;
	}

	public double getUnitScalar() {
		return unitScalar;
	}

	@Override
	public String toString() {
		return "GeographicCoordinateSystem [name=" + name + ", datum="
				+ datum + ", primeMerdianOffset=" + primeMerdianOffset
				+ ", unitScalar=" + unitScalar + "]";
	}
	

}
