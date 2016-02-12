/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.pmf.numl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.bund.bfr.pmf.sbml.PMFUnitDefinition;

public class TimeOntology {

  static final String ELEMENT_NAME = "ontologyTerm";
  private static final String ANNOTATION = "annotation";

  private static final String ID_TAG = "id";
  private static final String TERM_TAG = "term";
  private static final String SOURCE_TERM_ID_TAG = "sourceTermId";
  private static final String URI_TAG = "ontologyURI";

  public static final String ID = "time";
  public static final String TERM = "time";
  public static final String SOURCE_TERM_ID = "SBO:0000345";
  public static final String URI = "http://www.ebi.ac.uk/sbo";

  PMFUnitDefinition unitDefinition;

  public TimeOntology(final PMFUnitDefinition unitDefinition) {
    this.unitDefinition = unitDefinition;
  }

  public TimeOntology(final Element node) {
    final NodeList annotationNodes = node.getElementsByTagName(ANNOTATION);
    final Element annotationNode = (Element) annotationNodes.item(0);

    final NodeList unitNodes = annotationNode.getElementsByTagName(UnitDefinitionNuMLNode.TAG);
    final Element unitNode = (Element) unitNodes.item(0);

    unitDefinition = new UnitDefinitionNuMLNode(unitNode).toPMFUnitDefinition();
  }

  public PMFUnitDefinition getUnitDefinition() {
    return unitDefinition;
  }

  @Override
  public String toString() {
    return String.format("OntologyTerm [id=%s, term=%s, sourceTermId=%s, ontologyURI=%s]", ID, TERM,
        SOURCE_TERM_ID, URI);
  }

  @Override
  public boolean equals(final Object obj) {
    TimeOntology other = (TimeOntology) obj;
    return unitDefinition.equals(other.unitDefinition);
  }

  public Element toNode(final Document doc) {
    final Element node = doc.createElement(ELEMENT_NAME);
    node.setAttribute(ID_TAG, ID);
    node.setAttribute(TERM_TAG, TERM);
    node.setAttribute(SOURCE_TERM_ID_TAG, SOURCE_TERM_ID);
    node.setAttribute(URI_TAG, URI);

    final Element annotation = doc.createElement(ANNOTATION);
    node.appendChild(annotation);

    annotation.appendChild(new UnitDefinitionNuMLNode(unitDefinition, doc).node);

    return node;
  }
}
