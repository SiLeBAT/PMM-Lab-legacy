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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * This is the model implementation of ModelEstimation.
 * 
 * 
 * @author Christian Thoens
 */
public class ModelEstimationNodeModel extends NodeModel {

	protected static final String PRIMARY = "Primary";
	protected static final String SECONDARY = "Secondary";

	protected static final String NO_FITTING = "";
	protected static final String PRIMARY_FITTING = "Primary Fitting";
	protected static final String SECONDARY_FITTING = "Secondary Fitting";
	protected static final String ONESTEP_FITTING = "One-Step Fitting";

	protected static final String CFGKEY_FITTINGTYPE = "FittingType";
	protected static final String CFGKEY_ENFORCELIMITS = "EnforceLimits";
	protected static final String CFGKEY_EXPERTSETTINGS = "ExpertSettings";
	protected static final String CFGKEY_NPARAMETERSPACE = "NParameterSpace";
	protected static final String CFGKEY_NLEVENBERG = "NLevenberg";
	protected static final String CFGKEY_STOPWHENSUCCESSFUL = "StopWhenSuccessful";
	protected static final String CFGKEY_PARAMETERGUESSES = "ParameterGuesses";

	protected static final String DEFAULT_FITTINGTYPE = NO_FITTING;
	protected static final int DEFAULT_ENFORCELIMITS = 0;
	protected static final int DEFAULT_EXPERTSETTINGS = 0;
	protected static final int DEFAULT_NPARAMETERSPACE = 10000;
	protected static final int DEFAULT_NLEVENBERG = 10;
	protected static final int DEFAULT_STOPWHENSUCCESSFUL = 0;

	private static final int MAX_THREADS = 8;

	private KnimeSchema schema;
	private KnimeSchema outSchema;

	private String fittingType;
	private int enforceLimits;
	private int expertSettings;
	private int nParameterSpace;
	private int nLevenberg;
	private int stopWhenSuccessful;
	private Map<String, Map<String, Point2D.Double>> parameterGuesses;
	private Map<String, Map<String, Point2D.Double>> parameterLimits;

	/**
	 * Constructor for the node model.
	 */
	protected ModelEstimationNodeModel() {
		super(1, 1);
		fittingType = DEFAULT_FITTINGTYPE;
		enforceLimits = DEFAULT_ENFORCELIMITS;
		expertSettings = DEFAULT_EXPERTSETTINGS;
		nParameterSpace = DEFAULT_NPARAMETERSPACE;
		nLevenberg = DEFAULT_NLEVENBERG;
		stopWhenSuccessful = DEFAULT_STOPWHENSUCCESSFUL;
		parameterGuesses = new LinkedHashMap<>();
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
			readPrimaryTable(table);
			outTable = doPrimaryEstimation(table, exec);
		} else if (fittingType.equals(SECONDARY_FITTING)) {
			readSecondaryTable(table);
			outTable = doSecondaryEstimation(table, exec);
		} else if (fittingType.equals(ONESTEP_FITTING)) {
			readSecondaryTable(table);
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
		if (fittingType.equals(NO_FITTING)) {
			throw new InvalidSettingsException("Node has to be configured!");
		} else if (fittingType.equals(PRIMARY_FITTING)) {
			if (SchemaFactory.createM1DataSchema().conforms(inSpecs[0])) {
				schema = SchemaFactory.createM1DataSchema();
				outSchema = SchemaFactory.createM1DataSchema();
			} else {
				throw new InvalidSettingsException("Wrong input!");
			}
		} else if (fittingType.equals(SECONDARY_FITTING)) {
			if (SchemaFactory.createM12DataSchema().conforms(inSpecs[0])) {
				schema = SchemaFactory.createM12DataSchema();
				outSchema = SchemaFactory.createM12DataSchema();
			} else {
				throw new InvalidSettingsException("Wrong input!");
			}
		} else if (fittingType.equals(ONESTEP_FITTING)) {
			if (SchemaFactory.createM12DataSchema().conforms(inSpecs[0])) {
				schema = SchemaFactory.createM12DataSchema();
				outSchema = SchemaFactory.createM1DataSchema();
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
		settings.addString(CFGKEY_FITTINGTYPE, fittingType);
		settings.addInt(CFGKEY_ENFORCELIMITS, enforceLimits);
		settings.addInt(CFGKEY_EXPERTSETTINGS, expertSettings);
		settings.addInt(CFGKEY_NPARAMETERSPACE, nParameterSpace);
		settings.addInt(CFGKEY_NLEVENBERG, nLevenberg);
		settings.addInt(CFGKEY_STOPWHENSUCCESSFUL, stopWhenSuccessful);
		settings.addString(CFGKEY_PARAMETERGUESSES,
				XmlConverter.objectToXml(parameterGuesses));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		fittingType = settings.getString(CFGKEY_FITTINGTYPE);
		enforceLimits = settings.getInt(CFGKEY_ENFORCELIMITS);
		expertSettings = settings.getInt(CFGKEY_EXPERTSETTINGS);
		nParameterSpace = settings.getInt(CFGKEY_NPARAMETERSPACE);
		nLevenberg = settings.getInt(CFGKEY_NLEVENBERG);
		stopWhenSuccessful = settings.getInt(CFGKEY_STOPWHENSUCCESSFUL);
		parameterGuesses = XmlConverter.xmlToObject(
				settings.getString(CFGKEY_PARAMETERGUESSES),
				new LinkedHashMap<String, Map<String, Point2D.Double>>());
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

	protected void setWarning(String warningMessage) {
		setWarningMessage(warningMessage);
	}

	private void readPrimaryTable(BufferedDataTable table) {
		parameterLimits = new LinkedHashMap<>();

		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createM1Schema(), table);

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			String id = PRIMARY
					+ ((CatalogModelXml) tuple.getPmmXml(
							Model1Schema.ATT_MODELCATALOG).get(0)).getID();

			if (!parameterLimits.containsKey(id)) {
				Map<String, Point2D.Double> limits = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : tuple.getPmmXml(
						Model1Schema.ATT_PARAMETER).getElementSet()) {
					ParamXml element = (ParamXml) el;
					double min = Double.NaN;
					double max = Double.NaN;

					if (element.getMin() != null) {
						min = element.getMin();
					}

					if (element.getMax() != null) {
						max = element.getMax();
					}

					limits.put(element.getName(), new Point2D.Double(min, max));
				}

				parameterLimits.put(id, limits);
			}
		}
	}

