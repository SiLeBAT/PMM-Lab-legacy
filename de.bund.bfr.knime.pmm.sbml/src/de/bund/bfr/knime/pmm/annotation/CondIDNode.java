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
}
