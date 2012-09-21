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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.PmmException;
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
	private boolean dataValid;
	private Map<Integer, List<String>> dataChanges;

	private List<Integer> idList;
	private List<String> nameList;
	private List<KnimeTuple> tuples;
	private List<KnimeTuple> oldTuples;

	private JComboBox idBox;
	private JButton deleteRecordButton;
	private IntTextField condIDField;
	private Map<String, StringTextField> stringColumnFields;
	private Map<String, IntTextField> intColumnFields;
	private Map<String, DoubleTextField> doubleColumnFields;

	private JPanel tablePanel;
	private List<DoubleTextField> timeFields;
	private List<DoubleTextField> logcFields;
	private List<JButton> removeButtons;
	private List<JButton> addButtons;
	private List<JLabel> emptyLabels;

	/**
	 * New pane for configuring DataEdit node dialog. This is just a suggestion
	 * to demonstrate possible default dialog components.
	 */
	protected DataEditNodeDialog() {
		JPanel panel = new JPanel();

		schema = new TimeSeriesSchema();
		dataValid = true;
		dataChanges = new HashMap<Integer, List<String>>();
		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
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
			readTable(input[0]);
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
		JPanel mainPanel = new JPanel();
		JPanel upperPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel idPanel = new JPanel();

		idBox = new JComboBox(nameList.toArray());
		idBox.setSelectedIndex(0);
		idBox.addActionListener(this);
		deleteRecordButton = new JButton("Delete Record");
		deleteRecordButton.addActionListener(this);
		idPanel.add(new JLabel("Original ID:"));
		idPanel.add(idBox);
		idPanel.add(deleteRecordButton);
		condIDField = new IntTextField();
		condIDField.setEditable(false);
		rightPanel.add(condIDField);
		leftPanel.add(new JLabel(TimeSeriesSchema.ATT_CONDID + ":"));
		stringColumnFields = new HashMap<String, StringTextField>();
		intColumnFields = new HashMap<String, IntTextField>();
		doubleColumnFields = new HashMap<String, DoubleTextField>();

		leftPanel.setLayout(new GridLayout(0, 1, 5, 5));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new GridLayout(0, 1, 5, 5));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		idPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(new SpacePanel(idPanel), BorderLayout.NORTH);
		upperPanel.add(leftPanel, BorderLayout.WEST);
		upperPanel.add(rightPanel, BorderLayout.CENTER);

		for (int i = 0; i < schema.size(); i++) {
			int type = schema.getType(i);
			String name = schema.getName(i);
			String fullName = AttributeUtilities.getFullName(name);

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

		JPanel bottomPanel = new JPanel();
		JLabel timeLabel = new JLabel(TimeSeriesSchema.ATT_TIME);
		JLabel logcLabel = new JLabel(TimeSeriesSchema.ATT_LOGC);

		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logcLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tablePanel = new JPanel();
		tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tablePanel.setLayout(new GridLayout(0, 4, 5, 5));
		tablePanel.add(timeLabel);
		tablePanel.add(logcLabel);
		tablePanel.add(new JLabel());
		tablePanel.add(new JLabel());
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(tablePanel, BorderLayout.NORTH);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(upperPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(bottomPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

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

		for (DoubleTextField field : timeFields) {
			tablePanel.remove(field);
		}

		for (DoubleTextField field : logcFields) {
			tablePanel.remove(field);
		}

		for (JButton button : removeButtons) {
			tablePanel.remove(button);
		}

		for (JButton button : addButtons) {
			tablePanel.remove(button);
		}

		for (JLabel label : emptyLabels) {
			tablePanel.remove(label);
		}

		timeFields.clear();
		logcFields.clear();
		removeButtons.clear();
		addButtons.clear();
		emptyLabels.clear();

		List<Double> timeList = tuples.get(index).getDoubleList(
				TimeSeriesSchema.ATT_TIME);
		List<Double> logcList = tuples.get(index).getDoubleList(
				TimeSeriesSchema.ATT_LOGC);

		for (int i = 0; i < timeList.size(); i++) {
			DoubleTextField timeField = new DoubleTextField();
			DoubleTextField logcField = new DoubleTextField();
			JButton removebutton = new JButton("-");
			JButton addButton = new JButton("+");

			if (timeList.get(i) != null) {
				timeField.setValue(timeList.get(i));
			}

			if (logcList.get(i) != null) {
				logcField.setValue(logcList.get(i));
			}

			tablePanel.add(timeField);
			tablePanel.add(logcField);
			tablePanel.add(removebutton);
			tablePanel.add(addButton);
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

		tablePanel.add(label1);
		tablePanel.add(label2);
		tablePanel.add(label3);
		tablePanel.add(addButton);
		emptyLabels.add(label1);
		emptyLabels.add(label2);
		emptyLabels.add(label3);
		addButtons.add(addButton);
		addButton.addActionListener(this);

		tablePanel.revalidate();
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

		List<Double> timeList = new ArrayList<Double>();
		List<Double> logcList = new ArrayList<Double>();

		for (int j = 0; j < timeFields.size(); j++) {
			if (!timeFields.get(j).isValueValid()) {
				dataValid = false;
			}

			if (!logcFields.get(j).isValueValid()) {
				dataValid = false;
			}

			timeList.add(timeFields.get(j).getValue());
			logcList.add(logcFields.get(j).getValue());
		}

		tuples.get(i).setValue(TimeSeriesSchema.ATT_TIME, timeList);
		tuples.get(i).setValue(TimeSeriesSchema.ATT_LOGC, logcList);
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
	}

	private void processRowChanges() throws PmmException {
		int index = idBox.getSelectedIndex();
		List<String> rowChanges = new ArrayList<String>();

		List<Double> oldTimes = oldTuples.get(index).getDoubleList(
				TimeSeriesSchema.ATT_TIME);
		List<Double> newTimes = tuples.get(index).getDoubleList(
				TimeSeriesSchema.ATT_TIME);
		List<Double> oldLogcs = oldTuples.get(index).getDoubleList(
				TimeSeriesSchema.ATT_LOGC);
		List<Double> newLogcs = tuples.get(index).getDoubleList(
				TimeSeriesSchema.ATT_LOGC);

		if (oldTimes.size() != newTimes.size()) {
			if (!newTimes.isEmpty()) {
				rowChanges.add(TimeSeriesSchema.ATT_TIME + "->"
						+ DataEditNodeModel.convertToString(newTimes));
				rowChanges.add(TimeSeriesSchema.ATT_LOGC + "->"
						+ DataEditNodeModel.convertToString(newLogcs));
			} else {
				rowChanges.add(TimeSeriesSchema.ATT_TIME + "->");
				rowChanges.add(TimeSeriesSchema.ATT_LOGC + "->");
			}
		} else {
			int n = oldTimes.size();

			for (int i = 0; i < n; i++) {
				if (oldTimes.get(i) == null) {
					if (newTimes.get(i) != null) {
						rowChanges.add(TimeSeriesSchema.ATT_TIME + "->"
								+ DataEditNodeModel.convertToString(newTimes));
						break;
					}
				} else {
					if (!oldTimes.get(i).equals(newTimes.get(i))) {
						rowChanges.add(TimeSeriesSchema.ATT_TIME + "->"
								+ DataEditNodeModel.convertToString(newTimes));
						break;
					}
				}
			}

			for (int i = 0; i < n; i++) {
				if (oldLogcs.get(i) == null) {
					if (newLogcs.get(i) != null) {
						rowChanges.add(TimeSeriesSchema.ATT_LOGC + "->"
								+ DataEditNodeModel.convertToString(newLogcs));
						break;
					}
				} else {
					if (!oldLogcs.get(i).equals(newLogcs.get(i))) {
						rowChanges.add(TimeSeriesSchema.ATT_LOGC + "->"
								+ DataEditNodeModel.convertToString(newLogcs));
						break;
					}
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
			} else if (removeButtons.contains(e.getSource())) {
				int i = idBox.getSelectedIndex();
				int j = removeButtons.indexOf(e.getSource());
				List<Double> timeList = tuples.get(i).getDoubleList(
						TimeSeriesSchema.ATT_TIME);
				List<Double> logcList = tuples.get(i).getDoubleList(
						TimeSeriesSchema.ATT_LOGC);

				timeList.remove(j);
				logcList.remove(j);

				tuples.get(i).setValue(TimeSeriesSchema.ATT_TIME, timeList);
				tuples.get(i).setValue(TimeSeriesSchema.ATT_LOGC, logcList);
				updateTextFields();
				processRowChanges();
			} else if (addButtons.contains(e.getSource())) {
				int i = idBox.getSelectedIndex();
				int j = addButtons.indexOf(e.getSource());
				List<Double> timeList = tuples.get(i).getDoubleList(
						TimeSeriesSchema.ATT_TIME);
				List<Double> logcList = tuples.get(i).getDoubleList(
						TimeSeriesSchema.ATT_LOGC);

				timeList.add(j, 0.0);
				logcList.add(j, 0.0);

				tuples.get(i).setValue(TimeSeriesSchema.ATT_TIME, timeList);
				tuples.get(i).setValue(TimeSeriesSchema.ATT_LOGC, logcList);
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
