package de.bund.bfr.knime.pmm.sbml.sbmlutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.sbmlutil.Uncertainties;

public class UncertaintiesTest {

	@Test
	public void testCreateEstModel() {

		EstModelXml estModel = new EstModelXml(0, "estModel", 1.90, 0.34, 0.99, -32.97, -32.0, 16);
		estModel.setComment("test");
		estModel.setQualityScore(0);
		Uncertainties expectedUncertainties = new Uncertainties(estModel);

		Uncertainties obtainedUncertainties = new Uncertainties(0, "estModel", "test", 0.99, 0.34, 1.90, -32.97, -32.0, 16);

		assertEquals(expectedUncertainties, obtainedUncertainties);
	}

	@Test
	public void testComparisonDifferentName() {
		EstModelXml estModelA = new EstModelXml(-1, "estModelA", 0.0, 0.0, 0.0, 0.0, 0.0, 0);
		estModelA.setComment("test");
		EstModelXml estModelB = new EstModelXml(-1, "estModelB", 0.0, 0.0, 0.0, 0.0, 0.0, 0);
		estModelB.setComment("test");

		Uncertainties uncertaintiesA = new Uncertainties(estModelA);
		Uncertainties uncertaintiesB = new Uncertainties(estModelB);
		
		assertFalse(uncertaintiesA.equals(uncertaintiesB));
	}

	@Test
	public void testComparisonDifferentId() {
		EstModelXml estModelA = new EstModelXml(-1, "", 0.0, 0.0, 0.0, 0.0, 0.0, 0);
		estModelA.setComment("test");
		EstModelXml estModelB = new EstModelXml(-2, "", 0.0, 0.0, 0.0, 0.0, 0.0, 0);
		estModelB.setComment("test");

		Uncertainties uncertaintiesA = new Uncertainties(estModelA);
		Uncertainties uncertaintiesB = new Uncertainties(estModelB);

		assertFalse(uncertaintiesA.equals(uncertaintiesB));
	}

	@Test
	public void testComparisonDifferentComment() {
		EstModelXml estModelA = new EstModelXml(-1, "", 0.0, 0.0, 0.0, 0.0, 0.0, 0);
		estModelA.setComment("TestA");
		EstModelXml estModelB = new EstModelXml(-1, "", 0.0, 0.0, 0.0, 0.0, 0.0, 0);
		estModelB.setComment("TestB");

		Uncertainties uncertaintiesA = new Uncertainties(estModelA);
		Uncertainties uncertaintiesB = new Uncertainties(estModelB);

		assertFalse(uncertaintiesA.equals(uncertaintiesB));
	}

	@Test
	public void testComparisonDifferentR2() {
		EstModelXml estModelA = new EstModelXml(-1, "", 0.0, 0.0, 0.5, 0.0, 0.0, 0);
		estModelA.setComment("test");
		EstModelXml estModelB = new EstModelXml(-1, "", 0.0, 0.0, 0.9, 0.0, 0.0, 0);
		estModelB.setComment("test");

		Uncertainties uncertaintiesA = new Uncertainties(estModelA);
		Uncertainties uncertaintiesB = new Uncertainties(estModelB);

		assertFalse(uncertaintiesA.equals(uncertaintiesB));
	}

	@Test
	public void testComparisonDifferentRms() {
		EstModelXml estModelA = new EstModelXml(-1, "", 0.0, 0.1, 0.0, 0.0, 0.0, 0);
		estModelA.setComment("test");
		EstModelXml estModelB = new EstModelXml(-1, "", 0.0, 0.9, 0.0, 0.0, 0.0, 0);
		estModelB.setComment("test");

		Uncertainties uncertaintiesA = new Uncertainties(estModelA);
		Uncertainties uncertaintiesB = new Uncertainties(estModelB);

		assertFalse(uncertaintiesA.equals(uncertaintiesB));
	}

	@Test
	public void testComparisonDifferentSse() {
		EstModelXml estModelA = new EstModelXml(-1, "", 0.1, 0.0, 0.0, 0.0, 0.0, 0);
		estModelA.setComment("test");
		EstModelXml estModelB = new EstModelXml(-1, "", 0.9, 0.0, 0.0, 0.0, 0.0, 0);
		estModelB.setComment("test");

		Uncertainties uncertaintiesA = new Uncertainties(estModelA);
		Uncertainties uncertaintiesB = new Uncertainties(estModelB);

		assertFalse(uncertaintiesA.equals(uncertaintiesB));
	}

	@Test
	public void testComparisonDifferentAic() {
		EstModelXml estModelA = new EstModelXml(-1, "", 0.0, 0.0, 0.0, 0.1, 0.0, 0);
		estModelA.setComment("test");
		EstModelXml estModelB = new EstModelXml(-1, "", 0.0, 0.0, 0.0, 0.9, 0.0, 0);
		estModelB.setComment("test");

		Uncertainties uncertaintiesA = new Uncertainties(estModelA);
		Uncertainties uncertaintiesB = new Uncertainties(estModelB);

		assertFalse(uncertaintiesA.equals(uncertaintiesB));
	}

	@Test
	public void testComparisonDifferentBic() {
		EstModelXml estModelA = new EstModelXml(-1, "", 0.0, 0.0, 0.0, 0.0, 0.1, 0);
		estModelA.setComment("test");
		EstModelXml estModelB = new EstModelXml(-1, "", 0.0, 0.0, 0.0, 0.0, 0.9, 0);
		estModelB.setComment("test");

		Uncertainties uncertaintiesA = new Uncertainties(estModelA);
		Uncertainties uncertaintiesB = new Uncertainties(estModelB);

		assertFalse(uncertaintiesA.equals(uncertaintiesB));
	}

	@Test
	public void testComparisonDifferentDof() {
		EstModelXml estModelA = new EstModelXml(-1, "", 0.0, 0.0, 0.0, 0.0, 0.0, 1);
		estModelA.setComment("test");
		EstModelXml estModelB = new EstModelXml(-1, "", 0.0, 0.0, 0.0, 0.0, 0.0, 9);
		estModelB.setComment("test");

		Uncertainties uncertaintiesA = new Uncertainties(estModelA);
		Uncertainties uncertaintiesB = new Uncertainties(estModelB);

		assertFalse(uncertaintiesA.equals(uncertaintiesB));
	}
}
