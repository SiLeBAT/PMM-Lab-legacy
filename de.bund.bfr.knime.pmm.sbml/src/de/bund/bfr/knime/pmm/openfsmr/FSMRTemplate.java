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
 * Template for the Food Safety Model Repository. Includes the properties:
 * <ul>
 * <li><b>Model name:</b> a name given to the model</li>
 * <li><b>Model id:</b> an unambiguous ID</li>
 * <li><b>Model link:</b> the link allowing to download the model file.</li>
 * <li><b>PMF organism:</b> Modelled organism / entity.</li>
 * <li><b>PMF organism detail:</b> Plain text comment describing further details
 * on the modelled organism / entity</li>
 * <li><b>PMF environment:</b> The environment of the organism / entity.</li>
 * <li><b>PMF environment detail:</b> Plain text comments describing the
 * environment and or experimental conditions</li>
 * <li><b>Model creator:</b> The person who contributed to the encoding of hte
 * model in its present form by creating the model file</li>
 * <li><b>Model reference description:</b> A citation to the reference
 * description</li>
 * <li><b>Model reference description link:</b> A link to the reference
 * description</li>
 * <li><b>Model created:</b> Temporal information on the model creation date
 * </li>
 * <li><b>Model modified:</b> Temporal information on the last modification of
 * the model</li>
 * <li><b>Model rights:</b> Information on rights held in and over the resource
 * </li>
 * <li><b>Model notes:</b> Plain text comments describing the model</li>
 * <li><b>Model curation status</b>: The curation status of the model</li>
 * <li><b>Model type:</b> the type of model</li>
 * <li><b>Model subject:</b> the subject of the model</li>
 * <li><b>Model foodprocess:</b> Food production processes in which the model
 * could be used</li>
 * <li><b>Model dependent variables:</b> The properties the model predicts</li>
 * <li><b>Model dependent variable units:</b> Unit of the predicted properties
 * </li>
 * <li><b>Model dependent variable minimum:</b> The co-domain minimal values
 * </li>
 * <li><b>Model dependent variable maximum:</b> The co-domain maximum values
 * </li>
 * <li><b>Model independent variables:</b> The model's independent variable(s)
 * </li>
 * <li><b>Software:</b> Software solution(s) incorporating the model</li>
 * <li><b>Software link:</b> Software link</li>
 * <li><b>Software notes</b> Notes on the software notes</li>
 * <li><b>Software accessibility</b> Software accesibility</li>
 * <li><b>Software stochastic modeling</b> Does the software allow stochastic
 * modeling?</li>
 * <li><b>Software prediction conditions</b> Does the software allow predictions
 * from dynamic environmental conditions?</li>
 * <li><b>Atmosphere</b> Model parameter info (1, 2, 3)</li>
 * <li><b>init level</b> Model parameter info: unit, min and max</li>
 * <li><b>time</b> Model parameter info: unit, min and max</li>
 * <li><b>temp</b> Model parameter info: unit, min and max</li>
 * <li><b>ph</b> Model parameter info: unit, min and max</li>
 * <li><b>aw</b> Model parameter info: unit, min and max</li>
 * <li><b>NaCl</b> Model parameter info: unit, min and max</li>
 * <li><b>ALTA</b> Model parameter info: unit, min and max</li>
 * <li><b>CO2</b> Model parameter info: unit, min and max</li>
 * <li><b>ClO2</b> Model parameter info: unit, min and max</li>
 * <li><b>EDTA</b> Model parameter info: unit, min and max</li>
 * <li><b>HCl</b> Model parameter info: unit, min and max</li>
 * <li><b>N2</b> Model parameter info: unit, min and max</li>
 * <li><b>O2</b> Model parameter info: unit, min and max</li>
 * <li><b>Acetic acid</b> Model parameter info: unit, min and max</li>
 * <li><b>Additives</b> Free text</li>
 * <li><b>Anaerobic</b> Boolean</li>
 * <li><b>Antimicrobial dipping time</b> Model parameter info: unit, min and max
 * </li>
 * <li><b>Apple polyphenol</b> Model parameter info: unit, min and max</li>
 * <li><b>Ascorbicc acid</b> Model parameter info: unit, min and max</li>
 * <li><b>Atr induced</b> Boolean</li>
 * <li><b>Attachment time</b> Model parameter info: unit, min and max</li>
 * <li><b>Bean oil</b> Model parameter info: unit, min and max</li>
 * <li><b>Benzoic acid</b> Model parameter info: unit, min and max</li>
 * <li><b>Betaine</b> Model parameter info: unit, min and max</li>
 * <li><b>Carvacrol</b> Model parameter info: unit, min and max</li>
 * <li><b>Chitosan</b> Model parameter info: unit, min and max</li>
 * <li><b>Cinnamaldehyde</b> Model parameter info: unit, min and max</li>
 * <li><b>Citric acid</b> Model parameter info: unit, min and max</li>
 * <li><b>Co-culture</b> Boolean</li>
 * <li><b>Cured</b> Boolean</li>
 * <li><b>Cut</b> Boolean</li>
 * <li><b>Desired reduction</b> Model parameter info: unit, min and max</li>
 * <li><b>Diacetic acid</b> Model parameter info: unit, min and max</li>
 * <li><b>Diasccharide</b> Model parameter info: unit, min and max</li>
 * <li><b>Dried</b> Boolean</li>
 * <li><b>Ethanol</b> Model parameter info: unit, min and max</li>
 * <li><b>Exp inoculated</b> Boolean</li>
 * <li><b>Fat</b> Model parameter info: unit, min and max</li>
 * <li><b>Frozen</b> Boolean</li>
 * <li><b>Fructose</b> Model parameter info: unit, min and max</li>
 * <li><b>Gel microstructure</b> Model parameter info: unit, min and max</li>
 * <li><b>Glucose</b> Model parameter info: unit, min and max</li>
 * <li><b>Glycerol</b> Model parameter info: unit, min and max</li>
 * <li><b>Green tea ground powder</b> Model parameter info: unit, min and max
 * </li>
 * <li><b>Green tea leaf</b> Model parameter info: unit, min and max</li>
 * <li><b>Green tea polyphenol</b> Model parameter info: unit, min and max</li>
 * <li><b>Green tea water extract</b> Model parameter info: unit, min and max
 * </li>
 * <li><b>Heated</b> Boolean</li>
 * <li><b>Hexoses</b> Model parameter info: unit, min and max</li>
 * <li><b>Indigenous</b> Boolean</li>
 * <li><b>Init level histamine</b> Model parameter info: unit, min and max</li>
 * <li><b>Init level histidine</b> Model parameter info: unit, min and max</li>
 * <li><b>Injured</b> Model parameter info: unit, min and max</li>
 * <li><b>Irradiated</b> Boolean</li>
 * <li><b>Irradation</b> Model parameter info: unit, min and max</li>
 * <li><b>Lactic acid</b> Model parameter info: unit, min and max</li>
 * <li><b>Lactic bacteria fermented</b> Boolean</li>
 * <li><b>Lauricidin</b> Model parameter info: unit, min and max</li>
 * <li><b>Malic acid</b> Model parameter info: unit, min and max</li>
 * </ul>
 *
 * @author Miguel Alba
 */
public interface FSMRTemplate {

	/**
	 * Returns the model name of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getModelName();

	/**
	 * Returns the model id of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getModelId();

	/**
	 * Returns the model link of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public URL getModelLink();

	/**
	 * Returns the organism names of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getOrganismName();

	/**
	 * Returns the organism details of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getOrganismDetails();

	/**
	 * Returns the matrix name of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getMatrixName();

	/**
	 * Returns the matrix details of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getMatrixDetails();

	/**
	 * Returns the creator of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getCreator();

	/**
	 * Returns the reference description of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public String getReferenceDescription();

	/**
	 * Returns the reference description link of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public URL getReferenceDescriptionLink();

	/**
	 * Returns the created date of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Date getCreatedDate();

	/**
	 * Returns the modified date of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Date getModifiedDate();

	/**
	 * Returns the rights of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getRights();

	/**
	 * Returns the notes of this {@link FSMRTemplate}. If not set, returns null.
	 */
	public String getNotes();

	/**
	 * Returns the curatus status of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getCurationStatus();

	/**
	 * Returns the {@link ModelType} of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public ModelType getModelType();

	/**
	 * Returns the {@link ModelClass} of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public ModelClass getModelSubject();

	/**
	 * Returns the food process of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getFoodProcess();

	/**
	 * Returns the dependent variable of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getDependentVariable();

	/**
	 * Returns the dependent variable unit of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public String getDependentVariableUnit();

	/**
	 * Returns the dependent variable mininum of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public Double getDependentVariableMin();

	/**
	 * Returns the dependent variable maximum of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public Double getDependentVariableMax();

	/**
	 * Returns the independent variable of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public String getIndependentVariable();

	/**
	 * Returns the software solution of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getSoftware();

	/**
	 * Returns the link of the software solution of this {@link FSMRTemplate}.
	 * If not set, returns null.
	 */
	public URL getSoftwareLink();

	/**
	 * Returns the notes of the software solution of this {@link FSMRTemplate}.
	 * If not set, returns null.
	 */
	public String getSoftwareNotes();

	/**
	 * Returns the software accesibility of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public String getSoftwareAccesibility();

	/**
	 * Returns the software stochastic modeling of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public Boolean getSoftwareStochasticModeling();

	/**
	 * Returns the software prediction conditions of this {@link FSMRTemplate}.
	 * If not set, returns null.
	 */
	public String getSoftwarePredictionConditions();

