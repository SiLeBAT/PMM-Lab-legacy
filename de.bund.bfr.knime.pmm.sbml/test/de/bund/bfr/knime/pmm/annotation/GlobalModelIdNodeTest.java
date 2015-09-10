package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

public class GlobalModelIdNodeTest {

	@Test
	public void test() {
		GlobalModelIdNode gmn = new GlobalModelIdNode(1);
		GlobalModelIdNode gmn2 = new GlobalModelIdNode(gmn.getNode());
		assertEquals(1, gmn2.getGlobalModelId());
	}

}
