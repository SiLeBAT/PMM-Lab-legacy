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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.math.ParameterOptimizer;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class OneStepEstimationThread implements Runnable {

	private BufferedDataTable inTable;
	private KnimeSchema schema;
	private BufferedDataContainer container;

	private Map<String, Map<String, Point2D.Double>> parameterGuesses;

	private boolean enforceLimits;
	private int nParameterSpace;
	private int nLevenberg;
	private boolean stopWhenSuccessful;

	private AtomicInteger progress;

	public OneStepEstimationThread(BufferedDataTable inTable,
			KnimeSchema schema, BufferedDataContainer container,
			Map<String, Map<String, Point2D.Double>> parameterGuesses,
			boolean enforceLimits, int nParameterSpace, int nLevenberg,
			boolean stopWhenSuccessful, AtomicInteger progress) {
		this.inTable = inTable;
		this.schema = schema;
		this.container = container;
		this.parameterGuesses = parameterGuesses;
		this.enforceLimits = enforceLimits;
		this.nParameterSpace = nParameterSpace;
		this.nLevenberg = nLevenberg;
		this.stopWhenSuccessful = stopWhenSuccessful;
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
				PmmXmlDoc params = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
				String primID = ((CatalogModelXml) tuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).getID()
						+ "";
				Map<String, Point2D.Double> primaryGuesses = parameterGuesses
						.get(ModelEstimationNodeModel.PRIMARY + primID);

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

				String secID = ((CatalogModelXml) tuple.getPmmXml(
						Model2Schema.ATT_MODELCATALOG).get(0)).getID()
						+ "";
				PmmXmlDoc secParams = tuple
						.getPmmXml(Model2Schema.ATT_PARAMETER);
				Map<String, Point2D.Double> secGuesses = parameterGuesses
						.get(ModelEstimationNodeModel.SECONDARY + secID);

				if (secGuesses == null) {
					secGuesses = new LinkedHashMap<String, Point2D.Double>();
				}

				for (PmmXmlElementConvertable el : secParams.getElementSet()) {
					ParamXml element = (ParamXml) el;

					if (secGuesses.containsKey(element.getName())) {
						Point2D.Double guess = secGuesses
								.get(element.getName());

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

			List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(ModelCombiner
					.combine(seiTuples, true, true,
							new LinkedHashMap<String, String>()).keySet());
			Map<Integer, List<List<Double>>> argumentValuesMap = new LinkedHashMap<Integer, List<List<Double>>>();
			Map<Integer, List<Double>> targetValuesMap = new LinkedHashMap<Integer, List<Double>>();

			for (KnimeTuple tuple : tuples) {
				int id = ((CatalogModelXml) tuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).getID();
				PmmXmlDoc indepXml = tuple
						.getPmmXml(Model1Schema.ATT_INDEPENDENT);
				List<String> arguments = CellIO.getNameList(indepXml);
				PmmXmlDoc timeSeriesXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);

				List<Double> targetValues = new ArrayList<Double>();
				List<Double> timeList = new ArrayList<Double>();
				Map<String, List<Double>> miscLists = new LinkedHashMap<String, List<Double>>();
				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					timeList.add(element.getTime());
					targetValues.add(element.getConcentration());
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
					argumentValuesMap.put(id, new ArrayList<List<Double>>());

					for (int i = 0; i < arguments.size(); i++) {
						argumentValuesMap.get(id).add(new ArrayList<Double>());
					}
				}

				targetValuesMap.get(id).addAll(targetValues);

				for (int i = 0; i < arguments.size(); i++) {
					if (arguments.get(i).equals(AttributeUtilities.TIME)) {
						argumentValuesMap.get(id).get(i).addAll(timeList);
					} else {
						argumentValuesMap.get(id).get(i)
								.addAll(miscLists.get(arguments.get(i)));
					}
				}
			}

			Map<Integer, PmmXmlDoc> paramMap = new LinkedHashMap<Integer, PmmXmlDoc>();
			Map<Integer, PmmXmlDoc> indepMap = new LinkedHashMap<Integer, PmmXmlDoc>();
			Map<Integer, PmmXmlDoc> estModelMap = new LinkedHashMap<Integer, PmmXmlDoc>();
			int n = tuples.size();

			for (int i = 0; i < n; i++) {
				KnimeTuple tuple = tuples.get(i);
				PmmXmlDoc modelXml = tuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG);
				int id = ((CatalogModelXml) modelXml.get(0)).getID();

				if (!paramMap.containsKey(id)) {
					String formula = ((CatalogModelXml) modelXml.get(0))
							.getFormula();
					PmmXmlDoc paramXml = tuple
							.getPmmXml(Model1Schema.ATT_PARAMETER);
					PmmXmlDoc indepXml = tuple
							.getPmmXml(Model1Schema.ATT_INDEPENDENT);
					List<String> parameters = new ArrayList<String>();
					List<String> paramOrigNames = new ArrayList<String>();
					List<Double> minParameterValues = new ArrayList<Double>();
					List<Double> maxParameterValues = new ArrayList<Double>();
					List<Double> minGuessValues = new ArrayList<Double>();
					List<Double> maxGuessValues = new ArrayList<Double>();
					List<Double> targetValues = targetValuesMap.get(id);
					List<String> arguments = CellIO.getNameList(indepXml);
					List<List<Double>> argumentValues = argumentValuesMap
							.get(id);

					for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
						ParamXml element = (ParamXml) el;

						parameters.add(element.getName());
						paramOrigNames.add(element.getOrigName());
						minParameterValues.add(element.getMin());
						maxParameterValues.add(element.getMax());
						minGuessValues.add(element.getMinGuess());
						maxGuessValues.add(element.getMaxGuess());
					}

					MathUtilities
							.removeNullValues(targetValues, argumentValues);

					List<Double> parameterValues = Collections.nCopies(
							parameters.size(), null);
					List<Double> parameterErrors = Collections.nCopies(
							parameters.size(), null);
					List<Double> parameterTValues = Collections.nCopies(
							parameters.size(), null);
					List<Double> parameterPValues = Collections.nCopies(
							parameters.size(), null);
					List<List<Double>> covariances = new ArrayList<List<Double>>();

					for (int j = 0; j < parameters.size(); j++) {
						List<Double> nullList = Collections.nCopies(
								parameters.size(), null);

						covariances.add(nullList);
					}

					Double rms = null;
					Double rSquared = null;
					Double aic = null;
					Double bic = null;
					Integer dof = null;
					Integer estID = MathUtilities.getRandomNegativeInt();
					List<Double> minValues = Collections.nCopies(
							arguments.size(), null);
					List<Double> maxValues = Collections.nCopies(
							arguments.size(), null);
					boolean successful = false;
					ParameterOptimizer optimizer = null;

					if (!targetValues.isEmpty()) {
						optimizer = new ParameterOptimizer(formula, parameters,
								minParameterValues, maxParameterValues,
								minGuessValues, maxGuessValues, targetValues,
								arguments, argumentValues, enforceLimits);
						optimizer.optimize(progress, nParameterSpace,
								nLevenberg, stopWhenSuccessful);
						successful = optimizer.isSuccessful();
					}

					if (successful) {
						parameterValues = optimizer.getParameterValues();
						parameterErrors = optimizer
								.getParameterStandardErrors();
						parameterTValues = optimizer.getParameterTValues();
						parameterPValues = optimizer.getParameterPValues();
						covariances = optimizer.getCovariances();
						rms = optimizer.getRMS();
						rSquared = optimizer.getRSquare();
						aic = optimizer.getAIC();
						bic = optimizer.getBIC();
						dof = targetValues.size() - parameters.size();
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

						for (int k = 0; k < paramXml.getElementSet().size(); k++) {
							element.addCorrelation(
									((ParamXml) paramXml.get(k)).getOrigName(),
									covariances.get(j).get(k));
						}
					}

					for (int j = 0; j < indepXml.getElementSet().size(); j++) {
						IndepXml element = (IndepXml) indepXml.get(j);

						element.setMin(minValues.get(j));
						element.setMax(maxValues.get(j));
					}

					PmmXmlDoc estModelXml = tuple
							.getPmmXml(Model1Schema.ATT_ESTMODEL);

					((EstModelXml) estModelXml.get(0)).setID(estID);
					((EstModelXml) estModelXml.get(0)).setRMS(rms);
					((EstModelXml) estModelXml.get(0)).setR2(rSquared);
					((EstModelXml) estModelXml.get(0)).setAIC(aic);
					((EstModelXml) estModelXml.get(0)).setBIC(bic);
					((EstModelXml) estModelXml.get(0)).setDOF(dof);

					paramMap.put(id, paramXml);
					indepMap.put(id, indepXml);
					estModelMap.put(id, estModelXml);
				}

				tuple.setValue(Model1Schema.ATT_PARAMETER, paramMap.get(id));
				tuple.setValue(Model1Schema.ATT_INDEPENDENT, indepMap.get(id));
				tuple.setValue(Model1Schema.ATT_ESTMODEL, estModelMap.get(id));

				container.addRowToTable(tuple);
			}

			container.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
