package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class JSONLiteratureItem {

	JSONObject obj; // Json object

	public JSONLiteratureItem(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONLiteratureItem(LiteratureItem lit) {
		obj = new JSONObject();

		obj.put("id", lit.getId());
		obj.put("author", lit.getAuthor());
		obj.put("title", lit.getTitle());
		obj.put("abstractText", lit.getAbstractText());
		obj.put("year", lit.getYear());
		obj.put("journal", lit.getJournal());
		obj.put("volume", lit.getVolume());
		obj.put("issue", lit.getIssue());
		obj.put("page", lit.getPage());
		obj.put("approvalMode", lit.getApprovalMode());
		obj.put("website", lit.getWebsite());
		obj.put("type", lit.getType());
		obj.put("comment", lit.getComment());
		obj.put("dbuuid", lit.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}

	public LiteratureItem toLiteratureItem() {
		Long lid = (Long) obj.get("id");
		Integer id = (lid == null) ? null : lid.intValue();
		
		String author = (String) obj.get("author");

		String title = (String) obj.get("title");
		
		String abstractText = (String) obj.get("abstractText");

		Long lyear = (Long) obj.get("year");
		Integer year = (lyear == null) ? null : lyear.intValue();

		String journal = (String) obj.get("journal");

		String volume = (String) obj.get("volume");

		String issue = (String) obj.get("issue");

		Long lpage = (Long) obj.get("page");
		Integer page = (lpage == null) ? null : lpage.intValue();

		Long lApprovalMode = (Long) obj.get("approvalMode");
		Integer approvalMode = (lApprovalMode == null) ? null : lApprovalMode.intValue();

		String website = (String) obj.get("website");

		Long lType = (Long) obj.get("type");
		Integer type = (lType == null) ? null : lType.intValue();

		String comment = (String) obj.get("comment");

		String dbuuid = (String) obj.get("dbuuid");

		return new LiteratureItem(author, year, title, abstractText, journal,
				volume, issue, page, approvalMode, website, type, comment, id,
				dbuuid);
	}
}
