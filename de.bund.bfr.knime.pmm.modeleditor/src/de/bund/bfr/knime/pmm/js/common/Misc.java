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

import java.util.Arrays;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.MiscXml;

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
	public void setId(Integer id) {
		if (id != null) {
			this.id = id;
		}
	}

	/** Sets the name value with 'name'. Ignores null and empty strings. */
	public void setName(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			this.name = name;
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

	/** Sets the value with 'value'. */
	public void setValue(Double value) {
		if (value != null) {
			this.value = value;
		}
	}

	/** Sets the categories with 'categories'. */
	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	/** Sets the unit value with 'unit'. Ignores null and empty strings. */
	public void setUnit(String unit) {
		if (!Strings.isNullOrEmpty(unit)) {
			this.unit = unit;
		}
	}

	/**
	 * Sets the original unit with 'origUnit'. Ignores null and empty strings.
	 */
	public void setOrigUnit(String origUnit) {
		if (!Strings.isNullOrEmpty(origUnit)) {
			this.origUnit = origUnit;
		}
	}

	/** Sets the dbuuid value with 'dbuuid'. Ignores null and empty strings. */
	public void setDbuuid(String dbuuid) {
		if (!Strings.isNullOrEmpty(dbuuid)) {
			this.dbuuid = dbuuid;
		}
	}

	/** Saves misc properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		if (id != null)
			settings.addInt(ID, id);
		if (name != null)
			settings.addString(NAME, name);
		if (description != null)
			settings.addString(DESCRIPTION, description);
		if (value != null)
			settings.addDouble(VALUE, value);
		if (categories != null)
			settings.addStringArray(CATEGORY, categories);
		if (unit != null)
			settings.addString(UNIT, unit);
		if (origUnit != null)
			settings.addString(ORIGUNIT, origUnit);
		if (dbuuid != null)
			settings.addString(DBUUID, dbuuid);
	}

	/** Loads misc properties from a {@link NodeSettingsRO}. */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			id = settings.getInt(ID);
		} catch (InvalidSettingsException e) {
			id = null;
		}
		try {
			name = settings.getString(NAME);
		} catch (InvalidSettingsException e) {
			name = null;
		}
		try {
			description = settings.getString(DESCRIPTION);
		} catch (InvalidSettingsException e) {
			description = null;
		}
		try {
			value = settings.getDouble(VALUE);
		} catch (InvalidSettingsException e) {
			value = null;
		}
		try {
			categories = settings.getStringArray(CATEGORY);
		} catch (InvalidSettingsException e) {
			categories = null;
		}
		try {
			unit = settings.getString(UNIT);
		} catch (InvalidSettingsException e) {
			unit = null;
		}
		try {
			origUnit = settings.getString(ORIGUNIT);
		} catch (InvalidSettingsException e) {
			origUnit = null;
		}
		try {
			dbuuid = settings.getString(DBUUID);
		} catch (InvalidSettingsException e) {
			dbuuid = null;
		}
	}

	/** Creates an {@link MiscXml} from this {@link Misc}. */
	public MiscXml toMiscXml() {
		return new MiscXml(id, name, description, value, Arrays.asList(categories), unit, origUnit, dbuuid);
	}

	/** Creates an {@link Misc} from this {@link MiscXml}. */
	public static Misc toMisc(MiscXml miscXml) {
		Misc misc = new Misc();
		misc.setId(miscXml.getId());
		misc.setName(miscXml.getName());
		misc.setDescription(miscXml.getDescription());
		misc.setValue(miscXml.getValue());
		misc.setCategories(miscXml.getCategories().toArray(new String[miscXml.getCategories().size()]));
		misc.setUnit(miscXml.getUnit());
		misc.setOrigUnit(miscXml.getOrigUnit());
		misc.setDbuuid(miscXml.getDbuuid());

		return misc;
	}
}
