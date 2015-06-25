package de.bund.bfr.knime.pmm.model;

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

/**
 * Two step secondary model
 * 
 * Includes a SBML document for the secondary model (master) and N primary
 * models with data.
 * 
 * @author Miguel Alba
 */
public class TwoStepSecondaryModel {

	SBMLDocument secDoc; // secondary model (master) document
	List<PrimaryModelWData> primModels; // primary models with data

	public TwoStepSecondaryModel(SBMLDocument secDoc,
			List<PrimaryModelWData> primModels) {
		this.secDoc = secDoc;
		this.primModels = primModels;
	}

	public SBMLDocument getSecDoc() {
		return secDoc;
	}

	public List<PrimaryModelWData> getPrimModels() {
		return primModels;
	}
}
