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
	
	static final String CFGKEY_AGENT = "Agent";
	static final String CFGKEY_MATRIX = "Matrix";
	static final String CFGKEY_AGENTID = "AgentID";
	static final String CFGKEY_MATRIXID = "MatrixID";
	static final String CFGKEY_AGENTDETAIL = "AgentDetail";
	static final String CFGKEY_MATRIXDETAIL = "MatrixDetail";
	static final String CFGKEY_COMMENT = "Comment";
	static final String CFGKEY_TEMPERATURE = "Temperature";
	static final String CFGKEY_PH = "pH";
	static final String CFGKEY_AW = "aw";

	protected static final String PARAM_XMLSTRING = "xmlString";
		
	private String agent;
	private String matrix;
	private String agentDetail;
	private String matrixDetail;
	private Integer agentID;
	private Integer matrixID;
	private String comment;
	private Double temperature;
	private Double ph;
	private Double waterActivity;
	
	private PmmXmlDoc doc = null;
	
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
        	KnimeTuple tupleM1 = null;
			PmmTimeSeries tstuple = new PmmTimeSeries();
        	if (!formulaCreator) {
        		
        		PmmXmlDoc matDoc = new PmmXmlDoc(); 
				MatrixXml mx = new MatrixXml(matrixID, matrix, matrixDetail);
				matDoc.add(mx);
        		tstuple.setValue(TimeSeriesSchema.ATT_MATRIX, matDoc);
        		PmmXmlDoc agtDoc = new PmmXmlDoc(); 
				AgentXml ax = new AgentXml(agentID, agent, agentDetail);
				agtDoc.add(ax);
        		tstuple.setValue(TimeSeriesSchema.ATT_AGENT, agtDoc);
        		/*
    			tstuple.setValue(TimeSeriesSchema.ATT_AGENTDETAIL, agentDetail);
    			tstuple.setValue(TimeSeriesSchema.ATT_MATRIXDETAIL, matrixDetail);
    			tstuple.setValue(TimeSeriesSchema.ATT_AGENTNAME, agent);
    			tstuple.setValue(TimeSeriesSchema.ATT_MATRIXNAME, matrix);
    			tstuple.setValue(TimeSeriesSchema.ATT_AGENTID, agentID);
    			tstuple.setValue(TimeSeriesSchema.ATT_MATRIXID, matrixID);
    			*/
    			tstuple.setValue(TimeSeriesSchema.ATT_COMMENT, comment);
    			tstuple.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID,AttributeUtilities.ATT_TEMPERATURE,AttributeUtilities.ATT_TEMPERATURE,temperature,"°C");
    			tstuple.addMisc(AttributeUtilities.ATT_PH_ID,AttributeUtilities.ATT_PH,AttributeUtilities.ATT_PH,ph,null);
    			tstuple.addMisc(AttributeUtilities.ATT_AW_ID,AttributeUtilities.ATT_WATERACTIVITY,AttributeUtilities.ATT_WATERACTIVITY,waterActivity,null);
        	}
        	List<KnimeTuple> rowSec = new ArrayList<KnimeTuple>();
        	for (PmmXmlElementConvertable el : doc.getElementSet()) {      		
        		if (el instanceof ParametricModel) {        		
	        		ParametricModel model = (ParametricModel) el;	        		
	    			if (model.getLevel() == 1) { // can occur only once
	    				if (model.getIndependent().size() > 0) {
	    					tupleM1 = model.getKnimeTuple();
	    					tupleM1.setValue(Model1Schema.ATT_DATABASEWRITABLE, 1);
	    				}
	    			}
	    			else {
	    	    		// SecondaryModel
	    				//if (model.getIndepVarSet().size() > 0) {
	    					KnimeTuple tupleM2 = model.getKnimeTuple();
	    					tupleM2.setValue(Model2Schema.ATT_DATABASEWRITABLE, 1);
	    		    		rowSec.add(tupleM2);					
	    				//}	    		
	    			}
        		}
        	}
        	KnimeSchema ks = getSchema();
        	BufferedDataContainer buf = exec.createDataContainer(ks.createSpec());

        	if (tupleM1 != null) {
        		// Set primary variable names to TimeSeriesSchema.TIME and TimeSeriesSchema.LOGC
        		PmmXmlDoc modelXml = tupleM1.getPmmXml(Model1Schema.ATT_MODELCATALOG);
        		String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
        		PmmXmlDoc depVar = tupleM1.getPmmXml(Model1Schema.ATT_DEPENDENT);
        		PmmXmlDoc indepVar = tupleM1.getPmmXml(Model1Schema.ATT_INDEPENDENT);        		
        		
        		if (depVar.size() == 1) {
        			formula = MathUtilities.replaceVariable(formula, ((DepXml) depVar.get(0)).getName(), TimeSeriesSchema.LOGC);
        			((DepXml) depVar.get(0)).setName(TimeSeriesSchema.LOGC);        			
        		}
        		
        		if (indepVar.size() == 1) {
        			formula = MathUtilities.replaceVariable(formula, ((IndepXml) indepVar.get(0)).getName(), TimeSeriesSchema.TIME);
        			((IndepXml) indepVar.get(0)).setName(TimeSeriesSchema.TIME);
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
                		buf.addRowToTable(new DefaultRow(String.valueOf(i), KnimeTuple.merge(ks, tupleM1, rowSec.get(i))));    		
            		}
            	}
            	else { // nur TSM1 generieren
            		buf.addRowToTable(new DefaultRow(String.valueOf( 0 ), tupleM1));
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

        	buf.close();
            return new BufferedDataTable[]{ buf.getTable()};
    	}
    	else {
    		return null;
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
        return new DataTableSpec[] {getSchema().createSpec()}; // , createXmlSpec() 
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
        	// TimeSeries
    		if (agent != null) {
    			settings.addString(CFGKEY_AGENT, agent);
    		}
    		if (agentID != null) {
    			settings.addInt(CFGKEY_AGENTID, agentID);
    		}
    		if (agentDetail != null) {
    			settings.addString(CFGKEY_AGENTDETAIL, agentDetail);
    		}

    		if (matrix != null) {
    			settings.addString(CFGKEY_MATRIX, matrix);
    		}
    		if (matrixID != null) {
    			settings.addInt(CFGKEY_MATRIXID, matrixID);
    		}
    		if (matrixDetail != null) {
    			settings.addString(CFGKEY_MATRIXDETAIL, matrixDetail);
    		}

    		if (comment != null) {
    			settings.addString(CFGKEY_COMMENT, comment);
    		}

    		if (temperature != null) {
    			settings.addDouble(CFGKEY_TEMPERATURE, temperature);
    		}

    		if (ph != null) {
    			settings.addDouble(CFGKEY_PH, ph);
    		}

    		if (waterActivity != null) {
    			settings.addDouble(CFGKEY_AW, waterActivity);
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
    	if (!formulaCreator) {
        	// TimeSeries
    		try {
    			agent = settings.getString(CFGKEY_AGENT);
    		}
    		catch (InvalidSettingsException e) {
    			agent = null;
    		}
    		try {
    			agentID = settings.getInt(CFGKEY_AGENTID);
    		}
    		catch (InvalidSettingsException e) {
    			agentID = null;
    		}
    		try {
    			agentDetail = settings.getString(CFGKEY_AGENTDETAIL);
    		}
    		catch (InvalidSettingsException e) {
    			agentDetail = null;
    		}

    		try {
    			matrix = settings.getString(CFGKEY_MATRIX);
    		}
    		catch (InvalidSettingsException e) {
    			matrix = null;
    		}
    		try {
    			matrixID = settings.getInt(CFGKEY_MATRIXID);
    		}
    		catch (InvalidSettingsException e) {
    			matrixID = null;
    		}
    		try {
    			matrixDetail = settings.getString(CFGKEY_MATRIXDETAIL);
    		}
    		catch (InvalidSettingsException e) {
    			matrixDetail = null;
    		}


    		try {
    			comment = settings.getString(CFGKEY_COMMENT);
    		}
    		catch (InvalidSettingsException e) {
    			comment = null;
    		}

    		try {
    			temperature = settings.getDouble(CFGKEY_TEMPERATURE);
    		}
    		catch (InvalidSettingsException e) {
    			temperature = null;
    		}

    		try {
    			ph = settings.getDouble(CFGKEY_PH);
    		}
    		catch (InvalidSettingsException e) {
    			ph = null;
    		}

    		try {
    			waterActivity = settings.getDouble(CFGKEY_AW);
    		}
    		catch (InvalidSettingsException e) {
    			waterActivity = null;
    		}
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

