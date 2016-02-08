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
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.jdom2.Element;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.PMFURI;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.PrimaryModelWOData;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 1b: Primary models without data file.
 * 
 * @author Miguel Alba
 */
public class PrimaryModelWODataFile {

  private static final URI SBML_URI = URIFactory.createNuMLURI();
  private static final URI PMF_URI = URIFactory.createPMFURI();

  private static final SBMLReader READER = new SBMLReader();
  private static final SBMLWriter WRITER = new SBMLWriter();

  public static List<PrimaryModelWOData> readPMF(String filename) throws CombineArchiveException {
    return read(filename, SBML_URI);
  }

  public static List<PrimaryModelWOData> readPMFX(String filename) throws CombineArchiveException {
    return read(filename, PMF_URI);
  }

  public static void writePMF(String dir, String filename, List<PrimaryModelWOData> models)
      throws CombineArchiveException {
    String caName = String.format("%s/%s.pmf", dir, filename);
    write(caName, SBML_URI, models);
  }

  public static void writePMFX(String dir, String filename, List<PrimaryModelWOData> models)
      throws CombineArchiveException {
    String caName = String.format("%s/%s.pmfx", dir, filename);
    write(caName, PMF_URI, models);
  }


  /**
   * Reads primary models without data from a PMF or PMFX file. Faulty files are skipped.
   *
   * @param filename
   * @param modelURI URI used for the models: {@link PMFURI} or {@link SBMLUri}
   * @throws CombineArchivException if the CombineArchive could not be opened or closed properly
   */
  private static List<PrimaryModelWOData> read(final String filename, final URI modelURI)
      throws CombineArchiveException {

    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final List<PrimaryModelWOData> models = new LinkedList<>();

    // Parse models in the combineArchive
    final List<ArchiveEntry> modelEntries = combineArchive.getEntriesWithFormat(modelURI);
    for (final ArchiveEntry entry : modelEntries) {
      final String docName = entry.getFileName();

      try {
        final InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
        final SBMLDocument doc = READER.readSBMLFromStream(stream);
        stream.close();

        models.add(new PrimaryModelWOData(docName, doc));
      } catch (IOException | XMLStreamException e) {
        System.err.println(docName + " could not be retrieved");
        e.printStackTrace();
      }
    }

    CombineArchiveUtil.close(combineArchive);

    return models;
  }

  /**
   * Writes primary models without data to a PMF or PMFX file. Faulty data files are skipped.
   * Existent files with the same filename are overwritten.
   *
   * @param filename
   * @parma models
   * @throws CombineArchiveException if the CombineArchive cannot be opened or closed properly
   */
  private static void write(final String filename, final URI modelURI,
      final List<PrimaryModelWOData> models) throws CombineArchiveException {

    // Removes previous CombineArchive if it exists
    final File tmpFile = new File(filename);
    if (tmpFile.exists()) {
      tmpFile.delete();
    }

    // Creates new CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    // Add models
    for (final PrimaryModelWOData model : models) {
      try {
        // Creates temporary file for the model
        File sbmlFile = File.createTempFile("temp", "");
        sbmlFile.deleteOnExit();

        // Writes model to tmpFile and adds it to the file
        WRITER.write(model.getDoc(), sbmlFile);
        combineArchive.addEntry(sbmlFile, model.getDocName(), modelURI);
      } catch (SBMLException | XMLStreamException | IOException e) {
        System.err.println(model.getDocName() + " could not be saved");
        e.printStackTrace();
      }
    }

    // Adds description with model type
    final ModelType modelType = ModelType.PRIMARY_MODEL_WODATA;
    final Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).node;
    combineArchive.addDescription(new DefaultMetaDataObject(metadataAnnotation));

    // Packs and closes the combineArchive
    CombineArchiveUtil.pack(combineArchive);
    CombineArchiveUtil.close(combineArchive);
  }
}
