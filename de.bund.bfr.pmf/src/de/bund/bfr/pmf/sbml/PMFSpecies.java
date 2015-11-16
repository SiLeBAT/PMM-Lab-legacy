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

import org.sbml.jsbml.Species;

/**
 * Represents the species XML element of a PMF-SBML file.
 * 
 * @author Miguel Alba
 */
public interface PMFSpecies {

	public static final boolean BOUNDARY_CONDITION = true;
	public static final boolean CONSTANT = false;
	public static final boolean ONLY_SUBSTANCE_UNITS = false;

	/** Returns the {@link Species} of this {@link PMFSpecies}. */
	public Species getSpecies();

	/** Returns the compartment of this {@link PMFSpecies}. */
	public String getCompartment();

	/** Returns the id of this {@link PMFSpecies}. */
	public String getId();

	/** Returns the name of this {@link PMFSpecies}. */
	public String getName();

	/** Returns the substance units of this {@link PMFSpecies}. */
	public String getSubstanceUnits();

	/** Returns the units of this {@link PMFSpecies}. */
	public String getUnits();

	/** Returns the combase code of this {@link PMFSpecies}. */
	public String getCombaseCode();

	/** Returns the detail of this {@link PMFSpecies}. */
	public String getDetail();

	/** Returns the description of this {@link PMFSpecies}. */
	public String getDescription();

	/**
	 * Sets the compartment value of this {@link PMFSpecies} with 'compartment'.
	 */
	public void setCompartment(String compartment);

	/** Sets the id value of this {@link PMFSpecies} with 'id'. */
	public void setId(String id);

	/** Sets the name value of this {@link PMFSpecies} with 'name'. */
	public void setName(String name);

	/**
	 * Sets the substance unit value of this {@link PMFSpecies} with
	 * 'substanceUnits'.
	 */
	public void setSubstanceUnits(String substanceUnits);

	/** Sets the units value of this {@link PMFSpecies} with 'units'. */
	public void setUnits(String units);

	/**
	 * Sets the combase code value of this {@link PMFSpecies} with
	 * 'combaseCode'.
	 */
	public void setCombaseCode(String combaseCode);

	/** Sets the detail of this {@link PMFSpecies} with 'detail'. */
	public void setDetail(String detail);

	/** Sets the description of this {@link PMFSpecies} with 'description'. */
	public void setDescription(String description);

	/** Returns true if the unit of this {@link PMFSpecies} is set. */
	public boolean isSetUnits();

	/** Returns true if the combase code of this {@link PMFSpecies} is set. */
	public boolean isSetCombaseCode();

	/** Returns true if the detail of this {@link PMFSpecies} is set. */
	public boolean isSetDetail();

	/** Returns true if the description of this {@link PMFSpecies} is set. */
	public boolean isSetDescription();
}
