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
package de.bund.bfr.knime.pmm.openfsmr;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;

/**
 * Template for the Food Safety Model Repository. Includes the properties:
 * <ul>
 * <li><b>Model name:</b> a name given to the model</li>
 * <li><b>Model id:</b> an unambiguous ID</li>
 * <li><b>Model link:</b> the link allowing to download the model file.</li>
 * <li><b>PMF organism:</b> Modelled organism / entity.</li>
 * <li><b>PMF organism detail:</b> Plain text comment describing further details on the modelled
 * organism / entity</li>
 * <li><b>PMF environment:</b> The environment of the organism / entity.</li>
 * <li><b>PMF environment detail:</b> Plain text comments describing the environment and or
 * experimental conditions</li>
 * <li><b>Model creator:</b> The person who contributed to the encoding of hte model in its present
 * form by creating the model file</li>
 * <li><b>Model reference description:</b> A citation to the reference description</li>
 * <li><b>Model reference description link:</b> A link to the reference description</li>
 * <li><b>Model created:</b> Temporal information on the model creation date</li>
 * <li><b>Model modified:</b> Temporal information on the last modification of the model</li>
 * <li><b>Model rights:</b> Information on rights held in and over the resource</li>
 * <li><b>Model notes:</b> Plain text comments describing the model</li>
 * <li><b>Model curation status</b>: The curation status of the model</li>
 * <li><b>Model type:</b> the type of model</li>
 * <li><b>Model subject:</b> the subject of the model</li>
 * <li><b>Model foodprocess:</b> Food production processes in which the model could be used</li>
 * <li><b>Model dependent variables:</b> The properties the model predicts</li>
 * <li><b>Model dependent variable units:</b> Unit of the predicted properties</li>
 * <li><b>Model dependent variable minimum:</b> The co-domain minimal values</li>
 * <li><b>Model dependent variable maximum:</b> The co-domain maximum values</li>
 * <li><b>Model independent variables:</b></li> The model's independent variable(s)
 * <li><b>Model independent variable units</b>
 * <li><b>Model independent variable minimum values</b>
 * <li><b>Model independent variable maximum values</b>
 * <li><b>Has data</b></li>
 * </ul>
 *
 * @author Miguel Alba
 */
public interface FSMRTemplate extends Serializable {

  /**
   * Returns the model name of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the model name is not set
   */
  public String getModelName();

  /**
   * Returns the model id of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the model id is not set
   */
  public String getModelId();

  /**
   * Returns the model link of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the model link is not set
   */
  public URL getModelLink();

  /**
   * Returns the organism names of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the organism name is not set
   */
  public String getOrganismName();

  /**
   * Returns the organism details of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the organism details is not set
   */
  public String getOrganismDetails();

  /**
   * Returns the matrix name of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the matrix name is not set
   */
  public String getMatrixName();

  /**
   * Returns the matrix details of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the matrix details is not set
   */
  public String getMatrixDetails();

  /**
   * Returns the creator of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the creator is not set
   */
  public String getCreator();

  /**
   * Returns the family name of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the family name is not set
   */
  public String getFamilyName();

  /**
   * Returns the contact of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the contact is not set
   */
  public String getContact();

  /**
   * Returns the reference description of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the contact is not set
   */
  public String getReferenceDescription();

  /**
   * Returns the reference description link of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the reference description link is not set
   */
  public URL getReferenceDescriptionLink();

  /**
   * Returns the created date of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the created date is not set
   */
  public Date getCreatedDate();

  /**
   * Returns the modified date of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the modified date is not set
   */
  public Date getModifiedDate();

  /**
   * Returns the rights of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the rights are not set
   */
  public String getRights();

  /**
   * Returns the notes of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the notes are not set
   */
  public String getNotes();

  /**
   * Returns the curation status of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the curation status is not set
   */
  public String getCurationStatus();

  /**
   * Returns the {@link ModelType} of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the model type is not set
   */
  public ModelType getModelType();

