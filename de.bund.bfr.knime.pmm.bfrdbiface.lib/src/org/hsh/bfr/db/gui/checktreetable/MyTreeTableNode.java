package org.hsh.bfr.db.gui.checktreetable;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class MyTreeTableNode extends DefaultMutableTreeTableNode {

	private String[] row;
	
	public MyTreeTableNode(String[] row) {
		this.row = row;
	}
	
	public Object getValueAt(int column) {
		return row[column];
	}
}
