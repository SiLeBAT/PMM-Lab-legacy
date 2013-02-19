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
package de.bund.bfr.knime.pmm.microbialdataedit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyTable;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * <code>NodeDialog</code> for the "MicrobialDataEdit" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class MicrobialDataEditNodeDialog extends DataAwareNodeDialogPane
		implements ActionListener {

	private JButton addButton;
	private JButton removeButton;
	private Map<Integer, String> conditionsByID;
	private JList<String> conditionsList;
	private JTable conditionsTable;

	/**
	 * New pane for configuring the MicrobialDataEdit node.
	 */
	protected MicrobialDataEditNodeDialog() {
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		removeButton = new JButton("Remove");
		removeButton.addActionListener(this);
		conditionsByID = new LinkedHashMap<>();
		conditionsList = new JList<>();
		conditionsTable = new JTable();

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);

		JPanel listPanel = new JPanel();

		listPanel.setLayout(new BorderLayout());
		listPanel.add(buttonPanel, BorderLayout.NORTH);
		listPanel.add(new JScrollPane(conditionsList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(listPanel, BorderLayout.NORTH);
		panel.add(new JScrollPane(conditionsTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		KnimeRelationReader reader = new KnimeRelationReader(
				new TimeSeriesSchema(), input[0]);
		List<String> ids = new ArrayList<>();
		Map<Integer, Map<String, Double>> conditions;

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			String combaseID = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
			int condID = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
			String id;

			if (combaseID != null) {
				id = combaseID + " (" + condID + ")";
			} else {
				id = condID + "";
			}

			ids.add(id);
		}

		try {
			conditions = XmlConverter
					.xmlToIntDoubleMapMap(settings
							.getString(MicrobialDataEditNodeModel.CFGKEY_ADDEDCONDITIONS));
		} catch (InvalidSettingsException e) {
			conditions = new LinkedHashMap<>();
		}

		conditionsByID = new LinkedHashMap<>();

		for (int id : conditions.keySet()) {
			conditionsByID.put(
					id,
					DBKernel.getValue("SonstigeParameter", "ID", id + "",
							"Parameter") + "");
		}

		conditionsList.setListData(conditionsByID.values().toArray(
				new String[0]));

		ConditionsTableModel tableModel = new ConditionsTableModel(ids);

		for (int id : conditions.keySet()) {
			String name = conditionsByID.get(id);

			tableModel.addCondition(name, conditions.get(id));
		}

		conditionsTable.setModel(tableModel);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		Map<Integer, Map<String, Double>> addedConditions = new LinkedHashMap<>();
		Map<String, Map<String, Double>> valueMap = ((ConditionsTableModel) conditionsTable
				.getModel()).getValueMap();

		for (int id : conditionsByID.keySet()) {
			addedConditions.put(id, valueMap.get(conditionsByID.get(id)));
		}

		settings.addString(MicrobialDataEditNodeModel.CFGKEY_ADDEDCONDITIONS,
				XmlConverter.mapToXml(addedConditions));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			Integer id = openMiscDBWindow(null);

			if (id != null && !conditionsByID.containsKey(id)) {
				String name = DBKernel.getValue("SonstigeParameter", "ID", id
						+ "", "Parameter")
						+ "";

				conditionsByID.put(id, name);
				conditionsList.setListData(conditionsByID.values().toArray(
						new String[0]));
				((ConditionsTableModel) conditionsTable.getModel())
						.addCondition(name, new LinkedHashMap<String, Double>());
				conditionsTable.repaint();
			}
		} else if (e.getSource() == removeButton) {
			Set<String> removeSet = new LinkedHashSet<>(
					conditionsList.getSelectedValuesList());
			Set<Integer> idRemoveSet = new LinkedHashSet<>();

			for (int id : conditionsByID.keySet()) {
				if (removeSet.contains(conditionsByID.get(id))) {
					idRemoveSet.add(id);
				}
			}

			for (int id : idRemoveSet) {
				conditionsByID.remove(id);
			}

			conditionsList.setListData(conditionsByID.values().toArray(
					new String[0]));

			for (String name : removeSet) {
				((ConditionsTableModel) conditionsTable.getModel())
						.removeCondition(name);
			}

			conditionsTable.repaint();
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

	private static class ConditionsTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private List<String> ids;
		private List<String> conditions;
		private List<List<Double>> conditionsValues;

		public ConditionsTableModel(List<String> ids) {
			this.ids = ids;
			conditions = new ArrayList<>();
			conditionsValues = new ArrayList<>();
		}

		public void addCondition(String condition, Map<String, Double> values) {
			List<Double> valueList = new ArrayList<>();

			for (String id : ids) {
				valueList.add(values.get(id));
			}

			conditions.add(condition);
			conditionsValues.add(valueList);
			fireTableStructureChanged();
		}

		public void removeCondition(String condition) {
			int i = conditions.indexOf(condition);

			if (i != -1) {
				conditions.remove(i);
				conditionsValues.remove(i);
			}
			
			fireTableStructureChanged();
		}

		public Map<String, Map<String, Double>> getValueMap() {
			Map<String, Map<String, Double>> valueMap = new LinkedHashMap<>();

			for (int i = 0; i < conditions.size(); i++) {
				valueMap.put(conditions.get(i),
						new LinkedHashMap<String, Double>());

				for (int j = 0; j < ids.size(); j++) {
					valueMap.get(conditions.get(i)).put(ids.get(j),
							conditionsValues.get(i).get(j));
				}
			}

			return valueMap;
		}

		@Override
		public int getRowCount() {
			return ids.size();
		}

		@Override
		public int getColumnCount() {
			return conditions.size() + 1;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return ids.get(rowIndex);
			} else {
				return conditionsValues.get(columnIndex - 1).get(rowIndex);
			}
		}

		@Override
		public String getColumnName(int column) {
			if (column == 0) {
				return "ID";
			} else {
				return conditions.get(column - 1);
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return String.class;
			} else {
				return Double.class;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex != 0;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			conditionsValues.get(columnIndex - 1)
					.set(rowIndex, (Double) aValue);
			fireTableDataChanged();
		}

	}

}
