package de.bund.bfr.knime.pmm.model;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

/**
 * Case 3b: Tertiary model generated with 2-step fit approach.
 * 
 * Keeps one SBML Document per each tertiary model, linked to N SBMLDocuments
 * for the secondary models. The SBMLDocument of the tertiary model also links
 * to a number of data files.
 * 
 * @author Miguel Alba
 */
public class OneStepTertiaryModel {

	SBMLDocument tertiaryDoc;
	List<SBMLDocument> secDocs;
	List<NuMLDocument> dataDocs;

	public OneStepTertiaryModel(SBMLDocument tertiaryDoc,
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
