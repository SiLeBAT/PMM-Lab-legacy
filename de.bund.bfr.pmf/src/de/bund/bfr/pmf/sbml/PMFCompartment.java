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

import org.sbml.jsbml.Compartment;

/**
 * Represents the compartment XML element of a PMF-SBML file. It has the properties:
 * <ul>
 * <li>Id (mandatory)</li>
 * <li>Name (mandatory)</li>
 * <li>PMF code (optional)</li>
 * <li>Detail (optional)</li>
 * <li>Model variables(optional)</li>
 * </ul>
 *
 * @author Miguel Alba
 */
public interface PMFCompartment {

  /** Returns the id of this {@link PMFCompartment}. */
  public String getId();

  /** Returns the name of this {@link PMFCompartment}. */
  public String getName();

  /** Returns the PMF code of this {@link PMFCompartment}. If not set, returns null. */
  public String getPMFCode();

  /** Returns the detail of this {@link PMFCompartment}. If not set, returns null. */
  public String getDetail();

  /**
   * Returns the {@link ModelVariable}s of this {@link PMFCompartment}. If not set, returns null.
   */
  public ModelVariable[] getModelVariables();

  /** Returns the {@link Compartment} of this {@link PMFCompartment}. */
  public Compartment getCompartment();

  /**
   * Sets the id value of this {@link PMFCompartment} with 'id'. Ignores null or empty strings.
   */
  public void setId(final String id);

  /**
   * Sets the name value of this {@link PMFCompartment} with 'name'. Ignores null or empty strings.
   */
  public void setName(final String name);

  /**
   * Sets the PMF code value of this {@link PMFCompartment} with 'pmfCode'. Ignores null or empty
   * strings.
   */
  public void setPMFCode(final String pmfCode);

  /**
   * Sets the detail of this {@link PMFCompartment} with 'detail'. Ignores null or empty strings.
   */
  public void setDetail(final String detail);

  /**
   * Sets the model variables of this (@link PMFCompartment) with 'modelVariables'. Ignores null.
   */
  public void setModelVariables(final ModelVariable[] modelVariables);

  /** Returns true if the PMF code of this {@link PMFCompartment} is set. */
  public boolean isSetPMFCode();

  /** Returns true if the detail of this {@link PMFCompartment} is set. */
  public boolean isSetDetail();

  /** Returns true if the {@link ModelVariable}s of this {@link PMFCompartment} are set. */
  public boolean isSetModelVariables();
}