	private void readSecondaryTable(BufferedDataTable table) {
		readPrimaryTable(table);

		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createM2Schema(), table);

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			String id = SECONDARY
					+ ((CatalogModelXml) tuple.getPmmXml(
							Model2Schema.ATT_MODELCATALOG).get(0)).getID();

			if (!parameterLimits.containsKey(id)) {
				Map<String, Point2D.Double> limits = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : tuple.getPmmXml(
						Model2Schema.ATT_PARAMETER).getElementSet()) {
					ParamXml element = (ParamXml) el;
					double min = Double.NaN;
					double max = Double.NaN;

					if (element.getMin() != null) {
						min = element.getMin();
					}

					if (element.getMax() != null) {
						max = element.getMax();
					}

					limits.put(element.getName(), new Point2D.Double(min, max));
				}

				parameterLimits.put(id, limits);
			}
		}
	}

	private BufferedDataTable doPrimaryEstimation(BufferedDataTable table,
			ExecutionContext exec) throws CanceledExecutionException,
			InterruptedException {
		BufferedDataContainer container = exec.createDataContainer(outSchema
				.createSpec());
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		int n = table.getRowCount();
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(n);
		AtomicInteger runningThreads = new AtomicInteger(0);
		AtomicInteger finishedThreads = new AtomicInteger(0);
		Map<String, Map<String, Point2D.Double>> parameterGuesses;
		int nParameterSpace;
		int nLevenberg;
		int stopWhenSuccessful;

		if (expertSettings == 1) {
			parameterGuesses = this.parameterGuesses;
			nParameterSpace = this.nParameterSpace;
			nLevenberg = this.nLevenberg;
			stopWhenSuccessful = this.stopWhenSuccessful;
		} else {
			parameterGuesses = parameterLimits;
			nParameterSpace = DEFAULT_NPARAMETERSPACE;
			nLevenberg = DEFAULT_NLEVENBERG;
			stopWhenSuccessful = DEFAULT_STOPWHENSUCCESSFUL;
		}

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

			Thread thread = new Thread(new PrimaryEstimationThread(tuple,
					parameterGuesses, enforceLimits == 1, nParameterSpace,
					nLevenberg, stopWhenSuccessful == 1, runningThreads,
					finishedThreads));

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
			ExecutionContext exec) throws CanceledExecutionException,
			InterruptedException {
		BufferedDataContainer container = exec.createDataContainer(outSchema
				.createSpec());
		AtomicInteger progress = new AtomicInteger(Float.floatToIntBits(0.0f));
		Map<String, Map<String, Point2D.Double>> parameterGuesses;
		int nParameterSpace;
		int nLevenberg;
		int stopWhenSuccessful;

		if (expertSettings == 1) {
			parameterGuesses = this.parameterGuesses;
			nParameterSpace = this.nParameterSpace;
			nLevenberg = this.nLevenberg;
			stopWhenSuccessful = this.stopWhenSuccessful;
		} else {
			parameterGuesses = parameterLimits;
			nParameterSpace = DEFAULT_NPARAMETERSPACE;
			nLevenberg = DEFAULT_NLEVENBERG;
			stopWhenSuccessful = DEFAULT_STOPWHENSUCCESSFUL;
		}

		Thread thread = new Thread(new SecondaryEstimationThread(table, schema,
				container, parameterGuesses, enforceLimits == 1,
				nParameterSpace, nLevenberg, stopWhenSuccessful == 1, this,
				progress));

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
			ExecutionContext exec) throws CanceledExecutionException,
			InterruptedException {
		BufferedDataContainer container = exec.createDataContainer(outSchema
				.createSpec());
		AtomicInteger progress = new AtomicInteger(Float.floatToIntBits(0.0f));
		Map<String, Map<String, Point2D.Double>> parameterGuesses;
		int nParameterSpace;
		int nLevenberg;
		int stopWhenSuccessful;

		if (expertSettings == 1) {
			parameterGuesses = this.parameterGuesses;
			nParameterSpace = this.nParameterSpace;
			nLevenberg = this.nLevenberg;
			stopWhenSuccessful = this.stopWhenSuccessful;
		} else {
			parameterGuesses = parameterLimits;
			nParameterSpace = DEFAULT_NPARAMETERSPACE;
			nLevenberg = DEFAULT_NLEVENBERG;
			stopWhenSuccessful = DEFAULT_STOPWHENSUCCESSFUL;
		}

		Thread thread = new Thread(new OneStepEstimationThread(table, schema,
				container, parameterGuesses, enforceLimits == 1,
				nParameterSpace, nLevenberg, stopWhenSuccessful == 1, progress));

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

}
