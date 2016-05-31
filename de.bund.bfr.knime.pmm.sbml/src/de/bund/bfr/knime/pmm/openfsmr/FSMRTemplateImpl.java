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

import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;

/**
 * Implementation of the Open Food Safety Model Repository template ( {@link FSMRTemplate}) based on
 * instance variables.
 * 
 * @author Miguel Alba
 */
public class FSMRTemplateImpl implements FSMRTemplate {

  private static final long serialVersionUID = -3414978919227860002L;

  private String modelName = null;

  private String modelId = null;

  private URL modelLink = null;

  private String organismName = null;

  private String organismDetail = null;

  private String matrixName = null;

  private String matrixDetails = null;

  private String creator = null;

  private String familyName = null;

  private String contact = null;

  private String referenceDescription = null;

  private URL referenceDescriptionLink = null;

  private Date createdDate = null;

  private Date modifiedDate = null;

  private String rights = null;

  private String notes = null;

  private String curationStatus = null;

  private ModelType modelType = null;

  private ModelClass modelSubject = null;

  private String foodProcess = null;

  private String dependentVariable = null;

  private String dependentVariableUnit = null;

  private Double dependentVariableMin = null;

  private Double dependentVariableMax = null;

  private String[] independentVariables = null;

  private String[] independentVariableUnits = null;

  private Double[] independentVariableMins = null;

  private Double[] independentVariableMaxs = null;

  private Boolean hasData;

  /** {@inheritDoc} */
  public String getModelName() {
    return modelName;
  }

  /** {@inheritDoc} */
  public String getModelId() {
    return modelId;
  }

  /** {@inheritDoc} */
  public URL getModelLink() {
    return modelLink;
  }

  /** {@inheritDoc} */
  public String getOrganismName() {
    return organismName;
  }

  /** {@inheritDoc} */
  public String getOrganismDetails() {
    return organismDetail;
  }

  /** {@inheritDoc} */
  public String getMatrixName() {
    return matrixName;
  }

  /** {@inheritDoc} */
  public String getMatrixDetails() {
    return matrixDetails;
  }

  /** {@inheritDoc} */
  public String getCreator() {
    return creator;
  }

  /** {@inheritDoc} */
  public String getFamilyName() {
    return familyName;
  }

  /** {@inheritDoc} */
  public String getContact() {
    return contact;
  }

  /** {@inheritDoc} */
  public String getReferenceDescription() {
    return referenceDescription;
  }

  /** {@inheritDoc} */
  public URL getReferenceDescriptionLink() {
    return referenceDescriptionLink;
  }

  /** {@inheritDoc} */
  public Date getCreatedDate() {
    return createdDate;
  }

  /** {@inheritDoc} */
  public Date getModifiedDate() {
    return modifiedDate;
  }

  /** {@inheritDoc} */
  public String getRights() {
    return rights;
  }

  /** {@inheritDoc} */
  public String getNotes() {
    return notes;
  }

  /** {@inheritDoc} */
  public String getCurationStatus() {
    return curationStatus;
  }

  /** {@inheritDoc} */
  public ModelType getModelType() {
    return modelType;
  }

  /** {@inheritDoc} */
  public ModelClass getModelSubject() {
    return modelSubject;
  }

  /** {@inheritDoc} */
  public String getFoodProcess() {
    return foodProcess;
  }

  /** {@inheritDoc} */
  public String getDependentVariable() {
    return dependentVariable;
  }

  /** {@inheritDoc} */
  public String getDependentVariableUnit() {
    return dependentVariableUnit;
  }

  /** {@inheritDoc} */
  public Double getDependentVariableMin() {
    return dependentVariableMin;
  }

  /** {@inheritDoc} */
  public Double getDependentVariableMax() {
    return dependentVariableMax;
  }

  /** {@inheritDoc} */
  public String[] getIndependentVariables() {
    return independentVariables;
  }

  /** {@inheritDoc} */
  public String[] getIndependentVariablesUnits() {
    return independentVariableUnits;
  }

  /** {@inheritDoc} */
  public Double[] getIndependentVariablesMins() {
    return independentVariableMins;
  }

  /** {@inheritDoc} */
  public Double[] getIndependentVariablesMaxs() {
    return independentVariableMaxs;
  }

  /** {@inheritDoc} */
  public Boolean getHasData() {
    return hasData;
  }

  /** {@inheritDoc} */
  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  /** {@inheritDoc} */
  public void setModelId(String modelId) {
    this.modelId = modelId;
  }

  /** {@inheritDoc} */
  public void setModelLink(URL modelLink) {
    this.modelLink = modelLink;
  }

  /** {@inheritDoc} */
  public void setOrganismName(String organismName) {
    this.organismName = organismName;
  }

  /** {@inheritDoc} */
  public void setOrganismDetails(String organismDetails) {
    this.organismDetail = organismDetails;
  }

