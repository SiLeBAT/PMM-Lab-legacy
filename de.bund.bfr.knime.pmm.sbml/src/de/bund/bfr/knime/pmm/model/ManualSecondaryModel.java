package de.bund.bfr.knime.pmm.model;

import org.sbml.jsbml.SBMLDocument;

public class ManualSecondaryModel {
	SBMLDocument sbmlDoc;
	
	public ManualSecondaryModel(SBMLDocument sbmlDoc) {
		this.sbmlDoc = sbmlDoc;
	}
	
	public SBMLDocument getSBMLDoc() {
		return sbmlDoc;
	}
}

