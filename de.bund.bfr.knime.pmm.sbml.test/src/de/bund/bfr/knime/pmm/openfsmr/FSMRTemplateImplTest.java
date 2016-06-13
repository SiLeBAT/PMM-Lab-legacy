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

import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

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
		try {
			template.getModelName();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testModelIdAccesors() {
		String modelId = "PMM-Lab_342586849";
		template.setModelId(modelId);
		assertTrue(template.isSetModelId());
		assertEquals(modelId, template.getModelId());

		template.unsetModelId();
		assertFalse(template.isSetModelId());
		try {
			template.getModelId();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testModelLinkAccesors() throws MalformedURLException {
		// The protocol used, 'https', is valid, so it should not raise an
		// exception.
		URL link = new URL("https://drive.google.com/open?id=0B06JrEEq34hSaEt5UFVIMFM3M0k");
		template.setModelLink(link);
		assertTrue(template.isSetModelLink());
		assertEquals(link, template.getModelLink());

		template.unsetModelLink();
		assertFalse(template.isSetModelLink());
		try {
			template.getModelLink();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
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
		try {
			template.getOrganismName();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testOrganismDetailAccesors() {
		template.setOrganismDetails("broth");
		assertTrue(template.isSetOrganismDetails());
		assertEquals("broth", template.getOrganismDetails());

		template.unsetOrganismDetail();
		assertFalse(template.isSetOrganismDetails());
		try {
			template.getOrganismDetails();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testMatrixNameAccesors() {
		String matrixName = "Beef";
		template.setMatrixName(matrixName);
		assertTrue(template.isSetMatrixName());
		assertEquals(matrixName, template.getMatrixName());

		template.unsetMatrixName();
		assertFalse(template.isSetMatrixName());
		try {
			template.getMatrixName();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testMatrixDetailAccesors() {
		String matrixDetails = "(in: ground beef)";
		template.setMatrixDetails(matrixDetails);
		assertTrue(template.isSetMatrixDetails());
		assertEquals(matrixDetails, template.getMatrixDetails());

		template.unsetMatrixDetails();
		assertFalse(template.isSetMatrixDetails());
		try {
			template.getMatrixDetails();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testCreatorAccesors() {
		String creator = "Plaza Rodriguez et al.";
		template.setCreator(creator);
		assertTrue(template.isSetCreator());
		assertEquals(creator, template.getCreator());

		template.unsetCreator();
		assertFalse(template.isSetCreator());
		try {
			template.getCreator();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testFamilyNameAccesors() {
		String familyName = "Baranyi models";
		template.setFamilyName(familyName);
		assertTrue(template.isSetFamilyName());
		assertEquals(familyName, template.getFamilyName());

		template.unsetFamilyName();
		assertFalse(template.isSetFamilyName());
		try {
			template.getFamilyName();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testContactAccesors() {
		String contact = "some contact data";
		template.setContact(contact);
		assertTrue(template.isSetContact());
		assertEquals(contact, template.getContact());

		template.unsetContact();
		assertFalse(template.isSetContact());
		try {
			template.getContact();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
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
		try {
			template.getReferenceDescription();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testReferenceDescriptionLinkAccesors() throws MalformedURLException {
		// The protocol used, 'https', is valid, so it should not raise an
		// exception.
		URL link = new URL("http://smas.chemeng.ntua.gr/miram/files/publ_237_10_2_2005.pdf");
		template.setReferenceDescriptionLink(link);
		assertTrue(template.isSetReferenceDescriptionLink());
		assertEquals(link, template.getReferenceDescriptionLink());

		template.unsetReferenceDescriptionLink();
		assertFalse(template.isSetReferenceDescriptionLink());
		try {
			template.getReferenceDescriptionLink();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testCreatedDateAccesors() throws ParseException {
		SimpleDateFormat ft = new SimpleDateFormat("m/d/Y");
		Date createdDate = ft.parse("01/01/2014");
		template.setCreatedDate(createdDate);
		assertTrue(template.isSetCreatedDate());
		assertEquals(createdDate, template.getCreatedDate());

		template.unsetCreatedDate();
		assertFalse(template.isSetCreatedDate());
		try {
			template.getCreatedDate();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testModifiedDateAccesors() throws ParseException {
		SimpleDateFormat ft = new SimpleDateFormat("m/d/Y");
		Date modifiedDate = ft.parse("01/01/2015");
		template.setModifiedDate(modifiedDate);
		assertTrue(template.isSetModifiedDate());
		assertEquals(modifiedDate, template.getModifiedDate());

		template.unsetModifiedDate();
		assertFalse(template.isSetModifiedDate());
		try {
			template.getModifiedDate();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
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
		try {
			template.getRights();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testNoteAccesors() {
		String notes = "not curated";
		template.setNotes(notes);
		assertTrue(template.isSetNotes());
		assertEquals(notes, template.getNotes());

		template.unsetNotes();
		assertFalse(template.isSetNotes());
		try {
			template.getNotes();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testCurationStatusAccesors() {
		template.setCurationStatus("curated");
		assertTrue(template.isSetCurationStatus());
		assertEquals("curated", template.getCurationStatus());

		template.unsetCurationStatus();
		assertFalse(template.isSetCurationStatus());
		try {
			template.getCurationStatus();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testModelTypeAccesors() {
		template.setModelType(ModelType.TWO_STEP_TERTIARY_MODEL);
		assertTrue(template.isSetModelType());
		assertEquals(ModelType.TWO_STEP_TERTIARY_MODEL, template.getModelType());

		template.unsetModelType();
		assertFalse(template.isSetModelType());
		try {
			template.getModelType();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testModelSubjectAccesors() {
		template.setModelSubject(ModelClass.GROWTH);
		assertTrue(template.isSetModelSubject());
		assertEquals(ModelClass.GROWTH, template.getModelSubject());

		template.unsetModelSubject();
		assertFalse(template.isSetModelSubject());
		try {
			template.getModelSubject();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testFoodProcessAccesors() {
		template.setFoodProcess("cooking");
		assertTrue(template.isSetFoodProcess());
		assertEquals("cooking", template.getFoodProcess());

		template.unsetFoodProcess();
		assertFalse(template.isSetFoodProcess());
		try {
			template.getFoodProcess();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testDependentVariableAccesors() {
		template.setDependentVariable("Concentration");
		assertTrue(template.isSetDependentVariable());
		assertEquals("Concentration", template.getDependentVariable());

		template.unsetDependentVariable();
		assertFalse(template.isSetDependentVariable());
		try {
			template.getDependentVariable();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testDependentVariableUnitAccesors() {
		template.setDependentVariableUnit("ln");
		assertTrue(template.isSetDependentVariableUnit());
		assertEquals("ln", template.getDependentVariableUnit());

		template.unsetDependentVariableUnit();
		assertFalse(template.isSetDependentVariableUnit());
		try {
			template.getDependentVariableUnit();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testDependentVariableMinAccesors() {
		template.setDependentVariableMin(0.0);
		assertTrue(template.isSetDependentVariableMin());
		assertEquals(0.0, template.getDependentVariableMin(), 0.0);

		template.unsetDependentVariableMin();
		assertFalse(template.isSetDependentVariableMin());
		try {
			template.getDependentVariableMin();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testDependentVariableMaxAccesors() {
		template.setDependentVariableMax(10.0);
		assertTrue(template.isSetDependentVariableMax());
		assertEquals(10.0, template.getDependentVariableMax(), 0.0);

		template.unsetDependentVariableMax();
		assertFalse(template.isSetDependentVariableMax());
		try {
			template.getDependentVariableMax();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testIndependentVariableAccesors() {
		String[] independentVariables = new String[] { "time", "temperature" };
		template.setIndependentVariables(independentVariables);
		assertTrue(template.isSetIndependentVariables());
		assertArrayEquals(independentVariables, template.getIndependentVariables());

		template.unsetIndependentVariables();
		assertFalse(template.isSetIndependentVariables());
		try {
			template.getIndependentVariables();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}
	
	@Test
	public void testIndependentVariableUnitAccesors() {
		String[] indepVarUnits = new String[] { "s", "°C" };
		template.setIndependentVariablesUnits(indepVarUnits);
		assertTrue(template.isSetIndependentVariablesUnits());
		assertArrayEquals(indepVarUnits, template.getIndependentVariablesUnits());
		
		template.unsetIndependentVariableUnits();
		assertFalse(template.isSetIndependentVariablesUnits());
		try {
			template.getIndependentVariablesUnits();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}
	
	@Test
	public void testIndependentVariableMinsAccesors() {
		double[] mins = new double[] { 0, 1};
		template.setIndependentVariablesMins(mins);
		assertTrue(template.isSetIndependentVariablesMins());
		assertArrayEquals(mins, template.getIndependentVariablesMins(), 0.0);
		
		template.unsetIndependentVariableMins();
		assertFalse(template.isSetIndependentVariablesMins());
		try {
			template.getIndependentVariablesMins();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testIndependentVariableMaxsAccesors() {
		double[] maxs = new double[] { 0, 1};
		template.setIndependentVariablesMaxs(maxs);
		assertTrue(template.isSetIndependentVariablesMaxs());
		assertArrayEquals(maxs, template.getIndependentVariablesMaxs(), 0.0);
		
		template.unsetIndependentVariableMaxs();
		assertFalse(template.isSetIndependentVariablesMaxs());
		try {
			template.getIndependentVariablesMaxs();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}
	
	@Test
	public void testHasDataAccesors() {
		template.setHasData(false);
		assertTrue(template.isSetHasData());
		assertFalse(template.getHasData());
		
		template.unsetHasData();
		assertFalse(template.isSetHasData());
		try {
			template.getHasData();
			fail("Expected RuntimeException");
		} catch (RuntimeException e) {
		}
	}
}
