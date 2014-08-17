/*******************************************************************************
 * PMM-Lab © 2012-2014, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012-2014, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Christian Thöns (BfR)
 * Matthias Filter (BfR)
 * Armin A. Weiser (BfR)
 * Alexander Falenski (BfR)
 * Jörgen Brandt (BfR)
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
package de.bund.bfr.knime.pmm.manualmodelconf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of ManualModelConf.
 * 
 *
 * @author ManualModelConf
 */
public class ManualModelConfNodeModel extends NodeModel {
	
	protected static final String PARAM_XMLSTRING = "xmlString";
	protected static final String PARAM_TSXMLSTRING = "tsXmlString";
		
	private PmmXmlDoc doc = null;
	private PmmXmlDoc docTS = null;
	
	private boolean formulaCreator;
	
	/**
     * Constructor for the node model.
     */
    protected ManualModelConfNodeModel() {    
        this(false, false);        
    }
    protected ManualModelConfNodeModel(boolean hasEditFeature, boolean formulaCreator) {    
        super(hasEditFeature ? 1 : 0, 1);
        this.formulaCreator = formulaCreator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	if (doc != null) {        	
        	KnimeSchema ks = getSchema();
        	BufferedDataContainer buf = exec.createDataContainer(ks.createSpec());

        	KnimeTuple tupleM1 = null;
        	List<KnimeTuple> rowSec = new ArrayList<>();
        	PmmTimeSeries tstuple = new PmmTimeSeries();
        	for (PmmXmlElementConvertable el : doc.getElementSet()) {
        		if (el instanceof ParametricModel) {
	        		ParametricModel model = (ParametricModel) el;	 
	    			if (model.getLevel() == 1) {
	    				if (model.getIndependent().size() > 0) {
	    					if (tupleM1 != null) doBuf(tupleM1, tstuple, rowSec, buf, ks);
	    					tupleM1 = model.getKnimeTuple();
	    					tupleM1.setValue(Model1Schema.ATT_DATABASEWRITABLE, 1);
	    					rowSec = new ArrayList<>();
	    					tstuple = new PmmTimeSeries();
	    		        	if (!formulaCreator) {
	    		            	if (docTS != null) {
	    		                	for (PmmXmlElementConvertable ell : docTS.getElementSet()) {        		
	    		                		if (ell instanceof PmmTimeSeries) {
	    		                			PmmTimeSeries ts = (PmmTimeSeries) ell;
	    		                			if (ts.getCondId().intValue() == model.getCondId()) {
	    		                				tstuple = ts;
	    		                				break;
	    		                			}
	    		                		}
	    		                	}
	    		            	}        		
	    		        	}
	    				}
	    			}
	    			else {
	    	    		// SecondaryModel
	    				//if (model.getIndepVarSet().size() > 0) {
	    				if (model.getDepXml().getOrigName() == null) {
	    					model.getDepXml().setOrigName(model.getDepXml().getName());
	    				}
	    					KnimeTuple tupleM2 = model.getKnimeTuple();

	    					tupleM2.setValue(Model2Schema.ATT_DATABASEWRITABLE, 1);
	    					tupleM2.setValue(Model2Schema.ATT_DATABASEWRITABLE, 1);
	    		    		rowSec.add(tupleM2);					
	    				//}	    		
	    			}
        		}
        	}
			doBuf(tupleM1, tstuple, rowSec, buf, ks);

        	buf.close();
            return new BufferedDataTable[]{ buf.getTable()};
    	}
    	else {
    		return null;
    	}
    }
    private void doBuf(KnimeTuple tupleM1, PmmTimeSeries tstuple, List<KnimeTuple> rowSec, BufferedDataContainer buf, KnimeSchema ks) {
    	if (tupleM1 != null) {
    		// Set primary variable names to TimeSeriesSchema.TIME and TimeSeriesSchema.LOGC
    		PmmXmlDoc modelXml = tupleM1.getPmmXml(Model1Schema.ATT_MODELCATALOG);
    		String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
    		PmmXmlDoc depVar = tupleM1.getPmmXml(Model1Schema.ATT_DEPENDENT);
    		PmmXmlDoc indepVar = tupleM1.getPmmXml(Model1Schema.ATT_INDEPENDENT);        		
    		
    		if (depVar.size() == 1) {
    			formula = MathUtilities.replaceVariable(formula, ((DepXml) depVar.get(0)).getName(), AttributeUtilities.CONCENTRATION);
    			((DepXml) depVar.get(0)).setName(AttributeUtilities.CONCENTRATION);        			
    			((DepXml) depVar.get(0)).setOrigName(AttributeUtilities.CONCENTRATION);        			
    		}
    		
    		if (indepVar.size() == 1) {
    			formula = MathUtilities.replaceVariable(formula, ((IndepXml) indepVar.get(0)).getName(), AttributeUtilities.TIME);
    			((IndepXml) indepVar.get(0)).setName(AttributeUtilities.TIME);
    			((IndepXml) indepVar.get(0)).setOrigName(AttributeUtilities.TIME);
    		}
    		
    		((CatalogModelXml) modelXml.get(0)).setFormula(formula);
    		tupleM1.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
    		tupleM1.setValue(Model1Schema.ATT_DEPENDENT, depVar);
    		tupleM1.setValue(Model1Schema.ATT_INDEPENDENT, indepVar);
    		
    		if (!formulaCreator) {
            	KnimeSchema ts = new TimeSeriesSchema();
            	KnimeSchema m1 = new Model1Schema();
            	KnimeSchema tsm1 = KnimeSchema.merge(ts, m1);
    			tupleM1 = KnimeTuple.merge(tsm1, tstuple, tupleM1);
    		}
        	if (rowSec.size() > 0) {
        		for (int i=0;i<rowSec.size();i++) {
            		buf.addRowToTable(new DefaultRow(String.valueOf(buf.size()), KnimeTuple.merge(ks, tupleM1, rowSec.get(i))));    		
        		}
        	}
        	else { // nur TSM1 generieren
        		buf.addRowToTable(new DefaultRow(String.valueOf(buf.size()), tupleM1));
        	}
    	}
    	else if (rowSec.size() == 1) {
        	//KnimeTuple emptyTupleM1 = new KnimeTuple(new Model1Schema());
        	//buf.addRowToTable(new DefaultRow(String.valueOf(0), KnimeTuple.merge(ks, emptyTupleM1, rowSec.get(0))));    		
        	buf.addRowToTable(new DefaultRow(String.valueOf(0), rowSec.get(0)));    		
    	}
    	else {
    		if (!formulaCreator) buf.addRowToTable(new DefaultRow(String.valueOf(0), tstuple));    		
    	}    	
    }

