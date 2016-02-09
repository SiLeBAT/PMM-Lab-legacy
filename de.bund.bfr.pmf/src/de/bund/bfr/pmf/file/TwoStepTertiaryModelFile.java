/***************************************************************************************************
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
 **************************************************************************************************/
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
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.xml.XMLNode;
import org.xml.sax.SAXException;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.model.TwoStepTertiaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.bund.bfr.pmf.sbml.PrimaryModelNode;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 3a: File with tertiary model generated with 2-step fit approach.
 * 
 * @author Miguel Alba
 */
public class TwoStepTertiaryModelFile {

  private static final URI SBML_URI = URIFactory.createSBMLURI();
  private static final URI PMF_URI = URIFactory.createPMFURI();
  private static final URI NUML_URI = URIFactory.createNuMLURI();

  private static final SBMLWriter WRITER = new SBMLWriter();

  public static List<TwoStepTertiaryModel> readPMF(String filename) throws CombineArchiveException {
    return read(filename, SBML_URI);
  }

  public static List<TwoStepTertiaryModel> readPMFX(String filename)
      throws CombineArchiveException {
    return read(filename, PMF_URI);
  }

  /**
   */
  public static void writePMF(String dir, String filename, List<TwoStepTertiaryModel> models)
      throws Exception {
    String caName = String.format("%s/%s.pmf", dir, filename);
    write(caName, SBML_URI, models);
  }

  public static void writePMFX(String dir, String filename, List<TwoStepTertiaryModel> models)
      throws Exception {
    String caName = String.format("%s/%s.pmfx", dir, filename);
    write(caName, PMF_URI, models);
  }

  /**
   * Reads two step tertiary models from a PMF or PMFX file. Faulty models are skipped.
   *
   * @param filename
   * @param modelURI
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static List<TwoStepTertiaryModel> read(final String filename, final URI modelURI)
      throws CombineArchiveException {

    // Creates CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final List<TwoStepTertiaryModel> models = new LinkedList<>();

    // Gets data documents
    final List<ArchiveEntry> dataEntriesList = combineArchive.getEntriesWithFormat(NUML_URI);
    final Map<String, NuMLDocument> dataDocsMap = new HashMap<>(dataEntriesList.size());
    for (final ArchiveEntry entry : dataEntriesList) {
      final String docName = entry.getFileName();
      try {
        final NuMLDocument doc = CombineArchiveUtil.readData(entry.getPath());
        dataDocsMap.put(docName, doc);
      } catch (IOException | ParserConfigurationException | SAXException e) {
        System.err.println(docName + " could not be read");
        e.printStackTrace();
      }
    }

    final Element metaParent = combineArchive.getDescriptions().get(0).getXmlDescription();
    final Set<String> masterFiles = new PMFMetadataNode(metaParent).masterFiles;

    // Classify models into tertiary or secondary models
    final Map<String, SBMLDocument> tertDocs = new HashMap<>(masterFiles.size());
    final Map<String, SBMLDocument> primDocs = new HashMap<>();
    final Map<String, SBMLDocument> secDocs = new HashMap<>();

    for (final ArchiveEntry entry : combineArchive.getEntriesWithFormat(modelURI)) {
      final String docName = entry.getFileName();
      try {
        final SBMLDocument doc = CombineArchiveUtil.readModel(entry.getPath());

        if (masterFiles.contains(docName)) {
          tertDocs.put(docName, doc);
        } else if (doc.getModel().getListOfSpecies().size() == 0) {
          secDocs.put(docName, doc);
        } else {
          primDocs.put(docName, doc);
        }
      } catch (IOException | XMLStreamException e) {
        System.err.println(docName + " could not be read");
        e.printStackTrace();
      }
    }

    CombineArchiveUtil.close(combineArchive);

    for (final Map.Entry<String, SBMLDocument> entry : tertDocs.entrySet()) {
      final String tertDocName = entry.getKey();
      final SBMLDocument tertDoc = entry.getValue();

      final List<String> secModelNames = new LinkedList<>();
      final List<SBMLDocument> secModels = new LinkedList<>();
      final List<PrimaryModelWData> primModels = new LinkedList<>();

      final CompSBMLDocumentPlugin secCompPlugin =
          (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);

      // Gets secondary model documents
      final ListOf<ExternalModelDefinition> emds =
          secCompPlugin.getListOfExternalModelDefinitions();
      for (final ExternalModelDefinition emd : emds) {
        final String secModelName = emd.getSource();
        secModelNames.add(secModelName);

        final SBMLDocument secModel = secDocs.get(secModelName);
        secModels.add(secModel);
      }

      /**
       * All the secondary models of a Two step tertiary model are linked to the same primary
       * models. Thus these primary models can be retrieved from the first secondary model
       */
      final Model md = secModels.get(0).getModel();

