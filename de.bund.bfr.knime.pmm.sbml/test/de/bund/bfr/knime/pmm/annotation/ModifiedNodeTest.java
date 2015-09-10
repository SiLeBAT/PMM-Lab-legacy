package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

public class ModifiedNodeTest {

	@Test
	public void test() {
		ModifiedNode mn = new ModifiedNode("07.09.15");
		ModifiedNode mn2 = new ModifiedNode(mn.getNode());
		assertEquals("07.09.15", mn2.getModified());
	}

}
