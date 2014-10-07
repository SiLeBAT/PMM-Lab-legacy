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
package de.bund.bfr.knime.pmm.xlsmodelreader;

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
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;

public class SettingsHelper {

	private static final String CFGKEY_FILE_NAME = "FileName";
	private static final String CFGKEY_SHEET_NAME = "SheetName";
	private static final String CFGKEY_MODEL_MAPPINGS = "ModelMappings";
	private static final String CFGKEY_MODEL_DEP_UNIT = "ModelDepUnit";
	private static final String CFGKEY_MODEL_INDEP_UNIT = "ModelIndepUnit";
	private static final String CFGKEY_SEC_MODEL_MAPPINGS = "SecModelMappings";
	private static final String CFGKEY_SEC_MODEL_INDEP_MINS = "SecModelIndepMins";
	private static final String CFGKEY_SEC_MODEL_INDEP_MAXS = "SecModelIndepMaxs";
	private static final String CFGKEY_SEC_MODEL_INDEP_CATEGORIES = "SecModelIndepCategories";
	private static final String CFGKEY_SEC_MODEL_INDEP_UNITS = "SecModelIndepUnits";
	private static final String CFGKEY_COLUMN_MAPPINGS = "ColumnMappings";
	private static final String CFGKEY_AGENT_COLUMN = "AgentColumn";
	private static final String CFGKEY_AGENT_MAPPINGS = "AgentMappings";
	private static final String CFGKEY_MATRIX_COLUMN = "MatrixColumn";
	private static final String CFGKEY_MATRIX_MAPPINGS = "MatrixMappings";
	private static final String CFGKEY_MODEL_TUPLE = "ModelTuple";
	private static final String CFGKEY_SEC_MODEL_TUPLES = "SecModelTuples";
	private static final String CFGKEY_AGENT = "Agent";
	private static final String CFGKEY_MATRIX = "Matrix";
	private static final String CFGKEY_LITERATURE = "Literature";

	private String fileName;
	private String sheetName;
	private Map<String, String> modelMappings;
	private String modelDepUnit;
	private String modelIndepUnit;
	private Map<String, Map<String, String>> secModelMappings;
	private Map<String, Map<String, String>> secModelIndepMins;
	private Map<String, Map<String, String>> secModelIndepMaxs;
	private Map<String, Map<String, String>> secModelIndepCategories;
	private Map<String, Map<String, String>> secModelIndepUnits;
	private Map<String, Object> columnMappings;
	private String agentColumn;
	private Map<String, AgentXml> agentMappings;
	private String matrixColumn;
	private Map<String, MatrixXml> matrixMappings;
	private KnimeTuple modelTuple;
	private Map<String, KnimeTuple> secModelTuples;
	private AgentXml agent;
	private MatrixXml matrix;
	private List<LiteratureItem> literature;

	public SettingsHelper() {
		fileName = null;
		sheetName = null;
		modelTuple = null;
		secModelTuples = new LinkedHashMap<>();
		modelMappings = new LinkedHashMap<>();
		modelDepUnit = null;
		modelIndepUnit = null;
		secModelMappings = new LinkedHashMap<>();
		secModelIndepMins = new LinkedHashMap<>();
		secModelIndepMaxs = new LinkedHashMap<>();
		secModelIndepCategories = new LinkedHashMap<>();
		secModelIndepUnits = new LinkedHashMap<>();
		columnMappings = new LinkedHashMap<>();
		agentColumn = null;
		agentMappings = new LinkedHashMap<>();
		matrixColumn = null;
		matrixMappings = new LinkedHashMap<>();
		agent = null;
		matrix = null;
		literature = new ArrayList<>();
	}