  /**
   * Returns the {@link ModelClass} of this {@link FSMRTemplate}.
   *
   * @throws RuntimeException if the model subject is not set
   */
  public ModelClass getModelSubject();

  /**
   * Returns the food process of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the food process is not set
   */
  public String getFoodProcess();

  /**
   * Returns the dependent variable of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the dependent variable is not set
   */
  public String getDependentVariable();

  /**
   * Returns the dependent variable unit of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the dependent variable unit is not set
   */
  public String getDependentVariableUnit();

  /**
   * Returns the dependent variable mininum of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the dependent variable minimum is not set
   */
  public double getDependentVariableMin();

  /**
   * Returns the dependent variable maximum of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the dependent variable maximum is not set
   */
  public double getDependentVariableMax();

  /**
   * Returns the independent variables of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the independent variables are not set
   */
  public String[] getIndependentVariables();

  /**
   * Returns the independent variables units of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the independent variables units are not set
   */
  public String[] getIndependentVariablesUnits();

  /**
   * Returns the independent variables minimum values of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the minimum values of the independent variables are not set
   */
  public double[] getIndependentVariablesMins();

  /**
   * Returns the independent variables maximum values of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the maximum values of the independent variables are not set
   */
  public double[] getIndependentVariablesMaxs();

  /**
   * Returns the hasData variable of this {@link FSMRTemplate}.
   * 
   * @throws RuntimeException if the hasData variable is not set
   */
  public boolean getHasData();

  /**
   * Sets the model name of this {@link FSMRTemplate} with 'modelName'.
   */
  public void setModelName(String modelName);

  /** Sets the model id of this {@link FSMRTemplate} with 'modelId'. */
  public void setModelId(String modelId);

  /** Sets the model link of this {@link FSMRTemplate} with 'modelLink'. */
  public void setModelLink(URL modelLink);

  /**
   * Sets the organism name of this {@link FSMRTemplate} with 'organismName'.
   */
  public void setOrganismName(String organismName);

  /**
   * Sets the organism details of this {@link FSMRTemplate} with 'organismDetails'.
   */
  public void setOrganismDetails(String organismDetails);

  /** Sets the matrix name of this {@link FSMRTemplate} with 'matrixName'. */
  public void setMatrixName(String matrixName);

  /**
   * Sets the matrix details of this {@link FSMRTemplate} with 'matrixDetails'.
   */
  public void setMatrixDetails(String matrixDetails);

  /** Sets the creator of this {@link FSMRTemplate} with 'creator'. */
  public void setCreator(String creator);

  /** Sets the family name of this {@link FSMRTemplate} with 'familyName'. */
  public void setFamilyName(String familyName);

  /** Sets the contact of this {@link FSMRTemplate} with 'contact'. */
  public void setContact(String contact);

  /**
   * Sets the reference description of this {@link FSMRTemplate} with 'referenceDescription'.
   */
  public void setReferenceDescription(String referenceDescription);

  /**
   * Sets the reference description link of this {@link FSMRTemplate} with
   * 'referenceDescriptionLink'.
   */
  public void setReferenceDescriptionLink(URL referenceDescriptionLink);

  /**
   * Sets the created date of this {@link FSMRTemplate} with 'createdDate'.
   */
  public void setCreatedDate(Date createdDate);

  /**
   * Sets the modified date of this {@link FSMRTemplate} with 'modifiedDate'.
   */
  public void setModifiedDate(Date modifiedDate);

  /** Sets the rights of this {@link FSMRTemplate} with 'rights'. */
  public void setRights(String rights);

  /** Sets the notes of this {@link FSMRTemplate} with 'notes'. */
  public void setNotes(String notes);

  /**
   * Sets the curation status of this {@link FSMRTemplate} with 'curationStatus'.
   */
  public void setCurationStatus(String curationStatus);

