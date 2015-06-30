package de.bund.bfr.knime.pmm.model;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

/**
 * Case 3c: Tertiary model generated manually.
 * 
 * Keeps a master SBMLDocument per tertiary model linked to N SBMLDocuments for
 * the secondary models. It has no data.
 * 
 * @author Miguel Alba
 */
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
