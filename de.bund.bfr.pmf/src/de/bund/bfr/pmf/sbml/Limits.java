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

/**
 * Limit values of a parameter.
 * 
 * @author Miguel Alba
 */
public class Limits {

  private String var;
  private Double min;
  private Double max;

  /**
   * Creates new Limits of a variable.
   * 
   * @param var Variable name.
   * @param min Variable minimum value.
   * @param max Variable maximum value.
   */
  public Limits(final String var, final Double min, final Double max) {
    this.var = var;
    this.min = min;
    this.max = max;
  }

  public String getVar() {
    return var;
  }

  public Double getMin() {
    return min;
  }

  public Double getMax() {
    return max;
  }

  public String toString() {
    return String.format(Locale.ENGLISH, "Limits [var=%s, min=%.6f, max=%.6f]", var, min, max);
  }
}
