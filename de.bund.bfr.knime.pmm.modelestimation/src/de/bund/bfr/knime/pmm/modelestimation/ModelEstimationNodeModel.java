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
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.ListUtilities;
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

	static final String PRIMARY = "Primary";

	static final String NO_FITTING = "";
	static final String PRIMARY_FITTING = "Primary Fitting";
	static final String SECONDARY_FITTING = "Secondary Fitting";
	static final String ONESTEP_FITTING = "One-Step Fitting";

	static final String CFGKEY_FITTINGTYPE = "FittingType";
	static final String CFGKEY_ENFORCELIMITS = "EnforceLimits";
	static final String CFGKEY_NPARAMETERSPACE = "NParameterSpace";
	static final String CFGKEY_NLEVENBERG = "NLevenberg";
	static final String CFGKEY_STOPWHENSUCCESSFUL = "StopWhenSuccessful";
	static final String CFGKEY_PARAMETERGUESSES = "ParameterGuesses";

	static final String DEFAULT_FITTINGTYPE = NO_FITTING;
	static final int DEFAULT_ENFORCELIMITS = 0;
	static final int DEFAULT_NPARAMETERSPACE = 10000;
	static final int DEFAULT_NLEVENBERG = 10;
	static final int DEFAULT_STOPWHENSUCCESSFUL = 1;

	private static final int MAX_THREADS = 8;

	private KnimeSchema peiSchema;
	private KnimeSchema seiSchema;
	private KnimeSchema schema;

	private String fittingType;
	private int enforceLimits;
	private int nParameterSpace;
	private int nLevenberg;
	private int stopWhenSuccessful;
	private List<String> parameterGuesses;

	/**
	 * Constructor for the node model.
	 */
	protected ModelEstimationNodeModel() {
		super(1, 1);
		fittingType = DEFAULT_FITTINGTYPE;
		enforceLimits = DEFAULT_ENFORCELIMITS;
		nParameterSpace = DEFAULT_NPARAMETERSPACE;
		nLevenberg = DEFAULT_NLEVENBERG;
		stopWhenSuccessful = DEFAULT_STOPWHENSUCCESSFUL;
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

		if (fittingType.equals(PRIMARY_FITTING)) {
			outTable = doPrimaryEstimation(table, exec,
					getGuessMap(parameterGuesses));
		} else if (fittingType.equals(SECONDARY_FITTING)) {
			outTable = doSecondaryEstimation(table, exec,
					getGuessMap(parameterGuesses));
		} else if (fittingType.equals(ONESTEP_FITTING)) {
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

			if (fittingType.equals(NO_FITTING)) {
				throw new InvalidSettingsException("Node has to be configured!");
			} else if (fittingType.equals(PRIMARY_FITTING)) {
				if (peiSchema.conforms(inSpecs[0])) {
					schema = peiSchema;
					outSchema = peiSchema;
				} else {
					throw new InvalidSettingsException("Wrong input!");
				}
			} else if (fittingType.equals(SECONDARY_FITTING)) {
				if (seiSchema.conforms(inSpecs[0])) {
					schema = seiSchema;
					outSchema = seiSchema;
				} else {
					throw new InvalidSettingsException("Wrong input!");
				}
			} else if (fittingType.equals(ONESTEP_FITTING)) {
				if (seiSchema.conforms(inSpecs[0])) {
					schema = seiSchema;
					outSchema = peiSchema;
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
		settings.addString(CFGKEY_FITTINGTYPE, fittingType);
		settings.addInt(CFGKEY_ENFORCELIMITS, enforceLimits);
		settings.addInt(CFGKEY_NPARAMETERSPACE, nParameterSpace);
		settings.addInt(CFGKEY_NLEVENBERG, nLevenberg);
		settings.addInt(CFGKEY_STOPWHENSUCCESSFUL, stopWhenSuccessful);
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
			fittingType = settings.getString(CFGKEY_FITTINGTYPE);
		} catch (InvalidSettingsException e) {
			fittingType = DEFAULT_FITTINGTYPE;
		}

		try {
			enforceLimits = settings.getInt(CFGKEY_ENFORCELIMITS);
		} catch (InvalidSettingsException e) {
			enforceLimits = DEFAULT_ENFORCELIMITS;
		}

		try {
			nParameterSpace = settings.getInt(CFGKEY_NPARAMETERSPACE);
		} catch (InvalidSettingsException e) {
			nParameterSpace = DEFAULT_NPARAMETERSPACE;
		}

		try {
			nLevenberg = settings.getInt(CFGKEY_NLEVENBERG);
		} catch (InvalidSettingsException e) {
			nLevenberg = DEFAULT_NLEVENBERG;
		}

		try {
			stopWhenSuccessful = settings.getInt(CFGKEY_STOPWHENSUCCESSFUL);
		} catch (InvalidSettingsException e) {
			stopWhenSuccessful = DEFAULT_STOPWHENSUCCESSFUL;
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
				PmmXmlDoc timeSeriesXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				List<Double> targetValues = new ArrayList<Double>();
				List<Double> timeValues = new ArrayList<Double>();
				List<String> arguments = Arrays.asList(TimeSeriesSchema.TIME);

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					timeValues.add(element.getTime());
					targetValues.add(element.getLog10C());
				}

				for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
					ParamXml element = (ParamXml) el;

					parameters.add(element.getName());
					minParameterValues.add(element.getMin());
					maxParameterValues.add(element.getMax());

					if (guesses.containsKey(element.getName())) {
						Point2D.Double guess = guesses.get(element.getName());

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
						minGuessValues.add(element.getMin());
						maxGuessValues.add(element.getMax());
					}
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
				Double minIndep = null;
				Double maxIndep = null;
				Integer estID = null;
				boolean successful = false;
				ParameterOptimizer optimizer = null;

				if (!targetValues.isEmpty() && !timeValues.isEmpty()) {
					List<List<Double>> argumentValues = new ArrayList<List<Double>>();

					argumentValues.add(timeValues);
					MathUtilities
							.removeNullValues(targetValues, argumentValues);

					minIndep = Collections.min(argumentValues.get(0));
					maxIndep = Collections.max(argumentValues.get(0));
					optimizer = new ParameterOptimizer(formula, parameters,
							minParameterValues, maxParameterValues,
							minGuessValues, maxGuessValues, targetValues,
							arguments, argumentValues, enforceLimits == 1);
					optimizer.optimize(new AtomicInteger(), nParameterSpace,
							nLevenberg, stopWhenSuccessful == 1);
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
					estID = MathUtilities.getRandomNegativeInt();
				}

				for (int i = 0; i < paramXml.getElementSet().size(); i++) {
					ParamXml element = (ParamXml) paramXml.get(i);

					element.setValue(parameterValues.get(i));
					element.setError(parameterErrors.get(i));
					element.sett(parameterTValues.get(i));
					element.setP(parameterPValues.get(i));
				}

				PmmXmlDoc indepXml = tuple
						.getPmmXml(Model1Schema.ATT_INDEPENDENT);

				((IndepXml) indepXml.get(0)).setMin(minIndep);
				((IndepXml) indepXml.get(0)).setMax(maxIndep);

				tuple.setValue(Model1Schema.ATT_PARAMETER, paramXml);
				tuple.setValue(Model1Schema.ATT_RMS, rms);
				tuple.setValue(Model1Schema.ATT_AIC, aic);
				tuple.setValue(Model1Schema.ATT_BIC, bic);
				tuple.setValue(Model1Schema.ATT_RSQUARED, rSquare);
				tuple.setValue(Model1Schema.ATT_INDEPENDENT, indepXml);
				tuple.setValue(Model1Schema.ATT_ESTMODELID, estID);
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
					String id = ((DepXml) tuple.getPmmXml(
							Model2Schema.ATT_DEPENDENT).get(0)).getName();

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
						String depVarSec = ((DepXml) tuple.getPmmXml(
								Model2Schema.ATT_DEPENDENT).get(0)).getName();

						if (element.getValue() == null) {
							valueMissing = true;
						}

						if (element.getName().equals(depVarSec)) {
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
					String id = ((DepXml) tuple.getPmmXml(
							Model2Schema.ATT_DEPENDENT).get(0)).getName();

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
						String modelID = tuple.getInt(Model2Schema.ATT_MODELID)
								+ "";
						Map<String, Point2D.Double> modelGuesses = parameterGuesses
								.get(modelID);

						if (modelGuesses == null) {
							modelGuesses = new LinkedHashMap<String, Point2D.Double>();
						}

						for (PmmXmlElementConvertable el : paramXml
								.getElementSet()) {
							ParamXml element = (ParamXml) el;

							parameters.add(element.getName());
							minParameterValues.add(element.getMin());
							maxParameterValues.add(element.getMax());

							if (modelGuesses.containsKey(element.getName())) {
								Point2D.Double guess = modelGuesses.get(element
										.getName());

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
								minGuessValues.add(element.getMin());
								maxGuessValues.add(element.getMax());
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
							optimizer.optimize(progress, nParameterSpace,
									nLevenberg, stopWhenSuccessful == 1);
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
					Map<String, Point2D.Double> primaryGuesses = parameterGuesses
							.get(PRIMARY);

					if (primaryGuesses == null) {
						primaryGuesses = new LinkedHashMap<String, Point2D.Double>();
					}

					for (PmmXmlElementConvertable el : params.getElementSet()) {
						ParamXml element = (ParamXml) el;

						if (primaryGuesses.containsKey(element.getName())) {
							Point2D.Double guess = primaryGuesses.get(element
									.getName());

							if (!Double.isNaN(guess.x)) {
								element.setMinGuess(guess.x);
							} else {
								element.setMinGuess(null);
							}

							if (!Double.isNaN(guess.y)) {
								element.setMaxGuess(guess.y);
							} else {
								element.setMaxGuess(null);
							}
						} else {
							element.setMinGuess(element.getMin());
							element.setMaxGuess(element.getMax());
						}
					}

					String secID = tuple.getInt(Model2Schema.ATT_MODELID) + "";
					PmmXmlDoc secParams = tuple
							.getPmmXml(Model2Schema.ATT_PARAMETER);
					Map<String, Point2D.Double> secGuesses = parameterGuesses
							.get(secID);

					if (secGuesses == null) {
						secGuesses = new LinkedHashMap<String, Point2D.Double>();
					}

					for (PmmXmlElementConvertable el : secParams
							.getElementSet()) {
						ParamXml element = (ParamXml) el;

						if (secGuesses.containsKey(element.getName())) {
							Point2D.Double guess = secGuesses.get(element
									.getName());

							if (!Double.isNaN(guess.x)) {
								element.setMinGuess(guess.x);
							} else {
								element.setMinGuess(null);
							}

							if (!Double.isNaN(guess.y)) {
								element.setMaxGuess(guess.y);
							} else {
								element.setMaxGuess(null);
							}
						} else {
							element.setMinGuess(element.getMin());
							element.setMaxGuess(element.getMax());
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
					PmmXmlDoc timeSeriesXml = tuple
							.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
					int n = timeSeriesXml.getElementSet().size();
					Double temp = tuple
							.getDouble(TimeSeriesSchema.ATT_TEMPERATURE);
					Double ph = tuple.getDouble(TimeSeriesSchema.ATT_PH);
					Double aw = tuple
							.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY);

					List<Double> targetValues = new ArrayList<Double>();
					List<Double> timeList = new ArrayList<Double>();
					List<Double> tempList = new ArrayList<Double>(
							Collections.nCopies(n, temp));
					List<Double> phList = new ArrayList<Double>(
							Collections.nCopies(n, ph));
					List<Double> awList = new ArrayList<Double>(
							Collections.nCopies(n, aw));
					Map<String, List<Double>> miscLists = new LinkedHashMap<String, List<Double>>();
					PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

					for (PmmXmlElementConvertable el : timeSeriesXml
							.getElementSet()) {
						TimeSeriesXml element = (TimeSeriesXml) el;

						timeList.add(element.getTime());
						targetValues.add(element.getLog10C());
					}

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
						if (arguments.get(i).equals(TimeSeriesSchema.TIME)) {
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
							minGuessValues.add(element.getMinGuess());
							maxGuessValues.add(element.getMaxGuess());
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
							optimizer.optimize(progress, nParameterSpace,
									nLevenberg, stopWhenSuccessful == 1);
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
