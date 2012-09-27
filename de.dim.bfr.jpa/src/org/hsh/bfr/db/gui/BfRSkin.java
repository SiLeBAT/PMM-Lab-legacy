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
package org.hsh.bfr.db.gui;

/**
 * @author Armin
 *
 */
import javax.swing.*;
import java.awt.*;

import quick.dbtable.Skin;
import quick.dbtable.CellPropertiesModel;

public class BfRSkin extends Skin {
    int rowHeight = 60;
    Color lightGreen = Color.LIGHT_GRAY; //new Color(210,210,144);
    Color darkGreen = Color.GRAY; // new Color(51,102,102);
    Font font = new Font("Arial",Font.BOLD,12);

  CellPropertiesModel cp = new CellPropertiesModel() {
             public Color getForeground(int row, int col) {
                  return darkGreen;
             }
             public int getAlignment(int row, int col) {
                if( col== 0 ) return SwingConstants.LEFT;
                else return SwingConstants.RIGHT;
             }
  };

  public BfRSkin() {
    put(Skin.TABLE_FONT,font);
    put(Skin.SELECTION_BACKGROUND,lightGreen);//
    put(Skin.SELECTION_FOREGROUND, darkGreen);
    put(Skin.HEADER_FOREGROUND,Color.white);//
    put(Skin.HEADER_BACKGROUND,darkGreen);
    put(Skin.HEADER_FONT,font);//
//    put(Skin.FOCUS_CELL_HIGHLIGHT_BORDER,new javax.swing.border.MatteBorder(3, 3,3,3, header) );
//    put(Skin.FOCUS_CELL_BACKGROUND,Color.white);
//    put(Skin.FOCUS_CELL_FOREGROUND,Color.red);
    put(Skin.GRID_COLOR,lightGreen);   //
    put(Skin.NAVIGATION_FOREGROUND,Color.white);
    put(Skin.NAVIGATION_BACKGROUND,darkGreen);
    put(Skin.NAVIGATION_FONT,font);
    put(Skin.ROW_HEIGTH, new Integer(rowHeight));  //
    put(Skin.CELL_PROPERTIES_MODEL,cp);
    put(Skin.TABLE_BACKGROUND, Color.white);

    //examples to set other properties
    //put(Skin.TABLE_BACKGROUND,Color.lightGray);
    //put(Skin.TABLE_FOREGROUND,Color.white);
    //put(Skin.SHOW_HORIZONTAL_LINES, Boolean.TRUE);
    put(Skin.SHOW_VERTICAL_LINES, Boolean.FALSE);
    //put(Skin.NEXT_ICON,new ImageIcon(getClass().getResource("next.gif")));
    //put(Skin.PREVIOUS_ICON, new ImageIcon(getClass().getResource("prev.gif")));
    //put(Skin.FIRST_ICON,new ImageIcon(getClass().getResource("first.gif")));
    //put(Skin.LAST_ICON,new ImageIcon(getClass().getResource("last.gif")));
    //put(Skin.DELETE_ICON,new ImageIcon(getClass().getResource("delete.gif")));
    //put(Skin.ADD_ICON,new ImageIcon(getClass().getResource("new.gif")));
    //put(Skin.NAVIGATION_BUTTON_COLOR,Color.lightGray);
  }
}
