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

import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * JSON object with a PmmLab LiteratureItem.
 * 
 * @author Miguel Alba
 */
public class JSONLiteratureItem {

	JSONObject obj; // Json object
	
	// attribute keys
	static final String ATT_AUTHOR = "author";
	static final String ATT_YEAR = "year";
	static final String ATT_TITLE = "title";
	static final String ATT_ABSTRACT = "abstract";
	static final String ATT_JOURNAL = "journal";
	static final String ATT_VOLUME = "volume";
	static final String ATT_ISSUE = "issue";
	static final String ATT_PAGE = "page";
	static final String ATT_APPROVAL_MODE = "approvalMode";
	static final String ATT_WEBSITE = "website";
	static final String ATT_TYPE = "type";
	static final String ATT_COMMENT = "comment";
	static final String ATT_ID = "id";
	static final String ATT_DBUUID = "dbuuid";


	public JSONLiteratureItem(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONLiteratureItem(LiteratureItem lit) {
		obj = new JSONObject();
		
		obj.put(ATT_AUTHOR, lit.getAuthor());
		obj.put(ATT_YEAR, lit.getYear());
		obj.put(ATT_TITLE, lit.getTitle());
		obj.put(ATT_ABSTRACT, lit.getAbstractText());
		obj.put(ATT_JOURNAL, lit.getJournal());
		obj.put(ATT_VOLUME, lit.getVolume());
		obj.put(ATT_ISSUE, lit.getIssue());
		obj.put(ATT_PAGE, lit.getPage());
		obj.put(ATT_APPROVAL_MODE, lit.getApprovalMode());
		obj.put(ATT_WEBSITE, lit.getWebsite());
		obj.put(ATT_TYPE, lit.getType());
		obj.put(ATT_COMMENT, lit.getComment());
		obj.put(ATT_ID, lit.getId());
		obj.put(ATT_DBUUID, lit.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}

	public LiteratureItem toLiteratureItem() {
		String author = (String) obj.get(ATT_AUTHOR);

		Object yearObj = obj.get(ATT_YEAR);
		Integer year;
		if (yearObj == null) {
			year = null;
		} else if (yearObj instanceof Long) {
			year = ((Long) yearObj).intValue();
		} else {
			year = (Integer) yearObj;
		}

		String title = (String) obj.get(ATT_TITLE);
		String abstractText = (String) obj.get(ATT_ABSTRACT);
		String journal = (String) obj.get(ATT_JOURNAL);
		String volume = (String) obj.get(ATT_VOLUME);
		String issue = (String) obj.get(ATT_ISSUE);

		Object pageObj = obj.get(ATT_PAGE);
		Integer page;
		if (pageObj == null) {
			page = null;
		} else if (pageObj instanceof Long) {
			page = ((Long) pageObj).intValue();
		} else {
			page = (Integer) pageObj;
		}
		
		Object approvalModeObj = obj.get(ATT_APPROVAL_MODE);
		Integer approvalMode;
		if (approvalModeObj == null) {
			approvalMode = null;
		} else if (approvalModeObj instanceof Long) {
			approvalMode = ((Long) approvalModeObj).intValue();
		} else {
			approvalMode = (Integer) approvalModeObj;
		}

		String website = (String) obj.get(ATT_WEBSITE);

		Object typeObj = obj.get(ATT_TYPE);
		Integer type;
		if (typeObj == null) {
			type = null;
		} else if (typeObj instanceof Long) {
			type = ((Long) typeObj).intValue();
		} else {
			type = (Integer) typeObj;
		}

		String comment = (String) obj.get(ATT_COMMENT);

		Object idObj = obj.get(ATT_ID);
		Integer id;
		if (idObj == null) {
			id = null;
		} else if (idObj instanceof Long) {
			id = ((Long) idObj).intValue();
		} else {
			id = (Integer) idObj;
		}

		String dbuuid = (String) obj.get(ATT_DBUUID);

		return new LiteratureItem(author, year, title, abstractText, journal,
				volume, issue, page, approvalMode, website, type, comment, id,
				dbuuid);
	}
}
