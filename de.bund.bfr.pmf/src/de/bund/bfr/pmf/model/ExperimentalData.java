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
package de.bund.bfr.pmf.model;

import de.bund.bfr.pmf.numl.NuMLDocument;

/**
 * Case 0. Each document is a {@link NuMLDocument} that keeps a time series.
 * 
 * @author Miguel Alba
 */
public class ExperimentalData {

  private String docName;
  private NuMLDocument doc;

  public ExperimentalData(final String docName, final NuMLDocument doc) {
    this.docName = docName;
    this.doc = doc;
  }

  public NuMLDocument getDoc() {
    return doc;
  }

  public String getDocName() {
    return docName;
  }
}
