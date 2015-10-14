package de.bund.bfr.knime.pmm.annotation.numl;

import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;

public class AgentNuMLNode extends NuMLNodeBase {

	public final static String TAG = "sbml:species";
	
	private final static String SOURCE_TAG = "dc:source";
	private final static String DETAIL_TAG = "pmmlab:detail";
	private final static String DESCRIPTION_TAG = "pmmlab:description";
	
	public AgentNuMLNode(Agent agent) throws ParserConfigurationException {
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		node = doc.createElement(TAG);
		
		for (Map.Entry<String, String> entry : agent.getSpecies().writeXMLAttributes().entrySet()) {
			node.setAttribute(entry.getKey(), entry.getValue());
		}

		
		if (agent.getCasNumber() != null) {
			Element casNumberNode = doc.createElement(SOURCE_TAG);
			casNumberNode.setTextContent(agent.getCasNumber());
			node.appendChild(casNumberNode);
		}
		
		if (agent.getDetail() != null) {
			Element detailNode = doc.createElement(DETAIL_TAG);
			detailNode.setTextContent(agent.getDetail());
			node.appendChild(detailNode);
		}
		
		if (agent.getDescription() != null) {
			Element descNode = doc.createElement(DESCRIPTION_TAG);
			descNode.setTextContent(agent.getDescription());
			node.appendChild(descNode);
		}
	}
	
	public AgentNuMLNode(final Element node) {
		this.node = node;
	}
	
	public AgentXml toAgentXml() {
		AgentXml agentXml = new AgentXml();
		agentXml.setName(node.getAttribute("name"));
		
		// Gets and sets agent detail
		NodeList detailNodes = node.getElementsByTagName(DETAIL_TAG);
		if (detailNodes.getLength() == 1) {
			Element detailNode = (Element) detailNodes.item(0);
			agentXml.setDetail(detailNode.getTextContent());
		}
		
		return agentXml;
	}
}
