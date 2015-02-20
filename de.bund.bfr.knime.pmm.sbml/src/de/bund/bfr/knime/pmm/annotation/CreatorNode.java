package de.bund.bfr.knime.pmm.annotation;

import java.util.Locale;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Creator xml node. Uses the dc:creator tag.
 * 
 * @author malba
 */
public class CreatorNode {
	private XMLNode node;

	public CreatorNode(String givenName, String familyName, String contact) {
		XMLTriple triple = new XMLTriple("creator", null, "dc");
		String creator = String.format(Locale.ENGLISH, "%s. %s. %s", givenName,
				familyName, contact);
		node = new XMLNode(triple);
		node.addChild(new XMLNode(creator));
	}

	public XMLNode getNode() {
		return node;
	}
}