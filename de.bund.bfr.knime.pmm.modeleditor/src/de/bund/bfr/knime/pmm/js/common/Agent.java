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

import de.bund.bfr.knime.pmm.common.AgentXml;

/**
 * PmmLab agent. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>detail
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Agent implements ViewValue {

	// Configuration keys
	static final String ID = "id";
	static final String NAME = "name";
	static final String DETAIL = "detail";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private String detail;
	private String dbuuid;

	/** Returns the id of this {@link Agent}. If id is not set, returns null. */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the name of this {@link Agent}. If name is not set, returns null.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the detail of this {@link Agent}. If detail is not set, returns
	 * null.
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * Returns the dbuuid of this {@link Agent}. If dbuuid is not set, returns
	 * null.
	 */
	public String getDbuuid() {
		return dbuuid;
	}

	/** Sets the id value with 'id'. Ignores null values. */
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

	/** Sets the detail value with 'detail'. Ignores null and empty values. */
	public void setDetail(final String detail) {
		if (!Strings.isNullOrEmpty(detail)) {
			this.detail = detail;
		}
	}

	/** Sets the dbuuid value with 'dbuuid'. Ignores null and empty values. */
	public void setDbuuid(final String dbuuid) {
		if (!Strings.isNullOrEmpty(dbuuid)) {
			this.dbuuid = dbuuid;
		}
	}

	/** Saves agent properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		if (id != null) {
			settings.addInt(ID, id);
		}
		if (name != null) {
			settings.addString(NAME, name);
		}
		if (detail != null) {
			settings.addString(DETAIL, detail);
		}
		if (dbuuid != null) {
			settings.addString(DBUUID, dbuuid);
		}
	}

	/** Loads agent properties from a {@link NodeSettingsRO}. */
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
			detail = settings.getString(DETAIL);
		} catch (InvalidSettingsException e) {
			detail = null;
		}
		try {
			dbuuid = settings.getString(DBUUID);
		} catch (InvalidSettingsException e) {
			dbuuid = null;
		}
	}

	/** Creates an {@link AgentXml} from this {@link Agent}. */
	public AgentXml toAgentXml() {
		return new AgentXml(id, name, detail, dbuuid);
	}

	/** Creates an {@link Agent} from an {@link AgentXml}. */
	public static Agent toAgent(AgentXml agentXml) {
		Agent agent = new Agent();
		agent.setId(agentXml.getId());
		agent.setName(agentXml.getName());
		agent.setDetail(agentXml.getDetail());
		agent.setDbuuid(agentXml.getDbuuid());

		return agent;
	}
}
