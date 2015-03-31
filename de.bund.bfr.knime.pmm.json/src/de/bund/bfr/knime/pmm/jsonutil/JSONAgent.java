package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.AgentXml;

public class JSONAgent {

	JSONObject obj; // Json object

	public JSONAgent(JSONObject obj) {
		this.obj = obj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONAgent(AgentXml agent) {
		obj = new JSONObject();

		obj.put("id", agent.getId());
		obj.put("name", agent.getName());
		obj.put("detail", agent.getDetail());
		obj.put("dbuuid", agent.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public AgentXml toAgentXml() {
		int id = ((Long) obj.get("id")).intValue();
		String name = (String) obj.get("name");
		String detail = (String) obj.get("detail");
		String dbuuid = (String) obj.get("dbuuid");
		return new AgentXml(id, name, detail, dbuuid);
	}
}
