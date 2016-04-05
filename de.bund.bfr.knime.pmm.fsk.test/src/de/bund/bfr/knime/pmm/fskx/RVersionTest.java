package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RVersionTest {

  /** Test version with major and minor version numbers. */
  @Test
  public void testShortVersion() {
    RVersion version = RVersion.numericVersion("1.0");
    assertTrue(1 == version.major);
    assertTrue(0 == version.minor);
    
    assertEquals("1.0", version.toString());
  }
  
  /** Test version with major, minor, and patch numbers. */
  @Test
  public void testFullVersion() {
    RVersion version = RVersion.numericVersion("1.0.0");
    assertTrue(1 == version.major);
    assertTrue(0 == version.minor);
    assertTrue(0 == version.patch);
    
    assertEquals("1.0.0", version.toString());
  }

}
