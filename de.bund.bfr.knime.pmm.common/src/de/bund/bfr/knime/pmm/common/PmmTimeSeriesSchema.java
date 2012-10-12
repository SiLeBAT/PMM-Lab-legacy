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

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLCell;

@Deprecated
public class PmmTimeSeriesSchema {
	
	public static final String TABLE_TIMESERIES = "PMM time series";
	
	public static final String ATT_COMBASEID = "CombaseID";
	public static final String ATT_TEMPERATURE = "T";
	public static final String ATT_WATERACTIVITY = "Aw";
	public static final String ATT_PH = "Ph";
	public static final String ATT_AGENTNAME = "AgentName";
	public static final String ATT_MATRIXNAME = "MatrixName";
	public static final String ATT_MISC = "Misc";
	public static final String ATT_MAXIMUMRATE = "MaximumRate";
	public static final String ATT_TIME = "Time";
	public static final String ATT_LOGC = "LogC";
	public static final String ATT_DOUBLINGTIME = "DoublingTime";
	public static final String ATT_CONDID = "CondID";
	public static final String ATT_MATRIXID = "MatrixID";
	public static final String ATT_MATRIXDETAIL = "MatrixDetail";
	public static final String ATT_AGENTID = "AgentID";
	public static final String ATT_AGENTDETAIL = "AgentDetail";
	public static final String ATT_COMMENT = "Comment";
	
	public static final int ID_CONDID = 0;
	public static final int ID_COMBASEID = 1;
	public static final int ID_MISC = 2;
	public static final int ID_TEMP = 3;
	public static final int ID_PH = 4;
	public static final int ID_AW = 5;
	public static final int ID_AGENTID = 6;
	public static final int ID_AGENTNAME = 7;
	public static final int ID_AGENTDETAIL = 8;
	public static final int ID_MATRIXID = 9;
	public static final int ID_MATRIXNAME = 10;
	public static final int ID_MATRIXDETAIL = 11;	
	public static final int ID_TIME = 12;
	public static final int ID_LOG10N = 13;
	public static final int ID_MAXIMUMRATE = 14;
	public static final int ID_DOUBLINGTIME = 15;
	public static final int ID_COMMENT = 16;
	
	public static final int NCOL = 17;

	
	public static DataTableSpec createSpec() { return createSpec( "" ); }
	
    public static DataTableSpec createSpec( final String filename ) {
    	
    	DataColumnSpec[] spec;
    	
    	spec = new DataColumnSpec[ NCOL ];
    	
    	spec[ ID_CONDID ] = new DataColumnSpecCreator( ATT_CONDID, IntCell.TYPE ).createSpec();
    	spec[ ID_TIME ] = new DataColumnSpecCreator( ATT_TIME, StringCell.TYPE ).createSpec();
    	spec[ ID_LOG10N ] = new DataColumnSpecCreator( ATT_LOGC, StringCell.TYPE ).createSpec();
    	spec[ ID_TEMP ] = new DataColumnSpecCreator( ATT_TEMPERATURE, DoubleCell.TYPE ).createSpec();
    	spec[ ID_PH ] = new DataColumnSpecCreator( ATT_PH, DoubleCell.TYPE ).createSpec();
    	spec[ ID_AW ] = new DataColumnSpecCreator( ATT_WATERACTIVITY, DoubleCell.TYPE ).createSpec();
    	spec[ ID_AGENTNAME ] = new DataColumnSpecCreator( ATT_AGENTNAME, StringCell.TYPE ).createSpec();
    	spec[ ID_MATRIXNAME ] = new DataColumnSpecCreator( ATT_MATRIXNAME, StringCell.TYPE ).createSpec();
    	spec[ ID_MISC ] = new DataColumnSpecCreator( ATT_MISC, XMLCell.TYPE ).createSpec();
    	spec[ ID_MAXIMUMRATE ] = new DataColumnSpecCreator( ATT_MAXIMUMRATE, DoubleCell.TYPE ).createSpec();
    	spec[ ID_DOUBLINGTIME ] = new DataColumnSpecCreator( ATT_DOUBLINGTIME, DoubleCell.TYPE ).createSpec();
    	spec[ ID_COMBASEID ] = new DataColumnSpecCreator( ATT_COMBASEID, StringCell.TYPE ).createSpec();
    	spec[ ID_MATRIXID ] = new DataColumnSpecCreator( ATT_MATRIXID, IntCell.TYPE ).createSpec();
    	spec[ ID_AGENTID ] = new DataColumnSpecCreator( ATT_AGENTID, IntCell.TYPE ).createSpec();
    	spec[ ID_MATRIXDETAIL ] = new DataColumnSpecCreator( ATT_MATRIXDETAIL, StringCell.TYPE ).createSpec();
    	spec[ ID_AGENTDETAIL ] = new DataColumnSpecCreator( ATT_AGENTDETAIL, StringCell.TYPE ).createSpec();
    	spec[ ID_COMMENT ] = new DataColumnSpecCreator( ATT_COMMENT, StringCell.TYPE ).createSpec();
    	
    	return new DataTableSpec( TABLE_TIMESERIES+" "+filename, spec );
    }



}
