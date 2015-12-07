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

import static org.junit.Assert.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.sbml.jsbml.Unit;
import org.w3c.dom.Document;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.sbml.ModelVariable;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.PMFUnit;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.ReferenceType;
import de.bund.bfr.pmf.sbml.SBMLFactory;

public class NuMLDocumentTest {

	private ConcentrationOntology concOntology;
	private TimeOntology timeOntology;
	private ResultComponent resultComponent;

	@Before
	public void setUp() {
		createConcentrationOntology();
		createTimeOntology();
		createResultComponent();
	}

	private void createConcentrationOntology() {
		PMFUnit[] concUnits = new PMFUnit[] { new PMFUnit(1.0, 0, Unit.Kind.ITEM, 1.0),
				new PMFUnit(1.0, 0, Unit.Kind.GRAM, 1.0) };
		PMFUnitDefinition concUnitDef = new PMFUnitDefinition("log10_count_g", "log10(count/g)", "log10", concUnits);

		String compartmentId = "culture_broth_broth_culture_culture_medium";
		String compartmentName = "culture broth, broth culture, culture medium";
		String pmfCode = null;
		String compartmentDetail = "broth";
		ModelVariable[] modelVariables = new ModelVariable[] { new ModelVariable("Temperature", 10.0),
				new ModelVariable("pH", 5.63) };
		PMFCompartment compartment = SBMLFactory.createPMFCompartment(compartmentId, compartmentName, pmfCode,
				compartmentDetail, modelVariables);

		String speciesId = "species4024";
		String speciesName = "salmonella spp";
		String combaseCode = null;
		String speciesDetail = "Salmonella spec";
		String depDesc = null;
		PMFSpecies species = SBMLFactory.createPMFSpecies(compartmentId, speciesId, speciesName, concUnitDef.getId(),
				combaseCode, speciesDetail, depDesc);

		concOntology = new ConcentrationOntology(concUnitDef, compartment, species);
	}

	private void createTimeOntology() {
		PMFUnit[] hourUnits = new PMFUnit[] { new PMFUnit(3600, 0, Unit.Kind.SECOND, 1) };
		PMFUnitDefinition timeUnitDef = new PMFUnitDefinition("h", "h", null, hourUnits);
		timeOntology = new TimeOntology(timeUnitDef);
	}

	private void createResultComponent() {
		String id = "exp1";
		int condId = 1;
		String combaseId = "salm25";
		String creatorGivenName = "Jane Doe";
		String creatorFamilyName = "Doe";
		String creatorContact = "jane.doe@people.com";
		String createdDate = null;
		String modifiedDate = null;
		ModelType modelType = ModelType.EXPERIMENTAL_DATA;
		String notes = null;
		String rights = "CC";

		// Create references
		String author = "Baranyi, J.";
		int year = 1994;
		String title = "A dynamic approach to predicting microbial bacterial growth in food";
		String abstractText = "A new member of the family of groth models described by Baranyi ...";
		String journal = "International Journal of Food Microbiology";
		String volume = "23";
		String issue = "3";
		int page = 277;
		String website = "http://www.sciencedirect.com/science/article/pii/0168160594901570";
		ReferenceType referenceType = ReferenceType.Paper;

		Reference ref = SBMLFactory.createReference(author, year, title, abstractText, journal, volume, issue, page,
				null, website, referenceType, null);
		Reference[] references = new Reference[] { ref };

		AtomicDescription concDesc = new AtomicDescription("concentration", "concentration");
		AtomicDescription timeDesc = new AtomicDescription("Time", "time");
		TupleDescription tupleDescription = new TupleDescription(concDesc, timeDesc);

		Tuple[] tuples = new Tuple[] { new Tuple(new AtomicValue(2.67), new AtomicValue(0.00)),
				new Tuple(new AtomicValue(2.91), new AtomicValue(50.88)),
				new Tuple(new AtomicValue(2.87), new AtomicValue(73.02)),
				new Tuple(new AtomicValue(3.69), new AtomicValue(103.96)),
				new Tuple(new AtomicValue(4.25), new AtomicValue(145.01)) };

		resultComponent = new ResultComponent(id, condId, combaseId, creatorGivenName, creatorFamilyName,
				creatorContact, createdDate, modifiedDate, modelType, rights, notes, references, tupleDescription,
				tuples);
	}

	@Test
	public void test() {
		Document xmlDoc = null;
		try {
			xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		if (xmlDoc == null) {
			fail();
		} else {
			NuMLDocument doc = new NuMLDocument(concOntology, timeOntology, resultComponent);
			doc = new NuMLDocument(doc.toNode(xmlDoc));
			assertEquals(concOntology, doc.getConcentrationOntologyTerm());
			assertEquals(timeOntology, doc.getTimeOntologyTerm());
			assertEquals(resultComponent, doc.getResultComponent());
		}
	}

}
