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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Miguel Alba
 */
public class ModelVariableTest {
	
	private ModelVariable temperature = new ModelVariable("Temperature", 10.0);
	private ModelVariable pH = new ModelVariable("pH", 5.63);

	@Test
	public void testGetters() {
		assertEquals("Temperature", temperature.getName());
		assertEquals(10.0, temperature.getValue(), 0.01);
		
		assertEquals("pH", pH.getName());
		assertEquals(5.63, pH.getValue(), 0.01);
	}
	
	@Test
	public void testEquals() {
		assertEquals(temperature, temperature);
		assertFalse(temperature.equals(pH));
	}

	@Test
	public void testToString() {
		ModelVariable aw = new ModelVariable("Water activity", null);
		assertEquals(aw.toString(), "ModelVariable [name=Water activity]");
		
		assertEquals(pH.toString(), "ModelVariable [name=pH, value=5.630000]");
	}
}
