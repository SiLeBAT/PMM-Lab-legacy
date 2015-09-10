package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Modified date xml node. Uses the dcterms:modified tag. E.g.
 * <dcterms:modified>2014-08-19T00:00:00Z</dcterms:modified>
 * 
 * @author Miguel Alba
 */
public class ModifiedNode extends SBMLNodeBase {

	/**
	 * Builds a ModifiedNode with latest modification date.
	 * 
	 * @param modified
	 *            Latest modification date.
	 */
	public ModifiedNode(String modified) {
		XMLTriple triple = new XMLTriple("modified", null, "dcterms");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(modified));
	}
	
	public ModifiedNode(XMLNode node) {
		this.node = node;
	}
	
	public String getModified() {
		return node.getChild(0).getCharacters();
	}
}