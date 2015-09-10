package de.bund.bfr.knime.pmm.annotation;

import java.util.Locale;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Creator xml node. Uses the dc:creator tag. E.g.: <dc:creator>Mr Foo
 * Bar</dc:creator>
 * 
 * @author Miguel Alba
 */
public class CreatorNode extends SBMLNodeBase {

	public CreatorNode(String givenName, String familyName, String contact) {
		XMLTriple triple = new XMLTriple("creator", null, "dc");
		String creator = String.format(Locale.ENGLISH, "%s. %s. %s", givenName,
				familyName, contact);
		node = new XMLNode(triple);
		node.addChild(new XMLNode(creator));
	}
	
	public CreatorNode(XMLNode node) {
		this.node = node;
	}
	
	public String getCreator() {
		return node.getChild(0).getCharacters();
	}
}