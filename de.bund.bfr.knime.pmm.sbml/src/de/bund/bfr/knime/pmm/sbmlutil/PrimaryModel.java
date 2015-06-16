package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

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