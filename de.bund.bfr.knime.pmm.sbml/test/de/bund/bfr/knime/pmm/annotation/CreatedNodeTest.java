package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

public class CreatedNodeTest {

	@Test
	public void test() {
		CreatedNode cnt = new CreatedNode("21.03.89");
		CreatedNode cn2 = new CreatedNode(cnt.getNode());
		assertEquals("21.03.89", cn2.getCreated());
	}
}
