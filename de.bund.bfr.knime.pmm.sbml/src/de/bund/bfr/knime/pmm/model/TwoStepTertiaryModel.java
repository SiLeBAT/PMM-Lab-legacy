package de.bund.bfr.knime.pmm.model;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

public class TwoStepTertiaryModel {

	SBMLDocument tertiaryDoc;
	List<SBMLDocument> secDocs;
	List<NuMLDocument> dataDocs;

	public TwoStepTertiaryModel(SBMLDocument tertiaryDoc,
			List<SBMLDocument> secDocs, List<NuMLDocument> dataDocs) {
		this.tertiaryDoc = tertiaryDoc;
		this.secDocs = secDocs;
		this.dataDocs = dataDocs;
	}

	public SBMLDocument getTertiaryDoc() {
		return tertiaryDoc;
	}

	public List<SBMLDocument> getSecDocs() {
		return secDocs;
	}

	public List<NuMLDocument> getDataDocs() {
		return dataDocs;
	}
}
