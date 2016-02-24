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
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.jdom2.Element;
import org.knime.core.data.DataTableSpec;
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
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.ext.r.node.local.port.RPortObject;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.fskx.FSKUtil;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple.KEYS;
import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;
import de.bund.bfr.knime.pmm.fskx.RScript;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmf.file.CombineArchiveUtil;
import de.bund.bfr.pmf.file.uri.RUri;
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

  private static final PortType[] inPortTypes = {};
  private static final PortType[] outPortTypes =
      {BufferedDataTable.TYPE, BufferedDataTable.TYPE, RPortObject.TYPE};

  protected FSKXReaderNodeModel() {
    // 0 input ports and 3 output ports
    super(inPortTypes, outPortTypes);
  }

  /** {@inheritDoc} */
  @Override
  protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
      throws CombineArchiveException, FileAccessException {

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

    final EnumMap<FSKXTuple.KEYS, String> valuesMap = new EnumMap<>(FSKXTuple.KEYS.class);

    /**
     * Looks for model script. Since the model script is mandatory, it closes the CombineArchive and
     * throws a FileAccessException when the model script cannot be retrieved.
     */
    final String modelScriptFileName = metaDataNode.getMainScript();

    if (modelScriptFileName == null) {
      CombineArchiveUtil.close(combineArchive);
      throw new FileAccessException("Model script could not be accessed");
    }
    try {
      // Extracts model script to file
      File modelScriptFile = File.createTempFile("modelScript", "");
      modelScriptFile.deleteOnExit();
      rEntriesMap.get(modelScriptFileName).extractFile(modelScriptFile);

      RScript modelScript = new RScript(modelScriptFile); // throws IOException

      valuesMap.put(KEYS.ORIG_MODEL, modelScript.getOriginalScript());
      valuesMap.put(KEYS.SIMP_MODEL, modelScript.getSimplifiedScript());

      librariesSet.addAll(modelScript.getLibraries());
      sourcesSet.addAll(modelScript.getSources());
    } catch (IOException e) {
      CombineArchiveUtil.close(combineArchive);
      throw new FileAccessException("Model script could not be accessed");
    }

    /**
     * Looks for parameters script. Since the parameter script is optional, it produces an empty
     * script (empty string) if the parameter script could not be retrieved.
     */
    final String paramScriptFileName = metaDataNode.getParametersScript();

    if (paramScriptFileName == null) {
      valuesMap.put(KEYS.ORIG_PARAM, "");
      valuesMap.put(KEYS.SIMP_PARAM, "");
    } else {
      try {
        // Extracts parameter script to file
        File paramScriptFile = File.createTempFile("paramScript", "");
        paramScriptFile.deleteOnExit();
        rEntriesMap.get(paramScriptFileName).extractFile(paramScriptFile);

        RScript paramScript = new RScript(paramScriptFile); // throws IOException

        valuesMap.put(KEYS.ORIG_PARAM, paramScript.getOriginalScript());
        valuesMap.put(KEYS.SIMP_PARAM, paramScript.getSimplifiedScript());

        librariesSet.addAll(paramScript.getLibraries());
        sourcesSet.addAll(paramScript.getSources());
      } catch (IOException e) {
        valuesMap.put(KEYS.ORIG_PARAM, "");
        valuesMap.put(KEYS.SIMP_PARAM, "");
      }
    }

    /**
     * Looks for visualization script. Since the visualization script is optional, it produces an
     * empty script (empty string) if the visualization script could not be retrieved.
     */
    final String vizScriptFileName = metaDataNode.getVisualizationScript();
    
    if (vizScriptFileName == null) {
      valuesMap.put(KEYS.ORIG_VIZ, "");
      valuesMap.put(KEYS.SIMP_VIZ, "");
    } else {
      try {
        // Extracts parameter script to file
        File vizScriptFile = File.createTempFile("vizFile", "");
        vizScriptFile.deleteOnExit();
        rEntriesMap.get(vizScriptFile).extractFile(vizScriptFile);

        RScript vizScript = new RScript(vizScriptFile); // throws IOException

        valuesMap.put(KEYS.ORIG_PARAM, vizScript.getOriginalScript());
        valuesMap.put(KEYS.SIMP_PARAM, vizScript.getSimplifiedScript());

        librariesSet.addAll(vizScript.getLibraries());
        sourcesSet.addAll(vizScript.getSources());
      } catch (IOException e) {
        valuesMap.put(KEYS.ORIG_VIZ, "");
        valuesMap.put(KEYS.SIMP_VIZ, "");
      }
    }
    
    valuesMap.put(KEYS.LIBS, String.join(";", librariesSet)); // Adds R libraries
    valuesMap.put(KEYS.SOURCES, String.join(";", sourcesSet)); // Adds R sources

    /**
     * Process the SBMLDocument with the model meta data. Should an error occur the meta data table
     * will be empty.
     */
    final ArchiveEntry modelEntry =
        combineArchive.getEntriesWithFormat(URIFactory.createPMFURI()).get(0);
    KnimeTuple metaDataTuple;
    try {
      final InputStream stream =
          Files.newInputStream(modelEntry.getPath(), StandardOpenOption.READ);
      final SBMLDocument sbmlDoc = new SBMLReader().readSBMLFromStream(stream);
      stream.close();
      FSMRTemplate template = FSMRUtils.processPrevalenceModel(sbmlDoc);
      metaDataTuple = FSMRUtils.createTupleFromTemplate(template);
    } catch (IOException | XMLStreamException e) {
      metaDataTuple = new KnimeTuple(new OpenFSMRSchema());
    }

    // Gets R workspace
    RPortObject rPort;
    try {
      final String workspaceFileName = metaDataNode.getWorkspaceFile();
      final File tempFile = File.createTempFile("workspace", "");
      final ArchiveEntry entry = rEntriesMap.get(workspaceFileName);
      entry.extractFile(tempFile);
      rPort = new RPortObject(tempFile);
    } catch (IOException e) {
      throw new FileAccessException("Error accessing R workspace");
    }

    CombineArchiveUtil.close(combineArchive);

    // Creates table spec and container
    final DataTableSpec tableSpec = FSKUtil.createFSKTableSpec();
    final BufferedDataContainer dataContainer = exec.createDataContainer(tableSpec);

    // Adds row and closes the container
    final FSKXTuple row = new FSKXTuple(valuesMap);
    dataContainer.addRowToTable(row);
    dataContainer.close();

    // Creates model table spec and container
    final DataTableSpec modelTableSpec = new OpenFSMRSchema().createSpec();
    final BufferedDataContainer modelContainer = exec.createDataContainer(modelTableSpec);
    modelContainer.addRowToTable(metaDataTuple);
    modelContainer.close();

    return new PortObject[] {dataContainer.getTable(), modelContainer.getTable(), rPort};
  }

  /** {@inheritDoc} */
  @Override
  protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
      throws InvalidSettingsException {
    return new PortObjectSpec[] {null, null, null};
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

  class FileAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public FileAccessException(final String descr) {
      super(descr);
    }
  }
}
