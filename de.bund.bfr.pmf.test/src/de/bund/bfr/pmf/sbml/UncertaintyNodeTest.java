package de.bund.bfr.pmf.sbml;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class UncertaintyNodeTest {

	private Uncertainties exampleUncertainties;

	@Before
	public void setUp() {
		final int id = 101;
		final String modelName = "BacillusCereus_CultureMedium";
		final String comment = "uncertainties";
		final double r2 = 0.996;
		final double rms = 0.345;
		final double sse = 1.909;
		final double aic = -32.977;
		final double bic = -34.994;
		final int dof = 16;

		exampleUncertainties = SBMLFactory.createUncertainties(id, modelName, comment, r2, rms, sse, aic, bic, dof);
	}

	@Test
	public void testId() {
		Uncertainties uncertainties = SBMLFactory.createUncertainties(exampleUncertainties.getID(), null, null, null,
				null, null, null, null, null);

		UncertaintyNode node = new UncertaintyNode(uncertainties);
		node = new UncertaintyNode(node.getNode());
		assertEquals(exampleUncertainties.getID(), node.getMeasures().getID());
	}

	@Test
	public void testModelName() {
		Uncertainties uncertainties = SBMLFactory.createUncertainties(null, exampleUncertainties.getModelName(), null,
				null, null, null, null, null, null);

		UncertaintyNode node = new UncertaintyNode(uncertainties);
		node = new UncertaintyNode(node.getNode());
		assertEquals(exampleUncertainties.getModelName(), node.getMeasures().getModelName());
	}

	@Test
	public void testComment() {
		Uncertainties uncertainties = SBMLFactory.createUncertainties(null, null, exampleUncertainties.getComment(),
				null, null, null, null, null, null);

		UncertaintyNode node = new UncertaintyNode(uncertainties);
		node = new UncertaintyNode(node.getNode());
		assertEquals(exampleUncertainties.getComment(), node.getMeasures().getComment());
	}

	@Test
	public void testR2() {
		Uncertainties uncertainties = SBMLFactory.createUncertainties(null, null, null, exampleUncertainties.getR2(),
				null, null, null, null, null);

		UncertaintyNode node = new UncertaintyNode(uncertainties);
		node = new UncertaintyNode(node.getNode());
		assertEquals(exampleUncertainties.getR2(), node.getMeasures().getR2());
	}

	@Test
	public void testRMS() {
		Uncertainties uncertainties = SBMLFactory.createUncertainties(null, null, null, null,
				exampleUncertainties.getRMS(), null, null, null, null);

		UncertaintyNode node = new UncertaintyNode(uncertainties);
		node = new UncertaintyNode(node.getNode());
		assertEquals(exampleUncertainties.getRMS(), node.getMeasures().getRMS());
	}

	@Test
	public void testSSE() {
		Uncertainties uncertainties = SBMLFactory.createUncertainties(null, null, null, null, null,
				exampleUncertainties.getSSE(), null, null, null);

		UncertaintyNode node = new UncertaintyNode(uncertainties);
		node = new UncertaintyNode(node.getNode());
		assertEquals(exampleUncertainties.getSSE(), node.getMeasures().getSSE());
	}

	@Test
	public void testAIC() {
		Uncertainties uncertainties = SBMLFactory.createUncertainties(exampleUncertainties.getID(), null, null, null,
				null, null, exampleUncertainties.getAIC(), null, null);

		UncertaintyNode node = new UncertaintyNode(uncertainties);
		node = new UncertaintyNode(node.getNode());
		assertEquals(exampleUncertainties.getAIC(), node.getMeasures().getAIC());
	}

	@Test
	public void testBIC() {
		Uncertainties uncertainties = SBMLFactory.createUncertainties(exampleUncertainties.getID(), null, null, null,
				null, null, null, exampleUncertainties.getBIC(), null);

		UncertaintyNode node = new UncertaintyNode(uncertainties);
		node = new UncertaintyNode(node.getNode());
		assertEquals(exampleUncertainties.getBIC(), node.getMeasures().getBIC());
	}

	@Test
	public void tetsDOF() {
		Uncertainties uncertainties = SBMLFactory.createUncertainties(exampleUncertainties.getID(), null, null, null,
				null, null, null, null, exampleUncertainties.getDOF());

		UncertaintyNode node = new UncertaintyNode(uncertainties);
		node = new UncertaintyNode(node.getNode());
		assertEquals(exampleUncertainties.getDOF(), node.getMeasures().getDOF());
	}
}
