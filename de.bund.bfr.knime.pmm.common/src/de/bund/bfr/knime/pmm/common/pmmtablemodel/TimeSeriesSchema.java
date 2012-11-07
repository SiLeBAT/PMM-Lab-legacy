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
package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;

public class TimeSeriesSchema extends KnimeSchema {
	
	public static final String ATT_CONDID = "CondID";
	public static final String ATT_COMBASEID = "CombaseID";
	//public static final String ATT_MISCID = "MiscID";
	public static final String ATT_MISC = "Misc";
	//public static final String ATT_MISCVALUE = "MiscValue";
	//public static final String ATT_MISCUNIT = "MiscUnit";
	public static final String ATT_TEMPERATURE = "Temp";
	public static final String ATT_PH = "pH";
	public static final String ATT_WATERACTIVITY = "aw";
	public static final String ATT_AGENTID = "OrganismID";
	public static final String ATT_AGENTNAME = "OrganismName";
	public static final String ATT_AGENTDETAIL = "OrganismDetail";
	public static final String ATT_MATRIXID = "MatrixID";
	public static final String ATT_MATRIXNAME = "MatrixName";
	public static final String ATT_MATRIXDETAIL = "MatrixDetail";
	@Deprecated
	public static final String ATT_TIME = "Time";
	@Deprecated
	public static final String ATT_LOGC = "Log10C";
	public static final String ATT_TIMESERIES = "MD_Data";
	public static final String ATT_COMMENT = "Comment";
	@Deprecated
	public static final String ATT_LITIDTS = "MDLitID";
	@Deprecated
	public static final String ATT_LITTS = "MDLit";
	public static final String ATT_LITMD = "MD_Literatur";
	public static final String ATT_DBUUID = "MD_DB_UID";

	public static final String DATAID = "DataID";
	public static final String DATAPOINTS = "Data Points";
	
	public TimeSeriesSchema() {

		try {
			addIntAttribute( ATT_CONDID );
			addStringAttribute( ATT_COMBASEID );
			addIntAttribute( ATT_AGENTID );
			addStringAttribute( ATT_AGENTNAME );
			addStringAttribute( ATT_AGENTDETAIL );
			addIntAttribute( ATT_MATRIXID );
			addStringAttribute( ATT_MATRIXNAME );
			addStringAttribute( ATT_MATRIXDETAIL );
			addDoubleAttribute( ATT_TEMPERATURE );
			addDoubleAttribute( ATT_WATERACTIVITY );
			addDoubleAttribute( ATT_PH );
			addDoubleListAttribute( ATT_TIME );
			addDoubleListAttribute( ATT_LOGC );
			addXmlAttribute(ATT_TIMESERIES);
			//addIntListAttribute( ATT_MISCID );
			addXmlAttribute( ATT_MISC );
			//addStringListAttribute( ATT_MISCVALUE );
			//addStringListAttribute( ATT_MISCUNIT );
			addStringAttribute( ATT_COMMENT );
			addIntAttribute( ATT_LITIDTS );
			addStringAttribute( ATT_LITTS );
			addXmlAttribute(ATT_LITMD);
			
			addStringAttribute( ATT_DBUUID );
		}
		catch( PmmException ex ) {
			ex.printStackTrace( System.err );
		}
	}
	
}
