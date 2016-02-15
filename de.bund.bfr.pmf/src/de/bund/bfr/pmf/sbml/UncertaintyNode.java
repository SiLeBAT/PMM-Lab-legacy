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

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Uncertainty xml node. Uses the pmml:modelquality tag. E.g.
 * <pmml:modelquality r-squared="0.996" rootMeanSquaredError="0.345" dataName=
 * "Missing data name" AIC="-32.977" BIC="-34.994"/>
 * 
 * @author Miguel Alba
 */
public class UncertaintyNode {

  public final static String TAG = "modelquality";
  public final static String NS = "pmmlab";

  public final static String ID = "id";
  public final static String NAME = "name";
  public final static String COMMENT = "dataUsage";
  public final static String R2 = "r-squared";
  public final static String RMS = "rootMeanSquaredError";
  public final static String SSE = "sumSquaredError";
  public final static String AIC = "AIC";
  public final static String BIC = "BIC";
  public final static String DOF = "degreesOfFreedom";

  private XMLNode node;

  public UncertaintyNode(final XMLNode node) {
    this.node = node;
  }

  /**
   * Builds an UncertaintyNode from uncertainty measures.
   */
  public UncertaintyNode(final Uncertainties uncertainties) {
    final XMLAttributes attrs = new XMLAttributes();
    if (uncertainties.isSetID())
      attrs.add(ID, uncertainties.getID().toString());
    if (uncertainties.isSetModelName())
      attrs.add(NAME, uncertainties.getModelName());
    if (uncertainties.isSetComment())
      attrs.add(COMMENT, uncertainties.getComment());
    if (uncertainties.isSetR2())
      attrs.add(R2, uncertainties.getR2().toString());
    if (uncertainties.isSetRMS())
      attrs.add(RMS, uncertainties.getRMS().toString());
    if (uncertainties.isSetSSE())
      attrs.add(SSE, uncertainties.getSSE().toString());
    if (uncertainties.isSetAIC())
      attrs.add(AIC, uncertainties.getAIC().toString());
    if (uncertainties.isSetBIC())
      attrs.add(BIC, uncertainties.getBIC().toString());
    if (uncertainties.isSetDOF())
      attrs.add(DOF, uncertainties.getDOF().toString());

    node = new XMLNode(new XMLTriple(TAG, null, NS), attrs);
  }

  /**
   * Gets uncertainty measures.
   */
  public Uncertainties getMeasures() {

    final XMLAttributes attrs = node.getAttributes();

    final Integer id = (attrs.hasAttribute(ID)) ? Integer.parseInt(attrs.getValue(ID)) : null;
    final String modelName = attrs.getValue(NAME);
    final String comment = attrs.getValue(COMMENT);
    final Double r2 = (attrs.hasAttribute(R2)) ? Double.parseDouble(attrs.getValue(R2)) : null;
    final Double rms = (attrs.hasAttribute(RMS)) ? Double.parseDouble(attrs.getValue(RMS)) : null;
    final Double sse = (attrs.hasAttribute(SSE)) ? Double.parseDouble(attrs.getValue(SSE)) : null;
    final Double aic = (attrs.hasAttribute(AIC)) ? Double.parseDouble(attrs.getValue(AIC)) : null;
    final Double bic = (attrs.hasAttribute(BIC)) ? Double.parseDouble(attrs.getValue(BIC)) : null;
    final Integer dof = (attrs.hasAttribute(DOF)) ? Integer.parseInt(attrs.getValue(DOF)) : null;

    return new UncertaintiesImpl(id, modelName, comment, r2, rms, sse, aic, bic, dof);
  }

  public XMLNode getNode() {
    return node;
  }
}
