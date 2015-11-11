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

	SBMLDocument tertDoc;
	String terDocName;
	List<SBMLDocument> secDocs;
	List<String> secDocNames;

	public ManualTertiaryModel(SBMLDocument tertiaryDoc, String tertDocName, List<SBMLDocument> secDocs,
			List<String> secDocNames) {
		this.tertDoc = tertiaryDoc;
		this.terDocName = tertDocName;
		this.secDocs = secDocs;
		this.secDocNames = secDocNames;
	}

	public SBMLDocument getTertDoc() {
		return tertDoc;
	}

	public String getTerDocName() {
		return terDocName;
	}

	public List<SBMLDocument> getSecDocs() {
		return secDocs;
	}

	public List<String> getSecDocNames() {
		return secDocNames;
	}
}
