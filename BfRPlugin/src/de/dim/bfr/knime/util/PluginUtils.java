/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.knime.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.CancelledKeyException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.knime.base.node.io.csvwriter.CSVWriter;
import org.knime.base.node.io.csvwriter.FileWriterSettings;
import org.knime.base.node.io.filereader.FileAnalyzer;
import org.knime.base.node.io.filereader.FileReaderNodeSettings;
import org.knime.base.node.io.filereader.FileTable;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowIterator;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.KNIMEConstants;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;

import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.external.service.BFRNodeService;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

public class PluginUtils 
{   
    private static final NodeLogger LOGGER  = NodeLogger.getLogger(PluginUtils.class);
    public static final String RSTART = 	"###################################################" +
    										"################  Starting R-Script  ##############" +
    										"###################################################";
    public static final String TEMP_PATH    = KNIMEConstants.getKNIMETempDir().replace('\\', '/');
    public static final String DELIMITER    = ",";
    
	private static final String FIND_TABLE_PART_ONE= "SELECT TABLE_NAME, COUNT(TABLE_NAME) AS COUNTER FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME IN(";
	private static final String FIND_TABLE_PART_TWO = ") GROUP BY TABLE_NAME HAVING COUNT(TABLE_NAME) = "; //.length()

    public static final String VERSUCHSBEDINGUNGEN      = "Versuchsbedingungen";
    public static final String MESSWERTE                = "Messwerte";
    public static final String MODELLKATALOG            = "Modellkatalog";
    public static final String MODELLKATALOGPARAMETER   = "ModellkatalogParameter";
    public static final String GES_MODELL               = "GeschaetzteModelle";
    public static final String GES_MODELL_PARAMETER     = "GeschaetzteParameter";
    public static final String EINHEITEN				= "Einheiten";
    
    public static final String EXPERIMENTS_SIZE 		= "EXPERIMENTS_SIZE";

    public static final String MAIN_R_PACKAGE = "statup.r";

    public static final String DOUBLEKENNZAHLEN = "DoubleKennzahlen";

    public static final String COVCOR = "GeschaetzteParameterCovCor";

    public static final String PRIMARYSECONDARY = "PrimärSekundärRelation";

    public static final String NEW_EST_MODEL = "GeschaetzteModelle";

    public static final String NEW_EST_PARAMS = "GeschaetzteParameter";

    public static final String NEW_EST_COVCORS = "GeschaetzteParameterCovCor";

    public static final String NEW_PRIMSEC = "Sekundaermodelle_Primaermodelle";

    public static final String FORECAST = "FORECAST";
    
    public static final String PRIMARY_ESTIMATION 		= "PrimaryEstimationNodeFactory";
    public static final String SECONDARY_ESTIMATION 	= "SecondaryEstimationNodeFactory";
    public static final String PRIMARY_FORECASTER 		= "PrimaryForecasterNodeFactory";
    public static final String SECONDARY_FORECASTING 	= "SecondaryForecasterNodeFactory";

	public static final String PLOT_TYPE = "PLOT_TYPE";

	public static final String NEW_FORECAST_PRIM = "NEW_FORECAST_PRIM";

	public static final String NEWDATA = "NEWDATA";

	public static final String MODELTYPE = "MODELTYPE";

	public static final String CHOSEN_PARAM = "CHOSEN_PARAM";

	public static final String NEW_FORECAST_SEC = "NEW_FORECAST_SEC";
	private static final String KNIME_BFR_PACKAGE = "knimeBfR";
	public static final String ACTIVE_COMPONENT_LRDND = "ACTIVE_COMPONENT_LRDND";

	private static String plotType;

    public static String getRPath(){
        return BfRNodePluginActivator.getDefault().getPreferenceStore().getString(BfRNodePluginActivator.R_PATH);        
    }
    
    public static boolean deleteFile(final File file)
    {
        boolean del = false;
        if (file != null && file.exists()) 
        {
            del = FileUtil.deleteRecursively(file);
            // if file could not be deleted call GC and try again
            if (!del) {
                // It is possible that there are still open streams around
                // holding the file. Therefore these streams, actually belonging
                // to the garbage, has to be collected by the GC.
                System.gc();

                // try to delete again
                del = FileUtil.deleteRecursively(file);
                if (!del) {
                    // ok that's it no trials anymore ...
                    LOGGER.debug(file.getAbsoluteFile()
                            + " could not be deleted !");
                }
            }
        }
        return del;
    }

