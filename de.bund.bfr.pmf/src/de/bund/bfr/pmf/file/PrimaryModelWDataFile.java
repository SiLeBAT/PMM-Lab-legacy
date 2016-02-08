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
package de.bund.bfr.pmf.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.jdom2.Element;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.xml.XMLNode;
import org.xml.sax.SAXException;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.NuMLReader;
import de.bund.bfr.pmf.numl.NuMLWriter;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * @author Miguel Alba
 */
public class PrimaryModelWDataFile {

  private static final URI SBML_URI = URIFactory.createSBMLURI();
  private static final URI NUML_URI = URIFactory.createNuMLURI();
  private static final URI PMF_URI = URIFactory.createPMFURI();

  private static final SBMLReader READER = new SBMLReader();
  private static final SBMLWriter WRITER = new SBMLWriter();

  public static List<PrimaryModelWData> readPMF(String filename) throws CombineArchiveException {
    return read(filename, SBML_URI);
  }

  public static List<PrimaryModelWData> readPMFX(String filename) throws CombineArchiveException {
    return read(filename, PMF_URI);
  }

  /**
   * Writes experiments to PrimaryModelWDataFile.
   */
  public static void writePMF(String dir, String filename, List<PrimaryModelWData> models)
      throws Exception {
    String caName = String.format("%s/%s.pmf", dir, filename);
    write(caName, SBML_URI, models);
  }

  /**
   * Writes experiments to PrimaryModelWDataFile.
   */
  public static void writePMFX(String dir, String filename, List<PrimaryModelWData> models)
      throws Exception {
    String caName = String.format("%s/%s.pmfx", dir, filename);
    write(caName, PMF_URI, models);
  }

  // private static List<PrimaryModelWData> read(String filename, URI modelURI) {

  // List<PrimaryModelWData> models = new LinkedList<>();

  // // Creates CombineArchive
  // CombineArchive ca = new CombineArchive(new File(filename));

  // // Gets data entries
  // List<ArchiveEntry> dataEntries = ca.getEntriesWithFormat(NUML_URI);
  // HashMap<String, ArchiveEntry> dataEntriesMap = new HashMap<>(dataEntries.size());
  // for (ArchiveEntry dataEntry : dataEntries) {
  // dataEntriesMap.put(dataEntry.getFileName(), dataEntry);
  // }

  // // Parse models in the PMF file
  // List<ArchiveEntry> modelEntries = ca.getEntriesWithFormat(modelURI);
  // for (ArchiveEntry modelEntry : modelEntries) {

  // InputStream stream = Files.newInputStream(modelEntry.getPath(), StandardOpenOption.READ);
  // SBMLDocument sbmlDoc = READER.readSBMLFromStream(stream);
  // stream.close();

  // String sbmlDocName = modelEntry.getFileName();

  // // Parse data
  // PrimaryModelWData model;
  // XMLNode modelAnnotation = sbmlDoc.getModel().getAnnotation().getNonRDFannotation();
  // if (modelAnnotation == null) {
  // model = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
  // } else {
  // XMLNode metadataNode = modelAnnotation.getChildElement("metadata", "");
  // XMLNode node = metadataNode.getChildElement("dataSource", "");

  // // this model has no data
  // if (node == null) {
  // model = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
  // } else {
  // DataSourceNode dataSourceNode = new DataSourceNode(node);
  // String dataFileName = dataSourceNode.getFile();
  // ArchiveEntry dataEntry = dataEntriesMap.get(dataFileName);
  // if (dataEntry == null) {
  // model = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
  // } else {
  // stream = Files.newInputStream(dataEntry.getPath(), StandardOpenOption.READ);
  // NuMLDocument numlDoc = NuMLReader.read(stream);
  // stream.close();
  // model = new PrimaryModelWData(sbmlDocName, sbmlDoc, dataFileName, numlDoc);
  // }
  // }
  // }
  // models.add(model);
  // }

  // ca.close();
  // return models;
  // }

