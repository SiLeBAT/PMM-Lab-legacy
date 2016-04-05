/**
 * 
 */
package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Miguel Alba
 *
 */
public class RScriptTest {

  private final String origScript =
      "# This is a comment line: It should not appear in the simplified version\n"
          + "library(triangle)\n"
          + "source(other.R)\n"
          + "hist(result, breaks=50, main=\"PREVALENCE OF PARENT FLOCKS\")\n";
  private RScript rScript;

  @Before
  public void setUp() throws IOException {
    // Creates temporary file. Fails the test if an error occurs.
    File f;
    try {
      f = File.createTempFile("temp", "");
      f.deleteOnExit();
    } catch (IOException e) {
      fail(e.getMessage());
      throw e;
    }

    try (FileWriter fw = new FileWriter(f);) {
      fw.write(this.origScript);
      fw.close();
      this.rScript = new RScript(f);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void test() {
    
    assertEquals(origScript, rScript.getScript());

    List<String> expectedLibraries = Arrays.asList("triangle");
    List<String> obtainedLibraries = rScript.getLibraries();
    assertEquals(expectedLibraries, obtainedLibraries);

    List<String> expectedSources = Arrays.asList("other.R");
    List<String> obtainedSources = rScript.getSources();
    assertEquals(expectedSources, obtainedSources);
  }
}
