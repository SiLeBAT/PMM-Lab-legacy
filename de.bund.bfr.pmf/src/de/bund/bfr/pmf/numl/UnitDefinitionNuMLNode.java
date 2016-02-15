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

import org.sbml.jsbml.Unit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.bund.bfr.pmf.sbml.PMFUnit;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;

public class UnitDefinitionNuMLNode {

  public static final String TAG = "sbml:unitDefinition";
  private static final String TRANSFORMATION_TAG = "pmf:transformation";
  private static final String ID_ATTR = "id";
  private static final String NAME_ATTR = "name";

  Element node;

  public UnitDefinitionNuMLNode(final PMFUnitDefinition unitDefinition, final Document doc) {

    node = doc.createElement(TAG);
    node.setAttribute(ID_ATTR, unitDefinition.getId());
    node.setAttribute(NAME_ATTR, unitDefinition.getName());

    // Adds transformation name
    if (unitDefinition.isSetTransformationName()) {
      final Element transformationNode = doc.createElement(TRANSFORMATION_TAG);
      transformationNode.setTextContent(unitDefinition.getTransformationName());
      node.appendChild(transformationNode);
    }

    // Add units
    for (final PMFUnit unit : unitDefinition.getUnits()) {
      node.appendChild(new UnitNuMLNode(unit, doc).node);
    }
  }

  public UnitDefinitionNuMLNode(final Element node) {
    this.node = node;
  }

  public PMFUnitDefinition toPMFUnitDefinition() {

    final String id = node.getAttribute(ID_ATTR);
    final String name = node.getAttribute(NAME_ATTR);

    String transformationName = null;
    final NodeList transformationNameNodes = node.getElementsByTagName(TRANSFORMATION_TAG);
    if (transformationNameNodes.getLength() == 1) {
      final Element transformationNameNode = (Element) transformationNameNodes.item(0);
      transformationName = transformationNameNode.getTextContent();
    }

    PMFUnit[] units = null;
    final NodeList unitNodes = node.getElementsByTagName(UnitNuMLNode.UNIT_TAG);

    if (unitNodes.getLength() > 0) {
      units = new PMFUnit[unitNodes.getLength()];
      for (int i = 0; i < unitNodes.getLength(); i++) {
        final Element unitNode = (Element) unitNodes.item(i);
        units[i] = new UnitNuMLNode(unitNode).toUnit();
      }
    }

    return new PMFUnitDefinition(id, name, transformationName, units);
  }
}


class UnitNuMLNode {

  static final String UNIT_TAG = "sbml:unit";
  private static final String EXPONENT_ATTR = "exponent";
  private static final String KIND_ATTR = "kind";
  private static final String MULTIPLIER_ATTR = "multiplier";
  private static final String SCALE_ATTR = "scale";

  Element node;

  public UnitNuMLNode(final PMFUnit unit, final Document doc) {
    node = doc.createElement(UNIT_TAG);
    node.setAttribute(EXPONENT_ATTR, Double.toString(unit.getExponent()));
    node.setAttribute(KIND_ATTR, unit.getKind().getName());
    node.setAttribute(MULTIPLIER_ATTR, Double.toString(unit.getMultiplier()));
    node.setAttribute(SCALE_ATTR, Integer.toString(unit.getScale()));
  }

  public UnitNuMLNode(final Element node) {
    this.node = node;
  }

  public PMFUnit toUnit() {
    final double exponent = Double.parseDouble(node.getAttribute(EXPONENT_ATTR));
    final Unit.Kind kind = Unit.Kind.valueOf(node.getAttribute(KIND_ATTR).toUpperCase());
    final double multiplier = Double.parseDouble(node.getAttribute(MULTIPLIER_ATTR));
    final int scale = Integer.parseInt(node.getAttribute(SCALE_ATTR));

    return new PMFUnit(multiplier, scale, kind, exponent);
  }
}
