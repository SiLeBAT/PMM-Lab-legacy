package de.bund.bfr.knime.pmm.jsonencoder;

import org.json.simple.JSONObject;

public class JsonIntEncoder {
	
	JSONObject obj;  // Json object

	/**
	 * Initialize encoder.
	 * @param name: Column name.
	 * @param val: Value.
	 */
	public JsonIntEncoder(String name, int val) {
		obj = new JSONObject();
		obj.put(name, val);
	}
	
	public JSONObject getObj() {
		return obj;
	}
}
