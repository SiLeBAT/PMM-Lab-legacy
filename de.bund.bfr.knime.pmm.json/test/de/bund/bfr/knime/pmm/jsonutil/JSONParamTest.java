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
