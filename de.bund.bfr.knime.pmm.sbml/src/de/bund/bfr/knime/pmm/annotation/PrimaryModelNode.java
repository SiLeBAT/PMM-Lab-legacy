package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

public class PrimaryModelNode extends SBMLNodeBase {
	
	public static final String TAG = "primaryModel";
	public static final String NS = "pmmlab";
	
	public PrimaryModelNode(String model) {
		XMLTriple triple = new XMLTriple(TAG, "", NS);
		node = new XMLNode(triple);
		node.addChild(new XMLNode(model));
	}
}