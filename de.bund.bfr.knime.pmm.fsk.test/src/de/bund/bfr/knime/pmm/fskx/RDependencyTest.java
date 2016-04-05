package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RDependencyTest {

  @Test
  public void test() {
    // Test dependency with version
    RDependency dep = new RDependency();
    dep.name = "R";
    dep.version = RVersion.numericVersion("2.11");;

    assertEquals("R (>= 2.11)", dep.toString());
    
    // Test dependency without version
    RDependency dep2 = new RDependency();
    dep2.name = "triangle";
    assertEquals("triangle", dep2.toString());
  }
}