  /**
   * Reads primary models with data from a PMF or PMFX file. FAulty models are skipped.
   *
   * @param filename
   * @return List of primary models with data
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static List<PrimaryModelWData> read(final String filename, final URI modelURI)
      throws CombineArchiveException {

    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final List<PrimaryModelWData> models = new LinkedList<>();

    // Gets data entries
    final List<ArchiveEntry> dataEntriesList = combineArchive.getEntriesWithFormat(NUML_URI);
    final Map<String, ArchiveEntry> dataEntriesMap = new HashMap<>(dataEntriesList.size());
    for (final ArchiveEntry dataEntry : dataEntriesList) {
      dataEntriesMap.put(dataEntry.getFileName(), dataEntry);
    }

    // Parses models in the combineArchive
    final List<ArchiveEntry> modelEntries = combineArchive.getEntriesWithFormat(modelURI);
    for (final ArchiveEntry modelEntry : modelEntries) {
      final String modelDocName = modelEntry.getFileName();

      try {
        // Reads model
        final InputStream modelStream =
            Files.newInputStream(modelEntry.getPath(), StandardOpenOption.READ);
        final SBMLDocument modelDoc = READER.readSBMLFromStream(modelStream);

        //
        final Annotation modelAnnot = modelDoc.getModel().getAnnotation();
        if (!modelAnnot.isSetNonRDFannotation()) {
          System.err.println(modelDocName + " missing data");
          continue;
        }

        final XMLNode metaDataNode =
            modelAnnot.getNonRDFannotation().getChildElement("metadata", "");
        final XMLNode dataSourceRawNode = metaDataNode.getChildElement("dataSource", "");
        if (dataSourceRawNode == null) {
          System.err.println(modelDocName + " missing data");
          continue;
        }

        final DataSourceNode dataSourceNode = new DataSourceNode(dataSourceRawNode);
        final String dataDocName = dataSourceNode.getFile();
        if (!dataEntriesMap.containsKey(dataDocName)) {
          System.err.println(modelDocName + " missing data");
          continue;
        }

        final ArchiveEntry dataEntry = dataEntriesMap.get(dataDocName);
        final InputStream dataStream =
            Files.newInputStream(dataEntry.getPath(), StandardOpenOption.READ);
        final NuMLDocument dataDoc = NuMLReader.read(dataStream);
        dataStream.close();

        models.add(new PrimaryModelWData(modelDocName, modelDoc, dataDocName, dataDoc));
      } catch (IOException | XMLStreamException | ParserConfigurationException | SAXException e) {
        System.err.println(modelDocName + " could not be retrieved");
        e.printStackTrace();
      }
    }

    CombineArchiveUtil.close(combineArchive);

    return models;
  }


  // private static void write(String filename, URI modelURI, List<PrimaryModelWData> models) throws
  // Exception {
  //
  // // Removes previous CombineArchive if it exists
  // File fileTmp = new File(filename);
  // if (fileTmp.exists()) {
  // fileTmp.delete();
  // }
  //
  // // Creates new CombineArchive
  // CombineArchive ca = new CombineArchive(new File(filename));
  //
  // // Add models and data
  // for (PrimaryModelWData model : models) {
  // // Adds data set
  // if (model.getDataDoc() != null) {
  // // Creates tmp file for the model
  // File numlTmp = File.createTempFile("temp2", "");
  // numlTmp.deleteOnExit();
  //
  // // Writes data to numlTmp andd adds it to the file
  // NuMLWriter.write(model.getDataDoc(), numlTmp);
  // ca.addEntry(numlTmp, model.getDataDocName(), NUML_URI);
  // }
  //
  // // Creates tmp file for the model
  // File sbmlTmp = File.createTempFile("temp1", "");
  // sbmlTmp.deleteOnExit();
  //
  // // Writes model to sbmlTmp and adds it to the file
  // WRITER.write(model.getModelDoc(), sbmlTmp);
  // ca.addEntry(sbmlTmp, model.getModelDocName(), modelURI);
  // }
  //
  // // Adds description with model type
  // ModelType modelType = ModelType.PRIMARY_MODEL_WDATA;
  // Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).node;
  // ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));
  //
  // ca.pack();
  // ca.close();
  // }

  /**
   * Writes primary models with data to a PMF or PMFX file. Faulty models are skipped. Existent
   * files are overwritten.
   *
   * @param filename
   * @param modelURI
   * @param models
   * @throws CombineArchiveException if the combineArchive could not be opened or closed properly
   */
  private static void write(final String filename, final URI modelURI,
      final List<PrimaryModelWData> models) throws CombineArchiveException {

    // Removes previous CombineArchive if it exists
    final File tmpFile = new File(filename);
    if (tmpFile.exists()) {
      tmpFile.delete();
    }

    // Creates new CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    // Adds models and data
    for (final PrimaryModelWData model : models) {
      try {
        // Creates temporary file for the data
        final File tmpNuML = File.createTempFile("tmpData", "");
        tmpNuML.deleteOnExit();

        // Writes data to tmpNuML and adds it to the combineArchive
        NuMLWriter.write(model.getDataDoc(), tmpNuML);
        combineArchive.addEntry(tmpNuML, model.getDataDocName(), NUML_URI);

        // Creates temporary file for the model
        final File tmpSBML = File.createTempFile("tmpModel", "");
        tmpSBML.deleteOnExit();

        // Writes model to tmpSBML and adds it to the combineArchive
        WRITER.write(model.getModelDoc(), tmpSBML);
        combineArchive.addEntry(tmpSBML, model.getModelDocName(), modelURI);
      } catch (IOException | TransformerFactoryConfigurationError | TransformerException
          | SBMLException | XMLStreamException | ParserConfigurationException e) {
        System.err.println(model.getModelDocName() + " could not be saved");
        e.printStackTrace();
      }
    }

    final ModelType modelType = ModelType.PRIMARY_MODEL_WDATA;
    final Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).node;
    combineArchive.addDescription(new DefaultMetaDataObject(metadataAnnotation));

    // Packs and closes the combineArchive
    CombineArchiveUtil.pack(combineArchive);
    CombineArchiveUtil.close(combineArchive);
  }
}
