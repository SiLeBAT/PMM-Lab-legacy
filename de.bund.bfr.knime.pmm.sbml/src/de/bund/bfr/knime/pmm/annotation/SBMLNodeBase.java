package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNode;

/**
 * Base SBML node class.
 * 
 * @author Miguel Alba
 */
public abstract class SBMLNodeBase {
	protected XMLNode node;

	public XMLNode getNode() {
		return node;
	}
}