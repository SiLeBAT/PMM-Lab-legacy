/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.editoren;

import javax.swing.*;

import quick.dbtable.*;

public class MyImageCell extends JLabel implements CellComponent {

  /**
	 * 
	 */
	private static final long serialVersionUID = 4895061240137425505L;
	static final int IMAGE = 0;
  static final int MUSIC = 1;
  static final int WWW = 2;
  static final int TEXT = 3;
  static final int DATETIME = 4;
  public static final int DATE = 5;

  int cellType;
  String text;

  public MyImageCell(int cellType, Object o) {
    this.setText("");
    this.setHorizontalAlignment(SwingConstants.CENTER);
    this.cellType = cellType;
    this.setValue(o);
  }
  public MyImageCell() {
    this(IMAGE);
  }
  public MyImageCell(int cellType) {
    this(cellType, null);
  }

  public void setValue(Object value) {
    if (value != null && !value.equals("")) {
      if (cellType == WWW) {
        this.setText("WWW-Code");
      }
      else if (cellType == TEXT) {
        this.setText("TEXT");
      }
      else if (cellType == DATE) {
        if (value == null) this.setText("");
        else this.setText(getDate((java.util.Date) value, "dd.MM.yyyy"));
      }
    }
    else {
      this.setText("");
      this.setIcon(null);
    }
  }
  private String getDate(java.util.Date date, String DATE_FORMAT) {
	    if (date == null || date.getTime() == 0) return "";
	    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);
	    return dateFormat.format(date).toString();
	  }
  public Object getValue() {
    return this.getIcon();
  }
  public JComponent getComponent() {
    return this;
  }
  public void addActionListener(java.awt.event.ActionListener listener) {
     ;
  }
}
