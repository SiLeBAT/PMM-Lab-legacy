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
public class TupleDescriptionTest {

	private AtomicDescription timeDesc = new AtomicDescription("Time", "time");
	private AtomicDescription concDesc = new AtomicDescription("concentration", "concentration");
	private Document doc;

	@Before
	public void setUp() {
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNodes() {
		TupleDescription td = new TupleDescription(concDesc, timeDesc);
		assertEquals(td, new TupleDescription(td.toNode(doc)));
	}

	@Test
	public void testGetters() {
		TupleDescription td = new TupleDescription(concDesc, timeDesc);
		assertEquals(concDesc, td.getConcentrationDescription());
		assertEquals(timeDesc, td.getTimeDescription());
	}

	@Test
	public void testEquals() {
		AtomicDescription otherTimeDesc = new AtomicDescription("_Time", "_time");
		AtomicDescription otherConcDesc = new AtomicDescription("_Conce", "_conc");

		// Same concentration and time descriptions
		assertEquals(new TupleDescription(concDesc, timeDesc), new TupleDescription(concDesc, timeDesc));

		// Same concentration description and different time description
		assertFalse(new TupleDescription(concDesc, timeDesc).equals(new TupleDescription(concDesc, otherTimeDesc)));

		// Different concentration description and same time description
		assertFalse(new TupleDescription(concDesc, timeDesc).equals(new TupleDescription(otherConcDesc, timeDesc)));

		// Different concentration and time descriptions
		assertFalse(
				new TupleDescription(concDesc, timeDesc).equals(new TupleDescription(otherConcDesc, otherTimeDesc)));
	}

	@Test
	public void testToString() {
		TupleDescription td = new TupleDescription(concDesc, timeDesc);
		String expectedString = String.format("TupleDescription [concentrationDescription=%s, timeDescription=%s]",
				concDesc, timeDesc);
		String obtainedString = td.toString();
		assertEquals(expectedString, obtainedString);
	}
}
