/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.pmm.fskx;

import com.google.common.base.Objects;

/**
 * Numeric version for a R package.
 * 
 * At least two version numbers, the major and minor numbers. A third optional version number,
 * called patch, is recommended although not mandatory. Version numbers may be separated with dots
 * "." or dashes "-". The use of dashes is discouraged.
 * 
 * @author Miguel Alba
 */
public class RVersion {
  int major;
  int minor;
  Integer patch;
  
  private boolean isSetPatch = false;

  public static RVersion numericVersion(String string) {
    RVersion version = new RVersion();

    // actual parsing
    String[] tokens = string.split("-|\\.");

    version.major = Integer.valueOf(tokens[0]);
    version.minor = Integer.valueOf(tokens[1]);

    // Patch number
    if (tokens.length == 3) {
      version.patch = Integer.valueOf(tokens[2]);
      version.isSetPatch = true;
    }

    return version;
  }

  /** @return string with version numbers joined on dots. */
  @Override
  public String toString() {
    String string = major + "." + minor;
    if (isSetPatch) {
      string += "." + patch;
    }

    return string;
  }

  @Override
  public boolean equals(Object obj) {
    RVersion other = (RVersion) obj;
    return major == other.major && minor == other.minor && Objects.equal(patch, other.patch);
  }
}
