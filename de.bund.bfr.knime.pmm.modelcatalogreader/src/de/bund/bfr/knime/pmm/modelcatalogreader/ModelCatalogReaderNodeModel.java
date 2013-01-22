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
package de.bund.bfr.knime.pmm.modelcatalogreader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import org.hsh.bfr.db.DBKernel;
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
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DbIo;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.ui.ModelReaderUi;

/**
 * This is the model implementation of ModelCatalogReader.
 * 
 *
 * @author Jorgen Brandt
 */
public class ModelCatalogReaderNodeModel extends NodeModel {
    	
	static final String PARAM_LEVEL = "level";
	static final String PARAM_MODELCLASS = "modelClass";
	static final String PARAM_MODELFILTERENABLED = "modelFilterEnabled";
	static final String PARAM_MODELLIST = "modelList";
	static final String PARAM_FILENAME = "filename";
	static final String PARAM_LOGIN = "login";
	static final String PARAM_PASSWD = "passwd";
	static final String PARAM_OVERRIDE = "override";


	private String filename;
	private String login;
	private String passwd;
	private int level;
	private String modelClass;
	private boolean override;
	private String modelList;
	private boolean modelFilterEnabled;
    
	private Connection conn = null;

	/**
     * Constructor for the node model.
     */
    protected ModelCatalogReaderNodeModel() {
        super( 0, 1 );
        
        filename = "";
        login = "";
        passwd = "";
        level = 1;
        override = false;
        modelList = "";
        modelFilterEnabled = false;
        
   }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	ResultSet result;
    	Bfrdb db;
    	BufferedDataContainer buf;
        KnimeSchema schema;
        KnimeTuple tuple;
        String formula;
        String dbuuid;
    	
    	// fetch time series
    	
        // fetch database connection
        db = null;
    	if( override ) {
			db = new Bfrdb( filename, login, passwd );
			conn = db.getConnection();
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
			conn = null;
		}
    		
    	dbuuid = db.getDBUUID();
    	
    	if( level == 1 ) {
    		
    		result = db.selectModel(1);
    		
    		schema = new Model1Schema();
        	
    		buf = exec.createDataContainer( schema.createSpec() );
    	
	    	int i = 0;
	    	while( result.next() ) {
	    		
	    		// initialize row
	    		tuple = new KnimeTuple( schema );
	    		
	    		// fill row
	    		formula = result.getString( Bfrdb.ATT_FORMULA );
	    		if( formula != null ) {
					formula = formula.replaceAll( "~", "=" ).replaceAll( "\\s", "" );
				}
	    		
				PmmXmlDoc cmDoc = new PmmXmlDoc();
				CatalogModelXml cmx = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), formula); 
				cmDoc.add(cmx);
				tuple.setValue(Model1Schema.ATT_MODELCATALOG, cmDoc);

