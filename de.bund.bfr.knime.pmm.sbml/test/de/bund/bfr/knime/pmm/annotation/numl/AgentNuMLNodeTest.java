package de.bund.bfr.knime.pmm.annotation.numl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;

public class AgentNuMLNodeTest {

	@SuppressWarnings("static-method")
	@Test
	public void test() {
		
		String agentName = "salmonella spp";
		String agentDetail = "Salmonella spec";
		
		AgentXml agentXml = new AgentXml();
		agentXml.setName(agentName);
		agentXml.setDetail(agentDetail);
		Agent agent = new Agent(agentXml, null, null, null);
		
		AgentNuMLNode node1 = null;
		try {
			node1 = new AgentNuMLNode(agent);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			fail();
		}
		
		AgentXml agentXml2 = node1.toAgentXml();
		assertEquals(agentName, agentXml2.getName());
		assertEquals(agentDetail, agentXml2.getDetail());
	}

}
