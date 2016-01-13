package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.DepXml;

/**
 * PmmLab dep. Holds:
 * <ul>
 * <li>name
 * <li>origname
 * <li>min
 * <li>max
 * <li>category
 * <li>unit
 * <li>description
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Dep implements ViewValue {

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

	/** Returns the id of this {@link Dep}. If name is not set, returns null. */
	public String getName() {
		return name;
	}

	/**
	 * Returns the origname of this {@link Dep}. If origname is not set, returns
	 * null.
	 */
	public String getOrigname() {
		return origname;
	}

	/** Returns the min of this {@link Dep}. If min is not set, returns null. */
	public Double getMin() {
		return min;
	}

	/** Returns the max of this {@link Dep}. If max is not set, returns null. */
	public Double getMax() {
		return max;
	}

	/**
	 * Returns the category of this {@link Dep}. If category is not set, returns
	 * null.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the unit of this {@link Dep}. If unit is not set, returns null.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the description of this {@link Dep}. If description is not set,
	 * returns null.
	 */
	public String getDescription() {
		return description;
	}

	/** Sets the name value with 'name'. Ignores null and empty values. */
	public void setName(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			this.name = name;
		}
	}

	/**
	 * Sets the origname value with 'origname'. Ignores null and empty values.
	 */
	public void setOrigname(String origname) {
		if (!Strings.isNullOrEmpty(origname)) {
			this.origname = origname;
		}
	}

	/** Sets the min value with 'min'. */
	public void setMin(Double min) {
		if (min != null) {
			this.min = min;
		}
	}

	/** Sets the max value with 'max'. */
	public void setMax(Double max) {
		if (max != null) {
			this.max = max;
		}
	}

	/**
	 * Sets the category value with 'category'. Ignores null and empty values.
	 */
	public void setCategory(String category) {
		if (!Strings.isNullOrEmpty(category)) {
			this.category = category;
		}
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

	/** Saves dep properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		if (name != null) {
			settings.addString(NAME, name);
		}
		if (origname != null) {
			settings.addString(ORIGNAME, origname);
		}
		if (min != null) {
			settings.addDouble(MIN, min);
		}
		if (max != null) {
			settings.addDouble(MAX, max);
		}
		if (category != null) {
			settings.addString(CATEGORY, category);
		}
		if (unit != null) {
			settings.addString(UNIT, unit);
		}
		if (description != null) {
			settings.addString(DESCRIPTION, description);
		}
	}

	/** Loads dep properties from a {@link NodeSettingsRO}. */
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

	/** Creates an {@link DepXml} from this {@link Dep}. */
	public DepXml toDepXml() {
		DepXml depXml = new DepXml(name, origname, category, unit, description);
		depXml.setMin(min);
		depXml.setMax(max);

		return depXml;
	}

	/** Creates an {@link Dep} from this {@link DepXml}. */
	public static Dep toDep(DepXml depXml) {
		Dep dep = new Dep();
		dep.setName(depXml.getName());
		dep.setOrigname(depXml.getOrigName());
		dep.setMin(depXml.getMin());
		dep.setMax(depXml.getMax());
		dep.setCategory(depXml.getCategory());
		dep.setUnit(depXml.getUnit());
		dep.setDescription(depXml.getDescription());

		return dep;
	}
}