	/**
	 * Returns the init level unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getInitLevelUnit();

	/**
	 * Returns the init level min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getInitLevelMin();

	/**
	 * Returns the init level max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getInitLevelMax();

	/**
	 * Returns the time unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getTimeUnit();

	/**
	 * Returns the time min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getTimeMin();

	/**
	 * Returns the time max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getTimeMax();

	/**
	 * Returns the temperature unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getTemperatureUnit();

	/**
	 * Returns the temperature min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getTemperatureMin();

	/**
	 * Returns the temperature max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getTemperatureMax();

	/**
	 * Returns the pH unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getPhUnit();

	/**
	 * Returns the pH min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getPhMin();

	/**
	 * Returns the pH max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getPhMax();

	/**
	 * Returns the water activity unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getAwUnit();

	/**
	 * Returns the water activity min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getAwMin();

	/**
	 * Returns the water activity max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getAwMax();

	/**
	 * Returns the NaCl unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getNaClUnit();

	/**
	 * Returns the NaCl min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getNaClMin();

	/**
	 * Returns the NaCl max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getNaClMax();

	/**
	 * Returns the ALTA unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getAltaUnit();

	/**
	 * Returns the ALTA min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getAltaMin();

	/**
	 * Returns the ALTA max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getAltaMax();

	/**
	 * Returns the CO2 unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getCO2Unit();

	/**
	 * Returns the CO2 min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getCO2Min();

	/**
	 * Returns the CO2 max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getCO2Max();

	/**
	 * Returns the ClO2 unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getClO2Unit();

	/**
	 * Returns the ClO2 min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getClO2Min();

	/**
	 * Returns the ClO2 max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getClO2Max();

	/**
	 * Returns the EDTA unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getEdtaUnit();

	/**
	 * Returns the EDTA min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getEdtaMin();

	/**
	 * Returns the EDTA max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getEdtaMax();

	/**
	 * Returns the HCl unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getHClUnit();

	/**
	 * Returns the HCl min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getHClMin();

	/**
	 * Returns the HCl max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getHClMax();

	/**
	 * Returns the N2 unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getN2Unit();

	/**
	 * Returns the N2 min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getN2Min();

	/**
	 * Returns the N2 max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getN2Max();

	/**
	 * Returns the O2 unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getO2Unit();

	/**
	 * Returns the O2 min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getO2Min();

	/**
	 * Returns the O2 max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getO2Max();

	/**
	 * Returns the acetic acid unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getAceticAcidUnit();

	/**
	 * Returns the acetic acid min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getAceticAcidMin();

	/**
	 * Returns the acetic acid max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getAceticAcidMax();

	/**
	 * Returns the additives of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getAdditives();

	/**
	 * Returns the anaerobic of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Boolean getAnaerobic();

	/**
	 * Returns the antimicrobial dipping time unit of this {@link FSMRTemplate}.
	 * If not set, returns null.
	 */
	public String getAntimicrobialDippingTimeUnit();

	/**
	 * Returns the antimicrobial dipping time min of this {@link FSMRTemplate}.
	 * If not set, returns null.
	 */
	public Double getAntimicrobialDippingTimeMin();

	/**
	 * Returns the antimicrobial dipping time max of this {@link FSMRTemplate}.
	 * If not set, returns null.
	 */
	public Double getAntimicrobialDippingTimeMax();

	/**
	 * Returns the apple polyphenol unit of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public String getApplePolyphenolUnit();

	/**
	 * Returns the apple polyphenol min of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getApplePolyphenolMin();

	/**
	 * Returns the apple polyphenol max of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getApplePolyphenolMax();

	/**
	 * Returns the ascorbicc acid unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getAscorbiccAcidUnit();

	/**
	 * Returns the ascorbicc acid min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getAscorbiccAcidMin();

	/**
	 * Returns the ascorbicc acid max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getAscorbiccAcidMax();

	/**
	 * Returns the atrInduced of this {@link FSMRTemplate}. If not set returns
	 * null.
	 */
	public Boolean getAtrInduced();

	/**
	 * Returns the attachment time unit of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public String getAttachmentTimeUnit();

	/**
	 * Returns the attachment time min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getAttachmentTimeMin();

	/**
	 * Returns the attachment time max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getAttachmentTimeMax();

	/**
	 * Returns the bean oil unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getBeanOilUnit();

	/**
	 * Returns the bean oil min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getBeanOilMin();

	/**
	 * Returns the bean oil max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getBeanOilMax();

	/**
	 * Returns the benzoic acid unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getBenzoicAcidUnit();

	/**
	 * Returns the benzoic acid min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getBenzoicAcidMin();

	/**
	 * Returns the benzoic acid of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getBenzoicAcidMax();

	/**
	 * Returns the betaine unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getBetaineUnit();

	/**
	 * Returns the betaine min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getBetaineMin();

	/**
	 * Returns the betaine max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getBetaineMax();

	/**
	 * Returns the carvacrol unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getCarvacrolUnit();

	/**
	 * Returns the carvacrol min max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getCarvacrolMin();

	/**
	 * Returns the carvacrol of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getCarvacrolMax();

	/**
	 * Returns the chitosan unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getChitosanUnit();

	/**
	 * Returns the chitosan min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getChitosanMin();

	/**
	 * Returns the chitosan max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getChitosanMax();

	/**
	 * Returns the cinnamaldehyde unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getCinnamaldehydeUnit();

	/**
	 * Returns the cinnamaldehyde min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getCinnamaldehydeMin();

	/**
	 * Returns the cinnamaldehyde max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getCinnamaldehydeMax();

	/**
	 * Returns the citric acid unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getCitricAcidUnit();

	/**
	 * Returns the citric acid min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getCitricAcidMin();

	/**
	 * Returns the citric acid max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getCitricAcidMax();

	/**
	 * Returns the cured of this {@link FSMRTemplate}. If not set, returns null.
	 */
	public Boolean getCured();

	/**
	 * Returns the cut of this {@link FSMRTemplate}. If not set, returns null.
	 */
	public Boolean getCut();

	/**
	 * Returns the desired reduction unit of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public String getDesiredReductionUnit();

	/**
	 * Returns the desired reduction min of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getDesiredReductionMin();

	/**
	 * Returns the desired reduction max of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getDesiredReductionMax();

	/**
	 * Returns the diacetic acid unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getDiaceticAcidUnit();

	/**
	 * Returns the diacetic acid min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getDiaceticAcidMin();

	/**
	 * Returns the diacetic acid max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getDiaceticAcidMax();

	/**
	 * Returns the disaccharide unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getDisaccharideUnit();

	/**
	 * Returns the disaccharide min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getDisaccharideMin();

	/**
	 * Returns the disaccharide max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getDisaccharideMax();

	/**
	 * Returns the dried of this {@link FSMRTemplate}. If not set, returns null.
	 */
	public Boolean getDried();

	/**
	 * Returns the ethanol unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getEthanolUnit();

	/**
	 * Returns the ethanol min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getEthanolMin();

	/**
	 * Returns the ethanol max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getEthanolMax();

	/**
	 * Returns the expInoculated of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Boolean getExpInoculated();

	/**
	 * Returns the fat unit of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public String getFatUnit();

	/**
	 * Returns the fat min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getFatMin();

	/**
	 * Returns the fat max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getFatMax();

	/**
	 * Returns the frozen of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Boolean getFrozen();

	/**
	 * Returns the fructose unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getFructoseUnit();

	/**
	 * Returns the fructose min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getFructoseMin();

	/**
	 * Returns the fructose max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getFructoseMax();

	/**
	 * Returns the getl microstructure unit of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public String getGelMicrostructureUnit();

	/**
	 * Returns the getl microstructure min of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getGelMicrostructureMin();

	/**
	 * Returns the getl microstructure max of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getGelMicrostructureMax();

	/**
	 * Returns the glucose unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getGlucoseUnit();

	/**
	 * Returns the glucose min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getGlucoseMin();

	/**
	 * Returns the glucose max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getGlucoseMax();

	/**
	 * Returns the glycerol unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getGlycerolUnit();

	/**
	 * Returns the glycerol min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getGlycerolMin();

	/**
	 * Returns the glycerol max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getGlycerolMax();

	/**
	 * Returns the green tea ground powder unit of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public String getGreenTeaGroundPowderUnit();

	/**
	 * Returns the green tea ground powder min of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public Double getGreenTeaGroundPowderMin();

	/**
	 * Returns the green tea ground powder max of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public Double getGreenTeaGroundPowderMax();

	/**
	 * Returns the green tea leaf unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getGreenTeaLeafUnit();

	/**
	 * Returns the green tea leaf min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getGreenTeaLeafMin();

	/**
	 * Returns the green tea leaf max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getGreenTeaLeafMax();

	/**
	 * Returns the green tea water extract unit of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public String getGreenTeaWaterExtractUnit();

	/**
	 * Returns the green tea water extract min of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public Double getGreenTeaWaterExtractMin();

	/**
	 * Returns the green tea water extract max of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public Double getGreenTeaWaterExtractMax();

	/**
	 * Returns the heated of this {@link FSMRTemplate} with 'heated'. If not set
	 * returns null.
	 */
	public Boolean getHeated();

	/**
	 * Returns the hexoses unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getHexosesUnit();

	/**
	 * Returns the hexoses min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getHexosesMin();

	/**
	 * Returns the hexoses max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getHexosesMax();

	/**
	 * Returns the indigenous of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Boolean getIndigenous();

	/**
	 * Returns the init level histamine unit of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public String getInitLevelHistamineUnit();

	/**
	 * Returns the init level histamine min of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getInitLevelHistamineMin();

	/**
	 * Returns the init level histamine max of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getInitLevelHistamineMax();

	/**
	 * Returns the init level histidine unit of this {@link FSMRTemplate}. If
	 * not set, returns null.
	 */
	public String getInitLevelHistidineUnit();

