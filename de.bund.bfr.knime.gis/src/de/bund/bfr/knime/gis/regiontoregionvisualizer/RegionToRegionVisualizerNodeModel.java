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
	protected static final String CFG_FILE_REGION_ID_COLUMN = "FileRegionIDColumn";
	protected static final String CFG_NODE_ID_COLUMN = "NodeIDColumn";
	protected static final String CFG_NODE_REGION_ID_COLUMN = "NodeRegionIDColumn";
	protected static final String CFG_SKIP_EDGELESS_NODES = "SkipEdgelessNodes";
	protected static final String CFG_EDGE_FROM_COLUMN = "EdgeFromColumn";
	protected static final String CFG_EDGE_TO_COLUMN = "EdgeToColumn";

	protected static final boolean DEFAULT_SKIP_EDGELESS_NODES = true;

	private static final String INTERNAL_FILENAME1 = "RegionToRegionVisualizer1.zip";
	private static final String INTERNAL_FILENAME2 = "RegionToRegionVisualizer2.zip";

	private DataTable nodeTable;
	private DataTable edgeTable;
	private String fileName;
	private String fileRegionIdColumn;
	private String nodeIdColumn;
	private String nodeRegionIdColumn;
	private boolean skipEdgelessNodes;
	private String edgeFromColumn;
	private String edgeToColumn;

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
		nodeTable = inData[0];
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
		settings.addString(CFG_FILE_REGION_ID_COLUMN, fileRegionIdColumn);
		settings.addString(CFG_NODE_ID_COLUMN, nodeIdColumn);
		settings.addString(CFG_NODE_REGION_ID_COLUMN, nodeRegionIdColumn);
		settings.addBoolean(CFG_SKIP_EDGELESS_NODES, skipEdgelessNodes);
		settings.addString(CFG_EDGE_FROM_COLUMN, edgeFromColumn);
		settings.addString(CFG_EDGE_TO_COLUMN, edgeToColumn);
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
			fileRegionIdColumn = settings.getString(CFG_FILE_REGION_ID_COLUMN);
		} catch (InvalidSettingsException e) {
			fileRegionIdColumn = "";
		}

		try {
			nodeIdColumn = settings.getString(CFG_NODE_ID_COLUMN);
		} catch (InvalidSettingsException e) {
			nodeIdColumn = "";
		}

		try {
			nodeRegionIdColumn = settings.getString(CFG_NODE_REGION_ID_COLUMN);
		} catch (InvalidSettingsException e) {
			nodeRegionIdColumn = "";
		}

		try {
			skipEdgelessNodes = settings.getBoolean(CFG_SKIP_EDGELESS_NODES);
		} catch (InvalidSettingsException e) {
			skipEdgelessNodes = DEFAULT_SKIP_EDGELESS_NODES;
		}

		try {
			edgeFromColumn = settings.getString(CFG_EDGE_FROM_COLUMN);
		} catch (InvalidSettingsException e) {
			edgeFromColumn = "";
		}

		try {
			edgeToColumn = settings.getString(CFG_EDGE_TO_COLUMN);
		} catch (InvalidSettingsException e) {
			edgeToColumn = "";
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
		nodeTable = DataContainer.readFromZip(new File(internDir,
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
		DataContainer.writeToZip(nodeTable, new File(internDir,
				INTERNAL_FILENAME1), exec);
		DataContainer.writeToZip(edgeTable, new File(internDir,
				INTERNAL_FILENAME2), exec);
	}

	protected DataTable getNodeTable() {
		return nodeTable;
	}

	protected DataTable getEdgeTable() {
		return edgeTable;
	}

	protected String getFileName() {
		return fileName;
	}

	protected String getFileRegionIdColumn() {
		return fileRegionIdColumn;
	}

	protected String getNodeIdColumn() {
		return nodeIdColumn;
	}

	protected String getNodeRegionIdColumn() {
		return nodeRegionIdColumn;
	}

	protected boolean isSkipEdgelessNodes() {
		return skipEdgelessNodes;
	}

	protected String getEdgeFromColumn() {
		return edgeFromColumn;
	}

	protected String getEdgeToColumn() {
		return edgeToColumn;
	}

}
