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

import javax.xml.stream.XMLStreamException;

import org.jdom2.Element;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.ManualTertiaryModel;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * Case 2c: Manual secondary models. Secondary models generated manually.
 * 
 * @author Miguel Alba
 */
public class ManualTertiaryModelFile {

  private static final URI SBML_URI = URIFactory.createSBMLURI();
  private static final URI PMF_URI = URIFactory.createPMFURI();

  private static final SBMLWriter WRITER = new SBMLWriter();

  public static List<ManualTertiaryModel> readPMF(String filename) throws Exception {
    return read(filename, SBML_URI);
  }

  public static List<ManualTertiaryModel> readPMFX(String filename) throws Exception {
    return read(filename, PMF_URI);
  }

  public static void writePMF(String dir, String filename, List<ManualTertiaryModel> models)
      throws Exception {
    // Creates CombineArchive name
    String caName = String.format("%s/%s.pmf", dir, filename);
    write(caName, SBML_URI, models);
  }

  public static void writePMFX(String dir, String filename, List<ManualTertiaryModel> models)
      throws Exception {
    // Creates CombineArchive name
    String caName = String.format("%s/%s.pmfx", dir, filename);
    write(caName, PMF_URI, models);
  }

  /**
   * Reads manual tertiary models from a PMF or PFMX file. Faulty models are skipped.
   *
   * @param filename
   * @param modelURI
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static List<ManualTertiaryModel> read(final String filename, final URI modelURI)
      throws CombineArchiveException {

    // Creates CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final List<ManualTertiaryModel> models = new LinkedList<>();

    final MetaDataObject mdo = combineArchive.getDescriptions().get(0);
    final Element metaParent = mdo.getXmlDescription();
    final PMFMetadataNode metadataAnnotation = new PMFMetadataNode(metaParent);
    final Set<String> masterFiles = metadataAnnotation.masterFiles;

    final List<ArchiveEntry> sbmlEntries = combineArchive.getEntriesWithFormat(modelURI);

    // Classify models into tertiary or secondary models
    final int numTertDocs = masterFiles.size();
    final int numSecDocs = sbmlEntries.size() - numTertDocs;
    final Map<String, SBMLDocument> tertDocsMap = new HashMap<>(numTertDocs);
    final Map<String, SBMLDocument> secDocsMap = new HashMap<>(numSecDocs);

    // Reads model docs
    for (final ArchiveEntry entry : sbmlEntries) {
      final String docName = entry.getFileName();

      try {
        final SBMLDocument doc = CombineArchiveUtil.readModel(entry.getPath());
        if (masterFiles.contains(docName)) {
          tertDocsMap.put(docName, doc);
        } else {
          secDocsMap.put(docName, doc);
        }
      } catch (IOException | XMLStreamException e) {
        System.err.println(docName + " could not be read");
        e.printStackTrace();
      }
    }

    CombineArchiveUtil.close(combineArchive);

    for (final Map.Entry<String, SBMLDocument> entry : tertDocsMap.entrySet()) {
      final String tertDocName = entry.getKey();
      final SBMLDocument tertDoc = entry.getValue();

      final List<String> secModelNames = new LinkedList<>();
      final List<SBMLDocument> secModels = new LinkedList<>();
      final CompSBMLDocumentPlugin plugin =
          (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
      // Gets secondary model ids
      for (final ExternalModelDefinition emd : plugin.getListOfExternalModelDefinitions()) {
        final String secModelName = emd.getSource();
        secModelNames.add(secModelName);

        final SBMLDocument secModel = secDocsMap.get(secModelName);
        secModels.add(secModel);
      }

      models.add(new ManualTertiaryModel(tertDocName, tertDoc, secModelNames, secModels));
    }

    return models;
  }

  /**
   * Writes manual tertiary model to a PMF or PMFX file. Faulty models are skipped. Existent files
   * are overwritten.
   *
   * @param filename
   * @param modelURI
   * @param models
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static void write(final String filename, final URI modelURI,
      final List<ManualTertiaryModel> models) throws CombineArchiveException {

    // Removes previous CombineArchive if it exists
    File fileTmp = new File(filename);
    if (fileTmp.exists()) {
      fileTmp.delete();
    }

    // Creates new CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final Set<String> masterFiles = new HashSet<>(models.size());

    // Adds models and data
    for (final ManualTertiaryModel model : models) {
      try {
        // Creates tmp file for the tert model
        final File tertTmp = File.createTempFile("sec", "");
        tertTmp.deleteOnExit();
        // Writes tertiary model to tertTmp and adds it to the file
        WRITER.write(model.getTertiaryDoc(), tertTmp);
        final ArchiveEntry masterEntry =
            combineArchive.addEntry(tertTmp, model.getTertiaryDocName(), modelURI);
        masterFiles.add(masterEntry.getPath().getFileName().toString());
      } catch (IOException | SBMLException | XMLStreamException e) {
        System.err.println(model.getTertiaryDocName() + " could not be saved");
        continue;
      }

      for (int i = 0; i < model.getSecDocs().size(); i++) {
        final SBMLDocument secDoc = model.getSecDocs().get(i);
        final String secDocName = model.getSecDocNames().get(i);
        
        try {
          CombineArchiveUtil.writeModel(combineArchive, secDoc, secDocName, modelURI);
        } catch (IOException | SBMLException | XMLStreamException e) {
          System.err.println(secDocName + " could not be saved");
        }
      }
    }

    final ModelType modelType = ModelType.MANUAL_TERTIARY_MODEL;
    final Element metadataAnnotation = new PMFMetadataNode(modelType, masterFiles).node;
    combineArchive.addDescription(new DefaultMetaDataObject(metadataAnnotation));

    CombineArchiveUtil.pack(combineArchive);
    CombineArchiveUtil.close(combineArchive);
  }
}
