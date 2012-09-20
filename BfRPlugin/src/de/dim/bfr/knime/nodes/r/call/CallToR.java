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
package de.dim.bfr.knime.nodes.r.call;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;

import de.dim.bfr.knime.ports.BufferedTableContainer;
import de.dim.bfr.knime.util.PluginUtils;
import de.dim.bfr.r.RScriptObject;

public abstract class CallToR {

	private static final NodeLogger LOGGER = NodeLogger .getLogger(CallToR.class);
	private static final String LIM = "/";
	private File rCommandFile;
	private File rOutFile;
	
	public String callR(PortObject[] inData, ExecutionContext exec) throws IOException {
		rCommandFile = null;
		rOutFile = null;

		RScriptObject rScript = new RScriptObject();
		rScript.setWorkspaceDirectory(PluginUtils.TEMP_PATH);
		StringBuilder rFuncParams = new StringBuilder();

		HashMap<String, File> inFilesMap = createInFilesMap(inData);
		for (String varName : inFilesMap.keySet()) {
			rFuncParams.append("," + varName + "=" + varName);
			if (inFilesMap.get(varName) == null)
				rScript.setVariableNull(varName);
			else
				rScript.loadCSVIntoVariable(varName, inFilesMap.get(varName)
						.getAbsolutePath());
		}

		rScript.append(PluginUtils.getRLibraryCommand());
		addNewTablesToContainer(inData.hashCode() + ".csv");

		rScript.append(getCallParameters(rFuncParams));

		return buildShellCommand(rScript);
	}
	
	public abstract String getCallParameters(StringBuilder rFuncParams);
	
	public abstract HashMap<String, File> createInFilesMap(PortObject[] inData);
	
	public File getExecutionDir() {
		return rCommandFile.getParentFile();
	}
	
	public abstract BufferedTableContainer getContainer();
	
	public File getROutFile() {
		return rOutFile;
	}
	
	public File getRCommandFile() {
		return rCommandFile;
	}
	
	public abstract void addNewTablesToContainer(String end);
	
	public String buildShellCommand(RScriptObject rScript) throws IOException {
			String rCmd = rScript.toString();
		LOGGER.info(PluginUtils.RSTART);
		LOGGER.info("R Command: \n" + rCmd);
		rCommandFile = PluginUtils.writeRcommandFile(rCmd);
		rOutFile = new File(PluginUtils.TEMP_PATH + LIM + "R-outDataTempFile"
				+ System.identityHashCode(rCommandFile) + ".Rout");
		StringBuilder shellCmd = new StringBuilder();
		final String rBinaryPath = PluginUtils.getRPath();
		shellCmd.append(rBinaryPath);
		shellCmd.append(" CMD BATCH --vanilla");
		shellCmd.append(" " + rCommandFile.getAbsolutePath());
		shellCmd.append(" " + rOutFile.getAbsolutePath());
		String shellCommand = shellCmd.toString();
		LOGGER.debug("Shell command: \n" + shellCommand);
		return shellCommand;
	}
	
	public abstract String getConfigureComponent();
}
