package de.bund.bfr.knime.pmm.annotation.numl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

public class CondIDNuMLNodeTest {

	@SuppressWarnings("static-method")
	@Test
	public void test() {
		CondIDNuMLNode cin = null;
		try {
			cin = new CondIDNuMLNode(1);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			fail();
		}
		CondIDNuMLNode cin2 = new CondIDNuMLNode(cin.getNode());
		assertEquals(1, cin2.getCondId());
	}

}
