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
package de.bund.bfr.knime.pmm.predictorview;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import de.bund.bfr.knime.pmm.common.CollectionUtilities;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;

/**
 * This is the model implementation of PredictorView.
 * 
 * 
 * @author Christian Thoens
 */
public class PredictorViewNodeModel extends NodeModel {

	protected static final String CFGKEY_CONCENTRATIONPARAMETERS = "ConcentrationParameters";

	private static final String CFG_FILENAME = "PredictorView.zip";

	private DataTable table;
	private KnimeSchema schema;
	private KnimeSchema model1Schema;
	private KnimeSchema model12Schema;

	private List<String> concentrationParameters;

	/**
	 * Constructor for the node model.
	 */
	protected PredictorViewNodeModel() {
		super(1, 0);
		concentrationParameters = new ArrayList<String>();

		try {
			model1Schema = new Model1Schema();
			model12Schema = new KnimeSchema(new Model1Schema(),
					new Model2Schema());
		} catch (PmmException e) {
			e.printStackTrace();
		}
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
		try {
			if (model12Schema.conforms(inSpecs[0])) {
				schema = model12Schema;
			} else if (model1Schema.conforms(inSpecs[0])) {
				schema = model1Schema;
			} else {
				throw new InvalidSettingsException("Wrong input!");
			}

			if (concentrationParameters.isEmpty()) {
				throw new InvalidSettingsException("Node has to be configured");
			}

			return new DataTableSpec[] {};
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
		settings.addString(CFGKEY_CONCENTRATIONPARAMETERS,
				CollectionUtilities.getStringFromList(concentrationParameters));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		try {
			concentrationParameters = CollectionUtilities
					.getStringListFromString(settings
							.getString(CFGKEY_CONCENTRATIONPARAMETERS));
		} catch (InvalidSettingsException e) {
			concentrationParameters = new ArrayList<String>();
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
		File f = new File(internDir, CFG_FILENAME);

		table = DataContainer.readFromZip(f);

		try {
			if (model12Schema.conforms(table.getDataTableSpec())) {
				schema = model12Schema;
			} else if (model1Schema.conforms(table.getDataTableSpec())) {
				schema = model1Schema;
			}
		} catch (PmmException e) {
			e.printStackTrace();
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

	protected Map<String, String> getConcentrationParameters() {
		return toParameterMap(concentrationParameters);
	}

	protected boolean isModel1Schema() {
		return schema == model1Schema;
	}

	protected boolean isModel12Schema() {
		return schema == model12Schema;
	}

	protected static Map<String, String> toParameterMap(List<String> parameters) {
		Map<String, String> parameterMap = new LinkedHashMap<String, String>();

		for (String p : parameters) {
			String[] toks = p.split(":");
			String modelID = toks[0];
			String param = toks[1];

			if (param.equals("?")) {
				param = null;
			}

			parameterMap.put(modelID, param);
		}

		return parameterMap;
	}

	protected static List<String> toParameterList(
			Map<String, String> parameterMap) {
		List<String> parameters = new ArrayList<String>();

		for (String modelID : parameterMap.keySet()) {
			String param = parameterMap.get(modelID);

			if (param == null) {
				param = "?";
			}

			parameters.add(modelID + ":" + param);
		}

		return parameters;
	}

}
