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
public class TupleDescription {

  static final String ELEMENT_NAME = "tupleDescription";

  AtomicDescription concDesc;
  AtomicDescription timeDesc;

  public TupleDescription(final AtomicDescription concDesc, final AtomicDescription timeDesc) {
    this.concDesc = concDesc;
    this.timeDesc = timeDesc;
  }

  public TupleDescription(final Element node) {
    NodeList nodes = node.getElementsByTagName(AtomicDescription.ELEMENT_NAME);
    concDesc = new AtomicDescription((Element) nodes.item(0));
    timeDesc = new AtomicDescription((Element) nodes.item(1));
  }

  public AtomicDescription getConcentrationDescription() {
    return concDesc;
  }

  public AtomicDescription getTimeDescription() {
    return timeDesc;
  }

  @Override
  public String toString() {
    return String.format("TupleDescription [concentrationDescription=%s, timeDescription=%s]",
        concDesc, timeDesc);
  }

  @Override
  public boolean equals(final Object obj) {
    final TupleDescription other = (TupleDescription) obj;

    return concDesc.equals(other.concDesc) && timeDesc.equals(other.timeDesc);
  }

  public Element toNode(final Document doc) {
    final Element node = doc.createElement(ELEMENT_NAME);
    node.appendChild(concDesc.toNode(doc));
    node.appendChild(timeDesc.toNode(doc));

    return node;
  }
}
