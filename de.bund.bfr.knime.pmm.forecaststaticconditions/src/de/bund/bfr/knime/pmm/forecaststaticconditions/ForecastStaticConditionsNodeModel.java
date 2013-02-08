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
package de.bund.bfr.knime.pmm.forecaststaticconditions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.Node;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.CollectionUtilities;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of ForecastStaticConditions.
 * 
 * 
 * @author Christian Thoens
 */
public class ForecastStaticConditionsNodeModel extends NodeModel {

	static final String CFGKEY_CONCENTRATION = "Concentration";
	static final String CFGKEY_CONCENTRATIONPARAMETERS = "ConcentrationParameters";

	static final double DEFAULT_CONCENTRATION = 3.0;

	private double concentration;
	private Map<String, String> concentrationParameters;

	private KnimeSchema peiSchema;
	private KnimeSchema seiSchema;
	private KnimeSchema schema;

	/**
	 * Constructor for the node model.
	 */
	protected ForecastStaticConditionsNodeModel() {
		super(1, 1);
		concentration = DEFAULT_CONCENTRATION;
		concentrationParameters = new LinkedHashMap<>();

		try {
			peiSchema = new KnimeSchema(new Model1Schema(),
					new TimeSeriesSchema());
			seiSchema = new KnimeSchema(new KnimeSchema(new Model1Schema(),
					new Model2Schema()), new TimeSeriesSchema());
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
		BufferedDataTable output = null;

		if (schema == seiSchema) {
			output = performSecondaryForecast(inData[0], exec);
		} else if (schema == peiSchema) {
			output = performPrimaryForecast(inData[0], exec);
		}

		return new BufferedDataTable[] { output };
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
			if (seiSchema.conforms(inSpecs[0])) {
				schema = seiSchema;
			} else if (peiSchema.conforms(inSpecs[0])) {
				schema = peiSchema;
			} else {
				throw new InvalidSettingsException("Wrong input!");
			}

			if (concentrationParameters.isEmpty()) {
				throw new InvalidSettingsException("Node has to be configured");
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
		settings.addDouble(CFGKEY_CONCENTRATION, concentration);
		settings.addString(CFGKEY_CONCENTRATIONPARAMETERS,
				CollectionUtilities.getStringFromMap(concentrationParameters));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		concentration = settings.getDouble(CFGKEY_CONCENTRATION);
		concentrationParameters = CollectionUtilities
				.getStringMapFromString(settings
						.getString(CFGKEY_CONCENTRATIONPARAMETERS));
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

	private BufferedDataTable performSecondaryForecast(BufferedDataTable table,
			ExecutionContext exec) throws Exception {
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		Map<KnimeTuple, List<KnimeTuple>> combinedTuples = ModelCombiner
				.combine(tuples, schema, true, concentrationParameters);
		Set<String> idSet = new LinkedHashSet<String>();
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		int index = 0;

		for (KnimeTuple newTuple : combinedTuples.keySet()) {

			String id = ((CatalogModelXml) newTuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID()
					+ "(" + newTuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			String oldID = ((CatalogModelXml) combinedTuples.get(newTuple)
					.get(0).getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0))
					.getID() + "";

			if (!idSet.add(id)) {
				continue;
			}

			PmmXmlDoc timeSeriesXml = newTuple
					.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			String initialParameter = concentrationParameters.get(oldID);

			if (initialParameter != null) {
				PmmXmlDoc misc = newTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				String formula = ((CatalogModelXml) newTuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).getFormula();
				PmmXmlDoc params = newTuple
						.getPmmXml(Model1Schema.ATT_PARAMETER);
				Map<String, Double> constants = new LinkedHashMap<String, Double>();

				checkPrimaryModel(newTuple, initialParameter, false);
				checkSecondaryModels(combinedTuples.get(newTuple));
				checkData(newTuple);

				for (PmmXmlElementConvertable el : params.getElementSet()) {
					ParamXml element = (ParamXml) el;

					if (element.getName().equals(initialParameter)) {
						constants.put(element.getName(), concentration);
					} else {
						constants.put(element.getName(), element.getValue());
					}
				}

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					constants.put(element.getName(), element.getValue());
				}

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					constants.put(TimeSeriesSchema.TIME, element.getTime());
					element.setLog10C(computeLogc(formula, constants));
				}
			} else {
				setWarningMessage("Initial Concentration Parameter for "
						+ ((CatalogModelXml) combinedTuples.get(newTuple)
								.get(0)
								.getPmmXml(Model1Schema.ATT_MODELCATALOG)
								.get(0)).getName() + " is not specified");

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					element.setLog10C(null);
				}
			}

			for (KnimeTuple tuple : combinedTuples.get(newTuple)) {
				PmmXmlDoc params = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);

				if (initialParameter != null) {
					((ParamXml) params.get(CellIO.getNameList(params).indexOf(
							initialParameter))).setValue(concentration);
				} else {
					for (PmmXmlElementConvertable el : params.getElementSet()) {
						ParamXml element = (ParamXml) el;

						element.setValue(null);
					}
				}

				tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXml);
				tuple.setValue(Model1Schema.ATT_PARAMETER, params);
				container.addRowToTable(tuple);
			}

