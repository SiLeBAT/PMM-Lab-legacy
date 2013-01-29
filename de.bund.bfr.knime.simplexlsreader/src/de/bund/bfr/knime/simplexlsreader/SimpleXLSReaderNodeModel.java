package de.bund.bfr.knime.simplexlsreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * This is the model implementation of SimpleXLSReader.
 * 
 * 
 * @author Christian Thöns
 */
public class SimpleXLSReaderNodeModel extends NodeModel {

	protected static final String CFGKEY_FILENAME = "Count";

	protected static final String DEFAULT_FILENAME = "";

	private final SettingsModelString fileName = new SettingsModelString(
			CFGKEY_FILENAME, DEFAULT_FILENAME);

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
		//
		// // the data table spec of the single output table,
		// // the table will have three columns:
		// DataColumnSpec[] allColSpecs = new DataColumnSpec[3];
		// allColSpecs[0] = new DataColumnSpecCreator("Column 0",
		// StringCell.TYPE)
		// .createSpec();
		// allColSpecs[1] = new DataColumnSpecCreator("Column 1",
		// DoubleCell.TYPE)
		// .createSpec();
		// allColSpecs[2] = new DataColumnSpecCreator("Column 2", IntCell.TYPE)
		// .createSpec();
		// DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
		// // the execution context will provide us with storage capacity, in
		// this
		// // case a data container to which we will add rows sequentially
		// // Note, this container can also handle arbitrary big data tables, it
		// // will buffer to disc if necessary.
		// BufferedDataContainer container =
		// exec.createDataContainer(outputSpec);
		// // let's add m_count rows to it
		// for (int i = 0; i < m_count.getIntValue(); i++) {
		// RowKey key = new RowKey("Row " + i);
		// // the cells of the current row, the types of the cells must match
		// // the column spec (see above)
		// DataCell[] cells = new DataCell[3];
		// cells[0] = new StringCell("String_" + i);
		// cells[1] = new DoubleCell(0.5 * i);
		// cells[2] = new IntCell(i);
		// DataRow row = new DefaultRow(key, cells);
		// container.addRowToTable(row);
		//
		// // check if the execution monitor was canceled
		// exec.checkCanceled();
		// exec.setProgress(i / (double) m_count.getIntValue(), "Adding row "
		// + i);
		// }
		// // once we are done, we close the container and return its table
		// container.close();
		// BufferedDataTable out = container.getTable();
		// return new BufferedDataTable[] { out };

		return new BufferedDataTable[] {};
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
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		fileName.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		fileName.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		fileName.validateSettings(settings);
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
