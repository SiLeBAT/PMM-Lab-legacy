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
package de.bund.bfr.pmf.numl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Miguel Alba
 */
public class AtomicDescription {

	static final String ELEMENT_NAME = "atomicDescription";
	
	private static final String NAME = "name";
	private static final String ONTOLOGY_TERM = "ontologyTerm";
	private static final String VALUE_TYPE = "valueType";
	
	private String name;
	private String ontologyTermId;
	public static final DataType valueType = DataType.DOUBLE;

	public AtomicDescription(String name, String ontologyTermId) {
		this.name = name;
		this.ontologyTermId = ontologyTermId;
		
	}
	
	public AtomicDescription(Element node) {
		name = node.getAttribute(NAME);
		ontologyTermId = node.getAttribute(ONTOLOGY_TERM);
	}
	
	public String getName() {
		return name;
	}
	
	public String getOntologyTermId() {
		return ontologyTermId;
	}
	
	@Override
	public String toString() {
		String string = String.format("AtomicDescription [name=%s, ontologyTerm=%s, valueType=%s]", name,
				ontologyTermId, valueType);
		return string;
	}

	@Override
	public boolean equals(Object obj) {
		AtomicDescription other = (AtomicDescription) obj;
		return name.equals(other.name) && ontologyTermId.equals(other.ontologyTermId);
	}
	
	public Element toNode(Document doc) {
		Element node = doc.createElement(ELEMENT_NAME);
		node.setAttribute(NAME, name);
		node.setAttribute(ONTOLOGY_TERM, ontologyTermId);
		node.setAttribute(VALUE_TYPE, valueType.name());
		return node;
	}
}
