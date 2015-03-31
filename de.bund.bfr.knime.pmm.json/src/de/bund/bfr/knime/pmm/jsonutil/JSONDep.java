package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.DepXml;

public class JSONDep {
	JSONObject obj; // Json object

	public JSONDep(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONDep(DepXml dep) {
		obj = new JSONObject();

		obj.put("name", dep.getName());
		obj.put("origName", dep.getOrigName());
		obj.put("min", dep.getMin());
		obj.put("max", dep.getMax());
		obj.put("category", dep.getCategory());
		obj.put("unit", dep.getUnit());
		obj.put("description", dep.getDescription());
	}

	public JSONObject getObj() {
		return obj;
	}

	public DepXml toDepXml() {
		String name = (String) obj.get("name");
		String origName = (String) obj.get("origName");
		double min = (double) obj.get("min");
		double max = (double) obj.get("max");
		String category = (String) obj.get("category");
		String unit = (String) obj.get("unit");
		String description = (String) obj.get("description");

		DepXml dep = new DepXml(name, origName, category, unit, description);
		dep.setMin(min);
		dep.setMax(max);
		return dep;
	}
}
