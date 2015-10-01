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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Strings;

public class AtomicDescription extends DimensionDescription {

	protected static final String ELEMENT_NAME = "atomicDescription";

	private static final String NAME = "name";
	private static final String ONTOLOGY_TERM = "ontologyTerm";
	private static final String VALUE_TYPE = "valueType";

	private String name;
	private OntologyTerm ontologyTerm;
	private DataType valueType;

	public AtomicDescription(String name, OntologyTerm ontologyTerm, DataType valueType) {
		this.name = name;
		this.ontologyTerm = ontologyTerm;
		this.valueType = valueType;
	}

	protected AtomicDescription(Element node, List<OntologyTerm> ontologyTerms) {
		super(node);

		name = Strings.emptyToNull(node.getAttribute(NAME));
		ontologyTerm = findOntologyTerm(Strings.emptyToNull(node.getAttribute(ONTOLOGY_TERM)), ontologyTerms);
		valueType = DataType.fromName(Strings.emptyToNull(node.getAttribute(VALUE_TYPE)));
	}

	public String getName() {
		return name;
	}

	public OntologyTerm getOntologyTerm() {
		return ontologyTerm;
	}

	public DataType getValueType() {
		return valueType;
	}

	@Override
	public String toString() {
		return "AtomicDescription [name=" + name + ", ontologyTerm=" + ontologyTerm.getId() + ", valueType=" + valueType
				+ ", metaId=" + metaId + "]";
	}

	@Override
	protected Element toNode(Document doc) {
		Element node = doc.createElement(ELEMENT_NAME);

		Utils.setAttributeValue(node, NAME, name);
		Utils.setAttributeValue(node, ONTOLOGY_TERM, ontologyTerm.getId());
		Utils.setAttributeValue(node, VALUE_TYPE, valueType.toString());
		updateNode(node);

		return node;
	}
}
