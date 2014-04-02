package org.hsh.bfr.db.gui.dbtable.editoren;

import java.util.HashSet;

import org.hsh.bfr.db.MyTable;

public class MyMNSQLJoinCollector {

	private String toSelect = "";
	private String toJoin = "";
	private boolean hasUnknownFields = false;
	private int addCounter = 0;
	private HashSet<MyTable> alreadyJoined = new HashSet<MyTable>(); 

	public MyMNSQLJoinCollector(String toSelect, String toJoin) {
		this.toSelect = toSelect;
		this.toJoin = toJoin;
		addCounter = 0;
	}
	
	public void addToSelect(String addSelect) {
		toSelect += addSelect;
		addCounter++;
	}
	public void addToJoin(String addJoin) {
		toJoin += addJoin;
	}
	
	public String getToSelect() {
		return toSelect;
	}
	public String getToJoin() {
		return toJoin;
	}

	public HashSet<MyTable> getAlreadyJoined() {
		return alreadyJoined;
	}

	public boolean hasUnknownFields() {
		return hasUnknownFields;
	}
	public void setHasUnknownFields(boolean hasUnknownFields) {
		this.hasUnknownFields = hasUnknownFields;
	}

	public int getAddCounter() {
		return addCounter;
	}
}
