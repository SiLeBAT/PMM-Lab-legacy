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

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.pmf.numl.NuMLDocument;

/**
 * Case 2b: It has a {@link SBMLDocument} for a secondary model and its primary model, which can be
 * linked to n {@link NuMLDocument}s.
 * 
 * @author Miguel Alba
 */
public class OneStepSecondaryModel {

  private String modelDocName;
  private SBMLDocument modelDoc; // Document with primary and secondary models

  private List<String> dataDocNames;
  private List<NuMLDocument> dataDocs; // Data documents

  public OneStepSecondaryModel(final String modelDocName, final SBMLDocument modelDoc,
      final List<String> dataDocNames, final List<NuMLDocument> dataDocs) {
    this.modelDocName = modelDocName;
    this.modelDoc = modelDoc;
    this.dataDocNames = dataDocNames;
    this.dataDocs = dataDocs;
  }

  public String getModelDocName() {
    return modelDocName;
  }

  public SBMLDocument getModelDoc() {
    return modelDoc;
  }

  public List<String> getDataDocNames() {
    return dataDocNames;
  }

  public List<NuMLDocument> getDataDocs() {
    return dataDocs;
  }
}
