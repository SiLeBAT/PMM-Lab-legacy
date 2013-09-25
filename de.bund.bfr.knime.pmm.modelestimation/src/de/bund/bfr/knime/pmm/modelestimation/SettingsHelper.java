/*******************************************************************************
 * PMM-Lab © 2013, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2013, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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
package de.bund.bfr.knime.pmm.modelestimation;

import java.awt.geom.Point2D;
import java.util.LinkedHashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.XmlConverter;

public class SettingsHelper {

	protected static final String CFGKEY_FITTINGTYPE = "FittingType";
	protected static final String CFGKEY_ENFORCELIMITS = "EnforceLimits";
	protected static final String CFGKEY_EXPERTSETTINGS = "ExpertSettings";
	protected static final String CFGKEY_NPARAMETERSPACE = "NParameterSpace";
	protected static final String CFGKEY_NLEVENBERG = "NLevenberg";
	protected static final String CFGKEY_STOPWHENSUCCESSFUL = "StopWhenSuccessful";
	protected static final String CFGKEY_PARAMETERGUESSES = "ParameterGuesses";

	protected static final String NO_FITTING = "";
	protected static final String PRIMARY_FITTING = "Primary Fitting";
	protected static final String SECONDARY_FITTING = "Secondary Fitting";
	protected static final String ONESTEP_FITTING = "One-Step Fitting";

	protected static final String DEFAULT_FITTINGTYPE = NO_FITTING;
	protected static final boolean DEFAULT_ENFORCELIMITS = false;
	protected static final boolean DEFAULT_EXPERTSETTINGS = false;
	protected static final int DEFAULT_NPARAMETERSPACE = 10000;
	protected static final int DEFAULT_NLEVENBERG = 10;
	protected static final boolean DEFAULT_STOPWHENSUCCESSFUL = false;

	private String fittingType;
	private boolean enforceLimits;
	private boolean expertSettings;
	private int nParameterSpace;
	private int nLevenberg;
	private boolean stopWhenSuccessful;
	private Map<String, Map<String, Point2D.Double>> parameterGuesses;

	public SettingsHelper() {
		fittingType = DEFAULT_FITTINGTYPE;
		enforceLimits = DEFAULT_ENFORCELIMITS;
		expertSettings = DEFAULT_EXPERTSETTINGS;
		nParameterSpace = DEFAULT_NPARAMETERSPACE;
		nLevenberg = DEFAULT_NLEVENBERG;
		stopWhenSuccessful = DEFAULT_STOPWHENSUCCESSFUL;
		parameterGuesses = new LinkedHashMap<>();
	}

	public void loadSettings(NodeSettingsRO settings) {
		try {
			fittingType = settings.getString(CFGKEY_FITTINGTYPE);
		} catch (InvalidSettingsException e) {
			fittingType = DEFAULT_FITTINGTYPE;
		}

		try {
			enforceLimits = settings.getBoolean(CFGKEY_ENFORCELIMITS);
		} catch (InvalidSettingsException e) {
			enforceLimits = DEFAULT_ENFORCELIMITS;
		}

		try {
			expertSettings = settings.getBoolean(CFGKEY_EXPERTSETTINGS);
		} catch (InvalidSettingsException e) {
			expertSettings = DEFAULT_EXPERTSETTINGS;
		}

		try {
			nParameterSpace = settings.getInt(CFGKEY_NPARAMETERSPACE);
		} catch (InvalidSettingsException e) {
			nParameterSpace = DEFAULT_NPARAMETERSPACE;
		}

		try {
			nLevenberg = settings.getInt(CFGKEY_NLEVENBERG);
		} catch (InvalidSettingsException e) {
			nLevenberg = DEFAULT_NLEVENBERG;
		}

		try {
			stopWhenSuccessful = settings.getBoolean(CFGKEY_STOPWHENSUCCESSFUL);
		} catch (InvalidSettingsException e) {
			stopWhenSuccessful = DEFAULT_STOPWHENSUCCESSFUL;
		}

		try {
			parameterGuesses = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_PARAMETERGUESSES),
					new LinkedHashMap<String, Map<String, Point2D.Double>>());
		} catch (InvalidSettingsException e) {
			parameterGuesses = new LinkedHashMap<>();
		}
	}

	public void saveSettings(NodeSettingsWO settings) {
		settings.addString(CFGKEY_FITTINGTYPE, fittingType);
		settings.addBoolean(CFGKEY_ENFORCELIMITS, enforceLimits);
		settings.addBoolean(CFGKEY_EXPERTSETTINGS, expertSettings);
		settings.addInt(CFGKEY_NPARAMETERSPACE, nParameterSpace);
		settings.addInt(CFGKEY_NLEVENBERG, nLevenberg);
		settings.addBoolean(CFGKEY_STOPWHENSUCCESSFUL, stopWhenSuccessful);
		settings.addString(CFGKEY_PARAMETERGUESSES,
				XmlConverter.objectToXml(parameterGuesses));
	}

	public String getFittingType() {
		return fittingType;
	}

	public void setFittingType(String fittingType) {
		this.fittingType = fittingType;
	}

	public boolean isEnforceLimits() {
		return enforceLimits;
	}

	public void setEnforceLimits(boolean enforceLimits) {
		this.enforceLimits = enforceLimits;
	}

	public boolean isExpertSettings() {
		return expertSettings;
	}

	public void setExpertSettings(boolean expertSettings) {
		this.expertSettings = expertSettings;
	}

	public int getnParameterSpace() {
		return nParameterSpace;
	}

	public void setnParameterSpace(int nParameterSpace) {
		this.nParameterSpace = nParameterSpace;
	}

	public int getnLevenberg() {
		return nLevenberg;
	}

	public void setnLevenberg(int nLevenberg) {
		this.nLevenberg = nLevenberg;
	}

	public boolean isStopWhenSuccessful() {
		return stopWhenSuccessful;
	}

	public void setStopWhenSuccessful(boolean stopWhenSuccessful) {
		this.stopWhenSuccessful = stopWhenSuccessful;
	}

	public Map<String, Map<String, Point2D.Double>> getParameterGuesses() {
		return parameterGuesses;
	}

	public void setParameterGuesses(
			Map<String, Map<String, Point2D.Double>> parameterGuesses) {
		this.parameterGuesses = parameterGuesses;
	}
}
