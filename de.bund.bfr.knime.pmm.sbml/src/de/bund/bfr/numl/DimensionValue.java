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

import org.w3c.dom.Element;

public abstract class DimensionValue extends NMBase {

	public DimensionValue() {
	}

	protected DimensionValue(Element node) {
		super(node);
	}

	protected static DimensionValue createValue(Element node, DimensionDescription description) {
		switch (node.getNodeName()) {
		case AtomicValue.ELEMENT_NAME:
			return new AtomicValue(node, (AtomicDescription) description);
		case CompositeValue.ELEMENT_NAME:
			return new CompositeValue(node, (CompositeDescription) description);
		case Tuple.ELEMENT_NAME:
			return new Tuple(node, (TupleDescription) description);
		}

		return null;
	}
}
