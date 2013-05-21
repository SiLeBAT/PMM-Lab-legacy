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
package de.bund.bfr.knime.pmm.modelselectiontertiary;

import java.awt.Color;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartUtilities;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * This is the model implementation of ModelSelectionTertiary.
 * 
 * 
 * @author Christian Thoens
 */
public class ModelSelectionTertiaryNodeModel extends NodeModel {

	static final String CFG_SELECTEDIDS = "SelectedIDs";
	static final String CFG_COLORS = "Colors";
	static final String CFG_SHAPES = "Shapes";
	static final String CFG_SELECTALLIDS = "SelectAllIDs";
	static final String CFG_MANUALRANGE = "ManualRange";
	static final String CFG_MINX = "MinX";
	static final String CFG_MAXX = "MaxX";
	static final String CFG_MINY = "MinY";
	static final String CFG_MAXY = "MaxY";
	static final String CFG_DRAWLINES = "DrawLines";
	static final String CFG_SHOWLEGEND = "ShowLegend";
	static final String CFG_ADDLEGENDINFO = "AddLegendInfo";
	static final String CFG_DISPLAYHIGHLIGHTED = "DisplayHighlighted";
	static final String CFG_UNITX = "UnitX";
	static final String CFG_UNITY = "UnitY";
	static final String CFG_TRANSFORMY = "TransformY";
	static final String CFG_STANDARDVISIBLECOLUMNS = "StandardVisibleColumns";
	static final String CFG_VISIBLECOLUMNS = "VisibleColumns";
	static final String CFG_MODELFILTER = "ModelFilter";
	static final String CFG_DATAFILTER = "DataFilter";
	static final String CFG_FITTEDFILTER = "FittedFilter";

	static final int DEFAULT_SELECTALLIDS = 0;
	static final int DEFAULT_MANUALRANGE = 0;
	static final double DEFAULT_MINX = 0.0;
	static final double DEFAULT_MAXX = 100.0;
	static final double DEFAULT_MINY = 0.0;
	static final double DEFAULT_MAXY = 10.0;
	static final int DEFAULT_DRAWLINES = 0;
	static final int DEFAULT_SHOWLEGEND = 1;
	static final int DEFAULT_ADDLEGENDINFO = 0;
	static final int DEFAULT_DISPLAYHIGHLIGHTED = 0;
	static final String DEFAULT_UNITX = AttributeUtilities.HOURS;
	static final String DEFAULT_UNITY = AttributeUtilities.LOGCFU;
	static final String DEFAULT_TRANSFORMY = ChartConstants.NO_TRANSFORM;
	static final int DEFAULT_STANDARDVISIBLECOLUMNS = 1;
	static final String DEFAULT_MODELFILTER = "";
	static final String DEFAULT_DATAFILTER = "";
	static final String DEFAULT_FITTEDFILTER = "";

	private List<String> selectedIDs;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;
	private int selectAllIDs;
	private int manualRange;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private int drawLines;
	private int showLegend;
	private int addLegendInfo;
	private int displayHighlighted;
	private String unitX;
	private String unitY;
	private String transformY;
	private int standardVisibleColumns;
	private List<String> visibleColumns;
	private String modelFilter;
	private String dataFilter;
	private String fittedFilter;

	private KnimeSchema schema;

