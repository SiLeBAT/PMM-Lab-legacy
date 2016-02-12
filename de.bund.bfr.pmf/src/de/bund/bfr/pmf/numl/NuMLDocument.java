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

/**
 * @author Miguel Alba
 */
public class NuMLDocument {

  private static final String NUML_NAMESPACE = "http://www.numl.org/numl/level1/version1";
  private static final int VERSION = 1;
  private static final int LEVEL = 1;

  static final String ELEMENT_NAME = "numl";

  private ConcentrationOntology concOntology;
  private TimeOntology timeOntology;
  private ResultComponent resultComponent;

  public NuMLDocument(final ConcentrationOntology concOntology, final TimeOntology timeOntology,
      final ResultComponent resultComponent) {
    this.concOntology = concOntology;
    this.timeOntology = timeOntology;
    this.resultComponent = resultComponent;
  }

  public NuMLDocument(final Element node) {
    // Gets concentration and time ontologies
    final NodeList ontologyNodes = node.getElementsByTagName(ConcentrationOntology.ELEMENT_NAME);
    concOntology = new ConcentrationOntology((Element) ontologyNodes.item(0));
    timeOntology = new TimeOntology((Element) ontologyNodes.item(1));

    // Gets the result component
    final NodeList resultComponentNodes = node.getElementsByTagName(ResultComponent.ELEMENT_NAME);
    final Element resultComponentElement = (Element) resultComponentNodes.item(0);
    resultComponent = new ResultComponent(resultComponentElement);
  }

  public ConcentrationOntology getConcentrationOntologyTerm() {
    return concOntology;
  }

  public TimeOntology getTimeOntologyTerm() {
    return timeOntology;
  }

  public ResultComponent getResultComponent() {
    return resultComponent;
  }

  public Element toNode(final Document doc) {
    final Element node = doc.createElementNS(NUML_NAMESPACE, ELEMENT_NAME);
    node.setAttribute("version", Integer.toString(VERSION));
    node.setAttribute("level", Integer.toString(LEVEL));
    node.setAttribute("xmlns:pmf",
        "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
    node.setAttribute("xmlns:sbml", "http://www.sbml.org/sbml/level3/version1/core");
    node.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
    node.setAttribute("xmlns:dcterms", "http://purl.org/dc/terms/");
    node.setAttribute("xmlns:pmmlab",
        "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");

    node.appendChild(concOntology.toNode(doc));
    node.appendChild(timeOntology.toNode(doc));
    node.appendChild(resultComponent.toNode(doc));

    return node;
  }
}
