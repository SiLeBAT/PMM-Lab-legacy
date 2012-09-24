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
package de.bund.bfr.knime.pmm.xlstimeseriesreader;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;

/**
 * <code>NodeDialog</code> for the "XLSTimeSeriesReader" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class XLSTimeSeriesReaderNodeDialog extends NodeDialogPane implements
		ActionListener {

	private JButton fileButton;
	private StringTextField fileField;
	private JComboBox timeBox;
	private JComboBox logcBox;
	private JComboBox tempBox;

	/**
	 * New pane for configuring the XLSTimeSeriesReader node.
	 */
	protected XLSTimeSeriesReaderNodeDialog() {
		JPanel panel = new JPanel();
		JPanel optionsPanel = new JPanel();
		JPanel filePanel = new JPanel();
		JPanel unitsPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();

		fileButton = new JButton("Browse...");
		fileButton.addActionListener(this);
		fileField = new StringTextField();
		timeBox = new JComboBox(AttributeUtilities.getUnitsForAttribute(
				TimeSeriesSchema.ATT_TIME).toArray());
		logcBox = new JComboBox(AttributeUtilities.getUnitsForAttribute(
				TimeSeriesSchema.ATT_LOGC).toArray());
		tempBox = new JComboBox(AttributeUtilities.getUnitsForAttribute(
				TimeSeriesSchema.ATT_TEMPERATURE).toArray());

		filePanel.setBorder(BorderFactory.createTitledBorder("XLS File"));
		filePanel.setLayout(new BorderLayout(5, 5));
		filePanel.add(fileField, BorderLayout.CENTER);
		filePanel.add(fileButton, BorderLayout.EAST);

		leftPanel.setLayout(new GridLayout(3, 1, 5, 5));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftPanel.add(new JLabel("Unit for "
				+ AttributeUtilities.getFullName(TimeSeriesSchema.ATT_TIME)
				+ ":"));
		leftPanel.add(new JLabel("Unit for "
				+ AttributeUtilities.getFullName(TimeSeriesSchema.ATT_LOGC)
				+ ":"));
		leftPanel.add(new JLabel("Unit for "
				+ AttributeUtilities
						.getFullName(TimeSeriesSchema.ATT_TEMPERATURE) + ":"));

		rightPanel.setLayout(new GridLayout(3, 1, 5, 5));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel.add(timeBox);
		rightPanel.add(logcBox);
		rightPanel.add(tempBox);

		unitsPanel.setBorder(BorderFactory.createTitledBorder("Units"));
		unitsPanel.setLayout(new BorderLayout());
		unitsPanel.add(leftPanel, BorderLayout.WEST);
		unitsPanel.add(rightPanel, BorderLayout.EAST);

		optionsPanel.setLayout(new BorderLayout());
		optionsPanel.add(filePanel, BorderLayout.NORTH);
		optionsPanel.add(unitsPanel, BorderLayout.WEST);

		panel.setLayout(new BorderLayout());
		panel.add(optionsPanel, BorderLayout.NORTH);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		try {
			fileField.setValue(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILENAME));
		} catch (InvalidSettingsException e) {
			fileField.setValue(null);
		}

		try {
			timeBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_TIMEUNIT));
		} catch (InvalidSettingsException e) {
			timeBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_TIME));
		}

		try {
			logcBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_LOGCUNIT));
		} catch (InvalidSettingsException e) {
			logcBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_LOGC));
		}

		try {
			tempBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_TEMPUNIT));
		} catch (InvalidSettingsException e) {
			tempBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_TEMPERATURE));
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (fileField.getText().trim().isEmpty()) {
			throw new InvalidSettingsException("");
		}

		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILENAME,
				fileField.getText());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_TIMEUNIT,
				(String) timeBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_LOGCUNIT,
				(String) logcBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_TEMPUNIT,
				(String) tempBox.getSelectedItem());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser;

		try {
			fileChooser = new JFileChooser(new File(fileField.getText()));
		} catch (Exception ex) {
			fileChooser = new JFileChooser();
		}

		FileFilter xlsFilter = new FileFilter() {

			@Override
			public String getDescription() {
				return "Excel Spreadsheat (*.xls)";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory()
						|| f.getName().toLowerCase().endsWith(".xls");
			}
		};

		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(xlsFilter);

		if (fileChooser.showOpenDialog(fileButton) == JFileChooser.APPROVE_OPTION) {
			fileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}
}
