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
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DbIo;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.EstModelReaderUi;

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
	static final String PARAM_MATRIXSTRING = "matrixString";
	static final String PARAM_AGENTSTRING = "agentString";
	static final String PARAM_LEVEL = "level";
	static final String PARAM_MODELFILTERENABLED = "modelFilterEnabled";
	static final String PARAM_MODELLIST = "modelList";

	static final String PARAM_LITERATURESTRING = "literatureString";
	static final String PARAM_PARAMETERS = "parameters";
	static final String PARAM_PARAMETERNAME = "parameterName";
	static final String PARAM_PARAMETERMIN = "parameterMin";
	static final String PARAM_PARAMETERMAX = "parameterMax";

	private String filename;
	private String login;
	private String passwd;
	private boolean override;
	private int level;
	private boolean modelFilterEnabled;
	private String modelList;
	private int qualityMode;
	private double qualityThresh;
	private String matrixString;
	private String agentString;
	private String literatureString;
	
	private LinkedHashMap<String, Double[]> parameter;
	
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
        matrixString = "";
        literatureString = "";

        parameter = new LinkedHashMap<String, Double[]>();
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
    	KnimeSchema schema;
    	String dbuuid;
    	String formula;
    	
        // fetch database connection
        db = null;
    	if( override ) {
			db = new Bfrdb( filename, login, passwd );
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		}
    	
    	dbuuid = db.getDBUUID();
    	
		schema = createSchema();
		    	
    	if( level == 1 ) {
			result = db.selectEstModel(1);
		} else {
			result = db.selectEstModel(2);
		}
    	
    	// initialize data buffer
    	buf = exec.createDataContainer( schema.createSpec() );
    	
    	int i = 0;
    	while( result.next() ) {
    		
    		// initialize row
    		tuple = new KnimeTuple( schema );
    		    		
    		// fill ts
    		tuple.setValue( TimeSeriesSchema.ATT_CONDID, result.getInt( Bfrdb.ATT_CONDITIONID ) );
    		tuple.setValue( TimeSeriesSchema.ATT_COMBASEID, result.getString( Bfrdb.ATT_COMBASEID ) );
    		PmmXmlDoc miscDoc = db.getMiscXmlDoc(result.getInt(Bfrdb.ATT_CONDITIONID));
			MiscXml mx = new MiscXml(AttributeUtilities.ATT_TEMPERATURE_ID,AttributeUtilities.ATT_TEMPERATURE,AttributeUtilities.ATT_TEMPERATURE,result.getDouble(Bfrdb.ATT_TEMPERATURE),"°C");
			miscDoc.add(mx);
			mx = new MiscXml(AttributeUtilities.ATT_PH_ID,AttributeUtilities.ATT_PH,AttributeUtilities.ATT_PH,result.getDouble(Bfrdb.ATT_PH),null);
			miscDoc.add(mx);
			mx = new MiscXml(AttributeUtilities.ATT_AW_ID,AttributeUtilities.ATT_WATERACTIVITY,AttributeUtilities.ATT_WATERACTIVITY,result.getDouble(Bfrdb.ATT_AW),null);
			miscDoc.add(mx);
    		tuple.setValue( TimeSeriesSchema.ATT_MISC, miscDoc);
    		tuple.setValue( TimeSeriesSchema.ATT_TEMPERATURE, result.getString( Bfrdb.ATT_TEMPERATURE ) );
    		tuple.setValue( TimeSeriesSchema.ATT_PH, result.getString( Bfrdb.ATT_PH ) );
    		tuple.setValue( TimeSeriesSchema.ATT_WATERACTIVITY, result.getString( Bfrdb.ATT_AW ) );
    		tuple.setValue( TimeSeriesSchema.ATT_AGENTID, result.getString( Bfrdb.ATT_AGENTID ) );
    		tuple.setValue( TimeSeriesSchema.ATT_AGENTNAME, result.getString( Bfrdb.ATT_AGENTNAME ) );
    		tuple.setValue( TimeSeriesSchema.ATT_AGENTDETAIL, result.getString( Bfrdb.ATT_AGENTDETAIL ) );
    		tuple.setValue( TimeSeriesSchema.ATT_MATRIXID, result.getString( Bfrdb.ATT_MATRIXID ) );
    		tuple.setValue( TimeSeriesSchema.ATT_MATRIXNAME, result.getString( Bfrdb.ATT_MATRIXNAME ) );
    		tuple.setValue( TimeSeriesSchema.ATT_MATRIXDETAIL, result.getString( Bfrdb.ATT_MATRIXDETAIL ) );
    		PmmXmlDoc tsDoc = DbIo.convertStringLists2TSXmlDoc(result.getString(Bfrdb.ATT_TIME), result.getString(Bfrdb.ATT_LOG10N));
    		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, tsDoc);
    		tuple.setValue( TimeSeriesSchema.ATT_COMMENT, result.getString( Bfrdb.ATT_COMMENT ) );
    		
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
				tuple.setValue(TimeSeriesSchema.ATT_LITMD,l);
			}

    		tuple.setValue( TimeSeriesSchema.ATT_DBUUID, dbuuid );
    		
    		// fill m1
    		formula = result.getString( Bfrdb.ATT_FORMULA );
    		// Time=t,Log10C=LOG10N
    		LinkedHashMap<String, String> varMap = DbIo.getVarParMap(result.getString( Bfrdb.ATT_VARMAPTO ));
    		//tuple.setValue( Model1Schema.ATT_VARPARMAP, result.getString( Bfrdb.ATT_VARMAPTO ) );
    		//varMap = tuple.getMap( Model1Schema.ATT_VARPARMAP );
    		for (String to : varMap.keySet())	{
    			formula = MathUtilities.replaceVariable(formula, varMap.get(to), to);
    		}
    		
			PmmXmlDoc cmDoc = new PmmXmlDoc();
			CatalogModelXml cmx = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), formula); 
			cmDoc.add(cmx);
			tuple.setValue(Model1Schema.ATT_MODELCATALOG, cmDoc);

    		//tuple.setValue( Model1Schema.ATT_FORMULA, formula );
    		//tuple.setValue( Model1Schema.ATT_DEPVAR, result.getString( Bfrdb.ATT_DEP ) );
    		PmmXmlDoc depDoc = new PmmXmlDoc();
    		String dep = result.getString(Bfrdb.ATT_DEP);
    		DepXml dx;
    		if (varMap.containsKey(dep)) {
    			dx = new DepXml(varMap.get(dep));
    			dx.setName(dep);
    		}
    		else {
    			dx = new DepXml(dep);
    		}
    		depDoc.add(dx);
    		tuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);
    		//tuple.setValue( Model1Schema.ATT_INDEPVAR, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_INDEP ) ) );
    		//tuple.setValue( Model1Schema.ATT_PARAMNAME, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_PARAMNAME ) ) );
    		//tuple.setValue( Model1Schema.ATT_VALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_VALUE ) ) );
    		//tuple.setValue( Model1Schema.ATT_MODELNAME, result.getString( Bfrdb.ATT_NAME ) );
    		//tuple.setValue( Model1Schema.ATT_MODELID, result.getInt( Bfrdb.ATT_MODELID ) );
    		
    		int emid = result.getInt(Bfrdb.ATT_ESTMODELID);
			PmmXmlDoc emDoc = new PmmXmlDoc();
			EstModelXml emx = new EstModelXml(emid, "EM_" + emid, result.getDouble(Bfrdb.ATT_RMS), result.getDouble(Bfrdb.ATT_RSQUARED), result.getDouble("AIC"), result.getDouble("BIC"), null);
			emDoc.add(emx);
			tuple.setValue(Model1Schema.ATT_ESTMODEL, emDoc);
    		//tuple.setValue( Model1Schema.ATT_ESTMODELID, result.getInt( Bfrdb.ATT_ESTMODELID ) );
    		//tuple.setValue( Model1Schema.ATT_RMS, result.getString( Bfrdb.ATT_RMS ) );
    		//tuple.setValue( Model1Schema.ATT_RSQUARED, result.getString( Bfrdb.ATT_RSQUARED ) );
    		//tuple.setValue(Model1Schema.ATT_AIC, result.getString("AIC"));
    		//tuple.setValue(Model1Schema.ATT_BIC, result.getString("BIC"));
    		//tuple.setValue( Model1Schema.ATT_MINVALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MIN ) ) );
    		//tuple.setValue( Model1Schema.ATT_MAXVALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MAX ) ) );
    		//tuple.setValue( Model1Schema.ATT_MININDEP, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MININDEP ) ) );
    		//tuple.setValue( Model1Schema.ATT_MAXINDEP, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MAXINDEP ) ) );
    		tuple.setValue(Model1Schema.ATT_INDEPENDENT, DbIo.convertArrays2IndepXmlDoc(varMap, result.getArray(Bfrdb.ATT_INDEP),
    				result.getArray(Bfrdb.ATT_MININDEP), result.getArray(Bfrdb.ATT_MAXINDEP)));
    		tuple.setValue(Model1Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(varMap, result.getArray(Bfrdb.ATT_PARAMNAME),
    				result.getArray(Bfrdb.ATT_VALUE), result.getArray("StandardError"), result.getArray(Bfrdb.ATT_MIN),
    				result.getArray(Bfrdb.ATT_MAX)));
    		
    		s = result.getString("LitMID");
    		if (s != null) tuple.setValue(Model1Schema.ATT_MLIT, getLiterature(s));
    		s = result.getString("LitEmID");
    		if (s != null) tuple.setValue(Model1Schema.ATT_EMLIT, getLiterature(s));
    		
    		//tuple.setValue( Model1Schema.ATT_PARAMERR, DbIo.convertArray2String( result.getArray( "StandardError" ) ) );
    		tuple.setValue( Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE );
    		tuple.setValue( Model1Schema.ATT_DBUUID, dbuuid );

        	//List<Double> tList = tuple.getDoubleList( TimeSeriesSchema.ATT_TIME );
    		//paramList = tuple.getStringList( Model1Schema.ATT_PARAMNAME );
    		/*
    		PmmXmlDoc tList = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
    		PmmXmlDoc paramList = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
    	int numParam, numSample;
    	Double rms;
    		rms = tuple.getDouble( Model1Schema.ATT_RMS );
    		
    		if (tList != null) numSample = tList.size();
    		else numSample = -1;
    		
    		if (paramList != null) numParam = paramList.size();
    		else numParam = -1;
    		
    		if (rms == null) rms = -1.;
    		
    		tuple.setValue(Model1Schema.ATT_AIC, MathUtilities.akaikeCriterion(numParam, numSample, rms));    		
    		tuple.setValue(Model1Schema.ATT_BIC, MathUtilities.bayesCriterion(numParam, numSample, rms));
    		*/
    		
    		// fill m2
    		if (level == 2) {   			
        		formula = result.getString( Bfrdb.ATT_FORMULA+"2" );
        		//tuple.setValue( Model2Schema.ATT_VARPARMAP, result.getString( Bfrdb.ATT_VARMAPTO+"2" ) );
        		//varMap = tuple.getMap( Model2Schema.ATT_VARPARMAP );
        		varMap = DbIo.getVarParMap(result.getString( Bfrdb.ATT_VARMAPTO+"2" ));
        		for( String to : varMap.keySet() )	{
        			formula = MathUtilities.replaceVariable( formula, varMap.get( to ), to );
        		}

	    		//tuple.setValue( Model2Schema.ATT_FORMULA, formula );
	    		
    			cmDoc = new PmmXmlDoc();
    			cmx = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID+"2"), result.getString(Bfrdb.ATT_NAME+"2"), formula); 
    			cmDoc.add(cmx);
    			tuple.setValue(Model2Schema.ATT_MODELCATALOG, cmDoc);
	    		//tuple.setValue( Model2Schema.ATT_DEPVAR, result.getString( Bfrdb.ATT_DEP+"2" ) );
	    		depDoc = new PmmXmlDoc();
	    		dep = result.getString(Bfrdb.ATT_DEP+"2");
	    		if (varMap.containsKey(dep)) {
	    			dx = new DepXml(varMap.get(dep));
	    			dx.setName(dep);
	    		}
	    		else {
	    			dx = new DepXml(dep);
	    		}
	    		depDoc.add(dx);
	    		tuple.setValue(Model2Schema.ATT_DEPENDENT, depDoc);
	    		//tuple.setValue( Model2Schema.ATT_INDEPVAR, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_INDEP+"2" ) ) );
	    		//tuple.setValue( Model2Schema.ATT_PARAMNAME, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_PARAMNAME+"2" ) ) );
	    		//tuple.setValue( Model2Schema.ATT_VALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_VALUE+"2" ) ) );
	    		//tuple.setValue( Model2Schema.ATT_MODELNAME, result.getString( Bfrdb.ATT_NAME+"2" ) );
	    		//tuple.setValue( Model2Schema.ATT_MODELID, result.getInt( Bfrdb.ATT_MODELID+"2" ) );
	    		
	    		emid = result.getInt(Bfrdb.ATT_ESTMODELID+"2");
				emDoc = new PmmXmlDoc();
				emx = new EstModelXml(emid, "EM_" + emid, result.getDouble(Bfrdb.ATT_RMS+"2"), result.getDouble(Bfrdb.ATT_RSQUARED+"2"), result.getDouble("AIC2"), result.getDouble("BIC2"), null);
				emDoc.add(emx);
				tuple.setValue(Model2Schema.ATT_ESTMODEL, emDoc);
	    		//tuple.setValue( Model2Schema.ATT_ESTMODELID, result.getInt( Bfrdb.ATT_ESTMODELID+"2" ) );
	    		//tuple.setValue( Model2Schema.ATT_RMS, result.getString( Bfrdb.ATT_RMS+"2" ) );
	    		//tuple.setValue( Model2Schema.ATT_RSQUARED, result.getString( Bfrdb.ATT_RSQUARED+"2" ) );
	    		//tuple.setValue(Model2Schema.ATT_AIC, result.getString("AIC2"));
	    		//tuple.setValue(Model2Schema.ATT_BIC, result.getString("BIC2"));
	    		//tuple.setValue( Model2Schema.ATT_MINVALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MIN+"2" ) ) );
	    		//tuple.setValue( Model2Schema.ATT_MAXVALUE, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MAX+"2" ) ) );
	    		//tuple.setValue( Model2Schema.ATT_MININDEP, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MININDEP+"2" ) ) );
	    		//tuple.setValue( Model2Schema.ATT_MAXINDEP, DbIo.convertArray2String( result.getArray( Bfrdb.ATT_MAXINDEP+"2" ) ) );
	    		tuple.setValue(Model2Schema.ATT_INDEPENDENT, DbIo.convertArrays2IndepXmlDoc(varMap, result.getArray(Bfrdb.ATT_INDEP+"2"),
	    				result.getArray(Bfrdb.ATT_MININDEP+"2"), result.getArray(Bfrdb.ATT_MAXINDEP+"2")));
	    		tuple.setValue(Model2Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(varMap, result.getArray(Bfrdb.ATT_PARAMNAME+"2"),
	    				result.getArray(Bfrdb.ATT_VALUE+"2"), result.getArray("StandardError2"), result.getArray(Bfrdb.ATT_MIN+"2"),
	    				result.getArray(Bfrdb.ATT_MAX+"2")));

	    		s = result.getString("LitMID2");
	    		if (s != null) tuple.setValue(Model2Schema.ATT_MLIT, getLiterature(s));
	    		s = result.getString("LitEmID2");
	    		if (s != null) tuple.setValue(Model2Schema.ATT_EMLIT, getLiterature(s));

	    		//tuple.setValue( Model2Schema.ATT_PARAMERR, DbIo.convertArray2String( result.getArray( "StandardError2" ) ) );
	    		tuple.setValue( Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE );
	    		tuple.setValue( Model2Schema.ATT_DBUUID, dbuuid );
    		}
    		
    		// add row to data buffer
    		
    		// TODO: update filter
    		
    		if( EstModelReaderUi.passesFilter(
				level, qualityMode, qualityThresh,
				matrixString, agentString, literatureString, parameter,
				modelFilterEnabled, modelList, tuple ) )
				buf.addRowToTable( new DefaultRow( String.valueOf( i++ ), tuple ) );
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
			Object author = DBKernel.getValue("Literatur", "ID", id, "Erstautor");
			Object year = DBKernel.getValue("Literatur", "ID", id, "Jahr");
			Object title = DBKernel.getValue("Literatur", "ID", id, "Titel");
			Object abstrac = DBKernel.getValue("Literatur", "ID", id, "Abstract");
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
    	level = settings.getInt( PARAM_LEVEL );
    	modelFilterEnabled = settings.getBoolean( PARAM_MODELFILTERENABLED );
    	modelList = settings.getString( PARAM_MODELLIST );
    	qualityMode = settings.getInt( PARAM_QUALITYMODE );
    	qualityThresh = settings.getDouble( PARAM_QUALITYTHRESH );
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

