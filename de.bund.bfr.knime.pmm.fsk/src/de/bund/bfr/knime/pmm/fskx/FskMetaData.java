/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/

package de.bund.bfr.knime.pmm.fskx;

import java.net.URL;
import java.util.Date;
import java.util.List;

import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

/**
 * TODO: docstring
 * 
 * @author Miguel de Alba, BfR, Berlin
 */
public interface FskMetaData {

	// --- model name ---
	/**
	 * @return the model name
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getModelName();

	/**
	 * @return whether the model name is set
	 */
	public boolean isSetModelName();

	/**
	 * @param modelName
	 *            Null or empty strings are ignored
	 */
	public void setModelName(final String name);

	/** Unsets the model name. */
	public void unsetModelName();

	// --- model id ---
	/**
	 * @return the model id
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getModelId();

	/**
	 * @return whether the model id is set
	 */
	public boolean isSetModelId();

	/**
	 * @param modelId
	 *            Null or empty strings are ignored
	 */
	public void setModelId(final String id);

	/** Unsets the model id. */
	public void unsetModelId();

	// --- model link ---
	/**
	 * @return the model link
	 * @throws RuntimeException
	 *             if not set
	 */
	public URL getModelLink();

	/**
	 * @return whether the model link is set.
	 */
	public boolean isSetModelLink();

	/**
	 * @param link
	 *            Null URLs are ignored
	 */
	public void setModelLink(final URL link);

	/** Unsets the model link. */
	public void unsetModelLink();

	// --- organism name ---
	/**
	 * @return the organism name
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getOrganism();

	/**
	 * @return whether the organism name is set
	 */
	public boolean isSetOrganism();

	/**
	 * @param name
	 *            Null or empty strings are ignored
	 */
	public void setOrganism(final String organism);

	/** Unsets the organism name. */
	public void unsetOrganism();

	// --- organism details ---
	/**
	 * @return the organism details
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getOrganismDetails();

	/**
	 * @return whether the organism details are set
	 */
	public boolean isSetOrganismDetails();

	/**
	 * @param details
	 *            Null or empty strings are ignored
	 */
	public void setOrganismDetails(final String details);

	/** Unsets the organism details. */
	public void unsetOrganismDetails();

	// --- matrix name ---
	/**
	 * @return the matrix name
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getMatrix();

	/**
	 * @return whether the matrix is set
	 */
	public boolean isSetMatrix();

	/**
	 * @param name
	 *            Null or empty strings are ignored
	 */
	public void setMatrix(final String name);

	/** Unsets the matrix name. */
	public void unsetMatrix();

	// --- matrix details ---
	/**
	 * @return the matrix details
	 * @throws RuntimeException
	 *             if not et
	 */
	public String getMatrixDetails();

	/**
	 * @return whether the matrix details are set
	 */
	public boolean isSetMatrixDetails();

	/**
	 * @param details
	 *            Null or empty strings are ignored
	 */
	public void setMatrixDetails(final String details);

	/** Unsets the matrix details. */
	public void unsetMatrixDetails();

	// --- creator ---
	/**
	 * @return the model creator
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getCreator();

	/**
	 * @return whether the model creator is set
	 */
	public boolean isSetCreator();

	/**
	 * @param string
	 *            creator Null and empty strings are ignored
	 */
	public void setCreator(final String creator);

	/** Unsets the model creator. */
	public void unsetCreator();

	// --- family name ---
	/**
	 * @return the family name
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getFamilyName();

	/**
	 * @return whether the family name is set
	 */
	public boolean isSetFamilyName();

	/**
	 * @param familyName
	 *            Null and empty strings are ignored
	 */
	public void setFamilyName(final String familyName);

	/** Unsets the family name. */
	public void unsetFamilyName();

	// --- contact ---
	/**
	 * @return the contact
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getContact();

	/**
	 * @return whether the model contact is set
	 */
	public boolean isSetContact();

	/**
	 * @param contact
	 *            Null and empty strings are ignored
	 */
	public void setContact(final String contact);

	/** Unsets the model contact. */
	public void unsetContact();

	// --- reference description ---
	/**
	 * @return the reference description of the model
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getReferenceDescription();

	/**
	 * @return whether the reference description of the model is set
	 */
	public boolean isSetReferenceDescription();

