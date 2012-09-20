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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of XLSTimeSeriesReader.
 * 
 * 
 * @author Christian Thoens
 */
public class XLSTimeSeriesReaderNodeModel extends NodeModel {

	static final String CFGKEY_FILENAME = "FileName";

	private String fileName;

	private KnimeSchema schema;

	/**
	 * Constructor for the node model.
	 */
	protected XLSTimeSeriesReaderNodeModel() {
		super(0, 1);
		fileName = null;
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
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();
		InputStream inputStream = new FileInputStream(new File(fileName));
		Workbook wb = WorkbookFactory.create(inputStream);
		Sheet sheet = wb.getSheetAt(0);

		KnimeTuple tuple = null;
		String id = null;

		for (int i = 1;; i++) {
			if (sheet.getRow(i).getCell(7) == null
					&& sheet.getRow(i).getCell(8) == null) {
				if (tuple != null) {
					tuples.add(tuple);
				}

				break;
			}

			if (sheet.getRow(i).getCell(0) != null
					&& !sheet.getRow(i).getCell(0).toString().equals(id)) {
				id = sheet.getRow(i).getCell(0).toString();

				if (tuple != null) {
					tuples.add(tuple);
				}

				tuple = new KnimeTuple(schema);
				tuple.setValue(TimeSeriesSchema.ATT_CONDID,
						MathUtilities.getRandomNegativeInt());

				if (sheet.getRow(i).getCell(1) != null) {
					tuple.setValue(TimeSeriesSchema.ATT_AGENTNAME, sheet
							.getRow(i).getCell(1).toString());
				}

				if (sheet.getRow(i).getCell(2) != null) {
					tuple.setValue(TimeSeriesSchema.ATT_MATRIXNAME, sheet
							.getRow(i).getCell(2).toString());
				}

				if (sheet.getRow(i).getCell(3) != null) {
					tuple.setValue(TimeSeriesSchema.ATT_COMMENT, sheet
							.getRow(i).getCell(3).toString());
				}

				if (sheet.getRow(i).getCell(4) != null
						&& !sheet.getRow(i).getCell(4).toString().trim()
								.isEmpty()) {
					try {
						tuple.setValue(
								TimeSeriesSchema.ATT_TEMPERATURE,
								Double.parseDouble(sheet.getRow(i).getCell(4)
										.toString()));
					} catch (NumberFormatException e) {
						throw new Exception("Temperature value in row "
								+ (i + 1) + " is not valid");
					}
				}

				if (sheet.getRow(i).getCell(5) != null
						&& !sheet.getRow(i).getCell(5).toString().trim()
								.isEmpty()) {
					try {
						tuple.setValue(
								TimeSeriesSchema.ATT_PH,
								Double.parseDouble(sheet.getRow(i).getCell(5)
										.toString()));
					} catch (NumberFormatException e) {
						throw new Exception("pH value in row " + (i + 1)
								+ " is not valid");
					}
				}

				if (sheet.getRow(i).getCell(6) != null
						&& !sheet.getRow(i).getCell(6).toString().trim()
								.isEmpty()) {
					try {
						tuple.setValue(
								TimeSeriesSchema.ATT_WATERACTIVITY,
								Double.parseDouble(sheet.getRow(i).getCell(6)
										.toString()));
					} catch (NumberFormatException e) {
						throw new Exception("Water Activity value in row "
								+ (i + 1) + " is not valid");
					}
				}
			}

			if (sheet.getRow(i).getCell(7) != null
					&& !sheet.getRow(i).getCell(7).toString().trim().isEmpty()) {
				try {
					tuple.addValue(
							TimeSeriesSchema.ATT_TIME,
							Double.parseDouble(sheet.getRow(i).getCell(7)
									.toString()));
				} catch (NumberFormatException e) {
					throw new Exception("Time value in row " + (i + 1)
							+ " is not valid");
				}
			}

			if (sheet.getRow(i).getCell(8) != null
					&& !sheet.getRow(i).getCell(8).toString().trim().isEmpty()) {
				try {
					tuple.addValue(
							TimeSeriesSchema.ATT_LOGC,
							Double.parseDouble(sheet.getRow(i).getCell(8)
									.toString()));
				} catch (NumberFormatException e) {
					throw new Exception("LogC value in row " + (i + 1)
							+ " is not valid");
				}
			}
		}

		return tuples;
	}

}
