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

	/** Sets the name value with 'name'. Converts empty strings to null. */
	public void setName(final String name) {
		this.name = Strings.emptyToNull(name);
	}

	/**
	 * Sets the origname value with 'origname'. Converts empty strings to null.
	 */
	public void setOrigname(final String origname) {
		this.origname = Strings.emptyToNull(origname);
	}

	/** Sets the min value with 'min'. */
	public void setMin(final Double min) {
		this.min = min;
	}

	/** Sets the max value with 'max'. */
	public void setMax(final Double max) {
		this.max = max;
	}

	/**
	 * Sets the category value with 'category'. Converts empty strings to null.
	 */
	public void setCategory(final String category) {
		this.category = Strings.emptyToNull(category);
	}

	/** Sets the unit value with 'unit'. Converts empty strings to null. */
	public void setUnit(final String unit) {
		this.unit = Strings.emptyToNull(unit);
	}

	/**
	 * Sets the description value with 'description'. Converts empty strings to
	 * null.
	 */
	public void setDescription(final String description) {
		this.description = description.isEmpty() ? null : description;
	}

	/** Saves dep properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(ORIGNAME, origname, settings);
		SettingsHelper.addDouble(MIN, min, settings);
		SettingsHelper.addDouble(MAX, max, settings);
		SettingsHelper.addString(CATEGORY, category, settings);
		SettingsHelper.addString(UNIT, unit, settings);
		SettingsHelper.addString(DESCRIPTION, description, settings);
	}

	/**
	 * Loads dep properties from a {@link NodeSettingsRO}.
	 * 
	 * @throws InvalidSettingsException
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		name = SettingsHelper.getString(NAME, settings);
		origname = SettingsHelper.getString(ORIGNAME, settings);
		min = SettingsHelper.getDouble(MIN, settings);
		max = SettingsHelper.getDouble(MAX, settings);
		category = SettingsHelper.getString(CATEGORY, settings);
		unit = SettingsHelper.getString(UNIT, settings);
		description = SettingsHelper.getString(DESCRIPTION, settings);
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
