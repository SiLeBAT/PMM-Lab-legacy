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
package de.bund.bfr.knime.pmm.parameterrenamer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;

/**
 * This is the model implementation of ParameterRenamer.
 * 
 * 
 * @author Christian Thoens
 */
public class ParameterRenamerNodeModel extends NodeModel {

	static final String CFGKEY_ASSIGNMENTS = "Assignments";

	private List<String> assignments;

	private KnimeSchema schema;

	/**
	 * Constructor for the node model.
	 */
	protected ParameterRenamerNodeModel() {
		super(1, 1);
		schema = new Model1Schema();
		assignments = new ArrayList<String>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		KnimeRelationReader reader = new KnimeRelationReader(schema, inData[0]);
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		Map<String, String> replacements = new HashMap<String, String>();
		int index = 0;

		for (String assign : assignments) {
			String[] toks = assign.split("=");

			replacements.put(toks[0], toks[1]);
		}

		while (reader.hasMoreElements()) {
			KnimeTuple row = reader.nextElement();
			List<String> params = row.getStringList(Model1Schema.ATT_PARAMNAME);
			String formula = row.getString(Model1Schema.ATT_FORMULA);

			for (int i = 0; i < params.size(); i++) {
				if (replacements.containsKey(params.get(i))) {
					params.set(i, replacements.get(params.get(i)));
				}
			}

			for (String param1 : replacements.keySet()) {
				String param2 = replacements.get(param1);

				formula = MathUtilities
						.replaceVariable(formula, param1, param2);
			}

			row.setValue(Model1Schema.ATT_PARAMNAME, params);
			row.setValue(Model1Schema.ATT_FORMULA, formula);

			container.addRowToTable(row);
			exec.checkCanceled();
			exec.setProgress((double) index / (double) inData[0].getRowCount(),
					"");
			index++;
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
			if (!schema.conforms(inSpecs[0])) {
				throw new InvalidSettingsException("Wrong input!");
			}

			return new DataTableSpec[] { schema.createSpec() };
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
