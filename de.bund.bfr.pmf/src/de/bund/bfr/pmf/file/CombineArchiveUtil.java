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
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.jdom2.JDOMException;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.xml.sax.SAXException;

import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.NuMLReader;
import de.bund.bfr.pmf.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;

public class CombineArchiveUtil {

  private static final URI numlURI = URIFactory.createNuMLURI();

  private static final SBMLReader READER = new SBMLReader();
  private static final SBMLWriter WRITER = new SBMLWriter();

  private CombineArchiveUtil() {}

  /**
   * Opens safely a CombineArchive
   *
   * @param filename
   * @throws CombineArchiveException if the CombineArchive could not be opened properly
   */
  public static CombineArchive open(final String filename) throws CombineArchiveException {
    try {
      return new CombineArchive(new File(filename));
    } catch (IOException | JDOMException | ParseException | CombineArchiveException e) {
      throw new CombineArchiveException(filename + " could not be opened");
    }
  }

  /**
   * Packs safely a CombineArchive
   *
   * @param combineArchive
   * @throws CombineArchiveException if the CombineArchive could not be packed properly
   */
  public static void pack(final CombineArchive combineArchive) throws CombineArchiveException {
    try {
      combineArchive.pack();
    } catch (IOException | TransformerException e) {
      throw new CombineArchiveException(combineArchive.getEntityPath() + " could not be packed");
    }
  }

  /**
   * Closes safely a CombineArchive
   *
   * @param combineArchive
   * @throws CombineArchiveException if the CombineArchive could not be closed properly
   */
  public static void close(final CombineArchive combineArchive) throws CombineArchiveException {
    try {
      combineArchive.close();
    } catch (IOException e) {
      throw new CombineArchiveException(combineArchive.getEntityPath() + " could not be closed");
    }
  }

  public static NuMLDocument readData(final Path path)
      throws IOException, ParserConfigurationException, SAXException {
    final InputStream stream = Files.newInputStream(path, StandardOpenOption.READ);
    final NuMLDocument doc = NuMLReader.read(stream);
    stream.close();

    return doc;
  }

  public static void writeData(final CombineArchive combineArchive, final NuMLDocument doc,
      final String docName) throws IOException, TransformerFactoryConfigurationError,
          TransformerException, ParserConfigurationException {
    // Creates temporary file
    final File tmpFile = File.createTempFile("tmpNuML", "");
    tmpFile.deleteOnExit();

    // Writes data to tmpFile and adds it to combineArchive
    NuMLWriter.write(doc, tmpFile);
    combineArchive.addEntry(tmpFile, docName, numlURI);
  }

  public static SBMLDocument readModel(final Path path) throws IOException, XMLStreamException {
    final InputStream stream = Files.newInputStream(path, StandardOpenOption.READ);
    final SBMLDocument doc = READER.readSBMLFromStream(stream);
    stream.close();

    return doc;
  }

  public static void writeModel(final CombineArchive combineArchive, final SBMLDocument doc,
      final String docName, final URI modelURI)
          throws IOException, SBMLException, XMLStreamException {
    // Creates temporary file for the model
    final File tmpFile = File.createTempFile("tmpSBML", "");
    tmpFile.deleteOnExit();

    // Writes model to tmpFile and adds it to the file
    WRITER.write(doc, tmpFile);
    combineArchive.addEntry(tmpFile, docName, modelURI);
  }

  /** Removes previous CombineArchive if it exists */
  public static void removeExistentFile(final String filename) {
    final File tmpFile = new File(filename);
    if (tmpFile.exists()) {
      tmpFile.delete();
    }
  }
}
