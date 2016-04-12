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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.jdom2.JDOMException;
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
import org.knime.core.util.FileUtil;
import org.knime.ext.r.node.local.port.RPortObject;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.xml.stax.SBMLReader;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.fskx.MissingValueError;
import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;
import de.bund.bfr.knime.pmm.fskx.ZipUri;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplateImpl;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;

public class FskxReaderNodeModel extends NodeModel {

  // configuration keys
  public static final String CFGKEY_FILE = "filename";

  // defaults for persistent state
  private static final String DEFAULT_FILE = "c:/temp/foo.numl";

  // defaults for persistent state
  private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

  private static final PortType[] inPortTypes = {};
  private static final PortType[] outPortTypes = {FskPortObject.TYPE, RPortObject.TYPE};

  protected FskxReaderNodeModel() {
    super(inPortTypes, outPortTypes);
  }

  /**
   * {@inheritDoc}
   *
   * @throws MissingValueError
   */
  @Override
  protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
      throws CombineArchiveException, FileAccessException, MissingValueError {

    String model = "";
    String param = "";
    String viz = "";
    FSMRTemplate template = new FSMRTemplateImpl();
    File workspaceFile = null;
    Set<File> libs = new HashSet<>();

    File archiveFile = new File(filename.getStringValue());
    try (CombineArchive archive = new CombineArchive(archiveFile)) {
      // Gets annotation
      RMetaDataNode node = new RMetaDataNode(archive.getDescriptions().get(0).getXmlDescription());

      // Gets model script
      if (node.getMainScript() != null) {
        ArchiveEntry entry = archive.getEntry(node.getMainScript());
        model = loadScriptFromEntry(entry);
      }

      // Gets parameters script
      if (node.getParametersScript() != null) {
        ArchiveEntry entry = archive.getEntry(node.getParametersScript());
        param = loadScriptFromEntry(entry);
      }

      // Gets visualization script
      if (node.getVisualizationScript() != null) {
        ArchiveEntry entry = archive.getEntry(node.getVisualizationScript());
        viz = loadScriptFromEntry(entry);
      }

      // Gets workspace file
      if (node.getWorkspaceFile() != null) {
        ArchiveEntry entry = archive.getEntry(node.getWorkspaceFile());
        workspaceFile = FileUtil.createTempFile("workspace", ".r");
        entry.extractFile(workspaceFile);
      }

      // Gets model meta data
      URI pmfUri = URIFactory.createPMFURI();
      if (archive.getNumEntriesWithFormat(pmfUri) == 1) {
        ArchiveEntry entry = archive.getEntriesWithFormat(pmfUri).get(0);
        File f = FileUtil.createTempFile("metaData", ".pmf");
        entry.extractFile(f);

        SBMLDocument doc = new SBMLReader().readSBML(f);
        template = FSMRUtils.processPrevalenceModel(doc);
      }

      // Gets R libraries
      URI zipUri = ZipUri.createURI();

      File libDir = FileUtil.createTempDir("libs");
      for (ArchiveEntry entry : archive.getEntriesWithFormat(zipUri)) {
        String libName = entry.getFileName();
        // Create file
        File f = new File(libDir, libName);
        f.deleteOnExit();
        // Extract library into f
        entry.extractFile(f);
        libs.add(f);
      }
    } catch (IOException | JDOMException | ParseException | XMLStreamException e) {
      e.printStackTrace();
    }

    FskPortObject fskObj = new FskPortObject(model, param, viz, template, workspaceFile, libs);
    RPortObject rObj = new RPortObject(fskObj.getWorkspaceFile());

    return new PortObject[] {fskObj, rObj};
  }

  private String loadScriptFromEntry(final ArchiveEntry entry) throws IOException {
    // Create temporary file with a random name. The name does not matter, since the file will be
    // deleted by KNIME itself.
    File f = FileUtil.createTempFile("script", ".r");
    entry.extractFile(f);

    // Read script from f and return script
    FileInputStream fis = new FileInputStream(f);
    String script = IOUtils.toString(fis, "UTF-8");
    fis.close();

    return script;
  }

  /** {@inheritDoc} */
  @Override
  protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
      throws InvalidSettingsException {
    return new PortObjectSpec[] {null, null};
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
      throws IOException, CanceledExecutionException {
    // nothing
  }

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // nothing
  }

  /** {@inheritDoc} */
  @Override
  protected void reset() {
    // does nothing
  }

  class FileAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public FileAccessException(final String descr) {
      super(descr);
    }
  }
}
