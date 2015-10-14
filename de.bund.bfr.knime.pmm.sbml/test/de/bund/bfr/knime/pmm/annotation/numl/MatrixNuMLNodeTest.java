package de.bund.bfr.knime.pmm.annotation.numl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;

public class MatrixNuMLNodeTest {

	@SuppressWarnings("static-method")
	@Test
	public void test() {
		String name = "Culture medium";
		String detail = "broth";

		MatrixXml matrixXml = new MatrixXml();
		matrixXml.setName(name);
		matrixXml.setDetail(detail);

		Matrix matrix = new Matrix(matrixXml, new HashMap<String, Double>());

		try {
			MatrixNuMLNode node = new MatrixNuMLNode(matrix);
			MatrixXml matrixXml2 = node.toMatrixXml();
			assertEquals(name, matrixXml2.getName());
			assertEquals(detail, matrixXml2.getDetail());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			fail();
		}
	}

}