	/**
	 * Returns the init level histidine min of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getInitLevelHistidineMin();

	/**
	 * Returns the init level histidine max of this {@link FSMRTemplate}. If not
	 * set, returns null.
	 */
	public Double getInitLevelHistidineMax();

	/**
	 * Returns the injured unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getInjuredUnit();

	/**
	 * Returns the injured min of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getInjuredMin();

	/**
	 * Returns the injured max of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Double getInjuredMax();

	/**
	 * Returns the irradiated of this {@link FSMRTemplate}. If not set, returns
	 * null.
	 */
	public Boolean getIrradiated();

	/**
	 * Returns the irradiation unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getIrradiationUnit();

	/**
	 * Returns the irradiation min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getIrradiationMin();

	/**
	 * Returns the irradiation max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getIrradiationMax();

	/**
	 * Returns the lactic acid unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getLacticAcidUnit();

	/**
	 * Returns the lactic acid min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getLacticAcidMin();

	/**
	 * Returns the lactic acid max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getLacticAcidMax();

	/**
	 * Returns the lactic acid bacteria fermented of this {@link FSMRTemplate}.
	 * If not set, returns null.
	 */
	public Boolean getLacticBacteriaFermented();

	/**
	 * Returns the lauricidin unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getLauricidinUnit();

	/**
	 * Returns the lauricidin min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getLauricidinMin();

	/**
	 * Returns the lauricidin max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getLauricidinMax();

	/**
	 * Returns the malic acid unit of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public String getMalicAcidUnit();

	/**
	 * Returns the malic acid min of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getMalicAcidMin();

	/**
	 * Returns the malic acid max of this {@link FSMRTemplate}. If not set,
	 * returns null.
	 */
	public Double getMalicAcidMax();

	/**
	 * Sets the model name of this {@link FSMRTemplate} with 'modelName'. If not
	 * set, returns null.
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
	 * Sets the organism details of this {@link FSMRTemplate} with
	 * 'organismDetails'.
	 */
	public void setOrganismDetails(String organismDetails);

	/** Sets the matrix name of this {@link FSMRTemplate} with 'matrixName'. */
	public void setMatrixName(String matrixName);

	/**
	 * Sets the matrix details of this {@link FSMRTemplate} with
	 * 'matrixDetails'.
	 */
	public void setMatrixDetails(String matrixDetails);

	/** Sets the creator of this {@link FSMRTemplate} with 'creator'. */
	public void setCreator(String creator);

	/**
	 * Sets the reference description of this {@link FSMRTemplate} with
	 * 'referenceDescription'.
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
	 * Sets the curation status of this {@link FSMRTemplate} with
	 * 'curationStatus'.
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
	 * Sets the dependent variable of this {@link FSMRTemplate} with
	 * 'dependentVariable'.
	 */
	public void setDependentVariable(String dependentVariable);

	/**
	 * Sets the dependent variable unit of this {@link FSMRTemplate} with
	 * 'dependentVariableUnit'.
	 */
	public void setDependentVariableUnit(String dependentVariableUnit);

	/**
	 * Sets the dependent variable min of this {@link FSMRTemplate} with
	 * 'dependentVariableMin'.
	 */
	public void setDependentVariableMin(double dependentVariableMin);

	/**
	 * Sets the dependent variable max of this {@link FSMRTemplate} with
	 * 'dependentVariableMax'.
	 */
	public void setDependentVariableMax(double dependentVariableMax);

	/**
	 * Sets the independent variable of this {@link FSMRTemplate} with
	 * 'independentVariable'.
	 */
	public void setIndependentVariable(String independentVariable);

	/**
	 * Sets the software solution of this {@link FSMRTemplate} with 'software'.
	 */
	public void setSoftware(String software);

	/**
	 * Sets the link of the software solution of this {@link FSMRTemplate} with
	 * 'link'.
	 */
	public void setSoftwareLink(URL link);

	/**
	 * Sets the notes of the software solution of this {@link FSMRTemplate} with
	 * 'notes'.
	 */
	public void setSoftwareNotes(String notes);

	/**
	 * Sets the accesibilty of the software solution of this
	 * {@link FSMRTemplate} with 'softwareAccesibility'.
	 */
	public void setSoftwareAccesibility(String softwareAccesibility);

	/**
	 * Sets the stochastic modeling of the software solution of this
	 * {@link FSMRTemplate} with 'stochasticModeling'
	 */
	public void setSoftwareStochasticModeling(boolean stochasticModeling);

	/**
	 * Sets the prediction conditios of the software solution of this
	 * {@link FSMRTemplate} with 'preditionConditions'.
	 */
	public void setSoftwarePredictionConditions(String predictionConditions);

	/**
	 * Sets the init level unit of this {@link FSMRTemplate} with
	 * 'initLevelUnit'.
	 */
	public void setInitLevelUnit(String initLevelUnit);

	/**
	 * Sets the init level min of this {@link FSMRTemplate} with 'initLevelMin'.
	 */
	public void setInitLevelMin(double initLevelMin);

	/**
	 * Sets the init level max of this {@link FSMRTemplate} with 'initLevelMax'.
	 */
	public void setInitLevelMax(double initLevelMax);

	/** Sets the time unit of this {@link FSMRTemplate} with 'timeUnit'. */
	public void setTimeUnit(String timeUnit);

	/** Sets the time min of this {@link FSMRTemplate} with 'timeMin'. */
	public void setTimeMin(double timeMin);

	/** Sets the time max of this {@link FSMRTemplate} with 'timeMax'. */
	public void setTimeMax(double timeMax);

	/**
	 * Sets the temperature unit of this {@link FSMRTemplate} with
	 * 'temperatureUnit'.
	 */
	public void setTemperatureUnit(String temperatureUnit);

	/**
	 * Sets the temperature min of this {@link FSMRTemplate} with
	 * 'temperatureMin'.
	 */
	public void setTemperatureMin(double temperatureMin);

	/**
	 * Sets the temperature max of this {@link FSMRTemplate} with
	 * 'temperatureMax'.
	 */
	public void setTemperatureMax(double temperatureMax);

	/** Sets the pH unit of this {@link FSMRTemplate} with 'phUnit'. */
	public void setPhUnit(String phUnit);

	/** Sets the pH min of this {@link FSMRTemplate} with 'phMin'. */
	public void setPhMin(double phMin);

	/** Sets the pH max of this {@link FSMRTemplate} with 'phMax'. */
	public void setPhMax(double phMax);

	/**
	 * Sets the water activity unit of this {@link FSMRTemplate} with 'waUnit'.
	 */
	public void setAwUnit(String waUnit);

	/**
	 * Sets the water activity min of this {@link FSMRTemplate} with 'waMin'.
	 */
	public void setAwMin(double waMin);

	/**
	 * Sets the water activity max of this {@link FSMRTemplate} with 'waMax'.
	 */
	public void setAwMax(double waMax);

	/** Sets the NaCl unit of this {@link FSMRTemplate} with 'naclUnit'. */
	public void setNaClUnit(String naclUnit);

	/** Sets the NaCl min of this {@link FSMRTemplate} with 'naclMin'. */
	public void setNaClMin(double naclMin);

	/** Sets the NaCl max of this {@link FSMRTemplate} with 'naclMax'. */
	public void setNaClMax(double naclMax);

	/** Sets the ALTA unit of this {@link FSMRTemplate} with 'altaUnit'. */
	public void setAltaUnit(String altaUnit);

	/** Sets the ALTA min of this {@link FSMRTemplate} with 'altaMin'. */
	public void setAltaMin(double altaMin);

	/** Sets the ALTA max of this {@link FSMRTemplate} with 'altaMax'. */
	public void setAltaMax(double altaMax);

	/** Sets the CO2 unit of this {@link FSMRTemplate} with 'co2Unit'. */
	public void setCO2Unit(String co2Unit);

	/** Sets the CO2 min of this {@link FSMRTemplate} with 'co2Min'. */
	public void setCO2Min(double co2Min);

	/** Sets the CO2 max of this {@link FSMRTemplate} with 'co2Max'. */
	public void setCO2Max(double co2Max);

	/** Sets the ClO2 unit of this {@link FSMRTemplate} with 'clo2Unit'. */
	public void setClO2Unit(String clo2Unit);

	/** Sets the ClO2 min of this {@link FSMRTemplate} with 'clo2Min'. */
	public void setClO2Min(double clo2Min);

	/** Sets the ClO2 max of this {@link FSMRTemplate} with 'clo2Max'. */
	public void setClO2Max(double clo2Max);

	/** Sets the EDTA unit of this {@link FSMRTemplate} with 'edtaUnit'. */
	public void setEdtaUnit(String edtaUnit);

	/** Sets the EDTA min of this {@link FSMRTemplate} with 'edtaMin'. */
	public void setEdtaMin(double edtaMin);

	/** Sets the EDTA max of this {@link FSMRTemplate} with 'edtaMax'. */
	public void setEdtaMax(double edtaMax);

	/** Sets the HCl unit of this {@link FSMRTemplate} with 'hclUnit'. */
	public void setHClUnit(String hclUnit);

	/** Sets the HCl min of this {@link FSMRTemplate} with 'hclMin'. */
	public void setHClMin(double hclMin);

	/** Sets the HCl max of this {@link FSMRTemplate} with 'hclMax'. */
	public void setHClMax(double hclMax);

	/** Sets the N2 unit of this {@link FSMRTemplate} with 'n2Unit'. */
	public void setN2Unit(String n2Unit);

	/** Sets the N2 min of this {@link FSMRTemplate} with 'n2Min'. */
	public void setN2Min(double n2Min);

	/** Sets the N2 max of this {@link FSMRTemplate} with 'n2Max'. */
	public void setN2Max(double n2Max);

