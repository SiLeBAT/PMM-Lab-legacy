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
    final File caFile = new File(filePath.getStringValue());
    if (caFile.exists()) {
      caFile.delete();
    }

    // The input of the writer currently can be any table generated in KNIME that has the model
    // script and the visualization script as the 1st and 2nd columns respectively. However, it
    // is desirable to use FSKXTuple in the future.
    final DataRow row = inData[0].iterator().next();
    final StringCell modelCell = (StringCell) row.getCell(1);
    final StringCell visualizationScriptCell = (StringCell) row.getCell(0);

    // Creates file with the R model
    final File rFile = File.createTempFile("modelFile", "");
    rFile.deleteOnExit();
    final FileWriter modelFileWriter = new FileWriter(rFile);
    modelFileWriter.write(modelCell.getStringValue());
    modelFileWriter.close();

    // Creates file with the visualization script
    final File visualizationFile = File.createTempFile("vizFile", "");
    visualizationFile.deleteOnExit();
    final FileWriter vizFileWriter = new FileWriter(visualizationFile);
    vizFileWriter.write(visualizationScriptCell.getStringValue());
    vizFileWriter.close();

    final String mainScript = "model.R";
    final String visualizationScript = "visualization.R";

    // Creates CombineArchive and adds entries
    final URI rURI = new RUri().createURI();
    CombineArchive ca = new CombineArchive(new File(filePath.getStringValue()));
    ca.addEntry(rFile, mainScript, rURI);
    ca.addEntry(visualizationFile, visualizationScript, rURI);

    final Element node = new RMetaDataNode(mainScript, visualizationScript).getNode();
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
