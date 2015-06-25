package de.bund.bfr.knime.pmm.model;

import de.bund.bfr.numl.NuMLDocument;

/**
 * Case 0. Each document is a NuMLDocument that keeps a time series.
 * 
 * @author Miguel Alba
 */
public class ExperimentalData {

	NuMLDocument numlDoc;

	public ExperimentalData(NuMLDocument numlDoc) {
		this.numlDoc = numlDoc;
	}

	public NuMLDocument getNuMLDocument() {
		return numlDoc;
	}
}
