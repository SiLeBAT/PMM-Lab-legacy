package de.bund.bfr.pmf.sbml;

import static org.junit.Assert.*;

import org.junit.Test;

public class SecDepTest {

	@Test
	public void test() {
		SecDep secDep = new SecDep("mu_max", "sec dep", "dimensionless");
		secDep = new SecDep(secDep.getParam());
		assertEquals("sec dep", secDep.getDescription());
	}
}
