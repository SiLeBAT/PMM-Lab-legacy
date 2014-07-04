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
package de.bund.bfr.knime.pmm.xlstimeseriesreader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.XmlConverter;

public class SettingsHelper {

	protected static final String CFGKEY_FILENAME = "FileName";
	protected static final String CFGKEY_SHEETNAME = "SheetName";
	protected static final String CFGKEY_COLUMNMAPPINGS = "ColumnMappings";
	protected static final String CFGKEY_TIMEUNIT = "TimeUnit";
	protected static final String CFGKEY_CONCENTRATIONUNIT = "ConcentrationUnit";
	protected static final String CFGKEY_AGENTCOLUMN = "AgentColumn";
	protected static final String CFGKEY_AGENTMAPPINGS = "AgentMappings";
	protected static final String CFGKEY_MATRIXCOLUMN = "MatrixColumn";
	protected static final String CFGKEY_MATRIXMAPPINGS = "MatrixMappings";
	protected static final String CFGKEY_AGENT = "Agent";
	protected static final String CFGKEY_MATRIX = "Matrix";
	protected static final String CFGKEY_LITERATURE = "Literature";

	private String fileName;
	private String sheetName;
	private Map<String, Object> columnMappings;
	private String timeUnit;
	private String concentrationUnit;
	private String agentColumn;
	private Map<String, AgentXml> agentMappings;
	private String matrixColumn;
	private Map<String, MatrixXml> matrixMappings;
	private AgentXml agent;
	private MatrixXml matrix;
	private List<LiteratureItem> literature;

	public SettingsHelper() {
		fileName = null;
		sheetName = null;
		columnMappings = new LinkedHashMap<String, Object>();
		timeUnit = null;
		concentrationUnit = null;
		agentColumn = null;
		agentMappings = new LinkedHashMap<String, AgentXml>();
		matrixColumn = null;
		matrixMappings = new LinkedHashMap<String, MatrixXml>();
		agent = null;
		matrix = null;
		literature = new ArrayList<LiteratureItem>();
	}

	public void loadSettings(NodeSettingsRO settings) {
		try {
			fileName = settings.getString(CFGKEY_FILENAME);
		} catch (InvalidSettingsException e) {
		}

		try {
			sheetName = settings.getString(CFGKEY_SHEETNAME);
		} catch (InvalidSettingsException e) {
		}

		try {
			columnMappings = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_COLUMNMAPPINGS),
					new LinkedHashMap<String, Object>());
		} catch (InvalidSettingsException e) {
		}

		try {
			timeUnit = settings.getString(CFGKEY_TIMEUNIT);
		} catch (InvalidSettingsException e) {
		}

		try {
			concentrationUnit = settings.getString(CFGKEY_CONCENTRATIONUNIT);
		} catch (InvalidSettingsException e) {
		}

		try {
			agentColumn = settings.getString(CFGKEY_AGENTCOLUMN);
		} catch (InvalidSettingsException e) {
		}

		try {
			agentMappings = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_AGENTMAPPINGS),
					new LinkedHashMap<String, AgentXml>());
		} catch (InvalidSettingsException e) {
		}

		try {
			matrixColumn = settings.getString(CFGKEY_MATRIXCOLUMN);
		} catch (InvalidSettingsException e) {
		}

		try {
			matrixMappings = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_MATRIXMAPPINGS),
					new LinkedHashMap<String, MatrixXml>());
		} catch (InvalidSettingsException e) {
		}

		try {
			agent = XmlConverter.xmlToObject(settings.getString(CFGKEY_AGENT),
					null);
		} catch (InvalidSettingsException e) {
		}

		try {
			matrix = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_MATRIX), null);
		} catch (InvalidSettingsException e) {
		}

		try {
			literature = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_LITERATURE),
					new ArrayList<LiteratureItem>());
		} catch (InvalidSettingsException e) {
		}
	}

	public void saveSettings(NodeSettingsWO settings) {
		settings.addString(CFGKEY_FILENAME, fileName);
		settings.addString(CFGKEY_SHEETNAME, sheetName);
		settings.addString(CFGKEY_COLUMNMAPPINGS,
				XmlConverter.objectToXml(columnMappings));
		settings.addString(CFGKEY_TIMEUNIT, timeUnit);
		settings.addString(CFGKEY_CONCENTRATIONUNIT, concentrationUnit);
		settings.addString(CFGKEY_AGENTCOLUMN, agentColumn);
		settings.addString(CFGKEY_AGENTMAPPINGS,
				XmlConverter.objectToXml(agentMappings));
		settings.addString(CFGKEY_MATRIXCOLUMN, matrixColumn);
		settings.addString(CFGKEY_MATRIXMAPPINGS,
				XmlConverter.objectToXml(matrixMappings));
		settings.addString(CFGKEY_AGENT, XmlConverter.objectToXml(agent));
		settings.addString(CFGKEY_MATRIX, XmlConverter.objectToXml(matrix));
		settings.addString(CFGKEY_LITERATURE,
				XmlConverter.objectToXml(literature));
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Map<String, Object> getColumnMappings() {
		return columnMappings;
	}

	public void setColumnMappings(Map<String, Object> columnMappings) {
		this.columnMappings = columnMappings;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public String getConcentrationUnit() {
		return concentrationUnit;
	}

	public void setConcentrationUnit(String concentrationUnit) {
		this.concentrationUnit = concentrationUnit;
	}

	public String getAgentColumn() {
		return agentColumn;
	}

	public void setAgentColumn(String agentColumn) {
		this.agentColumn = agentColumn;
	}

	public Map<String, AgentXml> getAgentMappings() {
		return agentMappings;
	}

	public void setAgentMappings(Map<String, AgentXml> agentMappings) {
		this.agentMappings = agentMappings;
	}

	public String getMatrixColumn() {
		return matrixColumn;
	}

	public void setMatrixColumn(String matrixColumn) {
		this.matrixColumn = matrixColumn;
	}

	public Map<String, MatrixXml> getMatrixMappings() {
		return matrixMappings;
	}

	public void setMatrixMappings(Map<String, MatrixXml> matrixMappings) {
		this.matrixMappings = matrixMappings;
	}

	public AgentXml getAgent() {
		return agent;
	}

	public void setAgent(AgentXml agent) {
		this.agent = agent;
	}

	public MatrixXml getMatrix() {
		return matrix;
	}

	public void setMatrix(MatrixXml matrix) {
		this.matrix = matrix;
	}

	public List<LiteratureItem> getLiterature() {
		return literature;
	}

	public void setLiterature(List<LiteratureItem> literature) {
		this.literature = literature;
	}
}
