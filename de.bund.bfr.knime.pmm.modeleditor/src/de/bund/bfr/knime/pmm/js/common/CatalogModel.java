package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

/**
 * PmmLab catalog model. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>formula
 * <li>model class
 * <li>comment
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class CatalogModel implements ViewValue {

	// Configuration keys
	static final String ID = "id";
	static final String NAME = "name";
	static final String FORMULA = "formula";
	static final String MODEL_CLASS = "modelClass";
	static final String COMMENT = "comment";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private String formula;
	private Integer modelClass;
	private String comment;
	private String dbuuid;

	/**
	 * Returns the id of this {@link CatalogModel}. If id is not set, returns
	 * null.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the name of this {@link CatalogModel}. If name is not set,
	 * returns null.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the formula of this {@link CatalogModel}. If formula is not set,
	 * returns null.
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * Returns the model class of this {@link CataloModel}. If model class is
	 * not set, returns null.
	 */
	public Integer getModelClass() {
		return modelClass;
	}

	/**
	 * Returns the comment of this {@link CatalogModel}. If comment is not set,
	 * returns null.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns the dbuuid of this {@link CatalogModel}. If dbuuid is not set,
	 * returns null.
	 */
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

	/** Sets the formula value with 'formula'. Converts empty strings to null. */
	public void setFormula(final String formula) {
		this.formula = Strings.emptyToNull(formula);
	}

	/** Sets the model class value with 'modelClass'. */
	public void setModelClass(final Integer modelClass) {
		this.modelClass = modelClass;
	}

	/** Sets the comment value with 'comment'. Converts empty strings to null. */
	public void setComment(final String comment) {
		this.comment = Strings.emptyToNull(comment);
	}

	/** Sets the dbuuid value with 'dbuuid'. Converts empty strings to null. */
	public void setDbuuid(final String dbuuid) {
		this.dbuuid = Strings.emptyToNull(dbuuid);
	}

	/** Saves catalog model properties into a {@link CatalogModel}. */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(FORMULA, formula, settings);
		SettingsHelper.addInt(MODEL_CLASS, modelClass, settings);
		SettingsHelper.addString(COMMENT, comment, settings);
		SettingsHelper.addString(DBUUID, dbuuid, settings);
	}

	/**
	 * Loads catalog model properties from a {@link CatalogModel}.
	 * 
	 * @throws InvalidSettingsException
	 */
	public void loadFromNodeSettings(final NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		name = SettingsHelper.getString(NAME, settings);
		formula = SettingsHelper.getString(FORMULA, settings);
		modelClass = SettingsHelper.getInteger(MODEL_CLASS, settings);
		comment = SettingsHelper.getString(COMMENT, settings);
		dbuuid = SettingsHelper.getString(DBUUID, settings);
	}
}
