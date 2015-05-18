package de.bund.bfr.knime.pmm.annotation;

import java.util.HashMap;
import java.util.Map;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Uncertainty xml node. Uses the pmml:modelquality tag. E.g. <pmml:modelquality
 * r-squared="0.996" rootMeanSquaredError="0.345" dataName="Missing data name"
 * AIC="-32.977" BIC="-34.994"/>
 * 
 * @author Miguel Alba
 */
public class UncertaintyNode extends SBMLNodeBase {

	/**
	 * Builds an UncertaintyNode from an existing XMLNode.
	 * 
	 * @param node
	 *            XMLNode.
	 */
	public UncertaintyNode(XMLNode node) {
		this.node = node;
	}

	/**
	 * Builds an UncertaintyNode from uncertainty measures.
	 * 
	 * @param uncertainties
	 *            Map with uncertainty measure names as keys and their values.
	 *            Both their keys and values are Strings.
	 */
	public UncertaintyNode(Map<String, String> uncertainties) {
		XMLTriple triple = new XMLTriple("modelquality", null, "pmml");
		XMLAttributes attrs = new XMLAttributes();
		for (Map.Entry<String, String> entry : uncertainties.entrySet()) {
			attrs.add(entry.getKey(), entry.getValue());
		}
		node = new XMLNode(triple, attrs);
	}

	/**
	 * Gets uncertainty measures.
	 * 
	 * @return Uncertainty measures with their names as keys.
	 */
	public Map<String, String> getMeasures() {
		Map<String, String> measures = new HashMap<>();
		XMLAttributes attributes = node.getAttributes();
		for (int nattr = 0; nattr < attributes.getLength(); nattr++) {
			measures.put(attributes.getName(nattr), attributes.getValue(nattr));
		}
		return measures;
	}
}