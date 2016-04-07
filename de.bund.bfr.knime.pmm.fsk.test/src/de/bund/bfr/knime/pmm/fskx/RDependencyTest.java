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
