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
package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.IndepXml;

/**
 * PmmLab independent variable. Holds:
 * <ul>
 * <li>name
 * <li>original name
 * <li>min
 * <li>max
 * <li>category
 * <li>unit
 * <li>description
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Indep implements ViewValue {

	// Configuration keys
	static final String NAME = "name";
	static final String ORIGNAME = "origname";
	static final String MIN = "min";
	static final String MAX = "max";
	static final String CATEGORY = "category";
	static final String UNIT = "unit";
	static final String DESCRIPTION = "description";

	private String name;
	private String origname;
	private Double min;
	private Double max;
	private String category;
	private String unit;
	private String description;

	/**
	 * Returns the name of this {@link Indep}. If name is not set, returns null.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the original of this {@link Indep}. If the original name is not
	 * set, returns null.
	 */
	public String getOrigname() {
		return origname;
	}

	/**
	 * Returns the minimum value of this {@link Indep}. If the minimum value is
	 * not set, returns null.
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * Returns the maximum value of this {@link Indep}. If the maximum value is
	 * not set, returns null.
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * Returns the category value of this {@link Indep}. If the category is not
	 * set, returns null.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the unit value of this {@link Indep}. If the unit is not set,
	 * returns null.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the description of this {@link Indep}. If the description is not
	 * set, returns null.
	 */
	public String getDescription() {
		return description;
	}

	/** Sets the name value with 'name'. Ignores null and empty strings. */
	public void setName(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			this.name = name;
		}
	}

	/**
	 * Sets the original name value with 'origname'. Ignores null and empty
	 * strings.
	 */
	public void setOrigname(String origname) {
		if (!Strings.isNullOrEmpty(origname)) {
			this.origname = origname;
		}
	}

	/** Sets the minimum value with 'min'. */
	public void setMin(Double min) {
		if (min != null) {
			this.min = min;
		}
	}

	/** Sets the maximum vlaue with 'max'. */
	public void setMax(Double max) {
		if (max != null) {
			this.max = max;
		}
	}

	/**
	 * Sets the category value with 'category'. Ignores null and empty strings.
	 */
	public void setCategory(String category) {
		if (!Strings.isNullOrEmpty(category)) {
			this.category = category;
		}
	}

	/** Sets the unit value with 'unit'. Ignores null and empty strings. */
	public void setUnit(String unit) {
		if (!Strings.isNullOrEmpty(unit)) {
			this.unit = unit;
		}
	}

	/**
	 * Sets the description value with 'description'. Ignores null and empty
	 * strings.
	 */
	public void setDescription(String description) {
		if (!Strings.isNullOrEmpty(description)) {
			this.description = description;
		}
	}

	/** Saves indep properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		if (name != null)
			settings.addString(NAME, name);
		if (origname != null)
			settings.addString(ORIGNAME, origname);
		if (min != null)
			settings.addDouble(MIN, min);
		if (max != null)
			settings.addDouble(MAX, max);
		if (category != null)
			settings.addString(CATEGORY, category);
		if (unit != null)
			settings.addString(UNIT, unit);
		if (description != null)
			settings.addString(DESCRIPTION, description);
	}

	/** Loads indep properties from a {@link NodeSettingsRO}. */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			name = settings.getString(NAME);
		} catch (InvalidSettingsException e) {
			name = null;
		}
		try {
			origname = settings.getString(ORIGNAME);
		} catch (InvalidSettingsException e) {
			origname = null;
		}
		try {
			min = settings.getDouble(MIN);
		} catch (InvalidSettingsException e) {
			min = null;
		}
		try {
			max = settings.getDouble(MAX);
		} catch (InvalidSettingsException e) {
			max = null;
		}
		try {
			category = settings.getString(CATEGORY);
		} catch (InvalidSettingsException e) {
			category = null;
		}
		try {
			unit = settings.getString(UNIT);
		} catch (InvalidSettingsException e) {
			unit = null;
		}
		try {
			description = settings.getString(DESCRIPTION);
		} catch (InvalidSettingsException e) {
			description = null;
		}
	}

	/** Creates an {@link IndepXml} from this {@link Indep}. */
	public IndepXml toIndepXml() {
		IndepXml indepXml = new IndepXml(name, origname, min, max, category, unit, description);

		return indepXml;
	}

	/** Creates an {@link Indep} from an {@link Indep}. */
	public static Indep toIndep(IndepXml indepXml) {
		Indep indep = new Indep();
		indep.setName(indepXml.getName());
		indep.setOrigname(indepXml.getOrigName());
		indep.setMin(indepXml.getMin());
		indep.setMax(indepXml.getMax());
		indep.setCategory(indepXml.getCategory());
		indep.setUnit(indepXml.getUnit());
		indep.setDescription(indepXml.getDescription());

		return indep;
	}
}
