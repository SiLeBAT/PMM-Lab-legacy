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

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Misc implements ViewValue {

	// Configuration keys
	static final String ID = "id";
	static final String NAME = "name";
	static final String DESCRIPTION = "description";
	static final String VALUE = "value";
	static final String CATEGORY = "category";
	static final String UNIT = "unit";
	static final String ORIGUNIT = "origUnit";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private String description;
	private Double value;
	private String[] categories;
	private String unit;
	private String origUnit;
	private String dbuuid;

	/** Returns the id of this {@link Misc}. If not set, returns null. */
	public Integer getId() {
		return id;
	}

	/** Returns the name of this {@link Misc}. If not set, returns null. */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description of this {@link Misc}. If not set, returns null.
	 */
	public String getDescription() {
		return description;
	}

	/** Returns the value of this {@link Misc}. If not set, returns null. */
	public Double getValue() {
		return value;
	}

	/**
	 * Returns the categories of this {@link Misc}. If not set, returns null.
	 */
	public String[] getCategories() {
		return categories;
	}

	/**
	 * Returns the original unit of this {@link Misc}. If not set, returns null.
	 */
	public String getOrigUnit() {
		return origUnit;
	}

	/** Returns the unit of this {@link Misc}. If not set, returns null. */
	public String getUnit() {
		return unit;
	}

	/** Returns the dbuuid of this {@link Misc}. If not set, returns null. */
	public String getDbuuid() {
		return dbuuid;
	}

	/** Sets the id value with 'id'. */
	public void setId(final Integer id) {
		this.id = id;
	}

	/** Sets the name value with 'name'. Converts empty strings to null. */
	public void setName(final String name) {
		this.name = Strings.emptyToNull(name);
	}

	/**
	 * Sets the description value with 'description'. Converts empty strings to
	 * null.
	 */
	public void setDescription(final String description) {
		this.description = Strings.emptyToNull(description);
	}

	/** Sets the value with 'value'. */
	public void setValue(final Double value) {
		this.value = value;
	}

	/** Sets the categories with 'categories'. */
	public void setCategories(final String[] categories) {
		this.categories = categories;
	}

	/** Sets the unit value with 'unit'. Converts empty strings to null. */
	public void setUnit(final String unit) {
		this.unit = Strings.emptyToNull(unit);
	}

	/**
	 * Sets the original unit with 'origUnit'. Converts empty strings to null.
	 */
	public void setOrigUnit(final String origUnit) {
		this.origUnit = Strings.emptyToNull(origUnit);
	}

	/** Sets the dbuuid value with 'dbuuid'. Converts empty strings to null. */
	public void setDbuuid(final String dbuuid) {
		this.dbuuid = dbuuid;
	}

	/** Saves misc properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(DESCRIPTION, description, settings);
		SettingsHelper.addDouble(VALUE, value, settings);
		settings.addStringArray(CATEGORY, categories);
		SettingsHelper.addString(UNIT, unit, settings);
		SettingsHelper.addString(ORIGUNIT, origUnit, settings);
		SettingsHelper.addString(DBUUID, dbuuid, settings);
	}

	/**
	 * Loads misc properties from a {@link NodeSettingsRO}.
	 * 
	 * @throws InvalidSettingsException
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		name = SettingsHelper.getString(NAME, settings);
		description = SettingsHelper.getString(DESCRIPTION, settings);
		value = SettingsHelper.getDouble(VALUE, settings);
		try {
			categories = settings.getStringArray(CATEGORY);
		} catch (InvalidSettingsException e) {
			categories = null;
		}
		unit = SettingsHelper.getString(UNIT, settings);
		origUnit = SettingsHelper.getString(ORIGUNIT, settings);
		dbuuid = SettingsHelper.getString(DBUUID, settings);
	}
}
