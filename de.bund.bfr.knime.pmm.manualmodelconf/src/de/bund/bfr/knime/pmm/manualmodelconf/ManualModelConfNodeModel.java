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
import java.util.LinkedList;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
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
	static final String CFGKEY_COMMENT = "Comment";
	static final String CFGKEY_TEMPERATURE = "Temperature";
	static final String CFGKEY_PH = "pH";
	static final String CFGKEY_AW = "aw";

	protected static final String PARAM_XMLSTRING = "xmlString";
		
	private String agent;
	private String matrix;
	private String comment;
	private Double temperature;
	private Double ph;
	private Double waterActivity;

	private PmmXmlDoc doc = null;

	/**
     * Constructor for the node model.
     */
    protected ManualModelConfNodeModel() {
    
        super( 0, 1 );
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute( final BufferedDataTable[] inData,
            final ExecutionContext exec ) throws Exception {
    	if (doc != null) {
        	String indepString;
        	LinkedList<String> indepVarSet;
        	
        	//System.out.println( doc.toXmlString() );
        	
        	KnimeSchema ts = new TimeSeriesSchema();
        	KnimeSchema m1 = new Model1Schema();
        	KnimeSchema tsm1 = KnimeSchema.merge(ts, m1);
        	KnimeTuple tuple1 = null;
        	List<KnimeTuple> rowSec = new ArrayList<KnimeTuple>();
        	for (PmmXmlElementConvertable el : doc.getModelSet()) {
        		
        		if( !( el instanceof ParametricModel ) ) {
    				continue;
    			}
        		
        		ParametricModel model = ( ParametricModel )el;
        		
    			if (model.getLevel() == 1) { // can occur only once
    				tuple1 = model.getKnimeTuple();
    				PmmTimeSeries tstuple = new PmmTimeSeries();
    				tstuple.setValue(TimeSeriesSchema.ATT_AGENTDETAIL, agent);
    				tstuple.setValue(TimeSeriesSchema.ATT_MATRIXDETAIL, matrix);
    				tstuple.setValue(TimeSeriesSchema.ATT_COMMENT, comment);
    				tstuple.setValue(TimeSeriesSchema.ATT_TEMPERATURE, temperature);
    				tstuple.setValue(TimeSeriesSchema.ATT_PH, ph);
    				tstuple.setValue(TimeSeriesSchema.ATT_WATERACTIVITY, waterActivity);
    				tuple1 = KnimeTuple.merge(tsm1, tstuple, tuple1);
    			}
    			else {
    	    		// SecondaryModel
    				if (model.getIndepVarSet().size() > 0) {
    		    		rowSec.add(model.getKnimeTuple());					
    				}	    		
    			}

        	}
        	if (tuple1 != null) {
            	KnimeSchema ks = getSchema();
            	BufferedDataContainer buf = exec.createDataContainer(ks.createSpec());
            	if (rowSec.size() > 0) {
            		for (int i=0;i<rowSec.size();i++) {
                		buf.addRowToTable( new DefaultRow( String.valueOf(i), KnimeTuple.merge(ks, tuple1, rowSec.get(i)) ) );    		
            		}
            	}
            	else { // nur TSM1 generieren
            		buf.addRowToTable(new DefaultRow( String.valueOf( 0 ), tuple1 ));
            	}

        		// close table buffer
            	buf.close();
            	/*
            	BufferedDataContainer buf2 = exec.createDataContainer( createXmlSpec() );
            	buf2.addRowToTable( new DefaultRow( "0", new DataCell[] { new StringCell( xmlString ) } ) );
            	buf2.close();
            	*/
                return new BufferedDataTable[]{ buf.getTable()}; // , buf2.getTable() 
        	}
        	else {
                return null;    		
        	}
    	}
    	else {
    		return null;
    	}
    }

    private boolean hasSecondary() {
    	if (doc != null) {
        	for (PmmXmlElementConvertable el : doc.getModelSet()) {        		
        		if( !( el instanceof ParametricModel ) ) {
    				continue;
    			}
        		
        		ParametricModel model = ( ParametricModel )el;
        		if (model.getLevel() == 2) {
					return true;
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
        return new DataTableSpec[] { getSchema().createSpec() }; // , createXmlSpec() 
    }
    private KnimeSchema getSchema() {
    	KnimeSchema ks = null;
		try {
	    	if (hasSecondary()) {
	    		ks = KnimeSchema.merge(new TimeSeriesSchema(), KnimeSchema.merge(new Model1Schema(), new Model2Schema()));
	    	}
	    	else {
	    		ks = KnimeSchema.merge(new TimeSeriesSchema(), new Model1Schema());
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
			settings.addString( PARAM_XMLSTRING, doc.toXmlString() );
		}
    	
    	// TimeSeries
		if (agent != null) {
			settings.addString(CFGKEY_AGENT, agent);
		}

		if (matrix != null) {
			settings.addString(CFGKEY_MATRIX, matrix);
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom( final NodeSettingsRO settings )
            throws InvalidSettingsException {
    	// Modelle
    	try {
			if (settings.containsKey(PARAM_XMLSTRING)) {
				doc = new PmmXmlDoc(settings.getString(PARAM_XMLSTRING));
			}
			else {
				
			}
		}
    	catch (Exception e1) {
			e1.printStackTrace();
    		throw new InvalidSettingsException("Invalid model parameters");
		}
    	
    	// TimeSeries
		try {
			agent = settings.getString(CFGKEY_AGENT);
		}
		catch (InvalidSettingsException e) {
			agent = null;
		}

		try {
			matrix = settings.getString(CFGKEY_MATRIX);
		}
		catch (InvalidSettingsException e) {
			matrix = null;
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
    
    
    private DataTableSpec createXmlSpec() {
    	
    	DataColumnSpec[] spec;
    	
    	spec = new DataColumnSpec[ 1 ];
    	spec[ 0 ] = new DataColumnSpecCreator( "XmlString", StringCell.TYPE ).createSpec();
    	
    	return new DataTableSpec( spec );
    }

}

