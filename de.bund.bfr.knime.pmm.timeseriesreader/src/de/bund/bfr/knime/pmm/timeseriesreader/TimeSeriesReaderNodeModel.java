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
package de.bund.bfr.knime.pmm.timeseriesreader;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

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
import org.knime.core.node.config.Config;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.DbIo;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.MdReaderUi;

/**
 * This is the model implementation of TimeSeriesReader.
 * 
 *
 * @author Jorgen Brandt
 */
public class TimeSeriesReaderNodeModel extends NodeModel {
	    
	static final String PARAM_FILENAME = "filename";
	static final String PARAM_LOGIN = "login";
	static final String PARAM_PASSWD = "passwd";
	static final String PARAM_OVERRIDE = "override";
	static final String PARAM_MATRIXSTRING = "matrixString";
	static final String PARAM_AGENTSTRING = "agentString";
	static final String PARAM_LITERATURESTRING = "literatureString";
	static final String PARAM_PARAMETERS = "parameters";
	static final String PARAM_PARAMETERNAME = "parameterName";
	static final String PARAM_PARAMETERMIN = "parameterMin";
	static final String PARAM_PARAMETERMAX = "parameterMax";
	
	private String filename;
	private String login;
	private String passwd;
	private boolean override;
	private String matrixString;
	private String agentString;
	private String literatureString;
	
	private LinkedHashMap<String, Double[]> parameter;
    
    /**
     * Constructor for the node model.
     */
    protected TimeSeriesReaderNodeModel() {
    
        super( 0, 1 );
        
        filename = "";
        login = "";
        passwd = "";
        override = false;
        matrixString = "";
        agentString = "";
        literatureString = "";

        parameter = new LinkedHashMap<String, Double[]>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute( final BufferedDataTable[] inData,
            final ExecutionContext exec ) throws Exception {
    	
    	ResultSet result;
    	Bfrdb db;
    	BufferedDataContainer buf;
    	int i;
        PmmTimeSeries tuple;
        String dbuuid;

    	// fetch time series
        db = null;
    	if( override ) {
			db = new Bfrdb( filename, login, passwd );
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		}

    	dbuuid = db.getDBUUID();
    
    	result = db.selectTs();
    	
    	// initialize data buffer
    	buf = exec.createDataContainer(new TimeSeriesSchema().createSpec());
    	i = 0;
    	while (result.next()) {
    		// initialize row
    		tuple = new PmmTimeSeries();
    		
    		// fill row
    		tuple.setCondId( result.getInt( Bfrdb.ATT_CONDITIONID ) );
    		tuple.setCombaseId( result.getString( Bfrdb.ATT_COMBASEID ) );
    		PmmXmlDoc miscDoc = db.getMiscXmlDoc(result.getInt(Bfrdb.ATT_CONDITIONID));
    		tuple.setMisc(miscDoc);
    		tuple.setTemperature( result.getString( Bfrdb.ATT_TEMPERATURE ) );
    		try {tuple.setPh(result.getDouble(Bfrdb.ATT_PH));}
    		catch (PmmException e) {e.printStackTrace();}
    		try {tuple.setWaterActivity(result.getDouble(Bfrdb.ATT_AW));}
    		catch (PmmException e) {e.printStackTrace();}
    		tuple.setAgentId( result.getInt( Bfrdb.ATT_AGENTID ) );
    		tuple.setAgentName( result.getString( Bfrdb.ATT_AGENTNAME ) );
    		tuple.setAgentDetail( result.getString( Bfrdb.ATT_AGENTDETAIL ) );
    		tuple.setMatrixId( result.getInt( Bfrdb.ATT_MATRIXID ) );
    		tuple.setMatrixName( result.getString( Bfrdb.ATT_MATRIXNAME ) );
    		tuple.setMatrixDetail( result.getString( Bfrdb.ATT_MATRIXDETAIL ) );    
    		tuple.setMdData(DbIo.convertStringLists2TSXmlDoc(result.getString(Bfrdb.ATT_TIME), result.getString(Bfrdb.ATT_LOG10N)));
    		PmmXmlDoc tsDoc = DbIo.convertStringLists2TSXmlDoc(result.getString(Bfrdb.ATT_TIME), result.getString(Bfrdb.ATT_LOG10N));
    		tuple.setMdData(tsDoc);
    		tuple.setComment( result.getString( Bfrdb.ATT_COMMENT ) );
    		tuple.setValue( TimeSeriesSchema.ATT_DBUUID, dbuuid );
    		
    		String s = result.getString(Bfrdb.ATT_LITERATUREID);
    		if (s != null) {
    			PmmXmlDoc l = new PmmXmlDoc();
    			Object author = DBKernel.getValue("Literatur", "ID", s, "Erstautor");
    			Object year = DBKernel.getValue("Literatur", "ID", s, "Jahr");
    			Object title = DBKernel.getValue("Literatur", "ID", s, "Titel");
    			Object abstrac = DBKernel.getValue("Literatur", "ID", s, "Abstract");
    			LiteratureItem li = new LiteratureItem(author == null ? null : author.toString(),
    					(Integer) (year == null ? null : year),
    					title == null ? null : title.toString(),
    					abstrac == null ? null : abstrac.toString(),
    					Integer.valueOf(s)); 
    			l.add(li);
				tuple.setLiterature(l);
			}
    		
    		// add row to data buffer
    		if( MdReaderUi.passesFilter( matrixString, agentString, literatureString, parameter, tuple ) )
    			buf.addRowToTable( new DefaultRow( String.valueOf( i++ ), tuple ) );
    		
    	}
    	
    	// close data buffer
    	buf.close();
    	result.close();
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
		return new DataTableSpec[] { new TimeSeriesSchema().createSpec() };
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
    	settings.addString( PARAM_MATRIXSTRING, matrixString );
    	settings.addString( PARAM_AGENTSTRING, agentString );
    	settings.addString( PARAM_LITERATURESTRING, literatureString );
    	
		Config c = settings.addConfig(PARAM_PARAMETERS);
		String[] pars = new String[parameter.size()];
		String[] mins = new String[parameter.size()];
		String[] maxs = new String[parameter.size()];
		int i=0;
		for (String par : parameter.keySet()) {
			Double[] dbl = parameter.get(par);
			pars[i] = par;
			mins[i] = ""+dbl[0];
			maxs[i] = ""+dbl[1];
			i++;
		}
		c.addStringArray(PARAM_PARAMETERNAME, pars);
		c.addStringArray(PARAM_PARAMETERMIN, mins);
		c.addStringArray(PARAM_PARAMETERMAX, maxs);
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
    	matrixString = settings.getString( PARAM_MATRIXSTRING );
    	agentString = settings.getString( PARAM_AGENTSTRING );
    	literatureString = settings.getString( PARAM_LITERATURESTRING );

		Config c = settings.getConfig(PARAM_PARAMETERS);
		String[] pars = c.getStringArray(PARAM_PARAMETERNAME);
		String[] mins = c.getStringArray(PARAM_PARAMETERMIN);
		String[] maxs = c.getStringArray(PARAM_PARAMETERMAX);

        parameter = new LinkedHashMap<String, Double[]>();
		for (int i=0;i<pars.length;i++) {
			Double[] dbl = new Double[2];
			if (!mins[i].equals("null")) dbl[0] = Double.parseDouble(mins[i]);
			if (!maxs[i].equals("null")) dbl[1] = Double.parseDouble(maxs[i]);
			parameter.put(pars[i], dbl);
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

