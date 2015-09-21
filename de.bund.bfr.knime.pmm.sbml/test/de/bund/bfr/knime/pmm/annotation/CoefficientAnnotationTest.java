package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CoefficientAnnotationTest {

	/**
	 * Tests equality for a non null CoefficientAnnotation.
	 */
	@Test
	public void test() {
		double P = 2.0454252780410798E-7;
		double error = 0.19208418678873487;
		double t = 14.004276171669535;
		Map<String, Double> correlations = new HashMap<>();
		correlations.put("lag", 0.4478308631598644);
		correlations.put("Ymax", 0.0);
		correlations.put("k", -6.815517600840838E-19);
		correlations.put("Y0", 0.03689633481428959);
		String desc = "initial bacterial population -log10 transformed - min/max selected to improve fitting";
		
		CoefficientAnnotation ca = new CoefficientAnnotation(P, error, t, correlations, desc);
		CoefficientAnnotation ca2 = new CoefficientAnnotation(ca.getAnnotation());
		
		assertEquals(P, ca2.getP(), 0.0);
		assertEquals(error, ca2.getError(), 0.0);
		assertEquals(t, ca2.getT(), 0.0);
		assertEquals(correlations, ca2.getCorrelations());
		assertEquals(desc, ca2.getDescription());
	}
	
	/**
	 * Tests equality for a null CoefficientAnnotation.
	 */
	@Test
	public void testMissing() {
		CoefficientAnnotation ca = new CoefficientAnnotation(null, null, null, null, null);
		CoefficientAnnotation ca2 = new CoefficientAnnotation(ca.getAnnotation());
		
		assertNull(ca2.getP());
		assertNull(ca2.getError());
		assertNull(ca2.getT());
		assertNull(ca2.getCorrelations());
		assertNull(ca2.getDescription());
	}
}
