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
package de.bund.bfr.knime.pmm.fskx.writer;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import de.bund.bfr.knime.pmm.common.writer.DialogComponentDate;

public class FSKXWriterNodeDialog extends DefaultNodeSettingsPane {

  protected FSKXWriterNodeDialog() {

    // File dialog chooser
    final String fileHistoryId = "fileHistory";
    final int dlgType = JFileChooser.SAVE_DIALOG;
    final boolean directoryOnly = false;
    final String validExtensions = ".fskx|.FSKX";

    final SettingsModelString filePath =
        new SettingsModelString(FSKXWriterNodeModel.CFG_FILE, null);
    final DialogComponentFileChooser fileDlg = new DialogComponentFileChooser(filePath,
        fileHistoryId, dlgType, directoryOnly, validExtensions);
    fileDlg.setBorderTitle("Output file");
    addDialogComponent(fileDlg);

    // Creator given name field
    final SettingsModelString givenNameModel =
        new SettingsModelString(FSKXWriterNodeModel.CFG_CREATOR_GIVEN_NAME, null);
    final DialogComponentString givenNameComp =
        new DialogComponentString(givenNameModel, "Creator Given Name");
    addDialogComponent(givenNameComp);

    // Creator family name field
    final SettingsModelString familyNameModel =
        new SettingsModelString(FSKXWriterNodeModel.CFG_CREATOR_FAMILY_NAME, null);
    final DialogComponentString familyNameComp =
        new DialogComponentString(familyNameModel, "Family Given");
    addDialogComponent(familyNameComp);

    // Creator contact field
    final SettingsModelString creatorContactModel =
        new SettingsModelString(FSKXWriterNodeModel.CFG_CREATOR_CONTACT, null);
    final DialogComponentString creatorContactComp =
        new DialogComponentString(creatorContactModel, "Creator contact");
    addDialogComponent(creatorContactComp);

    // Created date model field
    final SettingsModelDate createdDateModel =
        new SettingsModelDate(FSKXWriterNodeModel.CFG_CREATED_DATE);
    final DialogComponentDate createdDateComp =
        new DialogComponentDate(createdDateModel, "Created");
    addDialogComponent(createdDateComp);

    // Modified date model field
    final SettingsModelDate modifiedDateModel =
        new SettingsModelDate(FSKXWriterNodeModel.CFG_MODIFIED_DATE);
    final DialogComponentDate modifiedDateComp =
        new DialogComponentDate(modifiedDateModel, "Modified");
    addDialogComponent(modifiedDateComp);

    // Reference link field
    final SettingsModelString referenceLinkModel =
        new SettingsModelString(FSKXWriterNodeModel.CFG_REFERENCE_LINK, null);
    final DialogComponentString referenceLinkComp =
        new DialogComponentString(referenceLinkModel, "Model reference description link");
    addDialogComponent(referenceLinkComp);

    // License field
    final SettingsModelString licenseModel =
        new SettingsModelString(FSKXWriterNodeModel.CFG_LICENSE, null);
    final DialogComponentString licenseComp = new DialogComponentString(licenseModel, "License");
    addDialogComponent(licenseComp);

    // Notes field
    final SettingsModelString notesModel =
        new SettingsModelString(FSKXWriterNodeModel.CFG_NOTES, null);
    final DialogComponentString notesComp = new DialogComponentString(notesModel, "Notes");
    addDialogComponent(notesComp);
  }
}
