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
package de.bund.bfr.knime.pmm.estimatedmodelreader;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Map;

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

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.DbIo;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.EstModelReaderUi;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 * This is the model implementation of EstimatedModelReader.
 * 
 *
 * @author Jorgen Brandt
 */
public class EstimatedModelReaderNodeModel extends NodeModel {
	
	static final String PARAM_FILENAME = "filename";
	static final String PARAM_LOGIN = "login";
	static final String PARAM_PASSWD = "passwd";
	static final String PARAM_OVERRIDE = "override";
	static final String PARAM_MATRIXENABLED = "matrixEnabled";
	static final String PARAM_AGENTENABLED = "agentEnabled";
	static final String PARAM_MATRIXSTRING = "matrixString";
	static final String PARAM_AGENTSTRING = "agentString";
	static final String PARAM_LEVEL = "level";
	static final String PARAM_MODELFILTERENABLED = "modelFilterEnabled";
	static final String PARAM_MODELLIST = "modelList";

	private String filename;
	private String login;
	private String passwd;
	private boolean override;
	private int level;
	private boolean modelFilterEnabled;
	private String modelList;
	private int qualityMode;
	private double qualityThresh;
	private boolean matrixEnabled;
	private boolean agentEnabled;
	private String matrixString;
	private String agentString;
	
	static final String PARAM_QUALITYMODE = "qualityFilterMode";
	static final String PARAM_QUALITYTHRESH = "qualityThreshold";
	
