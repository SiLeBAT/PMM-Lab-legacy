package de.bund.bfr.knime.pmm.openfsmr;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;

public class FSMRTemplateSettingsTest {

	private final FSMRTemplate template = new FSMRTemplateImpl();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

	@Before
	public void setUp() {
		try {
			template.setModelName("Salmonellaspp_GroundBeef_Temp_GrowthModel...");
			template.setModelId("PMM-Lab_342586849");
			template.setModelLink(new URL("https://drive.google.com/open?id=0B06JrEEq34hSaEt5UFVIMFM3M0k"));
			template.setOrganismName("Salmonella spp.");
			template.setOrganismDetails("broth");
			template.setMatrixName("Beef");
			template.setMatrixDetails("(in: ground beef)");
			template.setCreator("Plaza Rodriguez et al.");
			template.setFamilyName("Baranyi models");
			template.setContact("some contact data");
			template.setReferenceDescription(
					"Juneja, Vijay K. et al., 2009. " + "Mathematical modeling of growth of Salmonella in raw "
							+ "ground beef under isothermal conditions from 10 to 45°C. "
							+ "International Journal of Food Microbiology ():  ff.");
			template.setReferenceDescriptionLink(
					new URL("http://smas.chemeng.ntua.gr/miram/files/publ_237_10_2_2005.pdf"));
			template.setCreatedDate(new GregorianCalendar(2014, Calendar.JANUARY, 1).getTime());
			template.setModifiedDate(new GregorianCalendar(2015, Calendar.DECEMBER, 1).getTime());
			template.setRights("CC-BY-NC-SA");
			template.setNotes("not curated");
			template.setCurationStatus("curated");
			template.setModelType(ModelType.TWO_STEP_TERTIARY_MODEL);
			template.setModelSubject(ModelClass.GROWTH);
			template.setFoodProcess("cooking");
			template.setDependentVariable("Concentration");
			template.setDependentVariableUnit("ln");
			template.setDependentVariableMin(0.0);
			template.setDependentVariableMax(10.0);
			template.setIndependentVariables(new String[] { "time", "temperature" });
			template.setIndependentVariablesUnits(new String[] { "s", "°C" });
			template.setIndependentVariablesMins(new double[] { 0, 1 });
			template.setIndependentVariablesMaxs(new double[] { 0, 1 });
			template.setHasData(false);
		} catch (MalformedURLException e) {
			// passed url here are correct
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testTemplate() {
		FSMRTemplateSettings settings = new FSMRTemplateSettings();
		testInexistenceInTemplate(settings.getTemplate());

		settings.setTemplate(template);
		testExistenceInTemplate(settings.getTemplate());
	}

	@Test
	public void testLoadFromNodeSettings() {
		// test empty settings
		NodeSettings emptySettings = new NodeSettings("emptySettings");
		emptySettings.addString("Model name", null);
		emptySettings.addString("Model id", null);
		emptySettings.addString("Model link", null);
		emptySettings.addString("Organism name", null);
		emptySettings.addString("Organism detail", null);
		emptySettings.addString("Matrix name", null);
		emptySettings.addString("Matrix details", null);
		emptySettings.addString("Creator", null);
		emptySettings.addString("Family name", null);
		emptySettings.addString("Contact", null);
		emptySettings.addString("Reference description", null);
		emptySettings.addString("Reference description link", null);
		emptySettings.addString("Created date", null);
		emptySettings.addString("Modified date", null);
		emptySettings.addString("Rights", null);
		emptySettings.addString("Notes", null);
		emptySettings.addString("Curation status", null);
		emptySettings.addString("Model type", null);
		emptySettings.addString("Model subject", null);
		emptySettings.addString("Food process", null);
		emptySettings.addString("Dependent variable", null);
		emptySettings.addString("Dependent variable unit", null);
		emptySettings.addDouble("Dependent variable minimum value", Double.NaN);
		emptySettings.addDouble("Dependent variable maximum value", Double.NaN);
		emptySettings.addStringArray("Independent variables", (String[]) null);
		emptySettings.addStringArray("Independent variables units", (String[]) null);
		emptySettings.addDoubleArray("Independent variables minimum values", (double[]) null);
		emptySettings.addDoubleArray("Independent varaibles maximum values", (double[]) null);
		
		FSMRTemplateSettings templateSettings = new FSMRTemplateSettings();
		templateSettings.loadFromNodeSettings(emptySettings);
		testInexistenceInTemplate(templateSettings.getTemplate());

		// test filled settings
		NodeSettings filledSettings = new NodeSettings("filled settings");
		filledSettings.addString("Model name", template.getModelName());
		filledSettings.addString("Model id", template.getModelId());
		filledSettings.addString("Model link", template.getModelLink().toString());
		filledSettings.addString("Organism name", template.getOrganismName());
		filledSettings.addString("Organism details", template.getOrganismDetails());
		filledSettings.addString("Matrix name", template.getMatrixName());
		filledSettings.addString("Matrix details", template.getMatrixDetails());
		filledSettings.addString("Creator", template.getCreator());
		filledSettings.addString("Family name", template.getFamilyName());
		filledSettings.addString("Contact", template.getContact());
		filledSettings.addString("Reference description", template.getReferenceDescription());
		filledSettings.addString("Reference description link", template.getReferenceDescriptionLink().toString());
		filledSettings.addString("Created date", dateFormat.format(template.getCreatedDate()));
		filledSettings.addString("Modified date", dateFormat.format(template.getModifiedDate()));
		filledSettings.addString("Rights", template.getRights());
		filledSettings.addString("Notes", template.getNotes());
		filledSettings.addString("Curation status", template.getCurationStatus());
		filledSettings.addString("Model type", template.getModelType().name());
		filledSettings.addString("Model subject", template.getModelSubject().fullName());
		filledSettings.addString("Food process", template.getFoodProcess());
		filledSettings.addString("Dependent variable", template.getDependentVariable());
		filledSettings.addString("Dependent variable unit", template.getDependentVariableUnit());
		filledSettings.addDouble("Dependent variable minimum value", template.getDependentVariableMin());
		filledSettings.addDouble("Dependent variable maximum value", template.getDependentVariableMax());
		filledSettings.addStringArray("Independent variables", template.getIndependentVariables());
		filledSettings.addStringArray("Independent variables units", template.getIndependentVariablesUnits());
		filledSettings.addDoubleArray("Independent variables minimum values", template.getIndependentVariablesMins());
		filledSettings.addDoubleArray("Independent variables maximum values", template.getIndependentVariablesMaxs());
		filledSettings.addBoolean("Has data?", template.getHasData());

		testLoadingFilledSettings(filledSettings);
	}

	private void testLoadingFilledSettings(final NodeSettings settings) {
		FSMRTemplateSettings templateSettings = new FSMRTemplateSettings();
		templateSettings.loadFromNodeSettings(settings);
		FSMRTemplate templateFromSettings = templateSettings.getTemplate();

		assertEquals(template.getModelName(), templateFromSettings.getModelName());
		assertEquals(template.getModelId(), templateFromSettings.getModelId());
		assertEquals(template.getModelLink(), templateFromSettings.getModelLink());
		assertEquals(template.getOrganismName(), templateFromSettings.getOrganismName());
		assertEquals(template.getOrganismDetails(), templateFromSettings.getOrganismDetails());
		assertEquals(template.getMatrixName(), templateFromSettings.getMatrixName());
		assertEquals(template.getMatrixDetails(), templateFromSettings.getMatrixDetails());
		assertEquals(template.getCreator(), templateFromSettings.getCreator());
		assertEquals(template.getFamilyName(), templateFromSettings.getFamilyName());
		assertEquals(template.getReferenceDescription(), templateFromSettings.getReferenceDescription());
		assertEquals(template.getReferenceDescriptionLink(), templateFromSettings.getReferenceDescriptionLink());
		assertEquals(template.getCreatedDate(), templateFromSettings.getCreatedDate());
		assertEquals(template.getModifiedDate(), templateFromSettings.getModifiedDate());
		assertEquals(template.getRights(), templateFromSettings.getRights());
		assertEquals(template.getNotes(), templateFromSettings.getNotes());
		assertEquals(template.getCurationStatus(), templateFromSettings.getCurationStatus());
		assertEquals(template.getModelType(), templateFromSettings.getModelType());
		assertEquals(template.getModelSubject(), templateFromSettings.getModelSubject());
		assertEquals(template.getFoodProcess(), templateFromSettings.getFoodProcess());
		assertEquals(template.getDependentVariable(), templateFromSettings.getDependentVariable());
		assertEquals(template.getDependentVariableUnit(), templateFromSettings.getDependentVariableUnit());
		assertEquals(template.getDependentVariableMin(), templateFromSettings.getDependentVariableMin(), .0);
		assertEquals(template.getDependentVariableMax(), templateFromSettings.getDependentVariableMax(), .0);
		assertArrayEquals(template.getIndependentVariables(), templateFromSettings.getIndependentVariables());
		assertArrayEquals(template.getIndependentVariablesUnits(), templateFromSettings.getIndependentVariablesUnits());
		assertArrayEquals(template.getIndependentVariablesMins(), templateFromSettings.getIndependentVariablesMins(),
				.0);
		assertArrayEquals(template.getIndependentVariablesMaxs(), templateFromSettings.getIndependentVariablesMaxs(),
				.0);
		assertEquals(template.getHasData(), templateFromSettings.getHasData());
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException, MalformedURLException, ParseException {

		// test empty settings
		FSMRTemplateSettings templateSettings = new FSMRTemplateSettings();
		templateSettings.setTemplate(new FSMRTemplateImpl());

		NodeSettings emptySettings = new NodeSettings("empty settings");
		templateSettings.saveToNodeSettings(emptySettings);
		testSavingEmptySettings(emptySettings);

		// test filled settings
		templateSettings.setTemplate(template);

		NodeSettings filledSettings = new NodeSettings("irrelevant key");
		templateSettings.saveToNodeSettings(filledSettings);
		testSavingFilledSettings(filledSettings);
	}
	
	private void testInexistenceInTemplate(final FSMRTemplate template) {
		assertFalse(template.isSetModelName());
		assertFalse(template.isSetModelId());
		assertFalse(template.isSetModelLink());
		assertFalse(template.isSetOrganismName());
		assertFalse(template.isSetOrganismDetails());
		assertFalse(template.isSetMatrixName());
		assertFalse(template.isSetMatrixDetails());
		assertFalse(template.isSetCreator());
		assertFalse(template.isSetFamilyName());
		assertFalse(template.isSetContact());
		assertFalse(template.isSetReferenceDescription());
		assertFalse(template.isSetReferenceDescriptionLink());
		assertFalse(template.isSetCreatedDate());
		assertFalse(template.isSetModifiedDate());
		assertFalse(template.isSetRights());
		assertFalse(template.isSetNotes());
		assertFalse(template.isSetCurationStatus());
		assertFalse(template.isSetModelType());
		assertFalse(template.isSetModelSubject());
		assertFalse(template.isSetFoodProcess());
		assertFalse(template.isSetDependentVariable());
		assertFalse(template.isSetDependentVariableUnit());
		assertFalse(template.isSetDependentVariableMin());
		assertFalse(template.isSetDependentVariableMax());
		assertFalse(template.isSetIndependentVariables());
		assertFalse(template.isSetIndependentVariablesUnits());
		assertFalse(template.isSetIndependentVariablesMins());
		assertFalse(template.isSetIndependentVariablesMaxs());
	}
	
	private void testExistenceInTemplate(final FSMRTemplate template) {
		assertTrue(template.isSetModelName());
		assertTrue(template.isSetModelId());
		assertTrue(template.isSetModelLink());
		assertTrue(template.isSetOrganismName());
		assertTrue(template.isSetOrganismDetails());
		assertTrue(template.isSetMatrixName());
		assertTrue(template.isSetMatrixDetails());
		assertTrue(template.isSetCreator());
		assertTrue(template.isSetFamilyName());
		assertTrue(template.isSetContact());
		assertTrue(template.isSetReferenceDescription());
		assertTrue(template.isSetReferenceDescriptionLink());
		assertTrue(template.isSetCreatedDate());
		assertTrue(template.isSetModifiedDate());
		assertTrue(template.isSetRights());
		assertTrue(template.isSetNotes());
		assertTrue(template.isSetCurationStatus());
		assertTrue(template.isSetModelType());
		assertTrue(template.isSetModelSubject());
		assertTrue(template.isSetFoodProcess());
		assertTrue(template.isSetDependentVariable());
		assertTrue(template.isSetDependentVariableUnit());
		assertTrue(template.isSetDependentVariableMin());
		assertTrue(template.isSetDependentVariableMax());
		assertTrue(template.isSetIndependentVariables());
		assertTrue(template.isSetIndependentVariablesUnits());
		assertTrue(template.isSetIndependentVariablesMins());
		assertTrue(template.isSetIndependentVariablesMaxs());
	}

	private void testSavingEmptySettings(final NodeSettings settings) throws InvalidSettingsException {
		assertNull(settings.getString("Model name"));
		assertNull(settings.getString("Model id"));
		assertNull(settings.getString("Model link"));
		assertNull(settings.getString("Organism name"));
		assertNull(settings.getString("Organism details"));
		assertNull(settings.getString("Matrix name"));
		assertNull(settings.getString("Matrix details"));
		assertNull(settings.getString("Creator"));
		assertNull(settings.getString("Family name"));
		assertNull(settings.getString("Contact"));
		assertNull(settings.getString("Reference description"));
		assertNull(settings.getString("Reference description link"));
		assertNull(settings.getString("Created date"));
		assertNull(settings.getString("Modified date"));
		assertNull(settings.getString("Rights"));
		assertNull(settings.getString("Notes"));
		assertNull(settings.getString("Curation status"));
		assertNull(settings.getString("Model type"));
		assertNull(settings.getString("Model subject"));
		assertNull(settings.getString("Food process"));
		assertNull(settings.getString("Dependent variable"));
		assertNull(settings.getString("Dependent variable unit"));
		assertEquals(Double.NaN, settings.getDouble("Dependent variable minimum value"), .0);
		assertEquals(Double.NaN, settings.getDouble("Dependent variable maximum value"), .0);
		assertNull(settings.getStringArray("Independent variables"));
		assertNull(settings.getStringArray("Independent variables units"));
		assertNull(settings.getDoubleArray("Independent variables minimum values"));
		assertNull(settings.getDoubleArray("Independent variables maximum values"));
	}

	private void testSavingFilledSettings(final NodeSettings settings)
			throws InvalidSettingsException, MalformedURLException, ParseException {
		assertEquals(template.getModelName(), settings.getString("Model name"));
		assertEquals(template.getModelId(), settings.getString("Model id"));
		assertEquals(template.getModelLink(), new URL(settings.getString("Model link")));
		assertEquals(template.getOrganismName(), settings.getString("Organism name"));
		assertEquals(template.getOrganismDetails(), settings.getString("Organism details"));
		assertEquals(template.getMatrixName(), settings.getString("Matrix name"));
		assertEquals(template.getMatrixDetails(), settings.getString("Matrix details"));
		assertEquals(template.getCreator(), settings.getString("Creator"));
		assertEquals(template.getFamilyName(), settings.getString("Family name"));
		assertEquals(template.getContact(), settings.getString("Contact"));
		assertEquals(template.getReferenceDescription(), settings.getString("Reference description"));
		assertEquals(template.getReferenceDescriptionLink(), new URL(settings.getString("Reference description link")));
		assertEquals(template.getCreatedDate(), dateFormat.parse(settings.getString("Created date")));
		assertEquals(template.getModifiedDate(), dateFormat.parse(settings.getString("Modified date")));
		assertEquals(template.getRights(), settings.getString("Rights"));
		assertEquals(template.getNotes(), settings.getString("Notes"));
		assertEquals(template.getCurationStatus(), settings.getString("Curation status"));
		assertEquals(template.getModelType(), ModelType.valueOf(settings.getString("Model type")));
		assertEquals(template.getModelSubject(), ModelClass.fromName(settings.getString("Model subject")));
		assertEquals(template.getFoodProcess(), settings.getString("Food process"));
		assertEquals(template.getDependentVariable(), settings.getString("Dependent variable"));
		assertEquals(template.getDependentVariableUnit(), settings.getString("Dependent variable unit"));
		assertEquals(template.getDependentVariableMin(), settings.getDouble("Dependent variable minimum value"), .0);
		assertEquals(template.getDependentVariableMax(), settings.getDouble("Dependent variable maximum value"), .0);
		assertArrayEquals(template.getIndependentVariables(), settings.getStringArray("Independent variables"));
		assertArrayEquals(template.getIndependentVariablesUnits(),
				settings.getStringArray("Independent variables units"));
		assertArrayEquals(template.getIndependentVariablesMins(),
				settings.getDoubleArray("Independent variables minimum values"), .0);
		assertArrayEquals(template.getIndependentVariablesMaxs(),
				settings.getDoubleArray("Independent variables maximum values"), .0);
	}
}
