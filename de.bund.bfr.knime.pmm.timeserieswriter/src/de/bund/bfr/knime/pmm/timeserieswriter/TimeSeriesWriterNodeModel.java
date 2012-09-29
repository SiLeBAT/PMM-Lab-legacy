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
package de.bund.bfr.knime.pmm.timeserieswriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 * This is the model implementation of TimeSeriesWriter.
 * 
 *
 * @author Jorgen Brandt
 */
public class TimeSeriesWriterNodeModel extends NodeModel {
	
	static final String PARAM_FILENAME = "filename";
	static final String PARAM_LOGIN = "login";
	static final String PARAM_PASSWD = "passwd";
	static final String PARAM_OVERRIDE = "override";

	private String filename;
	private String login;
	private String passwd;
	private boolean override;
    
    /**
     * Constructor for the node model.
     */
    protected TimeSeriesWriterNodeModel() {
        super( 1, 0 );
        filename = "";
        login = "";
        passwd = "";
        override = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	Bfrdb db = null;
    	if( override ) {
			db = new Bfrdb( filename, login, passwd );
		} else {
			db = new Bfrdb( BfRNodePluginActivator.getBfRService() );
		}
    	
    	int n = inData[ 0 ].getRowCount();
    	
		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
		HashMap<Integer, Integer> foreignDbTsIds = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbMiscIds = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbAgentIds = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbMatrixIds = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitTsIds = new HashMap<Integer, Integer>();
    	String dbuuid = db.getDBUUID();

		int j = 0;
		String warnings = "";
		while (reader.hasMoreElements()) {
    		exec.setProgress( ( double )j++/n );
    		
			KnimeTuple row = reader.nextElement();
			String rowuuid = row.getString(TimeSeriesSchema.ATT_DBUUID);
			if (rowuuid != null && !rowuuid.equals(dbuuid)) {
				checkIDs(TimeSeriesSchema.ATT_CONDID, foreignDbTsIds, row, false);
				checkIDs(TimeSeriesSchema.ATT_MISCID, foreignDbMiscIds, row, true);
				checkIDs(TimeSeriesSchema.ATT_AGENTID, foreignDbAgentIds, row, false);
				checkIDs(TimeSeriesSchema.ATT_MATRIXID, foreignDbMatrixIds, row, false);
				checkIDs(TimeSeriesSchema.ATT_LITIDTS, foreignDbLitTsIds, row, true);
			}

			PmmTimeSeries ts = new PmmTimeSeries(row);
			
			db.insertTs( ts );
			if (ts.getWarning() != null && !ts.getWarning().trim().isEmpty()) {
				warnings += ts.getWarning() + "\n";
			}
		}
		if (!warnings.isEmpty()) {
			this.setWarningMessage(warnings.trim());
		}			
    	db.close();
        return null;
    }
    private void checkIDs(String attr, HashMap<Integer, Integer> foreignDbIds, KnimeTuple row, boolean hasList) throws PmmException {
    	if (hasList) {
    		List<Integer> keys = row.getIntList(attr);
        	for (Integer key : keys) {
        		if (!foreignDbIds.containsKey(key)) foreignDbIds.put(key, MathUtilities.getRandomNegativeInt());
        		row.setValue(attr, foreignDbIds.get(key));
        	}
    	}
    	else {
        	Integer key = row.getInt(attr);
    		if (!foreignDbIds.containsKey(key)) foreignDbIds.put(key, MathUtilities.getRandomNegativeInt());
    		row.setValue(attr, foreignDbIds.get(key));
    	}
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {}

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure( final DataTableSpec[] inSpecs )
            throws InvalidSettingsException {   	
    	getInSchema(inSpecs[0]);    	
        return null;//new DataTableSpec[]{};
    }
    private KnimeSchema getInSchema(final DataTableSpec inSpec) throws InvalidSettingsException {
    	KnimeSchema result = null;
    	String errorMsg = "Expected format: TS";
    	KnimeSchema inSchema = new TimeSeriesSchema();
    	try {
    		if (inSchema.conforms(inSpec)) {
    			result = inSchema;
   			}
    	}
    	catch (PmmException e) {
    	}
		if (result == null) {
			throw new InvalidSettingsException(errorMsg);
		}
    	return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	settings.addString( PARAM_FILENAME, filename );
    	settings.addString( PARAM_LOGIN, login );
    	settings.addString( PARAM_PASSWD, passwd );
    	settings.addBoolean( PARAM_OVERRIDE, override );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	filename = settings.getString( PARAM_FILENAME );
    	login = settings.getString( PARAM_LOGIN );
    	passwd = settings.getString( PARAM_PASSWD );
    	override = settings.getBoolean( PARAM_OVERRIDE );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {}

}

