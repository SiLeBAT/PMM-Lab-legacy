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
package de.bund.bfr.knime.pmm.modelanddatajoiner;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
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
 * This is the model implementation of ModelAndDataJoiner.
 * 
 * 
 * @author Christian Thoens
 */
public class ModelAndDataJoinerNodeModel extends NodeModel {

	private SettingsHelper set;

	/**
	 * Constructor for the node model.
	 */
	protected ModelAndDataJoinerNodeModel() {
		super(2, 1);
		set = new SettingsHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		Joiner joiner = null;

		if (set.getJoinType().equals(SettingsHelper.PRIMARY_JOIN)) {
			joiner = new PrimaryJoiner(inData[0], inData[1]);
		} else if (set.getJoinType().equals(SettingsHelper.SECONDARY_JOIN)) {
			joiner = new SecondaryJoiner(inData[0], inData[1]);
		} else if (set.getJoinType().equals(SettingsHelper.COMBINED_JOIN)) {
			joiner = new CombinedJoiner(inData[0], inData[1]);
		}

		return new BufferedDataTable[] { joiner.getOutputTable(
				set.getAssignments(), exec) };
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
		KnimeSchema outSchema = null;

		if (set.getJoinType().equals(SettingsHelper.NO_JOIN)) {
			throw new InvalidSettingsException("Node has to be configured!");
		} else if (set.getJoinType().equals(SettingsHelper.PRIMARY_JOIN)) {
			if (SchemaFactory.createM1Schema().conforms(inSpecs[0])
					&& SchemaFactory.createDataSchema().conforms(inSpecs[1])) {
				outSchema = SchemaFactory.createM1DataSchema();
			} else if (SchemaFactory.createM1Schema().conforms(inSpecs[1])
					&& SchemaFactory.createDataSchema().conforms(inSpecs[0])) {
				throw new InvalidSettingsException("Please switch the ports!");
			} else {
				throw new InvalidSettingsException("Wrong input!");
			}
		} else if (set.getJoinType().equals(SettingsHelper.SECONDARY_JOIN)) {
			if (SchemaFactory.createM2Schema().conforms(inSpecs[0])
					&& SchemaFactory.createM1DataSchema().conforms(inSpecs[1])) {
				outSchema = SchemaFactory.createM12DataSchema();
			} else if (SchemaFactory.createM2Schema().conforms(inSpecs[1])
					&& SchemaFactory.createM1DataSchema().conforms(inSpecs[0])) {
				throw new InvalidSettingsException("Please switch the ports!");
			} else {
				throw new InvalidSettingsException("Wrong input!");
			}
		} else if (set.getJoinType().equals(SettingsHelper.COMBINED_JOIN)) {
			if (SchemaFactory.createM12Schema().conforms(inSpecs[0])
					&& SchemaFactory.createDataSchema().conforms(inSpecs[1])) {
				outSchema = SchemaFactory.createM12DataSchema();
			} else if (SchemaFactory.createM12Schema().conforms(inSpecs[1])
					&& SchemaFactory.createDataSchema().conforms(inSpecs[0])) {
				throw new InvalidSettingsException("Please switch the ports!");
			} else {
				throw new InvalidSettingsException("Wrong input!");
			}
		}

		return new DataTableSpec[] { outSchema.createSpec() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		set.saveSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		set.loadSettings(settings);
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
