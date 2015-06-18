package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

/**
 * Case 4 Primary model. Holds a mandatory SBML document and an optional NuML
 * document.
 * @author Miguel Alba
 */
public class PrimaryModel {

	SBMLDocument sbmlDoc;
	NuMLDocument numlDoc;
	
	public PrimaryModel(SBMLDocument sbmlDoc) {
		this.sbmlDoc = sbmlDoc;
	}
	
	public PrimaryModel(SBMLDocument sbmlDoc, NuMLDocument numlDoc) {
		this.sbmlDoc = sbmlDoc;
		this.numlDoc = numlDoc;
	}
	
	public SBMLDocument getSBMLDocument() {
		return sbmlDoc;
	}
	
	public NuMLDocument getNuMLDocument() {
		return numlDoc;
	}
}