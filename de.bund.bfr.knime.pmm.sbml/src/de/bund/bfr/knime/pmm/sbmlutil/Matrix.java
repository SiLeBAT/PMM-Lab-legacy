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

		XMLNode nonRDFannot = compartment.getAnnotation().getNonRDFannotation();
		if (nonRDFannot != null) {
			// Gets non RDF annotation and turns it into a MatrixAnnotation
			MatrixAnnotation annot = new MatrixAnnotation(nonRDFannot);
			// Gets matrix data from matrixAnnotation
			code = annot.getCode();
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
			code = (String) DBKernel.getValue(null, "Codes_Matrices", colNames,
					colVals, "Code");

			// Copies model variables and description
			this.miscs = miscs;
			details = matrixXml.getDetail();

			// Builds and sets non RDF annotation
			XMLNode annot = new MatrixAnnotation(code, miscs, details)
					.getNode();
			compartment.getAnnotation().setNonRDFAnnotation(annot);
		}
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
		if (code == null) {
			return new MatrixXml(MathUtilities.getRandomNegativeInt(),
					compartment.getName(), null, null);
		} else {
			// Get matrix DB id
			String[] colNames = { "CodeSystem", "Code" };
			String[] colVals = { "PMF", code };
			int id = (int) DBKernel.getValue(null, "Codes_Matrices", colNames,
					colVals, "Basis");

			// Get matrix dbuuid
			String dbuuid = DBKernel.getLocalDBUUID();
			return new MatrixXml(id, compartment.getName(), details, dbuuid);
		}
	}

	/**
	 * Creates Groovy node.
	 */
	public Node toGroovyNode() {
		Map<String, String> attrs = compartment.writeXMLAttributes();
		attrs.put("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		attrs.put("xmlns:pmmlab", "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");

		Node node = new Node(null, "sbml:compartment", attrs);
		if (node != null) {
			node.appendNode("dc:source", code);
		}
		if (details != null) {
			node.appendNode("pmmlab:detail", details);
		}
		for (Entry<String, Double> entry : miscs.entrySet()) {
			Map<String, String> modelVariable = new HashMap<>();
			modelVariable.put("name", entry.getKey());
			modelVariable.put("value", entry.getValue().toString());
			node.appendNode("pmmlab:modelvariable", modelVariable);
		}
		return node;
	}
}

/** Matrix non RDF annotation. Holds matrix's PMF code and model variables */
class MatrixAnnotation {

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
		XMLNode metadata = node.getChildElement("metadata", "");

		// Gets PMF code
		code = metadata.getChildElement("source", "").getChild(0)
				.getCharacters();

		// Gets matrix details
		XMLNode detailsNode = metadata.getChildElement("detail", "");
		if (detailsNode != null) {
			details = detailsNode.getChild(0).getCharacters();
		}

		// Gets model variables
		miscs = new HashMap<>();
		for (XMLNode varNode : metadata.getChildElements("environment", "")) {
			XMLAttributes attrs = varNode.getAttributes();
			String varName = attrs.getValue("name");
			Double varValue;
			// If varNode has a value then parses it
			if (attrs.hasAttribute("value")) {
				varValue = Double.parseDouble(attrs.getValue("value"));
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
		node = new XMLNode(new XMLTriple("metadata", null, "pmf"));

		// Creates annotation for PMF code
		XMLNode codeNode = new XMLNode(new XMLTriple("source", null, "dc"));
		codeNode.addChild(new XMLNode(code));
		node.addChild(codeNode);

		// Creates annotation for matrix details
		if (details != null) {
			XMLTriple detailTriple = new XMLTriple("detail", null, "pmmlab");
			XMLNode detailNode = new XMLNode(detailTriple);
			detailNode.addChild(new XMLNode(details));
			node.addChild(detailNode);
		}

		// Creates annotations for model variables (Temperature, pH, aW)
		for (Entry<String, Double> entry : miscs.entrySet()) {
			XMLTriple triple = new XMLTriple("environment", null, "pmmlab");
			XMLAttributes attrs = new XMLAttributes();
			attrs.add("name", entry.getKey());
			if (entry.getValue() != null) {
				attrs.add("value", entry.getValue().toString());
			}
			node.addChild(new XMLNode(triple, attrs));
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