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

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * @author Miguel Alba
 */
public class AtomicDescriptionTest {

	private AtomicDescription concDesc;
	private AtomicDescription timeDesc;
	private Document doc;

	@Before
	public void setUp() {
		concDesc = new AtomicDescription("concentration", "concentration");
		timeDesc = new AtomicDescription("time", "time");
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNodes() {
		assertEquals(timeDesc, new AtomicDescription(timeDesc.toNode(doc)));
		assertEquals(concDesc, new AtomicDescription(concDesc.toNode(doc)));
	}

	@Test
	public void testGetters() {
		assertEquals(concDesc.getName(), "concentration");
		assertEquals(concDesc.getOntologyTermId(), "concentration");

		assertEquals(timeDesc.getName(), "time");
		assertEquals(timeDesc.getOntologyTermId(), "time");
	}

	@Test
	public void testEquals() {
		// Same name and ontology term
		assertEquals(concDesc, concDesc);
		// Same name and different ontology term
		assertFalse(timeDesc.equals(new AtomicDescription("time", "diff_ontology")));
		// Different name and same ontology term
		assertFalse(timeDesc.equals(new AtomicDescription("diff_name", "time")));
		// Different name and ontology term
		assertFalse(concDesc.equals(timeDesc));
	}

	@Test
	public void testToString() {
		assertEquals(concDesc.toString(),
				"AtomicDescription [name=concentration, ontologyTerm=concentration, valueType=DOUBLE]");
		assertEquals(timeDesc.toString(), "AtomicDescription [name=time, ontologyTerm=time, valueType=DOUBLE]");
	}
}
