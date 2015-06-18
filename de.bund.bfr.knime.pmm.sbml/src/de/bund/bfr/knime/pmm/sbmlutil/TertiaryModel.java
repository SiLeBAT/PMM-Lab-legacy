package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

/**
 * Case 1. Tertiary model generated with a one-step fit approach. Keeps an SBML
 * document for the tertiary model which include the primary model and the
 * secondary models. This tertiary model also keeps a list of NuML documents.
 * 
 * @author Miguel Alba
 */
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