      final XMLNode metadata =
          md.getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
      for (final XMLNode pmNode : metadata.getChildElements(PrimaryModelNode.TAG, "")) {
        // Gets model name from annotation
        final String mdName = pmNode.getChild(0).getCharacters();
        // Gets primary model
        final SBMLDocument mdDoc = primDocs.get(mdName);
        // Gets data source annotation of the primary model
        final XMLNode mdDocMetadata =
            mdDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
        final XMLNode node = mdDocMetadata.getChildElement("dataSource", "");
        // Gets data name from this annotation
        final String dataName = new DataSourceNode(node).getFile();
        // Gets data file
        final NuMLDocument dataDoc = dataDocsMap.get(dataName);

        primModels.add(new PrimaryModelWData(mdName, mdDoc, dataName, dataDoc));
      }

      models.add(
          new TwoStepTertiaryModel(tertDocName, tertDoc, primModels, secModelNames, secModels));
    }

    return models;
  }

  /**
   * Writes two step tertiary models to a PMF or PMFX file. Faulty models are skipped. Existent
   * files are overwritten.
   *
   * @param filename
   * @param modelURI
   * @param models
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static void write(final String filename, final URI modelURI,
      final List<TwoStepTertiaryModel> models) throws CombineArchiveException {

    // Removes previous CombineArchvie if it exists
    final File fileTmp = new File(filename);
    if (fileTmp.exists()) {
      fileTmp.delete();
    }

    // Creates new CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final Set<String> masterFiles = new HashSet<>(models.size());

    // Adds models and data
    for (final TwoStepTertiaryModel model : models) {

      for (final PrimaryModelWData pm : model.getPrimModels()) {
        try {
          CombineArchiveUtil.writeData(combineArchive, pm.getDataDoc(), pm.getDataDocName());
          CombineArchiveUtil.writeModel(combineArchive, pm.getModelDoc(), pm.getModelDocName(),
              modelURI);
        } catch (IOException | SBMLException | XMLStreamException
            | TransformerFactoryConfigurationError | TransformerException
            | ParserConfigurationException e) {
          System.err.println(pm.getModelDocName() + " could not be saved");
          e.printStackTrace();
        }
      }

      for (int i = 0; i < model.getSecDocs().size(); i++) {
        final String secDocName = model.getSecDocNames().get(i);
        final SBMLDocument secDoc = model.getSecDocs().get(i);
        try {
          CombineArchiveUtil.writeModel(combineArchive, secDoc, secDocName, modelURI);
        } catch (IOException | SBMLException | XMLStreamException e) {
          System.err.println(secDocName + " could not be saved");
          e.printStackTrace();
        }
      }

      // Creates tmp file for the secondary model
      try {
        File tertTmp = File.createTempFile("tert", "");
        tertTmp.deleteOnExit();

        // Writes tertiary model to tertTmp and adds it to the file
        WRITER.write(model.getTertDoc(), tertTmp);
        final ArchiveEntry masterEntry =
            combineArchive.addEntry(tertTmp, model.getTertDocName(), modelURI);

        masterFiles.add(masterEntry.getPath().getFileName().toString());
      } catch (IOException | SBMLException | XMLStreamException e) {
        System.err.println(model.getTertDocName() + " could not be saved");
        e.printStackTrace();
      }
    }

    // Adds description with model type
    final ModelType modelType = ModelType.TWO_STEP_TERTIARY_MODEL;
    final Element metaDataAnnotation = new PMFMetadataNode(modelType, masterFiles).node;
    combineArchive.addDescription(new DefaultMetaDataObject(metaDataAnnotation));

    CombineArchiveUtil.pack(combineArchive);
    CombineArchiveUtil.close(combineArchive);
  }
}
