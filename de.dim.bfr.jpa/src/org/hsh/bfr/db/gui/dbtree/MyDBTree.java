/**
 * 
 */
package org.hsh.bfr.db.gui.dbtree;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.MyTable;
import org.hsh.bfr.db.gui.dbtable.MyDBPanel;

/**
 * @author Armin
 *
 */
public class MyDBTree extends JTree implements TreeSelectionListener, KeyListener {

  private MyTable myT = null;
	private boolean catchEvent = true;
	private MyDBPanel myDBPanel1 = null;
	private MyDBTreeModel myModel = null; 

  public MyDBTree() {
    this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);   
    //ImageIcon customLeafIcon = new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Table.gif"));
    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    //renderer.setOpenIcon(customOpenIcon);
    //renderer.setClosedIcon(customClosedIcon);
    //renderer.setLeafIcon(customLeafIcon);
    this.setCellRenderer(renderer);
    this.setToggleClickCount(2);
		//this.setEditable(true);
    
    this.addTreeSelectionListener(this);
    this.addKeyListener(this);
    //this.setRootVisible(false);
	}
	
	public MyTable getActualTable() {
		return myT;
	}
	public void setTable(MyTable myT) {
		setTable(myT, null);
	}
	public void setTable(MyTable myT, String[] showOnly) {
		this.myT = myT;
		if (myT != null) {
			myModel = new MyDBTreeModel(myT, showOnly);
			this.setModel(myModel);
			this.setRootVisible(true);
		}
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent event) {
		if (catchEvent) { // !event.getValueIsAdjusting()    				
			DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
			/*
    		boolean isLeaf = selectedTreeNode.isLeaf();
			if (selectedTreeNode.getUserObject() instanceof MyDBTreeNode) {
				MyDBTreeNode mydbt = (MyDBTreeNode) selectedTreeNode.getUserObject();
				isLeaf = mydbt.isLeaf();
			}
			*/
			if (selectedTreeNode.getLevel() < 2 || !selectedTreeNode.isLeaf() && myT != null && myT.getTablename().equals("Methoden")) { // selectedTreeNode.getLevel() < 3    !selectedTreeNode.isLeaf() && 
				catchEvent = false;
				this.setSelectionPath(event.getOldLeadSelectionPath());
			}
			else {
				int id = getSelectedID();
				if (id > 0 && myDBPanel1 != null) myDBPanel1.setSelectedID(id);				
			}
		}
		catchEvent = true;
	}
	public void checkFilter(String filter) {
		if (myModel != null) {
			myModel.checkFilter(filter);
			this.setModel(null);
			if (DBKernel.debug) MyLogger.handleMessage("checkFilter - Fin!");
			this.setModel(myModel);
			this.setSelectedID(myDBPanel1.getSelectedID());
		}
	}
	public void setSelectedID(int id) {
		if (myModel != null) {
			try {
				int codeSystemNum = -1;
				if (this.getSelectionCount() > 0) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
				    if (node.getUserObject() instanceof MyDBTreeNode) {
				    	MyDBTreeNode myTN = (MyDBTreeNode) node.getUserObject();
				    	if (myTN.getID() == id) return;
				    	codeSystemNum = myTN.getCodeSystemNum();
				    }
				}
				DefaultMutableTreeNode dmtn = myModel.getTreeNode(id, codeSystemNum);
				if (dmtn != null) {
					TreePath tp = new TreePath(dmtn.getPath());
					if (tp != null && (this.getSelectionCount() == 0 || !this.getSelectionPath().equals(tp))) {
						this.setSelectionPath(tp);
						try {
							this.scrollPathToVisible(tp);
							myDBPanel1.getTreeScroller().getHorizontalScrollBar().setValue(0);
						}
						catch (Exception e1) {}
					}
				}
				else {
					this.setSelectionInterval(-1, -1);
				}
			}
			catch (Exception e) {
				MyLogger.handleException(e);
			}			
		}
	}
	private int getSelectedID() {
		int result = -1;
		if (this.getSelectionCount() > 0) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
	    if (node.getUserObject() instanceof MyDBTreeNode) {
	    	MyDBTreeNode myTN = (MyDBTreeNode) node.getUserObject();
	    	result = myTN.getID();
	    }
		}
		return result;
	}
	public void setMyDBPanel(MyDBPanel myDBPanel) {
		myDBPanel1 = myDBPanel;
	}

  public void keyPressed(KeyEvent keyEvent) {
    if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
    	keyEvent.consume();
    	DBKernel.myList.requestFocus();
    	return;
    }
  }
	public void keyReleased(KeyEvent arg0) {
	}
	public void keyTyped(KeyEvent arg0) {
	}
}
