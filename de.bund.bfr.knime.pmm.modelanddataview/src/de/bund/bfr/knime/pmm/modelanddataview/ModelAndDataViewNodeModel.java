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
package de.bund.bfr.knime.pmm.modelanddataview;

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

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * This is the model implementation of ModelAndDataView.
 * 
 * 
 * @author Christian Thoens
 */
public class ModelAndDataViewNodeModel extends NodeModel {

	protected static final String CFG_FILENAME = "DataView.zip";

	protected static final String CFG_CONTAINSDATA = "ContainsData";
	protected static final int DEFAULT_CONTAINSDATA = 1;

	private DataTable table;
	private KnimeSchema schema;

	private int containsData;

	/**
	 * Constructor for the node model.
	 */
	protected ModelAndDataViewNodeModel() {
		super(1, 0);
		containsData = DEFAULT_CONTAINSDATA;
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
		if (SchemaFactory.createM1DataSchema().conforms(inSpecs[0])) {
			schema = SchemaFactory.createM1DataSchema();
		} else if (SchemaFactory.createM1Schema().conforms(inSpecs[0])) {
			schema = SchemaFactory.createM1Schema();

			if (containsData == 1) {
				containsData = 0;
			}
		} else {
			throw new InvalidSettingsException("Wrong input!");
		}

		return new DataTableSpec[] {};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addInt(CFG_CONTAINSDATA, containsData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		containsData = settings.getInt(CFG_CONTAINSDATA);
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
		File f = new File(internDir, CFG_FILENAME);

		table = DataContainer.readFromZip(f);

		if (SchemaFactory.createM1DataSchema().conforms(table)) {
			schema = SchemaFactory.createM1DataSchema();
		} else if (SchemaFactory.createM1Schema().conforms(table)) {
			schema = SchemaFactory.createM1Schema();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		File f = new File(internDir, CFG_FILENAME);

		DataContainer.writeToZip(table, f, exec);
	}

	protected DataTable getTable() {
		return table;
	}

	protected KnimeSchema getSchema() {
		return schema;
	}

	protected int getContainsData() {
		return containsData;
	}
}
