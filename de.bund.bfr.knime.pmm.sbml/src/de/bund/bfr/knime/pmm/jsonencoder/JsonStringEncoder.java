package de.bund.bfr.knime.pmm.jsonencoder;

import org.json.simple.JSONObject;

public class JsonStringEncoder {

	JSONObject obj; // Json object

	/**
	 * Initialize encoder.
	 * 
	 * @param name
	 * @param val
	 */
	public JsonStringEncoder(String name, String val) {
		obj = new JSONObject();
		obj.put(name, val);
	}

	public JSONObject getObj() {
		return obj;
	}
}
