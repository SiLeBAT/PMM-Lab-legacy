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
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.FilePanel;
import de.bund.bfr.knime.pmm.common.ui.FilePanel.FileListener;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.NumberConcentration;
import de.bund.bfr.knime.pmm.common.units.NumberContent;
import de.bund.bfr.knime.pmm.common.units.PH;
import de.bund.bfr.knime.pmm.common.units.Temperature;
import de.bund.bfr.knime.pmm.common.units.Time;
import de.bund.bfr.knime.pmm.common.units.WaterActivity;

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

	private XLSReader xlsReader;

	private JPanel mainPanel;

	private FilePanel filePanel;
	private JComboBox<String> sheetBox;
	private List<String> fileSheetList;
	private List<String> fileColumnList;

	private List<LiteratureItem> literature;
	private JButton addLiteratureButton;
	private JButton removeLiteratureButton;
	private JList<String> literatureList;
	private List<String> literatureData;

	private JPanel agentPanel;
	private JComboBox<String> agentBox;
	private JButton agentButton;
	private String agentColumn;
	private AgentXml agent;
	private Map<String, AgentXml> agentMappings;
	private Map<String, JButton> agentButtons;

	private JPanel matrixPanel;
	private JComboBox<String> matrixBox;
	private JButton matrixButton;
	private String matrixColumn;
	private MatrixXml matrix;
	private Map<String, MatrixXml> matrixMappings;
	private Map<String, JButton> matrixButtons;

	private JPanel columnsPanel;
	private Map<String, JComboBox<String>> columnBoxes;
	private Map<String, JButton> columnButtons;
	private Map<String, JComboBox<String>> columnUnitBoxes;
	private Map<String, Object> columnMappings;
	private String timeUnit;
	private String concentrationUnit;

	private JLabel noLabel;

	/**
	 * New pane for configuring the XLSTimeSeriesReader node.
	 */
	protected XLSTimeSeriesReaderNodeDialog() {
		xlsReader = new XLSReader();

		filePanel = new FilePanel("XLS File", FilePanel.OPEN_DIALOG);
		filePanel.setAcceptAllFiles(false);
		filePanel.addFileFilter(".xls", "Excel Spreadsheat (*.xls)");
		filePanel.addFileListener(this);
		sheetBox = new JComboBox<>();
		sheetBox.addItemListener(this);
		fileSheetList = new ArrayList<>();
		fileColumnList = new ArrayList<>();

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
						.getName(TimeSeriesSchema.ATT_AGENT)));
		agentPanel.setLayout(new BorderLayout());
		agentPanel.add(noLabel, BorderLayout.CENTER);
		agentButtons = new LinkedHashMap<>();
		agentMappings = new LinkedHashMap<>();
		matrixPanel = new JPanel();
		matrixPanel.setBorder(BorderFactory
				.createTitledBorder(AttributeUtilities
						.getName(TimeSeriesSchema.ATT_MATRIX)));
		matrixPanel.setLayout(new BorderLayout());
		matrixPanel.add(noLabel, BorderLayout.CENTER);
		matrixButtons = new LinkedHashMap<>();
		matrixMappings = new LinkedHashMap<>();
		columnsPanel = new JPanel();
		columnsPanel.setBorder(BorderFactory
				.createTitledBorder("XLS Column -> PMM-Lab assignments"));
		columnsPanel.setLayout(new BorderLayout());
		columnsPanel.add(noLabel, BorderLayout.CENTER);
		columnBoxes = new LinkedHashMap<>();
		columnButtons = new LinkedHashMap<>();
		columnUnitBoxes = new LinkedHashMap<>();
		columnMappings = new LinkedHashMap<>();

		JPanel northLiteraturePanel = new JPanel();

		northLiteraturePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		northLiteraturePanel.add(addLiteratureButton);
		northLiteraturePanel.add(removeLiteratureButton);

		JPanel literaturePanel = new JPanel();

		literaturePanel.setBorder(BorderFactory
				.createTitledBorder("Literature"));
		literaturePanel.setLayout(new BorderLayout());
		literaturePanel.add(northLiteraturePanel, BorderLayout.NORTH);
		literaturePanel.add(new JScrollPane(literatureList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		JPanel optionsPanel = new JPanel();

		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
		optionsPanel.add(literaturePanel);
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
			fileSheetList = xlsReader.getSheets(new File(filePanel
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
			fileColumnList = xlsReader.getColumns(
					new File(filePanel.getFileName()),
					(String) sheetBox.getSelectedItem());
		} catch (Exception e) {
			fileColumnList = new ArrayList<>();
		}

		try {
			columnMappings = XmlConverter
					.xmlToObject(
							settings.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_COLUMNMAPPINGS),
							new LinkedHashMap<String, Object>());
		} catch (InvalidSettingsException e) {
			columnMappings = new LinkedHashMap<>();
		}

		try {
			timeUnit = settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_TIMEUNIT);
		} catch (InvalidSettingsException e) {
			timeUnit = null;
		}

		try {
			concentrationUnit = settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_CONCENTRATIONUNIT);
		} catch (InvalidSettingsException e) {
			concentrationUnit = null;
		}

		try {
			agent = XmlConverter
					.xmlToObject(
							settings.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENT),
							null);
		} catch (InvalidSettingsException e) {
			agent = null;
		}

		try {
			agentColumn = settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTCOLUMN);

			if (agentColumn == null) {
				if (agent != null) {
					agentColumn = OTHER_PARAMETER;
				} else {
					agentColumn = DO_NOT_USE;
				}
			}
		} catch (InvalidSettingsException e) {
			agentColumn = DO_NOT_USE;
		}

		try {
			agentMappings = XmlConverter
					.xmlToObject(
							settings.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTMAPPINGS),
							new LinkedHashMap<String, AgentXml>());
		} catch (InvalidSettingsException e) {
			agentMappings = new LinkedHashMap<>();
		}

		try {
			matrix = XmlConverter.xmlToObject(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIX),
					null);
		} catch (InvalidSettingsException e) {
			matrix = null;
		}

		try {
			matrixColumn = settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXCOLUMN);

			if (matrixColumn == null) {
				if (matrix != null) {
					matrixColumn = OTHER_PARAMETER;
				} else {
					matrixColumn = DO_NOT_USE;
				}
			}
		} catch (InvalidSettingsException e) {
			matrixColumn = DO_NOT_USE;
		}

		try {
			matrixMappings = XmlConverter
					.xmlToObject(
							settings.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXMAPPINGS),
							new LinkedHashMap<String, MatrixXml>());
		} catch (InvalidSettingsException e) {
			matrixMappings = new LinkedHashMap<>();
		}

		try {
			literature = XmlConverter.xmlToObject(settings
					.getString(XLSTimeSeriesReaderNodeModel.CFGKEY_LITERATURE),
					new ArrayList<LiteratureItem>());
			literatureData = new ArrayList<>();

			for (LiteratureItem item : literature) {
				literatureData.add(item.getAuthor() + "-" + item.getYear());
			}

			literatureList.setListData(literatureData.toArray(new String[0]));
		} catch (InvalidSettingsException e) {
			literature = new ArrayList<>();
			literatureData = new ArrayList<>();
			literatureList.setListData(literatureData.toArray(new String[0]));
		}

		updateAgentPanel();
		updateMatrixPanel();
		updateColumnsPanel();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		cleanMaps();

		if (filePanel.getFileName() == null) {
			throw new InvalidSettingsException("No file is specfied");
		}

		if (sheetBox.getSelectedItem() == null) {
			throw new InvalidSettingsException("No sheet is selected");
		}

		if (fileColumnList.isEmpty()) {
			throw new InvalidSettingsException("Specified file is invalid");
		}

		if (agentBox.getSelectedItem().equals(OTHER_PARAMETER) && agent == null) {
			throw new InvalidSettingsException("No assignment for "
					+ TimeSeriesSchema.ATT_AGENT);
		}

		if (matrixBox.getSelectedItem().equals(OTHER_PARAMETER)
				&& matrix == null) {
			throw new InvalidSettingsException("No assignment for "
					+ TimeSeriesSchema.ATT_MATRIX);
		}

		boolean idColumnMissing = true;
		Set<Object> assignments = new LinkedHashSet<>();

		for (String column : columnMappings.keySet()) {
			Object assignment = columnMappings.get(column);

			if (assignment == null) {
				throw new InvalidSettingsException("Column \"" + column
						+ "\" has no assignment");
			} else if (columnMappings.get(column).equals(XLSReader.ID_COLUMN)) {
				idColumnMissing = false;
			}

			if (!assignments.add(assignment)) {
				String name = null;

				if (assignment instanceof MiscXml) {
					name = ((MiscXml) assignment).getName();
				} else if (assignment instanceof String) {
					name = (String) assignment;
				}

				throw new InvalidSettingsException("\"" + name
						+ "\" can only be assigned once");
			}
		}

		if (idColumnMissing) {
			throw new InvalidSettingsException("\"" + XLSReader.ID_COLUMN
					+ "\" is unassigned");
		}

		if (agentColumn.equals(OTHER_PARAMETER)) {
			settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENT,
					XmlConverter.objectToXml(agent));
		} else {
			settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENT, null);
		}

		if (matrixColumn.equals(OTHER_PARAMETER)) {
			settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIX,
					XmlConverter.objectToXml(matrix));
		} else {
			settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIX, null);
		}

		if (!agentColumn.equals(OTHER_PARAMETER)
				&& !agentColumn.equals(DO_NOT_USE)) {
			settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTCOLUMN,
					agentColumn);
		} else {
			settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTCOLUMN,
					null);
		}

		if (!matrixColumn.equals(OTHER_PARAMETER)
				&& !matrixColumn.equals(DO_NOT_USE)) {
			settings.addString(
					XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXCOLUMN,
					matrixColumn);
		} else {
			settings.addString(
					XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXCOLUMN, null);
		}

		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_FILENAME,
				filePanel.getFileName());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_SHEETNAME,
				(String) sheetBox.getSelectedItem());
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_COLUMNMAPPINGS,
				XmlConverter.objectToXml(columnMappings));
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_TIMEUNIT,
				timeUnit);
		settings.addString(
				XLSTimeSeriesReaderNodeModel.CFGKEY_CONCENTRATIONUNIT,
				concentrationUnit);
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_AGENTMAPPINGS,
				XmlConverter.objectToXml(agentMappings));
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_MATRIXMAPPINGS,
				XmlConverter.objectToXml(matrixMappings));
		settings.addString(XLSTimeSeriesReaderNodeModel.CFGKEY_LITERATURE,
				XmlConverter.objectToXml(literature));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == agentButton) {
			Integer id;

			if (agent != null) {
				id = DBKernel.openAgentDBWindow(agent.getID());
			} else {
				id = DBKernel.openAgentDBWindow(null);
			}

			if (id != null) {
				String name = DBKernel.getValue("Agenzien", "ID", id + "",
						"Agensname") + "";

				agent = new AgentXml(id, name, null, DBKernel.getLocalDBUUID());
				agentButton.setText(name);
			}
		} else if (e.getSource() == matrixButton) {
			Integer id;

			if (matrix != null) {
				id = DBKernel.openMatrixDBWindow(matrix.getID());
			} else {
				id = DBKernel.openMatrixDBWindow(null);
			}

			if (id != null) {
				String name = DBKernel.getValue("Matrices", "ID", id + "",
						"Matrixname") + "";

				matrix = new MatrixXml(id, name, null,
						DBKernel.getLocalDBUUID());
				matrixButton.setText(name);
			}
		} else if (e.getSource() == addLiteratureButton) {
			Integer id = DBKernel.openLiteratureDBWindow(null);
			Set<Integer> ids = new LinkedHashSet<>();

			for (LiteratureItem item : literature) {
				ids.add(item.getID());
			}

			if (id != null && !ids.contains(id)) {
				String author = DBKernel.getValue("Literatur", "ID", id + "",
						"Erstautor") + "";
				String year = DBKernel.getValue("Literatur", "ID", id + "",
						"Jahr") + "";
				String title = DBKernel.getValue("Literatur", "ID", id + "",
						"Titel") + "";
				String mAbstract = DBKernel.getValue("Literatur", "ID",
						id + "", "Abstract") + "";

				literature.add(new LiteratureItem(author, Integer
						.parseInt(year), title, mAbstract, id));
				literatureData.add(author + "-" + year);
				literatureList.setListData(literatureData
						.toArray(new String[0]));
			}
		} else if (e.getSource() == removeLiteratureButton) {
			if (literatureList.getSelectedIndices().length > 0) {
				int[] indices = literatureList.getSelectedIndices();

				Arrays.sort(indices);

				for (int i = indices.length - 1; i >= 0; i--) {
					literature.remove(indices[i]);
					literatureData.remove(indices[i]);
				}

				literatureList.setListData(literatureData
						.toArray(new String[0]));
			}
		} else {
			for (String value : agentButtons.keySet()) {
				if (e.getSource() == agentButtons.get(value)) {
					Integer id;

					if (agentMappings.get(value) != null) {
						id = DBKernel.openAgentDBWindow(agentMappings
								.get(value).getID());
					} else {
						id = DBKernel.openAgentDBWindow(null);
					}

					if (id != null) {
						String name = DBKernel.getValue("Agenzien", "ID", id
								+ "", "Agensname")
								+ "";

						agentButtons.get(value).setText(name);
						agentMappings.put(value, new AgentXml(id, name, null,
								DBKernel.getLocalDBUUID()));
					}

					break;
				}
			}

			for (String value : matrixButtons.keySet()) {
				if (e.getSource() == matrixButtons.get(value)) {
					Integer id;

					if (matrixMappings.get(value) != null) {
						id = DBKernel.openMatrixDBWindow(matrixMappings.get(
								value).getID());
					} else {
						id = DBKernel.openMatrixDBWindow(null);
					}

					if (id != null) {
						String name = DBKernel.getValue("Matrices", "ID", id
								+ "", "Matrixname")
								+ "";

						matrixButtons.get(value).setText(name);
						matrixMappings.put(value, new MatrixXml(id, name, null,
								DBKernel.getLocalDBUUID()));
					}

					break;
				}
			}

			for (String column : columnButtons.keySet()) {
				if (e.getSource() == columnButtons.get(column)) {
					Integer id;

					if (columnMappings.get(column) instanceof MiscXml) {
						id = DBKernel
								.openMiscDBWindow(((MiscXml) columnMappings
										.get(column)).getID());
					} else {
						id = DBKernel.openMiscDBWindow(null);
					}

					if (id != null) {
						String name = DBKernel.getValue("SonstigeParameter",
								"ID", id + "", "Parameter") + "";
						String description = DBKernel.getValue(
								"SonstigeParameter", "ID", id + "",
								"Beschreibung")
								+ "";
						List<String> categoryIDs = Arrays.asList(DBKernel
								.getValue("SonstigeParameter", "ID", id + "",
										"Kategorie").toString().split(","));
						String unit = null;

						if (!categoryIDs.isEmpty()) {
							unit = Categories.getCategory(categoryIDs.get(0))
									.getStandardUnit();
						}

						columnButtons.get(column).setText(name);
						columnMappings.put(
								column,
								new MiscXml(id, name, description, null,
										categoryIDs, unit, DBKernel
												.getLocalDBUUID()));
						updateColumnsPanel();
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
				fileColumnList = xlsReader.getColumns(
						new File(filePanel.getFileName()),
						(String) sheetBox.getSelectedItem());
			} catch (Exception ex) {
				fileColumnList = new ArrayList<>();
			}

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
					String selected = (String) columnBoxes.get(column)
							.getSelectedItem();

					if (selected.equals(XLSReader.ID_COLUMN)
							|| selected.equals(MdInfoXml.ATT_COMMENT)
							|| selected.equals(AttributeUtilities.TIME)
							|| selected.equals(AttributeUtilities.LOGC)
							|| selected
									.equals(AttributeUtilities.AGENT_DETAILS)
							|| selected
									.equals(AttributeUtilities.MATRIX_DETAILS)) {
						columnMappings.put(column, selected);
					} else if (selected
							.equals(AttributeUtilities.ATT_TEMPERATURE)) {
						columnMappings.put(column, new MiscXml(
								AttributeUtilities.ATT_TEMPERATURE_ID,
								AttributeUtilities.ATT_TEMPERATURE, null, null,
								Arrays.asList(Categories.TEMPERATURE),
								new Temperature().getStandardUnit()));
					} else if (selected.equals(AttributeUtilities.ATT_PH)) {
						columnMappings.put(
								column,
								new MiscXml(AttributeUtilities.ATT_PH_ID,
										AttributeUtilities.ATT_PH, null, null,
										Arrays.asList(Categories.PH), new PH()
												.getStandardUnit()));
					} else if (selected
							.equals(AttributeUtilities.ATT_WATERACTIVITY)) {
						columnMappings.put(column, new MiscXml(
								AttributeUtilities.ATT_AW_ID,
								AttributeUtilities.ATT_WATERACTIVITY, null,
								null, Arrays.asList(Categories.WATER_ACTIVITY),
								new WaterActivity().getStandardUnit()));
					} else if (selected.equals(OTHER_PARAMETER)) {
						columnMappings.put(column, null);
					} else if (selected.equals(DO_NOT_USE)) {
						columnMappings.remove(column);
					}

					updateColumnsPanel();
					break;
				}
			}

			for (String column : columnUnitBoxes.keySet()) {
				if (e.getSource() == columnUnitBoxes.get(column)) {
					String unit = (String) columnUnitBoxes.get(column)
							.getSelectedItem();

					if (columnMappings.get(column) instanceof MiscXml) {
						MiscXml condition = (MiscXml) columnMappings
								.get(column);

						condition.setUnit(unit);
					} else if (columnMappings.get(column) instanceof String) {
						String mapping = (String) columnMappings.get(column);

						if (mapping.equals(AttributeUtilities.TIME)) {
							timeUnit = unit;
						} else if (mapping.equals(AttributeUtilities.LOGC)) {
							concentrationUnit = unit;
						}
					}

					break;
				}
			}
		}
	}

	@Override
	public void fileChanged(FilePanel source) {
		try {
			fileSheetList = xlsReader.getSheets(new File(filePanel
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
			fileColumnList = xlsReader.getColumns(
					new File(filePanel.getFileName()),
					(String) sheetBox.getSelectedItem());
		} catch (Exception e) {
			fileColumnList = new ArrayList<>();
		}

		updateColumnsPanel();
		updateAgentPanel();
		updateMatrixPanel();
		mainPanel.revalidate();
	}

	private void updateAgentPanel() {
		agentButtons.clear();
		agentBox = new JComboBox<>(new String[] { DO_NOT_USE, OTHER_PARAMETER });
		agentButton = new JButton(OTHER_PARAMETER);

		for (String column : fileColumnList) {
			agentBox.addItem(column);
		}

		agentBox.setSelectedItem(agentColumn);

		if (agent != null) {
			agentButton.setText(agent.getName());
		} else {
			agentButton.setText(OTHER_PARAMETER);
		}

		agentBox.addItemListener(this);
		agentButton.addActionListener(this);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new GridBagLayout());
		northPanel.add(new JLabel("XLS Column:"), createConstraints(0, 0));
		northPanel.add(agentBox, createConstraints(1, 0));

		if (agentBox.getSelectedItem().equals(DO_NOT_USE)) {
			// Do nothing
		} else if (agentBox.getSelectedItem().equals(OTHER_PARAMETER)) {
			northPanel.add(agentButton, createConstraints(1, 1));
		} else {
			int row = 1;
			String column = (String) agentBox.getSelectedItem();

			try {
				Set<String> values = xlsReader.getValuesInColumn(new File(
						filePanel.getFileName()), (String) sheetBox
						.getSelectedItem(), column);

				for (String value : values) {
					JButton button = new JButton();

					if (agentMappings.containsKey(value)) {
						button.setText(agentMappings.get(value).getName());
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
		matrixButtons.clear();
		matrixBox = new JComboBox<>(
				new String[] { DO_NOT_USE, OTHER_PARAMETER });
		matrixButton = new JButton(OTHER_PARAMETER);

		for (String column : fileColumnList) {
			matrixBox.addItem(column);
		}

		matrixBox.setSelectedItem(matrixColumn);

		if (matrix != null) {
			matrixButton.setText(matrix.getName());
		} else {
			matrixButton.setText(OTHER_PARAMETER);
		}

		matrixBox.addItemListener(this);
		matrixButton.addActionListener(this);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new GridBagLayout());
		northPanel.add(new JLabel("XLS Column:"), createConstraints(0, 0));
		northPanel.add(matrixBox, createConstraints(1, 0));

		if (matrixBox.getSelectedItem().equals(DO_NOT_USE)) {
			// Do nothing
		} else if (matrixBox.getSelectedItem().equals(OTHER_PARAMETER)) {
			northPanel.add(matrixButton, createConstraints(1, 1));
		} else {
			int row = 1;
			String column = (String) matrixBox.getSelectedItem();

			try {
				Set<String> values = xlsReader.getValuesInColumn(new File(
						filePanel.getFileName()), (String) sheetBox
						.getSelectedItem(), column);

				for (String value : values) {
					JButton button = new JButton();

					if (matrixMappings.containsKey(value)) {
						button.setText(matrixMappings.get(value).getName());
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
			columnUnitBoxes.clear();

			JPanel northPanel = new JPanel();
			int row = 0;

			northPanel.setLayout(new GridBagLayout());

			for (String column : fileColumnList) {
				JComboBox<String> box = new JComboBox<>(new String[] {
						XLSReader.ID_COLUMN, MdInfoXml.ATT_COMMENT,
						AttributeUtilities.TIME, AttributeUtilities.LOGC,
						AttributeUtilities.AGENT_DETAILS,
						AttributeUtilities.MATRIX_DETAILS,
						AttributeUtilities.ATT_TEMPERATURE,
						AttributeUtilities.ATT_PH,
						AttributeUtilities.ATT_WATERACTIVITY, OTHER_PARAMETER,
						DO_NOT_USE });
				JButton button = new JButton();

				if (columnMappings.containsKey(column)) {
					Object mapping = columnMappings.get(column);

					if (mapping == null) {
						box.setSelectedItem(OTHER_PARAMETER);
						button.setEnabled(true);
						button.setText(OTHER_PARAMETER);
					} else if (mapping instanceof String) {
						box.setSelectedItem(mapping);
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
					} else if (mapping instanceof MiscXml) {
						int id = ((MiscXml) mapping).getID();

						if (id == AttributeUtilities.ATT_TEMPERATURE_ID) {
							box.setSelectedItem(AttributeUtilities.ATT_TEMPERATURE);
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
						} else if (id == AttributeUtilities.ATT_PH_ID) {
							box.setSelectedItem(AttributeUtilities.ATT_PH);
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
						} else if (id == AttributeUtilities.ATT_AW_ID) {
							box.setSelectedItem(AttributeUtilities.ATT_WATERACTIVITY);
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
						} else {
							box.setSelectedItem(OTHER_PARAMETER);
							button.setEnabled(true);
							button.setText(((MiscXml) mapping).getName());
						}
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

				if (columnMappings.get(column) instanceof MiscXml) {
					MiscXml condition = (MiscXml) columnMappings.get(column);
					List<String> allUnits = new ArrayList<>();

					for (String cat : condition.getCategories()) {
						allUnits.addAll(Categories.getCategory(cat)
								.getAllUnits());
					}

					JComboBox<String> unitBox = new JComboBox<>(
							allUnits.toArray(new String[0]));

					if (condition.getUnit() != null) {
						unitBox.setSelectedItem(condition.getUnit());
					}

					unitBox.addItemListener(this);
					columnUnitBoxes.put(column, unitBox);
					northPanel.add(unitBox, createConstraints(3, row));
				} else if (columnMappings.get(column) instanceof String) {
					String mapping = (String) columnMappings.get(column);

					if (mapping.equals(AttributeUtilities.TIME)) {
						JComboBox<String> unitBox = new JComboBox<>(new Time()
								.getAllUnits().toArray(new String[0]));

						if (timeUnit == null) {
							timeUnit = new Time().getStandardUnit();
						}

						unitBox.setSelectedItem(timeUnit);
						unitBox.addItemListener(this);
						columnUnitBoxes.put(column, unitBox);
						northPanel.add(unitBox, createConstraints(3, row));
					} else if (mapping.equals(AttributeUtilities.LOGC)) {
						List<String> units = new ArrayList<>();

						units.addAll(new NumberContent().getAllUnits());
						units.addAll(new NumberConcentration().getAllUnits());

						JComboBox<String> unitBox = new JComboBox<>(
								units.toArray(new String[0]));

						if (concentrationUnit == null) {
							concentrationUnit = new NumberContent()
									.getStandardUnit();
						}

						unitBox.setSelectedItem(concentrationUnit);
						unitBox.addItemListener(this);
						columnUnitBoxes.put(column, unitBox);
						northPanel.add(unitBox, createConstraints(3, row));
					}
				}

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

	private GridBagConstraints createConstraints(int x, int y) {
		return new GridBagConstraints(x, y, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
	}

	private void cleanMaps() {
		Map<String, AgentXml> newAgentMappings = new LinkedHashMap<>();
		Map<String, MatrixXml> newMatrixMappings = new LinkedHashMap<>();
		Map<String, Object> newColumnMappings = new LinkedHashMap<>();

		for (String agent : agentButtons.keySet()) {
			newAgentMappings.put(agent, agentMappings.get(agent));
		}

		for (String matrix : matrixButtons.keySet()) {
			newMatrixMappings.put(matrix, matrixMappings.get(matrix));
		}

		for (String column : fileColumnList) {
			if (columnMappings.containsKey(column)) {
				newColumnMappings.put(column, columnMappings.get(column));
			}
		}

		agentMappings = newAgentMappings;
		matrixMappings = newMatrixMappings;
		columnMappings = newColumnMappings;
	}

}
