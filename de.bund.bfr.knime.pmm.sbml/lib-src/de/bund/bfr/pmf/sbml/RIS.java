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
package de.bund.bfr.pmf.sbml;

import de.bund.bfr.pmf.LiteratureSpecificationI;

/**
 * @author Miguel Alba
 */
public class RIS implements LiteratureSpecificationI {

	public String getAuthor() {
		return "AU";
	}

	public String getYear() {
		return "PY";
	}

	public String getTitle() {
		return "TI";
	}

	public String getAbstract() {
		return "AB";
	}

	public String getJournal() {
		return "T2";
	}

	public String getVolume() {
		return "VL";
	}

	public String getIssue() {
		return "IS";
	}

	public String getPage() {
		return "SP";
	}

	public String getApproval() {
		return "LB";
	}

	public String getWebsite() {
		return "UR";
	}

	public String getType() {
		return "M3";
	}

	public String getComment() {
		return "N1";
	}
}
