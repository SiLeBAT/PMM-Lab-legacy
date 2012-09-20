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

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class LiteratureItem implements PmmXmlElementConvertable {

	public static final String ELEMENT_LITERATURE = "Literature";
	public static final String ATT_AUTHOR = "Author";
	public static final String ATT_YEAR = "Year";
	public static final String ATT_ID = "ID";
	private static final String ATT_TAG = "Tag";
	
	public static final String TAG_M = "m";
	public static final String TAG_EM = "em";

	
	private String author;
	private int year;
	private int id;
	private String tag;
	
	public LiteratureItem( final String author, final int year, final String tag, final int id ) {
		this.author = author;
		this.year = year;
		this.id = id;
		this.tag = tag;
	}
	
	public LiteratureItem( final String author, final int year, final String tag ) {
		this( author, year, tag, MathUtilities.getRandomNegativeInt() );
	}
	
	public LiteratureItem( final Element el ) {

		this(
			el.getAttributeValue( LiteratureItem.ATT_AUTHOR ),
			Integer.valueOf( el.getAttributeValue( LiteratureItem.ATT_YEAR ) ),
			el.getAttributeValue( ATT_TAG ),
			Integer.valueOf( el.getAttributeValue( LiteratureItem.ATT_ID ) ) );
	}
	
	@Override
	public Element toXmlElement() {
		
		Element ret;
		
		ret = new Element( ELEMENT_LITERATURE );
		ret.setAttribute( ATT_AUTHOR, author );
		ret.setAttribute( ATT_YEAR, String.valueOf( year ) );
		ret.setAttribute( ATT_TAG, tag );
		ret.setAttribute( ATT_ID, String.valueOf( id ) );
		
		return ret;
	}
	
	public String getAuthor() { return author; }
	public int getYear() { return year; }
	public int getId() { return id; }
	public String getTag() { return tag; }
	@Override
	public String toString() { return author+"_"+year; }
	
	
}
