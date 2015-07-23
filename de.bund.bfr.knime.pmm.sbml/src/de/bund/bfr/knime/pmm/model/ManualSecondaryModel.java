package de.bund.bfr.knime.pmm.model;

import org.sbml.jsbml.SBMLDocument;

/**
 * Case 2c: Manual secondary model
 * 
 * Holds secondary models generated manually, with a SBMLDocument per each
 * secondary model.
 * 
 * @author Miguel Alba
 */
public class ManualSecondaryModel {
	SBMLDocument sbmlDoc;

	public ManualSecondaryModel(SBMLDocument sbmlDoc) {
		this.sbmlDoc = sbmlDoc;
	}

	public SBMLDocument getSBMLDoc() {
		return sbmlDoc;
	}
}
