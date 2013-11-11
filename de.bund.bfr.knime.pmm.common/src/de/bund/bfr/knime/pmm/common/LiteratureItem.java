/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
 ******************************************************************************/
package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class LiteratureItem implements PmmXmlElementConvertable {

	public static final String ELEMENT_LITERATURE = "Literature";

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
	private String dbuuid = null;

	public LiteratureItem(final String author, final Integer year,
			final String title, final String abstractText, String journal,
			String volume, String issue, Integer page, Integer approvalMode,
			String website, Integer type, String comment, final Integer id,
			String dbuuid) {
		this.author = author;
		this.year = year;
		this.title = title;
		this.abstractText = abstractText;
		this.journal = journal;
		this.volume = volume;
		this.issue = issue;
		this.page = page;
		this.approvalMode = approvalMode;
		this.website = website;
		this.type = type;
		this.comment = comment;
		this.id = id;
		this.dbuuid = dbuuid;
	}

	public LiteratureItem(final String author, final Integer year,
			final String title, final String abstractText, String journal,
			String volume, String issue, Integer page, Integer approvalMode,
			String website, Integer type, String comment, final Integer id) {
		this(author, year, title, abstractText, journal, volume, issue, page,
				approvalMode, website, type, comment, id, null);
	}

	public LiteratureItem(final String author, final Integer year,
			final String title, final String abstractText, String journal,
			String volume, String issue, Integer page, Integer approvalMode,
			String website, Integer type, String comment) {
		this(author, year, title, abstractText, journal, volume, issue, page,
				approvalMode, website, type, comment, MathUtilities
						.getRandomNegativeInt(), null);
	}

	public LiteratureItem(final Element el) {
		this(getString(el, ATT_AUTHOR), getInt(el, ATT_YEAR), getString(el,
				ATT_TITLE), getString(el, ATT_ABSTRACT), getString(el,
				ATT_JOURNAL), getString(el, ATT_VOLUME), getString(el,
				ATT_ISSUE), getInt(el, ATT_PAGE),
				getInt(el, ATT_APPROVAL_MODE), getString(el, ATT_WEBSITE),
				getInt(el, ATT_TYPE), getString(el, ATT_COMMENT), getInt(el,
						ATT_ID), getString(el, ATT_DBUUID));
	}

	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_LITERATURE);
		ret.setAttribute(ATT_AUTHOR, getNonNull(author));
		ret.setAttribute(ATT_YEAR, getNonNull(year));
		ret.setAttribute(ATT_TITLE, removeDirt(title));
		ret.setAttribute(ATT_ABSTRACT, removeDirt(abstractText));
		ret.setAttribute(ATT_JOURNAL, getNonNull(journal));
		ret.setAttribute(ATT_VOLUME, getNonNull(volume));
		ret.setAttribute(ATT_ISSUE, getNonNull(issue));
		ret.setAttribute(ATT_PAGE, getNonNull(page));
		ret.setAttribute(ATT_APPROVAL_MODE, getNonNull(approvalMode));
		ret.setAttribute(ATT_WEBSITE, getNonNull(website));
		ret.setAttribute(ATT_TYPE, getNonNull(type));
		ret.setAttribute(ATT_COMMENT, getNonNull(comment));
		ret.setAttribute(ATT_ID, getNonNull(id));
		ret.setAttribute("dbuuid", getNonNull(dbuuid));
		return ret;
	}

	private String removeDirt(String toClean) {
		String cleaned = (toClean == null ? "" : toClean);
		cleaned = cleaned.toString().replace("&amp;", "&"); // .replace("\n",
															// " ");
															// //.replaceAll("[^A-Za-zäöüßÄÖÜ0-9+-.,;': ()°%?&=<>/]",
															// "");
		cleaned = cleanInvalidXmlChars(cleaned);
		/*
		 * if (toClean != null && !toClean.equals(cleaned)) {
		 * System.err.println(toClean); System.err.println(cleaned); }
		 */
		return cleaned;
	}

	private String cleanInvalidXmlChars(String text) {
		String re = "[^^\u0009\r\n\u0020-\uD7FF\uE000-\uFFFD]";
		return text.replaceAll(re, " ");
	}

	private static String getString(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		return el.getAttributeValue(attr);
	}

	private static Integer getInt(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		try {
			return Integer.valueOf(el.getAttributeValue(attr));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static String getNonNull(String s) {
		if (s == null) {
			return "";
		}

		return s;
	}

	private static String getNonNull(Integer i) {
		if (i == null) {
			return "";
		}

		return i + "";
	}

	@Override
	public String toString() {
		return author + "_" + year + "_" + title;
	}

	public static List<String> getElements() {
		List<String> list = new ArrayList<String>();
		list.add("ID");
		list.add("Author");
		list.add("Year");
		list.add("Title");
		list.add("Abstract");
		list.add("Dbuuid");
		return list;
	}

	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("id")) {
			return IntCell.TYPE;
		} else if (element.equalsIgnoreCase("author")) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase("year")) {
			return IntCell.TYPE;
		} else if (element.equalsIgnoreCase("title")) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase("abstract")) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase("dbuuid")) {
			return StringCell.TYPE;
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstractText() {
		return abstractText;
	}

	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getApprovalMode() {
		return approvalMode;
	}

	public void setApprovalMode(Integer approvalMode) {
		this.approvalMode = approvalMode;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}
}
