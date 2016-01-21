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
public class IndepList {
	private static final String NUM_INDEPS = "numIndeps";
	private static final String INDEPS = "indeps";

	private int numIndeps;
	private Indep[] indeps;

	public Indep[] getIndeps() {
		return indeps;
	}

	public void setIndeps(final Indep[] indeps) {
		numIndeps = indeps.length;
		this.indeps = indeps;
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt(NUM_INDEPS, numIndeps);
		for (int i = 0; i < numIndeps; i++) {
			indeps[i].saveToNodeSettings(settings.addNodeSettings(INDEPS + i));
		}
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
		numIndeps = settings.getInt(NUM_INDEPS);
		indeps = new Indep[numIndeps];
		for (int i = 0; i < numIndeps; i++) {
			indeps[i] = new Indep();
			indeps[i].loadFromNodeSettings(settings.getNodeSettings(INDEPS + i));
		}
		} catch (InvalidSettingsException e) { }
	}
}
