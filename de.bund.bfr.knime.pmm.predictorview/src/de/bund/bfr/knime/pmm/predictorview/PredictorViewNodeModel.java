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

import java.awt.Color;
import java.awt.Shape;
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
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartUtilities;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Time;

/**
 * This is the model implementation of PredictorView.
 * 
 * 
 * @author Christian Thoens
 */
public class PredictorViewNodeModel extends NodeModel {

	protected static final String CFG_SELECTEDID = "SelectedID";
	protected static final String CFG_PARAMXVALUES = "ParamXValues";
	protected static final String CFG_TIMEVALUES = "TimeValues";
	protected static final String CFG_COLORS = "Colors";
	protected static final String CFG_SHAPES = "Shapes";
	protected static final String CFG_MANUALRANGE = "ManualRange";
	protected static final String CFG_MINX = "MinX";
	protected static final String CFG_MAXX = "MaxX";
	protected static final String CFG_MINY = "MinY";
	protected static final String CFG_MAXY = "MaxY";
	protected static final String CFG_DRAWLINES = "DrawLines";
	protected static final String CFG_SHOWLEGEND = "ShowLegend";
	protected static final String CFG_ADDLEGENDINFO = "AddLegendInfo";
	protected static final String CFG_DISPLAYHIGHLIGHTED = "DisplayHighlighted";
	protected static final String CFG_SHOWCONFIDENCE = "ShowConfidence";
	protected static final String CFG_UNITX = "UnitX";
	protected static final String CFG_UNITY = "UnitY";
	protected static final String CFG_TRANSFORMY = "TransformY";
	protected static final String CFG_STANDARDVISIBLECOLUMNS = "StandardVisibleColumns";
	protected static final String CFG_VISIBLECOLUMNS = "VisibleColumns";
	protected static final String CFG_MODELFILTER = "ModelFilter";
	protected static final String CFG_DATAFILTER = "DataFilter";
	protected static final String CFG_FITTEDFILTER = "FittedFilter";
	protected static final String CFGKEY_CONCENTRATIONPARAMETERS = "ConcentrationParameters";

	protected static final int DEFAULT_MANUALRANGE = 0;
	protected static final double DEFAULT_MINX = 0.0;
	protected static final double DEFAULT_MAXX = 100.0;
	protected static final double DEFAULT_MINY = 0.0;
	protected static final double DEFAULT_MAXY = 10.0;
	protected static final int DEFAULT_DRAWLINES = 0;
	protected static final int DEFAULT_SHOWLEGEND = 1;
	protected static final int DEFAULT_ADDLEGENDINFO = 0;
	protected static final int DEFAULT_DISPLAYHIGHLIGHTED = 0;
	protected static final int DEFAULT_SHOWCONFIDENCE = 0;
	protected static final String DEFAULT_TRANSFORMY = ChartConstants.NO_TRANSFORM;
	protected static final int DEFAULT_STANDARDVISIBLECOLUMNS = 1;

	private String selectedID;
	private Map<String, Double> paramXValues;
	private List<Double> timeValues;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;
	private int manualRange;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private int drawLines;
	private int showLegend;
	private int addLegendInfo;
	private int displayHighlighted;
	private int showConfidence;
	private String unitX;
	private String unitY;
	private String transformY;
	private int standardVisibleColumns;
	private List<String> visibleColumns;
	private String modelFilter;
	private String dataFilter;
	private String fittedFilter;
	private Map<String, String> concentrationParameters;

