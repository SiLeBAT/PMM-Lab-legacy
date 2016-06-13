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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

/**
 * Implementation of the Open Food Safety Model Repository template ( {@link FSMRTemplate}) based on
 * instance variables.
 * 
 * @author Miguel Alba
 */
public class FSMRTemplateImpl implements FSMRTemplate {

  private static final long serialVersionUID = -3414978919227860002L;

  private String modelName;
  private boolean isSetModelName;

  private String modelId;
  private boolean isSetModelId;

  private URL modelLink;
  private boolean isSetModelLink;

  private String organismName;
  private boolean isSetOrganismName;

  private String organismDetails;
  private boolean isSetOrganismDetails;

  private String matrixName;
  private boolean isSetMatrixName;

  private String matrixDetails;
  private boolean isSetMatrixDetails;

  private String creator;
  private boolean isSetCreator;

  private String familyName;
  private boolean isSetFamilyName;

  private String contact;
  private boolean isSetContact;

  private String referenceDescription;
  private boolean isSetReferenceDescription;

  private URL referenceDescriptionLink;
  private boolean isSetReferenceDescriptionLink;

  private Date createdDate;
  private boolean isSetCreatedDate;

  private Date modifiedDate;
  private boolean isSetModifiedDate;

  private String rights;
  private boolean isSetRights;

  private String notes;
  private boolean isSetNotes;

  private String curationStatus;
  private boolean isSetCurationStatus;

  private ModelType modelType;
  private boolean isSetModelType;

  private ModelClass modelSubject;
  private boolean isSetModelSubject;

  private String foodProcess;
  private boolean isSetFoodProcess;

  private String dependentVariable;
  private boolean isSetDependentVariable;

  private String dependentVariableUnit;
  private boolean isSetDependentVariableUnit;

  private double dependentVariableMin;
  private boolean isSetDependentVariableMin;

  private double dependentVariableMax;
  private boolean isSetDependentVariableMax;

  private String[] independentVariables;
  private boolean isSetIndependentVariables;

  private String[] independentVariableUnits;
  private boolean isSetIndependentVariablesUnits;

  private double[] independentVariableMins;
  private boolean isSetIndependentVariablesMins;

  private double[] independentVariableMaxs;
  private boolean isSetIndependentVariablesMaxs;

  private boolean hasData;
  private boolean isSetHasData;

  /** {@inheritDoc} */
  public String getModelName() {
    if (isSetModelName) {
      return modelName;
    }
    throw new RuntimeException("Model name not set");
  }

  /** {@inheritDoc} */
  public String getModelId() {
    if (isSetModelId) {
      return modelId;
    }
    throw new RuntimeException("Model id not set");
  }

  /** {@inheritDoc} */
  public URL getModelLink() {
    if (isSetModelLink) {
      return modelLink;
    }
    throw new RuntimeException("Model link not set");
  }

  /** {@inheritDoc} */
  public String getOrganismName() {
    if (isSetOrganismName) {
      return organismName;
    }
    throw new RuntimeException("Organism name not set");
  }

  /** {@inheritDoc} */
  public String getOrganismDetails() {
    if (isSetOrganismDetails) {
      return organismDetails;
    }
    throw new RuntimeException("Organism details not set");
  }

  /** {@inheritDoc} */
  public String getMatrixName() {
    if (isSetMatrixName) {
      return matrixName;
    }
    throw new RuntimeException("Matrix name not set");
  }

  /** {@inheritDoc} */
  public String getMatrixDetails() {
    if (isSetMatrixDetails) {
      return matrixDetails;
    }
    throw new RuntimeException("Matrix details not set");
  }

  /** {@inheritDoc} */
  public String getCreator() {
    if (isSetCreator) {
      return creator;
    }
    throw new RuntimeException("Creator not set");
  }

  /** {@inheritDoc} */
  public String getFamilyName() {
    if (isSetFamilyName) {
      return familyName;
    }
    throw new RuntimeException("Family name not set");
  }

