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
package de.bund.bfr.knime.gis.regiontoregionvisualizer;

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
 * This is the model implementation of RegionToRegionVisualizer.
 * 
 * 
 * @author Christian Thoens
 */
public class RegionToRegionVisualizerNodeModel extends NodeModel {

	protected static final String CFG_FILENAME = "FileName";
	protected static final String CFG_FILEIDCOLUMN = "FileIDColumn";
	protected static final String CFG_TABLEIDCOLUMN = "TableIDColumn";
	protected static final String CFG_TABLEVALUECOLUMN = "TableValueColumn";
	protected static final String CFG_EDGEFROMCOLUMN = "EdgeFromColumn";
	protected static final String CFG_EDGETOCOLUMN = "EdgeToColumn";
	protected static final String CFG_EDGEVALUECOLUMN = "EdgeValueColumn";

	private static final String INTERNAL_FILENAME1 = "RegionToRegionVisualizer1.zip";
	private static final String INTERNAL_FILENAME2 = "RegionToRegionVisualizer2.zip";

	private DataTable regionTable;
	private DataTable edgeTable;
	private String fileName;
	private String fileIdColumn;
	private String tableIdColumn;
	private String tableValueColumn;
	private String edgeFromColumn;
	private String edgeToColumn;
	private String edgeValueColumn;

	/**
	 * Constructor for the node model.
	 */
	protected RegionToRegionVisualizerNodeModel() {
		super(2, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		regionTable = inData[0];
		edgeTable = inData[1];

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
		settings.addString(CFG_EDGEFROMCOLUMN, edgeFromColumn);
		settings.addString(CFG_EDGETOCOLUMN, edgeToColumn);
		settings.addString(CFG_EDGEVALUECOLUMN, edgeValueColumn);
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

		try {
			edgeFromColumn = settings.getString(CFG_EDGEFROMCOLUMN);
		} catch (InvalidSettingsException e) {
			edgeFromColumn = "";
		}

		try {
			edgeToColumn = settings.getString(CFG_EDGETOCOLUMN);
		} catch (InvalidSettingsException e) {
			edgeToColumn = "";
		}

		try {
			edgeValueColumn = settings.getString(CFG_EDGEVALUECOLUMN);
		} catch (InvalidSettingsException e) {
			edgeValueColumn = "";
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
		regionTable = DataContainer.readFromZip(new File(internDir,
				INTERNAL_FILENAME1));
		edgeTable = DataContainer.readFromZip(new File(internDir,
				INTERNAL_FILENAME2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		DataContainer.writeToZip(regionTable, new File(internDir,
				INTERNAL_FILENAME1), exec);
		DataContainer.writeToZip(edgeTable, new File(internDir,
				INTERNAL_FILENAME2), exec);
	}

	protected DataTable getRegionTable() {
		return regionTable;
	}

	protected DataTable getEdgeTable() {
		return edgeTable;
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

	protected String getEdgeFromColumn() {
		return edgeFromColumn;
	}

	protected String getEdgeToColumn() {
		return edgeToColumn;
	}

	protected String getEdgeValueColumn() {
		return edgeValueColumn;
	}

}
