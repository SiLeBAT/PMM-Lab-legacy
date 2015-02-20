package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Model id xml node. Uses the dc:identifier tag.
 * 
 * @author malba
 */
public class ModelIdNode {

	private XMLNode node;

	public ModelIdNode(String modelId) {
		XMLTriple triple = new XMLTriple("identifier", null, "dc");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(modelId));
	}

	public XMLNode getNode() {
		return node;
	}
}