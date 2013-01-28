/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.ListUtilities;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of XLSTimeSeriesReader.
 * 
 * 
 * @author Christian Thoens
 */
public class XLSTimeSeriesReaderNodeModel extends NodeModel {

	protected static final String CFGKEY_FILENAME = "FileName";
	protected static final String CFGKEY_COLUMNMAPPINGS = "ColumnMappings";
	protected static final String CFGKEY_AGENTCOLUMN = "AgentColumn";
	protected static final String CFGKEY_AGENTMAPPINGS = "AgentMappings";
	protected static final String CFGKEY_MATRIXCOLUMN = "MatrixColumn";
	protected static final String CFGKEY_MATRIXMAPPINGS = "MatrixMappings";
	protected static final String CFGKEY_TIMEUNIT = "TimeUnit";
	protected static final String CFGKEY_LOGCUNIT = "LogcUnit";
	protected static final String CFGKEY_TEMPUNIT = "TempUnit";
	protected static final String CFGKEY_AGENTID = "AgentID";
	protected static final String CFGKEY_MATRIXID = "MatrixID";
	protected static final String CFGKEY_LITERATUREIDS = "LiteratureIDs";

	protected static final String DEFAULT_AGENTCOLUMN = null;
	protected static final String DEFAULT_MATRIXCOLUMN = null;
	protected static final int DEFAULT_AGENTID = -1;
	protected static final int DEFAULT_MATRIXID = -1;

	private String fileName;
	private Map<String, String> columnMappings;
	private String agentColumn;
	private Map<String, String> agentMappings;
	private String matrixColumn;
	private Map<String, String> matrixMappings;
	private String timeUnit;
	private String logcUnit;
	private String tempUnit;
	private int agentID;
	private int matrixID;
	private List<Integer> literatureIDs;

	private KnimeSchema timeSeriesSchema;

	/**
	 * Constructor for the node model.
	 */
	protected XLSTimeSeriesReaderNodeModel() {
		super(0, 1);
		fileName = null;
		columnMappings = new LinkedHashMap<>();
		agentColumn = DEFAULT_AGENTCOLUMN;
		agentMappings = new LinkedHashMap<>();
		matrixColumn = DEFAULT_MATRIXCOLUMN;
		matrixMappings = new LinkedHashMap<>();
		timeUnit = AttributeUtilities.getStandardUnit(TimeSeriesSchema.TIME);
		logcUnit = AttributeUtilities.getStandardUnit(TimeSeriesSchema.LOGC);
		tempUnit = AttributeUtilities
				.getStandardUnit(AttributeUtilities.ATT_TEMPERATURE);
		agentID = DEFAULT_AGENTID;
		matrixID = DEFAULT_MATRIXID;
		literatureIDs = new ArrayList<>();
		timeSeriesSchema = new TimeSeriesSchema();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		Map<String, Object> mappings = new LinkedHashMap<>();

		for (String column : columnMappings.keySet()) {
			String id = columnMappings.get(column);

			if (id.equals(XLSReader.ID_COLUMN)) {
				mappings.put(column, id);
			} else if (id.equals(TimeSeriesSchema.ATT_COMMENT)) {
				mappings.put(column, id);
			} else if (id.equals(TimeSeriesSchema.TIME)) {
				mappings.put(column, id);
			} else if (id.equals(TimeSeriesSchema.LOGC)) {
				mappings.put(column, id);
			} else if (id.equals(AttributeUtilities.ATT_TEMPERATURE_ID + "")) {
				mappings.put(column, new MiscXml(
						AttributeUtilities.ATT_TEMPERATURE_ID,
						AttributeUtilities.ATT_TEMPERATURE, null, null, null));
			} else if (id.equals(AttributeUtilities.ATT_PH_ID + "")) {
				mappings.put(column, new MiscXml(AttributeUtilities.ATT_PH_ID,
						AttributeUtilities.ATT_PH, null, null, null));
			} else if (id.equals(AttributeUtilities.ATT_AW_ID + "")) {
				mappings.put(column, new MiscXml(AttributeUtilities.ATT_AW_ID,
						AttributeUtilities.ATT_WATERACTIVITY, null, null, null));
			} else {
				String name = DBKernel.getValue("SonstigeParameter", "ID", id,
						"Parameter") + "";

				mappings.put(column, new MiscXml(Integer.parseInt(id), name,
						null, null, null));
			}
		}

		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(XLSReader
				.getTimeSeriesTuples(new File(fileName), mappings).values());

		String agentName = DBKernel.getValue("Agenzien", "ID", agentID + "",
				"Agensname") + "";
		String matrixName = DBKernel.getValue("Matrices", "ID", matrixID + "",
				"Matrixname") + "";

		for (KnimeTuple tuple : tuples) {
			tuple.setValue(TimeSeriesSchema.ATT_AGENTID, agentID);
			tuple.setValue(TimeSeriesSchema.ATT_AGENTNAME, agentName);
			tuple.setValue(TimeSeriesSchema.ATT_MATRIXID, matrixID);
			tuple.setValue(TimeSeriesSchema.ATT_MATRIXNAME, matrixName);
		}

		PmmXmlDoc literatureXML = new PmmXmlDoc();

		for (int id : literatureIDs) {
			String author = DBKernel.getValue("Literatur", "ID", id + "",
					"Erstautor") + "";
			String year = DBKernel.getValue("Literatur", "ID", id + "", "Jahr")
					+ "";
			String title = DBKernel.getValue("Literatur", "ID", id + "",
					"Titel") + "";
			String mAbstract = DBKernel.getValue("Literatur", "ID", id + "",
					"Abstract") + "";

			literatureXML.add(new LiteratureItem(author,
					Integer.parseInt(year), title, mAbstract, id));
		}

		for (KnimeTuple tuple : tuples) {
			tuple.setValue(TimeSeriesSchema.ATT_LITMD, literatureXML);
		}

		BufferedDataContainer container = exec
				.createDataContainer(timeSeriesSchema.createSpec());

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc timeSeriesXml = tuple
					.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);

