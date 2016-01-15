package de.bund.bfr.knime.pmm.js.common;

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
	public void setId(final Integer id) {
		this.id = id;
	}

	/** Sets the author value with 'author'. Converts empty strings to null. */
	public void setAuthor(final String author) {
		this.author = Strings.emptyToNull(author);
	}

	/** Sets the title value with 'title'. Converts empty strings to null. */
	public void setTitle(final String title) {
		this.title = Strings.emptyToNull(title);
	}

	/**
	 * Sets the abstract text with 'abstractText'. Converts empty strings to
	 * null.
	 */
	public void setAbstractText(final String abstractText) {
		this.abstractText = Strings.emptyToNull(abstractText);
	}

	/** Sets the year value with 'year'. */
	public void setYear(final Integer year) {
		this.year = year;
	}

	/**
	 * Sets the journal value with 'journal'. Converts empty strings to null.
	 */
	public void setJournal(final String journal) {
		this.journal = Strings.emptyToNull(journal);
	}

	/** Sets the volume value with 'volume'. Converts empty strings to null. */
	public void setVolume(final String volume) {
		this.volume = Strings.emptyToNull(volume);
	}

	/** Sets the issue value with 'issue'. Converts empty strings to null. */
	public void setIssue(final String issue) {
		this.issue = Strings.emptyToNull(issue);
	}

	/** Sets the page value with 'page'. */
	public void setPage(final Integer page) {
		this.page = page;
	}

	/** Set the approval mode value with 'approvalMode'. */
	public void setApprovalMode(final Integer approvalMode) {
		this.approvalMode = approvalMode;
	}

	/**
	 * Sets the website value with 'website'. Converts empty strings to null.
	 */
	public void setWebsite(final String website) {
		this.website = Strings.emptyToNull(website);
	}

	/** Sets the type value with 'type'. */
	public void setType(final Integer type) {
		this.type = type;
	}

	/** Sets the comment with 'comment'. Converts empty strings to null. */
	public void setComment(final String comment) {
		this.comment = Strings.emptyToNull(comment);
	}

	/** Sets the dbuuid with 'dbuuid'. Converts empty strings to null. */
	public void setDbuuid(final String dbuuid) {
		this.dbuuid = Strings.emptyToNull(dbuuid);
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(AUTHOR, author, settings);
		SettingsHelper.addString(TITLE, title, settings);
		SettingsHelper.addString(ABSTRACT, abstractText, settings);
		SettingsHelper.addInt(YEAR, year, settings);
		SettingsHelper.addString(JOURNAL, journal, settings);
		SettingsHelper.addString(VOLUME, volume, settings);
		SettingsHelper.addString(ISSUE, issue, settings);
		SettingsHelper.addInt(PAGE, page, settings);
		SettingsHelper.addInt(APPROVAL_MODE, approvalMode, settings);
		SettingsHelper.addString(WEBSITE, website, settings);
		SettingsHelper.addInt(TYPE, type, settings);
		SettingsHelper.addString(COMMENT, comment, settings);
		SettingsHelper.addString(DBUUID, dbuuid, settings);
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		author = SettingsHelper.getString(AUTHOR, settings);
		title = SettingsHelper.getString(TITLE, settings);
		abstractText = SettingsHelper.getString(ABSTRACT, settings);
		year = SettingsHelper.getInteger(YEAR, settings);
		journal = SettingsHelper.getString(JOURNAL, settings);
		volume = SettingsHelper.getString(VOLUME, settings);
		issue = SettingsHelper.getString(ISSUE, settings);
		page = SettingsHelper.getInteger(PAGE, settings);
		approvalMode = SettingsHelper.getInteger(APPROVAL_MODE, settings);
		website = SettingsHelper.getString(WEBSITE, settings);
		type = SettingsHelper.getInteger(TYPE, settings);
		comment = SettingsHelper.getString(COMMENT, settings);
		dbuuid = SettingsHelper.getString(DBUUID, settings);
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
