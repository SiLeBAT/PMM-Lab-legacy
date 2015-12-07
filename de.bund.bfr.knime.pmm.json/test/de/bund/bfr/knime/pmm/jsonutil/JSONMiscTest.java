package de.bund.bfr.knime.pmm.jsonutil;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.MiscXml;

public class JSONMiscTest {

	@Test
	public void test() {

		Integer id = -1;
		String name = "Temperature";
		String description = "Temperature";
		Double value = 10.0;
		List<String> categories = Arrays.asList("Temperature");
		String unit = "°C";
		String origUnit = "°C";
		String dbuuid = "c6582d2c-7cd6-4ee3-b425-68662b2558d9";

		MiscXml miscXml = new MiscXml(id, name, description, value, categories,
				unit, origUnit, dbuuid);
		JSONMisc jsonMisc = new JSONMisc(miscXml);
		miscXml = jsonMisc.toMiscXml();

		assertEquals(id, miscXml.getId());
		assertEquals(name, miscXml.getName());
		assertEquals(description, miscXml.getDescription());
		assertEquals(value, miscXml.getValue());
		assertEquals(categories, miscXml.getCategories());
		assertEquals(unit, miscXml.getUnit());
		assertEquals(origUnit, miscXml.getOrigUnit());
		assertEquals(dbuuid, miscXml.getDbuuid());
	}
}
