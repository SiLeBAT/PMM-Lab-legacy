package de.bund.bfr.knime.pmm.model;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

/**
 * Case 3a: Tertiary model generated with 2-step fit approach.
 * 
 * Keeps one SBML Document per each tertiary model, linked to N SBMLDocuments
 * for the secondary models. The SBMLDocument of the tertiary model also links
 * to a number of data files.
 * 
 * @author Miguel Alba
 *
 */
public class TwoStepTertiaryModel {

	SBMLDocument tertiaryDoc;
	List<PrimaryModelWData> primModels;
	List<SBMLDocument> secDocs;

	public TwoStepTertiaryModel(SBMLDocument tertDoc,
			List<PrimaryModelWData> primModels, List<SBMLDocument> secDocs) {
		this.tertiaryDoc = tertDoc;
		this.primModels = primModels;
		this.secDocs = secDocs;
	}

	public SBMLDocument getTertDoc() {
		return tertiaryDoc;
	}

	public List<PrimaryModelWData> getPrimModels() {
		return primModels;
	}

	public List<SBMLDocument> getSecDocs() {
		return secDocs;
	}
}
