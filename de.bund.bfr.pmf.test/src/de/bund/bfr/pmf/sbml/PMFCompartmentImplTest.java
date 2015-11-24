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

import org.junit.Before;
import org.junit.Test;
import org.sbml.jsbml.Compartment;

/**
 * @author Miguel Alba
 *
 */
public class PMFCompartmentImplTest {
	// <compartment id="Culture_medium" constant="true" name="Culture medium">
	// <annotation>
	// <pmf:metadata>
	// <pmmlab:detail>broth</pmmlab:detail>
	// <pmmlab:environment name="Temperature" value="10.0" />
	// <pmmlab:environment name="pH" value="5.63" />
	// </pmf:metadata>
	// </annotation>

	private Compartment originalCompartment;

	@Before
	public void setUp() {
		originalCompartment = new Compartment(3, 1);
		originalCompartment.setId("Culture_medium");
		originalCompartment.setName("Culture medium");
		originalCompartment.setConstant(true);
	}

	@Test
	public void testIdAccesors() {
		PMFCompartment compartment = new PMFCompartmentImpl(originalCompartment);
		assertEquals(originalCompartment.getId(), compartment.getId());
		
		// setId should ignore null strings
		compartment.setId(null);
		assertEquals(originalCompartment.getId(), compartment.getId());
		
		// setId should ignore empty strings
		compartment.setId("");
		assertEquals(originalCompartment.getId(), compartment.getId());
		
		// setId should accept non-emtpy strings
		compartment.setId("new_id");
		assertEquals("new_id", compartment.getId());
	}

	@Test
	public void testNameAccesors() {
		PMFCompartment compartment = new PMFCompartmentImpl(originalCompartment);
		assertEquals(originalCompartment.getName(), compartment.getName());
		
		// setName should ignore null strings
		compartment.setName(null);
		assertEquals(originalCompartment.getName(), compartment.getName());
		
		// setName should ignore empty strings
		compartment.setName("");
		assertEquals(originalCompartment.getName(), compartment.getName());
		
		// setName should accept non-empty strings
		compartment.setName("a_name");
		assertEquals("a_name", compartment.getName());
	}

	@Test
	public void testPMFcodeAccesors() {
		PMFCompartment compartment = new PMFCompartmentImpl(originalCompartment);
		
		// setPMFCode should ignore null strings
		compartment.setPMFCode(null);
		assertFalse(compartment.isSetPMFCode());
		assertNull(compartment.getPMFCode());
		
		// setPMFCode should ignore empty strings
		compartment.setPMFCode("");
		assertFalse(compartment.isSetPMFCode());
		assertNull(compartment.getPMFCode());
		
		// setPMFCode should accept non-emtpy strings
		compartment.setPMFCode("007");
		assertTrue(compartment.isSetPMFCode());
		assertEquals("007", compartment.getPMFCode());
	}

	@Test
	public void testDetailAccesors() {
		PMFCompartment compartment = new PMFCompartmentImpl(originalCompartment);
		
		// setDetail should ignore null strings
		compartment.setDetail(null);
		assertFalse(compartment.isSetDetail());
		assertNull(compartment.getDetail());

		// setDetail should ignore empty strings
		compartment.setDetail(null);
		assertFalse(compartment.isSetDetail());
		assertNull(compartment.getDetail());
		
		// setDetail should ignore non-empty strings
		compartment.setDetail("broth");
		assertTrue(compartment.isSetDetail());
		assertEquals("broth", compartment.getDetail());
	}

	@Test
	public void testModelVariableAccesors() {
		PMFCompartment compartment = new PMFCompartmentImpl(originalCompartment);
		
		// setModelVariables should ignore null
		compartment.setModelVariables(null);
		assertFalse(compartment.isSetModelVariables());
		assertNull(compartment.getModelVariables());

		ModelVariable[] modelVariables = new ModelVariable[] { new ModelVariable("Temperature", 10.0) };
		compartment.setModelVariables(modelVariables);
		assertTrue(compartment.isSetModelVariables());
		assertArrayEquals(modelVariables, compartment.getModelVariables());
	}
}
