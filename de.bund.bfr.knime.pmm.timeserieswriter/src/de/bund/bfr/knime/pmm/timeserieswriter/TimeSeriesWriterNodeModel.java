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

import org.hsh.bfr.db.DBKernel;
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
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

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
			db = new Bfrdb(DBKernel.getLocalConn(true));
		}
    	db.getConnection().setReadOnly(false);
    	
    	int n = inData[ 0 ].getRowCount();
    	
		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
		HashMap<Integer, PmmTimeSeries> alreadyInsertedTs = new HashMap<Integer, PmmTimeSeries>();
		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds = new HashMap<String, HashMap<String, HashMap<Integer, Integer>>>();
    	String dbuuid = db.getDBUUID();

		int j = 0;
		String warnings = "";
		while (reader.hasMoreElements()) {
    		exec.setProgress( ( double )j++/n );
    		
			KnimeTuple row = reader.nextElement();
			
			PmmTimeSeries ts = new PmmTimeSeries(row);	
			int rowTsID = ts.getCondId();
			if (alreadyInsertedTs.containsKey(rowTsID)) {
				ts = alreadyInsertedTs.get(rowTsID);
			}
			else {
				String[] attrs = new String[] {TimeSeriesSchema.ATT_CONDID, TimeSeriesSchema.ATT_MISC, TimeSeriesSchema.ATT_AGENTID,
						TimeSeriesSchema.ATT_MATRIXID, TimeSeriesSchema.ATT_LITMD};
				String[] dbTablenames = new String[] {"Versuchsbedingungen", "Sonstiges", "Agenzien", "Matrices", "Literatur"};

				checkIDs(true, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames, row.getString(TimeSeriesSchema.ATT_DBUUID));				
				db.insertTs(ts);				
				checkIDs(false, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames, row.getString(TimeSeriesSchema.ATT_DBUUID));
				
				alreadyInsertedTs.put(rowTsID, ts);
				
				if (ts.getWarning() != null && !ts.getWarning().trim().isEmpty()) {
					warnings += ts.getWarning() + "\n";
				}
			}
		}
		if (!warnings.isEmpty()) {
			this.setWarningMessage(warnings.trim());
		}			
    	db.getConnection().setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
    	db.close();
        return null;
    }
    private void checkIDs(boolean before, String dbuuid, KnimeTuple row, KnimeTuple ts,
    		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds,
    		String[] schemaAttr, String[] dbTablename, String rowuuid) throws PmmException {
		if (rowuuid != null && !rowuuid.equals(dbuuid)) {
			if (!foreignDbIds.containsKey(dbuuid)) foreignDbIds.put(dbuuid, new HashMap<String, HashMap<Integer, Integer>>());
			HashMap<String, HashMap<Integer, Integer>> d = foreignDbIds.get(dbuuid);
			
			for (int i=0;i<schemaAttr.length;i++) {
				if (!d.containsKey(dbTablename[i])) d.put(dbTablename[i], new HashMap<Integer, Integer>());
				setIDs(before, schemaAttr[i], d.get(dbTablename[i]), row, ts);
			}
		}    	
    }
    private void setIDs(boolean before, String attr, HashMap<Integer, Integer> foreignDbIds, KnimeTuple row, KnimeTuple schemaTuple) throws PmmException {
    	int type = schemaTuple.getSchema().getType(row.getIndex(attr));
    	if (type == KnimeAttribute.TYPE_XML) {
    		PmmXmlDoc x = row.getPmmXml(attr);
    		if (x != null) {
    			PmmXmlDoc fromToXmlDB = schemaTuple.getPmmXml(attr);
        		//if (before) schemaTuple.setCell(attr, CellIO.createMissingCell());
        		int i=0;
    			for (PmmXmlElementConvertable el : x.getElementSet()) {
    				if (el instanceof MiscXml) {
    					MiscXml mx = (MiscXml) el;
    					MiscXml mxDB = ((MiscXml) fromToXmlDB.get(i));
    					Integer key = mx.getID();
                		if (key != null && foreignDbIds.containsKey(key)) {
                			if (before) mxDB.setID(foreignDbIds.get(key)); //schemaTuple.addValue(attr, foreignDbIds.get(key));
                			else if (foreignDbIds.get(key) != mxDB.getID()) {
                				System.err.println("fillNewIDsIntoForeign ... shouldn't happen");
                			}
                		}
                		else {
                			if (before) mxDB.setID(MathUtilities.getRandomNegativeInt()); //schemaTuple.addValue(attr, MathUtilities.getRandomNegativeInt());
                			else foreignDbIds.put(key, mxDB.getID()); //schemaTuple.getIntList(attr).get(i));
                		}
    				}
    				else if (el instanceof LiteratureItem) {
    					LiteratureItem li = (LiteratureItem) el;
    					LiteratureItem liDB = ((LiteratureItem) fromToXmlDB.get(i));
    					Integer key = li.getId();
                		if (key != null && foreignDbIds.containsKey(key)) {
                			if (before) liDB.setId(foreignDbIds.get(key)); //schemaTuple.addValue(attr, foreignDbIds.get(key));
                			else if (foreignDbIds.get(key) != liDB.getId()) {
                				System.err.println("fillNewIDsIntoForeign ... shouldn't happen");
                			}
                		}
                		else {
                			if (before) liDB.setId(MathUtilities.getRandomNegativeInt()); //schemaTuple.addValue(attr, MathUtilities.getRandomNegativeInt());
                			else foreignDbIds.put(key, liDB.getId()); //schemaTuple.getIntList(attr).get(i));
                		}
    				}
            		i++;
    			}
    			schemaTuple.setValue(attr, fromToXmlDB);
    		}
    	}
    	else {
        	Integer key = row.getInt(attr);
        	if (key != null) {
        		if (foreignDbIds.containsKey(key)) {
        			if (before) schemaTuple.setValue(attr, foreignDbIds.get(key));
        			else if (foreignDbIds.get(key) != schemaTuple.getInt(attr)) {
        				System.err.println("fillNewIDsIntoForeign ... shouldn't happen");
        			}
        		}
        		else {
        			if (before) schemaTuple.setValue(attr, MathUtilities.getRandomNegativeInt());
        			else foreignDbIds.put(key, schemaTuple.getInt(attr));
        		}
        	}
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
    	String errorMsg = "Unexpected format - Microbial data is not present in the columns of the incoming table";
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

