/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.assertEquals;

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
    File f= File.createTempFile("temp", "");
    f.deleteOnExit();
    
    try (FileWriter fw = new FileWriter(f)) {
      fw.write(origScript);
      fw.close();
      rScript = new RScript(f);
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
