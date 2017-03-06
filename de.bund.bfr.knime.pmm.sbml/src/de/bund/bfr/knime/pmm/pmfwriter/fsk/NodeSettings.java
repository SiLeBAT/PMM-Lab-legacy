/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
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
 *************************************************************************************************
 */
package de.bund.bfr.knime.pmm.pmfwriter.fsk;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


class NodeSettings {

  SettingsModelString outSettings = new SettingsModelString("outPath", "");
  SettingsModelString modelName = new SettingsModelString("modelName", "");
  SettingsModelString givenName = new SettingsModelString("creatorGivenName", "");
  SettingsModelString familyName = new SettingsModelString("creatorFamilyName", "");
  SettingsModelString contact = new SettingsModelString("creatorContact", "");
  
  SettingsModelDate createdDate = new SettingsModelDate("createdDate");
  SettingsModelDate modifiedDate = new SettingsModelDate("modifiedDate");
  SettingsModelBoolean isSecondary = new SettingsModelBoolean("isSecondary", false);
  SettingsModelBoolean overwrite = new SettingsModelBoolean("overwrite", true);
  SettingsModelBoolean splitModels = new SettingsModelBoolean("splitModels", false);
  SettingsModelString referenceLink = new SettingsModelString("referenceLink", "");
  SettingsModelString license = new SettingsModelString("license", "");
  SettingsModelString notes = new SettingsModelString("notes", "");

  public void saveSettingsTo(NodeSettingsWO settings) {
  	outSettings.saveSettingsTo(settings);
  	modelName.saveSettingsTo(settings);
  	givenName.saveSettingsTo(settings);
  	familyName.saveSettingsTo(settings);
  	contact.saveSettingsTo(settings);
  	createdDate.saveSettingsTo(settings);
  	modifiedDate.saveSettingsTo(settings);
  	isSecondary.saveSettingsTo(settings);
  	overwrite.saveSettingsTo(settings);
  	splitModels.saveSettingsTo(settings);
  	referenceLink.saveSettingsTo(settings);
  	license.saveSettingsTo(settings);
  	notes.saveSettingsTo(settings);
  }

  public void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
  	outSettings.validateSettings(settings);
  	modelName.validateSettings(settings);
  	givenName.validateSettings(settings);
  	familyName.validateSettings(settings);
  	contact.validateSettings(settings);
  	createdDate.validateSettings(settings);
  	modifiedDate.validateSettings(settings);
  	isSecondary.validateSettings(settings);
  	overwrite.validateSettings(settings);
  	splitModels.validateSettings(settings);
  	referenceLink.validateSettings(settings);
  	license.validateSettings(settings);
  	notes.validateSettings(settings);
  }

  public void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
    outSettings.loadSettingsFrom(settings);
  	modelName.loadSettingsFrom(settings);
  	givenName.loadSettingsFrom(settings);
  	familyName.loadSettingsFrom(settings);
  	contact.loadSettingsFrom(settings);
  	createdDate.loadSettingsFrom(settings);
  	modifiedDate.loadSettingsFrom(settings);
  	isSecondary.loadSettingsFrom(settings);
  	overwrite.loadSettingsFrom(settings);
  	splitModels.loadSettingsFrom(settings);
  	referenceLink.loadSettingsFrom(settings);
  	license.loadSettingsFrom(settings);
  	notes.loadSettingsFrom(settings);
  }
}