package de.bund.bfr.knime.pmm.jsonencoder;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.AgentXml;

public class JsonAgentEncoder {

	JSONObject obj;  // Json object
	
	public JsonAgentEncoder(String name, AgentXml organism) {
		JSONObject agentObj = new JSONObject();
		agentObj.put("dbuuid", organism.getDbuuid());
		agentObj.put("detail", organism.getDetail());
		agentObj.put("id", organism.getId());
		agentObj.put("name", organism.getName());
		
		obj = new JSONObject();
		obj.put("Organism", agentObj);
	}
	
	public JSONObject getObj() {
		return obj;
	}
}
