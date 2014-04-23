package org.hsh.bfr.db;

import java.util.LinkedHashMap;


public abstract class MyDBI {
	public abstract LinkedHashMap<String, MyTable> getAllTables();
	public abstract LinkedHashMap<Integer, String> getTreeStructure();
	public abstract LinkedHashMap<String, int[]> getKnownCodeSysteme();	
	public abstract void createRoles();
	public abstract void addData();
	public abstract void addViews();
	public abstract void recreateTriggers();
	public abstract void updateCheck(final String fromVersion, final String toVersion);
	public abstract boolean isReadOnly();
	public abstract String getDBVersion();
	public abstract LinkedHashMap<Object, String> getHashMap(final String key);

	public MyTable getTable(String tableName) {
		LinkedHashMap<String, MyTable> allTables = getAllTables();
		if (allTables.containsKey(tableName)) return allTables.get(tableName);
		else return null;
	}
	public void bootstrapDB() {
		createRoles();
		LinkedHashMap<String, MyTable> allTables = getAllTables();
		for (MyTable myT : allTables.values()) {
			myT.createTable();
		}
		addViews();
		addData();
	}
}
