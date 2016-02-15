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
package de.bund.bfr.pmf.sbml;

import java.util.Locale;

import org.sbml.jsbml.Unit;

public class PMFUnit {

  private static final int LEVEL = 3;
  private static final int VERSION = 1;

  public static final double DEFAULT_MULTIPLIER = 1.0;
  public static final int DEFAULT_SCALE = 0;
  public static final double DEFAULT_EXPONENT = 1.0;

  Unit unit;

  /**
   * Creates a PMFUnit instance from a multiplier, scale, kind and exponent.
   */
  public PMFUnit(final double multiplier, final int scale, final Unit.Kind kind,
      final double exponent) {
    unit = new Unit(multiplier, scale, kind, exponent, LEVEL, VERSION);
  }

  public double getMultiplier() {
    return unit.getMultiplier();
  }


  public int getScale() {
    return unit.getScale();
  }


  public Unit.Kind getKind() {
    return unit.getKind();
  }


  public double getExponent() {
    return unit.getExponent();
  }

  /** Sets the exponent of this {@link PMFUnit}. */
  public void setExponent(final double exponent) {
    unit.setExponent(exponent);
  }

  /** Sets the multiplier of this {@link PMFUnit}. */
  public void setMultiplier(final double multiplier) {
    unit.setMultiplier(multiplier);
  }

  /** Sets the Unit.Kind of this {@link PMFUnit}. */
  public void setKind(final Unit.Kind kind) {
    unit.setKind(kind);
  }

  /** Sets the scale of this {@link PMFUnit}. */
  public void setScale(final int scale) {
    unit.setScale(scale);
  }

  public String toString() {
    return String.format(Locale.ENGLISH, "unit [exponent=%.6f, kind=%s, multiplier=%.6f, scale=%d]",
        unit.getExponent(), unit.getKind(), unit.getMultiplier(), unit.getScale());
  }

  public boolean equals(final Object other) {
    PMFUnit otherPMFUnit = (PMFUnit) other;
    if (unit.getMultiplier() != otherPMFUnit.getMultiplier())
      return false;
    if (unit.getScale() != otherPMFUnit.getScale())
      return false;
    if (unit.getKind() != otherPMFUnit.getKind())
      return false;
    if (unit.getExponent() != otherPMFUnit.getExponent())
      return false;
    return true;
  }
}