  /** {@inheritDoc} */
  public String getContact() {
    if (isSetContact) {
      return contact;
    }
    throw new RuntimeException("Contact not set");
  }

  /** {@inheritDoc} */
  public String getReferenceDescription() {
    if (isSetReferenceDescription) {
      return referenceDescription;
    }
    throw new RuntimeException("Reference descriptio not set");
  }

  /** {@inheritDoc} */
  public URL getReferenceDescriptionLink() {
    if (isSetReferenceDescriptionLink) {
      return referenceDescriptionLink;
    }
    throw new RuntimeException("Reference description link not set");
  }

  /** {@inheritDoc} */
  public Date getCreatedDate() {
    if (isSetCreatedDate) {
      return createdDate;
    }
    throw new RuntimeException("Created date not set");
  }

  /** {@inheritDoc} */
  public Date getModifiedDate() {
    if (isSetModifiedDate) {
      return modifiedDate;
    }
    throw new RuntimeException("Modified date not set");
  }

  /** {@inheritDoc} */
  public String getRights() {
    if (isSetRights) {
      return rights;
    }
    throw new RuntimeException("Rights not set");
  }

  /** {@inheritDoc} */
  public String getNotes() {
    if (isSetNotes) {
      return notes;
    }
    throw new RuntimeException("Notes not set");
  }

  /** {@inheritDoc} */
  public String getCurationStatus() {
    if (isSetCurationStatus) {
      return curationStatus;
    }
    throw new RuntimeException("Curation status not set");
  }

  /** {@inheritDoc} */
  public ModelType getModelType() {
    if (isSetModelType) {
      return modelType;
    }
    throw new RuntimeException("Model type not set");
  }

  /** {@inheritDoc} */
  public ModelClass getModelSubject() {
    if (isSetModelSubject) {
      return modelSubject;
    }
    throw new RuntimeException("Model subject not set");
  }

  /** {@inheritDoc} */
  public String getFoodProcess() {
    if (isSetFoodProcess) {
      return foodProcess;
    }
    throw new RuntimeException("Food process not set");
  }

  /** {@inheritDoc} */
  public String getDependentVariable() {
    if (isSetDependentVariable) {
      return dependentVariable;
    }
    throw new RuntimeException("Dependent variable not set");
  }

  /** {@inheritDoc} */
  public String getDependentVariableUnit() {
    if (isSetDependentVariableUnit) {
      return dependentVariableUnit;
    }
    throw new RuntimeException("Dependent variable unit not set");
  }

  /** {@inheritDoc} */
  public double getDependentVariableMin() {
    if (isSetDependentVariableMin) {
      return dependentVariableMin;
    }
    throw new RuntimeException("Minimum value of dependent variable not set");
  }

  /** {@inheritDoc} */
  public double getDependentVariableMax() {
    if (isSetDependentVariableMax) {
      return dependentVariableMax;
    }
    throw new RuntimeException("Maximum value of dependent variable not set");
  }

  /** {@inheritDoc} */
  public String[] getIndependentVariables() {
    if (isSetIndependentVariables) {
      return independentVariables;
    }
    throw new RuntimeException("Independent variables not set");
  }

  /** {@inheritDoc} */
  public String[] getIndependentVariablesUnits() {
    if (isSetIndependentVariablesUnits) {
      return independentVariableUnits;
    }
    throw new RuntimeException("Independent variables units not set");
  }

  /** {@inheritDoc} */
  public double[] getIndependentVariablesMins() {
    if (isSetIndependentVariablesMins) {
      return independentVariableMins;
    }
    throw new RuntimeException("Minimum values of independent variables not set");
  }

  /** {@inheritDoc} */
  public double[] getIndependentVariablesMaxs() {
    if (isSetIndependentVariablesMaxs) {
      return independentVariableMaxs;
    }
    throw new RuntimeException("Maximum values of independent variables not set");
  }

  /** {@inheritDoc} */
  public boolean getHasData() {
    if (isSetHasData) {
      return hasData;
    }
    throw new RuntimeException("hasData not set");
  }

