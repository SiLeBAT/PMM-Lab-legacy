package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.IndepXml;

public class JSONIndep {
	JSONObject obj; // json object

	public JSONIndep(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONIndep(IndepXml indep) {
		obj = new JSONObject();

		obj.put("name", indep.getName());
		obj.put("origName", indep.getOrigName());
		obj.put("min", indep.getMin());
		obj.put("max", indep.getMax());
		obj.put("category", indep.getCategory());
		obj.put("unit", indep.getUnit());
		obj.put("description", indep.getDescription());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public IndepXml toIndepXml() {
		String name = (String) obj.get("name");
		String origName = (String) obj.get("origName");
		double min = (double) obj.get("min");
		double max = (double) obj.get("max");
		String category = (String) obj.get("category");
		String unit = (String) obj.get("unit");
		String description = (String) obj.get("description");
		
		return new IndepXml(name, origName, min, max, category, unit, description);
	}
}
