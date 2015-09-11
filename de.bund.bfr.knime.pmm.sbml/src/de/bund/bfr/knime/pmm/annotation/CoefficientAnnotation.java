package de.bund.bfr.knime.pmm.annotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Coefficient annotation. Holds P, error, and t.
 * 
 * @author Miguel de Alba
 */
public class CoefficientAnnotation {

	Annotation annotation;
	Double P;
	Double error;
	Double t;
	Map<String, Double> correlations;
	String desc;

	static final String P_TAG = "P"; // P tag
	static final String P_NS = "pmmlab"; // P name space

	static final String ERROR_TAG = "error"; // error tag
	static final String ERROR_NS = "pmmlab"; // error name space

	static final String T_TAG = "t"; // t tag
	static final String T_NS = "pmmlab"; // t name space

	static final String CORRELATION_TAG = "correlation"; // correlation tag
	static final String CORRELATION_NS = "pmmlab"; // correlation name space

	static final String ATTRIBUTE_NAME = "origname";
	static final String ATTRIBUTE_VALUE = "value";

	static final String DESC_TAG = "description"; // description tag
	static final String DESC_NS = "pmmlab"; // description name space

	/**
	 * Builds a CoefficientAnnotation from existing annotation.
	 */
	public CoefficientAnnotation(Annotation annotation) {

		this.annotation = annotation;

		// Parses annotation
		XMLNode metadataNode = annotation.getNonRDFannotation().getChildElement("metadata", "");

		// Gets P
		XMLNode pNode = metadataNode.getChildElement(P_TAG, "");
		if (pNode != null) {
			P = Double.parseDouble(pNode.getChild(0).getCharacters());
		}

		// Gets error
		XMLNode errorNode = metadataNode.getChildElement(ERROR_TAG, "");
		if (errorNode != null) {
			error = Double.parseDouble(errorNode.getChild(0).getCharacters());
		}

		// Gets t
		XMLNode tNode = metadataNode.getChildElement(T_TAG, "");
		if (tNode != null) {
			t = Double.parseDouble(tNode.getChild(0).getCharacters());
		}

		// Gets correlations
		List<XMLNode> corrNodes = metadataNode.getChildElements(CORRELATION_TAG, "");
		if (!corrNodes.isEmpty()) {
			correlations = new HashMap<String, Double>();
			for (XMLNode corrNode : corrNodes) {
				XMLAttributes attrs = corrNode.getAttributes();
				String corrName = attrs.getValue(ATTRIBUTE_NAME);
				Double corrValue = Double.parseDouble(attrs.getValue(ATTRIBUTE_VALUE));
				correlations.put(corrName, corrValue);
			}
		}

		// Gets description
		XMLNode descNode = metadataNode.getChildElement(DESC_TAG, "");
		if (descNode != null) {
			desc = descNode.getChild(0).getCharacters();
		}
	}

	/**
	 * Builds new CoefficientAnnotation
	 */
	public CoefficientAnnotation(Double P, Double error, Double t, Map<String, Double> correlations, String desc) {

		// Copies data
		this.P = P;
		this.error = error;
		this.t = t;
		this.correlations = correlations;
		this.desc = desc;

		// Builds metadata node
		XMLNode metadataNode = new XMLNode(new XMLTriple("metadata", null, "pmf"));

		// Creates P annotation
		if (P != null) {
			XMLNode pNode = new XMLNode(new XMLTriple(P_TAG, null, P_NS));
			pNode.addChild(new XMLNode(P.toString()));
			metadataNode.addChild(pNode);
		}

		// Creates error annotation
		if (error != null) {
			XMLTriple errorTriple = new XMLTriple(ERROR_TAG, null, ERROR_NS);
			XMLNode errorNode = new XMLNode(errorTriple);
			errorNode.addChild(new XMLNode(error.toString()));
			metadataNode.addChild(errorNode);
		}

		// Creates t annotation
		if (t != null) {
			XMLTriple tTriple = new XMLTriple(T_TAG, null, T_NS);
			XMLNode tNode = new XMLNode(tTriple);
			tNode.addChild(new XMLNode(t.toString()));
			metadataNode.addChild(tNode);
		}

		// Creates correlation annotation
		if (correlations != null) {
			for (Entry<String, Double> entry : correlations.entrySet()) {
				String name = entry.getKey();
				Double value = entry.getValue();

				if (value != null) {
					XMLTriple triple = new XMLTriple(CORRELATION_TAG, null, CORRELATION_NS);
					XMLAttributes attrs = new XMLAttributes();
					attrs.add(ATTRIBUTE_NAME, name);
					attrs.add(ATTRIBUTE_VALUE, value.toString());
					metadataNode.addChild(new XMLNode(triple, attrs));
				}
			}
		}

		// Creates annotation for description
		if (desc != null) {
			XMLTriple descTriple = new XMLTriple(DESC_TAG, null, DESC_NS);
			XMLNode descNode = new XMLNode(descTriple);
			descNode.addChild(new XMLNode(desc));
			metadataNode.addChild(descNode);
		}

		this.annotation = new Annotation();
		this.annotation.setNonRDFAnnotation(metadataNode);
	}

	// Getters
	public Annotation getAnnotation() {
		return annotation;
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

	public String getDescription() {
		return desc;
	}
}