	/**
	 * Constructor for the node model.
	 */
	protected ModelSelectionTertiaryNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] {
				BufferedDataTable.TYPE, ImagePortObject.TYPE });
		selectedIDs = new ArrayList<String>();
		colors = new LinkedHashMap<String, Color>();
		shapes = new LinkedHashMap<String, Shape>();
		selectAllIDs = DEFAULT_SELECTALLIDS;
		manualRange = DEFAULT_MANUALRANGE;
		minX = DEFAULT_MINX;
		maxX = DEFAULT_MAXX;
		minY = DEFAULT_MINY;
		maxY = DEFAULT_MAXY;
		drawLines = DEFAULT_DRAWLINES;
		showLegend = DEFAULT_SHOWLEGEND;
		addLegendInfo = DEFAULT_ADDLEGENDINFO;
		displayHighlighted = DEFAULT_DISPLAYHIGHLIGHTED;
		unitX = DEFAULT_UNITX;
		unitY = DEFAULT_UNITY;
		transformY = DEFAULT_TRANSFORMY;
		standardVisibleColumns = DEFAULT_STANDARDVISIBLECOLUMNS;
		visibleColumns = new ArrayList<>();
		modelFilter = DEFAULT_MODELFILTER;
		dataFilter = DEFAULT_DATAFILTER;
		fittedFilter = DEFAULT_FITTEDFILTER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
			throws Exception {
		BufferedDataTable table = (BufferedDataTable) inObjects[0];
		TableReader reader = new TableReader(table,
				SchemaFactory.isM12DataSchema(schema));
		List<String> ids;

		if (selectAllIDs == 1) {
			ids = reader.getIds();
		} else {
			ids = selectedIDs;
		}

		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		Set<String> idSet = new LinkedHashSet<String>(ids);
		int index = 0;

		for (int i = 0; i < reader.getAllTuples().size(); i++) {
			KnimeTuple tuple = reader.getAllTuples().get(i);
			List<KnimeTuple> usedTuples = reader.getTupleCombinations().get(
					tuple);
			String id = reader.getAllIds().get(i);

			if (idSet.contains(id)) {
				for (KnimeTuple t : usedTuples) {
					container.addRowToTable(t);
				}
			}

			exec.checkCanceled();
			exec.setProgress((double) index
					/ (double) reader.getAllTuples().size(), "");
			index++;
		}

		container.close();

		ChartCreator creator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());

		creator.setParamX(AttributeUtilities.TIME);
		creator.setParamY(AttributeUtilities.LOGC);
		creator.setColors(colors);
		creator.setShapes(shapes);
		creator.setUseManualRange(manualRange == 1);
		creator.setMinX(minX);
		creator.setMaxX(maxX);
		creator.setMinY(minY);
		creator.setMaxY(maxY);
		creator.setDrawLines(drawLines == 1);
		creator.setShowLegend(showLegend == 1);
		creator.setAddInfoInLegend(addLegendInfo == 1);
		creator.setUnitX(unitX);
		creator.setUnitY(unitY);
		creator.setTransformY(transformY);

		return new PortObject[] {
				container.getTable(),
				new ImagePortObject(ChartUtilities.convertToPNGImageContent(
						creator.getChart(ids), 640, 480),
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
		if (SchemaFactory.createM12DataSchema().conforms(
				(DataTableSpec) inSpecs[0])) {
			schema = SchemaFactory.createM12DataSchema();
		} else if (SchemaFactory.createM12Schema().conforms(
				(DataTableSpec) inSpecs[0])) {
			schema = SchemaFactory.createM12Schema();
		} else {
			throw new InvalidSettingsException("Wrong input!");
		}

		return new PortObjectSpec[] { schema.createSpec(),
				new ImagePortObjectSpec(PNGImageContent.TYPE) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFG_SELECTEDIDS,
				XmlConverter.objectToXml(selectedIDs));
		settings.addString(CFG_COLORS, XmlConverter.colorMapToXml(colors));
		settings.addString(CFG_SHAPES, XmlConverter.shapeMapToXml(shapes));
		settings.addInt(CFG_SELECTALLIDS, selectAllIDs);
		settings.addInt(CFG_MANUALRANGE, manualRange);
		settings.addDouble(CFG_MINX, minX);
		settings.addDouble(CFG_MAXX, maxX);
		settings.addDouble(CFG_MINY, minY);
		settings.addDouble(CFG_MAXY, maxY);
		settings.addInt(CFG_DRAWLINES, drawLines);
		settings.addInt(CFG_SHOWLEGEND, showLegend);
		settings.addInt(CFG_ADDLEGENDINFO, addLegendInfo);
		settings.addInt(CFG_DISPLAYHIGHLIGHTED, displayHighlighted);
		settings.addString(CFG_UNITX, unitX);
		settings.addString(CFG_UNITY, unitY);
		settings.addString(CFG_TRANSFORMY, transformY);
		settings.addInt(CFG_STANDARDVISIBLECOLUMNS, standardVisibleColumns);
		settings.addString(CFG_VISIBLECOLUMNS,
				XmlConverter.objectToXml(visibleColumns));
		settings.addString(CFG_MODELFILTER, modelFilter);
		settings.addString(CFG_DATAFILTER, dataFilter);
		settings.addString(CFG_FITTEDFILTER, fittedFilter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		selectedIDs = XmlConverter.xmlToObject(
				settings.getString(CFG_SELECTEDIDS), new ArrayList<String>());
		colors = XmlConverter.xmlToColorMap(settings.getString(CFG_COLORS));
		shapes = XmlConverter.xmlToShapeMap(settings.getString(CFG_SHAPES));
		selectAllIDs = settings.getInt(CFG_SELECTALLIDS);
		manualRange = settings.getInt(CFG_MANUALRANGE);
		minX = settings.getDouble(CFG_MINX);
		maxX = settings.getDouble(CFG_MAXX);
		minY = settings.getDouble(CFG_MINY);
		maxY = settings.getDouble(CFG_MAXY);
		drawLines = settings.getInt(CFG_DRAWLINES);
		showLegend = settings.getInt(CFG_SHOWLEGEND);
		addLegendInfo = settings.getInt(CFG_ADDLEGENDINFO);
		displayHighlighted = settings.getInt(CFG_DISPLAYHIGHLIGHTED);
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
