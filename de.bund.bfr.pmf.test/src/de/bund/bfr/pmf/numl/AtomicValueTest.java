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
public class AtomicValueTest {
	
	private AtomicValue timeValue;
	private AtomicValue concValue;
	private Document doc;
	
	@Before
	public void setUp() {
		timeValue = new AtomicValue(0.00);
		concValue = new AtomicValue(2.67);
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		assertEquals(0.0, timeValue.getValue(), 0.01);
		assertEquals(2.67, concValue.getValue(), 0.01);
		
		assertEquals(timeValue, new AtomicValue(timeValue.toNode(doc)));
		assertEquals(concValue, new AtomicValue(concValue.toNode(doc)));
		
		assertFalse(timeValue.equals(concValue));
	}
	
	@Test
	public void testToString() {
		assertEquals(concValue.toString(), "AtomicValue [value=2.670000]");
		assertEquals(timeValue.toString(), "AtomicValue [value=0.000000]");
	}
}
