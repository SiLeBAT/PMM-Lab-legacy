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
public class UncertaintiesImplTest {

	private final int id = 101;
	private final String modelName = "BacillusCereus_CultureMedium";
	private final String comment = "uncertainties";
	private final double r2 = 0.996;
	private final double rms = 0.345;
	private final double sse = 1.909;
	private final double aic = -32.997;
	private final double bic = -34.994;
	private final int dof = 16;

	@Test
	public void testIdAccesors() {
		// Constructor should ignore null values for id
		UncertaintiesImpl uncert = new UncertaintiesImpl(null, null, null, null, null, null, null, null, null);
		assertFalse(uncert.isSetID());
		assertNull(uncert.getID());

		uncert.setID(id);
		assertTrue(uncert.isSetID());
		assertTrue(id == uncert.getID());
	}

	@Test
	public void testModelNameAccesors() {
		// Constructor should ignore null values for model name
		UncertaintiesImpl uncert = new UncertaintiesImpl(null, null, null, null, null, null, null, null, null);
		assertFalse(uncert.isSetModelName());
		assertNull(uncert.getModelName());
		
		// Constructor should ignore empty strings for model name
		uncert = new UncertaintiesImpl(null, "", null, null, null, null, null, null, null);
		assertFalse(uncert.isSetModelName());
		assertNull(uncert.getModelName());

		// setModelName should ignore null strings
		uncert.setModelName(null);
		assertFalse(uncert.isSetModelName());
		assertNull(uncert.getModelName());

		// setModelName should ignore empty strings
		uncert.setModelName("");
		assertFalse(uncert.isSetModelName());
		assertNull(uncert.getModelName());

		// setModelName should accept non-empty strings
		uncert.setModelName(modelName);
		assertTrue(uncert.isSetModelName());
		assertEquals(uncert.getModelName(), modelName);
	}

	@Test
	public void testCommentAccesors() {
		// Constructor should ignore null values for comment
		UncertaintiesImpl uncert = new UncertaintiesImpl(null, null, null, null, null, null, null, null, null);
		assertFalse(uncert.isSetComment());
		assertNull(uncert.getComment());

		// Constructor should ignore empty strings for comment
		uncert = new UncertaintiesImpl(null, null, "", null, null, null, null, null, null);
		assertFalse(uncert.isSetComment());
		assertNull(uncert.getComment());

		// setComment should ignore null strings
		uncert.setComment(null);
		assertFalse(uncert.isSetComment());
		assertNull(uncert.getComment());

		// setComment should ignore empty strings
		uncert.setComment("");
		assertFalse(uncert.isSetComment());
		assertNull(uncert.getComment());

		// setComment should accept non-empty strings
		uncert.setComment(comment);
		assertTrue(uncert.isSetComment());
		assertEquals(uncert.getComment(), comment);
	}

	@Test
	public void testR2Accesors() {
		// Constructor should ignore null values for r2
		UncertaintiesImpl uncert = new UncertaintiesImpl(null, null, null, null, null, null, null, null, null);
		assertFalse(uncert.isSetR2());
		assertNull(uncert.getR2());

		uncert.setR2(r2);
		assertTrue(uncert.isSetR2());
		assertTrue(Double.compare(uncert.getR2(), r2) == 0);
	}

	@Test
	public void testRMSAccesors() {
		// Constructor should ignore null values for rms
		UncertaintiesImpl uncert = new UncertaintiesImpl(null, null, null, null, null, null, null, null, null);
		assertFalse(uncert.isSetRMS());
		assertNull(uncert.getRMS());

		uncert.setRMS(rms);
		assertTrue(uncert.isSetRMS());
		assertTrue(Double.compare(uncert.getRMS(), rms) == 0);
	}

	@Test
	public void testSSEaccesors() {
		UncertaintiesImpl uncert = new UncertaintiesImpl(null, null, null, null, null, null, null, null, null);
		assertFalse(uncert.isSetSSE());
		assertNull(uncert.getSSE());

		uncert.setSSE(sse);
		assertTrue(uncert.isSetSSE());
		assertTrue(Double.compare(uncert.getSSE(), sse) == 0);
	}

	@Test
	public void testAICaccesors() {
		UncertaintiesImpl uncert = new UncertaintiesImpl(null, null, null, null, null, null, null, null, null);
		assertFalse(uncert.isSetAIC());
		assertNull(uncert.getAIC());

		uncert.setAIC(aic);
		assertTrue(uncert.isSetAIC());
		assertTrue(Double.compare(uncert.getAIC(), aic) == 0);
	}

	@Test
	public void testBICaccesors() {
		UncertaintiesImpl uncert = new UncertaintiesImpl(null, null, null, null, null, null, null, null, null);
		assertFalse(uncert.isSetBIC());
		assertNull(uncert.getBIC());

		uncert.setBIC(bic);
		assertTrue(uncert.isSetBIC());
		assertTrue(Double.compare(uncert.getBIC(), bic) == 0);
	}

	@Test
	public void testDOFaccesors() {
		UncertaintiesImpl uncert = new UncertaintiesImpl(null, null, null, null, null, null, null, null, null);
		assertFalse(uncert.isSetDOF());
		assertNull(uncert.getDOF());

		uncert.setDOF(dof);
		assertTrue(uncert.isSetDOF());
		assertTrue(uncert.getDOF() == dof);
	}
}
