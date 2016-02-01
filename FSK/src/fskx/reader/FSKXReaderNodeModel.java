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
package fskx.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jdom2.Element;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import fskx.FSKXTuple;
import fskx.RMetaDataNode;
import fskx.RUri;

public class FSKXReaderNodeModel extends NodeModel {

  // configuration keys
  public static final String CFGKEY_FILE = "filename";

  // defaults for persistent state
  private static final String DEFAULT_FILE = "c:/temp/foo.numl";

  // defaults for persistent state
  private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

  protected FSKXReaderNodeModel() {
    // 0 input ports and 1 input port
    super(0, 1);
  }

  /** {@inheritDoc} */
  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws Exception {

    String filepath = filename.getStringValue();
    CombineArchive ca = new CombineArchive(new File(filepath));

    // Gets annotation
    final Element xmlElement = ca.getDescriptions().get(0).getXmlDescription();
    final RMetaDataNode metaDataNode = new RMetaDataNode(xmlElement);

    final String mainScript = metaDataNode.getMainScript();
    final String vizScript = metaDataNode.getVisualizationScript();

    List<ArchiveEntry> rEntries = ca.getEntriesWithFormat(new RUri().createURI());

    String mainScriptString = "";
    String vizScriptString = "";
    for (ArchiveEntry entry : rEntries) {
      if (entry.getFileName().equals(mainScript)) {
        // Deals with main script
        final InputStream mainScriptStream =
            Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
        mainScriptString = IOUtils.toString(mainScriptStream, "UTF-8");
      } else if (entry.getFileName().equals(vizScript)) {
        // Deals with viz script
        final InputStream vizScriptStream =
            Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
        vizScriptString = IOUtils.toString(vizScriptStream, "UTF-8");
      }
    }
    ca.close();

    // Creates column spec, table spec and container
    final DataColumnSpecCreator mainScriptSpecCreator =
        new DataColumnSpecCreator("RModel", StringCell.TYPE);
    final DataColumnSpecCreator vizScriptSpecCreator =
        new DataColumnSpecCreator("Visualization", StringCell.TYPE);
    final DataColumnSpec[] colSpec = new DataColumnSpec[] {mainScriptSpecCreator.createSpec(),
        vizScriptSpecCreator.createSpec()};
    final DataTableSpec tableSpec = new DataTableSpec(colSpec);
    final BufferedDataContainer dataContainer = exec.createDataContainer(tableSpec);

    // Adds row and closes the container
    final FSKXTuple row = new FSKXTuple(mainScriptString, vizScriptString);
    dataContainer.addRowToTable(row);
    dataContainer.close();

    return new BufferedDataTable[] {dataContainer.getTable()};
  }

  /** {@inheritDoc} */
  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {null};
  }

  /** {@inheritDoc} */
  @Override
  protected void saveSettingsTo(final NodeSettingsWO settings) {
    filename.saveSettingsTo(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    filename.loadSettingsFrom(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    filename.validateSettings(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {};

  /** {@inheritDoc} */
  @Override
  protected void reset() {}

}
