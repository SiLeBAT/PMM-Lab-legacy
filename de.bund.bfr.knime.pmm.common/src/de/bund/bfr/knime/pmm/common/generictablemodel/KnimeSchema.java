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
package de.bund.bfr.knime.pmm.common.generictablemodel;

import java.util.LinkedList;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLCell;

import de.bund.bfr.knime.pmm.common.PmmException;

public class KnimeSchema {
	
	private LinkedList<KnimeAttribute> attributeSet;
	
	public KnimeSchema() {
		attributeSet = new LinkedList<KnimeAttribute>();
	}
	
	/** Merges the two schemata a and b to a new schema.
	 * 
	 * @param a
	 * @param b
	 * @throws PmmException 
	 */
	public KnimeSchema( KnimeSchema a, KnimeSchema b ) throws PmmException {
				
		int i, j, m, n;
		
		if( a == null || b == null )
			throw new PmmException( "Schema must not be null." );
		
		m = a.size();
		n = b.size();
		
		for( i = 0; i < m; i++ )
			for( j = 0; j < n; j++ )
				if( a.getName( i ).equals( b.getName( j ) ) )
					throw new PmmException( "Duplicate names are not allowed." );
		
		attributeSet = new LinkedList<KnimeAttribute>();
		attributeSet.addAll( a.attributeSet );
		attributeSet.addAll( b.attributeSet );
	}
	
	public static KnimeSchema merge( KnimeSchema a, KnimeSchema b ) throws PmmException {
		return new KnimeSchema( a, b );
	}
		
	public void addIntAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_INT );
	}
	
	public void addDoubleAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_DOUBLE );
	}
	
	public void addStringAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_STRING );
	}
	
	public void addIntListAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_COMMASEP_INT );
	}
	
	public void addDoubleListAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_COMMASEP_DOUBLE );
	}
	
	public void addStringListAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_COMMASEP_STRING );
	}
	
	public void addMapAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_MAP );
	}
	
	public void addXmlAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_XML );
	}
	
	public boolean conforms( DataColumnSpec[] spec ) throws PmmException {
		
		if( spec == null )
			throw new PmmException( "Array of column specs must not be null." );
		
		return conforms( new DataTableSpec( spec ) );
	}
	
	public boolean conforms( DataTable table ) throws PmmException {
		
		if( table == null )
			throw new PmmException( "Buffered data table must not be null." );
		
		return conforms( table.getDataTableSpec() );
	}
	
	public boolean conforms( DataTableSpec tspec ) throws PmmException {
		
		int i, n;
		boolean present;
		DataColumnSpec cspec;
		
		if( tspec == null )
			throw new PmmException( "Table spec must not be null." );
		
		n = tspec.getNumColumns();
		for( KnimeAttribute col : attributeSet ) {
			
			present = false;
			for( i = 0; i < n; i++ ) {
				
				cspec = tspec.getColumnSpec( i );
				
				if( col.isInt() && cspec.getType() != IntCell.TYPE )
					continue;
				if( col.isDouble() && cspec.getType() != DoubleCell.TYPE )
					continue;
				
				if( col.getName().equals( cspec.getName() ) )
					present = true;
				
			}
			
			if( !present )
				return false;
		}
		
		return true;
	}
	
	/** Returns true if the other schema is a subset of the attributes of this schema.
	 * 
	 * @param other
	 * @return
	 */
	public boolean conforms( KnimeSchema other ) {
		
		int i, j;
		boolean found;
		
		for( i = 0; i < other.size(); i++ ) {
			
			found = false;
			for( j = 0; j < size(); j++ )
				if( other.getType( i ) == getType( j ) )
					if( other.getName( i ).equals( getName( j ) ) ) {
						found = true;
						break;
					}
			if( found == false )
				return false;
		}
		
		return true;
	}
	
	public boolean containsAtt( String name ) {
		
		int i;
		
		for( i = 0; i < size(); i++ )
			if( getName( i ).equals( name ) )
				return true;
		
		return false;
	}
	
	public DataTableSpec createSpec() {		
		DataColumnSpec[] spec;
		KnimeAttribute col;
		int i;
		DataType t;
		
		spec = new DataColumnSpec[ size() ];
		for( i = 0; i < size(); i++  ) {			
			col = attributeSet.get( i );			
			switch(col.getType()) {			
				case KnimeAttribute.TYPE_INT :
					t = IntCell.TYPE; break;					
				case KnimeAttribute.TYPE_DOUBLE :
					t = DoubleCell.TYPE; break;
				case KnimeAttribute.TYPE_XML :
					t = XMLCell.TYPE; break;
				default :
					t = StringCell.TYPE;
			}
			
			spec[ i ] = new DataColumnSpecCreator( col.getName(), t ).createSpec();
		}
		
		return new DataTableSpec( spec );
	}
	
	public int getIndex( String attName ) throws PmmException {
		
		int i;
		
		for( i = 0; i < size(); i++ )
			if( attributeSet.get( i ).getName().equals( attName ) )
				return i;
		
		throw new PmmException( "An attribute with the name "
			+attName+" is not part of the schema." );
	}
	
	public String getName( final int i ) {
		return attributeSet.get( i ).getName();
	}
	
	public int getType( final int i ) { return attributeSet.get( i ).getType(); }
	
	public int size() {
		return attributeSet.size();
	}
	
	private void addAttribute( final KnimeAttribute col ) throws PmmException {
		
		if( col == null )
			throw new PmmException( "Attribute must not be null." );
		
		attributeSet.add( col );
	}
	
	private void addAttribute( final String name, final int type ) throws PmmException {
		addAttribute( new KnimeAttribute( name, type ) );
	}
	
	public static String getAttribute(String attribute, int level) {
		if (level == 1) return attribute;
		else if (level == 2) return attribute + "Sec";
		else return null;
	}
	
}
