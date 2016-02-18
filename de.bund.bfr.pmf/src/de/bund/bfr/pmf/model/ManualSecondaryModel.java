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

import org.sbml.jsbml.SBMLDocument;

/**
 * Case 2c. Holds secondary models generated manually with a {@link SBMLDocument} per each secondary
 * model.
 * 
 * @author Miguel Alba
 */
public class ManualSecondaryModel {

  private String docName;
  private SBMLDocument doc;

  public ManualSecondaryModel(final String docName, final SBMLDocument doc) {
    this.docName = docName;
    this.doc = doc;
  }

  public String getDocName() {
    return docName;
  }

  public SBMLDocument getDoc() {
    return doc;
  }
}
