package de.bund.bfr.knime.pmm.jsonutil;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.ParamXml;

public class JSONParamList {
	JSONArray obj;
	
	public JSONParamList(JSONArray obj) {
		this.obj = obj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONParamList(List<ParamXml> params) {
		obj = new JSONArray();
		
		for (ParamXml param : params) {
			JSONParam jp = new JSONParam(param);
			obj.add(jp.getObj());
		}
	}

	public JSONArray getObj() {
		return obj;
	}
	
	public List<ParamXml> toParamXml() {
		List<ParamXml> params = new ArrayList<>();
		
		for (int i = 0; i < obj.size(); i++) {
			JSONObject jp = (JSONObject) obj.get(i);
			params.add(new JSONParam(jp).toParamXml());
		}
		
		return params;
	}
}
