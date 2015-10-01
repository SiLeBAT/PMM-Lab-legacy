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

import java.util.List;

import org.w3c.dom.Element;

public abstract class DimensionDescription extends NMBase {

	public DimensionDescription() {
	}

	protected DimensionDescription(Element node) {
		super(node);
	}

	protected static DimensionDescription createDescription(Element node, List<OntologyTerm> ontologyTerms) {
		switch (node.getNodeName()) {
		case AtomicDescription.ELEMENT_NAME:
			return new AtomicDescription(node, ontologyTerms);
		case CompositeDescription.ELEMENT_NAME:
			return new CompositeDescription(node, ontologyTerms);
		case TupleDescription.ELEMENT_NAME:
			return new TupleDescription(node, ontologyTerms);
		}

		return null;
	}

	protected static OntologyTerm findOntologyTerm(String id, List<OntologyTerm> ontologyTerms) {
		if (id != null && ontologyTerms != null) {
			for (OntologyTerm term : ontologyTerms) {
				if (id.equals(term.getId())) {
					return term;
				}
			}
		}

		return null;
	}
}
