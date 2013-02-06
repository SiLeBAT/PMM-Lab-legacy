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
import org.knime.core.data.def.BooleanCell;
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
		GraphCanvas.GraphSelectionListener, GISCanvas.GISSelectionListener {

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
			graphCanvas.addSelectionListener(this);
			gisCanvas = createGISCanvas(idToRegionMap, connectedNodes);
			gisCanvas.addSelectionListener(this);

			JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					graphCanvas, gisCanvas);

			panel.setResizeWeight(0.5);
			setComponent(panel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void graphNodeSelectionChanged(Set<GraphCanvas.Node> selectedNodes) {
		Set<GISCanvas.Node> selectedGisNodes = new LinkedHashSet<>();
		Map<String, GISCanvas.Node> gisNodesByRegion = new LinkedHashMap<>();

		for (GISCanvas.Node gisNode : gisCanvas.getNodes()) {
			gisNodesByRegion.put(gisNode.getId(), gisNode);
		}

		for (GraphCanvas.Node graphNode : selectedNodes) {
			GISCanvas.Node gisNode = gisNodesByRegion
					.get(graphNode.getRegion());

			if (gisNode != null) {
				selectedGisNodes.add(gisNode);
			}
		}

		gisCanvas.removeSelectionListener(this);
		gisCanvas.setSelectedNodes(selectedGisNodes);
		gisCanvas.addSelectionListener(this);
		gisCanvas.repaint();
	}

	@Override
	public void graphEdgeSelectionChanged(Set<GraphCanvas.Edge> selectedEdges) {
		Set<GISCanvas.Edge> selectedGisEdges = new LinkedHashSet<>();
		Map<String, Map<String, GISCanvas.Edge>> gisEdgesByRegion = new LinkedHashMap<>();

		for (GISCanvas.Edge gisEdge : gisCanvas.getEdges()) {
			String fromRegion = gisEdge.getFrom().getId();
			String toRegion = gisEdge.getTo().getId();

			if (!gisEdgesByRegion.containsKey(fromRegion)) {
				gisEdgesByRegion.put(fromRegion,
						new LinkedHashMap<String, GISCanvas.Edge>());
			}

			gisEdgesByRegion.get(fromRegion).put(toRegion, gisEdge);
		}

		for (GraphCanvas.Edge graphEdge : selectedEdges) {
			String fromRegion = graphEdge.getFrom().getRegion();
			String toRegion = graphEdge.getTo().getRegion();

			if (gisEdgesByRegion.containsKey(fromRegion)) {
				GISCanvas.Edge gisEdge = gisEdgesByRegion.get(fromRegion).get(
						toRegion);

				if (gisEdge != null) {
					selectedGisEdges.add(gisEdge);
				}
			}
		}

		gisCanvas.removeSelectionListener(this);
		gisCanvas.setSelectedEdges(selectedGisEdges);
		gisCanvas.addSelectionListener(this);
		gisCanvas.repaint();
	}

	@Override
	public void gisNodeSelectionChanged(Set<GISCanvas.Node> selectedNodes) {
		Set<GraphCanvas.Node> selectedGraphNodes = new LinkedHashSet<>();
		Map<String, List<GraphCanvas.Node>> graphNodesByRegion = new LinkedHashMap<>();

		for (GraphCanvas.Node graphNode : graphCanvas.getNodes()) {
			if (!graphNodesByRegion.containsKey(graphNode.getRegion())) {
				graphNodesByRegion.put(graphNode.getRegion(),
						new ArrayList<GraphCanvas.Node>());
			}

			graphNodesByRegion.get(graphNode.getRegion()).add(graphNode);
		}

		for (GISCanvas.Node gisNode : selectedNodes) {
			List<GraphCanvas.Node> graphNodes = graphNodesByRegion.get(gisNode
					.getId());

			if (graphNodes != null) {
				selectedGraphNodes.addAll(graphNodes);
			}
		}

		graphCanvas.removeSelectionListener(this);
		graphCanvas.setSelectedNodes(selectedGraphNodes);
		graphCanvas.addSelectionListener(this);
	}

	@Override
	public void gisEdgeSelectionChanged(Set<GISCanvas.Edge> selectedEdges) {
		Set<GraphCanvas.Edge> selectedGraphEdges = new LinkedHashSet<>();
		Map<String, Map<String, List<GraphCanvas.Edge>>> graphEdgesByRegion = new LinkedHashMap<>();

		for (GraphCanvas.Edge graphEdge : graphCanvas.getEdges()) {
			String fromRegion = graphEdge.getFrom().getRegion();
			String toRegion = graphEdge.getTo().getRegion();

			if (!graphEdgesByRegion.containsKey(fromRegion)) {
				graphEdgesByRegion.put(fromRegion,
						new LinkedHashMap<String, List<GraphCanvas.Edge>>());
			}

			if (!graphEdgesByRegion.get(fromRegion).containsKey(toRegion)) {
				graphEdgesByRegion.get(fromRegion).put(toRegion,
						new ArrayList<GraphCanvas.Edge>());
			}

			graphEdgesByRegion.get(fromRegion).get(toRegion).add(graphEdge);
		}

		for (GISCanvas.Edge gisEdge : selectedEdges) {
			String fromRegion = gisEdge.getFrom().getId();
			String toRegion = gisEdge.getTo().getId();

			if (graphEdgesByRegion.containsKey(fromRegion)) {
				List<GraphCanvas.Edge> graphEdges = graphEdgesByRegion.get(
						fromRegion).get(toRegion);

				if (graphEdges != null) {
					selectedGraphEdges.addAll(graphEdges);
				}
			}
		}

		graphCanvas.removeSelectionListener(this);
		graphCanvas.setSelectedEdges(selectedGraphEdges);
		graphCanvas.addSelectionListener(this);
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
			} else if (columnSpec.getType() == BooleanCell.TYPE) {
				nodeProperties.put(columnSpec.getName(), Boolean.class);
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
					} else if (columnSpec.getType() == BooleanCell.TYPE) {
						properties.put(columnSpec.getName(),
								((BooleanCell) cell).getBooleanValue());
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

	private GISCanvas createGISCanvas(Map<String, String> idToRegionMap,
			Set<String> connectedNodes) throws IOException {
		File file = new File(getNodeModel().getFileName());

		if (!file.exists()) {
			try {
				file = new File(new URI(getNodeModel().getFileName()).getPath());
			} catch (URISyntaxException e1) {
			}
		}

		/* ------------------------------------------------------------------ */

		Map<String, ShpPolygon> polygonMap = new LinkedHashMap<>();
		ShapefileReader reader = new ShapefileReader(file);
		List<DbfFieldDef> fields = reader.getTableHeader()
				.getFieldDefinitions();
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

				polygonMap.put(id, (ShpPolygon) shp);
			}
		}

		/* ------------------------------------------------------------------ */

		Map<String, Map<String, Double>> nodeMap = new LinkedHashMap<>();
		List<String> nodeProperties = new ArrayList<>();
		DataTable nodeTable = getNodeModel().getNodeTable();
		DataTableSpec nodeTableSpec = nodeTable.getDataTableSpec();
		int nodeIdIndex = nodeTableSpec.findColumnIndex(getNodeModel()
				.getNodeIdColumn());
		RowIterator nodeIt = nodeTable.iterator();

		for (int i = 0; i < nodeTableSpec.getNumColumns(); i++) {
			if (nodeTableSpec.getColumnSpec(i).getType() == DoubleCell.TYPE
					|| nodeTableSpec.getColumnSpec(i).getType() == IntCell.TYPE) {
				nodeProperties.add(nodeTableSpec.getColumnSpec(i).getName());
			}
		}

		while (nodeIt.hasNext()) {
			DataRow row = nodeIt.next();

			String id = row.getCell(nodeIdIndex).toString().trim();

			if (getNodeModel().isSkipEdgelessNodes()
					&& !connectedNodes.contains(id)) {
				continue;
			}

			String region = idToRegionMap.get(id);

			if (region != null) {
				if (!nodeMap.containsKey(region)) {
					nodeMap.put(region, new LinkedHashMap<String, Double>());

					for (String property : nodeProperties) {
						nodeMap.get(region).put(property, 0.0);
					}
				}

				for (String property : nodeProperties) {
					try {
						int column = nodeTableSpec.findColumnIndex(property);
						double value = Double.parseDouble(row.getCell(column)
								.toString().trim());

						nodeMap.get(region).put(property,
								nodeMap.get(region).get(property) + value);
					} catch (Exception e) {
					}
				}
			}
		}

		/* ------------------------------------------------------------------ */

		Map<String, Map<String, Map<String, Double>>> edgeMap = new LinkedHashMap<>();
		List<String> edgeProperties = new ArrayList<>();
		DataTable edgeTable = getNodeModel().getEdgeTable();
		DataTableSpec edgeTableSpec = edgeTable.getDataTableSpec();
		int fromIndex = edgeTableSpec.findColumnIndex(getNodeModel()
				.getEdgeFromColumn());
		int toIndex = edgeTableSpec.findColumnIndex(getNodeModel()
				.getEdgeToColumn());
		RowIterator edgeIt = edgeTable.iterator();

		for (int i = 0; i < edgeTableSpec.getNumColumns(); i++) {
			if (edgeTableSpec.getColumnSpec(i).getType() == DoubleCell.TYPE
					|| edgeTableSpec.getColumnSpec(i).getType() == IntCell.TYPE) {
				edgeProperties.add(edgeTableSpec.getColumnSpec(i).getName());
			}
		}

		while (edgeIt.hasNext()) {
			DataRow row = edgeIt.next();

			String from = row.getCell(fromIndex).toString().trim();
			String to = row.getCell(toIndex).toString().trim();
			String fromRegion = idToRegionMap.get(from);
			String toRegion = idToRegionMap.get(to);

			if (fromRegion != null && toRegion != null) {
				if (!edgeMap.containsKey(fromRegion)) {
					edgeMap.put(fromRegion,
							new LinkedHashMap<String, Map<String, Double>>());
				}

				if (!edgeMap.get(fromRegion).containsKey(toRegion)) {
					edgeMap.get(fromRegion).put(toRegion,
							new LinkedHashMap<String, Double>());

					for (String property : edgeProperties) {
						edgeMap.get(fromRegion).get(toRegion)
								.put(property, 0.0);
					}
				}

				for (String property : edgeProperties) {
					try {
						int column = edgeTableSpec.findColumnIndex(property);
						double value = Double.parseDouble(row.getCell(column)
								.toString().trim());

						edgeMap.get(fromRegion)
								.get(toRegion)
								.put(property,
										edgeMap.get(fromRegion).get(toRegion)
												.get(property)
												+ value);
					} catch (Exception e) {
					}
				}
			}
		}

		/* ------------------------------------------------------------------ */

		Map<String, GISCanvas.Node> nodes = new LinkedHashMap<>();

		for (String id : polygonMap.keySet()) {
			Map<String, Double> properties = nodeMap.get(id);

			if (properties == null) {
				properties = new LinkedHashMap<>();

				for (String property : nodeProperties) {
					properties.put(property, 0.0);
				}
			}

			nodes.put(id,
					new GISCanvas.Node(id, properties, polygonMap.get(id)));
		}

		/* ------------------------------------------------------------------ */

		List<GISCanvas.Edge> edges = new ArrayList<>();

		for (String from : edgeMap.keySet()) {
			for (String to : edgeMap.get(from).keySet()) {
				GISCanvas.Node n1 = nodes.get(from);
				GISCanvas.Node n2 = nodes.get(to);

				if (n1 != null && n2 != null) {
					edges.add(new GISCanvas.Edge(n1, n2, edgeMap.get(from).get(
							to)));
				}
			}
		}

		return new GISCanvas(new ArrayList<GISCanvas.Node>(nodes.values()),
				edges, nodeProperties, edgeProperties);
	}

}
