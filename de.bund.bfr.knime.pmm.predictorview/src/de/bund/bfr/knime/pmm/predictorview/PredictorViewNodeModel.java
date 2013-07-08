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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartUtilities;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;

/**
 * This is the model implementation of PredictorView.
 * 
 * 
 * @author Christian Thoens
 */
public class PredictorViewNodeModel extends NodeModel {

	private SettingsHelper set;

	/**
	 * Constructor for the node model.
	 */
	protected PredictorViewNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] {
				BufferedDataTable.TYPE, ImagePortObject.TYPE });
		set = new SettingsHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
			throws Exception {
		DataTable table = (DataTable) inObjects[0];
		TableReader reader = new TableReader(table,
				set.getConcentrationParameters());
		ChartCreator creator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		BufferedDataContainer container = exec
				.createDataContainer(SchemaFactory.createDataSchema()
						.createSpec());

		if (set.getSelectedID() != null
				&& reader.getPlotables().get(set.getSelectedID()) != null) {
			Plotable plotable = reader.getPlotables().get(set.getSelectedID());
			Map<String, List<Double>> arguments = new LinkedHashMap<>();

			for (Map.Entry<String, Double> entry : set.getParamXValues()
					.entrySet()) {
				arguments.put(entry.getKey(), Arrays.asList(entry.getValue()));
			}

			plotable.setSamples(set.getTimeValues());
			plotable.setFunctionArguments(arguments);
			creator.setParamX(AttributeUtilities.TIME);
			creator.setParamY(plotable.getFunctionValue());
			creator.setUseManualRange(set.isManualRange());
			creator.setMinX(set.getMinX());
			creator.setMaxX(set.getMaxX());
			creator.setMinY(set.getMinY());
			creator.setMaxY(set.getMaxY());
			creator.setDrawLines(set.isDrawLines());
			creator.setShowLegend(set.isShowLegend());
			creator.setAddInfoInLegend(set.isAddLegendInfo());
			creator.setShowConfidenceInterval(set.isShowConfidence());
			creator.setUnitX(set.getUnitX());
			creator.setUnitY(set.getUnitY());
			creator.setTransformY(set.getTransformY());
			creator.setColors(set.getColors());
			creator.setShapes(set.getShapes());

			container.addRowToTable(createDataTuple(reader));
		}

		container.close();

		return new PortObject[] {
				container.getTable(),
				new ImagePortObject(ChartUtilities.convertToPNGImageContent(
						creator.getChart(set.getSelectedID()), 640, 480),
						new ImagePortObjectSpec(PNGImageContent.TYPE)) };
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
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		if (!SchemaFactory.createM1Schema()
				.conforms((DataTableSpec) inSpecs[0])) {
			throw new InvalidSettingsException("Wrong input!");
		}

		return new PortObjectSpec[] {
				SchemaFactory.createDataSchema().createSpec(),
				new ImagePortObjectSpec(PNGImageContent.TYPE) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		set.saveSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		set.loadSettings(settings);
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

	private KnimeTuple createDataTuple(TableReader reader) {
		KnimeTuple dataTuple;
		KnimeTuple tuple = reader.getTupleMap().get(set.getSelectedID());
		Plotable plotable = reader.getPlotables().get(set.getSelectedID());
		Map<String, List<Double>> conditions = plotable.getFunctionArguments();
		PmmXmlDoc miscXml;
		PmmXmlDoc timeSeriesXml = new PmmXmlDoc();
		Set<String> allMiscs = new LinkedHashSet<>();

		if (tuple.getSchema().conforms(SchemaFactory.createDataSchema())) {
			miscXml = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
		} else {
			miscXml = new PmmXmlDoc();
		}

		dataTuple = new KnimeTuple(SchemaFactory.createDataSchema());

		for (PmmXmlElementConvertable el : miscXml.getElementSet()) {
			MiscXml element = (MiscXml) el;

			if (conditions.containsKey(element.getName())) {
				element.setValue(conditions.get(element.getName()).get(0));
			}

			allMiscs.add(element.getName());
		}

		for (String cond : conditions.keySet()) {
			if (!allMiscs.contains(cond)
					&& !cond.equals(set.getConcentrationParameters().get(
							set.getSelectedID()))) {
				miscXml.add(new MiscXml(MathUtilities.getRandomNegativeInt(),
						cond, null, conditions.get(cond).get(0), plotable
								.getCategories().get(cond), plotable.getUnits()
								.get(cond), null));
			}
		}

		List<Double> values = new ArrayList<>();

		for (Double t : set.getTimeValues()) {
			values.add(Categories.getTimeCategory().convert(t, set.getUnitX(),
					plotable.getUnits().get(AttributeUtilities.TIME)));
		}

		plotable.setSamples(values);

		double[][] points = plotable.getFunctionSamplePoints(
				AttributeUtilities.TIME, AttributeUtilities.CONCENTRATION,
				set.getUnitX(), set.getUnitY(), ChartConstants.NO_TRANSFORM,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		double[][] errors = plotable.getFunctionSamplePointsErrors(
				AttributeUtilities.TIME, AttributeUtilities.CONCENTRATION,
				set.getUnitX(), set.getUnitY(), ChartConstants.NO_TRANSFORM,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		if (points != null && points.length == 2) {
			for (int i = 0; i < points[0].length; i++) {
				Double time = null;
				Double logc = null;
				Double error = null;

				if (!Double.isNaN(points[0][i])) {
					time = points[0][i];
				}

				if (!Double.isNaN(points[1][i])) {
					logc = points[1][i];
				}

				if (errors != null && errors.length == 2
						&& !Double.isNaN(errors[1][i])) {
					error = errors[1][i];
				}

				if (time != null || logc != null) {
					timeSeriesXml.add(new TimeSeriesXml(null, time, set
							.getUnitX(), logc, set.getUnitY(), error));
				}
			}
		}

		PmmXmlDoc agentXml = new PmmXmlDoc();
		PmmXmlDoc matrixXml = new PmmXmlDoc();
		PmmXmlDoc infoXml = new PmmXmlDoc();

		agentXml.add(new AgentXml());
		matrixXml.add(new MatrixXml());
		infoXml.add(new MdInfoXml(null, null, null, null, null));

		dataTuple.setValue(TimeSeriesSchema.ATT_MISC, miscXml);
		dataTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXml);
		dataTuple.setValue(TimeSeriesSchema.ATT_CONDID,
				MathUtilities.getRandomNegativeInt());
		dataTuple.setValue(TimeSeriesSchema.ATT_COMBASEID, null);
		dataTuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
		dataTuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);
		dataTuple.setValue(TimeSeriesSchema.ATT_MDINFO, infoXml);

		return dataTuple;
	}
}
