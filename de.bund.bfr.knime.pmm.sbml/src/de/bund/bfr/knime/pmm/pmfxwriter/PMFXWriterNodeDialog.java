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
package de.bund.bfr.knime.pmm.pmfxwriter;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import de.bund.bfr.knime.pmm.common.writer.DialogComponentDate;

/**
 * @author Christian Thoens
 */
public class PMFXWriterNodeDialog extends DefaultNodeSettingsPane {

	private static final String OUT_HISTORY = "Out History";

	protected PMFXWriterNodeDialog() {
		DialogComponentFileChooser outComp = new DialogComponentFileChooser(
				new SettingsModelString(PMFXWriterNodeModel.CFG_OUT_PATH, null), OUT_HISTORY, JFileChooser.SAVE_DIALOG,
				true);
		DialogComponentString nameComp = new DialogComponentString(
				new SettingsModelString(PMFXWriterNodeModel.CFG_MODEL_NAME, null), "File Name");
		DialogComponentString givenNameComp = new DialogComponentString(
				new SettingsModelString(PMFXWriterNodeModel.CFG_CREATOR_GIVEN_NAME, null), "Creator Given Name");
		DialogComponentString familyNameComp = new DialogComponentString(
				new SettingsModelString(PMFXWriterNodeModel.CFG_CREATOR_FAMILY_NAME, null), "Creator Family Name");
		DialogComponentString creatorContactComp = new DialogComponentString(
				new SettingsModelString(PMFXWriterNodeModel.CFG_CREATOR_CONTACT, null), "Creator Contact");

		SettingsModelDate createdDateModel = new SettingsModelDate(PMFXWriterNodeModel.CFG_CREATED_DATE);
		DialogComponentDate createdComp = new DialogComponentDate(createdDateModel, "Created");

		SettingsModelDate modifiedDateModel = new SettingsModelDate(PMFXWriterNodeModel.CFG_LAST_MODIFIED_DATE);
		DialogComponentDate modifiedComp = new DialogComponentDate(modifiedDateModel, "Last Modified");

		SettingsModelBoolean isSecondary = new SettingsModelBoolean(PMFXWriterNodeModel.CFG_ISSECONDARY, false);
		DialogComponentBoolean isSecondaryCheckbox = new DialogComponentBoolean(isSecondary, "Is secondary?");

		SettingsModelBoolean overwrite = new SettingsModelBoolean(PMFXWriterNodeModel.CFG_OVERWRITE, true);
		DialogComponentBoolean overwriteCheckbox = new DialogComponentBoolean(overwrite, "Overwrite, ok?");

		SettingsModelBoolean splitModels = new SettingsModelBoolean(PMFXWriterNodeModel.CFG_SPLITMODELS, false);
		DialogComponentBoolean splitCheckbox = new DialogComponentBoolean(splitModels, "Split top level models?");

		SettingsModelString referenceLink = new SettingsModelString(PMFXWriterNodeModel.CFG_REFERENCE_LINK, null);
		DialogComponentString referenceLinkComp = new DialogComponentString(referenceLink,
				"Model reference description link");

		SettingsModelString license = new SettingsModelString(PMFXWriterNodeModel.CFG_LIC, null);
		DialogComponentString licenseComp = new DialogComponentString(license, "License");

		SettingsModelString notes = new SettingsModelString(PMFXWriterNodeModel.CFG_NOTES, null);
		DialogComponentMultiLineString notesComp = new DialogComponentMultiLineString(notes, "Notes");

		outComp.setBorderTitle("Output Path");

		addDialogComponent(outComp);
		addDialogComponent(nameComp);
		addDialogComponent(isSecondaryCheckbox);
		addDialogComponent(overwriteCheckbox);
		addDialogComponent(splitCheckbox);
		addDialogComponent(givenNameComp);
		addDialogComponent(familyNameComp);
		addDialogComponent(creatorContactComp);
		addDialogComponent(referenceLinkComp);
		addDialogComponent(licenseComp);
		addDialogComponent(notesComp);
		addDialogComponent(createdComp);
		addDialogComponent(modifiedComp);
	}
}
