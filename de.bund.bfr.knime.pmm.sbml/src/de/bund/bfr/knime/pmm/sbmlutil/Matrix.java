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
	private Map<String, Double> miscs;

	public Matrix(Compartment compartment) {

		XMLNode annotation = compartment.getAnnotation().getNonRDFannotation();
		XMLNode metadata = annotation.getChildElement("metadata", "");

		// Get PMF code
		code = metadata.getChildElement("source", "").getChild(0)
				.getCharacters();

		// Get misc items
		miscs = new HashMap<>();
		for (XMLNode variableNode : metadata.getChildElements("modelvariable",
				"")) {
			XMLAttributes attrs = variableNode.getAttributes();
			miscs.put(attrs.getValue("name"),
					Double.parseDouble(attrs.getValue("value")));
		}
		
		this.compartment = compartment;
	}

	/** Build a PMM Lab Matrix from a MatrixXml */
	public Matrix(MatrixXml matrixXml, Map<String, Double> miscs) {
		// Build compartment
		compartment = new Compartment(matrixXml.getName().replaceAll("\\W+", " ").trim().replace(" ", "_"));
		compartment.setConstant(true);
		compartment.setName(matrixXml.getName());

		// Get PMF code from DB
		String[] colNames = { "CodeSystem", "Basis" };
		String[] colVals = { "PMF", matrixXml.getId().toString() };
		code = (String) DBKernel.getValue(null, "Codes_Matrices", colNames,
				colVals, "Code");

		this.miscs = miscs;

		// Build annotation
		XMLNode metadata = new XMLNode(new XMLTriple("metadata", null, "pmf"));

		// Add PMF code annotation
		XMLNode sourceNode = new XMLNode(new XMLTriple("source", null, "dc"));
		sourceNode.addChild(new XMLNode(code));
		metadata.addChild(sourceNode);

		// Add model variable (Temperature, pH, aW) annotations
		for (Entry<String, Double> entry : miscs.entrySet()) {
			XMLTriple triple = new XMLTriple("modelvariable", null, "pmml");
			XMLAttributes attrs = new XMLAttributes();
			attrs.add("name", entry.getKey());
			attrs.add("value", entry.getValue().toString());
			metadata.addChild(new XMLNode(triple, attrs));
		}

		compartment.getAnnotation().setNonRDFAnnotation(metadata);
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

		return new MatrixXml(id, compartment.getName(), "", dbuuid);
	}

	public Node toGroovyNode() {
		return new Node(null, "sbml:compartment",
				compartment.writeXMLAttributes());
	}
}