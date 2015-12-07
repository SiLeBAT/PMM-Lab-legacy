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

import de.bund.bfr.knime.pmm.common.EstModelXml;

public class JSONEstModelTest {

	@Test
	public void test() {

		Integer id = 67;
		String name = "";
		Double sse = 1.909565079707032;
		Double rms = 0.3454675346276253;
		Double r2 = 0.9960322874417514;
		Double aic = -32.97713528768575;
		Double bic = -34.994206193469736;
		Integer dof = 16;
		Boolean checked = null;
		Integer qualityScore = null;
		String dbuuid = "c6582d2c-7cd6-4ee3-b425-68662b2558d9";
		String comment = "";

		EstModelXml estModel = new EstModelXml(id, name, sse, rms, r2, aic,
				bic, dof, checked, qualityScore, dbuuid);
		estModel.setComment(comment);
		JSONEstModel jsonEstModel = new JSONEstModel(estModel);
		estModel = jsonEstModel.toEstModelXml();
		
		// Tests estModel
		assertEquals(id, estModel.getId());
		assertEquals(name, estModel.getName());
		assertEquals(sse, estModel.getSse());
		assertEquals(rms, estModel.getRms());
		assertEquals(r2, estModel.getR2());
		assertEquals(aic, estModel.getAic());
		assertEquals(bic, estModel.getBic());
		assertEquals(dof, estModel.getDof());
		assertEquals(checked, estModel.getChecked());
		assertEquals(qualityScore, estModel.getQualityScore());
		assertEquals(dbuuid, estModel.getDbuuid());
		assertEquals(comment, estModel.getComment());
	}
}
