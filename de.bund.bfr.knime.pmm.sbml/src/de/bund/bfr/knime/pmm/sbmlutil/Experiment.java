/**
 * Holds models and their data.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.numl.NuMLDocument;

/**
 * Holds a mandatory SBML document and an optional NuML document.
 * 
 * @author Miguel Alba
 */
public class Experiment {

	SBMLDocument model;
	NuMLDocument data;

	/**
	 * Creates an experiment without data.
	 * @param model SBMLDocument.
	 */
	public Experiment(SBMLDocument model) {
		this.model = model;
	}

	/**
	 * Creates an experiment with data.
	 * @param model SBMLDocument.
	 * @param data NuMLDocument.
	 */
	public Experiment(SBMLDocument model, NuMLDocument data) {
		this.model = model;
		this.data = data;
	}

	// Getters and setters
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