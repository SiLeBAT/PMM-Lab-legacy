package de.bund.bfr.knime.pmm.annotation;

import java.util.Map;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import groovy.util.Node;
import groovy.util.NodeList;

public class AgentNuMLNode extends NuMLNodeBase {

	public static final String TAG = "species";
	public static final String NS = "sbml";

	public AgentNuMLNode(Agent agent) {

		Map<String, String> attrs = agent.getSpecies().writeXMLAttributes();
		attrs.put("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		attrs.put("xmlns:pmmlab", "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");

		String name = String.format("%s:%s", NS, TAG);

		node = new Node(null, name, attrs);

		if (agent.getCasNumber() != null) {
			node.appendNode("dc:source", agent.getCasNumber());
		}

		if (agent.getDetail() != null) {
			node.appendNode("pmmlab:detail", agent.getDetail());
		}

		if (agent.getDescription() != null) {
			node.appendNode("pmmlab:description", agent.getDescription());
		}
	}

	public AgentNuMLNode(final Node node) {
		this.node = node;
	}

	public AgentXml toAgentXml() {
		AgentXml agentXml = new AgentXml();
		agentXml.setName((String) node.attribute("name")); // Gets and sets name

		// Gets and sets agent detail
		NodeList detailNodes = (NodeList) node.get("detail");
		if (detailNodes.size() == 1) {
			Node detailNode = (Node) detailNodes.get(0);
			agentXml.setDetail(detailNode.text());
		}

		return agentXml;
	}
}
