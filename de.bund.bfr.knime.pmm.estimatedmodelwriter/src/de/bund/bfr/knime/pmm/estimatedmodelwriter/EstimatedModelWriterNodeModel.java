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
package de.bund.bfr.knime.pmm.estimatedmodelwriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of EstimatedModelWriter.
 * 
 *
 * @author Jorgen Brandt
 */
public class EstimatedModelWriterNodeModel extends NodeModel {
    
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
    protected EstimatedModelWriterNodeModel() {
    
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
    	
    	int n = inData[ 0 ].getRowCount();
    	
		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		boolean model2Conform = inSchema.conforms(new Model2Schema());
		Integer rowEstM2ID = null;
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds = new HashMap<String, HashMap<String, HashMap<Integer, Integer>>>();
    	String dbuuid = db.getDBUUID();
		
		HashMap<Integer, List<Integer>> primEstIDs = new HashMap<Integer, List<Integer>>();
		ParametricModel ppm = null, spm;
		
		int j = 0;
		HashMap<Integer, ParametricModel> alreadyInsertedModel = new HashMap<Integer, ParametricModel>();
		HashMap<Integer, ParametricModel> alreadyInsertedEModel = new HashMap<Integer, ParametricModel>();
		HashMap<Integer, PmmTimeSeries> alreadyInsertedTs = new HashMap<Integer, PmmTimeSeries>();
		boolean M1Writable = false, M2Writable = false;
		String warnings = "";
		while (reader.hasMoreElements()) {
    		exec.setProgress( ( double )j++/n );

			KnimeTuple row = reader.nextElement();
			
    		// TimeSeries
			PmmTimeSeries ts = new PmmTimeSeries(row);	
			int rowTsID = ts.getCondId();
			Integer newTsID = null;
			if (alreadyInsertedTs.containsKey(rowTsID)) {
				ts = alreadyInsertedTs.get(rowTsID);
				newTsID = ts.getCondId();
			}
			else {
				String[] attrs = new String[] {TimeSeriesSchema.ATT_CONDID, TimeSeriesSchema.ATT_MISC, TimeSeriesSchema.ATT_AGENTID,
						TimeSeriesSchema.ATT_MATRIXID, TimeSeriesSchema.ATT_LITMD};
				String[] dbTablenames = new String[] {"Versuchsbedingungen", "Sonstiges", "Agenzien", "Matrices", "Literatur"};
				
				checkIDs(true, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames, row.getString(TimeSeriesSchema.ATT_DBUUID));				
				newTsID = db.insertTs(ts);				
				checkIDs(false, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames, row.getString(TimeSeriesSchema.ATT_DBUUID));
				
				//ts.setCondId(newTsID);
				alreadyInsertedTs.put(rowTsID, ts);
			}
			
			if (newTsID != null) {
				Integer newPrimEstID = null;
				Integer dw = row.getInt(Model1Schema.ATT_DATABASEWRITABLE);
				M1Writable = (dw != null && dw == 1);
				if (M1Writable) {					
					Integer rowMcID = row.getInt(Model1Schema.ATT_MODELID);
		    		String modelName = row.getString(Model1Schema.ATT_MODELNAME);
		    		String formula = row.getString(Model1Schema.ATT_FORMULA);
					PmmXmlDoc depXml = row.getPmmXml(Model1Schema.ATT_DEPENDENT);
					DepXml dx = (DepXml) depXml.getElementSet().get(0);

					PmmXmlDoc paramXml = row.getPmmXml(Model1Schema.ATT_PARAMETER);
					PmmXmlDoc indepXml = row.getPmmXml(Model1Schema.ATT_INDEPENDENT);
					
					PmmXmlDoc mLitXmlDoc = row.getPmmXml(Model1Schema.ATT_MLIT);
					PmmXmlDoc emLitXmlDoc = row.getPmmXml(Model1Schema.ATT_EMLIT);
					
					Integer rowEstM1ID = row.getInt(Model1Schema.ATT_ESTMODELID);
		    		Double rms = row.getDouble(Model1Schema.ATT_RMS);
		    		Double r2 = row.getDouble(Model1Schema.ATT_RSQUARED);
		    		Double aic = row.getDouble(Model1Schema.ATT_AIC);
		    		Double bic = row.getDouble(Model1Schema.ATT_BIC);

		    		// Modellkatalog primary
					if (alreadyInsertedModel.containsKey(rowMcID)) {
						ppm = alreadyInsertedModel.get(rowMcID);
					}
					else {
			    		ppm = new ParametricModel(modelName, formula, dx, 1, rowMcID); // , rowEstM1ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM1ID
			    		ppm.setFormula(ppm.revertFormula());
			    		ppm.setParameter(paramXml);
			    		ppm.setIndependent(indepXml);
			    		ppm.setMLit(mLitXmlDoc);
			    		
						String[] attrs = new String[] {Model1Schema.ATT_MODELID, Model1Schema.ATT_MLIT};
						String[] dbTablenames = new String[] {"Modellkatalog", "Literatur"};
						
						checkIDs(true, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));
						db.insertM(ppm);
						checkIDs(false, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));

						alreadyInsertedModel.put(rowMcID, ppm);
					}
		    		ppm.setRms(rms == null ? Double.NaN : rms);
		    		ppm.setRsquared(r2 == null ? Double.NaN : r2);
		    		ppm.setAic(aic == null ? Double.NaN : aic);
		    		ppm.setBic(bic == null ? Double.NaN : bic);
    		
					ppm.setCondId(newTsID);
					if (alreadyInsertedEModel.containsKey(rowEstM1ID)) {
						newPrimEstID = alreadyInsertedEModel.get(rowEstM1ID).getEstModelId();
					}
					else {
			    		ppm.setEstModelId(rowEstM1ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM1ID);
			    		ppm.setParameter(paramXml);
			    		ppm.setIndependent(indepXml);
			    		ppm.setDepXml(dx);
			    		ppm.setEstLit(emLitXmlDoc);

			    		String[] attrs = new String[] {Model1Schema.ATT_ESTMODELID, Model1Schema.ATT_EMLIT};
						String[] dbTablenames = new String[] {"GeschaetzteModelle", "Literatur"};

						checkIDs(true, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));
						newPrimEstID = db.insertEm(ppm);
						checkIDs(false, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));

						if (newPrimEstID != null) {
				    		//ppm.setEstModelId(newPrimEstID);
				    		alreadyInsertedEModel.put(rowEstM1ID, ppm.clone());
			    		}
					}
				}
				else {
					String text = "Estimated primary model (ID: " + row.getInt(Model1Schema.ATT_ESTMODELID) +
						") is not storable due to joining with unassociated kinetic data\n";
					if (warnings.indexOf(text) < 0)	warnings += text;
				}
				if (model2Conform && newPrimEstID != null) {
					dw = row.getInt(Model2Schema.ATT_DATABASEWRITABLE);
					M2Writable = (dw != null && dw == 1);
					rowEstM2ID = row.getInt(Model2Schema.ATT_ESTMODELID);
					//System.err.println(newPrimEstID + "\t" + rowEstM2ID);
					if (M2Writable) {
			    		Integer rowMcID = row.getInt(Model2Schema.ATT_MODELID);
			    		String modelName = row.getString(Model2Schema.ATT_MODELNAME);
			    		String formula = row.getString(Model2Schema.ATT_FORMULA);
						PmmXmlDoc depXml = row.getPmmXml(Model2Schema.ATT_DEPENDENT);
						DepXml dx = (DepXml) depXml.getElementSet().get(0);

							PmmXmlDoc paramXml = row.getPmmXml(Model2Schema.ATT_PARAMETER);
							PmmXmlDoc indepXml = row.getPmmXml(Model2Schema.ATT_INDEPENDENT);

							PmmXmlDoc mLitXmlDoc = row.getPmmXml(Model2Schema.ATT_MLIT);
							PmmXmlDoc emLitXmlDoc = row.getPmmXml(Model2Schema.ATT_EMLIT);

				    		Double rms = row.getDouble(Model2Schema.ATT_RMS);
				    		Double r2 = row.getDouble(Model2Schema.ATT_RSQUARED);
				    		Double aic = row.getDouble(Model2Schema.ATT_AIC);
				    		Double bic = row.getDouble(Model2Schema.ATT_BIC);
				    						    		
				    		// Modellkatalog secondary
							if (alreadyInsertedModel.containsKey(rowMcID)) {
								spm = alreadyInsertedModel.get(rowMcID);
							}
							else {
					    		spm = new ParametricModel( modelName, formula, dx, 2, rowMcID, rowEstM2ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM2ID );
					    		spm.setFormula(spm.revertFormula());
					    		spm.setParameter(paramXml);
					    		spm.setIndependent(indepXml);
					    		spm.setMLit(mLitXmlDoc);
	
								String[] attrs = new String[] {Model2Schema.ATT_MODELID, Model2Schema.ATT_MLIT};
								String[] dbTablenames = new String[] {"Modellkatalog", "Literatur"};
								
								checkIDs(true, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, row.getString(Model2Schema.ATT_DBUUID));
					    		db.insertM(spm);
								checkIDs(false, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, row.getString(Model2Schema.ATT_DBUUID));

								alreadyInsertedModel.put(rowMcID, spm);
							}
						
							if (alreadyInsertedEModel.containsKey(rowEstM2ID)) {
								spm = alreadyInsertedEModel.get(rowEstM2ID);
							}
							else {
					    		spm.setRms(rms);
					    		spm.setRsquared(r2);
					    		spm.setAic(aic);
					    		spm.setBic(bic);
					    		spm.setEstModelId(rowEstM2ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM2ID);
					    		spm.setParameter(paramXml);
					    		spm.setIndependent(indepXml);
					    		spm.setDepXml(dx);
					    		spm.setEstLit(emLitXmlDoc);

					    		String[] attrs = new String[] {Model2Schema.ATT_ESTMODELID, Model2Schema.ATT_EMLIT};
								String[] dbTablenames = new String[] {"GeschaetzteModelle", "Literatur"};

								checkIDs(true, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, row.getString(Model2Schema.ATT_DBUUID));
								db.insertEm(spm);
								checkIDs(false, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, row.getString(Model2Schema.ATT_DBUUID));
								alreadyInsertedEModel.put(rowEstM2ID, spm.clone());
							}

							if (!primEstIDs.containsKey(spm.getEstModelId())) primEstIDs.put(spm.getEstModelId(), new ArrayList<Integer>());
							primEstIDs.get(spm.getEstModelId()).add(newPrimEstID);
						//}
		    		}
					else {
						String text = "Estimated secondary model (ID: " + rowEstM2ID +
							") is not storable due to joining with unassociated primary model\n";
						if (warnings.indexOf(text) < 0)	warnings += text;
					}
				}
				else {
					//System.err.println("newPrimEstID: " + newPrimEstID);
				}
			}
		}
		if (model2Conform) {
			if (M2Writable) {
				for (Integer estModelId : primEstIDs.keySet()) {
					db.insertEm2(estModelId, primEstIDs.get(estModelId));
				}
			}
			else {
				String text = "Estimated secondary model (ID: " + rowEstM2ID +
					") is not storable due to joining with unassociated primary model\n";
			if (warnings.indexOf(text) < 0)	warnings += text;
			}
		}
		if (!warnings.isEmpty()) {
			this.setWarningMessage(warnings.trim());
		}			
    	db.close();
        return null;
    }

    // Modelle
    private void checkIDs(boolean before, String dbuuid, KnimeTuple row, ParametricModel pm,
    		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds,
    		String[] schemaAttr, String[] dbTablename, String rowuuid) throws PmmException {
		if (rowuuid != null && !rowuuid.equals(dbuuid)) {
			if (!foreignDbIds.containsKey(dbuuid)) foreignDbIds.put(dbuuid, new HashMap<String, HashMap<Integer, Integer>>());
			HashMap<String, HashMap<Integer, Integer>> d = foreignDbIds.get(dbuuid);
			
			for (int i=0;i<schemaAttr.length;i++) {
				if (!d.containsKey(dbTablename[i])) d.put(dbTablename[i], new HashMap<Integer, Integer>());
				setIDs(before, schemaAttr[i], dbTablename[i], d.get(dbTablename[i]), row, pm);
			}
		}    	
    }
    private void setIDs(boolean before, String attr, String dbTablename, HashMap<Integer, Integer> foreignDbIdsTable,
    		KnimeTuple row, ParametricModel pm) throws PmmException {
    	if (dbTablename.equals("Literatur")) {
        	PmmXmlDoc lili = row.getPmmXml(attr);
    		if (lili != null) {
    			PmmXmlDoc fromToXmlDB = attr.startsWith(Model1Schema.ATT_EMLIT) ? pm.getEstModelLit() : pm.getModelLit();
        		int i=0;
    			for (PmmXmlElementConvertable el : lili.getElementSet()) {
    				if (el instanceof LiteratureItem) {
    					LiteratureItem li = (LiteratureItem) el;
    					LiteratureItem liDB = ((LiteratureItem) fromToXmlDB.get(i));
    					Integer key = li.getId();
		        		if (key != null && foreignDbIdsTable.containsKey(key)) {
		        			if (before) liDB.setId(foreignDbIdsTable.get(key));
		        			else if (foreignDbIdsTable.get(key) != liDB.getId()) {
		        				System.err.println("checkIDs ... shouldn't happen");
		        			}
		        		}
		        		else {
		        			if (before) liDB.setId(MathUtilities.getRandomNegativeInt());
		        			else foreignDbIdsTable.put(key, liDB.getId());
		        		}
    				}
            		i++;
    			}
    		}
    	}
    	else if (attr.startsWith(Model1Schema.ATT_MODELID)) { // Modellkatalog
        	Integer key = row.getInt(attr);
        	if (key != null) {
	    		if (foreignDbIdsTable.containsKey(key)) {
	    			if (before) pm.setModelId(foreignDbIdsTable.get(key));
	    			else if (foreignDbIdsTable.get(key) != pm.getModelId()) {
	    				System.err.println("checkIDs ... shouldn't happen");
	    			}
	    		}
	    		else {
	    			if (before) pm.setModelId(MathUtilities.getRandomNegativeInt());
	    			else foreignDbIdsTable.put(key, pm.getModelId());
	    		}
        	}
    	}
    	else if (attr.startsWith(Model1Schema.ATT_ESTMODELID)) { // GeschaetzteModelle
        	Integer key = row.getInt(attr);
        	if (key != null) {
	    		if (foreignDbIdsTable.containsKey(key)) {
	    			if (before) pm.setEstModelId(foreignDbIdsTable.get(key));
	    			else if (foreignDbIdsTable.get(key) != pm.getEstModelId()) {
	    				System.err.println("checkIDs ... shouldn't happen");
	    			}
	    		}
	    		else {
	    			if (before) pm.setEstModelId(MathUtilities.getRandomNegativeInt());
	    			else foreignDbIdsTable.put(key, pm.getEstModelId());
	    		}
        	}
    	}
    }
    
    // TimeSeries
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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
    	getInSchema(inSpecs[0]);
    	return null;
    }
    private KnimeSchema getInSchema(final DataTableSpec inSpec) throws InvalidSettingsException {
    	KnimeSchema result = null;
    	String errorMsg = "Expected format: TS and (M1 or M1 and M2)";
    	KnimeSchema inSchema = new TimeSeriesSchema();
    	try {
    		if (inSchema.conforms(inSpec)) {
    			result = inSchema;
   			}
    	}
    	catch (PmmException e) {
    	}
    	boolean hasM1 = false;
    	inSchema = new Model1Schema();
    	try {
    		if (inSchema.conforms(inSpec)) {
    			result = KnimeSchema.merge(result, inSchema);
    			hasM1 = true;
    		}	
    	}
    	catch (PmmException e) {
    	}
    	inSchema = new Model2Schema();
    	try {
    		if (inSchema.conforms(inSpec)) {
    			if (hasM1) {
					result = KnimeSchema.merge(result, inSchema);
				}
    		}	
    	}
    	catch (PmmException e) {
    	}
    	if (!hasM1) {
    		throw new InvalidSettingsException(errorMsg);
    	}
    	return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo( final NodeSettingsWO settings ) {
    	
    	settings.addString( PARAM_FILENAME, filename );
    	settings.addString( PARAM_LOGIN, login );
    	settings.addString( PARAM_PASSWD, passwd );
    	settings.addBoolean( PARAM_OVERRIDE, override );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom( final NodeSettingsRO settings )
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
    protected void validateSettings( final NodeSettingsRO settings )
            throws InvalidSettingsException {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals( final File internDir,
            final ExecutionMonitor exec ) throws IOException,
            CanceledExecutionException {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals( final File internDir,
            final ExecutionMonitor exec ) throws IOException,
            CanceledExecutionException {}
    
}

