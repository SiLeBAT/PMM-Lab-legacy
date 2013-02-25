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
package de.bund.bfr.knime.pmm.secondarymodelanddataview;

import java.awt.Color;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.image.png.PNGImageContent;
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
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * This is the model implementation of SecondaryModelAndDataView.
 * 
 * 
 * @author Christian Thoens
 */
public class SecondaryModelAndDataViewNodeModel extends NodeModel {

	protected static final String CFG_SELECTEDID = "SelectedID";
	protected static final String CFG_CURRENTPARAMX = "CurrentParamX";
	protected static final String CFG_SELECTEDVALUESX = "SelectedValuesX";
	protected static final String CFG_COLORS = "Colors";
	protected static final String CFG_SHAPES = "Shapes";
	protected static final String CFG_COLORLISTS = "ColorLists";
	protected static final String CFG_SHAPELISTS = "ShapeLists";

	private String selectedID;
	private String currentParamX;
	private Map<String, List<Boolean>> selectedValuesX;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;
	private Map<String, List<Color>> colorLists;
	private Map<String, List<Shape>> shapeLists;

	/**
	 * Constructor for the node model.
	 */
	protected SecondaryModelAndDataViewNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE },
				new PortType[] { ImagePortObject.TYPE });
		selectedID = null;
		currentParamX = null;
		selectedValuesX = new LinkedHashMap<>();
		colors = new LinkedHashMap<>();
		shapes = new LinkedHashMap<>();
		colorLists = new LinkedHashMap<>();
		shapeLists = new LinkedHashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
			throws Exception {
		DataTable table = (DataTable) inObjects[0];
		TableReader reader;
		boolean containsData;

		if (SchemaFactory.createDataSchema().conforms(table)) {
			reader = new TableReader(table, true);

			if (Collections.max(reader.getColorCounts()) == 0) {
				reader = new TableReader(table, false);
				containsData = false;
			} else {
				containsData = true;
			}
		} else {
			reader = new TableReader(table, false);
			containsData = false;
		}

		ChartCreator creator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());

		if (selectedID != null) {
			Plotable plotable = reader.getPlotables().get(selectedID);
			Map<String, List<Double>> arguments = new LinkedHashMap<>();

			for (String param : selectedValuesX.keySet()) {
				List<Double> values = new ArrayList<>();

				for (int i = 0; i < selectedValuesX.get(param).size(); i++) {
					if (selectedValuesX.get(param).get(i)) {
						values.add(plotable.getValueList(param).get(i));
					}
				}

				arguments.put(param, values);
			}

			plotable.setFunctionArguments(arguments);
			creator.setParamX(currentParamX);
			creator.setParamY(plotable.getFunctionValue());
			creator.setTransformY(ChartConstants.NO_TRANSFORM);

			if (containsData) {
				creator.setColorLists(colorLists);
				creator.setShapeLists(shapeLists);
			} else {
				creator.setColors(colors);
				creator.setShapes(shapes);
			}
		}

		return new PortObject[] { new ImagePortObject(
				ChartUtilities.convertToPNGImageContent(
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
		if (!SchemaFactory.createM2Schema()
				.conforms((DataTableSpec) inSpecs[0])) {
			throw new InvalidSettingsException("Wrong input!");
		}

		return new PortObjectSpec[] { new ImagePortObjectSpec(
				PNGImageContent.TYPE) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFG_SELECTEDID, selectedID);
		settings.addString(CFG_CURRENTPARAMX, currentParamX);
		settings.addString(CFG_SELECTEDVALUESX,
				XmlConverter.mapToXml(selectedValuesX));
		settings.addString(CFG_COLORS, XmlConverter.colorMapToXml(colors));
		settings.addString(CFG_SHAPES, XmlConverter.shapeMapToXml(shapes));
		settings.addString(CFG_COLORLISTS,
				XmlConverter.colorListMapToXml(colorLists));
		settings.addString(CFG_SHAPELISTS,
				XmlConverter.shapeListMapToXml(shapeLists));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		selectedID = settings.getString(CFG_SELECTEDID);
		currentParamX = settings.getString(CFG_CURRENTPARAMX);
		selectedValuesX = XmlConverter.xmlToBoolListMap(settings
				.getString(CFG_SELECTEDVALUESX));
		colors = XmlConverter.xmlToColorMap(settings.getString(CFG_COLORS));
		shapes = XmlConverter.xmlToShapeMap(settings.getString(CFG_SHAPES));
		colorLists = XmlConverter.xmlToColorListMap(settings
				.getString(CFG_COLORLISTS));
		shapeLists = XmlConverter.xmlToShapeListMap(settings
				.getString(CFG_SHAPELISTS));
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
