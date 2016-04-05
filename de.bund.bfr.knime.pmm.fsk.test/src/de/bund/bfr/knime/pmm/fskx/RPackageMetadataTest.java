package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Test;

public class RPackageMetadataTest {
  
  String desc = "Package: qcc\n"
      + "Version: 2.6\n"
      + "Date: 2014-10-07\n"
      + "Title: Quality Control Charts\n"
      + "Description: Shewhart quality control charts for continuous, attribute and count data. "
      + "Cusum and EWMA charts. Operating characteristic curves. Process capability analysis. "
      + "Pareto chart and cause-and-effect chart. Multivariate control charts.\n"
      + "Author: Luca Scrucca <luca@stat.unipg.it>\n"
      + "Maintainer: Luca Scrucca <luca@stat.unipg.it>\n"
      + "Depends: R (>= 2.11)\n"
      + "Imports: MASS\n"
      + "License: GPL (>= 2)\n"
      + "Repository: CRAN\n"
      + "ByteCompile: true\n"
      + "LazyLoad: yes\n"
      + "Packaged: 2014-10-07 10:16:19 UTC; luca\n"
      + "NeedsCompilation: no\n"
      + "Date/Publication: 2014-10-07 12:42:16\n"
      + "Built: R 3.2.4; ; 2016-03-15 01:45:36 UTC; windows\n";

  @Test
  public void test() {
    InputStream stream = new ByteArrayInputStream(desc.getBytes(StandardCharsets.UTF_8));
    RPackageMetadata metaData = RPackageMetadata.parseDescription(stream);
    
    assertEquals(metaData.m_package, "qcc");
    
    assertTrue(metaData.m_version.major == 2);
    assertTrue(metaData.m_version.minor == 6);
    
    String description = "Shewhart quality control charts for continuous, attribute and count data."
        + " Cusum and EWMA charts. Operating characteristic curves. Process capability analysis. "
        + "Pareto chart and cause-and-effect chart. Multivariate control charts.";
    assertEquals(metaData.m_description, description);
    
    RDependency dep = new RDependency();
    dep.name = "R";
    dep.version = RVersion.numericVersion("2.11");
    assertEquals(metaData.m_dependencies, Arrays.asList(dep));
    // TODO:
  }
}
