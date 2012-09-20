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
 #' @name callforecast.primary
 #' @aliases callforecast.primary
 #' @title Forecasting primary models
 #' @usage callforecast.primary(NEWDATA=NULL,MODELCATALOG=NULL,
 #'  PARAMETERCATALOG=NULL,EST_MODELS=NULL,EST_PARAMETERS=NULL)
 #' @param NEWDATA data frame or vector with time points to be forecasted
 #' @param MODELCATALOG data frame, extract from the database table \var{Modellkatalog}
 #' @param PARAMETERCATALOG data frame, extract from the database table \var{ModellkatalogParameter}
 #' @param EST_MODELS data frame, extract from the database table \var{GeschaetzeModelle}
 #' @param EST_PARAMETERS data frame, extract from the database table \var{GeschaetzteParameter}
 #' @return Returns the forcasted values of the chosen Primary Model(s) for the given time points. * 
 */

public class PrimaryForecasting extends CallToR {

	private static final String LIM = "/";
	private static final int MAIN_PORT = 0;
	private BufferedTableContainer container;
	private String forecastFilename;

	public PrimaryForecasting() {
		super();
		this.container = new BufferedTableContainer();
		System.out.println("Pause");
	}

	public HashMap<String, File> createInFilesMap(PortObject[] inData) {
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("NEWDATA", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.NEWDATA));
		map.put("MODELCATALOG", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.MODELLKATALOG));
		map.put("PARAMETERCATALOG",
				((BufferedTableContainer) inData[MAIN_PORT])
						.getTable(PluginUtils.MODELLKATALOGPARAMETER));
		map.put("EST_MODELS", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.GES_MODELL));
		map.put("EST_PARAMETERS", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.GES_MODELL_PARAMETER));
		return map;
	}

	public void addNewTablesToContainer(String end) {
		forecastFilename = PluginUtils.TEMP_PATH + LIM + "FORECASTPRIM_" + end;
		this.container.addTableFile(PluginUtils.NEW_FORECAST_PRIM, new File(
				forecastFilename));
	}

	@Override
	public String getCallParameters(StringBuilder rFuncParams) {
		return PluginUtils.getRPrimaryForecastCommand(rFuncParams.substring(1),
				forecastFilename);
	}

	@Override
	public BufferedTableContainer getContainer() {
		return container;
	}

	@Override
	public String getConfigureComponent() {
		return PluginUtils.PRIMARY_FORECASTER;
	}

}