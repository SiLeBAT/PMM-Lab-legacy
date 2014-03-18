package org.hsh.bfr.db;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;


public abstract class MyDBI {
	public abstract LinkedHashMap<String, MyTable> getAllTables();
	public abstract void recreateTriggers();
	public abstract void updateCheck(final String fromVersion, final String toVersion);
	public abstract boolean isReadOnly();
	public abstract boolean isReadOnly(final String tableName);
	public abstract boolean hasCommentColumn(final String tableName);
	public abstract boolean hasTestedColumn(final String tableName);
	public abstract boolean hasScoreColumn(final String tableName);
	public abstract boolean hasODSN(final String tableName); // "ON DELETE SET NULL" for FOREIGN Keys (in MyTable)
	public abstract Callable<Void> getCaller4Trigger(final String tableName);
	public abstract HashMap<String, String> getProbableSAs();
	public abstract String getPath4FirstDB();
	public abstract String getDBVersion();
	public abstract LinkedHashMap<Object, String> getHashMap(final String key);

	public MyTable getTable(String tableName) {
		if (getAllTables().containsKey(tableName)) return getAllTables().get(tableName);
		else return null;
	}
}
