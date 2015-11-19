package de.bund.bfr.knime.pmm.annotation.sbml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.sbml.MatrixAnnotation;

public class MatrixAnnotationTest {

	private String pmfCode;
	private String matrixDetails;

	@Before
	public void setUp() {
		pmfCode = "21103";
		matrixDetails = "broth";
	}

	/**
	 * Tests equality for a MatrixAnnotation with a PMF code, matrix details and
	 * model variables (one of them with a null value).
	 */
	@Test
	public void test() {
		Map<String, Double> miscs = new HashMap<>();
		miscs.put("Temperature", 10.0);
		miscs.put("pH", 5.63);
		miscs.put("wA", null);

		MatrixAnnotation ma = new MatrixAnnotation(pmfCode, miscs, matrixDetails);
		MatrixAnnotation ma2 = new MatrixAnnotation(ma.getAnnotation());
		assertEquals(pmfCode, ma2.getPmfCode());
		assertEquals(matrixDetails, ma2.getDetails());
		assertEquals(miscs, ma2.getVars());
	}

	/**
	 * Tests equality for a MatrixAnnotation with no PMF code.
	 */
	@Test
	public void testMissingPMFCode() {
		Map<String, Double> miscs = new HashMap<>();
		miscs.put("Temperature", 10.0);
		miscs.put("pH", 5.63);

		MatrixAnnotation ma = new MatrixAnnotation(null, miscs, matrixDetails);
		MatrixAnnotation ma2 = new MatrixAnnotation(ma.getAnnotation());
		assertNull(ma2.getPmfCode());
	}

	/**
	 * Tests that a MatrixAnnotation non initialized details are null.
	 */
	@Test
	public void testMissingMatrixDetail() {
		Map<String, Double> miscs = new HashMap<>();
		miscs.put("Temperature", 10.0);
		miscs.put("pH", 5.63);

		MatrixAnnotation ma = new MatrixAnnotation(pmfCode, miscs, null);
		MatrixAnnotation ma2 = new MatrixAnnotation(ma.getAnnotation());
		assertNull(ma2.getDetails());
	}

	@Test
	/**
	 * Tests that a MatrixAnnotation non initialized model variables are null.
	 */
	public void testMissingMiscs() {
		MatrixAnnotation ma = new MatrixAnnotation(pmfCode, null, matrixDetails);
		MatrixAnnotation ma2 = new MatrixAnnotation(ma.getAnnotation());
		assertNull(ma2.getVars());
	}
}
