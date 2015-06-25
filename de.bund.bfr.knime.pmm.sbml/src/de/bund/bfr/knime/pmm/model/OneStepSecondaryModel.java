package de.bund.bfr.knime.pmm.model;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

/**
 * One step secondary model
 * 
 * It has a SBML document for a secondary model and its primary model, which can
 * be linked to N NuMLDocuments.
 * 
 * @author Miguel Alba
 */
public class OneStepSecondaryModel {

	SBMLDocument sbmlDoc; // Document with primary and secondary models
	List<NuMLDocument> numlDocs; // Data documents

	public OneStepSecondaryModel(SBMLDocument sbmlDoc,
			List<NuMLDocument> numlDocs) {
		this.sbmlDoc = sbmlDoc;
		this.numlDocs = numlDocs;
	}

	public SBMLDocument getSBMLDoc() {
		return sbmlDoc;
	}

	public List<NuMLDocument> getNumlDocs() {
		return numlDocs;
	}
}
