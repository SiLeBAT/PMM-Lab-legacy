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

import java.util.Enumeration;
import java.util.Iterator;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;

import de.bund.bfr.knime.pmm.common.PmmException;

public class KnimeRelationReader implements Enumeration<KnimeTuple> {
	
	Iterator<DataRow> rowIterator;
	KnimeSchema schema;
	DataTableSpec spec;
	
	public KnimeRelationReader( KnimeSchema schema, DataTable buffer ) throws PmmException {
				
		if( !schema.conforms( buffer ) )
			throw new PmmException( "Mapping of buffer on schema impossible." );
		
		this.schema = schema;
		rowIterator = buffer.iterator();
		spec = buffer.getDataTableSpec();
	}

	@Override
	public boolean hasMoreElements() {
		return rowIterator.hasNext();
	}

	@Override
	public KnimeTuple nextElement() {
		try {
			return new KnimeTuple( schema, spec, rowIterator.next() );
		} catch ( PmmException ex ) {
			return null;
		}
	}

}
