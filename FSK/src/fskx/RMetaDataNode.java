/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 ******************************************************************************/
package fskx;

import org.jdom2.Element;

/**
 * Keeps the names of the main script and the visualization script.
 *
 * @author Miguel de Alba
 */
public class RMetaDataNode {

	private static final String MAIN_SCRIPT_TAG = "mainScript";
	private static final String VIZ_SCRIPT_TAG = "visualizationScript";

	private String mainScript;
	private String vizScript;
	private Element node;

	public RMetaDataNode(final String mainScript, final String vizScript) {
		this.mainScript = mainScript;
		this.vizScript = vizScript;

		node = new Element("metaParent");

		// Main script annotation
		final Element mainScriptElement = new Element("mainScript");
		mainScriptElement.addContent(mainScript);
		node.addContent(mainScriptElement);

		// Visualization script annotation
		final Element vizScriptElement = new Element("visualizationScript");
		vizScriptElement.addContent(vizScript);
		node.addContent(vizScriptElement);
	}

	public RMetaDataNode(final Element node) {
		mainScript = node.getChildText(MAIN_SCRIPT_TAG);
		vizScript = node.getChildText(VIZ_SCRIPT_TAG);
		this.node = node;
	}

	public String getMainScript() {
		return mainScript;
	}

	public String getVisualizationScript() {
		return vizScript;
	}

	public Element getNode() {
		return node;
	}
}