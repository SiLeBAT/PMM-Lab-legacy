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

/**
 * Holds uncertainty values of a model:
 * <ul>
 * <li>ID</li>
 * <li>Model name</li>
 * <li>Comment</li>
 * <li>Coefficient of determination, R squared.</li>
 * <li>Sum of squared errors of prediction, SSE</li>
 * <li>Akaike information criterion, AIC</li>
 * <li>Bayesian information criterion, BIC</li>
 * <li>Degrees of freedom, dof</li>
 * </ul>
 * 
 * @author Miguel Alba
 */
public interface Uncertainties {

  /** Returns the id of this {@link Uncertainties}. If not set returns null. */
  public Integer getID();

  /** Returns the modelName of this {@link Uncertainties}. If not set returns null. */
  public String getModelName();

  /** Returns the comment of this {@link Uncertainties}. If not set returns null. */
  public String getComment();

  /** Returns the r2 of this {@link Uncertainties}. If not set returns null. */
  public Double getR2();

  /** Returns the rms of this {@link Uncertainties}. If not set returns null. */
  public Double getRMS();

  /** Returns the sse of this {link Uncertainties}. If not set returns null. */
  public Double getSSE();

  /** Returns the aic of this {link Uncertainties}. If not set returns null. */
  public Double getAIC();

  /** Returns the bic of this {link Uncertainties}. If not set returns null. */
  public Double getBIC();

  /** Returns the dof of this {link Uncertainties}. If not set returns null. */
  public Integer getDOF();

  /** Sets the id value with 'id'. */
  public void setID(int id);

  /**
   * Sets the modelName value with 'modelName'. If 'modelName' is null or empty it will do nothing.
   */
  public void setModelName(final String modelName);

  /** Sets the comment value with 'comment'. If 'comment' is null or empty it will do nothing. */
  public void setComment(final String comment);

  /** Sets the r2 value with 'r2'. */
  public void setR2(final double r2);

  /** Sets the rms value with 'rms'. */
  public void setRMS(final double rms);

  /** Sets the sse value with 'sse'. */
  public void setSSE(final double sse);

  /** Sets the aic value with 'aic'. */
  public void setAIC(final double aic);

  /** Sets the bic value with 'bic'. */
  public void setBIC(final double bic);

  /** Sets the dof value with 'dof'. */
  public void setDOF(final int dof);

  /** Returns true if the id of this {@link Uncertainties} is set. */
  public boolean isSetID();

  /** Returns true if the modelName of this {@link Uncertainties} is set. */
  public boolean isSetModelName();

  /** Returns true if the comment of this {@link Uncertainties} is set. */
  public boolean isSetComment();

  /** Returns true if the r2 of this {@link Uncertainties} is set. */
  public boolean isSetR2();

  /** Returns true if the rms of this {@link Uncertainties} is set. */
  public boolean isSetRMS();

  /** Returns true if the sse of this {@link Uncertainties} is set. */
  public boolean isSetSSE();

  /** Returns true if the aic of this {@link Uncertainties} is set. */
  public boolean isSetAIC();

  /** Returns true if the bic of this {@link Uncertainties} is set. */
  public boolean isSetBIC();

  /** Returns true if the dof of this {@link Uncertainties} is set. */
  public boolean isSetDOF();
}
