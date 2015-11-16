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
public class TupleTest {

	private AtomicValue concValueA = new AtomicValue(5.0);
	private AtomicValue timeValueA = new AtomicValue(0.0);
	private AtomicValue concValueB = new AtomicValue(10.0);
	private AtomicValue timeValueB = new AtomicValue(2.0);
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
		Tuple tupleA = new Tuple(concValueA, timeValueA);
		Tuple tupleB = new Tuple(concValueB, timeValueB);

		assertEquals(tupleA, new Tuple(tupleA.toNode(doc)));
		assertEquals(tupleB, new Tuple(tupleB.toNode(doc)));
	}

	@Test
	public void testGetters() {
		Tuple tupleA = new Tuple(concValueA, timeValueA);
		assertEquals(tupleA.getConcValue(), concValueA);
		assertEquals(tupleA.getTimeValue(), timeValueA);

		Tuple tupleB = new Tuple(concValueB, timeValueB);
		assertEquals(tupleB.getConcValue(), concValueB);
		assertEquals(tupleB.getTimeValue(), timeValueB);
	}

	@Test
	public void testToString() {
		Tuple tupleA = new Tuple(concValueA, timeValueA);
		String expectedString = String.format("Tuple [atomicValues=%s %s]", tupleA.getConcValue(),
				tupleA.getTimeValue());
		String obtainedString = tupleA.toString();
		assertEquals(expectedString, obtainedString);

		Tuple tupleB = new Tuple(concValueB, timeValueB);
		expectedString = String.format("Tuple [atomicValues=%s %s]", tupleB.getConcValue(), tupleB.getTimeValue());
		obtainedString = tupleB.toString();
		assertEquals(expectedString, obtainedString);
		assertEquals(expectedString, obtainedString);
	}

	@Test
	public void testEquals() {
		// Same concentration and time values
		assertEquals(new Tuple(concValueA, timeValueA), new Tuple(concValueA, timeValueA));

		// Same concentration value and different time value
		assertFalse(new Tuple(concValueA, timeValueA).equals(new Tuple(concValueA, timeValueB)));

		// Different concentration value and same time value
		assertFalse(new Tuple(concValueA, timeValueA).equals(new Tuple(concValueB, timeValueB)));

		// Different concentration and time values
		assertFalse(new Tuple(concValueA, timeValueA).equals(new Tuple(concValueB, timeValueB)));
	}
}
