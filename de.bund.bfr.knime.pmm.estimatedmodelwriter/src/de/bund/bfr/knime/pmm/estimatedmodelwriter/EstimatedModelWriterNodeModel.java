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
import de.bund.bfr.knime.pmm.common.DbConfigurationUi;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
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
    
	private static final int ID_KEY = 0;
	private static final int ID_VALUE = 1;
	private static final int ID_CONDITIONID = 2;
	private static final int ID_MODELID = 3;
	private static final int ID_RSS = 4;
	private static final int ID_R2 = 5;
	private static final int ID_FORMULA = 6;
	private static final int ID_MODELNAME = 7;
	private static final int ID_DEPVAR = 8;
	private static final int ID_INDEPVAR = 9;
	private static final int ID_LEVEL = 10;
	private static final int NCOL = 11;
	
	private static final int MODE_NOACTION = 0;
	private static final int MODE_FROMESTIMATE = 1;
	private static final int MODE_FROMLITERATURE = 2;
		
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
		
		List<Integer> primIDs = new ArrayList<Integer>();
		HashMap<ParametricModel, List<Integer>> secModels = new HashMap<ParametricModel, List<Integer>>();
		ParametricModel ppm, spm, lastPpm = null, lastSpm = null;
		
		int j = 0;
		HashMap<Integer, ParametricModel> alreadyInsertedModel = new HashMap<Integer, ParametricModel>();
		HashMap<Integer, ParametricModel> alreadyInsertedEModel = new HashMap<Integer, ParametricModel>();
		HashMap<Integer, PmmTimeSeries> alreadyInsertedTs = new HashMap<Integer, PmmTimeSeries>();
		while (reader.hasMoreElements()) {
    		exec.setProgress( ( double )j++/n );

			KnimeTuple row = reader.nextElement();
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

    		
    		// Modellkatalog
			if (alreadyInsertedModel.containsKey(modelId)) {
				ppm = alreadyInsertedModel.get(modelId);
	    		ppm.setEstModelId(estModelId == null ? -1 : estModelId);
				doMinMax(ppm, paramName, paramValues, paramErrs, minVal, maxVal, false);
	    		doMinMax(ppm, indepVar, null, null, minIndep, maxIndep, true);
	    		doLit(ppm, litStr, litID, false);
	    		doLit(ppm, litEMStr, litEMID, true);
			}
			else {
	    		ppm = new ParametricModel( modelName, formula, depVar, 1, modelId, estModelId == null ? -1 : estModelId);				
	    		doMinMax(ppm, paramName, paramValues, paramErrs, minVal, maxVal, false);
	    		doMinMax(ppm, indepVar, null, null, minIndep, maxIndep, true);
	    		doLit(ppm, litStr, litID, false);
	    		doLit(ppm, litEMStr, litEMID, true);
	    		
	    		int newMID = db.insertM( ppm );
	    		ppm.setModelId(newMID);
				alreadyInsertedModel.put(modelId, ppm);
			}
    		// paramErr still missing...
    		ppm.setRms(rms == null ? Double.NaN : rms);
    		ppm.setRsquared(r2 == null ? Double.NaN : r2);
    		
    		// TimeSeries
			PmmTimeSeries ts = new PmmTimeSeries(row);	
			int tsID = ts.getCondId();
			Integer newTsID = null;
			if (alreadyInsertedTs.containsKey(tsID)) {
				ts = alreadyInsertedTs.get(tsID);
				newTsID = ts.getCondId();
			}
			else {
				newTsID = db.insertTs( ts );
				ts.setCondId(newTsID);
				alreadyInsertedTs.put(tsID, ts);
			}
			
			if (newTsID != null) {
				ppm.setCondId(newTsID);
				Integer newPrimID = null;
				if (alreadyInsertedEModel.containsKey(estModelId)) {
					newPrimID = alreadyInsertedEModel.get(estModelId).getEstModelId();
				}
				else {
		    		newPrimID = db.insertEm( ppm, null );	
		    		ppm.setEstModelId(newPrimID);
		    		alreadyInsertedEModel.put(estModelId, ppm);
				}
				if (model2Conform && newPrimID != null) {
		    		modelId = row.getInt(Model2Schema.ATT_MODELID);
		    		modelName = row.getString(Model2Schema.ATT_MODELNAME);
		    		formula = row.getString(Model2Schema.ATT_FORMULA);
		    		depVar = row.getString(Model2Schema.ATT_DEPVAR);
		    		indepVar = row.getStringList(Model2Schema.ATT_INDEPVAR);
		    		paramName = row.getStringList(Model2Schema.ATT_PARAMNAME);
		    		if (paramName != null) {
			    		minVal = row.getDoubleList(Model2Schema.ATT_MINVALUE);
			    		maxVal = row.getDoubleList(Model2Schema.ATT_MAXVALUE);
			    		minIndep = row.getDoubleList(Model2Schema.ATT_MININDEP);
			    		maxIndep = row.getDoubleList(Model2Schema.ATT_MAXINDEP);
			    		litStr = row.getStringList(Model2Schema.ATT_LITM);
			    		litID = row.getIntList(Model2Schema.ATT_LITIDM);
			    		litEMStr = row.getStringList(Model2Schema.ATT_LITEM);
			    		litEMID = row.getIntList(Model2Schema.ATT_LITIDEM);
		
			    		// ab hier: different from ModelCatalogWriter
			    		estSecModelId = row.getInt(Model2Schema.ATT_ESTMODELID);
			    		paramValues = row.getDoubleList(Model2Schema.ATT_VALUE);
			    		paramErrs = row.getDoubleList(Model2Schema.ATT_PARAMERR);
			    		rms = row.getDouble(Model2Schema.ATT_RMS);
			    		r2 = row.getDouble(Model2Schema.ATT_RSQUARED);

						if (alreadyInsertedModel.containsKey(modelId)) {
							spm = alreadyInsertedModel.get(modelId);
				    		spm.setEstModelId(estSecModelId == null ? -1 : estSecModelId);
				    		doMinMax(spm, paramName, paramValues, paramErrs, minVal, maxVal, false);
				    		doMinMax(spm, indepVar, null, null, minIndep, maxIndep, true);
				    		doLit(spm, litStr, litID, false);
				    		doLit(spm, litEMStr, litEMID, true);
						}
						else {
				    		spm = new ParametricModel( modelName, formula, depVar, 2, modelId, estSecModelId == null ? -1 : estSecModelId );				    		
				    		doMinMax(spm, paramName, paramValues, paramErrs, minVal, maxVal, false);
				    		doMinMax(spm, indepVar, null, null, minIndep, maxIndep, true);
				    		doLit(spm, litStr, litID, false);
				    		doLit(spm, litEMStr, litEMID, true);

				    		int newSMID = db.insertM( spm );
				    		spm.setModelId(newSMID);
							alreadyInsertedModel.put(modelId, spm);
						}
			    		// paramErr still missing...
			    		spm.setRms(rms);
			    		spm.setRsquared(r2);
						
			    		// Überarbeiten!!! Hier nur noch auf IDs gehen, vorher vielleicht sortieren nach IDs...
						if (lastSpm != null && lastSpm.getRms() != rms && lastSpm.getRsquared() != r2) {
							if (!alreadyInsertedEModel.containsKey(estSecModelId)) {
								Integer newSecID = db.insertEm( lastSpm, lastPpm );
								spm.setEstModelId(newSecID);
					    		alreadyInsertedEModel.put(estSecModelId, spm);
								db.insertEm2(newSecID, primIDs);
								primIDs = new ArrayList<Integer>();		
							}
						}
						primIDs.add(newPrimID);
						lastSpm = spm;
		    		}
				}
				else {
					System.err.println("newPrimID: " + newPrimID);
				}
			}
			lastPpm = ppm;
		}
		if (model2Conform && lastSpm != null) {
			if (!alreadyInsertedEModel.containsKey(estSecModelId)) {
				Integer newSecID = db.insertEm( lastSpm, lastPpm );
				db.insertEm2(newSecID, primIDs);
			}
		}
    	db.close();
        return null;
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
    	
    	settings.addString( DbConfigurationUi.PARAM_FILENAME, filename );
    	settings.addString( DbConfigurationUi.PARAM_LOGIN, login );
    	settings.addString( DbConfigurationUi.PARAM_PASSWD, passwd );
    	settings.addBoolean( DbConfigurationUi.PARAM_OVERRIDE, override );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom( final NodeSettingsRO settings )
            throws InvalidSettingsException {
    	
    	filename = settings.getString( DbConfigurationUi.PARAM_FILENAME );
    	login = settings.getString( DbConfigurationUi.PARAM_LOGIN );
    	passwd = settings.getString( DbConfigurationUi.PARAM_PASSWD );
    	override = settings.getBoolean( DbConfigurationUi.PARAM_OVERRIDE );
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

