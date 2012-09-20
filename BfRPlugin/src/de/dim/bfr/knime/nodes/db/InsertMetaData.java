/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.knime.nodes.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.node.NodeLogger;

public class InsertMetaData {
	
	public static class TableMetaData{
		
		private static NodeLogger logger = NodeLogger.getLogger(TableMetaData.class);
		
		private String tableName;
		private List<String> columnNames = new ArrayList<String>();
		private List<String> primaryColumns = new ArrayList<String>();
		private Map<String, ForeignKeyMetaData> foreignKeyMetaDatas = new HashMap<String, InsertMetaData.ForeignKeyMetaData>();
		private Map<String, String> oldIdNewId = new HashMap<String, String>();
		
		private List<String> oldIdsOrder = new ArrayList<String>();
		
		public TableMetaData(String tableName){
			this.tableName = tableName;
		}
		
		public String getTableName() {
			return tableName;
		}

		public List<String> getColumnNames() {
			return columnNames;
		}

		public List<String> getPrimaryColumns() {
			return primaryColumns;
		}
		
		public boolean isForeignKey(String columnName){
			return foreignKeyMetaDatas.containsKey(columnName);
		}
		
		public Collection<ForeignKeyMetaData> getForeignKeyMetaDatas() {
			return foreignKeyMetaDatas.values();
		}

		public void addForeignKeyMetaData(ForeignKeyMetaData metaData){
			foreignKeyMetaDatas.put(metaData.getColumnName(), metaData);
		}
		
		public ForeignKeyMetaData getForeignKeyMetaDataForColumn(String column) {
			return foreignKeyMetaDatas.get(column);
		}

		public void setNewId(int i, String id) {
			logger.info("setting new id [" + id + "] for [" + oldIdsOrder.get(i) + "]");
			oldIdNewId.put(oldIdsOrder.get(i), id.toString());
		}

		public boolean isPrimaryColumn(String string) {
			return primaryColumns.contains(string);
		}

		public void addOldId(String id) {
			oldIdsOrder.add(id);
		}
		
		public String getNewIdForOldId(String oldId){
			return oldIdNewId.get(oldId);
		}
	}
	
	public static class ForeignKeyMetaData{
		
		private String columnName;
		private String targetTableName;
		private String targetPrimaryKey;
		
		public ForeignKeyMetaData(String columnName, String targetTableName,
				String targetPrimaryKey) {
			super();
			this.columnName = columnName;
			this.targetTableName = targetTableName;
			this.targetPrimaryKey = targetPrimaryKey;
		}

		public String getColumnName() {
			return columnName;
		}

		public String getTargetTableName() {
			return targetTableName;
		}

		public String getTargetPrimaryKey() {
			return targetPrimaryKey;
		}
	}
	
	private Set<String> tables;
	private List<String> order = new ArrayList<String>();
	private Map<String, TableMetaData> metaData = new HashMap<String, TableMetaData>();

	public InsertMetaData(Set<String> tables) {
		this.tables = tables;
		order.addAll(tables);
	}
	
	boolean isTableKnown(String tableName){
		return tables.contains(tableName);
	}
	
	public void placeBefore(String before, String target){
		if(!order.contains(before))
			throw new IllegalArgumentException("Unknown before Table: " + before);
		if(!order.contains(target))
			throw new IllegalArgumentException("Unknown target Table: " + target);
		
		int indexBefore = order.indexOf(before);
		int indexTarget = order.indexOf(target);
		if(indexBefore > indexTarget){
			order.remove(before);
			order.add(indexTarget, before);
		}
	}

	public void addTableMetaData(TableMetaData tableMetaData) {
		metaData.put(tableMetaData.getTableName(), tableMetaData);
	}
	
	public TableMetaData getMetaDataForTable(String tableName){
		return metaData.get(tableName);
	}

	public Set<String> getTables() {
		return tables;
	}

	public List<String> getOrder() {
		return order;
	}
	
	
}