	/** Sets the O2 unit of this {@link FSMRTemplate} with 'o2Unit'. */
	public void setO2Unit(String o2Unit);

	/** Sets the O2 min of this {@link FSMRTemplate} with 'o2Min'. */
	public void setO2Min(double o2Min);

	/** Sets the O2 max of this {@link FSMRTemplate} with 'o2Max'. */
	public void setO2Max(double o2Max);

	/**
	 * Sets the acetic acid unit of this {@link FSMRTemplate} with
	 * 'aceticAcidUnit'.
	 */
	public void setAceticAcidUnit(String aceticAcidUnit);

	/**
	 * Sets the acetic acid min of this {@link FSMRTemplate} with
	 * 'aceticAcidMin'.
	 */
	public void setAceticAcidMin(double aceticAcidMin);

	/**
	 * Sets the acetic acid max of this {@link FSMRTemplate} with
	 * 'aceticAcidMax'.
	 */
	public void setAceticAcidMax(double aceticAcidMax);

	/** Sets the additives of this {@link FSMRTemplate} with 'additives'. */
	public void setAdditives(String additives);

	/** Sets the anaerobic of this {@link FSMRTemplate} with 'anaerobic'. */
	public void setAnaerobic(boolean anaerobic);

	/**
	 * Sets the antimicrobial dipping time unit of this {@link FSMRTemplate}
	 * with 'antimicrobialDippingTimeUnit'.
	 */
	public void setAntimicrobialDippingTimeUnit(String antimicrobialDippingTimeUnit);

	/**
	 * Sets the antimicrobial dipping time min of this {@link FSMRTemplate} with
	 * 'antimicrobialDippingTimeMin'.
	 */
	public void setAntimicrobialDippingTimeMin(double antimicrobialDippingTimeMin);

	/**
	 * Sets the antimicrobial dipping time max of this {@link FSMRTemplate} with
	 * 'antimicrobialDippingTimeMax'.
	 */
	public void setAntimicrobialDippingTimeMax(double antimicrobialDippingTimeMax);

	/**
	 * Sets the apple polyphenol time unit of this {@link FSMRTemplate} with
	 * 'applePolyphenolUnit'.
	 */
	public void setApplePolyphenolUnit(String applePolyphenolUnit);

	/**
	 * Sets the apple polyphenol min of this {@link FSMRTemplate} with
	 * 'applePolyphenolMin'.
	 */
	public void setApplePolyphenolMin(double applePolyphenolMin);

	/**
	 * Sets the apple polyphenol max of this {@link FSMRTemplate} with
	 * 'applePolyphenolMax'.
	 */
	public void setApplePolyphenolMax(double applePolyphenolMax);

	/**
	 * Sets the ascorbicc acid unit of this {@link FSMRTemplate} with
	 * 'ascorbiccAcidUnit'.
	 */
	public void setAscorbiccAcidUnit(String ascorbiccAcidUnit);

	/**
	 * Sets the ascorbicc acid min of this {@link FSMRTemplate} with
	 * 'ascorbiccAcidMin'.
	 */
	public void setAscorbiccAcidMin(double ascorbiccAcidMin);

	/**
	 * Sets the ascorbicc acid max of this {@link FSMRTemplate} with
	 * 'ascorbiccAcidMax'.
	 */
	public void setAscorbiccAcidMax(double ascorbiccAcidMax);

	/** Sets the atrInduced of this {@link FSMRTemplate} with 'atrInduced'. */
	public void setAtrInduced(boolean atrInduced);

	/**
	 * Sets the attachment time unit of this {@link FSMRTemplate} with
	 * 'attachmentTimeUnit'.
	 */
	public void setAttachmentTimeUnit(String attachmentTimeUnit);

	/**
	 * Sets the attachment time min of this {@link FSMRTemplate} with
	 * 'attachmentTimeMin'.
	 */
	public void setAttachmentTimeMin(double attachmentTimeMin);

	/**
	 * Sets the attachment time max of this {@link FSMRTemplate} with
	 * 'attachmentTimeMax'.
	 */
	public void setAttachmentTimeMax(double attachmentTimeMax);

	/**
	 * Sets the bean oil unit of this {@link FSMRTemplate} with 'beanOilUnit'.
	 */
	public void setBeanOilUnit(String beanOilUnit);

	/** Sets the bean oil min of this {@link FSMRTemplate} with 'beanOilMin'. */
	public void setBeanOilMin(double beanOilMin);

	/** Sets the bean oil max of this {@link FSMRTemplate} with 'beanOilMax'. */
	public void setBeanOilMax(double beanOilMax);

	/**
	 * Sets the benzoic acid unit of this {@link FSMRTemplate} with
	 * 'benzoicAcicUnit'.
	 */
	public void setBenzoicAcidUnit(String benzoicAcidUnit);

	/**
	 * Sets the benzoic acid min of this {@link FSMRTemplate} with
	 * 'benzoicAcidMin'.
	 */
	public void setBenzoicAcidMin(double benzoidAcidMin);

	/**
	 * Sets the benzoic acid max of this {@link FSMRTemplate} with
	 * 'benzoicAcidMax'.
	 */
	public void setBenzoicAcidMax(double benzoidAcidMax);

	/**
	 * Sets the betaine unit of this {@link FSMRTemplate} with 'betaineUnit'.
	 */
	public void setBetaineUnit(String betaineUnit);

	/** Sets the betaine min of this {@link FSMRTemplate} with 'betaineMin'. */
	public void setBetaineMin(double betaineMin);

	/** Sets the betaine max of this {@link FSMRTemplate} with 'betaineMax'. */
	public void setBetaineMax(double betaineMax);

	/**
	 * Sets the carvacrol unit of this {@link FSMRTemplate} with
	 * 'carvacrolUnit'.
	 */
	public void setCarvacrolUnit(String carvacrolUnit);

	/**
	 * Sets the carvacrol min of this {@link FSMRTemplate} with 'carvacrolMin'.
	 */
	public void setCarvacrolMin(double carvacrolMin);

	/**
	 * Sets the carvacrol max of this {@link FSMRTemplate} with 'carvacrolMax'.
	 */
	public void setCarvacrolMax(double benzoidAcidMax);

	/**
	 * Sets the chitosan unit of this {@link FSMRTemplate} with 'chitosanUnit'.
	 */
	public void setChitosanUnit(String chitosanUnit);

	/**
	 * Sets the chitosan min of this {@link FSMRTemplate} with 'chitosanMin'.
	 */
	public void setChitosanMin(double chitosanMin);

	/**
	 * Sets the chitosan max of this {@link FSMRTemplate} with 'chitosanMax'.
	 */
	public void setChitosanMax(double chitosanMax);

	/**
	 * Sets the cinnamaldehyde time unit of this {@link FSMRTemplate} with
	 * 'cinnamaldehydeUnit'.
	 */
	public void setCinnamaldehydeUnit(String cinnamaldehydeUnit);

	/**
	 * Sets the cinnamaldehyde min of this {@link FSMRTemplate} with
	 * 'cinnamaldehydeMin'.
	 */
	public void setCinnamaldehydeMin(double cinnamaldehydeMin);

	/**
	 * Sets the cinnamaldehyde max of this {@link FSMRTemplate} with
	 * 'cinnamaldehydeMax'.
	 */
	public void setCinnamaldehydeMax(double cinnamaldehydeMax);

	/**
	 * Sets the citric acid time unit of this {@link FSMRTemplate} with
	 * 'citricAcidUnit'.
	 */
	public void setCitricAcidUnit(String citricAcidUnit);

	/**
	 * Sets the citric acid min of this {@link FSMRTemplate} with
	 * 'citricAcidMin'.
	 */
	public void setCitricAcidMin(double citricAcidMin);

	/**
	 * Sets the citric acid max of this {@link FSMRTemplate} with
	 * 'citricAcidMax'.
	 */
	public void setCitricAcidMax(double citricAcidMax);

	/** Sets the cured of this {@link FSMRTemplate} with 'cured'. */
	public void setCured(Boolean cured);

	/** Sets the cut of this {@link FSMRTemplate} with 'cut'. */
	public void setCut(Boolean cut);

	/**
	 * Sets the desired reduction time unit of this {@link FSMRTemplate} with
	 * 'desiredReductionUnit'.
	 */
	public void setDesiredReductionUnit(String desiredReductionUnit);

	/**
	 * Sets the desired reduction min of this {@link FSMRTemplate} with
	 * 'desiredReductionMin'.
	 */
	public void setDesiredReductionMin(double desiredReductionMin);

	/**
	 * Sets the desired reduction max of this {@link FSMRTemplate} with
	 * 'desiredReductionMax'.
	 */
	public void setDesiredReductionMax(double desiredReductionMax);

	/**
	 * Sets the diacetic acid unit of this {@link FSMRTemplate} with
	 * 'diaceticAcidUnit'.
	 */
	public void setDiaceticAcidUnit(String diaceticAcidUnit);

	/**
	 * Sets the diacetic acid min of this {@link FSMRTemplate} with
	 * 'diaceticAcidMin'.
	 */
	public void setDiaceticAcidMin(double diaceticAcidMin);

	/**
	 * Sets the diacetic acid max of this {@link FSMRTemplate} with
	 * 'diaceticAcidMax'.
	 */
	public void setDiaceticAcidMax(double diaceticAcidMax);

	/**
	 * Sets the disaccharide time unit of this {@link FSMRTemplate} with
	 * 'disaccharideUnit'.
	 */
	public void setDisaccharideUnit(String disaccharideUnit);

	/**
	 * Sets the disaccharide min of this {@link FSMRTemplate} with
	 * 'disaccharideMin'.
	 */
	public void setDisaccharideMin(double disaccharideMin);

	/**
	 * Sets the disaccharide max of this {@link FSMRTemplate} with
	 * 'disaccharideMax'.
	 */
	public void setDisaccharideMax(double disaccharideMax);

