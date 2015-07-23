package de.bund.bfr.knime.pmm.model;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

/**
 * Case 1a: Primary model with data.
 * 
 * Each primary model includes a NuMLDocument.
 * 
 * @author Miguel Alba
 */
public class PrimaryModelWData {

	SBMLDocument sbmlDoc;
	NuMLDocument numlDoc;

	public PrimaryModelWData(SBMLDocument sbmlDoc, NuMLDocument numlDoc) {
		this.sbmlDoc = sbmlDoc;
		this.numlDoc = numlDoc;
	}

	public SBMLDocument getSBMLDoc() {
		return sbmlDoc;
	}

	public NuMLDocument getNuMLDoc() {
		return numlDoc;
	}
}
