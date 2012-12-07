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
package de.bund.bfr.knime.pmm.dataedit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.IntTextField;
import de.bund.bfr.knime.pmm.common.ui.SpacePanel;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;
import de.bund.bfr.knime.pmm.common.ui.TextListener;

/**
 * <code>NodeDialog</code> for the "DataEdit" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens, Joergen Brandt
 */
public class DataEditNodeDialog extends DataAwareNodeDialogPane implements
		ActionListener, TextListener {

	private KnimeSchema schema;
	private BufferedDataTable table;

	private boolean dataValid;
	private Map<Integer, List<String>> dataChanges;

	private List<Integer> idList;
	private List<String> nameList;
	private List<KnimeTuple> tuples;
	private List<KnimeTuple> oldTuples;

	private JComboBox<String> idBox;
	private JButton deleteRecordButton;
	private IntTextField condIDField;
	private Map<String, StringTextField> stringColumnFields;
	private Map<String, IntTextField> intColumnFields;
	private Map<String, DoubleTextField> doubleColumnFields;

	private JPanel miscPanel;
	private JPanel miscButtonsPanel;
	private List<IntTextField> miscIDFields;
	private List<StringTextField> miscNameFields;
	private List<StringTextField> miscDescriptionFields;
	private List<DoubleTextField> miscValueFields;
	private List<StringTextField> miscUnitFields;
	private List<JButton> miscRemoveButtons;
	private List<JButton> miscAddButtons;
	private List<JLabel> miscEmptyLabels;
	private JPanel timeSeriesPanel;
	private JPanel timeSeriesButtonsPanel;
	private List<DoubleTextField> timeFields;
	private List<DoubleTextField> logcFields;
	private List<JButton> removeButtons;
	private List<JButton> addButtons;
	private List<JLabel> emptyLabels;

	private JButton clearButton;

	/**
	 * New pane for configuring DataEdit node dialog. This is just a suggestion
	 * to demonstrate possible default dialog components.
	 */
	protected DataEditNodeDialog() {
		JPanel panel = new JPanel();

		schema = new TimeSeriesSchema();
		dataValid = true;
		dataChanges = new LinkedHashMap<Integer, List<String>>();
		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		table = input[0];
		dataChanges.clear();

		try {
			List<String> changes = new ArrayList<String>(Arrays.asList(settings
					.getStringArray(DataEditNodeModel.CFG_DATACHANGES)));

			for (String c : changes) {
				int i = c.indexOf(":");
				int id = Integer.parseInt(c.substring(0, i));
				String change = c.substring(i + 1);

				if (!dataChanges.containsKey(id)) {
					dataChanges.put(id, new ArrayList<String>());
				}

				dataChanges.get(id).add(change);
			}
		} catch (InvalidSettingsException e) {
		}

		try {
			readTable(table);
		} catch (PmmException e) {
			e.printStackTrace();
		}

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainPanel());
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!dataValid) {
			throw new InvalidSettingsException("");
		}

		List<String> changes = new ArrayList<String>();

		for (int id : dataChanges.keySet()) {
			for (String change : dataChanges.get(id)) {
				changes.add(id + ":" + change);
			}
		}

		settings.addStringArray(DataEditNodeModel.CFG_DATACHANGES,
				changes.toArray(new String[0]));
	}

	private void readTable(BufferedDataTable table) throws PmmException {
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);

		idList = new ArrayList<Integer>();
		nameList = new ArrayList<String>();
		tuples = new ArrayList<KnimeTuple>();
		oldTuples = new ArrayList<KnimeTuple>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			int id = tuple.getInt(TimeSeriesSchema.ATT_CONDID);

			tuples.add(tuple);

			if (!dataChanges.containsKey(id)
					|| dataChanges.get(id).size() != 1
					|| !dataChanges.get(id).get(0)
							.equals(DataEditNodeModel.DELETED)) {
				idList.add(id);
				nameList.add(id + "");
			}
		}

		tuples = DataEditNodeModel.performChanges(tuples, dataChanges);

		for (KnimeTuple tuple : tuples) {
			oldTuples.add(new KnimeTuple(schema, schema.createSpec(), tuple));
		}
	}

	private JPanel createMainPanel() {
		idBox = new JComboBox<String>(nameList.toArray(new String[0]));
		idBox.setSelectedIndex(0);
		idBox.addActionListener(this);
		deleteRecordButton = new JButton("Delete Record");
		deleteRecordButton.addActionListener(this);
		condIDField = new IntTextField();
		condIDField.setEditable(false);
		stringColumnFields = new LinkedHashMap<String, StringTextField>();
		intColumnFields = new LinkedHashMap<String, IntTextField>();
		doubleColumnFields = new LinkedHashMap<String, DoubleTextField>();

		// ------------------------------------------------------------------ //

		JPanel idPanel = new JPanel();

		idPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		idPanel.add(new JLabel("Original ID:"));
		idPanel.add(idBox);
		idPanel.add(deleteRecordButton);

		// ------------------------------------------------------------------ //

		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();

		leftPanel.setLayout(new GridLayout(0, 1, 5, 5));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftPanel.add(new JLabel(TimeSeriesSchema.ATT_CONDID + ":"));
		rightPanel.setLayout(new GridLayout(0, 1, 5, 5));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel.add(condIDField);

		for (int i = 0; i < schema.size(); i++) {
			int type = schema.getType(i);
			String name = schema.getName(i);
			String fullName = AttributeUtilities.getFullNameWithUnit(name);

			if (type == KnimeAttribute.TYPE_STRING
					&& !name.equals(TimeSeriesSchema.ATT_COMBASEID)) {
				stringColumnFields.put(name, new StringTextField(true));
				leftPanel.add(new JLabel(fullName + ":"));
				rightPanel.add(stringColumnFields.get(name));
			} else if (type == KnimeAttribute.TYPE_INT
					&& !name.equals(TimeSeriesSchema.ATT_CONDID)) {
				intColumnFields.put(name, new IntTextField(true));
				leftPanel.add(new JLabel(fullName + ":"));
				rightPanel.add(intColumnFields.get(name));
			} else if (type == KnimeAttribute.TYPE_DOUBLE) {
				doubleColumnFields.put(name, new DoubleTextField(true));
				leftPanel.add(new JLabel(fullName + ":"));
				rightPanel.add(doubleColumnFields.get(name));
			}
		}

		// ------------------------------------------------------------------ //

		JPanel upperPanel = new JPanel();

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(new SpacePanel(idPanel), BorderLayout.NORTH);
		upperPanel.add(leftPanel, BorderLayout.WEST);
		upperPanel.add(rightPanel, BorderLayout.CENTER);

		// ------------------------------------------------------------------ //

		JPanel centerPanel1 = new JPanel();
		JPanel tablePanel1 = new JPanel();
		JLabel idLabel = new JLabel("ID");
		JLabel nameLabel = new JLabel("Name");
		JLabel descriptionLabel = new JLabel("Description");
		JLabel valueLabel = new JLabel("Value");
		JLabel unitLabel = new JLabel("Unit");

		idLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		unitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		miscPanel = new JPanel();
		miscPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		miscPanel.setLayout(new GridLayout(0, 5, 5, 5));
		miscPanel.add(idLabel);
		miscPanel.add(nameLabel);
		miscPanel.add(descriptionLabel);
		miscPanel.add(valueLabel);
		miscPanel.add(unitLabel);
		miscButtonsPanel = new JPanel();
		miscButtonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		miscButtonsPanel.setLayout(new GridLayout(0, 2, 5, 5));
		miscButtonsPanel.add(new JLabel());
		miscButtonsPanel.add(new JLabel());
		tablePanel1.setLayout(new BorderLayout());
		tablePanel1.add(miscPanel, BorderLayout.CENTER);
		tablePanel1.add(miscButtonsPanel, BorderLayout.EAST);
		centerPanel1.setLayout(new BorderLayout());
		centerPanel1.add(tablePanel1, BorderLayout.NORTH);

		// ------------------------------------------------------------------ //

		JPanel centerPanel2 = new JPanel();
		JPanel tablePanel2 = new JPanel();
		JLabel timeLabel = new JLabel(
				AttributeUtilities.getFullNameWithUnit(TimeSeriesSchema.TIME));
		JLabel logcLabel = new JLabel(
				AttributeUtilities.getFullNameWithUnit(TimeSeriesSchema.LOGC));

		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logcLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timeSeriesPanel = new JPanel();
		timeSeriesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		timeSeriesPanel.setLayout(new GridLayout(0, 2, 5, 5));
		timeSeriesPanel.add(timeLabel);
		timeSeriesPanel.add(logcLabel);
		timeSeriesButtonsPanel = new JPanel();
		timeSeriesButtonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5,
				5, 5));
		timeSeriesButtonsPanel.setLayout(new GridLayout(0, 2, 5, 5));
		timeSeriesButtonsPanel.add(new JLabel());
		timeSeriesButtonsPanel.add(new JLabel());
		tablePanel2.setLayout(new BorderLayout());
		tablePanel2.add(timeSeriesPanel, BorderLayout.CENTER);
		tablePanel2.add(timeSeriesButtonsPanel, BorderLayout.EAST);
		centerPanel2.setLayout(new BorderLayout());
		centerPanel2.add(tablePanel2, BorderLayout.NORTH);

		// ------------------------------------------------------------------ //

		JSplitPane centerPanel = new JSplitPane();

		centerPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		centerPanel.setTopComponent(new JScrollPane(centerPanel1,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		centerPanel.setBottomComponent(new JScrollPane(centerPanel2,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		centerPanel.setResizeWeight(0.5);

		// ------------------------------------------------------------------ //

		JPanel bottomPanel = new JPanel();

		clearButton = new JButton("Clear Changes");
		clearButton.addActionListener(this);
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(clearButton);

		// ------------------------------------------------------------------ //

		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(upperPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		// ------------------------------------------------------------------ //

		miscIDFields = new ArrayList<IntTextField>();
		miscNameFields = new ArrayList<StringTextField>();
		miscDescriptionFields = new ArrayList<StringTextField>();
		miscValueFields = new ArrayList<DoubleTextField>();
		miscUnitFields = new ArrayList<StringTextField>();
		miscRemoveButtons = new ArrayList<JButton>();
		miscAddButtons = new ArrayList<JButton>();
		miscEmptyLabels = new ArrayList<JLabel>();
		timeFields = new ArrayList<DoubleTextField>();
		logcFields = new ArrayList<DoubleTextField>();
		removeButtons = new ArrayList<JButton>();
		addButtons = new ArrayList<JButton>();
		emptyLabels = new ArrayList<JLabel>();
		addDocumentListeners();

		try {
			updateTextFields();
		} catch (PmmException e) {
			e.printStackTrace();
		}

		return mainPanel;
	}

	private void updateTextFields() throws PmmException {
		int index = idBox.getSelectedIndex();

		condIDField.setValue(tuples.get(index).getInt(
				TimeSeriesSchema.ATT_CONDID));
		removeDocumentListeners();

		for (String column : stringColumnFields.keySet()) {
			String value = tuples.get(index).getString(column);

			stringColumnFields.get(column).setValue(value);
		}

		for (String column : intColumnFields.keySet()) {
			Integer value = tuples.get(index).getInt(column);

			intColumnFields.get(column).setValue(value);
		}

		for (String column : doubleColumnFields.keySet()) {
			Double value = tuples.get(index).getDouble(column);

			doubleColumnFields.get(column).setValue(value);
		}

		// ------------------------------------------------------------------ //

		for (IntTextField field : miscIDFields) {
			miscPanel.remove(field);
		}

		for (StringTextField field : miscNameFields) {
			miscPanel.remove(field);
		}

		for (StringTextField field : miscDescriptionFields) {
			miscPanel.remove(field);
		}

		for (DoubleTextField field : miscValueFields) {
			miscPanel.remove(field);
		}

		for (StringTextField field : miscUnitFields) {
			miscPanel.remove(field);
		}

		for (JButton button : miscRemoveButtons) {
			miscButtonsPanel.remove(button);
		}

		for (JButton button : miscAddButtons) {
			miscButtonsPanel.remove(button);
		}

		for (JLabel label : miscEmptyLabels) {
			miscPanel.remove(label);
			miscButtonsPanel.remove(label);
		}

		miscIDFields.clear();
		miscNameFields.clear();
		miscDescriptionFields.clear();
		miscValueFields.clear();
		miscUnitFields.clear();
		miscAddButtons.clear();
		miscRemoveButtons.clear();
		miscEmptyLabels.clear();

		PmmXmlDoc miscXml = tuples.get(index).getPmmXml(
				TimeSeriesSchema.ATT_MISC);

		for (PmmXmlElementConvertable el : miscXml.getElementSet()) {
			MiscXml element = (MiscXml) el;
			IntTextField idField = new IntTextField();
			StringTextField nameField = new StringTextField();
			StringTextField descriptionField = new StringTextField(true);
			DoubleTextField valueField = new DoubleTextField(true);
			StringTextField unitField = new StringTextField(true);
			JButton removebutton = new JButton("-");
			JButton addButton = new JButton("+");

			idField.setValue(element.getID());
			idField.setPreferredSize(new Dimension(100, idField
					.getPreferredSize().height));
			idField.setEditable(false);
			nameField.setValue(element.getName());
			nameField.setPreferredSize(new Dimension(100, nameField
					.getPreferredSize().height));
			descriptionField.setValue(element.getDescription());
			descriptionField.setPreferredSize(new Dimension(100,
					descriptionField.getPreferredSize().height));
			valueField.setValue(element.getValue());
			valueField.setPreferredSize(new Dimension(100, valueField
					.getPreferredSize().height));
			unitField.setValue(element.getUnit());
			unitField.setPreferredSize(new Dimension(100, unitField
					.getPreferredSize().height));

			miscPanel.add(idField);
			miscPanel.add(nameField);
			miscPanel.add(descriptionField);
			miscPanel.add(valueField);
			miscPanel.add(unitField);
			miscButtonsPanel.add(removebutton);
			miscButtonsPanel.add(addButton);
			miscIDFields.add(idField);
			miscNameFields.add(nameField);
			miscDescriptionFields.add(descriptionField);
			miscValueFields.add(valueField);
			miscUnitFields.add(unitField);
			miscRemoveButtons.add(removebutton);
			miscAddButtons.add(addButton);
			removebutton.addActionListener(this);
			addButton.addActionListener(this);
		}

		JLabel miscLabel1 = new JLabel();
		JLabel miscLabel2 = new JLabel();
		JLabel miscLabel3 = new JLabel();
		JLabel miscLabel4 = new JLabel();
		JLabel miscLabel5 = new JLabel();
		JLabel miscLabel6 = new JLabel();
		JButton miscAddButton = new JButton("+");

		miscPanel.add(miscLabel1);
		miscPanel.add(miscLabel2);
		miscPanel.add(miscLabel3);
		miscPanel.add(miscLabel4);
		miscPanel.add(miscLabel5);
		miscButtonsPanel.add(miscLabel6);
		miscButtonsPanel.add(miscAddButton);
		miscEmptyLabels.add(miscLabel1);
		miscEmptyLabels.add(miscLabel2);
		miscEmptyLabels.add(miscLabel3);
		miscEmptyLabels.add(miscLabel4);
		miscEmptyLabels.add(miscLabel5);
		miscEmptyLabels.add(miscLabel6);
		miscAddButtons.add(miscAddButton);
		miscAddButton.addActionListener(this);

		// ------------------------------------------------------------------ //

		for (DoubleTextField field : timeFields) {
			timeSeriesPanel.remove(field);
		}

		for (DoubleTextField field : logcFields) {
			timeSeriesPanel.remove(field);
		}

		for (JButton button : removeButtons) {
			timeSeriesButtonsPanel.remove(button);
		}

		for (JButton button : addButtons) {
			timeSeriesButtonsPanel.remove(button);
		}

		for (JLabel label : emptyLabels) {
			timeSeriesPanel.remove(label);
			timeSeriesButtonsPanel.remove(label);
		}

		timeFields.clear();
		logcFields.clear();
		removeButtons.clear();
		addButtons.clear();
		emptyLabels.clear();

		PmmXmlDoc timeSeriesXml = tuples.get(index).getPmmXml(
				TimeSeriesSchema.ATT_TIMESERIES);

		for (PmmXmlElementConvertable el : timeSeriesXml.getElementSet()) {
			TimeSeriesXml element = (TimeSeriesXml) el;
			DoubleTextField timeField = new DoubleTextField();
			DoubleTextField logcField = new DoubleTextField();
			JButton removebutton = new JButton("-");
			JButton addButton = new JButton("+");

			timeField.setValue(element.getTime());
			timeField.setPreferredSize(new Dimension(100, timeField
					.getPreferredSize().height));
			logcField.setValue(element.getLog10C());
			logcField.setPreferredSize(new Dimension(100, logcField
					.getPreferredSize().height));

			timeSeriesPanel.add(timeField);
			timeSeriesPanel.add(logcField);
			timeSeriesButtonsPanel.add(removebutton);
			timeSeriesButtonsPanel.add(addButton);
			timeFields.add(timeField);
			logcFields.add(logcField);
			removeButtons.add(removebutton);
			addButtons.add(addButton);
			removebutton.addActionListener(this);
			addButton.addActionListener(this);
		}

		JButton addButton = new JButton("+");
		JLabel label1 = new JLabel();
		JLabel label2 = new JLabel();
		JLabel label3 = new JLabel();

		timeSeriesPanel.add(label1);
		timeSeriesPanel.add(label2);
		timeSeriesButtonsPanel.add(label3);
		timeSeriesButtonsPanel.add(addButton);
		emptyLabels.add(label1);
		emptyLabels.add(label2);
		emptyLabels.add(label3);
		addButtons.add(addButton);
		addButton.addActionListener(this);

		// ------------------------------------------------------------------ //

		miscPanel.revalidate();
		miscButtonsPanel.revalidate();
		timeSeriesPanel.revalidate();
		timeSeriesButtonsPanel.revalidate();
		addDocumentListeners();
	}

	private void updateData() throws PmmException {
		int i = idBox.getSelectedIndex();

		dataValid = true;

		for (String column : stringColumnFields.keySet()) {
			StringTextField field = stringColumnFields.get(column);

			if (field.isValueValid()) {
				tuples.get(i).setValue(column, field.getValue());
			} else {
				tuples.get(i).setValue(column, null);
				dataValid = false;
			}
		}

		for (String column : intColumnFields.keySet()) {
			IntTextField field = intColumnFields.get(column);

			if (field.isValueValid()) {
				tuples.get(i).setValue(column, field.getValue());
			} else {
				tuples.get(i).setValue(column, null);
				dataValid = false;
			}
		}

		for (String column : doubleColumnFields.keySet()) {
			DoubleTextField field = doubleColumnFields.get(column);

			if (field.isValueValid()) {
				tuples.get(i).setValue(column, field.getValue());
			} else {
				tuples.get(i).setValue(column, null);
				dataValid = false;
			}
		}

		PmmXmlDoc miscXml = new PmmXmlDoc();

		for (int j = 0; j < miscIDFields.size(); j++) {
			if (!miscIDFields.get(j).isValueValid()) {
				dataValid = false;
			}

			if (!miscNameFields.get(j).isValueValid()) {
				dataValid = false;
			}

			if (!miscDescriptionFields.get(j).isValueValid()) {
				dataValid = false;
			}

			if (!miscValueFields.get(j).isValueValid()) {
				dataValid = false;
			}

			if (!miscUnitFields.get(j).isValueValid()) {
				dataValid = false;
			}

			miscXml.add(new MiscXml(miscIDFields.get(j).getValue(),
					miscNameFields.get(j).getValue(), miscDescriptionFields
							.get(j).getValue(), miscValueFields.get(j)
							.getValue(), miscUnitFields.get(j).getValue()));
		}

		tuples.get(i).setValue(TimeSeriesSchema.ATT_MISC, miscXml);

		PmmXmlDoc timeSeriesXml = new PmmXmlDoc();

		for (int j = 0; j < timeFields.size(); j++) {
			if (!timeFields.get(j).isValueValid()) {
				dataValid = false;
			}

			if (!logcFields.get(j).isValueValid()) {
				dataValid = false;
			}

			timeSeriesXml.add(new TimeSeriesXml(null, timeFields.get(j)
					.getValue(), logcFields.get(j).getValue()));
		}

		tuples.get(i).setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXml);
		processRowChanges();
	}

	private void addDocumentListeners() {
		for (StringTextField field : stringColumnFields.values()) {
			field.addTextListener(this);
		}

		for (IntTextField field : intColumnFields.values()) {
			field.addTextListener(this);
		}

		for (DoubleTextField field : doubleColumnFields.values()) {
			field.addTextListener(this);
		}

		for (DoubleTextField field : timeFields) {
			field.addTextListener(this);
		}

		for (DoubleTextField field : logcFields) {
			field.addTextListener(this);
		}

		for (StringTextField field : miscNameFields) {
			field.addTextListener(this);
		}

		for (StringTextField field : miscDescriptionFields) {
			field.addTextListener(this);
		}

		for (DoubleTextField field : miscValueFields) {
			field.addTextListener(this);
		}

		for (StringTextField field : miscUnitFields) {
			field.addTextListener(this);
		}
	}

	private void removeDocumentListeners() {
		for (StringTextField field : stringColumnFields.values()) {
			field.removeTextListener(this);
		}

		for (IntTextField field : intColumnFields.values()) {
			field.removeTextListener(this);
		}

		for (DoubleTextField field : doubleColumnFields.values()) {
			field.removeTextListener(this);
		}

		for (DoubleTextField field : timeFields) {
			field.removeTextListener(this);
		}

		for (DoubleTextField field : logcFields) {
			field.removeTextListener(this);
		}

		for (StringTextField field : miscNameFields) {
			field.removeTextListener(this);
		}

		for (StringTextField field : miscDescriptionFields) {
			field.removeTextListener(this);
		}

		for (DoubleTextField field : miscValueFields) {
			field.removeTextListener(this);
		}

		for (StringTextField field : miscUnitFields) {
			field.removeTextListener(this);
		}
	}

	private void processRowChanges() throws PmmException {
		int index = idBox.getSelectedIndex();
		List<String> rowChanges = new ArrayList<String>();

		PmmXmlDoc oldMisc = oldTuples.get(index).getPmmXml(
				TimeSeriesSchema.ATT_MISC);
		PmmXmlDoc newMisc = tuples.get(index).getPmmXml(
				TimeSeriesSchema.ATT_MISC);

		if (newMisc.getElementSet().size() != oldMisc.getElementSet().size()) {
			rowChanges.add(TimeSeriesSchema.ATT_MISC + "->"
					+ DataEditNodeModel.miscToString(newMisc));
		} else {
			for (int i = 0; i < oldMisc.getElementSet().size(); i++) {
				String oldName = ((MiscXml) oldMisc.get(i)).getName();
				String newName = ((MiscXml) newMisc.get(i)).getName();
				String oldDescription = ((MiscXml) oldMisc.get(i))
						.getDescription();
				String newDescription = ((MiscXml) newMisc.get(i))
						.getDescription();
				Double oldValue = ((MiscXml) oldMisc.get(i)).getValue();
				Double newValue = ((MiscXml) newMisc.get(i)).getValue();
				String oldUnit = ((MiscXml) oldMisc.get(i)).getUnit();
				String newUnit = ((MiscXml) newMisc.get(i)).getUnit();

				if (!areEqual(oldName, newName)
						|| !areEqual(oldDescription, newDescription)
						|| !areEqual(oldValue, newValue)
						|| !areEqual(oldUnit, newUnit)) {
					rowChanges.add(TimeSeriesSchema.ATT_MISC + "->"
							+ DataEditNodeModel.miscToString(newMisc));
					break;
				}
			}
		}

		PmmXmlDoc oldTimeSeries = oldTuples.get(index).getPmmXml(
				TimeSeriesSchema.ATT_TIMESERIES);
		PmmXmlDoc newTimeSeries = tuples.get(index).getPmmXml(
				TimeSeriesSchema.ATT_TIMESERIES);

		if (newTimeSeries.getElementSet().size() != oldTimeSeries
				.getElementSet().size()) {
			rowChanges.add(TimeSeriesSchema.ATT_TIMESERIES + "->"
					+ DataEditNodeModel.timeSeriesToString(newTimeSeries));
		} else {
			for (int i = 0; i < oldTimeSeries.getElementSet().size(); i++) {
				Double oldTime = ((TimeSeriesXml) oldTimeSeries.get(i))
						.getTime();
				Double oldLogc = ((TimeSeriesXml) oldTimeSeries.get(i))
						.getLog10C();
				Double newTime = ((TimeSeriesXml) newTimeSeries.get(i))
						.getTime();
				Double newLogc = ((TimeSeriesXml) newTimeSeries.get(i))
						.getLog10C();

				if (!areEqual(oldTime, newTime) || !areEqual(oldLogc, newLogc)) {
					rowChanges.add(TimeSeriesSchema.ATT_TIMESERIES
							+ "->"
							+ DataEditNodeModel
									.timeSeriesToString(newTimeSeries));
					break;
				}
			}
		}

		for (String column : stringColumnFields.keySet()) {
			String oldValue = oldTuples.get(index).getString(column);
			String newValue = tuples.get(index).getString(column);

			if (oldValue == null) {
				if (newValue != null) {
					rowChanges.add(column + "->" + newValue);
				}
			} else {
				if (!oldValue.equals(newValue)) {
					if (newValue != null) {
						rowChanges.add(column + "->" + newValue);
					} else {
						rowChanges.add(column + "->");
					}
				}
			}
		}

		for (String column : intColumnFields.keySet()) {
			Integer oldValue = oldTuples.get(index).getInt(column);
			Integer newValue = tuples.get(index).getInt(column);

			if (oldValue == null) {
				if (newValue != null) {
					rowChanges.add(column + "->" + newValue);
				}
			} else {
				if (!oldValue.equals(newValue)) {
					if (newValue != null) {
						rowChanges.add(column + "->" + newValue);
					} else {
						rowChanges.add(column + "->");
					}
				}
			}
		}

		for (String column : doubleColumnFields.keySet()) {
			Double oldValue = oldTuples.get(index).getDouble(column);
			Double newValue = tuples.get(index).getDouble(column);

			if (oldValue == null) {
				if (newValue != null) {
					rowChanges.add(column + "->" + newValue);
				}
			} else {
				if (!oldValue.equals(newValue)) {
					if (newValue != null) {
						rowChanges.add(column + "->" + newValue);
					} else {
						rowChanges.add(column + "->");
					}
				}
			}
		}

		int condID = tuples.get(index).getInt(TimeSeriesSchema.ATT_CONDID);
		int oldCondID = oldTuples.get(index)
				.getInt(TimeSeriesSchema.ATT_CONDID);

		if (!rowChanges.isEmpty()) {
			if (condID == oldCondID) {
				condID = MathUtilities.getRandomNegativeInt();
				tuples.get(index).setValue(TimeSeriesSchema.ATT_CONDID, condID);
				condIDField.setValue(condID);
			}

			rowChanges.add(TimeSeriesSchema.ATT_CONDID + "->" + condID);
			dataChanges.put(idList.get(index), rowChanges);
		} else {
			if (condID != oldCondID) {
				tuples.get(index).setValue(TimeSeriesSchema.ATT_CONDID,
						oldCondID);
				condIDField.setValue(oldCondID);
			}

			dataChanges.remove(idList.get(index));
		}
	}

	private boolean areEqual(String v1, String v2) {
		if (v1 == null) {
			return v2 == null;
		} else {
			return v1.equals(v2);
		}
	}

	private boolean areEqual(Double v1, Double v2) {
		if (v1 == null) {
			return v2 == null;
		} else {
			return v1.equals(v2);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == idBox) {
				updateTextFields();
			} else if (e.getSource() == deleteRecordButton) {
				int i = idBox.getSelectedIndex();

				dataChanges.put(
						idList.get(i),
						new ArrayList<String>(Arrays
								.asList(DataEditNodeModel.DELETED)));
				idList.remove(i);
				tuples.remove(i);
				oldTuples.remove(i);
				idBox.removeItemAt(i);
				updateTextFields();
			} else if (e.getSource() == clearButton) {
				dataChanges.clear();
				readTable(table);
				((JPanel) getTab("Options")).removeAll();
				((JPanel) getTab("Options")).add(createMainPanel());
			} else if (removeButtons.contains(e.getSource())) {
				int i = idBox.getSelectedIndex();
				int j = removeButtons.indexOf(e.getSource());
				PmmXmlDoc timeSeriesXml = tuples.get(i).getPmmXml(
						TimeSeriesSchema.ATT_TIMESERIES);

				timeSeriesXml.getElementSet().remove(j);

				tuples.get(i).setValue(TimeSeriesSchema.ATT_TIMESERIES,
						timeSeriesXml);
				updateTextFields();
				processRowChanges();
			} else if (addButtons.contains(e.getSource())) {
				int i = idBox.getSelectedIndex();
				int j = addButtons.indexOf(e.getSource());
				PmmXmlDoc timeSeriesXml = tuples.get(i).getPmmXml(
						TimeSeriesSchema.ATT_TIMESERIES);

				timeSeriesXml.getElementSet().add(j,
						new TimeSeriesXml(null, 0.0, 0.0));

				tuples.get(i).setValue(TimeSeriesSchema.ATT_TIMESERIES,
						timeSeriesXml);
				updateTextFields();
				processRowChanges();
			} else if (miscRemoveButtons.contains(e.getSource())) {
				int i = idBox.getSelectedIndex();
				int j = miscRemoveButtons.indexOf(e.getSource());
				PmmXmlDoc miscXml = tuples.get(i).getPmmXml(
						TimeSeriesSchema.ATT_MISC);

				miscXml.getElementSet().remove(j);

				tuples.get(i).setValue(TimeSeriesSchema.ATT_MISC, miscXml);
				updateTextFields();
				processRowChanges();
			} else if (miscAddButtons.contains(e.getSource())) {
				int i = idBox.getSelectedIndex();
				int j = miscAddButtons.indexOf(e.getSource());
				PmmXmlDoc miscXml = tuples.get(i).getPmmXml(
						TimeSeriesSchema.ATT_MISC);

				miscXml.getElementSet().add(
						j,
						new MiscXml(MathUtilities.getRandomNegativeInt(), "",
								"", 0.0, ""));

				tuples.get(i).setValue(TimeSeriesSchema.ATT_MISC, miscXml);
				updateTextFields();
				processRowChanges();
			}
		} catch (PmmException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void textChanged() {
		try {
			updateData();
		} catch (PmmException e) {
			e.printStackTrace();
		}
	}

}
