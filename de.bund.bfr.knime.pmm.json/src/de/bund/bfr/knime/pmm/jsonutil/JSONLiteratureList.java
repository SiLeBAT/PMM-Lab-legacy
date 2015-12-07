/**
 * Code and decode a list of LiteratureItems into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * Code a list of literature items into JSON.
 * 
 * @author Miguel Alba
 */
public class JSONLiteratureList {
	JSONArray obj;

	public JSONLiteratureList(JSONArray obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONLiteratureList(List<LiteratureItem> lits) {
		obj = new JSONArray();

		for (LiteratureItem lit : lits) {
			JSONLiteratureItem jlit = new JSONLiteratureItem(lit);
			obj.add(jlit.getObj());
		}
	}

	public JSONArray getObj() {
		return obj;
	}

	public List<LiteratureItem> toLiteratureItem() {
		List<LiteratureItem> lits = new ArrayList<>();

		for (int i = 0; i < obj.size(); i++) {
			JSONObject jp = (JSONObject) obj.get(i);
			lits.add(new JSONLiteratureItem(jp).toLiteratureItem());
		}

		return lits;
	}
}