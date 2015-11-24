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
public class PMFCoefficientImplTest {

	private double P = 0.006;
	private double error = 0.471;
	private double t = 3.101;
	private Correlation[] correlations = new Correlation[] { new Correlation("h0", 0.221),
			new Correlation("Y0", 0.124) };
	private String description = "physiological state of the microorganism";

	@Test
	public void testIdAccesors() {
		PMFCoefficient coefficient = new PMFCoefficientImpl("h0", 1.460, "dimensionless");
		assertEquals("h0", coefficient.getId());
		
		coefficient.setId("mu_max");
		assertEquals("mu_max", coefficient.getId());
	}
	
	@Test
	public void testValueAccesors() {
		PMFCoefficient coefficient = new PMFCoefficientImpl("h0", 1.460, "dimensionless");
		assertEquals(1.460, coefficient.getValue(), 0.0);
		
		coefficient.setValue(0.000);
		assertEquals(0.000, coefficient.getValue(), 0.0);
	}
	
	@Test
	public void testUnitAccesors() {
		PMFCoefficient coefficient = new PMFCoefficientImpl("h0", 1.460, "dimensionless");
		assertEquals("dimensionless", coefficient.getUnit());
		
		coefficient.setUnit("s");
		assertEquals("s", coefficient.getUnit());
	}

	@Test
	public void testPAccesors() {
		PMFCoefficient h0 = new PMFCoefficientImpl("h0", 1.460, "dimensionless");
		assertFalse(h0.isSetP());
		assertNull(h0.getP());

		h0.setP(P);
		assertTrue(h0.isSetP());
		assertEquals(P, h0.getP(), 0.0);
	}

	@Test
	public void testErrorAccesors() {
		PMFCoefficient h0 = new PMFCoefficientImpl("h0", 1.460, "dimensionless");
		assertFalse(h0.isSetError());
		assertNull(h0.getError());

		h0.setError(error);
		assertTrue(h0.isSetError());
		assertEquals(error, h0.getError(), 0.0);
	}

	@Test
	public void testTAccesors() {
		PMFCoefficient h0 = new PMFCoefficientImpl("h0", 1.460, "dimensionless");
		assertFalse(h0.isSetT());
		assertNull(h0.getT());

		h0.setT(t);
		assertTrue(h0.isSetT());
		assertEquals(t, h0.getT(), 0.0);
	}

	@Test
	public void testCorrelationAccesors() {
		PMFCoefficient h0 = new PMFCoefficientImpl("h0", 1.460, "dimensionless");
		assertFalse(h0.isSetCorrelations());
		assertNull(h0.getCorrelations());

		// setCorrelation should ignore null values
		h0.setCorrelations(null);
		assertFalse(h0.isSetCorrelations());
		assertNull(h0.getCorrelations());

		// setCorrelation should accept non-null correlations
		h0.setCorrelations(correlations);
		assertTrue(h0.isSetCorrelations());
		assertArrayEquals(correlations, h0.getCorrelations());
	}

	@Test
	public void testDescriptionAccesors() {
		PMFCoefficient h0 = new PMFCoefficientImpl("h0", 1.460, "dimensionless");
		assertFalse(h0.isSetDescription());
		assertNull(h0.getDescription());

		// setDescription should ignore null strings
		h0.setDescription(null);
		assertFalse(h0.isSetDescription());
		assertNull(h0.getDescription());

		// setDescription should ignore empty strings
		h0.setDescription("");
		assertFalse(h0.isSetDescription());
		assertNull(h0.getDescription());

		// setDescription should accept non-empty strings
		h0.setDescription(description);
		assertTrue(h0.isSetDescription());
		assertEquals(description, h0.getDescription());
	}
}
