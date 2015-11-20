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
				+ "ground beef under isothermal conditions from 10 to 45Â°C. "
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
		template.setIndependentVariable("time");
		assertTrue(template.isSetIndependentVariable());
		assertEquals("time", template.getIndependentVariable());

		template.unsetIndependentVariable();
		assertFalse(template.isSetIndependentVariable());
		assertNull(template.getIndependentVariable());
	}

	@Test
	public void testSoftwareAccesors() {
		String software = "PMM-Lab";
		template.setSoftware(software);
		assertTrue(template.isSetSoftware());
		assertEquals(software, template.getSoftware());

		template.unsetSoftware();
		assertFalse(template.isSetSoftware());
		assertNull(template.getSoftware());
	}

	@Test
	public void testSoftwareLinkAccesors() {
		URL link = null;
		try {
			// The protocol used, 'https', is valid, so it should not raise an exception.
			link = new URL("http://sourceforge.net/projects/pmmlab/");
			template.setSoftwareLink(link);
			assertTrue(template.isSetSoftwareLink());
			assertEquals(link, template.getSoftwareLink());

			template.unsetSoftwareLink();
			assertFalse(template.isSetSoftwareLink());
			assertNull(template.getSoftwareLink());
		} catch (MalformedURLException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSoftwareNoteAccesors() {
		String notes = "Predictive Microbial Modeling plug-in for KNIME";
		template.setSoftwareNotes(notes);
		assertTrue(template.isSetSoftwareNotes());
		assertEquals(notes, template.getSoftwareNotes());

		template.unsetSoftwareNotes();
		assertFalse(template.isSetSoftwareNotes());
		assertNull(template.getSoftwareNotes());
	}

	@Test
	public void testSoftwareAccesibilityAccesors() {
	    template.setSoftwareAccesibility("accesible");
	    assertTrue(template.isSetSoftwareAccessibility());
	    assertEquals("accesible", template.getSoftwareAccesibility());

	    template.unsetSoftwareAccesibility();
	    assertFalse(template.isSetSoftwareAccessibility());
	    assertNull(template.getSoftwareAccesibility());
	}

	@Test
	public void testSoftwareStochasticModelingAccesors() {
	    template.setSoftwareStochasticModeling(true);
	    assertTrue(template.isSetSoftwareStochasticModeling());
	    assertTrue(template.getSoftwareStochasticModeling());

	    template.unsetSoftwareStochasticModeling();
	    assertFalse(template.isSetSoftwareStochasticModeling());
	    assertNull(template.getSoftwareStochasticModeling());
	}

	@Test
	public void testSoftwarePredictionConditionsTest() {
	    template.setSoftwarePredictionConditions("great");
	    assertTrue(template.isSetSoftwarePredictionConditions());
	    assertEquals("great", template.getSoftwarePredictionConditions());

	    template.unsetSoftwarePredictionConditions();
	    assertFalse(template.isSetSoftwarePredictionConditions());
	    assertNull(template.getSoftwarePredictionConditions());
	}

	@Test
	public void testInitLevelUnitAccesors() {
	    template.setInitLevelUnit("m");
	    assertTrue(template.isSetInitLevelUnit());
	    assertEquals("m", template.getInitLevelUnit());

	    template.unsetInitLevelUnit();
	    assertFalse(template.isSetInitLevelUnit());
	    assertNull(template.getInitLevelUnit());
	}

	@Test
	public void testInitLevelMinAccesors() {
	    template.setInitLevelMin(0.0);
	    assertTrue(template.isSetInitLevelMin());
	    assertEquals(0.0, template.getInitLevelMin(), 0.0);

	    template.unsetInitLevelMin();
	    assertFalse(template.isSetInitLevelMin());
	    assertNull(template.getInitLevelMin());
	}

	@Test
	public void testInitLevelMaxAccesors() {
	    template.setInitLevelMax(10.0);
	    assertTrue(template.isSetInitLevelMax());
	    assertEquals(10.0, template.getInitLevelMax(), 0.0);

	    template.unsetInitLevelMax();
	    assertFalse(template.isSetInitLevelMax());
	    assertNull(template.getInitLevelMax());
	}

	@Test
	public void testTimeUnitAccesors() {
	    template.setTimeUnit("s");
	    assertTrue(template.isSetTimeUnit());
	    assertEquals("s", template.getTimeUnit());
	    
	    template.unsetTimeUnit();
	    assertFalse(template.isSetTimeUnit());
	    assertNull(template.getInitLevelMin());
	}

	@Test
	public void testTimeMinAccesors() {
	    template.setTimeMin(0.0);
	    assertTrue(template.isSetTimeMin());
	    assertEquals(0.0, template.getTimeMin(), 0.0);

	    template.unsetTimeMin();
	    assertFalse(template.isSetTimeMin());
	    assertNull(template.getTimeMin());
	}

	@Test
	public void testTimeMaxAccesors() {
	    template.setTimeMax(10.0);
	    assertTrue(template.isSetTimeMax());
	    assertEquals(10.0, template.getTimeMax(), 0.0);

	    template.unsetTimeMax();
	    assertFalse(template.isSetTimeMax());
	    assertNull(template.getTimeMax());
	}

	@Test
	public void testTemperatureUnitAccesors() {
	    template.setTemperatureUnit("ºC");
	    assertTrue(template.isSetTemperatureUnit());
	    assertEquals("ºC", template.getTemperatureUnit());

	    template.unsetTemperatureUnit();
	    assertFalse(template.isSetTemperatureUnit());
	    assertNull(template.getTemperatureUnit());
	}

	@Test
	public void testTemperatureMinAccesors() {
	    template.setTemperatureMin(0.0);
	    assertTrue(template.isSetTemperatureMin());
	    assertEquals(0.0, template.getTemperatureMin(), 0.0);

	    template.unsetTemperatureMin();
	    assertFalse(template.isSetTemperatureMin());
	    assertNull(template.getTemperatureMin());
	}

	@Test
	public void testTemperatureMaxAccesors() {
	    template.setTemperatureMax(10.0);
	    assertTrue(template.isSetTemperatureMax());
	    assertEquals(10.0, template.getTemperatureMax(), 0.0);

	    template.unsetTemperatureMax();
	    assertFalse(template.isSetTemperatureMax());
	    assertNull(template.getTemperatureMax());
	}

	@Test
	public void testPhUnitAccesors() {
	    template.setPhUnit("pH");
	    assertTrue(template.isSetPhUnit());
	    assertEquals("pH", template.getPhUnit());

	    template.unsetPhUnit();
	    assertFalse(template.isSetPhUnit());
	    assertNull(template.getPhUnit());
	}

	@Test
	public void testPhMinAccesors() {
	    template.setPhMin(0);
	    assertTrue(template.isSetPhMin());
	    assertEquals(0, template.getPhMin(), 0.0);

	    template.unsetPhMin();
	    assertFalse(template.isSetPhMin());
	    assertNull(template.getPhMin());
	}

	@Test
	public void testPhMaxAccesors() {
	    template.setPhMax(7);
	    assertTrue(template.isSetPhMax());
	    assertEquals(7, template.getPhMax(), 0.0);

	    template.unsetPhMax();
	    assertFalse(template.isSetPhMax());
	    assertNull(template.getPhMax());
	}

	@Test
	public void testAwUnitAccesors() {
	    template.setAwUnit("aw");
	    assertTrue(template.isSetAwUnit());
	    assertEquals("aw", template.getAwUnit());

	    template.unsetAwUnit();
	    assertFalse(template.isSetAwUnit());
	    assertNull(template.getAwUnit());
	}

	@Test
	public void testAwMinAccesors() {
	    template.setAwMin(0);
	    assertTrue(template.isSetAwMin());
	    assertEquals(0, template.getAwMin(), 0.0);

	    template.unsetAwMin();
	    assertFalse(template.isSetAwMin());
	    assertNull(template.getAwMin());
	}

	@Test
	public void testAwMaxAccesors() {
	    template.setAwMax(7);
	    assertTrue(template.isSetAwMax());
	    assertEquals(7, template.getAwMax(), 0.0);

	    template.unsetAwMax();
	    assertFalse(template.isSetAwMax());
	    assertNull(template.getAwMax());
	}

	@Test
	public void testNaClUnitAccesors() {
	    template.setNaClUnit("osm");
	    assertTrue(template.isSetNaClUnit());
	    assertEquals("osm", template.getNaClUnit());

	    template.unsetNaClUnit();
	    assertFalse(template.isSetNaClUnit());
	    assertNull(template.getNaClUnit());
	}

	@Test
	public void testNaClMinAccesors() {
	    template.setNaClMin(0);
	    assertTrue(template.isSetNaClMin());
	    assertEquals(0, template.getNaClMin(), 0.0);

	    template.unsetNaClMin();
	    assertFalse(template.isSetNaClMin());
	    assertNull(template.getNaClMin());
	}

	@Test
	public void testNaClMaxAccesors() {
	    template.setNaClMax(7);
	    assertTrue(template.isSetNaClMax());
	    assertEquals(7, template.getNaClMax(), 0.0);

	    template.unsetNaClMax();
	    assertFalse(template.isSetNaClMax());
	    assertNull(template.getNaClMax());
	}

	@Test
	public void testAltaUnitAccesors() {
	    template.setAltaUnit("alta");
	    assertTrue(template.isSetAltaUnit());
	    assertEquals("alta", template.getAltaUnit());

	    template.unsetAltaUnit();
	    assertFalse(template.isSetAltaUnit());
	    assertNull(template.getAltaUnit());
	}

	@Test
	public void testAltaMinAccesors() {
	    template.setAltaMin(0);
	    assertTrue(template.isSetAltaMin());
	    assertEquals(0, template.getAltaMin(), 0.0);

	    template.unsetAltaMin();
	    assertFalse(template.isSetAltaMin());
	    assertNull(template.getAltaMin());
	}

	@Test
	public void testAltaMaxAccesors() {
	    template.setAltaMax(7);
	    assertTrue(template.isSetAltaMax());
	    assertEquals(7, template.getAltaMax(), 0.0);

	    template.unsetAltaMax();
	    assertFalse(template.isSetAltaMax());
	    assertNull(template.getAltaMax());
	}

	@Test
	public void testCO2UnitAccesors() {
	    template.setCO2Unit("l");
	    assertTrue(template.isSetCO2Unit());
	    assertEquals("l", template.getCO2Unit());

	    template.unsetCO2Unit();
	    assertFalse(template.isSetCO2Unit());
	    assertNull(template.getCO2Unit());
	}

	@Test
	public void testCO2MinAccesors() {
	    template.setCO2Min(0);
	    assertTrue(template.isSetCO2Min());
	    assertEquals(0, template.getCO2Min(), 0.0);

	    template.unsetCO2Min();
	    assertFalse(template.isSetCO2Min());
	    assertNull(template.getCO2Min());
	}

	@Test
	public void testCO2MaxAccesors() {
	    template.setCO2Max(7);
	    assertTrue(template.isSetCO2Max());
	    assertEquals(7, template.getCO2Max(), 0.0);

	    template.unsetCO2Max();
	    assertFalse(template.isSetCO2Max());
	    assertNull(template.getCO2Max());
	}

	@Test
	public void testClO2UnitAccesors() {
	    template.setClO2Unit("l");
	    assertTrue(template.isSetClO2Unit());
	    assertEquals("l", template.getClO2Unit());

	    template.unsetClO2Unit();
	    assertFalse(template.isSetClO2Unit());
	    assertNull(template.getClO2Unit());
	}

	@Test
	public void testClO2MinAccesors() {
	    template.setClO2Min(0);
	    assertTrue(template.isSetClO2Min());
	    assertEquals(0, template.getClO2Min(), 0.0);

	    template.unsetClO2Min();
	    assertFalse(template.isSetClO2Min());
	    assertNull(template.getClO2Min());
	}

	@Test
	public void testClO2MaxAccesors() {
	    template.setClO2Max(7);
	    assertTrue(template.isSetClO2Max());
	    assertEquals(7, template.getClO2Max(), 0.0);

	    template.unsetClO2Max();
	    assertFalse(template.isSetClO2Max());
	    assertNull(template.getClO2Max());
	}

	@Test
	public void testEdtaUnitAccesors() {
	    template.setEdtaUnit("l");
	    assertTrue(template.isSetEdtaUnit());
	    assertEquals("l", template.getEdtaUnit());

	    template.unsetEdtaUnit();
	    assertFalse(template.isSetEdtaUnit());
	    assertNull(template.getEdtaUnit());
	}

	@Test
	public void testEdtaMinAccesors() {
	    template.setEdtaMin(0);
	    assertTrue(template.isSetEdtaMin());
	    assertEquals(0, template.getEdtaMin(), 0.0);

	    template.unsetEdtaMin();
	    assertFalse(template.isSetEdtaMin());
	    assertNull(template.getEdtaMin());
	}

	@Test
	public void testEdtaMaxAccesors() {
	    template.setEdtaMax(7);
	    assertTrue(template.isSetEdtaMax());
	    assertEquals(7, template.getEdtaMax(), 0.0);

	    template.unsetEdtaMax();
	    assertFalse(template.isSetEdtaMax());
	    assertNull(template.getEdtaMax());
	}

	@Test
	public void testHClUnitAccesors() {
	    template.setHClUnit("l");
	    assertTrue(template.isSetHClUnit());
	    assertEquals("l", template.getHClUnit());

	    template.unsetHClUnit();
	    assertFalse(template.isSetHClUnit());
	    assertNull(template.getHClUnit());
	}

	@Test
	public void testHClMinAccesors() {
	    template.setHClMin(0);
	    assertTrue(template.isSetHClMin());
	    assertEquals(0, template.getHClMin(), 0.0);

	    template.unsetHClMin();
	    assertFalse(template.isSetHClMin());
	    assertNull(template.getHClMin());
	}

	@Test
	public void testHClMaxAccesors() {
	    template.setHClMax(7);
	    assertTrue(template.isSetHClMax());
	    assertEquals(7, template.getHClMax(), 0.0);

	    template.unsetHClMax();
	    assertFalse(template.isSetHClMax());
	    assertNull(template.getHClMax());
	}

	@Test
	public void testN2UnitAccesors() {
	    template.setN2Unit("l");
	    assertTrue(template.isSetN2Unit());
	    assertEquals("l", template.getN2Unit());

	    template.unsetN2Unit();
	    assertFalse(template.isSetN2Unit());
	    assertNull(template.getN2Unit());
	}

	@Test
	public void testN2MinAccesors() {
	    template.setN2Min(0);
	    assertTrue(template.isSetN2Min());
	    assertEquals(0, template.getN2Min(), 0.0);

	    template.unsetN2Min();
	    assertFalse(template.isSetN2Min());
	    assertNull(template.getN2Min());
	}

	@Test
	public void testN2MaxAccesors() {
	    template.setN2Max(7);
	    assertTrue(template.isSetN2Max());
	    assertEquals(7, template.getN2Max(), 0.0);

	    template.unsetN2Max();
	    assertFalse(template.isSetN2Max());
	    assertNull(template.getN2Max());
	}

	@Test
	public void testO2UnitAccesors() {
	    template.setO2Unit("l");
	    assertTrue(template.isSetO2Unit());
	    assertEquals("l", template.getO2Unit());

	    template.unsetO2Unit();
	    assertFalse(template.isSetO2Unit());
	    assertNull(template.getO2Unit());
	}

	@Test
	public void testO2MinAccesors() {
	    template.setO2Min(0);
	    assertTrue(template.isSetO2Min());
	    assertEquals(0, template.getO2Min(), 0.0);

	    template.unsetO2Min();
	    assertFalse(template.isSetO2Min());
	    assertNull(template.getO2Min());
	}

	@Test
	public void testO2MaxAccesors() {
	    template.setO2Max(7);
	    assertTrue(template.isSetO2Max());
	    assertEquals(7, template.getO2Max(), 0.0);

	    template.unsetO2Max();
	    assertFalse(template.isSetO2Max());
	    assertNull(template.getO2Max());
	}

	@Test
	public void testAceticAcidUnitAccesors() {
	    template.setAceticAcidUnit("l");
	    assertTrue(template.isSetAceticAcidUnit());
	    assertEquals("l", template.getAceticAcidUnit());

	    template.unsetAceticAcidUnit();
	    assertFalse(template.isSetAceticAcidUnit());
	    assertNull(template.getAceticAcidUnit());
	}

	@Test
	public void testAceticAcidMinAccesors() {
	    template.setAceticAcidMin(0);
	    assertTrue(template.isSetAceticAcidMin());
	    assertEquals(0, template.getAceticAcidMin(), 0.0);

	    template.unsetAceticAcidMin();
	    assertFalse(template.isSetAceticAcidMin());
	    assertNull(template.getAceticAcidMin());
	}

	@Test
	public void testAceticAcidMaxAccesors() {
	    template.setAceticAcidMax(7);
	    assertTrue(template.isSetAceticAcidMax());
	    assertEquals(7, template.getAceticAcidMax(), 0.0);

	    template.unsetAceticAcidMax();
	    assertFalse(template.isSetAceticAcidMax());
	    assertNull(template.getAceticAcidMax());
	}

	@Test
	public void testAdditivesAccesors() {
	    template.setAdditives("no addivitives");
	    assertTrue(template.isSetAdditives());
	    assertEquals("no addivitives", template.getAdditives());

	    template.unsetAdditives();
	    assertFalse(template.isSetAdditives());
	    assertNull(template.getAdditives());
	}

	@Test
	public void testAnaerobicAccesors() {
	    template.setAnaerobic(false);
	    assertTrue(template.isSetAnaerobic());
	    assertFalse(template.getAnaerobic());

	    template.unsetAnaerobic();
	    assertFalse(template.isSetAnaerobic());
	    assertNull(template.getAnaerobic());
	}

	@Test
	public void testAntimicrobialDippingTimeUnitAccesors() {
	    template.setAntimicrobialDippingTimeUnit("s");
	    assertTrue(template.isSetAntimicrobialDippingTimeUnit());
	    assertEquals("s", template.getAntimicrobialDippingTimeUnit());

	    template.unsetAntimicrobialDippingTimeUnit();
	    assertFalse(template.isSetAntimicrobialDippingTimeUnit());
	    assertNull(template.getAntimicrobialDippingTimeUnit());
	}

	@Test
	public void testAntimicrobialDippingTimeMinAccesors() {
	    template.setAntimicrobialDippingTimeMin(0);
	    assertTrue(template.isSetAntimicrobialDippingTimeMin());
	    assertEquals(0, template.getAntimicrobialDippingTimeMin(), 0.0);

	    template.unsetAntimicrobialDippingTimeMin();
	    assertFalse(template.isSetAntimicrobialDippingTimeMin());
	    assertNull(template.getAntimicrobialDippingTimeMin());
	}

	@Test
	public void testAntimicrobialDippingTimeMaxAccesors() {
	    template.setAntimicrobialDippingTimeMax(7);
	    assertTrue(template.isSetAntimicrobialDippingTimeMax());
	    assertEquals(7, template.getAntimicrobialDippingTimeMax(), 0.0);

	    template.unsetAntimicrobialDippingTimeMax();
	    assertFalse(template.isSetAntimicrobialDippingTimeMax());
	    assertNull(template.getAntimicrobialDippingTimeMax());
	}

	@Test
	public void testApplePolyphenolUnitAccesors() {
	    template.setApplePolyphenolUnit("mg");
	    assertTrue(template.isSetApplePolyphenolUnit());
	    assertEquals("mg", template.getApplePolyphenolUnit());

	    template.unsetApplePolyphenolUnit();
	    assertFalse(template.isSetApplePolyphenolUnit());
	    assertNull(template.getApplePolyphenolUnit());
	}

	@Test
	public void testApplePolyphenolMinAccesors() {
	    template.setApplePolyphenolMin(0);
	    assertTrue(template.isSetApplePolyphenolMin());
	    assertEquals(0, template.getApplePolyphenolMin(), 0.0);

	    template.unsetApplePolyphenolMin();
	    assertFalse(template.isSetApplePolyphenolMin());
	    assertNull(template.getApplePolyphenolMin());
	}

	@Test
	public void testApplePolyphenolMaxAccesors() {
	    template.setApplePolyphenolMax(7);
	    assertTrue(template.isSetApplePolyphenolMax());
	    assertEquals(7, template.getApplePolyphenolMax(), 0.0);

	    template.unsetApplePolyphenolMax();
	    assertFalse(template.isSetApplePolyphenolMax());
	    assertNull(template.getApplePolyphenolMax());
	}

	@Test
	public void testAscorbiccAcidUnitAccesors() {
	    template.setAscorbiccAcidUnit("mg");
	    assertTrue(template.isSetAscorbiccAcidUnit());
	    assertEquals("mg", template.getAscorbiccAcidUnit());

	    template.unsetAscorbiccAcidUnit();
	    assertFalse(template.isSetAscorbiccAcidUnit());
	    assertNull(template.getAscorbiccAcidUnit());
	}

	@Test
	public void testAscorbiccAcidMinAccesors() {
	    template.setAscorbiccAcidMin(0);
	    assertTrue(template.isSetAscorbiccAcidMin());
	    assertEquals(0, template.getAscorbiccAcidMin(), 0.0);

	    template.unsetAscorbiccAcidMin();
	    assertFalse(template.isSetAscorbiccAcidMin());
	    assertNull(template.getAscorbiccAcidMin());
	}

	@Test
	public void testAscorbiccAcidMaxAccesors() {
	    template.setAscorbiccAcidMax(7);
	    assertTrue(template.isSetAscorbiccAcidMax());
	    assertEquals(7, template.getAscorbiccAcidMax(), 0.0);

	    template.unsetAscorbiccAcidMax();
	    assertFalse(template.isSetAscorbiccAcidMax());
	    assertNull(template.getAscorbiccAcidMax());
	}

	@Test
	public void testAtrInducedAccesors() {
	    template.setAtrInduced(false);
	    assertTrue(template.isSetAtrInduced());
	    assertFalse(template.getAtrInduced());

	    template.unsetAtrInduced();
	    assertFalse(template.isSetAtrInduced());
	    assertNull(template.getAtrInduced());
	}

	@Test
	public void testAttachmentTimeUnitAccesors() {
	    template.setAttachmentTimeUnit("s");
	    assertTrue(template.isSetAttachmentTimeUnit());
	    assertEquals("s", template.getAttachmentTimeUnit());

	    template.unsetAttachmentTimeUnit();
	    assertFalse(template.isSetAttachmentTimeUnit());
	    assertNull(template.getAttachmentTimeUnit());
	}

	@Test
	public void testAttachmentTimeMinAccesors() {
	    template.setAttachmentTimeMin(0);
	    assertTrue(template.isSetAttachmentTimeMin());
	    assertEquals(0, template.getAttachmentTimeMin(), 0.0);

	    template.unsetAttachmentTimeMin();
	    assertFalse(template.isSetAttachmentTimeMin());
	    assertNull(template.getAttachmentTimeMin());
	}

	@Test
	public void testAttachmentTimeMaxAccesors() {
	    template.setAttachmentTimeMax(7);
	    assertTrue(template.isSetAttachmentTimeMax());
	    assertEquals(7, template.getAttachmentTimeMax(), 0.0);

	    template.unsetAttachmentTimeMax();
	    assertFalse(template.isSetAttachmentTimeMax());
	    assertNull(template.getAttachmentTimeMax());
	}

	@Test
	public void testBeanOilUnitAccesors() {
	    template.setBeanOilUnit("l");
	    assertTrue(template.isSetBeanOilUnit());
	    assertEquals("l", template.getBeanOilUnit());

	    template.unsetBeanOilUnit();
	    assertFalse(template.isSetBeanOilUnit());
	    assertNull(template.getBeanOilUnit());
	}

	@Test
	public void testBeanOilMinAccesors() {
	    template.setBeanOilMin(0);
	    assertTrue(template.isSetBeanOilMin());
	    assertEquals(0, template.getBeanOilMin(), 0.0);

	    template.unsetBeanOilMin();
	    assertFalse(template.isSetBeanOilMin());
	    assertNull(template.getBeanOilMin());
	}

	@Test
	public void testBeanOilMaxAccesors() {
	    template.setBeanOilMax(7);
	    assertTrue(template.isSetBeanOilMax());
	    assertEquals(7, template.getBeanOilMax(), 0.0);

	    template.unsetBeanOilMax();
	    assertFalse(template.isSetBeanOilMax());
	    assertNull(template.getBeanOilMax());
	}

	@Test
	public void testBenzoicAcidUnitAccesors() {
	    template.setBenzoicAcidUnit("l");
	    assertTrue(template.isSetBenzoicAcidUnit());
	    assertEquals("l", template.getBenzoicAcidUnit());

	    template.unsetBenzoicAcidUnit();
	    assertFalse(template.isSetBenzoicAcidUnit());
	    assertNull(template.getBenzoicAcidUnit());
	}

	@Test
	public void testBenzoicAcidMinAccesors() {
	    template.setBenzoicAcidMin(0);
	    assertTrue(template.isSetBenzoicAcidMin());
	    assertEquals(0, template.getBenzoicAcidMin(), 0.0);

	    template.unsetBenzoicAcidMin();
	    assertFalse(template.isSetBenzoicAcidMin());
	    assertNull(template.getBenzoicAcidMin());
	}

	@Test
	public void testBenzoicAcidMaxAccesors() {
	    template.setBenzoicAcidMax(7);
	    assertTrue(template.isSetBenzoicAcidMax());
	    assertEquals(7, template.getBenzoicAcidMax(), 0.0);

	    template.unsetBenzoicAcidMax();
	    assertFalse(template.isSetBenzoicAcidMax());
	    assertNull(template.getBenzoicAcidMax());
	}

	@Test
	public void testBetaineUnitAccesors() {
	    template.setBetaineUnit("l");
	    assertTrue(template.isSetBetaineUnit());
	    assertEquals("l", template.getBetaineUnit());

	    template.unsetBetaineUnit();
	    assertFalse(template.isSetBetaineUnit());
	    assertNull(template.getBetaineUnit());
	}

	@Test
	public void testBetaineMinAccesors() {
	    template.setBetaineMin(0);
	    assertTrue(template.isSetBetaineMin());
	    assertEquals(0, template.getBetaineMin(), 0.0);

	    template.unsetBetaineMin();
	    assertFalse(template.isSetBetaineMin());
	    assertNull(template.getBetaineMin());
	}

	@Test
	public void testBetaineMaxAccesors() {
	    template.setBetaineMax(7);
	    assertTrue(template.isSetBetaineMax());
	    assertEquals(7, template.getBetaineMax(), 0.0);

	    template.unsetBetaineMax();
	    assertFalse(template.isSetBetaineMax());
	    assertNull(template.getBetaineMax());
	}

	@Test
	public void testCarvacrolUnitAccesors() {
	    template.setCarvacrolUnit("l");
	    assertTrue(template.isSetCarvacrolUnit());
	    assertEquals("l", template.getCarvacrolUnit());

	    template.unsetCarvacrolUnit();
	    assertFalse(template.isSetCarvacrolUnit());
	    assertNull(template.getCarvacrolUnit());
	}

	@Test
	public void testCarvacrolMinAccesors() {
	    template.setCarvacrolMin(0);
	    assertTrue(template.isSetCarvacrolMin());
	    assertEquals(0, template.getCarvacrolMin(), 0.0);

	    template.unsetCarvacrolMin();
	    assertFalse(template.isSetCarvacrolMin());
	    assertNull(template.getCarvacrolMin());
	}

	@Test
	public void testCarvacrolMaxAccesors() {
	    template.setCarvacrolMax(7);
	    assertTrue(template.isSetCarvacrolMax());
	    assertEquals(7, template.getCarvacrolMax(), 0.0);

	    template.unsetCarvacrolMax();
	    assertFalse(template.isSetCarvacrolMax());
	    assertNull(template.getCarvacrolMax());
	}

	@Test
	public void testChitosanUnitAccesors() {
	    template.setChitosanUnit("l");
	    assertTrue(template.isSetChitosanUnit());
	    assertEquals("l", template.getChitosanUnit());

	    template.unsetChitosanUnit();
	    assertFalse(template.isSetChitosanUnit());
	    assertNull(template.getChitosanUnit());
	}

	@Test
	public void testChitosanMinAccesors() {
	    template.setChitosanMin(0);
	    assertTrue(template.isSetChitosanMin());
	    assertEquals(0, template.getChitosanMin(), 0.0);

	    template.unsetChitosanMin();
	    assertFalse(template.isSetChitosanMin());
	    assertNull(template.getChitosanMin());
	}

	@Test
	public void testChitosanMaxAccesors() {
	    template.setChitosanMax(7);
	    assertTrue(template.isSetChitosanMax());
	    assertEquals(7, template.getChitosanMax(), 0.0);

	    template.unsetChitosanMax();
	    assertFalse(template.isSetChitosanMax());
	    assertNull(template.getChitosanMax());
	}

	@Test
	public void testCinnamaldehydeUnitAccesors() {
	    template.setCinnamaldehydeUnit("l");
	    assertTrue(template.isSetCinnamaldehydeUnit());
	    assertEquals("l", template.getCinnamaldehydeUnit());

	    template.unsetCinnamaldehydeUnit();
	    assertFalse(template.isSetCinnamaldehydeUnit());
	    assertNull(template.getCinnamaldehydeUnit());
	}

	@Test
	public void testCinnamaldehydeMinAccesors() {
	    template.setCinnamaldehydeMin(0);
	    assertTrue(template.isSetCinnamaldehydeMin());
	    assertEquals(0, template.getCinnamaldehydeMin(), 0.0);

	    template.unsetCinnamaldehydeMin();
	    assertFalse(template.isSetCinnamaldehydeMin());
	    assertNull(template.getCinnamaldehydeMin());
	}

	@Test
	public void testCinnamaldehydeMaxAccesors() {
	    template.setCinnamaldehydeMax(7);
	    assertTrue(template.isSetCinnamaldehydeMax());
	    assertEquals(7, template.getCinnamaldehydeMax(), 0.0);

	    template.unsetCinnamaldehydeMax();
	    assertFalse(template.isSetCinnamaldehydeMax());
	    assertNull(template.getCinnamaldehydeMax());
	}

	@Test
	public void testCitricAcidUnitAccesors() {
	    template.setCitricAcidUnit("l");
	    assertTrue(template.isSetCitricAcidUnit());
	    assertEquals("l", template.getCitricAcidUnit());

	    template.unsetCitricAcidUnit();
	    assertFalse(template.isSetCitricAcidUnit());
	    assertNull(template.getCitricAcidUnit());
	}

	@Test
	public void testCitricAcidMinAccesors() {
	    template.setCitricAcidMin(0);
	    assertTrue(template.isSetCitricAcidMin());
	    assertEquals(0, template.getCitricAcidMin(), 0.0);

	    template.unsetCitricAcidMin();
	    assertFalse(template.isSetCitricAcidMin());
	    assertNull(template.getCitricAcidMin());
	}

	@Test
	public void testCitricAcidMaxAccesors() {
	    template.setCitricAcidMax(7);
	    assertTrue(template.isSetCitricAcidMax());
	    assertEquals(7, template.getCitricAcidMax(), 0.0);

	    template.unsetCitricAcidMax();
	    assertFalse(template.isSetCitricAcidMax());
	    assertNull(template.getCitricAcidMax());
	}

	// TODO: need to implement them before :S
//	@Test
//	public void testCoCultureAccesors() {
//	}

	@Test
	public void testCuredAccesors() {
	    template.setCured(true);
	    assertTrue(template.isSetCured());
	    assertTrue(template.getCured());

	    template.unsetCured();
	    assertFalse(template.isSetCured());
	    assertNull(template.getCured());
	}

	@Test
	public void testCutAccesors() {
	    template.setCut(true);
	    assertTrue(template.isSetCut());
	    assertTrue(template.getCut());

	    template.unsetCut();
	    assertFalse(template.isSetCut());
	    assertNull(template.getCut());
	}

	@Test
	public void testDesiredReductionUnitAccesors() {
	    template.setDesiredReductionUnit("l");
	    assertTrue(template.isSetDesiredReductionUnit());
	    assertEquals("l", template.getDesiredReductionUnit());

	    template.unsetDesiredReductionUnit();
	    assertFalse(template.isSetDesiredReductionUnit());
	    assertNull(template.getDesiredReductionUnit());
	}

	@Test
	public void testDesiredReductionMinAccesors() {
	    template.setDesiredReductionMin(0);
	    assertTrue(template.isSetDesiredReductionMin());
	    assertEquals(0, template.getDesiredReductionMin(), 0.0);

	    template.unsetDesiredReductionMin();
	    assertFalse(template.isSetDesiredReductionMin());
	    assertNull(template.getDesiredReductionMin());
	}

	@Test
	public void testDesiredReductionMaxAccesors() {
	    template.setDesiredReductionMax(7);
	    assertTrue(template.isSetDesiredReductionMax());
	    assertEquals(7, template.getDesiredReductionMax(), 0.0);

	    template.unsetDesiredReductionMax();
	    assertFalse(template.isSetDesiredReductionMax());
	    assertNull(template.getDesiredReductionMax());
	}

	@Test
	public void testDiaceticAcidUnitAccesors() {
	    template.setDiaceticAcidUnit("l");
	    assertTrue(template.isSetDiaceticAcidUnit());
	    assertEquals("l", template.getDiaceticAcidUnit());

	    template.unsetDiaceticAcidUnit();
	    assertFalse(template.isSetDiaceticAcidUnit());
	    assertNull(template.getDiaceticAcidUnit());
	}

	@Test
	public void testDiaceticAcidMinAccesors() {
	    template.setDiaceticAcidMin(0);
	    assertTrue(template.isSetDiaceticAcidMin());
	    assertEquals(0, template.getDiaceticAcidMin(), 0.0);

	    template.unsetDiaceticAcidMin();
	    assertFalse(template.isSetDiaceticAcidMin());
	    assertNull(template.getDiaceticAcidMin());
	}

	@Test
	public void testDiaceticAcidMaxAccesors() {
	    template.setDiaceticAcidMax(7);
	    assertTrue(template.isSetDiaceticAcidMax());
	    assertEquals(7, template.getDiaceticAcidMax(), 0.0);

	    template.unsetDiaceticAcidMax();
	    assertFalse(template.isSetDiaceticAcidMax());
	    assertNull(template.getDiaceticAcidMax());
	}

	@Test
	public void testDisaccharideUnitAccesors() {
	    template.setDisaccharideUnit("l");
	    assertTrue(template.isSetDisaccharideUnit());
	    assertEquals("l", template.getDisaccharideUnit());

	    template.unsetDisaccharideUnit();
	    assertFalse(template.isSetDisaccharideUnit());
	    assertNull(template.getDisaccharideUnit());
	}

	@Test
	public void testDisaccharideMinAccesors() {
	    template.setDisaccharideMin(0);
	    assertTrue(template.isSetDisaccharideMin());
	    assertEquals(0, template.getDisaccharideMin(), 0.0);

	    template.unsetDisaccharideMin();
	    assertFalse(template.isSetDisaccharideMin());
	    assertNull(template.getDisaccharideMin());
	}

	@Test
	public void testDisaccharideMaxAccesors() {
	    template.setDisaccharideMax(7);
	    assertTrue(template.isSetDisaccharideMax());
	    assertEquals(7, template.getDisaccharideMax(), 0.0);

	    template.unsetDisaccharideMax();
	    assertFalse(template.isSetDisaccharideMax());
	    assertNull(template.getDisaccharideMax());
	}

	@Test
	public void testDriedAccesors() {
	    template.setDried(false);
	    assertTrue(template.isSetDried());
	    assertFalse(template.getDried());

	    template.unsetDried();
	    assertFalse(template.isSetDried());
	    assertNull(template.getDried());
	}

	@Test
	public void testEthanolUnitAccesors() {
	    template.setEthanolUnit("l");
	    assertTrue(template.isSetEthanolUnit());
	    assertEquals("l", template.getEthanolUnit());

	    template.unsetEthanolUnit();
	    assertFalse(template.isSetEthanolUnit());
	    assertNull(template.getEthanolUnit());
	}

	@Test
	public void testEthanolMinAccesors() {
	    template.setEthanolMin(0);
	    assertTrue(template.isSetEthanolMin());
	    assertEquals(0, template.getEthanolMin(), 0.0);

	    template.unsetEthanolMin();
	    assertFalse(template.isSetEthanolMin());
	    assertNull(template.getEthanolMin());
	}

	@Test
	public void testEthanolMaxAccesors() {
	    template.setEthanolMax(7);
	    assertTrue(template.isSetEthanolMax());
	    assertEquals(7, template.getEthanolMax(), 0.0);

	    template.unsetEthanolMax();
	    assertFalse(template.isSetEthanolMax());
	    assertNull(template.getEthanolMax());
	}

	@Test
	public void testExpInoculatedAccesors() {
	    template.setExpInoculated(true);
	    assertTrue(template.isSetExpInoculated());
	    assertTrue(template.getExpInoculated());

	    template.unsetExpInoculated();
	    assertFalse(template.isSetExpInoculated());
	    assertNull(template.getExpInoculated());
	}

	@Test 
	public void testFatUnitAccesors() {
	    template.setFatUnit("%");
	    assertTrue(template.isSetFatUnit());
	    assertEquals("%", template.getFatUnit());

	    template.unsetFatUnit();
	    assertFalse(template.isSetFatUnit());
	    assertNull(template.getFatUnit());
	}

	@Test
	public void testFatMinAccesors() {
	    template.setFatMin(0);
	    assertTrue(template.isSetFatMin());
	    assertEquals(0, template.getFatMin(), 0.0);

	    template.unsetFatMin();
	    assertFalse(template.isSetFatMin());
	    assertNull(template.getFatMin());
	}

	@Test
	public void testFatMaxAccesors() {
	    template.setFatMax(7);
	    assertTrue(template.isSetFatMax());
	    assertEquals(7, template.getFatMax(), 0.0);

	    template.unsetFatMax();
	    assertFalse(template.isSetFatMax());
	    assertNull(template.getFatMax());
	}

	@Test
	public void testFrozenAccesors() {
	    template.setFrozen(true);
	    assertTrue(template.isSetFrozen());
	    assertTrue(template.getFrozen());

	    template.unsetFrozen();
	    assertFalse(template.isSetFrozen());
	    assertNull(template.getFrozen());
	}

	@Test 
	public void testFructoseUnitAccesors() {
	    template.setFructoseUnit("%");
	    assertTrue(template.isSetFructoseUnit());
	    assertEquals("%", template.getFructoseUnit());

	    template.unsetFructoseUnit();
	    assertFalse(template.isSetFructoseUnit());
	    assertNull(template.getFructoseUnit());
	}

	@Test
	public void testFructoseMinAccesors() {
	    template.setFructoseMin(0);
	    assertTrue(template.isSetFructoseMin());
	    assertEquals(0, template.getFructoseMin(), 0.0);

	    template.unsetFructoseMin();
	    assertFalse(template.isSetFructoseMin());
	    assertNull(template.getFructoseMin());
	}

	@Test
	public void testFructoseMaxAccesors() {
	    template.setFructoseMax(7);
	    assertTrue(template.isSetFructoseMax());
	    assertEquals(7, template.getFructoseMax(), 0.0);

	    template.unsetFructoseMax();
	    assertFalse(template.isSetFructoseMax());
	    assertNull(template.getFructoseMax());
	}

	@Test 
	public void testGelMicrostructureUnitAccesors() {
	    template.setGelMicrostructureUnit("%");
	    assertTrue(template.isSetGelMicrostructureUnit());
	    assertEquals("%", template.getGelMicrostructureUnit());

	    template.unsetGelMicrostructureUnit();
	    assertFalse(template.isSetGelMicrostructureUnit());
	    assertNull(template.getGelMicrostructureUnit());
	}

	@Test
	public void testGelMicrostructureMinAccesors() {
	    template.setGelMicrostructureMin(0);
	    assertTrue(template.isSetGelMicrostructureMin());
	    assertEquals(0, template.getGelMicrostructureMin(), 0.0);

	    template.unsetGelMicrostructureMin();
	    assertFalse(template.isSetGelMicrostructureMin());
	    assertNull(template.getGelMicrostructureMin());
	}

	@Test
	public void testGelMicrostructureMaxAccesors() {
	    template.setGelMicrostructureMax(7);
	    assertTrue(template.isSetGelMicrostructureMax());
	    assertEquals(7, template.getGelMicrostructureMax(), 0.0);

	    template.unsetGelMicrostructureMax();
	    assertFalse(template.isSetGelMicrostructureMax());
	    assertNull(template.getGelMicrostructureMax());
	}

	@Test 
	public void testGlucoseUnitAccesors() {
	    template.setGlucoseUnit("%");
	    assertTrue(template.isSetGlucoseUnit());
	    assertEquals("%", template.getGlucoseUnit());

	    template.unsetGlucoseUnit();
	    assertFalse(template.isSetGlucoseUnit());
	    assertNull(template.getGlucoseUnit());
	}

	@Test
	public void testGlucoseMinAccesors() {
	    template.setGlucoseMin(0);
	    assertTrue(template.isSetGlucoseMin());
	    assertEquals(0, template.getGlucoseMin(), 0.0);

	    template.unsetGlucoseMin();
	    assertFalse(template.isSetGlucoseMin());
	    assertNull(template.getGlucoseMin());
	}

	@Test
	public void testGlucoseMaxAccesors() {
	    template.setGlucoseMax(7);
	    assertTrue(template.isSetGlucoseMax());
	    assertEquals(7, template.getGlucoseMax(), 0.0);

	    template.unsetGlucoseMax();
	    assertFalse(template.isSetGlucoseMax());
	    assertNull(template.getGlucoseMax());
	}

	@Test 
	public void testGlycerolUnitAccesors() {
	    template.setGlycerolUnit("%");
	    assertTrue(template.isSetGlycerolUnit());
	    assertEquals("%", template.getGlycerolUnit());

	    template.unsetGlycerolUnit();
	    assertFalse(template.isSetGlycerolUnit());
	    assertNull(template.getGlycerolUnit());
	}

	@Test
	public void testGlycerolMinAccesors() {
	    template.setGlycerolMin(0);
	    assertTrue(template.isSetGlycerolMin());
	    assertEquals(0, template.getGlycerolMin(), 0.0);

	    template.unsetGlycerolMin();
	    assertFalse(template.isSetGlycerolMin());
	    assertNull(template.getGlycerolMin());
	}

	@Test
	public void testGlycerolMaxAccesors() {
	    template.setGlycerolMax(7);
	    assertTrue(template.isSetGlycerolMax());
	    assertEquals(7, template.getGlycerolMax(), 0.0);

	    template.unsetGlycerolMax();
	    assertFalse(template.isSetGlycerolMax());
	    assertNull(template.getGlycerolMax());
	}

	@Test 
	public void testGreenTeaGroundPowderUnitAccesors() {
	    template.setGreenTeaGroundPowderUnit("%");
	    assertTrue(template.isSetGreenTeaGroundPowderUnit());
	    assertEquals("%", template.getGreenTeaGroundPowderUnit());

	    template.unsetGreenTeaGroundPowderUnit();
	    assertFalse(template.isSetGreenTeaGroundPowderUnit());
	    assertNull(template.getGreenTeaGroundPowderUnit());
	}

	@Test
	public void testGreenTeaGroundPowderMinAccesors() {
	    template.setGreenTeaGroundPowderMin(0);
	    assertTrue(template.isSetGreenTeaGroundPowderMin());
	    assertEquals(0, template.getGreenTeaGroundPowderMin(), 0.0);

	    template.unsetGreenTeaGroundPowderMin();
	    assertFalse(template.isSetGreenTeaGroundPowderMin());
	    assertNull(template.getGreenTeaGroundPowderMin());
	}

	@Test
	public void testGreenTeaGroundPowderMaxAccesors() {
	    template.setGreenTeaGroundPowderMax(7);
	    assertTrue(template.isSetGreenTeaGroundPowderMax());
	    assertEquals(7, template.getGreenTeaGroundPowderMax(), 0.0);

	    template.unsetGreenTeaGroundPowderMax();
	    assertFalse(template.isSetGreenTeaGroundPowderMax());
	    assertNull(template.getGreenTeaGroundPowderMax());
	}

	@Test 
	public void testGreenTeaLeafUnitAccesors() {
	    template.setGreenTeaLeafUnit("%");
	    assertTrue(template.isSetGreenTeaLeafUnit());
	    assertEquals("%", template.getGreenTeaLeafUnit());

	    template.unsetGreenTeaLeafUnit();
	    assertFalse(template.isSetGreenTeaLeafUnit());
	    assertNull(template.getGreenTeaLeafUnit());
	}

	@Test
	public void testGreenTeaLeafMinAccesors() {
	    template.setGreenTeaLeafMin(0);
	    assertTrue(template.isSetGreenTeaLeafMin());
	    assertEquals(0, template.getGreenTeaLeafMin(), 0.0);

	    template.unsetGreenTeaLeafMin();
	    assertFalse(template.isSetGreenTeaLeafMin());
	    assertNull(template.getGreenTeaLeafMin());
	}

	@Test
	public void testGreenTeaLeafMaxAccesors() {
	    template.setGreenTeaLeafMax(7);
	    assertTrue(template.isSetGreenTeaLeafMax());
	    assertEquals(7, template.getGreenTeaLeafMax(), 0.0);

	    template.unsetGreenTeaLeafMax();
	    assertFalse(template.isSetGreenTeaLeafMax());
	    assertNull(template.getGreenTeaLeafMax());
	}

	@Test 
	public void testGreenTeaWaterExtractUnitAccesors() {
	    template.setGreenTeaWaterExtractUnit("%");
	    assertTrue(template.isSetGreenTeaWaterExtractUnit());
	    assertEquals("%", template.getGreenTeaWaterExtractUnit());

	    template.unsetGreenTeaWaterExtractUnit();
	    assertFalse(template.isSetGreenTeaWaterExtractUnit());
	    assertNull(template.getGreenTeaWaterExtractUnit());
	}

	@Test
	public void testGreenTeaWaterExtractMinAccesors() {
	    template.setGreenTeaWaterExtractMin(0);
	    assertTrue(template.isSetGreenTeaWaterExtractMin());
	    assertEquals(0, template.getGreenTeaWaterExtractMin(), 0.0);

	    template.unsetGreenTeaWaterExtractMin();
	    assertFalse(template.isSetGreenTeaWaterExtractMin());
	    assertNull(template.getGreenTeaWaterExtractMin());
	}

	@Test
	public void testGreenTeaWaterExtractMaxAccesors() {
	    template.setGreenTeaWaterExtractMax(7);
	    assertTrue(template.isSetGreenTeaWaterExtractMax());
	    assertEquals(7, template.getGreenTeaWaterExtractMax(), 0.0);

	    template.unsetGreenTeaWaterExtractMax();
	    assertFalse(template.isSetGreenTeaWaterExtractMax());
	    assertNull(template.getGreenTeaWaterExtractMax());
	}

	@Test
	public void testHeatedAccesors() {
	    template.setHeated(false);
	    assertTrue(template.isSetHeated());
	    assertFalse(template.getHeated());

	    template.unsetHeated();
	    assertFalse(template.isSetHeated());
	    assertNull(template.getHeated());
	}

	@Test 
	public void testHexosesUnitAccesors() {
	    template.setHexosesUnit("%");
	    assertTrue(template.isSetHexosesUnit());
	    assertEquals("%", template.getHexosesUnit());

	    template.unsetHexosesUnit();
	    assertFalse(template.isSetHexosesUnit());
	    assertNull(template.getHexosesUnit());
	}

	@Test
	public void testHexosesMinAccesors() {
	    template.setHexosesMin(0);
	    assertTrue(template.isSetHexosesMin());
	    assertEquals(0, template.getHexosesMin(), 0.0);

	    template.unsetHexosesMin();
	    assertFalse(template.isSetHexosesMin());
	    assertNull(template.getHexosesMin());
	}

	@Test
	public void testHexosesMaxAccesors() {
	    template.setHexosesMax(7);
	    assertTrue(template.isSetHexosesMax());
	    assertEquals(7, template.getHexosesMax(), 0.0);

	    template.unsetHexosesMax();
	    assertFalse(template.isSetHexosesMax());
	    assertNull(template.getHexosesMax());
	}

	@Test
	public void testIndigenous() {
	    template.setIndigenous(false);
	    assertTrue(template.isSetIndigenous());
	    assertFalse(template.getIndigenous());

	    template.unsetIndigenous();
	    assertFalse(template.isSetIndigenous());
	    assertNull(template.getIndigenous());
	}

	@Test 
	public void testInitLevelHistamineUnitAccesors() {
	    template.setInitLevelHistamineUnit("%");
	    assertTrue(template.isSetInitLevelHistamineUnit());
	    assertEquals("%", template.getInitLevelHistamineUnit());

	    template.unsetInitLevelHistamineUnit();
	    assertFalse(template.isSetInitLevelHistamineUnit());
	    assertNull(template.getInitLevelHistamineUnit());
	}

	@Test
	public void testInitLevelHistamineMinAccesors() {
	    template.setInitLevelHistamineMin(0);
	    assertTrue(template.isSetInitLevelHistamineMin());
	    assertEquals(0, template.getInitLevelHistamineMin(), 0.0);

	    template.unsetInitLevelHistamineMin();
	    assertFalse(template.isSetInitLevelHistamineMin());
	    assertNull(template.getInitLevelHistamineMin());
	}

	@Test
	public void testInitLevelHistamineMaxAccesors() {
	    template.setInitLevelHistamineMax(7);
	    assertTrue(template.isSetInitLevelHistamineMax());
	    assertEquals(7, template.getInitLevelHistamineMax(), 0.0);

	    template.unsetInitLevelHistamineMax();
	    assertFalse(template.isSetInitLevelHistamineMax());
	    assertNull(template.getInitLevelHistamineMax());
	}

	@Test 
	public void testInitLevelHistidineUnitAccesors() {
	    template.setInitLevelHistidineUnit("%");
	    assertTrue(template.isSetInitLevelHistidineUnit());
	    assertEquals("%", template.getInitLevelHistidineUnit());

	    template.unsetInitLevelHistidineUnit();
	    assertFalse(template.isSetInitLevelHistidineUnit());
	    assertNull(template.getInitLevelHistidineUnit());
	}

	@Test
	public void testInitLevelHistidineMinAccesors() {
	    template.setInitLevelHistidineMin(0);
	    assertTrue(template.isSetInitLevelHistidineMin());
	    assertEquals(0, template.getInitLevelHistidineMin(), 0.0);

	    template.unsetInitLevelHistidineMin();
	    assertFalse(template.isSetInitLevelHistidineMin());
	    assertNull(template.getInitLevelHistidineMin());
	}

	@Test
	public void testInitLevelHistidineMaxAccesors() {
	    template.setInitLevelHistidineMax(7);
	    assertTrue(template.isSetInitLevelHistidineMax());
	    assertEquals(7, template.getInitLevelHistidineMax(), 0.0);

	    template.unsetInitLevelHistidineMax();
	    assertFalse(template.isSetInitLevelHistidineMax());
	    assertNull(template.getInitLevelHistidineMax());
	}

	@Test 
	public void testInjuredUnitAccesors() {
	    template.setInjuredUnit("%");
	    assertTrue(template.isSetInjuredUnit());
	    assertEquals("%", template.getInjuredUnit());

	    template.unsetInjuredUnit();
	    assertFalse(template.isSetInjuredUnit());
	    assertNull(template.getInjuredUnit());
	}

	@Test
	public void testInjuredMinAccesors() {
	    template.setInjuredMin(0);
	    assertTrue(template.isSetInjuredMin());
	    assertEquals(0, template.getInjuredMin(), 0.0);

	    template.unsetInjuredMin();
	    assertFalse(template.isSetInjuredMin());
	    assertNull(template.getInjuredMin());
	}

	@Test
	public void testInjuredMaxAccesors() {
	    template.setInjuredMax(7);
	    assertTrue(template.isSetInjuredMax());
	    assertEquals(7, template.getInjuredMax(), 0.0);

	    template.unsetInjuredMax();
	    assertFalse(template.isSetInjuredMax());
	    assertNull(template.getInjuredMax());
	}

	@Test
	public void testIrradiated() {
	    template.setIrradiated(false);
	    assertTrue(template.isSetIrradiated());
	    assertFalse(template.getIrradiated());

	    template.unsetIrradiated();
	    assertFalse(template.isSetIrradiated());
	    assertNull(template.getIrradiated());
	}

	@Test 
	public void testIrradiationUnitAccesors() {
	    template.setIrradiationUnit("%");
	    assertTrue(template.isSetIrradiationUnit());
	    assertEquals("%", template.getIrradiationUnit());

	    template.unsetIrradiationUnit();
	    assertFalse(template.isSetIrradiationUnit());
	    assertNull(template.getIrradiationUnit());
	}

	@Test
	public void testIrradiationMinAccesors() {
	    template.setIrradiationMin(0);
	    assertTrue(template.isSetIrradiationMin());
	    assertEquals(0, template.getIrradiationMin(), 0.0);

	    template.unsetIrradiationMin();
	    assertFalse(template.isSetIrradiationMin());
	    assertNull(template.getIrradiationMin());
	}

	@Test
	public void testIrradiationMaxAccesors() {
	    template.setIrradiationMax(7);
	    assertTrue(template.isSetIrradiationMax());
	    assertEquals(7, template.getIrradiationMax(), 0.0);

	    template.unsetIrradiationMax();
	    assertFalse(template.isSetIrradiationMax());
	    assertNull(template.getIrradiationMax());
	}

	@Test 
	public void testLacticAcidUnitAccesors() {
	    template.setLacticAcidUnit("%");
	    assertTrue(template.isSetLacticAcidUnit());
	    assertEquals("%", template.getLacticAcidUnit());

	    template.unsetLacticAcidUnit();
	    assertFalse(template.isSetLacticAcidUnit());
	    assertNull(template.getLacticAcidUnit());
	}

	@Test
	public void testLacticAcidMinAccesors() {
	    template.setLacticAcidMin(0);
	    assertTrue(template.isSetLacticAcidMin());
	    assertEquals(0, template.getLacticAcidMin(), 0.0);

	    template.unsetLacticAcidMin();
	    assertFalse(template.isSetLacticAcidMin());
	    assertNull(template.getLacticAcidMin());
	}

	@Test
	public void testLacticAcidMaxAccesors() {
	    template.setLacticAcidMax(7);
	    assertTrue(template.isSetLacticAcidMax());
	    assertEquals(7, template.getLacticAcidMax(), 0.0);

	    template.unsetLacticAcidMax();
	    assertFalse(template.isSetLacticAcidMax());
	    assertNull(template.getLacticAcidMax());
	}

	@Test
	public void testLacticBacteriaFermented() {
	    template.setLacticBacteriaFermented(false);
	    assertTrue(template.isSetLacticBacteriaFermented());
	    assertFalse(template.getLacticBacteriaFermented());

	    template.unsetLacticBacteriaFermented();
	    assertFalse(template.isSetLacticBacteriaFermented());
	    assertNull(template.getLacticBacteriaFermented());
	}

	@Test 
	public void testLauricidinUnitAccesors() {
	    template.setLauricidinUnit("%");
	    assertTrue(template.isSetLauricidinUnit());
	    assertEquals("%", template.getLauricidinUnit());

	    template.unsetLauricidinUnit();
	    assertFalse(template.isSetLauricidinUnit());
	    assertNull(template.getLauricidinUnit());
	}

	@Test
	public void testLauricidinMinAccesors() {
	    template.setLauricidinMin(0);
	    assertTrue(template.isSetLauricidinMin());
	    assertEquals(0, template.getLauricidinMin(), 0.0);

	    template.unsetLauricidinMin();
	    assertFalse(template.isSetLauricidinMin());
	    assertNull(template.getLauricidinMin());
	}

	@Test
	public void testLauricidinMaxAccesors() {
	    template.setLauricidinMax(7);
	    assertTrue(template.isSetLauricidinMax());
	    assertEquals(7, template.getLauricidinMax(), 0.0);

	    template.unsetLauricidinMax();
	    assertFalse(template.isSetLauricidinMax());
	    assertNull(template.getLauricidinMax());
	}

	@Test 
	public void testMalicAcidUnitAccesors() {
	    template.setMalicAcidUnit("%");
	    assertTrue(template.isSetMalicAcidUnit());
	    assertEquals("%", template.getMalicAcidUnit());

	    template.unsetMalicAcidUnit();
	    assertFalse(template.isSetMalicAcidUnit());
	    assertNull(template.getMalicAcidUnit());
	}

	@Test
	public void testMalicAcidMinAccesors() {
	    template.setMalicAcidMin(0);
	    assertTrue(template.isSetMalicAcidMin());
	    assertEquals(0, template.getMalicAcidMin(), 0.0);

	    template.unsetMalicAcidMin();
	    assertFalse(template.isSetMalicAcidMin());
	    assertNull(template.getMalicAcidMin());
	}

	@Test
	public void testMalicAcidMaxAccesors() {
	    template.setMalicAcidMax(7);
	    assertTrue(template.isSetMalicAcidMax());
	    assertEquals(7, template.getMalicAcidMax(), 0.0);

	    template.unsetMalicAcidMax();
	    assertFalse(template.isSetMalicAcidMax());
	    assertNull(template.getMalicAcidMax());
	}
}
