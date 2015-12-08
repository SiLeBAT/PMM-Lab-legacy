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
package de.bund.bfr.knime.pmm.openfsmr;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;

/**
 * @author Miguel Alba
 *
 */
public class FSMRTemplateImplTest {

	private FSMRTemplateImpl template = new FSMRTemplateImpl();

	@Test
	public void testModelNameAccesors() {
		String modelName = "Salmonellaspp_GroundBeef_Temp_GrowthModel_Plaza_Rodriguez2014_Logistic_TwoStepFitting_PMM_Lab";
		template.setModelName(modelName);
		assertTrue(template.isSetModelName());
		assertEquals(modelName, template.getModelName());

		template.unsetModelName();
		assertFalse(template.isSetModelName());
		assertNull(template.getModelName());
	}

	@Test
	public void testModelIdAccesors() {
		String modelId = "PMM-Lab_342586849";
		template.setModelId(modelId);
		assertTrue(template.isSetModelId());
		assertEquals(modelId, template.getModelId());

		template.unsetModelId();
		assertFalse(template.isSetModelId());
		assertNull(template.getModelId());
	}

	@Test
	public void testModelLinkAccesors() {
		URL link = null;
		try {
			// The protocol used, 'https', is valid, so it should not raise an exception.
			link = new URL("https://drive.google.com/open?id=0B06JrEEq34hSaEt5UFVIMFM3M0k");		
			template.setModelLink(link);
			assertTrue(template.isSetModelLink());
			assertEquals(link, template.getModelLink());

			template.unsetModelLink();
			assertFalse(template.isSetModelLink());
			assertNull(template.getModelLink());
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testOrganismNameAccesors() {
		String organismName = "Salmonella spp.";
		template.setOrganismName(organismName);
		assertTrue(template.isSetOrganismName());
		assertEquals(organismName, template.getOrganismName());

		template.unsetOrganismName();
		assertFalse(template.isSetOrganismName());
		assertNull(template.getOrganismName());
	}

	@Test
	public void testOrganismDetailAccesors() {
		template.setOrganismDetails("broth");
		assertTrue(template.isSetOrganismDetails());
		assertEquals("broth", template.getOrganismDetails());

		template.unsetOrganismDetail();
		assertFalse(template.isSetOrganismDetails());
		assertNull(template.getOrganismDetails());
	}

	@Test
	public void testMatrixNameAccesors() {
		String matrixName = "Beef";
		template.setMatrixName(matrixName);
		assertTrue(template.isSetMatrixName());
		assertEquals(matrixName, template.getMatrixName());

		template.unsetMatrixName();
		assertFalse(template.isSetMatrixName());
		assertNull(template.getMatrixName());
	}

	@Test
	public void testMatrixDetailAccesors() {
		String matrixDetails = "(in: ground beef)";
		template.setMatrixDetails(matrixDetails);
		assertTrue(template.isSetMatrixDetails());
		assertEquals(matrixDetails, template.getMatrixDetails());

		template.unsetMatrixDetails();
		assertFalse(template.isSetMatrixDetails());
		assertNull(template.getMatrixDetails());
	}

	@Test
	public void testCreatorAccesors() {
		String creator = "Plaza Rodriguez et al.";
		template.setCreator(creator);
		assertTrue(template.isSetCreator());
		assertEquals(creator, template.getCreator());

		template.unsetCreator();
		assertFalse(template.isSetCreator());
		assertNull(template.getCreator());
	}

	@Test
	public void testReferenceDescriptionAccesors() {
		String referenceDescription = "Juneja, Vijay K. et al., 2009. "
				+ "Mathematical modeling of growth of Salmonella in raw "
				+ "ground beef under isothermal conditions from 10 to 45°C. "
				+ "International Journal of Food Microbiology ():  ff.";
		template.setReferenceDescription(referenceDescription);
		assertTrue(template.isSetReferenceDescription());
		assertEquals(referenceDescription, template.getReferenceDescription());

		template.unsetReferenceDescription();
		assertFalse(template.isSetReferenceDescription());
		assertNull(template.getReferenceDescription());
	}

	@Test
	public void testReferenceDescriptionLinkAccesors() {
		URL link = null;
		try {
			// The protocol used, 'https', is valid, so it should not raise an exception.
			link = new URL("http://smas.chemeng.ntua.gr/miram/files/publ_237_10_2_2005.pdf");		
			template.setReferenceDescriptionLink(link);
			assertTrue(template.isSetReferenceDescriptionLink());
			assertEquals(link, template.getReferenceDescriptionLink());

			template.unsetReferenceDescriptionLink();
			assertFalse(template.isSetReferenceDescriptionLink());
			assertNull(template.getReferenceDescriptionLink());
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreatedDateAccesors() {
		SimpleDateFormat ft = new SimpleDateFormat("m/d/Y");
		Date createdDate;
		try {
			createdDate = ft.parse("01/01/2014");
			template.setCreatedDate(createdDate);
			assertTrue(template.isSetCreatedDate());
			assertEquals(createdDate, template.getCreatedDate());

			template.unsetCreatedDate();
			assertFalse(template.isSetCreatedDate());
			assertNull(template.getCreatedDate());
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testModifiedDateAccesors() {
		SimpleDateFormat ft = new SimpleDateFormat("m/d/Y");
		Date modifiedDate;
		try {
			modifiedDate = ft.parse("01/01/2015");
			template.setModifiedDate(modifiedDate);
			assertTrue(template.isSetModifiedDate());
			assertEquals(modifiedDate, template.getModifiedDate());

			template.unsetModifiedDate();
			assertFalse(template.isSetModifiedDate());
			assertNull(template.getModifiedDate());
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRightAccesors() {
		String rights = "CC-BY-NC-SA";
		template.setRights(rights);
		assertTrue(template.isSetRights());
		assertEquals(rights, template.getRights());

		template.unsetRights();
		assertFalse(template.isSetRights());
		assertNull(template.getRights());
	}

	@Test
	public void testNoteAccesors() {
		String notes = "not curated";
		template.setNotes(notes);
		assertTrue(template.isSetNotes());
		assertEquals(notes, template.getNotes());

		template.unsetNotes();
		assertFalse(template.isSetNotes());
		assertNull(template.getNotes());
	}

	@Test
	public void testCurationStatusAccesors() {
		template.setCurationStatus("curated");
		assertTrue(template.isSetCurationStatus());
		assertEquals("curated", template.getCurationStatus());

		template.unsetCurationStatus();
		assertFalse(template.isSetCurationStatus());
		assertNull(template.getCurationStatus());
	}

	@Test
	public void testModelTypeAccesors() {
		template.setModelType(ModelType.TWO_STEP_TERTIARY_MODEL);
		assertTrue(template.isSetModelType());
		assertEquals(ModelType.TWO_STEP_TERTIARY_MODEL, template.getModelType());

		template.unsetModelType();
		assertFalse(template.isSetModelType());
		assertNull(template.getModelType());
	}

	@Test
	public void testModelSubjectAccesors() {
		template.setModelSubject(ModelClass.GROWTH);
		assertTrue(template.isSetModelSubject());
		assertEquals(ModelClass.GROWTH, template.getModelSubject());

		template.unsetModelSubject();
		assertFalse(template.isSetModelSubject());
		assertNull(template.getModelSubject());
	}

	@Test
	public void testFoodProcessAccesors() {
		template.setFoodProcess("cooking");
		assertTrue(template.isSetFoodProcess());
		assertEquals("cooking", template.getFoodProcess());
		
		template.unsetFoodProcess();
		assertFalse(template.isSetFoodProcess());
		assertNull(template.getFoodProcess());
	}

	@Test
	public void testDependentVariableAccesors() {
		template.setDependentVariable("Concentration");
		assertTrue(template.isSetDependentVariable());
		assertEquals("Concentration", template.getDependentVariable());

		template.unsetDependentVariable();
		assertFalse(template.isSetDependentVariable());
		assertNull(template.getDependentVariable());
	}

	@Test
	public void testDependentVariableMinAccesors() {
		template.setDependentVariableMin(0.0);
		assertTrue(template.isSetDependentVariableMin());
		assertEquals(0.0, template.getDependentVariableMin(), 0.0);

		template.unsetDependentVariableMin();
		assertFalse(template.isSetDependentVariableMin());
		assertNull(template.getDependentVariableMin());
	}

	@Test
	public void testDependentVariableMaxAccesors() {
		template.setDependentVariableMax(10.0);
		assertTrue(template.isSetDependentVariableMax());
		assertEquals(10.0, template.getDependentVariableMax(), 0.0);

		template.unsetDependentVariableMax();
		assertFalse(template.isSetDependentVariableMax());
		assertNull(template.getDependentVariableMax());
	}

	@Test
	public void testIndependentVariableAccesors() {
		String[] independentVariables = new String[] { "time", "temperature" };
		template.setIndependentVariables(independentVariables);
		assertTrue(template.isSetIndependentVariables());
		assertArrayEquals(independentVariables, template.getIndependentVariables());

		template.unsetIndependentVariables();
		assertFalse(template.isSetIndependentVariables());
		assertNull(template.getIndependentVariables());
	}
}