  /** {@inheritDoc} */
  public void setMatrixName(String matrixName) {
    this.matrixName = matrixName;
  }

  /** {@inheritDoc} */
  public void setMatrixDetails(String matrixDetails) {
    this.matrixDetails = matrixDetails;
  }

  /** {@inheritDoc} */
  public void setCreator(String creator) {
    this.creator = creator;
  }

  /** {@inheritDoc} */
  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  /** {@inheritDoc} */
  public void setContact(String contact) {
    this.contact = contact;
  }

  /** {@inheritDoc} */
  public void setReferenceDescription(String referenceDescription) {
    this.referenceDescription = referenceDescription;
  }

  /** {@inheritDoc} */
  public void setReferenceDescriptionLink(URL referenceDescriptionLink) {
    this.referenceDescriptionLink = referenceDescriptionLink;
  }

  /** {@inheritDoc} */
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  /** {@inheritDoc} */
  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  /** {@inheritDoc} */
  public void setRights(String rights) {
    this.rights = rights;
  }

  /** {@inheritDoc} */
  public void setNotes(String notes) {
    this.notes = notes;
  }

  /** {@inheritDoc} */
  public void setCurationStatus(String curationStatus) {
    this.curationStatus = curationStatus;
  }

  /** {@inheritDoc} */
  public void setModelType(ModelType type) {
    this.modelType = type;
  }

  /** {@inheritDoc} */
  public void setModelSubject(ModelClass subject) {
    this.modelSubject = subject;
  }

  /** {@inheritDoc} */
  public void setFoodProcess(String foodProcess) {
    this.foodProcess = foodProcess;
  }

  /** {@inheritDoc} */
  public void setDependentVariable(String dependentVariable) {
    this.dependentVariable = dependentVariable;
  }

  /** {@inheritDoc} */
  public void setDependentVariableUnit(String dependentVariableUnit) {
    this.dependentVariableUnit = dependentVariableUnit;
  }

  /** {@inheritDoc} */
  public void setDependentVariableMin(double dependentVariableMin) {
    this.dependentVariableMin = dependentVariableMin;
  }

  /** {@inheritDoc} */
  public void setDependentVariableMax(double dependentVariableMax) {
    this.dependentVariableMax = dependentVariableMax;
  }

  /** {@inheritDoc} */
  public void setIndependentVariables(String[] independentVariables) {
    this.independentVariables = independentVariables;
  }

  /** {@inheritDoc} */
  public void setIndependentVariablesUnits(String[] independentVariableUnits) {
    this.independentVariableUnits = independentVariableUnits;
  }

  /** {@inheritDoc} */
  public void setIndependentVariablesMins(Double[] independentVariableMins) {
    this.independentVariableMins = independentVariableMins;
  }

  /** {@inheritDoc} */
  public void setIndependentVariablesMaxs(Double[] independentVariableMaxs) {
    this.independentVariableMaxs = independentVariableMaxs;
  }

  /** {@inheritDoc} */
  public void setHasData(boolean hasData) {
    this.hasData = hasData;
  }

  /** {@inheritDoc} */
  public void unsetModelName() {
    modelName = null;
  }

  /** {@inheritDoc} */
  public void unsetModelId() {
    modelId = null;
  }

  /** {@inheritDoc} */
  public void unsetModelLink() {
    modelLink = null;
  }

  /** {@inheritDoc} */
  public void unsetOrganismName() {
    organismName = null;
  }

  /** {@inheritDoc} */
  public void unsetOrganismDetail() {
    organismDetail = null;
  }

  /** {@inheritDoc} */
  public void unsetMatrixName() {
    matrixName = null;
  }

  /** {@inheritDoc} */
  public void unsetMatrixDetails() {
    matrixDetails = null;
  }

  /** {@inheritDoc} */
  public void unsetCreator() {
    creator = null;
  }

  /** {@inheritDoc} */
  public void unsetFamilyName() {
    familyName = null;
  }

  /** {@inheritDoc} */
  public void unsetContact() {
    contact = null;
  }

  /** {@inheritDoc} */
  public void unsetReferenceDescription() {
    referenceDescription = null;
  }

  /** {@inheritDoc} */
  public void unsetReferenceDescriptionLink() {
    referenceDescriptionLink = null;
  }

  /** {@inheritDoc} */
  public void unsetCreatedDate() {
    createdDate = null;
  }

  /** {@inheritDoc} */
  public void unsetModifiedDate() {
    modifiedDate = null;
  }

  /** {@inheritDoc} */
  public void unsetRights() {
    rights = null;
  }

  /** {@inheritDoc} */
  public void unsetNotes() {
    notes = null;
  }

  /** {@inheritDoc} */
  public void unsetCurationStatus() {
    curationStatus = null;
  }

