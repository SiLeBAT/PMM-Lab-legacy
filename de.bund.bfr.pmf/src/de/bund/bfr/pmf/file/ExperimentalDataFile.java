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

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.jdom2.Element;
import org.xml.sax.SAXException;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.ExperimentalData;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 0: Experimental data file.
 * 
 * @author Miguel Alba
 */
public class ExperimentalDataFile {

  private static final URI numlURI = URIFactory.createNuMLURI();

  private ExperimentalDataFile() {}

  public static List<ExperimentalData> readPMF(final String filename)
      throws CombineArchiveException {
    return read(filename);
  }

  public static List<ExperimentalData> readPMFX(final String filename)
      throws CombineArchiveException {
    return read(filename);
  }

  public static void writePMF(final String dir, final String filename,
      List<ExperimentalData> dataRecords) throws CombineArchiveException {
    String caName = String.format("%s/%s.pmf", dir, filename);
    write(caName, dataRecords);
  }

  public static void writePMFX(final String dir, final String filename,
      List<ExperimentalData> dataRecords) throws CombineArchiveException {
    String caName = String.format("%s/%s.pmfx", dir, filename);
    write(caName, dataRecords);
  }

  /**
   * Reads experimental data files from a PMF or PMFX file. Faulty data files are skipped.
   * 
   * @param filename
   * @return List of experimental data files
   * @throws CombineArchiveException if the CombineArchive could not be opened or closed properly
   */
  private static List<ExperimentalData> read(final String filename) throws CombineArchiveException {

    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    final List<ExperimentalData> dataRecords = new LinkedList<>();

    for (final ArchiveEntry entry : combineArchive.getEntriesWithFormat(numlURI)) {
      final String docName = entry.getFileName();
      try {
        final NuMLDocument doc = CombineArchiveUtil.readData(entry.getPath());
        dataRecords.add(new ExperimentalData(docName, doc));
      } catch (IOException | ParserConfigurationException | SAXException e) {
        System.err.println(docName + " could not be retrieved");
        e.printStackTrace();
      }
    }

    CombineArchiveUtil.close(combineArchive);

    return dataRecords;
  }

  /**
   * Writes experimental data files to a PMF or PMFX file. Faulty data files are skipped Existent
   * files with the same filename are overwritten.
   * 
   * @param filename
   * @param dataRecords
   * @throws CombineArchiveException if the CombineArchive cannot be opened or closed properly
   */
  private static void write(final String filename, final List<ExperimentalData> dataRecords)
      throws CombineArchiveException {

    CombineArchiveUtil.removeExistentFile(filename);

    // Creates new CombineArchive
    final CombineArchive combineArchive = CombineArchiveUtil.open(filename);

    // Add data records
    for (final ExperimentalData ed : dataRecords) {
      try {
        CombineArchiveUtil.writeData(combineArchive, ed.getDoc(), ed.getDocName());
      } catch (IOException | TransformerFactoryConfigurationError | TransformerException
          | ParserConfigurationException e) {
        System.err.println(ed.getDocName() + " could not be saved");
        e.printStackTrace();
      }
    }

    final ModelType modelType = ModelType.EXPERIMENTAL_DATA;
    final Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).node;
    combineArchive.addDescription(new DefaultMetaDataObject(metadataAnnotation));

    // Packs and closes the combineArchive
    CombineArchiveUtil.pack(combineArchive);
    CombineArchiveUtil.close(combineArchive);
  }
}
