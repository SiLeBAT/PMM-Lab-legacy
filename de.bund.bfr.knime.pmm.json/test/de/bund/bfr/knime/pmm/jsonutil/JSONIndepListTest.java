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
