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

import org.junit.Before;
import org.junit.Test;
import org.sbml.jsbml.Unit;
import org.w3c.dom.Document;

import de.bund.bfr.pmf.sbml.PMFUnit;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;

/**
 * @author Miguel Alba
 */
public class UnitDefinitionNuMLNodeTest {

	private PMFUnitDefinition unitDefinition;
	private Document doc;

	@Before
	public void setUp() {
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PMFUnit[] units = new PMFUnit[] { new PMFUnit(1, 0, Unit.Kind.ITEM, 1), new PMFUnit(1, 0, Unit.Kind.GRAM, -1) };
		unitDefinition = new PMFUnitDefinition("ln_count_g", "ln(count/g)", "ln", units);
	}
	
	@Test
	public void test() {
		UnitDefinitionNuMLNode unitDefNode = new UnitDefinitionNuMLNode(unitDefinition, doc);
		assertEquals(unitDefinition, unitDefNode.toPMFUnitDefinition());
		assertEquals(unitDefinition, new UnitDefinitionNuMLNode(unitDefNode.node).toPMFUnitDefinition());
	}

}
