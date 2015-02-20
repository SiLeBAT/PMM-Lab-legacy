package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Model title xml node. Uses the dc:title tag.
 * 
 * @author malba
 */
public class ModelTitleNode {
	private XMLNode node;

	public ModelTitleNode(String modelTitle) {
		XMLTriple triple = new XMLTriple("title", null, "dc");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(modelTitle));
	}

	public XMLNode getNode() {
		return node;
	}
}