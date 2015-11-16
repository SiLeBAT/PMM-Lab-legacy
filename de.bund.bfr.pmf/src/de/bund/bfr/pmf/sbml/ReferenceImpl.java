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
package de.bund.bfr.pmf.sbml;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Miguel Alba
 */
public class ReferenceImpl implements Reference {

	private static final String AUTHOR = "author";
	private static final String YEAR = "year";
	private static final String TITLE = "title";
	private static final String ABSTRACT_TEXT = "abstractText";
	private static final String JOURNAL = "journal";
	private static final String VOLUME = "volume";
	private static final String ISSUE = "issue";
	private static final String PAGE = "page";
	private static final String APPROVAL_MODE = "approvalMode";
	private static final String WEBSITE = "website";
	private static final String TYPE = "type";
	private static final String COMMENT = "comment";

	private Map<String, String> properties;

	public ReferenceImpl(String author, Integer year, String title, String abstractText, String journal, String volume,
			String issue, Integer page, Integer approvalMode, String website, ReferenceType referenceType, String comment) {

		properties = new HashMap<>(12);
		if (author != null && !author.isEmpty()) {
			properties.put(AUTHOR, author);
		}

		if (year != null) {
			properties.put(YEAR, year.toString());
		}

		if (title != null && !title.isEmpty()) {
			properties.put(TITLE, title);
		}

		if (abstractText != null && !abstractText.isEmpty()) {
			properties.put(ABSTRACT_TEXT, abstractText);
		}

		if (journal != null && !journal.isEmpty()) {
			properties.put(JOURNAL, journal);
		}

		if (volume != null && !volume.isEmpty()) {
			properties.put(VOLUME, volume);
		}

		if (issue != null && !issue.isEmpty()) {
			properties.put(ISSUE, issue);
		}

		if (page != null) {
			properties.put(PAGE, page.toString());
		}

		if (approvalMode != null) {
			properties.put(APPROVAL_MODE, approvalMode.toString());
		}

		if (website != null && !website.isEmpty()) {
			properties.put(WEBSITE, website.toString());
		}

		if (referenceType != null && !website.isEmpty()) {
			properties.put(TYPE, referenceType.name());
		}

		if (comment != null && !comment.isEmpty()) {
			properties.put(COMMENT, comment);
		}
	}

	/** {@inheritDoc} */
	public String getAuthor() {
		return properties.get(AUTHOR);
	}

	/** {@inheritDoc} */
	public void setAuthor(String author) {
		properties.put(AUTHOR, author);
	}

	/** {@inheritDoc} */
	public boolean isSetAuthor() {
		return properties.containsKey(AUTHOR);
	}

	/** {@inheritDoc} */
	public Integer getYear() {
		return Integer.parseInt(properties.get(YEAR));
	}

	/** {@inheritDoc} */
	public void setYear(Integer year) {
		properties.put(YEAR, year.toString());
	}

	/** {@inheritDoc} */
	public boolean isSetYear() {
		return properties.containsKey(YEAR);
	}

	/** {@inheritDoc} */
	public String getTitle() {
		return properties.get(TITLE);
	}

	/** {@inheritDoc} */
	public void setTitle(String title) {
		properties.put(TITLE, title);
	}

	/** {@inheritDoc} */
	public boolean isSetTitle() {
		return properties.containsKey(TITLE);
	}

	/** {@inheritDoc} */
	public String getAbstractText() {
		return properties.get(ABSTRACT_TEXT);
	}

	/** {@inheritDoc} */
	public void setAbstractText(String abstractText) {
		properties.put(ABSTRACT_TEXT, abstractText);
	}

	/** {@inheritDoc} */
	public boolean isSetAbstractText() {
		return properties.containsKey(ABSTRACT_TEXT);
	}

	/** {@inheritDoc} */
	public String getJournal() {
		return properties.get(JOURNAL);
	}

	/** {@inheritDoc} */
	public void setJournal(String journal) {
		properties.put(JOURNAL, journal);
	}

	/** {@inheritDoc} */
	public boolean isSetJournal() {
		return properties.containsKey(JOURNAL);
	}

	/** {@inheritDoc} */
	public String getVolume() {
		return properties.get(VOLUME);
	}

	/** {@inheritDoc} */
	public void setVolume(String volume) {
		properties.put(VOLUME, volume);
	}

	/** {@inheritDoc} */
	public boolean isSetVolume() {
		return properties.containsKey(VOLUME);
	}

	/** {@inheritDoc} */
	public String getIssue() {
		return properties.get(ISSUE);
	}

	/** {@inheritDoc} */
	public void setIssue(String issue) {
		properties.put(ISSUE, issue);
	}

	/** {@inheritDoc} */
	public boolean isSetIssue() {
		return properties.containsKey(ISSUE);
	}

	/** {@inheritDoc} */
	public Integer getPage() {
		if (properties.containsKey(PAGE))
			return Integer.parseInt(properties.get(PAGE));
		return null;
	}

	/** {@inheritDoc} */
	public void setPage(Integer page) {
		properties.put(PAGE, page.toString());
	}

	/** {@inheritDoc} */
	public boolean isSetPage() {
		return properties.containsKey(PAGE);
	}

	/** {@inheritDoc} */
	public Integer getApprovalMode() {
		if (properties.containsKey(APPROVAL_MODE))
			return Integer.parseInt(properties.get(APPROVAL_MODE));
		return null;
	}

	/** {@inheritDoc} */
	public void setApprovalMode(Integer approvalMode) {
		properties.put(APPROVAL_MODE, approvalMode.toString());
	}

	/** {@inheritDoc} */
	public boolean isSetApprovalMode() {
		return properties.containsKey(APPROVAL_MODE);
	}

	/** {@inheritDoc} */
	public String getWebsite() {
		return properties.get(WEBSITE);
	}

	/** {@inheritDoc} */
	public void setWebsite(String website) {
		properties.put(WEBSITE, website);
	}

	/** {@inheritDoc} */
	public boolean isSetWebsite() {
		return properties.containsKey(WEBSITE);
	}

	/** {@inheritDoc} */
	public ReferenceType getType() {
		if (properties.containsKey(TYPE))
			return ReferenceType.valueOf(properties.get(TYPE));
		return null;
	}

	/** {@inheritDoc} */
	public void setType(ReferenceType type) {
		properties.put(TYPE, type.name());
	}

	/** {@inheritDoc} */
	public boolean isSetType() {
		return properties.containsKey(TYPE);
	}

	/** {@inheritDoc} */
	public String getComment() {
		return properties.get(COMMENT);
	}

	/** {@inheritDoc} */
	public void setComment(String comment) {
		properties.put(COMMENT, comment);
	}

	/** {@inheritDoc} */
	public boolean isSetComment() {
		return properties.containsKey(COMMENT);
	}

	public String toString() {
		return String.format("%s_%s_%s", properties.get(AUTHOR), properties.get(YEAR), properties.get(TITLE));
	}
}