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
package de.bund.bfr.knime.pcml.node.pcmltotable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;

import de.bund.bfr.knime.pcml.port.PCMLUtil;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.util.Agent;
import de.bund.bfr.knime.util.Matrix;
import de.bund.bfr.knime.util.NameAndDbId;
import de.bund.bfr.pcml10.ColumnDocument.Column;
import de.bund.bfr.pcml10.ColumnListDocument.ColumnList;
import de.bund.bfr.pcml10.NameAndDatabaseId;
import de.bund.bfr.pcml10.PCMLDocument;
import de.bund.bfr.pcml10.ProcessDataDocument.ProcessData;
import de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode;
import de.bund.bfr.pcml10.RowDocument.Row;

public class PCMLDataTable {
	private static int NUM_STATIC_COLUMNS = 3;
	
	@Getter private DataTableSpec spec;
	@Getter private BufferedDataTable table;
	private PCMLDocument pcmlDoc;
	
	/**
	 * Initialize wit the given docuement. 
	 * @param pcmlDoc the PCML document.
	 */
	public PCMLDataTable(final PCMLDocument pcmlDoc) {
		this.pcmlDoc = pcmlDoc;
		
	}
	
	public BufferedDataTable execute(final ExecutionContext exec) {
		Map<NameAndDbId, Integer> columns = createColumnMap(pcmlDoc);
		Map<String, ProcessNode> processNodes = createProcessNodeMap(pcmlDoc);
		
		spec = createOutSpec(columns);
    	BufferedDataContainer cont = exec.createDataContainer(spec);
    	int counter = 0;
    	for (ProcessData data : pcmlDoc.getPCML().getProcessChainData().getProcessDataArray()) {
    		ProcessNode processNode = processNodes.get(data.getRef());
    		double time = data.getTime();
    		Map<String, NameAndDbId> columnList = createColumnList(
    				data.getDataTable().getColumnList());
    		for (Row row : data.getDataTable().getInlineTable().getRowArray()) {
    			// increment time by the time step of the process node
    			time = time + processNode.getParameters().getStepWidth();
    			// create a cursor    			
    			XmlCursor cursor = row.newCursor();
    			cursor.toFirstChild();
    			Map<NameAndDbId, String> rowData = new HashMap<NameAndDbId, String>();
    			for (int i = 0; i < columnList.size(); i++) {
    				NameAndDbId column = columnList.get(cursor.getName().getLocalPart());
    				rowData.put(column, cursor.getTextValue());
    				cursor.toNextSibling();
    			}
    			DataCell[] cells = createDataCells(time, rowData, processNode, columns);
    			DataRow dataRow = new DefaultRow(RowKey.createRowKey(counter), cells);
    	    	cont.addRowToTable(dataRow);
    			counter ++;
    		}
    			
    	}
    	
    	cont.close();
    	table = cont.getTable();
    	return table;
	}

	/** Create the cells of a single row of the output table. */
	private DataCell[] createDataCells(final double time,
			final Map<NameAndDbId, String> rowData, 
			final ProcessNode processNode,
			final Map<NameAndDbId, Integer> columns) {
		DataCell[] cells = new DataCell[NUM_STATIC_COLUMNS + columns.size()];
		// fill with missing cells
		Arrays.fill(cells, DataType.getMissingCell());
		// set static cells
		cells[0] = new DoubleCell(time);
		cells[1] = new StringCell(processNode.getProcess().getName());
		cells[2] = new StringCell(processNode.getId());
		// set row contents
		for (NameAndDbId name : rowData.keySet()) {
			int index = columns.get(name) + NUM_STATIC_COLUMNS;
			if (rowData.get(name).isEmpty()) {
				cells[index] = DataType.getMissingCell();
			}
			else {
				Double value = Double.valueOf(rowData.get(name));
				cells[index] = new DoubleCell(value);				
			}
		}
		return cells;
	}

