package de.bund.bfr.knime.pmm.model;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

public class ManualTertiaryModel {

	SBMLDocument tertiaryDoc;
	List<SBMLDocument> secDocs;

	public ManualTertiaryModel(SBMLDocument tertiaryDoc,
			List<SBMLDocument> secDocs) {
		this.tertiaryDoc = tertiaryDoc;
		this.secDocs = secDocs;
	}

	public SBMLDocument getTertiaryDoc() {
		return tertiaryDoc;
	}

	public List<SBMLDocument> getSecDocs() {
		return secDocs;
	}
}
