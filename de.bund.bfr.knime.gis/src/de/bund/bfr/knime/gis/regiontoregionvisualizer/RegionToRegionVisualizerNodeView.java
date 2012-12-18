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
package de.bund.bfr.knime.gis.regiontoregionvisualizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JSplitPane;

import org.eclipse.stem.gis.ShapefileReader;
import org.eclipse.stem.gis.dbf.DbfFieldDef;
import org.eclipse.stem.gis.shp.ShpPolygon;
import org.eclipse.stem.gis.shp.ShpRecord;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.RowIterator;
import org.knime.core.node.NodeView;

import de.bund.bfr.knime.gis.GISCanvas;
import de.bund.bfr.knime.gis.GraphCanvas;

/**
 * <code>NodeView</code> for the "RegionToRegionVisualizer" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class RegionToRegionVisualizerNodeView extends
		NodeView<RegionToRegionVisualizerNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link GISVisualizationNodeModel})
	 */
	protected RegionToRegionVisualizerNodeView(
			final RegionToRegionVisualizerNodeModel nodeModel) {
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
			GraphCanvas graphCanvas = createGraphCanvas(getNodeModel()
					.getNodeTable(), getNodeModel().getEdgeTable(),
					getNodeModel().getNodeIdColumn(), getNodeModel()
							.getNodeRegionIdColumn(), getNodeModel()
							.getEdgeFromColumn(), getNodeModel()
							.getEdgeToColumn(), getNodeModel()
							.getEdgeValueColumn());
			GISCanvas gisCanvas = createGISCanvas(getNodeModel().getFileName(),
					getNodeModel().getFileRegionIdColumn());
			Map<String, String> idToRegionMap = createIdToRegionMap(
					getNodeModel().getNodeTable(), getNodeModel()
							.getNodeIdColumn(), getNodeModel()
							.getNodeRegionIdColumn());
			Map<String, Double> regionData = createRegionDataMap(getNodeModel()
					.getNodeTable(), getNodeModel().getNodeIdColumn(),
					getNodeModel().getNodeValueColumn(), idToRegionMap);
			Map<GISCanvas.Edge, Double> edgeData = createEdgeDataMap(
					getNodeModel().getEdgeTable(), getNodeModel()
							.getEdgeFromColumn(), getNodeModel()
							.getEdgeToColumn(), getNodeModel()
							.getEdgeValueColumn(), idToRegionMap);

			gisCanvas.setRegionData(regionData);
			gisCanvas.setEdgeData(edgeData);

			JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					graphCanvas, gisCanvas);

			panel.setResizeWeight(0.5);
			setComponent(panel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private GraphCanvas createGraphCanvas(DataTable nodeTable,
			DataTable edgeTable, String nodeIdColumn, String nodeRegionColumn,
			String edgeFromColumn, String edgeToColumn, String edgeValueColumn) {
		int nodeIdIndex = nodeTable.getDataTableSpec().findColumnIndex(
				nodeIdColumn);
		int nodeRegionIndex = nodeTable.getDataTableSpec().findColumnIndex(
				nodeRegionColumn);
		int edgeFromIndex = edgeTable.getDataTableSpec().findColumnIndex(
				edgeFromColumn);
		int edgeToIndex = edgeTable.getDataTableSpec().findColumnIndex(
				edgeToColumn);
		int edgeValueIndex = edgeTable.getDataTableSpec().findColumnIndex(
				edgeValueColumn);
		Map<String, GraphCanvas.Node> nodes = new LinkedHashMap<String, GraphCanvas.Node>();
		List<GraphCanvas.Edge> edges = new ArrayList<GraphCanvas.Edge>();

		RowIterator nodeIt = nodeTable.iterator();

		while (nodeIt.hasNext()) {
			try {
				DataRow row = nodeIt.next();
				String id = row.getCell(nodeIdIndex).toString().trim();
				String region = row.getCell(nodeRegionIndex).toString().trim();
				Map<String, String> properties = new LinkedHashMap<String, String>();

				for (int i = 0; i < nodeTable.getDataTableSpec()
						.getNumColumns(); i++) {
					properties.put(nodeTable.getDataTableSpec()
							.getColumnSpec(i).getName().trim(), row.getCell(i)
							.toString().trim());
				}

				nodes.put(id, new GraphCanvas.Node(region, properties));
			} catch (Exception e) {
			}
		}

		RowIterator edgeIt = edgeTable.iterator();

		while (edgeIt.hasNext()) {
			try {
				DataRow row = edgeIt.next();
				String from = row.getCell(edgeFromIndex).toString().trim();
				String to = row.getCell(edgeToIndex).toString().trim();
				double value = Double.parseDouble(row.getCell(edgeValueIndex)
						.toString().trim());
				GraphCanvas.Node node1 = nodes.get(from);
				GraphCanvas.Node node2 = nodes.get(to);

				if (node1 != null && node2 != null) {
					edges.add(new GraphCanvas.Edge(node1, node2, value));
				}
			} catch (Exception e) {
			}
		}

		return new GraphCanvas(new ArrayList<GraphCanvas.Node>(nodes.values()),
				edges);
	}

	private GISCanvas createGISCanvas(String fileName, String fileRegionIdColumn)
			throws IOException {
		ShapefileReader reader = new ShapefileReader(new File(fileName));
		List<DbfFieldDef> fields = reader.getTableHeader()
				.getFieldDefinitions();
		Map<String, ShpPolygon> shapes = new LinkedHashMap<String, ShpPolygon>();
		int idColumnIndex = -1;

		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).getFieldName().trim().equals(fileRegionIdColumn)) {
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

	private Map<String, String> createIdToRegionMap(DataTable table,
			String idColumn, String regionColumn) {
		Map<String, String> idToRegionMap = new LinkedHashMap<String, String>();
		int idIndex = table.getDataTableSpec().findColumnIndex(idColumn);
		int regionIndex = table.getDataTableSpec()
				.findColumnIndex(regionColumn);

		RowIterator it = table.iterator();

		while (it.hasNext()) {
			DataRow row = it.next();

			if (!row.getCell(regionIndex).isMissing()) {
				String id = row.getCell(idIndex).toString().trim();
				String region = row.getCell(regionIndex).toString().trim();

				idToRegionMap.put(id, region);
			}
		}

		return idToRegionMap;
	}

	private Map<String, Double> createRegionDataMap(DataTable table,
			String idColumn, String valueColumn,
			Map<String, String> idToRegionMap) {
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
				String region = idToRegionMap.get(id);

				if (region != null) {
					if (dataMap.containsKey(region)) {
						dataMap.put(region, dataMap.get(region) + value);
					} else {
						dataMap.put(region, value);
					}
				}
			} catch (Exception e) {
			}
		}

		return dataMap;
	}

	private Map<GISCanvas.Edge, Double> createEdgeDataMap(DataTable table,
			String fromColumn, String toColumn, String valueColumn,
			Map<String, String> idToRegionMap) {
		Map<GISCanvas.Edge, Double> dataMap = new LinkedHashMap<GISCanvas.Edge, Double>();
		int fromIndex = table.getDataTableSpec().findColumnIndex(fromColumn);
		int toIndex = table.getDataTableSpec().findColumnIndex(toColumn);
		int valueIndex = table.getDataTableSpec().findColumnIndex(valueColumn);
		RowIterator it = table.iterator();

		while (it.hasNext()) {
			DataRow row = it.next();

			try {
				String from = row.getCell(fromIndex).toString().trim();
				String to = row.getCell(toIndex).toString().trim();
				double value = Double.parseDouble(row.getCell(valueIndex)
						.toString().trim());
				String fromRegion = idToRegionMap.get(from);
				String toRegion = idToRegionMap.get(to);

				if (fromRegion != null && toRegion != null) {
					GISCanvas.Edge edge = new GISCanvas.Edge(fromRegion,
							toRegion);

					if (dataMap.containsKey(edge)) {
						dataMap.put(edge, dataMap.get(edge) + value);
					} else {
						dataMap.put(edge, value);
					}
				}
			} catch (Exception e) {
			}
		}

		return dataMap;
	}

}
