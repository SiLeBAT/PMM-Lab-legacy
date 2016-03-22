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

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

import de.bund.bfr.knime.pmm.common.ui.BoxFileSelection;

/**
 * <code>NodeDialog</code> for the OpenFSMR Converter node.
 * 
 * @author Miguel Alba
 */
public class OpenFSMRConverterNodeDialog extends NodeDialogPane {

  // models
  private SettingsModelString m_selectedDirectory;
  private final SettingsModelStringArray m_selectedFiles;

  /** New pane for configuring the OpenFSMR Converter node. */
  protected OpenFSMRConverterNodeDialog() {
    super();

    m_selectedDirectory = new SettingsModelString(OpenFSMRConverterNodeModel.CFGKEY_DIR,
        OpenFSMRConverterNodeModel.DEFAULT_DIR);
    m_selectedDirectory.setEnabled(true);
    m_selectedFiles = new SettingsModelStringArray(OpenFSMRConverterNodeModel.CFGKEY_FILES,
        OpenFSMRConverterNodeModel.DEFAULT_FILES);
    m_selectedFiles.setEnabled(true);

    addTab("Selection:",
        new BoxFileSelection(m_selectedDirectory, m_selectedFiles, ".pmf", "PMF files selection"));
    removeTab("Options");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.knime.core.node.NodeDialogPane#loadSettingsFrom(org.knime.core.node. NodeSettingsRO,
   * org.knime.core.data.DataTableSpec[])
   */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs)
      throws NotConfigurableException {
    try {
      m_selectedDirectory.loadSettingsFrom(settings);
      m_selectedFiles.loadSettingsFrom(settings);
    } catch (InvalidSettingsException e) {
      e.printStackTrace();
      throw new NotConfigurableException(e.getMessage(), e.getCause());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.knime.core.node.NodeDialogPane#saveSettingsTo(org.knime.core.node. NodeSettingsWO)
   */
  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    m_selectedDirectory.saveSettingsTo(settings);
    m_selectedFiles.saveSettingsTo(settings);
  }
}
