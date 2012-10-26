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

import java.awt.geom.Point2D;
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

import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.ListUtilities;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
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

	protected static final String PRIMARY = "Primary";

	static final String CFGKEY_ENFORCELIMITS = "EnforceLimits";
	static final String CFGKEY_ONESTEPMETHOD = "OneStepMethod";
	static final String CFGKEY_PARAMETERGUESSES = "ParameterGuesses";
	static final int DEFAULT_ENFORCELIMITS = 0;
	static final int DEFAULT_ONESTEPMETHOD = 0;

	private static final int MAX_THREADS = 8;

	private KnimeSchema peiSchema;
	private KnimeSchema seiSchema;
	private KnimeSchema schema;

	private int enforceLimits;
	private int oneStepMethod;
	private List<String> parameterGuesses;

	/**
	 * Constructor for the node model.
	 */
	protected ModelEstimationNodeModel() {
		super(1, 1);
		enforceLimits = DEFAULT_ENFORCELIMITS;
		oneStepMethod = DEFAULT_ONESTEPMETHOD;
		parameterGuesses = new ArrayList<String>();

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
			outTable = doPrimaryEstimation(table, exec,
					getGuessMap(parameterGuesses));
		} else if (schema == seiSchema && oneStepMethod != 1) {
			outTable = doSecondaryEstimation(table, exec,
					getGuessMap(parameterGuesses));
		} else if (schema == seiSchema && oneStepMethod == 1) {
			outTable = doOneStepEstimation(table, exec,
					getGuessMap(parameterGuesses));
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
		settings.addString(CFGKEY_PARAMETERGUESSES,
				ListUtilities.getStringFromList(parameterGuesses));
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

		try {
			parameterGuesses = ListUtilities.getStringListFromString(settings
					.getString(CFGKEY_PARAMETERGUESSES));
		} catch (InvalidSettingsException e) {
			parameterGuesses = new ArrayList<String>();
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

	protected static Map<String, Map<String, Point2D.Double>> getGuessMap(
			List<String> guesses) {
		Map<String, Map<String, Point2D.Double>> guessMap = new LinkedHashMap<String, Map<String, Point2D.Double>>();

		for (String modelGuesses : guesses) {
			String[] modelElements = modelGuesses.split(":");

			if (modelElements.length == 2) {
				String modelName = modelElements[0];
				Map<String, Point2D.Double> modelMap = new LinkedHashMap<String, Point2D.Double>();

				for (String guess : modelElements[1].split(",")) {
					String[] elements = guess.split("=");

					if (elements.length == 2) {
						String paramName = elements[0];
						String[] range = elements[1].split("~");
						double min = Double.NaN;
						double max = Double.NaN;

						if (range.length == 2) {
							try {
								min = Double.parseDouble(range[0]);
							} catch (NumberFormatException e) {
							}

							try {
								max = Double.parseDouble(range[1]);
							} catch (NumberFormatException e) {
							}
						}

						modelMap.put(paramName, new Point2D.Double(min, max));
					}
				}

				guessMap.put(modelName, modelMap);
			}
		}

		return guessMap;
	}

	protected static List<String> guessMapToList(
			Map<String, Map<String, Point2D.Double>> map) {
		List<String> list = new ArrayList<String>();

		for (String modelName : map.keySet()) {
			Map<String, Point2D.Double> modelMap = map.get(modelName);
			StringBuilder modelString = new StringBuilder(modelName + ":");

			for (String paramName : modelMap.keySet()) {
				Point2D.Double range = modelMap.get(paramName);
				String rangeString = "";

				if (!Double.isNaN(range.x)) {
					rangeString += range.x + "~";
				} else {
					rangeString += "?~";
				}

				if (!Double.isNaN(range.y)) {
					rangeString += range.y;
				} else {
					rangeString += "?";
				}

				modelString.append(paramName + "=" + rangeString + ",");
			}

			if (modelString.charAt(modelString.length() - 1) == ',') {
				modelString.deleteCharAt(modelString.length() - 1);
			}

			list.add(modelString.toString());
		}

		return list;
	}

	private BufferedDataTable doPrimaryEstimation(BufferedDataTable table,
			ExecutionContext exec,
			Map<String, Map<String, Point2D.Double>> parameterGuesses)
			throws PmmException, CanceledExecutionException,
			InterruptedException {
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		int n = table.getRowCount();
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(n);
		AtomicInteger runningThreads = new AtomicInteger(0);
		AtomicInteger finishedThreads = new AtomicInteger(0);

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

			Map<String, Point2D.Double> guesses = parameterGuesses.get(PRIMARY);

			if (guesses == null) {
				guesses = new LinkedHashMap<String, Point2D.Double>();
			}

			Thread thread = new Thread(new PrimaryEstimationThread(tuple,
					guesses, runningThreads, finishedThreads));

			runningThreads.incrementAndGet();
			thread.start();
		}

		while (true) {
			exec.checkCanceled();
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
			ExecutionContext exec,
			Map<String, Map<String, Point2D.Double>> parameterGuesses)
			throws CanceledExecutionException, InterruptedException {
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		AtomicInteger progress = new AtomicInteger(Float.floatToIntBits(0.0f));
		Thread thread = new Thread(new SecondaryEstimationThread(table,
				parameterGuesses, container, progress));

		thread.start();

		while (true) {
			exec.checkCanceled();
			exec.setProgress(Float.intBitsToFloat(progress.get()), "");

			if (!thread.isAlive()) {
				break;
			}

			Thread.sleep(100);
		}

		return container.getTable();
	}

	private BufferedDataTable doOneStepEstimation(BufferedDataTable table,
			ExecutionContext exec,
			Map<String, Map<String, Point2D.Double>> parameterGuesses)
			throws CanceledExecutionException, InterruptedException {
		BufferedDataContainer container = exec.createDataContainer(peiSchema
				.createSpec());
		AtomicInteger progress = new AtomicInteger(Float.floatToIntBits(0.0f));
		Thread thread = new Thread(new OneStepEstimationThread(table,
				parameterGuesses, container, progress));

		thread.start();

		while (true) {
			exec.checkCanceled();
			exec.setProgress(Float.intBitsToFloat(progress.get()), "");

			if (!thread.isAlive()) {
				break;
			}

			Thread.sleep(100);
		}

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
		private Map<String, Point2D.Double> guesses;

		private AtomicInteger runningThreads;
		private AtomicInteger finishedThreads;

		public PrimaryEstimationThread(KnimeTuple tuple,
				Map<String, Point2D.Double> guesses,
				AtomicInteger runningThreads, AtomicInteger finishedThreads) {
			this.tuple = tuple;
			this.guesses = guesses;
			this.runningThreads = runningThreads;
			this.finishedThreads = finishedThreads;
		}

		@Override
		public void run() {
			try {
				String formula = tuple.getString(Model1Schema.ATT_FORMULA);
				PmmXmlDoc paramXml = tuple
						.getPmmXml(Model1Schema.ATT_PARAMETER);
				List<String> parameters = new ArrayList<String>();
				List<Double> minParameterValues = new ArrayList<Double>();
				List<Double> maxParameterValues = new ArrayList<Double>();
				List<Double> minGuessValues = new ArrayList<Double>();
				List<Double> maxGuessValues = new ArrayList<Double>();
				List<Double> targetValues = tuple
						.getDoubleList(TimeSeriesSchema.ATT_LOGC);
				List<Double> timeValues = tuple
						.getDoubleList(TimeSeriesSchema.ATT_TIME);
				List<String> arguments = Arrays
						.asList(TimeSeriesSchema.ATT_TIME);
				List<List<Double>> argumentValues = new ArrayList<List<Double>>();

				for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
					ParamXml element = (ParamXml) el;

					parameters.add(element.getName());
					minParameterValues.add(element.getMin());
					maxParameterValues.add(element.getMax());
				}

				List<Double> parameterValues = Collections.nCopies(
						parameters.size(), null);
				List<Double> parameterErrors = Collections.nCopies(
						parameters.size(), null);
				List<Double> parameterTValues = Collections.nCopies(
						parameters.size(), null);
				List<Double> parameterPValues = Collections.nCopies(
						parameters.size(), null);
				Double rms = null;
				Double rSquare = null;
				Double aic = null;
				Double bic = null;
				List<Double> minIndep = null;
				List<Double> maxIndep = null;
				boolean successful = false;
				ParameterOptimizer optimizer = null;

				for (String param : parameters) {
					if (guesses.containsKey(param)) {
						Point2D.Double guess = guesses.get(param);

						if (!Double.isNaN(guess.x)) {
							minGuessValues.add(guess.x);
						} else {
							minGuessValues.add(null);
						}

						if (!Double.isNaN(guess.y)) {
							maxGuessValues.add(guess.y);
						} else {
							maxGuessValues.add(null);
						}
					} else {
						minGuessValues.add(null);
						maxGuessValues.add(null);
					}
				}

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
							minGuessValues, maxGuessValues, targetValues,
							arguments, argumentValues, enforceLimits == 1);
					optimizer.optimize(new AtomicInteger());
					successful = optimizer.isSuccessful();
				}

				if (successful) {
					parameterValues = optimizer.getParameterValues();
					parameterErrors = optimizer.getParameterStandardErrors();
					parameterTValues = optimizer.getParameterTValues();
					parameterPValues = optimizer.getParameterPValues();
					rms = optimizer.getRMS();
					rSquare = optimizer.getRSquare();
					aic = optimizer.getAIC();
					bic = optimizer.getBIC();
				}

				for (int i = 0; i < paramXml.getElementSet().size(); i++) {
					ParamXml element = (ParamXml) paramXml.get(i);

					element.setValue(parameterValues.get(i));
					element.setError(parameterErrors.get(i));
					element.sett(parameterTValues.get(i));
					element.setP(parameterPValues.get(i));
				}

				tuple.setValue(Model1Schema.ATT_PARAMETER, paramXml);
				tuple.setValue(Model1Schema.ATT_RMS, rms);
				tuple.setValue(Model1Schema.ATT_AIC, aic);
				tuple.setValue(Model1Schema.ATT_BIC, bic);
				tuple.setValue(Model1Schema.ATT_RSQUARED, rSquare);
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

	private class SecondaryEstimationThread implements Runnable {

		private BufferedDataTable inTable;
		private Map<String, Map<String, Point2D.Double>> parameterGuesses;
		private BufferedDataContainer container;
		private AtomicInteger progress;

		public SecondaryEstimationThread(BufferedDataTable inTable,
				Map<String, Map<String, Point2D.Double>> parameterGuesses,
				BufferedDataContainer container, AtomicInteger progress) {
			this.inTable = inTable;
			this.parameterGuesses = parameterGuesses;
			this.container = container;
			this.progress = progress;
		}

		@Override
		public void run() {
			try {
				KnimeRelationReader reader = new KnimeRelationReader(schema,
						inTable);
				int n = inTable.getRowCount();
				List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(n);

				Map<String, List<Double>> depVarMap = new LinkedHashMap<String, List<Double>>();
				Map<String, List<Double>> temperatureMap = new LinkedHashMap<String, List<Double>>();
				Map<String, List<Double>> phMap = new LinkedHashMap<String, List<Double>>();
				Map<String, List<Double>> waterActivityMap = new LinkedHashMap<String, List<Double>>();
				Map<String, Map<String, List<Double>>> miscMaps = new LinkedHashMap<String, Map<String, List<Double>>>();
				Set<String> ids = new LinkedHashSet<String>();
				List<String> miscParams = getAllMiscParams(inTable);

				for (String param : miscParams) {
					miscMaps.put(param,
							new LinkedHashMap<String, List<Double>>());
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
						miscMaps.put(id,
								new LinkedHashMap<String, List<Double>>());

						for (String param : miscParams) {
							miscMaps.get(param)
									.put(id, new ArrayList<Double>());
						}
					}

					PmmXmlDoc params = tuple
							.getPmmXml(Model1Schema.ATT_PARAMETER);
					Double value = null;
					Double minValue = null;
					Double maxValue = null;
					boolean valueMissing = false;

					for (PmmXmlElementConvertable el : params.getElementSet()) {
						ParamXml element = (ParamXml) el;

						if (element.getValue() == null) {
							valueMissing = true;
						}

						if (element.getName().equals(
								tuple.getString(Model2Schema.ATT_DEPVAR))) {
							value = element.getValue();
							minValue = element.getMin();
							maxValue = element.getMax();
						}
					}

					if (valueMissing) {
						continue;
					}

					if ((minValue != null && value < minValue)
							|| (maxValue != null && value > maxValue)) {
						setWarningMessage("Some primary parameters are out of their range of values");
					}

					depVarMap.get(id).add(value);
					temperatureMap.get(id).add(
							tuple.getDouble(TimeSeriesSchema.ATT_TEMPERATURE));
					phMap.get(id).add(tuple.getDouble(TimeSeriesSchema.ATT_PH));
					waterActivityMap
							.get(id)
							.add(tuple
									.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY));

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

				Map<String, PmmXmlDoc> paramMap = new LinkedHashMap<String, PmmXmlDoc>();
				Map<String, PmmXmlDoc> indepMap = new LinkedHashMap<String, PmmXmlDoc>();
				Map<String, Double> rmsMap = new LinkedHashMap<String, Double>();
				Map<String, Double> rSquaredMap = new LinkedHashMap<String, Double>();
				Map<String, Double> aicMap = new LinkedHashMap<String, Double>();
				Map<String, Double> bicMap = new LinkedHashMap<String, Double>();
				Map<String, Integer> estIDMap = new LinkedHashMap<String, Integer>();

				for (int i = 0; i < n; i++) {
					KnimeTuple tuple = tuples.get(i);
					String id = tuple.getString(Model2Schema.ATT_DEPVAR);

					tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE,
							Model2Schema.WRITABLE);

					if (!paramMap.containsKey(id)) {
						String formula = tuple
								.getString(Model2Schema.ATT_FORMULA);
						PmmXmlDoc paramXml = tuple
								.getPmmXml(Model2Schema.ATT_PARAMETER);
						PmmXmlDoc indepXml = tuple
								.getPmmXml(Model2Schema.ATT_INDEPENDENT);
						List<String> parameters = new ArrayList<String>();
						List<Double> minParameterValues = new ArrayList<Double>();
						List<Double> maxParameterValues = new ArrayList<Double>();
						List<Double> minGuessValues = new ArrayList<Double>();
						List<Double> maxGuessValues = new ArrayList<Double>();
						List<Double> targetValues = depVarMap.get(id);
						List<String> arguments = CellIO.getNameList(indepXml);
						List<List<Double>> argumentValues = new ArrayList<List<Double>>();

						for (PmmXmlElementConvertable el : paramXml
								.getElementSet()) {
							ParamXml element = (ParamXml) el;

							parameters.add(element.getName());
							minParameterValues.add(element.getMin());
							maxParameterValues.add(element.getMax());
						}

						for (String param : parameters) {
							String modelID = tuple
									.getInt(Model2Schema.ATT_MODELID) + "";

							if (parameterGuesses.containsKey(modelID)
									&& parameterGuesses.get(modelID)
											.containsKey(param)) {
								Point2D.Double guess = parameterGuesses.get(
										tuple.getInt(modelID)).get(param);

								if (!Double.isNaN(guess.x)) {
									minGuessValues.add(guess.x);
								} else {
									minGuessValues.add(null);
								}

								if (!Double.isNaN(guess.y)) {
									maxGuessValues.add(guess.y);
								} else {
									maxGuessValues.add(null);
								}
							} else {
								minGuessValues.add(null);
								maxGuessValues.add(null);
							}
						}

						for (String arg : arguments) {
							if (arg.equals(TimeSeriesSchema.ATT_TEMPERATURE)) {
								argumentValues.add(temperatureMap.get(id));
							} else if (arg.equals(TimeSeriesSchema.ATT_PH)) {
								argumentValues.add(phMap.get(id));
							} else if (arg
									.equals(TimeSeriesSchema.ATT_WATERACTIVITY)) {
								argumentValues.add(waterActivityMap.get(id));
							} else if (miscParams.contains(arg)) {
								argumentValues.add(miscMaps.get(arg).get(id));
							}
						}

						MathUtilities.removeNullValues(targetValues,
								argumentValues);

						List<Double> parameterValues = Collections.nCopies(
								parameters.size(), null);
						List<Double> parameterErrors = Collections.nCopies(
								parameters.size(), null);
						List<Double> parameterTValues = Collections.nCopies(
								parameters.size(), null);
						List<Double> parameterPValues = Collections.nCopies(
								parameters.size(), null);
						Double rms = null;
						Double rSquared = null;
						Double aic = null;
						Double bic = null;
						Integer estID = MathUtilities.getRandomNegativeInt();
						List<Double> minValues = Collections.nCopies(
								arguments.size(), null);
						List<Double> maxValues = Collections.nCopies(
								arguments.size(), null);
						boolean successful = false;
						ParameterOptimizer optimizer = null;

						if (!targetValues.isEmpty()) {
							optimizer = new ParameterOptimizer(formula,
									parameters, minParameterValues,
									maxParameterValues, minGuessValues,
									maxGuessValues, targetValues, arguments,
									argumentValues, enforceLimits == 1);
							optimizer.optimize(progress);
							successful = optimizer.isSuccessful();
						}

						if (successful) {
							parameterValues = optimizer.getParameterValues();
							parameterErrors = optimizer
									.getParameterStandardErrors();
							parameterTValues = optimizer.getParameterTValues();
							parameterPValues = optimizer.getParameterPValues();
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
						}

						for (int j = 0; j < paramXml.getElementSet().size(); j++) {
							ParamXml element = (ParamXml) paramXml.get(j);

							element.setValue(parameterValues.get(j));
							element.setError(parameterErrors.get(j));
							element.sett(parameterTValues.get(j));
							element.setP(parameterPValues.get(j));
						}

						for (int j = 0; j < indepXml.getElementSet().size(); j++) {
							IndepXml element = (IndepXml) indepXml.get(j);

							element.setMin(minValues.get(j));
							element.setMax(maxValues.get(j));
						}

						paramMap.put(id, paramXml);
						rmsMap.put(id, rms);
						rSquaredMap.put(id, rSquared);
						aicMap.put(id, aic);
						bicMap.put(id, bic);
						indepMap.put(id, indepXml);
						estIDMap.put(id, estID);
					}

					tuple.setValue(Model2Schema.ATT_PARAMETER, paramMap.get(id));
					tuple.setValue(Model2Schema.ATT_RMS, rmsMap.get(id));
					tuple.setValue(Model2Schema.ATT_RSQUARED,
							rSquaredMap.get(id));
					tuple.setValue(Model2Schema.ATT_AIC, aicMap.get(id));
					tuple.setValue(Model2Schema.ATT_BIC, bicMap.get(id));
					tuple.setValue(Model2Schema.ATT_INDEPENDENT,
							indepMap.get(id));
					tuple.setValue(Model2Schema.ATT_ESTMODELID,
							estIDMap.get(id));

					container.addRowToTable(tuple);
				}

				container.close();
			} catch (PmmException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private class OneStepEstimationThread implements Runnable {

		private BufferedDataTable inTable;
		private Map<String, Map<String, Point2D.Double>> parameterGuesses;

		private BufferedDataContainer container;
		private AtomicInteger progress;

		public OneStepEstimationThread(BufferedDataTable inTable,
				Map<String, Map<String, Point2D.Double>> parameterGuesses,
				BufferedDataContainer container, AtomicInteger progress) {
			this.inTable = inTable;
			this.parameterGuesses = parameterGuesses;
			this.container = container;
			this.progress = progress;
		}

		@Override
		public void run() {
			try {
				KnimeRelationReader reader = new KnimeRelationReader(schema,
						inTable);
				List<KnimeTuple> seiTuples = new ArrayList<KnimeTuple>();

				while (reader.hasMoreElements()) {
					seiTuples.add(reader.nextElement());
				}

				for (KnimeTuple tuple : seiTuples) {
					PmmXmlDoc params = tuple
							.getPmmXml(Model1Schema.ATT_PARAMETER);

					for (PmmXmlElementConvertable el : params.getElementSet()) {
						ParamXml element = (ParamXml) el;

						if (parameterGuesses.containsKey(PRIMARY)
								&& parameterGuesses.get(PRIMARY).containsKey(
										element.getName())) {
							Point2D.Double guess = parameterGuesses
									.get(PRIMARY).get(element.getName());

							if (!Double.isNaN(guess.x)) {
								element.setP(guess.x);
							} else {
								element.setP(null);
							}

							if (!Double.isNaN(guess.y)) {
								element.sett(guess.y);
							} else {
								element.sett(null);
							}
						} else {
							element.setP(null);
							element.sett(null);
						}
					}

					String secID = tuple.getInt(Model2Schema.ATT_MODELID) + "";
					PmmXmlDoc secParams = tuple
							.getPmmXml(Model2Schema.ATT_PARAMETER);

					for (PmmXmlElementConvertable el : secParams
							.getElementSet()) {
						ParamXml element = (ParamXml) el;

						if (parameterGuesses.containsKey(secID)
								&& parameterGuesses.get(secID).containsKey(
										element.getName())) {
							Point2D.Double guess = parameterGuesses.get(secID)
									.get(element.getName());

							if (!Double.isNaN(guess.x)) {
								element.setP(guess.x);
							} else {
								element.setP(null);
							}

							if (!Double.isNaN(guess.y)) {
								element.sett(guess.y);
							} else {
								element.sett(null);
							}
						} else {
							element.setP(null);
							element.sett(null);
						}
					}

					tuple.setValue(Model1Schema.ATT_PARAMETER, params);
					tuple.setValue(Model2Schema.ATT_PARAMETER, secParams);
				}

				List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(
						ModelCombiner.combine(seiTuples, seiSchema, true,
								new LinkedHashMap<String, String>()).keySet());
				Map<Integer, List<List<Double>>> argumentValuesMap = new LinkedHashMap<Integer, List<List<Double>>>();
				Map<Integer, List<Double>> targetValuesMap = new LinkedHashMap<Integer, List<Double>>();

				for (KnimeTuple tuple : tuples) {
					int id = tuple.getInt(Model1Schema.ATT_MODELID);
					PmmXmlDoc indepXml = tuple
							.getPmmXml(Model1Schema.ATT_INDEPENDENT);
					List<String> arguments = CellIO.getNameList(indepXml);
					List<Double> targetValues = tuple
							.getDoubleList(TimeSeriesSchema.ATT_LOGC);
					List<Double> timeList = tuple
							.getDoubleList(TimeSeriesSchema.ATT_TIME);
					List<Double> tempList = new ArrayList<Double>(
							Collections.nCopies(
									timeList.size(),
									tuple.getDouble(TimeSeriesSchema.ATT_TEMPERATURE)));
					List<Double> phList = new ArrayList<Double>(
							Collections.nCopies(timeList.size(),
									tuple.getDouble(TimeSeriesSchema.ATT_PH)));
					List<Double> awList = new ArrayList<Double>(
							Collections.nCopies(
									timeList.size(),
									tuple.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY)));
					Map<String, List<Double>> miscLists = new LinkedHashMap<String, List<Double>>();
					PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

					for (PmmXmlElementConvertable el : misc.getElementSet()) {
						MiscXml element = (MiscXml) el;
						List<Double> list = new ArrayList<Double>(
								Collections.nCopies(timeList.size(),
										element.getValue()));

						miscLists.put(element.getName(), list);
					}

					if (!targetValuesMap.containsKey(id)) {
						targetValuesMap.put(id, new ArrayList<Double>());
						argumentValuesMap
								.put(id, new ArrayList<List<Double>>());

						for (int i = 0; i < arguments.size(); i++) {
							argumentValuesMap.get(id).add(
									new ArrayList<Double>());
						}
					}

					targetValuesMap.get(id).addAll(targetValues);

					for (int i = 0; i < arguments.size(); i++) {
						if (arguments.get(i).equals(TimeSeriesSchema.ATT_TIME)) {
							argumentValuesMap.get(id).get(i).addAll(timeList);
						} else if (arguments.get(i).equals(
								TimeSeriesSchema.ATT_TEMPERATURE)) {
							argumentValuesMap.get(id).get(i).addAll(tempList);
						} else if (arguments.get(i).equals(
								TimeSeriesSchema.ATT_PH)) {
							argumentValuesMap.get(id).get(i).addAll(phList);
						} else if (arguments.get(i).equals(
								TimeSeriesSchema.ATT_WATERACTIVITY)) {
							argumentValuesMap.get(id).get(i).addAll(awList);
						} else {
							argumentValuesMap.get(id).get(i)
									.addAll(miscLists.get(arguments.get(i)));
						}
					}
				}

				Map<Integer, PmmXmlDoc> paramMap = new LinkedHashMap<Integer, PmmXmlDoc>();
				Map<Integer, PmmXmlDoc> indepMap = new LinkedHashMap<Integer, PmmXmlDoc>();
				Map<Integer, Double> rmsMap = new LinkedHashMap<Integer, Double>();
				Map<Integer, Double> rSquaredMap = new LinkedHashMap<Integer, Double>();
				Map<Integer, Double> aicMap = new LinkedHashMap<Integer, Double>();
				Map<Integer, Double> bicMap = new LinkedHashMap<Integer, Double>();
				Map<Integer, Integer> estIDMap = new LinkedHashMap<Integer, Integer>();
				int n = tuples.size();

				for (int i = 0; i < n; i++) {
					KnimeTuple tuple = tuples.get(i);
					int id = tuple.getInt(Model1Schema.ATT_MODELID);

					if (!paramMap.containsKey(id)) {
						String formula = tuple
								.getString(Model1Schema.ATT_FORMULA);
						PmmXmlDoc paramXml = tuple
								.getPmmXml(Model1Schema.ATT_PARAMETER);
						PmmXmlDoc indepXml = tuple
								.getPmmXml(Model1Schema.ATT_INDEPENDENT);
						List<String> parameters = new ArrayList<String>();
						List<Double> minParameterValues = new ArrayList<Double>();
						List<Double> maxParameterValues = new ArrayList<Double>();
						List<Double> minGuessValues = new ArrayList<Double>();
						List<Double> maxGuessValues = new ArrayList<Double>();
						List<Double> targetValues = targetValuesMap.get(id);
						List<String> arguments = CellIO.getNameList(indepXml);
						List<List<Double>> argumentValues = argumentValuesMap
								.get(id);

						for (PmmXmlElementConvertable el : paramXml
								.getElementSet()) {
							ParamXml element = (ParamXml) el;

							parameters.add(element.getName());
							minParameterValues.add(element.getMin());
							maxParameterValues.add(element.getMax());
							minGuessValues.add(element.getP());
							maxGuessValues.add(element.gett());
						}

						MathUtilities.removeNullValues(targetValues,
								argumentValues);

						List<Double> parameterValues = Collections.nCopies(
								parameters.size(), null);
						List<Double> parameterErrors = Collections.nCopies(
								parameters.size(), null);
						List<Double> parameterTValues = Collections.nCopies(
								parameters.size(), null);
						List<Double> parameterPValues = Collections.nCopies(
								parameters.size(), null);
						Double rms = null;
						Double rSquared = null;
						Double aic = null;
						Double bic = null;
						Integer estID = MathUtilities.getRandomNegativeInt();
						List<Double> minValues = Collections.nCopies(
								arguments.size(), null);
						List<Double> maxValues = Collections.nCopies(
								arguments.size(), null);
						boolean successful = false;
						ParameterOptimizer optimizer = null;

						if (!targetValues.isEmpty()) {
							optimizer = new ParameterOptimizer(formula,
									parameters, minParameterValues,
									maxParameterValues, minGuessValues,
									maxGuessValues, targetValues, arguments,
									argumentValues, enforceLimits == 1);
							optimizer.optimize(progress);
							successful = optimizer.isSuccessful();
						}

						if (successful) {
							parameterValues = optimizer.getParameterValues();
							parameterErrors = optimizer
									.getParameterStandardErrors();
							parameterTValues = optimizer.getParameterTValues();
							parameterPValues = optimizer.getParameterPValues();
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
						}

						for (int j = 0; j < paramXml.getElementSet().size(); j++) {
							ParamXml element = (ParamXml) paramXml.get(j);

							element.setValue(parameterValues.get(j));
							element.setError(parameterErrors.get(j));
							element.sett(parameterTValues.get(j));
							element.setP(parameterPValues.get(j));
						}

						for (int j = 0; j < indepXml.getElementSet().size(); j++) {
							IndepXml element = (IndepXml) indepXml.get(j);

							element.setMin(minValues.get(j));
							element.setMax(maxValues.get(j));
						}

						paramMap.put(id, paramXml);
						rmsMap.put(id, rms);
						rSquaredMap.put(id, rSquared);
						aicMap.put(id, aic);
						bicMap.put(id, bic);
						indepMap.put(id, indepXml);
						estIDMap.put(id, estID);
					}

					tuple.setValue(Model1Schema.ATT_PARAMETER, paramMap.get(id));
					tuple.setValue(Model1Schema.ATT_RMS, rmsMap.get(id));
					tuple.setValue(Model1Schema.ATT_RSQUARED,
							rSquaredMap.get(id));
					tuple.setValue(Model1Schema.ATT_AIC, aicMap.get(id));
					tuple.setValue(Model1Schema.ATT_BIC, bicMap.get(id));
					tuple.setValue(Model1Schema.ATT_INDEPENDENT,
							indepMap.get(id));
					tuple.setValue(Model1Schema.ATT_ESTMODELID,
							estIDMap.get(id));

					container.addRowToTable(tuple);
				}

				container.close();
			} catch (PmmException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

}
