/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.knime.nodes.r.view;

import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Displays the R result image.
 * 
 * @author Thomas Gabriel, University of Konstanz
 */
public final class CustomRPlotterViewPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JLabel m_label;
    
    /**
     * Creates a new panel with an empty label.
     * @param image The content to show.
     */
    public CustomRPlotterViewPanel(final Image image) {
        m_label = new JLabel("<No Plot>");
        super.setLayout(new GridLayout(1, 1));
        super.add(m_label);
        update(image);
    }

    /**
     * Creates a new panel with an empty label.
     */
    public CustomRPlotterViewPanel() {
        m_label = new JLabel("<No Plot>");
        super.add(m_label);
    }

    /**
     * @param image The new image or null to display.
     */
    public void update(final Image image) {
        if (image == null) {
            m_label.setIcon(null);
            m_label.setText("<No Plot>");
        } else {
            m_label.setText(null);
            m_label.setIcon(new ImageIcon(image));
        }
        super.repaint();
    }


}