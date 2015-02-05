package de.bund.bfr.knime.pmm.sbmlcommon;

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
}