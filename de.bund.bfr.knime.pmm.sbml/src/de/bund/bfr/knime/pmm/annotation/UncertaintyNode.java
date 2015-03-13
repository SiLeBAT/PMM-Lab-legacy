package de.bund.bfr.knime.pmm.annotation;

import java.util.HashMap;
import java.util.Map;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Uncertainty xml node. Uses the pmml:modelquality tag.
 * 
 * @author malba
 *
 */
public class UncertaintyNode {
	private XMLNode node;
	
	public UncertaintyNode(XMLNode node) {
		this.node = node;
	}

	public UncertaintyNode(Map<String, String> uncertainties) {
		XMLTriple triple = new XMLTriple("modelquality", null, "pmml");
		XMLAttributes attrs = new XMLAttributes();
		for (Map.Entry<String, String> entry : uncertainties.entrySet()) {
			attrs.add(entry.getKey(), entry.getValue());
		}
		node = new XMLNode(triple, attrs);
	}

	public XMLNode getNode() {
		return node;
	}

	public Map<String, String> getMeasures() {
		Map<String, String> measures = new HashMap<String, String>();
		XMLAttributes attributes = node.getAttributes();
		for (int nattr = 0; nattr < attributes.getLength(); nattr++) {
			measures.put(attributes.getName(nattr), attributes.getValue(nattr));
		}
		return measures;
	}
}