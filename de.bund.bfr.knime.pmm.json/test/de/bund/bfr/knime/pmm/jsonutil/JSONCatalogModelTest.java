/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.jsonutil;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

public class JSONCatalogModelTest {

	@Test
	public void test() {

		int id = 77;
		String name = "Baranyi model (Baranyi and Roberts,1995) - iPMP Full Growth Models Eq 6";
		String formula = "Value=Y0+mu_max*Time+ln(exp(-mu_max*Time)+exp(-h0)-exp(-mu_max*Time-h0))-ln(1+((exp(mu_max*(Time+(1/mu_max)*ln(exp(-mu_max*Time)+exp(-h0)-exp(-mu_max*Time-h0))))-1)/(exp(Ymax-Y0))))*((((((Ymax>Y0)*(mu_max>=0))))))";
		int modelClass = 1;
		String dbuuid = "c6582d2c-7cd6-4ee3-b425-68662b2558d9";
		String comment = "";

		CatalogModelXml catModel = new CatalogModelXml(id, name, formula,
				modelClass, dbuuid);
		catModel.setComment(comment);

		JSONCatalogModel jsonCatalogModel = new JSONCatalogModel(catModel);
		catModel = jsonCatalogModel.toCatalogModelXml();

		// Tests obtainedCatalogModel
		assertTrue(id == catModel.getId());
		assertEquals(name, catModel.getName());
		assertEquals(formula, catModel.getFormula());
		assertTrue(modelClass == catModel.getModelClass());
		assertEquals(dbuuid, catModel.getDbuuid());
		assertEquals(comment, catModel.getComment());
	}
}
