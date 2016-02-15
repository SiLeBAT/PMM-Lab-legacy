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

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.pmf.PMFUtil;

/** @author Miguel Alba */
public class SecIndep {

  Parameter param;
  String desc;

  public SecIndep(final Parameter param) {
    if (param.getAnnotation().isSetNonRDFannotation()) {
      desc = new SecIndepAnnotation(param.getAnnotation()).desc;
    }
  }

  public SecIndep(final String name, final String desc, final String unit) {
    param = new Parameter(name);
    param.setConstant(false);
    param.setValue(0.0);

    if (desc != null) {
      param.setAnnotation(new SecIndepAnnotation(desc).annotation);
    }

    param.setUnits((unit == null) ? "dimensionless" : PMFUtil.createId(unit));
  }

  public Parameter getParam() {
    return param;
  }

  public String getDescription() {
    return desc;
  }
}


class SecIndepAnnotation {

  private static final String METADATA_NS = "pmf";
  private static final String METADATA_TAG = "metadata";

  private static final String DESC_TAG = "description";
  private static final String DESC_NS = "pmf";

  Annotation annotation;
  String desc;

  public SecIndepAnnotation(final Annotation annotation) {
    this.annotation = annotation;

    final XMLNode metadata = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");
    final XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
    desc = descNode.getChild(0).getCharacters();
  }

  public SecIndepAnnotation(final String desc) {

    // Creates description node and adds it to the annotation node
    final XMLNode descNode = new XMLNode(new XMLTriple(DESC_TAG, null, DESC_NS));
    descNode.addChild(new XMLNode(desc));

    // Creates metadata node
    final XMLNode metadata = new XMLNode(new XMLTriple(METADATA_TAG, null, METADATA_NS));
    metadata.addChild(descNode);

    // Creates annotation
    annotation = new Annotation();
    annotation.setNonRDFAnnotation(metadata);

    // copies description
    this.desc = desc;
  }
}
