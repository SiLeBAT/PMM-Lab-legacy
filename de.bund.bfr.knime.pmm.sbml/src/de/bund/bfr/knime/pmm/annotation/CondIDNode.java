package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

public class CondIDNode extends SBMLNodeBase {

	public final static String TAG = "condID";
	public final static String NS = "pmmlab";
	
	public CondIDNode(int id) {
		XMLTriple triple = new XMLTriple(TAG, null, NS);
		node = new XMLNode(triple);
		node.addChild(new XMLNode(Integer.toString(id)));
	}
	
	public CondIDNode(XMLNode node) {
		this.node = node;
	}
	
	public int getCondId() {
		return Integer.parseInt(node.getChild(0).getCharacters());
	}
}