  /** {@inheritDoc} */
  public void setModelName(final String modelName) {
    this.modelName = modelName;
    isSetModelName = true;
  }

  /** {@inheritDoc} */
  public void setModelId(final String modelId) {
    this.modelId = modelId;
    isSetModelId = true;
  }

  /** {@inheritDoc} */
  public void setModelLink(final URL modelLink) {
    this.modelLink = modelLink;
    isSetModelLink = true;
  }

  /** {@inheritDoc} */
  public void setOrganismName(final String organismName) {
    this.organismName = organismName;
    isSetOrganismName = true;
  }

  /** {@inheritDoc} */
  public void setOrganismDetails(final String organismDetails) {
    this.organismDetails = organismDetails;
    isSetOrganismDetails = true;
  }

  /** {@inheritDoc} */
  public void setMatrixName(final String matrixName) {
    this.matrixName = matrixName;
    isSetMatrixName = true;
  }

  /** {@inheritDoc} */
  public void setMatrixDetails(final String matrixDetails) {
    this.matrixDetails = matrixDetails;
    isSetMatrixDetails = true;
  }

  /** {@inheritDoc} */
  public void setCreator(final String creator) {
    this.creator = creator;
    isSetCreator = true;
  }

  /** {@inheritDoc} */
  public void setFamilyName(final String familyName) {
    this.familyName = familyName;
    isSetFamilyName = true;
  }

  /** {@inheritDoc} */
  public void setContact(final String contact) {
    this.contact = contact;
    isSetContact = true;
  }

  /** {@inheritDoc} */
  public void setReferenceDescription(final String referenceDescription) {
    this.referenceDescription = referenceDescription;
    isSetReferenceDescription = true;
  }

  /** {@inheritDoc} */
  public void setReferenceDescriptionLink(URL referenceDescriptionLink) {
    this.referenceDescriptionLink = referenceDescriptionLink;
    isSetReferenceDescriptionLink = true;
  }

