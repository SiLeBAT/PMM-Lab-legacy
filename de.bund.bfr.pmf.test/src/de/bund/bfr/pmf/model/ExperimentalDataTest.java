package de.bund.bfr.pmf.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.bund.bfr.pmf.numl.NuMLDocument;

public class ExperimentalDataTest {

  private NuMLDocument doc;

  @Test
  public void test() {
    doc = ModelTestUtil.createDummyData();
    final ExperimentalData ed = new ExperimentalData("test.numl", doc);
    
    assertEquals("test.numl", ed.getDocName());
    assertEquals(doc.getConcentrationOntologyTerm(), ed.getDoc().getConcentrationOntologyTerm());
    assertEquals(doc.getTimeOntologyTerm(), ed.getDoc().getTimeOntologyTerm());
    assertEquals(doc.getResultComponent(), ed.getDoc().getResultComponent());
  }
}
