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

	private String software = null;

	private URL softwareLink = null;

	private String softwareNotes = null;

	private String softwareAccesibility = null;

	private Boolean softwareStochasticModeling = null;

	private String softwarePredictionConditions = null;

	private String initLevelUnit = null;

	private Double initLevelMin = null;

	private Double initLevelMax = null;

	private String timeUnit = null;

	private Double timeMin = null;

	private Double timeMax = null;

	private String temperatureUnit = null;

	private Double temperatureMin = null;

	private Double temperatureMax = null;

	private String phUnit = null;

	private Double phMin = null;

	private Double phMax = null;

	private String awUnit = null;

	private Double awMin = null;

	private Double awMax = null;

	private String naclUnit = null;

	private Double naclMin = null;

	private Double naclMax = null;

	private String altaUnit = null;

	private Double altaMin = null;

	private Double altaMax = null;

	private String co2Unit = null;

	private Double co2Min = null;

	private Double co2Max = null;

	private String clo2Unit = null;

	private Double clo2Min = null;

	private Double clo2Max = null;

	private String edtaUnit = null;

	private Double edtaMin = null;

	private Double edtaMax = null;

	private String hclUnit = null;

	private Double hclMin = null;

	private Double hclMax = null;

	private String n2Unit = null;

	private Double n2Min = null;

	private Double n2Max = null;

	private String o2Unit = null;

	private Double o2Min = null;

	private Double o2Max = null;

	private String aceticAcidUnit = null;

	private Double aceticAcidMin = null;

	private Double aceticAcidMax = null;

	private String additives;

	private Boolean anaerobic;

	private String antimicrobialDippingTimeUnit;

	private Double antimicrobialDippingTimeMin;

	private Double antimicrobialDippingTimeMax;

	private String applePolyphenolUnit;

	private Double applePolyphenolMin;

	private Double applePolyphenolMax;

	private String ascorbiccAcidUnit;

	private Double ascorbiccAcidMin;

	private Double ascorbiccAcidMax;

	private Boolean atrInduced;

	private String attachmentTimeUnit;

	private Double attachmentTimeMin;

	private Double attachmentTimeMax;

	private String beanOilUnit;

	private Double beanOilMin;

	private Double beanOilMax;

	private String benzoicAcidUnit;

	private Double benzoicAcidMin;

	private Double benzoicAcidMax;

	private String betaineUnit;

	private Double betaineMin;

	private Double betaineMax;

	private String carvacrolUnit;

	private Double carvacrolMin;

	private Double carvacrolMax;

	private String chitosanUnit;

	private Double chitosanMin;

	private Double chitosanMax;

	private String cinnamaldehydeUnit;

	private Double cinnamaldehydeMin;

	private Double cinnamaldehydeMax;

	private String citricAcidUnit;

	private Double citricAcidMin;

	private Double citricAcidMax;

	private Boolean cured;

	private Boolean cut;

	private String desiredReductionUnit;

	private Double desiredReductionMin;

	private Double desiredReductionMax;

	private String diaceticAcidUnit;

	private Double diaceticAcidMin;

	private Double diaceticAcidMax;

	private String disaccharideUnit;

	private Double disaccharideMin;

	private Double disaccharideMax;

	private Boolean dried;

	private String ethanolUnit;

	private Double ethanolMin;

	private Double ethanolMax;

	private Boolean expInoculated;

	private String fatUnit;

	private Double fatMin;

	private Double fatMax;

	private Boolean frozen;

	private String fructoseUnit;

	private Double fructoseMin;

	private Double fructoseMax;

	private String gelMicrostructureUnit;

	private Double gelMicrostructureMin;

	private Double gelMicrostructureMax;

	private String glucoseUnit;

	private Double glucoseMin;

	private Double glucoseMax;

	private String glycerolUnit;

	private Double glycerolMin;

	private Double glycerolMax;

	private String greenTeaGroundPowderUnit;

	private Double greenTeaGroundPowderMin;

	private Double greenTeaGroundPowderMax;

	private String greenTeaLeafUnit;

	private Double greenTeaLeafMin;

	private Double greenTeaLeafMax;

	private String greenTeaWaterExtractUnit;

	private Double greenTeaWaterExtractMin;

	private Double greenTeaWaterExtractMax;

	private Boolean heated;

	private String hexosesUnit;

	private Double hexosesMin;

	private Double hexosesMax;

	private Boolean indigenous;

	private String initLevelHistamineUnit;

	private Double initLevelHistamineMin;

	private Double initLevelHistamineMax;

	private String initLevelHistidineUnit;

	private Double initLevelHistidineMin;

	private Double initLevelHistidineMax;

	private String injuredUnit;

	private Double injuredMin;

	private Double injuredMax;

	private Boolean irradiated;

	private String irradiationUnit;

	private Double irradiationMin;

	private Double irradiationMax;

	private String lacticAcidUnit;

	private Double lacticAcidMin;

	private Double lacticAcidMax;

	private Boolean lacticBacteriaFermented;

	private String lauricidinUnit;

	private Double lauricidinMin;

	private Double lauricidinMax;

	private String malicAcidUnit;

	private Double malicAcidMin;

	private Double malicAcidMax;

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
	public String getSoftware() {
		return software;
	}

	/** {@inheritDoc} */
	public URL getSoftwareLink() {
		return softwareLink;
	}

	/** {@inheritDoc} */
	public String getSoftwareNotes() {
		return softwareNotes;
	}

	/** {@inheritDoc} */
	public String getSoftwareAccesibility() {
		return softwareAccesibility;
	}

	/** {@inheritDoc} */
	public Boolean getSoftwareStochasticModeling() {
		return softwareStochasticModeling;
	}

	/** {@inheritDoc} */
	public String getSoftwarePredictionConditions() {
		return softwarePredictionConditions;
	}

	/** {@inheritDoc} */
	public String getInitLevelUnit() {
		return initLevelUnit;
	}

	/** {@inheritDoc} */
	public Double getInitLevelMin() {
		return initLevelMin;
	}

	/** {@inheritDoc} */
	public Double getInitLevelMax() {
		return initLevelMax;
	}

	/** {@inheritDoc} */
	public String getTimeUnit() {
		return timeUnit;
	}

	/** {@inheritDoc} */
	public Double getTimeMin() {
		return timeMin;
	}

	/** {@inheritDoc} */
	public Double getTimeMax() {
		return timeMax;
	}

	/** {@inheritDoc} */
	public String getTemperatureUnit() {
		return temperatureUnit;
	}

	/** {@inheritDoc} */
	public Double getTemperatureMin() {
		return temperatureMin;
	}

	/** {@inheritDoc} */
	public Double getTemperatureMax() {
		return temperatureMax;
	}

	/** {@inheritDoc} */
	public String getPhUnit() {
		return phUnit;
	}

	/** {@inheritDoc} */
	public Double getPhMin() {
		return phMin;
	}

	/** {@inheritDoc} */
	public Double getPhMax() {
		return phMax;
	}

	/** {@inheritDoc} */
	public String getAwUnit() {
		return awUnit;
	}

	/** {@inheritDoc} */
	public Double getAwMin() {
		return awMin;
	}

	/** {@inheritDoc} */
	public Double getAwMax() {
		return awMax;
	}

	/** {@inheritDoc} */
	public String getNaClUnit() {
		return naclUnit;
	}

	/** {@inheritDoc} */
	public Double getNaClMin() {
		return naclMin;
	}

	/** {@inheritDoc} */
	public Double getNaClMax() {
		return naclMax;
	}

	/** {@inheritDoc} */
	public String getAltaUnit() {
		return altaUnit;
	}

	/** {@inheritDoc} */
	public Double getAltaMin() {
		return altaMin;
	}

	/** {@inheritDoc} */
	public Double getAltaMax() {
		return altaMax;
	}

	/** {@inheritDoc} */
	public String getCO2Unit() {
		return co2Unit;
	}

	/** {@inheritDoc} */
	public Double getCO2Min() {
		return co2Min;
	}

	/** {@inheritDoc} */
	public Double getCO2Max() {
		return co2Max;
	}

	/** {@inheritDoc} */
	public String getClO2Unit() {
		return clo2Unit;
	}

	/** {@inheritDoc} */
	public Double getClO2Min() {
		return clo2Min;
	}

	/** {@inheritDoc} */
	public Double getClO2Max() {
		return clo2Max;
	}

	/** {@inheritDoc} */
	public String getEdtaUnit() {
		return edtaUnit;
	}

	/** {@inheritDoc} */
	public Double getEdtaMin() {
		return edtaMin;
	}

	/** {@inheritDoc} */
	public Double getEdtaMax() {
		return edtaMax;
	}

	/** {@inheritDoc} */
	public String getHClUnit() {
		return hclUnit;
	}

	/** {@inheritDoc} */
	public Double getHClMin() {
		return hclMin;
	}

	/** {@inheritDoc} */
	public Double getHClMax() {
		return hclMax;
	}

	/** {@inheritDoc} */
	public String getN2Unit() {
		return n2Unit;
	}

	/** {@inheritDoc} */
	public Double getN2Min() {
		return n2Min;
	}

	/** {@inheritDoc} */
	public Double getN2Max() {
		return n2Max;
	}

	/** {@inheritDoc} */
	public String getO2Unit() {
		return o2Unit;
	}

	/** {@inheritDoc} */
	public Double getO2Min() {
		return o2Min;
	}

	/** {@inheritDoc} */
	public Double getO2Max() {
		return o2Max;
	}

	/** {@inheritDoc} */
	public String getAceticAcidUnit() {
		return aceticAcidUnit;
	}

	/** {@inheritDoc} */
	public Double getAceticAcidMin() {
		return aceticAcidMin;
	}

	/** {@inheritDoc} */
	public Double getAceticAcidMax() {
		return aceticAcidMax;
	}

	/** {@inheritDoc} */
	public String getAdditives() {
		return additives;
	}

	/** {@inheritDoc} */
	public Boolean getAnaerobic() {
		return anaerobic;
	}

	/** {@inheritDoc} */
	public String getAntimicrobialDippingTimeUnit() {
		return antimicrobialDippingTimeUnit;
	}

	/** {@inheritDoc} */
	public Double getAntimicrobialDippingTimeMin() {
		return antimicrobialDippingTimeMin;
	}

	/** {@inheritDoc} */
	public Double getAntimicrobialDippingTimeMax() {
		return antimicrobialDippingTimeMax;
	}

	/** {@inheritDoc} */
	public String getApplePolyphenolUnit() {
		return applePolyphenolUnit;
	}

	/** {@inheritDoc} */
	public Double getApplePolyphenolMin() {
		return applePolyphenolMin;
	}

	/** {@inheritDoc} */
	public Double getApplePolyphenolMax() {
		return applePolyphenolMax;
	}

	/** {@inheritDoc} */
	public String getAscorbiccAcidUnit() {
		return ascorbiccAcidUnit;
	}

	/** {@inheritDoc} */
	public Double getAscorbiccAcidMin() {
		return ascorbiccAcidMin;
	}

	/** {@inheritDoc} */
	public Double getAscorbiccAcidMax() {
		return ascorbiccAcidMax;
	}

	/** {@inheritDoc} */
	public Boolean getAtrInduced() {
		return atrInduced;
	}

	/** {@inheritDoc} */
	public String getAttachmentTimeUnit() {
		return attachmentTimeUnit;
	}

	/** {@inheritDoc} */
	public Double getAttachmentTimeMin() {
		return attachmentTimeMin;
	}

	/** {@inheritDoc} */
	public Double getAttachmentTimeMax() {
		return attachmentTimeMax;
	}

	/** {@inheritDoc} */
	public String getBeanOilUnit() {
		return beanOilUnit;
	}

	/** {@inheritDoc} */
	public Double getBeanOilMin() {
		return beanOilMin;
	}

	/** {@inheritDoc} */
	public Double getBeanOilMax() {
		return beanOilMax;
	}

	/** {@inheritDoc} */
	public String getBenzoicAcidUnit() {
		return benzoicAcidUnit;
	}

	/** {@inheritDoc} */
	public Double getBenzoicAcidMin() {
		return benzoicAcidMin;
	}

	/** {@inheritDoc} */
	public Double getBenzoicAcidMax() {
		return benzoicAcidMax;
	}

	/** {@inheritDoc} */
	public String getBetaineUnit() {
		return betaineUnit;
	}

	/** {@inheritDoc} */
	public Double getBetaineMin() {
		return betaineMin;
	}

	/** {@inheritDoc} */
	public Double getBetaineMax() {
		return betaineMax;
	}

	/** {@inheritDoc} */
	public String getCarvacrolUnit() {
		return carvacrolUnit;
	}

	/** {@inheritDoc} */
	public Double getCarvacrolMin() {
		return carvacrolMin;
	}

	/** {@inheritDoc} */
	public Double getCarvacrolMax() {
		return carvacrolMax;
	}

	/** {@inheritDoc} */
	public String getChitosanUnit() {
		return chitosanUnit;
	}

	/** {@inheritDoc} */
	public Double getChitosanMin() {
		return chitosanMin;
	}

	/** {@inheritDoc} */
	public Double getChitosanMax() {
		return chitosanMax;
	}

	/** {@inheritDoc} */
	public String getCinnamaldehydeUnit() {
		return cinnamaldehydeUnit;
	}

	/** {@inheritDoc} */
	public Double getCinnamaldehydeMin() {
		return cinnamaldehydeMin;
	}

	/** {@inheritDoc} */
	public Double getCinnamaldehydeMax() {
		return cinnamaldehydeMax;
	}

	/** {@inheritDoc} */
	public String getCitricAcidUnit() {
		return citricAcidUnit;
	}

	/** {@inheritDoc} */
	public Double getCitricAcidMin() {
		return citricAcidMin;
	}

	/** {@inheritDoc} */
	public Double getCitricAcidMax() {
		return citricAcidMax;
	}

	/** {@inheritDoc} */
	public Boolean getCured() {
		return cured;
	}

	/** {@inheritDoc} */
	public Boolean getCut() {
		return cut;
	}

	/** {@inheritDoc} */
	public String getDesiredReductionUnit() {
		return desiredReductionUnit;
	}

	/** {@inheritDoc} */
	public Double getDesiredReductionMin() {
		return desiredReductionMin;
	}

	/** {@inheritDoc} */
	public Double getDesiredReductionMax() {
		return desiredReductionMax;
	}

	/** {@inheritDoc} */
	public String getDiaceticAcidUnit() {
		return diaceticAcidUnit;
	}

	/** {@inheritDoc} */
	public Double getDiaceticAcidMin() {
		return diaceticAcidMin;
	}

	/** {@inheritDoc} */
	public Double getDiaceticAcidMax() {
		return diaceticAcidMax;
	}

	/** {@inheritDoc} */
	public String getDisaccharideUnit() {
		return disaccharideUnit;
	}

	/** {@inheritDoc} */
	public Double getDisaccharideMin() {
		return disaccharideMin;
	}

	/** {@inheritDoc} */
	public Double getDisaccharideMax() {
		return disaccharideMax;
	}

	/** {@inheritDoc} */
	public Boolean getDried() {
		return dried;
	}

	/** {@inheritDoc} */
	public String getEthanolUnit() {
		return ethanolUnit;
	}

	/** {@inheritDoc} */
	public Double getEthanolMin() {
		return ethanolMin;
	}

	/** {@inheritDoc} */
	public Double getEthanolMax() {
		return ethanolMax;
	}

	/** {@inheritDoc} */
	public Boolean getExpInoculated() {
		return expInoculated;
	}

	/** {@inheritDoc} */
	public String getFatUnit() {
		return fatUnit;
	}

	/** {@inheritDoc} */
	public Double getFatMin() {
		return fatMin;
	}

	/** {@inheritDoc} */
	public Double getFatMax() {
		return fatMax;
	}

	/** {@inheritDoc} */
	public Boolean getFrozen() {
		return frozen;
	}

	/** {@inheritDoc} */
	public String getFructoseUnit() {
		return fructoseUnit;
	}

	/** {@inheritDoc} */
	public Double getFructoseMin() {
		return fructoseMin;
	}

	/** {@inheritDoc} */
	public Double getFructoseMax() {
		return fructoseMax;
	}

	/** {@inheritDoc} */
	public String getGelMicrostructureUnit() {
		return gelMicrostructureUnit;
	}

	/** {@inheritDoc} */
	public Double getGelMicrostructureMin() {
		return gelMicrostructureMin;
	}

	/** {@inheritDoc} */
	public Double getGelMicrostructureMax() {
		return gelMicrostructureMax;
	}

	/** {@inheritDoc} */
	public String getGlucoseUnit() {
		return glucoseUnit;
	}

	/** {@inheritDoc} */
	public Double getGlucoseMin() {
		return glucoseMin;
	}

	/** {@inheritDoc} */
	public Double getGlucoseMax() {
		return glucoseMax;
	}

	/** {@inheritDoc} */
	public String getGlycerolUnit() {
		return glycerolUnit;
	}

	/** {@inheritDoc} */
	public Double getGlycerolMin() {
		return glycerolMin;
	}

	/** {@inheritDoc} */
	public Double getGlycerolMax() {
		return glycerolMax;
	}

	/** {@inheritDoc} */
	public String getGreenTeaGroundPowderUnit() {
		return greenTeaGroundPowderUnit;
	}

	/** {@inheritDoc} */
	public Double getGreenTeaGroundPowderMin() {
		return greenTeaGroundPowderMin;
	}

	/** {@inheritDoc} */
	public Double getGreenTeaGroundPowderMax() {
		return greenTeaGroundPowderMax;
	}

	/** {@inheritDoc} */
	public String getGreenTeaLeafUnit() {
		return greenTeaLeafUnit;
	}

	/** {@inheritDoc} */
	public Double getGreenTeaLeafMin() {
		return greenTeaLeafMin;
	}

	/** {@inheritDoc} */
	public Double getGreenTeaLeafMax() {
		return greenTeaLeafMax;
	}

	/** {@inheritDoc} */
	public String getGreenTeaWaterExtractUnit() {
		return greenTeaWaterExtractUnit;
	}

	/** {@inheritDoc} */
	public Double getGreenTeaWaterExtractMin() {
		return greenTeaWaterExtractMin;
	}

	/** {@inheritDoc} */
	public Double getGreenTeaWaterExtractMax() {
		return greenTeaWaterExtractMax;
	}

	/** {@inheritDoc} */
	public Boolean getHeated() {
		return heated;
	}

	/** {@inheritDoc} */
	public String getHexosesUnit() {
		return hexosesUnit;
	}

	/** {@inheritDoc} */
	public Double getHexosesMin() {
		return hexosesMin;
	}

	/** {@inheritDoc} */
	public Double getHexosesMax() {
		return hexosesMax;
	}

	/** {@inheritDoc} */
	public Boolean getIndigenous() {
		return indigenous;
	}

	/** {@inheritDoc} */
	public String getInitLevelHistamineUnit() {
		return initLevelHistamineUnit;
	}

	/** {@inheritDoc} */
	public Double getInitLevelHistamineMin() {
		return initLevelHistamineMin;
	}

	/** {@inheritDoc} */
	public Double getInitLevelHistamineMax() {
		return initLevelHistamineMax;
	}

	/** {@inheritDoc} */
	public String getInitLevelHistidineUnit() {
		return initLevelHistidineUnit;
	}

	/** {@inheritDoc} */
	public Double getInitLevelHistidineMin() {
		return initLevelHistidineMin;
	}

	/** {@inheritDoc} */
	public Double getInitLevelHistidineMax() {
		return initLevelHistidineMax;
	}

	/** {@inheritDoc} */
	public String getInjuredUnit() {
		return injuredUnit;
	}

	/** {@inheritDoc} */
	public Double getInjuredMin() {
		return injuredMin;
	}

	/** {@inheritDoc} */
	public Double getInjuredMax() {
		return injuredMax;
	}

	/** {@inheritDoc} */
	public Boolean getIrradiated() {
		return irradiated;
	}

	/** {@inheritDoc} */
	public String getIrradiationUnit() {
		return irradiationUnit;
	}

	/** {@inheritDoc} */
	public Double getIrradiationMin() {
		return irradiationMin;
	}

	/** {@inheritDoc} */
	public Double getIrradiationMax() {
		return irradiationMax;
	}

	/** {@inheritDoc} */
	public String getLacticAcidUnit() {
		return lacticAcidUnit;
	}

	/** {@inheritDoc} */
	public Double getLacticAcidMin() {
		return lacticAcidMin;
	}

	/** {@inheritDoc} */
	public Double getLacticAcidMax() {
		return lacticAcidMax;
	}

	/** {@inheritDoc} */
	public Boolean getLacticBacteriaFermented() {
		return lacticBacteriaFermented;
	}

	/** {@inheritDoc} */
	public String getLauricidinUnit() {
		return lauricidinUnit;
	}

	/** {@inheritDoc} */
	public Double getLauricidinMin() {
		return lauricidinMin;
	}

	/** {@inheritDoc} */
	public Double getLauricidinMax() {
		return lauricidinMax;
	}

	/** {@inheritDoc} */
	public String getMalicAcidUnit() {
		return malicAcidUnit;
	}

	/** {@inheritDoc} */
	public Double getMalicAcidMin() {
		return malicAcidMin;
	}

	/** {@inheritDoc} */
	public Double getMalicAcidMax() {
		return malicAcidMax;
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
	public void setSoftware(String software) {
		this.software = software;
	}

	/** {@inheritDoc} */
	public void setSoftwareLink(URL link) {
		this.softwareLink = link;
	}

	/** {@inheritDoc} */
	public void setSoftwareNotes(String notes) {
		this.softwareNotes = notes;
	}

	/** {@inheritDoc} */
	public void setSoftwareAccesibility(String softwareAccesibility) {
		this.softwareAccesibility = softwareAccesibility;
	}

	/** {@inheritDoc} */
	public void setSoftwareStochasticModeling(boolean stochasticModeling) {
		this.softwareStochasticModeling = stochasticModeling;
	}

	/** {@inheritDoc} */
	public void setSoftwarePredictionConditions(String predictionConditions) {
		softwarePredictionConditions = predictionConditions;
	}

	/** {@inheritDoc} */
	public void setInitLevelUnit(String initLevelUnit) {
		this.initLevelUnit = initLevelUnit;
	}

	/** {@inheritDoc} */
	public void setInitLevelMin(double initLevelMin) {
		this.initLevelMin = initLevelMin;
	}

	/** {@inheritDoc} */
	public void setInitLevelMax(double initLevelMax) {
		this.initLevelMax = initLevelMax;
	}

	/** {@inheritDoc} */
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	/** {@inheritDoc} */
	public void setTimeMin(double timeMin) {
		this.timeMin = timeMin;
	}

	/** {@inheritDoc} */
	public void setTimeMax(double timeMax) {
		this.timeMax = timeMax;
	}

	/** {@inheritDoc} */
	public void setTemperatureUnit(String temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
	}

	/** {@inheritDoc} */
	public void setTemperatureMin(double temperatureMin) {
		this.temperatureMin = temperatureMin;
	}

	/** {@inheritDoc} */
	public void setTemperatureMax(double temperatureMax) {
		this.temperatureMax = temperatureMax;
	}

	/** {@inheritDoc} */
	public void setPhUnit(String phUnit) {
		this.phUnit = phUnit;
	}

	/** {@inheritDoc} */
	public void setPhMin(double phMin) {
		this.phMin = phMin;
	}

	/** {@inheritDoc} */
	public void setPhMax(double phMax) {
		this.phMax = phMax;
	}

	/** {@inheritDoc} */
	public void setAwUnit(String awUnit) {
		this.awUnit = awUnit;
	}

	/** {@inheritDoc} */
	public void setAwMin(double awMin) {
		this.awMin = awMin;
	}

	/** {@inheritDoc} */
	public void setAwMax(double awMax) {
		this.awMax = awMax;
	}

	/** {@inheritDoc} */
	public void setNaClUnit(String naclUnit) {
		this.naclUnit = naclUnit;
	}

	/** {@inheritDoc} */
	public void setNaClMin(double naclMin) {
		this.naclMin = naclMin;
	}

	/** {@inheritDoc} */
	public void setNaClMax(double naclMax) {
		this.naclMax = naclMax;
	}

	/** {@inheritDoc} */
	public void setAltaUnit(String altaUnit) {
		this.altaUnit = altaUnit;
	}

	/** {@inheritDoc} */
	public void setAltaMin(double altaMin) {
		this.altaMin = altaMin;
	}

	/** {@inheritDoc} */
	public void setAltaMax(double altaMax) {
		this.altaMax = altaMax;
	}

	/** {@inheritDoc} */
	public void setCO2Unit(String co2Unit) {
		this.co2Unit = co2Unit;
	}

	/** {@inheritDoc} */
	public void setCO2Min(double co2Min) {
		this.co2Min = co2Min;
	}

	/** {@inheritDoc} */
	public void setCO2Max(double co2Max) {
		this.co2Max = co2Max;
	}

	/** {@inheritDoc} */
	public void setClO2Unit(String clo2Unit) {
		this.clo2Unit = clo2Unit;
	}

	/** {@inheritDoc} */
	public void setClO2Min(double clo2Min) {
		this.clo2Min = clo2Min;
	}

	/** {@inheritDoc} */
	public void setClO2Max(double clo2Max) {
		this.clo2Max = clo2Max;
	}

	/** {@inheritDoc} */
	public void setEdtaUnit(String edtaUnit) {
		this.edtaUnit = edtaUnit;
	}

	/** {@inheritDoc} */
	public void setEdtaMin(double edtaMin) {
		this.edtaMin = edtaMin;
	}

	/** {@inheritDoc} */
	public void setEdtaMax(double edtaMax) {
		this.edtaMax = edtaMax;
	}

	/** {@inheritDoc} */
	public void setHClUnit(String hclUnit) {
		this.hclUnit = hclUnit;
	}

	/** {@inheritDoc} */
	public void setHClMin(double hclMin) {
		this.hclMin = hclMin;
	}

	/** {@inheritDoc} */
	public void setHClMax(double hclMax) {
		this.hclMax = hclMax;
	}

	/** {@inheritDoc} */
	public void setN2Unit(String n2Unit) {
		this.n2Unit = n2Unit;
	}

	/** {@inheritDoc} */
	public void setN2Min(double n2Min) {
		this.n2Min = n2Min;
	}

	/** {@inheritDoc} */
	public void setN2Max(double n2Max) {
		this.n2Max = n2Max;
	}

	/** {@inheritDoc} */
	public void setO2Unit(String o2Unit) {
		this.o2Unit = o2Unit;
	}

	/** {@inheritDoc} */
	public void setO2Min(double o2Min) {
		this.o2Min = o2Min;
	}

	/** {@inheritDoc} */
	public void setO2Max(double o2Max) {
		this.o2Max = o2Max;
	}

	/** {@inheritDoc} */
	public void setAceticAcidUnit(String aceticAcidUnit) {
		this.aceticAcidUnit = aceticAcidUnit;
	}

	/** {@inheritDoc} */
	public void setAceticAcidMin(double aceticAcidMin) {
		this.aceticAcidMin = aceticAcidMin;
	}

	/** {@inheritDoc} */
	public void setAceticAcidMax(double aceticAcidMax) {
		this.aceticAcidMax = aceticAcidMax;
	}

	/** {@inheritDoc} */
	public void setAdditives(String additives) {
		this.additives = additives;
	}

	/** {@inheritDoc} */
	public void setAnaerobic(boolean anaerobic) {
		this.anaerobic = anaerobic;
	}

	/** {@inheritDoc} */
	public void setAntimicrobialDippingTimeUnit(String antimicrobialDippingTimeUnit) {
		this.antimicrobialDippingTimeUnit = antimicrobialDippingTimeUnit;
	}

	/** {@inheritDoc} */
	public void setAntimicrobialDippingTimeMin(double antimicrobialDippingTimeMin) {
		this.antimicrobialDippingTimeMin = antimicrobialDippingTimeMin;
	}

	/** {@inheritDoc} */
	public void setAntimicrobialDippingTimeMax(double antimicrobialDippingTimeMax) {
		this.antimicrobialDippingTimeMax = antimicrobialDippingTimeMax;
	}

	/** {@inheritDoc} */
	public void setApplePolyphenolUnit(String applePolyphenolUnit) {
		this.applePolyphenolUnit = applePolyphenolUnit;
	}

	/** {@inheritDoc} */
	public void setApplePolyphenolMin(double applePolyphenolMin) {
		this.applePolyphenolMin = applePolyphenolMin;
	}

	/** {@inheritDoc} */
	public void setApplePolyphenolMax(double applePolyphenolMax) {
		this.applePolyphenolMax = applePolyphenolMax;
	}

	/** {@inheritDoc} */
	public void setAscorbiccAcidUnit(String ascorbiccAcidUnit) {
		this.ascorbiccAcidUnit = ascorbiccAcidUnit;
	}

	/** {@inheritDoc} */
	public void setAscorbiccAcidMin(double ascorbiccAcidMin) {
		this.ascorbiccAcidMin = ascorbiccAcidMin;
	}

	/** {@inheritDoc} */
	public void setAscorbiccAcidMax(double ascorbiccAcidMax) {
		this.ascorbiccAcidMax = ascorbiccAcidMax;
	}

	/** {@inheritDoc} */
	public void setAtrInduced(boolean atrInduced) {
		this.atrInduced = atrInduced;
	}

	/** {@inheritDoc} */
	public void setAttachmentTimeUnit(String attachmentTimeUnit) {
		this.attachmentTimeUnit = attachmentTimeUnit;
	}

	/** {@inheritDoc} */
	public void setAttachmentTimeMin(double attachmentTimeMin) {
		this.attachmentTimeMin = attachmentTimeMin;
	}

	/** {@inheritDoc} */
	public void setAttachmentTimeMax(double attachmentTimeMax) {
		this.attachmentTimeMax = attachmentTimeMax;
	}

	/** {@inheritDoc} */
	public void setBeanOilUnit(String beanOilUnit) {
		this.beanOilUnit = beanOilUnit;
	}

	/** {@inheritDoc} */
	public void setBeanOilMin(double beanOilMin) {
		this.beanOilMin = beanOilMin;
	}

	/** {@inheritDoc} */
	public void setBeanOilMax(double beanOilMax) {
		this.beanOilMax = beanOilMax;
	}

	/** {@inheritDoc} */
	public void setBenzoicAcidUnit(String benzoicAcidUnit) {
		this.benzoicAcidUnit = benzoicAcidUnit;
	}

	/** {@inheritDoc} */
	public void setBenzoicAcidMin(double benzoicAcidMin) {
		this.benzoicAcidMin = benzoicAcidMin;
	}

	/** {@inheritDoc} */
	public void setBenzoicAcidMax(double benzoicAcidMax) {
		this.benzoicAcidMax = benzoicAcidMax;
	}

	/** {@inheritDoc} */
	public void setBetaineUnit(String betaineUnit) {
		this.betaineUnit = betaineUnit;
	}

	/** {@inheritDoc} */
	public void setBetaineMin(double betaineMin) {
		this.betaineMin = betaineMin;
	}

	/** {@inheritDoc} */
	public void setBetaineMax(double betaineMax) {
		this.betaineMax = betaineMax;
	}

	/** {@inheritDoc} */
	public void setCarvacrolUnit(String carvacrolUnit) {
		this.carvacrolUnit = carvacrolUnit;
	}

	/** {@inheritDoc} */
	public void setCarvacrolMin(double carvacrolMin) {
		this.carvacrolMin = carvacrolMin;
	}

	/** {@inheritDoc} */
	public void setCarvacrolMax(double carvacrolMax) {
		this.carvacrolMax = carvacrolMax;
	}

	/** {@inheritDoc} */
	public void setChitosanUnit(String chitosanUnit) {
		this.chitosanUnit = chitosanUnit;
	}

	/** {@inheritDoc} */
	public void setChitosanMin(double chitosanMin) {
		this.chitosanMin = chitosanMin;
	}

	/** {@inheritDoc} */
	public void setChitosanMax(double chitosanMax) {
		this.chitosanMax = chitosanMax;
	}

	/** {@inheritDoc} */
	public void setCinnamaldehydeUnit(String cinnamaldehydeUnit) {
		this.cinnamaldehydeUnit = cinnamaldehydeUnit;
	}

	/** {@inheritDoc} */
	public void setCinnamaldehydeMin(double cinnamaldehydeMin) {
		this.cinnamaldehydeMin = cinnamaldehydeMin;
	}

	/** {@inheritDoc} */
	public void setCinnamaldehydeMax(double cinnamaldehydeMax) {
		this.cinnamaldehydeMax = cinnamaldehydeMax;
	}

	/** {@inheritDoc} */
	public void setCitricAcidUnit(String citricAcidUnit) {
		this.citricAcidUnit = citricAcidUnit;
	}

	/** {@inheritDoc} */
	public void setCitricAcidMin(double citricAcidMin) {
		this.citricAcidMin = citricAcidMin;
	}

	/** {@inheritDoc} */
	public void setCitricAcidMax(double citricAcidMax) {
		this.citricAcidMax = citricAcidMax;
	}

	/** {@inheritDoc} */
	public void setCured(Boolean cured) {
		this.cured = cured;
	}

	/** {@inheritDoc} */
	public void setCut(Boolean cut) {
		this.cut = cut;
	}

	/** {@inheritDoc} */
	public void setDesiredReductionUnit(String desiredReductionUnit) {
		this.desiredReductionUnit = desiredReductionUnit;
	}

	/** {@inheritDoc} */
	public void setDesiredReductionMin(double desiredReductionMin) {
		this.desiredReductionMin = desiredReductionMin;
	}

	/** {@inheritDoc} */
	public void setDesiredReductionMax(double desiredReductionMax) {
		this.desiredReductionMax = desiredReductionMax;
	}

	/** {@inheritDoc} */
	public void setDiaceticAcidUnit(String diaceticAcidUnit) {
		this.diaceticAcidUnit = diaceticAcidUnit;
	}

	/** {@inheritDoc} */
	public void setDiaceticAcidMin(double diaceticAcidMin) {
		this.diaceticAcidMin = diaceticAcidMin;
	}

	/** {@inheritDoc} */
	public void setDiaceticAcidMax(double diaceticAcidMax) {
		this.diaceticAcidMax = diaceticAcidMax;
	}

	/** {@inheritDoc} */
	public void setDisaccharideUnit(String disaccharideUnit) {
		this.disaccharideUnit = disaccharideUnit;
	}

	/** {@inheritDoc} */
	public void setDisaccharideMin(double disaccharideMin) {
		this.disaccharideMin = disaccharideMin;
	}

	/** {@inheritDoc} */
	public void setDisaccharideMax(double disaccharideMax) {
		this.disaccharideMax = disaccharideMax;
	}

	/** {@inheritDoc} */
	public void setDried(boolean dried) {
		this.dried = dried;
	}

	/** {@inheritDoc} */
	public void setEthanolUnit(String ethanolUnit) {
		this.ethanolUnit = ethanolUnit;
	}

	/** {@inheritDoc} */
	public void setEthanolMin(double ethanolMin) {
		this.ethanolMin = ethanolMin;
	}

	/** {@inheritDoc} */
	public void setEthanolMax(double ethanolMax) {
		this.ethanolMax = ethanolMax;
	}

	/** {@inheritDoc} */
	public void setExpInoculated(boolean expInoculated) {
		this.expInoculated = expInoculated;
	}

	/** {@inheritDoc} */
	public void setFatUnit(String fatUnit) {
		this.fatUnit = fatUnit;
	}

	/** {@inheritDoc} */
	public void setFatMin(double fatMin) {
		this.fatMin = fatMin;
	}

	/** {@inheritDoc} */
	public void setFatMax(double fatMax) {
		this.fatMax = fatMax;
	}

	/** {@inheritDoc} */
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	/** {@inheritDoc} */
	public void setFructoseUnit(String fructoseUnit) {
		this.fructoseUnit = fructoseUnit;
	}

	/** {@inheritDoc} */
	public void setFructoseMin(double fructoseMin) {
		this.fructoseMin = fructoseMin;
	}

	/** {@inheritDoc} */
	public void setFructoseMax(double fructoseMax) {
		this.fructoseMax = fructoseMax;
	}

	/** {@inheritDoc} */
	public void setGelMicrostructureUnit(String gelMicrostructureUnit) {
		this.gelMicrostructureUnit = gelMicrostructureUnit;
	}

	/** {@inheritDoc} */
	public void setGelMicrostructureMin(double gelMicrostructureMin) {
		this.gelMicrostructureMin = gelMicrostructureMin;
	}

	/** {@inheritDoc} */
	public void setGelMicrostructureMax(double gelMicrostructureMax) {
		this.gelMicrostructureMax = gelMicrostructureMax;
	}

	/** {@inheritDoc} */
	public void setGlucoseUnit(String glucoseUnit) {
		this.glucoseUnit = glucoseUnit;
	}

	/** {@inheritDoc} */
	public void setGlucoseMin(double glucoseMin) {
		this.glucoseMin = glucoseMin;
	}

	/** {@inheritDoc} */
	public void setGlucoseMax(double glucoseMax) {
		this.glucoseMax = glucoseMax;
	}

	/** {@inheritDoc} */
	public void setGlycerolUnit(String glycerolUnit) {
		this.glycerolUnit = glycerolUnit;
	}

	/** {@inheritDoc} */
	public void setGlycerolMin(double glycerolMin) {
		this.glycerolMin = glycerolMin;
	}

	/** {@inheritDoc} */
	public void setGlycerolMax(double glycerolMax) {
		this.glycerolMax = glycerolMax;
	}

	/** {@inheritDoc} */
	public void setGreenTeaGroundPowderUnit(String greenTeaGroundPowderUnit) {
		this.greenTeaGroundPowderUnit = greenTeaGroundPowderUnit;
	}

	/** {@inheritDoc} */
	public void setGreenTeaGroundPowderMin(double greenTeaGroundPowderMin) {
		this.greenTeaGroundPowderMin = greenTeaGroundPowderMin;
	}

	/** {@inheritDoc} */
	public void setGreenTeaGroundPowderMax(double greenTeaGroundPowderMax) {
		this.greenTeaGroundPowderMax = greenTeaGroundPowderMax;
	}

	/** {@inheritDoc} */
	public void setGreenTeaLeafUnit(String greenTeaLeafUnit) {
		this.greenTeaLeafUnit = greenTeaLeafUnit;
	}

	/** {@inheritDoc} */
	public void setGreenTeaLeafMin(double greenTeaLeafMin) {
		this.greenTeaLeafMin = greenTeaLeafMin;
	}

	/** {@inheritDoc} */
	public void setGreenTeaLeafMax(double greenTeaLeafMax) {
		this.greenTeaLeafMax = greenTeaLeafMax;
	}

	/** {@inheritDoc} */
	public void setGreenTeaWaterExtractUnit(String greenTeaWaterExtractUnit) {
		this.greenTeaWaterExtractUnit = greenTeaWaterExtractUnit;
	}

	/** {@inheritDoc} */
	public void setGreenTeaWaterExtractMin(double greenTeaWaterExtractMin) {
		this.greenTeaWaterExtractMin = greenTeaWaterExtractMin;
	}

	/** {@inheritDoc} */
	public void setGreenTeaWaterExtractMax(double greenTeaWaterExtractMax) {
		this.greenTeaWaterExtractMax = greenTeaWaterExtractMax;
	}

	/** {@inheritDoc} */
	public void setHeated(boolean heated) {
		this.heated = heated;
	}

	/** {@inheritDoc} */
	public void setHexosesUnit(String hexosesUnit) {
		this.hexosesUnit = hexosesUnit;
	}

	/** {@inheritDoc} */
	public void setHexosesMin(double hexosesMin) {
		this.hexosesMin = hexosesMin;
	}

	/** {@inheritDoc} */
	public void setHexosesMax(double hexosesMax) {
		this.hexosesMax = hexosesMax;
	}

	/** {@inheritDoc} */
	public void setIndigenous(boolean indigenous) {
		this.indigenous = indigenous;
	}

	/** {@inheritDoc} */
	public void setInitLevelHistamineUnit(String initLevelHistamineUnit) {
		this.initLevelHistamineUnit = initLevelHistamineUnit;
	}

	/** {@inheritDoc} */
	public void setInitLevelHistamineMin(double initLevelHistamineMin) {
		this.initLevelHistamineMin = initLevelHistamineMin;
	}

	/** {@inheritDoc} */
	public void setInitLevelHistamineMax(double initLevelHistamineMax) {
		this.initLevelHistamineMax = initLevelHistamineMax;
	}

	/** {@inheritDoc} */
	public void setInitLevelHistidineUnit(String initLevelHistidineUnit) {
		this.initLevelHistidineUnit = initLevelHistidineUnit;
	}

	/** {@inheritDoc} */
	public void setInitLevelHistidineMin(double initLevelHistidineMin) {
		this.initLevelHistidineMin = initLevelHistidineMin;
	}

	/** {@inheritDoc} */
	public void setInitLevelHistidineMax(double initLevelHistidineMax) {
		this.initLevelHistidineMax = initLevelHistidineMax;
	}

	/** {@inheritDoc} */
	public void setInjuredUnit(String injuredUnit) {
		this.injuredUnit = injuredUnit;
	}

	/** {@inheritDoc} */
	public void setInjuredMin(double injuredMin) {
		this.injuredMin = injuredMin;
	}

	/** {@inheritDoc} */
	public void setInjuredMax(double injuredMax) {
		this.injuredMax = injuredMax;
	}

	/** {@inheritDoc} */
	public void setIrradiated(boolean irradiated) {
		this.irradiated = irradiated;
	}

	/** {@inheritDoc} */
	public void setIrradiationUnit(String irradiationUnit) {
		this.irradiationUnit = irradiationUnit;
	}

	/** {@inheritDoc} */
	public void setIrradiationMin(double irradiationMin) {
		this.irradiationMin = irradiationMin;
	}

	/** {@inheritDoc} */
	public void setIrradiationMax(double irradiationMax) {
		this.irradiationMax = irradiationMax;
	}

	/** {@inheritDoc} */
	public void setLacticAcidUnit(String lacticAcidUnit) {
		this.lacticAcidUnit = lacticAcidUnit;
	}

	/** {@inheritDoc} */
	public void setLacticAcidMin(double lacticAcidMin) {
		this.lacticAcidMin = lacticAcidMin;
	}

	/** {@inheritDoc} */
	public void setLacticAcidMax(double lacticAcidMax) {
		this.lacticAcidMax = lacticAcidMax;
	}

	/** {@inheritDoc} */
	public void setLacticBacteriaFermented(boolean lacticBacteriaFermented) {
		this.lacticBacteriaFermented = lacticBacteriaFermented;
	}

	/** {@inheritDoc} */
	public void setLauricidinUnit(String lauricidinUnit) {
		this.lauricidinUnit = lauricidinUnit;
	}

	/** {@inheritDoc} */
	public void setLauricidinMin(double lauricidinMin) {
		this.lauricidinMin = lauricidinMin;
	}

	/** {@inheritDoc} */
	public void setLauricidinMax(double lauricidinMax) {
		this.lauricidinMax = lauricidinMax;
	}

	/** {@inheritDoc} */
	public void setMalicAcidUnit(String malicAcidUnit) {
		this.malicAcidUnit = malicAcidUnit;
	}

	/** {@inheritDoc} */
	public void setMalicAcidMin(double malicAcidMin) {
		this.malicAcidMin = malicAcidMin;
	}

	/** {@inheritDoc} */
	public void setMalicAcidMax(double malicAcidMax) {
		this.malicAcidMax = malicAcidMax;
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
	public void unsetSoftware() {
		software = null;
	}

	/** {@inheritDoc} */
	public void unsetSoftwareLink() {
		softwareLink = null;
	}

	/** {@inheritDoc} */
	public void unsetSoftwareNotes() {
		softwareNotes = null;
	}

	/** {@inheritDoc} */
	public void unsetSoftwareAccesibility() {
		softwareAccesibility = null;
	}

	/** {@inheritDoc} */
	public void unsetSoftwareStochasticModeling() {
		softwareStochasticModeling = null;
	}

	/** {@inheritDoc} */
	public void unsetSoftwarePredictionConditions() {
		softwarePredictionConditions = null;
	}

	/** {@inheritDoc} */
	public void unsetInitLevelUnit() {
		initLevelUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetInitLevelMin() {
		initLevelMin = null;
	}

	/** {@inheritDoc} */
	public void unsetInitLevelMax() {
		initLevelMax = null;
	}

	/** {@inheritDoc} */
	public void unsetTimeUnit() {
		timeUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetTimeMin() {
		timeMin = null;
	}

	/** {@inheritDoc} */
	public void unsetTimeMax() {
		timeMax = null;
	}

	/** {@inheritDoc} */
	public void unsetTemperatureUnit() {
		temperatureUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetTemperatureMin() {
		temperatureMin = null;
	}

	/** {@inheritDoc} */
	public void unsetTemperatureMax() {
		temperatureMax = null;
	}

	/** {@inheritDoc} */
	public void unsetPhUnit() {
		phUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetPhMin() {
		phMin = null;
	}

	/** {@inheritDoc} */
	public void unsetPhMax() {
		phMax = null;
	}

	/** {@inheritDoc} */
	public void unsetAwUnit() {
		awUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetAwMin() {
		awMin = null;
	}

	/** {@inheritDoc} */
	public void unsetAwMax() {
		awMax = null;
	}

	/** {@inheritDoc} */
	public void unsetNaClUnit() {
		naclUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetNaClMin() {
		naclMin = null;
	}

	/** {@inheritDoc} */
	public void unsetNaClMax() {
		naclMax = null;
	}

	/** {@inheritDoc} */
	public void unsetAltaUnit() {
		altaUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetAltaMin() {
		altaMin = null;
	}

	/** {@inheritDoc} */
	public void unsetAltaMax() {
		altaMax = null;
	}

	/** {@inheritDoc} */
	public void unsetCO2Unit() {
		co2Unit = null;
	}

	/** {@inheritDoc} */
	public void unsetCO2Min() {
		co2Min = null;
	}

	/** {@inheritDoc} */
	public void unsetCO2Max() {
		co2Max = null;
	}

	/** {@inheritDoc} */
	public void unsetClO2Unit() {
		clo2Unit = null;
	}

	/** {@inheritDoc} */
	public void unsetClO2Min() {
		clo2Min = null;
	}

	/** {@inheritDoc} */
	public void unsetClO2Max() {
		clo2Max = null;
	}

	/** {@inheritDoc} */
	public void unsetEdtaUnit() {
		edtaUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetEdtaMin() {
		edtaMin = null;
	}

	/** {@inheritDoc} */
	public void unsetEdtaMax() {
		edtaMax = null;
	}

	/** {@inheritDoc} */
	public void unsetHClUnit() {
		hclUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetHClMin() {
		hclMin = null;
	}

	/** {@inheritDoc} */
	public void unsetHClMax() {
		hclMax = null;
	}

	/** {@inheritDoc} */
	public void unsetN2Unit() {
		n2Unit = null;
	}

	/** {@inheritDoc} */
	public void unsetN2Min() {
		n2Min = null;
	}

	/** {@inheritDoc} */
	public void unsetN2Max() {
		n2Max = null;
	}

	/** {@inheritDoc} */
	public void unsetO2Unit() {
		o2Unit = null;
	}

	/** {@inheritDoc} */
	public void unsetO2Min() {
		o2Min = null;
	}

	/** {@inheritDoc} */
	public void unsetO2Max() {
		o2Max = null;
	}

	/** {@inheritDoc} */
	public void unsetAceticAcidUnit() {
		aceticAcidUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetAceticAcidMin() {
		aceticAcidMin = null;
	}

	/** {@inheritDoc} */
	public void unsetAceticAcidMax() {
		aceticAcidMax = null;
	}

	/** {@inheritDoc} */
	public void unsetAdditives() {
		additives = null;
	}

	/** {@inheritDoc} */
	public void unsetAnaerobic() {
		anaerobic = null;
	}

	/** {@inheritDoc} */
	public void unsetAntimicrobialDippingTimeUnit() {
		antimicrobialDippingTimeUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetAntimicrobialDippingTimeMin() {
		antimicrobialDippingTimeMin = null;
	}

	/** {@inheritDoc} */
	public void unsetAntimicrobialDippingTimeMax() {
		antimicrobialDippingTimeMax = null;
	}

	/** {@inheritDoc} */
	public void unsetApplePolyphenolUnit() {
		applePolyphenolUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetApplePolyphenolMin() {
		applePolyphenolMin = null;
	}

	/** {@inheritDoc} */
	public void unsetApplePolyphenolMax() {
		applePolyphenolMax = null;
	}

	/** {@inheritDoc} */
	public void unsetAscorbiccAcidUnit() {
		ascorbiccAcidUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetAscorbiccAcidMin() {
		ascorbiccAcidMin = null;
	}

	/** {@inheritDoc} */
	public void unsetAscorbiccAcidMax() {
		ascorbiccAcidMax = null;
	}

	/** {@inheritDoc} */
	public void unsetAtrInduced() {
		atrInduced = null;
	}

	/** {@inheritDoc} */
	public void unsetAttachmentTimeUnit() {
		attachmentTimeUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetAttachmentTimeMin() {
		attachmentTimeMin = null;
	}

	/** {@inheritDoc} */
	public void unsetAttachmentTimeMax() {
		attachmentTimeMax = null;
	}

	/** {@inheritDoc} */
	public void unsetBeanOilUnit() {
		beanOilUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetBeanOilMin() {
		beanOilMin = null;
	}

	/** {@inheritDoc} */
	public void unsetBeanOilMax() {
		beanOilMax = null;
	}

	/** {@inheritDoc} */
	public void unsetBenzoicAcidUnit() {
		benzoicAcidUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetBenzoicAcidMin() {
		benzoicAcidMin = null;
	}

	/** {@inheritDoc} */
	public void unsetBenzoicAcidMax() {
		benzoicAcidMax = null;
	}

	/** {@inheritDoc} */
	public void unsetBetaineUnit() {
		betaineUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetBetaineMin() {
		betaineMin = null;
	}

	/** {@inheritDoc} */
	public void unsetBetaineMax() {
		betaineMax = null;
	}

	/** {@inheritDoc} */
	public void unsetCarvacrolUnit() {
		carvacrolUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetCarvacrolMin() {
		carvacrolMin = null;
	}

	/** {@inheritDoc} */
	public void unsetCarvacrolMax() {
		carvacrolMax = null;
	}

	/** {@inheritDoc} */
	public void unsetChitosanUnit() {
		chitosanUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetChitosanMin() {
		chitosanMin = null;
	}

	/** {@inheritDoc} */
	public void unsetChitosanMax() {
		chitosanMax = null;
	}

	/** {@inheritDoc} */
	public void unsetCinnamaldehydeUnit() {
		cinnamaldehydeUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetCinnamaldehydeMin() {
		cinnamaldehydeMin = null;
	}

	/** {@inheritDoc} */
	public void unsetCinnamaldehydeMax() {
		cinnamaldehydeMax = null;
	}

	/** {@inheritDoc} */
	public void unsetCitricAcidUnit() {
		citricAcidUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetCitricAcidMin() {
		citricAcidMin = null;
	}

	/** {@inheritDoc} */
	public void unsetCitricAcidMax() {
		citricAcidMax = null;
	}

	/** {@inheritDoc} */
	public void unsetCured() {
		cured = null;
	}

	/** {@inheritDoc} */
	public void unsetCut() {
		cut = null;
	}

	/** {@inheritDoc} */
	public void unsetDesiredReductionUnit() {
		desiredReductionUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetDesiredReductionMin() {
		desiredReductionMin = null;
	}

	/** {@inheritDoc} */
	public void unsetDesiredReductionMax() {
		desiredReductionMax = null;
	}

	/** {@inheritDoc} */
	public void unsetDiaceticAcidUnit() {
		diaceticAcidUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetDiaceticAcidMin() {
		diaceticAcidMin = null;
	}

	/** {@inheritDoc} */
	public void unsetDiaceticAcidMax() {
		diaceticAcidMax = null;
	}

	/** {@inheritDoc} */
	public void unsetDisaccharideUnit() {
		disaccharideUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetDisaccharideMin() {
		disaccharideMin = null;
	}

	/** {@inheritDoc} */
	public void unsetDisaccharideMax() {
		disaccharideMax = null;
	}

	/** {@inheritDoc} */
	public void unsetDried() {
		dried = null;
	}

	/** {@inheritDoc} */
	public void unsetEthanolUnit() {
		ethanolUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetEthanolMin() {
		ethanolMin = null;
	}

	/** {@inheritDoc} */
	public void unsetEthanolMax() {
		ethanolMax = null;
	}

	/** {@inheritDoc} */
	public void unsetExpInoculated() {
		expInoculated = null;
	}

	/** {@inheritDoc} */
	public void unsetFatUnit() {
		fatUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetFatMin() {
		fatMin = null;
	}

	/** {@inheritDoc} */
	public void unsetFatMax() {
		fatMax = null;
	}

	/** {@inheritDoc} */
	public void unsetFrozen() {
		frozen = null;
	}

	/** {@inheritDoc} */
	public void unsetFructoseUnit() {
		fructoseUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetFructoseMin() {
		fructoseMin = null;
	}

	/** {@inheritDoc} */
	public void unsetFructoseMax() {
		fructoseMax = null;
	}

	/** {@inheritDoc} */
	public void unsetGelMicrostructureUnit() {
		gelMicrostructureUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetGelMicrostructureMin() {
		gelMicrostructureMin = null;
	}

	/** {@inheritDoc} */
	public void unsetGelMicrostructureMax() {
		gelMicrostructureMax = null;
	}

	/** {@inheritDoc} */
	public void unsetGlucoseUnit() {
		glucoseUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetGlucoseMin() {
		glucoseMin = null;
	}

	/** {@inheritDoc} */
	public void unsetGlucoseMax() {
		glucoseMax = null;
	}

	/** {@inheritDoc} */
	public void unsetGlycerolUnit() {
		glycerolUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetGlycerolMin() {
		glycerolMin = null;
	}

	/** {@inheritDoc} */
	public void unsetGlycerolMax() {
		glycerolMax = null;
	}

	/** {@inheritDoc} */
	public void unsetGreenTeaGroundPowderUnit() {
		greenTeaGroundPowderUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetGreenTeaGroundPowderMin() {
		greenTeaGroundPowderMin = null;
	}

	/** {@inheritDoc} */
	public void unsetGreenTeaGroundPowderMax() {
		greenTeaGroundPowderMax = null;
	}

	/** {@inheritDoc} */
	public void unsetGreenTeaLeafUnit() {
		greenTeaLeafUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetGreenTeaLeafMin() {
		greenTeaLeafMin = null;
	}

	/** {@inheritDoc} */
	public void unsetGreenTeaLeafMax() {
		greenTeaLeafMax = null;
	}

	/** {@inheritDoc} */
	public void unsetGreenTeaWaterExtractUnit() {
		greenTeaWaterExtractUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetGreenTeaWaterExtractMin() {
		greenTeaWaterExtractMin = null;
	}

	/** {@inheritDoc} */
	public void unsetGreenTeaWaterExtractMax() {
		greenTeaWaterExtractMax = null;
	}

	/** {@inheritDoc} */
	public void unsetHeated() {
		heated = null;
	}

	/** {@inheritDoc} */
	public void unsetHexosesUnit() {
		hexosesUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetHexosesMin() {
		hexosesMin = null;
	}

	/** {@inheritDoc} */
	public void unsetHexosesMax() {
		hexosesMax = null;
	}

	/** {@inheritDoc} */
	public void unsetIndigenous() {
		indigenous = null;
	}

	/** {@inheritDoc} */
	public void unsetInitLevelHistamineUnit() {
		initLevelHistamineUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetInitLevelHistamineMin() {
		initLevelHistamineMin = null;
	}

	/** {@inheritDoc} */
	public void unsetInitLevelHistamineMax() {
		initLevelHistamineMax = null;
	}

	/** {@inheritDoc} */
	public void unsetInitLevelHistidineUnit() {
		initLevelHistidineUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetInitLevelHistidineMin() {
		initLevelHistidineMin = null;
	}

	/** {@inheritDoc} */
	public void unsetInitLevelHistidineMax() {
		initLevelHistidineMax = null;
	}

	/** {@inheritDoc} */
	public void unsetInjuredUnit() {
		injuredUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetInjuredMin() {
		injuredMin = null;
	}

	/** {@inheritDoc} */
	public void unsetInjuredMax() {
		injuredMax = null;
	}

	/** {@inheritDoc} */
	public void unsetIrradiated() {
		irradiated = null;
	}

	/** {@inheritDoc} */
	public void unsetIrradiationUnit() {
		irradiationUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetIrradiationMin() {
		irradiationMin = null;
	}

	/** {@inheritDoc} */
	public void unsetIrradiationMax() {
		irradiationMax = null;
	}

	/** {@inheritDoc} */
	public void unsetLacticAcidUnit() {
		lacticAcidUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetLacticAcidMin() {
		lacticAcidMin = null;
	}

	/** {@inheritDoc} */
	public void unsetLacticAcidMax() {
		lacticAcidMax = null;
	}

	/** {@inheritDoc} */
	public void unsetLacticBacteriaFermented() {
		lacticBacteriaFermented = null;
	}

	/** {@inheritDoc} */
	public void unsetLauricidinUnit() {
		lauricidinUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetLauricidinMin() {
		lauricidinMin = null;
	}

	/** {@inheritDoc} */
	public void unsetLauricidinMax() {
		lauricidinMax = null;
	}

	/** {@inheritDoc} */
	public void unsetMalicAcidUnit() {
		malicAcidUnit = null;
	}

	/** {@inheritDoc} */
	public void unsetMalicAcidMin() {
		malicAcidMin = null;
	}

	/** {@inheritDoc} */
	public void unsetMalicAcidMax() {
		malicAcidMax = null;
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

	/** {@inheritDoc) */
	public boolean isSetSoftware() {
		return software != null;
	}

	/** {@inheritDoc} */
	public boolean isSetSoftwareLink() {
		return softwareLink != null;
	}

	/** {@inheritDoc} */
	public boolean isSetSoftwareNotes() {
		return softwareNotes != null;
	}

	/** {@inheritDoc} */
	public boolean isSetSoftwareAccessibility() {
		return softwareAccesibility != null;
	}

	/** {@inheritDoc} */
	public boolean isSetSoftwareStochasticModeling() {
		return softwareStochasticModeling != null;
	}

	/** {@inheritDoc} */
	public boolean isSetSoftwarePredictionConditions() {
		return softwarePredictionConditions != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInitLevelUnit() {
		return initLevelUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInitLevelMin() {
		return initLevelMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInitLevelMax() {
		return initLevelMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetTimeUnit() {
		return timeUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetTimeMin() {
		return timeMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetTimeMax() {
		return timeMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetTemperatureUnit() {
		return temperatureUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetTemperatureMin() {
		return temperatureMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetTemperatureMax() {
		return temperatureMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetPhUnit() {
		return phUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetPhMin() {
		return phMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetPhMax() {
		return phMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAwUnit() {
		return awUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAwMin() {
		return awMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAwMax() {
		return awMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetNaClUnit() {
		return naclUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetNaClMin() {
		return naclMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetNaClMax() {
		return naclMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAltaUnit() {
		return altaUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAltaMin() {
		return altaMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAltaMax() {
		return altaMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCO2Unit() {
		return co2Unit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCO2Min() {
		return co2Min != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCO2Max() {
		return co2Max != null;
	}

	/** {@inheritDoc} */
	public boolean isSetClO2Unit() {
		return clo2Unit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetClO2Min() {
		return clo2Min != null;
	}

	/** {@inheritDoc} */
	public boolean isSetClO2Max() {
		return clo2Max != null;
	}

	/** {@inheritDoc} */
	public boolean isSetEdtaUnit() {
		return edtaUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetEdtaMin() {
		return edtaMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetEdtaMax() {
		return edtaMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetHClUnit() {
		return hclUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetHClMin() {
		return hclMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetHClMax() {
		return hclMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetN2Unit() {
		return n2Unit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetN2Min() {
		return n2Min != null;
	}

	/** {@inheritDoc} */
	public boolean isSetN2Max() {
		return n2Max != null;
	}

	/** {@inheritDoc} */
	public boolean isSetO2Unit() {
		return o2Unit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetO2Min() {
		return o2Min != null;
	}

	/** {@inheritDoc} */
	public boolean isSetO2Max() {
		return o2Max != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAceticAcidUnit() {
		return aceticAcidUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAceticAcidMin() {
		return aceticAcidMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAceticAcidMax() {
		return aceticAcidMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAdditives() {
		return additives != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAnaerobic() {
		return anaerobic != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAntimicrobialDippingTimeUnit() {
		return antimicrobialDippingTimeUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAntimicrobialDippingTimeMin() {
		return antimicrobialDippingTimeMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAntimicrobialDippingTimeMax() {
		return antimicrobialDippingTimeMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetApplePolyphenolUnit() {
		return applePolyphenolUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetApplePolyphenolMin() {
		return applePolyphenolMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetApplePolyphenolMax() {
		return applePolyphenolMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAscorbiccAcidUnit() {
		return ascorbiccAcidUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAscorbiccAcidMin() {
		return ascorbiccAcidMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAscorbiccAcidMax() {
		return ascorbiccAcidMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAtrInduced() {
		return atrInduced != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAttachmentTimeUnit() {
		return attachmentTimeUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAttachmentTimeMin() {
		return attachmentTimeMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetAttachmentTimeMax() {
		return attachmentTimeMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetBeanOilUnit() {
		return beanOilUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetBeanOilMin() {
		return beanOilMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetBeanOilMax() {
		return beanOilMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetBenzoicAcidUnit() {
		return benzoicAcidUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetBenzoicAcidMin() {
		return benzoicAcidMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetBenzoicAcidMax() {
		return benzoicAcidMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetBetaineUnit() {
		return betaineUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetBetaineMin() {
		return betaineMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetBetaineMax() {
		return betaineMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCarvacrolUnit() {
		return carvacrolUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCarvacrolMin() {
		return carvacrolMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCarvacrolMax() {
		return carvacrolMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetChitosanUnit() {
		return chitosanUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetChitosanMin() {
		return chitosanMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetChitosanMax() {
		return chitosanMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCinnamaldehydeUnit() {
		return cinnamaldehydeUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCinnamaldehydeMin() {
		return cinnamaldehydeMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCinnamaldehydeMax() {
		return cinnamaldehydeMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCitricAcidUnit() {
		return citricAcidUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCitricAcidMin() {
		return citricAcidMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCitricAcidMax() {
		return citricAcidMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCured() {
		return cured != null;
	}

	/** {@inheritDoc} */
	public boolean isSetCut() {
		return cut != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDesiredReductionUnit() {
		return desiredReductionUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDesiredReductionMin() {
		return desiredReductionMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDesiredReductionMax() {
		return desiredReductionMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDiaceticAcidUnit() {
		return diaceticAcidUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDiaceticAcidMin() {
		return diaceticAcidMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDiaceticAcidMax() {
		return diaceticAcidMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDisaccharideUnit() {
		return disaccharideUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDisaccharideMin() {
		return disaccharideMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDisaccharideMax() {
		return disaccharideMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDried() {
		return dried != null;
	}

	/** {@inheritDoc} */
	public boolean isSetEthanolUnit() {
		return ethanolUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetEthanolMin() {
		return ethanolMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetEthanolMax() {
		return ethanolMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetExpInoculated() {
		return expInoculated != null;
	}

	/** {@inheritDoc} */
	public boolean isSetFatUnit() {
		return fatUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetFatMin() {
		return fatMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetFatMax() {
		return fatMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetFrozen() {
		return frozen != null;
	}

	/** {@inheritDoc} */
	public boolean isSetFructoseUnit() {
		return fructoseUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetFructoseMin() {
		return fructoseMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetFructoseMax() {
		return fructoseMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGelMicrostructureUnit() {
		return gelMicrostructureUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGelMicrostructureMin() {
		return gelMicrostructureMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGelMicrostructureMax() {
		return gelMicrostructureMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGlucoseUnit() {
		return glucoseUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGlucoseMin() {
		return glucoseMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGlucoseMax() {
		return glucoseMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGlycerolUnit() {
		return glycerolUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGlycerolMin() {
		return glycerolMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGlycerolMax() {
		return glycerolMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGreenTeaGroundPowderUnit() {
		return greenTeaGroundPowderUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGreenTeaGroundPowderMin() {
		return greenTeaGroundPowderMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGreenTeaGroundPowderMax() {
		return greenTeaGroundPowderMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGreenTeaLeafUnit() {
		return greenTeaLeafUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGreenTeaLeafMin() {
		return greenTeaLeafMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGreenTeaLeafMax() {
		return greenTeaLeafMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGreenTeaWaterExtractUnit() {
		return greenTeaWaterExtractUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGreenTeaWaterExtractMin() {
		return greenTeaWaterExtractMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetGreenTeaWaterExtractMax() {
		return greenTeaWaterExtractMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetHeated() {
		return heated != null;
	}

	/** {@inheritDoc} */
	public boolean isSetHexosesUnit() {
		return hexosesUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetHexosesMin() {
		return hexosesMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetHexosesMax() {
		return hexosesMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetIndigenous() {
		return indigenous != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInitLevelHistamineUnit() {
		return initLevelHistamineUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInitLevelHistamineMin() {
		return initLevelHistamineMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInitLevelHistamineMax() {
		return initLevelHistamineMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInitLevelHistidineUnit() {
		return initLevelHistidineUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInitLevelHistidineMin() {
		return initLevelHistidineMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInitLevelHistidineMax() {
		return initLevelHistidineMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInjuredUnit() {
		return injuredUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInjuredMin() {
		return injuredMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetInjuredMax() {
		return injuredMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetIrradiated() {
		return irradiated != null;
	}

	/** {@inheritDoc} */
	public boolean isSetIrradiationUnit() {
		return irradiationUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetIrradiationMin() {
		return irradiationMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetIrradiationMax() {
		return irradiationMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetLacticAcidUnit() {
		return lacticAcidUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetLacticAcidMin() {
		return lacticAcidMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetLacticAcidMax() {
		return lacticAcidMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetLacticBacteriaFermented() {
		return lacticBacteriaFermented != null;
	}

	/** {@inheritDoc} */
	public boolean isSetLauricidinUnit() {
		return lauricidinUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetLauricidinMin() {
		return lauricidinMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetLauricidinMax() {
		return lauricidinMax != null;
	}

	/** {@inheritDoc} */
	public boolean isSetMalicAcidUnit() {
		return malicAcidUnit != null;
	}

	/** {@inheritDoc} */
	public boolean isSetMalicAcidMin() {
		return malicAcidMin != null;
	}

	/** {@inheritDoc} */
	public boolean isSetMalicAcidMax() {
		return malicAcidMax != null;
	}
}
