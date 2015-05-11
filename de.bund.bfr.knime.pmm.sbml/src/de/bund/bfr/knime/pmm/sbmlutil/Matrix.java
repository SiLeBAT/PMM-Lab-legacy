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

public class Matrix {
	private Compartment compartment;

	private String code; // PMF code
	private String details; // Matrix details
	private Map<String, Double> miscs;

	public Matrix(Compartment compartment) {
		XMLNode nonRDFAnnotation = compartment.getAnnotation()
				.getNonRDFannotation();
		MatrixAnnotation matrixAnnotation = new MatrixAnnotation(
				nonRDFAnnotation);
		code = matrixAnnotation.getCode();
		miscs = matrixAnnotation.getVars();
		details = matrixAnnotation.getDetails();
		this.compartment = compartment;
	}

	/** Build a PMM Lab Matrix from a MatrixXml */
	public Matrix(MatrixXml matrixXml, Map<String, Double> miscs) {
		// Build compartment
		compartment = new Compartment(Util.createId(matrixXml.getName()));
		compartment.setConstant(true);
		compartment.setName(matrixXml.getName());

		// Get PMF code from DB
		String[] colNames = { "CodeSystem", "Basis" };
		String[] colVals = { "PMF", matrixXml.getId().toString() };
		code = (String) DBKernel.getValue(null, "Codes_Matrices", colNames,
				colVals, "Code");

		this.miscs = miscs;
		details = matrixXml.getDetail();

		// Build and set non RDF annotation
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

	public Node toGroovyNode() {
		return new Node(null, "sbml:compartment",
				compartment.writeXMLAttributes());
	}
}

// Matrix non RDF annotation. Holds matrix's PMF code and model variables
class MatrixAnnotation {
	private static String METADATA_TAG = "metadata";
	private static String PMF_TAG = "pmf";
	private static String CODE_TAG = "source";
	private static String CODE_NS = "dc";
	private static String DETAILS_TAG = "details";
	private static String DETAILS_NS = "pmf";
	private static String VARIABLE_TAG = "modelvariable";
	private static String VARIABLE_NS = "pmml";

	// Attribute names of a variable element
	private static String VARIABLE_NAME = "name";
	private static String VARIABLE_VALUE = "value";

	private XMLNode node;
	private String code; // PMF code
	private String details; // Matrix details
	private Map<String, Double> miscs; // model variables

	// Get PMF code and model variable from an matrix annotation
	public MatrixAnnotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");

		// Get PMF code
		code = metadata.getChildElement(CODE_TAG, "").getChild(0)
				.getCharacters();

		// Get matrix details
		XMLNode detailsNode = metadata.getChildElement(DETAILS_TAG, "");
		if (detailsNode != null) {
			details = detailsNode.getChild(0).getCharacters();
		}

		// Get model variables
		miscs = new HashMap<>();
		for (XMLNode varNode : metadata.getChildElements(VARIABLE_TAG, "")) {
			XMLAttributes attrs = varNode.getAttributes();
			String varName = attrs.getValue(VARIABLE_NAME);
			Double varValue;
			// If varNode has a value then parse it
			if (attrs.hasAttribute(VARIABLE_VALUE)) {
				varValue = Double.parseDouble(attrs.getValue(VARIABLE_VALUE));
			}
			// Otherwise, if empty string assign a null value
			else {
				varValue = null;
			}
			miscs.put(varName, varValue);
		}
	}

	// Build new matrix annotation
	public MatrixAnnotation(String code, Map<String, Double> vars,
			String details) {
		this.code = code;
		miscs = vars;
		this.details = details;

		// Build metadata node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));

		// Create annotation for PMF code
		XMLNode codeNode = new XMLNode(new XMLTriple(CODE_TAG, null, CODE_NS));
		codeNode.addChild(new XMLNode(code));
		node.addChild(codeNode);

		// Create annotations for model variables (Temperature, pH, aW)
		for (Entry<String, Double> entry : miscs.entrySet()) {
			XMLTriple triple = new XMLTriple(VARIABLE_TAG, null, VARIABLE_NS);
			XMLAttributes attrs = new XMLAttributes();
			attrs.add(VARIABLE_NAME, entry.getKey());
			if (entry.getValue() != null) {
				attrs.add(VARIABLE_VALUE, entry.getValue().toString());
			}

			node.addChild(new XMLNode(triple, attrs));
		}

		// Create annotation for matrix details
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