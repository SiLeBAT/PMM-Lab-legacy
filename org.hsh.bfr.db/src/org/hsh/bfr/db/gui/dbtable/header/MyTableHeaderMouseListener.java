/*******************************************************************************
 * PMM-Lab � 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab � 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * J�rgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Th�ns (BfR)
 * Annemarie K�sbohrer (BfR)
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
package org.hsh.bfr.db.gui.dbtable.header;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.hsh.bfr.db.gui.dbtable.MyDBTable;


/**
 * @author Armin
 *
 */
public class MyTableHeaderMouseListener implements MouseListener {

  private final MyDBTable theTable;

  public MyTableHeaderMouseListener(final MyDBTable theTable) {
    this.theTable = theTable;
  }

  public void actionPerformed(ActionEvent e) {
/*
  	if (table.getModel() instanceof SortableTableModel) {
      ((SortableTableModel)tabelle.getModel()).sort(Integer.parseInt(e.getActionCommand()));
    }
    */
  }

  public void mouseClicked(MouseEvent e) {
    //super.mouseClicked(e);
    System.out.println("Header clicked : (X: " + e.getX() + ", Y: " + e.getY() + ") With button " + e.getButton() );
    theTable.sorterChanged(null);
}

	public void mouseEntered(MouseEvent arg0) {
    System.out.println("mouseEntered");
	}

	public void mouseExited(MouseEvent arg0) {
    System.out.println("mouseExited");
	}

	public void mousePressed(MouseEvent arg0) {
    System.out.println("mousePressed");
	}

	public void mouseReleased(MouseEvent arg0) {
    System.out.println("mouseReleased");
	}
}