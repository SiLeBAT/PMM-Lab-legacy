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
