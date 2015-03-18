package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Model class xml node. Uses the dc:type tag.
 * E.g. <dc:type>Primary</dc:type>
 * 
 * @author Miguel Alba
 */
public class ModelClassNode extends SBMLNodeBase {
	public ModelClassNode(String modelClass) {
		XMLTriple triple = new XMLTriple("type", null, "dc");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(modelClass));
	}
}