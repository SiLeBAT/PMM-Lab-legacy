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
package de.bund.bfr.knime.pmm.bfrdbiface.lib;

import java.sql.SQLException;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.DbRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class DbPeiReader extends DbRelationReader {
	
	public DbPeiReader() throws PmmException, SQLException {
		
		super( KnimeSchema.merge( new TimeSeriesSchema(), new Model1Schema() ) );
		
		setQuery( Bfrdb.queryEstPei() );
		
		addSibling( TimeSeriesSchema.ATT_CONDID, Bfrdb.ATT_CONDITIONID );
		addSibling( TimeSeriesSchema.ATT_COMBASEID, Bfrdb.ATT_COMBASEID );
		addSibling( TimeSeriesSchema.ATT_MISCID, Bfrdb.ATT_MISCID );
		addSibling( TimeSeriesSchema.ATT_MISC, Bfrdb.ATT_MISC );
		addSibling( TimeSeriesSchema.ATT_TEMPERATURE, Bfrdb.ATT_TEMPERATURE );
		addSibling( TimeSeriesSchema.ATT_PH, Bfrdb.ATT_PH );
		addSibling( TimeSeriesSchema.ATT_WATERACTIVITY, Bfrdb.ATT_AW );
		addSibling( TimeSeriesSchema.ATT_AGENTID, Bfrdb.ATT_AGENTID );
		addSibling( TimeSeriesSchema.ATT_AGENTNAME, Bfrdb.ATT_AGENTNAME );
		addSibling( TimeSeriesSchema.ATT_AGENTDETAIL, Bfrdb.ATT_AGENTDETAIL );
		addSibling( TimeSeriesSchema.ATT_MATRIXID, Bfrdb.ATT_MATRIXID );
		addSibling( TimeSeriesSchema.ATT_MATRIXNAME, Bfrdb.ATT_MATRIXNAME );
		addSibling( TimeSeriesSchema.ATT_MATRIXDETAIL, Bfrdb.ATT_MATRIXDETAIL );
		addSibling( TimeSeriesSchema.ATT_TIME, Bfrdb.ATT_TIME );
		addSibling( TimeSeriesSchema.ATT_LOGC, Bfrdb.ATT_LOG10N );
		addSibling( TimeSeriesSchema.ATT_COMMENT, Bfrdb.ATT_COMMENT );
		// addSibling( TimeSeriesSchema.ATT_LITIDTS, Bfrdb.ATT_LITERATUREID );
		// addSibling( TimeSeriesSchema.ATT_LITTS, Bfrdb.ATT_LITERATURETEXT );
		
		addSibling( Model1Schema.ATT_FORMULA, Bfrdb.ATT_FORMULA );
		addSibling( Model1Schema.ATT_DEPVAR, Bfrdb.ATT_DEP );
		addSibling( Model1Schema.ATT_INDEPVAR, Bfrdb.ATT_INDEP );
		// addSibling( Model1Schema.ATT_)
	}
	
	

}
