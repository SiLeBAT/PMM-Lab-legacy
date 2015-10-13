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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TupleDescription extends DimensionDescription {

	protected static final String ELEMENT_NAME = "tupleDescription";

	private List<AtomicDescription> atomicDescriptions;

	public TupleDescription(List<AtomicDescription> atomicDescriptions) {
		this.atomicDescriptions = atomicDescriptions;
	}

	protected TupleDescription(Element node, List<OntologyTerm> ontologyTerms) {
		super(node);

		atomicDescriptions = new ArrayList<>();

		for (Element child : Utils.getChildren(node)) {
			if (child.getNodeName().equals(AtomicDescription.ELEMENT_NAME)) {
				atomicDescriptions.add(new AtomicDescription(child, ontologyTerms));
			}
		}
	}

	public List<AtomicDescription> getAtomicDescriptions() {
		return new ArrayList<>(atomicDescriptions);
	}

	@Override
	public Iterable<? extends NMBase> getChildren() {
		return atomicDescriptions;
	}

	@Override
	public String toString() {
		return "TupleDescription [atomicDescriptions=" + atomicDescriptions + ", metaId=" + metaId + "]";
	}

	@Override
	protected Element toNode(Document doc) {
		Element node = doc.createElement(ELEMENT_NAME);

		for (AtomicDescription desc : atomicDescriptions) {
			node.appendChild(desc.toNode(doc));
		}
		
		addAnnotationAndNotes(doc, node);

		return node;
	}
}
