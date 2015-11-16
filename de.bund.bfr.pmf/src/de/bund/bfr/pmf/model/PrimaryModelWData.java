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
package de.bund.bfr.pmf.model;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.pmf.numl.NuMLDocument;

/**
 * Case 1a. Each primary model includes a {@link NuMLDocument}.
 * 
 * @author Miguel Alba
 */
public class PrimaryModelWData {

	private SBMLDocument modelDoc;
	private NuMLDocument dataDoc;

	private String modelDocName;
	private String dataDocName;

	public PrimaryModelWData(String modelDocName, SBMLDocument modelDoc, String dataDocName, NuMLDocument dataDoc) {
		this.modelDocName = modelDocName;
		this.modelDoc = modelDoc;
		this.dataDocName = dataDocName;
		this.dataDoc = dataDoc;
	}

	public SBMLDocument getModelDoc() {
		return modelDoc;
	}

	public NuMLDocument getDataDoc() {
		return dataDoc;
	}
	
	public String getModelDocName() {
		return modelDocName;
	}
	
	public String getDataDocName() {
		return dataDocName;
	}
}
