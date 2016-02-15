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

import java.util.List;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Secondary model annotation. Holds global model ID, references and uncertainty measures.
 * 
 * @author Miguel de Alba
 */
public class Model2Annotation {

  private static final String METADATA_NS = "pmf";
  private static final String METADATA_TAG = "metadata";

  Reference[] references;
  int globalModelID;
  Uncertainties uncertainties;
  Annotation annotation;

  /**
   * Gets global model id, uncertainties and literature items of the model.
   */
  public Model2Annotation(final Annotation annotation) {
    this.annotation = annotation;

    final XMLNode metadata = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");

    // Gets globalModelID
    globalModelID = new GlobalModelIdNode(metadata.getChildElement(GlobalModelIdNode.TAG, ""))
        .getGlobalModelId();

    // Gets model quality annotation
    final XMLNode qualityNode = metadata.getChildElement(UncertaintyNode.TAG, "");
    if (qualityNode != null) {
      uncertainties = new UncertaintyNode(qualityNode).getMeasures();
    }

    // Gets references
    final List<XMLNode> refNodes = metadata.getChildElements(ReferenceSBMLNode.TAG, "");
    references = new ReferenceImpl[refNodes.size()];
    for (int i = 0; i < refNodes.size(); i++) {
      references[i] = new ReferenceSBMLNode(refNodes.get(i)).toReference();
    }
  }

  /** Builds new coefficient annotation for global model id, uncertainties and references. */
  public Model2Annotation(final int globalModelID, final Uncertainties uncertainties,
      final Reference[] references) {
    // Builds metadata node
    final XMLNode metadataNode = new XMLNode(new XMLTriple(METADATA_TAG, "", METADATA_NS));

    // Builds globalModelID node
    metadataNode.addChild(new GlobalModelIdNode(globalModelID).node);

    // Builds uncertainties node
    metadataNode.addChild(new UncertaintyNode(uncertainties).getNode());

    // Builds references node
    for (final Reference reference : references) {
      metadataNode.addChild(new ReferenceSBMLNode(reference).node);
    }

    // Saves fields
    this.globalModelID = globalModelID;
    this.references = references;
    this.uncertainties = uncertainties;
    this.annotation = new Annotation();
    this.annotation.setNonRDFAnnotation(metadataNode);
  }

  // Getters
  public int getGlobalModelID() {
    return globalModelID;
  }

  public Reference[] getReferences() {
    return references;
  }

  public Uncertainties getUncertainties() {
    return uncertainties;
  }

  public Annotation getAnnotation() {
    return annotation;
  }
}
