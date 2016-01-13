package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

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
		if (id != null) {
			this.id = id;
		}
	}

	/** Sets the name value with 'name'. Ignores null and empty values. */
	public void setName(final String name) {
		if (!Strings.isNullOrEmpty(name)) {
			this.name = name;
		}
	}

	/** Sets the formula value with 'formula'. Ignores null and empty values. */
	public void setFormula(final String formula) {
		if (!Strings.isNullOrEmpty(formula)) {
			this.formula = formula;
		}
	}

	/** Sets the model class value with 'modelClass'. */
	public void setModelClass(final Integer modelClass) {
		if (modelClass != null) {
			this.modelClass = modelClass;
		}
	}

	/** Sets the comment value with 'comment'. Ignores null values. */
	public void setComment(final String comment) {
		if (!Strings.isNullOrEmpty(comment)) {
			this.comment = comment;
		}
	}

	/** Sets the dbuuid value with 'dbuuid'. Ignores null values. */
	public void setDbuuid(final String dbuuid) {
		if (!Strings.isNullOrEmpty(dbuuid)) {
			this.dbuuid = dbuuid;
		}
	}

	/** Saves catalog model properties into a {@link CatalogModel}. */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		if (id != null) {
			settings.addInt(ID, id);
		}
		if (name != null) {
			settings.addString(NAME, name);
		}
		if (formula != null) {
			settings.addString(FORMULA, formula);
		}
		if (modelClass != null) {
			settings.addInt(MODEL_CLASS, modelClass);
		}
		if (comment != null) {
			settings.addString(COMMENT, comment);
		}
		if (dbuuid != null) {
			settings.addString(DBUUID, dbuuid);
		}
	}

	/** Loads catalog model properties from a {@link CatalogModel}. */
	public void loadFromNodeSettings(final NodeSettingsRO settings) {
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
			formula = settings.getString(FORMULA);
		} catch (InvalidSettingsException e) {
			formula = null;
		}
		try {
			modelClass = settings.getInt(MODEL_CLASS);
		} catch (InvalidSettingsException e) {
			modelClass = null;
		}
		try {
			comment = settings.getString(COMMENT);
		} catch (InvalidSettingsException e) {
			comment = null;
		}
		try {
			dbuuid = settings.getString(DBUUID);
		} catch (InvalidSettingsException e) {
			dbuuid = null;
		}
	}

	/** Creates an {@link CatalogModelXml} from this {@link CatalogModel}. */
	public CatalogModelXml toCatalogModelXml() {
	    CatalogModelXml catalogModelXml = new CatalogModelXml(id, name,
	    	formula, modelClass, dbuuid);
	    catalogModelXml.setComment(comment);

	    return catalogModelXml;
	}

	/** Creates an {@link CatalogModel} from an {@link CatalogModelXml}. */
	public static CatalogModel toCatalogModel(CatalogModelXml catalogModelXml) {
	    CatalogModel catalogModel = new CatalogModel();
	    catalogModel.setId(catalogModelXml.getId());
	    catalogModel.setName(catalogModelXml.getName());
	    catalogModel.setFormula(catalogModelXml.getFormula());
	    catalogModel.setModelClass(catalogModelXml.getModelClass());
	    catalogModel.setComment(catalogModelXml.getComment());
	    catalogModel.setDbuuid(catalogModelXml.getDbuuid());

	    return catalogModel;
	}

}
