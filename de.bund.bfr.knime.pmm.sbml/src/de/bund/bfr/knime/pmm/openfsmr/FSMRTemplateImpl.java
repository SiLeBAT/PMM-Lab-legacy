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
package de.bund.bfr.knime.pmm.openfsmr;

import java.net.URL;
import java.util.Date;

import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;

/**
 * Implementation of the Open Food Safety Model Repository template (
 * {@link FSMRTemplate}) based on instance variables.
 * 
 * @author Miguel Alba
 */
public class FSMRTemplateImpl implements FSMRTemplate {

	private String modelName = null;

	private String modelId = null;

	private URL modelLink = null;

	private String organismName = null;

	private String organismDetail = null;

	private String matrixName = null;

	private String matrixDetails = null;

	private String creator = null;

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

	private String independentVariable = null;

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
	public String getIndependentVariable() {
		return independentVariable;
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
	public void setIndependentVariable(String independentVariable) {
		this.independentVariable = independentVariable;
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
	public void unsetIndependentVariable() {
		independentVariable = null;
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
	public boolean isSetIndependentVariable() {
		return independentVariable != null;
	}
}
