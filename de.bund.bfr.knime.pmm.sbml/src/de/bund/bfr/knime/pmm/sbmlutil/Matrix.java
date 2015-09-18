/**
 * Pmm Lab matrix.
 * 
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.Map;

import org.hsh.bfr.db.DBKernel;
import org.sbml.jsbml.Compartment;

import de.bund.bfr.knime.pmm.annotation.MatrixAnnotation;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

/**
 * Matrix with PMF code, description and model variables.
 * 
 * @author Miguel Alba
 */
public class Matrix {
	Compartment compartment;

	String code; // PMF code
	String details; // Matrix details
	Map<String, Double> miscs; // Model variables

	/**
	 * Creates a Matrix from an existing SBML compartment.
	 * 
	 * @param compartment
	 *            SBML Compartment with a MatrixAnnotation.
	 */
	public Matrix(Compartment compartment) {
		this.compartment = compartment;

		if (compartment.isSetAnnotation()) {
			MatrixAnnotation annot = new MatrixAnnotation(compartment.getAnnotation());
			code = annot.getPmfCode();
			miscs = annot.getVars();
			details = annot.getDetails();
		}
	}

	/**
	 * Creates a Matrix from a Pmm Lab MatrixXml and model variables.
	 * 
	 * @param matrixXml
	 *            Pmm Lab MatrixXml.
	 * @param miscs
	 *            Map of model variables, with their names as keys.
	 */
	public Matrix(MatrixXml matrixXml, Map<String, Double> miscs) {
		if (matrixXml.getName() == null) {
			compartment = new Compartment("MISSING_COMPARTMENT");
			compartment.setConstant(true);
			compartment.setName("MISSING_COMPARTMENT");
			this.miscs = miscs;
		} else {
			// Builds compartment
			compartment = new Compartment(Util.createId(matrixXml.getName()));
			compartment.setConstant(true);
			compartment.setName(matrixXml.getName());

			// Gets PMF code from DB
			String[] colNames = { "CodeSystem", "Basis" };
			String[] colVals = { "PMF", matrixXml.getId().toString() };
			code = (String) DBKernel.getValue(null, "Codes_Matrices", colNames, colVals, "Code");

			// Copies model variables and description
			this.miscs = miscs;
			details = matrixXml.getDetail();

			// Builds and sets non RDF annotation
			compartment.setAnnotation(new MatrixAnnotation(code, miscs, details).getAnnotation());
		}
	}

	// Getters
	public Compartment getCompartment() {
		return compartment;
	}

	public String getCode() {
		return code;
	}
	
	public String getDetails() {
		return details;
	}

	public Map<String, Double> getMiscs() {
		return miscs;
	}

	// Create MatrixXml
	public MatrixXml toMatrixXml() {
		if (code == null) {
			return new MatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(), details, null);
		} else {
			// Get matrix DB id
			String[] colNames = { "CodeSystem", "Code" };
			String[] colVals = { "PMF", code };
			int id = (int) DBKernel.getValue(null, "Codes_Matrices", colNames, colVals, "Basis");

			// Get matrix dbuuid
			String dbuuid = DBKernel.getLocalDBUUID();
			return new MatrixXml(id, compartment.getName(), details, dbuuid);
		}
	}
}