    public static File writeIntoCsvFile(final BufferedDataTable inData, final ExecutionContext exec) throws IOException, CanceledExecutionException 
    {
        if(inData == null)
            return null;
        
        // create Temp file
        File tempInDataFile = File.createTempFile("R-inDataTempFile-" +  System.identityHashCode(inData), ".csv", new File(TEMP_PATH));
        tempInDataFile.deleteOnExit();

        // write data to file
        FileWriter fw = new FileWriter(tempInDataFile);
        FileWriterSettings fws = new FileWriterSettings();
        fws.setColSeparator(DELIMITER);
        fws.setWriteColumnHeader(true);
        fws.setWriteRowID(true);

        CSVWriter writer = new CSVWriter(fw, fws);

        DataTableSpec inSpec    = inData.getDataTableSpec();
        DataTableSpec outSpec   = createRenamedDataTableSpec(inSpec); // a-zA-Z0-9 ist alles was akzeptiert wird, der rest wird zu einem Punkt
        
        if (!inSpec.equalStructure(outSpec)) 
            LOGGER.info("Some columns are renamed: " + inSpec + " <> " + outSpec);

        BufferedDataTable newTable  = exec.createSpecReplacerTable(inData, outSpec);
        ExecutionMonitor subExec    = exec.createSubProgress(0.5);
        writer.write(newTable, subExec);

        writer.close();
        
        return tempInDataFile;
    }
    
    public static File[] writeIntoCsvFiles(final BufferedDataTable[] inDataArray, final ExecutionContext exec) throws IOException, CanceledExecutionException 
    {
        File[] tempInDataFiles = new File[inDataArray.length];
        
        for(int i=0; i<inDataArray.length; i++)
            tempInDataFiles[i] = writeIntoCsvFile(inDataArray[i], exec); 
        
         return tempInDataFiles;
     }
    
    /**
     * Renames all column names by replacing all characters which are not
     * numeric or letters.
     * @param spec spec to replace column names
     * @return new spec with replaced column names
     */
    public static final DataTableSpec createRenamedDataTableSpec(final DataTableSpec spec) 
    {
        DataColumnSpec[] cspecs = new DataColumnSpec[spec.getNumColumns()];
        Set<String> newColNames = new HashSet<String>();
        for (int i = 0; i < cspecs.length; i++) {
            DataColumnSpecCreator cr =
                new DataColumnSpecCreator(spec.getColumnSpec(i));
            String oldName = spec.getColumnSpec(i).getName();
            // uniquify formatted column name
            String newName = formatColumn(oldName);
            
            int colIdx = 0;
            String uniqueColName = newName;
            if (!oldName.equals(newName)) {
                while (newColNames.contains(uniqueColName)
                        || spec.containsName(uniqueColName)) {
                    uniqueColName = newName + "_" + colIdx++;
                }
                cr.setName(uniqueColName);
                newColNames.add(uniqueColName);
                LOGGER.info("Original column \"" + oldName
                        + "\" was renamed to \"" + uniqueColName + "\".");
            }
            cspecs[i] = cr.createSpec();
        }
        return new DataTableSpec(spec.getName(), cspecs);
    }
    
    public static final String formatColumn(final String name) {
//        return name.replaceAll("[^a-_zA-Z0-9]", ".");
    	return name;
    }
    
    public static File writeRcommandFile(final String cmd) throws IOException 
    {
        File tempCommandFile = File.createTempFile("R-inDataTempFile-", ".r", new File(TEMP_PATH));
        tempCommandFile.deleteOnExit();
        
        FileWriter fw = new FileWriter(tempCommandFile);
        fw.write(cmd);
        fw.close();
                
        return tempCommandFile;
    }

//    public static String getSourceCommand() throws IOException {
//        File f = getRPackageFile();
//        String path = f.getAbsolutePath().replace("\\", "\\\\");
//        return "source(\""+path+"\")\n";
//    }
    
    public static String getRLibraryCommand() throws IOException {
    	return "library(" + KNIME_BFR_PACKAGE + ")\n";
    }

