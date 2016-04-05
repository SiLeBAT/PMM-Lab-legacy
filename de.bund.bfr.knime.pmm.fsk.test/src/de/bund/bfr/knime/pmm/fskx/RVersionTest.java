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
