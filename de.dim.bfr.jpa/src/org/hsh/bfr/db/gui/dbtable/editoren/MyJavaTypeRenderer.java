/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.editoren;

import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import quick.dbtable.CellComponent;

/**
 * @author Armin
 *
 */
public class MyJavaTypeRenderer extends JTextArea implements CellComponent {

	public MyJavaTypeRenderer() {
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
	}
	
	public void setValue(Object value) { 
		Object[] o;
		if (value == null) this.setText(""); 
		else if (value instanceof Object[] && (o = (Object[]) value).length > 0) {
			String newText = o[0].toString();
			for (int i=1;i<o.length;i++) if (o[i] != null) newText += "\n" + o[i];
			this.setText(newText);				
		}
		else {
			this.setText(value.toString()); 
		}
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
