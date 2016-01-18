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
public class UnitList {
	private static final String NUM_UNITS = "numUnits";
	private static final String UNITS = "units";

	private int numUnits;
	private Unit[] units;

	public Unit[] getUnits() {
		return units;
	}

	public void setUnits(final Unit[] units) {
		numUnits = units.length;
		this.units = units;
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt(NUM_UNITS, numUnits);
		for (int i = 0; i < numUnits; i++) {
			units[i].saveToNodeSettings(settings.addNodeSettings(UNITS + i));
		}
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			numUnits = settings.getInt(NUM_UNITS);
			units = new Unit[numUnits];
			for (int i = 0; i < numUnits; i++) {
				units[i] = new Unit();
				units[i].loadFromNodeSettings(settings.getNodeSettings(UNITS + i));
			}
		} catch (InvalidSettingsException e) {
		}
	}
}
