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

import de.bund.bfr.pmf.numl.NuMLDocument;

/**
 * @author Miguel Alba
 */
public class OneStepTertiaryModelTest {

  @Test
  public void test() {
    final SBMLDocument tertDoc = ModelTestUtil.createDummyModel();

    final List<String> secDocNames = Arrays.asList("secModel.sbml");
    final List<SBMLDocument> secDocs = Arrays.asList(ModelTestUtil.createDummyModel());

    final List<String> dataDocNames = Arrays.asList("dataDoc.numl");
    final List<NuMLDocument> dataDocs = Arrays.asList(ModelTestUtil.createDummyData());

    final OneStepTertiaryModel model = new OneStepTertiaryModel("tertDoc.sbml", tertDoc,
        secDocNames, secDocs, dataDocNames, dataDocs);

    assertEquals("tertDoc.sbml", model.getTertiaryDocName());
    assertEquals(tertDoc, model.getTertiaryDoc());
    assertEquals(secDocNames, model.getSecDocNames());
    assertEquals(secDocs, model.getSecDocs());
    assertEquals(dataDocNames, model.getDataDocNames());
    assertEquals(dataDocs, model.getDataDocs());
  }
}
