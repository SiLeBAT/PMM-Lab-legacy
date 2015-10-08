/**
 * Code and decode an AgentXml into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.AgentXml;

/**
 * JSON object with a PmmLab AgentXml.
 * 
 * @author Miguel Alba
 */
public class JSONAgent {

	JSONObject obj; // Json object

	// attribute keys
	static final String ATT_ID = "id";
	static final String ATT_NAME = "name";
	static final String ATT_DETAIL = "detail";
	static final String ATT_DBUUID = "dbuuid";

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
		
		Object idObj = obj.get(ATT_ID);
		Integer id;
		if (idObj instanceof Long) {
			id = ((Long) idObj).intValue();
		} else {
			id = (Integer) idObj;
		}
		
		String name = (String) obj.get(ATT_NAME);
		String detail = (String) obj.get(ATT_DETAIL);
		String dbuuid = (String) obj.get(ATT_DBUUID);
		return new AgentXml(id, name, detail, dbuuid);
	}
}
