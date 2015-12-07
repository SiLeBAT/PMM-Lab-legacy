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

import de.bund.bfr.knime.pmm.common.IndepXml;

/**
 * JSON object with a PmmLab IndepXml.
 * 
 * @author Miguel Alba
 */
public class JSONIndep {
	JSONObject obj; // json object

	// attribute keys
	static final String ATT_NAME = "name";
	static final String ATT_ORIGNAME = "origname";
	static final String ATT_MIN = "min";
	static final String ATT_MAX = "max";
	static final String ATT_CATEGORY = "category";
	static final String ATT_UNIT = "unit";
	static final String ATT_DESCRIPTION = "description";

	public JSONIndep(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONIndep(IndepXml indep) {
		obj = new JSONObject();

		obj.put(ATT_NAME, indep.getName());
		obj.put(ATT_ORIGNAME, indep.getOrigName());
		obj.put(ATT_MIN, indep.getMin());
		obj.put(ATT_MAX, indep.getMax());
		obj.put(ATT_CATEGORY, indep.getCategory());
		obj.put(ATT_UNIT, indep.getUnit());
		obj.put(ATT_DESCRIPTION, indep.getDescription());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public IndepXml toIndepXml() {
		String name = (String) obj.get(ATT_NAME);
		String origName = (String) obj.get(ATT_ORIGNAME);
		Double min = (Double) obj.get(ATT_MIN);
		Double max = (Double) obj.get(ATT_MAX);
		String category = (String) obj.get(ATT_CATEGORY);
		String unit = (String) obj.get(ATT_UNIT);
		String description = (String) obj.get(ATT_DESCRIPTION);
		
		return new IndepXml(name, origName, min, max, category, unit, description);
	}
}
