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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Miguel Alba
 */
public class LimitsTest {
	
	private Limits limits = new Limits("Temperature", 65.0, 80.0);

	@Test
	public void testAccesors() {
		assertEquals(limits.getVar(), "Temperature");
		assertTrue(Double.compare(limits.getMin(), 65.0) == 0);
		assertTrue(Double.compare(limits.getMax(), 80.0) == 0);
	}
	
	@Test
	public void testToString() {
		String expectedString = "Limits [var=Temperature, min=65.000000, max=80.000000]";
		String obtainedString = limits.toString();
		assertEquals(expectedString, obtainedString);
	}
}
