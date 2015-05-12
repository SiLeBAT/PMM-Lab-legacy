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

public class Coefficient {

	Parameter param;
	Double P;
	Double error;
	Double t;
	Map<String, Double> correlations;
	String desc;

	public Coefficient(Parameter param) {
		// Get non RDF annotation
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

	public Coefficient(ParamXml paramXml) {
		param = new Parameter(paramXml.getName());

		if (paramXml.getValue() != null) {
			param.setValue(paramXml.getValue());
		}

		if (paramXml.getUnit() == null) {
			param.setUnits("dimensionless");
		} else {
			param.setUnits(Util.createId(paramXml.getUnit()));
		}

		param.setConstant(true);

		// Save P, error, t, and correlations
		P = paramXml.getP();
		error = paramXml.getError();
		t = paramXml.getT();
		correlations = paramXml.getAllCorrelations();
		desc = paramXml.getDescription();

		// Build and set non RDF annotation
		CoefficientAnnotation annot = new CoefficientAnnotation(P, error, t,
				correlations, desc);
		param.getAnnotation().setNonRDFAnnotation(annot.getNode());
	}

	public ParamXml toParamXml() {
		ParamXml paramXml = new ParamXml(param.getId(), param.getValue(),
				error, null, null, P, t);
		paramXml.setDescription(desc);
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

// Coefficient non RDF annotation. Holds P, error, and t
class CoefficientAnnotation {

	private static final String METADATA_TAG = "metadata";
	private static final String PMF_TAG = "pmf";
	private static final String P_TAG = "P";
	private static final String ERROR_TAG = "error";
	private static final String T_TAG = "t";
	private static final String CORR_TAG = "correlation";
	private static final String DESC_TAG = "description";

	// Attribute names of a correlation element
	private static final String CORR_NS = "pmml";
	private static final String CORR_NAME = "origname";
	private static final String CORR_VALUE = "value";

	private XMLNode node;
	private Double P;
	private Double error;
	private Double t;
	private Map<String, Double> correlations;
	private String desc;

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
		
		// Get description
		XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
		if (descNode != null) {
			desc = descNode.getChild(0).getCharacters();
		}
	}

	// Build new coefficient annotation for P, error, t, and correlations
	public CoefficientAnnotation(Double P, Double error, Double t,
			Map<String, Double> correlations, String desc) {
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
		
		// Create annotation for correlations
		for (Entry<String, Double> entry : correlations.entrySet()) {
			XMLTriple triple = new XMLTriple(CORR_TAG, null, CORR_NS);
			XMLAttributes attrs = new XMLAttributes();
			attrs.add(CORR_NAME, entry.getKey());
			attrs.add(CORR_VALUE, entry.getValue().toString());
			node.addChild(new XMLNode(triple, attrs));
		}
		
		// Create annotation for description
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