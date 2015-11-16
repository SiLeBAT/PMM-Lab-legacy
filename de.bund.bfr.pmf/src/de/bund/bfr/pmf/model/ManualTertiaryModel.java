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
 * Case 3c. Keeps a master {@link SBMLDocument} per tertiary model linked to n
 * {@link SBMLDocument}s for the secondary models. It has no data.
 * 
 * @author Miguel Alba
 */
public class ManualTertiaryModel {

	private String tertDocName;
	private SBMLDocument tertDoc;
	private List<String> secDocNames;
	private List<SBMLDocument> secDocs;

	public ManualTertiaryModel(String tertDocName, SBMLDocument tertDoc, List<String> secDocNames,
			List<SBMLDocument> secDocs) {
		this.tertDocName = tertDocName;
		this.tertDoc = tertDoc;
		this.secDocNames = secDocNames;
		this.secDocs = secDocs;
	}

	public String getTertiaryDocName() {
		return tertDocName;
	}

	public SBMLDocument getTertiaryDoc() {
		return tertDoc;
	}

	public List<String> getSecDocNames() {
		return secDocNames;
	}

	public List<SBMLDocument> getSecDocs() {
		return secDocs;
	}
}
