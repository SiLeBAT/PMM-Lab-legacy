/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
 ******************************************************************************/
package de.bund.bfr.knime.pmm.extendedtable;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;

public class ModelMetadata {

	private static final String ELEMENT_PMMDOC = "PmmDoc";

	private AgentXml agentXml;
	private MatrixXml matrixXml;
	private List<ModelLiteratureItem> modelLiteratureItems;
	private List<EstimatedModelLiteratureItem> estimatedModelLiteratureItems;
	private String warning;

	public ModelMetadata() {
		agentXml = null;
		matrixXml = null;
		modelLiteratureItems = new ArrayList<>();
		estimatedModelLiteratureItems = new ArrayList<>();
		warning = "";
	}

	public ModelMetadata(String xmlString) throws IOException, JDOMException {
		this();
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xmlString));

		Element rootElement = doc.getRootElement();
		parseElement(rootElement);
	}

	private void parseElement(Element rootElement) {

		Element agentElement = rootElement.getChild(AgentXml.ELEMENT_AGENT);
		if (agentElement != null) {
			agentXml = new AgentXml(agentElement);
		}

		Element matrixElement = rootElement.getChild(MatrixXml.ELEMENT_MATRIX);
		if (matrixElement != null) {
			matrixXml = new MatrixXml(matrixElement);
		}

		for (Element literatureElement : rootElement.getChildren(ModelLiteratureItem.ELEMENT_LITERATURE)) {
			modelLiteratureItems.add(new ModelLiteratureItem(literatureElement));
		}

		for (Element literatureElement : rootElement.getChildren(EstimatedModelLiteratureItem.ELEMENT_LITERATURE)) {
			estimatedModelLiteratureItems.add(new EstimatedModelLiteratureItem(literatureElement));
		}
	}

	public void addWarning(String warning) {
		this.warning += warning;
	}

	public String getWarning() {
		return warning;
	}

	public void setAgentXml(AgentXml agentXml) {
		this.agentXml = agentXml;
	}

	public void clearAgentXml() {
		this.agentXml = null;
	}

	public void setMatrixXml(MatrixXml matrixXml) {
		this.matrixXml = matrixXml;
	}

	public void clearMatrixXml() {
		this.matrixXml = null;
	}

	public void addLiteratureItem(ModelLiteratureItem literatureItem) {
		modelLiteratureItems.add(literatureItem);
	}

	public void removeLiteratureItem(ModelLiteratureItem literatureItem) {
		modelLiteratureItems.remove(literatureItem);
	}

	public void addLiteratureItem(EstimatedModelLiteratureItem literatureItem) {
		estimatedModelLiteratureItems.add(literatureItem);
	}

	public void removeLiteratureItem(EstimatedModelLiteratureItem literatureItem) {
		estimatedModelLiteratureItems.remove(literatureItem);
	}

	public org.w3c.dom.Document getW3C() {
		try {
			Document doc = toXmlDocument();
			return new DOMOutputter().output(doc);
		} catch (JDOMException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Document toXmlDocument() {
		Document doc = new Document();
		Element rootElement = new Element(ELEMENT_PMMDOC);
		doc.setRootElement(rootElement);

		if (agentXml != null) {
			rootElement.addContent(agentXml.toXmlElement());
		}
		if (matrixXml != null) {
			rootElement.addContent(matrixXml.toXmlElement());
		}
		for (ModelLiteratureItem literatureItem : modelLiteratureItems) {
			rootElement.addContent(literatureItem.toXmlElement());
		}
		for (EstimatedModelLiteratureItem literatureItem : estimatedModelLiteratureItems) {
			rootElement.addContent(literatureItem.toXmlElement());
		}

		return doc;
	}
}