			for (PmmXmlElementConvertable el : timeSeriesXml.getElementSet()) {
				TimeSeriesXml element = (TimeSeriesXml) el;

				element.setTime(AttributeUtilities.convertToStandardUnit(
						TimeSeriesSchema.TIME, element.getTime(), timeUnit));
				element.setLog10C(AttributeUtilities.convertToStandardUnit(
						TimeSeriesSchema.LOGC, element.getLog10C(), logcUnit));
			}

			tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXml);

			PmmXmlDoc miscXml = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : miscXml.getElementSet()) {
				MiscXml element = (MiscXml) el;

				if (AttributeUtilities.ATT_TEMPERATURE
						.equals(element.getName())) {
					element.setValue(AttributeUtilities.convertToStandardUnit(
							AttributeUtilities.ATT_TEMPERATURE,
							element.getValue(), tempUnit));
				}
			}

			tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXml);

			container.addRowToTable(tuple);
		}

		container.close();

		return new BufferedDataTable[] { container.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		if (fileName == null) {
			throw new InvalidSettingsException("");
		}

		return new DataTableSpec[] { timeSeriesSchema.createSpec() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFGKEY_FILENAME, fileName);
		settings.addString(CFGKEY_COLUMNMAPPINGS, ListUtilities
				.getStringFromList(getMappingsAsList(columnMappings)));
		settings.addString(CFGKEY_AGENTCOLUMN, agentColumn);
		settings.addString(CFGKEY_AGENTMAPPINGS, ListUtilities
				.getStringFromList(getMappingsAsList(agentMappings)));
		settings.addString(CFGKEY_MATRIXCOLUMN, matrixColumn);
		settings.addString(CFGKEY_MATRIXMAPPINGS, ListUtilities
				.getStringFromList(getMappingsAsList(matrixMappings)));
		settings.addString(CFGKEY_TIMEUNIT, timeUnit);
		settings.addString(CFGKEY_LOGCUNIT, logcUnit);
		settings.addString(CFGKEY_TEMPUNIT, tempUnit);
		settings.addInt(CFGKEY_AGENTID, agentID);
		settings.addInt(CFGKEY_MATRIXID, matrixID);
		settings.addString(CFGKEY_LITERATUREIDS,
				ListUtilities.getStringFromList(literatureIDs));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		try {
			fileName = settings.getString(CFGKEY_FILENAME);
		} catch (InvalidSettingsException e) {
			fileName = null;
		}

		try {
			columnMappings = getMappingsAsMap(ListUtilities
					.getStringListFromString(settings
							.getString(CFGKEY_COLUMNMAPPINGS)));
		} catch (InvalidSettingsException e) {
			columnMappings = new LinkedHashMap<>();
		}

		try {
			agentColumn = settings.getString(CFGKEY_AGENTCOLUMN);
		} catch (InvalidSettingsException e) {
			agentColumn = DEFAULT_AGENTCOLUMN;
		}

		try {
			agentMappings = getMappingsAsMap(ListUtilities
					.getStringListFromString(settings
							.getString(CFGKEY_AGENTMAPPINGS)));
		} catch (InvalidSettingsException e) {
			agentMappings = new LinkedHashMap<>();
		}

		try {
			matrixColumn = settings.getString(CFGKEY_MATRIXCOLUMN);
		} catch (InvalidSettingsException e) {
			matrixColumn = DEFAULT_MATRIXCOLUMN;
		}

		try {
			matrixMappings = getMappingsAsMap(ListUtilities
					.getStringListFromString(settings
							.getString(CFGKEY_MATRIXMAPPINGS)));
		} catch (InvalidSettingsException e) {
			matrixMappings = new LinkedHashMap<>();
		}

		try {
			timeUnit = settings.getString(CFGKEY_TIMEUNIT);
		} catch (InvalidSettingsException e) {
			timeUnit = AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.TIME);
		}

		try {
			logcUnit = settings.getString(CFGKEY_LOGCUNIT);
		} catch (InvalidSettingsException e) {
			logcUnit = AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.LOGC);
		}

		try {
			tempUnit = settings.getString(CFGKEY_TEMPUNIT);
		} catch (InvalidSettingsException e) {
			tempUnit = AttributeUtilities
					.getStandardUnit(AttributeUtilities.ATT_TEMPERATURE);
		}

		try {
			agentID = settings.getInt(CFGKEY_AGENTID);
		} catch (InvalidSettingsException e) {
			agentID = DEFAULT_AGENTID;
		}

		try {
			matrixID = settings.getInt(CFGKEY_MATRIXID);
		} catch (InvalidSettingsException e) {
			matrixID = DEFAULT_MATRIXID;
		}

		try {
			literatureIDs = ListUtilities.getIntListFromString(settings
					.getString(CFGKEY_LITERATUREIDS));
		} catch (InvalidSettingsException e) {
			literatureIDs = new ArrayList<>();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	protected static Map<String, String> getMappingsAsMap(List<String> list) {
		Map<String, String> map = new LinkedHashMap<>();

		for (String mapping : list) {
			String[] toks = mapping.split("=");

			try {
				map.put(toks[0], toks[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}

		return map;
	}

	protected static List<String> getMappingsAsList(Map<String, String> map) {
		List<String> list = new ArrayList<>();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			list.add(entry.getKey() + "=" + entry.getValue());
		}

		return list;
	}

}