  /** Sets the {@link ModelType}s of this {@link FSMRTemplate} with 'type'. */
  public void setModelType(ModelType type);

  /** Sets the model subject of this {@link FSMRTemplate} with 'subject'. */
  public void setModelSubject(ModelClass subject);

  /**
   * Sets the food process of this {@link FSMRTemplate} with 'foodProcess'.
   */
  public void setFoodProcess(String foodProcess);

  /**
   * Sets the dependent variable of this {@link FSMRTemplate} with 'dependentVariable'.
   */
  public void setDependentVariable(String dependentVariable);

  /**
   * Sets the dependent variable unit of this {@link FSMRTemplate} with 'dependentVariableUnit'.
   */
  public void setDependentVariableUnit(String dependentVariableUnit);

  /**
   * Sets the dependent variable min of this {@link FSMRTemplate} with 'dependentVariableMin'.
   */
  public void setDependentVariableMin(double dependentVariableMin);

  /**
   * Sets the dependent variable max of this {@link FSMRTemplate} with 'dependentVariableMax'.
   */
  public void setDependentVariableMax(double dependentVariableMax);

  /**
   * Sets the independent variables of this {@link FSMRTemplate} with 'independentVariable'.
   */
  public void setIndependentVariables(String[] independentVariable);

  /**
   * Sets the independent variables units of this {@link FSMRTemplate} with
   * 'independentVariableUnits'.
   */
  public void setIndependentVariablesUnits(String[] independentVariableUnits);

  /**
   * Sets the independent variables minimum values of this {@link FSMRTemplate} with
   * 'independentVariableMins'.
   */
  public void setIndependentVariablesMins(double[] independentVariableMins);

  /**
   * Sets the independent variables maximum values of this {@link FSMRTemplate} with
   * 'independentVariableMaxs'.
   */
  public void setIndependentVariablesMaxs(double[] independentVariableMaxs);

  /**
   * Sets the hasData variable of this {@link FSMRTemplate} with 'hasData'.
   */
  public void setHasData(boolean hasData);

  /** Sets the model name value to null. */
  public void unsetModelName();

  /** Sets the model id value to null. */
  public void unsetModelId();

  /** Sets the model link value to null. */
  public void unsetModelLink();

  /** Sets the organism name value to null. */
  public void unsetOrganismName();

  /** Sets the organisnm detail value to null. */
  public void unsetOrganismDetail();

  /** Sets the matrix name value to null. */
  public void unsetMatrixName();

  /** Sets the matrix details value to null. */
  public void unsetMatrixDetails();

  /** Sets the creator value to null. */
  public void unsetCreator();

  /** Sets the family name value to null. */
  public void unsetFamilyName();

  /** Sets the contact value to null. */
  public void unsetContact();

  /** Sets the reference description value to null. */
  public void unsetReferenceDescription();

  /** Sets the reference description link value to null. */
  public void unsetReferenceDescriptionLink();

  /** Sets the created date value to null. */
  public void unsetCreatedDate();

  /** Sets the modified date value to null. */
  public void unsetModifiedDate();

  /** Sets the rights value to null. */
  public void unsetRights();

  /** Sets the notes value to null. */
  public void unsetNotes();

  /** Sets the curation status value to null. */
  public void unsetCurationStatus();

  /** Sets the model type value to null. */
  public void unsetModelType();

  /** Sets the model subject value to null. */
  public void unsetModelSubject();

  /** Sets the food process value to null. */
  public void unsetFoodProcess();

  /** Sets the dependent variable value to null. */
  public void unsetDependentVariable();

  /** Sets the dependent variable min value to null. */
  public void unsetDependentVariableMin();

  /** Sets the dependent variable max value to null. */
  public void unsetDependentVariableMax();

  /** Sets the independent variables value to null. */
  public void unsetIndependentVariables();

  /** Sets the independent variables units values to null. */
  public void unsetIndependentVariableUnits();

  /** Sets the independent variables minimum values to null. */
  public void unsetIndependentVariableMins();

