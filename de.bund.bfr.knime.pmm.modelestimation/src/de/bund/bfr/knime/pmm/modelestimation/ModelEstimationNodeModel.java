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
package de.bund.bfr.knime.pmm.modelestimation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.combine.ModelCombiner;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.math.ParameterOptimizer;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of ModelEstimation.
 * 
 * 
 * @author Christian Thoens
 */
public class ModelEstimationNodeModel extends NodeModel {

	static final String CFGKEY_ENFORCELIMITS = "EnforceLimits";
	static final String CFGKEY_ONESTEPMETHOD = "OneStepMethod";
	static final int DEFAULT_ENFORCELIMITS = 0;
	static final int DEFAULT_ONESTEPMETHOD = 0;

	private static final int MAX_THREADS = 8;

	private KnimeSchema peiSchema;
	private KnimeSchema seiSchema;
	private KnimeSchema schema;

	private AtomicInteger runningThreads;
	private AtomicInteger finishedThreads;

	private int enforceLimits;
	private int oneStepMethod;

	/**
	 * Constructor for the node model.
	 */
	protected ModelEstimationNodeModel() {
		super(1, 1);
		enforceLimits = DEFAULT_ENFORCELIMITS;
		oneStepMethod = DEFAULT_ONESTEPMETHOD;

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
		BufferedDataTable table = inData[0];
		BufferedDataTable outTable = null;

		if (schema == peiSchema) {
			outTable = doPrimaryEstimation(table, exec);
		} else if (schema == seiSchema && oneStepMethod != 1) {
			outTable = doSecondaryEstimation(table, exec);
		} else if (schema == seiSchema && oneStepMethod == 1) {
			outTable = doOneStepEstimation(table, exec);
		}

		return new BufferedDataTable[] { outTable };
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
			KnimeSchema outSchema = null;

			if (seiSchema.conforms((DataTableSpec) inSpecs[0])) {
				schema = seiSchema;

				if (oneStepMethod != 1) {
					outSchema = seiSchema;
				} else {
					outSchema = peiSchema;
				}
			} else if (peiSchema.conforms((DataTableSpec) inSpecs[0])) {
				schema = peiSchema;
				outSchema = peiSchema;
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
		settings.addInt(CFGKEY_ENFORCELIMITS, enforceLimits);
		settings.addInt(CFGKEY_ONESTEPMETHOD, oneStepMethod);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		try {
			enforceLimits = settings.getInt(CFGKEY_ENFORCELIMITS);
		} catch (InvalidSettingsException e) {
			enforceLimits = DEFAULT_ENFORCELIMITS;
		}

		try {
			oneStepMethod = settings.getInt(CFGKEY_ONESTEPMETHOD);
		} catch (InvalidSettingsException e) {
			oneStepMethod = DEFAULT_ONESTEPMETHOD;
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

	private BufferedDataTable doPrimaryEstimation(BufferedDataTable table,
			ExecutionContext exec) throws PmmException,
			CanceledExecutionException, InterruptedException {
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		int n = table.getRowCount();
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(n);

		runningThreads = new AtomicInteger(0);
		finishedThreads = new AtomicInteger(0);

		for (int i = 0; i < n; i++) {
			tuples.add(reader.nextElement());
		}

		for (KnimeTuple tuple : tuples) {
			while (true) {
				exec.checkCanceled();
				exec.setProgress((double) finishedThreads.get() / (double) n,
						"");

				if (runningThreads.get() < MAX_THREADS) {
					break;
				}

				Thread.sleep(100);
			}

			Thread thread = new Thread(new PrimaryEstimationThread(tuple));

			runningThreads.incrementAndGet();
			thread.start();
		}

		while (true) {
			exec.setProgress((double) finishedThreads.get() / (double) n, "");

			if (runningThreads.get() == 0) {
				break;
			}

			Thread.sleep(100);
		}

		for (KnimeTuple tuple : tuples) {
			tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					Model1Schema.WRITABLE);
			container.addRowToTable(tuple);
		}

		container.close();
		return container.getTable();
	}

	private BufferedDataTable doSecondaryEstimation(BufferedDataTable table,
			ExecutionContext exec) throws CanceledExecutionException,
			PmmException, ParseException {
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		int n = table.getRowCount();
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(n);

		Map<String, List<Double>> depVarMap = new LinkedHashMap<String, List<Double>>();
		Map<String, List<Double>> temperatureMap = new LinkedHashMap<String, List<Double>>();
		Map<String, List<Double>> phMap = new LinkedHashMap<String, List<Double>>();
		Map<String, List<Double>> waterActivityMap = new LinkedHashMap<String, List<Double>>();
		Map<String, Map<String, List<Double>>> miscMaps = new LinkedHashMap<String, Map<String, List<Double>>>();
		Set<String> ids = new LinkedHashSet<String>();
		List<String> miscParams = getAllMiscParams(table);

		for (String param : miscParams) {
			miscMaps.put(param, new LinkedHashMap<String, List<Double>>());
		}

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			String id = tuple.getString(Model2Schema.ATT_DEPVAR);

			tuples.add(tuple);

			if (ids.add(id)) {
				depVarMap.put(id, new ArrayList<Double>());
				temperatureMap.put(id, new ArrayList<Double>());
				phMap.put(id, new ArrayList<Double>());
				waterActivityMap.put(id, new ArrayList<Double>());
				miscMaps.put(id, new LinkedHashMap<String, List<Double>>());

				for (String param : miscParams) {
					miscMaps.get(param).put(id, new ArrayList<Double>());
				}
			}

			List<String> keys = tuple.getStringList(Model1Schema.ATT_PARAMNAME);
			List<Double> values = tuple.getDoubleList(Model1Schema.ATT_VALUE);
			List<Double> minValues = tuple
					.getDoubleList(Model1Schema.ATT_MINVALUE);
			List<Double> maxValues = tuple
					.getDoubleList(Model1Schema.ATT_MAXVALUE);

			if (values.contains(null)) {
				continue;
			}

			double value = values.get(keys.indexOf(tuple
					.getString(Model2Schema.ATT_DEPVAR)));
			Double minValue = minValues.get(keys.indexOf(tuple
					.getString(Model2Schema.ATT_DEPVAR)));
			Double maxValue = maxValues.get(keys.indexOf(tuple
					.getString(Model2Schema.ATT_DEPVAR)));

			if ((minValue != null && value < minValue)
					|| (maxValue != null && value > maxValue)) {
				setWarningMessage("Some primary parameters are out of their range of values");
			}

			depVarMap.get(id).add(value);
			temperatureMap.get(id).add(
					tuple.getDouble(TimeSeriesSchema.ATT_TEMPERATURE));
			phMap.get(id).add(tuple.getDouble(TimeSeriesSchema.ATT_PH));
			waterActivityMap.get(id).add(
					tuple.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY));

			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (String param : miscParams) {
				Double paramValue = null;

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					if (param.equals(element.getName())) {
						paramValue = element.getValue();
						break;
					}
				}

				miscMaps.get(param).get(id).add(paramValue);
			}
		}

