package de.bund.bfr.knime.pmm.sbmlcommon;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

public class OrganismNode {
	private XMLNode node;
	
	public OrganismNode(String casNumber) {
		XMLTriple triple = new XMLTriple("source", null, "dc");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(casNumber));
	}
	
	public XMLNode getNode() {
		return node;
	}
}
