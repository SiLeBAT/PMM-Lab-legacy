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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.XmlConverter;
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

	private static final String DO_NOT_USE = "Do Not Use";
	private static final String OTHER_PARAMETER = "Select Other";

	private JPanel mainPanel;

	private FilePanel filePanel;
	private JComboBox<String> sheetBox;
	private List<String> fileSheetList;
	private List<String> fileColumnList;

	private JComboBox<String> timeBox;
	private JComboBox<String> logcBox;
	private JComboBox<String> tempBox;

	private JButton addLiteratureButton;
	private JButton removeLiteratureButton;
	private JList<String> literatureList;
	private List<Integer> literatureIDs;
	private List<String> literatureData;

	private JPanel agentPanel;
	private JComboBox<String> agentBox;
	private JButton agentButton;
	private String agentColumn;
	private int agentID;
	private Map<String, String> agentMappings;
	private Map<String, JButton> agentButtons;

	private JPanel matrixPanel;
	private JComboBox<String> matrixBox;
	private JButton matrixButton;
	private String matrixColumn;
	private int matrixID;
	private Map<String, String> matrixMappings;
	private Map<String, JButton> matrixButtons;

	private JPanel columnsPanel;
	private Map<String, JComboBox<String>> columnBoxes;
	private Map<String, JButton> columnButtons;
	private Map<String, String> columnMappings;

	private JLabel noLabel;

	/**
	 * New pane for configuring the XLSTimeSeriesReader node.
	 */
	protected XLSTimeSeriesReaderNodeDialog() {
		filePanel = new FilePanel("XLS File", FilePanel.OPEN_DIALOG);
		filePanel.setAcceptAllFiles(false);
		filePanel.addFileFilter(".xls", "Excel Spreadsheat (*.xls)");
		filePanel.addFileListener(this);
		sheetBox = new JComboBox<>();
		sheetBox.addItemListener(this);
		fileSheetList = new ArrayList<>();
		fileColumnList = new ArrayList<>();

		timeBox = new JComboBox<String>(AttributeUtilities
				.getUnitsForAttribute(AttributeUtilities.TIME).toArray(
						new String[0]));
		logcBox = new JComboBox<String>(AttributeUtilities
				.getUnitsForAttribute(AttributeUtilities.LOGC).toArray(
						new String[0]));
		tempBox = new JComboBox<String>(AttributeUtilities
				.getUnitsForAttribute(AttributeUtilities.ATT_TEMPERATURE)
				.toArray(new String[0]));

		addLiteratureButton = new JButton("Add");
		addLiteratureButton.addActionListener(this);
		removeLiteratureButton = new JButton("Remove");
		removeLiteratureButton.addActionListener(this);
		literatureList = new JList<>();

		noLabel = new JLabel();
		noLabel.setPreferredSize(new Dimension(100, 50));
		agentPanel = new JPanel();
		agentPanel.setBorder(BorderFactory
				.createTitledBorder(AttributeUtilities
						.getFullName(TimeSeriesSchema.ATT_AGENT)));
		agentPanel.setLayout(new BorderLayout());
		agentPanel.add(noLabel, BorderLayout.CENTER);
		agentButtons = new LinkedHashMap<>();
		agentMappings = new LinkedHashMap<>();
		matrixPanel = new JPanel();
		matrixPanel.setBorder(BorderFactory
				.createTitledBorder(AttributeUtilities
						.getFullName(TimeSeriesSchema.ATT_MATRIX)));
		matrixPanel.setLayout(new BorderLayout());
		matrixPanel.add(noLabel, BorderLayout.CENTER);
		matrixButtons = new LinkedHashMap<>();
		matrixMappings = new LinkedHashMap<>();
		columnsPanel = new JPanel();
		columnsPanel.setBorder(BorderFactory
				.createTitledBorder("XLS Column assignments"));
		columnsPanel.setLayout(new BorderLayout());
		columnsPanel.add(noLabel, BorderLayout.CENTER);
		columnBoxes = new LinkedHashMap<>();
		columnButtons = new LinkedHashMap<>();
		columnMappings = new LinkedHashMap<>();

		JPanel northUnitsPanel = new JPanel();

		northUnitsPanel.setLayout(new GridBagLayout());
		northUnitsPanel.add(
				new JLabel(AttributeUtilities
						.getFullName(AttributeUtilities.TIME) + ":"),
				createConstraints(0, 0));
		northUnitsPanel.add(
				new JLabel(AttributeUtilities
						.getFullName(AttributeUtilities.LOGC) + ":"),
				createConstraints(0, 1));
		northUnitsPanel
				.add(new JLabel(AttributeUtilities
						.getFullName(AttributeUtilities.ATT_TEMPERATURE) + ":"),
						createConstraints(0, 2));
		northUnitsPanel.add(timeBox, createConstraints(1, 0));
		northUnitsPanel.add(logcBox, createConstraints(1, 1));
		northUnitsPanel.add(tempBox, createConstraints(1, 2));

		JPanel westUnitsPanel = new JPanel();

		westUnitsPanel.setLayout(new BorderLayout());
		westUnitsPanel.add(northUnitsPanel, BorderLayout.NORTH);

		JPanel unitsPanel = new JPanel();

		unitsPanel.setBorder(BorderFactory.createTitledBorder("Units"));
		unitsPanel.setLayout(new BorderLayout());
		unitsPanel.add(westUnitsPanel, BorderLayout.WEST);

		JPanel northLiteraturePanel = new JPanel();

		northLiteraturePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		northLiteraturePanel.add(addLiteratureButton);
		northLiteraturePanel.add(removeLiteratureButton);

		JPanel westLiteraturePanel = new JPanel();

		westLiteraturePanel.setLayout(new BorderLayout());
		westLiteraturePanel.add(northLiteraturePanel, BorderLayout.NORTH);
		westLiteraturePanel.add(new JScrollPane(literatureList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		JPanel literaturePanel = new JPanel();

		literaturePanel.setBorder(BorderFactory
				.createTitledBorder("Literature"));
		literaturePanel.setLayout(new BorderLayout());
		literaturePanel.add(westLiteraturePanel, BorderLayout.WEST);

		JPanel unitsLiteraturePanel = new JPanel();

		unitsLiteraturePanel.setLayout(new BorderLayout());
		unitsLiteraturePanel.add(unitsPanel, BorderLayout.NORTH);
		unitsLiteraturePanel.add(literaturePanel, BorderLayout.CENTER);

		JPanel optionsPanel = new JPanel();

		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
		optionsPanel.add(unitsLiteraturePanel);
		optionsPanel.add(agentPanel);
		optionsPanel.add(matrixPanel);
		optionsPanel.add(columnsPanel);

		JPanel sheetPanel = new JPanel();

		sheetPanel.setBorder(BorderFactory.createTitledBorder("Sheet"));
		sheetPanel.setLayout(new BorderLayout());
		sheetPanel.add(sheetBox, BorderLayout.NORTH);

		JPanel fileSheetPanel = new JPanel();

		fileSheetPanel.setLayout(new BorderLayout());
		fileSheetPanel.add(filePanel, BorderLayout.CENTER);
		fileSheetPanel.add(sheetPanel, BorderLayout.EAST);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(fileSheetPanel, BorderLayout.NORTH);
		mainPanel.add(optionsPanel, BorderLayout.CENTER);

		addTab("Options", mainPanel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		try {
			filePanel.removeFileListener(this);
			filePanel.setFileName(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILENAME));
			filePanel.addFileListener(this);
		} catch (InvalidSettingsException e) {
			filePanel.setFileName(null);
		}

		try {
			fileSheetList = XLSReader.getSheets(new File(filePanel
					.getFileName()));
		} catch (Exception e) {
			fileSheetList = new ArrayList<>();
		}

		try {
			sheetBox.removeItemListener(this);
			sheetBox.removeAllItems();

			for (String sheet : fileSheetList) {
				sheetBox.addItem(sheet);
			}

			sheetBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_SHEETNAME));
			sheetBox.addItemListener(this);
		} catch (InvalidSettingsException e) {
			sheetBox.removeAllItems();
		}

		try {
			fileColumnList = XLSReader.getColumns(
					new File(filePanel.getFileName()),
					(String) sheetBox.getSelectedItem());
		} catch (Exception e) {
			fileColumnList = new ArrayList<>();
		}

		try {
			columnMappings = XmlConverter
					.xmlToStringMap(settings
							.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_COLUMNMAPPINGS));
		} catch (InvalidSettingsException e) {
			columnMappings = new LinkedHashMap<>();
		}

		try {
			timeBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_TIMEUNIT));
		} catch (InvalidSettingsException e) {
			timeBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(AttributeUtilities.TIME));
		}

		try {
			logcBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_LOGCUNIT));
		} catch (InvalidSettingsException e) {
			logcBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(AttributeUtilities.LOGC));
		}

		try {
			tempBox.setSelectedItem(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_TEMPUNIT));
		} catch (InvalidSettingsException e) {
			tempBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(AttributeUtilities.ATT_TEMPERATURE));
		}

		try {
			agentColumn = settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTCOLUMN);

			if (agentColumn == null) {
				agentColumn = DO_NOT_USE;
			}
		} catch (InvalidSettingsException e) {
			agentColumn = DO_NOT_USE;
		}

		try {
			agentID = settings
					.getInt(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTID);
		} catch (InvalidSettingsException e) {
			agentID = -1;
		}

		try {
			agentMappings = XmlConverter
					.xmlToStringMap(settings
							.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTMAPPINGS));
		} catch (InvalidSettingsException e) {
			agentMappings = new LinkedHashMap<>();
		}

		try {
			matrixColumn = settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXCOLUMN);

			if (matrixColumn == null) {
				matrixColumn = DO_NOT_USE;
			}
		} catch (InvalidSettingsException e) {
			matrixColumn = DO_NOT_USE;
		}

		try {
			matrixID = settings
					.getInt(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXID);
		} catch (InvalidSettingsException e) {
			matrixID = -1;
		}

		try {
			matrixMappings = XmlConverter
					.xmlToStringMap(settings
							.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXMAPPINGS));
		} catch (InvalidSettingsException e) {
			matrixMappings = new LinkedHashMap<>();
		}

		try {
			literatureIDs = XmlConverter
					.xmlToIntList(settings
							.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_LITERATUREIDS));
			literatureData = new ArrayList<>();

			for (int id : literatureIDs) {
				String author = DBKernel.getValue("Literatur", "ID", id + "",
						"Erstautor") + "";
				String year = DBKernel.getValue("Literatur", "ID", id + "",
						"Jahr") + "";

				literatureData.add(author + "-" + year);
			}

			literatureList.setListData(literatureData.toArray(new String[0]));
		} catch (InvalidSettingsException e) {
			literatureIDs = new ArrayList<>();
			literatureList.setListData(new String[0]);
		}

		updateAgentPanel();
		updateMatrixPanel();
		updateColumnsPanel();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (filePanel.getFileName() == null) {
			throw new InvalidSettingsException("No file is specfied");
		}

		if (sheetBox.getSelectedItem() == null) {
			throw new InvalidSettingsException("No sheet is selected");
		}

		if (fileColumnList.isEmpty()) {
			throw new InvalidSettingsException("Specified file is invalid");
		}

		if (agentBox.getSelectedItem().equals(OTHER_PARAMETER) && agentID == -1) {
			throw new InvalidSettingsException("No assignment for "
					+ TimeSeriesSchema.ATT_AGENT);
		}

		if (matrixBox.getSelectedItem().equals(OTHER_PARAMETER)
				&& matrixID == -1) {
			throw new InvalidSettingsException("No assignment for "
					+ TimeSeriesSchema.ATT_MATRIX);
		}

		boolean idColumnMissing = true;
		Set<String> assignments = new LinkedHashSet<>();

		for (String column : columnMappings.keySet()) {
			String assignment = columnMappings.get(column);

			if (assignment == null) {
				throw new InvalidSettingsException("Column \"" + column
						+ "\" has no assignment");
			} else if (columnMappings.get(column).equals(XLSReader.ID_COLUMN)) {
				idColumnMissing = false;
			}

			if (!assignments.add(assignment)) {
				String name = null;

				try {
					int id = Integer.parseInt(assignment);

					if (id == AttributeUtilities.ATT_TEMPERATURE_ID) {
						name = AttributeUtilities.ATT_TEMPERATURE;
					} else if (id == AttributeUtilities.ATT_PH_ID) {
						name = AttributeUtilities.ATT_PH;
					} else if (id == AttributeUtilities.ATT_AW_ID) {
						name = AttributeUtilities.ATT_WATERACTIVITY;
					} else {
						name = DBKernel.getValue("SonstigeParameter", "ID", id
								+ "", "Parameter")
								+ "";
					}
				} catch (NumberFormatException e) {
					name = assignment;
				}

				throw new InvalidSettingsException("\"" + name
						+ "\" can only be assigned once");
			}
		}

		if (idColumnMissing) {
			throw new InvalidSettingsException("\"" + XLSReader.ID_COLUMN
					+ "\" is unassigned");
		}

		int agentID = this.agentID;
		int matrixID = this.matrixID;

		if (!agentColumn.equals(OTHER_PARAMETER)) {
			agentID = -1;
		}

		if (!matrixColumn.equals(OTHER_PARAMETER)) {
			matrixID = -1;
		}

		String agentColumn = this.agentColumn;
		String matrixColumn = this.matrixColumn;

		if (agentColumn.equals(OTHER_PARAMETER)
				|| agentColumn.equals(DO_NOT_USE)) {
			agentColumn = null;
		}

		if (matrixColumn.equals(OTHER_PARAMETER)
				|| matrixColumn.equals(DO_NOT_USE)) {
			matrixColumn = null;
		}

		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILENAME,
				filePanel.getFileName());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_SHEETNAME,
				(String) sheetBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_COLUMNMAPPINGS,
				XmlConverter.mapToXml(columnMappings));
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_TIMEUNIT,
				(String) timeBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_LOGCUNIT,
				(String) logcBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_TEMPUNIT,
				(String) tempBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTCOLUMN,
				agentColumn);
		settings.addInt(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTID, agentID);
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTMAPPINGS,
				XmlConverter.mapToXml(agentMappings));
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXCOLUMN,
				matrixColumn);
		settings.addInt(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXID, matrixID);
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXMAPPINGS,
				XmlConverter.mapToXml(matrixMappings));
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_LITERATUREIDS,
				XmlConverter.listToXml(literatureIDs));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == agentButton) {
			Integer newAgentID = openAgentDBWindow(agentID);

			if (newAgentID != null) {
				String agent = ""
						+ DBKernel.getValue("Agenzien", "ID", newAgentID + "",
								"Agensname");

				agentID = newAgentID;
				agentButton.setText(agent);
			}
		} else if (e.getSource() == matrixButton) {
			Integer newMatrixID = openMatrixDBWindow(matrixID);

			if (newMatrixID != null) {
				String matrix = ""
						+ DBKernel.getValue("Matrices", "ID", newMatrixID + "",
								"Matrixname");

				matrixID = newMatrixID;
				matrixButton.setText(matrix);
			}
		} else if (e.getSource() == addLiteratureButton) {
			Integer id = openLiteratureDBWindow(null);

			if (id != null && !literatureIDs.contains(id)) {
				String author = DBKernel.getValue("Literatur", "ID", id + "",
						"Erstautor") + "";
				String year = DBKernel.getValue("Literatur", "ID", id + "",
						"Jahr") + "";

				literatureIDs.add(id);
				literatureData.add(author + "-" + year);
				literatureList.setListData(literatureData
						.toArray(new String[0]));
			}
		} else if (e.getSource() == removeLiteratureButton) {
			if (literatureList.getSelectedIndices().length > 0) {
				int[] indices = literatureList.getSelectedIndices();

				Arrays.sort(indices);

				for (int i = indices.length - 1; i >= 0; i--) {
					literatureIDs.remove(i);
					literatureData.remove(i);
				}

				literatureList.setListData(literatureData
						.toArray(new String[0]));
			}
		} else {
			for (String value : agentButtons.keySet()) {
				if (e.getSource() == agentButtons.get(value)) {
					Integer id = null;

					try {
						id = Integer.parseInt(agentMappings.get(value));
					} catch (NumberFormatException ex) {
					}

					Integer newID = openAgentDBWindow(id);

					if (newID != null) {
						String agent = DBKernel.getValue("Agenzien", "ID",
								newID + "", "Agensname") + "";

						agentButtons.get(value).setText(agent);
						agentMappings.put(value, newID + "");
					}

					break;
				}
			}

			for (String value : matrixButtons.keySet()) {
				if (e.getSource() == matrixButtons.get(value)) {
					Integer id = null;

					try {
						id = Integer.parseInt(matrixMappings.get(value));
					} catch (NumberFormatException ex) {
					}

					Integer newID = openMatrixDBWindow(id);

					if (newID != null) {
						String matrix = DBKernel.getValue("Matrices", "ID",
								newID + "", "Matrixname") + "";

						matrixButtons.get(value).setText(matrix);
						matrixMappings.put(value, newID + "");
					}

					break;
				}
			}

			for (String column : columnButtons.keySet()) {
				if (e.getSource() == columnButtons.get(column)) {
					Integer intID = null;

					try {
						intID = Integer.parseInt(columnMappings.get(column));
					} catch (NumberFormatException ex) {
					}

					Integer miscID = openMiscDBWindow(intID);

					if (miscID != null) {
						String misc = ""
								+ DBKernel.getValue("SonstigeParameter", "ID",
										miscID + "", "Parameter");

						columnButtons.get(column).setText(misc);
						columnMappings.put(column, miscID + "");
					}

					break;
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() != ItemEvent.SELECTED) {
			return;
		}

		if (e.getSource() == sheetBox) {
			try {
				fileColumnList = XLSReader.getColumns(
						new File(filePanel.getFileName()),
						(String) sheetBox.getSelectedItem());
			} catch (Exception ex) {
				fileColumnList = new ArrayList<>();
			}

			columnMappings.clear();
			updateColumnsPanel();
			updateAgentPanel();
			updateMatrixPanel();
			mainPanel.revalidate();
		} else if (e.getSource() == agentBox) {
			agentColumn = (String) agentBox.getSelectedItem();
			updateAgentPanel();
		} else if (e.getSource() == matrixBox) {
			matrixColumn = (String) matrixBox.getSelectedItem();
			updateMatrixPanel();
		} else {
			for (String column : columnBoxes.keySet()) {
				if (e.getSource() == columnBoxes.get(column)) {
					JComboBox<String> box = columnBoxes.get(column);
					JButton button = columnButtons.get(column);

					if (box.getSelectedItem().equals(XLSReader.ID_COLUMN)) {
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
						columnMappings.put(column, XLSReader.ID_COLUMN);
					} else if (box.getSelectedItem().equals(
							TimeSeriesSchema.ATT_COMMENT)) {
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
						columnMappings
								.put(column, TimeSeriesSchema.ATT_COMMENT);
					} else if (box.getSelectedItem().equals(
							AttributeUtilities.TIME)) {
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
						columnMappings.put(column, AttributeUtilities.TIME);
					} else if (box.getSelectedItem().equals(
							AttributeUtilities.LOGC)) {
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
						columnMappings.put(column, AttributeUtilities.LOGC);
					} else if (box.getSelectedItem().equals(
							AttributeUtilities.ATT_TEMPERATURE)) {
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
						columnMappings.put(column,
								AttributeUtilities.ATT_TEMPERATURE_ID + "");
					} else if (box.getSelectedItem().equals(
							AttributeUtilities.ATT_PH)) {
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
						columnMappings.put(column, AttributeUtilities.ATT_PH_ID
								+ "");
					} else if (box.getSelectedItem().equals(
							AttributeUtilities.ATT_WATERACTIVITY)) {
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
						columnMappings.put(column, AttributeUtilities.ATT_AW_ID
								+ "");
					} else if (box.getSelectedItem().equals(OTHER_PARAMETER)) {
						button.setEnabled(true);
						button.setText(OTHER_PARAMETER);
						columnMappings.put(column, null);
					} else if (box.getSelectedItem().equals(DO_NOT_USE)) {
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
						columnMappings.remove(column);
					}

					break;
				}
			}
		}
	}

	@Override
	public void fileChanged(FilePanel source) {
		try {
			fileSheetList = XLSReader.getSheets(new File(filePanel
					.getFileName()));
		} catch (Exception e) {
			fileSheetList = new ArrayList<>();
		}

		sheetBox.removeItemListener(this);
		sheetBox.removeAllItems();

		for (String sheet : fileSheetList) {
			sheetBox.addItem(sheet);
		}

		sheetBox.setSelectedIndex(0);
		sheetBox.addItemListener(this);

		try {
			fileColumnList = XLSReader.getColumns(
					new File(filePanel.getFileName()),
					(String) sheetBox.getSelectedItem());
		} catch (Exception e) {
			fileColumnList = new ArrayList<>();
		}

		columnMappings.clear();
		updateColumnsPanel();
		updateAgentPanel();
		updateMatrixPanel();
		mainPanel.revalidate();
	}

	private void updateAgentPanel() {
		agentBox = new JComboBox<>(new String[] { DO_NOT_USE, OTHER_PARAMETER });
		agentButton = new JButton(OTHER_PARAMETER);

		for (String column : fileColumnList) {
			agentBox.addItem(column);
		}

		agentBox.setSelectedItem(agentColumn);

		if (agentID != -1) {
			agentButton.setText(""
					+ DBKernel.getValue("Agenzien", "ID", agentID + "",
							"Agensname"));
		} else {
			agentButton.setText(OTHER_PARAMETER);
		}

		agentBox.addItemListener(this);
		agentButton.addActionListener(this);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new GridBagLayout());
		northPanel.add(new JLabel("Column:"), createConstraints(0, 0));
		northPanel.add(agentBox, createConstraints(1, 0));

		if (agentBox.getSelectedItem().equals(DO_NOT_USE)) {
			// Do nothing
		} else if (agentBox.getSelectedItem().equals(OTHER_PARAMETER)) {
			northPanel.add(agentButton, createConstraints(1, 1));
		} else {
			int row = 1;
			String column = (String) agentBox.getSelectedItem();

			try {
				Set<String> values = XLSReader.getValuesInColumn(new File(
						filePanel.getFileName()), (String) sheetBox
						.getSelectedItem(), column);

				for (String value : values) {
					JButton button = new JButton();

					if (agentMappings.containsKey(value)) {
						button.setText(DBKernel.getValue("Agenzien", "ID",
								agentMappings.get(value), "Agensname") + "");
					} else {
						button.setText(OTHER_PARAMETER);
					}

					button.addActionListener(this);
					agentButtons.put(value, button);

					northPanel.add(new JLabel(value + ":"),
							createConstraints(0, row));
					northPanel.add(button, createConstraints(1, row));
					row++;
				}
			} catch (Exception e) {
			}
		}

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(northPanel, BorderLayout.NORTH);

		agentPanel.removeAll();
		agentPanel.add(new JScrollPane(panel), BorderLayout.CENTER);
	}

	private void updateMatrixPanel() {
		matrixBox = new JComboBox<>(
				new String[] { DO_NOT_USE, OTHER_PARAMETER });
		matrixButton = new JButton(OTHER_PARAMETER);

		for (String column : fileColumnList) {
			matrixBox.addItem(column);
		}

		matrixBox.setSelectedItem(matrixColumn);

		if (matrixID != -1) {
			matrixButton.setText(""
					+ DBKernel.getValue("Matrices", "ID", matrixID + "",
							"Matrixname"));
		} else {
			matrixButton.setText(OTHER_PARAMETER);
		}

		matrixBox.addItemListener(this);
		matrixButton.addActionListener(this);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new GridBagLayout());
		northPanel.add(new JLabel("Column:"), createConstraints(0, 0));
		northPanel.add(matrixBox, createConstraints(1, 0));

		if (matrixBox.getSelectedItem().equals(DO_NOT_USE)) {
			// Do nothing
		} else if (matrixBox.getSelectedItem().equals(OTHER_PARAMETER)) {
			northPanel.add(matrixButton, createConstraints(1, 1));
		} else {
			int row = 1;
			String column = (String) matrixBox.getSelectedItem();

			try {
				Set<String> values = XLSReader.getValuesInColumn(new File(
						filePanel.getFileName()), (String) sheetBox
						.getSelectedItem(), column);

				for (String value : values) {
					JButton button = new JButton();

					if (matrixMappings.containsKey(value)) {
						button.setText(DBKernel.getValue("Matrices", "ID",
								matrixMappings.get(value), "Matrixname") + "");
					} else {
						button.setText(OTHER_PARAMETER);
					}

					button.addActionListener(this);
					matrixButtons.put(value, button);

					northPanel.add(new JLabel(value + ":"),
							createConstraints(0, row));
					northPanel.add(button, createConstraints(1, row));
					row++;
				}
			} catch (Exception e) {
			}
		}

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(northPanel, BorderLayout.NORTH);

		matrixPanel.removeAll();
		matrixPanel.add(new JScrollPane(panel), BorderLayout.CENTER);
	}

	private void updateColumnsPanel() {
		if (!fileColumnList.isEmpty()) {
			columnBoxes.clear();
			columnButtons.clear();

			JPanel northPanel = new JPanel();
			int row = 0;

			northPanel.setLayout(new GridBagLayout());

			for (String column : fileColumnList) {
				JComboBox<String> box = new JComboBox<>(new String[] {
						XLSReader.ID_COLUMN, TimeSeriesSchema.ATT_COMMENT,
						AttributeUtilities.TIME, AttributeUtilities.LOGC,
						AttributeUtilities.ATT_TEMPERATURE,
						AttributeUtilities.ATT_PH,
						AttributeUtilities.ATT_WATERACTIVITY, OTHER_PARAMETER,
						DO_NOT_USE });
				JButton button = new JButton();

				if (columnMappings.containsKey(column)) {
					String id = columnMappings.get(column);

					if (id.equals(XLSReader.ID_COLUMN)) {
						box.setSelectedItem(XLSReader.ID_COLUMN);
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
					} else if (id.equals(TimeSeriesSchema.ATT_COMMENT)) {
						box.setSelectedItem(TimeSeriesSchema.ATT_COMMENT);
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
					} else if (id.equals(AttributeUtilities.TIME)) {
						box.setSelectedItem(AttributeUtilities.TIME);
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
					} else if (id.equals(AttributeUtilities.LOGC)) {
						box.setSelectedItem(AttributeUtilities.LOGC);
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
					} else if (id.equals(AttributeUtilities.ATT_TEMPERATURE_ID
							+ "")) {
						box.setSelectedItem(AttributeUtilities.ATT_TEMPERATURE);
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
					} else if (id.equals(AttributeUtilities.ATT_PH_ID + "")) {
						box.setSelectedItem(AttributeUtilities.ATT_PH);
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
					} else if (id.equals(AttributeUtilities.ATT_AW_ID + "")) {
						box.setSelectedItem(AttributeUtilities.ATT_WATERACTIVITY);
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
					} else {
						box.setSelectedItem(OTHER_PARAMETER);
						button.setEnabled(true);
						button.setText(DBKernel.getValue("SonstigeParameter",
								"ID", id, "Parameter") + "");
					}
				} else {
					box.setSelectedItem(DO_NOT_USE);
					button.setEnabled(false);
					button.setText(OTHER_PARAMETER);
				}

				box.addItemListener(this);
				button.addActionListener(this);
				columnBoxes.put(column, box);
				columnButtons.put(column, button);

				northPanel.add(new JLabel(column + ":"),
						createConstraints(0, row));
				northPanel.add(box, createConstraints(1, row));
				northPanel.add(button, createConstraints(2, row));
				row++;
			}

			JPanel panel = new JPanel();

			panel.setLayout(new BorderLayout());
			panel.add(northPanel, BorderLayout.NORTH);

			columnsPanel.removeAll();
			columnsPanel.add(new JScrollPane(panel), BorderLayout.CENTER);
		} else {
			columnsPanel.removeAll();
			columnsPanel.add(noLabel, BorderLayout.CENTER);
			columnButtons.clear();
		}
	}

	private Integer openMiscDBWindow(Integer id) {
		MyTable myT = DBKernel.myList.getTable("SonstigeParameter");
		Object newVal = DBKernel.myList.openNewWindow(myT, id,
				"SonstigeParameter", null, null, null, null, true);

		if (newVal instanceof Integer) {
			return (Integer) newVal;
		} else {
			return null;
		}
	}

	private Integer openAgentDBWindow(Integer id) {
		MyTable myT = DBKernel.myList.getTable("Agenzien");
		Object newVal = DBKernel.myList.openNewWindow(myT, id, "Agenzien",
				null, null, null, null, true);

		if (newVal instanceof Integer) {
			return (Integer) newVal;
		} else {
			return null;
		}
	}

	private Integer openMatrixDBWindow(Integer id) {
		MyTable myT = DBKernel.myList.getTable("Matrices");
		Object newVal = DBKernel.myList.openNewWindow(myT, id, "Matrices",
				null, null, null, null, true);

		if (newVal instanceof Integer) {
			return (Integer) newVal;
		} else {
			return null;
		}
	}

	private Integer openLiteratureDBWindow(Integer id) {
		MyTable myT = DBKernel.myList.getTable("Literatur");
		Object newVal = DBKernel.myList.openNewWindow(myT, id, "Literatur",
				null, null, null, null, true);

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
