package org.hsh.bfr.db;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;


public interface MyDBI {
	public LinkedHashMap<String, MyTable> getAllTables();
	public MyTable getTable(final String tableName);
	public void recreateTriggers();
	public void loadMyTables();
	public void updateCheck(final String fromVersion, final String toVersion);
	public boolean isReadOnly(final String tableName);
	public boolean hasCommentColumn(final String tableName);
	public boolean hasTestedColumn(final String tableName);
	public boolean hasScoreColumn(final String tableName);
	public boolean hasODSN(final String tableName); // "ON DELETE SET NULL" for FOREIGN Keys (in MyTable)
	public Callable<Void> getCaller4Trigger(final String tableName);
	public HashMap<String, String> getProbableSAs();
	public String getPath4FirstDB();
}
