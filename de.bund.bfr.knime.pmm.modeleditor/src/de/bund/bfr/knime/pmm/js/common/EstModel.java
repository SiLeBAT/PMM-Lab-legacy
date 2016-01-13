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

import de.bund.bfr.knime.pmm.common.EstModelXml;

/**
 * PmmLab estimated model. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>sse
 * <li>rms
 * <li>r2
 * <li>aic
 * <li>bic
 * <li>dof
 * <li>quality score
 * <li>checked
 * <li>comment
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class EstModel implements ViewValue {

	// Configuration keys
	static final String ID = "id";
	static final String NAME = "name";
	static final String SSE = "sse";
	static final String RMS = "rms";
	static final String R2 = "r2";
	static final String AIC = "aic";
	static final String BIC = "bic";
	static final String DOF = "dof";
	static final String QUALITY_SCORE = "qualityScore";
	static final String CHECKED = "checked";
	static final String COMMENT = "comment";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private Double sse;
	private Double rms;
	private Double r2;
	private Double aic;
	private Double bic;
	private Integer dof;
	private Integer qualityScore;
	private Boolean checked;
	private String comment;
	private String dbuuid;

	/**
	 * Returns the id of this {@link EstModel}. If id is not set, returns null.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the name of this {@link EstModel}. If name is not set, returns
	 * null.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the sse of this {@link EstModel}. If sse is not set, returns
	 * null.
	 */
	public Double getSse() {
		return sse;
	}

	/**
	 * Returns the rms of this {@link EstModel}. If rms is not set, returns
	 * null.
	 */
	public Double getRms() {
		return rms;
	}

	/**
	 * Returns the r2 of this {@link EstModel}. If r2 is not set, returns null.
	 */
	public Double getR2() {
		return r2;
	}

	/**
	 * Returns the aic of this {@link EstModel}. If aic is not set, returns
	 * null.
	 */
	public Double getAIC() {
		return aic;
	}

	/**
	 * Returns the bic of this {@link EstModel}. If bic is not set, returns
	 * null.
	 */
	public Double getBIC() {
		return bic;
	}

	/**
	 * Returns the dof of this {@link EstModel}. If dof is not set, returns
	 * null.
	 */
	public Integer getDof() {
		return dof;
	}

	/**
	 * Returns the quality score of this {@link EstModel}. If dof is not set,
	 * returns null.
	 */
	public Integer getQualityScore() {
		return qualityScore;
	}

	/**
	 * Returns the checked of this {@link EstModel}. If checked is not set,
	 * returns null.
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * Returns the comment of this {@link EstModel}. If comment is not set,
	 * returns null.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns the dbuuid of this {@link EstModel}. If dbuuid is not set,
	 * returns null.
	 */
	public String getDbuuid() {
		return dbuuid;
	}

	/** Sets the id value with 'id. */
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

	/** Sets the sse value with 'sse'. */
	public void setSse(Double sse) {
		if (sse != null) {
			this.sse = sse;
		}
	}

	/** Sets the rms value with 'rms'. */
	public void setRms(Double rms) {
		if (rms != null) {
			this.rms = rms;
		}
	}

	/** Sets the r2 value with 'r2'. */
	public void setR2(Double r2) {
		if (r2 != null) {
			this.r2 = r2;
		}
	}

	/** Sets the aic value with 'aic'. */
	public void setAIC(Double aic) {
		if (aic != null) {
			this.aic = aic;
		}
	}

	/** Sets the bic value with 'bic'. */
	public void setBIC(Double bic) {
		if (bic != null) {
			this.bic = bic;
		}
	}

	/** Sets the dof value with 'dof'. */
	public void setDof(Integer dof) {
		if (dof != null) {
			this.dof = dof;
		}
	}

	/** Sets the quality score value with 'qualityScore'. */
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

	/**
	 * Sets the comment value with 'comment'. Ignores null and empty strings.
	 */
	public void setComment(String comment) {
		if (!Strings.isNullOrEmpty(comment)) {
			this.comment = comment;
		}
	}

	/** Sets the dbuuid value with 'dbuuid'. Ignores null and empty strings. */
	public void setDbuuid(String dbuuid) {
		if (!Strings.isNullOrEmpty(dbuuid)) {
			this.dbuuid = dbuuid;
		}
	}

	/** Saves estimated model properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		if (id != null)
			settings.addInt(ID, id);
		if (name != null)
			settings.addString(NAME, name);
		if (sse != null)
			settings.addDouble(SSE, sse);
		if (rms != null)
			settings.addDouble(RMS, rms);
		if (r2 != null)
			settings.addDouble(R2, r2);
		if (aic != null)
			settings.addDouble(AIC, aic);
		if (bic != null)
			settings.addDouble(BIC, bic);
		if (dof != null)
			settings.addInt(DOF, dof);
		if (qualityScore != null)
			settings.addInt(QUALITY_SCORE, qualityScore);
		if (checked != null)
			settings.addBoolean(CHECKED, checked);
		if (comment != null)
			settings.addString(COMMENT, comment);
		if (dbuuid != null)
			settings.addString(DBUUID, dbuuid);
	}

	/** Loads estimated model properties from a {@link NodeSettingsRO}. */
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
			sse = settings.getDouble(SSE);
		} catch (InvalidSettingsException e) {
			sse = null;
		}

		try {
			rms = settings.getDouble(RMS);
		} catch (InvalidSettingsException e) {
			rms = null;
		}

		try {
			r2 = settings.getDouble(R2);
		} catch (InvalidSettingsException e) {
			r2 = null;
		}

		try {
			aic = settings.getDouble(AIC);
		} catch (InvalidSettingsException e) {
			aic = null;
		}

		try {
			bic = settings.getDouble(BIC);
		} catch (InvalidSettingsException e) {
			bic = null;
		}

		try {
			dof = settings.getInt(DOF);
		} catch (InvalidSettingsException e) {
			dof = null;
		}

		try {
			qualityScore = settings.getInt(QUALITY_SCORE);
		} catch (InvalidSettingsException e) {
			qualityScore = null;
		}

		try {
			checked = settings.getBoolean(CHECKED);
		} catch (InvalidSettingsException e) {
			checked = null;
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

	/** Creates an {@link EstModelXml} from this {@link EstModel}. */
	public EstModelXml toEstModelXml() {
		EstModelXml estModelXml = new EstModelXml(id, name, sse, rms, r2, aic, bic, dof, true, qualityScore, dbuuid);
		estModelXml.setComment(comment);

		return estModelXml;
	}

	/** Creates an {@link EstModel} from an {@link EstModelXml}. */
	public static EstModel toEstModel(EstModelXml estModelXml) {
		EstModel estModel = new EstModel();
		estModel.setId(estModelXml.getId());
		estModel.setName(estModelXml.getName());
		estModel.setSse(estModelXml.getSse());
		estModel.setRms(estModelXml.getRms());
		estModel.setR2(estModelXml.getR2());
		estModel.setAIC(estModelXml.getAic());
		estModel.setBIC(estModelXml.getBic());
		estModel.setDof(estModelXml.getDof());
		estModel.setQualityScore(estModelXml.getQualityScore());
		estModel.setChecked(estModelXml.getChecked());
		estModel.setComment(estModelXml.getComment());
		estModel.setDbuuid(estModelXml.getDbuuid());

		return estModel;
	}
}
