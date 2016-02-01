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
package fskx.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import org.jdom2.Element;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import fskx.RMetaDataNode;
import fskx.RUri;

/**
 */
public class FSKXWriterNodeModel extends NodeModel {

  protected static final String CFG_FILE = "file";

  private SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);

  protected FSKXWriterNodeModel() {
    super(1, 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws Exception {

    // Creates file for the CombineArchive
    File caFile = new File(filePath.getStringValue());
    if (caFile.exists()) {
      caFile.delete();
    }

    DataRow row = inData[0].iterator().next();
    StringCell modelCell = (StringCell) row.getCell(1);
    StringCell visualizationScriptCell = (StringCell) row.getCell(0);

    // Creates file with the R model
    File rFile = File.createTempFile("rFile", "");
    rFile.deleteOnExit();
    FileWriter fileWriter = new FileWriter(rFile);
    fileWriter.write(modelCell.getStringValue());
    fileWriter.close();

    // Creates file with the visualization script
    File visualizationFile = File.createTempFile("vizFile", "");
    visualizationFile.deleteOnExit();
    fileWriter = new FileWriter(visualizationFile);
    fileWriter.write(visualizationScriptCell.getStringValue());
    fileWriter.close();

    final String mainScript = "model.R";
    final String visualizationScript = "visualization.R";
    final Element node = new RMetaDataNode(mainScript, visualizationScript).getNode();

    // Creates CombineArchive and adds entries
    final URI rURI = new RUri().createURI();
    CombineArchive ca = new CombineArchive(new File(filePath.getStringValue()));
    ca.addEntry(rFile, mainScript, rURI);
    ca.addEntry(visualizationFile, visualizationScript, rURI);

    ca.addDescription(new DefaultMetaDataObject(node));
    ca.pack();
    ca.close();

    return new BufferedDataTable[] {};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void reset() {}

  /**
   * {@inheritDoc}
   */
  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveSettingsTo(final NodeSettingsWO settings) {
    filePath.saveSettingsTo(settings);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    filePath.loadSettingsFrom(settings);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    filePath.validateSettings(settings);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void loadInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}
}
