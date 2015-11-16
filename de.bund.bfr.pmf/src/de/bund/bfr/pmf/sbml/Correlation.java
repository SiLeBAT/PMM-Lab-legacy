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

import java.util.Locale;

/**
 * @author Miguel Alba
 */
public class Correlation {

	private String name;
	private Double value;

	public Correlation(String name) {
		this.name = name;
		this.value = null;
	}

	public Correlation(String name, double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Double getValue() {
		return value;
	}

	public boolean isSetValue() {
		return value != null;
	}

	public String toString() {
		return String.format(Locale.ENGLISH, "Correlation [name=%s, value=%.6f]", name, value);
	}

	public boolean equals(Object obj) {
		Correlation other = (Correlation) obj;
		return name.equals(other.name) && value == other.value;
	}
}
