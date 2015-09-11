package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

public class GlobalModelIdNode extends SBMLNodeBase {
	
	public final static String TAG = "globalModelID";
	public final static String NS = "pmmlab";
	
	public GlobalModelIdNode(int id) {
		XMLTriple triple = new XMLTriple(TAG, null, NS);
		node = new XMLNode(triple);
		node.addChild(new XMLNode(Integer.toString(id)));
	}
	
	public GlobalModelIdNode(final XMLNode node) {
		this.node = node;
	}
	
	public int getGlobalModelId() {
		return Integer.parseInt(node.getChild(0).getCharacters());
	}
}