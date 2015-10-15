package de.bund.bfr.knime.pmm.annotation.sbml;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.sbml.UncertaintyNode;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.sbmlutil.Uncertainties;

public class UncertaintyNodeTest {
	
	private Uncertainties uncertainties;

	@Before
	public void setUp() {
		EstModelXml estModel = new EstModelXml(1, "myexp", 0.5, 0.8, 0.2, 0.3, 0.2, 2);
		estModel.setComment("old experiment");
		uncertainties = new Uncertainties(estModel);
	}

	/**
	 * Tests equality for an UncertaintyNode with non null uncertainties.
	 */
	@Test
	public void test() {
		UncertaintyNode un = new UncertaintyNode(uncertainties);
		Uncertainties uncertainties2 = un.getMeasures();

		assertEquals(uncertainties, uncertainties2);
	}

	/**
	 * Tests equality for an UncertaintyNode with null uncertainties.
	 */
	@Test
	public void testNullValues() {
		EstModelXml estModel = new EstModelXml(1, "", null, null, null, null, null, null);
		estModel.setComment("");
		Uncertainties nullUncertainties = new Uncertainties(estModel);

		UncertaintyNode un = new UncertaintyNode(nullUncertainties);
		Uncertainties uncertainties2 = un.getMeasures();

		assertEquals(nullUncertainties, uncertainties2);
	}

	/**
	 * Tests equality for an UncertaintyNode made of another UncertaintyNode.
	 */
	@Test
	public void testNode() {
		UncertaintyNode un = new UncertaintyNode(uncertainties);
		UncertaintyNode un2 = new UncertaintyNode(un.getNode());
		Uncertainties uncertainties2 = un2.getMeasures();
		assertEquals(uncertainties, uncertainties2);
	}

}
