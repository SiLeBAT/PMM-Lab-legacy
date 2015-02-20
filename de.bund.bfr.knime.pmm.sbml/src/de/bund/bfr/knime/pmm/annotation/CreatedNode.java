package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Created date xml node. Uses the dcterms:created tag.
 * 
 * @author malba
 */
public class CreatedNode {
	private XMLNode node;

	public CreatedNode(String created) {
		XMLTriple triple = new XMLTriple("created", null, "dcterms");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(created));
	}

	public XMLNode getNode() {
		return node;
	}
}