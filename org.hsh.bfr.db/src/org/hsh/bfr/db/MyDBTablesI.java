package org.hsh.bfr.db;

import java.util.LinkedHashMap;


public interface MyDBTablesI {
	public LinkedHashMap<String, MyTable> getAllTables();
	public MyTable getTable(final String tableName);
	public void recreateTriggers();
	public void loadMyTables();
}
