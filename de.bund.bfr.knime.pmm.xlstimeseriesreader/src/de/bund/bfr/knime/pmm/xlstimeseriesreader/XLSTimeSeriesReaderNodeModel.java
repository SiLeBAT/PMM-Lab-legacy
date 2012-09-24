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
import java.util.List;
import java.util.Map;

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

	static final String CFGKEY_FILENAME = "FileName";
	static final String CFGKEY_TIMEUNIT = "TimeUnit";
	static final String CFGKEY_LOGCUNIT = "LogcUnit";
	static final String CFGKEY_TEMPUNIT = "TempUnit";

	private String fileName;
	private String timeUnit;
	private String logcUnit;
	private String tempUnit;

	private KnimeSchema schema;

	/**
	 * Constructor for the node model.
	 */
	protected XLSTimeSeriesReaderNodeModel() {
		super(0, 1);
		fileName = null;
		timeUnit = AttributeUtilities
				.getStandardUnit(TimeSeriesSchema.ATT_TIME);
		logcUnit = AttributeUtilities
				.getStandardUnit(TimeSeriesSchema.ATT_LOGC);
		tempUnit = AttributeUtilities
				.getStandardUnit(TimeSeriesSchema.ATT_TEMPERATURE);
		schema = new TimeSeriesSchema();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		List<KnimeTuple> tuples = readTableFromXLS(fileName, schema);
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());

		for (KnimeTuple tuple : tuples) {
			List<Double> timeList = tuple
					.getDoubleList(TimeSeriesSchema.ATT_TIME);
			List<Double> logcList = tuple
					.getDoubleList(TimeSeriesSchema.ATT_LOGC);
			Double temp = tuple.getDouble(TimeSeriesSchema.ATT_TEMPERATURE);

			for (int i = 0; i < timeList.size(); i++) {
				timeList.set(i, AttributeUtilities.convertToStandardUnit(
						TimeSeriesSchema.ATT_TIME, timeList.get(i), timeUnit));
				logcList.set(i, AttributeUtilities.convertToStandardUnit(
						TimeSeriesSchema.ATT_LOGC, logcList.get(i), logcUnit));
			}

			temp = AttributeUtilities.convertToStandardUnit(
					TimeSeriesSchema.ATT_TEMPERATURE, temp, tempUnit);

			tuple.setValue(TimeSeriesSchema.ATT_TIME, timeList);
			tuple.setValue(TimeSeriesSchema.ATT_LOGC, logcList);
			tuple.setValue(TimeSeriesSchema.ATT_TEMPERATURE, temp);
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

		return new DataTableSpec[] { schema.createSpec() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFGKEY_FILENAME, fileName);
		settings.addString(CFGKEY_TIMEUNIT, timeUnit);
		settings.addString(CFGKEY_LOGCUNIT, logcUnit);
		settings.addString(CFGKEY_TEMPUNIT, tempUnit);
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
			timeUnit = settings.getString(CFGKEY_TIMEUNIT);
		} catch (InvalidSettingsException e) {
			timeUnit = AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_TIME);
		}

		try {
			logcUnit = settings.getString(CFGKEY_LOGCUNIT);
		} catch (InvalidSettingsException e) {
			logcUnit = AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_LOGC);
		}

		try {
			tempUnit = settings.getString(CFGKEY_TEMPUNIT);
		} catch (InvalidSettingsException e) {
			tempUnit = AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_TEMPERATURE);
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

	protected static List<KnimeTuple> readTableFromXLS(String fileName,
			KnimeSchema schema) throws Exception {
		Map<String, KnimeTuple> tuples = XLSReader
				.getTuples(new File(fileName));

		return new ArrayList<KnimeTuple>(tuples.values());
	}

}
