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
package de.bund.bfr.knime.pmm.combaseio;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.ui.FilePanel;

/**
 * <code>NodeDialog</code> for the "CombaseWriter" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Jorgen Brandt
 */
public class CombaseWriterNodeDialog extends NodeDialogPane {

	private FilePanel filePanel;

	/**
	 * New pane for configuring the CombaseWriter node.
	 */
	protected CombaseWriterNodeDialog() {
		filePanel = new FilePanel("Combase File", FilePanel.SAVE_DIALOG);
		filePanel.setAcceptAllFiles(false);
		filePanel.addFileFilter(".csv", "Combase File (*.csv)");

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(filePanel, BorderLayout.NORTH);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		String fileName;

		try {
			fileName = settings
					.getString(CombaseWriterNodeModel.PARAM_FILENAME);
		} catch (InvalidSettingsException e) {
			fileName = CombaseWriterNodeModel.DEFAULT_FILENAME;
		}

		filePanel.setFileName(fileName);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!filePanel.isFileNameValid()) {
			throw new InvalidSettingsException("");
		}

		settings.addString(CombaseWriterNodeModel.PARAM_FILENAME,
				filePanel.getFileName());
	}
}
