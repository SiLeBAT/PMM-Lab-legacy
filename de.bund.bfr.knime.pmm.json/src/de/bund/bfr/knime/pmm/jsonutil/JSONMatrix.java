/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.MatrixXml;

/**
 * JSON object with a PmmLab matrix.
 * 
 * @author Miguel Alba
 */
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
		Object idObj = obj.get(ATT_ID);
		Integer id;
		if (idObj instanceof Long) {
			id = ((Long) idObj).intValue();
		} else {
			id = (Integer) idObj;
		}
		String name = (String) obj.get(ATT_NAME);
		String detail = (String) obj.get(ATT_DETAIL);
		String dbuuid = (String) obj.get(ATT_DBUUID);
		
		return new MatrixXml(id, name, detail, dbuuid);
	}
}
