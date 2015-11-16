/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.pmf.sbml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sbml.jsbml.Unit;

/**
 * @author Miguel Alba
 *
 */
public class PMFUnitTest {
	private final double multiplier = 1;
	private final int scale = 0;
	private final Unit.Kind kind = Unit.Kind.ITEM;
	private final double exponent = 1;

	@Test
	public void testMultiplierAccesors() {
		PMFUnit unit = new PMFUnit(multiplier, scale, kind, exponent);
		assertEquals(unit.getMultiplier(), multiplier, 0.0);
		
		unit.setMultiplier(0);
		assertEquals(unit.getMultiplier(), 0, 0.0);
	}
	
	@Test
	public void testScaleAccesors() {
		PMFUnit unit = new PMFUnit(multiplier, scale, kind, exponent);
		assertEquals(unit.getScale(), scale, 0.0);
		
		unit.setScale(0);
		assertEquals(unit.getScale(), 0);
	}
	
	@Test
	public void testKindAccesors() {
		PMFUnit unit = new PMFUnit(multiplier, scale, kind, exponent);
		assertEquals(unit.getKind(), kind);
		
		unit.setKind(Unit.Kind.DIMENSIONLESS);
		assertEquals(unit.getKind(), Unit.Kind.DIMENSIONLESS);
	}
	
	@Test
	public void testExponentAccesors() {
		PMFUnit unit = new PMFUnit(multiplier, scale, kind, exponent);
		assertEquals(unit.getExponent(), exponent, 0.0);
		
		unit.setExponent(2.0);
		assertEquals(unit.getExponent(), 2.0, 0.0);
	}
	
	@Test
	public void testToString() {
		String expectedString = "unit [exponent=1.000000, kind=ITEM, multiplier=1.000000, scale=0]";
		String obtainedString = new PMFUnit(multiplier, scale, kind, exponent).toString();
		assertEquals(expectedString, obtainedString);
	}
}
