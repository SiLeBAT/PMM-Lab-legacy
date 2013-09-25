/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
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
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.TimeSeriesDialog;
import de.bund.bfr.knime.pmm.common.units.Categories;

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
	private JList<String> addedConditionsList;
	private JTable table;

	private List<Integer> addedConditionIDs;
	private List<String> addedConditionNames;
	private Set<Integer> usedMiscIDs;

	/**
	 * New pane for configuring the MicrobialDataEdit node.
	 */
	protected MicrobialDataEditNodeDialog() {
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		removeButton = new JButton("Remove");
		removeButton.addActionListener(this);
		addedConditionsList = new JList<>();
		table = new JTable();

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);

		JPanel listPanel = new JPanel();

		listPanel.setBorder(BorderFactory
				.createTitledBorder("Conditions to Add"));
		listPanel.setLayout(new BorderLayout());
		listPanel.add(buttonPanel, BorderLayout.NORTH);
		listPanel.add(new JScrollPane(addedConditionsList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(listPanel, BorderLayout.NORTH);
		panel.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		SettingsHelper set = new SettingsHelper();

		set.loadSettings(settings);

		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createDataSchema(), input[0]);
		List<KnimeTuple> tuples = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		List<AgentXml> agentList = new ArrayList<>();
		List<String> agentDetailList = new ArrayList<>();
		List<MatrixXml> matrixList = new ArrayList<>();
		List<String> matrixDetailList = new ArrayList<>();
		List<String> commentList = new ArrayList<>();
		List<Integer> qualityScoreList = new ArrayList<>();
		List<Boolean> checkedList = new ArrayList<>();
		List<List<TimeSeriesXml>> timeSeriesList = new ArrayList<>();
		List<MiscXml> usedMiscs = new ArrayList<>();
		List<List<Double>> usedMiscValues = new ArrayList<>();
		List<List<String>> usedMiscUnits = new ArrayList<>();

		usedMiscIDs = new LinkedHashSet<>();
		addedConditionIDs = new ArrayList<>();
		addedConditionNames = new ArrayList<>();

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		for (KnimeTuple tuple : tuples) {
			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					TimeSeriesSchema.ATT_MISC).getElementSet()) {
				MiscXml misc = (MiscXml) el;

				if (usedMiscIDs.add(misc.getID())) {
					usedMiscs.add(misc);
					usedMiscValues.add(new ArrayList<Double>());
					usedMiscUnits.add(new ArrayList<String>());
				}
			}
		}

		for (KnimeTuple tuple : tuples) {
			String combaseID = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
			int condID = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
			MdInfoXml infoXml = (MdInfoXml) tuple.getPmmXml(
					TimeSeriesSchema.ATT_MDINFO).get(0);
			PmmXmlDoc miscXml = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
			List<TimeSeriesXml> series = new ArrayList<>();

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					TimeSeriesSchema.ATT_TIMESERIES).getElementSet()) {
				series.add((TimeSeriesXml) el);
			}

			String id;

			if (combaseID != null) {
				id = combaseID + " (" + condID + ")";
			} else {
				id = condID + "";
			}

			ids.add(id);

			if (set.getAgents().containsKey(id)) {
				agentList.add(set.getAgents().get(id));
			} else {
				agentList.add((AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0));
			}

			if (set.getAgentDetails().containsKey(id)) {
				agentDetailList.add(set.getAgentDetails().get(id));
			} else {
				agentDetailList.add(((AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0)).getDetail());
			}

			if (set.getMatrices().containsKey(id)) {
				matrixList.add(set.getMatrices().get(id));
			} else {
				matrixList.add((MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0));
			}

			if (set.getMatrixDetails().containsKey(id)) {
				matrixDetailList.add(set.getMatrixDetails().get(id));
			} else {
				matrixDetailList.add(((MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0)).getDetail());
			}

			if (set.getComments().containsKey(id)) {
				commentList.add(set.getComments().get(id));
			} else {
				commentList.add(infoXml.getComment());
			}

			if (set.getQualityScores().containsKey(id)) {
				qualityScoreList.add(set.getQualityScores().get(id));
			} else {
				qualityScoreList.add(infoXml.getQualityScore());
			}

			if (set.getChecks().containsKey(id)) {
				checkedList.add(set.getChecks().get(id));
			} else {
				checkedList.add(infoXml.getChecked());
			}

			if (set.getTimeSeries().containsKey(id)) {
				timeSeriesList.add(set.getTimeSeries().get(id));
			} else {
				timeSeriesList.add(series);
			}

			for (int i = 0; i < usedMiscs.size(); i++) {
				Double value = null;
				String unit = null;
				int miscID = usedMiscs.get(i).getID();

				if (set.getConditions().containsKey(miscID)
						&& set.getConditionValues().get(miscID).containsKey(id)) {
					value = set.getConditionValues().get(miscID).get(id);
					unit = set.getConditionUnits().get(miscID).get(id);
				} else {
					for (PmmXmlElementConvertable el : miscXml.getElementSet()) {
						MiscXml cond = (MiscXml) el;

						if (cond.getID() == usedMiscs.get(i).getID()) {
							value = cond.getValue();
							unit = cond.getUnit();
							break;
						}
					}
				}

				usedMiscValues.get(i).add(value);
				usedMiscUnits.get(i).add(unit);
			}
		}

		for (int id : usedMiscIDs) {
			set.getAddedConditions().remove(id);
			set.getAddedConditionValues().remove(id);
			set.getAddedConditionUnits().remove(id);
		}

		for (int miscID : set.getAddedConditions().keySet()) {
			addedConditionIDs.add(miscID);
			addedConditionNames.add(set.getAddedConditions().get(miscID)
					.getName());
		}

		addedConditionsList.setListData(addedConditionNames
				.toArray(new String[0]));

		EditTable tableModel = new EditTable(ids, agentList, agentDetailList,
				matrixList, matrixDetailList, commentList, qualityScoreList,
				checkedList, timeSeriesList, usedMiscs, usedMiscValues,
				usedMiscUnits);

		for (int miscID : set.getAddedConditions().keySet()) {
			tableModel.addCondition(set.getAddedConditions().get(miscID), set
					.getAddedConditionValues().get(miscID), set
					.getAddedConditionUnits().get(miscID));
		}

		table.setModel(tableModel);
		table.getColumn(TimeSeriesSchema.ATT_AGENT).setCellRenderer(
				new AgentRenderer());
		table.getColumn(TimeSeriesSchema.ATT_AGENT).setCellEditor(
				new AgentEditor());
		table.getColumn(TimeSeriesSchema.ATT_MATRIX).setCellRenderer(
				new MatrixRenderer());
		table.getColumn(TimeSeriesSchema.ATT_MATRIX).setCellEditor(
				new MatrixEditor());
		table.getColumn(MdInfoXml.ATT_QUALITYSCORE).setCellRenderer(
				new QualityScoreRenderer());
		table.getColumn(MdInfoXml.ATT_QUALITYSCORE).setCellEditor(
				new QualityScoreEditor());
		table.getColumn(TimeSeriesSchema.ATT_TIMESERIES).setCellRenderer(
				new TimeSeriesRenderer());
		table.getColumn(TimeSeriesSchema.ATT_TIMESERIES).setCellEditor(
				new TimeSeriesEditor());
		table.setRowHeight((new JComboBox<String>()).getPreferredSize().height);

		for (MiscXml cond : usedMiscs) {
			List<String> units = new ArrayList<>();

			for (String cat : cond.getCategories()) {
				units.addAll(Categories.getCategory(cat).getAllUnits());
			}

			table.getColumn(cond.getName() + " Unit").setCellEditor(
					new DefaultCellEditor(new JComboBox<>(units
							.toArray(new String[0]))));
		}

		for (MiscXml cond : set.getAddedConditions().values()) {
			List<String> units = new ArrayList<>();

			for (String cat : cond.getCategories()) {
				units.addAll(Categories.getCategory(cat).getAllUnits());
			}

			table.getColumn(cond.getName() + " Unit").setCellEditor(
					new DefaultCellEditor(new JComboBox<>(units
							.toArray(new String[0]))));
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		EditTable model = (EditTable) table.getModel();
		SettingsHelper set = new SettingsHelper();

		set.setAddedConditions(model.getAddedConditionMap());
		set.setAddedConditionValues(model.getAddedConditionValueMap());
		set.setAddedConditionUnits(model.getAddedConditionUnitMap());
		set.setConditions(model.getConditionMap());
		set.setConditionValues(model.getConditionValueMap());
		set.setConditionUnits(model.getConditionUnitMap());
		set.setAgents(model.getAgentMap());
		set.setAgentDetails(model.getAgentDetailMap());
		set.setMatrices(model.getMatrixMap());
		set.setMatrixDetails(model.getMatrixDetailMap());
		set.setComments(model.getCommentMap());
		set.setQualityScores(model.getQualityScoreMap());
		set.setChecks(model.getCheckedMap());
		set.setTimeSeries(model.getTimeSeriesMap());
		set.saveSettings(settings);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			Integer id = DBKernel.openMiscDBWindow(null);

			if (id != null && !addedConditionIDs.contains(id)) {
				String name = DBKernel.getValue("SonstigeParameter", "ID", id
						+ "", "Parameter")
						+ "";
				String description = DBKernel.getValue("SonstigeParameter",
						"ID", id + "", "Beschreibung") + "";
				List<String> categories = Arrays.asList(DBKernel
						.getValue("SonstigeParameter", "ID", id + "",
								"Kategorie").toString().split(","));

				if (!usedMiscIDs.contains(id)) {
					List<String> units = new ArrayList<>();

					for (String cat : categories) {
						units.addAll(Categories.getCategory(cat).getAllUnits());
					}

					addedConditionNames.add(name);
					addedConditionsList.setListData(addedConditionNames
							.toArray(new String[0]));
					((EditTable) table.getModel())
							.addCondition(
									new MiscXml(id, name, description, null,
											categories, null, DBKernel
													.getLocalDBUUID()),
									new LinkedHashMap<String, Double>(),
									new LinkedHashMap<String, String>());
					table.getColumn(name + " Unit").setCellEditor(
							new DefaultCellEditor(new JComboBox<>(units
									.toArray(new String[0]))));

					table.repaint();
				} else {
					JOptionPane.showMessageDialog(addButton,
							"Data already contains " + name, "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getSource() == removeButton) {
			int[] indices = addedConditionsList.getSelectedIndices();
			Set<Integer> removedIDs = new LinkedHashSet<>();

			Arrays.sort(indices);

			for (int i = indices.length - 1; i >= 0; i--) {
				removedIDs.add(addedConditionIDs.get(indices[i]));
				addedConditionIDs.remove(indices[i]);
				addedConditionNames.remove(indices[i]);
			}

			addedConditionsList.setListData(addedConditionNames
					.toArray(new String[0]));

			for (int id : removedIDs) {
				((EditTable) table.getModel()).removeCondition(id);
			}

			table.repaint();
		}
	}

	private static class EditTable extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private List<String> ids;
		private List<AgentXml> agents;
		private List<String> agentDetails;
		private List<MatrixXml> matrices;
		private List<String> matrixDetails;
		private List<String> comments;
		private List<Integer> qualityScores;
		private List<Boolean> checks;
		private List<List<TimeSeriesXml>> timeSeries;
		private List<MiscXml> conditions;
		private List<List<Double>> conditionValues;
		private List<List<String>> conditionUnits;
		private List<MiscXml> addedConditions;
		private List<List<Double>> addedConditionValues;
		private List<List<String>> addedConditionUnits;

		public EditTable(List<String> ids, List<AgentXml> agents,
				List<String> agentDetails, List<MatrixXml> matrices,
				List<String> matrixDetails, List<String> comments,
				List<Integer> qualityScores, List<Boolean> checks,
				List<List<TimeSeriesXml>> timeSeries, List<MiscXml> conditions,
				List<List<Double>> conditionValues,
				List<List<String>> conditionUnits) {
			this.ids = ids;
			this.agents = agents;
			this.agentDetails = agentDetails;
			this.matrices = matrices;
			this.matrixDetails = matrixDetails;
			this.comments = comments;
			this.qualityScores = qualityScores;
			this.checks = checks;
			this.timeSeries = timeSeries;
			this.conditions = conditions;
			this.conditionValues = conditionValues;
			this.conditionUnits = conditionUnits;
			addedConditions = new ArrayList<>();
			addedConditionValues = new ArrayList<>();
			addedConditionUnits = new ArrayList<>();
		}

		public void addCondition(MiscXml condition, Map<String, Double> values,
				Map<String, String> units) {
			List<Double> valueList = new ArrayList<>();
			List<String> unitList = new ArrayList<>();
			String standardUnit = null;

			if (!condition.getCategories().isEmpty()) {
				standardUnit = Categories.getCategory(
						condition.getCategories().get(0)).getStandardUnit();
			}

			for (String id : ids) {
				valueList.add(values.get(id));

				if (units.containsKey(id)) {
					unitList.add(units.get(id));
				} else {
					unitList.add(standardUnit);
				}
			}

			addedConditions.add(condition);
			addedConditionValues.add(valueList);
			addedConditionUnits.add(unitList);
			fireTableStructureChanged();
		}

		public void removeCondition(int id) {
			for (int i = 0; i < addedConditions.size(); i++) {
				if (addedConditions.get(i).getID() == id) {
					addedConditions.remove(i);
					addedConditionValues.remove(i);
					addedConditionUnits.remove(i);
					break;
				}
			}

			fireTableStructureChanged();
		}

		public Map<Integer, MiscXml> getAddedConditionMap() {
			Map<Integer, MiscXml> map = new LinkedHashMap<>();

			for (MiscXml cond : addedConditions) {
				map.put(cond.getID(), cond);
			}

			return map;
		}

		public Map<Integer, Map<String, Double>> getAddedConditionValueMap() {
			Map<Integer, Map<String, Double>> valueMap = new LinkedHashMap<>();

			for (int i = 0; i < addedConditions.size(); i++) {
				valueMap.put(addedConditions.get(i).getID(),
						new LinkedHashMap<String, Double>());

				for (int j = 0; j < ids.size(); j++) {
					valueMap.get(addedConditions.get(i).getID()).put(
							ids.get(j), addedConditionValues.get(i).get(j));
				}
			}

			return valueMap;
		}

		public Map<Integer, Map<String, String>> getAddedConditionUnitMap() {
			Map<Integer, Map<String, String>> unitMap = new LinkedHashMap<>();

			for (int i = 0; i < addedConditions.size(); i++) {
				unitMap.put(addedConditions.get(i).getID(),
						new LinkedHashMap<String, String>());

				for (int j = 0; j < ids.size(); j++) {
					unitMap.get(addedConditions.get(i).getID()).put(ids.get(j),
							addedConditionUnits.get(i).get(j));
				}
			}

			return unitMap;
		}

		public Map<Integer, MiscXml> getConditionMap() {
			Map<Integer, MiscXml> map = new LinkedHashMap<>();

			for (MiscXml cond : conditions) {
				map.put(cond.getID(), cond);
			}

			return map;
		}

		public Map<Integer, Map<String, Double>> getConditionValueMap() {
			Map<Integer, Map<String, Double>> valueMap = new LinkedHashMap<>();

			for (int i = 0; i < conditions.size(); i++) {
				valueMap.put(conditions.get(i).getID(),
						new LinkedHashMap<String, Double>());

				for (int j = 0; j < ids.size(); j++) {
					valueMap.get(conditions.get(i).getID()).put(ids.get(j),
							conditionValues.get(i).get(j));
				}
			}

			return valueMap;
		}

		public Map<Integer, Map<String, String>> getConditionUnitMap() {
			Map<Integer, Map<String, String>> unitMap = new LinkedHashMap<>();

			for (int i = 0; i < conditions.size(); i++) {
				unitMap.put(conditions.get(i).getID(),
						new LinkedHashMap<String, String>());

				for (int j = 0; j < ids.size(); j++) {
					unitMap.get(conditions.get(i).getID()).put(ids.get(j),
							conditionUnits.get(i).get(j));
				}
			}

			return unitMap;
		}

		public Map<String, AgentXml> getAgentMap() {
			Map<String, AgentXml> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), agents.get(i));
			}

			return map;
		}

		public Map<String, String> getAgentDetailMap() {
			Map<String, String> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), agentDetails.get(i));
			}

			return map;
		}

		public Map<String, MatrixXml> getMatrixMap() {
			Map<String, MatrixXml> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), matrices.get(i));
			}

			return map;
		}

		public Map<String, String> getMatrixDetailMap() {
			Map<String, String> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), matrixDetails.get(i));
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

		public Map<String, Integer> getQualityScoreMap() {
			Map<String, Integer> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), qualityScores.get(i));
			}

			return map;
		}

		public Map<String, Boolean> getCheckedMap() {
			Map<String, Boolean> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), checks.get(i));
			}

			return map;
		}

		public Map<String, List<TimeSeriesXml>> getTimeSeriesMap() {
			Map<String, List<TimeSeriesXml>> map = new LinkedHashMap<>();

			for (int i = 0; i < getRowCount(); i++) {
				map.put(ids.get(i), timeSeries.get(i));
			}

			return map;
		}

		@Override
		public int getRowCount() {
			return ids.size();
		}

		@Override
		public int getColumnCount() {
			return addedConditions.size() * 2 + conditions.size() * 2 + 9;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ids.get(rowIndex);
			case 1:
				return agents.get(rowIndex);
			case 2:
				return agentDetails.get(rowIndex);
			case 3:
				return matrices.get(rowIndex);
			case 4:
				return matrixDetails.get(rowIndex);
			case 5:
				return comments.get(rowIndex);
			case 6:
				return qualityScores.get(rowIndex);
			case 7:
				return checks.get(rowIndex);
			case 8:
				return timeSeries.get(rowIndex);
			default:
				int i = columnIndex - 9;

				if (i < conditions.size() * 2) {
					if (i % 2 == 0) {
						return conditionValues.get(i / 2).get(rowIndex);
					} else {
						return conditionUnits.get(i / 2).get(rowIndex);
					}
				} else {
					i -= conditions.size() * 2;

					if (i % 2 == 0) {
						return addedConditionValues.get(i / 2).get(rowIndex);
					} else {
						return addedConditionUnits.get(i / 2).get(rowIndex);
					}
				}
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
				return AttributeUtilities.AGENT_DETAILS;
			case 3:
				return TimeSeriesSchema.ATT_MATRIX;
			case 4:
				return AttributeUtilities.MATRIX_DETAILS;
			case 5:
				return MdInfoXml.ATT_COMMENT;
			case 6:
				return MdInfoXml.ATT_QUALITYSCORE;
			case 7:
				return MdInfoXml.ATT_CHECKED;
			case 8:
				return TimeSeriesSchema.ATT_TIMESERIES;
			default:
				int i = column - 9;

				if (i < conditions.size() * 2) {
					String cond = conditions.get(i / 2).getName();

					if (i % 2 == 0) {
						return cond;
					} else {
						return cond + " Unit";
					}
				} else {
					i -= conditions.size() * 2;

					String cond = addedConditions.get(i / 2).getName();

					if (i % 2 == 0) {
						return cond;
					} else {
						return cond + " Unit";
					}
				}
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return AgentXml.class;
			case 2:
				return String.class;
			case 3:
				return MatrixXml.class;
			case 4:
				return String.class;
			case 5:
				return String.class;
			case 6:
				return Integer.class;
			case 7:
				return Boolean.class;
			case 8:
				return List.class;
			default:
				int i = columnIndex - 9;

				if (i % 2 == 0) {
					return Double.class;
				} else {
					return String.class;
				}
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex != 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				break;
			case 1:
				agents.set(rowIndex, (AgentXml) aValue);
				break;
			case 2:
				agentDetails.set(rowIndex, (String) aValue);
				break;
			case 3:
				matrices.set(rowIndex, (MatrixXml) aValue);
				break;
			case 4:
				matrixDetails.set(rowIndex, (String) aValue);
				break;
			case 5:
				comments.set(rowIndex, (String) aValue);
				break;
			case 6:
				qualityScores.set(rowIndex, (Integer) aValue);
				break;
			case 7:
				checks.set(rowIndex, (Boolean) aValue);
				break;
			case 8:
				timeSeries.set(rowIndex, (List<TimeSeriesXml>) aValue);
				break;
			default:
				int i = columnIndex - 9;

				if (i < conditions.size() * 2) {
					if (i % 2 == 0) {
						conditionValues.get(i / 2).set(rowIndex,
								(Double) aValue);
					} else {
						conditionUnits.get(i / 2)
								.set(rowIndex, (String) aValue);
					}
				} else {
					i -= conditions.size() * 2;

					if (i % 2 == 0) {
						addedConditionValues.get(i / 2).set(rowIndex,
								(Double) aValue);
					} else {
						addedConditionUnits.get(i / 2).set(rowIndex,
								(String) aValue);
					}
				}
			}

			fireTableDataChanged();
		}

	}

	private class AgentRenderer implements TableCellRenderer {

		private JLabel label;

		public AgentRenderer() {
			label = new JLabel();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			AgentXml agent = (AgentXml) value;

			if (agent != null) {
				label.setText(agent.getName());
			} else {
				label.setText("");
			}

			return label;
		}
	}

	private class AgentEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private AgentXml agent;

		public AgentEditor() {
			button = new JButton();
			button.addActionListener(this);
			agent = null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			agent = (AgentXml) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return agent;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
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
				stopCellEditing();
			}
		}
	}

	private class MatrixRenderer implements TableCellRenderer {

		private JLabel label;

		public MatrixRenderer() {
			label = new JLabel();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			MatrixXml matrix = (MatrixXml) value;

			if (matrix != null) {
				label.setText(matrix.getName());
			} else {
				label.setText("");
			}

			return label;
		}
	}

	private class MatrixEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private MatrixXml matrix;

		public MatrixEditor() {
			button = new JButton();
			button.addActionListener(this);
			matrix = null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			matrix = (MatrixXml) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return matrix;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
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
				stopCellEditing();
			}
		}
	}

	private class QualityScoreRenderer extends JComponent implements
			TableCellRenderer {

		private static final long serialVersionUID = 1L;

		private Color color;

		public QualityScoreRenderer() {
			color = Color.WHITE;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Integer score = (Integer) value;

			if (score == null) {
				color = Color.WHITE;
			} else if (score == 1) {
				color = Color.GREEN;
			} else if (score == 2) {
				color = Color.YELLOW;
			} else if (score == 3) {
				color = Color.RED;
			}

			return this;
		}

		@Override
		protected void paintComponent(Graphics g) {
			Rectangle rect = g.getClipBounds();

			if (rect != null) {
				g.setColor(color);
				g.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	private class QualityScoreEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		private static final long serialVersionUID = 1L;

		private Color color;

		public QualityScoreEditor() {
			color = Color.WHITE;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			Integer score = (Integer) value;

			if (score == null) {
				color = Color.WHITE;
			} else if (score == 1) {
				color = Color.GREEN;
			} else if (score == 2) {
				color = Color.YELLOW;
			} else if (score == 3) {
				color = Color.RED;
			}

			JComboBox<Color> box = new JComboBox<>(new Color[] { Color.WHITE,
					Color.GREEN, Color.YELLOW, Color.RED });

			box.setSelectedItem(color);
			box.setRenderer(new ColorListRenderer());
			box.addActionListener(this);

			return box;
		}

		@Override
		public Object getCellEditorValue() {
			if (color.equals(Color.WHITE)) {
				return null;
			} else if (color.equals(Color.GREEN)) {
				return 1;
			} else if (color.equals(Color.YELLOW)) {
				return 2;
			} else if (color.equals(Color.RED)) {
				return 3;
			}

			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			color = (Color) ((JComboBox<Color>) e.getSource())
					.getSelectedItem();
			stopCellEditing();
		}

		private class ColorListRenderer extends DefaultListCellRenderer {

			private static final long serialVersionUID = 1L;

			private Color color;
			private boolean isSelected;

			public ColorListRenderer() {
				color = Color.WHITE;
				isSelected = false;
			}

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				color = (Color) value;
				this.isSelected = isSelected;

				return super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
			}

			@Override
			protected void paintComponent(Graphics g) {
				Rectangle rect = g.getClipBounds();

				if (rect != null) {
					g.setColor(color);
					g.fillRect(rect.x, rect.y, rect.width, rect.height);

					if (isSelected) {
						g.setColor(UIManager.getDefaults().getColor(
								"List.selectionBackground"));
					} else {
						g.setColor(UIManager.getDefaults().getColor(
								"List.background"));
					}

					((Graphics2D) g).setStroke(new BasicStroke(5));
					g.drawRect(rect.x, rect.y, rect.width, rect.height);
				}
			}
		}
	}

	private class TimeSeriesRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			return new JButton("View");
		}
	}

	private class TimeSeriesEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private List<TimeSeriesXml> timeSeries;

		public TimeSeriesEditor() {
			button = new JButton("View");
			button.addActionListener(this);
			timeSeries = new ArrayList<>();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			timeSeries = (List<TimeSeriesXml>) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return timeSeries;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			TimeSeriesDialog dialog = new TimeSeriesDialog(button, timeSeries,
					true);

			dialog.setVisible(true);
		}
	}

}
