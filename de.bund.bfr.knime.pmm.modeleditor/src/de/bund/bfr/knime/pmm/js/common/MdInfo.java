package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.MdInfoXml;

/**
 * PmmLab model info. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>comment
 * <li>quality score
 * <li>checked
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class MdInfo implements ViewValue {

	// Configuration keys
	static final String ID = "ID";
	static final String NAME = "Name";
	static final String COMMENT = "Comment";
	static final String QUALITYSCORE = "QualityScore";
	static final String CHECKED = "Checked";

	private Integer id;
	private String name;
	private String comment;
	private Integer qualityScore;
	private Boolean checked;

	/** Returns the id of this {@link MdInfo}. If not set, returns null. */
	public Integer getId() {
		return id;
	}

	/** Returns the name of this {@link MdInfo}. If not set returns null. */
	public String getName() {
		return name;
	}

	/** Returns the comment of this {@link MdInfo}. If not set returns null. */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns the quality score of this {@link MdInfo}. If not set returns
	 * null.
	 */
	public Integer getQualityScore() {
		return qualityScore;
	}

	/** Returns the checked of this {@link MdInfo}. If not set returns null. */
	public Boolean getChecked() {
		return checked;
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
	 * Sets the comment value with 'comment'. Ignores null and empty strings.
	 */
	public void setComment(String comment) {
		if (!Strings.isNullOrEmpty(comment)) {
			this.comment = comment;
		}
	}

	/** Sets the quality score with 'qualityScore'. */
	public void setQualityScore(Integer qualityScore) {
		if (qualityScore != null) {
			this.qualityScore = qualityScore;
		}
	}

	/** Sets the checked value with 'checked'. */
	public void setChecked(Boolean checked) {
		if (checked != null) {
			this.checked = checked;
		}
	}

	/** Saves model info properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		if (id != null) {
			settings.addInt(ID, id);
		}
		if (name != null) {
			settings.addString(NAME, name);
		}
		if (comment != null) {
			settings.addString(COMMENT, comment);
		}
		if (qualityScore != null) {
			settings.addInt(QUALITYSCORE, qualityScore);
		}
		if (checked != null) {
			settings.addBoolean(CHECKED, checked);
		}
	}

	/** Loads model info properties from a {@link NodeSettingsRO}. */
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
			comment = settings.getString(COMMENT);
		} catch (InvalidSettingsException e) {
			comment = null;
		}
		try {
			qualityScore = settings.getInt(QUALITYSCORE);
		} catch (InvalidSettingsException e) {
			qualityScore = null;
		}
		try {
			checked = settings.getBoolean(CHECKED);
		} catch (InvalidSettingsException e) {
			checked = null;
		}
	}

	/** Creates an {@link MdInfoXml} from this {@link MdInfo}. */
	public MdInfoXml toMdInfoXml() {
		return new MdInfoXml(id, name, comment, qualityScore, checked);
	}

	/** Creates an {@link MdInfo} from an {@link MdInfoXml}. */
	public static MdInfo toMdInfo(MdInfoXml mdInfoXml) {
		MdInfo mdInfo = new MdInfo();
		mdInfo.setId(mdInfoXml.getId());
		mdInfo.setName(mdInfoXml.getName());
		mdInfo.setComment(mdInfoXml.getComment());
		mdInfo.setQualityScore(mdInfoXml.getQualityScore());
		mdInfo.setChecked(mdInfoXml.getChecked());

		return mdInfo;
	}
}
