/**
 * Code and decode a MiscXml into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import java.util.List;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.MiscXml;

public class JSONMisc {
	JSONObject obj; // Json object
	
	// attribute keys
	static final String ATT_ID = "id";
	static final String ATT_NAME = "name";
	static final String ATT_DESCRIPTION = "description";
	static final String ATT_VALUE = "value";
	static final String ATT_CATEGORY = "category";
	static final String ATT_UNIT = "unit";
	static final String ATT_ORIGUNIT = "origUnit";
	static final String ATT_DBUUID = "dbuuid";


	public JSONMisc(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONMisc(MiscXml misc) {
		obj = new JSONObject();

		obj.put(ATT_ID, misc.getId());
		obj.put(ATT_NAME, misc.getName());
		obj.put(ATT_DESCRIPTION, misc.getDescription());
		obj.put(ATT_VALUE, misc.getValue());
		obj.put(ATT_CATEGORY, misc.getCategories());
		obj.put(ATT_UNIT, misc.getUnit());
		obj.put(ATT_ORIGUNIT, misc.getOrigUnit());
		obj.put(ATT_DBUUID, misc.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}

	public MiscXml toMiscXml() {
		Integer id = (Integer) obj.get(ATT_ID);
		String name = (String) obj.get(ATT_NAME);
		String description = (String) obj.get(ATT_DESCRIPTION);
		Double value = (Double) obj.get(ATT_VALUE);
		List<String> categories = (List<String>) obj.get(ATT_CATEGORY);
		String unit = (String) obj.get(ATT_UNIT);
		String origUnit = (String) obj.get(ATT_ORIGUNIT);
		String dbuuid = (String) obj.get(ATT_DBUUID);

		return new MiscXml(id, name, description, value, categories, unit,
				origUnit, dbuuid);
	}
}
