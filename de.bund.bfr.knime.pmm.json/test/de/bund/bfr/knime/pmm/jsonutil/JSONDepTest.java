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
package de.bund.bfr.knime.pmm.jsonutil;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.DepXml;

public class JSONDepTest {

	@Test
	public void test() {

		String name = "Value";
		String origName = "Value";
		Double min = null;
		Double max = null;
		String category = "Number Content (count/mass)";
		String unit = "ln(count/g)";
		String description = "bacterial population at time t -ln()";
		
		DepXml depXml = new DepXml(name, origName, category, unit, description);
		depXml.setMax(max);
		depXml.setMin(min);
		JSONDep jsonDep = new JSONDep(depXml);
		depXml = jsonDep.toDepXml();
		
		// Tests depXml
		assertEquals(name, depXml.getName());
		assertEquals(origName, depXml.getOrigName());
		assertEquals(category, depXml.getCategory());
		assertEquals(unit, depXml.getUnit());
		assertEquals(description, depXml.getDescription());
		assertEquals(min, depXml.getMin());
		assertEquals(max, depXml.getMax());
	}
}
