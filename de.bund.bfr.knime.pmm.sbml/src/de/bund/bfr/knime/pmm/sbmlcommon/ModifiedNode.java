package de.bund.bfr.knime.pmm.sbmlcommon;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Modified date xml node. Uses the dcterms:modified tag.
 * 
 * @author malba
 */
public class ModifiedNode {
	private XMLNode node;

	public ModifiedNode(String modified) {
		XMLTriple triple = new XMLTriple("modified", null, "dcterms");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(modified));
	}

	public XMLNode getNode() {
		return node;
	}
}