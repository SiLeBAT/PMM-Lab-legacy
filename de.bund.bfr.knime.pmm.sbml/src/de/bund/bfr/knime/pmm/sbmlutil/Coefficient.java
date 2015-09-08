/** Pmm Lab coefficient.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;

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

		if (nonRDFannotation != null) {
			CoefficientAnnotation annot = new CoefficientAnnotation(nonRDFannotation);

			P = annot.getP();
			error = annot.getError();
			t = annot.getT();
			correlations = annot.getCorrelations();
			desc = annot.getDescription();
		}
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
			param.setUnits(Unit.Kind.DIMENSIONLESS);
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
		CoefficientAnnotation annot = new CoefficientAnnotation(P, error, t, correlations, desc);
		param.getAnnotation().setNonRDFAnnotation(annot.getNode());
	}

	/**
	 * Creates a Pmm Lab ParamXml.
	 */
	public ParamXml toParamXml(ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {
		// Creates ParamXml and adds description
		ParamXml paramXml = new ParamXml(param.getId(), param.getValue(), error, null, null, P, t);
		paramXml.setDescription(desc);

		// Adds correlations
		for (Entry<String, Double> entry : correlations.entrySet()) {
			paramXml.addCorrelation(entry.getKey(), entry.getValue());
		}
		
		// Assigns unit and category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			paramXml.setUnit(unitName);
			paramXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
		}
		
		// Adds limits
        if (limits.containsKey(param.getId())) {
        	Limits constLimits = limits.get(param.getId());
        	paramXml.setMax(constLimits.getMax());
        	paramXml.setMin(constLimits.getMin());
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
		XMLNode metadata = node.getChildElement("metadata", "");

		// Gets P
		XMLNode pNode = metadata.getChildElement("P", "");
		if (pNode != null) {
			P = Double.parseDouble(pNode.getChild(0).getCharacters());
		}

		// Gets error
		XMLNode errorNode = metadata.getChildElement("error", "");
		if (errorNode != null) {
			error = Double.parseDouble(errorNode.getChild(0).getCharacters());
		}

		// Gets t
		XMLNode tNode = metadata.getChildElement("t", "");
		if (tNode != null) {
			t = Double.parseDouble(tNode.getChild(0).getCharacters());
		}

		// Gets correlations
		correlations = new HashMap<>();
		for (XMLNode corrNode : metadata.getChildElements("correlation", "")) {
			XMLAttributes attrs = corrNode.getAttributes();
			String corrName = attrs.getValue("origname");
			Double corrValue = Double.parseDouble(attrs.getValue("value"));
			correlations.put(corrName, corrValue);
		}

		// Gets description
		XMLNode descNode = metadata.getChildElement("description", "");
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
	public CoefficientAnnotation(Double P, Double error, Double t, Map<String, Double> correlations, String desc) {
		// Builds metadata node
		node = new XMLNode(new XMLTriple("metadata", null, "pmf"));

		// Creates annotation for P
		if (P != null) {
			XMLNode pNode = new XMLNode(new XMLTriple("P", null, "pmmlab"));
			pNode.addChild(new XMLNode(P.toString()));
			node.addChild(pNode);
		}

		// Creates annotation for error
		if (error != null) {
			XMLTriple errorTriple = new XMLTriple("error", null, "pmmlab");
			XMLNode errorNode = new XMLNode(errorTriple);
			errorNode.addChild(new XMLNode(error.toString()));
			node.addChild(errorNode);
		}

		// Creates annotation for t
		if (t != null) {
			XMLTriple tTriple = new XMLTriple("t", null, "pmmlab");
			XMLNode tNode = new XMLNode(tTriple);
			tNode.addChild(new XMLNode(t.toString()));
			node.addChild(tNode);
		}

		// Creates annotation for correlations
		for (Entry<String, Double> entry : correlations.entrySet()) {
			String name = entry.getKey();
			Double value = entry.getValue();

			if (value != null) {
				XMLTriple triple = new XMLTriple("correlation", null, "pmmlab");
				XMLAttributes attrs = new XMLAttributes();
				attrs.add("origname", name);
				attrs.add("value", value.toString());
				node.addChild(new XMLNode(triple, attrs));
			}
		}

		// Creates annotation for description
		if (desc != null) {
			XMLTriple descTriple = new XMLTriple("description", null, "pmmlab");
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