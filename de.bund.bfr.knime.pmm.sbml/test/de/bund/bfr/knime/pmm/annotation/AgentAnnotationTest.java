package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AgentAnnotationTest {
	
	private String casNumber;
	private String agentDetail;
	private String depDesc;
	
	@Before
	public void setUp() {
		casNumber = "C0036111";
		agentDetail = "Salmonella spec.";
		depDesc = "bacterial population at time t -ln() transformed";
	}

	/**
	 * Sanity test for an AgentAnnotation with CAS number, agent details and
	 * dependent variable description.
	 */
	@Test
	public void test() {
		AgentAnnotation aa = new AgentAnnotation(casNumber, agentDetail, depDesc);
		AgentAnnotation aa2 = new AgentAnnotation(aa.getAnnotation());
		assertEquals(casNumber, aa2.getCasNumber());
		assertEquals(agentDetail, aa2.getDetail());
		assertEquals(depDesc, aa2.getDepDesc());
	}

	/**
	 * Tests that an AgentAnnotation non initialized CAS number is null.
	 */
	@Test
	public void testMissingCasNumber() {
		AgentAnnotation aa = new AgentAnnotation(null, agentDetail, depDesc);
		AgentAnnotation aa2 = new AgentAnnotation(aa.getAnnotation());
		assertNull(aa2.getCasNumber());
	}

	/**
	 * Tests that an AgentAnnotation non initialized agent detail is null.
	 */
	@Test
	public void testMissingAgentDetail() {
		AgentAnnotation aa = new AgentAnnotation(casNumber, null, depDesc);
		AgentAnnotation aa2 = new AgentAnnotation(aa.getAnnotation());
		assertNull(aa2.getDetail());
	}

	/**
	 * Tests that an AgentAnnotation non initialized dep desc is null.
	 */
	@Test
	public void testMissingDepDesc() {
		AgentAnnotation aa = new AgentAnnotation(casNumber, agentDetail, null);
		AgentAnnotation aa2 = new AgentAnnotation(aa.getAnnotation());
		assertNull(aa2.getDepDesc());
	}
}
