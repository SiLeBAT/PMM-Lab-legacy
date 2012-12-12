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
package de.bund.bfr.knime.gis.regionvisualizer;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.eclipse.stem.gis.ShapefileReader;
import org.eclipse.stem.gis.dbf.DbfFieldDef;
import org.eclipse.stem.gis.shp.ShpPolygon;
import org.eclipse.stem.gis.shp.ShpRecord;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.RowIterator;
import org.knime.core.node.NodeView;

import de.bund.bfr.knime.gis.GISCanvas;

/**
 * <code>NodeView</code> for the "RegionVisualizer" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class RegionVisualizerNodeView extends
		NodeView<RegionVisualizerNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link GISVisualizationNodeModel})
	 */
	protected RegionVisualizerNodeView(final RegionVisualizerNodeModel nodeModel) {
		super(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {
		try {
			GISCanvas canvas = createGISCanvas(getNodeModel().getFileName(),
					getNodeModel().getFileIdColumn());
			Map<String, Double> dataMap = createDataMap(getNodeModel()
					.getTable(), getNodeModel().getTableIdColumn(),
					getNodeModel().getTableValueColumn());

			canvas.setRegionData(dataMap);

			JPanel panel = new JPanel();

			panel.setLayout(new BorderLayout());
			panel.add(canvas, BorderLayout.CENTER);

			setComponent(panel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private GISCanvas createGISCanvas(String fileName, String fileIdColumn)
			throws IOException {
		ShapefileReader reader = new ShapefileReader(new File(fileName));
		List<DbfFieldDef> fields = reader.getTableHeader()
				.getFieldDefinitions();
		Map<String, ShpPolygon> shapes = new LinkedHashMap<String, ShpPolygon>();
		int idColumnIndex = -1;

		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).getFieldName().trim().equals(fileIdColumn)) {
				idColumnIndex = i;
			}
		}

		while (reader.hasMoreRecords()) {
			ShpRecord shp = reader.getNextRecord();

			if (shp instanceof ShpPolygon) {
				String id = shp.getTableAttributes().getData()
						.get(idColumnIndex).toString().trim();

				shapes.put(id, (ShpPolygon) shp);
			}
		}

		return new GISCanvas(shapes);
	}

	private Map<String, Double> createDataMap(DataTable table, String idColumn,
			String valueColumn) {
		Map<String, Double> dataMap = new LinkedHashMap<String, Double>();
		int idIndex = table.getDataTableSpec().findColumnIndex(idColumn);
		int valueIndex = table.getDataTableSpec().findColumnIndex(valueColumn);
		RowIterator it = table.iterator();

		while (it.hasNext()) {
			DataRow row = it.next();

			try {
				String id = row.getCell(idIndex).toString().trim();
				double value = Double.parseDouble(row.getCell(valueIndex)
						.toString().trim());

				dataMap.put(id, value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		return dataMap;
	}

}
