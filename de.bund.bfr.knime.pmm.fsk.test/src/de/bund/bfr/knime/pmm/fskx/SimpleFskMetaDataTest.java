package de.bund.bfr.knime.pmm.fskx;

import de.bund.bfr.pmfml.ModelClass;
import junit.framework.TestCase;

public class SimpleFskMetaDataTest extends TestCase {

  public void testDefaultValues() {
    SimpleFskMetaData metadata = new SimpleFskMetaData();
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
    assertNull(metadata.dependentVariable);
    assertNull(metadata.dependentVariableUnit);
    assertTrue(Double.isNaN(metadata.dependentVariableMin));
    assertTrue(Double.isNaN(metadata.dependentVariableMax));
    assertNull(metadata.independentVariables);
    assertNull(metadata.independentVariableUnits);
    assertNull(metadata.independentVariableMins);
    assertNull(metadata.independentVariableMaxs);
    assertNull(metadata.independentVariableValues);
    assertFalse(metadata.hasData);
  }
}
