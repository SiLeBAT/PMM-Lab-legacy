package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

import com.google.common.base.Strings;

import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class FskTemplateSettingsTest {

  private final FskMetaData template = new FskMetaData();
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

  @Before
  public void setUp() {
    try {
      template.modelName = "Salmonellaspp_GroundBeef_Temp_GrowthModel...";
      template.modelId = "PMM-Lab_342586849";
      template.modelLink = new URL("https://drive.google.com/open?id=0B06JrEEq34hSaEt5UFVIMFM3M0k");
      template.organism = "Salmonella spp.";
      template.organismDetails = "broth";
      template.matrix = "Beef";
      template.matrixDetails = "(in: ground beef)";
      template.creator = "Plaza Rodriguez et al.";
      template.familyName = "Baranyi models";
      template.contact = "some contact data";
      template.referenceDescription = "Juneja, Vijay K. et al., 2009. "
          + "Mathematical modeling of growth of Salmonella in raw "
          + "ground beef under isothermal conditions from 10 to 45°C. "
          + "International Journal of Food Microbiology ():  ff.";
      template.referenceDescriptionLink =
          new URL("http://smas.chemeng.ntua.gr/miram/files/publ_237_10_2_2005.pdf");
      template.createdDate = new GregorianCalendar(2014, Calendar.JANUARY, 1).getTime();
      template.modifiedDate = new GregorianCalendar(2015, Calendar.DECEMBER, 1).getTime();
      template.rights = "CC-BY-NC-SA";
      template.notes = "not curated";
      template.curated = true;
      template.type = ModelType.TWO_STEP_TERTIARY_MODEL;
      template.subject = ModelClass.GROWTH;
      template.foodProcess = "cooking";
      template.dependentVariable = "Concentration";
      template.dependentVariableUnit = "ln";
      template.dependentVariableMin = 0.0;
      template.dependentVariableMax = 10.0;
      template.independentVariables = Arrays.asList("time", "temperature");
      template.independentVariableUnits = Arrays.asList("s", "°C");
      template.independentVariableMins = Arrays.asList(0.0, 1.0);
      template.independentVariableMaxs = Arrays.asList(0.0, 1.0);
      template.hasData = false;
    } catch (MalformedURLException e) {
      // passed url here are correct
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTemplate() {
    FskTemplateSettings settings = new FskTemplateSettings();
    testInexistenceInTemplate(settings.template);

    settings.template = template;
    testExistenceInTemplate(settings.template);
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

    FskTemplateSettings templateSettings = new FskTemplateSettings();
    templateSettings.loadFromNodeSettings(emptySettings);
    testInexistenceInTemplate(templateSettings.template);

    // test filled settings
    NodeSettings filledSettings = new NodeSettings("filled settings");
    filledSettings.addString("Model name", template.modelName);
    filledSettings.addString("Model id", template.modelId);
    filledSettings.addString("Model link", template.modelLink.toString());
    filledSettings.addString("Organism name", template.organism);
    filledSettings.addString("Organism details", template.organismDetails);
    filledSettings.addString("Matrix name", template.matrix);
    filledSettings.addString("Matrix details", template.matrixDetails);
    filledSettings.addString("Creator", template.creator);
    filledSettings.addString("Family name", template.familyName);
    filledSettings.addString("Contact", template.contact);
    filledSettings.addString("Reference description", template.referenceDescription);
    filledSettings.addString("Reference description link",
        template.referenceDescriptionLink.toString());
    filledSettings.addString("Created date", dateFormat.format(template.createdDate));
    filledSettings.addString("Modified date", dateFormat.format(template.modifiedDate));
    filledSettings.addString("Rights", template.rights);
    filledSettings.addString("Notes", template.notes);
    filledSettings.addBoolean("Curation status", template.curated);
    filledSettings.addString("Model type", template.type.name());
    filledSettings.addString("Model subject", template.subject.fullName());
    filledSettings.addString("Food process", template.foodProcess);
    filledSettings.addString("Dependent variable", template.dependentVariable);
    filledSettings.addString("Dependent variable unit", template.dependentVariableUnit);
    filledSettings.addDouble("Dependent variable minimum value", template.dependentVariableMin);
    filledSettings.addDouble("Dependent variable maximum value", template.dependentVariableMax);
    filledSettings.addStringArray("Independent variables",
        template.independentVariables.toArray(new String[0]));
    filledSettings.addStringArray("Independent variables units",
        template.independentVariableUnits.toArray(new String[0]));
    filledSettings.addDoubleArray("Independent variables minimum values",
        template.independentVariableMins.stream().mapToDouble(Double::doubleValue).toArray());
    filledSettings.addDoubleArray("Independent variables maximum values",
        template.independentVariableMaxs.stream().mapToDouble(Double::doubleValue).toArray());
    filledSettings.addBoolean("Has data?", template.hasData);

    testLoadingFilledSettings(filledSettings);
  }

  private void testLoadingFilledSettings(final NodeSettings settings) {
    FskTemplateSettings templateSettings = new FskTemplateSettings();
    templateSettings.loadFromNodeSettings(settings);
    FskMetaData templateFromSettings = templateSettings.template;

    assertEquals(template.modelName, templateFromSettings.modelName);
    assertEquals(template.modelId, templateFromSettings.modelId);
    assertEquals(template.modelLink, templateFromSettings.modelLink);
    assertEquals(template.organism, templateFromSettings.organism);
    assertEquals(template.organismDetails, templateFromSettings.organismDetails);
    assertEquals(template.matrix, templateFromSettings.matrix);
    assertEquals(template.matrixDetails, templateFromSettings.matrixDetails);
    assertEquals(template.creator, templateFromSettings.creator);
    assertEquals(template.familyName, templateFromSettings.familyName);
    assertEquals(template.referenceDescription, templateFromSettings.referenceDescription);
    assertEquals(template.referenceDescriptionLink, templateFromSettings.referenceDescriptionLink);
    assertEquals(template.createdDate, templateFromSettings.createdDate);
    assertEquals(template.modifiedDate, templateFromSettings.modifiedDate);
    assertEquals(template.rights, templateFromSettings.rights);
    assertEquals(template.notes, templateFromSettings.notes);
    assertEquals(template.curated, templateFromSettings.curated);
    assertEquals(template.type, templateFromSettings.type);
    assertEquals(template.subject, templateFromSettings.subject);
    assertEquals(template.foodProcess, templateFromSettings.foodProcess);
    assertEquals(template.dependentVariable, templateFromSettings.dependentVariable);
    assertEquals(template.dependentVariableUnit, templateFromSettings.dependentVariableUnit);
    assertEquals(template.dependentVariableMin, templateFromSettings.dependentVariableMin, .0);
    assertEquals(template.dependentVariableMax, templateFromSettings.dependentVariableMax, .0);
    assertEquals(template.independentVariables, templateFromSettings.independentVariables);
    assertEquals(template.independentVariableUnits, templateFromSettings.independentVariableUnits);
    assertEquals(template.independentVariableMins, templateFromSettings.independentVariableMins);
    assertEquals(template.independentVariableMaxs, templateFromSettings.independentVariableMaxs);
    assertEquals(template.hasData, templateFromSettings.hasData);
  }

  @Test
  public void testSaveToNodeSettings()
      throws InvalidSettingsException, MalformedURLException, ParseException {

    // test empty settings
    FskTemplateSettings templateSettings = new FskTemplateSettings();
    templateSettings.template = new FskMetaData();

    NodeSettings emptySettings = new NodeSettings("empty settings");
    templateSettings.saveToNodeSettings(emptySettings);
    testSavingEmptySettings(emptySettings);

    // test filled settings
    templateSettings.template = template;

    NodeSettings filledSettings = new NodeSettings("irrelevant key");
    templateSettings.saveToNodeSettings(filledSettings);
    testSavingFilledSettings(filledSettings);
  }

  private void testInexistenceInTemplate(final FskMetaData template) {
    assertTrue(Strings.isNullOrEmpty(template.modelName));
    assertTrue(Strings.isNullOrEmpty(template.modelId));
    assertNull(template.modelLink);
    assertTrue(Strings.isNullOrEmpty(template.organism));
    assertTrue(Strings.isNullOrEmpty(template.organismDetails));
    assertTrue(Strings.isNullOrEmpty(template.matrix));
    assertTrue(Strings.isNullOrEmpty(template.matrixDetails));
    assertTrue(Strings.isNullOrEmpty(template.creator));
    assertTrue(Strings.isNullOrEmpty(template.familyName));
    assertTrue(Strings.isNullOrEmpty(template.contact));
    assertTrue(Strings.isNullOrEmpty(template.referenceDescription));
    assertNull(template.referenceDescriptionLink);
    assertNull(template.createdDate);
    assertNull(template.modifiedDate);
    assertTrue(Strings.isNullOrEmpty(template.rights));
    assertTrue(Strings.isNullOrEmpty(template.notes));
    assertFalse(template.curated);
    assertNull(template.type);
    assertEquals(template.subject, ModelClass.UNKNOWN);
    assertTrue(Strings.isNullOrEmpty(template.foodProcess));
    assertTrue(Strings.isNullOrEmpty(template.dependentVariable));
    assertTrue(Strings.isNullOrEmpty(template.dependentVariableUnit));
    assertTrue(Double.isNaN(template.dependentVariableMin));
    assertTrue(Double.isNaN(template.dependentVariableMax));
    assertTrue(template.independentVariables == null || template.independentVariables.isEmpty());
    assertTrue(
        template.independentVariableUnits == null || template.independentVariableUnits.isEmpty());
    assertTrue(
        template.independentVariableMins == null || template.independentVariableMins.isEmpty());
    assertTrue(
        template.independentVariableMaxs == null || template.independentVariableMaxs.isEmpty());
  }

  private void testExistenceInTemplate(final FskMetaData template) {
    assertFalse(Strings.isNullOrEmpty(template.modelName));
    assertFalse(Strings.isNullOrEmpty(template.modelId));
    assertNotNull(template.modelLink);
    assertFalse(Strings.isNullOrEmpty(template.organism));
    assertFalse(Strings.isNullOrEmpty(template.organismDetails));
    assertFalse(Strings.isNullOrEmpty(template.matrix));
    assertFalse(Strings.isNullOrEmpty(template.matrixDetails));
    assertFalse(Strings.isNullOrEmpty(template.creator));
    assertFalse(Strings.isNullOrEmpty(template.familyName));
    assertFalse(Strings.isNullOrEmpty(template.contact));
    assertFalse(Strings.isNullOrEmpty(template.referenceDescription));
    assertNotNull(template.referenceDescriptionLink);
    assertNotNull(template.createdDate);
    assertNotNull(template.modifiedDate);
    assertFalse(Strings.isNullOrEmpty(template.rights));
    assertFalse(Strings.isNullOrEmpty(template.notes));
    assertTrue(template.curated);
    assertNotNull(template.type);
    assertNotNull(template.subject);
    assertFalse(Strings.isNullOrEmpty(template.foodProcess));
    assertFalse(Strings.isNullOrEmpty(template.dependentVariable));
    assertFalse(Strings.isNullOrEmpty(template.dependentVariableUnit));
    assertFalse(Double.isNaN(template.dependentVariableMin));
    assertFalse(Double.isNaN(template.dependentVariableMax));
    assertTrue(template.independentVariables != null && !template.independentVariables.isEmpty());
    assertTrue(
        template.independentVariableUnits != null && !template.independentVariableUnits.isEmpty());
    assertTrue(
        template.independentVariableMins != null && !template.independentVariableMins.isEmpty());
    assertTrue(
        template.independentVariableMaxs != null && !template.independentVariableMaxs.isEmpty());
  }

  private void testSavingEmptySettings(final NodeSettings settings)
      throws InvalidSettingsException {
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
    assertFalse(settings.getBoolean("Curation status"));
    assertNull(settings.getString("Model type"));
    assertEquals(settings.getString("Model subject"), ModelClass.UNKNOWN.fullName());
    assertNull(settings.getString("Food process"));
    assertNull(settings.getString("Dependent variable"));
    assertNull(settings.getString("Dependent variable unit"));
    assertTrue(Double.isNaN(settings.getDouble("Dependent variable minimum value")));
    assertTrue(Double.isNaN(settings.getDouble("Dependent variable maximum value")));
    assertNull(settings.getStringArray("Independent variables"));
    assertNull(settings.getStringArray("Independent variables units"));
    assertNull(settings.getDoubleArray("Independent variables minimum values"));
    assertNull(settings.getDoubleArray("Independent variables maximum values"));
  }

  private void testSavingFilledSettings(final NodeSettings settings)
      throws InvalidSettingsException, MalformedURLException, ParseException {
    assertEquals(template.modelName, settings.getString("Model name"));
    assertEquals(template.modelId, settings.getString("Model id"));
    assertEquals(template.modelLink, new URL(settings.getString("Model link")));
    assertEquals(template.organism, settings.getString("Organism name"));
    assertEquals(template.organismDetails, settings.getString("Organism details"));
    assertEquals(template.matrix, settings.getString("Matrix name"));
    assertEquals(template.matrixDetails, settings.getString("Matrix details"));
    assertEquals(template.creator, settings.getString("Creator"));
    assertEquals(template.familyName, settings.getString("Family name"));
    assertEquals(template.contact, settings.getString("Contact"));
    assertEquals(template.referenceDescription, settings.getString("Reference description"));
    assertEquals(template.referenceDescriptionLink,
        new URL(settings.getString("Reference description link")));
    assertEquals(template.createdDate, dateFormat.parse(settings.getString("Created date")));
    assertEquals(template.modifiedDate, dateFormat.parse(settings.getString("Modified date")));
    assertEquals(template.rights, settings.getString("Rights"));
    assertEquals(template.notes, settings.getString("Notes"));
    assertEquals(template.curated, settings.getBoolean("Curation status"));
    assertEquals(template.type, ModelType.valueOf(settings.getString("Model type")));
    assertEquals(template.subject, ModelClass.fromName(settings.getString("Model subject")));
    assertEquals(template.foodProcess, settings.getString("Food process"));
    assertEquals(template.dependentVariable, settings.getString("Dependent variable"));
    assertEquals(template.dependentVariableUnit, settings.getString("Dependent variable unit"));
    assertEquals(template.dependentVariableMin,
        settings.getDouble("Dependent variable minimum value"), .0);
    assertEquals(template.dependentVariableMax,
        settings.getDouble("Dependent variable maximum value"), .0);
    assertEquals(template.independentVariables,
        Arrays.asList(settings.getStringArray("Independent variables")));
    assertEquals(template.independentVariableUnits,
        Arrays.asList(settings.getStringArray("Independent variables units")));

    // TODO: should replace Lists in FskMetaData with arrays to simplify the checking with settings
    // arrays
    {
      List<Double> obtained =
          Arrays.stream(settings.getDoubleArray("Independent variables minimum values")).boxed()
              .collect(Collectors.toList());
      assertEquals(template.independentVariableMins, obtained);
    }

    // TODO: should replace Lists in FskMetaData with arrays to simplify the checking with settings
    // arrays
    {
      List<Double> obtained =
          Arrays.stream(settings.getDoubleArray("Independent variables maximum values")).boxed()
              .collect(Collectors.toList());
      assertEquals(template.independentVariableMaxs, obtained);
    }
  }
}
