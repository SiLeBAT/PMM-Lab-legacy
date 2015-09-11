package de.bund.bfr.knime.pmm.annotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

public class MatrixAnnotation {

	Annotation annotation;
	String pmfCode;
	String details; // matrix details
	Map<String, Double> miscs; // model variables

	static final String CODE_TAG = "source"; // PMF code tag
	static final String CODE_NS = "dc"; // PMF code namespace

	static final String DETAIL_TAG = "pmmlab"; // Matrix detail tag
	static final String DETAIL_NS = "pmmlab"; // Matrix detail namespace

	static final String VAR_TAG = "environment"; // Model variable tag
	static final String VAR_NS = "pmmlab"; // Model variable namespace

	static final String ATTRIBUTE_NAME = "name";
	static final String ATTRIBUTE_VALUE = "value";

	/**
	 * Gets PMF code, description and model variables from a matrix annotation.
	 * 
	 * @param annotation
	 *            Annotation
	 */
	public MatrixAnnotation(final Annotation annotation) {

		this.annotation = annotation;

		// Parses annotation
		XMLNode metadata = annotation.getNonRDFannotation().getChildElement("metadata", "");

		// Gets PMF code
		XMLNode codeNode = metadata.getChildElement(CODE_TAG, "");
		if (codeNode != null) {
			pmfCode = codeNode.getChild(0).getCharacters();
		}

		// Gets matrix details
		XMLNode detailsNode = metadata.getChildElement(DETAIL_TAG, "");
		if (detailsNode != null) {
			details = detailsNode.getChild(0).getCharacters();
		}

		// Gets model variables
		List<XMLNode> varNodes = metadata.getChildElements(VAR_TAG, "");
		if (!varNodes.isEmpty()) {
			miscs = new HashMap<String, Double>();
			for (XMLNode varNode : metadata.getChildElements(VAR_TAG, "")) {
				XMLAttributes attrs = varNode.getAttributes();
				String varName = attrs.getValue(ATTRIBUTE_NAME);
				Double varValue;
				// If varNode has a value then parses it
				if (attrs.hasAttribute(ATTRIBUTE_VALUE)) {
					varValue = Double.parseDouble(attrs.getValue(ATTRIBUTE_VALUE));
				}
				// Otherwise, if empty string assigns a null value
				else {
					varValue = null;
				}
				miscs.put(varName, varValue);
			}
		}
	}

	/**
	 * Creates a MatrixAnnotation for a code, description and model variables.
	 */
	public MatrixAnnotation(final String pmfCode, final Map<String, Double> miscs, final String details) {

		this.pmfCode = pmfCode;
		this.miscs = miscs;
		this.details = details;

		// Builds metadata node
		XMLNode metadataNode = new XMLNode(new XMLTriple("metadata", null, "pmf"));

		// Creates annotation for the PMF code
		if (pmfCode != null) {
			XMLNode codeNode = new XMLNode(new XMLTriple(CODE_TAG, null, CODE_NS));
			codeNode.addChild(new XMLNode(pmfCode));
			metadataNode.addChild(codeNode);
		}

		// Creates annotation for the matrix details
		if (details != null) {
			XMLTriple detailsTriple = new XMLTriple(DETAIL_TAG, null, DETAIL_NS);
			XMLNode detailsNode = new XMLNode(detailsTriple);
			detailsNode.addChild(new XMLNode(details));
			metadataNode.addChild(detailsNode);
		}

		// Creates annotations for model variables (Temperature, pH, aW)
		if (miscs != null) {
			for (Entry<String, Double> entry : miscs.entrySet()) {
				XMLTriple varTriple = new XMLTriple(VAR_TAG, null, VAR_NS);
				XMLAttributes attrs = new XMLAttributes();
				attrs.add(ATTRIBUTE_NAME, entry.getKey());
				if (entry.getValue() != null) {
					attrs.add(ATTRIBUTE_VALUE, entry.getValue().toString());
				}
				metadataNode.addChild(new XMLNode(varTriple, attrs));
			}
		}

		this.annotation = new Annotation();
		this.annotation.setNonRDFAnnotation(metadataNode);
	}

	// Getters
	public Annotation getAnnotation() {
		return annotation;
	}

	public String getPmfCode() {
		return pmfCode;
	}

	public Map<String, Double> getVars() {
		return miscs;
	}

	public String getDetails() {
		return details;
	}
}