	    		//tuple.setValue( Model1Schema.ATT_FORMULA, formula );
	    		PmmXmlDoc depDoc = new PmmXmlDoc();
	    		depDoc.add(new DepXml(result.getString(Bfrdb.ATT_DEP)));
	    		tuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);
	    		//tuple.setValue( Model1Schema.ATT_MODELNAME, result.getString( Bfrdb.ATT_NAME ) );
	    		//tuple.setValue( Model1Schema.ATT_MODELID, result.getInt( Bfrdb.ATT_MODELID ) );
	    		tuple.setValue(Model1Schema.ATT_INDEPENDENT, DbIo.convertArrays2IndepXmlDoc(null, result.getArray(Bfrdb.ATT_INDEP),
	    				null, null));	    		
	    		tuple.setValue(Model1Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(null, result.getArray(Bfrdb.ATT_PARAMNAME),
	    				null, null, result.getArray(Bfrdb.ATT_MINVALUE), result.getArray(Bfrdb.ATT_MAXVALUE)));	    		
	    		
	    		//int ri = MathUtilities.getRandomNegativeInt();
				PmmXmlDoc emDoc = new PmmXmlDoc();
				EstModelXml emx = new EstModelXml(null, null, null, null, null, null, null);
				emDoc.add(emx);
				tuple.setValue(Model1Schema.ATT_ESTMODEL, emDoc);

				String s = result.getString(Bfrdb.ATT_LITERATUREID);
	    		if (s != null) {
					tuple.setValue(Model1Schema.ATT_MLIT, getLiterature(s));
				}
				
	    		tuple.setValue( Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE );
	    		tuple.setValue( Model1Schema.ATT_DBUUID, dbuuid );
	    		
	    		// add row to data buffer
				if( ModelReaderUi.passesFilter( modelFilterEnabled, modelList, tuple ) ) {
					buf.addRowToTable( new DefaultRow( String.valueOf( i++ ), tuple ) );
				}
	    	}

    	}
    	else {
    		
    		result = db.selectModel(2);
    		
    		schema = new Model2Schema();
        	
    		buf = exec.createDataContainer( schema.createSpec() );
    	
	    	int i = 0;
	    	while( result.next() ) {
	    		
	    		// initialize row
	    		tuple = new KnimeTuple( schema );
	    		
	    		// fill row
	    		formula = result.getString( Bfrdb.ATT_FORMULA );
	    		if( formula != null ) {
					formula = formula.replaceAll( "~", "=" ).replaceAll( "\\s", "" );
				}

				PmmXmlDoc cmDoc = new PmmXmlDoc();
				CatalogModelXml cmx = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), formula); 
				cmDoc.add(cmx);
				tuple.setValue(Model2Schema.ATT_MODELCATALOG, cmDoc);

	    		//tuple.setValue( Model2Schema.ATT_FORMULA, formula );
	    		//tuple.setValue( Model2Schema.ATT_PARAMNAME, DbIo.convertArray2String(result.getArray( Bfrdb.ATT_PARAMNAME ) ));
	    		//tuple.setValue( Model2Schema.ATT_DEPVAR, result.getString( Bfrdb.ATT_DEP ) );
	    		PmmXmlDoc depDoc = new PmmXmlDoc();
	    		depDoc.add(new DepXml(result.getString(Bfrdb.ATT_DEP)));
	    		tuple.setValue(Model2Schema.ATT_DEPENDENT, depDoc);
	    		//tuple.setValue( Model2Schema.ATT_INDEPVAR, DbIo.convertArray2String(result.getArray( Bfrdb.ATT_INDEP ) ));
	    		//tuple.setValue( Model2Schema.ATT_MODELNAME, result.getString( Bfrdb.ATT_NAME ) );
	    		//tuple.setValue( Model2Schema.ATT_MODELID, result.getInt( Bfrdb.ATT_MODELID ) );
	    		//tuple.setValue( Model2Schema.ATT_MINVALUE, DbIo.convertArray2String(result.getArray( Bfrdb.ATT_MINVALUE ) ));
	    		//tuple.setValue( Model2Schema.ATT_MAXVALUE, DbIo.convertArray2String(result.getArray( Bfrdb.ATT_MAXVALUE ) ));
	    		tuple.setValue(Model2Schema.ATT_INDEPENDENT, DbIo.convertArrays2IndepXmlDoc(null, result.getArray(Bfrdb.ATT_INDEP),
	    				null, null));	    		
	    		tuple.setValue(Model2Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(null, result.getArray(Bfrdb.ATT_PARAMNAME),
	    				null, null, result.getArray(Bfrdb.ATT_MINVALUE), result.getArray(Bfrdb.ATT_MAXVALUE)));	    		

	    		//int ri = MathUtilities.getRandomNegativeInt();
				PmmXmlDoc emDoc = new PmmXmlDoc();
				EstModelXml emx = new EstModelXml(null, null, null, null, null, null, null);
				emDoc.add(emx);
				tuple.setValue(Model2Schema.ATT_ESTMODEL, emDoc);

				String s = result.getString(Bfrdb.ATT_LITERATUREID);
	    		if (s != null) {
					tuple.setValue(Model2Schema.ATT_MLIT, getLiterature(s));
				}

	    		tuple.setValue( Model2Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE );
	    		tuple.setValue( Model2Schema.ATT_DBUUID, dbuuid );

	    		// add row to data buffer
				if( ModelReaderUi.passesFilter( modelFilterEnabled, modelList, tuple ) ) {
					buf.addRowToTable( new DefaultRow( String.valueOf( i++ ), tuple ) );
				}
	    	}
    	}
 
    	
    	// close data buffer
    	buf.close();
    	db.close();

        return new BufferedDataTable[]{ buf.getTable() };
    }
    private PmmXmlDoc getLiterature(String s) {
		PmmXmlDoc l = new PmmXmlDoc();
		String [] ids = s.split(",");
		for (String id : ids) {
			Object author = DBKernel.getValue(conn,"Literatur", "ID", id, "Erstautor");
			Object year = DBKernel.getValue(conn,"Literatur", "ID", id, "Jahr");
			Object title = DBKernel.getValue(conn,"Literatur", "ID", id, "Titel");
			Object abstrac = DBKernel.getValue(conn,"Literatur", "ID", id, "Abstract");
			LiteratureItem li = new LiteratureItem(author == null ? null : author.toString(),
					(Integer) (year == null ? null : year),
					title == null ? null : title.toString(),
					abstrac == null ? null : abstrac.toString(),
					Integer.valueOf(id)); 
			l.add(li);
		}    
		return l;
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
    	
    	if( level == 1 ) {
			return new DataTableSpec[]{ new Model1Schema().createSpec() };
		} else {
			return new DataTableSpec[]{ new Model2Schema().createSpec() };
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	settings.addString( PARAM_FILENAME, filename );
    	settings.addString( PARAM_LOGIN, login );
    	settings.addString( PARAM_PASSWD, passwd );
    	settings.addInt( PARAM_LEVEL, level );
    	settings.addString(PARAM_MODELCLASS, modelClass);
    	settings.addBoolean( PARAM_OVERRIDE, override );
    	settings.addString( PARAM_MODELLIST, modelList );
    	settings.addBoolean( PARAM_MODELFILTERENABLED, modelFilterEnabled );
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
    	level = settings.getInt( PARAM_LEVEL );
    	modelClass = settings.getString(PARAM_MODELCLASS);
    	override = settings.getBoolean( PARAM_OVERRIDE );
    	modelList = settings.getString( PARAM_MODELLIST );
    	modelFilterEnabled = settings.getBoolean( PARAM_MODELFILTERENABLED );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	
    	int s;
    	
    	s = settings.getInt( PARAM_LEVEL );
    	if( !( s == 1 || s == 2 ) ) {
			throw new InvalidSettingsException( "Parameter level must be "+
    				"in {1,2}" );
		}
    }
    
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

