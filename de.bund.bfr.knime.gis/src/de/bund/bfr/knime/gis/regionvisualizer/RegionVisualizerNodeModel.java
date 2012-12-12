package de.bund.bfr.knime.gis.regionvisualizer;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.DataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of RegionVisualizer.
 * 
 * 
 * @author Christian Thoens
 */
public class RegionVisualizerNodeModel extends NodeModel {

	protected static final String CFG_FILENAME = "FileName";
	protected static final String CFG_FILEIDCOLUMN = "FileIDColumn";
	protected static final String CFG_TABLEIDCOLUMN = "TableIDColumn";
	protected static final String CFG_TABLEVALUECOLUMN = "TableValueColumn";

	private static final String INTERNAL_FILENAME = "GISVisualization.zip";

	private DataTable table;
	private String fileName;
	private String fileIdColumn;
	private String tableIdColumn;
	private String tableValueColumn;

	/**
	 * Constructor for the node model.
	 */
	protected RegionVisualizerNodeModel() {
		super(1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		table = inData[0];

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
		return new DataTableSpec[] {};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFG_FILENAME, fileName);
		settings.addString(CFG_FILEIDCOLUMN, fileIdColumn);
		settings.addString(CFG_TABLEIDCOLUMN, tableIdColumn);
		settings.addString(CFG_TABLEVALUECOLUMN, tableValueColumn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		try {
			fileName = settings.getString(CFG_FILENAME);
		} catch (InvalidSettingsException e) {
			fileName = "";
		}

		try {
			fileIdColumn = settings.getString(CFG_FILEIDCOLUMN);
		} catch (InvalidSettingsException e) {
			fileIdColumn = "";
		}

		try {
			tableIdColumn = settings.getString(CFG_TABLEIDCOLUMN);
		} catch (InvalidSettingsException e) {
			tableIdColumn = "";
		}

		try {
			tableValueColumn = settings.getString(CFG_TABLEVALUECOLUMN);
		} catch (InvalidSettingsException e) {
			tableValueColumn = "";
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
		table = DataContainer
				.readFromZip(new File(internDir, INTERNAL_FILENAME));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		DataContainer.writeToZip(table, new File(internDir, INTERNAL_FILENAME),
				exec);
	}

	protected DataTable getTable() {
		return table;
	}

	protected String getFileName() {
		return fileName;
	}

	protected String getFileIdColumn() {
		return fileIdColumn;
	}

	protected String getTableIdColumn() {
		return tableIdColumn;
	}

	protected String getTableValueColumn() {
		return tableValueColumn;
	}

}
