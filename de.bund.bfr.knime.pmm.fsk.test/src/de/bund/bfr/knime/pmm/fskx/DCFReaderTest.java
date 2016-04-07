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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.Test;

public class DCFReaderTest {

  @Test
  public void test() {
    String desc = "Package: qcc\n" + "Version: 2.6\n" + "Date: 2014-10-07\n"
        + "Title: Quality Control Charts\n"
        + "Description: Shewhart quality control charts for continuous, attribute and count data. "
        + "Cusum and EWMA charts. Operating characteristic curves. Process capability analysis. "
        + "Pareto chart and cause-and-effect chart. Multivariate control charts.\n"
        + "Author: Luca Scrucca <luca@stat.unipg.it>\n"
        + "Maintainer: Luca Scrucca <luca@stat.unipg.it>\n" + "Depends: R (>= 2.11)\n"
        + "Imports: MASS\n" + "License: GPL (>= 2)\n" + "Repository: CRAN\n" + "ByteCompile: true\n"
        + "LazyLoad: yes\n" + "Packaged: 2014-10-07 10:16:19 UTC; luca\n" + "NeedsCompilation: no\n"
        + "Date/Publication: 2014-10-07 12:42:16\n"
        + "Built: R 3.2.4; ; 2016-03-15 01:45:36 UTC; windows\n";
    InputStream stream = new ByteArrayInputStream(desc.getBytes(StandardCharsets.UTF_8));
    Map<String, String> props = DCFReader.read(stream);

    // Check package property
    assertTrue(props.containsKey("Package"));
    assertEquals("qcc", props.get("Package"));

    // Check version property
    assertTrue(props.containsKey("Version"));
    assertEquals("2.6", props.get("Version"));

    // Check date property
    assertTrue(props.containsKey("Date"));
    assertEquals("2014-10-07", props.get("Date"));

    // Check title property
    assertTrue(props.containsKey("Title"));
    assertEquals("Quality Control Charts", props.get("Title"));

    // Check description property
    assertTrue(props.containsKey("Description"));
    assertEquals(
        "Shewhart quality control charts for continuous, attribute and count data. "
            + "Cusum and EWMA charts. Operating characteristic curves. Process capability analysis. "
            + "Pareto chart and cause-and-effect chart. Multivariate control charts.",
        props.get("Description"));

    // Check author property
    assertTrue(props.containsKey("Author"));
    assertEquals("Luca Scrucca <luca@stat.unipg.it>", props.get("Author"));

    // Check maintainer property
    assertTrue(props.containsKey("Maintainer"));
    assertEquals("Luca Scrucca <luca@stat.unipg.it>", props.get("Maintainer"));

    // Check depends property
    assertTrue(props.containsKey("Depends"));
    assertEquals("R (>= 2.11)", props.get("Depends"));

    // Check imports property
    assertTrue(props.containsKey("Imports"));
    assertEquals("MASS", props.get("Imports"));

    // Check license property
    assertTrue(props.containsKey("License"));
    assertEquals("GPL (>= 2)", props.get("License"));

    // Check repository property
    assertTrue(props.containsKey("Repository"));
    assertEquals("CRAN", props.get("Repository"));

    // Check byteCompile property
    assertTrue(props.containsKey("ByteCompile"));
    assertEquals("true", props.get("ByteCompile"));

    // Check lazyLoad property
    assertTrue(props.containsKey("LazyLoad"));
    assertEquals("yes", props.get("LazyLoad"));

    // Check packaged property
    assertTrue(props.containsKey("Packaged"));
    assertEquals("2014-10-07 10:16:19 UTC; luca", props.get("Packaged"));

    // Check needCompilation property
    assertTrue(props.containsKey("NeedsCompilation"));
    assertEquals("no", props.get("NeedsCompilation"));

    // Check datePublication property
    assertTrue(props.containsKey("Date/Publication"));
    assertEquals("2014-10-07 12:42:16", props.get("Date/Publication"));

    // Check built property
    assertTrue(props.containsKey("Built"));
    assertEquals("R 3.2.4; ; 2016-03-15 01:45:36 UTC; windows", props.get("Built"));
  }

}
