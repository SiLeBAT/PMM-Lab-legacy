package org.hsh.bfr.db.gui.checktreetable;


import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import javax.swing.*;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class MyTreeTable extends JDialog {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -3559058496638244231L;

	private CheckTreeTableManager manager;
	private MyTreeTableModel theModel;
	private String columnNames[];
	private LinkedHashMap<String[], LinkedHashSet<String[]>> tableValues;
	
	public static void main(String[] args) {
        new MyTreeTable(null, null);
    }

	public MyTreeTable(String[] columnNames, LinkedHashMap<String[], LinkedHashSet<String[]>> tableValues) {
		this.columnNames = columnNames;
		this.tableValues = tableValues;
		
		DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
		if (tableValues == null) {
	        root.add(new DefaultMutableTreeTableNode());
		}
		else {
			for (String[] p : tableValues.keySet()) {
				MyTreeTableNode nextNode = new MyTreeTableNode(p);
				LinkedHashSet<String[]> lhs = tableValues.get(p);
				root.add(nextNode);
				for (String[] sa : lhs) {
					nextNode.add(new MyTreeTableNode(sa));
				}
			}
		}

		theModel = new MyTreeTableModel(root);
    	final JXTreeTable treeTable = new MymTreeTable(theModel);

    	manager = new CheckTreeTableManager(treeTable);
    	
        treeTable.setFillsViewportHeight(false);

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(treeTable), BorderLayout.CENTER);

        //treeTable.setRootVisible(true);
        //treeTable.expandAll();

        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setSize(800, 500);
        this.setModal(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
	
	public TreePath[] getCheckedPaths() {
		TreePath checkedPaths[] = manager.getSelectionModel().getSelectionPaths();
		return checkedPaths;
	}
	public MyTreeTableModel getTheModel() {
		return theModel;
	}

    private class MymTreeTable extends JXTreeTable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 572361892777356523L;

		private MymTreeTable(TreeTableModel treeModel) {
            super(treeModel);
        }
    }

    public class MyTreeTableModel extends DefaultTreeTableModel {
        private MyTreeTableModel(TreeTableNode root) {
            super(root);
        }

        public Object getValueAt(Object node, int column) {
        	if (tableValues == null) return "Value" + column;
        	else if (node instanceof MyTreeTableNode) {
        		MyTreeTableNode mttn = (MyTreeTableNode) node;
        		return mttn.getValueAt(column);
        	}
        	else return "";
        }

        public String getColumnName(int column) {
        	if (columnNames == null) return "Column" + column;
        	else return columnNames[column];
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }
    }
}