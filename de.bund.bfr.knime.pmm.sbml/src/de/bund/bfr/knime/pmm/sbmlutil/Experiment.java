/**
 * Holds models and their data.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

public class Experiment {
	private SBMLDocument model;
	private NuMLDocument data;

	public Experiment(SBMLDocument model) {
		this.model = model;
	}

	public Experiment(SBMLDocument model, NuMLDocument data) {
		this.model = model;
		this.data = data;
	}

	public SBMLDocument getModel() {
		return model;
	}

	public void setModel(SBMLDocument model) {
		this.model = model;
	}

	public NuMLDocument getData() {
		return data;
	}

	public void setData(NuMLDocument data) {
		this.data = data;
	}
}