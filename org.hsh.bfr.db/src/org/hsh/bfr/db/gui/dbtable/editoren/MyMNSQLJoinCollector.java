package org.hsh.bfr.db.gui.dbtable.editoren;

import java.util.HashSet;

import org.hsh.bfr.db.MyTable;

public class MyMNSQLJoinCollector {

	private String toSelect = "";
	private String toJoin = "";
	private HashSet<MyTable> alreadyJoined = new HashSet<MyTable>(); 

	public MyMNSQLJoinCollector(String toSelect, String toJoin) {
		this.toSelect = toSelect;
		this.toJoin = toJoin;
	}
	
	public void addToSelect(String addSelect) {
		toSelect += addSelect;
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
}
