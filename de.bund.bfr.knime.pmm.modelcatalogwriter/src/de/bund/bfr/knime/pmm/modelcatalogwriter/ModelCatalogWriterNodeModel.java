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
package de.bund.bfr.knime.pmm.modelcatalogwriter;

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
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 * This is the model implementation of ModelCatalogWriter.
 * 
 *
 * @author Jorgen Brandt
 */
public class ModelCatalogWriterNodeModel extends NodeModel {
    
	private String filename;
	private String login;
	private String passwd;
	private boolean override;

	
    /**
     * Constructor for the node model.
     */
    protected ModelCatalogWriterNodeModel() {
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
		boolean model1Conform = inSchema.conforms(new Model1Schema());
		boolean model2Conform = inSchema.conforms(new Model2Schema());
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
		HashMap<Integer, Integer> foreignDbMC1Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbME1Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitMC1Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitME1Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbMC2Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbME2Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitMC2Ids = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> foreignDbLitME2Ids = new HashMap<Integer, Integer>();
    	String dbuuid = db.getDBUUID();

		int j = 0;
		List<Integer> alreadySaved = new ArrayList<Integer>();
		while (reader.hasMoreElements()) {
    		exec.setProgress( ( double )j++/n );
    		
			KnimeTuple row = reader.nextElement();
			if (model1Conform) {
				String rowuuid = row.getString(Model1Schema.ATT_DBUUID);
				if (rowuuid != null && !rowuuid.equals(dbuuid)) {
					checkIDs(Model1Schema.ATT_MODELID, foreignDbMC1Ids, row, false);
					checkIDs(Model1Schema.ATT_ESTMODELID, foreignDbME1Ids, row, false);
					checkIDs(Model1Schema.ATT_LITIDM, foreignDbLitMC1Ids, row, true);
					checkIDs(Model1Schema.ATT_LITIDEM, foreignDbLitME1Ids, row, true);
				}
				Integer modelId = row.getInt(Model1Schema.ATT_MODELID);
				if (modelId != null && !alreadySaved.contains(modelId)) {
					alreadySaved.add(modelId);
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

		    		ParametricModel pm = new ParametricModel( modelName, formula, depVar, 1, modelId );
		    		
		    		doMinMax(pm, paramName, minVal, maxVal, false);
		    		doMinMax(pm, indepVar, minIndep, maxIndep, true);
		    		doLit(pm, litStr, litID, false);
		    		doLit(pm, litEMStr, litEMID, true);

		    		db.insertM( pm );
				}
			}
			if (model2Conform) {
				String rowuuid = row.getString(Model2Schema.ATT_DBUUID);
				if (rowuuid != null && !rowuuid.equals(dbuuid)) {
					checkIDs(Model2Schema.ATT_MODELID, foreignDbMC2Ids, row, false);
					checkIDs(Model2Schema.ATT_ESTMODELID, foreignDbME2Ids, row, false);
					checkIDs(Model2Schema.ATT_LITIDM, foreignDbLitMC2Ids, row, true);
					checkIDs(Model2Schema.ATT_LITIDEM, foreignDbLitME2Ids, row, true);
				}
	    		Integer modelId = row.getInt(Model2Schema.ATT_MODELID);
				if (modelId != null && !alreadySaved.contains(modelId)) {
					alreadySaved.add(modelId);
		    		String modelName = row.getString(Model2Schema.ATT_MODELNAME);
		    		String formula = row.getString(Model2Schema.ATT_FORMULA);
		    		String depVar = row.getString(Model2Schema.ATT_DEPVAR);
		    		List<String> indepVar = row.getStringList(Model2Schema.ATT_INDEPVAR);
		    		List<String> paramName = row.getStringList(Model2Schema.ATT_PARAMNAME);
		    		if (paramName != null) {
			    		List<Double> minVal = row.getDoubleList(Model2Schema.ATT_MINVALUE);
			    		List<Double> maxVal = row.getDoubleList(Model2Schema.ATT_MAXVALUE);
			    		List<Double> minIndep = row.getDoubleList(Model2Schema.ATT_MININDEP);
			    		List<Double> maxIndep = row.getDoubleList(Model2Schema.ATT_MAXINDEP);
			    		List<String> litStr = row.getStringList(Model2Schema.ATT_LITM);
			    		List<Integer> litID = row.getIntList(Model2Schema.ATT_LITIDM);
			    		List<String> litEMStr = row.getStringList(Model2Schema.ATT_LITEM);
			    		List<Integer> litEMID = row.getIntList(Model2Schema.ATT_LITIDEM);
		
			    		ParametricModel pm = new ParametricModel( modelName, formula, depVar, 2, modelId );
			    		
			    		doMinMax(pm, paramName, minVal, maxVal, false);
			    		doMinMax(pm, indepVar, minIndep, maxIndep, true);
			    		doLit(pm, litStr, litID, false);
			    		doLit(pm, litEMStr, litEMID, true);
			    		
			    		db.insertM( pm );
		    		}
	    		}
			}
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
    private void doMinMax(final ParametricModel pm, final List<String> paramName, final List<Double> minVal,
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
				pm.addParam(paramName.get(i), Double.NaN, Double.NaN, minInD, maxInD);
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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
    	getInSchema(inSpecs[0]);
		return null;
    }
    private KnimeSchema getInSchema(final DataTableSpec inSpec) throws InvalidSettingsException {
    	KnimeSchema result = null;
		String errorMsg = "Expected format: M1 or M2";
		KnimeSchema inSchema = new Model1Schema();
		try {
			if (inSchema.conforms(inSpec)) {
				result = inSchema;
			}
		}
		catch (PmmException e) {
		}
		inSchema = new Model2Schema();
		try {
			if (inSchema.conforms(inSpec)) {
				if (result == null) {
					result = inSchema;
				} else {
					result = KnimeSchema.merge(result, inSchema);
				}
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
    	settings.addString( DbConfigurationUi.PARAM_FILENAME, filename );
    	settings.addString( DbConfigurationUi.PARAM_LOGIN, login );
    	settings.addString( DbConfigurationUi.PARAM_PASSWD, passwd );
    	settings.addBoolean( DbConfigurationUi.PARAM_OVERRIDE, override );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
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

