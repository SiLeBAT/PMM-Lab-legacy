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
import static org.junit.Assert.assertFalse;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sbml.jsbml.Unit;
import org.w3c.dom.Document;

import de.bund.bfr.pmf.sbml.PMFUnit;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;

/**
 * @author Miguel Alba
 */
public class TimeOntologyTest {

	private PMFUnitDefinition hoursUnitDefinition;
	private PMFUnitDefinition secondsUnitDefinition;
	private Document doc;

	@Before
	public void setUp() {
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PMFUnit[] hourUnits = new PMFUnit[] { new PMFUnit(3600, 0, Unit.Kind.SECOND, 1) };
		hoursUnitDefinition = new PMFUnitDefinition("h", "h", null, hourUnits);

		PMFUnit[] secondUnits = new PMFUnit[] { new PMFUnit(1, 0, Unit.Kind.SECOND, 1) };
		secondsUnitDefinition = new PMFUnitDefinition("s", "s", null, secondUnits);
	}

	@After
	public void tearDown() {
		hoursUnitDefinition = null;
		secondsUnitDefinition = null;
	}

	@Test
	public void testNodes() {
		TimeOntology hoursOntology = new TimeOntology(hoursUnitDefinition);
		TimeOntology secondsOntology = new TimeOntology(secondsUnitDefinition);

		assertEquals(hoursOntology, new TimeOntology(hoursOntology.toNode(doc)));
		assertEquals(secondsOntology, new TimeOntology(secondsOntology.toNode(doc)));
	}
	
	@Test
	public void testGetter() {
		TimeOntology hoursOntology = new TimeOntology(hoursUnitDefinition);
		TimeOntology secondsOntology = new TimeOntology(secondsUnitDefinition);
		
		assertEquals(hoursOntology.getUnitDefinition(), hoursUnitDefinition);
		assertEquals(secondsOntology.getUnitDefinition(), secondsUnitDefinition);
	}

	@Test
	public void testEquals() {
		TimeOntology hoursOntology = new TimeOntology(hoursUnitDefinition);
		TimeOntology secondsOntology = new TimeOntology(secondsUnitDefinition);

		assertEquals(hoursOntology, hoursOntology);
		assertFalse(hoursOntology.equals(secondsOntology));
	}

	@Test
	public void testToString() {
		TimeOntology hoursOntology = new TimeOntology(hoursUnitDefinition);
		String hoursExpectedString = String.format("OntologyTerm [id=%s, term=%s, sourceTermId=%s, ontologyURI=%s]",
				TimeOntology.ID, TimeOntology.TERM, TimeOntology.SOURCE_TERM_ID, TimeOntology.URI);
		String hoursObtainedString = hoursOntology.toString();
		assertEquals(hoursExpectedString, hoursObtainedString);
	}
}
