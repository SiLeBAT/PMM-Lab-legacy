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

import java.util.List;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.MiscXml;

/**
 * JSON object with a PmmLab MiscXml.
 * 
 * @author Miguel Alba
 */
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

	@SuppressWarnings("unchecked")
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
