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

/**
 * @author Miguel Alba
 */
public enum ReferenceType {
	Paper(1), SOP(2), LA(3), Handbuch(4), Laborbuch(5), Buch(6), Webseite(7), Bericht(8);

	private int value;

	private ReferenceType(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
	
	/**
	 * @param value Value of the reference type.
	 * @return The ReferenceType associated to the value. If value is invalid it will return null.
	 */
	public static ReferenceType fromValue(int value) {
		for (ReferenceType referenceType : values()) {
			if (referenceType.value == value) {
				return referenceType;
			}
		}
		return null;
	}
};
