package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Model class xml node. Uses the dc:type tag.
 * 
 * @author malba
 */
public class ModelClassNode {
	private XMLNode node;

	public ModelClassNode(String modelClass) {
		XMLTriple triple = new XMLTriple("type", null, "dc");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(modelClass));
	}

	public XMLNode getNode() {
		return node;
	}
}