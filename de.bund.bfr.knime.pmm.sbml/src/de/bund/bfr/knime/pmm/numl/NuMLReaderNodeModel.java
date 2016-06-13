/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.numl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.reader.DataTuple;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.numl.NuMLReader;

public class NuMLReaderNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_FILE = "filename";

	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.numl";

	// persistent state
	private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

	/**
	 * Constructor for the node model
	 */
	protected NuMLReaderNodeModel() {
		// 0 input ports and 1 input port
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		File file = new File(filename.getStringValue());
		NuMLDocument doc = NuMLReader.read(file);
		KnimeTuple tuple = new DataTuple(doc).getTuple();

		DataTableSpec dataSpec = SchemaFactory.createDataSchema().createSpec();
		BufferedDataContainer dataContainer = exec.createDataContainer(dataSpec);
		dataContainer.addRowToTable(tuple);
		dataContainer.close();

		BufferedDataTable[] table = { dataContainer.getTable() };
		return table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		filename.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		filename.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		filename.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
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
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}
}


class Util {
	private Util() {
	}

	/**
	 * Parses misc items.
	 * 
	 * @param miscs
	 *            . Dictionary that maps miscs names and their values.
	 * @return
	 */
	public static PmmXmlDoc parseMiscs(Map<String, Double> miscs) {
		PmmXmlDoc cell = new PmmXmlDoc();

		if (miscs != null) {
			// First misc item has id -1 and the rest of items have negative
			// ints
			int counter = -1;
			for (Entry<String, Double> entry : miscs.entrySet()) {
				String name = entry.getKey();
				Double value = entry.getValue();

				List<String> categories;
				String description, unit;

				switch (name) {
				case "Temperature":
					categories = Arrays.asList(Categories.getTempCategory().getName());
					description = name;
					unit = Categories.getTempCategory().getStandardUnit();

					cell.add(new MiscXml(counter, name, description, value, categories, unit));

					counter -= 1;
					break;

				case "pH":
					categories = Arrays.asList(Categories.getPhCategory().getName());
					description = name;
					unit = Categories.getPhUnit();

					cell.add(new MiscXml(counter, name, description, value, categories, unit));

					counter -= 1;
					break;
				}
			}
		}
		return cell;
	}
	
	/**
	 * Creates time series
	 */
	public static PmmXmlDoc createTimeSeries(String timeUnit, String concUnit, String concUnitObjectType,
			double[][] data) {

		PmmXmlDoc mdData = new PmmXmlDoc();

		Double concStdDev = null;
		Integer numberOfMeasurements = null;

		for (double[] point : data) {
			double conc = point[0];
			double time = point[1];
			String name = "t" + mdData.size();

			TimeSeriesXml t = new TimeSeriesXml(name, time, timeUnit, conc, concUnit, concStdDev, numberOfMeasurements);
			t.setConcentrationUnitObjectType(concUnitObjectType);
			mdData.add(t);
		}

		return mdData;
	}
}