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
package de.bund.bfr.pmf.numl;

import static org.junit.Assert.assertEquals;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sbml.jsbml.Unit;
import org.w3c.dom.Document;

import de.bund.bfr.pmf.sbml.ModelVariable;
import de.bund.bfr.pmf.sbml.PMFCompartmentImpl;
import de.bund.bfr.pmf.sbml.PMFSpeciesImpl;
import de.bund.bfr.pmf.sbml.PMFUnit;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;

/**
 * @author Miguel Alba
 */
public class ConcentrationOntologyTest {

	private PMFUnitDefinition unitDefinition;
	private PMFCompartmentImpl compartment;
	private PMFSpeciesImpl species;
	private Document doc;

	@Before
	public void setUp() {
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PMFUnit[] units = new PMFUnit[] { new PMFUnit(1.0, 0, Unit.Kind.ITEM, 1.0),
				new PMFUnit(1.0, 0, Unit.Kind.GRAM, 1.0) };
		unitDefinition = new PMFUnitDefinition("log10_count_g", "log10(count/g)", "log10", units);

		String compartmentId = "culture_broth_broth_culture_culture_medium";
		String compartmentName = "culture broth, broth culture, culture medium";
		String pmfCode = null;
		String compartmentDetail = "broth";
		ModelVariable[] modelVariables = new ModelVariable[] { new ModelVariable("Temperature", 10.0),
				new ModelVariable("pH", 5.63) };
		compartment = new PMFCompartmentImpl(compartmentId, compartmentName, pmfCode, compartmentDetail, modelVariables);

		String speciesId = "species4024";
		String speciesName = "salmonella spp";
		String combaseCode = null;
		String speciesDetail = "Salmonella spec";
		String depDesc = null;
		species = new PMFSpeciesImpl(compartmentId, speciesId, speciesName, unitDefinition.getId(), combaseCode,
				speciesDetail, depDesc);
	}

	@After
	public void tearDown() {
		unitDefinition = null;
		compartment = null;
		species = null;
	}

	@Test
	public void testGetters() {
		ConcentrationOntology concOntology = new ConcentrationOntology(unitDefinition, compartment, species);
		assertEquals(concOntology.getUnitDefinition(), unitDefinition);
		assertEquals(concOntology.getCompartment(), compartment);
		assertEquals(concOntology.getSpecies(), species);
	}

	@Test
	public void testNodes() {
		ConcentrationOntology concOntology = new ConcentrationOntology(unitDefinition, compartment, species);
		assertEquals(concOntology, new ConcentrationOntology(concOntology.toNode(doc)));
	}

	@Test
	public void testToString() {
		String expectedString = String.format("OntologyTerm [id=%s, term=%s, sourceTermId=%s, ontologyURI=%s]",
				ConcentrationOntology.ID, ConcentrationOntology.TERM, ConcentrationOntology.SOURCE_TERM_ID,
				ConcentrationOntology.URI);
		ConcentrationOntology concOntology = new ConcentrationOntology(unitDefinition, compartment, species);
		String obtainedString = concOntology.toString();
		assertEquals(expectedString, obtainedString);
	}
}
