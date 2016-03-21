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
 */
package de.bund.bfr.knime.pmm.fskx.r2fsk;

import javax.swing.Box;
import javax.swing.JFileChooser;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

public class R2FSKNodeDialog extends NodeDialogPane {

  // models
  private final SettingsModelString m_modelScript;
  private final SettingsModelString m_paramScript;
  private final SettingsModelString m_visualizationScript;
  private final SettingsModelString m_metaDataDoc;
  private final SettingsModelString m_libDirectory;
  private final SettingsModelStringArray m_selectedLibs;


  private static final int dialogType = JFileChooser.OPEN_DIALOG; // type of the dialogs

  protected R2FSKNodeDialog() {

    super();

    m_modelScript = new SettingsModelString(R2FSKNodeModel.CFGKEY_MODEL_SCRIPT, "");
    m_paramScript = new SettingsModelString(R2FSKNodeModel.CFGKEY_PARAM_SCRIPT, "");
    m_visualizationScript = new SettingsModelString(R2FSKNodeModel.CFGKEY_VISUALIZATION_SCRIPT, "");
    m_metaDataDoc = new SettingsModelString(R2FSKNodeModel.CFGKEY_SPREADSHEET, "");
    m_libDirectory = new SettingsModelString(R2FSKNodeModel.CFGKEY_DIR_LIBS, "");
    m_selectedLibs = new SettingsModelStringArray(R2FSKNodeModel.CFGKEY_LIBS, new String[0]);

    // Creates the GUI
    Box box = Box.createHorizontalBox();
    box.add(createFilesSelection());
    // box.add(createLibsSelection());
    box.add(new BoxFileSelection(m_libDirectory, m_selectedLibs, ".zip", "R libraries selection"));
    addTab("Selection", box);
    removeTab("Options");
  }

  /** Creates Box to select R scripts and spreadsheet with meta data. */
  private Box createFilesSelection() {
    String rFilters = ".r|.R"; // Extension filters for the R scripts

    DialogComponentFileChooser modelScriptChooser =
        new DialogComponentFileChooser(m_modelScript, "modelScript-history", dialogType, rFilters);
    modelScriptChooser.setBorderTitle("Select model script");

    DialogComponentFileChooser paramScriptChooser =
        new DialogComponentFileChooser(m_paramScript, "paramScript-history", dialogType, rFilters);
    paramScriptChooser.setBorderTitle("Select param script");

    DialogComponentFileChooser vizScriptChooser = new DialogComponentFileChooser(
        m_visualizationScript, "vizScript-history", dialogType, rFilters);
    vizScriptChooser.setBorderTitle("Select visualization script");

    DialogComponentFileChooser metaDataChooser =
        new DialogComponentFileChooser(m_metaDataDoc, "metaData-history", dialogType);
    metaDataChooser.setBorderTitle("Select spreadsheet");

    Box box = Box.createVerticalBox();
    box.add(modelScriptChooser.getComponentPanel());
    box.add(paramScriptChooser.getComponentPanel());
    box.add(vizScriptChooser.getComponentPanel());
    box.add(metaDataChooser.getComponentPanel());

    return box;
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
      throws NotConfigurableException {
    try {
      m_modelScript.loadSettingsFrom(settings);
      m_paramScript.loadSettingsFrom(settings);
      m_visualizationScript.loadSettingsFrom(settings);
      m_metaDataDoc.loadSettingsFrom(settings);
      m_libDirectory.loadSettingsFrom(settings);
      m_selectedLibs.loadSettingsFrom(settings);
    } catch (InvalidSettingsException e) {
      throw new NotConfigurableException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    m_modelScript.saveSettingsTo(settings);
    m_paramScript.saveSettingsTo(settings);
    m_visualizationScript.saveSettingsTo(settings);
    m_metaDataDoc.saveSettingsTo(settings);
    m_libDirectory.saveSettingsTo(settings);
    m_selectedLibs.saveSettingsTo(settings);
  }
}
