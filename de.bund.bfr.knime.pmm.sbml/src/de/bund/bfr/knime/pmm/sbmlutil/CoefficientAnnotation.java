package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

// Coefficient non RDF annotation. Holds P, error, and t
public class CoefficientAnnotation {
	
	private static final String METADATA_TAG = "metadata";
	private static final String PMF_TAG = "pmf";
	private static final String P_TAG = "P";
	private static final String ERROR_TAG = "error";
	private static final String T_TAG = "t";

	private XMLNode node;
	private Double P;
	private Double error;
	private Double t;
	
	// Get P, error, and t from existing coefficient annotation
	public CoefficientAnnotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");
		
		// Get P
		XMLNode pNode = metadata.getChildElement(P_TAG, "");
		if (pNode != null) {
			P = Double.parseDouble(pNode.getChild(0).getCharacters());
		}

		// Get error
		XMLNode errorNode = metadata.getChildElement(ERROR_TAG, "");
		if (errorNode != null) {
			error = Double.parseDouble(errorNode.getChild(0).getCharacters());
		}

		// Get t
		XMLNode tNode = metadata.getChildElement(T_TAG, "");
		if (tNode != null) {
			t = Double.parseDouble(tNode.getChild(0).getCharacters());
		}
	}
	
	// Build new coefficient annotation for P, error, and t values
	public CoefficientAnnotation(Double P, Double error, Double t) {
		// Build metadata node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));
		
		// Create annotation for P
		if (P != null) {
			XMLNode pNode = new XMLNode(new XMLTriple(P_TAG, null, PMF_TAG));
			pNode.addChild(new XMLNode(P.toString()));
			node.addChild(pNode);
		}
		
		// Create annotation for error
		if (error != null) {
			XMLNode errorNode = new XMLNode(new XMLTriple(ERROR_TAG, null, PMF_TAG));
			errorNode.addChild(new XMLNode(error.toString()));
			node.addChild(errorNode);
		}
		
		// Create annotation for t
		if (t != null) {
			XMLNode tNode = new XMLNode(new XMLTriple(T_TAG, null, PMF_TAG));
			tNode.addChild(new XMLNode(t.toString()));
			node.addChild(tNode);
		}
	}
	
	// Getters
	public XMLNode getNode() {
		return node;
	}
	
	public Double getP() {
		return P;
	}
	
	public Double getError() {
		return error;
	}
	
	public Double getT() {
		return t;
	}
}
