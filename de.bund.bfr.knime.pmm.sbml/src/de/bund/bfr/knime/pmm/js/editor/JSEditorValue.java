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
package de.bund.bfr.knime.pmm.js.editor;

import javax.json.JsonException;
import javax.json.JsonValue;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.dialog.DialogNodeValue;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 
 * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland, University of
 *         Konstanz
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class JSEditorValue extends JSONViewContent implements DialogNodeValue {

	private static final String CFG_MODELS = "model";
	private static final String DEFAULT_MODELS = "";
	private String m_models = DEFAULT_MODELS;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		settings.addString(CFG_MODELS, m_models);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadFromNodeSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_models = settings.getString(CFG_MODELS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonIgnore
	public void loadFromNodeSettingsInDialog(final NodeSettingsRO settings) {
		m_models = settings.getString(CFG_MODELS, DEFAULT_MODELS);
	}

	public void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the models
	 */
	@JsonProperty("String")
	public String getModels() {
		return m_models;
	}

	/**
	 * @param models
	 *            the models to set
	 */
	@JsonProperty("String")
	public void setModels(final String models) {
		m_models = models;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("models=");
		sb.append(m_models);
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(m_models).toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		JSEditorValue other = (JSEditorValue) obj;
		return new EqualsBuilder().append(m_models, other.m_models).isEquals();
	}

	@Override
	public void loadFromString(String fromCmdLine)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFromJson(JsonValue json) throws JsonException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonValue toJson() {
		// TODO Auto-generated method stub
		return null;
	}
}
