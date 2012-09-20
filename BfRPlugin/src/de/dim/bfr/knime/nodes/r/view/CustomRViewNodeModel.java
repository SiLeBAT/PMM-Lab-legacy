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
package de.dim.bfr.knime.nodes.r.view;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knime.base.node.util.exttool.CommandExecution;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;

import de.dim.bfr.knime.ports.BufferedTableContainer;
import de.dim.bfr.knime.util.PluginUtils;
import de.dim.bfr.r.RScriptObject;

/**
 * The <code>RLocalViewsNodeModel</code> provides functionality to create
 * a R script with user defined R code calling R plots, run it and display
 * the generated plots in the nodes view.
 *
 * @author Kilian Thiel, University of Konstanz
 */
public class CustomRViewNodeModel extends NodeModel {

    private static final NodeLogger LOGGER =
        NodeLogger.getLogger(CustomRViewNodeModel.class);


    /**
     * The temp directory used to save csv, script R output files temporarily.
     */

    private static final String INTERNAL_FILE_NAME = "Rplot";
    private static final String PRE_HASH = "R-View-";

	private static final int MAIN_DATA_TABLE_PORT = 0;

    private Image m_resultImage;

    private String m_filename;

	private double r_plot_x_max = 20;

	private double r_plot_x_min = 0;

	private Integer pngHash;

	private List<Image> pngImages;

    /**
     * Creates new instance of <code>CustomRViewNodeModel</code> with one data
     * in port and no data out port.
     * @param pref provider for R executable
     */
    public CustomRViewNodeModel() {
    	super(new PortType[]{BufferedTableContainer.TYPE} ,new PortType[0]);
        m_resultImage = null;
    }

    /**
     * @return result image for the view, only available after successful
     *         execution of the node model.
     */
    Image getResultImage() {
        return m_resultImage;
    }
    
    List<Image> getResultImages() {
    	return pngImages;
    }

    /**
     * After execution of the R code and image instance is created which can
     * be displayed by the nodes view.
     *
     * {@inheritDoc}
     * @return 
     */
    protected final void postprocessDataTable() throws CanceledExecutionException, Exception 
    {
    	pngImages = new ArrayList<Image>();
        File tempPath = new File(PluginUtils.TEMP_PATH);
        File[] files = tempPath.listFiles();
        List<Integer> fileNames = new ArrayList<Integer>();
        for (File e : files) {
            if (e.getName().endsWith("png") && e.getName().contains(pngHash.toString())) {
            	fileNames.add(Integer.parseInt((e.getName().substring(7+pngHash.toString().length(), e.getName().length()-4))));
            }
        }
        Collections.sort(fileNames);
        for (int i : fileNames) {
        	FileInputStream fis = new FileInputStream(new File(tempPath+ "/" + PRE_HASH + pngHash + i + ".png"));
        	PNGImageContent png = new PNGImageContent(fis);
        	fis.close();
        	pngImages.add(png.getImage());
        }

        LOGGER.info("Number of processed images: " + pngImages.size());
    }

