package de.bund.bfr.knime.pmm.model;

import org.sbml.jsbml.SBMLDocument;

/**
 * Case 1b: Primary model without data files.
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
