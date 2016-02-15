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

import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Coefficient that extends the SBML {@link Parameter} with more data: P, error, correlations and a
 * description string.
 * 
 * @author Miguel Alba
 */
public class PMFCoefficientImpl implements PMFCoefficient {

  private static final int LEVEL = 3;
  private static final int VERSION = 1;

  private Parameter param;
  private Double P;
  private Double error;
  private Double t;
  private Correlation[] correlations;
  private String desc;

  private static final String P_TAG = "P";
  private static final String P_NS = "pmmlab";
  private static final String ERROR_TAG = "error";
  private static final String ERROR_NS = "pmmlab";
  private static final String T_TAG = "t";
  private static final String T_NS = "pmmlab";
  private static final String CORRELATION_TAG = "correlation";
  private static final String CORRELATION_NS = "pmmlab";
  private static final String ATTRIBUTE_NAME = "origname";
  private static final String ATTRIBUTE_VALUE = "value";
  private static final String DESC_TAG = "description";
  private static final String DESC_NS = "pmmlab";
  private static final String METADATA_NS = "pmf";
  private static final String METADATA_TAG = "metadata";

  /** Creates a PMFCoefficientImpl from a {@link Parameter}. */
  public PMFCoefficientImpl(final Parameter parameter) {
    param = parameter;
    if (param.getAnnotation().isSetNonRDFannotation()) {
      // Parses annotation
      final XMLNode metadataNode =
          param.getAnnotation().getNonRDFannotation().getChildElement(METADATA_TAG, "");

      // Gets P
      final XMLNode pNode = metadataNode.getChildElement(P_TAG, "");
      if (pNode != null) {
        P = Double.parseDouble(pNode.getChild(0).getCharacters());
      }

      // Gets error
      final XMLNode errorNode = metadataNode.getChildElement(ERROR_TAG, "");
      if (errorNode != null) {
        error = Double.parseDouble(errorNode.getChild(0).getCharacters());
      }

      // Gets t
      final XMLNode tNode = metadataNode.getChildElement(T_TAG, "");
      if (tNode != null) {
        t = Double.parseDouble(tNode.getChild(0).getCharacters());
      }

      // Gets correlations
      final List<XMLNode> corrNodes = metadataNode.getChildElements(CORRELATION_TAG, "");
      if (!corrNodes.isEmpty()) {
        final int numCorrNodes = corrNodes.size();
        correlations = new Correlation[numCorrNodes];

        for (int i = 0; i < numCorrNodes; i++) {
          final XMLNode corrNode = corrNodes.get(i);
          final XMLAttributes attrs = corrNode.getAttributes();
          final String corrName = attrs.getValue(ATTRIBUTE_NAME);
          if (attrs.hasAttribute(ATTRIBUTE_VALUE)) {
            final String valueAsString = attrs.getValue(ATTRIBUTE_VALUE);
            final Double corrValue = Double.parseDouble(valueAsString);
            correlations[i] = new Correlation(corrName, corrValue);
          } else {
            correlations[i] = new Correlation(corrName);
          }
        }
      }

      // Gets description
      final XMLNode descNode = metadataNode.getChildElement(DESC_TAG, "");
      if (descNode != null) {
        desc = descNode.getChild(0).getCharacters();
      }
    }
  }

