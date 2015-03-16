package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Created date xml node. Uses the dcterms:created tag. Example:
 * <dcterms:created>Thu Jan 01 01:00:00 CET 1970</dcterms:created>
 * 
 * @author Miguel Alba
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