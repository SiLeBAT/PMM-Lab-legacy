package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

public class CreatorNodeTest {

	@Test
	public void test() {
		CreatorNode cn = new CreatorNode("Mr", "Bond", "James Bond");
		CreatorNode cn2 = new CreatorNode(cn.getNode());
		assertEquals("Mr. Bond. James Bond", cn2.getCreator());
	}

}