	public void loadSettings(NodeSettingsRO settings) {
		try {
			fileName = settings.getString(CFGKEY_FILE_NAME);
		} catch (InvalidSettingsException e) {
		}

		try {
			sheetName = settings.getString(CFGKEY_SHEET_NAME);
		} catch (InvalidSettingsException e) {
		}

		try {
			modelTuple = XmlConverter.xmlToTuple(settings
					.getString(CFGKEY_MODEL_TUPLE));
		} catch (InvalidSettingsException e) {
		}

		try {
			secModelTuples = XmlConverter.xmlToTupleMap(settings
					.getString(CFGKEY_SEC_MODEL_TUPLES));
		} catch (InvalidSettingsException e) {
		}

		try {
			modelMappings = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_MODEL_MAPPINGS),
					new LinkedHashMap<String, String>());
		} catch (InvalidSettingsException e) {
		}

		try {
			modelDepUnit = settings.getString(CFGKEY_MODEL_DEP_UNIT);
		} catch (InvalidSettingsException e) {
		}

		try {
			modelIndepUnit = settings.getString(CFGKEY_MODEL_INDEP_UNIT);
		} catch (InvalidSettingsException e1) {
		}

		try {
			secModelMappings = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_SEC_MODEL_MAPPINGS),
					new LinkedHashMap<String, Map<String, String>>());
		} catch (InvalidSettingsException e) {
		}

		try {
			secModelIndepMins = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_SEC_MODEL_INDEP_MINS),
					new LinkedHashMap<String, Map<String, String>>());
		} catch (InvalidSettingsException e) {
		}

		try {
			secModelIndepMaxs = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_SEC_MODEL_INDEP_MAXS),
					new LinkedHashMap<String, Map<String, String>>());
		} catch (InvalidSettingsException e) {
		}

		try {
			secModelIndepCategories = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_SEC_MODEL_INDEP_CATEGORIES),
					new LinkedHashMap<String, Map<String, String>>());
		} catch (InvalidSettingsException e) {
		}

		try {
			secModelIndepUnits = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_SEC_MODEL_INDEP_UNITS),
					new LinkedHashMap<String, Map<String, String>>());
		} catch (InvalidSettingsException e) {
		}

		try {
			columnMappings = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_COLUMN_MAPPINGS),
					new LinkedHashMap<String, Object>());
		} catch (InvalidSettingsException e) {
		}

		try {
			agentColumn = settings.getString(CFGKEY_AGENT_COLUMN);
		} catch (InvalidSettingsException e) {
		}

		try {
			agentMappings = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_AGENT_MAPPINGS),
					new LinkedHashMap<String, AgentXml>());
		} catch (InvalidSettingsException e) {
		}

		try {
			matrixColumn = settings.getString(CFGKEY_MATRIX_COLUMN);
		} catch (InvalidSettingsException e) {
		}

		try {
			matrixMappings = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_MATRIX_MAPPINGS),
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
		settings.addString(CFGKEY_FILE_NAME, fileName);
		settings.addString(CFGKEY_SHEET_NAME, sheetName);
		settings.addString(CFGKEY_MODEL_TUPLE,
				XmlConverter.tupleToXml(modelTuple));
		settings.addString(CFGKEY_SEC_MODEL_TUPLES,
				XmlConverter.tupleMapToXml(secModelTuples));
		settings.addString(CFGKEY_MODEL_MAPPINGS,
				XmlConverter.objectToXml(modelMappings));
		settings.addString(CFGKEY_MODEL_DEP_UNIT, modelDepUnit);
		settings.addString(CFGKEY_MODEL_INDEP_UNIT, modelIndepUnit);
		settings.addString(CFGKEY_SEC_MODEL_MAPPINGS,
				XmlConverter.objectToXml(secModelMappings));
		settings.addString(CFGKEY_SEC_MODEL_INDEP_MINS,
				XmlConverter.objectToXml(secModelIndepMins));
		settings.addString(CFGKEY_SEC_MODEL_INDEP_MAXS,
				XmlConverter.objectToXml(secModelIndepMaxs));
		settings.addString(CFGKEY_SEC_MODEL_INDEP_CATEGORIES,
				XmlConverter.objectToXml(secModelIndepCategories));
		settings.addString(CFGKEY_SEC_MODEL_INDEP_UNITS,
				XmlConverter.objectToXml(secModelIndepUnits));
		settings.addString(CFGKEY_COLUMN_MAPPINGS,
				XmlConverter.objectToXml(columnMappings));
		settings.addString(CFGKEY_AGENT_COLUMN, agentColumn);
		settings.addString(CFGKEY_AGENT_MAPPINGS,
				XmlConverter.objectToXml(agentMappings));
		settings.addString(CFGKEY_MATRIX_COLUMN, matrixColumn);
		settings.addString(CFGKEY_MATRIX_MAPPINGS,
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

	public Map<String, String> getModelMappings() {
		return modelMappings;
	}

	public void setModelMappings(Map<String, String> modelMappings) {
		this.modelMappings = modelMappings;
	}

	public String getModelDepUnit() {
		return modelDepUnit;
	}

	public void setModelDepUnit(String modelDepUnit) {
		this.modelDepUnit = modelDepUnit;
	}

	public String getModelIndepUnit() {
		return modelIndepUnit;
	}

	public void setModelIndepUnit(String modelIndepUnit) {
		this.modelIndepUnit = modelIndepUnit;
	}

	public Map<String, Map<String, String>> getSecModelMappings() {
		return secModelMappings;
	}

	public void setSecModelMappings(
			Map<String, Map<String, String>> secModelMappings) {
		this.secModelMappings = secModelMappings;
	}

	public Map<String, Map<String, String>> getSecModelIndepMins() {
		return secModelIndepMins;
	}

	public void setSecModelIndepMins(
			Map<String, Map<String, String>> secModelIndepMins) {
		this.secModelIndepMins = secModelIndepMins;
	}

	public Map<String, Map<String, String>> getSecModelIndepMaxs() {
		return secModelIndepMaxs;
	}

	public void setSecModelIndepMaxs(
			Map<String, Map<String, String>> secModelIndepMaxs) {
		this.secModelIndepMaxs = secModelIndepMaxs;
	}

	public Map<String, Map<String, String>> getSecModelIndepCategories() {
		return secModelIndepCategories;
	}

	public void setSecModelIndepCategories(
			Map<String, Map<String, String>> secModelIndepCategories) {
		this.secModelIndepCategories = secModelIndepCategories;
	}

	public Map<String, Map<String, String>> getSecModelIndepUnits() {
		return secModelIndepUnits;
	}

	public void setSecModelIndepUnits(
			Map<String, Map<String, String>> secModelIndepUnits) {
		this.secModelIndepUnits = secModelIndepUnits;
	}

	public Map<String, Object> getColumnMappings() {
		return columnMappings;
	}

	public void setColumnMappings(Map<String, Object> columnMappings) {
		this.columnMappings = columnMappings;
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

	public KnimeTuple getModelTuple() {
		return modelTuple;
	}

	public void setModelTuple(KnimeTuple modelTuple) {
		this.modelTuple = modelTuple;
	}

	public Map<String, KnimeTuple> getSecModelTuples() {
		return secModelTuples;
	}

	public void setSecModelTuples(Map<String, KnimeTuple> secModelTuples) {
		this.secModelTuples = secModelTuples;
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
