/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.editoren;

import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;

import quick.dbtable.CellComponent;

/**
 * @author Armin
 *
 */
public class MyBlobSizeRenderer extends JLabel implements CellComponent {

	private DecimalFormat df = new DecimalFormat("0.00");

	public MyBlobSizeRenderer() {
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.TOP);
	}
	public void setValue(Object value) { 
		if (value == null) this.setText(""); 
		else if (value instanceof Integer) {
			int size = (Integer) value;
			if (size < 1024) this.setText(size + " B");
			else if (size < 1024*1024) this.setText(df.format(size / 1024.0) + " kB");
			else this.setText(df.format(size / 1024.0 / 1024) + " MB");
		}
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
