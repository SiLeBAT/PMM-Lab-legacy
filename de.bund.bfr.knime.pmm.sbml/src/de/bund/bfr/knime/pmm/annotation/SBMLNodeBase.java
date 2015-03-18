package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;

public abstract class SBMLNodeBase {
	protected XMLNode node;
	
	public XMLNode getNode() {
		return node;
	}
}
