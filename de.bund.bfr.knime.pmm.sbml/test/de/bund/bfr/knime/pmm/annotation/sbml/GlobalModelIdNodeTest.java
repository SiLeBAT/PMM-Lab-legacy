package de.bund.bfr.knime.pmm.annotation.sbml;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.sbml.GlobalModelIdNode;

public class GlobalModelIdNodeTest {

	@Test
	public void test() {
		GlobalModelIdNode gmn = new GlobalModelIdNode(1);
		GlobalModelIdNode gmn2 = new GlobalModelIdNode(gmn.getNode());
		assertEquals(1, gmn2.getGlobalModelId());
	}

}
