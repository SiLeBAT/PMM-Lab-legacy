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

public class OntologyTerm extends NMBase {

	protected static final String ELEMENT_NAME = "ontologyTerm";

	private static final String ID = "id";
	private static final String TERM = "term";
	private static final String SOURCE_TERM_ID = "sourceTermId";
	private static final String ONTOLOGY_URI = "ontologyURI";

	private String id;
	private String term;
	private String sourceTermId;
	private String ontologyURI;

	public OntologyTerm(String id, String term, String sourceTermId, String ontologyURI) {
		this.id = id;
		this.term = term;
		this.sourceTermId = sourceTermId;
		this.ontologyURI = ontologyURI;
	}

	protected OntologyTerm(Element node) {
		super(node);

		id = Strings.emptyToNull(node.getAttribute(ID));
		term = Strings.emptyToNull(node.getAttribute(TERM));
		sourceTermId = Strings.emptyToNull(node.getAttribute(SOURCE_TERM_ID));
		ontologyURI = Strings.emptyToNull(node.getAttribute(ONTOLOGY_URI));
	}

	public String getId() {
		return id;
	}

	public String getTerm() {
		return term;
	}

	public String getSourceTermId() {
		return sourceTermId;
	}

	public String getOntologyURI() {
		return ontologyURI;
	}

	@Override
	public Iterable<? extends NMBase> getChildren() {
		return ImmutableList.of();
	}

	@Override
	public String toString() {
		return "OntologyTerm [id=" + id + ", term=" + term + ", sourceTermId=" + sourceTermId + ", ontologyURI="
				+ ontologyURI + ", metaId=" + metaId + "]";
	}

	@Override
	protected Element toNode(Document doc) {
		Element node = doc.createElement(ELEMENT_NAME);

		Utils.setAttributeValue(node, ID, id);
		Utils.setAttributeValue(node, TERM, term);
		Utils.setAttributeValue(node, SOURCE_TERM_ID, sourceTermId);
		Utils.setAttributeValue(node, ONTOLOGY_URI, ontologyURI);
		updateNode(node);

		return node;
	}
}
