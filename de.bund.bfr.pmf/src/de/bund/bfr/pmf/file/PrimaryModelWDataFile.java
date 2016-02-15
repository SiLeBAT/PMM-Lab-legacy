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

import java.io.IOException;
import java.net.URI;
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
import org.sbml.jsbml.xml.XMLNode;
import org.xml.sax.SAXException;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.numl.NuMLDocument;
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

  public static List<PrimaryModelWData> readPMF(final String filename)
      throws CombineArchiveException {
    return read(filename, SBML_URI);
  }

  public static List<PrimaryModelWData> readPMFX(final String filename)
      throws CombineArchiveException {
    return read(filename, PMF_URI);
  }

  /**
   * Writes experiments to PrimaryModelWDataFile.
   */
  public static void writePMF(final String dir, final String filename,
      final List<PrimaryModelWData> models) throws CombineArchiveException {
    final String caName = String.format("%s/%s.pmf", dir, filename);
    write(caName, SBML_URI, models);
  }

  /**
   * Writes experiments to PrimaryModelWDataFile.
   */
  public static void writePMFX(final String dir, final String filename,
      final List<PrimaryModelWData> models) throws CombineArchiveException {
    final String caName = String.format("%s/%s.pmfx", dir, filename);
    write(caName, PMF_URI, models);
  }

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
        final SBMLDocument modelDoc = CombineArchiveUtil.readModel(modelEntry.getPath());

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
        final NuMLDocument dataDoc = CombineArchiveUtil.readData(dataEntry.getPath());

        models.add(new PrimaryModelWData(modelDocName, modelDoc, dataDocName, dataDoc));
      } catch (IOException | XMLStreamException | ParserConfigurationException | SAXException e) {
        System.err.println(modelDocName + " could not be retrieved");
        e.printStackTrace();
      }
    }

    CombineArchiveUtil.close(combineArchive);

    return models;
  }

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

    CombineArchiveUtil.removeExistentFile(filename);

    // Creates new CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    // Adds models and data
    for (final PrimaryModelWData model : models) {
      try {
        CombineArchiveUtil.writeData(combineArchive, model.getDataDoc(), model.getDataDocName());
        CombineArchiveUtil.writeModel(combineArchive, model.getModelDoc(), model.getModelDocName(),
            modelURI);
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
