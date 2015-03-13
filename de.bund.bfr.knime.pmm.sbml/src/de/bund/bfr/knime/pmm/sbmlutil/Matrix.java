package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.Compartment;

import de.bund.bfr.knime.pmm.common.MatrixXml;

/**
 * Wrapper class of a Pmm Lab matrix.
 * 
 * @author malba
 */
public class Matrix {
	private Compartment compartment;

	public Matrix(Compartment compartment) {
		this.compartment = compartment;
	}

	public Compartment getCompartment() {
		return compartment;
	}

	// Create MatrixXml
	public MatrixXml toMatrixXml() {
		String name = compartment.getName();
		MatrixXml matrix = new MatrixXml();
		matrix.setName(name);
		return matrix;
	}

	/**
	 * Convert a MatrixXml into a Matrix. If the id is missing the matrix will
	 * be assigned a default string.
	 * 
	 * @param matrixXml
	 * @return
	 */
	public static Matrix convertMatrixXmlToMatrix(MatrixXml matrixXml) {
		final String COMPARTMENT_MISSING = "ComparmentMissing";
		String name;
		String id;

		if (matrixXml.getName() == null) {
			id = name = COMPARTMENT_MISSING;
		} else {
			id = createId(matrixXml.getName());
			name = matrixXml.getName();
		}

		Compartment compartment = new Compartment(id);
		compartment.setName(name);
		// Every Pmm Lab matrix is constant
		compartment.setConstant(true);
		return new Matrix(compartment);
	}

	private static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}
}
