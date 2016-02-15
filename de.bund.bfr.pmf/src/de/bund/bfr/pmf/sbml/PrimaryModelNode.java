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
package de.bund.bfr.pmf.sbml;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * @author Miguel Alba
 */
public class PrimaryModelNode {

  public static final String TAG = "primaryModel";
  public static final String NS = "pmmlab";

  XMLNode node;

  public PrimaryModelNode(final String model) {
    node = new XMLNode(new XMLTriple(TAG, "", NS));
    node.addChild(new XMLNode(model));
  }

  public PrimaryModelNode(final XMLNode node) {
    this.node = node;
  }

  public String getPrimaryModel() {
    return node.getChild(0).getCharacters();
  }

  public XMLNode getNode() {
    return node;
  }
}
