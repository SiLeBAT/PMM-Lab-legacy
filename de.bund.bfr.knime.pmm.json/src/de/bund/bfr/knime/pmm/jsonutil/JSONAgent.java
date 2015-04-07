/**
 * Code and decode an AgentXml into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.AgentXml;

public class JSONAgent {

	JSONObject obj; // Json object

	// attribute keys
	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_DETAIL = "detail";
	private static final String ATT_DBUUID = "dbuuid";

	public JSONAgent(JSONObject obj) {
		this.obj = obj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONAgent(AgentXml agent) {
		obj = new JSONObject();

		obj.put(ATT_ID, agent.getId());
		obj.put(ATT_NAME, agent.getName());
		obj.put(ATT_DETAIL, agent.getDetail());
		obj.put(ATT_DBUUID, agent.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public AgentXml toAgentXml() {
		int id = ((Long)obj.get(ATT_ID)).intValue();
		String name = (String) obj.get(ATT_NAME);
		String detail = (String) obj.get(ATT_DETAIL);
		String dbuuid = (String) obj.get(ATT_DBUUID);
		return new AgentXml(id, name, detail, dbuuid);
	}
}
