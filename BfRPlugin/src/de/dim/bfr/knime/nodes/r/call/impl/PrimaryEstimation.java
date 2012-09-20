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
package de.dim.bfr.knime.nodes.r.call.impl;

import java.io.File;
import java.util.HashMap;

import org.knime.core.node.port.PortObject;

import de.dim.bfr.knime.nodes.r.call.CallToR;
import de.dim.bfr.knime.ports.BufferedTableContainer;
import de.dim.bfr.knime.util.PluginUtils;

/*
 * #' @usage callestimate.primary(CONDITIONS=NULL,MEASURED=NULL,NUMBERS=NULL,UNITS=NULL,
 #'  MODELCATALOG=NULL,PARAMETERCATALOG=NULL,EST_MODELS=NULL,EST_PARAMETERS=NULL,
 #'  EST_COVCOR=NULL,collapse=TRUE)
 #' @param CONDITIONS data frame, extract from the database table \var{Versuchsbedingungen}
 #' @param MEASURED data frame, extract from the database table \var{Messwerte}
 #' @param UNITS data frame, extract from the database table \var{Einheiten}
 #' @param NUMBERS data frame, extract from the database table \var{DoubleKennzahlen}
 #' @param MODELCATALOG data frame, extract from the database table \var{Modellkatalog}
 #' @param PARAMETERCATALOG data frame, extract from the database table \var{ModellkatalogParameter}
 #' @param EST_MODELS data frame, extract from the database table \var{GeschaetzeModelle}             
 #' @param EST_PARAMETERS data frame, extract from the database table \var{GeschaetzteParameter}
 #' @param EST_COVCOR data frame, extract from the database table \var{GeschaetzteParameterCovCor}
 #' @param collapse logical value. Wenn FALSE, dann werden nur die neu gescätzten Modelle zurückgeliefert.
 */

public class PrimaryEstimation extends CallToR {

	private static final String LIM = "/";
	private static final int MAIN_PORT = 0;
	private BufferedTableContainer container;
	private String estModelFileName;
	private String estParamFileName;
	private String estCovCorFileName;

	public PrimaryEstimation() {
		super();
		container = new BufferedTableContainer();
	}


	public void addNewTablesToContainer(String end) {
		estModelFileName = PluginUtils.TEMP_PATH + LIM + "ESTMODEL_" + end;
		estParamFileName = PluginUtils.TEMP_PATH + LIM + "ESTPARAMETER_" + end;
		estCovCorFileName = PluginUtils.TEMP_PATH + LIM + "COVCOR_" + end;

		this.container.addTableFile(PluginUtils.NEW_EST_MODEL, new File(
				estModelFileName));
		this.container.addTableFile(PluginUtils.NEW_EST_PARAMS, new File(
				estParamFileName));
		this.container.addTableFile(PluginUtils.NEW_EST_COVCORS, new File(
				estCovCorFileName));
	}

	public HashMap<String, File> createInFilesMap(PortObject[] inData) {
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("CONDITIONS", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.VERSUCHSBEDINGUNGEN));
		map.put("MEASURED", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.MESSWERTE));
		map.put("UNITS", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.EINHEITEN));
		map.put("NUMBERS", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.DOUBLEKENNZAHLEN));
		map.put("MODELCATALOG", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.MODELLKATALOG));
		map.put("PARAMETERCATALOG",
				((BufferedTableContainer) inData[MAIN_PORT])
						.getTable(PluginUtils.MODELLKATALOGPARAMETER));
		map.put("EST_MODELS", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.GES_MODELL));
		map.put("EST_PARAMETERS", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.GES_MODELL_PARAMETER));
		map.put("EST_COVCOR", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.COVCOR));
		return map;
	}

	public String getCallParameters(StringBuilder rFuncParams) {
		return PluginUtils.getRPrimaryEstimateCommand(rFuncParams.substring(1),
				estModelFileName, estParamFileName, estCovCorFileName);
	}


	@Override
	public BufferedTableContainer getContainer() {
		return container;
	}


	@Override
	public String getConfigureComponent() {
		return PluginUtils.PRIMARY_ESTIMATION;
	}


}
