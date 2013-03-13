/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
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
	private static final String ATT_ID = "id";
	
	private Integer id;
	private String name;
	private String author;
	private String title;
	private String m_abstract;
	private Integer year;
	private String dbuuid = null;
	
	public LiteratureItem(final String author, final Integer year, final String title, final String m_abstract, final Integer id, String dbuuid) {
		this.author = author;
		this.title = title;
		this.m_abstract = m_abstract;
		this.year = year;
		this.id = id;
		this.dbuuid = dbuuid;
		setName();
	}
	
	public LiteratureItem(final String author, final Integer year, final String title, final String m_abstract, final Integer id) {
		this(author, year, title, m_abstract, id, null);
	}
	public LiteratureItem(final String author, final Integer year, final String title, final String m_abstract) {
		this(author, year, title, m_abstract, MathUtilities.getRandomNegativeInt(), null);
	}
	
	public LiteratureItem(final Element el) {
		this(el.getAttributeValue(ATT_AUTHOR).isEmpty() ? null : el.getAttributeValue(ATT_AUTHOR),
			el.getAttributeValue(ATT_YEAR).isEmpty() ? null : Integer.valueOf(el.getAttributeValue(ATT_YEAR)),
			el.getAttributeValue(ATT_TITLE).isEmpty() ? null : el.getAttributeValue(ATT_TITLE),
			el.getAttributeValue(ATT_ABSTRACT).isEmpty() ? null : el.getAttributeValue(ATT_ABSTRACT),
			Integer.valueOf(el.getAttributeValue(ATT_ID)),
			el.getAttribute("dbuuid") == null ? null : el.getAttribute("dbuuid").getValue());
	}
	
	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_LITERATURE);
		ret.setAttribute(ATT_AUTHOR, author == null ? "" : author);
		ret.setAttribute(ATT_YEAR, year == null ? "" : String.valueOf(year));
		ret.setAttribute(ATT_TITLE, removeDirt(title));
		ret.setAttribute(ATT_ABSTRACT, removeDirt(m_abstract));
		ret.setAttribute(ATT_ID, String.valueOf(id));		
		ret.setAttribute("dbuuid", dbuuid == null ? "" : dbuuid);
		return ret;
	}
	private String removeDirt(String toClean) {
		String cleaned = (toClean == null ? "" : toClean);
		cleaned = cleaned.toString().replace("&amp;", "&"); //.replace("\n", " "); //.replaceAll("[^A-Za-zäöüßÄÖÜ0-9+-.,;': ()°%?&=<>/]", "");
		cleaned = cleanInvalidXmlChars(cleaned);
		/*
		if (toClean != null && !toClean.equals(cleaned)) {
			System.err.println(toClean);
			System.err.println(cleaned);
		}
		*/
		return cleaned;
	}
    private String cleanInvalidXmlChars(String text) {        
        String re = "[^^\u0009\r\n\u0020-\uD7FF\uE000-\uFFFD]";
        return text.replaceAll(re, " ");
    }
	
	public String getName() {return name;}
	public String getAuthor() {return author;}
	public String getTitle() {return title;}
	public String getAbstract() {return m_abstract;}
	public Integer getYear() {return year;}
	public Integer getID() {return id;}
	public String getDbuuid() {return dbuuid;}

	public void setAuthor(String author) {this.author = (author == null) ? "" : author;}
	public void setTitle(String title) {this.title = title;}
	public void setAbstract(String m_abstract) {this.m_abstract = m_abstract;}
	public void setYear(Integer year) {this.year = year;}
	public void setId(Integer id) {this.id = id;}
	public void setName(String name) {this.name = name;}
	public void setName() {name = author+"_"+year+"_"+title;}
	public void setDbuuid(String dbuuid) {this.dbuuid = dbuuid;}

	@Override
	public String toString() {setName(); return name;}
	
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
		}
		else if (element.equalsIgnoreCase("author")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("year")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("title")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("abstract")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("dbuuid")) {
			return StringCell.TYPE;
		}
		return null;
	}
}
