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
import com.google.common.collect.ImmutableList;

public class CompositeValue extends DimensionValue {

	protected static final String ELEMENT_NAME = "compositeValue";

	private static final String INDEX_VALUE = "indexValue";

	private Object indexValue;
	private DimensionValue value;

	public CompositeValue(Object indexValue, DimensionValue value) {
		this.indexValue = indexValue;
		this.value = value;
	}

	protected CompositeValue(Element node, CompositeDescription description) {
		super(node);

		indexValue = description.getIndexType().parse(Strings.emptyToNull(node.getAttribute(INDEX_VALUE)));
		value = null;

		for (Element child : Utils.getChildren(node)) {
			DimensionValue value = createValue(child, description.getDescription());

			if (value != null) {
				this.value = value;
				break;
			}
		}
	}

	public Object getIndexValue() {
		return indexValue;
	}

	public DimensionValue getValue() {
		return value;
	}

	@Override
	public Iterable<? extends NMBase> getChildren() {
		return ImmutableList.of(value);
	}

	@Override
	public String toString() {
		return "CompositeValue [indexValue=" + indexValue + ", value=" + value + ", metaId=" + metaId + "]";
	}

	@Override
	protected Element toNode(Document doc) {
		Element node = doc.createElement(ELEMENT_NAME);

		Utils.setAttributeValue(node, INDEX_VALUE, indexValue.toString());
		node.appendChild(value.toNode(doc));
		updateNode(node);

		return node;
	}
}