	/** Sets the dried of this {@link FSMRTemplate} with 'dried'. */
	public void setDried(boolean dried);

	/**
	 * Sets the ethanol time unit of this {@link FSMRTemplate} with
	 * 'ethanolUnit'.
	 */
	public void setEthanolUnit(String ethanolUnit);

	/**
	 * Sets the ethanol min of this {@link FSMRTemplate} with 'ethanolMin'.
	 */
	public void setEthanolMin(double ethanolMin);

	/**
	 * Sets the ethanol max of this {@link FSMRTemplate} with 'ethanolMax'.
	 */
	public void setEthanolMax(double ethanolMax);

	/**
	 * Sets the expInoculated of this {@link FSMRTemplate} with 'expInoculated'.
	 */
	public void setExpInoculated(boolean expInoculated);

	/**
	 * Sets the fat time unit of this {@link FSMRTemplate} with 'fatUnit'.
	 */
	public void setFatUnit(String fatUnit);

	/**
	 * Sets the fat min of this {@link FSMRTemplate} with 'fatMin'.
	 */
	public void setFatMin(double fatMin);

	/**
	 * Sets the fat max of this {@link FSMRTemplate} with 'fatMax'.
	 */
	public void setFatMax(double fatMax);

	/** Sets the frozen of this {@link FSMRTemplate} with 'frozen'. */
	public void setFrozen(boolean frozen);

	/**
	 * Sets the fructose time unit of this {@link FSMRTemplate} with
	 * 'fructoseUnit'.
	 */
	public void setFructoseUnit(String fructoseUnit);

	/**
	 * Sets the fructose min of this {@link FSMRTemplate} with 'fructoseMin'.
	 */
	public void setFructoseMin(double fructoseMin);

	/**
	 * Sets the fructose max of this {@link FSMRTemplate} with 'fructoseMax'.
	 */
	public void setFructoseMax(double fructoseMax);

	/**
	 * Sets the gel microstructure time unit of this {@link FSMRTemplate} with
	 * 'gel microstructureUnit'.
	 */
	public void setGelMicrostructureUnit(String gelMicrostructureUnit);

	/**
	 * Sets the gel microstructure min of this {@link FSMRTemplate} with 'gel
	 * microstructureMin'.
	 */
	public void setGelMicrostructureMin(double gelMicrostructureMin);

	/**
	 * Sets the gel microstructure max of this {@link FSMRTemplate} with 'gel
	 * microstructureMax'.
	 */
	public void setGelMicrostructureMax(double gelMicrostructureMax);

	/**
	 * Sets the glucose time unit of this {@link FSMRTemplate} with
	 * 'glucoseUnit'.
	 */
	public void setGlucoseUnit(String glucoseUnit);

	/**
	 * Sets the glucose min of this {@link FSMRTemplate} with 'glucoseMin'.
	 */
	public void setGlucoseMin(double glucoseMin);

	/**
	 * Sets the glucose max of this {@link FSMRTemplate} with 'glucoseMax'.
	 */
	public void setGlucoseMax(double glucoseMax);

	/**
	 * Sets the glycerol time unit of this {@link FSMRTemplate} with
	 * 'glycerolUnit'.
	 */
	public void setGlycerolUnit(String glycerolUnit);

	/**
	 * Sets the glycerol min of this {@link FSMRTemplate} with 'glycerolMin'.
	 */
	public void setGlycerolMin(double glycerolMin);

	/**
	 * Sets the glycerol max of this {@link FSMRTemplate} with 'glycerolMax'.
	 */
	public void setGlycerolMax(double glycerolMax);

	/**
	 * Sets the greenTeaGroundPowderUnit of this {@link FSMRTemplate} with
	 * 'greenTeaGroundPowderUnit'.
	 */
	public void setGreenTeaGroundPowderUnit(String greenTeaGroundPowderUnit);

	/**
	 * Sets the greenTeaGroundPowderMin of this {@link FSMRTemplate} with
	 * 'greenTeaGroundPowderMin'.
	 */
	public void setGreenTeaGroundPowderMin(double greenTeaGroundPowderMin);

	/**
	 * Sets the greenTeaGroundPowderMax of this {@link FSMRTemplate} with
	 * 'greenTeaGroundPowderMax'.
	 */
	public void setGreenTeaGroundPowderMax(double greenTeaGroundPowderMax);

	/**
	 * Sets the greenTeaLeafUnit of this {@link FSMRTemplate} with
	 * 'greenTeaLeafUnit'.
	 */
	public void setGreenTeaLeafUnit(String greenTeaLeafUnit);

	/**
	 * Sets the greenTeaLeafMin of this {@link FSMRTemplate} with
	 * 'greenTeaLeafMin'.
	 */
	public void setGreenTeaLeafMin(double greenTeaLeafMin);

	/**
	 * Sets the greenTeaLeafMax of this {@link FSMRTemplate} with
	 * 'greenTeaLeafMax'.
	 */
	public void setGreenTeaLeafMax(double greenTeaLeafMax);

	/**
	 * Sets the greenTeaWaterExtractUnit of this {@link FSMRTemplate} with
	 * 'greenTeaWaterExtractUnit'.
	 */
	public void setGreenTeaWaterExtractUnit(String greenTeaWaterExtractUnit);

	/**
	 * Sets the greenTeaWaterExtractMin of this {@link FSMRTemplate} with
	 * 'greenTeaWaterExtractMin'.
	 */
	public void setGreenTeaWaterExtractMin(double greenTeaWaterExtractMin);

	/**
	 * Sets the greenTeaWaterExtractMax of this {@link FSMRTemplate} with
	 * 'greenTeaWaterExtractMax'.
	 */
	public void setGreenTeaWaterExtractMax(double greenTeaWaterExtractMax);

	/** Sets the heated of this {@link FSMRTemplate} with 'heated'. */
	public void setHeated(boolean heated);

	/**
	 * Sets the hexosesUnit of this {@link FSMRTemplate} with 'hexosesUnit'.
	 */
	public void setHexosesUnit(String hexosesUnit);

	/**
	 * Sets the hexosesMin of this {@link FSMRTemplate} with 'hexosesMin'.
	 */
	public void setHexosesMin(double hexosesMin);

	/**
	 * Sets the hexosesMax of this {@link FSMRTemplate} with 'hexosesMax'.
	 */
	public void setHexosesMax(double hexosesMax);

	/** Sets the indigenous of this {@link FSMRTemplate} with 'indigenous'. */
	public void setIndigenous(boolean indigenous);

	/**
	 * Sets the initLevelHistamineUnit of this {@link FSMRTemplate} with
	 * 'initLevelHistamineUnit'.
	 */
	public void setInitLevelHistamineUnit(String initLevelHistamineUnit);

	/**
	 * Sets the initLevelHistamineMin of this {@link FSMRTemplate} with
	 * 'initLevelHistamineMin'.
	 */
	public void setInitLevelHistamineMin(double initLevelHistamineMin);

	/**
	 * Sets the initLevelHistamineMax of this {@link FSMRTemplate} with
	 * 'initLevelHistamineMax'.
	 */
	public void setInitLevelHistamineMax(double initLevelHistamineMax);

	/**
	 * Sets the initLevelHistidineUnit of this {@link FSMRTemplate} with
	 * 'initLevelHistidineUnit'.
	 */
	public void setInitLevelHistidineUnit(String initLevelHistidineUnit);

	/**
	 * Sets the initLevelHistidineMin of this {@link FSMRTemplate} with
	 * 'initLevelHistidineMin'.
	 */
	public void setInitLevelHistidineMin(double initLevelHistidineMin);

	/**
	 * Sets the initLevelHistidineMax of this {@link FSMRTemplate} with
	 * 'initLevelHistidineMax'.
	 */
	public void setInitLevelHistidineMax(double initLevelHistidineMax);

	/**
	 * Sets the injuredUnit of this {@link FSMRTemplate} with 'injuredUnit'.
	 */
	public void setInjuredUnit(String injuredUnit);

	/**
	 * Sets the injuredMin of this {@link FSMRTemplate} with 'injuredMin'.
	 */
	public void setInjuredMin(double injuredMin);

	/**
	 * Sets the injuredMax of this {@link FSMRTemplate} with 'injuredMax'.
	 */
	public void setInjuredMax(double injuredMax);

	/** Sets the irradiated of this {@link FSMRTemplate} with 'irradiated'. */
	public void setIrradiated(boolean irradiated);

	/**
	 * Sets the irradiationUnit of this {@link FSMRTemplate} with
	 * 'irradiationUnit'.
	 */
	public void setIrradiationUnit(String irradiationUnit);

	/**
	 * Sets the irradiationMin of this {@link FSMRTemplate} with
	 * 'irradiationMin'.
	 */
	public void setIrradiationMin(double irradiationMin);

	/**
	 * Sets the irradiationMax of this {@link FSMRTemplate} with
	 * 'irradiationMax'.
	 */
	public void setIrradiationMax(double irradiationMax);

	/**
	 * Sets the lacticAcidUnit of this {@link FSMRTemplate} with
	 * 'lacticAcidUnit'.
	 */
	public void setLacticAcidUnit(String lacticAcidUnit);

	/**
	 * Sets the lacticAcidMin of this {@link FSMRTemplate} with 'lacticAcidMin'.
	 */
	public void setLacticAcidMin(double lacticAcidMin);

	/**
	 * Sets the lacticAcidMax of this {@link FSMRTemplate} with 'lacticAcidMax'.
	 */
	public void setLacticAcidMax(double lacticAcidMax);

	/**
	 * Sets the lactic bacteria fermented of this {@link FSMRTemplate} with
	 * 'lacticBacteriaFermented'.
	 */
	public void setLacticBacteriaFermented(boolean lacticBacteriaFermented);

