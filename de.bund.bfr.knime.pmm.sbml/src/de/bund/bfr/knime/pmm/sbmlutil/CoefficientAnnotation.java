/**
 * Coefficient non RDF annotation. Holds P, error, and t.
 * @author Miguel Alba (malba@optimumquality.es)
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

// Coefficient non RDF annotation. Holds P, error, and t
public class CoefficientAnnotation {

	private static final String METADATA_TAG = "metadata";
	private static final String PMF_TAG = "pmf";
	private static final String P_TAG = "P";
	private static final String ERROR_TAG = "error";
	private static final String T_TAG = "t";
	private static final String CORR_TAG = "correlation";

	// Attribute names of a correlation element
	private static final String CORR_NS = "pmml";
	private static final String CORR_NAME = "origname";
	private static final String CORR_VALUE = "value";

	private XMLNode node;
	private Double P;
	private Double error;
	private Double t;
	private Map<String, Double> correlations;

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

		// Get correlations
		correlations = new HashMap<>();
		for (XMLNode corrNode : metadata.getChildElements(CORR_TAG, "")) {
			XMLAttributes attrs = corrNode.getAttributes();
			String corrName = attrs.getValue(CORR_NAME);
			Double corrValue = Double.parseDouble(attrs.getValue(CORR_VALUE));
			correlations.put(corrName, corrValue);
		}
	}

	// Build new coefficient annotation for P, error, t, and correlations
	public CoefficientAnnotation(Double P, Double error, Double t,
			Map<String, Double> correlations) {
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
			XMLNode errorNode = new XMLNode(new XMLTriple(ERROR_TAG, null,
					PMF_TAG));
			errorNode.addChild(new XMLNode(error.toString()));
			node.addChild(errorNode);
		}

		// Create annotation for t
		if (t != null) {
			XMLNode tNode = new XMLNode(new XMLTriple(T_TAG, null, PMF_TAG));
			tNode.addChild(new XMLNode(t.toString()));
			node.addChild(tNode);
		}
		
		for (Entry<String, Double> entry : correlations.entrySet()) {
			XMLTriple triple = new XMLTriple(CORR_TAG, null, CORR_NS);
			XMLAttributes attrs = new XMLAttributes();
			attrs.add(CORR_NAME, entry.getKey());
			attrs.add(CORR_VALUE, entry.getValue().toString());
			node.addChild(new XMLNode(triple, attrs));
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
	
	public Map<String, Double> getCorrelations() {
		return correlations;
	}
}
