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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	static final String CFGKEY_ASSIGNMENTS = "Assignments";
	static final String CFGKEY_JOINSAMECONDITIONS = "JoinSameConditions";

	static final int DEFAULT_JOINSAMECONDITIONS = 0;

	private static final int PRIMARY_JOIN = 1;
	private static final int SECONDARY_JOIN = 2;
	private static final int COMBINED_JOIN = 3;

	private List<String> assignments;
	private int joinSameConditions;

	private int joinType;

	/**
	 * Constructor for the node model.
	 */
	protected ModelAndDataJoinerNodeModel() {
		super(2, 1);
		assignments = new ArrayList<String>();
		joinSameConditions = DEFAULT_JOINSAMECONDITIONS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		Joiner joiner = null;

		if (joinType == SECONDARY_JOIN) {
			joiner = new SecondaryJoiner(inData[0], inData[1]);
		} else if (joinType == PRIMARY_JOIN) {
			joiner = new PrimaryJoiner(inData[0], inData[1],
					joinSameConditions == 1);
		} else if (joinType == COMBINED_JOIN) {
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
			KnimeSchema outSchema;

			if (model2Schema.conforms(inSpecs[0])
					&& peiSchema.conforms(inSpecs[1])) {
				outSchema = seiSchema;
				joinType = SECONDARY_JOIN;
			} else if (model12Schema.conforms(inSpecs[0])
					&& dataSchema.conforms(inSpecs[1])) {
				outSchema = seiSchema;
				joinType = COMBINED_JOIN;
			} else if (model1Schema.conforms(inSpecs[0])
					&& dataSchema.conforms(inSpecs[1])) {
				outSchema = peiSchema;
				joinType = PRIMARY_JOIN;
			} else if (model1Schema.conforms(inSpecs[1])
					&& dataSchema.conforms(inSpecs[0])) {
				throw new InvalidSettingsException("Please switch the ports!");
			} else if (model1Schema.conforms(inSpecs[1])
					&& dataSchema.conforms(inSpecs[0])) {
				throw new InvalidSettingsException("Please switch the ports!");
			} else if (model1Schema.conforms(inSpecs[1])
					&& dataSchema.conforms(inSpecs[0])) {
				throw new InvalidSettingsException("Please switch the ports!");
			} else {
				throw new InvalidSettingsException("Wrong input!");
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
		if (assignments != null) {
			settings.addStringArray(CFGKEY_ASSIGNMENTS,
					assignments.toArray(new String[0]));
		}

		settings.addInt(CFGKEY_JOINSAMECONDITIONS, joinSameConditions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		try {
			assignments = new ArrayList<String>(Arrays.asList(settings
					.getStringArray(CFGKEY_ASSIGNMENTS)));
		} catch (InvalidSettingsException e) {
			assignments = new ArrayList<String>();
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