  /** {@inheritDoc} */
  public void unsetModelType() {
    modelType = null;
  }

  /** {@inheritDoc} */
  public void unsetModelSubject() {
    modelSubject = null;
  }

  /** {@inheritDoc} */
  public void unsetFoodProcess() {
    foodProcess = null;
  }

  /** {@inheritDoc} */
  public void unsetDependentVariable() {
    dependentVariable = null;
  }

  /** {@inheritDoc} */
  public void unsetDependentVariableMin() {
    dependentVariableMin = null;
  }

  /** {@inheritDoc} */
  public void unsetDependentVariableMax() {
    dependentVariableMax = null;
  }

  /** {@inheritDoc} */
  public void unsetIndependentVariables() {
    independentVariables = null;
  }

  /** {@inheritDoc} */
  public void unsetIndependentVariableUnits() {
    independentVariableUnits = null;
  }

  /** {@inheritDoc} */
  public void unsetIndependentVariableMins() {
    independentVariableMins = null;
  }

  /** {@inheritDoc} */
  public void unsetIndependentVariableMaxs() {
    independentVariableMaxs = null;
  }

  /** {@inheritDoc} */
  public void unsetHasData() {
    hasData = null;
  }

  /** {@inheritDoc} */
  public boolean isSetModelName() {
    return modelName != null;
  }

  /** {@inheritDoc} */
  public boolean isSetModelId() {
    return modelId != null;
  }

  /** {@inheritDoc} */
  public boolean isSetModelLink() {
    return modelLink != null;
  }

  /** {@inheritDoc} */
  public boolean isSetOrganismName() {
    return organismName != null;
  }

  /** {@inheritDoc} */
  public boolean isSetOrganismDetails() {
    return organismDetail != null;
  }

  /** {@inheritDoc} */
  public boolean isSetMatrixName() {
    return matrixName != null;
  }

  /** {@inheritDoc} */
  public boolean isSetMatrixDetails() {
    return matrixDetails != null;
  }

  /** {@inheritDoc} */
  public boolean isSetCreator() {
    return creator != null;
  }

  /** {@inheritDoc} */
  public boolean isSetFamilyName() {
    return familyName != null;
  }

  /** {@inheritDoc} */
  public boolean isSetContact() {
    return contact != null;
  }

  /** {@inheritDoc} */
  public boolean isSetReferenceDescription() {
    return referenceDescription != null;
  }

  /** {@inheritDoc} */
  public boolean isSetReferenceDescriptionLink() {
    return referenceDescriptionLink != null;
  }

  /** {@inheritDoc} */
  public boolean isSetCreatedDate() {
    return createdDate != null;
  }

  /** {@inheritDoc} */
  public boolean isSetModifiedDate() {
    return modifiedDate != null;
  }

  /** {@inheritDoc} */
  public boolean isSetRights() {
    return rights != null;
  }

  /** {@inheritDoc} */
  public boolean isSetNotes() {
    return notes != null;
  }

  /** {@inheritDoc} */
  public boolean isSetCurationStatus() {
    return curationStatus != null;
  }

  /** {@inheritDoc} */
  public boolean isSetModelType() {
    return modelType != null;
  }

  /** {@inheritDoc} */
  public boolean isSetModelSubject() {
    return modelSubject != null;
  }

  /** {@inheritDoc} */
  public boolean isSetFoodProcess() {
    return foodProcess != null;
  }

  /** {@inheritDoc} */
  public boolean isSetDependentVariable() {
    return dependentVariable != null;
  }

  /** {@inheritDoc} */
  public boolean isSetDependentVariableUnit() {
    return dependentVariableUnit != null;
  }

  /** {@inheritDoc} */
  public boolean isSetDependentVariableMin() {
    return dependentVariableMin != null;
  }

  /** {@inheritDoc} */
  public boolean isSetDependentVariableMax() {
    return dependentVariableMax != null;
  }

  /** {@inheritDoc} */
  public boolean isSetIndependentVariables() {
    return independentVariables != null;
  }

  /** {@inheritDoc} */
  public boolean isSetIndependentVariablesUnits() {
    return independentVariableUnits != null;
  }

  /** {@inheritDoc} */
  public boolean isSetIndependentVariablesMins() {
    return independentVariableMins != null;
  }

  /** {@inheritDoc} */
  public boolean isSetIndependentVariablesMaxs() {
    return independentVariableMaxs != null;
  }

  /** {@inheritDoc} */
  public boolean isSetHasData() {
    return hasData != null;
  }

  //
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
        setCurationStatus(template.getCurationStatus());
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
        setDependentVariableMin(new Double(template.getDependentVariableMin()));
      }
      if (template.isSetDependentVariableMax()) {
        setDependentVariableMax(new Double(template.getDependentVariableMax()));
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
