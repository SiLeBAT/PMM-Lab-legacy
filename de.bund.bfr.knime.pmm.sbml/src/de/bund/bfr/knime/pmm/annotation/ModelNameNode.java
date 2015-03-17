package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Model class xml node. Uses the pmml:formulaName tag.
 * E.g. <pmml:formulaName>Log10mumax formula</pmml:formulaName>
 * 
 * @author Miguel Alba
 */
public class ModelNameNode extends SBMLNodeBase {
	public ModelNameNode(String modelClass) {
		XMLTriple triple = new XMLTriple("formulaName", null, "pmml");
		node = new XMLNode(triple);
		node.addChild(new XMLNode(modelClass));
	}
}