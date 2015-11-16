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
public class UncertaintiesTest {
	
	private UncertaintiesImpl uncert = new UncertaintiesImpl();
    
	@Test
	public void testModelNameAccesors() {
		assertFalse(uncert.isSetModelName());
		assertNull(uncert.getModelName());
		
		String modelName = "BacillusCereus_CultureMedium";
		uncert.setModelName(modelName);
		assertTrue(uncert.isSetModelName());
		assertEquals(uncert.getModelName(), modelName);
	}
	
	@Test
	public void testCommentAccesors() {
		assertFalse(uncert.isSetComment());
		assertNull(uncert.getComment());
		
		uncert.setComment("");
		assertTrue(uncert.isSetComment());
		assertEquals(uncert.getComment(), "");
	}
	
	@Test
	public void testR2Accesors() {
		assertFalse(uncert.isSetR2());
		assertNull(uncert.getR2());
		
		uncert.setR2(0.996);
		assertTrue(uncert.isSetR2());
		assertTrue(Double.compare(uncert.getR2(), 0.996) == 0);
	}
	
	@Test
	public void testRMSAccesors() {
		assertFalse(uncert.isSetRMS());
		assertNull(uncert.getRMS());
		
		uncert.setRMS(0.345);
		assertTrue(uncert.isSetRMS());
		assertTrue(Double.compare(uncert.getRMS(), 0.345) == 0);
	}

	@Test
	public void testSSEaccesors() {
		assertFalse(uncert.isSetSSE());
		assertNull(uncert.getSSE());
		
		uncert.setSSE(1.909);
		assertTrue(uncert.isSetSSE());
		assertTrue(Double.compare(uncert.getSSE(), 1.909) == 0);
	}
	
	@Test
	public void testAICaccesors() {
		assertFalse(uncert.isSetAIC());
		assertNull(uncert.getAIC());
		
		uncert.setAIC(-32.977);
		assertTrue(uncert.isSetAIC());
		assertTrue(Double.compare(uncert.getAIC(), -32.977) == 0);
	}
	
	@Test
	public void testBICaccesors() {
		assertFalse(uncert.isSetBIC());
		assertNull(uncert.getBIC());

		uncert.setBIC(-34.994);
		assertTrue(uncert.isSetBIC());
		assertTrue(Double.compare(uncert.getBIC(), -34.994) == 0);
	}
	
	@Test
	public void testDOFaccesors() {
		assertFalse(uncert.isSetDOF());
		assertNull(uncert.getDOF());
		
		uncert.setDOF(16);
		assertTrue(uncert.isSetDOF());
		assertTrue(uncert.getDOF() == 16);
	}
}
