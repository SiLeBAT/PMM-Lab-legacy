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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.FilePanel;

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

	private FilePanel filePanel;
	private JComboBox<String> formatBox;
	private JComboBox<String> timeBox;
	private JComboBox<String> logcBox;
	private JComboBox<String> tempBox;

	/**
	 * New pane for configuring the XLSTimeSeriesReader node.
	 */
	protected XLSTimeSeriesReaderNodeDialog() {
		JPanel panel = new JPanel();
		JPanel mainPanel = new JPanel();
		JPanel optionsPanel = new JPanel();
		JPanel formatPanel = new JPanel();
		JPanel unitsPanel = new JPanel();
		JPanel leftUnitsPanel = new JPanel();
		JPanel rightUnitsPanel = new JPanel();

		filePanel = new FilePanel("XLS File", FilePanel.OPEN_DIALOG);
		filePanel.setAcceptAllFiles(false);
		filePanel.addFileFilter(".xls", "Excel Spreadsheat (*.xls)");
		formatBox = new JComboBox<String>(new String[] {
				XLSTimeSeriesReaderNodeModel.TIMESERIESFORMAT,
				XLSTimeSeriesReaderNodeModel.DVALUEFORMAT });
		formatBox.addActionListener(this);
		timeBox = new JComboBox<String>(AttributeUtilities
				.getUnitsForAttribute(TimeSeriesSchema.TIME).toArray(
						new String[0]));
		logcBox = new JComboBox<String>(AttributeUtilities
				.getUnitsForAttribute(TimeSeriesSchema.LOGC).toArray(
						new String[0]));
		tempBox = new JComboBox<String>(AttributeUtilities
				.getUnitsForAttribute(TimeSeriesSchema.ATT_TEMPERATURE)
				.toArray(new String[0]));

		formatPanel.setBorder(BorderFactory.createTitledBorder("File Format"));
		formatPanel.setLayout(new GridLayout(1, 1));
		formatPanel.add(formatBox);

		leftUnitsPanel.setLayout(new GridLayout(3, 1, 5, 5));
		leftUnitsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftUnitsPanel.add(new JLabel("Unit for "
				+ AttributeUtilities.getFullName(TimeSeriesSchema.TIME) + ":"));
		leftUnitsPanel.add(new JLabel("Unit for "
				+ AttributeUtilities.getFullName(TimeSeriesSchema.LOGC) + ":"));
		leftUnitsPanel.add(new JLabel("Unit for "
				+ AttributeUtilities
						.getFullName(TimeSeriesSchema.ATT_TEMPERATURE) + ":"));

		rightUnitsPanel.setLayout(new GridLayout(3, 1, 5, 5));
		rightUnitsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightUnitsPanel.add(timeBox);
		rightUnitsPanel.add(logcBox);
		rightUnitsPanel.add(tempBox);

		unitsPanel.setBorder(BorderFactory.createTitledBorder("Units"));
		unitsPanel.setLayout(new BorderLayout());
		unitsPanel.add(leftUnitsPanel, BorderLayout.WEST);
		unitsPanel.add(rightUnitsPanel, BorderLayout.EAST);

		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		optionsPanel.add(formatPanel);
		optionsPanel.add(unitsPanel);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(filePanel, BorderLayout.NORTH);
		mainPanel.add(optionsPanel, BorderLayout.WEST);

		panel.setLayout(new BorderLayout());
		panel.add(mainPanel, BorderLayout.NORTH);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		try {
			filePanel.setFileName(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILENAME));
		} catch (InvalidSettingsException e) {
			filePanel.setFileName(null);
		}

		try {
			formatBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILEFORMAT));
		} catch (InvalidSettingsException e) {
			formatBox
					.setSelectedItem(XLSTimeSeriesReaderNodeModel.DEFAULT_FILEFORMAT);
		}

		try {
			timeBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_TIMEUNIT));
		} catch (InvalidSettingsException e) {
			timeBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.TIME));
		}

		try {
			logcBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_LOGCUNIT));
		} catch (InvalidSettingsException e) {
			logcBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.LOGC));
		}

		try {
			tempBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_TEMPUNIT));
		} catch (InvalidSettingsException e) {
			tempBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_TEMPERATURE));
		}

		updateComboBoxes();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (filePanel.getFileName() == null) {
			throw new InvalidSettingsException("");
		}

		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILENAME,
				filePanel.getFileName());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILEFORMAT,
				(String) formatBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_TIMEUNIT,
				(String) timeBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_LOGCUNIT,
				(String) logcBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_TEMPUNIT,
				(String) tempBox.getSelectedItem());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == formatBox) {
			updateComboBoxes();
		}
	}

	private void updateComboBoxes() {
		if (formatBox.getSelectedItem().equals(
				XLSTimeSeriesReaderNodeModel.TIMESERIESFORMAT)) {
			logcBox.setEnabled(true);
		} else if (formatBox.getSelectedItem().equals(
				XLSTimeSeriesReaderNodeModel.DVALUEFORMAT)) {
			logcBox.setEnabled(false);
		}
	}

}
