package de.bund.bfr.knime.pmm.jsonutil;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.jsonutil.JSONAgent;

public class JSONAgentTest {

	@Test
	public void test() {
		
		String dbuuid = "c6582d2c-7cd6-4ee3-b425-68662b2558d9";
		String detail = "Salmonella spec";
		Integer id = 4024;
		String name = "salmonella spp";
		
		AgentXml originalAgentXml = new AgentXml(id, name, detail, dbuuid);
		JSONAgent jsonAgent = new JSONAgent(originalAgentXml);
		AgentXml obtainedAgentXml = jsonAgent.toAgentXml();
		
		// Tests obtainedAgentXml
		assertEquals(dbuuid, obtainedAgentXml.getDbuuid());
		assertEquals(detail, obtainedAgentXml.getDetail());
		assertEquals(id, obtainedAgentXml.getId());
		assertEquals(name, obtainedAgentXml.getName());
	}
}
