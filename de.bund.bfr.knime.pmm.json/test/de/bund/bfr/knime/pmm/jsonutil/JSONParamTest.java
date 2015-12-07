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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.ParamXml;

public class JSONParamTest {

	@Test
	public void test() {
		String name = "mu_max";
		String origName = "mu_max";
		Double value = 0.03412808921078558;
		Double error = 9.922557266695599E-4;
		Double min = 0.0;
		Double max = 5.0;
		Double P = 2.220446049250313E-16;
		Double t = 34.39444922664667;
		Double minGuess = null;
		Double maxGuess = null;
		String category = "";
		String unit = null;
		String description = "specific growth rate related to ln() transformed data - min/max selected to improve fitting";
		HashMap<String, Double> correlations = new HashMap<>();
		correlations.put("Ymax", -6.137961639952814E-5);
		correlations.put("mu_max", 9.845714271085363E-7);
		correlations.put("h0", 3.2387019240707804E-4);
		correlations.put("Y0", 7.843710581862156E-5);
		
		ParamXml paramXml = new ParamXml(name, value, error, minGuess, maxGuess, P, t, category, unit);
		paramXml.setOrigName(origName);
		paramXml.setMin(min);
		paramXml.setMax(max);
		paramXml.setDescription(description);
		for (Map.Entry<String, Double> entry : correlations.entrySet()) {
			paramXml.addCorrelation(entry.getKey(), entry.getValue());
		}
		
		JSONParam jsonParam = new JSONParam(paramXml);
		paramXml = jsonParam.toParamXml();

		// Tests paramXml
		assertEquals(name, paramXml.getName());
		assertEquals(origName, paramXml.getOrigName());
		assertEquals(value, paramXml.getValue());
		assertEquals(error, paramXml.getError());
		assertEquals(min, paramXml.getMin());
		assertEquals(max, paramXml.getMax());
		assertEquals(P, paramXml.getP());
		assertEquals(t, paramXml.getT());
		assertEquals(minGuess, paramXml.getMinGuess());
		assertEquals(maxGuess, paramXml.getMaxGuess());
		assertEquals(category, paramXml.getCategory());
		assertEquals(unit, paramXml.getUnit());
		assertEquals(description, paramXml.getDescription());
		assertEquals(correlations, paramXml.getAllCorrelations());
	}

}