  /** {@inheritDoc} */
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    isSetCreatedDate = true;
  }

  /** {@inheritDoc} */
  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
    isSetModifiedDate = true;
  }

  /** {@inheritDoc} */
  public void setRights(final String rights) {
    this.rights = rights;
    isSetRights = true;
  }

  /** {@inheritDoc} */
  public void setNotes(final String notes) {
    this.notes = notes;
    isSetNotes = true;
  }

  /** {@inheritDoc} */
  public void setCurationStatus(final String curationStatus) {
    this.curationStatus = curationStatus;
    isSetCurationStatus = true;
  }

  /** {@inheritDoc} */
  public void setModelType(final ModelType type) {
    this.modelType = type;
    isSetModelType = true;
  }

  /** {@inheritDoc} */
  public void setModelSubject(final ModelClass subject) {
    this.modelSubject = subject;
    isSetModelSubject = true;
  }

  /** {@inheritDoc} */
  public void setFoodProcess(final String foodProcess) {
    this.foodProcess = foodProcess;
    isSetFoodProcess = true;
  }

  /** {@inheritDoc} */
  public void setDependentVariable(final String dependentVariable) {
    this.dependentVariable = dependentVariable;
    isSetDependentVariable = true;
  }

  /** {@inheritDoc} */
  public void setDependentVariableUnit(final String dependentVariableUnit) {
    this.dependentVariableUnit = dependentVariableUnit;
    isSetDependentVariableUnit = true;
  }

  /** {@inheritDoc} */
  public void setDependentVariableMin(double dependentVariableMin) {
    this.dependentVariableMin = dependentVariableMin;
    isSetDependentVariableMin = true;
  }

  /** {@inheritDoc} */
  public void setDependentVariableMax(double dependentVariableMax) {
    this.dependentVariableMax = dependentVariableMax;
    isSetDependentVariableMax = true;
  }

  /** {@inheritDoc} */
  public void setIndependentVariables(final String[] independentVariables) {
    this.independentVariables = independentVariables;
    isSetIndependentVariables = true;
  }

  /** {@inheritDoc} */
  public void setIndependentVariablesUnits(final String[] independentVariableUnits) {
    this.independentVariableUnits = independentVariableUnits;
    isSetIndependentVariablesUnits = true;
  }

  /** {@inheritDoc} */
  public void setIndependentVariablesMins(double[] independentVariableMins) {
    this.independentVariableMins = independentVariableMins;
    isSetIndependentVariablesMins = true;
  }

  /** {@inheritDoc} */
  public void setIndependentVariablesMaxs(double[] independentVariableMaxs) {
    this.independentVariableMaxs = independentVariableMaxs;
    isSetIndependentVariablesMaxs = true;
  }

  /** {@inheritDoc} */
  public void setHasData(boolean hasData) {
    this.hasData = hasData;
    isSetHasData = true;
  }

  /** {@inheritDoc} */
  public void unsetModelName() {
    modelName = null;
    isSetModelName = false;
  }

  /** {@inheritDoc} */
  public void unsetModelId() {
    modelId = null;
    isSetModelId = false;
  }

  /** {@inheritDoc} */
  public void unsetModelLink() {
    modelLink = null;
    isSetModelLink = false;
  }

  /** {@inheritDoc} */
  public void unsetOrganismName() {
    organismName = null;
    isSetOrganismName = false;
  }

  /** {@inheritDoc} */
  public void unsetOrganismDetail() {
    organismDetails = null;
    isSetOrganismDetails = false;
  }

  /** {@inheritDoc} */
  public void unsetMatrixName() {
    matrixName = null;
    isSetMatrixName = false;
  }

  /** {@inheritDoc} */
  public void unsetMatrixDetails() {
    matrixDetails = null;
    isSetMatrixDetails = false;
  }

  /** {@inheritDoc} */
  public void unsetCreator() {
    creator = null;
    isSetCreator = false;
  }

  /** {@inheritDoc} */
  public void unsetFamilyName() {
    familyName = null;
    isSetFamilyName = false;
  }

  /** {@inheritDoc} */
  public void unsetContact() {
    contact = null;
    isSetContact = false;
  }

  /** {@inheritDoc} */
  public void unsetReferenceDescription() {
    referenceDescription = null;
    isSetReferenceDescription = false;
  }

  /** {@inheritDoc} */
  public void unsetReferenceDescriptionLink() {
    referenceDescriptionLink = null;
    isSetReferenceDescriptionLink = false;
  }

  /** {@inheritDoc} */
  public void unsetCreatedDate() {
    createdDate = null;
    isSetCreatedDate = false;
  }

  /** {@inheritDoc} */
  public void unsetModifiedDate() {
    modifiedDate = null;
    isSetModifiedDate = false;
  }

  /** {@inheritDoc} */
  public void unsetRights() {
    rights = null;
    isSetRights = false;
  }

  /** {@inheritDoc} */
  public void unsetNotes() {
    notes = null;
    isSetNotes = false;
  }

  /** {@inheritDoc} */
  public void unsetCurationStatus() {
    curationStatus = null;
    isSetCurationStatus = false;
  }

  /** {@inheritDoc} */
  public void unsetModelType() {
    modelType = null;
    isSetModelType = false;
  }

  /** {@inheritDoc} */
  public void unsetModelSubject() {
    modelSubject = null;
    isSetModelSubject = false;
  }

  /** {@inheritDoc} */
  public void unsetFoodProcess() {
    foodProcess = null;
    isSetFoodProcess = false;
  }

  /** {@inheritDoc} */
  public void unsetDependentVariable() {
    dependentVariable = null;
    isSetDependentVariable = false;
  }

  /** {@inheritDoc} */
  public void unsetDependentVariableUnit() {
    dependentVariableUnit = null;
    isSetDependentVariableUnit = false;
  }

  /** {@inheritDoc} */
  public void unsetDependentVariableMin() {
    dependentVariableMin = Double.NaN;
    isSetDependentVariableMin = false;
  }

  /** {@inheritDoc} */
  public void unsetDependentVariableMax() {
    dependentVariableMax = Double.NaN;
    isSetDependentVariableMax = false;
  }

  /** {@inheritDoc} */
  public void unsetIndependentVariables() {
    independentVariables = null;
    isSetIndependentVariables = false;
  }

  /** {@inheritDoc} */
  public void unsetIndependentVariableUnits() {
    independentVariableUnits = null;
    isSetIndependentVariablesUnits = false;
  }

  /** {@inheritDoc} */
  public void unsetIndependentVariableMins() {
    independentVariableMins = null;
    isSetIndependentVariablesMins = false;
  }

  /** {@inheritDoc} */
  public void unsetIndependentVariableMaxs() {
    independentVariableMaxs = null;
    isSetIndependentVariablesMaxs = false;
  }

  /** {@inheritDoc} */
  public void unsetHasData() {
    isSetHasData = false;
  }

  /** {@inheritDoc} */
  public boolean isSetModelName() {
    return isSetModelName;
  }

  /** {@inheritDoc} */
  public boolean isSetModelId() {
    return isSetModelId;
  }

  /** {@inheritDoc} */
  public boolean isSetModelLink() {
    return isSetModelLink;
  }

  /** {@inheritDoc} */
  public boolean isSetOrganismName() {
    return isSetOrganismName;
  }

  /** {@inheritDoc} */
  public boolean isSetOrganismDetails() {
    return isSetOrganismDetails;
  }

  /** {@inheritDoc} */
  public boolean isSetMatrixName() {
    return isSetMatrixName;
  }

  /** {@inheritDoc} */
  public boolean isSetMatrixDetails() {
    return isSetMatrixDetails;
  }

  /** {@inheritDoc} */
  public boolean isSetCreator() {
    return isSetCreator;
  }

  /** {@inheritDoc} */
  public boolean isSetFamilyName() {
    return isSetFamilyName;
  }

  /** {@inheritDoc} */
  public boolean isSetContact() {
    return isSetContact;
  }

  /** {@inheritDoc} */
  public boolean isSetReferenceDescription() {
    return isSetReferenceDescription;
  }

  /** {@inheritDoc} */
  public boolean isSetReferenceDescriptionLink() {
    return isSetReferenceDescriptionLink;
  }

  /** {@inheritDoc} */
  public boolean isSetCreatedDate() {
    return isSetCreatedDate;
  }

  /** {@inheritDoc} */
  public boolean isSetModifiedDate() {
    return isSetModifiedDate;
  }

  /** {@inheritDoc} */
  public boolean isSetRights() {
    return isSetRights;
  }

  /** {@inheritDoc} */
  public boolean isSetNotes() {
    return isSetNotes;
  }

  /** {@inheritDoc} */
  public boolean isSetCurationStatus() {
    return isSetCurationStatus;
  }

  /** {@inheritDoc} */
  public boolean isSetModelType() {
    return isSetModelType;
  }

  /** {@inheritDoc} */
  public boolean isSetModelSubject() {
    return isSetModelSubject;
  }

  /** {@inheritDoc} */
  public boolean isSetFoodProcess() {
    return isSetFoodProcess;
  }

  /** {@inheritDoc} */
  public boolean isSetDependentVariable() {
    return isSetDependentVariable;
  }

  /** {@inheritDoc} */
  public boolean isSetDependentVariableUnit() {
    return isSetDependentVariableUnit;
  }

  /** {@inheritDoc} */
  public boolean isSetDependentVariableMin() {
    return isSetDependentVariableMin;
  }

  /** {@inheritDoc} */
  public boolean isSetDependentVariableMax() {
    return isSetDependentVariableMax;
  }

  /** {@inheritDoc} */
  public boolean isSetIndependentVariables() {
    return isSetIndependentVariables;
  }

  /** {@inheritDoc} */
  public boolean isSetIndependentVariablesUnits() {
    return isSetIndependentVariablesUnits;
  }

  /** {@inheritDoc} */
  public boolean isSetIndependentVariablesMins() {
    return isSetIndependentVariablesMins;
  }

  /** {@inheritDoc} */
  public boolean isSetIndependentVariablesMaxs() {
    return isSetIndependentVariablesMaxs;
  }

  /** {@inheritDoc} */
  public boolean isSetHasData() {
    return isSetHasData;
  }

  public FSMRTemplateImpl() {}

  public FSMRTemplateImpl(final FSMRTemplate template) {
    try {
      if (template.isSetModelName()) {
        setModelName(new String(template.getModelName()));
      }
      if (template.isSetModelId()) {
        setModelId(new String(template.getModelId()));
      }
      if (template.isSetModelLink()) {
        setModelLink(new URL(template.getModelLink().toString()));
      }
      if (template.isSetOrganismName()) {
        setOrganismName(new String(template.getOrganismName()));
      }
      if (template.isSetOrganismDetails()) {
        setOrganismDetails(new String(template.getOrganismDetails()));
      }
      if (template.isSetMatrixName()) {
        setMatrixName(new String(template.getMatrixName()));
      }
      if (template.isSetMatrixDetails()) {
        setMatrixDetails(new String(template.getMatrixDetails()));
      }
      if (template.isSetCreator()) {
        setCreator(new String(template.getCreator()));
      }
      if (template.isSetFamilyName()) {
        setFamilyName(new String(template.getFamilyName()));
      }
      if (template.isSetContact()) {
        setContact(new String(template.getContact()));
      }
      if (template.isSetReferenceDescription()) {
        setReferenceDescription(new String(template.getReferenceDescription()));
      }
      if (template.isSetReferenceDescriptionLink()) {
        setReferenceDescriptionLink(new URL(template.getReferenceDescriptionLink().toString()));
      }
      if (template.isSetCreatedDate()) {
        setCreatedDate(new Date(template.getCreatedDate().getTime()));
      }
      if (template.isSetModifiedDate()) {
        setModifiedDate(new Date(template.getModifiedDate().getTime()));
      }
      if (template.isSetRights()) {
        setRights(new String(template.getRights()));
      }
      if (template.isSetNotes()) {
        setNotes(new String(template.getNotes()));
      }
      if (template.isSetCurationStatus()) {
        setCurationStatus(new String(template.getCurationStatus()));
      }
      if (template.isSetModelType()) {
        setModelType(template.getModelType());
      }
      if (template.isSetModelSubject()) {
        setModelSubject(template.getModelSubject());
      } else {
        setModelSubject(ModelClass.UNKNOWN);
      }
      if (template.isSetFoodProcess()) {
        setFoodProcess(new String(template.getFoodProcess()));
      }
      if (template.isSetDependentVariable()) {
        setDependentVariable(new String(template.getDependentVariable()));
      }
      if (template.isSetDependentVariableUnit()) {
        setDependentVariableUnit(new String(template.getDependentVariableUnit()));
      }
      if (template.isSetDependentVariableMin()) {
        setDependentVariableMin(template.getDependentVariableMin());
      }
      if (template.isSetDependentVariableMax()) {
        setDependentVariableMax(template.getDependentVariableMax());
      }
      if (template.isSetIndependentVariables()) {
        setIndependentVariables(template.getIndependentVariables().clone());
      }
      if (template.isSetIndependentVariablesUnits()) {
        setIndependentVariablesUnits(template.getIndependentVariablesUnits().clone());
      }
      if (template.isSetIndependentVariablesMins()) {
        setIndependentVariablesMins(template.getIndependentVariablesMins());
      }
      if (template.isSetIndependentVariablesMaxs()) {
        setIndependentVariablesMaxs(template.getIndependentVariablesMaxs());
      }
      if (template.isSetHasData()) {
        setHasData(template.getHasData());
      }
    } catch (MalformedURLException e) {
      // passed template has valid settings so these exceptions are never
      // thrown
      throw new RuntimeException(e);
    }
  }
}
