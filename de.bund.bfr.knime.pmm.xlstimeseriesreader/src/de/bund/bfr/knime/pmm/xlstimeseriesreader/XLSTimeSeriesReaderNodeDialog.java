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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.ListUtilities;
import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.FilePanel;
import de.bund.bfr.knime.pmm.common.ui.FilePanel.FileListener;

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
		ActionListener, ItemListener, FileListener {

	private static final String OTHER_PARAMETER = "Other Parameter";
	private static final String NO_PARAMETER = "Select Other";

	private FilePanel filePanel;

	private JComboBox<String> timeBox;
	private JComboBox<String> logcBox;
	private JComboBox<String> tempBox;

	private JPanel columnsPanel;
	private Map<String, JComboBox<String>> mappingBoxes;
	private Map<String, JButton> mappingButtons;
	private Map<String, Integer> mappingIDs;
	private JLabel noLabel;

	/**
	 * New pane for configuring the XLSTimeSeriesReader node.
	 */
	protected XLSTimeSeriesReaderNodeDialog() {
		filePanel = new FilePanel("XLS File", FilePanel.OPEN_DIALOG);
		filePanel.setAcceptAllFiles(false);
		filePanel.addFileFilter(".xls", "Excel Spreadsheat (*.xls)");
		filePanel.addFileListener(this);

		timeBox = new JComboBox<String>(AttributeUtilities
				.getUnitsForAttribute(TimeSeriesSchema.TIME).toArray(
						new String[0]));
		logcBox = new JComboBox<String>(AttributeUtilities
				.getUnitsForAttribute(TimeSeriesSchema.LOGC).toArray(
						new String[0]));
		tempBox = new JComboBox<String>(AttributeUtilities
				.getUnitsForAttribute(AttributeUtilities.ATT_TEMPERATURE)
				.toArray(new String[0]));
		noLabel = new JLabel();
		noLabel.setPreferredSize(new Dimension(100, 50));
		columnsPanel = new JPanel();
		columnsPanel.setBorder(BorderFactory.createTitledBorder("Columns"));
		columnsPanel.setLayout(new BorderLayout());
		columnsPanel.add(noLabel, BorderLayout.CENTER);
		mappingBoxes = new LinkedHashMap<>();
		mappingButtons = new LinkedHashMap<>();
		mappingIDs = new LinkedHashMap<>();

		JPanel leftUnitsPanel = new JPanel();

		leftUnitsPanel.setLayout(new GridLayout(3, 1, 5, 5));
		leftUnitsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftUnitsPanel.add(new JLabel("Unit for "
				+ AttributeUtilities.getFullName(TimeSeriesSchema.TIME) + ":"));
		leftUnitsPanel.add(new JLabel("Unit for "
				+ AttributeUtilities.getFullName(TimeSeriesSchema.LOGC) + ":"));
		leftUnitsPanel
				.add(new JLabel(
						"Unit for "
								+ AttributeUtilities
										.getFullName(AttributeUtilities.ATT_TEMPERATURE)
								+ ":"));

		JPanel rightUnitsPanel = new JPanel();

		rightUnitsPanel.setLayout(new GridLayout(3, 1, 5, 5));
		rightUnitsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightUnitsPanel.add(timeBox);
		rightUnitsPanel.add(logcBox);
		rightUnitsPanel.add(tempBox);

		JPanel northUnitsPanel = new JPanel();

		northUnitsPanel.setLayout(new BorderLayout());
		northUnitsPanel.add(leftUnitsPanel, BorderLayout.WEST);
		northUnitsPanel.add(rightUnitsPanel, BorderLayout.CENTER);

		JPanel unitsPanel = new JPanel();

		unitsPanel.setBorder(BorderFactory.createTitledBorder("Units"));
		unitsPanel.setLayout(new BorderLayout());
		unitsPanel.add(northUnitsPanel, BorderLayout.NORTH);

		JPanel optionsPanel = new JPanel();

		optionsPanel.setLayout(new BorderLayout());
		optionsPanel.add(unitsPanel, BorderLayout.WEST);
		optionsPanel.add(columnsPanel, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(filePanel, BorderLayout.NORTH);
		mainPanel.add(optionsPanel, BorderLayout.WEST);

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(mainPanel, BorderLayout.NORTH);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		Map<String, Integer> mapIDs;

		try {
			filePanel.removeFileListener(this);
			filePanel.setFileName(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILENAME));
			filePanel.addFileListener(this);
		} catch (InvalidSettingsException e) {
			filePanel.setFileName(null);
		}

		try {
			mapIDs = XLSTimeSeriesReaderNodeModel
					.getColumnMappingsAsMap(ListUtilities.getStringListFromString(settings
							.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_COLUMNMAPPINGS)));
		} catch (InvalidSettingsException e) {
			mapIDs = new LinkedHashMap<>();
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
					.getStandardUnit(AttributeUtilities.ATT_TEMPERATURE));
		}

		updateMappingButtons(mapIDs);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (filePanel.getFileName() == null) {
			throw new InvalidSettingsException("");
		}

		for (Integer id : mappingIDs.values()) {
			if (id == null) {
				throw new InvalidSettingsException("");
			}
		}

		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILENAME,
				filePanel.getFileName());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_COLUMNMAPPINGS,
				ListUtilities.getStringFromList(XLSTimeSeriesReaderNodeModel
						.getColumnMappingsAsList(mappingIDs)));
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_TIMEUNIT,
				(String) timeBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_LOGCUNIT,
				(String) logcBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_TEMPUNIT,
				(String) tempBox.getSelectedItem());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (String column : mappingButtons.keySet()) {
			if (e.getSource() == mappingButtons.get(column)) {
				Integer miscID = openDBWindow(mappingIDs.get(column));

				if (miscID != null) {
					String misc = ""
							+ DBKernel.getValue("SonstigeParameter", "ID",
									miscID + "", "Parameter");

					mappingButtons.get(column).setText(misc);
					mappingIDs.put(column, miscID);
				}

				break;
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			for (String column : mappingButtons.keySet()) {
				if (e.getSource() == mappingBoxes.get(column)) {
					JComboBox<String> box = mappingBoxes.get(column);
					JButton button = mappingButtons.get(column);

					if (box.getSelectedItem().equals(XLSReader.ID_COLUMN)) {
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
						mappingIDs.put(column, XLSReader.ID_COLUMN_ID);
					} else if (box.getSelectedItem().equals(
							TimeSeriesSchema.ATT_COMMENT)) {
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
						mappingIDs.put(column, XLSReader.COMMENT_COLUMN_ID);
					} else if (box.getSelectedItem().equals(
							TimeSeriesSchema.TIME)) {
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
						mappingIDs.put(column, XLSReader.TIME_COLUMN_ID);
					} else if (box.getSelectedItem().equals(
							TimeSeriesSchema.LOGC)) {
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
						mappingIDs.put(column, XLSReader.LOGC_COLUMN_ID);
					} else if (box.getSelectedItem().equals(
							AttributeUtilities.ATT_TEMPERATURE)) {
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
						mappingIDs.put(column,
								AttributeUtilities.ATT_TEMPERATURE_ID);
					} else if (box.getSelectedItem().equals(
							AttributeUtilities.ATT_PH)) {
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
						mappingIDs.put(column, AttributeUtilities.ATT_PH_ID);
					} else if (box.getSelectedItem().equals(
							AttributeUtilities.ATT_WATERACTIVITY)) {
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
						mappingIDs.put(column, AttributeUtilities.ATT_AW_ID);
					} else {
						button.setEnabled(true);
						button.setText(NO_PARAMETER);
						mappingIDs.put(column, null);
					}

					break;
				}
			}
		}
	}

	@Override
	public void fileChanged(FilePanel source) {
		updateMappingButtons(new LinkedHashMap<String, Integer>());
	}

	private void updateMappingButtons(Map<String, Integer> initialMapIDs) {
		try {
			List<String> columnList = XLSReader
					.getTimeSeriesMiscColumns(new File(filePanel.getFileName()));

			columnsPanel.removeAll();
			mappingBoxes.clear();
			mappingButtons.clear();

			JPanel northPanel = new JPanel();
			int row = 0;

			northPanel.setLayout(new GridBagLayout());

			for (String column : columnList) {
				JComboBox<String> box = new JComboBox<>(new String[] {
						XLSReader.ID_COLUMN, TimeSeriesSchema.ATT_COMMENT,
						TimeSeriesSchema.TIME, TimeSeriesSchema.LOGC,
						AttributeUtilities.ATT_TEMPERATURE,
						AttributeUtilities.ATT_PH,
						AttributeUtilities.ATT_WATERACTIVITY, OTHER_PARAMETER });
				JButton button = new JButton();

				if (initialMapIDs.containsKey(column)) {
					int id = initialMapIDs.get(column);

					if (id == XLSReader.ID_COLUMN_ID) {
						box.setSelectedItem(XLSReader.ID_COLUMN);
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
					} else if (id == XLSReader.COMMENT_COLUMN_ID) {
						box.setSelectedItem(TimeSeriesSchema.ATT_COMMENT);
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
					} else if (id == XLSReader.TIME_COLUMN_ID) {
						box.setSelectedItem(TimeSeriesSchema.TIME);
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
					} else if (id == XLSReader.LOGC_COLUMN_ID) {
						box.setSelectedItem(TimeSeriesSchema.LOGC);
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
					} else if (id == AttributeUtilities.ATT_TEMPERATURE_ID) {
						box.setSelectedItem(AttributeUtilities.ATT_TEMPERATURE);
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
					} else if (id == AttributeUtilities.ATT_PH_ID) {
						box.setSelectedItem(AttributeUtilities.ATT_PH);
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
					} else if (id == AttributeUtilities.ATT_AW_ID) {
						box.setSelectedItem(AttributeUtilities.ATT_WATERACTIVITY);
						button.setEnabled(false);
						button.setText(NO_PARAMETER);
					} else {
						box.setSelectedItem(OTHER_PARAMETER);
						button.setEnabled(true);
						button.setText(""
								+ DBKernel.getValue("SonstigeParameter", "ID",
										id + "", "Parameter"));
					}

					mappingIDs.put(column, id);
				} else {
					box.setSelectedItem(OTHER_PARAMETER);
					button.setEnabled(true);
					button.setText(NO_PARAMETER);
					mappingIDs.put(column, null);
				}

				box.addItemListener(this);
				button.addActionListener(this);
				mappingBoxes.put(column, box);
				mappingButtons.put(column, button);

				northPanel.add(new JLabel(column + ":"),
						createConstraints(0, row));
				northPanel.add(box, createConstraints(1, row));
				northPanel.add(button, createConstraints(2, row));
				row++;
			}

			columnsPanel.add(northPanel, BorderLayout.NORTH);
		} catch (Exception e) {
			columnsPanel.removeAll();
			columnsPanel.add(noLabel, BorderLayout.CENTER);
			mappingButtons.clear();
		}
	}

	private Integer openDBWindow(Integer id) {
		MyTable myT = DBKernel.myList.getTable("SonstigeParameter");
		Object newVal = DBKernel.myList.openNewWindow(myT, id,
				"SonstigeParameter", null, null, null, null, true);

		if (newVal instanceof Integer) {
			return (Integer) newVal;
		} else {
			return null;
		}
	}

	private GridBagConstraints createConstraints(int x, int y) {
		return new GridBagConstraints(x, y, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
	}

}
