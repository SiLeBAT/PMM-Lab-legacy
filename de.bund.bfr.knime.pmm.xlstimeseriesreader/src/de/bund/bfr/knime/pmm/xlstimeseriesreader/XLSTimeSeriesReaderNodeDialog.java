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
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
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
	private static final String SELECT = "Select";

	private FilePanel filePanel;

	private JComboBox<String> timeBox;
	private JComboBox<String> logcBox;
	private JComboBox<String> tempBox;

	private JButton agentButton;
	private JButton matrixButton;
	private Integer agentID;
	private Integer matrixID;

	private JButton addLiteratureButton;
	private JButton removeLiteratureButton;
	private JList<String> literatureList;
	private List<Integer> literatureIDs;
	private List<String> literatureData;

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

		agentButton = new JButton(SELECT);
		agentButton.addActionListener(this);
		matrixButton = new JButton(SELECT);
		matrixButton.addActionListener(this);

		addLiteratureButton = new JButton("Add");
		addLiteratureButton.addActionListener(this);
		removeLiteratureButton = new JButton("Remove");
		removeLiteratureButton.addActionListener(this);
		literatureList = new JList<>();

		noLabel = new JLabel();
		noLabel.setPreferredSize(new Dimension(100, 50));
		columnsPanel = new JPanel();
		columnsPanel.setBorder(BorderFactory.createTitledBorder("Columns"));
		columnsPanel.setLayout(new BorderLayout());
		columnsPanel.add(noLabel, BorderLayout.CENTER);
		mappingBoxes = new LinkedHashMap<>();
		mappingButtons = new LinkedHashMap<>();
		mappingIDs = new LinkedHashMap<>();

		JPanel northUnitsPanel = new JPanel();

		northUnitsPanel.setLayout(new GridBagLayout());
		northUnitsPanel.add(
				new JLabel(AttributeUtilities
						.getFullName(TimeSeriesSchema.TIME) + ":"),
				createConstraints(0, 0));
		northUnitsPanel.add(
				new JLabel(AttributeUtilities
						.getFullName(TimeSeriesSchema.LOGC) + ":"),
				createConstraints(0, 1));
		northUnitsPanel
				.add(new JLabel(AttributeUtilities
						.getFullName(AttributeUtilities.ATT_TEMPERATURE) + ":"),
						createConstraints(0, 2));
		northUnitsPanel.add(timeBox, createConstraints(1, 0));
		northUnitsPanel.add(logcBox, createConstraints(1, 1));
		northUnitsPanel.add(tempBox, createConstraints(1, 2));

		JPanel unitsPanel = new JPanel();

		unitsPanel.setBorder(BorderFactory.createTitledBorder("Units"));
		unitsPanel.setLayout(new BorderLayout());
		unitsPanel.add(northUnitsPanel, BorderLayout.WEST);

		JPanel northInfoPanel = new JPanel();

		northInfoPanel.setLayout(new GridBagLayout());
		northInfoPanel.add(
				new JLabel(AttributeUtilities
						.getFullName(TimeSeriesSchema.ATT_AGENTNAME) + ":"),
				createConstraints(0, 0));
		northInfoPanel.add(agentButton, createConstraints(1, 0));
		northInfoPanel.add(
				new JLabel(AttributeUtilities
						.getFullName(TimeSeriesSchema.ATT_MATRIXNAME) + ":"),
				createConstraints(0, 1));
		northInfoPanel.add(matrixButton, createConstraints(1, 1));

		JPanel infoPanel = new JPanel();

		infoPanel.setBorder(BorderFactory.createTitledBorder("Info"));
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(northInfoPanel, BorderLayout.NORTH);

		JPanel unitInfoPanel = new JPanel();

		unitInfoPanel.setLayout(new BorderLayout());
		unitInfoPanel.add(unitsPanel, BorderLayout.NORTH);
		unitInfoPanel.add(infoPanel, BorderLayout.CENTER);

		JPanel northLiteraturePanel = new JPanel();

		northLiteraturePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		northLiteraturePanel.add(addLiteratureButton);
		northLiteraturePanel.add(removeLiteratureButton);

		JPanel literaturePanel = new JPanel();

		literaturePanel.setBorder(BorderFactory
				.createTitledBorder("Literature"));
		literaturePanel.setLayout(new BorderLayout());
		literaturePanel.add(northLiteraturePanel, BorderLayout.NORTH);
		literaturePanel.add(literatureList, BorderLayout.CENTER);

		JPanel optionsPanel = new JPanel();

		optionsPanel.setLayout(new BorderLayout());
		optionsPanel.add(unitInfoPanel, BorderLayout.WEST);
		optionsPanel.add(literaturePanel, BorderLayout.CENTER);
		optionsPanel.add(columnsPanel, BorderLayout.EAST);

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

		try {
			int agentID = settings
					.getInt(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTID);

			if (agentID != XLSTimeSeriesReaderNodeModel.DEFAULT_AGENTID) {
				agentButton.setText(""
						+ DBKernel.getValue("Agenzien", "ID", agentID + "",
								"Agensname"));
				this.agentID = agentID;
			} else {
				agentButton.setText(SELECT);
				this.agentID = null;
			}
		} catch (InvalidSettingsException e) {
			agentButton.setText(SELECT);
			agentID = null;
		}

		try {
			int matrixID = settings
					.getInt(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXID);

			if (matrixID != XLSTimeSeriesReaderNodeModel.DEFAULT_MATRIXID) {
				matrixButton.setText(""
						+ DBKernel.getValue("Matrices", "ID", matrixID + "",
								"Matrixname"));
				this.matrixID = matrixID;
			} else {
				matrixButton.setText(SELECT);
				this.matrixID = null;
			}
		} catch (InvalidSettingsException e) {
			matrixButton.setText(SELECT);
			matrixID = null;
		}

		try {
			literatureIDs = ListUtilities
					.getIntListFromString(settings
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

		if (agentID == null || matrixID == null) {
			throw new InvalidSettingsException("");
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
		settings.addInt(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTID, agentID);
		settings.addInt(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXID, matrixID);
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_LITERATUREIDS,
				ListUtilities.getStringFromList(literatureIDs));
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
			for (String column : mappingButtons.keySet()) {
				if (e.getSource() == mappingButtons.get(column)) {
					Integer miscID = openMiscDBWindow(mappingIDs.get(column));

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