	/**
	 * Sets the lauricidinUnit of this {@link FSMRTemplate} with
	 * 'lauricidinUnit'.
	 */
	public void setLauricidinUnit(String lauricidinUnit);

	/**
	 * Sets the lauricidinMin of this {@link FSMRTemplate} with 'lauricidinMin'.
	 */
	public void setLauricidinMin(double lauricidinMin);

	/**
	 * Sets the lauricidinMax of this {@link FSMRTemplate} with 'lauricidinMax'.
	 */
	public void setLauricidinMax(double lauricidinMax);

	/**
	 * Sets the malicAcidUnit of this {@link FSMRTemplate} with 'malicAcidUnit'.
	 */
	public void setMalicAcidUnit(String malicAcidUnit);

	/**
	 * Sets the malicAcidMin of this {@link FSMRTemplate} with 'malicAcidMin'.
	 */
	public void setMalicAcidMin(double malicAcidMin);

	/**
	 * Sets the malicAcidMax of this {@link FSMRTemplate} with 'malicAcidMax'.
	 */
	public void setMalicAcidMax(double malicAcidMax);

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

	/** Sets the independent variable value to null. */
	public void unsetIndependentVariable();

	/** Sets the software value to null. */
	public void unsetSoftware();

	/** Sets the software link value to null. */
	public void unsetSoftwareLink();

	/** Sets the software notes value to null. */
	public void unsetSoftwareNotes();

	/** Sets the software accesibility value to null. */
	public void unsetSoftwareAccesibility();

	/** Sets the software stochastic modeling value to null. */
	public void unsetSoftwareStochasticModeling();

	/** Sets the software prediction conditions value to null. */
	public void unsetSoftwarePredictionConditions();

	/** Sets the init level unit value to null. */
	public void unsetInitLevelUnit();

	/** Sets the init level min value to null. */
	public void unsetInitLevelMin();

	/** Sets the init level max value to null. */
	public void unsetInitLevelMax();

	/** Sets the time unit to null. */
	public void unsetTimeUnit();

	/** Sets the time min to null. */
	public void unsetTimeMin();

	/** Sets the time max to null. */
	public void unsetTimeMax();

	/** Sets the temperature unit to null. */
	public void unsetTemperatureUnit();

	/** Sets the temperature min to null. */
	public void unsetTemperatureMin();

	/** Sets the temperature max to null. */
	public void unsetTemperatureMax();

	/** Sets the pH unit to null. */
	public void unsetPhUnit();

	/** Sets the pH min to null. */
	public void unsetPhMin();

	/** Sets the pH max to null. */
	public void unsetPhMax();

	/** Sets the water activity unit to null. */
	public void unsetAwUnit();

	/** Sets the water activity min to null. */
	public void unsetAwMin();

	/** Sets the water activity max to null. */
	public void unsetAwMax();

	/** Sets the NaCl unit to null. */
	public void unsetNaClUnit();

	/** Sets the NaCl min to null. */
	public void unsetNaClMin();

	/** Sets the NaCl max to null. */
	public void unsetNaClMax();

	/** Sets the ALTA unit to null. */
	public void unsetAltaUnit();

	/** Sets the ALTA min to null. */
	public void unsetAltaMin();

	/** Sets the ALTA max to null. */
	public void unsetAltaMax();

	/** Sets the CO2 unit to null. */
	public void unsetCO2Unit();

	/** Sets the CO2 min to null. */
	public void unsetCO2Min();

	/** Sets the CO2 max to null. */
	public void unsetCO2Max();

	/** Sets the ClO2 unit to null. */
	public void unsetClO2Unit();

	/** Sets the ClO2 min to null. */
	public void unsetClO2Min();

	/** Sets the ClO2 max to null. */
	public void unsetClO2Max();

	/** Sets the EDTA unit to null. */
	public void unsetEdtaUnit();

	/** Sets the EDTA min to null. */
	public void unsetEdtaMin();

	/** Sets the EDTA max to null. */
	public void unsetEdtaMax();

	/** Sets the HCl unit to null. */
	public void unsetHClUnit();

	/** Sets the HCl min to null. */
	public void unsetHClMin();

	/** Sets the HCl max to null. */
	public void unsetHClMax();

	/** Sets the N2 unit to null. */
	public void unsetN2Unit();

	/** Sets the N2 min to null. */
	public void unsetN2Min();

	/** Sets the N2 max to null. */
	public void unsetN2Max();

	/** Sets the O2 unit to null. */
	public void unsetO2Unit();

	/** Sets the O2 min to null. */
	public void unsetO2Min();

	/** Sets the O2 max to null. */
	public void unsetO2Max();

	/** Sets the acetic acid unit to null. */
	public void unsetAceticAcidUnit();

	/** Sets the acetic acid min to null. */
	public void unsetAceticAcidMin();

	/** Sets the acetic acid max to null. */
	public void unsetAceticAcidMax();

	/** Sets the additives to null. */
	public void unsetAdditives();

	/** Sets the anaerobic to null. */
	public void unsetAnaerobic();

	/** Sets the antimicrobial dipping time unit to null. */
	public void unsetAntimicrobialDippingTimeUnit();

	/** Sets the antimicrobial dipping time min to null. */
	public void unsetAntimicrobialDippingTimeMin();

	/** Sets the antimicrobial dipping time max to null. */
	public void unsetAntimicrobialDippingTimeMax();

	/** Sets the apple polyphenol unit to null. */
	public void unsetApplePolyphenolUnit();

	/** Sets the apple polyphenol min to null. */
	public void unsetApplePolyphenolMin();

	/** Sets the apple polyphenol max to null. */
	public void unsetApplePolyphenolMax();

	/** Sets the ascorbicc acid unit to null. */
	public void unsetAscorbiccAcidUnit();

	/** Sets the ascorbicc acid min to null. */
	public void unsetAscorbiccAcidMin();

	/** Sets the ascorbicc acid max to null. */
	public void unsetAscorbiccAcidMax();

	/** Sets the atrInduced to null. */
	public void unsetAtrInduced();

	/** Sets the attachment time unit to null. */
	public void unsetAttachmentTimeUnit();

	/** Sets the attachment time min to null. */
	public void unsetAttachmentTimeMin();

	/** Sets the attachment time max to null. */
	public void unsetAttachmentTimeMax();

	/** Sets the bean oil unit to null. */
	public void unsetBeanOilUnit();

	/** Sets the bean oil min to null. */
	public void unsetBeanOilMin();

	/** Sets the bean oil max to null. */
	public void unsetBeanOilMax();

	/** Sets the benzoic acid unit to null. */
	public void unsetBenzoicAcidUnit();

	/** Sets the benzoic acid min to null. */
	public void unsetBenzoicAcidMin();

	/** Sets the benzoic acid max to null. */
	public void unsetBenzoicAcidMax();

	/** Sets the betaine unit to null. */
	public void unsetBetaineUnit();

	/** Sets the betaine min to null. */
	public void unsetBetaineMin();

	/** Sets the betaine max to null. */
	public void unsetBetaineMax();

	/** Sets the carvacrol unit to null. */
	public void unsetCarvacrolUnit();

	/** Sets the carvacrol min to null. */
	public void unsetCarvacrolMin();

	/** Sets the carvacrol max to null. */
	public void unsetCarvacrolMax();

	/** Sets the chitosan unit to null. */
	public void unsetChitosanUnit();

	/** Sets the chitosan min to null. */
	public void unsetChitosanMin();

	/** Sets the chitosan max to null. */
	public void unsetChitosanMax();

	/** Sets the cinnamaldehyde unit to null. */
	public void unsetCinnamaldehydeUnit();

	/** Sets the cinnamaldehyde min to null. */
	public void unsetCinnamaldehydeMin();

	/** Sets the cinnamaldehyde max to null. */
	public void unsetCinnamaldehydeMax();

	/** Sets the citric acid unit to null. */
	public void unsetCitricAcidUnit();

	/** Sets the citric acid min to null. */
	public void unsetCitricAcidMin();

	/** Sets the citric acid max to null. */
	public void unsetCitricAcidMax();

	/** Sets the cured to null. */
	public void unsetCured();

	/** Sets the cut to null. */
	public void unsetCut();

	/** Sets the desired reduction unit to null. */
	public void unsetDesiredReductionUnit();

	/** Sets the desired reduction min to null. */
	public void unsetDesiredReductionMin();

	/** Sets the desired reduction max to null. */
	public void unsetDesiredReductionMax();

	/** Sets the diacetic acid unit to null. */
	public void unsetDiaceticAcidUnit();

	/** Sets the diacetic acid min to null. */
	public void unsetDiaceticAcidMin();

	/** Sets the diacetic acid max to null. */
	public void unsetDiaceticAcidMax();

	/** Sets the disaccharide unit to null. */
	public void unsetDisaccharideUnit();

	/** Sets the disaccharide min to null. */
	public void unsetDisaccharideMin();

	/** Sets the disaccharide max to null. */
	public void unsetDisaccharideMax();

	/** Sets the dried to null. */
	public void unsetDried();

	/** Sets the ethanol unit to null. */
	public void unsetEthanolUnit();

	/** Sets the ethanol min to null. */
	public void unsetEthanolMin();

	/** Sets the ethanol max to null. */
	public void unsetEthanolMax();

	/** Sets the expInoculated to null. */
	public void unsetExpInoculated();

	/** Sets the fat unit to null. */
	public void unsetFatUnit();

	/** Sets the fat min to null. */
	public void unsetFatMin();

	/** Sets the fat max to null. */
	public void unsetFatMax();

	/** Sets the frozen to null. */
	public void unsetFrozen();