		Map<String, List<Double>> paramValueMap = new LinkedHashMap<String, List<Double>>();
		Map<String, List<Double>> paramErrorMap = new LinkedHashMap<String, List<Double>>();
		Map<String, Double> rmsMap = new LinkedHashMap<String, Double>();
		Map<String, Double> rSquaredMap = new LinkedHashMap<String, Double>();
		Map<String, Double> aicMap = new LinkedHashMap<String, Double>();
		Map<String, Double> bicMap = new LinkedHashMap<String, Double>();
		Map<String, List<Double>> minIndepMap = new LinkedHashMap<String, List<Double>>();
		Map<String, List<Double>> maxIndepMap = new LinkedHashMap<String, List<Double>>();
		Map<String, Integer> estIDMap = new LinkedHashMap<String, Integer>();

		for (int i = 0; i < n; i++) {
			KnimeTuple tuple = tuples.get(i);
			String id = tuple.getString(Model2Schema.ATT_DEPVAR);

			tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE,
					Model2Schema.WRITABLE);

			if (!paramValueMap.containsKey(id)) {
				String formula = tuple.getString(Model2Schema.ATT_FORMULA);
				List<String> parameters = tuple
						.getStringList(Model2Schema.ATT_PARAMNAME);
				List<Double> minParameterValues = tuple
						.getDoubleList(Model2Schema.ATT_MINVALUE);
				List<Double> maxParameterValues = tuple
						.getDoubleList(Model2Schema.ATT_MAXVALUE);
				List<Double> targetValues = depVarMap.get(id);
				List<String> arguments = tuple
						.getStringList(Model2Schema.ATT_INDEPVAR);
				List<List<Double>> argumentValues = new ArrayList<List<Double>>();

				for (String arg : arguments) {
					if (arg.equals(TimeSeriesSchema.ATT_TEMPERATURE)) {
						argumentValues.add(temperatureMap.get(id));
					} else if (arg.equals(TimeSeriesSchema.ATT_PH)) {
						argumentValues.add(phMap.get(id));
					} else if (arg.equals(TimeSeriesSchema.ATT_WATERACTIVITY)) {
						argumentValues.add(waterActivityMap.get(id));
					} else if (miscParams.contains(arg)) {
						argumentValues.add(miscMaps.get(arg).get(id));
					}
				}

				MathUtilities.removeNullValues(targetValues, argumentValues);

				List<Double> parameterValues;
				List<Double> parameterErrors;
				Double rms;
				Double rSquared;
				Double aic;
				Double bic;
				Integer estID = MathUtilities.getRandomNegativeInt();
				List<Double> minValues;
				List<Double> maxValues;
				boolean successful = false;
				ParameterOptimizer optimizer = null;

				if (!targetValues.isEmpty()) {
					optimizer = new ParameterOptimizer(formula, parameters,
							minParameterValues, maxParameterValues,
							targetValues, arguments, argumentValues,
							enforceLimits == 1);
					optimizer.optimize();
					successful = optimizer.isSuccessful();
				}

				if (successful) {
					parameterValues = optimizer.getParameterValues();
					parameterErrors = optimizer.getParameterStandardErrors();
					rms = optimizer.getRMS();
					rSquared = optimizer.getRSquare();
					aic = optimizer.getAIC();
					bic = optimizer.getBIC();
					minValues = new ArrayList<Double>();
					maxValues = new ArrayList<Double>();

					for (List<Double> values : argumentValues) {
						minValues.add(Collections.min(values));
						maxValues.add(Collections.max(values));
					}
				} else {
					parameterValues = Collections.nCopies(parameters.size(),
							null);
					parameterErrors = Collections.nCopies(parameters.size(),
							null);
					rms = null;
					rSquared = null;
					aic = null;
					bic = null;
					minValues = Collections.nCopies(arguments.size(), null);
					maxValues = Collections.nCopies(arguments.size(), null);
				}

				paramValueMap.put(id, parameterValues);
				rmsMap.put(id, rms);
				rSquaredMap.put(id, rSquared);
				aicMap.put(id, aic);
				bicMap.put(id, bic);
				paramErrorMap.put(id, parameterErrors);
				minIndepMap.put(id, minValues);
				maxIndepMap.put(id, maxValues);
				estIDMap.put(id, estID);
			}

