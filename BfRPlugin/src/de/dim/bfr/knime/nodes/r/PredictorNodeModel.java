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
package de.dim.bfr.knime.nodes.r;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.knime.base.node.util.exttool.CommandExecution;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import de.dim.bfr.knime.nodes.loadrelateddata.LoadRelatedDataNodeDialog;
import de.dim.bfr.knime.nodes.r.call.CallToR;
import de.dim.bfr.knime.nodes.r.call.impl.PrimaryEstimation;
import de.dim.bfr.knime.nodes.r.call.impl.PrimaryForecasting;
import de.dim.bfr.knime.nodes.r.call.impl.SecondaryEstimation;
import de.dim.bfr.knime.nodes.r.call.impl.SecondaryForecasting;
import de.dim.bfr.knime.nodes.r.prim.estimate.PrimaryEstimationNodeFactory;
import de.dim.bfr.knime.nodes.r.prim.forecast.PrimaryForecasterNodeFactory;
import de.dim.bfr.knime.nodes.r.sec.estimate.SecondaryEstimationNodeFactory;
import de.dim.bfr.knime.nodes.r.sec.forecast.SecondaryForecasterNodeFactory;
import de.dim.bfr.knime.ports.BufferedTableContainer;
import de.dim.bfr.knime.util.PluginUtils;

/**
 * The <code>RLocalViewsNodeModel</code> provides functionality to create
 * a R script with user defined R code calling R plots, run it and display
 * the generated plot in the nodes view.
 *
 * @author Kilian Thiel, University of Konstanz
 */
public class PredictorNodeModel extends NodeModel {

    private static final NodeLogger LOGGER =
        NodeLogger.getLogger(PredictorNodeModel.class);

    /**
     * The temp directory used to save csv, script R output files temporarily.
     */

    
    private Image m_resultImage;


	private Class<? extends NodeFactory> fac;

	private String configuration;

	private String nodeName;

    /**
     * Creates new instance of <code>CustomRViewNodeModel</code> with one data
     * in port and no data out port.
     * @param class1 
     * @param nodeName 
     * @param pref provider for R executable
     * @throws InvalidSettingsException 
     */
    public PredictorNodeModel(@SuppressWarnings("rawtypes") Class<? extends NodeFactory> class1, String nodeName) {
        super(new PortType[]{BufferedTableContainer.TYPE} ,new PortType[]{BufferedTableContainer.TYPE});
        fac = class1;
        m_resultImage = null;
        this.nodeName = nodeName;
    }

    /**
     * @return result image for the view, only available after successful
     *         execution of the node model.
     */
    Image getResultImage() {
        return m_resultImage;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec) throws CanceledExecutionException, Exception 
    {   
    	return executeR(inData, exec);
    }
    
    protected PortObject[] executeR(final PortObject[] inData, final ExecutionContext exec) throws CanceledExecutionException, Exception 
    {
        BufferedTableContainer result = null; //= new BufferedTableContainer();
    	
    	CallToR call = null;
    	if (fac.isAssignableFrom(PrimaryEstimationNodeFactory.class) && configuration.equals(LoadRelatedDataNodeDialog.PRIMARY_ESTIMATION))
    		call = new PrimaryEstimation();
    	else if (fac.isAssignableFrom(SecondaryEstimationNodeFactory.class) && configuration.equals(LoadRelatedDataNodeDialog.SECONDARY_ESTIMATION)) {
    		String modelType = peekFlowVariableString(PluginUtils.MODELTYPE);
    		String param = peekFlowVariableString(PluginUtils.CHOSEN_PARAM);
    		call = new SecondaryEstimation(param, modelType);
    	}
    	else if (fac.isAssignableFrom(PrimaryForecasterNodeFactory.class) && configuration.equals(LoadRelatedDataNodeDialog.PRIMARY_FORECASTING))
    		call = new PrimaryForecasting();
    	else if (fac.isAssignableFrom(SecondaryForecasterNodeFactory.class) && configuration.equals(LoadRelatedDataNodeDialog.SECONDARY_FORECASTING))
    		call = new SecondaryForecasting();
    	else {
    		LOGGER.error("LoadRelatedData is configured for " + configuration + " but connected to the node " + nodeName.substring(0, nodeName.length() - 11) + ".");
    		throw new CanceledExecutionException("Configuration doesn't fit node choice.");
    	}
    	String shellCommand = call.callR(inData, exec);
    	CommandExecution cmdExec = new CommandExecution(shellCommand);
    	cmdExec.setExecutionDir(call.getExecutionDir());
    	
        int exitValue = cmdExec.execute(exec); // <-- execute

		if (exitValue != 0) {
			String rErr = "";

			synchronized (cmdExec) {

				BufferedReader bfr1 = new BufferedReader(new FileReader(
						call.getROutFile()));
				String line1 = "";
				while ((line1 = bfr1.readLine()) != null)
					LOGGER.error(line1);
				bfr1.close();
			}

			LOGGER.debug("Execution of R Script failed with exit code: "
					+ exitValue);
			LOGGER.error(rErr);
			throw new IllegalStateException("Execution of R script failed: "
					+ rErr);
		}

		BufferedReader bfr = new BufferedReader(new FileReader(
				call.getROutFile()));
		String line = "";
		while ((line = bfr.readLine()) != null)
			LOGGER.info(line);
		bfr.close();

		result = call.getContainer();
		PluginUtils.deleteFile(call.getRCommandFile());
		// PluginUtils.deleteFile(rOutFile);
		// }
		// finally
		// {
		// /*for(String key : map.keySet())
		// PluginUtils.deleteFile(map.get(key));*/
		// }
		//
		return new BufferedTableContainer[] { result };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		if (getAvailableFlowVariables().containsKey(PluginUtils.ACTIVE_COMPONENT_LRDND))
			configuration = peekFlowVariableString(PluginUtils.ACTIVE_COMPONENT_LRDND); 
		else throw new InvalidSettingsException("LoadRelatedData node is either unconfigured or not yet executed.");
		
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
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
//		selection.loadSettingsFrom(settings);
//		modelClass = KlasseTyp.get(selection.getStringValue()).getValue();
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
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {

	}

	/**
	 * The created image is saved.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File nodeInternDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
	}

}
