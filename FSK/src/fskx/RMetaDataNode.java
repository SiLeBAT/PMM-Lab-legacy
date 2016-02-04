/*******************************************************************************
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
 ******************************************************************************/
package fskx;

import org.jdom2.Element;

/**
 * Keeps the names of the R scripts:
 * <ul>
 * <li>main script
 * <li>visualization script
 * <li>parameters script
 *
 * @author Miguel de Alba
 */
public class RMetaDataNode {

  private static final String MAIN_SCRIPT_TAG = "mainScript";
  private static final String PARAM_SCRIPT_TAG = "parametersScript";
  private static final String VIZ_SCRIPT_TAG = "visualizationScript";

  private String mainScript;
  private String paramsScript;
  private String vizScript;
  private Element node;

  public RMetaDataNode(final String mainScript, final String paramsScript, final String vizScript) {
    this.mainScript = mainScript;
    this.paramsScript = paramsScript;
    this.vizScript = vizScript;

    node = new Element("metaParent");

    // Main script annotation
    final Element mainScriptElement = new Element(MAIN_SCRIPT_TAG);
    mainScriptElement.addContent(mainScript);
    node.addContent(mainScriptElement);

    // Parameters script annotation
    final Element paramsScriptElement = new Element(PARAM_SCRIPT_TAG);
    paramsScriptElement.addContent(paramsScript);
    node.addContent(paramsScriptElement);

    // Visualization script annotation
    final Element vizScriptElement = new Element(VIZ_SCRIPT_TAG);
    vizScriptElement.addContent(vizScript);
    node.addContent(vizScriptElement);
  }

  public RMetaDataNode(final Element node) {
    mainScript = node.getChildText(MAIN_SCRIPT_TAG);
    paramsScript = node.getChildText(PARAM_SCRIPT_TAG);
    vizScript = node.getChildText(VIZ_SCRIPT_TAG);
    this.node = node;
  }

  public String getMainScript() {
    return mainScript;
  }

  public String getParametersScript() {
    return paramsScript;
  }

  public String getVisualizationScript() {
    return vizScript;
  }

  public Element getNode() {
    return node;
  }
}
