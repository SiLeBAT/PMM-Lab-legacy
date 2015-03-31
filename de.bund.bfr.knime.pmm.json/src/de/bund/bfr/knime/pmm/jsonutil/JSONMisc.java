package de.bund.bfr.knime.pmm.jsonutil;

import java.util.List;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.MiscXml;

public class JSONMisc {
	JSONObject obj; // Json object

	public JSONMisc(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONMisc(MiscXml misc) {
		obj = new JSONObject();

		obj.put("id", misc.getId());
		obj.put("name", misc.getName());
		obj.put("description", misc.getDescription());
		obj.put("value", misc.getValue());
		obj.put("category", misc.getCategories());
		obj.put("unit", misc.getUnit());
		obj.put("origUnit", misc.getOrigUnit());
		obj.put("dbuuid", misc.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}

	public MiscXml toMiscXml() {
		Integer id = (Integer) obj.get("id");
		String name = (String) obj.get("name");
		String description = (String) obj.get("description");
		Double value = (Double) obj.get("value");
		List<String> categories = (List<String>) obj.get("category");
		String unit = (String) obj.get("unit");
		String origUnit = (String) obj.get("origUnit");
		String dbuuid = (String) obj.get("dbuuid");

		return new MiscXml(id, name, description, value, categories, unit,
				origUnit, dbuuid);
	}
}
