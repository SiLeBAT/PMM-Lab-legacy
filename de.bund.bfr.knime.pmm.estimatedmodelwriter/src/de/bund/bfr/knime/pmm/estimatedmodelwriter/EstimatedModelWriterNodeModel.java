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
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
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
		Integer estSecModelId = null;
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
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
    	String dbuuid = db.getDBUUID();
		
		List<Integer> primIDs = new ArrayList<Integer>();
		ParametricModel ppm = null, spm, lastSpm = null;
		List<String> lastVarParMap = null;
		
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
			String rowuuid = row.getString(TimeSeriesSchema.ATT_DBUUID);
			if (rowuuid != null && !rowuuid.equals(dbuuid)) {
				checkIDs(TimeSeriesSchema.ATT_CONDID, foreignDbTsIds, row, false);
				checkIDs(TimeSeriesSchema.ATT_MISCID, foreignDbMiscIds, row, true);
				checkIDs(TimeSeriesSchema.ATT_AGENTID, foreignDbAgentIds, row, false);
				checkIDs(TimeSeriesSchema.ATT_MATRIXID, foreignDbMatrixIds, row, false);
				checkIDs(TimeSeriesSchema.ATT_LITIDTS, foreignDbLitTsIds, row, true);
			}
			PmmTimeSeries ts = new PmmTimeSeries(row);	
			int tsID = ts.getCondId();
			Integer newTsID = null;
			if (alreadyInsertedTs.containsKey(tsID)) {
				ts = alreadyInsertedTs.get(tsID);
				newTsID = ts.getCondId();
			}
			else {
				newTsID = db.insertTs(ts);
				ts.setCondId(newTsID);
				alreadyInsertedTs.put(tsID, ts);
			}
			
			if (newTsID != null) {

				Integer newPrimID = null;
				Integer dw = row.getInt(Model1Schema.ATT_DATABASEWRITABLE);
				M1Writable = (dw != null && dw == 1);
				if (M1Writable) {
					rowuuid = row.getString(Model1Schema.ATT_DBUUID);
					if (rowuuid != null && !rowuuid.equals(dbuuid)) {
						checkIDs(Model1Schema.ATT_MODELID, foreignDbMC1Ids, row, false);
						checkIDs(Model1Schema.ATT_ESTMODELID, foreignDbME1Ids, row, false);
						checkIDs(Model1Schema.ATT_LITIDM, foreignDbLitMC1Ids, row, true);
						checkIDs(Model1Schema.ATT_LITIDEM, foreignDbLitME1Ids, row, true);
					}
					
					Integer modelId = row.getInt(Model1Schema.ATT_MODELID);
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
					Integer estModelId = row.getInt(Model1Schema.ATT_ESTMODELID);
		    		List<Double> paramValues = row.getDoubleList(Model1Schema.ATT_VALUE);
		    		List<Double> paramErrs = row.getDoubleList(Model1Schema.ATT_PARAMERR);
		    		Double rms = row.getDouble(Model1Schema.ATT_RMS);
		    		Double r2 = row.getDouble(Model1Schema.ATT_RSQUARED);
		    		List<String> varParMap = row.getStringList(Model1Schema.ATT_VARPARMAP);		
		    		
		    		// Modellkatalog
					if (alreadyInsertedModel.containsKey(modelId)) {
						ppm = alreadyInsertedModel.get(modelId);
			    		ppm.setEstModelId(estModelId == null ? MathUtilities.getRandomNegativeInt() : estModelId);
						doMinMax(ppm, paramName, paramValues, paramErrs, minVal, maxVal, false);
			    		doMinMax(ppm, indepVar, null, null, minIndep, maxIndep, true);
			    		doLit(ppm, litStr, litID, false);
			    		doLit(ppm, litEMStr, litEMID, true);
					}
					else {
			    		ppm = new ParametricModel( modelName, formula, depVar, 1, modelId, estModelId == null ? MathUtilities.getRandomNegativeInt() : estModelId);				
			    		doMinMax(ppm, paramName, paramValues, paramErrs, minVal, maxVal, false);
			    		doMinMax(ppm, indepVar, null, null, minIndep, maxIndep, true);
			    		doLit(ppm, litStr, litID, false);
			    		doLit(ppm, litEMStr, litEMID, true);
			    		
			    		int newMID = db.insertM(ppm, varParMap);
			    		ppm.setModelId(newMID);
						alreadyInsertedModel.put(modelId, ppm);
					}
		    		ppm.setRms(rms == null ? Double.NaN : rms);
		    		ppm.setRsquared(r2 == null ? Double.NaN : r2);
    		
					ppm.setCondId(newTsID);
					if (alreadyInsertedEModel.containsKey(estModelId)) {
						newPrimID = alreadyInsertedEModel.get(estModelId).getEstModelId();
					}
					else {
			    		newPrimID = db.insertEm(ppm, varParMap);
			    		if (newPrimID != null) {
				    		ppm.setEstModelId(newPrimID);
				    		alreadyInsertedEModel.put(estModelId, ppm);			    			
			    		}
					}
				}
				else {
					String text = "Estimated primary model (ID: " + row.getInt(Model1Schema.ATT_ESTMODELID) +
						") is not storable due to joining with unassociated kinetic data\n";
					if (warnings.indexOf(text) < 0)	warnings += text;
				}
				if (model2Conform && newPrimID != null) {
					dw = row.getInt(Model2Schema.ATT_DATABASEWRITABLE);
					M2Writable = (dw != null && dw == 1);
					estSecModelId = row.getInt(Model2Schema.ATT_ESTMODELID);
					if (M2Writable) {
						rowuuid = row.getString(Model2Schema.ATT_DBUUID);
						if (rowuuid != null && !rowuuid.equals(dbuuid)) {
							checkIDs(Model2Schema.ATT_MODELID, foreignDbMC2Ids, row, false);
							checkIDs(Model2Schema.ATT_ESTMODELID, foreignDbME2Ids, row, false);
							checkIDs(Model2Schema.ATT_LITIDM, foreignDbLitMC2Ids, row, true);
							checkIDs(Model2Schema.ATT_LITIDEM, foreignDbLitME2Ids, row, true);
						}

			    		Integer modelId = row.getInt(Model2Schema.ATT_MODELID);
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
				    		estSecModelId = row.getInt(Model2Schema.ATT_ESTMODELID);
				    		List<Double> paramValues = row.getDoubleList(Model2Schema.ATT_VALUE);
				    		List<Double> paramErrs = row.getDoubleList(Model2Schema.ATT_PARAMERR);
				    		Double rms = row.getDouble(Model2Schema.ATT_RMS);
				    		Double r2 = row.getDouble(Model2Schema.ATT_RSQUARED);
				    		List<String> varParMap = row.getStringList(Model2Schema.ATT_VARPARMAP);
	
							if (alreadyInsertedModel.containsKey(modelId)) {
								spm = alreadyInsertedModel.get(modelId);
					    		spm.setEstModelId(estSecModelId == null ? MathUtilities.getRandomNegativeInt() : estSecModelId);
					    		doMinMax(spm, paramName, paramValues, paramErrs, minVal, maxVal, false);
					    		doMinMax(spm, indepVar, null, null, minIndep, maxIndep, true);
					    		doLit(spm, litStr, litID, false);
					    		doLit(spm, litEMStr, litEMID, true);
							}
							else {
					    		spm = new ParametricModel( modelName, formula, depVar, 2, modelId, estSecModelId == null ? MathUtilities.getRandomNegativeInt() : estSecModelId );				    		
					    		doMinMax(spm, paramName, paramValues, paramErrs, minVal, maxVal, false);
					    		doMinMax(spm, indepVar, null, null, minIndep, maxIndep, true);
					    		doLit(spm, litStr, litID, false);
					    		doLit(spm, litEMStr, litEMID, true);
	
					    		int newSMID = db.insertM(spm, varParMap);
					    		spm.setModelId(newSMID);
								alreadyInsertedModel.put(modelId, spm);
							}
				    		spm.setRms(rms);
				    		spm.setRsquared(r2);
						
				    		// ... vorher vielleicht sortieren nach IDs...
							if (lastSpm != null && lastSpm.getEstModelId() != estSecModelId) {
								if (!alreadyInsertedEModel.containsKey(estSecModelId)) {
									Integer newSecID = db.insertEm(lastSpm, varParMap);
									if (newSecID != null) {
										spm.setEstModelId(newSecID);
							    		alreadyInsertedEModel.put(estSecModelId, spm);
										db.insertEm2(newSecID, primIDs);
										primIDs = new ArrayList<Integer>();												
									}
								}
							}
							primIDs.add(newPrimID);
							lastSpm = spm;
							lastVarParMap = varParMap;
						}
		    		}
					else {
						String text = "Estimated secondary model (ID: " + estSecModelId +
							") is not storable due to joining with unassociated primary model\n";
						if (warnings.indexOf(text) < 0)	warnings += text;
					}
				}
				else {
					//System.err.println("newPrimID: " + newPrimID);
				}
			}
		}
		if (model2Conform && lastSpm != null) {
			if (M2Writable) {
				if (!alreadyInsertedEModel.containsKey(estSecModelId)) {
					Integer newSecID = db.insertEm(lastSpm, lastVarParMap);
					if (newSecID != null) {
						db.insertEm2(newSecID, primIDs);						
					}
				}
			}
			else {
				String text = "Estimated secondary model (ID: " + estSecModelId +
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

    private void doLit(final ParametricModel pm, final List<String> litStr,
    		final List<Integer> litID, final boolean isEstimated) {
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
				pm.addIndepVar(paramName.get(i), minInD, maxInD);
			} else {
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

