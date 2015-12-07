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
