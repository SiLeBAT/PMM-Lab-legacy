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
package de.bund.bfr.numl;

public enum DataType {
	STRING("string"), FLOAT("float"), DOUBLE("double"), INTEGER("integer");

	private String name;

	private DataType(String name) {
		this.name = name;
	}

	public Object parse(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}

		switch (this) {
		case STRING:
			return value;
		case FLOAT:
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException e) {
				return null;
			}
		case DOUBLE:
			try {
				return Double.parseDouble(value);
			} catch (NumberFormatException e) {
				return null;
			}
		case INTEGER:
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return null;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return name;
	}

	public static DataType fromName(String name) {
		for (DataType type : values()) {
			if (type.toString().equals(name)) {
				return type;
			}
		}

		return null;
	}
}
