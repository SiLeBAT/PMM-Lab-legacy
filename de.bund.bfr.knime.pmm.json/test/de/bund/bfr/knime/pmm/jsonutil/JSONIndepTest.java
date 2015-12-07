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

import de.bund.bfr.knime.pmm.common.IndepXml;

public class JSONIndepTest {

	@Test
	public void test() {

		String name = "Time";
		String origName = "Time";
		Double min = 0.0;
		Double max = 554.0;
		String category = "Time";
		String unit = "h";
		String description = "time";
		
		IndepXml indepXml = new IndepXml(name, origName, min, max, category, unit, description);
		JSONIndep jsonIndep = new JSONIndep(indepXml);
		indepXml = jsonIndep.toIndepXml();
		
		// Tests indepXml
		assertEquals(name, indepXml.getName());
		assertEquals(origName, indepXml.getOrigName());
		assertEquals(min, indepXml.getMin());
		assertEquals(max, indepXml.getMax());
		assertEquals(category, indepXml.getCategory());
		assertEquals(unit, indepXml.getUnit());
		assertEquals(description, indepXml.getDescription());
	}
}
