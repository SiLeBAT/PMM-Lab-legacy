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
package de.bund.bfr.knime.pmm.fskx.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

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
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.fskx.FSKUtil;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple;
import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;
import de.bund.bfr.knime.pmm.fskx.RUri;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple.KEYS;
import de.bund.bfr.pmf.file.CombineArchiveUtil;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;

public class FSKXReaderNodeModel extends NodeModel {

  // configuration keys
  public static final String CFGKEY_FILE = "filename";

  // defaults for persistent state
  private static final String DEFAULT_FILE = "c:/temp/foo.numl";

  // defaults for persistent state
  private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

  protected FSKXReaderNodeModel() {
    // 0 input ports and 2 input port
    super(0, 2);
  }

  /** {@inheritDoc} */
  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws CombineArchiveException, FileAccessException {

    String filepath = filename.getStringValue();

    /**
     * Try to open the CombineArchive. Should an error occur, a FileAccessException would be thrown.
     */
    CombineArchive combineArchive = CombineArchiveUtil.open(filepath);

    final Set<String> librariesSet = new HashSet<>(); // Set of libraries
    final Set<String> sourcesSet = new HashSet<>(); // Set of sources

    // Gets annotation
    final Element xmlElement = combineArchive.getDescriptions().get(0).getXmlDescription();
    final RMetaDataNode metaDataNode = new RMetaDataNode(xmlElement);

    final List<ArchiveEntry> rEntriesList =
        combineArchive.getEntriesWithFormat(new RUri().createURI());
    final Map<String, ArchiveEntry> rEntriesMap = new HashMap<>(rEntriesList.size());
    for (final ArchiveEntry entry : rEntriesList) {
      rEntriesMap.put(entry.getFileName(), entry);
    }

    /**
     * Looks for model script. Since the model script is mandatory, it closes the CombineArchive and
     * throws a FileAccessException when the model script cannot be retrieved.
     */
    final String modelScriptFileName = metaDataNode.getMainScript();
    String origModelScript; // original model script (with comments)
    String simpModelScript; // simplified model script (with no comments)
    if (modelScriptFileName == null) {
      CombineArchiveUtil.close(combineArchive);
      throw new FileAccessException("Model script could not be accessed");
    } else {
      try {
        origModelScript = getTextFromArchiveEntry(rEntriesMap.get(modelScriptFileName));

        // Extract libraries and sources from the parameters script
        final String[] lines = origModelScript.split("\\r?\\n");
        librariesSet.addAll(FSKUtil.extractLibrariesFromLines(lines));
        sourcesSet.addAll(FSKUtil.extractSourcesFromLines(lines));

        // Creates simplified model script
        simpModelScript = FSKUtil.createSimplifiedScript(lines);
      } catch (IOException e) {
        CombineArchiveUtil.close(combineArchive);
        throw new FileAccessException("Model script could not be accessed");
      }
    }

    /**
     * Looks for parameters script. Since the parameter script is optional, it produces an empty
     * script (empty string) if the parameter script could not be retrieved.
     */
    final String paramScriptFileName = metaDataNode.getParametersScript();
    String origParamScript; // original model script (with comments)
    String simpParamScript; // simplified model script (without comments)
    if (paramScriptFileName == null) {
      origParamScript = simpParamScript = "";
    } else {
      try {
        origParamScript = getTextFromArchiveEntry(rEntriesMap.get(paramScriptFileName));

        // Extract libraries and sources from the parameters script
        final String[] lines = origParamScript.split("\\r?\\n");
        librariesSet.addAll(FSKUtil.extractLibrariesFromLines(lines));
        sourcesSet.addAll(FSKUtil.extractSourcesFromLines(lines));

        // Creates simplified parameters script
        simpParamScript = FSKUtil.createSimplifiedScript(lines);
      } catch (IOException e) {
        origParamScript = simpParamScript = "";
      }
    }

    /**
     * Looks for visualization script. Since the visualization script is optional, it produces an
     * empty script (empty string) if the visualization script could not be retrieved.
     */
    final String vizScriptFileName = metaDataNode.getVisualizationScript();
    String origVisualizationScript; // original visualization script
    String simpVisualizationScript; // simplified visualization script
    if (vizScriptFileName == null) {
      origVisualizationScript = simpVisualizationScript = "";
    } else {
      try {
        origVisualizationScript = getTextFromArchiveEntry(rEntriesMap.get(vizScriptFileName));

        // Extract libraries and sources from the visualization script
        final String[] lines = origVisualizationScript.split("\\r?\\n");
        librariesSet.addAll(FSKUtil.extractLibrariesFromLines(lines));
        sourcesSet.addAll(FSKUtil.extractSourcesFromLines(lines));

        // Creates simplified visualization script
        simpVisualizationScript = FSKUtil.createSimplifiedScript(lines);
      } catch (IOException e) {
        origVisualizationScript = simpVisualizationScript = "";
      }
    }

    /**
     * Process the SBMLDocument with the model meta data. Should an error occur the meta data table
     * will be empty.
     */
    final ArchiveEntry modelEntry =
        combineArchive.getEntriesWithFormat(URIFactory.createPMFURI()).get(0);
    KnimeTuple tuple;
    try {
      final InputStream stream =
          Files.newInputStream(modelEntry.getPath(), StandardOpenOption.READ);
      final SBMLDocument sbmlDoc = new SBMLReader().readSBMLFromStream(stream);
      tuple = FSKUtil.processMetaData(sbmlDoc);
    } catch (IOException | XMLStreamException e) {
      tuple = new KnimeTuple(SchemaFactory.createM1DataSchema());
    }

    CombineArchiveUtil.close(combineArchive);

    // Creates column spec, table spec and container
    final DataColumnSpecCreator[] columnSpecCreators =
        new DataColumnSpecCreator[] {new DataColumnSpecCreator("origModel", StringCell.TYPE),
            new DataColumnSpecCreator("simpModel", StringCell.TYPE),
            new DataColumnSpecCreator("origParams", StringCell.TYPE),
            new DataColumnSpecCreator("simpParams", StringCell.TYPE),
            new DataColumnSpecCreator("origVisualization", StringCell.TYPE),
            new DataColumnSpecCreator("simpVisualization", StringCell.TYPE),
            new DataColumnSpecCreator("RLibraries", StringCell.TYPE),
            new DataColumnSpecCreator("RSources", StringCell.TYPE)};

    final DataColumnSpec[] columnSpec = new DataColumnSpec[columnSpecCreators.length];
    for (int i = 0; i < columnSpecCreators.length; i++) {
      columnSpec[i] = columnSpecCreators[i].createSpec();
    }

    final DataTableSpec tableSpec = new DataTableSpec(columnSpec);
    final BufferedDataContainer dataContainer = exec.createDataContainer(tableSpec);

    // Adds row and closes the container
    EnumMap<FSKXTuple.KEYS, String> valuesMap = new EnumMap<>(FSKXTuple.KEYS.class);
    valuesMap.put(KEYS.ORIG_MODEL, origModelScript); // Adds original model script
    valuesMap.put(KEYS.SIMP_MODEL, simpModelScript); // Adds simplified model script
    valuesMap.put(KEYS.ORIG_PARAM, origParamScript); // Adds original parameters script
    valuesMap.put(KEYS.SIMP_PARAM, simpParamScript); // Adds simplified parameters script
    valuesMap.put(KEYS.ORIG_VIZ, origVisualizationScript); // Adds original visualization script
    valuesMap.put(KEYS.SIMP_VIZ, simpVisualizationScript); // Adds simplified visualization script
    valuesMap.put(KEYS.LIBS, String.join(";", librariesSet)); // Adds R libraries
    valuesMap.put(KEYS.SOURCES, String.join(";", sourcesSet)); // Adds R sources
    final FSKXTuple row = new FSKXTuple(valuesMap);
    dataContainer.addRowToTable(row);
    dataContainer.close();

    // Creates model table spec and container
    final DataTableSpec modelTableSpec = SchemaFactory.createM1DataSchema().createSpec();
    final BufferedDataContainer modelContainer = exec.createDataContainer(modelTableSpec);
    modelContainer.addRowToTable(tuple);
    modelContainer.close();

    return new BufferedDataTable[] {dataContainer.getTable(), modelContainer.getTable()};
  }

  /** {@inheritDoc} */
  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {null, null};
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

  /**
   * Gets text content from an ArchiveEntry of a CombineArchive.
   * 
   * @throws IOException if an I/O error occurs
   */
  private String getTextFromArchiveEntry(final ArchiveEntry entry) throws IOException {
    final InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
    final String text = IOUtils.toString(stream, StandardCharsets.UTF_8);

    return text;
  }

  class FileAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public FileAccessException(final String descr) {
      super(descr);
    }
  }
}
