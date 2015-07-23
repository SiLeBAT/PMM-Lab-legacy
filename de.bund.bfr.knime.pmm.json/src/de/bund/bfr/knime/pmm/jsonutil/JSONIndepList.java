package de.bund.bfr.knime.pmm.jsonutil;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.IndepXml;

public class JSONIndepList {

	JSONArray obj;
	
	public JSONIndepList(JSONArray obj) {
		this.obj = obj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONIndepList(List<IndepXml> indeps) {
		obj = new JSONArray();
		
		for (IndepXml indep : indeps) {
			obj.add(new JSONIndep(indep).getObj());
		}
	}
	
	public JSONArray getObj() {
		return obj;
	}
	
	public List<IndepXml> toIndepXml() {
		List<IndepXml> indeps = new LinkedList<>();
		
		for (int i =  0; i < obj.size(); i++) {
			JSONObject jo = (JSONObject) obj.get(i);
			indeps.add(new JSONIndep(jo).toIndepXml());
		}
		
		return indeps;
	}
}
