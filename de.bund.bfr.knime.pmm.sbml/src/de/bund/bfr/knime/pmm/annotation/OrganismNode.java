package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * XML node with the CAS number of an organism.
 * E.g. <dc:source>http://identifiers.org/ncim/C0452849</dc:source>
 * @author Miguel Alba
 */
public class OrganismNode extends SBMLNodeBase {
	public OrganismNode(String casNumber) {
		XMLTriple triple = new XMLTriple("source", null, "dc");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(casNumber));
	}
}
