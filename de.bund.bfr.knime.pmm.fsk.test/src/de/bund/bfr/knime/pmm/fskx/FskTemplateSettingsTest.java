package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import de.bund.bfr.knime.pmm.fskx.FskMetaData.DataType;
import de.bund.bfr.knime.pmm.fskx.FskMetaData.Software;
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
      template.software = Software.R;

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

      template.dependentVariable.name = "Concentration";
      template.dependentVariable.unit = "ln";
      template.dependentVariable.type = DataType.numeric;
      template.dependentVariable.min = "0.0";
      template.dependentVariable.max = "10.0";

      {
        Variable timeVar = new Variable();
        timeVar.name = "time";
        timeVar.unit = "s";
        timeVar.type = DataType.numeric;
        timeVar.min = "0.0";
        timeVar.max = "1.0";
        template.independentVariables.add(timeVar);
      }

      {
        Variable tempVar = new Variable();
        tempVar.name = "temperature";
        tempVar.unit = "°C";
        tempVar.type = DataType.numeric;
        tempVar.min = "0.0";
        tempVar.max = "1.0";
        template.independentVariables.add(tempVar);
      }

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
  public void testLoadFromNodeSettings() throws InvalidSettingsException {
    // test empty settings
    NodeSettings emptySettings = new NodeSettings("emptySettings");
    emptySettings.addString("name", null);
    emptySettings.addString("id", null);
    emptySettings.addString("model_link", null);

    emptySettings.addString("species", null);
    emptySettings.addString("species_details", null);

    emptySettings.addString("matrix", null);
    emptySettings.addString("matrix_details", null);

    emptySettings.addString("creator", null);
    emptySettings.addString("family_name", null);
    emptySettings.addString("contact", null);
    emptySettings.addString("software", null);

    emptySettings.addString("reference_description", null);
    emptySettings.addString("reference_description_link", null);

    emptySettings.addString("created_date", null);
    emptySettings.addString("modified_date", null);

    emptySettings.addString("rights", null);
    emptySettings.addString("notes", null);
    emptySettings.addString("curation_status", null);
    emptySettings.addString("model_type", null);
    emptySettings.addString("subject", null);
    emptySettings.addString("food_process", null);

    emptySettings.addString("depvar", null);
    emptySettings.addString("depvar_unit", null);
    emptySettings.addString("depvar_type", null);
    emptySettings.addString("depvar_min", null);
    emptySettings.addString("depvar_max", null);

    emptySettings.addStringArray("indepvars", (String[]) null);
    emptySettings.addStringArray("indepvars_units", (String[]) null);
    emptySettings.addStringArray("indepvars_types", (String[]) null);
    emptySettings.addStringArray("indepvars_mins", (String[]) null);
    emptySettings.addStringArray("indepvars_maxs", (String[]) null);

    FskTemplateSettings templateSettings = new FskTemplateSettings();
    templateSettings.loadFromNodeSettings(emptySettings);
    testInexistenceInTemplate(templateSettings.template);

    // test filled settings
    NodeSettings filledSettings = new NodeSettings("filled settings");

    filledSettings.addString("name", template.modelName);
    filledSettings.addString("id", template.modelId);
    filledSettings.addString("model_link", template.modelLink.toString());

    filledSettings.addString("species", template.organism);
    filledSettings.addString("species_details", template.organismDetails);

    filledSettings.addString("matrix", template.matrix);
    filledSettings.addString("matrix_details", template.matrixDetails);

    filledSettings.addString("creator", template.creator);
    filledSettings.addString("family_name", template.familyName);
    filledSettings.addString("contact", template.contact);
    filledSettings.addString("software", template.software.name());

    filledSettings.addString("reference_description", template.referenceDescription);
    filledSettings.addString("reference_description_link",
        template.referenceDescriptionLink.toString());

    filledSettings.addString("created_date", dateFormat.format(template.createdDate));
    filledSettings.addString("modified_date", dateFormat.format(template.modifiedDate));

    filledSettings.addString("rights", template.rights);
    filledSettings.addString("notes", template.notes);
    filledSettings.addBoolean("curation_status", template.curated);
    filledSettings.addString("model_type", template.type.name());
    filledSettings.addString("subject", template.subject.fullName());
    filledSettings.addString("food_process", template.foodProcess);

    filledSettings.addString("depvar", template.dependentVariable.name);
    filledSettings.addString("depvar_unit", template.dependentVariable.unit);
    filledSettings.addString("depvar_type", template.dependentVariable.type.name());
    filledSettings.addString("depvar_min", template.dependentVariable.min);
    filledSettings.addString("depvar_max", template.dependentVariable.max);

    filledSettings.addStringArray("indepvars",
        template.independentVariables.stream().map(v -> v.name).toArray(String[]::new));
    filledSettings.addStringArray("indepvars_units",
        template.independentVariables.stream().map(v -> v.unit).toArray(String[]::new));
    filledSettings.addStringArray("indepvars_types",
        template.independentVariables.stream().map(v -> v.type.name()).toArray(String[]::new));
    filledSettings.addStringArray("indepvars_mins",
        template.independentVariables.stream().map(v -> v.min).toArray(String[]::new));
    filledSettings.addStringArray("indepvars_maxs",
        template.independentVariables.stream().map(v -> v.max).toArray(String[]::new));

    filledSettings.addBoolean("Has data?", template.hasData);

    testLoadingFilledSettings(filledSettings);
  }

  private void testLoadingFilledSettings(final NodeSettings settings)
      throws InvalidSettingsException {
    FskTemplateSettings templateSettings = new FskTemplateSettings();
    templateSettings.loadFromNodeSettings(settings);
    FskMetaData templateFromSettings = templateSettings.template;

    assertEquals(template, templateFromSettings);
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
    assertNull(template.modelName);
    assertNull(template.modelId);
    assertNull(template.modelLink);

    assertNull(template.organism);
    assertNull(template.organismDetails);

    assertNull(template.matrix);
    assertNull(template.matrixDetails);

    assertNull(template.creator);
    assertNull(template.familyName);
    assertNull(template.contact);

    assertNull(template.referenceDescription);
    assertNull(template.referenceDescriptionLink);

    assertNull(template.createdDate);
    assertNull(template.modifiedDate);

    assertNull(template.rights);
    assertNull(template.notes);
    assertFalse(template.curated);
    assertNull(template.type);
    assertEquals(ModelClass.UNKNOWN, template.subject);
    assertNull(template.foodProcess);

    // Check dependent variable
    assertNotNull(template.dependentVariable);
    assertNull(template.dependentVariable.name);
    assertNull(template.dependentVariable.unit);
    assertNull(template.dependentVariable.type);
    assertNull(template.dependentVariable.min);
    assertNull(template.dependentVariable.max);
    assertNull(template.dependentVariable.value);

    // Check independent variable
    assertNotNull(template.independentVariables);
    assertTrue(template.independentVariables.isEmpty());
  }

  private void testExistenceInTemplate(final FskMetaData template) {

    assertNotNull(template.modelName);
    assertNotNull(template.modelId);
    assertNotNull(template.modelLink);

    assertNotNull(template.organism);
    assertNotNull(template.organismDetails);

    assertNotNull(template.matrix);
    assertNotNull(template.matrixDetails);

    assertNotNull(template.creator);
    assertNotNull(template.familyName);
    assertNotNull(template.contact);
    assertNotNull(template.software);

    assertNotNull(template.referenceDescription);
    assertNotNull(template.referenceDescriptionLink);

    assertNotNull(template.createdDate);
    assertNotNull(template.modifiedDate);

    assertNotNull(template.rights);
    assertNotNull(template.notes);
    assertTrue(template.curated);
    assertNotNull(template.type);
    assertNotNull(template.subject);
    assertNotNull(template.foodProcess);

    // Dependent variables
    assertNotNull(template.dependentVariable);
    assertNotNull(template.dependentVariable.name);
    assertNotNull(template.dependentVariable.unit);
    assertNotNull(template.dependentVariable.type);
    assertNotNull(template.dependentVariable.min);
    assertNotNull(template.dependentVariable.max);

    // Independent variables
    assertNotNull(template.independentVariables);
    assertFalse(template.independentVariables.isEmpty());

    assertFalse(template.hasData);
  }

  private void testSavingEmptySettings(final NodeSettings settings)
      throws InvalidSettingsException {
    assertNull(settings.getString("name"));
    assertNull(settings.getString("id"));
    assertNull(settings.getString("model_link"));

    assertNull(settings.getString("species"));
    assertNull(settings.getString("species_details"));

    assertNull(settings.getString("matrix"));
    assertNull(settings.getString("matrix_details"));

    assertNull(settings.getString("creator"));
    assertNull(settings.getString("family_name"));
    assertNull(settings.getString("contact"));
    assertNull(settings.getString("software"));

    assertNull(settings.getString("reference_description"));
    assertNull(settings.getString("reference_description_link"));

    assertNull(settings.getString("created_date"));
    assertNull(settings.getString("modified_date"));

    assertNull(settings.getString("rights"));
    assertNull(settings.getString("notes"));
    assertFalse(settings.getBoolean("curation_status"));
    assertNull(settings.getString("model_type"));
    assertEquals(settings.getString("subject"), ModelClass.UNKNOWN.fullName());
    assertNull(settings.getString("food_process"));

    assertNull(settings.getString("depvar"));
    assertNull(settings.getString("depvar_unit"));
    assertNull(settings.getString("depvar_type"));
    assertNull(settings.getString("depvar_min"));
    assertNull(settings.getString("depvar_max"));

    assertTrue(settings.getStringArray("indepvars").length == 0);
    assertTrue(settings.getStringArray("indepvars_units").length == 0);
    assertTrue(settings.getStringArray("indepvars_types").length == 0);
    assertTrue(settings.getStringArray("indepvars_mins").length == 0);
    assertTrue(settings.getStringArray("indepvars_maxs").length == 0);
  }

  private void testSavingFilledSettings(final NodeSettings settings)
      throws InvalidSettingsException, MalformedURLException, ParseException {

    assertEquals(template.modelName, settings.getString("name"));
    assertEquals(template.modelId, settings.getString("id"));
    assertEquals(template.modelLink, new URL(settings.getString("model_link")));

    assertEquals(template.organism, settings.getString("species"));
    assertEquals(template.organismDetails, settings.getString("species_details"));

    assertEquals(template.matrix, settings.getString("matrix"));
    assertEquals(template.matrixDetails, settings.getString("matrix_details"));

    assertEquals(template.creator, settings.getString("creator"));
    assertEquals(template.familyName, settings.getString("family_name"));
    assertEquals(template.contact, settings.getString("contact"));
    assertEquals(template.software, Software.valueOf(settings.getString("software")));

    assertEquals(template.referenceDescription, settings.getString("reference_description"));
    assertEquals(template.referenceDescriptionLink,
        new URL(settings.getString("reference_description_link")));

    assertEquals(template.createdDate, dateFormat.parse(settings.getString("created_date")));
    assertEquals(template.modifiedDate, dateFormat.parse(settings.getString("modified_date")));

    assertEquals(template.rights, settings.getString("rights"));
    assertEquals(template.notes, settings.getString("notes"));
    assertEquals(template.curated, settings.getBoolean("curation_status"));
    assertEquals(template.type, ModelType.valueOf(settings.getString("model_type")));
    assertEquals(template.subject, ModelClass.fromName(settings.getString("subject")));
    assertEquals(template.foodProcess, settings.getString("food_process"));

    assertEquals(template.dependentVariable.name, settings.getString("depvar"));
    assertEquals(template.dependentVariable.unit, settings.getString("depvar_unit"));
    assertEquals(template.dependentVariable.type,
        DataType.valueOf(settings.getString("depvar_type")));
    assertEquals(template.dependentVariable.min, settings.getString("depvar_min"));
    assertEquals(template.dependentVariable.max, settings.getString("depvar_max"));

    {
      String[] expectedNames =
          template.independentVariables.stream().map(v -> v.name).toArray(String[]::new);
      String[] expectedUnits =
          template.independentVariables.stream().map(v -> v.unit).toArray(String[]::new);
      String[] expectedTypes =
          template.independentVariables.stream().map(v -> v.type.name()).toArray(String[]::new);
      String[] expectedMins =
          template.independentVariables.stream().map(v -> v.min).toArray(String[]::new);
      String[] expectedMaxs =
          template.independentVariables.stream().map(v -> v.max).toArray(String[]::new);

      assertArrayEquals(expectedNames, settings.getStringArray("indepvars"));
      assertArrayEquals(expectedUnits, settings.getStringArray("indepvars_units"));
      assertArrayEquals(expectedTypes, settings.getStringArray("indepvars_types"));
      assertArrayEquals(expectedMins, settings.getStringArray("indepvars_mins"));
      assertArrayEquals(expectedMaxs, settings.getStringArray("indepvars_maxs"));
    }
  }
}
