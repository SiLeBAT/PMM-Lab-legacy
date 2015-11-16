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
package de.bund.bfr.pmf;

/**
 * @author Miguel Alba
 */
public enum ModelClass {
	UNKNOWN(0, "unknown"),
	GROWTH(1, "growth"),
	INACTIVATION(2, "inactivation"),
	SURVIVAL(3, "survival"),
	GROWTH_INACTIVATION(4, "growth/inactivation"),
	INACTIVATION_SURVIVAL(5, "inactivation/survival"),
	GROWTH_SURVIVAL(6, "growth/survival"),
	GROWTH_INACTIVATION_SURVIVAL(7, "growth_inactivation_survival"),
	T(8, "T"),
	PH(9, "pH"),
	AW(10, "aw"),
	T_PH(11, "T/pH"),
	T_AW(12, "T/aw"),
	PH_AW(13, "pH/aw"),
	T_PH_AW(14, "T/pH/aw");
	
	private int value;
	private String fullName;
	
	private ModelClass(int value, String fullName) {
		this.value = value;
		this.fullName = fullName;
	}
	
	public static ModelClass fromName(String fullName) {
		for (ModelClass modelClass : values())
			if (modelClass.fullName.equals(fullName))
				return modelClass;
		return UNKNOWN;
	}
	
	public static ModelClass fromValue(int value) {
		for (ModelClass modelClass : values())
			if (modelClass.value == value)
				return modelClass;
		return UNKNOWN;
	}
	
	public String fullName() {
		return fullName;
	}
}
