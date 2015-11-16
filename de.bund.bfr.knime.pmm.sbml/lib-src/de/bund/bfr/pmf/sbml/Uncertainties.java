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
package de.bund.bfr.pmf.sbml;

/**
 * Holds uncertainty values of a model.
 *
 * @author Miguel Alba
 */
public interface Uncertainties {

	/** Returns the id of this {@link Uncertainties}. */
	public Integer getID(); 

	/** Returns the modelName of this {@link Uncertainties}. */
	public String getModelName(); 

	/** Returns the comment of this {@link Uncertainties}. */
	public String getComment(); 

	/** Returns the r2 of this {@link Uncertainties}. */
	public Double getR2();

	/** Returns the rms of this {@link Uncertainties}. */
	public Double getRMS();

	/** Returns the sse of this {link Uncertainties}. */
	public Double getSSE();

	/** Returns the aic of this {link Uncertainties}. */
	public Double getAIC();

	/** Returns the bic of this {link Uncertainties}. */
	public Double getBIC();

	/** Returns the dof of this {link Uncertainties}. */
	public Integer getDOF();
	
	/** Sets the id value with 'id'. */
	public void setID(Integer id);
	
	/** Sets the modelName value with 'modelName'. */
	public void setModelName(String modelName); 

	/** Sets the comment value with 'comment'. */
	public void setComment(String comment); 

	/** Sets the r2 value with 'r2'. */
	public void setR2(Double r2); 

	/** Sets the rms value with 'rms'. */
	public void setRMS(Double rms);

	/** Sets the sse value with 'sse'. */
	public void setSSE(Double sse); 

	/** Sets the aic value with 'aic'. */
	public void setAIC(Double aic); 

	/** Sets the bic value with 'bic'. */
	public void setBIC(Double bic);

	/** Sets the dof value with 'dof'. */
	public void setDOF(Integer dof); 

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

	/** Returns true if the bic of this {@link Uncertainties} is set.*/
	public boolean isSetBIC(); 

	/** Returns true if the dof of this {@link Uncertainties} is set.*/
	public boolean isSetDOF(); 
}
