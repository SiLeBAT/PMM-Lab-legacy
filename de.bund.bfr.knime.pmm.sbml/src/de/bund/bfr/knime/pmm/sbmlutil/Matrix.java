/**
 * Pmm Lab matrix.
 * 
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import groovy.util.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.hsh.bfr.db.DBKernel;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.MatrixXml;

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
		// Gets non RDF annotation and turns it into a MatrixAnnotation
		XMLNode nonRDFAnnotation = compartment.getAnnotation()
				.getNonRDFannotation();
		MatrixAnnotation matrixAnnotation = new MatrixAnnotation(
				nonRDFAnnotation);

		// Gets matrix data from matrixAnnotation
		code = matrixAnnotation.getCode();
		miscs = matrixAnnotation.getVars();
		details = matrixAnnotation.getDetails();
		this.compartment = compartment;
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
		// Builds compartment
		compartment = new Compartment(Util.createId(matrixXml.getName()));
		compartment.setConstant(true);
		compartment.setName(matrixXml.getName());

		// Gets PMF code from DB
		String[] colNames = { "CodeSystem", "Basis" };
		String[] colVals = { "PMF", matrixXml.getId().toString() };
		code = (String) DBKernel.getValue(null, "Codes_Matrices", colNames,
				colVals, "Code");

		// Copies model variables and description
		this.miscs = miscs;
		details = matrixXml.getDetail();

		// Builds and sets non RDF annotation
		XMLNode annot = new MatrixAnnotation(code, miscs, details).getNode();
		compartment.getAnnotation().setNonRDFAnnotation(annot);
	}

	// Getters
	public Compartment getCompartment() {
		return compartment;
	}

	public String getCode() {
		return code;
	}

	public Map<String, Double> getMiscs() {
		return miscs;
	}

	// Create MatrixXml
	public MatrixXml toMatrixXml() {
		// Get matrix DB id
		String[] colNames = { "CodeSystem", "Code" };
		String[] colVals = { "PMF", code };
		int id = (int) DBKernel.getValue(null, "Codes_Matrices", colNames,
				colVals, "Basis");

		// Get matrix dbuuid
		String dbuuid = DBKernel.getLocalDBUUID();

		return new MatrixXml(id, compartment.getName(), details, dbuuid);
	}

	/**
	 * Creates Groovy node.
	 */
	public Node toGroovyNode() {
		return new Node(null, "sbml:compartment",
				compartment.writeXMLAttributes());
	}
}

/** Matrix non RDF annotation. Holds matrix's PMF code and model variables */
class MatrixAnnotation {
	static String METADATA_TAG = "metadata";
	static String PMF_TAG = "pmf";
	static String CODE_TAG = "source"; // PMF code tag
	static String CODE_NS = "dc"; // PMF code namespace
	static String DETAILS_TAG = "details"; // description tag
	static String DETAILS_NS = "pmf"; // description namespace
	static String VARIABLE_TAG = "modelvariable"; // model variable tag
	static String VARIABLE_NS = "pmml"; // model variable namespace

	// Attribute names of a variable element
	static String VARIABLE_NAME = "name";
	static String VARIABLE_VALUE = "value";

	XMLNode node;
	String code; // PMF code
	String details; // Matrix details
	Map<String, Double> miscs; // model variables

	/**
	 * Gets PMF code, description and model variables from an matrix annotation.
	 * 
	 * @param node
	 *            XMLNode.
	 */
	public MatrixAnnotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");

		// Gets PMF code
		code = metadata.getChildElement(CODE_TAG, "").getChild(0)
				.getCharacters();

		// Gets matrix details
		XMLNode detailsNode = metadata.getChildElement(DETAILS_TAG, "");
		if (detailsNode != null) {
			details = detailsNode.getChild(0).getCharacters();
		}

		// Gets model variables
		miscs = new HashMap<>();
		for (XMLNode varNode : metadata.getChildElements(VARIABLE_TAG, "")) {
			XMLAttributes attrs = varNode.getAttributes();
			String varName = attrs.getValue(VARIABLE_NAME);
			Double varValue;
			// If varNode has a value then parses it
			if (attrs.hasAttribute(VARIABLE_VALUE)) {
				varValue = Double.parseDouble(attrs.getValue(VARIABLE_VALUE));
			}
			// Otherwise, if empty string assigns a null value
			else {
				varValue = null;
			}
			miscs.put(varName, varValue);
		}
	}

	/**
	 * Creates MatrixAnnotation for a code, description and model variables.
	 * 
	 * @param code
	 *            PMF code.
	 * @param vars
	 *            Map of model variables with names as their keys.
	 * @param details
	 *            Matrix description.
	 */
	public MatrixAnnotation(String code, Map<String, Double> vars,
			String details) {
		this.code = code;
		miscs = vars;
		this.details = details;

		// Builds metadata node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));

		// Creates annotation for PMF code
		XMLNode codeNode = new XMLNode(new XMLTriple(CODE_TAG, null, CODE_NS));
		codeNode.addChild(new XMLNode(code));
		node.addChild(codeNode);

		// Creates annotations for model variables (Temperature, pH, aW)
		for (Entry<String, Double> entry : miscs.entrySet()) {
			XMLTriple triple = new XMLTriple(VARIABLE_TAG, null, VARIABLE_NS);
			XMLAttributes attrs = new XMLAttributes();
			attrs.add(VARIABLE_NAME, entry.getKey());
			if (entry.getValue() != null) {
				attrs.add(VARIABLE_VALUE, entry.getValue().toString());
			}

			node.addChild(new XMLNode(triple, attrs));
		}

		// Creates annotation for matrix details
		if (details != null) {
			XMLNode detailsNode = new XMLNode(new XMLTriple(DETAILS_TAG, null,
					DETAILS_NS));
			detailsNode.addChild(new XMLNode(details));
			node.addChild(detailsNode);
		}
	}

	// Getters
	public XMLNode getNode() {
		return node;
	}

	public String getCode() {
		return code;
	}

	public Map<String, Double> getVars() {
		return miscs;
	}

	public String getDetails() {
		return details;
	}
}