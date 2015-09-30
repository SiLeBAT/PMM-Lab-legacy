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
package de.bund.bfr.numl2;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

class Utils {

	private Utils() {
	}

	public static List<Element> getChildren(Element node) {
		List<Element> children = new ArrayList<>();

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i) instanceof Element) {
				children.add((Element) node.getChildNodes().item(i));
			}
		}

		return children;
	}

	public static void setAttributeValue(Element node, String name, String value) {
		if (value != null) {
			node.setAttribute(name, value);
		}
	}
}
