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
public class Unit implements ViewValue {

	// Configuration keys
	static final String ID = "id";
	static final String UNIT = "unit";
	static final String DESCRIPTION = "description";
	static final String NAME = "name";
	static final String KIND_OF_PROPERTY_QUANTITY = "kindOfPropertyQuantity";
	static final String NOTATION_CASE_SENSITIVE = "notationCaseSensitive";
	static final String CONVERT_TO = "convertTo";
	static final String CONVERSION_FUNCTION_FACTOR = "conversionFunctionFactor";
	static final String INVERSION_CONVERSION_FUNCTION_FACTOR = "inversionConversionFunctionFactor";
	static final String OBJECT_TYPE = "objectType";
	static final String DISPLAY_IN_GUI_AS = "displayInGuiAs";
	static final String MATHML_STRING = "mathmlString";
	static final String PRIORITY_FOR_DISPLAY_IN_GUI = "priorityForDisplayInGui";

	// variables
	private Integer id;
	private String unit;
	private String description;
	private String name;
	private String kind_of_property_quantity;
	private String notation_case_sensitive;
	private String convert_to;
	private String conversion_function_factor;
	private String inverse_conversion_function_factor;
	private String object_type;
	private String display_in_GUI_as;
	private String mathML_string;
	private String priority_for_display_in_GUI;

	/** Returns the id of this {@link Unit}. If not set, returns null. */
	public Integer getId() {
		return id;
	}

	/** Returns the unit of this {@link Unit}. If not set, returns null. */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the description of this {@link Unit}. If not set, returns null.
	 */
	public String getDescription() {
		return description;
	}

	/** Returns the name of this {@link Unit}. If not set, returns null. */
	public String getName() {
		return name;
	}

	/**
	 * Returns the kind_of_property_quantity of this {@link Unit}. If not set,
	 * returns null.
	 */
	public String getKindOfPropertyQuantity() {
		return kind_of_property_quantity;
	}

	/**
	 * Returns the notation_case_sensitive of this {@link Unit}. If not set,
	 * returns null.
	 */
	public String getNotationCaseSensitive() {
		return notation_case_sensitive;
	}

	/**
	 * Returns the convert_to of this {@link Unit}. If not set, returns null.
	 */
	public String getConvertTo() {
		return convert_to;
	}

	/**
	 * Returns the conversion_function_factor of this {@link Unit}. If not set,
	 * returns null.
	 */
	public String getConversionFunctionFactor() {
		return conversion_function_factor;
	}

	/**
	 * Returns the inverse_conversion_function_factor of this {@link Unit}. If
	 * not set, returns null.
	 */
	public String getInverseConversionFunctionFactor() {
		return inverse_conversion_function_factor;
	}

	/**
	 * Returns the object_type of this {@link Unit}. If not set, returns null.
	 */
	public String getObjectType() {
		return object_type;
	}

	/**
	 * Returns the display_in_GUI_as of this {@link Unit}. If not set, returns
	 * null.
	 */
	public String getDisplayInGuiAs() {
		return display_in_GUI_as;
	}

	/**
	 * Returns the mathmlString of this {@link Unit}. If not set, returns null.
	 */
	public String getMathMLString() {
		return mathML_string;
	}

	/**
	 * Returns the priority_for_display_in_GUI of this {@link Unit}. If not set,
	 * returns null.
	 */
	public String getPriorityForDisplayInGui() {
		return priority_for_display_in_GUI;
	}

	/** Sets the id value with 'id'. */
	public void setId(int id) {
		this.id = id;
	}

	/** Sets the unit value with 'unit'. Ignores null and empty values. */
	public void setUnit(String unit) {
		if (!Strings.isNullOrEmpty(unit)) {
			this.unit = unit;
		}
	}

	/**
	 * Sets the description value with 'description'. Ignores null and empty
	 * values.
	 */
	public void setDescription(String description) {
		if (!Strings.isNullOrEmpty(description)) {
			this.description = description;
		}
	}

