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

import org.junit.Test;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.pmf.numl.NuMLDocument;

/**
 * @author Miguel Alba
 *
 */
public class TwoStepSecondaryModelTest {

  @Test
  public void test() {
    final SBMLDocument secModel = ModelTestUtil.createDummyModel();
    final SBMLDocument primModelDoc = ModelTestUtil.createDummyModel();
    final NuMLDocument primModelData = ModelTestUtil.createDummyData();
    final PrimaryModelWData primModel =
        new PrimaryModelWData("primModel.sbml", primModelDoc, "primModelData.numl", primModelData);
    final TwoStepSecondaryModel model = new TwoStepSecondaryModel("secModel.sbml", secModel,
        Arrays.asList(primModel));
    
    assertEquals("secModel.sbml", model.getSecDocName());
    assertEquals(secModel, model.getSecDoc());
    assertEquals(Arrays.asList(primModel), model.getPrimModels());
  }
}
