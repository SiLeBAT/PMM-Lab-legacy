/** Pmm Lab coefficient.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.ParamXml;

/**
 * Coefficient that extends the SBML parameter with more data: P, error,
 * correlations and a description string.
 * 
 * @author Miguel Alba
 */
public class Coefficient {

	Parameter param;
	Double P;
	Double error;
	Double t;
	Map<String, Double> correlations;
	String desc;

	/**
	 * Builds a Coefficient from existing SBML parameter.
	 * 
	 * @param param
	 *            SBML parameter with CoefficientAnnotation.
	 */
	public Coefficient(Parameter param) {
		// Get fields from non RDF annotation
		XMLNode nonRDFannotation = param.getAnnotation().getNonRDFannotation();
		CoefficientAnnotation annot = new CoefficientAnnotation(
				nonRDFannotation);

		P = annot.getP();
		error = annot.getError();
		t = annot.getT();
		correlations = annot.getCorrelations();
		desc = annot.getDescription();
		this.param = param;
	}

	/**
	 * Builds a Coefficient from a Pmm Lab ParamXml.
	 * 
	 * @param paramXml
	 *            Pmm Lab ParamXml.
	 */
	public Coefficient(ParamXml paramXml) {
		// Creates SBML parameter with paramXml's name as its id
		param = new Parameter(paramXml.getName());
		param.setConstant(true);

		/*
		 * If paramXml has a value then assign it to the SBML parameter (most
		 * instances of ParamXml have a value)
		 */
		// TODO: An exception or KNIME warning could be thrown here to warn of
		// uninitialized parameters.
		if (paramXml.getValue() != null) {
			param.setValue(paramXml.getValue());
		}

		// If paramXml has not unit then assigns "dimensionless" to SBML
		// parameter
		if (paramXml.getUnit() == null) {
			param.setUnits("dimensionless");
		} else {
			param.setUnits(Util.createId(paramXml.getUnit()));
		}

		// Saves P, error, t, and correlations
		P = paramXml.getP();
		error = paramXml.getError();
		t = paramXml.getT();
		correlations = paramXml.getAllCorrelations();
		desc = paramXml.getDescription();

		// Builds and sets non RDF annotation
		CoefficientAnnotation annot = new CoefficientAnnotation(P, error, t,
				correlations, desc);
		param.getAnnotation().setNonRDFAnnotation(annot.getNode());
	}

	/**
	 * Creates a Pmm Lab ParamXml.
	 */
	public ParamXml toParamXml() {
		// Creates ParamXml and adds description
		ParamXml paramXml = new ParamXml(param.getId(), param.getValue(),
				error, null, null, P, t);
		paramXml.setDescription(desc);

		// Adds correlations
		for (Entry<String, Double> entry : correlations.entrySet()) {
			paramXml.addCorrelation(entry.getKey(), entry.getValue());
		}
		return paramXml;
	}

	// Getters
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

	public Parameter getParameter() {
		return param;
	}
}

/** Coefficient non RDF annotation. Holds P, error, and t. */
class CoefficientAnnotation {

	static final String METADATA_TAG = "metadata";
	static final String PMF_TAG = "pmf";
	static final String P_TAG = "P";
	static final String ERROR_TAG = "error";
	static final String T_TAG = "t";
	static final String CORR_TAG = "correlation";
	static final String DESC_TAG = "description";

	// Attribute names of a correlation element
	static final String CORR_NS = "pmml";
	static final String CORR_NAME = "origname"; // correlation attribute name
	static final String CORR_VALUE = "value"; // correlation attribute value

	XMLNode node;
	Double P;
	Double error;
	Double t;
	Map<String, Double> correlations;
	String desc;

	/**
	 * Builds an CoefficientAnnotation from existing XMLNode.
	 * 
	 * @param node
	 *            XMLNode with P, error, t, correlations and desc.
	 */
	public CoefficientAnnotation(XMLNode node) {
		this.node = node; // copies XMLNode

		// Parses annotation
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");

		// Gets P
		XMLNode pNode = metadata.getChildElement(P_TAG, "");
		if (pNode != null) {
			P = Double.parseDouble(pNode.getChild(0).getCharacters());
		}

		// Gets error
		XMLNode errorNode = metadata.getChildElement(ERROR_TAG, "");
		if (errorNode != null) {
			error = Double.parseDouble(errorNode.getChild(0).getCharacters());
		}

		// Gets t
		XMLNode tNode = metadata.getChildElement(T_TAG, "");
		if (tNode != null) {
			t = Double.parseDouble(tNode.getChild(0).getCharacters());
		}

		// Gets correlations
		correlations = new HashMap<>();
		for (XMLNode corrNode : metadata.getChildElements(CORR_TAG, "")) {
			XMLAttributes attrs = corrNode.getAttributes();
			String corrName = attrs.getValue(CORR_NAME);
			Double corrValue = Double.parseDouble(attrs.getValue(CORR_VALUE));
			correlations.put(corrName, corrValue);
		}

		// Gets description
		XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
		if (descNode != null) {
			desc = descNode.getChild(0).getCharacters();
		}
	}

	/**
	 * Builds new CoefficientAnnotation.
	 * 
	 * @param P
	 * @param error
	 * @param t
	 * @param correlations
	 * @param desc
	 */
	public CoefficientAnnotation(Double P, Double error, Double t,
			Map<String, Double> correlations, String desc) {
		// Builds metadata node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));

		// Creates annotation for P
		if (P != null) {
			XMLNode pNode = new XMLNode(new XMLTriple(P_TAG, null, PMF_TAG));
			pNode.addChild(new XMLNode(P.toString()));
			node.addChild(pNode);
		}

		// Creates annotation for error
		if (error != null) {
			XMLNode errorNode = new XMLNode(new XMLTriple(ERROR_TAG, null,
					PMF_TAG));
			errorNode.addChild(new XMLNode(error.toString()));
			node.addChild(errorNode);
		}

		// Creates annotation for t
		if (t != null) {
			XMLNode tNode = new XMLNode(new XMLTriple(T_TAG, null, PMF_TAG));
			tNode.addChild(new XMLNode(t.toString()));
			node.addChild(tNode);
		}

		// Creates annotation for correlations
		for (Entry<String, Double> entry : correlations.entrySet()) {
			XMLTriple triple = new XMLTriple(CORR_TAG, null, CORR_NS);
			XMLAttributes attrs = new XMLAttributes();
			attrs.add(CORR_NAME, entry.getKey());
			attrs.add(CORR_VALUE, entry.getValue().toString());
			node.addChild(new XMLNode(triple, attrs));
		}

		// Creates annotation for description
		if (desc != null) {
			XMLTriple descTriple = new XMLTriple(DESC_TAG, null, PMF_TAG);
			XMLNode descNode = new XMLNode(descTriple);
			descNode.addChild(new XMLNode(desc));
			node.addChild(descNode);
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

	public String getDescription() {
		return desc;
	}
}