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
import java.text.ParseException;

import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;

import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;

public class CombineArchiveUtil {

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
}