  /**
   * Creates a PMFCoefficientImpl fron an id, value, unit, P, error, t, correlations and
   * description.
   */
  public PMFCoefficientImpl(final String id, final double value, final String unit, final Double P,
      final Double error, final Double t, final Correlation[] correlations, final String desc) {
    param = new Parameter(id, LEVEL, VERSION);
    param.setValue(value);
    param.setUnits(unit);
    param.setConstant(true);

    if (P != null || error != null || t != null || correlations != null || desc != null) {

      // Builds metadata node
      final XMLNode metadataNode = new XMLNode(new XMLTriple(METADATA_TAG, null, METADATA_NS));

      // Creates P annotation
      if (P != null) {
        final XMLNode pNode = new XMLNode(new XMLTriple(P_TAG, null, P_NS));
        pNode.addChild(new XMLNode(P.toString()));
        metadataNode.addChild(pNode);
      }

      // Creates error annotation
      if (error != null) {
        final XMLNode errorNode = new XMLNode(new XMLTriple(ERROR_TAG, null, ERROR_NS));
        errorNode.addChild(new XMLNode(error.toString()));
        metadataNode.addChild(errorNode);
      }

      // Creates t annotation
      if (t != null) {
        final XMLNode tNode = new XMLNode(new XMLTriple(T_TAG, null, T_NS));
        tNode.addChild(new XMLNode(t.toString()));
        metadataNode.addChild(tNode);
      }

      // Creates correlation annotation
      if (correlations != null) {
        for (final Correlation correlation : correlations) {
          final XMLAttributes attrs = new XMLAttributes();
          attrs.add(ATTRIBUTE_NAME, correlation.getName());
          if (correlation.isSetValue()) {
            attrs.add(ATTRIBUTE_VALUE, Double.toString(correlation.getValue()));
          }

          final XMLTriple triple = new XMLTriple(CORRELATION_TAG, null, CORRELATION_NS);
          metadataNode.addChild(new XMLNode(triple, attrs));
        }
      }

      // Creates annotation for description
      if (desc != null) {
        final XMLNode descNode = new XMLNode(new XMLTriple(DESC_TAG, null, DESC_NS));
        descNode.addChild(new XMLNode(desc));
        metadataNode.addChild(descNode);
      }

      param.getAnnotation().setNonRDFAnnotation(metadataNode);
    }
    this.P = P;
    this.error = error;
    this.t = t;
    this.correlations = correlations;
    this.desc = desc;
  }

  /** Creates a PMFCoefficientImpl instance from an id, value and unit. */
  public PMFCoefficientImpl(final String id, final double value, final String unit) {
    this(id, value, unit, null, null, null, null, null);
  }

  /** {@inheritDoc} */
  public Parameter getParameter() {
    return param;
  }

  /** {@inheritDoc} */
  public String getId() {
    return param.getId();
  }

  /** {@inheritDoc} */
  public double getValue() {
    return param.getValue();
  }

  /** {@inheritDoc} */
  public String getUnit() {
    return param.getUnits();
  }

  /** {@inheritDoc} */
  public Double getP() {
    return P;
  }

  /** {@inheritDoc} */
  public Double getError() {
    return error;
  }

  /** {@inheritDoc} */
  public Double getT() {
    return t;
  }

  /** {@inheritDoc} */
  public Correlation[] getCorrelations() {
    return correlations;
  }

  /** {@inheritDoc} */
  public String getDescription() {
    return desc;
  }

  /** {@inheritDoc} */
  public void setId(final String id) {
    param.setId(id);
  }

  /** {@inheritDoc} */
  public void setValue(final double value) {
    param.setValue(value);
  }

  /** {@inheritDoc} */
  public void setUnit(final String unit) {
    param.setUnits(unit);
  }

  /** {@inheritDoc} */
  public void setP(final double p) {
    this.P = p;
  }

  /** {@inheritDoc} */
  public void setT(final double t) {
    this.t = t;
  }

  /** {@inheritDoc} */
  public void setError(final double error) {
    this.error = error;
  }

  /** {@inheritDoc} */
  public void setCorrelations(final Correlation[] correlations) {
    this.correlations = correlations;
  }

  /** {@inheritDoc} */
  public void setDescription(final String description) {
    if (description != null && !description.isEmpty()) {
      this.desc = description;
    }
  }

  /** {@inheritDoc} */
  public boolean isSetP() {
    return P != null;
  }

  /** {@inheritDoc} */
  public boolean isSetError() {
    return error != null;
  }

  /** {@inheritDoc} */
  public boolean isSetT() {
    return t != null;
  }

  /** {@inheritDoc} */
  public boolean isSetCorrelations() {
    return correlations != null;
  }

  /** {@inheritDoc} */
  public boolean isSetDescription() {
    return desc != null;
  }
}
