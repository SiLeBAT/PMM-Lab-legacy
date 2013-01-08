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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JSplitPane;

import org.eclipse.stem.gis.ShapefileReader;
import org.eclipse.stem.gis.dbf.DbfFieldDef;
import org.eclipse.stem.gis.shp.ShpPolygon;
import org.eclipse.stem.gis.shp.ShpRecord;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
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
		NodeView<RegionToRegionVisualizerNodeModel> implements
		GraphCanvas.NodeSelectionListener {

	private GraphCanvas graphCanvas;
	private GISCanvas gisCanvas;

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
			Map<String, String> idToRegionMap = getIdToRegionMap();
			Set<String> connectedNodes = getIdsOfConnectedNodes();

			graphCanvas = createGraphCanvas(connectedNodes);
			graphCanvas.addNodeSelectionListener(this);
			gisCanvas = createGISCanvas();
			gisCanvas.setRegionData(createRegionDataMap(idToRegionMap,
					connectedNodes));
			gisCanvas.setEdgeData(createEdgeDataMap(idToRegionMap));

			JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					graphCanvas, gisCanvas);

			panel.setResizeWeight(0.5);
			setComponent(panel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(Set<GraphCanvas.Node> selectedNodes) {
		List<String> regions = new ArrayList<String>();

		for (GraphCanvas.Node node : selectedNodes) {
			regions.add(node.getRegion());
		}

		gisCanvas.setHighlightedRegions(regions);
		gisCanvas.repaint();
	}

	private Map<String, String> getIdToRegionMap() {
		Map<String, String> idToRegionMap = new LinkedHashMap<String, String>();
		int idIndex = getNodeModel().getNodeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getNodeIdColumn());
		int regionIndex = getNodeModel().getNodeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getNodeRegionIdColumn());

		RowIterator it = getNodeModel().getNodeTable().iterator();

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

	private Set<String> getIdsOfConnectedNodes() {
		Set<String> connectedNodes = new LinkedHashSet<>();
		int edgeFromIndex = getNodeModel().getEdgeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getEdgeFromColumn());
		int edgeToIndex = getNodeModel().getEdgeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getEdgeToColumn());
		RowIterator edgeIt = getNodeModel().getEdgeTable().iterator();

		while (edgeIt.hasNext()) {
			try {
				DataRow row = edgeIt.next();
				String from = row.getCell(edgeFromIndex).toString().trim();
				String to = row.getCell(edgeToIndex).toString().trim();

				connectedNodes.add(from);
				connectedNodes.add(to);
			} catch (Exception e) {
			}
		}

		return connectedNodes;
	}

	private GraphCanvas createGraphCanvas(Set<String> connectedNodes) {
		int nodeIdIndex = getNodeModel().getNodeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getNodeIdColumn());
		int nodeRegionIndex = getNodeModel().getNodeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getNodeRegionIdColumn());
		int edgeFromIndex = getNodeModel().getEdgeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getEdgeFromColumn());
		int edgeToIndex = getNodeModel().getEdgeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getEdgeToColumn());
		Map<String, Class<?>> nodeProperties = new LinkedHashMap<>();

		for (int i = 0; i < getNodeModel().getNodeTable().getDataTableSpec()
				.getNumColumns(); i++) {
			DataColumnSpec columnSpec = getNodeModel().getNodeTable()
					.getDataTableSpec().getColumnSpec(i);

			if (columnSpec.getType() == StringCell.TYPE) {
				nodeProperties.put(columnSpec.getName(), String.class);
			} else if (columnSpec.getType() == IntCell.TYPE) {
				nodeProperties.put(columnSpec.getName(), Integer.class);
			} else if (columnSpec.getType() == DoubleCell.TYPE) {
				nodeProperties.put(columnSpec.getName(), Double.class);
			}
		}

		Map<String, Class<?>> edgeProperties = new LinkedHashMap<>();

		for (int i = 0; i < getNodeModel().getEdgeTable().getDataTableSpec()
				.getNumColumns(); i++) {
			DataColumnSpec columnSpec = getNodeModel().getEdgeTable()
					.getDataTableSpec().getColumnSpec(i);

			if (columnSpec.getType() == StringCell.TYPE) {
				edgeProperties.put(columnSpec.getName(), String.class);
			} else if (columnSpec.getType() == IntCell.TYPE) {
				edgeProperties.put(columnSpec.getName(), Integer.class);
			} else if (columnSpec.getType() == DoubleCell.TYPE) {
				edgeProperties.put(columnSpec.getName(), Double.class);
			}
		}

		Map<String, GraphCanvas.Node> nodes = new LinkedHashMap<>();
		RowIterator nodeIt = getNodeModel().getNodeTable().iterator();

		while (nodeIt.hasNext()) {
			try {
				DataRow row = nodeIt.next();
				String id = row.getCell(nodeIdIndex).toString().trim();

				if (getNodeModel().isSkipEdgelessNodes()
						&& !connectedNodes.contains(id)) {
					continue;
				}

				String region = row.getCell(nodeRegionIndex).toString().trim();
				Map<String, Object> properties = new LinkedHashMap<>();

				for (int i = 0; i < getNodeModel().getNodeTable()
						.getDataTableSpec().getNumColumns(); i++) {
					DataColumnSpec columnSpec = getNodeModel().getNodeTable()
							.getDataTableSpec().getColumnSpec(i);
					DataCell cell = row.getCell(i);

					if (cell.isMissing()) {
						properties.put(columnSpec.getName(), null);
					} else if (columnSpec.getType() == StringCell.TYPE) {
						properties.put(columnSpec.getName(),
								((StringCell) cell).getStringValue());
					} else if (columnSpec.getType() == IntCell.TYPE) {
						properties.put(columnSpec.getName(),
								((IntCell) cell).getIntValue());
					} else if (columnSpec.getType() == DoubleCell.TYPE) {
						properties.put(columnSpec.getName(),
								((DoubleCell) cell).getDoubleValue());
					}
				}

				nodes.put(id, new GraphCanvas.Node(region, properties));
			} catch (Exception e) {
			}
		}

		List<GraphCanvas.Edge> edges = new ArrayList<GraphCanvas.Edge>();
		RowIterator edgeIt = getNodeModel().getEdgeTable().iterator();

		while (edgeIt.hasNext()) {
			try {
				DataRow row = edgeIt.next();
				String from = row.getCell(edgeFromIndex).toString().trim();
				String to = row.getCell(edgeToIndex).toString().trim();
				GraphCanvas.Node node1 = nodes.get(from);
				GraphCanvas.Node node2 = nodes.get(to);
				Map<String, Object> properties = new LinkedHashMap<>();

				for (int i = 0; i < getNodeModel().getEdgeTable()
						.getDataTableSpec().getNumColumns(); i++) {
					DataColumnSpec columnSpec = getNodeModel().getEdgeTable()
							.getDataTableSpec().getColumnSpec(i);
					DataCell cell = row.getCell(i);

					if (cell.isMissing()) {
						properties.put(columnSpec.getName(), null);
					} else if (columnSpec.getType() == StringCell.TYPE) {
						properties.put(columnSpec.getName(),
								((StringCell) cell).getStringValue());
					} else if (columnSpec.getType() == IntCell.TYPE) {
						properties.put(columnSpec.getName(),
								((IntCell) cell).getIntValue());
					} else if (columnSpec.getType() == DoubleCell.TYPE) {
						properties.put(columnSpec.getName(),
								((DoubleCell) cell).getDoubleValue());
					}
				}

				if (node1 != null && node2 != null) {
					edges.add(new GraphCanvas.Edge(node1, node2, properties));
				}
			} catch (Exception e) {
			}
		}

		return new GraphCanvas(new ArrayList<GraphCanvas.Node>(nodes.values()),
				edges, nodeProperties, edgeProperties);
	}

	private GISCanvas createGISCanvas() throws IOException {
		File file = new File(getNodeModel().getFileName());

		if (!file.exists()) {
			try {
				file = new File(new URI(getNodeModel().getFileName()).getPath());
			} catch (URISyntaxException e1) {
			}
		}

		ShapefileReader reader = new ShapefileReader(file);
		List<DbfFieldDef> fields = reader.getTableHeader()
				.getFieldDefinitions();
		Map<String, ShpPolygon> shapes = new LinkedHashMap<String, ShpPolygon>();
		int idColumnIndex = -1;

		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).getFieldName().trim()
					.equals(getNodeModel().getFileRegionIdColumn())) {
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

	private Map<String, Map<String, Double>> createRegionDataMap(
			Map<String, String> idToRegionMap, Set<String> connectedNodes) {
		Map<String, Map<String, Double>> dataMap = new LinkedHashMap<>();
		DataTable nodeTable = getNodeModel().getNodeTable();
		DataTableSpec nodeTableSpec = nodeTable.getDataTableSpec();
		int idIndex = nodeTable.getDataTableSpec().findColumnIndex(
				getNodeModel().getNodeIdColumn());

		for (int i = 0; i < nodeTableSpec.getNumColumns(); i++) {
			if (nodeTableSpec.getColumnSpec(i).getType() == DoubleCell.TYPE
					|| nodeTableSpec.getColumnSpec(i).getType() == IntCell.TYPE) {
				dataMap.put(nodeTableSpec.getColumnSpec(i).getName(),
						new LinkedHashMap<String, Double>());
			}
		}

		RowIterator it = nodeTable.iterator();

		while (it.hasNext()) {
			DataRow row = it.next();

			String id = row.getCell(idIndex).toString().trim();

			if (getNodeModel().isSkipEdgelessNodes()
					&& !connectedNodes.contains(id)) {
				continue;
			}

			String region = idToRegionMap.get(id);

			if (region != null) {
				for (String property : dataMap.keySet()) {
					try {
						int column = nodeTableSpec.findColumnIndex(property);
						double value = Double.parseDouble(row.getCell(column)
								.toString().trim());

						if (dataMap.get(property).containsKey(region)) {
							dataMap.get(property).put(region,
									dataMap.get(property).get(region) + value);
						} else {
							dataMap.get(property).put(region, value);
						}
					} catch (Exception e) {
					}
				}
			}
		}

		return dataMap;
	}

	private Map<GISCanvas.Edge, Double> createEdgeDataMap(
			Map<String, String> idToRegionMap) {
		Map<GISCanvas.Edge, Double> dataMap = new LinkedHashMap<GISCanvas.Edge, Double>();
		int fromIndex = getNodeModel().getEdgeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getEdgeFromColumn());
		int toIndex = getNodeModel().getEdgeTable().getDataTableSpec()
				.findColumnIndex(getNodeModel().getEdgeToColumn());
		RowIterator it = getNodeModel().getEdgeTable().iterator();

		while (it.hasNext()) {
			DataRow row = it.next();

			try {
				String from = row.getCell(fromIndex).toString().trim();
				String to = row.getCell(toIndex).toString().trim();
				String fromRegion = idToRegionMap.get(from);
				String toRegion = idToRegionMap.get(to);

				if (fromRegion != null && toRegion != null) {
					GISCanvas.Edge edge = new GISCanvas.Edge(fromRegion,
							toRegion);

					if (dataMap.containsKey(edge)) {
						dataMap.put(edge, null);
					} else {
						dataMap.put(edge, null);
					}
				}
			} catch (Exception e) {
			}
		}

		return dataMap;
	}

}
