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
package de.bund.bfr.knime.pmm.timeseriescreator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of TimeSeriesCreator.
 * 
 * 
 * @author Christian Thoens
 */
public class TimeSeriesCreatorNodeModel extends NodeModel {

	static final String CFGKEY_AGENT = "Agent";
	static final String CFGKEY_MATRIX = "Matrix";
	static final String CFGKEY_COMMENT = "Comment";
	static final String CFGKEY_TEMPERATURE = "Temperature";
	static final String CFGKEY_PH = "ph";
	static final String CFGKEY_WATERACTIVITY = "WaterActivity";
	static final String CFGKEY_MISCNAMES = "MiscNames";
	static final String CFGKEY_MISCVALUES = "MiscValues";
	static final String CFGKEY_TIMEVALUES = "TimeValues";
	static final String CFGKEY_LOGCVALUES = "LogcValue";
	static final String CFGKEY_TIMEUNIT = "TimeUnit";
	static final String CFGKEY_LOGCUNIT = "LogcUnit";
	static final String CFGKEY_TEMPUNIT = "TempUnit";

	private KnimeSchema schema;

	private String agent;
	private String matrix;
	private String comment;
	private Double temperature;
	private Double ph;
	private Double waterActivity;
	private List<Double> timeValues;
	private List<Double> logcValues;
	private String timeUnit;
	private String logcUnit;
	private String tempUnit;
	private List<String> miscNames;
	private List<Double> miscValues;

	/**
	 * Constructor for the node model.
	 */
	protected TimeSeriesCreatorNodeModel() {
		super(0, 1);
		schema = new TimeSeriesSchema();
		timeValues = new ArrayList<Double>();
		logcValues = new ArrayList<Double>();
		timeUnit = AttributeUtilities.getStandardUnit(TimeSeriesSchema.TIME);
		logcUnit = AttributeUtilities.getStandardUnit(TimeSeriesSchema.LOGC);
		tempUnit = AttributeUtilities
				.getStandardUnit(AttributeUtilities.ATT_TEMPERATURE);
		miscNames = new ArrayList<String>();
		miscValues = new ArrayList<Double>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		int id = MathUtilities.getRandomNegativeInt();
		PmmXmlDoc timeSeriesXml = new PmmXmlDoc();
		PmmXmlDoc miscXML = new PmmXmlDoc();

		miscXML.add(new MiscXml(AttributeUtilities.ATT_TEMPERATURE_ID,
				AttributeUtilities.ATT_TEMPERATURE, "", AttributeUtilities
						.convertToStandardUnit(
								AttributeUtilities.ATT_TEMPERATURE,
								temperature, tempUnit), ""));
		miscXML.add(new MiscXml(AttributeUtilities.ATT_PH_ID,
				AttributeUtilities.ATT_PH, "", ph, ""));
		miscXML.add(new MiscXml(AttributeUtilities.ATT_AW_ID,
				AttributeUtilities.ATT_WATERACTIVITY, "", waterActivity, ""));

		for (int i = 0; i < miscNames.size(); i++) {
			miscXML.add(new MiscXml(MathUtilities.getRandomNegativeInt(),
					miscNames.get(i), "", miscValues.get(i), ""));
		}

		for (int i = 0; i < timeValues.size(); i++) {
			timeSeriesXml.add(new TimeSeriesXml(null, AttributeUtilities
					.convertToStandardUnit(TimeSeriesSchema.TIME,
							timeValues.get(i), timeUnit), AttributeUtilities
					.convertToStandardUnit(TimeSeriesSchema.LOGC,
							logcValues.get(i), logcUnit)));
		}

		KnimeTuple tuple = new KnimeTuple(schema);

		tuple.setValue(TimeSeriesSchema.ATT_CONDID, id);
		tuple.setValue(TimeSeriesSchema.ATT_AGENTDETAIL, agent);
		tuple.setValue(TimeSeriesSchema.ATT_MATRIXDETAIL, matrix);
		tuple.setValue(TimeSeriesSchema.ATT_COMMENT, comment);
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXml);
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXML);

		container.addRowToTable(tuple);
		exec.setProgress(1, "Adding row 0");
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
		return new DataTableSpec[] { schema.createSpec() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		if (agent != null) {
			settings.addString(CFGKEY_AGENT, agent);
		}

		if (matrix != null) {
			settings.addString(CFGKEY_MATRIX, matrix);
		}

		if (comment != null) {
			settings.addString(CFGKEY_COMMENT, comment);
		}

		if (temperature != null) {
			settings.addDouble(CFGKEY_TEMPERATURE, temperature);
		}

		if (ph != null) {
			settings.addDouble(CFGKEY_PH, ph);
		}

		if (waterActivity != null) {
			settings.addDouble(CFGKEY_WATERACTIVITY, waterActivity);
		}

		settings.addString(CFGKEY_TIMEVALUES,
				ListUtilities.getStringFromList(timeValues));
		settings.addString(CFGKEY_LOGCVALUES,
				ListUtilities.getStringFromList(logcValues));
		settings.addString(CFGKEY_TIMEUNIT, timeUnit);
		settings.addString(CFGKEY_LOGCUNIT, logcUnit);
		settings.addString(CFGKEY_TEMPUNIT, tempUnit);
		settings.addString(CFGKEY_MISCNAMES,
				ListUtilities.getStringFromList(miscNames));
		settings.addString(CFGKEY_MISCVALUES,
				ListUtilities.getStringFromList(miscValues));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		try {
			agent = settings.getString(CFGKEY_AGENT);
		} catch (InvalidSettingsException e) {
			agent = null;
		}

		try {
			matrix = settings.getString(CFGKEY_MATRIX);
		} catch (InvalidSettingsException e) {
			matrix = null;
		}

		try {
			comment = settings.getString(CFGKEY_COMMENT);
		} catch (InvalidSettingsException e) {
			comment = null;
		}

		try {
			temperature = settings.getDouble(CFGKEY_TEMPERATURE);
		} catch (InvalidSettingsException e) {
			temperature = null;
		}

		try {
			ph = settings.getDouble(CFGKEY_PH);
		} catch (InvalidSettingsException e) {
			ph = null;
		}

		try {
			waterActivity = settings.getDouble(CFGKEY_WATERACTIVITY);
		} catch (InvalidSettingsException e) {
			waterActivity = null;
		}

		try {
			timeValues = ListUtilities.getDoubleListFromString(settings
					.getString(CFGKEY_TIMEVALUES));
		} catch (InvalidSettingsException e) {
			timeValues = new ArrayList<Double>();
		}

		try {
			logcValues = ListUtilities.getDoubleListFromString(settings
					.getString(CFGKEY_LOGCVALUES));
		} catch (InvalidSettingsException e) {
			logcValues = new ArrayList<Double>();
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
			miscNames = ListUtilities.getStringListFromString(settings
					.getString(CFGKEY_MISCNAMES));
		} catch (InvalidSettingsException e) {
			miscNames = new ArrayList<String>();
		}

		try {
			miscValues = ListUtilities.getDoubleListFromString(settings
					.getString(CFGKEY_MISCVALUES));
		} catch (InvalidSettingsException e) {
			miscValues = new ArrayList<Double>();
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

}
