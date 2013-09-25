/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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
package de.bund.bfr.knime.pmm.common.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JComboBox;
import javax.swing.JDialog;

public class UI {

	private UI() {
	}

	public static void select(JComboBox<?> box, Object item) {
		box.setSelectedItem(item);

		if (box.getSelectedItem() != null
				&& !box.getSelectedItem().equals(item)) {
			box.setSelectedItem(null);
		}
	}

	public static void select(JComboBox<?> box, Object item, Object defaultItem) {
		box.setSelectedItem(item);

		if (box.getSelectedItem() != null
				&& !box.getSelectedItem().equals(item)) {
			box.setSelectedItem(defaultItem);
		}
	}

	public static void adjustDialog(JDialog dialog) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(
				dialog.getGraphicsConfiguration());
		int maxWidth = screenSize.width - insets.left - insets.right;
		int maxHeight = screenSize.height - insets.top - insets.bottom;

		dialog.setSize(Math.min(dialog.getWidth(), maxWidth),
				Math.min(dialog.getHeight(), maxHeight));

		int minX = insets.left;
		int minY = insets.top;
		int maxX = screenSize.width - insets.right - dialog.getWidth();
		int maxY = screenSize.height - insets.bottom - dialog.getHeight();

		dialog.setLocation(Math.max(dialog.getX(), minX),
				Math.max(dialog.getY(), minY));
		dialog.setLocation(Math.min(dialog.getX(), maxX),
				Math.min(dialog.getY(), maxY));
	}
}
