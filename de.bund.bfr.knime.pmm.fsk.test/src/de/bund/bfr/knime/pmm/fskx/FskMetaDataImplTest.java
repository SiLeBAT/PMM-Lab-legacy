package de.bund.bfr.knime.pmm.fskx;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import junit.framework.TestCase;

public class FskMetaDataImplTest extends TestCase {

  private FskMetaData metadata = new FskMetaDataImpl();

  public void testModelName() {
    
    assertFalse(metadata.isSetModelName());
    try {
      metadata.getModelName();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }
    
    metadata.setModelName(null);
    assertFalse(metadata.isSetModelName());
    
    metadata.setModelName("");
    assertFalse(metadata.isSetModelName());
    
    metadata.setModelName("Salmonellaspp");
    assertTrue(metadata.isSetModelName());
    assertEquals(metadata.getModelName(), "Salmonellaspp");
  }

  public void testModelId() {
    
    assertFalse(metadata.isSetModelId());
    try {
      metadata.getModelId();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setModelId(null);
    assertFalse(metadata.isSetModelId());

    metadata.setModelId("");
    assertFalse(metadata.isSetModelId());
    
    metadata.setModelId("PMM-Lab_342586849");
    assertTrue(metadata.isSetModelId());
    assertEquals(metadata.getModelId(), "PMM-Lab_342586849");

  }

  public void testModelLink() throws MalformedURLException {

    assertFalse(metadata.isSetModelLink());
    try {
      metadata.getModelLink();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setModelLink(null);
    assertFalse(metadata.isSetModelLink());

    // The protocol used, 'https', is valid so it should not raise an exception
    URL link = new URL(
      "https://drive.google.com/open?id=0B06JrEEq34hSaEt5UFVIMFM3M0k");
    metadata.setModelLink(link);
    assertTrue(metadata.isSetModelLink());
    assertEquals(metadata.getModelLink(), link);
  }

  public void testOrganism() {

    assertFalse(metadata.isSetOrganism());
    try {
      metadata.getOrganism();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setOrganism(null);
    assertFalse(metadata.isSetOrganism());

    metadata.setOrganism("");
    assertFalse(metadata.isSetOrganism());

    metadata.setOrganism("Salmonella spp");
    assertTrue(metadata.isSetOrganism());
    assertEquals(metadata.getOrganism(), "Salmonella spp");
  }

  public void testOrganismDetails() {

    assertFalse(metadata.isSetOrganismDetails());
    try {
      metadata.getOrganismDetails();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setOrganismDetails(null);
    assertFalse(metadata.isSetOrganismDetails());

    metadata.setOrganismDetails("");
    assertFalse(metadata.isSetOrganismDetails());

    metadata.setOrganismDetails("broth");
    assertTrue(metadata.isSetOrganismDetails());
    assertEquals("broth", metadata.getOrganismDetails());
  }

  public void testMatrix() {

    assertFalse(metadata.isSetMatrix());
    try {
      metadata.getMatrix();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setMatrix(null);
    assertFalse(metadata.isSetMatrix());

    metadata.setMatrix("");
    assertFalse(metadata.isSetMatrix());

    metadata.setMatrix("Beef");
    assertTrue(metadata.isSetMatrix());
    assertEquals("Beef", metadata.getMatrix());
  }

  public void testMatrixDetails() {

    assertFalse(metadata.isSetMatrixDetails());
    try {
      metadata.getMatrixDetails();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setMatrixDetails(null);
    assertFalse(metadata.isSetMatrixDetails());

    metadata.setMatrixDetails("");
    assertFalse(metadata.isSetMatrixDetails());

    metadata.setMatrixDetails("(in: ground beef)");
    assertTrue(metadata.isSetMatrixDetails());
    assertEquals("(in: ground beef)", metadata.getMatrixDetails());
  }

  public void testCreator() {

    assertFalse(metadata.isSetCreator());
    try {
      metadata.getCreator();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setCreator(null);
    assertFalse(metadata.isSetCreator());

    metadata.setCreator("");
    assertFalse(metadata.isSetCreator());

    metadata.setCreator("Plaza Rodriguez et al.");
    assertTrue(metadata.isSetCreator());
    assertEquals("Plaza Rodriguez et al.", metadata.getCreator());
  }

  public void testFamilyName() {

    assertFalse(metadata.isSetFamilyName());
    try {
      metadata.getFamilyName();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setFamilyName(null);
    assertFalse(metadata.isSetFamilyName());

    metadata.setFamilyName("");
    assertFalse(metadata.isSetFamilyName());

    metadata.setFamilyName("Baranyi models");
    assertTrue(metadata.isSetFamilyName());
    assertEquals("Baranyi models", metadata.getFamilyName());
  }

  public void testContact() {

    assertFalse(metadata.isSetContact());
    try {
      metadata.getContact();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setContact(null);
    assertFalse(metadata.isSetContact());

    metadata.setContact("");
    assertFalse(metadata.isSetContact());

    metadata.setContact("some contact data");
    assertTrue(metadata.isSetContact());
    assertEquals("some contact data", metadata.getContact());
  }

  public void testReferenceDescription() {

    assertFalse(metadata.isSetReferenceDescription());
    try {
      metadata.getReferenceDescription();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setReferenceDescription(null);
    assertFalse(metadata.isSetReferenceDescription());

    metadata.setReferenceDescription("");
    assertFalse(metadata.isSetReferenceDescription());

    metadata.setReferenceDescription("Juneja, Vijay K. et al., 2009.");
    assertTrue(metadata.isSetReferenceDescription());
    assertEquals("Juneja, Vijay K. et al., 2009.", metadata.getReferenceDescription());
  }

  public void testReferenceDescriptionLink() throws MalformedURLException {

    assertFalse(metadata.isSetReferenceDescriptionLink());
    try {
      metadata.getReferenceDescriptionLink();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setReferenceDescriptionLink(null);
    assertFalse(metadata.isSetReferenceDescriptionLink());

    URL link = new URL("http://smas.chemeng.ntua.gr/miram/files/publ_237_10_2_2005.pdf");
    metadata.setReferenceDescriptionLink(link);
    assertTrue(metadata.isSetReferenceDescriptionLink());
    assertEquals(metadata.getReferenceDescriptionLink(), link);
  }

  public void testCreatedDate() {

    assertFalse(metadata.isSetCreatedDate());
    try {
      metadata.getCreatedDate();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setCreatedDate(null);
    assertFalse(metadata.isSetCreatedDate());

    Date date = new Date(2015, 1, 1);
    metadata.setCreatedDate(date);
    assertTrue(metadata.isSetCreatedDate());
    assertEquals(metadata.getCreatedDate(), date);
  }

  public void testModifiedDate() {

    assertFalse(metadata.isSetModifiedDate());
    try {
      metadata.getModifiedDate();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setModifiedDate(null);
    assertFalse(metadata.isSetModifiedDate());

    Date date = new Date(2015, 1, 3);
    metadata.setModifiedDate(date);
    assertTrue(metadata.isSetModifiedDate());
    assertEquals(metadata.getModifiedDate(), date);
  }

  public void testRights() {

    assertFalse(metadata.isSetRights());
    try {
      metadata.getRights();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setRights(null);
    assertFalse(metadata.isSetRights());

    metadata.setRights("");
    assertFalse(metadata.isSetRights());

    metadata.setRights("CC-BY-NC-SA");
    assertTrue(metadata.isSetRights());
    assertEquals("CC-BY-NC-SA", metadata.getRights());
  }

  public void testNotes() {

    assertFalse(metadata.isSetNotes());
    try {
      metadata.getNotes();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setNotes(null);
    assertFalse(metadata.isSetNotes());

    metadata.setNotes("");
    assertFalse(metadata.isSetNotes());

    metadata.setNotes("not curated");
    assertTrue(metadata.isSetNotes());
    assertEquals("not curated", metadata.getNotes());
  }

  public void testCurated() {
    fail();
  }

  public void testModelType() {

    assertFalse(metadata.isSetModelType());
    try {
      metadata.getModelType();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setModelType(null);
    assertFalse(metadata.isSetModelType());

    metadata.setModelType(ModelType.TWO_STEP_TERTIARY_MODEL);
    assertTrue(metadata.isSetModelType());
    assertEquals(metadata.getModelType(), ModelType.TWO_STEP_TERTIARY_MODEL);
  }

  public void testModelSubject() {

    assertFalse(metadata.isSetModelSubject());
    try {
      metadata.getModelSubject();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setModelSubject(null);
    assertFalse(metadata.isSetModelSubject());

    metadata.setModelSubject(ModelClass.GROWTH);
    assertTrue(metadata.isSetModelSubject());
    assertEquals(metadata.getModelSubject(), ModelClass.GROWTH);
  }

  public void testFoodProcess() {

    assertFalse(metadata.isSetFoodProcess());
    try {
      metadata.getFoodProcess();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setFoodProcess(null);
    assertFalse(metadata.isSetFoodProcess());

    metadata.setFoodProcess("");
    assertFalse(metadata.isSetFoodProcess());

    metadata.setFoodProcess("cooking");
    assertTrue(metadata.isSetFoodProcess());
    assertEquals(metadata.getFoodProcess(), "cooking");
  }

  public void testDependentVariable() {

    assertFalse(metadata.isSetDependentVariable());
    try {
      metadata.getDependentVariable();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setDependentVariable(null);
    assertFalse(metadata.isSetDependentVariable());

    metadata.setDependentVariable("");
    assertFalse(metadata.isSetDependentVariable());

    metadata.setDependentVariable("concentration");
    assertTrue(metadata.isSetDependentVariable());
    assertEquals("concentration", metadata.getDependentVariable());
  }

  public void testDependentVariableUnit() {

    assertFalse(metadata.isSetDependentVariableUnit());
    try {
      metadata.getDependentVariableUnit();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setDependentVariableUnit(null);
    assertFalse(metadata.isSetDependentVariableUnit());

    metadata.setDependentVariableUnit("");
    assertFalse(metadata.isSetDependentVariableUnit());

    metadata.setDependentVariableUnit("ln");
    assertTrue(metadata.isSetDependentVariableUnit());
    assertEquals("ln", metadata.getDependentVariableUnit());
  }

  public void testDependentVariableMin() {

    assertFalse(metadata.isSetDependentVariableMin());
    try {
      metadata.getDependentVariableMin();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setDependentVariableMin(0.0);
    assertTrue(metadata.isSetDependentVariableMin());
    assertEquals(metadata.getDependentVariableMin(), 0.0, .0);
  }

  public void testDependentVariableMax() {

    assertFalse(metadata.isSetDependentVariableMax());
    try {
      metadata.getDependentVariableMax();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed
    }

    metadata.setDependentVariableMax(10.0);
    assertTrue(metadata.isSetDependentVariableMax());
    assertEquals(metadata.getDependentVariableMax(), 10.0, .0);
  }

  public void testIndependentVariables() {

    assertFalse(metadata.isSetIndependentVariables());
    try {
      metadata.getIndependentVariables();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setIndependentVariables(null);
    assertFalse(metadata.isSetIndependentVariables());

    metadata.setIndependentVariables(Arrays.asList());
    assertFalse(metadata.isSetIndependentVariables());
    
    List<String> vars = Arrays.asList("time", "temperature");
    metadata.setIndependentVariables(vars);
    assertTrue(metadata.isSetIndependentVariables());
    assertEquals(metadata.getIndependentVariables(), vars);
  }

  public void testIndependentVariableUnits() {

    assertFalse(metadata.isSetIndependentVariables());
    try {
      metadata.getIndependentVariableUnits();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setIndependentVariableUnits(null);
    assertFalse(metadata.isSetIndependentVariableUnits());

    metadata.setIndependentVariableUnits(Arrays.asList());
    assertFalse(metadata.isSetIndependentVariableUnits());

    List<String> units = Arrays.asList("s", "°C");
    metadata.setIndependentVariableUnits(units);
    assertTrue(metadata.isSetIndependentVariableUnits());
    assertEquals(metadata.getIndependentVariableUnits(), units);
  }

  public void testIndependentVariableMins() {

    assertFalse(metadata.isSetIndependentVariableMins());
    try {
      metadata.getIndependentVariableMins();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setIndependentVariableMins(null);
    assertFalse(metadata.isSetIndependentVariableMins());

    metadata.setIndependentVariableMins(Arrays.asList());
    assertFalse(metadata.isSetIndependentVariableMins());

    List<Double> mins = Arrays.asList(0.0, 1.0);
    metadata.setIndependentVariableMins(mins);
    assertTrue(metadata.isSetIndependentVariableMins());
    assertEquals(metadata.getIndependentVariableMins(), mins);
  }

  public void testIndependentVariableMaxs() {
    assertFalse(metadata.isSetIndependentVariableMaxs());
    try {
      metadata.getIndependentVariableMaxs();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setIndependentVariableMaxs(null);
    assertFalse(metadata.isSetIndependentVariableMaxs());

    metadata.setIndependentVariableMaxs(Arrays.asList());
    assertFalse(metadata.isSetIndependentVariableMaxs());

    List<Double> maxs = Arrays.asList(10.0, 5.0);
    metadata.setIndependentVariableMaxs(maxs);
    assertTrue(metadata.isSetIndependentVariableMaxs());
    assertEquals(metadata.getIndependentVariableMaxs(), maxs);
  }

  public void testIndependentVariableValues() {
    assertFalse(metadata.isSetIndependentVariableValues());
    try {
      metadata.getIndependentVariableValues();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // do nothing - test is passed 
    }

    metadata.setIndependentVariableValues(null);
    assertFalse(metadata.isSetIndependentVariableValues());

    metadata.setIndependentVariableValues(Arrays.asList());
    assertFalse(metadata.isSetIndependentVariableValues());

    List<Double> values = Arrays.asList(7.0, 3.0);
    metadata.setIndependentVariableValues(values);
    assertTrue(metadata.isSetIndependentVariableValues());
    assertEquals(metadata.getIndependentVariableValues(), values);
  }

  public void testHasData() {
    fail();
  }
}
