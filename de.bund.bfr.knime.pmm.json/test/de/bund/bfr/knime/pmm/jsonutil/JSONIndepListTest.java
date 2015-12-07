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

import de.bund.bfr.knime.pmm.common.IndepXml;

public class JSONIndepListTest {

	@Test
	public void test() {

		// temp indep
		String tempName = "Temperature";
		String tempOrigName = "Temperature";
		Double tempMin = 10.0;
		Double tempMax = 30.0;
		String tempCategory = null;
		String tempUnit = null;
		String tempDesc = "variable";

		IndepXml tempIndep = new IndepXml(tempName, tempOrigName, tempMin,
				tempMax, tempCategory, tempUnit, tempDesc);

		// ph indep
		String phName = "pH";
		String phOrigName = null;
		Double phMin = 5.63;
		Double phMax = 6.77;
		String phCategory = null;
		String phUnit = null;
		String phDesc = "variable";
		
		IndepXml phIndep = new IndepXml(phName, phOrigName, phMin, phMax, phCategory, phUnit, phDesc);

		List<IndepXml> expectedIndepXmls = Arrays.asList(tempIndep, phIndep);
		JSONIndepList jsonIndepList = new JSONIndepList(expectedIndepXmls);
		List<IndepXml> obtainedIndepXmls = jsonIndepList.toIndepXml();
		
		for (int i = 0; i < obtainedIndepXmls.size(); i++) {
			IndepXml expectedIndep = expectedIndepXmls.get(i);
			IndepXml obtainedIndep = obtainedIndepXmls.get(i);
			
			assertEquals(expectedIndep.getName(), obtainedIndep.getName());
			assertEquals(expectedIndep.getOrigName(), obtainedIndep.getOrigName());
			assertEquals(expectedIndep.getMin(), obtainedIndep.getMin());
			assertEquals(expectedIndep.getMax(), obtainedIndep.getMax());
			assertEquals(expectedIndep.getCategory(), obtainedIndep.getCategory());
			assertEquals(expectedIndep.getUnit(), obtainedIndep.getUnit());
			assertEquals(expectedIndep.getDescription(), obtainedIndep.getDescription());
		}
	}

}
