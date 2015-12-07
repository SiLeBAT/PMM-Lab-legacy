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

import de.bund.bfr.knime.pmm.common.MatrixXml;

public class JSONMatrixTest {

	@Test
	public void test() {
		
		Integer id = 21003;
		String name = "culture broth, broth culture, culture medium";
		String detail = "broth";
		String dbuuid = "c6582d2c-7cd6-4ee3-b425-68662b2558d9"; 
		
		MatrixXml matrixXml = new MatrixXml(id, name, detail, dbuuid);
		JSONMatrix jsonMatrix = new JSONMatrix(matrixXml);
		matrixXml = jsonMatrix.toMatrixXml();
		
		// Tests matrixXml
		assertEquals(id, matrixXml.getId());
		assertEquals(name, matrixXml.getName());
		assertEquals(detail, matrixXml.getDetail());
		assertEquals(dbuuid, matrixXml.getDbuuid());
	}
}
