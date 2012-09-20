/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.editoren;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import javax.swing.JComponent;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;
import org.hsh.bfr.db.imports.InfoBox;

import quick.dbtable.CellComponent;

/**
 * @author Armin
 *
 */
public class MyCheckBoxEditor extends JCheckBox  implements ActionListener, CellComponent {
  
	private MyDBTable myDB = null;
	private boolean isGeprueftCheckBox = false;
	private boolean isNull = false;
	
	public MyCheckBoxEditor(String tooltip, MyDBTable myDB) {
		this.myDB = myDB;
		this.setToolTipText(tooltip);
		isGeprueftCheckBox = tooltip.endsWith("Richtigkeit überprüft");
		//this.addChangeListener(this);
		this.addActionListener(this);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		//filterIM();
  }

  public void setValue(Object value) {
	  isNull = false;
  	if (value == null) {isNull = true; this.setSelected(false);}
  	else if (value instanceof Boolean) {
  		this.setSelected((Boolean) value);
  	}
  	else this.setSelected(false);
  }

  public Object getValue() {
  	boolean selected = this.isSelected();
  	//System.out.println(isNull);
  	/*
  	if (isGeprueftCheckBox && selected) { // Falls selected = true. Ist das erlaubt? Es muss ein anderer User sein, der das abhakt!
    	int id = myDB.getSelectedID();  	
    	if (id > 0) {
	      	String tablename = myDB.getActualTable().getTablename();
	      	Vector<String> v = DBKernel.getUsersFromChangeLog(tablename, id);   
	      	for (int i=0;i<v.size();i++) {
	      		if (v.get(i).equals(DBKernel.getUsername())) {
	      			selected = false;
	      			break;
	      		}
	      	}
	    }
  	}
  	*/
  	if (!selected && isNull) return null;
  	else return selected;
  }

  public JComponent getComponent() {
     return this;
  }

	public void actionPerformed(ActionEvent e) {
		if (isGeprueftCheckBox) {
			/*
	        AbstractButton abstractButton = (AbstractButton)e.getSource();
	        ButtonModel buttonModel = abstractButton.getModel();
	        boolean armed = buttonModel.isArmed();
	        boolean pressed = buttonModel.isPressed();
	        boolean selected = buttonModel.isSelected();
	        */
	        //if (((JCheckBox) e.getSource()).isSelected()) {
		    	int id = myDB.getSelectedID();  	
		    	if (id > 0) {
			      	String tablename = myDB.getActualTable().getTablename();
			      	LinkedHashMap<String, Timestamp> v = DBKernel.getFirstUserFromChangeLog(tablename, id);   
			      	//System.out.println(v.keySet().toString());
		      		if (v.containsKey(DBKernel.getUsername())) {
				        this.setSelected(!this.isSelected());
	    				InfoBox ib = new InfoBox("Der Benutzer, der diesen Datensatz erstellt hat,\ndarf das Geprueft-Feld nicht ändern!", true, new Dimension(333, 150), null, true);
	    				ib.setVisible(true);    				
		      		}		
			      	if (isNull && this.isSelected()) isNull = false;			   
			    }
	        //}
		}
	}
}
