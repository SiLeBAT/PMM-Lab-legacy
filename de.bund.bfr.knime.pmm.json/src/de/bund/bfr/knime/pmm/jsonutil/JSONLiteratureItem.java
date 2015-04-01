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
	private static final String ATT_AUTHOR = "author";
	private static final String ATT_YEAR = "year";
	private static final String ATT_TITLE = "title";
	private static final String ATT_ABSTRACT = "abstract";
	private static final String ATT_JOURNAL = "journal";
	private static final String ATT_VOLUME = "volume";
	private static final String ATT_ISSUE = "issue";
	private static final String ATT_PAGE = "page";
	private static final String ATT_APPROVAL_MODE = "approvalMode";
	private static final String ATT_WEBSITE = "website";
	private static final String ATT_TYPE = "type";
	private static final String ATT_COMMENT = "comment";
	private static final String ATT_ID = "id";
	private static final String ATT_DBUUID = "dbuuid";


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

		Long lyear = (Long) obj.get(ATT_YEAR);
		Integer year = (lyear == null) ? null : lyear.intValue();

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

		Long lid = (Long) obj.get(ATT_ID);
		Integer id = (lid == null) ? null : lid.intValue();

		String dbuuid = (String) obj.get(ATT_DBUUID);

		return new LiteratureItem(author, year, title, abstractText, journal,
				volume, issue, page, approvalMode, website, type, comment, id,
				dbuuid);
	}
}
