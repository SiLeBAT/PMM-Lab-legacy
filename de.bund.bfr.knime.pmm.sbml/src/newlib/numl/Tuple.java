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
package newlib.numl;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Tuple extends DimensionValue {

	protected static final String ELEMENT_NAME = "tuple";

	private List<AtomicValue> atomicValues;

	public Tuple(List<AtomicValue> atomicValues) {
		this.atomicValues = atomicValues;
	}

	protected Tuple(Element node, TupleDescription description) {
		super(node);

		atomicValues = new ArrayList<>();

		int index = 0;

		for (Element child : Utils.getChildren(node)) {
			if (child.getNodeName().equals(AtomicValue.ELEMENT_NAME)) {
				atomicValues.add(new AtomicValue(child, description.getAtomicDescriptions().get(index++)));
			}
		}
	}

	public List<AtomicValue> getAtomicValues() {
		return new ArrayList<>(atomicValues);
	}

	@Override
	public String toString() {
		return "Tuple [atomicValues=" + atomicValues + ", metaId=" + metaId + "]";
	}

	@Override
	protected Element toNode(Document doc) {
		Element node = doc.createElement(ELEMENT_NAME);

		for (AtomicValue value : atomicValues) {
			node.appendChild(value.toNode(doc));
		}

		updateNode(node);

		return node;
	}
}
