/**
 * Code and decode a DepXml into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.DepXml;

public class JSONDep {
	JSONObject obj; // Json object

	// attribute keys
	static final String ATT_NAME = "name";
	static final String ATT_ORIGNAME = "origname";
	static final String ATT_MIN = "min";
	static final String ATT_MAX = "max";
	static final String ATT_CATEGORY = "category";
	static final String ATT_UNIT = "unit";
	static final String ATT_DESCRIPTION = "description";

	public JSONDep(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONDep(DepXml dep) {
		obj = new JSONObject();

		obj.put(ATT_NAME, dep.getName());
		obj.put(ATT_ORIGNAME, dep.getOrigName());
		obj.put(ATT_MIN, dep.getMin());
		obj.put(ATT_MAX, dep.getMax());
		obj.put(ATT_CATEGORY, dep.getCategory());
		obj.put(ATT_UNIT, dep.getUnit());
		obj.put(ATT_DESCRIPTION, dep.getDescription());
	}

	public JSONObject getObj() {
		return obj;
	}

	public DepXml toDepXml() {
		String name = (String) obj.get(ATT_NAME);
		String origName = (String) obj.get(ATT_ORIGNAME);
		Double min = (Double) obj.get(ATT_MIN);
		Double max = (Double) obj.get(ATT_MAX);
		String category = (String) obj.get(ATT_CATEGORY);
		String unit = (String) obj.get(ATT_UNIT);
		String description = (String) obj.get(ATT_DESCRIPTION);

		DepXml dep = new DepXml(name, origName, category, unit, description);
		dep.setMin(min);
		dep.setMax(max);
		return dep;
	}
}