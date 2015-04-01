/**
 * Code and decode a list of MiscXmls into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.MiscXml;

public class JSONMiscList {
	JSONArray obj;
	
	public JSONMiscList(JSONArray obj) {
		this.obj = obj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONMiscList(List<MiscXml> miscList) {
		obj = new JSONArray();
		
		for (MiscXml misc : miscList) {
			JSONMisc jm = new JSONMisc(misc);
			obj.add(jm.getObj());
		}
	}

	public JSONArray getObj() {
		return obj;
	}
	
	public List<MiscXml> toMiscXml() {
		obj = new JSONArray();
		List<MiscXml> misc = new ArrayList<>();
		
		for (int i = 0; i < obj.size(); i++) {
			JSONObject jp = (JSONObject) obj.get(i);
			misc.add(new JSONMisc(jp).toMiscXml());
		}	
			
		return misc;
	}
}