	/**
	 * @param description
	 *            Null and empty strings are ignored
	 */
	public void setReferenceDescription(final String description);

	/** Unsets the reference description. */
	public void unsetReferenceDescription();

	// --- reference description link ---

	/**
	 * @return the link to the reference description of the model
	 * @throws RuntimeException
	 *             if not set
	 */
	public URL getReferenceDescriptionLink();

	/**
	 * @return whether the link to the reference description of the model is set
	 */
	public boolean isSetReferenceDescriptionLink();

	/**
	 * @param link
	 *            Null URLs are ignored
	 */
	public void setReferenceDescriptionLink(final URL link);

	/** Unsets the link to the reference description of the model. */
	public void unsetReferenceDescriptionLink();

	// --- creation date ---

	/**
	 * @return the creation date
	 * @throws RuntimeException
	 *             if not set
	 */
	public Date getCreatedDate();

	/**
	 * @return whether the creation date is set
	 */
	public boolean isSetCreatedDate();

	/**
	 * @param date
	 *            Null Dates are ignored
	 */
	public void setCreatedDate(final Date date);

	/** Unsets the creation date. */
	public void unsetCreatedDate();

	// --- last modification date ---

	/**
	 * @return the last modification date
	 * @throws RuntimeException
	 *             if not set
	 */
	public Date getModifiedDate();

	/**
	 * @return whether last modification date is set
	 */
	public boolean isSetModifiedDate();

	/**
	 * @param date
	 *            Null Dates are ignored
	 */
	public void setModifiedDate(final Date date);

	/** Unsets the last modification date. */
	public void unsetModifiedDate();

	// --- rights ---

	/**
	 * @return the rights of the model
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getRights();

	/**
	 * @return whether the model rights are set
	 */
	public boolean isSetRights();

	/**
	 * @param rights
	 *            Null or empty strings are ignored
	 */
	public void setRights(final String rights);

	/** Unsets the model rights. */
	public void unsetRights();

	// --- notes ---

	/**
	 * @return the notes of the model
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getNotes();

	/**
	 * @return whether the model notes are set
	 */
	public boolean isSetNotes();

	/**
	 * @param notes
	 *            Null or empty strings are ignored
	 */
	public void setNotes(final String notes);

	/** Unsets the model notes. */
	public void unsetNotes();

	/**
	 * The model is not curated unless specified.
	 * 
	 * @return whether the model is curated
	 */
	public boolean isCurated();

	/**
	 * Sets whether the model is curated
	 * 
	 * @param isCurated
	 */
	public void setCurated(final boolean isCurated);

	// --- model type ---
	/**
	 * @return the model type
	 * @throws RuntimeException
	 *             if not set
	 */
	public ModelType getModelType();

	/**
	 * @return whether the model type is set
	 */
	public boolean isSetModelType();

	/**
	 * @param type
	 *            Null is ignored
	 */
	public void setModelType(final ModelType type);

	/** Unsets the model type. */
	public void unsetModelType();

	// --- model subject ---
	/**
	 * @return the model class
	 * @throws RuntimeException
	 *             if not set
	 */
	public ModelClass getModelSubject();

	/**
	 * @return whether the model subject is set
	 */
	public boolean isSetModelSubject();

	/**
	 * @param modelSubject
	 *            Null is ignored
	 */
	public void setModelSubject(final ModelClass subject);

	/** Unsets the model class. */
	public void unsetModelSubject();

	// --- food process ---

	/**
	 * @return the food process
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getFoodProcess();

	/**
	 * @return whether the food process is set
	 */
	public boolean isSetFoodProcess();

	/**
	 * @param process
	 *            Null or empty strings are ignored
	 */
	public void setFoodProcess(final String process);

	/** Unsets the food process. */
	public void unsetFoodProcess();

	// --- dependent variable ---

	/**
	 * @return the dependent variable
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getDependentVariable();

	/**
	 * @return whether the dependent variable is set
	 */
	public boolean isSetDependentVariable();

	/**
	 * @param var
	 *            Null and empty strings are ignored
	 */
	public void setDependentVariable(final String var);

	/** Unsets the dependent variable. */
	public void unsetDependentVariable();

	// --- dependent variable unit ---

