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

import java.util.List;

import org.sbml.jsbml.SBMLDocument;

/**
 * Case 2a. Includes a {@link SBMLDocument} for the secondary model (master) and
 * n primary models with data.
 * 
 * @author Miguel Alba
 */
public class TwoStepSecondaryModel {

	private SBMLDocument secDoc; // secondary model (master) document
	private String secDocName;
	private List<PrimaryModelWData> primModels; // primary models with data

	public TwoStepSecondaryModel(String secDocName, SBMLDocument secDoc, List<PrimaryModelWData> primModels) {
		this.secDocName = secDocName;
		this.secDoc = secDoc;
		this.primModels = primModels;
	}

	public String getSecDocName() {
		return secDocName;
	}

	public SBMLDocument getSecDoc() {
		return secDoc;
	}

	public List<PrimaryModelWData> getPrimModels() {
		return primModels;
	}
}
