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

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class AgentList {
	private static final String NUM_AGENTS = "numAgents";
	private static final String AGENTS = "agents";

	private int numAgents;
	private Agent[] agents;

	public Agent[] getAgents() {
		return agents;
	}

	public void setAgents(final Agent[] agents) {
		numAgents = agents.length;
		this.agents = agents;
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt(NUM_AGENTS, numAgents);
		for (int i = 0; i < numAgents; i++) {
			agents[i].saveToNodeSettings(settings.addNodeSettings(AGENTS + i));
		}
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			numAgents = settings.getInt(NUM_AGENTS);
			agents = new Agent[numAgents];
			for (int i = 0; i < numAgents; i++) {
				agents[i] = new Agent();
				agents[i].loadFromNodeSettings(settings.getNodeSettings(AGENTS + i));
			}
		} catch (InvalidSettingsException e) {
		}
	}
}
