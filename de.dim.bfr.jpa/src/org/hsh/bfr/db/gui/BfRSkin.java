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
