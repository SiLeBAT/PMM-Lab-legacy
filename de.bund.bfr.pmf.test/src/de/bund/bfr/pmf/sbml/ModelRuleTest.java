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
package de.bund.bfr.pmf.sbml;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.bund.bfr.pmf.ModelClass;

public class ModelRuleTest {

	private String formulaName;
	private String variable;
	private String formula;
	private ModelClass modelClass;
	private int pmmlabID;
	Reference[] references;

	@Before
	public void setUp() {
		formulaName = "Baranyi model";
		variable = "Value";
		formula = "LOG10Nres+log10((10^(LOG10N0-LOG10Nres)-1)*10^(-(Time/delta)^p)+1)";
		modelClass = ModelClass.GROWTH;

		String author = "Baranyi, J.";
		int year = 1995;
		String title = "Mathematics of predictive food";
		String abstractText = "Commonly encountered problems...";
		String journal = "International Journal of Food Microbiology";
		String volume = "23";
		String issue = "2";
		int page = 199;
		int approvalMode = 1;
		String website = "http://smas.chemeng.ntua.gr/miram/files/publ_237_10_2_2005.pdf";
		ReferenceType referenceType = ReferenceType.Paper;
		String comment = "";
		Reference ref = SBMLFactory.createReference(author, year, title, abstractText, journal, volume, issue, page,
				approvalMode, website, referenceType, comment);
		references = new Reference[] { ref };
	}

	@Test
	public void test() {
		ModelRule rule = new ModelRule(variable, formula, formulaName, modelClass, pmmlabID, references);
		assertEquals(variable, rule.getVariable());
		assertEquals(formula, rule.getFormula());
	}

	@Test
	public void testFormulaNameAccesors() {
		ModelRule rule = new ModelRule(variable, formula, formulaName, modelClass, pmmlabID, references);
		assertEquals(formulaName, rule.getFormulaName());

		rule.setFormulaName("other formula");
		assertEquals("other formula", rule.getFormulaName());
	}

	@Test
	public void testModelClassAccesors() {
		ModelRule rule = new ModelRule(variable, formula, formulaName, modelClass, pmmlabID, references);
		assertEquals(modelClass, rule.getModelClass());

		rule.setModelClass(ModelClass.SURVIVAL);
		assertEquals(ModelClass.SURVIVAL, rule.getModelClass());
	}

	@Test
	public void testPmmLabIdAccesors() {
		ModelRule rule = new ModelRule(variable, formula, formulaName, modelClass, pmmlabID, references);
		assertTrue(pmmlabID == rule.getPmmlabID());

		rule.setPmmlabID(Integer.MAX_VALUE);
		assertTrue(Integer.MAX_VALUE == rule.getPmmlabID());
	}

	@Test
	public void testReferenceAccesors() {
		ModelRule rule = new ModelRule(variable, formula, formulaName, modelClass, pmmlabID, references);
		assertArrayEquals(references, rule.getReferences());

		final String author = "Baranyi, J.";
		final int year = 1994;
		final String title = "A dynamic approach to predicting bacterial growth in food";
		final String abstractText = "A new member ...";
		final String journal = "International Journal of Food Microbiology";
		final String volume = "23";
		final String issue = "3";
		final int page = 277;
		final int approvalMode = 1;
		final String website = "http://www.sciencedirect.com/science/article/pii/0168160594901570";
		final ReferenceType type = ReferenceType.Paper;
		final String comment = "improvised comment";
		Reference otherRef = SBMLFactory.createReference(author, year, title, abstractText, journal, volume, issue, page, approvalMode, website, type, comment);
		Reference[] otherRefs = new Reference[] { otherRef };
		
		rule.setReferences(otherRefs);
		assertArrayEquals(otherRefs, rule.getReferences());
	}
}
