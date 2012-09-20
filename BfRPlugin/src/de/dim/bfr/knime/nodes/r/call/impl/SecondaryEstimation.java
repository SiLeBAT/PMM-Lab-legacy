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
 @usage callestimate.secondary(primparameter,modeltype,CONDITIONS,NUMBERS,
 #' MODELCATALOG,PARAMETERCATALOG,EST_MODELS,EST_PARAMETERS,EST_COVCOR,
 #' PRIMARYSECONDARY,collapse=TRUE)
 #' @param primparameter character vector of length one, any parameter of primary model 
 #'       (f.e. parameter \var{lag} for primary models of the class \var{baranyi})
 #' @param modeltype character vector, that specified the class of secondary model 
 #'       that will be etmated by this function. Possible values are "T", "pH",
 #'       "aw", "T/aw", "T/pH", "pH/aw", "T/aw/pH" or any combination of them
 #' (f.e. \code{modeltype=c("T","aw","pH","T/pH")})
 #' @param CONDITIONS data frame, extract from the database table \var{Versuchsbedingungen}
 #' @param NUMBERS data frame, extract from the database table \var{DoubleKennzahlen}
 #' @param MODELCATALOG data frame, extract from the database table \var{Modellkatalog}
 #' @param PARAMETERCATALOG data frame, extract from the database table \var{ModellkatalogParameter}
 #' @param EST_MODELS data frame, extract from the database table \var{GeschaetzteModelle}
 #' @param EST_PARAMETERS data frame, extract from the database table \var{GeschaetzteParameter}
 #' @param EST_COVCOR data frame, extract from the database table \var{GeschaetzteParameterCovCor}                         
 #' @param PRIMARYSECONDARY data frame, extract from the database table \var{Sekundaermodelle_Primaermodelle}
 #' @param collapse logical value. Wenn FALSE, liefert die Funktion nur die neuen
 #' Modelle zurück. Wenn TRUE, dann werden die neuen daten an die Input-Tabellen
 #' angehängt.
 #' @return The function returns a named list that contains data frames EST_MODELS, 
 #' EST_PARAMETERS, EST_COVCOR, PRIMARYSECONDARY filled with new estimated
 #' secondary model(s). For invalid Entries or by occuring errors within internal
 #' functions the function terminates with an output FALSE. 
 */

public class SecondaryEstimation extends CallToR {

	private static final int MAIN_PORT = 0;
	private static final String LIM = "/";
	private BufferedTableContainer container;
	private String param;
	private String modelType;
	private String estModelFileName;
	private String estParamFileName;
	private String estCovCorFileName;
	private String primSecFileName;

	public SecondaryEstimation(String param, String modelType) {
		super();
		container = new BufferedTableContainer();
		this.param = param;
		this.modelType = modelType;
	}

	public HashMap<String, File> createInFilesMap(PortObject[] inData) {
		HashMap<String, File> map = new HashMap<String, File>();
		map.put("CONDITIONS", ((BufferedTableContainer) inData[MAIN_PORT])
				.getTable(PluginUtils.VERSUCHSBEDINGUNGEN));
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
		map.put("PRIMARYSECONDARY",
				((BufferedTableContainer) inData[MAIN_PORT])
						.getTable(PluginUtils.PRIMARYSECONDARY));
		return map;
	}

	@Override
	public void addNewTablesToContainer(String end) {
		estModelFileName = PluginUtils.TEMP_PATH + LIM + "ESTMODEL_" + end;
		estParamFileName = PluginUtils.TEMP_PATH + LIM + "ESTPARAMETER_" + end;
		estCovCorFileName = PluginUtils.TEMP_PATH + LIM + "COVCOR_" + end;
		primSecFileName = PluginUtils.TEMP_PATH + LIM + "PRIMSEC_" + end;

		container.addTableFile(PluginUtils.NEW_EST_MODEL, new File(
				estModelFileName));
		container.addTableFile(PluginUtils.NEW_EST_PARAMS, new File(
				estParamFileName));
		container.addTableFile(PluginUtils.NEW_EST_COVCORS, new File(
				estCovCorFileName));
		container.addTableFile(PluginUtils.NEW_PRIMSEC, new File(
				primSecFileName));
	}

	@Override
	public String getCallParameters(StringBuilder rFuncParams) {
		return PluginUtils.getRSecondaryEstimateCommand(
				rFuncParams.substring(1), modelType, param, estModelFileName,
				estParamFileName, estCovCorFileName, primSecFileName);
	}

	@Override
	public BufferedTableContainer getContainer() {
		return container;
	}

	@Override
	public String getConfigureComponent() {
		return PluginUtils.SECONDARY_ESTIMATION;
	}

}