	/**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec) throws CanceledExecutionException, Exception 
    {   
    	executeR(inData, exec);
        return null;
    }
    
    protected void executeR(final PortObject[] inData, final ExecutionContext exec) throws CanceledExecutionException, Exception 
    {
        // preprocess data in in DataTable.
        // in this case compute and store the filename for the PNG into m_filename (inside the method)
        
        // file-storage for all csv-files (one for each table read from the in-port)
        File rCommandFile 		= null;
        File rOutFile 			= null;
        String LIM = System.getProperty("file.separator");
    	HashMap<String, File> map = new HashMap<String, File>();

        try 
        {
            // write data tables to csv
        	// in-port data tables are now stored in csv files readable by R.
            map.put("CONDITIONS", ((BufferedTableContainer) inData[MAIN_DATA_TABLE_PORT]).getTable(PluginUtils.VERSUCHSBEDINGUNGEN));
            map.put("MEASURED", ((BufferedTableContainer) inData[MAIN_DATA_TABLE_PORT]).getTable(PluginUtils.MESSWERTE));
            map.put("MODELCATALOG", ((BufferedTableContainer) inData[MAIN_DATA_TABLE_PORT]).getTable(PluginUtils.MODELLKATALOG));
            map.put("PARAMETERCATALOG", ((BufferedTableContainer) inData[MAIN_DATA_TABLE_PORT]).getTable(PluginUtils.MODELLKATALOGPARAMETER));
            map.put("EST_MODELS", ((BufferedTableContainer) inData[MAIN_DATA_TABLE_PORT]).getTable(PluginUtils.GES_MODELL));
            map.put("EST_PARAMETERS", ((BufferedTableContainer) inData[MAIN_DATA_TABLE_PORT]).getTable(PluginUtils.GES_MODELL_PARAMETER));
            map.put("NUMBERS", ((BufferedTableContainer) inData[MAIN_DATA_TABLE_PORT]).getTable(PluginUtils.DOUBLEKENNZAHLEN));
            map.put("UNITS", ((BufferedTableContainer) inData[MAIN_DATA_TABLE_PORT]).getTable(PluginUtils.EINHEITEN));
                      
            // execute R cmd            
            RScriptObject rScript = new RScriptObject();
            rScript.setWorkspaceDirectory(PluginUtils.TEMP_PATH);

            StringBuilder rFuncParams = new StringBuilder();
            
            // Läuft über die map mit den keys: VERSUCHSBEDINGUNGEN, MESSWERTE, ... etc
            // (So sind dann entsprechend die Variablen im R-Code benannt)
            for(String varName : map.keySet())
            {
            	rFuncParams.append(","+varName+"="+varName); // Parameter für Dispatcher aufruf einstellen
            	
            	// Variablen m R-Code setzten. Null-werte werden etnsprechend gesetzt.
            	if(map.get(varName) == null)
            		rScript.setVariableNull(varName);
            	else
            		rScript.loadCSVIntoVariable(varName, map.get(varName).getAbsolutePath());
            }
        	
//        	m_filename = PluginUtils.getPNGFileName(inData).values()
            HashMap<Integer, String> fileMap = PluginUtils.getPNGFileNameMap(inData);
            for (Map.Entry<Integer, String> e : fileMap.entrySet()) {
            	pngHash = e.getKey();
            	m_filename = e.getValue();
            }
               	
        	// TODO: besser hier: R-Script im classpath halten, an dieser stelle temporär außerhalb erstellen und R diese Kopie einlesen lassen!        	
        	rScript.append(PluginUtils.getRLibraryCommand());
        	rScript.append(PluginUtils.getRVisualisationCommand(rFuncParams.substring(1),m_filename.replace("\\","\\\\"),r_plot_x_min,r_plot_x_max));
                       
            // write R command	            
            String rCmd = rScript.toString();
            LOGGER.info(PluginUtils.RSTART);
            LOGGER.info("R Command: \n" + rCmd);
            rCommandFile = PluginUtils.writeRcommandFile(rCmd);
            
            rOutFile = new File(PluginUtils.TEMP_PATH+ LIM +"R-outDataTempFile-" + System.identityHashCode(rCommandFile) + ".Rout");// File.createTempFile("R-outDataTempFile-" + System.identityHashCode(rCommandFile), ".Rout",rCommandFile.getParentFile());
            LOGGER.debug(rOutFile.getAbsolutePath());
            // create shell command
            StringBuilder shellCmd = new StringBuilder();

            final String rBinaryPath = PluginUtils.getRPath();
            shellCmd.append(rBinaryPath);

            shellCmd.append(" CMD BATCH --vanilla");		// --vanilla in order not to create temporary workspace files. 
            shellCmd.append(" " + rCommandFile.getAbsolutePath());	// input-file parameter set to the temporary (R-command containing) R-script file
            shellCmd.append(" " + rOutFile.getAbsolutePath());		// output-file parameter set to the temporary output file

            // execute shell command
            String shcmd = shellCmd.toString();
            LOGGER.debug("Shell command: \n" + shcmd);

            CommandExecution cmdExec = new CommandExecution(shcmd);
            cmdExec.setExecutionDir(rCommandFile.getParentFile());
            int exitVal = cmdExec.execute(exec); // <-- execute

            if (exitVal != 0) 
            {
                String rErr = "";

                synchronized (cmdExec) 
                {
                    // save error description of the Rout file to the ErrorOut
                    LinkedList<String> list = new LinkedList<String>(cmdExec.getStdErr());

                    list.add("#############################################");
                    list.add("#");
                    list.add("# Content of .Rout file: ");
                    list.add("#");
                    list.add("#############################################");
                    list.add(" ");
                    BufferedReader bfr = new BufferedReader(new FileReader(rOutFile));
                    String line;
                    while ((line = bfr.readLine()) != null) 
                        list.add(line);	                    
                    bfr.close();

                    // use row before last as R error.
                    int index = list.size() - 2;
                    if (index >= 0) 
                        rErr = list.get(index);
                    
                    //setFailedExternalErrorOutput(list);
                }

                LOGGER.error("Execution of R Script failed with exit code: " + exitVal);
                LOGGER.error(rErr);
                throw new IllegalStateException("Execution of R script failed: " + rErr);
            }

            BufferedReader bfr = new BufferedReader(new FileReader(rOutFile));
            String line = "";
            while ((line = bfr.readLine()) != null) 
            	LOGGER.info(line);
            bfr.close();
 
            PluginUtils.deleteFile(rCommandFile);
    		//PluginUtils.deleteFile(rOutFile);
        }
        finally 
        {
        	/*for(String key : map.keySet())
       			PluginUtils.deleteFile(map.get(key));*/
        }
        
        // postprocess data in out DataTable and return this table
        postprocessDataTable();        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)throws InvalidSettingsException 
    {
		String m_R_path = PluginUtils.getRPath();

		if (m_R_path == null || m_R_path.trim().length() == 0)
			throw new InvalidSettingsException("R Binary not specified.");

		File binaryFile = new File(m_R_path);

		if (!binaryFile.exists())
			throw new InvalidSettingsException("R Binary \"" + m_R_path
					+ "\" not found.");

		if (!binaryFile.isFile() || !binaryFile.canExecute())
			throw new InvalidSettingsException("R Binary \"" + m_R_path
					+ "\" not executable.");

//		if (!(inSpecs[0] instanceof BufferedTableContainerSpec))
//			throw new InvalidSettingsException(
//					"Input Data is not valid! BufferedTableContainer expected but "
//							+ inSpecs[0].getClass().getName() + " found!");

		return null;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException 
    {
    	r_plot_x_max = settings.getDouble(CustomRViewNodeDialog.PLOT_MAX_X,20);
    	
    	r_plot_x_min = settings.getDouble(CustomRViewNodeDialog.PLOT_MIN_X,0);
    }

	/**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    }


    /**
     * The saved image is loaded.
     *
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File nodeInternDir,
            final ExecutionMonitor exec)
            throws IOException, CanceledExecutionException {
        //super.loadInternals(nodeInternDir, exec);
    	

        File file = new File(nodeInternDir, INTERNAL_FILE_NAME + ".png");
        if (file.exists() && file.canRead()) {
            File pngFile = new File(INTERNAL_FILE_NAME, ".png");
            FileUtil.copy(file, pngFile);
            InputStream is = new FileInputStream(pngFile);
            m_resultImage = new PNGImageContent(is).getImage();
            is.close();
        }
    }

    /**
     * The created image is saved.
     *
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File nodeInternDir,
            final ExecutionMonitor exec)
            throws IOException, CanceledExecutionException {
        //super.saveInternals(nodeInternDir, exec);

        File imgFile = new File(m_filename);
        if (imgFile.exists() && imgFile.canWrite()) {
            File file = new File(nodeInternDir, INTERNAL_FILE_NAME + ".png");
            FileUtil.copy(imgFile, file);
        }
    }

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
	}
	
}
