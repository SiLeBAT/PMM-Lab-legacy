package de.bund.bfr.knime.pmm.jsonutil;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.MatrixXml;

public class JSONMatrixTest {

	@Test
	public void test() {
		
		Integer id = 21003;
		String name = "culture broth, broth culture, culture medium";
		String detail = "broth";
		String dbuuid = "c6582d2c-7cd6-4ee3-b425-68662b2558d9"; 
		
		MatrixXml matrixXml = new MatrixXml(id, name, detail, dbuuid);
		JSONMatrix jsonMatrix = new JSONMatrix(matrixXml);
		matrixXml = jsonMatrix.toMatrixXml();
		
		// Tests matrixXml
		assertEquals(id, matrixXml.getId());
		assertEquals(name, matrixXml.getName());
		assertEquals(detail, matrixXml.getDetail());
		assertEquals(dbuuid, matrixXml.getDbuuid());
	}
}
