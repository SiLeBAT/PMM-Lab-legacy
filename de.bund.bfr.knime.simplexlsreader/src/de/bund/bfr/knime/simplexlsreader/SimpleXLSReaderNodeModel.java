package de.bund.bfr.knime.simplexlsreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * This is the model implementation of SimpleXLSReader.
 * 
 * 
 * @author Christian Thöns
 */
public class SimpleXLSReaderNodeModel extends NodeModel {

	protected static final String CFGKEY_FILENAME = "FileName";
	protected static final String CFGKEY_SHEETINDEX = "SheetIndex";

	protected static final String DEFAULT_FILENAME = "";
	protected static final int DEFAULT_SHEETINDEX = 0;

	private SettingsModelString fileName = new SettingsModelString(
			CFGKEY_FILENAME, DEFAULT_FILENAME);
	private SettingsModelInteger sheetIndex = new SettingsModelInteger(
			CFGKEY_SHEETINDEX, DEFAULT_SHEETINDEX);

	private DataTableSpec spec;
	private Map<String, Integer> columns;

	/**
	 * Constructor for the node model.
	 */
	protected SimpleXLSReaderNodeModel() {
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		InputStream inputStream = new FileInputStream(fileName.getStringValue());
		Workbook wb = WorkbookFactory.create(inputStream);

		inputStream.close();

		Sheet sheet = wb.getSheetAt(sheetIndex.getIntValue());
		BufferedDataContainer container = exec.createDataContainer(spec);

		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			Row xlsRow = sheet.getRow(i);

			if (xlsRow == null) {
				break;
			}

			boolean isEmpty = true;
			DataCell[] cells = new DataCell[spec.getNumColumns()];

			for (int j = 0; j < spec.getNumColumns(); j++) {
				String columnName = spec.getColumnNames()[j];
				Cell xlsCell = xlsRow.getCell(columns.get(columnName));

				if (xlsCell != null && !xlsCell.toString().trim().isEmpty()) {
					cells[j] = new StringCell(xlsCell.toString().trim());
					isEmpty = false;
				} else {
					cells[j] = new StringCell("");
				}
			}

			if (isEmpty) {
				break;
			}

			container.addRowToTable(new DefaultRow(i + "", cells));
			exec.checkCanceled();
			exec.setProgress((double) i / (double) sheet.getLastRowNum(),
					"Adding row " + i);
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
		try {
			InputStream inputStream = new FileInputStream(
					fileName.getStringValue());
			Workbook wb = WorkbookFactory.create(inputStream);

			inputStream.close();

			Sheet sheet = wb.getSheetAt(sheetIndex.getIntValue());
			Row row = sheet.getRow(0);
			List<DataColumnSpec> columnSpecs = new ArrayList<>();

			columns = new LinkedHashMap<>();

			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);

				if (cell != null && !cell.toString().trim().isEmpty()) {
					String name = cell.toString().trim();

					columns.put(name, i);
					columnSpecs.add(new DataColumnSpecCreator(name,
							StringCell.TYPE).createSpec());
				}
			}

			spec = new DataTableSpec(columnSpecs.toArray(new DataColumnSpec[0]));

			return new DataTableSpec[] { spec };
		} catch (InvalidFormatException e) {
		} catch (IOException e) {
		}

		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		fileName.saveSettingsTo(settings);
		sheetIndex.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		fileName.loadSettingsFrom(settings);
		sheetIndex.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		fileName.validateSettings(settings);
		sheetIndex.validateSettings(settings);
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
