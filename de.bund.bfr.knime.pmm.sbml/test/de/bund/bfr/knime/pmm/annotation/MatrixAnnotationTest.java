package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MatrixAnnotationTest {

	@Test
	public void test() {
		String pmfCode = "21103";
		String matrixDetails = "broth";
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

	@Test
	public void testMissingPMFCode() {
		String matrixDetails = "broth";
		Map<String, Double> miscs = new HashMap<>();
		miscs.put("Temperature", 10.0);
		miscs.put("pH", 5.63);

		MatrixAnnotation ma = new MatrixAnnotation(null, miscs, matrixDetails);
		MatrixAnnotation ma2 = new MatrixAnnotation(ma.getAnnotation());
		assertNull(ma2.getPmfCode());
	}

	@Test
	public void testMissingMatrixDetail() {
		String pmfCode = "21103";
		Map<String, Double> miscs = new HashMap<>();
		miscs.put("Temperature", 10.0);
		miscs.put("pH", 5.63);
		
		MatrixAnnotation ma = new MatrixAnnotation(pmfCode, miscs, null);
		MatrixAnnotation ma2 = new MatrixAnnotation(ma.getAnnotation());
		assertNull(ma2.getDetails());
	}
	
	@Test
	public void testMissingMiscs() {
		String pmfCode = "21103";
		String matrixDetails = "broth";
		
		MatrixAnnotation ma = new MatrixAnnotation(pmfCode, null, matrixDetails);
		MatrixAnnotation ma2 = new MatrixAnnotation(ma.getAnnotation());
		assertNull(ma2.getVars());
	}
}