	/** Sets the name of this {@link Unit}. Ignores null and empty values. */
	public void setName(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			this.name = name;
		}
	}

	/**
	 * Sets the kind_of_property_quantity of this {@link Unit}. Ignores null and
	 * empty values.
	 */
	public void setKindOfPropertyQuantity(String kind_of_property_quantity) {
		if (!Strings.isNullOrEmpty(kind_of_property_quantity)) {
			this.kind_of_property_quantity = kind_of_property_quantity;
		}
	}

	/**
	 * Sets the notation_case_sensitive of this {@link Unit}. Ignores null and
	 * empty values.
	 */
	public void setNotationCaseSensitive(String notation_case_sensitive) {
		if (!Strings.isNullOrEmpty(notation_case_sensitive)) {
			this.notation_case_sensitive = notation_case_sensitive;
		}
	}

	/**
	 * Sets the convert_to of this {@link Unit}. Ignores null and empty values.
	 */
	public void setConvertTo(String convert_to) {
		if (!Strings.isNullOrEmpty(convert_to)) {
			this.convert_to = convert_to;
		}
	}

	/**
	 * Sets the conversion_function_factor of this {@link Unit}. Ignores null
	 * and empty values.
	 */
	public void setConversionFunctionFactor(String conversion_function_factor) {
		if (!Strings.isNullOrEmpty(conversion_function_factor)) {
			this.conversion_function_factor = conversion_function_factor;
		}
	}

	/**
	 * Sets the inverse_conversion_function_factor of this {@link Unit}. Ignores
	 * null and empty values.
	 */
	public void setInverseConversionFunctionFactor(String inverse_conversion_function_factor) {
		if (!Strings.isNullOrEmpty(inverse_conversion_function_factor)) {
			this.inverse_conversion_function_factor = inverse_conversion_function_factor;
		}
	}

	/**
	 * Sets the object_type of this {@link Unit}. Ignores null and empty values.
	 */
	public void setObjectType(String object_type) {
		if (!Strings.isNullOrEmpty(object_type)) {
			this.object_type = object_type;
		}
	}

	/**
	 * Sets the display_in_GUI_as of this {@link Unit}. Ignores null and empty
	 * values.
	 */
	public void setDisplayInGuiAs(String display_in_GUI_as) {
		if (!Strings.isNullOrEmpty(display_in_GUI_as)) {
			this.display_in_GUI_as = display_in_GUI_as;
		}
	}

	/**
	 * Sets the mathML_string of this {@link Unit}. Ignores null and empty
	 * values.
	 */
	public void setMathMLString(String mathML_string) {
		if (!Strings.isNullOrEmpty(mathML_string)) {
			this.mathML_string = mathML_string;
		}
	}

	/**
	 * Sets the priority_for_display_in_GUI of this {@link Unit}. Ignores null
	 * and empty values.
	 */
	public void setPriorityForDisplayInGui(String priority_for_display_in_GUI) {
		if (!Strings.isNullOrEmpty(priority_for_display_in_GUI)) {
			this.priority_for_display_in_GUI = priority_for_display_in_GUI;
		}
	}

	/** Saves unit properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		if (id != null)
			settings.addInt(ID, id);
		if (unit != null)
			settings.addString(UNIT, unit);
		if (description != null)
			settings.addString(DESCRIPTION, description);
		if (name != null)
			settings.addString(NAME, name);
		if (kind_of_property_quantity != null)
			settings.addString(KIND_OF_PROPERTY_QUANTITY, kind_of_property_quantity);
		if (notation_case_sensitive != null)
			settings.addString(NOTATION_CASE_SENSITIVE, notation_case_sensitive);
		if (convert_to != null)
			settings.addString(CONVERT_TO, convert_to);
		if (conversion_function_factor != null)
			settings.addString(CONVERSION_FUNCTION_FACTOR, conversion_function_factor);
		if (inverse_conversion_function_factor != null)
			settings.addString(INVERSION_CONVERSION_FUNCTION_FACTOR, inverse_conversion_function_factor);
		if (object_type != null)
			settings.addString(OBJECT_TYPE, object_type);
		if (display_in_GUI_as != null)
			settings.addString(DISPLAY_IN_GUI_AS, display_in_GUI_as);
		if (mathML_string != null)
			settings.addString(MATHML_STRING, mathML_string);
		if (priority_for_display_in_GUI != null)
			settings.addString(PRIORITY_FOR_DISPLAY_IN_GUI, priority_for_display_in_GUI);
	}

	/** Loads unit properties from a {@link NodeSettingsRO}. */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			id = settings.getInt(ID);
		} catch (InvalidSettingsException e) {
			id = null;
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
		try {
			name = settings.getString(NAME);
		} catch (InvalidSettingsException e) {
			name = null;
		}
		try {
			kind_of_property_quantity = settings.getString(KIND_OF_PROPERTY_QUANTITY);
		} catch (InvalidSettingsException e) {
			kind_of_property_quantity = null;
		}
		try {
			notation_case_sensitive = settings.getString(NOTATION_CASE_SENSITIVE);
		} catch (InvalidSettingsException e) {
			notation_case_sensitive = null;
		}
		try {
			convert_to = settings.getString(CONVERT_TO);
		} catch (InvalidSettingsException e) {
			convert_to = null;
		}
		try {
			conversion_function_factor = settings.getString(CONVERSION_FUNCTION_FACTOR);
		} catch (InvalidSettingsException e) {
			conversion_function_factor = null;
		}
		try {
			inverse_conversion_function_factor = settings.getString(INVERSION_CONVERSION_FUNCTION_FACTOR);
		} catch (InvalidSettingsException e) {
			inverse_conversion_function_factor = null;
		}
		try {
			object_type = settings.getString(OBJECT_TYPE);
		} catch (InvalidSettingsException e) {
			object_type = null;
		}
		try {
			display_in_GUI_as = settings.getString(DISPLAY_IN_GUI_AS);
		} catch (InvalidSettingsException e) {
			display_in_GUI_as = null;
		}
		try {
			mathML_string = settings.getString(MATHML_STRING);
		} catch (InvalidSettingsException e) {
			mathML_string = null;
		}
		try {
			priority_for_display_in_GUI = settings.getString(PRIORITY_FOR_DISPLAY_IN_GUI);
		} catch (InvalidSettingsException e) {
			priority_for_display_in_GUI = null;
		}
	}
}
