package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimaryModelNodeTest {

	@Test
	public void test() {
		PrimaryModelNode pmn = new PrimaryModelNode("salmonella.sbml");
		PrimaryModelNode pmn2 = new PrimaryModelNode(pmn.getNode());
		assertEquals("salmonella.sbml", pmn2.getPrimaryModel());
	}

}