			exec.setProgress((double) index / (double) combinedTuples.size(),
					"");
			exec.checkCanceled();
			index++;
		}

		container.close();

		return container.getTable();
	}

	private BufferedDataTable performPrimaryForecast(BufferedDataTable table,
			ExecutionContext exec) throws Exception {
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		int index = 0;

		for (KnimeTuple tuple : tuples) {
			String id = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID()
					+ "";
			PmmXmlDoc params = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			PmmXmlDoc timeSeriesXml = tuple
					.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			String initialParameter = concentrationParameters.get(id);

			if (initialParameter != null) {
				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				String formula = ((CatalogModelXml) tuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).getFormula();
				Map<String, Double> constants = new LinkedHashMap<String, Double>();

				((ParamXml) params.get(CellIO.getNameList(params).indexOf(
						initialParameter))).setValue(concentration);
				checkPrimaryModel(tuple, initialParameter, true);
				checkData(tuple);

				for (PmmXmlElementConvertable el : params.getElementSet()) {
					ParamXml element = (ParamXml) el;

					constants.put(element.getName(), element.getValue());
				}

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					constants.put(element.getName(), element.getValue());
				}

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					constants.put(TimeSeriesSchema.TIME, element.getTime());
					element.setLog10C(computeLogc(formula, constants));
				}
			} else {
				setWarningMessage("Initial Concentration Parameter for "
						+ ((CatalogModelXml) tuple.getPmmXml(
								Model1Schema.ATT_MODELCATALOG).get(0))
								.getName() + "is not specified");

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					element.setLog10C(null);
				}
			}

			tuple.setValue(Model1Schema.ATT_PARAMETER, params);
			tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXml);
			container.addRowToTable(tuple);
			exec.setProgress((double) index / (double) tuples.size(), "");
			exec.checkCanceled();
			index++;
		}

		container.close();

		return container.getTable();
	}

	private Double computeLogc(String formula, Map<String, Double> constants) {
		DJep parser = MathUtilities.createParser();

		for (String constant : constants.keySet()) {
			Double value = constants.get(constant);

			if (value != null) {
				parser.addConstant(constant, value);
			}
		}

		try {
			Node f = parser.parse(formula.replaceAll(TimeSeriesSchema.LOGC
					+ "=", ""));
			double value = (Double) parser.evaluate(f);

			return value;
		} catch (Exception e) {
			return null;
		}
	}

	private void checkPrimaryModel(KnimeTuple tuple, String initialParameter,
			boolean outputEstID) throws PmmException {
		String modelName = ((CatalogModelXml) tuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0)).getName();
		Integer estID = ((EstModelXml) tuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0)).getID();
		PmmXmlDoc params = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);

		for (PmmXmlElementConvertable el : params.getElementSet()) {
			ParamXml element = (ParamXml) el;

			if (!element.getName().equals(initialParameter)
					&& element.getValue() == null) {
				if (outputEstID) {
					setWarningMessage(element.getName() + " in " + modelName
							+ " (" + estID + ") is not specified");
				} else {
					setWarningMessage(element.getName() + " in " + modelName
							+ " is not specified");
				}
			}
		}
	}

	private void checkSecondaryModels(List<KnimeTuple> tuples)
			throws PmmException {
		for (KnimeTuple tuple : tuples) {
			String depVar = ((DepXml) tuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0)).getName();
			PmmXmlDoc params = tuple.getPmmXml(Model2Schema.ATT_PARAMETER);

			for (PmmXmlElementConvertable el : params.getElementSet()) {
				ParamXml element = (ParamXml) el;

				if (element.getValue() == null) {
					setWarningMessage(element.getName() + " in " + depVar
							+ "-model is not specified");
				}
			}
		}
	}

	private void checkData(KnimeTuple tuple) throws PmmException {
		int condID = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
		PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
		PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
		List<String> indepVars = new ArrayList<String>();
		List<Double> minIndepValues = new ArrayList<Double>();
		List<Double> maxIndepValues = new ArrayList<Double>();

		for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
			IndepXml element = (IndepXml) el;

			indepVars.add(element.getName());
			minIndepValues.add(element.getMin());
			maxIndepValues.add(element.getMax());
		}

		for (int i = 0; i < indepVars.size(); i++) {
			String indep = indepVars.get(i);
			Double min = minIndepValues.get(i);
			Double max = maxIndepValues.get(i);

			if (indep.equals(TimeSeriesSchema.TIME)) {
				continue;
			}

			Double value = null;

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				if (indep.equals(element.getName())) {
					value = element.getValue();
					break;
				}
			}

			if (value == null) {
				setWarningMessage(indep + " is not specified in " + condID);
			} else if ((min != null && value < min)
					|| (max != null && value > max)) {
				setWarningMessage(indep + " in " + condID + " is out of range");
			}
		}
	}
}