    public static HashMap<Integer, String> getPNGFileNameMap(Object[] inData){
    	int sysHash = getSysHash(inData);
//        return TEMP_PATH + "/R-View-" + System.identityHashCode(inData) + ".png";
    	HashMap<Integer,String> map = new HashMap<Integer, String>();
//    	map.put(sysHash, TEMP_PATH + "/R-View-" + System.identityHashCode(inData) + ".png");
    	map.put(sysHash, TEMP_PATH + "/R-View-" + sysHash + ".png");
    	return map;
    }
    
    public static String getPNGFileName(Object[] inData) {
        return TEMP_PATH + "/R-View-" + System.identityHashCode(inData) + ".png";
    }

    
    private static int getSysHash(Object[] inData) {
		return System.identityHashCode(inData);
	}

	public static BufferedDataTable readOutData(final File outData, final ExecutionContext exec) throws CanceledExecutionException 
    {
        FileReaderNodeSettings settings = new FileReaderNodeSettings();
        try {
			settings.setDataFileLocationAndUpdateTableName(outData.toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        settings.addDelimiterPattern(PluginUtils.DELIMITER, false, false, false);
        settings.addRowDelimiter("\n", true);
        settings.addQuotePattern("\"", "\"");
        settings.setDelimiterUserSet(true);
        settings.setFileHasColumnHeaders(true);
        settings.setFileHasColumnHeadersUserSet(true);
        settings.setFileHasRowHeaders(true);
        settings.setFileHasRowHeadersUserSet(true);
        settings.setQuoteUserSet(true);
        settings.setWhiteSpaceUserSet(true);
        settings.setMissValuePatternStrCols("NA");
        try {
			settings = FileAnalyzer.analyze(settings, null);
			 DataTableSpec tSpec = settings.createDataTableSpec();

        FileTable fTable = new FileTable(tSpec, settings, settings
                    .getSkippedColumns(), exec);

        return exec.createBufferedDataTable(fTable, exec);
		} catch (IOException e) {
			LOGGER.error("Cannot find serialized data. Please re-execute the workflow.");
			LOGGER.debug(e);
		}
		return null;
    }

    

    public static BufferedDataTable readFromDB(final String m_query, final ExecutionContext exec) throws CanceledExecutionException, Exception
    {
        exec.setProgress("Opening database connection...");
        try 
        {
            BFRNodeService service = BfRNodePluginActivator.getBfRService();
        
            Connection conn = service.getJDBCConnection();
            Statement st = conn.createStatement();
            
            exec.setProgress("Reading data from database...");

            final ResultSet result = st.executeQuery(m_query);
            
            final DataTableSpec m_spec = PluginUtils.createTableSpec(result.getMetaData());

            DataTable dt = new DataTable() 
            {
                @Override
                public RowIterator iterator() {
                    return new CustomDBRowIterator(m_spec,result);
                }
                
                @Override
                public DataTableSpec getDataTableSpec() {
                    return m_spec;
                }
            };

            BufferedDataTable bdt = exec.createBufferedDataTable(dt, exec);
            st.close();
            service.closeJDBCConnection(conn);

            return bdt;
        }
        catch (CanceledExecutionException cee) { throw cee;} 
        catch (Exception e) { throw e;} 
        catch (Throwable t) { throw new Exception(t);}   
    }   
    
    public static DataTableSpec createTableSpec(final ResultSetMetaData meta) throws SQLException 
    {
        int cols = meta.getColumnCount();
        if (cols == 0) {
            return new DataTableSpec("database");
        }
        DataTableSpec spec = null;
        for (int i = 0; i < cols; i++) {
            int dbIdx = i + 1;
            String name = meta.getColumnName(dbIdx);
            int type = meta.getColumnType(dbIdx);
            DataType newType;
            switch (type) {
                // all types that can be interpreted as integer
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.BIT:
                case Types.BOOLEAN:
                    newType = IntCell.TYPE;
                    break;
                // all types that can be interpreted as double
                case Types.FLOAT:
                case Types.DOUBLE:
                case Types.NUMERIC:
                case Types.DECIMAL:
                case Types.REAL:
                case Types.BIGINT:
                    newType = DoubleCell.TYPE;
                    break;
                case Types.TIME:
                case Types.DATE:
                case Types.TIMESTAMP:
                    newType = DateAndTimeCell.TYPE;
                    break;
                default:
                    newType = StringCell.TYPE;
            }
            if (spec == null) {
                spec = new DataTableSpec("database",
                        new DataColumnSpecCreator(
                        name, newType).createSpec());
            } else {
                name = DataTableSpec.getUniqueColumnName(spec, name);
                spec = new DataTableSpec("database", spec,
                       new DataTableSpec(new DataColumnSpecCreator(
                               name, newType).createSpec()));
            }
        }
        return spec;
    }

//    public static File getRPackageFile() throws IOException
//    {
//        File rTempFile = BfRNodePluginActivator.getRPackageFile();
        
        /*InputStream stream = PluginUtils.class.getClassLoader().getResourceAsStream(MAIN_R_PACKAGE);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        rTempFile = File.createTempFile("package", ".r",new File(PluginUtils.TEMP_PATH));
        rTempFile.deleteOnExit();
        BufferedWriter bw = new BufferedWriter(new FileWriter(rTempFile));
        while(br.ready())
            bw.write(br.readLine()+'\n');
        br.close();
        bw.close();
        */
//        return rTempFile;
//    }
    
    public static BufferedDataTable readDoublekennzahlenFromDB(ExecutionContext exec) throws CanceledExecutionException, Exception {
        return readFromDB("SELECT * FROM \"PUBLIC\".\"DoubleKennzahlen\"", exec);
    }
    
    public static BufferedDataTable readStatistikModellFromDB(ExecutionContext exec) throws CanceledExecutionException, Exception {
    	return readFromDB("SELECT * FROM \"PUBLIC\".\"Modellkatalog\"", exec);
    }
    
    public static BufferedDataTable readStatistikModellByRFromDB(ExecutionContext exec) throws CanceledExecutionException, Exception {
    	return readFromDB("SELECT * FROM \"PUBLIC\".\"Modellkatalog\" WHERE \"Software\" LIKE \'R\'", exec);
    }
    
	public static BufferedDataTable readStatistikModellParameterFromDB(ExecutionContext exec) throws CanceledExecutionException, Exception {
		return readFromDB("SELECT * FROM \"PUBLIC\".\"ModellkatalogParameter\"", exec);
	}

    public static String getPredictorCommand(String inPortDataVars, String csvOutputFilename, String pngFilename) {
        return "custom_prediction_dispatcher("+inPortDataVars+",csv_filename=\""+csvOutputFilename+"\",\""+pngFilename+"\")";
    }

    public static String getRVisualisationCommand(String inPortDataVars, String pngFilename, double min, double max) {
        return "callgraph.primary(" + inPortDataVars + ",PNGFILE=\"" + pngFilename + "\",minmax=c("+min+","+max+"),demomode=FALSE)\n";
    }
    
    public static String getRPrimaryForecastCommand(String inPortDataVars, String forecastFilename) {
		return "prediction <- callforecast.primary(" + inPortDataVars + ")\n" + 
			"write.csv(prediction, \""+forecastFilename+"\")\n";
    }
 
    public static String getRSecondaryForecastCommand(String inPortDataVars, String forecastFilename) {
        return "forecast <- callforecast.secondary(" + inPortDataVars +")\n" +
        "write.csv(forecast,\""+forecastFilename+"\")\n";
    }
    
    public static String getRPrimaryEstimateCommand(String inPortDataVars, String estModelFilename, String estParameterFilename, String estCovCorFilename) {
        return "results.primary <- callestimate.primary(" + inPortDataVars + ",collapse=FALSE)\n" +
        "EST_MODELS<-results.primary[[\"EST_MODELS\"]]\n" +
        "EST_PARAMETERS<-results.primary[[\"EST_PARAMETERS\"]]\n" +
        "EST_COVCOR<-results.primary[[\"EST_COVCOR\"]]\n" +
        "write.csv(EST_MODELS,\""+estModelFilename+"\")\n" +
        "write.csv(EST_PARAMETERS,\""+estParameterFilename+"\")\n" +
        "write.csv(EST_COVCOR,\""+estCovCorFilename+"\")\n";
    }

    public static String getRSecondaryEstimateCommand(String inPortDataVars, String type, String primParams, String estModelFilename, String estParameterFilename, String estCovCorFilename, String primSecFilename ) {        
        return "results.secondary <- callestimate.secondary(" + inPortDataVars + ",modeltype=c("+ type +"),primparameter=c("+primParams+"),collapse=FALSE)\n" +
        "EST_MODELS<-results.secondary[[\"EST_MODELS\"]]\n" +
        "EST_PARAMETERS<-results.secondary[[\"EST_PARAMETERS\"]]\n" +
        "EST_COVCOR<-results.secondary[[\"EST_COVCOR\"]]\n" +
        "PRIMARYSECONDARY<-results.secondary[[\"PRIMARYSECONDARY\"]]\n" +
        "write.csv(EST_MODELS,\""+estModelFilename+"\")\n" +
        "write.csv(EST_PARAMETERS,\""+estParameterFilename+"\")\n" +
        "write.csv(EST_COVCOR,\""+estCovCorFilename+"\")\n" +
        "write.csv(PRIMARYSECONDARY,\""+primSecFilename+"\")\n";
    }

    public static BufferedDataTable readPrimSecFromDB(ExecutionContext exec) throws CanceledExecutionException, Exception {
        return readFromDB("SELECT * FROM \"PUBLIC\".\"Sekundaermodelle_Primaermodelle\"", exec);
    }
    
    public static BufferedDataTable readPrimSecFromDBWherePrim(int[] primId, ExecutionContext exec) throws CanceledExecutionException, Exception {
    	StringBuilder builder = new StringBuilder();
		for (int i : primId) {
			builder.append(",");
			builder.append(i);
		}
		String primIdsString = builder.substring(1);
    	
		return readFromDB("SELECT * FROM \"PUBLIC\".\"Sekundaermodelle_Primaermodelle\" WHERE \"GeschaetztesPrimaermodell\" IN (" + primIdsString + ")" , exec);
    }
    
    public static BufferedDataTable readEinheitenFromDB(ExecutionContext exec) throws CanceledExecutionException, Exception {
    	return readFromDB("SELECT * FROM \"PUBLIC\".\"Einheiten\"", exec);
    }

    public static BufferedDataTable readConditionsFromDBWhereIdIN(int[] ids, ExecutionContext exec) throws CanceledExecutionException, Exception {
    	StringBuilder builder = new StringBuilder();
    	for (int i = 0; i < ids.length; i++) {
	    	builder.append(",");
	    	builder.append(ids[i]);
    	}
    	String idString = builder.substring(1);
    	System.out.println("SELECT * FROM \"PUBLIC\".\"Versuchsbedingungen\" WHERE ID IN (" + idString + ")");
		return readFromDB("SELECT * FROM \"PUBLIC\".\"Versuchsbedingungen\" WHERE ID IN (" + idString + ")", exec);
	}
    
   
    public static String getTableNameByColNames(List<String> colNames) throws CancelledKeyException{
		LOGGER.info("searching for missing tablename");
		StringBuilder tableStatement = new StringBuilder();
    	StringBuilder buildColNames = new StringBuilder();
    	for (String colName : colNames) {
    		buildColNames.append(", '" + colName + "'");
    	}
    	String columns = buildColNames.substring(2);
    	tableStatement.append(FIND_TABLE_PART_ONE)
	    	.append(columns)
    		.append(FIND_TABLE_PART_TWO).append(colNames.size());
    	String result = "";
    	BFRNodeService service = BfRNodePluginActivator.getBfRService();
    	Connection con = service.getJDBCConnection();
		LOGGER.info("Identifying table...");
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			LOGGER.debug(tableStatement.toString());
			rs = stmt.executeQuery(tableStatement.toString());
			rs.next();
			result = rs.getObject("TABLE_NAME").toString();
			if(rs.next()){
				String colList = "";
				for(String col : colNames)
					colList += " " + col;
				throw new CanceledExecutionException("Error! Could not identify table with columns [" + columns + "] because it matches more than one table");
			}
			LOGGER.info("Identified table as: [" + result + "]");
		} catch (SQLException e) {
			LOGGER.error("Could not identify the target database table.", e);
		} catch (CanceledExecutionException e) {
			e.printStackTrace();
		} finally {
			try {
				if(!stmt.isClosed())
					stmt.close();
				if(rs != null && !rs.isClosed())
					rs.close();
				service.closeJDBCConnection(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
    
    public static List<String> getTableNamesByColNames(List<String> colNames) throws CancelledKeyException{
		LOGGER.info("Identifying table...");
		StringBuilder tableStatement = new StringBuilder();
    	StringBuilder buildColNames = new StringBuilder();
    	for (String colName : colNames) {
    		buildColNames.append(", '" + colName + "'");
    	}
    	String columns = buildColNames.substring(2);
    	tableStatement.append(FIND_TABLE_PART_ONE)
	    	.append(columns)
    		.append(FIND_TABLE_PART_TWO).append(colNames.size());
    	List<String> result = new ArrayList<String>();
    	BFRNodeService service = BfRNodePluginActivator.getBfRService();
    	Connection con = service.getJDBCConnection();
		LOGGER.info("Calling database...");
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			LOGGER.debug(tableStatement.toString());
			rs = stmt.executeQuery(tableStatement.toString());
			while (rs.next()) {
				result.add(rs.getObject("TABLE_NAME").toString());
			}
			if (result.size() == 1) {
				LOGGER.info(" Identified table as: " + result.toString() );
			} else if (result.size() < 1) {
				LOGGER.info("No matching tables found");
			}else {
				LOGGER.info(result.size() + " matching tables found : [" + result.toString() + "]");
			}
		} catch (SQLException e) {
			LOGGER.error("Could not identify the target database table.", e);
		} finally {
			try {
				if(!stmt.isClosed())
					stmt.close();
				if(rs != null && !rs.isClosed())
					rs.close();
				service.closeJDBCConnection(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
    
	public static List[] getCsvContent(String fileName, boolean idColOnly)
			throws FileNotFoundException {

		File file = new File(fileName);
		List<String> header = new ArrayList<String>();
		List<String> data = new ArrayList<String>();

		Scanner lineScan = new Scanner(file);
		Scanner scanner = new Scanner(lineScan.nextLine());
		scanner.useDelimiter(",");

		while (scanner.hasNext()) {
			header.add(scanner.next());
		}

		while (lineScan.hasNextLine()) {
			scanner = new Scanner(lineScan.nextLine());
			scanner.useDelimiter(",");
			int i = 0;
			while (scanner.hasNext()) {
				i++;
				String colName = scanner.next();
				if (!idColOnly)
					data.add(colName);
				else if (idColOnly && i == header.indexOf("\"ID\"") + 1) {
					data.add(colName);
				}
			}
		}
		return new List[] { header, data };
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List createUniqueList(List duplicateList, boolean sort) {
		List uniqueList = new ArrayList();
		for (Object o : duplicateList) {
			if (!uniqueList.contains(o))
				uniqueList.add(o);
		}
		if (sort)
			Collections.sort(uniqueList);
		return uniqueList;
	}

	public static int getLastIdFromTable(String tableName) throws SQLException {
		BFRNodeService service = BfRNodePluginActivator.getBfRService();
    	Connection con = service.getJDBCConnection();
    	Statement stmt = con.createStatement();
    	ResultSet resultSet = stmt.executeQuery("SELECT MAX(ID) AS ID FROM \"PUBLIC\".\"" + tableName + "\"");
    	int id = 0;
    	while (resultSet.next()) {
    		id = Integer.parseInt(resultSet.getObject("ID").toString());
    	}
    	resultSet.close();
    	stmt.close();
    	service.closeJDBCConnection(con);
    	return id;
	}
	
	public static int getLastIdEstModels() {
		BFRNodeService service = BfRNodePluginActivator.getBfRService();
		List<Integer> idList = new ArrayList<Integer>();
		for (GeschaetztStatistikModell m : service.getAllEstimatedModels())
			idList.add(m.getId());
		PluginUtils.createUniqueList(idList, true);
		return idList.get(idList.size()-1);
	}
	
	public static int getLastIdEstParameters() {
		BFRNodeService service = BfRNodePluginActivator.getBfRService();
		List<GeschModellParameter> estParams = new ArrayList<GeschModellParameter>();
		for (GeschaetztStatistikModell m : service.getAllEstimatedModels())
			estParams.addAll(m.getParameter());
		List<Integer> idList = new ArrayList<Integer>();
		for (GeschModellParameter p : estParams) 
			idList.add(p.getId());
		PluginUtils.createUniqueList(idList, true);
		return idList.get(idList.size()-1);
	}
}