	/** Creates a mapping of the element name of a column to the NameAndDbId
	 * object of the column.
	 */
	private Map<String, NameAndDbId> createColumnList(final ColumnList columnList) {
		Map<String, NameAndDbId> map = new HashMap<String, NameAndDbId>();
		for (Column col : columnList.getColumnArray()) {
			map.put(col.getName(), getPCMLName(col));
		}
		return map;
	}


	/** Creates the output spec. */
	private DataTableSpec createOutSpec(final Map<NameAndDbId, Integer> columns) {
		LinkedHashMap<String, DataColumnSpec> colSpecs = new LinkedHashMap<String, DataColumnSpec>();
		// the time
		colSpecs.put(AttributeUtilities.TIME, new DataColumnSpecCreator(AttributeUtilities.TIME, DoubleCell.TYPE).createSpec());
		// the process name
		colSpecs.put("process", new DataColumnSpecCreator("process", StringCell.TYPE).createSpec());
		// the process id
		colSpecs.put("process id", new DataColumnSpecCreator("process id", StringCell.TYPE).createSpec());
		// the columns
		for (NameAndDbId col : columns.keySet()) {
			if (colSpecs.containsKey(col.getName())) {
				colSpecs.put(col.getName() + "_" + col.getId(),
						new DataColumnSpecCreator(col.getName() + "_" + col.getId(), DoubleCell.TYPE).createSpec());				
			}
			else {
				colSpecs.put(col.getName(), new DataColumnSpecCreator(col.getName(), DoubleCell.TYPE).createSpec());				
			}
		}
    	DataTableSpec outSpec = new DataTableSpec(colSpecs.values().toArray(
    			new DataColumnSpec[colSpecs.size()]));
    	return outSpec;
	}

	/** Create a list of all process nodes for fast access. This is map of the
	 * process nodes id to the object itself.
	 */
	private Map<String, ProcessNode> createProcessNodeMap(final PCMLDocument pcmlDoc) {
		Map<String, ProcessNode> processNodes = new HashMap<String, ProcessNode>();
		
		for (ProcessNode processNode : pcmlDoc.getPCML().getProcessChain().getProcessNodeArray()) {
			processNodes.put(processNode.getId(), processNode);
		}
		
		return processNodes;
	}

	/** Create a column mapping for the outport. The map contains the 
	 * NameAndDbId object mapped to the offset of the outport index. The number
	 * of static outport columns like "time" and "process-node" plus the offset
	 * is the index of the outport column representing this NameAndDbId object.
	 */
	private Map<NameAndDbId, Integer> createColumnMap(final PCMLDocument pcmlDoc) {
		Map<NameAndDbId, Integer> columns = 
			new LinkedHashMap<NameAndDbId, Integer>();
		
		XmlObject[] xmlObjects = pcmlDoc.selectPath(
			      "declare namespace s='" + PCMLUtil.getPCMLNamespace(pcmlDoc) + "' " +
			      ".//s:ColumnList");
		for (XmlObject xmlObject : xmlObjects) {
			ColumnList columnList = (ColumnList)xmlObject;
			for (Column c : columnList.getColumnArray()) {
				NameAndDbId pcmlName = getPCMLName(c);
				if (!columns.containsKey(pcmlName)) {
					columns.put(pcmlName, columns.size());
				}
			}
		}
		
		return columns;
	}

	/** Create a NameAndDbId object for the given column. */
	private NameAndDbId getPCMLName(final Column c) {
		if (c.getMatrix() != null) {
			NameAndDatabaseId name = c.getMatrix();
			return new Matrix(name.getName(), name.getDbId());
		} else if (c.getAgent() != null) {
			NameAndDatabaseId name = c.getAgent();
			return new Agent(name.getName(), name.getDbId());
		} else {
			NameAndDatabaseId name = c.getColumnId();
			return new NameAndDbId(name.getName(), name.getDbId());
		}
	}
	
	
}
