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

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RPackageMetadataTest {
  
  /** Test when the package property is present. */
  @Test
  public void testPackage() {
    Map<String, String> props = new HashMap<>();
    props.put("Package", "qcc");
    RPackageMetadata metaData = new RPackageMetadata(props);
    assertEquals("qcc", metaData.m_package);
  }
  
  /** Test when the type property is present. */
  @Test
  public void testType() {
    Map<String, String> props = new HashMap<>();
    props.put("Type", "Package");
    RPackageMetadata metaData = new RPackageMetadata(props);
    assertEquals("Package", metaData.m_type);
  }
  
  /** Test when the title property is present. */
  @Test
  public void testTitle() {
    Map<String, String> props = new HashMap<>();
    props.put("Title", "Quality Control Charts");
    RPackageMetadata metaData = new RPackageMetadata(props);
    assertEquals("Quality Control Charts", metaData.m_title);
  }

  /** Test when the version property is present. */
  @Test
  public void testVersion() {
    Map<String, String> props = new HashMap<>();
    props.put("Version", "2.6");
    RPackageMetadata metaData = new RPackageMetadata(props);
    assertEquals(RVersion.numericVersion("2.6"), metaData.m_version);
  }
  
  /** Test when the date property is present. */
  @Test
  public void testDate() {
    Map<String, String> props = new HashMap<>();
    props.put("Date", "2014-10-07");
    RPackageMetadata metaData = new RPackageMetadata(props);
    assertTrue(2014 == metaData.m_date.get(Calendar.YEAR));
    assertTrue(9 == metaData.m_date.get(Calendar.MONTH));  // 0-based
    assertTrue(7 == metaData.m_date.get(Calendar.DAY_OF_MONTH));
    
    // non-dates should be ignored
    props.clear();
    props.put("Date", "clearly not a date; 26/1/-1847");
    metaData = new RPackageMetadata(props);
    assertNull(metaData.m_date);
  }
  
  /** Test when the description is present. */
  @Test
  public void testDescription() {
    Map<String, String> props = new HashMap<>();
    String descr = "Shewhart quality control charts for continuous, attribute and count data. "
        + "Cusum and EWMA charts. Operating characteristic curves. Process capability analysis. "
        + "Pareto chart and cause-and-effect chart. Multivariate control charts.";
    props.put("Description", descr);
    RPackageMetadata metaData = new RPackageMetadata(props);
    assertEquals(descr, metaData.m_description);
  }
  
  @Test
  public void testDependencies() {
    
    // Test with dependencies
    Map<String, String> props = new HashMap<>();
    props.put("Depends", "R (>= 3.0.2), stats");
    RPackageMetadata metaData = new RPackageMetadata(props);
    
    RDependency rDep = new RDependency();
    rDep.name = "R";
    rDep.version = RVersion.numericVersion("3.0.2");
    
    RDependency statsDep = new RDependency();
    statsDep.name = "stats";
    
    assertEquals(Arrays.asList(rDep, statsDep), metaData.m_dependencies);
  }
  
  @Test
  public void testEmptyMetaData() {
    
    RPackageMetadata metaData = new RPackageMetadata(new HashMap<String, String>());
    assertNull(metaData.m_package);
    assertNull(metaData.m_title);
    assertNull(metaData.m_version);
    assertNull(metaData.m_date);
    assertNull(metaData.m_description);
    assertNull(metaData.m_dependencies);
  }
}
