/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.editoren;

import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

import quick.dbtable.CellComponent;

/**
 * @author Armin
 *
 */
public class MyLabelRenderer extends JLabel implements CellComponent {

	public MyLabelRenderer() {
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.TOP);
	}
	public void setValue(Object value) { 
		if (value == null) this.setText(""); 
		else this.setText(value.toString()); 
	}

	public void addActionListener(ActionListener arg0) {
	}

	public JComponent getComponent() {
		return this;
	}

	public Object getValue() {
		return null;
	}

}
