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

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public class CompositeDescription extends DimensionDescription {

	protected static final String ELEMENT_NAME = "compositeDescription";

	private static final String NAME = "name";
	private static final String ONTOLOGY_TERM = "ontologyTerm";
	private static final String INDEX_TYPE = "indexType";

	private String name;
	private OntologyTerm ontologyTerm;
	private DataType indexType;
	private DimensionDescription description;

	public CompositeDescription(String name, OntologyTerm ontologyTerm, DataType indexType,
			DimensionDescription description) {
		this.name = name;
		this.ontologyTerm = ontologyTerm;
		this.indexType = indexType;
		this.description = description;
	}

	protected CompositeDescription(Element node, List<OntologyTerm> ontologyTerms) {
		super(node);

		name = Strings.emptyToNull(node.getAttribute(NAME));
		ontologyTerm = findOntologyTerm(Strings.emptyToNull(node.getAttribute(ONTOLOGY_TERM)), ontologyTerms);
		indexType = DataType.fromName(Strings.emptyToNull(node.getAttribute(INDEX_TYPE)));
		description = null;

		for (Element child : Utils.getChildren(node)) {
			DimensionDescription description = createDescription(child, ontologyTerms);

			if (description != null) {
				this.description = description;
				break;
			}
		}
	}

	public String getName() {
		return name;
	}

	public OntologyTerm getOntologyTerm() {
		return ontologyTerm;
	}

	public DataType getIndexType() {
		return indexType;
	}

	public DimensionDescription getDescription() {
		return description;
	}

	@Override
	public Iterable<? extends NMBase> getChildren() {
		return ImmutableList.of(description);
	}

	@Override
	public String toString() {
		return "CompositeDescription [name=" + name + ", ontologyTerm=" + ontologyTerm.getId() + ", indexType="
				+ indexType + ", description=" + description + ", metaId=" + metaId + "]";
	}

	@Override
	protected Element toNode(Document doc) {
		Element node = doc.createElement(ELEMENT_NAME);

		Utils.setAttributeValue(node, NAME, name);
		Utils.setAttributeValue(node, ONTOLOGY_TERM, ontologyTerm.getId());
		Utils.setAttributeValue(node, INDEX_TYPE, indexType.toString());
		node.appendChild(description.toNode(doc));
		updateNode(node);

		return node;
	}
}
