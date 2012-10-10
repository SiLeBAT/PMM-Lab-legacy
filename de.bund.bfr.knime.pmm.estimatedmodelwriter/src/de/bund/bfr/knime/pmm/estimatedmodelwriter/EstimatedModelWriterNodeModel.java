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
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

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
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

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
			db = new Bfrdb( BfRNodePluginActivator.getBfRService() );
		}
    	
    	int n = inData[ 0 ].getRowCount();
    	
		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		boolean model2Conform = inSchema.conforms(new Model2Schema());
		Integer rowEstM2ID = null;
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds = new HashMap<String, HashMap<String, HashMap<Integer, Integer>>>();
		/*
		HashMap<Integer, Integer> foreignDbTsIds = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbMiscIds = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbAgentIds = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbMatrixIds = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitTsIds = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbMC1Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbME1Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitMC1Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitME1Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbMC2Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbME2Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitMC2Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitME2Ids = new HashMap<Integer, Integer>();
		*/
    	String dbuuid = db.getDBUUID();
		
		List<Integer> primEstIDs = new ArrayList<Integer>();
		ParametricModel ppm = null, spm, lastSpm = null;
		List<String> lastVarParMap = null;
		KnimeTuple lastRow = null;
		
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
				String[] attrs = new String[] {TimeSeriesSchema.ATT_CONDID, TimeSeriesSchema.ATT_MISCID, TimeSeriesSchema.ATT_AGENTID,
						TimeSeriesSchema.ATT_MATRIXID, TimeSeriesSchema.ATT_LITIDTS};
				String[] dbTablenames = new String[] {"Versuchsbedingungen", "Sonstiges", "Agenzien", "Matrices", "Literatur"};
				
				checkIDs(true, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames);				
				newTsID = db.insertTs(ts);				
				checkIDs(false, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames);
				
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
		    		String depVar = row.getString(Model1Schema.ATT_DEPVAR);
		    		List<String> indepVar = row.getStringList(Model1Schema.ATT_INDEPVAR);
		    		List<String> paramName = row.getStringList(Model1Schema.ATT_PARAMNAME);

		    		List<Double> minVal = row.getDoubleList(Model1Schema.ATT_MINVALUE);
		    		List<Double> maxVal = row.getDoubleList(Model1Schema.ATT_MAXVALUE);
		    		List<Double> minIndep = row.getDoubleList(Model1Schema.ATT_MININDEP);
		    		List<Double> maxIndep = row.getDoubleList(Model1Schema.ATT_MAXINDEP);
		    		List<String> litStr = row.getStringList(Model1Schema.ATT_LITM);
		    		List<Integer> litID = row.getIntList(Model1Schema.ATT_LITIDM);
		    		List<String> litEMStr = row.getStringList(Model1Schema.ATT_LITEM);
		    		List<Integer> litEMID = row.getIntList(Model1Schema.ATT_LITIDEM);
		    		
		    		// ab hier: different from ModelCatalogWriter
					Integer rowEstM1ID = row.getInt(Model1Schema.ATT_ESTMODELID);
		    		List<Double> paramValues = row.getDoubleList(Model1Schema.ATT_VALUE);
		    		List<Double> paramErrs = row.getDoubleList(Model1Schema.ATT_PARAMERR);
		    		Double rms = row.getDouble(Model1Schema.ATT_RMS);
		    		Double r2 = row.getDouble(Model1Schema.ATT_RSQUARED);
		    		List<String> varParMap = row.getStringList(Model1Schema.ATT_VARPARMAP);		
		    		
		    		// Modellkatalog
					if (alreadyInsertedModel.containsKey(rowMcID)) {
						ppm = alreadyInsertedModel.get(rowMcID);
					}
					else {
			    		ppm = new ParametricModel(modelName, formula, depVar, 1, rowMcID); // , rowEstM1ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM1ID
			    		doMinMax(ppm, paramName, paramValues, paramErrs, minVal, maxVal, false);
			    		doMinMax(ppm, indepVar, null, null, minIndep, maxIndep, true);
			    		doLit(ppm, litStr, litID, false);
			    		doLit(ppm, litEMStr, litEMID, true);
			    		
						String[] attrs = new String[] {Model1Schema.ATT_MODELID, Model1Schema.ATT_LITIDM};
						String[] dbTablenames = new String[] {"Modellkatalog", "Literatur"};
						
						checkIDs(true, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));
						db.insertM(ppm, varParMap);
						checkIDs(false, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));

						alreadyInsertedModel.put(rowMcID, ppm);
					}
		    		ppm.setRms(rms == null ? Double.NaN : rms);
		    		ppm.setRsquared(r2 == null ? Double.NaN : r2);
    		
					ppm.setCondId(newTsID);
					if (alreadyInsertedEModel.containsKey(rowEstM1ID)) {
						newPrimEstID = alreadyInsertedEModel.get(rowEstM1ID).getEstModelId();
					}
					else {
			    		ppm.setEstModelId(rowEstM1ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM1ID);
			    		doMinMax(ppm, paramName, paramValues, paramErrs, minVal, maxVal, false);
			    		doMinMax(ppm, indepVar, null, null, minIndep, maxIndep, true);
			    		doLit(ppm, litStr, litID, false);
			    		doLit(ppm, litEMStr, litEMID, true);

			    		String[] attrs = new String[] {Model1Schema.ATT_ESTMODELID, Model1Schema.ATT_LITIDEM};
						String[] dbTablenames = new String[] {"GeschaetzteModelle", "Literatur"};

						checkIDs(true, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));
						newPrimEstID = db.insertEm(ppm, varParMap);
						//System.err.println("Prim\t" + rowEstM1ID + "\t" + newPrimEstID);
						checkIDs(false, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));

						if (newPrimEstID != null) {
				    		//ppm.setEstModelId(newPrimEstID);
				    		alreadyInsertedEModel.put(rowEstM1ID, ppm);
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
			    		String depVar = row.getString(Model2Schema.ATT_DEPVAR);
			    		List<String> indepVar = row.getStringList(Model2Schema.ATT_INDEPVAR);
			    		List<String> paramName = row.getStringList(Model2Schema.ATT_PARAMNAME);
			    		if (!paramName.isEmpty()) {
			    			List<Double> minVal = row.getDoubleList(Model2Schema.ATT_MINVALUE);
			    			List<Double> maxVal = row.getDoubleList(Model2Schema.ATT_MAXVALUE);
			    			List<Double> minIndep = row.getDoubleList(Model2Schema.ATT_MININDEP);
			    			List<Double> maxIndep = row.getDoubleList(Model2Schema.ATT_MAXINDEP);
			    			List<String> litStr = row.getStringList(Model2Schema.ATT_LITM);
			    			List<Integer> litID = row.getIntList(Model2Schema.ATT_LITIDM);
				    		List<String> litEMStr = row.getStringList(Model2Schema.ATT_LITEM);
				    		List<Integer> litEMID = row.getIntList(Model2Schema.ATT_LITIDEM);

				    		// ab hier: different from ModelCatalogWriter
				    		//rowEstM2ID = row.getInt(Model2Schema.ATT_ESTMODELID);
				    		List<Double> paramValues = row.getDoubleList(Model2Schema.ATT_VALUE);
				    		List<Double> paramErrs = row.getDoubleList(Model2Schema.ATT_PARAMERR);
				    		Double rms = row.getDouble(Model2Schema.ATT_RMS);
				    		Double r2 = row.getDouble(Model2Schema.ATT_RSQUARED);
				    		List<String> varParMap = row.getStringList(Model2Schema.ATT_VARPARMAP);
	
							if (alreadyInsertedModel.containsKey(rowMcID)) {
								spm = alreadyInsertedModel.get(rowMcID);
					    		spm.setEstModelId(rowEstM2ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM2ID);
					    		doMinMax(spm, paramName, paramValues, paramErrs, minVal, maxVal, false);
					    		doMinMax(spm, indepVar, null, null, minIndep, maxIndep, true);
					    		doLit(spm, litStr, litID, false);
					    		doLit(spm, litEMStr, litEMID, true);
							}
							else {
					    		spm = new ParametricModel( modelName, formula, depVar, 2, rowMcID, rowEstM2ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM2ID );				    		
					    		doMinMax(spm, paramName, paramValues, paramErrs, minVal, maxVal, false);
					    		doMinMax(spm, indepVar, null, null, minIndep, maxIndep, true);
					    		doLit(spm, litStr, litID, false);
					    		doLit(spm, litEMStr, litEMID, true);
	
								String[] attrs = new String[] {Model2Schema.ATT_MODELID, Model2Schema.ATT_LITIDM};
								String[] dbTablenames = new String[] {"Modellkatalog", "Literatur"};
								
								checkIDs(true, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, row.getString(Model2Schema.ATT_DBUUID));
					    		db.insertM(spm, varParMap);
								checkIDs(false, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, row.getString(Model2Schema.ATT_DBUUID));

								alreadyInsertedModel.put(rowMcID, spm);
							}
				    		spm.setRms(rms);
				    		spm.setRsquared(r2);
						
				    		// ... vorher vielleicht sortieren nach IDs...
							if (lastSpm != null && lastRow.getInt(Model2Schema.ATT_ESTMODELID) - rowEstM2ID != 0) {
								// depVar and formula may have changed within workflow
								spm.setDepVar(depVar);
								spm.setFormula(formula);
								int lastEstM2ID = lastRow.getInt(Model2Schema.ATT_ESTMODELID);
								if (!alreadyInsertedEModel.containsKey(lastEstM2ID)) {

						    		String[] attrs = new String[] {Model2Schema.ATT_ESTMODELID, Model2Schema.ATT_LITIDEM};
									String[] dbTablenames = new String[] {"GeschaetzteModelle", "Literatur"};

									checkIDs(true, dbuuid, lastRow, lastSpm, foreignDbIds, attrs, dbTablenames, lastRow.getString(Model2Schema.ATT_DBUUID));
									Integer newSecEstID = db.insertEm(lastSpm, lastVarParMap);
									//System.err.println("Sec1\t" + lastEstM2ID + "\t" + newSecEstID);
									checkIDs(false, dbuuid, lastRow, lastSpm, foreignDbIds, attrs, dbTablenames, lastRow.getString(Model2Schema.ATT_DBUUID));
									if (newSecEstID != null) {
										//spm.setEstModelId(newSecID);
							    		alreadyInsertedEModel.put(lastEstM2ID, lastSpm);
										db.insertEm2(newSecEstID, primEstIDs);
										primEstIDs = new ArrayList<Integer>();												
									}
								}
							}
							primEstIDs.add(newPrimEstID);
							lastSpm = spm.clone();
							lastVarParMap = varParMap;
							lastRow = row;
						}
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
		if (model2Conform && lastSpm != null) {
			if (M2Writable) {
				if (!alreadyInsertedEModel.containsKey(lastSpm.getEstModelId())) { // lastSpm.getEstModelId() rowEstM2ID
					String[] attrs = new String[] {Model2Schema.ATT_ESTMODELID, Model2Schema.ATT_LITIDEM};
					String[] dbTablenames = new String[] {"GeschaetzteModelle", "Literatur"};

					checkIDs(true, dbuuid, lastRow, lastSpm, foreignDbIds, attrs, dbTablenames, lastRow.getString(Model2Schema.ATT_DBUUID));
					Integer newSecEstID = db.insertEm(lastSpm, lastVarParMap);
					//System.err.println("Sec2\t" + rowEstM2ID + "\t" + newSecEstID);
					checkIDs(false, dbuuid, lastRow, lastSpm, foreignDbIds, attrs, dbTablenames, lastRow.getString(Model2Schema.ATT_DBUUID));

					if (newSecEstID != null) {
						db.insertEm2(newSecEstID, primEstIDs);						
					}
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
    private void checkIDs(boolean before, String dbuuid, KnimeTuple row, ParametricModel pm, HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds,
    		String[] schemaAttr, String[] dbTablename, String rowuuid) throws PmmException {
		if (rowuuid != null && !rowuuid.equals(dbuuid)) {
			if (!foreignDbIds.containsKey(dbuuid)) foreignDbIds.put(dbuuid, new HashMap<String, HashMap<Integer, Integer>>());
			HashMap<String, HashMap<Integer, Integer>> d = foreignDbIds.get(dbuuid);
			
			for (int i=0;i<schemaAttr.length;i++) {
				if (!d.containsKey(dbTablename[i])) d.put(dbTablename[i], new HashMap<Integer, Integer>());
				HashMap<Integer, Integer> foreignDbIdsTable = d.get(dbTablename[i]);
		    	if (dbTablename[i].equals("Literatur")) { // hasList
		        	boolean est = schemaAttr[i].equals(Model1Schema.ATT_LITIDEM) || schemaAttr[i].equals(Model2Schema.ATT_LITIDEM);
		        	LinkedList<LiteratureItem> lili;
		        	if (est) lili = pm.getEstModelLit();
		        	else lili = pm.getModelLit();
		    		List<Integer> keys = row.getIntList(schemaAttr[i]);
		    		if (keys != null) {
			    		int ii=0;
			        	for (LiteratureItem li : lili) {
			        		Integer key = keys.get(ii);
			        		if (key != null && foreignDbIdsTable.containsKey(key)) {
			        			if (before) li.setID(foreignDbIdsTable.get(key));
			        			else if (foreignDbIdsTable.get(key) != li.getId()) {
			        				System.err.println("checkIDs ... shouldn't happen");
			        			}
			        		}
			        		else {
			        			if (before) {
			        				li.setID(MathUtilities.getRandomNegativeInt());
			        			}
			        			else foreignDbIdsTable.put(key, li.getId());
			        		}
			        		ii++;
			        		//row.setValue(attr, foreignDbIdsTable.get(key));
			        	}
		    		}
		    	}
		    	else {
		        	boolean est = schemaAttr[i].equals(Model1Schema.ATT_ESTMODELID) || schemaAttr[i].equals(Model2Schema.ATT_ESTMODELID);
		        	Integer key = row.getInt(schemaAttr[i]);
		        	if (key != null) {
			        	int id = pm.getModelId();
			        	if (est) id = pm.getEstModelId();
			    		if (foreignDbIdsTable.containsKey(key)) {
			    			if (before) {
			    				if (est) pm.setEstModelId(foreignDbIdsTable.get(key));
			    				else pm.setModelId(foreignDbIdsTable.get(key));
			    			}
			    			else if (foreignDbIdsTable.get(key) != id) {
			    				System.err.println("checkIDs ... shouldn't happen");
			    			}
			    		}
			    		else {
			    			if (before) {
			    				if (est) pm.setEstModelId(MathUtilities.getRandomNegativeInt());
			    				else pm.setModelId(MathUtilities.getRandomNegativeInt());
			    			}
			    			else foreignDbIdsTable.put(key, id);
			    		}
		        	}
		    	}
			}
		}    	
    }
    private void checkIDs(boolean before, String dbuuid, KnimeTuple row, KnimeTuple ts, HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds,
    		String[] schemaAttr, String[] dbTablename) throws PmmException {
		String rowuuid = row.getString(TimeSeriesSchema.ATT_DBUUID);
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
    	if (type == KnimeAttribute.TYPE_COMMASEP_INT || type == KnimeAttribute.TYPE_COMMASEP_DOUBLE
    			|| type == KnimeAttribute.TYPE_COMMASEP_STRING || type == KnimeAttribute.TYPE_MAP) { // hasList
    		List<Integer> keys = row.getIntList(attr);
    		if (keys != null) {
        		int i=0;
        		if (before) schemaTuple.setCell(attr, CellIO.createMissingCell());
            	for (Integer key : keys) {
            		if (key != null && foreignDbIds.containsKey(key)) {
            			if (before) schemaTuple.addValue(attr, foreignDbIds.get(key));
            			else if (foreignDbIds.get(key) != schemaTuple.getIntList(attr).get(i)) {
            				System.err.println("fillNewIDsIntoForeign ... shouldn't happen");
            			}
            		}
            		else {
            			if (before) schemaTuple.addValue(attr, MathUtilities.getRandomNegativeInt());
            			else foreignDbIds.put(key, schemaTuple.getIntList(attr).get(i));
            		}
            		i++;
            		//row.setValue(attr, foreignDbIds.get(key));
            	}
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

    private void doLit(final ParametricModel pm, final List<String> litStr,
    		final List<Integer> litID, final boolean isEstimated) {
    	if (isEstimated) pm.removeEstModelLits();
    	else pm.removeModelLits();
    	if (litID != null) {
    		for (int i=0;i<litID.size();i++) {
    			String author = "";
    			int year = 0;
    			if (litStr.size() == litID.size()) {
    				StringTokenizer tok = new StringTokenizer(litStr.get(i), "_");
    				if (tok.hasMoreTokens()) {
    					author = tok.nextToken();
    				}
    				if (tok.hasMoreTokens()) {
    					try {
    						year = Integer.parseInt(tok.nextToken());	    						
    					}
    					catch (Exception e) {}
    				}
    			}
    			if (isEstimated) {
    				pm.addEstModelLit(author, year, litID.get(i));
    			} else {
    				pm.addModelLit(author, year, litID.get(i));
    			}
    		}    	
    	}
    }
    private void doMinMax(final ParametricModel pm, final List<String> paramName, final List<Double> paramValues, final List<Double> paramErrs, final List<Double> minVal,
    		final List<Double> maxVal, final boolean isIndep) {
		for (int i=0;i<paramName.size();i++) {
			Double minInD = null;
			Double maxInD = null;
    		if (minVal != null && minVal.size() == paramName.size()) {
    			minInD = minVal.get(i);
    		}
    		if (maxVal != null && maxVal.size() == paramName.size()) {
    			maxInD = maxVal.get(i);
    		}
    		if (isIndep) {
    			pm.removeIndepVar(paramName.get(i));
				pm.addIndepVar(paramName.get(i), minInD, maxInD);
			} else {
    			pm.removeParam(paramName.get(i));
				pm.addParam(paramName.get(i), paramValues.get(i) == null ? Double.NaN : paramValues.get(i), paramErrs.get(i) == null ? Double.NaN : paramErrs.get(i), minInD, maxInD);
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

