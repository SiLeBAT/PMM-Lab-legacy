/**
 * Code and decode a MatrixXml into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.MatrixXml;

public class JSONMatrix {

	JSONObject obj; // Json object
	
	// attribute keys
	static final String ATT_ID = "id";
	static final String ATT_NAME = "name";
	static final String ATT_DETAIL = "detail";
	static final String ATT_DBUUID = "dbuuid";
	
	public JSONMatrix(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONMatrix(MatrixXml matrix) {
		obj = new JSONObject();

		obj.put(ATT_ID, matrix.getId());
		obj.put(ATT_NAME, matrix.getName());
		obj.put(ATT_DETAIL, matrix.getDetail());
		obj.put(ATT_DBUUID, matrix.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public MatrixXml toMatrixXml() {
		int id = ((Long) obj.get(ATT_ID)).intValue();
		String name = (String) obj.get(ATT_NAME);
		String detail = (String) obj.get(ATT_DETAIL);
		String dbuuid = (String) obj.get(ATT_DBUUID);
		
		return new MatrixXml(id, name, detail, dbuuid);
	}
}
