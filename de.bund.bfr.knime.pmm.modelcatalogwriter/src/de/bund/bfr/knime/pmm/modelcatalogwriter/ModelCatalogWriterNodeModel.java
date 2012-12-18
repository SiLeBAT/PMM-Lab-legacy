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
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;

/**
 * This is the model implementation of ModelCatalogWriter.
 * 
 *
 * @author Jorgen Brandt
 */
public class ModelCatalogWriterNodeModel extends NodeModel {
    
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
			db = new Bfrdb(DBKernel.getLocalConn(true));
		}
    	db.getConnection().setReadOnly(false);
    	
    	int n = inData[ 0 ].getRowCount();
    	
		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		boolean model1Conform = inSchema.conforms(new Model1Schema());
		boolean model2Conform = inSchema.conforms(new Model2Schema());
		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds = new HashMap<String, HashMap<String, HashMap<Integer, Integer>>>();
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
    	String dbuuid = db.getDBUUID();

		int j = 0;
		List<Integer> alreadySaved = new ArrayList<Integer>();
		while (reader.hasMoreElements()) {
    		exec.setProgress( ( double )j++/n );
    		
			KnimeTuple row = reader.nextElement();
			if (model1Conform) {
				Integer rowMcID = row.getInt(Model1Schema.ATT_MODELID);
				if (rowMcID != null && !alreadySaved.contains(rowMcID)) {
					alreadySaved.add(rowMcID);
		    		String modelName = row.getString(Model1Schema.ATT_MODELNAME);
		    		String formula = row.getString(Model1Schema.ATT_FORMULA);
					PmmXmlDoc depXml = row.getPmmXml(Model1Schema.ATT_DEPENDENT);
					DepXml dx = (DepXml) depXml.getElementSet().get(0);
					PmmXmlDoc paramXml = row.getPmmXml(Model1Schema.ATT_PARAMETER);
					PmmXmlDoc indepXml = row.getPmmXml(Model1Schema.ATT_INDEPENDENT);

					PmmXmlDoc mLitXmlDoc = row.getPmmXml(Model1Schema.ATT_MLIT);
					PmmXmlDoc emLitXmlDoc = row.getPmmXml(Model1Schema.ATT_EMLIT);

		    		ParametricModel pm = new ParametricModel(modelName, formula, dx, 1, rowMcID);
		    		pm.setFormula(pm.revertFormula());
		    		pm.setParameter(paramXml);
		    		pm.setIndependent(indepXml);
		    		pm.setMLit(mLitXmlDoc);
		    		pm.setEstLit(emLitXmlDoc);

					String[] attrs = new String[] {Model1Schema.ATT_MODELID, Model1Schema.ATT_MLIT};
					String[] dbTablenames = new String[] {"Modellkatalog", "Literatur"};
					
					checkIDs(true, dbuuid, row, pm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));
					db.insertM(pm);
					checkIDs(false, dbuuid, row, pm, foreignDbIds, attrs, dbTablenames, row.getString(Model1Schema.ATT_DBUUID));
				}
			}
			if (model2Conform) {
	    		Integer rowMcID = row.getInt(Model2Schema.ATT_MODELID);
				if (rowMcID != null && !alreadySaved.contains(rowMcID)) {
					alreadySaved.add(rowMcID);
		    		String modelName = row.getString(Model2Schema.ATT_MODELNAME);
		    		String formula = row.getString(Model2Schema.ATT_FORMULA);
					PmmXmlDoc depXml = row.getPmmXml(Model2Schema.ATT_DEPENDENT);
					DepXml dx = (DepXml) depXml.getElementSet().get(0);

						PmmXmlDoc paramXml = row.getPmmXml(Model2Schema.ATT_PARAMETER);
						PmmXmlDoc indepXml = row.getPmmXml(Model2Schema.ATT_INDEPENDENT);

						PmmXmlDoc mLitXmlDoc = row.getPmmXml(Model2Schema.ATT_MLIT);
						PmmXmlDoc emLitXmlDoc = row.getPmmXml(Model2Schema.ATT_EMLIT);

			    		ParametricModel pm = new ParametricModel(modelName, formula, dx, 2, rowMcID);
			    		pm.setFormula(pm.revertFormula());
			    		pm.setParameter(paramXml);
			    		pm.setIndependent(indepXml);
			    		pm.setMLit(mLitXmlDoc);
			    		pm.setEstLit(emLitXmlDoc);
			    		
						String[] attrs = new String[] {Model2Schema.ATT_MODELID, Model2Schema.ATT_MLIT};
						String[] dbTablenames = new String[] {"Modellkatalog", "Literatur"};
						
						checkIDs(true, dbuuid, row, pm, foreignDbIds, attrs, dbTablenames, row.getString(Model2Schema.ATT_DBUUID));
			    		db.insertM(pm);
						checkIDs(false, dbuuid, row, pm, foreignDbIds, attrs, dbTablenames, row.getString(Model2Schema.ATT_DBUUID));
		    		//}
	    		}
			}
		}
    	
    	db.getConnection().setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
    	db.close();
        return null;
    }

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
    			PmmXmlDoc fromToXmlDB = pm.getModelLit();
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
    	else { // Modellkatalog
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

