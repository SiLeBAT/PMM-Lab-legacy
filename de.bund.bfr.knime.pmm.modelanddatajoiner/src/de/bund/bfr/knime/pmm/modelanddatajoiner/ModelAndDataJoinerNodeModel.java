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

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of ModelAndDataJoiner.
 * 
 * 
 * @author Christian Thoens
 */
public class ModelAndDataJoinerNodeModel extends NodeModel {

	static final String NO_JOIN = "";
	static final String PRIMARY_JOIN = "Primary Join";
	static final String SECONDARY_JOIN = "Secondary Join";
	static final String COMBINED_JOIN = "Combined Join";

	static final String CFGKEY_JOINTYPE = "JoinType";
	static final String CFGKEY_ASSIGNMENTS = "Assignments";
	static final String CFGKEY_JOINSAMECONDITIONS = "JoinSameConditions";

	static final String DEFAULT_JOINTYPE = NO_JOIN;
	static final int DEFAULT_JOINSAMECONDITIONS = 0;

	private String joinType;
	private String assignments;
	private int joinSameConditions;

	/**
	 * Constructor for the node model.
	 */
	protected ModelAndDataJoinerNodeModel() {
		super(2, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		Joiner joiner = null;

		if (joinType.equals(PRIMARY_JOIN)) {
			joiner = new PrimaryJoiner(inData[0], inData[1],
					joinSameConditions == 1);
		} else if (joinType.equals(SECONDARY_JOIN)) {
			joiner = new SecondaryJoiner(inData[0], inData[1]);
		} else if (joinType.equals(COMBINED_JOIN)) {
			joiner = new CombinedJoiner(inData[0], inData[1]);
		}

		return new BufferedDataTable[] { joiner.getOutputTable(assignments,
				exec) };
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
			KnimeSchema model1Schema = new Model1Schema();
			KnimeSchema model2Schema = new Model2Schema();
			KnimeSchema model12Schema = new KnimeSchema(new Model1Schema(),
					new Model2Schema());
			KnimeSchema dataSchema = new TimeSeriesSchema();
			KnimeSchema peiSchema = new KnimeSchema(new Model1Schema(),
					new TimeSeriesSchema());
			KnimeSchema seiSchema = new KnimeSchema(new KnimeSchema(
					new Model1Schema(), new Model2Schema()),
					new TimeSeriesSchema());
			KnimeSchema outSchema = null;

			if (joinType.equals(NO_JOIN)) {
				throw new InvalidSettingsException("Node has to be configured!");
			} else if (joinType.equals(PRIMARY_JOIN)) {
				if (model1Schema.conforms(inSpecs[0])
						&& dataSchema.conforms(inSpecs[1])) {
					outSchema = peiSchema;
				} else if (model1Schema.conforms(inSpecs[1])
						&& dataSchema.conforms(inSpecs[0])) {
					throw new InvalidSettingsException(
							"Please switch the ports!");
				} else {
					throw new InvalidSettingsException("Wrong input!");
				}
			} else if (joinType.equals(SECONDARY_JOIN)) {
				if (model2Schema.conforms(inSpecs[0])
						&& peiSchema.conforms(inSpecs[1])) {
					outSchema = seiSchema;
				} else if (model2Schema.conforms(inSpecs[1])
						&& peiSchema.conforms(inSpecs[0])) {
					throw new InvalidSettingsException(
							"Please switch the ports!");
				} else {
					throw new InvalidSettingsException("Wrong input!");
				}
			} else if (joinType.equals(COMBINED_JOIN)) {
				if (model12Schema.conforms(inSpecs[0])
						&& dataSchema.conforms(inSpecs[1])) {
					outSchema = seiSchema;
				} else if (model12Schema.conforms(inSpecs[1])
						&& dataSchema.conforms(inSpecs[0])) {
					throw new InvalidSettingsException(
							"Please switch the ports!");
				} else {
					throw new InvalidSettingsException("Wrong input!");
				}
			}

			return new DataTableSpec[] { outSchema.createSpec() };
		} catch (PmmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFGKEY_JOINTYPE, joinType);
		settings.addString(CFGKEY_ASSIGNMENTS, assignments);
		settings.addInt(CFGKEY_JOINSAMECONDITIONS, joinSameConditions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		try {
			joinType = settings.getString(CFGKEY_JOINTYPE);
		} catch (InvalidSettingsException e) {
			joinType = DEFAULT_JOINTYPE;
		}

		try {
			assignments = settings.getString(CFGKEY_ASSIGNMENTS);
		} catch (InvalidSettingsException e) {
			assignments = "";
		}

		try {
			joinSameConditions = settings.getInt(CFGKEY_JOINSAMECONDITIONS);
		} catch (InvalidSettingsException e) {
			joinSameConditions = DEFAULT_JOINSAMECONDITIONS;
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

}
