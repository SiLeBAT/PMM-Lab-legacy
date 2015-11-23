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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Miguel Alba
 *
 */
public class CorrelationTest {

	private Correlation ph = new Correlation("pH", 5.63);
	private Correlation aw = new Correlation("aw");

	@Test
	public void testAccesors() {
		assertEquals(ph.getName(), "pH");
		assertTrue(ph.isSetValue());
		assertTrue(Double.compare(ph.getValue(), 5.63) == 0);
		
		assertEquals(aw.getName(), "aw");
		assertFalse(aw.isSetValue());
	}

	@Test
	public void testToString() {
		assertEquals("Correlation [name=pH, value=5.630000]", ph.toString());
	}
	
	@Test
	public void testEquals() {
		assertEquals(ph, ph);
		assertFalse(ph.equals(aw));
	}

}
