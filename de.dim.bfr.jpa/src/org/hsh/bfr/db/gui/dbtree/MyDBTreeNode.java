/**
 * 
 */
package org.hsh.bfr.db.gui.dbtree;


/**
 * @author Armin
 *
 */
public class MyDBTreeNode {

	private int id = -1;
	private String code = "";
	private String description = "";
	private int codeSystemNum = -1;
	private boolean isLeaf = false;
	private boolean isVisible = true;
	
	public MyDBTreeNode(int id, String code, String description, boolean isLeaf, int codeSystemNum) {
		this.id = id;
		this.code = code;
		this.codeSystemNum = codeSystemNum;
		this.description = description;
		this.isLeaf = isLeaf;
	}

	public int getID() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
	public int getCodeSystemNum() {
		return codeSystemNum;
	}
	
	public String toString() {
		if (code == null || code.trim().length() == 0) return description;
		else return code + ": " + description;
	}
	
	public boolean isLeaf() {
		return isLeaf;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
}