	/**
     * Constructor for the node model.
     */
    protected EstimatedModelReaderNodeModel() {
    	
        super( 0, 1 );
        
        filename = "";
        login = "";
        passwd = "";
        level = 1;
        modelFilterEnabled = false;
        modelList = "";
        qualityThresh = .8;
        qualityMode = EstModelReaderUi.MODE_OFF;
        agentString = "";
        agentEnabled = false;
        matrixString = "";
        matrixEnabled = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute( final BufferedDataTable[] inData,
            final ExecutionContext exec )throws Exception {

    	ResultSet result;
    	Bfrdb db;
    	BufferedDataContainer buf;
    	KnimeTuple tuple;
    	int i, j, n;
    	KnimeSchema schema;
    	String dbuuid;
    	String formula;
    	Map<String,String> varMap;
    	
        // fetch database connection
        db = null;
    	if( override ) {
			db = new Bfrdb( filename, login, passwd );
		} else {
			db = new Bfrdb( BfRNodePluginActivator.getBfRService() );
		}
    	
    	dbuuid = db.getDBUUID();
    	
		schema = createSchema();
		    	
    	if( level == 1 ) {
			result = db.selectEstModel( 1 );
		} else {
			result = db.selectEstModel( 2 );
		}
    	
    	// initialize data buffer
    	buf = exec.createDataContainer( schema.createSpec() );
    	
    	i = 0;
    	while( result.next() ) {
    		
    		// initialize row
    		tuple = new KnimeTuple( schema );
    		
    		
    		// fill ts
    		tuple.setValue( TimeSeriesSchema.ATT_CONDID, result.getInt( Bfrdb.ATT_CONDITIONID ) );
    		tuple.setValue( TimeSeriesSchema.ATT_COMBASEID, result.getString( Bfrdb.ATT_COMBASEID ) );
    		tuple.setValue( TimeSeriesSchema.ATT_MISCID, result.getString( Bfrdb.ATT_MISCID ) );
    		tuple.setValue( TimeSeriesSchema.ATT_MISC, result.getString( Bfrdb.ATT_MISC ) );
    		tuple.setValue( TimeSeriesSchema.ATT_TEMPERATURE, result.getString( Bfrdb.ATT_TEMPERATURE ) );
    		tuple.setValue( TimeSeriesSchema.ATT_PH, result.getString( Bfrdb.ATT_PH ) );
    		tuple.setValue( TimeSeriesSchema.ATT_WATERACTIVITY, result.getString( Bfrdb.ATT_AW ) );
    		tuple.setValue( TimeSeriesSchema.ATT_AGENTID, result.getString( Bfrdb.ATT_AGENTID ) );
    		tuple.setValue( TimeSeriesSchema.ATT_AGENTNAME, result.getString( Bfrdb.ATT_AGENTNAME ) );
    		tuple.setValue( TimeSeriesSchema.ATT_AGENTDETAIL, result.getString( Bfrdb.ATT_AGENTDETAIL ) );
    		tuple.setValue( TimeSeriesSchema.ATT_MATRIXID, result.getString( Bfrdb.ATT_MATRIXID ) );
    		tuple.setValue( TimeSeriesSchema.ATT_MATRIXNAME, result.getString( Bfrdb.ATT_MATRIXNAME ) );
    		tuple.setValue( TimeSeriesSchema.ATT_MATRIXDETAIL, result.getString( Bfrdb.ATT_MATRIXDETAIL ) );
    		tuple.setValue( TimeSeriesSchema.ATT_TIME, result.getString( Bfrdb.ATT_TIME ) );
    		tuple.setValue( TimeSeriesSchema.ATT_LOGC, result.getString( Bfrdb.ATT_LOG10N ) );
    		tuple.setValue( TimeSeriesSchema.ATT_COMMENT, result.getString( Bfrdb.ATT_COMMENT ) );
    		tuple.setValue( TimeSeriesSchema.ATT_LITIDTS, result.getInt( Bfrdb.ATT_LITERATUREID ) );
    		tuple.setValue( TimeSeriesSchema.ATT_LITTS, result.getString( Bfrdb.ATT_LITERATURETEXT ) );
    		tuple.setValue( TimeSeriesSchema.ATT_DBUUID, dbuuid );
    		
    		// fill m1
    		formula = result.getString( Bfrdb.ATT_FORMULA );
    		tuple.setValue( Model1Schema.ATT_VARPARMAP, result.getString( Bfrdb.ATT_VARMAPTO ) );
    		varMap = tuple.getMap( Model1Schema.ATT_VARPARMAP );
    		for( String to : varMap.keySet() )	
    			formula = MathUtilities.replaceVariable( formula, varMap.get( to ), to );
    		
    		
    		tuple.setValue( Model1Schema.ATT_FORMULA, formula );
    		tuple.setValue( Model1Schema.ATT_DEPVAR, result.getString( Bfrdb.ATT_DEP ) );
    		tuple.setValue( Model1Schema.ATT_INDEPVAR, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_INDEP ) ) );
    		tuple.setValue( Model1Schema.ATT_PARAMNAME, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_PARAMNAME ) ) );
    		tuple.setValue( Model1Schema.ATT_VALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_VALUE ) ) );
    		tuple.setValue( Model1Schema.ATT_MODELNAME, result.getString( Bfrdb.ATT_NAME ) );
    		tuple.setValue( Model1Schema.ATT_MODELID, result.getInt( Bfrdb.ATT_MODELID ) );
    		tuple.setValue( Model1Schema.ATT_ESTMODELID, result.getInt( Bfrdb.ATT_ESTMODELID ) );
    		tuple.setValue( Model1Schema.ATT_RMS, result.getString( Bfrdb.ATT_RMS ) );
    		tuple.setValue( Model1Schema.ATT_RSQUARED, result.getString( Bfrdb.ATT_RSQUARED ) );
    		tuple.setValue( Model1Schema.ATT_MINVALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MIN ) ) );
    		tuple.setValue( Model1Schema.ATT_MAXVALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MAX ) ) );
    		tuple.setValue( Model1Schema.ATT_MININDEP, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MININDEP ) ) );
    		tuple.setValue( Model1Schema.ATT_MAXINDEP, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MAXINDEP ) ) );
    		tuple.setValue( Model1Schema.ATT_LITIDM, result.getString( "LitMID" ) );
    		tuple.setValue( Model1Schema.ATT_LITM, result.getString( "LitM" ) );
    		tuple.setValue( Model1Schema.ATT_LITIDEM, result.getString( "LitEmID" ) );
    		tuple.setValue( Model1Schema.ATT_LITEM, result.getString( "LitEm" ) );
    		tuple.setValue( Model1Schema.ATT_PARAMERR, DbIo.convertArray2String( result.getArray( "StandardError" ) ) );
    		tuple.setValue( Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE );
    		tuple.setValue( Model1Schema.ATT_DBUUID, dbuuid );
    		
    		n = tuple.getStringList( Model1Schema.ATT_PARAMNAME ).size();
    		if( tuple.isNull( Model1Schema.ATT_VALUE ) )
    			for( j = 0; j < n; j++ )	
    				tuple.addValue( Model1Schema.ATT_VALUE, null );    				
    		if( tuple.isNull( Model1Schema.ATT_MINVALUE ) )
    			for( j = 0; j < n; j++ )	
    				tuple.addValue( Model1Schema.ATT_MINVALUE, null );
    		if( tuple.isNull( Model1Schema.ATT_MAXVALUE ) )
    			for( j = 0; j < n; j++ )	
    				tuple.addValue( Model1Schema.ATT_MAXVALUE, null );
    		if( tuple.isNull( Model1Schema.ATT_PARAMERR ) )
    			for( j = 0; j < n; j++ )	
    				tuple.addValue( Model1Schema.ATT_PARAMERR, null );
    		
    		n = tuple.getStringList( Model1Schema.ATT_INDEPVAR ).size();
    		if( tuple.isNull( Model1Schema.ATT_MININDEP ) )
    			for( j = 0; j < n; j++ )
    				tuple.addValue( Model1Schema.ATT_MININDEP, null );
    		if( tuple.isNull( Model1Schema.ATT_MAXINDEP ) )
    			for( j = 0; j < n; j++ )
    				tuple.addValue( Model1Schema.ATT_MAXINDEP, null );

    				
    		
    		// fill m2
    		if( level == 2 ) {
    			
        		formula = result.getString( Bfrdb.ATT_FORMULA+"2" );
        		tuple.setValue( Model2Schema.ATT_VARPARMAP, result.getString( Bfrdb.ATT_VARMAPTO+"2" ) );
        		varMap = tuple.getMap( Model2Schema.ATT_VARPARMAP );
        		for( String to : varMap.keySet() )	
        			formula = MathUtilities.replaceVariable( formula, varMap.get( to ), to );

	    		tuple.setValue( Model2Schema.ATT_FORMULA, formula );
	    		tuple.setValue( Model2Schema.ATT_DEPVAR, result.getString( Bfrdb.ATT_DEP+"2" ) );
	    		tuple.setValue( Model2Schema.ATT_INDEPVAR, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_INDEP+"2" ) ) );
	    		tuple.setValue( Model2Schema.ATT_PARAMNAME, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_PARAMNAME+"2" ) ) );
	    		tuple.setValue( Model2Schema.ATT_VALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_VALUE+"2" ) ) );
	    		tuple.setValue( Model2Schema.ATT_MODELNAME, result.getString( Bfrdb.ATT_NAME+"2" ) );
	    		tuple.setValue( Model2Schema.ATT_MODELID, result.getInt( Bfrdb.ATT_MODELID+"2" ) );
	    		tuple.setValue( Model2Schema.ATT_ESTMODELID, result.getInt( Bfrdb.ATT_ESTMODELID+"2" ) );
	    		tuple.setValue( Model2Schema.ATT_RMS, result.getString( Bfrdb.ATT_RMS+"2" ) );
	    		tuple.setValue( Model2Schema.ATT_RSQUARED, result.getString( Bfrdb.ATT_RSQUARED+"2" ) );
	    		tuple.setValue( Model2Schema.ATT_MINVALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MIN+"2" ) ) );
	    		tuple.setValue( Model2Schema.ATT_MAXVALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MAX+"2" ) ) );
	    		tuple.setValue( Model2Schema.ATT_MININDEP, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MININDEP+"2" ) ) );
	    		tuple.setValue( Model2Schema.ATT_MAXINDEP, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MAXINDEP+"2" ) ) );
	    		tuple.setValue( Model2Schema.ATT_LITIDM, result.getString( "LitMID2" ) );
	    		tuple.setValue( Model2Schema.ATT_LITM, result.getString( "LitM2" ) );
	    		tuple.setValue( Model2Schema.ATT_LITIDEM, result.getString( "LitEmID2" ) );
	    		tuple.setValue( Model2Schema.ATT_LITEM, result.getString( "LitEm2" ) );
	    		tuple.setValue( Model2Schema.ATT_PARAMERR, DbIo.convertArray2String( result.getArray( "StandardError" ) ) );
	    		tuple.setValue( Model2Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE );
	    		tuple.setValue( Model2Schema.ATT_DBUUID, dbuuid );
	    		
	    		n = tuple.getStringList( Model2Schema.ATT_PARAMNAME ).size();
	    		if( tuple.isNull( Model2Schema.ATT_VALUE ) )
	    			for( j = 0; j < n; j++ )	
	    				tuple.addValue( Model2Schema.ATT_VALUE, null );    				
	    		if( tuple.isNull( Model2Schema.ATT_MINVALUE ) )
	    			for( j = 0; j < n; j++ )	
	    				tuple.addValue( Model2Schema.ATT_MINVALUE, null );
	    		if( tuple.isNull( Model2Schema.ATT_MAXVALUE ) )
	    			for( j = 0; j < n; j++ )	
	    				tuple.addValue( Model2Schema.ATT_MAXVALUE, null );
	    		if( tuple.isNull( Model2Schema.ATT_PARAMERR ) )
	    			for( j = 0; j < n; j++ )	
	    				tuple.addValue( Model2Schema.ATT_PARAMERR, null );
	    		
	    		n = tuple.getStringList( Model2Schema.ATT_INDEPVAR ).size();
	    		if( tuple.isNull( Model2Schema.ATT_MININDEP ) )
	    			for( j = 0; j < n; j++ )
	    				tuple.addValue( Model2Schema.ATT_MININDEP, null );
	    		if( tuple.isNull( Model2Schema.ATT_MAXINDEP ) )
	    			for( j = 0; j < n; j++ )
	    				tuple.addValue( Model2Schema.ATT_MAXINDEP, null );
    		}
    		
    		// add row to data buffer
    		
    		// TODO: update filter
    		
    		if( EstModelReaderUi.passesFilter(
				level, qualityMode, qualityThresh,
				matrixEnabled, matrixString, agentEnabled, agentString,
				modelFilterEnabled, modelList, tuple ) )
				buf.addRowToTable( new DefaultRow( String.valueOf( i++ ), tuple ) );
    	}
    	
    	// close data buffer
    	buf.close();
    	db.close();

        return new BufferedDataTable[]{ buf.getTable() };
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
    	
    	DataTableSpec[] outSpec;
    	
    	outSpec = null;
    	try {
    		outSpec = new DataTableSpec[]{ createSchema().createSpec() };
    	}
    	catch( PmmException ex ) {
    		ex.printStackTrace();
    	}
    	
    	return outSpec;
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
    	settings.addInt( PARAM_LEVEL, level );
    	settings.addBoolean( PARAM_MODELFILTERENABLED, modelFilterEnabled );
    	settings.addString( PARAM_MODELLIST, modelList );
    	settings.addInt( PARAM_QUALITYMODE, qualityMode );
    	settings.addDouble( PARAM_QUALITYTHRESH, qualityThresh );
    	settings.addBoolean( PARAM_MATRIXENABLED, matrixEnabled );
    	settings.addString( PARAM_MATRIXSTRING, matrixString );
    	settings.addBoolean( PARAM_AGENTENABLED, agentEnabled );
    	settings.addString( PARAM_AGENTSTRING, agentString );
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
    	level = settings.getInt( PARAM_LEVEL );
    	modelFilterEnabled = settings.getBoolean( PARAM_MODELFILTERENABLED );
    	modelList = settings.getString( PARAM_MODELLIST );
    	qualityMode = settings.getInt( PARAM_QUALITYMODE );
    	qualityThresh = settings.getDouble( PARAM_QUALITYTHRESH );
    	matrixEnabled = settings.getBoolean( PARAM_MATRIXENABLED );
    	matrixString = settings.getString( PARAM_MATRIXSTRING );
    	agentEnabled = settings.getBoolean( PARAM_AGENTENABLED );
    	agentString = settings.getString( PARAM_AGENTSTRING );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings( final NodeSettingsRO settings )
            throws InvalidSettingsException {
    	// level.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals( final File internDir,
            final ExecutionMonitor exec )throws IOException,
            CanceledExecutionException {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals( final File internDir,
            final ExecutionMonitor exec )throws IOException,
            CanceledExecutionException {}
    
    private KnimeSchema createSchema() throws PmmException {
    	
    	KnimeSchema schema;
		schema = KnimeSchema.merge( new TimeSeriesSchema(), new Model1Schema() );
    	
    	if( level == 2 ) {
			schema = KnimeSchema.merge( schema, new Model2Schema() );
		}
    	

    	return schema;
    }
    
}