	/** Sets the fructose unit to null. */
	public void unsetFructoseUnit();

	/** Sets the fructose min to null. */
	public void unsetFructoseMin();

	/** Sets the fructose max to null. */
	public void unsetFructoseMax();

	/** Sets the gel microstructure unit to null. */
	public void unsetGelMicrostructureUnit();

	/** Sets the gel microstructure min to null. */
	public void unsetGelMicrostructureMin();

	/** Sets the gel microstructure max to null. */
	public void unsetGelMicrostructureMax();

	/** Sets the glucose unit to null. */
	public void unsetGlucoseUnit();

	/** Sets the glucose min to null. */
	public void unsetGlucoseMin();

	/** Sets the glucose max to null. */
	public void unsetGlucoseMax();

	/** Sets the glycerol unit to null. */
	public void unsetGlycerolUnit();

	/** Sets the glycerol min to null. */
	public void unsetGlycerolMin();

	/** Sets the glycerol max to null. */
	public void unsetGlycerolMax();

	/** Sets the green tea ground powder unit to null. */
	public void unsetGreenTeaGroundPowderUnit();

	/** Sets the green tea ground powder min to null. */
	public void unsetGreenTeaGroundPowderMin();

	/** Sets the green tea ground powder max to null. */
	public void unsetGreenTeaGroundPowderMax();

	/** Sets the green tea leaf unit to null. */
	public void unsetGreenTeaLeafUnit();

	/** Sets the green tea leaf min to null. */
	public void unsetGreenTeaLeafMin();

	/** Sets the green tea leaf max to null. */
	public void unsetGreenTeaLeafMax();

	/** Sets the green tea water extract unit to null. */
	public void unsetGreenTeaWaterExtractUnit();

	/** Sets the green tea water extract min to null. */
	public void unsetGreenTeaWaterExtractMin();

	/** Sets the green tea water extract max to null. */
	public void unsetGreenTeaWaterExtractMax();

	/** Sets the heated to null. */
	public void unsetHeated();

	/** Sets the hexoses unit to null. */
	public void unsetHexosesUnit();

	/** Sets the hexoses min to null. */
	public void unsetHexosesMin();

	/** Sets the hexoses max to null. */
	public void unsetHexosesMax();

	/** Sets the indigenous to null. */
	public void unsetIndigenous();

	/** Sets the init level histamine unit to null. */
	public void unsetInitLevelHistamineUnit();

	/** Sets the init level histamine min to null. */
	public void unsetInitLevelHistamineMin();

	/** Sets the init level histamine max to null. */
	public void unsetInitLevelHistamineMax();

	/** Sets the init level histidine unit to null. */
	public void unsetInitLevelHistidineUnit();

	/** Sets the init level histidine min to null. */
	public void unsetInitLevelHistidineMin();

	/** Sets the init level histidine max to null. */
	public void unsetInitLevelHistidineMax();

	/** Sets the injured unit to null. */
	public void unsetInjuredUnit();

	/** Sets the injured min to null. */
	public void unsetInjuredMin();

	/** Sets the injured max to null. */
	public void unsetInjuredMax();

	/** Sets the irradiated to null. */
	public void unsetIrradiated();

	/** Sets the irradiation unit to null. */
	public void unsetIrradiationUnit();

	/** Sets the irradiation min to null. */
	public void unsetIrradiationMin();

	/** Sets the irradiation max to null. */
	public void unsetIrradiationMax();

	/** Sets the lactic acid unit to null. */
	public void unsetLacticAcidUnit();

	/** Sets the lactic acid min to null. */
	public void unsetLacticAcidMin();

	/** Sets the lactic acid max to null. */
	public void unsetLacticAcidMax();

	/** Sets the lacticBacteriaFermented to null. */
	public void unsetLacticBacteriaFermented();

	/** Sets the lauricidin unit to null. */
	public void unsetLauricidinUnit();

	/** Sets the lauricidin min to null. */
	public void unsetLauricidinMin();

	/** Sets the lauricidin max to null. */
	public void unsetLauricidinMax();

	/** Sets the malic acid unit to null. */
	public void unsetMalicAcidUnit();

	/** Sets the malic acid min to null. */
	public void unsetMalicAcidMin();

	/** Sets the malic acid max to null. */
	public void unsetMalicAcidMax();

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
	 * Returns true if the organism details of this {@link FSMRTemplate} are
	 * set.
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

	/**
	 * Returns true if the reference description of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetReferenceDescription();

	/**
	 * Returns true if the reference description link of this
	 * {@link FSMRTemplate} is set.
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
	 * Returns true if the {@link ModelType} of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetModelType();

	/** Returns true if the subject of this {@link FSMRTemplate} is set. */
	public boolean isSetModelSubject();

	/** Returns true if the food process of this {@link FSMRTemplate} is set. */
	public boolean isSetFoodProcess();

	/**
	 * Returns true if the dependent variable of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetDependentVariable();

	/**
	 * Returns true if the dependent variable unit of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetDependentVariableUnit();

	/**
	 * Returns true if the dependent variable min of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetDependentVariableMin();

	/**
	 * Returns true if the dependent variable max of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetDependentVariableMax();

	/**
	 * Returns true if the independent variable of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetIndependentVariable();

	/**
	 * Returns true if the software solution of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetSoftware();

	/**
	 * Returns true if the link of the software solution of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetSoftwareLink();

	/**
	 * Returns true if the notes of the software solution of this
	 * {@link FSMRTemplate} are set.
	 */
	public boolean isSetSoftwareNotes();

	/**
	 * Returns true if the accessibility of the software solution of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetSoftwareAccessibility();

	/**
	 * Returns true if the stochastic modeling of the software solution of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetSoftwareStochasticModeling();

	/**
	 * Returns true if the prediction conditions of the software solution of
	 * this {@link FSMRTemplate} is set.
	 */
	public boolean isSetSoftwarePredictionConditions();

	/**
	 * Returns true if the init level unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetInitLevelUnit();

	/**
	 * Returns true if the init level min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetInitLevelMin();

	/**
	 * Returns true if the init level max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetInitLevelMax();

	/** Returns true if the time unit of this {@link FSMRTemplate} is set. */
	public boolean isSetTimeUnit();

	/** Returns true if the time min of this {@link FSMRTemplate} is set. */
	public boolean isSetTimeMin();

	/** Returns true if the time max of this {@link FSMRTemplate} is set. */
	public boolean isSetTimeMax();

	/**
	 * Retuns true if the temperature unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetTemperatureUnit();

	/**
	 * Returns true if the temperature min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetTemperatureMin();

	/**
	 * Returns true if the temperature max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetTemperatureMax();

	/** Returns true if the pH unit of this {@link FSMRTemplate} is set. */
	public boolean isSetPhUnit();

	/** Returns true if the pH min of this {@link FSMRTemplate} is set. */
	public boolean isSetPhMin();

	/** Returns true if the pH max of this {@link FSMRTemplate} is set. */
	public boolean isSetPhMax();

	/** Returns true if the wa unit of this {@link FSMRTemplate} is set. */
	public boolean isSetAwUnit();

	/** Returns true if the wa min of this {@link FSMRTemplate} is set. */
	public boolean isSetAwMin();

	/** Returns true if the wa max of this {@link FSMRTemplate} is set. */
	public boolean isSetAwMax();

	/** Returns true if the NaCl unit of this {@link FSMRTemplate} is set. */
	public boolean isSetNaClUnit();

	/** Returns true if the NaCl min of this {@link FSMRTemplate} is set. */
	public boolean isSetNaClMin();

	/** Returns true if the NaCl max of this {@link FSMRTemplate} is set. */
	public boolean isSetNaClMax();

	/** Returns true if the ALTA unit of this {@link FSMRTemplate} is set. */
	public boolean isSetAltaUnit();

	/** Returns true if the ALTA min of this {@link FSMRTemplate} is set. */
	public boolean isSetAltaMin();

	/** Returns true if the ALTA max of this {@link FSMRTemplate} is set. */
	public boolean isSetAltaMax();

	/** Returns true if the CO2 unit of this {@link FSMRTemplate} is set. */
	public boolean isSetCO2Unit();

	/** Returns true if the CO2 min of this {@link FSMRTemplate} is set. */
	public boolean isSetCO2Min();

	/** Returns true if the CO2 max of this {@link FSMRTemplate} is set. */
	public boolean isSetCO2Max();

	/** Returns true if the ClO2 unit of this {@link FSMRTemplate} is set. */
	public boolean isSetClO2Unit();

	/** Returns true if the ClO2 min of this {@link FSMRTemplate} is set. */
	public boolean isSetClO2Min();

	/** Returns true if the ClO2 max of this {@link FSMRTemplate} is set. */
	public boolean isSetClO2Max();

	/** Returns true if the EDTA unit of this {@link FSMRTemplate} is set. */
	public boolean isSetEdtaUnit();

	/** Returns true if the EDTA min of this {@link FSMRTemplate} is set. */
	public boolean isSetEdtaMin();

	/** Returns true if the EDTA max of this {@link FSMRTemplate} is set. */
	public boolean isSetEdtaMax();

	/** Returns true if the HCl unit of this {@link FSMRTemplate} is set. */
	public boolean isSetHClUnit();

	/** Returns true if the HCl min of this {@link FSMRTemplate} is set. */
	public boolean isSetHClMin();

	/** Returns true if the HCl max of this {@link FSMRTemplate} is set. */
	public boolean isSetHClMax();

	/** Returns true if the N2 unit of this {@link FSMRTemplate} is set. */
	public boolean isSetN2Unit();

	/** Returns true if the N2 min of this {@link FSMRTemplate} is set. */
	public boolean isSetN2Min();

	/** Returns true if the N2 max of this {@link FSMRTemplate} is set. */
	public boolean isSetN2Max();

