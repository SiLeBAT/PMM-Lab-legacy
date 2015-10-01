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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.Iterables;

public class NuMLDocument extends NMBase {

	public static final String NUML_NAMESPACE = "http://www.numl.org/numl/level1/version1";
	public static final int VERSION = 1;
	public static final int LEVEL = 1;

	protected static final String ELEMENT_NAME = "numl";

	private static final String ONTOLOGY_TERMS = "ontologyTerms";

	private List<OntologyTerm> ontologyTerms;
	private List<ResultComponent> resultComponents;

	public NuMLDocument(List<OntologyTerm> ontologyTerms, List<ResultComponent> resultComponents) {
		this.ontologyTerms = ontologyTerms;
		this.resultComponents = resultComponents;
	}

	protected NuMLDocument(Element node) {
		super(node);

		ontologyTerms = new ArrayList<>();
		resultComponents = new ArrayList<>();

		for (Element child : Utils.getChildren(node)) {
			switch (child.getNodeName()) {
			case ONTOLOGY_TERMS:
				for (Element childChild : Utils.getChildren(child)) {
					if (childChild.getNodeName().equals(OntologyTerm.ELEMENT_NAME)) {
						ontologyTerms.add(new OntologyTerm(childChild));
					}
				}
				break;
			case ResultComponent.ELEMENT_NAME:
				resultComponents.add(new ResultComponent(child, ontologyTerms));
				break;
			}
		}
	}

	public List<OntologyTerm> getOntologyTerms() {
		return new ArrayList<>(ontologyTerms);
	}

	public List<ResultComponent> getResultComponents() {
		return new ArrayList<>(resultComponents);
	}

	@Override
	public Iterable<? extends NMBase> getChildren() {
		return Iterables.concat(ontologyTerms, resultComponents);
	}

	@Override
	public String toString() {
		return "NuMLDocument [ontologyTerms=" + ontologyTerms + ", resultComponents=" + resultComponents + ", metaId="
				+ metaId + "]";
	}

	@Override
	protected Element toNode(Document doc) {
		Element node = doc.createElementNS(NUML_NAMESPACE, ELEMENT_NAME);

		node.setAttribute("version", String.valueOf(VERSION));
		node.setAttribute("level", String.valueOf(LEVEL));

		Element ontologyTermsNode = doc.createElement(ONTOLOGY_TERMS);

		for (OntologyTerm term : ontologyTerms) {
			ontologyTermsNode.appendChild(term.toNode(doc));
		}

		node.appendChild(ontologyTermsNode);

		for (ResultComponent comp : resultComponents) {
			node.appendChild(comp.toNode(doc));
		}

		updateNode(node);

		return node;
	}
}
