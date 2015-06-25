package de.bund.bfr.knime.pmm.model;

import org.sbml.jsbml.SBMLDocument;

/**
 * Primary model without data.
 * 
 * It has no data.
 * 
 * @author Miguel Alba
 */
public class PrimaryModelWOData {

	SBMLDocument sbmlDoc;

	public PrimaryModelWOData(SBMLDocument sbmlDoc) {
		this.sbmlDoc = sbmlDoc;
	}

	public SBMLDocument getSBMLDoc() {
		return sbmlDoc;
	}
}
