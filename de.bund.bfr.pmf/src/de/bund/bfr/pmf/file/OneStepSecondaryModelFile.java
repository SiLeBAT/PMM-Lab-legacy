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
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLNode;
import org.xml.sax.SAXException;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.OneStepSecondaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 2b: One step secondary model file. Secondary models generated "implicitly" during 1-step
 * fitting of tertiary models.
 * 
 * @author Miguel Alba
 */
public class OneStepSecondaryModelFile {

  private static final URI SBML_URI = URIFactory.createSBMLURI();
  private static final URI PMF_URI = URIFactory.createPMFURI();
  private static final URI NuML_URI = URIFactory.createNuMLURI();

  public static List<OneStepSecondaryModel> readPMF(String filename) throws Exception {
    return read(filename, SBML_URI);
  }

  public static List<OneStepSecondaryModel> readPMFX(String filename) throws Exception {
    return read(filename, PMF_URI);
  }

  /**
   */
  public static void writePMF(String dir, String filename, List<OneStepSecondaryModel> models)
      throws Exception {

    // Creates CombineArchive name
    String caName = String.format("%s/%s.pmf", dir, filename);
    write(caName, SBML_URI, models);
  }

  public static void writePMFX(String dir, String filename, List<OneStepSecondaryModel> models)
      throws Exception {

    // Creates CombineArchive name
    String caName = String.format("%s/%s.pmfx", dir, filename);
    write(caName, PMF_URI, models);
  }

  /**
   * Reads {@link OneStepSecondaryMmodel}(s) from a PMF or PMFX file. Faulty models are skipped.
   *
   * @param filename
   * @param modelURI
   * @param models
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static List<OneStepSecondaryModel> read(final String filename, final URI modelURI)
      throws CombineArchiveException {
    // Creates CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final List<OneStepSecondaryModel> models = new LinkedList<>();

    // Gets data entries
    final List<ArchiveEntry> dataEntriesList = combineArchive.getEntriesWithFormat(NuML_URI);
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

    for (final ArchiveEntry entry : combineArchive.getEntriesWithFormat(modelURI)) {
      final String modelName = entry.getFileName();
      final SBMLDocument modelDoc;
      try {
        modelDoc = CombineArchiveUtil.readModel(entry.getPath());
      } catch (IOException | SBMLException | XMLStreamException e) {
        System.err.println(entry.getFileName() + " could not be read");
        e.printStackTrace();
        continue;
      }

      // Looks for DataSourceNode
      final CompSBMLDocumentPlugin secCompPlugin =
          (CompSBMLDocumentPlugin) modelDoc.getPlugin(CompConstants.shortLabel);
      final ModelDefinition md = secCompPlugin.getModelDefinition(0);
      final XMLNode m2Annot = md.getAnnotation().getNonRDFannotation();
      final XMLNode metadata = m2Annot.getChildElement("metadata", "");

      final List<String> numlDocNames = new LinkedList<>();
      final List<NuMLDocument> numlDocs = new LinkedList<>();

      for (final XMLNode node : metadata.getChildElements("dataSource", "")) {
        final DataSourceNode dsn = new DataSourceNode(node);
        final String dataFileName = dsn.getFile();

        numlDocNames.add(dataFileName);
        numlDocs.add(dataEntriesMap.get(dataFileName));
      }
      models.add(new OneStepSecondaryModel(modelName, modelDoc, numlDocNames, numlDocs));
    }

    CombineArchiveUtil.close(combineArchive);

    return models;
  }

  /**
   * Writes one step secondary models to a PMF or PMFX file. Faulty models are skipped. Existent
   * files are overwritten.
   *
   * @param filename
   * @param modelURI
   * @param models
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static void write(final String filename, final URI modelURI,
      final List<OneStepSecondaryModel> models) throws CombineArchiveException {

    CombineArchiveUtil.removeExistentFile(filename);

    // Creates new CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    // Adds models and data
    for (final OneStepSecondaryModel model : models) {
      try {
        for (int i = 0; i < model.getDataDocs().size(); i++) {
          final String dataDocName = model.getDataDocNames().get(i);
          final NuMLDocument dataDoc = model.getDataDocs().get(i);
          CombineArchiveUtil.writeData(combineArchive, dataDoc, dataDocName);
        }

        CombineArchiveUtil.writeModel(combineArchive, model.getModelDoc(), model.getModelDocName(),
            modelURI);
      } catch (IOException | TransformerFactoryConfigurationError | TransformerException
          | ParserConfigurationException | SBMLException | XMLStreamException e) {
        System.err.println(model.getModelDocName() + " could not be read");
        e.printStackTrace();
      }
    }

    // Adds description with model type
    final ModelType modelType = ModelType.ONE_STEP_SECONDARY_MODEL;
    final Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).node;
    combineArchive.addDescription(new DefaultMetaDataObject(metadataAnnotation));

    CombineArchiveUtil.pack(combineArchive);
    CombineArchiveUtil.close(combineArchive);
  }
}
