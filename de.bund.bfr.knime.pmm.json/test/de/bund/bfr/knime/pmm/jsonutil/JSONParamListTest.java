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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.ParamXml;

public class JSONParamListTest {

	@Test
	public void test() {

		List<ParamXml> paramXmls = new LinkedList<>();

		// mu_max parameter
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

		ParamXml paramXml = new ParamXml(name, false, value, error, minGuess,
				maxGuess, P, t, category, unit);
		paramXml.setOrigName(origName);
		paramXml.setMin(min);
		paramXml.setMax(max);
		paramXml.setDescription(description);
		for (Map.Entry<String, Double> entry : correlations.entrySet()) {
			paramXml.addCorrelation(entry.getKey(), entry.getValue());
		}

		paramXmls.add(paramXml);

		// h0 parameter
		name = "h0";
		origName = "h0";
		value = 1.4600645100584562;
		error = 0.4708716463769545;
		min = null;
		max = null;
		P = 0.006868797484142597;
		t = 3.1007696498455273;
		minGuess = null;
		maxGuess = null;
		category = null;
		unit = null;
		description = "physiological state of the microorganism - product of maximum specific growth rate and the lag";
		correlations = new HashMap<>();
		correlations.put("Ymax", -0.014839269963920113);
		correlations.put("mu_max", 3.2387019240707874E-4);
		correlations.put("Y0", 0.1240873232218696);
		correlations.put("h0", 0.2217201073617437);

		paramXml = new ParamXml(name, false, value, error, minGuess, maxGuess, P, t,
				category, unit);
		paramXml.setOrigName(origName);
		paramXml.setMin(min);
		paramXml.setMax(max);
		paramXml.setDescription(description);
		for (Map.Entry<String, Double> entry : correlations.entrySet()) {
			paramXml.addCorrelation(entry.getKey(), entry.getValue());
		}

		paramXmls.add(paramXml);

		JSONParamList jsonParamList = new JSONParamList(paramXmls);
		List<ParamXml> obtainedParams = jsonParamList.toParamXml();

		for (int i = 0; i < obtainedParams.size(); i++) {
			ParamXml expectedParam = paramXmls.get(0);
			ParamXml obtainedParam = paramXmls.get(0);

			// Tests paramXml
			assertEquals(expectedParam.getName(), obtainedParam.getName());
			assertEquals(expectedParam.getOrigName(), obtainedParam.getOrigName());
			assertEquals(expectedParam.isStartParam(), obtainedParam.isStartParam());
			assertEquals(expectedParam.getValue(), obtainedParam.getValue());
			assertEquals(expectedParam.getError(), obtainedParam.getError());
			assertEquals(expectedParam.getMin(), obtainedParam.getMin());
			assertEquals(expectedParam.getMax(), obtainedParam.getMax());
			assertEquals(expectedParam.getP(), obtainedParam.getP());
			assertEquals(expectedParam.getT(), obtainedParam.getT());
			assertEquals(expectedParam.getMinGuess(), obtainedParam.getMinGuess());
			assertEquals(expectedParam.getMaxGuess(), obtainedParam.getMaxGuess());
			assertEquals(expectedParam.getCategory(), obtainedParam.getCategory());
			assertEquals(expectedParam.getUnit(), obtainedParam.getUnit());
			assertEquals(expectedParam.getDescription(), obtainedParam.getDescription());
			assertEquals(expectedParam.getAllCorrelations(), obtainedParam.getAllCorrelations());
		}
	}
}