	/**
	 * @return the unit of the dependent variable
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getDependentVariableUnit();

	/**
	 * @return whether the unit of the dependent variable is set
	 */
	public boolean isSetDependentVariableUnit();

	/**
	 * @param unit
	 *            Null and empty strings are ignored
	 */
	public void setDependentVariableUnit(final String unit);

	/** Unsets the unit of the dependent variable. */
	public void unsetDependentVariableUnit();

	// -- minimum value of the dependent variable ---

	/**
	 * @return the minimum value of the dependent variable
	 * @throws RuntimeException
	 *             if not set
	 */
	public double getDependentVariableMin();

	/**
	 * @return whether the minimum value of the dependent variable is set
	 */
	public boolean isSetDependentVariableMin();

	public void setDependentVariableMin(final double min);

	/** Unsets the minimum value of the independent variable. */
	public void unsetDependentVariableMin();

	// --- maximum value of the dependent variable ---

	/**
	 * @return the maximumm value of the dependent variable
	 * @throws RuntimeException
	 *             if not set
	 */
	public double getDependentVariableMax();

	/**
	 * @return whether the maximum value of the dependent variable is set
	 */
	public boolean isSetDependentVariableMax();

	public void setDependentVariableMax(final double max);

	/** Unsets the maximum value of the dependent variable. */
	public void unsetDependentVariableMax();

	// --- independent variables ---

	/**
	 * @return the independent variables
	 * @throws RuntimeException
	 *             if not set
	 */
	public List<String> getIndependentVariables();

	/**
	 * @return whether the independent variables are set
	 */
	public boolean isSetIndependentVariables();

	/**
	 * @param vars
	 *            Null or empty lists are ignored
	 */
	public void setIndependentVariables(final List<String> vars);

	/** Unsets the independent variables. */
	public void unsetIndependentVariables();

	// --- units of the independent variables ---

	/**
	 * @return the units of the independent variables
	 * @throws RuntimeException
	 *             if not set
	 */
	public List<String> getIndependentVariableUnits();

	/**
	 * @return whether the units of the independent variables are set
	 */
	public boolean isSetIndependentVariableUnits();

	/**
	 * @param vars
	 *            Null and empty lists are ignored
	 */
	public void setIndependentVariableUnits(final List<String> units);

	/** Unsets the units of the independent variables. */
	public void unsetIndependentVariableUnits();

	// --- Minimum values of the independent variables ---
	/**
	 * @return the minimum values of the independent variables
	 * @throws RuntimeException
	 *             if not set
	 */
	public List<Double> getIndependentVariableMins();

	/**
	 * @return whether the minimum values of the independent variables are set
	 */
	public boolean isSetIndependentVariableMins();

	/**
	 * @param mins
	 *            Null and empty lists are ignored
	 */
	public void setIndependentVariableMins(final List<Double> mins);

	/** Unsets the minimum values of the independent variables. */
	public void unsetIndependentVariableMins();

	// --- maximum values of the independent variables ---
	/**
	 * @return the maximum values of the independent variables
	 * @throws RuntimeException
	 *             if not set
	 */
	public List<Double> getIndependentVariableMaxs();

	/**
	 * @return whether the maximum values of the independent variables are set
	 */
	public boolean isSetIndependentVariableMaxs();

	/**
	 * @param maxs
	 *            Null and empty strings are ignored
	 */
	public void setIndependentVariableMaxs(final List<Double> maxs);

	/**
	 * Unsets the maximum values of the independent variables
	 */
	public void unsetIndependentVariableMaxs();

	// --- values of the independent variables ---
	/**
	 * @return the values of the independent variables
	 * @throws RuntimeException
	 *             if not set
	 */
	public List<Double> getIndependentVariableValues();

	/**
	 * @return whether the values of the independent variables are set
	 */
	public boolean isSetIndependentVariableValues();

	/**
	 * @param values
	 *            Null and empty lists are ignored
	 */
	public void setIndependentVariableValues(final List<Double> values);

	/** Unsets the values of the independent variables. */
	public void unsetIndependentVariableValues();

	/**
	 * The model has no data unless specified.
	 * 
	 * @return whether the model has data associated
	 */
	public boolean hasData();

	/**
	 * Sets whether the model has data.
	 */
	public void setHasData(final boolean hasData);
}