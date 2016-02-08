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
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.jdom2.Element;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.xml.XMLNode;
import org.xml.sax.SAXException;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.model.TwoStepSecondaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.bund.bfr.pmf.sbml.PrimaryModelNode;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * Case 2a: Two step secondary model file. Secondary models generated with the classical 2-step
 * approach from primary models.
 * 
 * @author Miguel Alba
 */
public class TwoStepSecondaryModelFile {

  private static final URI SBML_URI = URIFactory.createSBMLURI();
  private static final URI PMF_URI = URIFactory.createPMFURI();
  private static final URI NUML_URI = URIFactory.createNuMLURI();

  private static final SBMLWriter WRITER = new SBMLWriter();

  public static List<TwoStepSecondaryModel> readPMF(String filename) throws Exception {
    return read(filename, SBML_URI);
  }

  public static List<TwoStepSecondaryModel> readPMFX(String filename) throws Exception {
    return read(filename, PMF_URI);
  }

  /**
   */
  public static void writePMF(String dir, String filename, List<TwoStepSecondaryModel> models)
      throws Exception {
    String caName = String.format("%s/%s.pmf", dir, filename);
    write(caName, SBML_URI, models);
  }

  public static void writePMFX(String dir, String filename, List<TwoStepSecondaryModel> models)
      throws Exception {
    String caName = String.format("%s/%s.pmfx", dir, filename);
    write(caName, PMF_URI, models);
  }

  /**
   * Reads {@link TwoStepSecondaryModel}(s) from a PMF or PMFX file. Faulty models are skipped.
   *
   * @param filenmae
   * @param modelURI
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static List<TwoStepSecondaryModel> read(final String filename, final URI modelURI)
      throws CombineArchiveException {

    // Creates CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final List<TwoStepSecondaryModel> models = new LinkedList<>();

    // Gets data entries
    final List<ArchiveEntry> dataEntriesList = combineArchive.getEntriesWithFormat(NUML_URI);
    final Map<String, NuMLDocument> dataEntriesMap = new HashMap<>(dataEntriesList.size());
    for (final ArchiveEntry entry : dataEntriesList) {
      try {
        final NuMLDocument doc = CombineArchiveUtil.readData(entry.getPath());
        dataEntriesMap.put(entry.getFileName(), doc);
      } catch (IOException | ParserConfigurationException | SAXException e) {
        System.err.println(entry.getFileName() + " could not be read");
        e.printStackTrace();
      }
    }

    // Gets master files
    final MetaDataObject mdo = combineArchive.getDescriptions().get(0);
    final Element metaParent = mdo.getXmlDescription();
    final PMFMetadataNode metadataAnnotation = new PMFMetadataNode(metaParent);
    final Set<String> masterFiles = metadataAnnotation.masterFiles;

    // List of SBML entries
    final List<ArchiveEntry> sbmlEntries = combineArchive.getEntriesWithFormat(modelURI);

    // Classify models into primary or secondary models
    final int numSecModels = masterFiles.size();
    final int numPrimModels = sbmlEntries.size() - masterFiles.size();
    final Map<String, SBMLDocument> secModelsMap = new HashMap<>(numSecModels);
    final Map<String, SBMLDocument> primModelsMap = new HashMap<>(numPrimModels);

    for (final ArchiveEntry entry : sbmlEntries) {
      final String docName = entry.getFileName();
      try {
        final SBMLDocument doc = CombineArchiveUtil.readModel(entry.getPath());

        if (masterFiles.contains(entry.getFileName())) {
          secModelsMap.put(docName, doc);
        } else {
          primModelsMap.put(docName, doc);
        }
      } catch (IOException | XMLStreamException e) {
        System.err.println(docName + " could not be read");
        e.printStackTrace();
      }
    }

    CombineArchiveUtil.close(combineArchive);

    for (final Map.Entry<String, SBMLDocument> entry : secModelsMap.entrySet()) {
      final String secModelName = entry.getKey();
      final SBMLDocument secModelDoc = entry.getValue();

      final Model md = secModelDoc.getModel();

      final Annotation m2Annot = md.getAnnotation();
      if (!m2Annot.isSetNonRDFannotation()) {
        continue;
      }

      final XMLNode m2MetaData = m2Annot.getNonRDFannotation().getChildElement("metadata", "");
      final List<PrimaryModelWData> pms = new LinkedList<>();

      final List<XMLNode> refs = m2MetaData.getChildElements(PrimaryModelNode.TAG, "");
      for (final XMLNode ref : refs) {
        final String primModelName = ref.getChild(0).getCharacters();
        final SBMLDocument primModelDoc = primModelsMap.get(primModelName);

        // Looks for DataSourceNode
        final XMLNode m1Annot = primModelDoc.getModel().getAnnotation().getNonRDFannotation();
        final XMLNode m1MetaData = m1Annot.getChildElement("metadata", "");
        final XMLNode dataSourceRawNode = m1MetaData.getChildElement("dataSource", "");

        if (dataSourceRawNode == null) {
          continue;
        }

        final DataSourceNode dsn = new DataSourceNode(dataSourceRawNode);
        final String dataFileName = dsn.getFile();
        final NuMLDocument numlDoc = dataEntriesMap.get(dataFileName);

        pms.add(new PrimaryModelWData(primModelName, primModelDoc, dataFileName, numlDoc));
      }
      models.add(new TwoStepSecondaryModel(secModelName, secModelDoc, pms));
    }
    return models;
  }

  /**
   * Writes primary models with data to a PMF or PMFX file. Faulty models are skipped. Existent
   * files are overwritten.
   *
   * @param filename
   * @param modelURI
   * @param models
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static void write(final String filename, final URI modelURI,
      final List<TwoStepSecondaryModel> models) throws CombineArchiveException {

    // Removes previous CombineArchive if it exists
    final File tmpFile = new File(filename);
    if (tmpFile.exists()) {
      tmpFile.delete();
    }

    // Creates new CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final Set<String> masterFiles = new HashSet<>(models.size());

    // Adds models and data
    for (final TwoStepSecondaryModel model : models) {
      try {
        // Creates temporary file for the secondary model
        final File secTmpFile = File.createTempFile("sec", "");
        secTmpFile.deleteOnExit();

        // Writes model to secTmpFile and adds it to the file
        WRITER.write(model.getSecDoc(), secTmpFile);
        final ArchiveEntry masterEntry =
            combineArchive.addEntry(secTmpFile, model.getSecDocName(), modelURI);
        masterFiles.add(masterEntry.getPath().getFileName().toString());

        for (final PrimaryModelWData primModel : model.getPrimModels()) {
          CombineArchiveUtil.writeData(combineArchive, primModel.getDataDoc(),
              primModel.getDataDocName());
          CombineArchiveUtil.writeModel(combineArchive, primModel.getModelDoc(),
              primModel.getModelDocName(), modelURI);
        }
      } catch (IOException | SBMLException | XMLStreamException
          | TransformerFactoryConfigurationError | TransformerException
          | ParserConfigurationException e) {
        System.err.println(model.getSecDocName() + " could not be read");
        e.printStackTrace();
      }
    }

    // Adds description with model type
    ModelType modelType = ModelType.TWO_STEP_SECONDARY_MODEL;
    Element metadataAnnotation = new PMFMetadataNode(modelType, masterFiles).node;
    combineArchive.addDescription(new DefaultMetaDataObject(metadataAnnotation));

    CombineArchiveUtil.pack(combineArchive);
    CombineArchiveUtil.close(combineArchive);
  }
}
