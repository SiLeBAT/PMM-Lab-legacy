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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.ext.r.node.local.port.RPortObject;

import de.bund.bfr.knime.pmm.fskx.FSKFiles;
import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;
import de.bund.bfr.knime.pmm.fskx.ZipUri;
import de.bund.bfr.pmf.file.uri.RUri;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 */
public class FSKXWriterNodeModel extends NodeModel {

  // Configuration keys
  protected static final String CFG_FILE = "file";
  protected static final String CFG_CREATOR_GIVEN_NAME = "creatorGivenName";
  protected static final String CFG_CREATOR_FAMILY_NAME = "creatorFamilyName";
  protected static final String CFG_CREATOR_CONTACT = "creatorContact";
  protected static final String CFG_CREATED_DATE = "creationDate";
  protected static final String CFG_MODIFIED_DATE = "modifiedDate";
  protected static final String CFG_REFERENCE_LINK = "referenceLink";
  protected static final String CFG_LICENSE = "license";
  protected static final String CFG_NOTES = "notes";

  private SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);
  private SettingsModelString creatorGivenName =
      new SettingsModelString(CFG_CREATOR_GIVEN_NAME, null);
  private SettingsModelString creatorFamilyName =
      new SettingsModelString(CFG_CREATOR_FAMILY_NAME, null);
  private SettingsModelString creatorContact = new SettingsModelString(CFG_CREATOR_CONTACT, null);
  private SettingsModelDate createdDate = new SettingsModelDate(CFG_CREATED_DATE);
  private SettingsModelDate modifiedDate = new SettingsModelDate(CFG_MODIFIED_DATE);
  private SettingsModelString referenceLink = new SettingsModelString(CFG_REFERENCE_LINK, null);
  private SettingsModelString license = new SettingsModelString(CFG_LICENSE, null);
  private SettingsModelString notes = new SettingsModelString(CFG_NOTES, null);

  private static final PortType[] inPortTypes =
      {BufferedDataTable.TYPE, BufferedDataTable.TYPE, RPortObject.TYPE, BufferedDataTable.TYPE};
  private static final PortType[] outPortTypes = {};

  protected FSKXWriterNodeModel() {
    super(inPortTypes, outPortTypes);

    // Sets current date in the dialog components
    long currentDate = Calendar.getInstance().getTimeInMillis();
    this.createdDate.setTimeInMillis(currentDate);
    this.modifiedDate.setTimeInMillis(currentDate);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws FileCreationException If a critical file could not be created. E.g. model script.
   * @throws IOException
   */
  @Override
  protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
    throws CombineArchiveException {
    
    BufferedDataTable rTable = (BufferedDataTable) inData[0];
    BufferedDataTable metaDataTable = (BufferedDataTable) inData[1];
    PortObject rWorkspace = inData[2];
    BufferedDataTable libTable = (BufferedDataTable) inData[3];
    
    FSKFiles files;
    try {
      files = new FSKFiles(rTable, metaDataTable, rWorkspace, libTable);
    } catch (IOException e) {
      throw new CombineArchiveException(e.getMessage());
    }
    
    File archiveFile = new File(this.filePath.getStringValue());
    try {
      Files.deleteIfExists(archiveFile.toPath());
    } catch (IOException e) {
      throw new CombineArchiveException(e.getMessage());
    }
    
    // Try to create CombineArchive
    try (CombineArchive archive = new CombineArchive(archiveFile)) {
      RMetaDataNode metaDataNode = new RMetaDataNode();
      URI rURI = RUri.createURI();
      
      // Adds R model script
      if (files.getModelScript() == null) {
        throw new CombineArchiveException("Missing model script file");
      }
      
      archive.addEntry(files.getModelScript(), "model.R", rURI);
      metaDataNode.setMainScript("model.R");
      
      // Adds R parameters script
      if (files.getParamScript() != null) {
        archive.addEntry(files.getParamScript(), "params.R", rURI);
        metaDataNode.setParamScript("params.R");
      }
      
      // Adds R visualization script
      if (files.getVizScript() != null) {
        archive.addEntry(files.getVizScript(), "visualization.R", rURI);
        metaDataNode.setVisualizationScript("visualization.R");
      }
      
      // Adds R workspace
      if (files.getWorkspace() != null) {
        archive.addEntry(files.getWorkspace(), files.getWorkspace().getName(), rURI);
        metaDataNode.setWorkspaceFile(files.getWorkspace().getName());
      }
      
      // Adds PMF document with meta data
      if (files.getMetaData() != null) {
        archive.addEntry(files.getMetaData(), "metadata.pmf", URIFactory.createPMFURI());
      }
      
      archive.addDescription(new DefaultMetaDataObject(metaDataNode.getNode()));
      
      // Adds R libraries
      URI zipUri = ZipUri.createURI();
      for (Map.Entry<String, File> libEntry : files.getLibs().entrySet()) {
        archive.addEntry(libEntry.getValue(), libEntry.getKey(), zipUri);
      }
      
      archive.pack();
      archive.close();
    } catch (IOException | JDOMException | ParseException | TransformerException e) {
      e.printStackTrace();
    }
    
    return new BufferedDataTable[] {};
  }

  /** {@inheritDoc} */
  @Override
  protected void reset() {
    // does nothing
  }

  /** {@inheritDoc} */
  @Override
  protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
      throws InvalidSettingsException {
    return new PortObjectSpec[] {

    };
  }

  /** {@inheritDoc} */
  @Override
  protected void saveSettingsTo(final NodeSettingsWO settings) {
    this.filePath.saveSettingsTo(settings);
    this.creatorGivenName.saveSettingsTo(settings);
    this.creatorFamilyName.saveSettingsTo(settings);
    this.creatorContact.saveSettingsTo(settings);
    this.createdDate.saveSettingsTo(settings);
    this.modifiedDate.saveSettingsTo(settings);
    this.referenceLink.saveSettingsTo(settings);
    this.license.saveSettingsTo(settings);
    this.notes.saveSettingsTo(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    this.filePath.loadSettingsFrom(settings);
    this.creatorGivenName.loadSettingsFrom(settings);
    this.creatorFamilyName.loadSettingsFrom(settings);
    this.creatorContact.loadSettingsFrom(settings);
    this.createdDate.loadSettingsFrom(settings);
    this.modifiedDate.loadSettingsFrom(settings);
    this.referenceLink.loadSettingsFrom(settings);
    this.license.loadSettingsFrom(settings);
    this.notes.loadSettingsFrom(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    this.filePath.validateSettings(settings);
    this.creatorGivenName.validateSettings(settings);
    this.creatorFamilyName.validateSettings(settings);
    this.creatorContact.validateSettings(settings);
    this.createdDate.validateSettings(settings);
    this.modifiedDate.validateSettings(settings);
    this.referenceLink.validateSettings(settings);
    this.license.validateSettings(settings);
    this.notes.validateSettings(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // nothing
  }

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // nothing
  }

  class FileCreationException extends Exception {
    private static final long serialVersionUID = 1L;

    public FileCreationException(final String descr) {
      super(descr);
    }
  }
}


