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
 *******************************************************************************/
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
