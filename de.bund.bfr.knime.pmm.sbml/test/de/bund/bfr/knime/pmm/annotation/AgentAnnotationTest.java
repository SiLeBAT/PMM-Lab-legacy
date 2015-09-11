package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

public class AgentAnnotationTest {

	@Test
	public void test() {
		String casNumber = "C0036111";
		String agentDetail = "Salmonella spec";
		String depDesc = "bacterial population at time t -ln() transformed";

		AgentAnnotation aa = new AgentAnnotation(casNumber, agentDetail, depDesc);
		AgentAnnotation aa2 = new AgentAnnotation(aa.getAnnotation());
		assertEquals(casNumber, aa2.getCasNumber());
		assertEquals(agentDetail, aa2.getDetail());
		assertEquals(depDesc, aa2.getDepDesc());
	}
	
	@Test
	public void testMissingCasNumber() {
		String agentDetail = "Salmonella spec";
		String depDesc = "bacterial population at time t -ln() transformed";

		AgentAnnotation aa = new AgentAnnotation(null, agentDetail, depDesc);
		AgentAnnotation aa2 = new AgentAnnotation(aa.getAnnotation());
		assertNull(aa2.getCasNumber());
	}

	@Test
	public void testMissingAgentDetail() {
		String casNumber = "C0036111";
		String depDesc = "bacterial population at time t -ln() transformed";

		AgentAnnotation aa = new AgentAnnotation(casNumber, null, depDesc);
		AgentAnnotation aa2 = new AgentAnnotation(aa.getAnnotation());
		assertNull(aa2.getDetail());
	}
	
	@Test
	public void testMissingDepDesc() {
		String casNumber = "C0036111";
		String agentDetail = "Salmonella spec";

		AgentAnnotation aa = new AgentAnnotation(casNumber, agentDetail, null);
		AgentAnnotation aa2 = new AgentAnnotation(aa.getAnnotation());
		assertNull(aa2.getDepDesc());
	}
}
