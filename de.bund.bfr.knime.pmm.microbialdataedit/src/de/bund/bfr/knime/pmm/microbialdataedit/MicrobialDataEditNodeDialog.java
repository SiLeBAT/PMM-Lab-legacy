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
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.TimeSeriesDialog;

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
		Map<MiscXml, Map<String, Double>> addedConditions;
		Map<MiscXml, Map<String, Double>> conditions;
		Map<String, AgentXml> agents;
		Map<String, String> agentDetails;
		Map<String, MatrixXml> matrices;
		Map<String, String> matrixDetails;
		Map<String, String> comments;
		Map<String, Integer> qualityScores;
		Map<String, Boolean> checks;
		Map<String, List<TimeSeriesXml>> timeSeries;

		try {
			addedConditions = XmlConverter
					.xmlToMiscStringDoubleMap(settings
							.getString(MicrobialDataEditNodeModel.CFGKEY_ADDEDCONDITIONS));
		} catch (InvalidSettingsException e) {
			addedConditions = new LinkedHashMap<>();
		}

		try {
			conditions = XmlConverter.xmlToMiscStringDoubleMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_CONDITIONS));
		} catch (InvalidSettingsException e) {
			conditions = new LinkedHashMap<>();
		}

		try {
			agents = XmlConverter.xmlToAgentMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_AGENTS));
		} catch (InvalidSettingsException e) {
			agents = new LinkedHashMap<>();
		}

		try {
			agentDetails = XmlConverter.xmlToStringMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_AGENTDETAILS));
		} catch (InvalidSettingsException e1) {
			agentDetails = new LinkedHashMap<>();
		}

		try {
			matrices = XmlConverter.xmlToMatrixMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_MATRICES));
		} catch (InvalidSettingsException e) {
			matrices = new LinkedHashMap<>();
		}

		try {
			matrixDetails = XmlConverter
					.xmlToStringMap(settings
							.getString(MicrobialDataEditNodeModel.CFGKEY_MATRIXDETAILS));
		} catch (InvalidSettingsException e1) {
			matrixDetails = new LinkedHashMap<>();
		}

		try {
			comments = XmlConverter.xmlToStringMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_COMMENTS));
		} catch (InvalidSettingsException e) {
			comments = new LinkedHashMap<>();
		}

		try {
			qualityScores = XmlConverter
					.xmlToIntMap(settings
							.getString(MicrobialDataEditNodeModel.CFGKEY_QUALITYSCORES));
		} catch (InvalidSettingsException e) {
			qualityScores = new LinkedHashMap<>();
		}

		try {
			checks = XmlConverter.xmlToBoolMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_CHECKS));
		} catch (InvalidSettingsException e) {
			checks = new LinkedHashMap<>();
		}

		try {
			timeSeries = XmlConverter.xmlToTimeSeriesMap(settings
					.getString(MicrobialDataEditNodeModel.CFGKEY_TIMESERIES));
		} catch (InvalidSettingsException e) {
			timeSeries = new LinkedHashMap<>();
		}

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

			if (agents.containsKey(id)) {
				agentList.add(agents.get(id));
			} else {
				agentList.add((AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0));
			}

			if (agentDetails.containsKey(id)) {
				agentDetailList.add(agentDetails.get(id));
			} else {
				agentDetailList.add(((AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0)).getDetail());
			}

			if (matrices.containsKey(id)) {
				matrixList.add(matrices.get(id));
			} else {
				matrixList.add((MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0));
			}

			if (matrixDetails.containsKey(id)) {
				matrixDetailList.add(matrixDetails.get(id));
			} else {
				matrixDetailList.add(((MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0)).getDetail());
			}

			if (comments.containsKey(id)) {
				commentList.add(comments.get(id));
			} else {
				commentList.add(infoXml.getComment());
			}

			if (qualityScores.containsKey(id)) {
				qualityScoreList.add(qualityScores.get(id));
			} else {
				qualityScoreList.add(infoXml.getQualityScore());
			}

			if (checks.containsKey(id)) {
				checkedList.add(checks.get(id));
			} else {
				checkedList.add(infoXml.getChecked());
			}

			if (timeSeries.containsKey(id)) {
				timeSeriesList.add(timeSeries.get(id));
			} else {
				timeSeriesList.add(series);
			}

			for (int i = 0; i < usedMiscs.size(); i++) {
				boolean valueFound = false;
				Double value = null;

				for (MiscXml cond : conditions.keySet()) {
					if (cond.getID() == usedMiscs.get(i).getID()) {
						if (conditions.get(cond).containsKey(id)) {
							valueFound = true;
							value = conditions.get(cond).get(id);
						}

						break;
					}
				}

				if (!valueFound) {
					for (PmmXmlElementConvertable el : miscXml.getElementSet()) {
						MiscXml cond = (MiscXml) el;

						if (cond.getID() == usedMiscs.get(i).getID()) {
							value = cond.getValue();
							break;
						}
					}
				}

				usedMiscValues.get(i).add(value);
			}
		}

		for (int id : usedMiscIDs) {
			for (MiscXml misc : addedConditions.keySet()) {
				if (misc.getID() == id) {
					addedConditions.remove(misc);
					break;
				}
			}
		}

		for (MiscXml misc : addedConditions.keySet()) {
			addedConditionIDs.add(misc.getID());
			addedConditionNames.add(misc.getName());
		}

		addedConditionsList.setListData(addedConditionNames
				.toArray(new String[0]));

		EditTable tableModel = new EditTable(ids, agentList, agentDetailList,
				matrixList, matrixDetailList, commentList, qualityScoreList,
				checkedList, timeSeriesList, usedMiscs, usedMiscValues);

		for (MiscXml misc : addedConditions.keySet()) {
			tableModel.addCondition(misc, addedConditions.get(misc));
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
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_ADDEDCONDITIONS,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getAddedConditionMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_CONDITIONS,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getConditionMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_AGENTS,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getAgentMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_AGENTDETAILS,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getAgentDetailMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_MATRICES,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getMatrixMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_MATRIXDETAILS,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getMatrixDetailMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_COMMENTS,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getCommentMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_QUALITYSCORES,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getQualityScoreMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_CHECKS,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getCheckedMap()));
		settings.addString(MicrobialDataEditNodeModel.CFGKEY_TIMESERIES,
				XmlConverter.mapToXml(((EditTable) table.getModel())
						.getTimeSeriesMap()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			Integer id = DBKernel.openMiscDBWindow(null);

			if (id != null && !addedConditionIDs.contains(id)) {
				String name = DBKernel.getValue("SonstigeParameter", "ID", id
						+ "", "Parameter")
						+ "";

				if (!usedMiscIDs.contains(id)) {
					addedConditionNames.add(name);
					addedConditionsList.setListData(addedConditionNames
							.toArray(new String[0]));
					((EditTable) table.getModel()).addCondition(new MiscXml(id,
							name, null, null, null, DBKernel.getLocalDBUUID()),
							new LinkedHashMap<String, Double>());
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
		private List<MiscXml> addedConditions;
		private List<List<Double>> addedConditionValues;

		public EditTable(List<String> ids, List<AgentXml> agents,
				List<String> agentDetails, List<MatrixXml> matrices,
				List<String> matrixDetails, List<String> comments,
				List<Integer> qualityScores, List<Boolean> checks,
				List<List<TimeSeriesXml>> timeSeries, List<MiscXml> conditions,
				List<List<Double>> conditionValues) {
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
			addedConditions = new ArrayList<>();
			addedConditionValues = new ArrayList<>();
		}

		public void addCondition(MiscXml condition, Map<String, Double> values) {
			List<Double> valueList = new ArrayList<>();

			for (String id : ids) {
				valueList.add(values.get(id));
			}

			addedConditions.add(condition);
			addedConditionValues.add(valueList);
			fireTableStructureChanged();
		}

		public void removeCondition(int id) {
			for (int i = 0; i < addedConditions.size(); i++) {
				if (addedConditions.get(i).getID() == id) {
					addedConditions.remove(i);
					addedConditionValues.remove(i);
					break;
				}
			}

			fireTableStructureChanged();
		}

		public Map<MiscXml, Map<String, Double>> getAddedConditionMap() {
			Map<MiscXml, Map<String, Double>> valueMap = new LinkedHashMap<>();

			for (int i = 0; i < addedConditions.size(); i++) {
				valueMap.put(addedConditions.get(i),
						new LinkedHashMap<String, Double>());

				for (int j = 0; j < ids.size(); j++) {
					valueMap.get(addedConditions.get(i)).put(ids.get(j),
							addedConditionValues.get(i).get(j));
				}
			}

			return valueMap;
		}

		public Map<MiscXml, Map<String, Double>> getConditionMap() {
			Map<MiscXml, Map<String, Double>> valueMap = new LinkedHashMap<>();

			for (int i = 0; i < conditions.size(); i++) {
				valueMap.put(conditions.get(i),
						new LinkedHashMap<String, Double>());

				for (int j = 0; j < ids.size(); j++) {
					valueMap.get(conditions.get(i)).put(ids.get(j),
							conditionValues.get(i).get(j));
				}
			}

			return valueMap;
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
			return addedConditions.size() + conditions.size() + 9;
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
				if (columnIndex - 9 < conditions.size()) {
					return conditionValues.get(columnIndex - 9).get(rowIndex);
				} else {
					return addedConditionValues.get(
							columnIndex - conditions.size() - 9).get(rowIndex);
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
				if (column - 9 < conditions.size()) {
					return conditions.get(column - 9).getName();
				} else {
					return addedConditions.get(column - conditions.size() - 9)
							.getName();
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
				return Double.class;
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
				if (columnIndex - 9 < conditions.size()) {
					conditionValues.get(columnIndex - 9).set(rowIndex,
							(Double) aValue);
				} else {
					addedConditionValues.get(
							columnIndex - conditions.size() - 9).set(rowIndex,
							(Double) aValue);
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
