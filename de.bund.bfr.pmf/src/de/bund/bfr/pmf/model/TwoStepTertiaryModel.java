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
 * Case 3a. Keeps one {@link SBMLDocument} per each tertiary model, linked to n
 * {@link SBMLDocument}s for the secondary models. The {@link SBMLDocument} of
 * the tertiary model also links to a number of data files.
 * 
 * @author Miguel Alba
 */
public class TwoStepTertiaryModel {

	private String tertDocName;
	private SBMLDocument tertDoc;
	private List<PrimaryModelWData> primModels;
	private List<String> secDocNames;
	private List<SBMLDocument> secDocs;

	public TwoStepTertiaryModel(String tertDocName, SBMLDocument tertDoc, List<PrimaryModelWData> primModels,
			List<String> secDocNames, List<SBMLDocument> secDocs) {
		this.tertDocName = tertDocName;
		this.tertDoc = tertDoc;
		this.primModels = primModels;
		this.secDocNames = secDocNames;
		this.secDocs = secDocs;
	}

	public String getTertDocName() {
		return tertDocName;
	}

	public SBMLDocument getTertDoc() {
		return tertDoc;
	}

	public List<PrimaryModelWData> getPrimModels() {
		return primModels;
	}

	public List<String> getSecDocNames() {
		return secDocNames;
	}

	public List<SBMLDocument> getSecDocs() {
		return secDocs;
	}
}
