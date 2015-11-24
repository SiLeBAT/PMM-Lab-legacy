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
import org.sbml.jsbml.Species;

/**
 * @author Miguel Alba
 *
 */
public class PMFSpeciesImplTest {

	private Species originalSpecies;

	@Before
	public void setUp() {
		originalSpecies = new Species(3, 1);
		originalSpecies.setCompartment("Culture_medium");
		originalSpecies.setId("species4024");
		originalSpecies.setName("salmonella spp");
		originalSpecies.setSubstanceUnits("ln_count_g");
		originalSpecies.setBoundaryCondition(PMFSpeciesImpl.BOUNDARY_CONDITION);
		originalSpecies.setConstant(PMFSpeciesImpl.CONSTANT);
		originalSpecies.setHasOnlySubstanceUnits(PMFSpeciesImpl.ONLY_SUBSTANCE_UNITS);
	}

	@Test
	public void testCompartmentAccesors() {
		PMFSpecies species = new PMFSpeciesImpl(originalSpecies);
		assertEquals(originalSpecies.getCompartment(), species.getCompartment());

		// setCompartment should ignore null strings
		species.setCompartment(null);
		assertEquals(originalSpecies.getCompartment(), species.getCompartment());

		// setCompartment should ignore empty strings
		species.setCompartment("");
		assertEquals(originalSpecies.getCompartment(), species.getCompartment());

		// setCompartment should take non-empty strings
		species.setCompartment("New_culture_medium");
		assertEquals("New_culture_medium", species.getCompartment());
	}

	@Test
	public void testIdAccesors() {
		PMFSpecies species = new PMFSpeciesImpl(originalSpecies);
		assertEquals(originalSpecies.getId(), species.getId());

		// setId should ignore null strings
		species.setId(null);
		assertEquals(originalSpecies.getId(), species.getId());

		// setId should ignore empty strings
		species.setId("");
		assertEquals(originalSpecies.getId(), species.getId());

		// setId should take non-empty strings
		species.setId("new_id");
		assertEquals("new_id", species.getId());
	}

	@Test
	public void testNameAccesors() {
		PMFSpecies species = new PMFSpeciesImpl(originalSpecies);
		assertEquals(originalSpecies.getName(), species.getName());

		// setName should ignore null strings
		species.setName(null);
		assertEquals(originalSpecies.getName(), species.getName());

		// setName should ignore empty strings
		species.setName("");
		assertEquals(originalSpecies.getName(), species.getName());

		// setName should accept non-empty strings
		species.setName("new_name");
		assertEquals("new_name", species.getName());
	}

	@Test
	public void testUnitAccesors() {
		PMFSpecies species = new PMFSpeciesImpl(originalSpecies);
		assertEquals(originalSpecies.getUnits(), species.getUnits());
		
		// setUnits should ignore null strings
		species.setUnits(null);
		assertEquals(originalSpecies.getUnits(), species.getUnits());
		
		// setUnits should ignore empty strings
		species.setUnits("");
		assertEquals(originalSpecies.getUnits(), species.getUnits());
		
		// setUnits should accept non-empty strings
		species.setUnits("log10");
		assertEquals("log10", species.getUnits());
	}
	
	@Test
	public void testCombaseCodeAccesors() {
		PMFSpecies species = new PMFSpeciesImpl(originalSpecies);
		
		// setCombaseCode should ignore null strings
		species.setCombaseCode(null);
		assertFalse(species.isSetCombaseCode());
		assertNull(species.getCombaseCode());
		
		// setCombaseCode should ignore empty strings
		species.setCombaseCode("");
		assertFalse(species.isSetCombaseCode());
		assertNull(species.getCombaseCode());
		
		// setCombaseCode should accept non-empty strings
		species.setCombaseCode("007");
		assertTrue(species.isSetCombaseCode());
		assertEquals("007", species.getCombaseCode());
	}

	@Test
	public void testDetailAccesors() {
		PMFSpecies species = new PMFSpeciesImpl(originalSpecies);
		
		// setDetail should ignore null strings
		species.setDetail(null);
		assertFalse(species.isSetDetail());
		assertNull(species.getDetail());
		
		// setDetail should ignore empty strings
		species.setDetail("");
		assertFalse(species.isSetDetail());
		assertNull(species.getDetail());
		
		// setDetail should accept non-empty strings
		species.setDetail("Salmonella spec.");
		assertTrue(species.isSetDetail());
		assertEquals("Salmonella spec.", species.getDetail());
	}

	@Test
	public void testDescriptionAccesors() {
		PMFSpecies species = new PMFSpeciesImpl(originalSpecies);
		
		// setDescription should ignore null strings
		species.setDescription(null);
		assertFalse(species.isSetDescription());
		assertNull(species.getDescription());
		
		// setDescription should ignore empty strings
		species.setDescription("");
		assertFalse(species.isSetDescription());
		assertNull(species.getDescription());
		
		// setDescription should accept non-empty strings
		species.setDescription("description");
		assertTrue(species.isSetDescription());
		assertEquals("description", species.getDescription());
	}
}
