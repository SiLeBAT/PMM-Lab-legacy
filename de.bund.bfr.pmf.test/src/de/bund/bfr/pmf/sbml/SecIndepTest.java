package de.bund.bfr.pmf.sbml;

import static org.junit.Assert.*;

import org.junit.Test;

public class SecIndepTest {

	@Test
	public void test() {
		SecIndep secIndep = new SecIndep("mu_max", "variable", "dimensionless");
		secIndep = new SecIndep(secIndep.getParam());
		assertEquals("variable", secIndep.getDescription());
	}
}
