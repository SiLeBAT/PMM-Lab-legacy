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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.MiscXml;

public class JSONMiscListTest {

	@Test
	public void test() {

		// temperature misc
		int tempId = -1;
		String tempName = "Temperature";
		String tempDesc = "Temperature";
		Double tempVal = 10.0;
		List<String> tempCategories = Arrays.asList("Temperature");
		String tempUnit = "°C";
		String tempOrigUnit = "°C";
		String tempDbuuid = "c6582d2c-7cd6-4ee3-b425-68662b2558d9";

		MiscXml tempMisc = new MiscXml(tempId, tempName, tempDesc, tempVal,
				tempCategories, tempUnit, tempOrigUnit, tempDbuuid);

		// pH misc
		int phId = -2;
		String phName = "pH";
		String phDesc = "pH";
		Double phVal = 5.63;
		List<String> phCategories = Arrays.asList("Dimensionless quantity");
		String phUnit = "[pH]";
		String phOrigUnit = "[pH]";
		String phDbuuid = "c6582d2c-7cd6-4ee3-b425-68662b2558d9";

		MiscXml phMisc = new MiscXml(phId, phName, phDesc, phVal, phCategories,
				phUnit, phOrigUnit, phDbuuid);

		List<MiscXml> miscXmls = Arrays.asList(tempMisc, phMisc);
		
		JSONMiscList jsonMiscList = new JSONMiscList(miscXmls);
		List<MiscXml> obtainedMiscs = jsonMiscList.toMiscXml();
		
		// Tests obtainedMiscs
		for (int i = 0; i < obtainedMiscs.size(); i++) {
			MiscXml expectedMisc = miscXmls.get(0);
			MiscXml obtainedMisc = obtainedMiscs.get(0);
			
			assertEquals(expectedMisc.getId(), obtainedMisc.getId());
			assertEquals(expectedMisc.getName(), obtainedMisc.getName());
			assertEquals(expectedMisc.getDescription(), obtainedMisc.getDescription());
			assertEquals(expectedMisc.getValue(), obtainedMisc.getValue());
			assertEquals(expectedMisc.getCategories(), obtainedMisc.getCategories());
			assertEquals(expectedMisc.getUnit(), obtainedMisc.getUnit());
			assertEquals(expectedMisc.getOrigUnit(), obtainedMisc.getOrigUnit());
			assertEquals(expectedMisc.getDbuuid(), obtainedMisc.getDbuuid());
		}

	}
}