	/**
	 * Constructor for the node model.
	 */
	protected PredictorViewNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] {
				BufferedDataTable.TYPE, ImagePortObject.TYPE });
		selectedID = null;
		paramXValues = new LinkedHashMap<>();
		timeValues = new ArrayList<>();
		colors = new LinkedHashMap<>();
		shapes = new LinkedHashMap<>();
		manualRange = DEFAULT_MANUALRANGE;
		minX = DEFAULT_MINX;
		maxX = DEFAULT_MAXX;
		minY = DEFAULT_MINY;
		maxY = DEFAULT_MAXY;
		drawLines = DEFAULT_DRAWLINES;
		showLegend = DEFAULT_SHOWLEGEND;
		addLegendInfo = DEFAULT_ADDLEGENDINFO;
		displayHighlighted = DEFAULT_DISPLAYHIGHLIGHTED;
		showConfidence = DEFAULT_SHOWCONFIDENCE;
		unitX = null;
		unitY = null;
		transformY = DEFAULT_TRANSFORMY;
		standardVisibleColumns = DEFAULT_STANDARDVISIBLECOLUMNS;
		visibleColumns = new ArrayList<>();
		modelFilter = null;
		dataFilter = null;
		fittedFilter = null;
		concentrationParameters = new LinkedHashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
			throws Exception {
		DataTable table = (DataTable) inObjects[0];
		TableReader reader = new TableReader(table, concentrationParameters);
		ChartCreator creator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		BufferedDataContainer container = exec
				.createDataContainer(SchemaFactory.createDataSchema()
						.createSpec());

		if (selectedID != null && reader.getPlotables().get(selectedID) != null) {
			Plotable plotable = reader.getPlotables().get(selectedID);
			Map<String, List<Double>> arguments = new LinkedHashMap<>();

			for (Map.Entry<String, Double> entry : paramXValues.entrySet()) {
				arguments.put(entry.getKey(), Arrays.asList(entry.getValue()));
			}

			plotable.setSamples(timeValues);
			plotable.setFunctionArguments(arguments);
			creator.setParamX(AttributeUtilities.TIME);
			creator.setParamY(plotable.getFunctionValue());
			creator.setUseManualRange(manualRange == 1);
			creator.setMinX(minX);
			creator.setMaxX(maxX);
			creator.setMinY(minY);
			creator.setMaxY(maxY);
			creator.setDrawLines(drawLines == 1);
			creator.setShowLegend(showLegend == 1);
			creator.setAddInfoInLegend(addLegendInfo == 1);
			creator.setShowConfidenceInterval(showConfidence == 1);
			creator.setUnitX(unitX);
			creator.setUnitY(unitY);
			creator.setTransformY(transformY);
			creator.setColors(colors);
			creator.setShapes(shapes);

			container.addRowToTable(createDataTuple(reader));
		}

		container.close();

		return new PortObject[] {
				container.getTable(),
				new ImagePortObject(ChartUtilities.convertToPNGImageContent(
						creator.getChart(selectedID), 640, 480),
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
		settings.addString(CFG_SELECTEDID, selectedID);
		settings.addString(CFG_PARAMXVALUES,
				XmlConverter.objectToXml(paramXValues));
		settings.addString(CFG_TIMEVALUES, XmlConverter.objectToXml(timeValues));
		settings.addString(CFG_COLORS, XmlConverter.colorMapToXml(colors));
		settings.addString(CFG_SHAPES, XmlConverter.shapeMapToXml(shapes));
		settings.addInt(CFG_MANUALRANGE, manualRange);
		settings.addDouble(CFG_MINX, minX);
		settings.addDouble(CFG_MAXX, maxX);
		settings.addDouble(CFG_MINY, minY);
		settings.addDouble(CFG_MAXY, maxY);
		settings.addInt(CFG_DRAWLINES, drawLines);
		settings.addInt(CFG_SHOWLEGEND, showLegend);
		settings.addInt(CFG_ADDLEGENDINFO, addLegendInfo);
		settings.addInt(CFG_DISPLAYHIGHLIGHTED, displayHighlighted);
		settings.addInt(CFG_SHOWCONFIDENCE, showConfidence);
		settings.addString(CFG_UNITX, unitX);
		settings.addString(CFG_UNITY, unitY);
		settings.addString(CFG_TRANSFORMY, transformY);
		settings.addInt(CFG_STANDARDVISIBLECOLUMNS, standardVisibleColumns);
		settings.addString(CFG_VISIBLECOLUMNS,
				XmlConverter.objectToXml(visibleColumns));
		settings.addString(CFG_MODELFILTER, modelFilter);
		settings.addString(CFG_DATAFILTER, dataFilter);
		settings.addString(CFG_FITTEDFILTER, fittedFilter);
		settings.addString(CFGKEY_CONCENTRATIONPARAMETERS,
				XmlConverter.objectToXml(concentrationParameters));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		selectedID = settings.getString(CFG_SELECTEDID);
		paramXValues = XmlConverter.xmlToObject(
				settings.getString(CFG_PARAMXVALUES),
				new LinkedHashMap<String, Double>());
		timeValues = XmlConverter.xmlToObject(
				settings.getString(CFG_TIMEVALUES), new ArrayList<Double>());
		colors = XmlConverter.xmlToColorMap(settings.getString(CFG_COLORS));
		shapes = XmlConverter.xmlToShapeMap(settings.getString(CFG_SHAPES));
		manualRange = settings.getInt(CFG_MANUALRANGE);
		minX = settings.getDouble(CFG_MINX);
		maxX = settings.getDouble(CFG_MAXX);
		minY = settings.getDouble(CFG_MINY);
		maxY = settings.getDouble(CFG_MAXY);
		drawLines = settings.getInt(CFG_DRAWLINES);
		showLegend = settings.getInt(CFG_SHOWLEGEND);
		addLegendInfo = settings.getInt(CFG_ADDLEGENDINFO);
		displayHighlighted = settings.getInt(CFG_DISPLAYHIGHLIGHTED);
		showConfidence = settings.getInt(CFG_SHOWCONFIDENCE);
		unitX = settings.getString(CFG_UNITX);
		unitY = settings.getString(CFG_UNITY);
		transformY = settings.getString(CFG_TRANSFORMY);
		standardVisibleColumns = settings.getInt(CFG_STANDARDVISIBLECOLUMNS);
		visibleColumns = XmlConverter
				.xmlToObject(settings.getString(CFG_VISIBLECOLUMNS),
						new ArrayList<String>());
		modelFilter = settings.getString(CFG_MODELFILTER);
		dataFilter = settings.getString(CFG_DATAFILTER);
		fittedFilter = settings.getString(CFG_FITTEDFILTER);
		concentrationParameters = XmlConverter.xmlToObject(
				settings.getString(CFGKEY_CONCENTRATIONPARAMETERS),
				new LinkedHashMap<String, String>());
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
		KnimeTuple tuple = reader.getTupleMap().get(selectedID);
		Plotable plotable = reader.getPlotables().get(selectedID);
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
					&& !cond.equals(concentrationParameters.get(selectedID))) {
				miscXml.add(new MiscXml(MathUtilities.getRandomNegativeInt(),
						cond, null, conditions.get(cond).get(0), plotable
								.getCategories().get(cond), plotable.getUnits()
								.get(cond), null));
			}
		}

		List<Double> values = new ArrayList<>();

		for (Double t : timeValues) {
			values.add(new Time().convert(t, unitX,
					plotable.getUnits().get(AttributeUtilities.TIME)));
		}

		plotable.setSamples(values);

		String timeUnit = unitX;
		String concentrationUnit = unitY;
		double[][] points = plotable.getFunctionSamplePoints(
				AttributeUtilities.TIME, AttributeUtilities.LOGC, timeUnit,
				concentrationUnit, ChartConstants.NO_TRANSFORM,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		if (points != null && points.length == 2) {
			for (int i = 0; i < points[0].length; i++) {
				Double time = null;
				Double logc = null;

				if (!Double.isNaN(points[0][i])) {
					time = points[0][i];
				}

				if (!Double.isNaN(points[1][i])) {
					logc = points[1][i];
				}

				if (time != null || logc != null) {
					timeSeriesXml.add(new TimeSeriesXml(null, time, timeUnit,
							logc, concentrationUnit));
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
