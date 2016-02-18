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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.sbml.jsbml.SBMLDocument;

/**
 * @author Miguel Alba
 */
public class TwoStepTertiaryModelTest {

  @Test
  public void test() {
    final SBMLDocument tertDoc = ModelTestUtil.createDummyModel();

    final List<String> secDocNames = Arrays.asList("secModel.sbml");
    final List<SBMLDocument> secDocs = Arrays.asList(ModelTestUtil.createDummyModel());

    final List<PrimaryModelWData> primModels = Arrays.asList(new PrimaryModelWData("primModel.sbml",
        ModelTestUtil.createDummyModel(), "primModel.numl", ModelTestUtil.createDummyData()));

    final TwoStepTertiaryModel model =
        new TwoStepTertiaryModel("tertModel.sbml", tertDoc, primModels, secDocNames, secDocs);
    assertEquals("tertModel.sbml", model.getTertDocName());
    assertEquals(tertDoc, model.getTertDoc());
    assertEquals(primModels, model.getPrimModels());
    assertEquals(secDocNames, model.getSecDocNames());
    assertEquals(secDocs, model.getSecDocs());
  }
}
