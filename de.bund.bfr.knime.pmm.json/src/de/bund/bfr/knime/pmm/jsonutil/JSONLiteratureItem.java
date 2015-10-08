/**
 * Code and decode a LiteratureItem into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

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

//		Long lyear = (Long) obj.get(ATT_YEAR);
//		Integer year = (lyear == null) ? null : lyear.intValue();

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

		Long lpage = (Long) obj.get(ATT_PAGE);
		Integer page = (lpage == null) ? null : lpage.intValue();

		Long lApprovalMode = (Long) obj.get(ATT_APPROVAL_MODE);
		Integer approvalMode = (lApprovalMode == null) ? null : lApprovalMode.intValue();

		String website = (String) obj.get(ATT_WEBSITE);

		Long lType = (Long) obj.get(ATT_TYPE);
		Integer type = (lType == null) ? null : lType.intValue();

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
