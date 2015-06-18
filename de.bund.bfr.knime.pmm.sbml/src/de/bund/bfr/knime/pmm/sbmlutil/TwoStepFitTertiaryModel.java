package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.SBMLDocument;

/**
 * Case 5. Tertiary model generated with a two-step fit approach. One SBML
 * document for the tertiary model (master) including references to the
 * secondary submodels and n SBML documents for the secondary models.
 * 
 * @author Miguel Alba
 */
public class TwoStepFitTertiaryModel {

	SBMLDocument masterDoc; // tertiary model (master)
	SBMLDocument[] secDocs; // secondary models

	public TwoStepFitTertiaryModel(SBMLDocument masterDoc,
			SBMLDocument[] secDocs) {
		this.masterDoc = masterDoc;
		this.secDocs = secDocs;
	}

	public SBMLDocument getMasterDoc() {
		return masterDoc;
	}

	public SBMLDocument[] getSecondaryDocs() {
		return secDocs;
	}
}
