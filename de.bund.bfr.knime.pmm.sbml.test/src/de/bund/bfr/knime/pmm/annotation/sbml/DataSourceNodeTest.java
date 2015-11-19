package de.bund.bfr.knime.pmm.annotation.sbml;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.sbml.DataSourceNode;

public class DataSourceNodeTest {

	@Test
	public void test() {
		DataSourceNode dsn = new DataSourceNode("myexp.sbml");
		DataSourceNode dsn2 = new DataSourceNode(dsn.getNode());
		assertEquals("myexp.sbml", dsn2.getFile());
	}
}
