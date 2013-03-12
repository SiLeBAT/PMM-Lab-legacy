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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
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
	private Set<Integer> usedMiscIDs;

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
		usedMiscIDs = new LinkedHashSet<>();

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);

		JPanel listPanel = new JPanel();

		listPanel.setBorder(BorderFactory
				.createTitledBorder("Conditions to Add"));
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
		Map<Integer, Map<String, Double>> addedConditions;
		Map<String, Integer> agents;
		Map<String, Integer> matrices;
		Map<String, String> comments;

		try {
			addedConditions = XmlConverter
					.xmlToIntDoubleMapMap(settings
							.getString(MicrobialDataEditNodeModel.CFGKEY_ADDEDCONDITIONS));
		} catch (InvalidSettingsException e) {
			addedConditions = new LinkedHashMap<>();
		}

		try {
			agents = XmlConverter.xmlToIntMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_AGENTS));
		} catch (InvalidSettingsException e) {
			agents = new LinkedHashMap<>();
		}

		try {
			matrices = XmlConverter.xmlToIntMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_MATRICES));
		} catch (InvalidSettingsException e) {
			matrices = new LinkedHashMap<>();
		}

		try {
			comments = XmlConverter.xmlToStringMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_COMMENTS));
		} catch (InvalidSettingsException e) {
			comments = new LinkedHashMap<>();
		}

		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createDataSchema(), input[0]);
		List<String> ids = new ArrayList<>();
		List<Integer> agentList = new ArrayList<>();
		List<Integer> matrixList = new ArrayList<>();
		List<String> commentList = new ArrayList<>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			String combaseID = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
			int condID = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
			PmmXmlDoc miscXml = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
			String id;

			if (combaseID != null) {
				id = combaseID + " (" + condID + ")";
			} else {
				id = condID + "";
			}

			ids.add(id);

			if (agents.containsKey(id)) {
				agentList.add(agents.get(id));
			} else {
				agentList.add(((AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0)).getID());
			}

			if (matrices.containsKey(id)) {
				matrixList.add(matrices.get(id));
			} else {
				matrixList.add(((MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0)).getID());
			}

			if (comments.containsKey(id)) {
				commentList.add(comments.get(id));
			} else {
				commentList.add(((MdInfoXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MDINFO).get(0)).getComment());
			}

			for (PmmXmlElementConvertable el : miscXml.getElementSet()) {
				usedMiscIDs.add(((MiscXml) el).getID());
			}
		}

		for (int id : usedMiscIDs) {
			addedConditions.remove(id);
		}

		conditionsByID = new LinkedHashMap<>();

		for (int id : addedConditions.keySet()) {
			conditionsByID.put(
					id,
					DBKernel.getValue("SonstigeParameter", "ID", id + "",
							"Parameter") + "");
		}

		conditionsList.setListData(conditionsByID.values().toArray(
				new String[0]));

		EditTable tableModel = new EditTable(ids, agentList, matrixList,
				commentList);

		for (int id : addedConditions.keySet()) {
			String name = conditionsByID.get(id);

			tableModel.addCondition(name, addedConditions.get(id));
		}

		conditionsTable.setModel(tableModel);
		conditionsTable.getColumn(TimeSeriesSchema.ATT_AGENT).setCellRenderer(
				new AgentRenderer());
		conditionsTable.getColumn(TimeSeriesSchema.ATT_AGENT).setCellEditor(
				new AgentEditor());
		conditionsTable.getColumn(TimeSeriesSchema.ATT_MATRIX).setCellRenderer(
				new MatrixRenderer());
		conditionsTable.getColumn(TimeSeriesSchema.ATT_MATRIX).setCellEditor(
				new MatrixEditor());
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		Map<Integer, Map<String, Double>> addedConditions = new LinkedHashMap<>();
		Map<String, Map<String, Double>> valueMap = ((EditTable) conditionsTable
				.getModel()).getConditionValueMap();

		for (int id : conditionsByID.keySet()) {
			addedConditions.put(id, valueMap.get(conditionsByID.get(id)));
		}

		settings.addString(MicrobialDataEditNodeModel.CFGKEY_ADDEDCONDITIONS,
				XmlConverter.mapToXml(addedConditions));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_AGENTS,
				XmlConverter.mapToXml(((EditTable) conditionsTable.getModel())
						.getAgentMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_MATRICES,
				XmlConverter.mapToXml(((EditTable) conditionsTable.getModel())
						.getMatrixMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_COMMENTS,
				XmlConverter.mapToXml(((EditTable) conditionsTable.getModel())
						.getCommentMap()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			Integer id = DBKernel.openMiscDBWindow(null);

			if (id != null && !conditionsByID.containsKey(id)) {
				String name = DBKernel.getValue("SonstigeParameter", "ID", id
						+ "", "Parameter")
						+ "";

				if (!usedMiscIDs.contains(id)) {
					conditionsByID.put(id, name);
					conditionsList.setListData(conditionsByID.values().toArray(
							new String[0]));
					((EditTable) conditionsTable.getModel()).addCondition(name,
							new LinkedHashMap<String, Double>());
					conditionsTable.repaint();
				} else {
					JOptionPane.showMessageDialog(addButton,
							"Data already contains " + name, "Error",
							JOptionPane.ERROR_MESSAGE);
				}
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
				((EditTable) conditionsTable.getModel()).removeCondition(name);
			}

			conditionsTable.repaint();
		}
	}

	private static class EditTable extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private List<String> ids;
		private List<Integer> agentIds;
		private List<Integer> matrixIds;
		private List<String> comments;
		private List<String> conditions;
		private List<List<Double>> conditionsValues;

		public EditTable(List<String> ids, List<Integer> agentIds,
				List<Integer> matrixIds, List<String> comments) {
			this.ids = ids;
			this.agentIds = agentIds;
			this.matrixIds = matrixIds;
			this.comments = comments;
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

		public Map<String, Map<String, Double>> getConditionValueMap() {
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

		public Map<String, Integer> getAgentMap() {
			Map<String, Integer> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), agentIds.get(i));
			}

			return map;
		}

		public Map<String, Integer> getMatrixMap() {
			Map<String, Integer> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), matrixIds.get(i));
			}

			return map;
		}

		public Map<String, String> getCommentMap() {
			Map<String, String> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), comments.get(i));
			}

			return map;
		}

		@Override
		public int getRowCount() {
			return ids.size();
		}

		@Override
		public int getColumnCount() {
			return conditions.size() + 4;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ids.get(rowIndex);
			case 1:
				return agentIds.get(rowIndex);
			case 2:
				return matrixIds.get(rowIndex);
			case 3:
				return comments.get(rowIndex);
			default:
				return conditionsValues.get(columnIndex - 4).get(rowIndex);
			}
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return "ID";
			case 1:
				return TimeSeriesSchema.ATT_AGENT;
			case 2:
				return TimeSeriesSchema.ATT_MATRIX;
			case 3:
				return AttributeUtilities.ATT_COMMENT;
			default:
				return conditions.get(column - 4);
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return Integer.class;
			case 2:
				return Integer.class;
			case 3:
				return String.class;
			default:
				return Double.class;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex != 0;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				break;
			case 1:
				agentIds.set(rowIndex, (Integer) aValue);
				break;
			case 2:
				matrixIds.set(rowIndex, (Integer) aValue);
				break;
			case 3:
				comments.set(rowIndex, (String) aValue);
				break;
			default:
				conditionsValues.get(columnIndex - 4).set(rowIndex,
						(Double) aValue);
				break;
			}

			fireTableDataChanged();
		}

	}

	private class AgentRenderer implements TableCellRenderer {

		private JLabel label;
		private Integer agentId;

		public AgentRenderer() {
			label = new JLabel();
			agentId = null;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			agentId = (Integer) value;

			if (agentId != null) {
				label.setText(DBKernel.getValue("Agenzien", "ID", agentId + "",
						"Agensname") + "");
			} else {
				label.setText("");
			}

			return label;
		}
	}

	private class AgentEditor extends AbstractCellEditor implements
			TableCellEditor {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private Integer agentId;

		public AgentEditor() {
			button = new JButton();
			agentId = null;
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Integer id = DBKernel.openAgentDBWindow(agentId);

					if (id != null) {
						agentId = id;
						stopCellEditing();
					}
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			agentId = (Integer) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return agentId;
		}
	}

	private class MatrixRenderer implements TableCellRenderer {

		private JLabel label;
		private Integer matrixId;

		public MatrixRenderer() {
			label = new JLabel();
			matrixId = null;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			matrixId = (Integer) value;

			if (matrixId != null) {
				label.setText(DBKernel.getValue("Matrices", "ID",
						matrixId + "", "Matrixname") + "");
			} else {
				label.setText("");
			}

			return label;
		}
	}

	private class MatrixEditor extends AbstractCellEditor implements
			TableCellEditor {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private Integer matrixId;

		public MatrixEditor() {
			button = new JButton();
			matrixId = null;
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Integer id = DBKernel.openMatrixDBWindow(matrixId);

					if (id != null) {
						matrixId = id;
						stopCellEditing();
					}
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			matrixId = (Integer) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return matrixId;
		}
	}

}
