package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

public class TertiaryModel {

	SBMLDocument sbmlDoc;
	List<NuMLDocument> numlDocs;

	public TertiaryModel(SBMLDocument sbmlDoc) {
		this.sbmlDoc = sbmlDoc;
	}

	public TertiaryModel(SBMLDocument sbmlDoc, List<NuMLDocument> numlDocs) {
		this.sbmlDoc = sbmlDoc;
		this.numlDocs = numlDocs;
	}

	public SBMLDocument getSBMLDocument() {
		return sbmlDoc;
	}

	public List<NuMLDocument> getNuMLDocuments() {
		return numlDocs;
	}
}