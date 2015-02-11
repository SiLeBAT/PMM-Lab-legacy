package de.bund.bfr.knime.pmm.sbmlcommon;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Model class xml node. Uses the dc:type tag.
 * 
 * @author malba
 */
public class ModelNameNode {
	private XMLNode node;

	public ModelNameNode(String modelClass) {
		XMLTriple triple = new XMLTriple("modelName", null, "pmml");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(modelClass));
	}

	public XMLNode getNode() {
		return node;
	}
}