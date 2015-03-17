package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Model id xml node. Uses the dc:identifier tag.
 * E.g. <dc:identifier>Meta_Salmonellaspp_GroundBeef...</dc:identifier>
 * 
 * @author Miguel Alba
 */
public class ModelIdNode extends SBMLNodeBase {

	public ModelIdNode(String modelId) {
		XMLTriple triple = new XMLTriple("identifier", null, "dc");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(modelId));
	}
}