			tuple.setValue(Model2Schema.ATT_VALUE, paramValueMap.get(id));
			tuple.setValue(Model2Schema.ATT_RMS, rmsMap.get(id));
			tuple.setValue(Model2Schema.ATT_RSQUARED, rSquaredMap.get(id));
			tuple.setValue(Model2Schema.ATT_AIC, aicMap.get(id));
			tuple.setValue(Model2Schema.ATT_BIC, bicMap.get(id));
			tuple.setValue(Model2Schema.ATT_PARAMERR, paramErrorMap.get(id));
			tuple.setValue(Model2Schema.ATT_MININDEP, minIndepMap.get(id));
			tuple.setValue(Model2Schema.ATT_MAXINDEP, maxIndepMap.get(id));
			tuple.setValue(Model2Schema.ATT_ESTMODELID, estIDMap.get(id));

			container.addRowToTable(tuple);
			exec.checkCanceled();
			exec.setProgress((double) i / (double) n, "");
		}

		container.close();
		return container.getTable();
	}

	private BufferedDataTable doOneStepEstimation(BufferedDataTable table,
			ExecutionContext exec) throws CanceledExecutionException,
			PmmException, ParseException {
		BufferedDataContainer container = exec.createDataContainer(peiSchema
				.createSpec());
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		List<KnimeTuple> seiTuples = new ArrayList<KnimeTuple>();

		while (reader.hasMoreElements()) {
			seiTuples.add(reader.nextElement());
		}

		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(ModelCombiner
				.combine(seiTuples, seiSchema, true,
						new LinkedHashMap<String, String>()).keySet());
		Map<Integer, List<List<Double>>> argumentValuesMap = new LinkedHashMap<Integer, List<List<Double>>>();
		Map<Integer, List<Double>> targetValuesMap = new LinkedHashMap<Integer, List<Double>>();

		for (KnimeTuple tuple : tuples) {
			int id = tuple.getInt(Model1Schema.ATT_MODELID);
			List<String> arguments = tuple
					.getStringList(Model1Schema.ATT_INDEPVAR);
			List<Double> targetValues = tuple
					.getDoubleList(TimeSeriesSchema.ATT_LOGC);
			List<Double> timeList = tuple
					.getDoubleList(TimeSeriesSchema.ATT_TIME);
			List<Double> tempList = new ArrayList<Double>(Collections.nCopies(
					timeList.size(),
					tuple.getDouble(TimeSeriesSchema.ATT_TEMPERATURE)));
			List<Double> phList = new ArrayList<Double>(Collections.nCopies(
					timeList.size(), tuple.getDouble(TimeSeriesSchema.ATT_PH)));
			List<Double> awList = new ArrayList<Double>(Collections.nCopies(
					timeList.size(),
					tuple.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY)));

			if (!targetValuesMap.containsKey(id)) {
				targetValuesMap.put(id, new ArrayList<Double>());
				argumentValuesMap.put(id, new ArrayList<List<Double>>());

				for (int i = 0; i < arguments.size(); i++) {
					argumentValuesMap.get(id).add(new ArrayList<Double>());
				}
			}

			targetValuesMap.get(id).addAll(targetValues);

			for (int i = 0; i < arguments.size(); i++) {
				if (arguments.get(i).equals(TimeSeriesSchema.ATT_TIME)) {
					argumentValuesMap.get(id).get(i).addAll(timeList);
				} else if (arguments.get(i).equals(
						TimeSeriesSchema.ATT_TEMPERATURE)) {
					argumentValuesMap.get(id).get(i).addAll(tempList);
				} else if (arguments.get(i).equals(TimeSeriesSchema.ATT_PH)) {
					argumentValuesMap.get(id).get(i).addAll(phList);
				} else if (arguments.get(i).equals(
						TimeSeriesSchema.ATT_WATERACTIVITY)) {
					argumentValuesMap.get(id).get(i).addAll(awList);
				}
			}
		}

		Map<Integer, List<Double>> paramValueMap = new LinkedHashMap<Integer, List<Double>>();
		Map<Integer, List<Double>> paramErrorMap = new LinkedHashMap<Integer, List<Double>>();
		Map<Integer, Double> rmsMap = new LinkedHashMap<Integer, Double>();
		Map<Integer, Double> rSquaredMap = new LinkedHashMap<Integer, Double>();
		Map<Integer, Double> aicMap = new LinkedHashMap<Integer, Double>();
		Map<Integer, Double> bicMap = new LinkedHashMap<Integer, Double>();
		Map<Integer, List<Double>> minIndepMap = new LinkedHashMap<Integer, List<Double>>();
		Map<Integer, List<Double>> maxIndepMap = new LinkedHashMap<Integer, List<Double>>();
		Map<Integer, Integer> estIDMap = new LinkedHashMap<Integer, Integer>();
		int n = tuples.size();

		for (int i = 0; i < n; i++) {
			KnimeTuple tuple = tuples.get(i);
			int id = tuple.getInt(Model1Schema.ATT_MODELID);

			if (!paramValueMap.containsKey(id)) {
				String formula = tuple.getString(Model1Schema.ATT_FORMULA);
				List<String> parameters = tuple
						.getStringList(Model1Schema.ATT_PARAMNAME);
				List<Double> minParameterValues = tuple
						.getDoubleList(Model1Schema.ATT_MINVALUE);
				List<Double> maxParameterValues = tuple
						.getDoubleList(Model1Schema.ATT_MAXVALUE);
				List<Double> targetValues = targetValuesMap.get(id);
				List<String> arguments = tuple
						.getStringList(Model1Schema.ATT_INDEPVAR);
				List<List<Double>> argumentValues = argumentValuesMap.get(id);

				MathUtilities.removeNullValues(targetValues, argumentValues);

				List<Double> parameterValues;
				List<Double> parameterErrors;
				Double rms;
				Double rSquared;
				Double aic;
				Double bic;
				Integer estID = MathUtilities.getRandomNegativeInt();
				List<Double> minValues;
				List<Double> maxValues;
				boolean successful = false;
				ParameterOptimizer optimizer = null;

				if (!targetValues.isEmpty()) {
					optimizer = new ParameterOptimizer(formula, parameters,
							minParameterValues, maxParameterValues,
							targetValues, arguments, argumentValues,
							enforceLimits == 1);
					optimizer.optimize();
					successful = optimizer.isSuccessful();
				}

				if (successful) {
					parameterValues = optimizer.getParameterValues();
					parameterErrors = optimizer.getParameterStandardErrors();
					rms = optimizer.getRMS();
					rSquared = optimizer.getRSquare();
					aic = optimizer.getAIC();
					bic = optimizer.getBIC();
					minValues = new ArrayList<Double>();
					maxValues = new ArrayList<Double>();

					for (List<Double> values : argumentValues) {
						minValues.add(Collections.min(values));
						maxValues.add(Collections.max(values));
					}
				} else {
					parameterValues = Collections.nCopies(parameters.size(),
							null);
					parameterErrors = Collections.nCopies(parameters.size(),
							null);
					rms = null;
					rSquared = null;
					aic = null;
					bic = null;
					minValues = null;
					maxValues = null;
				}

				paramValueMap.put(id, parameterValues);
				rmsMap.put(id, rms);
				rSquaredMap.put(id, rSquared);
				aicMap.put(id, aic);
				bicMap.put(id, bic);
				paramErrorMap.put(id, parameterErrors);
				minIndepMap.put(id, minValues);
				maxIndepMap.put(id, maxValues);
				estIDMap.put(id, estID);
			}

			tuple.setValue(Model1Schema.ATT_VALUE, paramValueMap.get(id));
			tuple.setValue(Model1Schema.ATT_RMS, rmsMap.get(id));
			tuple.setValue(Model1Schema.ATT_RSQUARED, rSquaredMap.get(id));
			tuple.setValue(Model1Schema.ATT_AIC, aicMap.get(id));
			tuple.setValue(Model1Schema.ATT_BIC, bicMap.get(id));
			tuple.setValue(Model1Schema.ATT_PARAMERR, paramErrorMap.get(id));
			tuple.setValue(Model1Schema.ATT_MININDEP, minIndepMap.get(id));
			tuple.setValue(Model1Schema.ATT_MAXINDEP, maxIndepMap.get(id));
			tuple.setValue(Model1Schema.ATT_ESTMODELID, estIDMap.get(id));

			container.addRowToTable(tuple);
			exec.checkCanceled();
			exec.setProgress((double) i / (double) n, "");
		}

		container.close();
		return container.getTable();
	}

	private List<String> getAllMiscParams(BufferedDataTable table)
			throws PmmException {
		KnimeRelationReader reader = new KnimeRelationReader(
				new TimeSeriesSchema(), table);
		Set<String> paramSet = new LinkedHashSet<String>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				paramSet.add(element.getName());
			}
		}

		return new ArrayList<String>(paramSet);
	}

	private class PrimaryEstimationThread implements Runnable {

		private KnimeTuple tuple;

		public PrimaryEstimationThread(KnimeTuple tuple) {
			this.tuple = tuple;
		}

		@Override
		public void run() {
			try {
				String formula = tuple.getString(Model1Schema.ATT_FORMULA);
				List<String> parameters = tuple
						.getStringList(Model1Schema.ATT_PARAMNAME);
				List<Double> minParameterValues = tuple
						.getDoubleList(Model1Schema.ATT_MINVALUE);
				List<Double> maxParameterValues = tuple
						.getDoubleList(Model1Schema.ATT_MAXVALUE);
				List<Double> targetValues = tuple
						.getDoubleList(TimeSeriesSchema.ATT_LOGC);
				List<Double> timeValues = tuple
						.getDoubleList(TimeSeriesSchema.ATT_TIME);
				List<String> arguments = Arrays
						.asList(TimeSeriesSchema.ATT_TIME);
				List<List<Double>> argumentValues = new ArrayList<List<Double>>();
				List<Double> parameterValues;
				List<Double> parameterErrors;
				Double rms;
				Double rSquare;
				Double aic;
				Double bic;
				List<Double> minIndep;
				List<Double> maxIndep;
				boolean successful = false;
				ParameterOptimizer optimizer = null;

				if (!targetValues.isEmpty() && !timeValues.isEmpty()) {
					argumentValues.add(timeValues);
					MathUtilities
							.removeNullValues(targetValues, argumentValues);

					minIndep = Arrays.asList(Collections.min(argumentValues
							.get(0)));
					maxIndep = Arrays.asList(Collections.max(argumentValues
							.get(0)));
					optimizer = new ParameterOptimizer(formula, parameters,
							minParameterValues, maxParameterValues,
							targetValues, arguments, argumentValues,
							enforceLimits == 1);
					optimizer.optimize();
					successful = optimizer.isSuccessful();
				} else {
					minIndep = null;
					maxIndep = null;
				}

				if (successful) {
					parameterValues = optimizer.getParameterValues();
					parameterErrors = optimizer.getParameterStandardErrors();
					rms = optimizer.getRMS();
					rSquare = optimizer.getRSquare();
					aic = optimizer.getAIC();
					bic = optimizer.getBIC();
				} else {
					parameterValues = Collections.nCopies(parameters.size(),
							null);
					parameterErrors = Collections.nCopies(parameters.size(),
							null);
					rms = null;
					rSquare = null;
					aic = null;
					bic = null;
				}

				tuple.setValue(Model1Schema.ATT_VALUE, parameterValues);
				tuple.setValue(Model1Schema.ATT_RMS, rms);
				tuple.setValue(Model1Schema.ATT_AIC, aic);
				tuple.setValue(Model1Schema.ATT_BIC, bic);
				tuple.setValue(Model1Schema.ATT_RSQUARED, rSquare);
				tuple.setValue(Model1Schema.ATT_PARAMERR, parameterErrors);
				tuple.setValue(Model1Schema.ATT_MININDEP, minIndep);
				tuple.setValue(Model1Schema.ATT_MAXINDEP, maxIndep);
				tuple.setValue(Model1Schema.ATT_ESTMODELID,
						MathUtilities.getRandomNegativeInt());
				runningThreads.decrementAndGet();
				finishedThreads.incrementAndGet();
			} catch (PmmException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

}