    private boolean hasSecondary() {
    	if (doc != null) {
        	for (PmmXmlElementConvertable el : doc.getElementSet()) {        		
        		if (el instanceof ParametricModel) {
	        		ParametricModel model = (ParametricModel) el;
	        		if (model.getLevel() == 2) { //  && model.getIndepVarSet().size() > 0
						return true;
					}
        		}
        	}
    	}
    	return false;
    }
    private boolean hasPrimary() {
    	if (doc != null) {
        	for (PmmXmlElementConvertable el : doc.getElementSet()) {        		
        		if (el instanceof ParametricModel) {
	        		ParametricModel model = (ParametricModel) el;
	        		if (model.getLevel() == 1 && model.getIndependent().size() > 0) {
						return true;
					}
        		}
        	}
    	}
    	return false;
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
    	if (doc == null) {
			throw new InvalidSettingsException("No Settings available");
		}
    	KnimeSchema ks = getSchema();
        return new DataTableSpec[] {ks == null ? null : ks.createSpec()}; // , createXmlSpec() 
    }
    private KnimeSchema getSchema() {
    	KnimeSchema ks = null;
		try {
			boolean hp = hasPrimary();
			boolean hs = hasSecondary();
			if (hp && hs) {
				if (formulaCreator) ks = KnimeSchema.merge(new Model1Schema(), new Model2Schema());
				else ks = KnimeSchema.merge(new TimeSeriesSchema(), KnimeSchema.merge(new Model1Schema(), new Model2Schema()));
			}
	    	else if (hp) {
	    		if (formulaCreator) ks = new Model1Schema();
	    		else ks = KnimeSchema.merge(new TimeSeriesSchema(), new Model1Schema());
	    	}	
	    	else if (hs) {
	    		ks = new Model2Schema();
	    	}	
	    	else {
	    		if (formulaCreator) ks = null;
	    		else ks = new TimeSeriesSchema();
	    	}	
		}
		catch (PmmException e) {
			e.printStackTrace();
		}
		return ks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo( final NodeSettingsWO settings ) {
    	// Modelle
    	if (doc != null) {
    		String xmlStr = doc.toXmlString();
    		//System.err.println(xmlStr);
			settings.addString(PARAM_XMLSTRING, xmlStr);
		}
    	if (!formulaCreator) {
        	if (docTS != null) {
        		String tsXmlStr = docTS.toXmlString();
        		//System.err.println(xmlStr);
    			settings.addString(PARAM_TSXMLSTRING, tsXmlStr);
    		}
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom( final NodeSettingsRO settings )
            throws InvalidSettingsException {
    	// Modelle
    	try {
			if (settings.containsKey(PARAM_XMLSTRING)) {
				String xmlStr = settings.getString(PARAM_XMLSTRING);
	    		//System.err.println(xmlStr);
				doc = new PmmXmlDoc(xmlStr);
			}
		}
    	catch (Exception e1) {
			e1.printStackTrace();
    		throw new InvalidSettingsException("Invalid model parameters");
		}
    	try {
	    	if (!formulaCreator) {
	    		if (settings.containsKey(PARAM_TSXMLSTRING)) {
	    			String tsXmlStr = settings.getString(PARAM_TSXMLSTRING);
	        		docTS = new PmmXmlDoc(tsXmlStr);
	    		}
	    		else { // old version...
	            	// TimeSeries
	    			String CFGKEY_AGENT = "Agent";
	    			String CFGKEY_MATRIX = "Matrix";
	    			String CFGKEY_AGENTID = "AgentID";
	    			String CFGKEY_MATRIXID = "MatrixID";
	    			String CFGKEY_AGENTDETAIL = "AgentDetail";
	    			String CFGKEY_MATRIXDETAIL = "MatrixDetail";
	    			String CFGKEY_COMMENT = "Comment";
	    			String CFGKEY_TEMPERATURE = "Temperature";
	    			String CFGKEY_PH = "pH";
	    			String CFGKEY_AW = "aw";
	    			String agent, agentDetail, matrix, matrixDetail, comment;
	    			Integer agentID, matrixID;
	    			Double temperature, ph, waterActivity;
	    			
	    			settings.containsKey(CFGKEY_AGENT);
	        		try {agent = settings.getString(CFGKEY_AGENT);}
	        		catch (InvalidSettingsException e) {agent = null;}
	        		try {agentID = settings.getInt(CFGKEY_AGENTID);}
	        		catch (InvalidSettingsException e) {agentID = null;}
	        		try {agentDetail = settings.getString(CFGKEY_AGENTDETAIL);}
	        		catch (InvalidSettingsException e) {agentDetail = null;}
	        		try {matrix = settings.getString(CFGKEY_MATRIX);}
	        		catch (InvalidSettingsException e) {matrix = null;}
	        		try {matrixID = settings.getInt(CFGKEY_MATRIXID);}
	        		catch (InvalidSettingsException e) {matrixID = null;}
	        		try {matrixDetail = settings.getString(CFGKEY_MATRIXDETAIL);}
	        		catch (InvalidSettingsException e) {matrixDetail = null;}
	        		try {comment = settings.getString(CFGKEY_COMMENT);}
	        		catch (InvalidSettingsException e) {comment = null;}
	        		try {temperature = settings.getDouble(CFGKEY_TEMPERATURE);}
	        		catch (InvalidSettingsException e) {temperature = null;}
	        		try {ph = settings.getDouble(CFGKEY_PH);}
	        		catch (InvalidSettingsException e) {ph = null;}
	        		try {waterActivity = settings.getDouble(CFGKEY_AW);}
	        		catch (InvalidSettingsException e) {waterActivity = null;}	   
	        		
	    			PmmTimeSeries tstuple = new PmmTimeSeries();
	            		PmmXmlDoc matDoc = new PmmXmlDoc(); 
	    				MatrixXml mx = new MatrixXml(matrixID, matrix, matrixDetail);
	    				matDoc.add(mx);
	            		tstuple.setValue(TimeSeriesSchema.ATT_MATRIX, matDoc);
	            		PmmXmlDoc agtDoc = new PmmXmlDoc(); 
	    				AgentXml ax = new AgentXml(agentID, agent, agentDetail);
	    				agtDoc.add(ax);
	            		tstuple.setValue(TimeSeriesSchema.ATT_AGENT, agtDoc);
	        			PmmXmlDoc mdInfoDoc = new PmmXmlDoc();
	        			int ri = MathUtilities.getRandomNegativeInt();
	        			MdInfoXml mdix = new MdInfoXml(ri, "i"+ri, comment, null, null);
	        			mdInfoDoc.add(mdix);
	        			tstuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);
	        			tstuple.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID,AttributeUtilities.ATT_TEMPERATURE,AttributeUtilities.ATT_TEMPERATURE,temperature,null,"°C");
	        			tstuple.addMisc(AttributeUtilities.ATT_PH_ID,AttributeUtilities.ATT_PH,AttributeUtilities.ATT_PH,ph,null,null);
	        			tstuple.addMisc(AttributeUtilities.ATT_AW_ID,AttributeUtilities.ATT_AW,AttributeUtilities.ATT_AW,waterActivity,null,null);

	        		docTS = new PmmXmlDoc();
	        		docTS.add(tstuple);
	    		}
	    	}
		}
    	catch (Exception e1) {
			e1.printStackTrace();
			docTS.add(new PmmTimeSeries());
    		throw new InvalidSettingsException("Invalid model parameters");
		}
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
    
    /*
    private DataTableSpec createXmlSpec() {
    	
    	DataColumnSpec[] spec;
    	
    	spec = new DataColumnSpec[ 1 ];
    	spec[ 0 ] = new DataColumnSpecCreator( "XmlString", StringCell.TYPE ).createSpec();
    	
    	return new DataTableSpec( spec );
    }
*/
}

