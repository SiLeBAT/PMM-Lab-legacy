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
package de.bund.bfr.knime.pmm.combaseio;

import java.io.File;
import java.io.IOException;

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

import de.bund.bfr.knime.pmm.combaseio.lib.CombaseReader;

import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of CombaseReader.
 * 
 *
 * @author Jorgen Brandt
 */
public class CombaseReaderNodeModel extends NodeModel {
		
		
	protected static final String PARAM_FILENAME = "filename";
	protected static final String PARAM_SKIPEMPTY = "skipEmpty";

	private String filename;
    
    /**
     * Constructor for the node model.
     */
    protected CombaseReaderNodeModel() {
    	
        super( 0, 2 );
    	// super( 0, 1 );
        
        filename = "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	BufferedDataContainer buf, buf2;
    	CombaseReader reader;
    	PmmTimeSeries candidate;
    	int j;
    	PmmXmlDoc doc;
    	ParamXml paramXml;
    	KnimeSchema tsm1Schema;
    	KnimeTuple modelTuple;
    	
    	tsm1Schema = KnimeSchema.merge( new TimeSeriesSchema(), new Model1Schema() );

    	// initialize combase reader
    	reader = new CombaseReader( filename );
    	
    	// initialize table buffer
    	// buf = exec.createDataContainer( PmmTimeSeriesSchema.createSpec( new File( filename ).getName() ) );
    	buf = exec.createDataContainer( new TimeSeriesSchema().createSpec() );
    	buf2 = exec.createDataContainer( tsm1Schema.createSpec() );
    	
    	j = 0;
    	while( reader.hasMoreElements() ) {
    		
    		// fetch time series
    		candidate = reader.nextElement();
    		
    		if( candidate.isEmpty() ) {
    			PmmXmlDoc indepXML = new PmmXmlDoc();
    			
    			modelTuple = new KnimeTuple( new Model1Schema() );
    			indepXML.add(new IndepXml("t", null, null));
    			    			
    			modelTuple.setValue( Model1Schema.ATT_FORMULA, "LogC=LogC0+mumax*t" );
    			modelTuple.setValue( Model1Schema.ATT_PARAMNAME, "LocC0,mumax" );
    			modelTuple.setValue( Model1Schema.ATT_VALUE, "?,"+candidate.getMaximumRate() );
    			modelTuple.setValue( Model1Schema.ATT_INDEPENDENT, indepXML );
    			modelTuple.setValue( Model1Schema.ATT_DEPVAR, "LogC" );
    			modelTuple.setValue( Model1Schema.ATT_MODELID, MathUtilities.getRandomNegativeInt() );
    			modelTuple.setValue( Model1Schema.ATT_ESTMODELID, MathUtilities.getRandomNegativeInt() );
    			modelTuple.setValue( Model1Schema.ATT_MININDEP, "?" );
    			modelTuple.setValue( Model1Schema.ATT_MAXINDEP, "?" );
    			
    			doc = new PmmXmlDoc();
    			
    			paramXml = new ParamXml();
    			paramXml.setName( "LogC0" );
    			doc.add( paramXml );
    			
    			paramXml = new ParamXml();
    			paramXml.setName( "mumax" );
    			paramXml.setValue( candidate.getMaximumRate() );
    			doc.add( paramXml );
    			
    			modelTuple.setValue( Model1Schema.ATT_PARAMETER, doc );
    			
    			buf2.addRowToTable( new DefaultRow( String.valueOf( j++ ), KnimeTuple.merge( tsm1Schema, candidate, modelTuple ) ) );
    			
    			continue;
    		}
    		
    		// doc.add( candidate );
			buf.addRowToTable( new DefaultRow( String.valueOf( j++ ), candidate ) );
    	
    	}
    	reader.close();
    	buf.close();
    	buf2.close();
    	
    	/* buf2 = exec.createDataContainer( createXmlSpec() );
    	row = new StringCell[ 1 ];
    	row[ 0 ] = new StringCell( doc.toXmlString() );
    	
    	buf2.addRowToTable( new DefaultRow( "0", row ) );
    	buf2.close(); */
    	
        // return new BufferedDataTable[]{ buf.getTable(), buf2.getTable() };
    	return new BufferedDataTable[]{ buf.getTable(), buf2.getTable() };
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
    	
    	DataTableSpec[] ret;
    	
    	ret = null;

    	if( filename.isEmpty() )
    		throw new InvalidSettingsException( "Filename must be specified." );
    	
    	
        // return new DataTableSpec[] { PmmTimeSeriesSchema.createSpec( new File( filename ).getName() ), createXmlSpec() };
    	try {
			ret = new DataTableSpec[] {
				new TimeSeriesSchema().createSpec(),
				KnimeSchema.merge(  new TimeSeriesSchema(),
									new Model1Schema() ).createSpec() };
		} catch (PmmException e) {
			e.printStackTrace();
		}
		
		return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo( final NodeSettingsWO settings ) {

    	settings.addString( PARAM_FILENAME, filename );
    	// settings.addBoolean( PARAM_SKIPEMPTY, skipEmpty );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {

    	filename = settings.getString( PARAM_FILENAME );
    	// skipEmpty = settings.getBoolean( PARAM_SKIPEMPTY );
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
        
    protected static DataTableSpec createXmlSpec() {
    	
    	DataColumnSpec[] spec;
    	
    	spec = new DataColumnSpec[ 1 ];
    	// spec[ 0 ] = new DataColumnSpecCreator( "xmlString", XMLCell.TYPE ).createSpec();
    	spec[ 0 ] = new DataColumnSpecCreator( "xmlString", StringCell.TYPE ).createSpec();
    	
    	return new DataTableSpec( spec );
    }

}

