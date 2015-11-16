/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.pmf.sbml;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Species;

import de.bund.bfr.pmf.ModelType;

/**
 * @author Miguel Alba
 *
 */
public class SBMLFactory {

	public static Metadata createMetadata() {
		return new MetadataImpl();
	}

	public static Metadata createMetadata(String givenName, String familyName, String contact, String createdDate,
			String modifiedDate, ModelType type, String rights, String referenceLink) {
		return new MetadataImpl(givenName, familyName, contact, createdDate, modifiedDate, type, rights, referenceLink);
	}

	public static PMFCoefficient createPMFCoefficient(Parameter parameter) {
		return new PMFCoefficientImpl(parameter);
	}

	public static PMFCoefficient createPMFCoefficient(String id, double value, String unit, Double P, Double error,
			Double t, Correlation[] correlations, String desc) {
		return new PMFCoefficientImpl(id, value, unit, P, error, t, correlations, desc);
	}

	public static PMFCoefficient createPMFCoefficient(String id, double value, String unit) {
		return new PMFCoefficientImpl(id, value, unit);
	}
	
	public static PMFCompartment createPMFCompartment(Compartment compartment) {
		return new PMFCompartmentImpl(compartment);
	}
	
	public static PMFCompartment createPMFCompartment(String id, String name, String  pmfCode, String detail,
			ModelVariable[] modelVariables) {
		return new PMFCompartmentImpl(id, name, pmfCode, detail, modelVariables);
	}
	
	public static PMFCompartment createPMFCompartment(String id, String name) {
		return new PMFCompartmentImpl(id, name);
	}

	public static PMFSpecies createPMFSpecies(Species species) {
		return new PMFSpeciesImpl(species);
	}

	public static PMFSpecies createPMFSpecies(String compartment, String id, String name, String substanceUnits,
			String combaseCode, String detail, String description) {
		return new PMFSpeciesImpl(compartment, id, name, substanceUnits, combaseCode, detail, description);
	}

	public static PMFSpecies createPMFSpecies(String compartment, String id, String name, String substanceUnits) {
		return new PMFSpeciesImpl(compartment, id, name, substanceUnits);
	}

	public static Reference createReference(String author, Integer year, String title, String abstractText,
			String journal, String volume, String issue, Integer page, Integer approvalMode, String website,
			ReferenceType referenceType, String comment) {
		return new ReferenceImpl(author, year, title, abstractText, journal, volume, issue, page, approvalMode, website,
				referenceType, comment);
	}

	public static Uncertainties createUncertainties() {
		return new UncertaintiesImpl();
	}

	public static Uncertainties createUncertainties(Integer id, String modelName, String comment, Double r2, Double rms,
			Double sse, Double aic, Double bic, Integer dof) {
		return new UncertaintiesImpl(id, modelName, comment, r2, rms, sse, aic, bic, dof);
	}
}
