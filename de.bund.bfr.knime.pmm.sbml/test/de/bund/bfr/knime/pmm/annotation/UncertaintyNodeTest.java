package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.sbmlutil.Uncertainties;

public class UncertaintyNodeTest {

	@Test
	public void test() {
		EstModelXml estModel = new EstModelXml(1, "myexp", 0.5, 0.8, 0.2, 0.3, 0.2, 2);
		estModel.setComment("old experiment");
		Uncertainties uncertainties = new Uncertainties(estModel);

		UncertaintyNode un = new UncertaintyNode(uncertainties);
		Uncertainties uncertainties2 = un.getMeasures();

		assertEquals(uncertainties, uncertainties2);
	}

	@Test
	public void testNullValues() {
		EstModelXml estModel = new EstModelXml(1, "", null, null, null, null, null, null);
		estModel.setComment("");
		Uncertainties uncertainties = new Uncertainties(estModel);

		UncertaintyNode un = new UncertaintyNode(uncertainties);
		Uncertainties uncertainties2 = un.getMeasures();

		assertEquals(uncertainties, uncertainties2);
	}

	@Test
	public void testNode() {
		EstModelXml estModel = new EstModelXml(1, "myexp", 0.5, 0.8, 0.2, 0.3, 0.2, 2);
		estModel.setComment("old experiment");

		Uncertainties uncertainties = new Uncertainties(estModel);
		UncertaintyNode un = new UncertaintyNode(uncertainties);

		UncertaintyNode un2 = new UncertaintyNode(un.getNode());
		Uncertainties uncertainties2 = un2.getMeasures();

		assertEquals(uncertainties, uncertainties2);
	}

}
