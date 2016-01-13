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
public class MiscList {
	private static final String NUM_MISCS = "numMiscs";
	private static final String MISCS = "miscs";

	private int numMiscs;
	private Misc[] miscs;

	public Misc[] getMiscs() {
		return miscs;
	}

	public void setMiscs(Misc[] miscs) {
		numMiscs = miscs.length;
		this.miscs = miscs;
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt(NUM_MISCS, numMiscs);
		for (int i = 0; i < numMiscs; i++) {
			miscs[i].saveToNodeSettings(settings.addNodeSettings(MISCS + i));
		}
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		numMiscs = settings.getInt(NUM_MISCS);
		miscs = new Misc[numMiscs];
		for (int i = 0; i < numMiscs; i++) {
			miscs[i].loadFromNodeSettings(settings.getNodeSettings(MISCS + i));
		}
	}
}
