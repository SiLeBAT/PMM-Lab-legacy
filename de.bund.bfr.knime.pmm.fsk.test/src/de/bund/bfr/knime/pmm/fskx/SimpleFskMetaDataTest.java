package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.*;

import de.bund.bfr.pmfml.ModelClass;

public class SimpleFskMetaDataTest {

  public void testDefaultValues() {
    FskMetaData metadata = new FskMetaData();
    assertNull(metadata.modelName);
    assertNull(metadata.modelId);
    assertNull(metadata.modelLink);
    assertNull(metadata.organism);
    assertNull(metadata.organismDetails);
    assertNull(metadata.matrix);
    assertNull(metadata.matrixDetails);
    assertNull(metadata.creator);
    assertNull(metadata.familyName);
    assertNull(metadata.contact);
    assertNull(metadata.referenceDescription);
    assertNull(metadata.referenceDescriptionLink);
    assertNull(metadata.createdDate);
    assertNull(metadata.modifiedDate);
    assertNull(metadata.rights);
    assertNull(metadata.notes);
    assertFalse(metadata.curated);
    assertNull(metadata.type);
    assertEquals(ModelClass.UNKNOWN, metadata.subject);
    assertNull(metadata.foodProcess);

    // Check dependent variable
    assertNotNull(metadata.dependentVariable);
    assertNull(metadata.dependentVariable.name);
    assertNull(metadata.dependentVariable.unit);
    assertNull(metadata.dependentVariable.type);
    assertNull(metadata.dependentVariable.min);
    assertNull(metadata.dependentVariable.max);
    assertNull(metadata.dependentVariable.value);

    // Check independent variables
    assertNotNull(metadata.independentVariables);
    assertTrue(metadata.independentVariables.isEmpty());

    assertFalse(metadata.hasData);
  }
}