	/** Returns true if the O2 unit of this {@link FSMRTemplate} is set. */
	public boolean isSetO2Unit();

	/** Returns true if the O2 min of this {@link FSMRTemplate} is set. */
	public boolean isSetO2Min();

	/** Returns true if the O2 max of this {@link FSMRTemplate} is set. */
	public boolean isSetO2Max();

	/**
	 * Returns true if the acetic acid unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetAceticAcidUnit();

	/**
	 * Returns true if the acetic acid min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetAceticAcidMin();

	/**
	 * Returns true if the acetic acid max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetAceticAcidMax();

	/** Returns true if the additives of this {@link FSMRTemplate} is set. */
	public boolean isSetAdditives();

	/** Returns true if the anaerobic of this {@link FSMRTemplate} is set. */
	public boolean isSetAnaerobic();

	/**
	 * Returns true if the antimicrobial dipping time unit of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetAntimicrobialDippingTimeUnit();

	/**
	 * Returns true if the antimicrobial dipping time min of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetAntimicrobialDippingTimeMin();

	/**
	 * Returns true if the antimicrobial dipping time max of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetAntimicrobialDippingTimeMax();

	/**
	 * Returns true if the apple polyphenol unit of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetApplePolyphenolUnit();

	/**
	 * Returns true if the apple polyphenol min of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetApplePolyphenolMin();

	/**
	 * Returns true if the apple polyphenol max of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetApplePolyphenolMax();

	/**
	 * Returns true if the ascorbicc acid unit of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetAscorbiccAcidUnit();

	/**
	 * Returns true if the ascorbicc acid min of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetAscorbiccAcidMin();

	/**
	 * Returns true if the ascorbicc acid max of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetAscorbiccAcidMax();

	/** Returns true if the atrInduced of this {@link FSMRTemplate} is set. */
	public boolean isSetAtrInduced();

	/**
	 * Returns true if the attachment time unit of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetAttachmentTimeUnit();

	/**
	 * Returns true if the attachment time min of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetAttachmentTimeMin();

	/**
	 * Returns true if the attachment time max of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetAttachmentTimeMax();

	/**
	 * Returns true if the bean oil unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetBeanOilUnit();

	/**
	 * Returns true if the bean oil min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetBeanOilMin();

	/**
	 * Returns true if the bean oil max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetBeanOilMax();

	/**
	 * Returns true if the benzoic oil unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetBenzoicAcidUnit();

	/**
	 * Returns true if the benzoic oil min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetBenzoicAcidMin();

	/**
	 * Returns true if the benzoic oil max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetBenzoicAcidMax();

	/**
	 * Returns true if the betaine unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetBetaineUnit();

	/**
	 * Returns true if the betaine min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetBetaineMin();

	/**
	 * Returns true if the betaine max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetBetaineMax();

	/**
	 * Returns true if the carvacrol unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetCarvacrolUnit();

	/**
	 * Returns true if the carvacrol min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetCarvacrolMin();

	/**
	 * Returns true if the carvacrol max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetCarvacrolMax();

	/**
	 * Returns true if the chitosan unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetChitosanUnit();

	/**
	 * Returns true if the chitosan min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetChitosanMin();

	/**
	 * Returns true if the chitosan max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetChitosanMax();

	/**
	 * Returns true if the cinnamaldehyde unit of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetCinnamaldehydeUnit();

	/**
	 * Returns true if the cinnamaldehyde min of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetCinnamaldehydeMin();

	/**
	 * Returns true if the cinnamaldehyde max of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetCinnamaldehydeMax();

	/**
	 * Returns true if the citric acid unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetCitricAcidUnit();

	/**
	 * Returns true if the citric acid min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetCitricAcidMin();

	/**
	 * Returns true if the citric acid max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetCitricAcidMax();

	/** Returns true if the cured of this {@link FSMRTemplate} is set. */
	public boolean isSetCured();

	/** Returns true if the cut of this {@link FSMRTemplate} is set. */
	public boolean isSetCut();

	/**
	 * Returns true if the desired reduction unit of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetDesiredReductionUnit();

	/**
	 * Returns true if the desired reduction min of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetDesiredReductionMin();

	/**
	 * Returns true if the desired reduction max of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetDesiredReductionMax();

	/**
	 * Returns true if the diacetic acid unit of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetDiaceticAcidUnit();

	/**
	 * Returns true if the diacetic acid min of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetDiaceticAcidMin();

	/**
	 * Returns true if the diacetic acid max of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetDiaceticAcidMax();

	/**
	 * Returns true if the disaccharide unit of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetDisaccharideUnit();

	/**
	 * Returns true if the disaccharide min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetDisaccharideMin();

	/**
	 * Returns true if the disaccharide max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetDisaccharideMax();

	/** Returns true if the dried of this {@link FSMRTemplate} is set. */
	public boolean isSetDried();

	/**
	 * Returns true if the ethanol unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetEthanolUnit();

	/**
	 * Returns true if the ethanol min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetEthanolMin();

	/**
	 * Returns true if the ethanol max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetEthanolMax();

	/**
	 * Returns true if the expInoculated of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetExpInoculated();

	/**
	 * Returns true if the fat unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetFatUnit();

	/**
	 * Returns true if the fat min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetFatMin();

	/**
	 * Returns true if the fat max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetFatMax();

	/** Returns true if the frozen of this {@link FSMRTemplate} is set. */
	public boolean isSetFrozen();

	/**
	 * Returns true if the fructose unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetFructoseUnit();

	/**
	 * Returns true if the fructose min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetFructoseMin();

	/**
	 * Returns true if the fructose max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetFructoseMax();

	/**
	 * Returns true if the gel microstructure unit of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetGelMicrostructureUnit();

	/**
	 * Returns true if the gel microstructure min of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetGelMicrostructureMin();

	/**
	 * Returns true if the gel microstructure max of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetGelMicrostructureMax();

	/**
	 * Returns true if the glucose unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetGlucoseUnit();

	/**
	 * Returns true if the glucose min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetGlucoseMin();

	/**
	 * Returns true if the glucose max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetGlucoseMax();

	/**
	 * Returns true if the glycerol unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetGlycerolUnit();

	/**
	 * Returns true if the glycerol min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetGlycerolMin();

	/**
	 * Returns true if the glycerol max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetGlycerolMax();

	/**
	 * Returns true if the green tea ground powder unit of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetGreenTeaGroundPowderUnit();

	/**
	 * Returns true if the green tea ground powder min of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetGreenTeaGroundPowderMin();

	/**
	 * Returns true if the green tea ground powder max of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetGreenTeaGroundPowderMax();

	/**
	 * Returns true if the green tea leaf unit of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetGreenTeaLeafUnit();

	/**
	 * Returns true if the green tea leaf min of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetGreenTeaLeafMin();

	/**
	 * Returns true if the green tea leaf max of this {@link FSMRTemplate} is
	 * set.
	 */
	public boolean isSetGreenTeaLeafMax();

	/**
	 * Returns true if the green tea water extract unit of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetGreenTeaWaterExtractUnit();

	/**
	 * Returns true if the green tea water extract min of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetGreenTeaWaterExtractMin();

	/**
	 * Returns true if the green tea water extract max of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetGreenTeaWaterExtractMax();

	/** Returns true if the heated of this {@link FSMRTemplate} if set. */
	public boolean isSetHeated();

	/**
	 * Returns true if the hexoses unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetHexosesUnit();

	/**
	 * Returns true if the hexoses min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetHexosesMin();

	/**
	 * Returns true if the hexoses max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetHexosesMax();

	/** Returns true if the indigenous of this {@link FSMRTemplate} is set. */
	public boolean isSetIndigenous();

	/**
	 * Returns true if the init level histamine unit of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetInitLevelHistamineUnit();

	/**
	 * Returns true if the init level histamine min of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetInitLevelHistamineMin();

	/**
	 * Returns true if the init level histamine max of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetInitLevelHistamineMax();

	/**
	 * Returns true if the init level histidine unit of this
	 * {@link FSMRTemplate} is set.
	 */
	public boolean isSetInitLevelHistidineUnit();

	/**
	 * Returns true if the init level histidine min of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetInitLevelHistidineMin();

	/**
	 * Returns true if the init level histidine max of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetInitLevelHistidineMax();

	/**
	 * Returns true if the injured unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetInjuredUnit();

	/**
	 * Returns true if the injured min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetInjuredMin();

	/**
	 * Returns true if the injured max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetInjuredMax();

	/** Returns true if the irrradiated of this {@link FSMRTemplate} is set. */
	public boolean isSetIrradiated();

	/**
	 * Returns true if the irradiation unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetIrradiationUnit();

	/**
	 * Returns true if the irradiation min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetIrradiationMin();

	/**
	 * Returns true if the irradiation max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetIrradiationMax();

	/**
	 * Returns true if the lactic acid unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetLacticAcidUnit();

	/**
	 * Returns true if the lactic acid min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetLacticAcidMin();

	/**
	 * Returns true if the lactic acid max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetLacticAcidMax();

	/**
	 * Returns true if the lacticBacteriaFermented of this {@link FSMRTemplate}
	 * is set.
	 */
	public boolean isSetLacticBacteriaFermented();

	/**
	 * Returns true if the lauricidin unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetLauricidinUnit();

	/**
	 * Returns true if the lauricidin min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetLauricidinMin();

	/**
	 * Returns true if the lauricidin max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetLauricidinMax();

	/**
	 * Returns true if the malic acid unit of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetMalicAcidUnit();

	/**
	 * Returns true if the malic acid min of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetMalicAcidMin();

	/**
	 * Returns true if the malic acid max of this {@link FSMRTemplate} is set.
	 */
	public boolean isSetMalicAcidMax();
}
