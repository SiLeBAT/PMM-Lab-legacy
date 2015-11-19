package de.bund.bfr.knime.pmm.annotation.sbml;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.sbml.PrimaryModelNode;

public class PrimaryModelNodeTest {

	@Test
	public void test() {
		PrimaryModelNode pmn = new PrimaryModelNode("salmonella.sbml");
		PrimaryModelNode pmn2 = new PrimaryModelNode(pmn.getNode());
		assertEquals("salmonella.sbml", pmn2.getPrimaryModel());
	}

}
