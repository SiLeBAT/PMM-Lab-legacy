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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Strings;

public class AtomicValue extends DimensionValue {

	protected static final String ELEMENT_NAME = "atomicValue";

	private Object value;

	public AtomicValue(Object value) {
		this.value = value;
	}

	protected AtomicValue(Element node, AtomicDescription description) {
		super(node);

		value = description.getValueType().parse(Strings.emptyToNull(node.getTextContent()));
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "AtomicValue [value=" + value + ", metaId=" + metaId + "]";
	}

	@Override
	protected Element toNode(Document doc) {
		Element node = doc.createElement(ELEMENT_NAME);

		node.setTextContent(value.toString());
		updateNode(node);

		return node;
	}
}
