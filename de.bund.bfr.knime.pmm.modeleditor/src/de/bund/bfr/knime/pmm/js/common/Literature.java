package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * PmmLab literature item. Holds:
 * <ul>
 * <li>author
 * <li>year
 * <li>title
 * <li>abstract
 * <li>journal
 * <li>volume
 * <li>issue
 * <li>page
 * <li>approval mode
 * <li>website
 * <li>type
 * <li>comment
 * <li>id
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Literature implements ViewValue {

	// Configuration keys
	static final String AUTHOR = "author";
	static final String YEAR = "year";
	static final String TITLE = "title";
	static final String ABSTRACT = "abstract";
	static final String JOURNAL = "journal";
	static final String VOLUME = "volume";
	static final String ISSUE = "issue";
	static final String PAGE = "page";
	static final String APPROVAL_MODE = "approvalMode";
	static final String WEBSITE = "website";
	static final String TYPE = "type";
	static final String COMMENT = "comment";
	static final String ID = "id";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String author;
	private String title;
	private String abstractText;
	private Integer year;
	private String journal;
	private String volume;
	private String issue;
	private Integer page;
	private Integer approvalMode;
	private String website;
	private Integer type;
	private String comment;
	private String dbuuid;

	/**
	 * Returns the id of this {@link Literature}. If id is not set, returns
	 * null.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the author of this {@link Literature}. If author is not set,
	 * returns null.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Returns the title of this {@link Literature}. If the title is not set,
	 * returns null.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the abstract text of this {@link Literature}. If the abstract
	 * text is not set, returns null.
	 */
	public String getAbstractText() {
		return abstractText;
	}

	/**
	 * Returns the year of this {@link Literature}. If the year is not set,
	 * returns null.
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * Returns the journal of this {@link Literature}. If the journal is not
	 * set, returns null.
	 */
	public String getJournal() {
		return journal;
	}

	/**
	 * Returns the volume of this {@link Literature}. If the volume is not set,
	 * returns null.
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * Returns the issue of this {@link Literature}. If the issue is not set,
	 * returns null.
	 */
	public String getIssue() {
		return issue;
	}

	/**
	 * Returns the page of this {@link Literature}. If the page is not set,
	 * returns null.
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * Returns the approval mode of this {@link Literature}. If the approval
	 * mode is not set, returns null.
	 */
	public Integer getApprovalMode() {
		return approvalMode;
	}

	/**
	 * Returns the website of this {@link Literature}. If the website is not
	 * set, returns null.
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * Returns the type of this {@link Literature}. If the type is not set,
	 * returns null.
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * Returns the comment of this {@link Literature}. If the comment is not
	 * set, returns null.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns the dbuuid of this {@link Literature}. If the dbuuid is not set,
	 * returns null.
	 */
	public String getDbuuid() {
		return dbuuid;
	}

	/** Sets the id value with 'id'. */
	public void setId(Integer id) {
		if (id != null) {
			this.id = id;
		}
	}

	/** Sets the author value with 'author'. Ignores null and empty strings. */
	public void setAuthor(String author) {
		if (!Strings.isNullOrEmpty(author)) {
			this.author = author;
		}
	}

	/** Sets the title value with 'title'. Ignores null and empty strings. */
	public void setTitle(String title) {
		if (!Strings.isNullOrEmpty(title)) {
			this.title = title;
		}
	}

	/**
	 * Sets the abstract text with 'abstractText'. Ignores null and empty
	 * strings.
	 */
	public void setAbstractText(String abstractText) {
		if (!Strings.isNullOrEmpty(abstractText)) {
			this.abstractText = abstractText;
		}
	}

	/** Sets the year value with 'year'. */
	public void setYear(Integer year) {
		if (year != null) {
			this.year = year;
		}
	}

	/**
	 * Sets the journal value with 'journal'. Ignores null and empty strings.
	 */
	public void setJournal(String journal) {
		if (!Strings.isNullOrEmpty(journal)) {
			this.journal = journal;
		}
	}

	/** Sets the volume value with 'volume'. Ignores null and empty strings. */
	public void setVolume(String volume) {
		if (!Strings.isNullOrEmpty(volume)) {
			this.volume = volume;
		}
	}

	/** Sets the issue value with 'issue'. Ignores null and empty strings. */
	public void setIssue(String issue) {
		if (!Strings.isNullOrEmpty(issue)) {
			this.issue = issue;
		}
	}

	/** Sets the page value with 'page'. */
	public void setPage(Integer page) {
		if (page != null) {
			this.page = page;
		}
	}

	/** Set the approval mode value with 'approvalMode'. */
	public void setApprovalMode(Integer approvalMode) {
		if (approvalMode != null) {
			this.approvalMode = approvalMode;
		}
	}

	/**
	 * Sets the website value with 'website'. Ignores null and empty strings.
	 */
	public void setWebsite(String website) {
		if (!Strings.isNullOrEmpty(website)) {
			this.website = website;
		}
	}

	/** Sets the type value with 'type'. */
	public void setType(Integer type) {
		if (type != null) {
			this.type = type;
		}
	}

	/** Sets the comment with 'comment'. */
	public void setComment(String comment) {
		if (!Strings.isNullOrEmpty(comment)) {
			this.comment = comment;
		}
	}

	/** Sets the dbuuid with 'dbuuid'. */
	public void setDbuuid(String dbuuid) {
		if (!Strings.isNullOrEmpty(dbuuid)) {
			this.dbuuid = dbuuid;
		}
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		if (id != null)
			settings.addInt(ID, id);
		if (author != null)
			settings.addString(AUTHOR, author);
		if (title != null)
			settings.addString(TITLE, title);
		if (abstractText != null)
			settings.addString(ABSTRACT, abstractText);
		if (year != null)
			settings.addInt(YEAR, year);
		if (journal != null)
			settings.addString(JOURNAL, journal);
		if (volume != null)
			settings.addString(VOLUME, volume);
		if (issue != null)
			settings.addString(ISSUE, issue);
		if (page != null)
			settings.addInt(PAGE, page);
		if (approvalMode != null)
			settings.addInt(APPROVAL_MODE, approvalMode);
		if (website != null)
			settings.addString(WEBSITE, website);
		if (type != null)
			settings.addInt(TYPE, type);
		if (comment != null)
			settings.addString(COMMENT, comment);
		if (dbuuid != null)
			settings.addString(DBUUID, dbuuid);
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			id = settings.getInt(ID);
		} catch (InvalidSettingsException e) {
			id = null;
		}
		try {
			author = settings.getString(AUTHOR);
		} catch (InvalidSettingsException e) {
			author = null;
		}
		try {
			title = settings.getString(TITLE);
		} catch (InvalidSettingsException e) {
			title = null;
		}
		try {
			abstractText = settings.getString(ABSTRACT);
		} catch (InvalidSettingsException e) {
			abstractText = null;
		}
		try {
			year = settings.getInt(YEAR);
		} catch (InvalidSettingsException e) {
			year = null;
		}
		try {
			journal = settings.getString(JOURNAL);
		} catch (InvalidSettingsException e) {
			journal = null;
		}
		try {
			volume = settings.getString(VOLUME);
		} catch (InvalidSettingsException e) {
			volume = null;
		}
		try {
			issue = settings.getString(ISSUE);
		} catch (InvalidSettingsException e) {
			issue = null;
		}
		try {
			page = settings.getInt(PAGE);
		} catch (InvalidSettingsException e) {
			page = null;
		}
		try {
			approvalMode = settings.getInt(APPROVAL_MODE);
		} catch (InvalidSettingsException e) {
			approvalMode = null;
		}
		try {
			website = settings.getString(WEBSITE);
		} catch (InvalidSettingsException e) {
			website = null;
		}
		try {
			type = settings.getInt(TYPE);
		} catch (InvalidSettingsException e) {
			type = null;
		}
		try {
			comment = settings.getString(COMMENT);
		} catch (InvalidSettingsException e) {
			comment = null;
		}
		try {
			dbuuid = settings.getString(DBUUID);
		} catch (InvalidSettingsException e) {
			dbuuid = null;
		}
	}

	/** Creates an {@link LiteratureItem} from this {@link Literature}. */
	public LiteratureItem toLiteratureItem() {
		return new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page, approvalMode,
				website, type, comment, id, dbuuid);
	}

	/** Creates an {@link Literature} from an {@link LiteratureItem}. */
	public static Literature toLiterature(LiteratureItem literatureItem) {
		Literature literature = new Literature();
		literature.setAuthor(literatureItem.getAuthor());
		literature.setYear(literatureItem.getYear());
		literature.setTitle(literatureItem.getTitle());
		literature.setAbstractText(literatureItem.getAbstractText());
		literature.setJournal(literatureItem.getJournal());
		literature.setVolume(literatureItem.getVolume());
		literature.setIssue(literatureItem.getIssue());
		literature.setPage(literatureItem.getPage());
		literature.setApprovalMode(literatureItem.getApprovalMode());
		literature.setWebsite(literatureItem.getWebsite());
		literature.setType(literatureItem.getType());
		literature.setComment(literatureItem.getComment());
		literature.setId(literatureItem.getId());
		literature.setDbuuid(literatureItem.getDbuuid());

		return literature;
	}
}
