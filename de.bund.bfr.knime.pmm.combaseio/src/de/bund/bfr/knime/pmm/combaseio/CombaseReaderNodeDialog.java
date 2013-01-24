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
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.FilePanel;

/**
 * <code>NodeDialog</code> for the "CombaseReader" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Jorgen Brandt
 */
public class CombaseReaderNodeDialog extends NodeDialogPane {

	private FilePanel filePanel;
	private DoubleTextField eleminationField;
	private DoubleTextField growthField;

	/**
	 * New pane for configuring the CombaseReader node.
	 */
	protected CombaseReaderNodeDialog() {
		filePanel = new FilePanel("Selected File:", FilePanel.OPEN_DIALOG);
		filePanel.setAcceptAllFiles(false);
		filePanel.addFileFilter(".csv", "Combase File (*.csv)");
		eleminationField = new DoubleTextField();
		eleminationField.setPreferredSize(new Dimension(100, eleminationField
				.getPreferredSize().height));
		growthField = new DoubleTextField();
		growthField.setPreferredSize(new Dimension(100, growthField
				.getPreferredSize().height));

		JPanel leftOptionsPanel = new JPanel();

		leftOptionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftOptionsPanel.setLayout(new GridLayout(2, 1, 5, 5));
		leftOptionsPanel.add(new JLabel("Start value for elimination:"));
		leftOptionsPanel.add(new JLabel("Start value for growth:"));

		JPanel rightOptionsPanel = new JPanel();

		rightOptionsPanel
				.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightOptionsPanel.setLayout(new GridLayout(2, 1, 5, 5));
		rightOptionsPanel.add(eleminationField);
		rightOptionsPanel.add(growthField);

		JPanel optionsPanel = new JPanel();

		optionsPanel
				.setBorder(BorderFactory
						.createTitledBorder("Values for Data Model if only Maximum Rate is known"));
		optionsPanel.setLayout(new BorderLayout());
		optionsPanel.add(leftOptionsPanel, BorderLayout.WEST);
		optionsPanel.add(rightOptionsPanel, BorderLayout.EAST);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.add(filePanel);
		northPanel.add(optionsPanel);

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(northPanel, BorderLayout.NORTH);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		String fileName;
		double startElim;
		double startGrow;

		try {
			fileName = settings
					.getString(CombaseReaderNodeModel.PARAM_FILENAME);
		} catch (InvalidSettingsException e) {
			fileName = CombaseReaderNodeModel.DEFAULT_FILENAME;
		}

		try {
			startElim = settings
					.getDouble(CombaseReaderNodeModel.PARAM_STARTELIM);
		} catch (InvalidSettingsException e) {
			startElim = CombaseReaderNodeModel.DEFAULT_STARTELIM;
		}

		try {
			startGrow = settings
					.getDouble(CombaseReaderNodeModel.PARAM_STARTGROW);
		} catch (InvalidSettingsException e) {
			startGrow = CombaseReaderNodeModel.DEFAULT_STARTGROW;
		}

		filePanel.setFileName(fileName);
		eleminationField.setValue(startElim);
		growthField.setValue(startGrow);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!filePanel.isFileNameValid() || !eleminationField.isValueValid()
				|| !growthField.isValueValid()) {
			throw new InvalidSettingsException("");
		}

		settings.addString(CombaseReaderNodeModel.PARAM_FILENAME,
				filePanel.getFileName());
		settings.addDouble(CombaseReaderNodeModel.PARAM_STARTELIM,
				eleminationField.getValue());
		settings.addDouble(CombaseReaderNodeModel.PARAM_STARTGROW,
				growthField.getValue());
	}

}