  /** Sets the independent variables maximum values to null. */
  public void unsetIndependentVariableMaxs();

  /** Sets the hasData variable to null. */
  public void unsetHasData();

  /** Returns true if the model name of this {@link FSMRTemplate} is set. */
  public boolean isSetModelName();

  /** Returns true if the model id of this {@link FSMRTemplate} is set. */
  public boolean isSetModelId();

  /** Returns true if the model link of this {@link FSMRTemplate} is set. */
  public boolean isSetModelLink();

  /**
   * Returns true if the organism name of this {@link FSMRTemplate} is set.
   */
  public boolean isSetOrganismName();

  /**
   * Returns true if the organism details of this {@link FSMRTemplate} are set.
   */
  public boolean isSetOrganismDetails();

  /** Returns true if the matrix name of this {@link FSMRTemplate} is set. */
  public boolean isSetMatrixName();

  /**
   * Returns true if the matrix details of this {@link FSMRTemplate} are set.
   */
  public boolean isSetMatrixDetails();

  /** Returns true if the creator of this {@link FSMRTemplate} is set. */
  public boolean isSetCreator();

  /** Returns true if the family name of this {@link FSMRTemplate} is set. */
  public boolean isSetFamilyName();

  /** Returns true if the family name of this {@link FSMRTemplate} is set. */
  public boolean isSetContact();

  /**
   * Returns true if the reference description of this {@link FSMRTemplate} is set.
   */
  public boolean isSetReferenceDescription();

  /**
   * Returns true if the reference description link of this {@link FSMRTemplate} is set.
   */
  public boolean isSetReferenceDescriptionLink();

  /** Returns true if the created date of this {@link FSMRTemplate} is set. */
  public boolean isSetCreatedDate();

  /**
   * Returns true if the modified date of this {@link FSMRTemplate} is set.
   */
  public boolean isSetModifiedDate();

  /** Returns true if the rights of this {@link FSMRTemplate} is set. */
  public boolean isSetRights();

  /** Returns true if the notes of this {@link FSMRTemplate} is set. */
  public boolean isSetNotes();

  /**
   * Returns true if the curation status of this {@link FSMRTemplate} are set.
   */
  public boolean isSetCurationStatus();

  /**
   * Returns true if the {@link ModelType} of this {@link FSMRTemplate} is set.
   */
  public boolean isSetModelType();

  /** Returns true if the subject of this {@link FSMRTemplate} is set. */
  public boolean isSetModelSubject();

  /** Returns true if the food process of this {@link FSMRTemplate} is set. */
  public boolean isSetFoodProcess();

  /**
   * Returns true if the dependent variable of this {@link FSMRTemplate} is set.
   */
  public boolean isSetDependentVariable();

  /**
   * Returns true if the dependent variable unit of this {@link FSMRTemplate} is set.
   */
  public boolean isSetDependentVariableUnit();

  /**
   * Returns true if the dependent variable min of this {@link FSMRTemplate} is set.
   */
  public boolean isSetDependentVariableMin();

  /**
   * Returns true if the dependent variable max of this {@link FSMRTemplate} is set.
   */
  public boolean isSetDependentVariableMax();

  /**
   * Returns true if the independent variables of this {@link FSMRTemplate} is set.
   */
  public boolean isSetIndependentVariables();

  /** Returns true if the independent variables units of this {@link FSMRTemplate} are set. */
  public boolean isSetIndependentVariablesUnits();

  /**
   * Returns true if the independent variable minimum values of this {@link FSMRTemplate} are set.
   */
  public boolean isSetIndependentVariablesMins();

  /**
   * Returns true if the independent variable maximum values of this {@link FSMRTemplate} are set.
   */
  public boolean isSetIndependentVariablesMaxs();

  /**
   * Returns true if the hasData variables of this {@link FSMRTemplate} is set.
   */
  public boolean isSetHasData();
}
