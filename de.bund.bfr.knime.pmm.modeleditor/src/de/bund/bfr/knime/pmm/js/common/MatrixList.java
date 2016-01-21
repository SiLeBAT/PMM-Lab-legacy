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
public class MatrixList {
	private static final String NUM_MATRICES = "numMatrices";
	private static final String MATRICES = "matrices";

	private int numMatrices;
	private Matrix[] matrices;

	public Matrix[] getMatrices() {
		return matrices;
	}

	public void setMatrices(Matrix[] matrices) {
		numMatrices = matrices.length;
		this.matrices = matrices;
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt(NUM_MATRICES, numMatrices);
		for (int i = 0; i < numMatrices; i++) {
			matrices[i].saveToNodeSettings(settings.addNodeSettings(MATRICES + i));
		}
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			numMatrices = settings.getInt(NUM_MATRICES);
			matrices = new Matrix[numMatrices];
			for (int i = 0; i < numMatrices; i++) {
				matrices[i] = new Matrix();
				matrices[i].loadFromNodeSettings(settings.getNodeSettings(MATRICES + i));
			}
		} catch (InvalidSettingsException e) {
		}
	}
}
