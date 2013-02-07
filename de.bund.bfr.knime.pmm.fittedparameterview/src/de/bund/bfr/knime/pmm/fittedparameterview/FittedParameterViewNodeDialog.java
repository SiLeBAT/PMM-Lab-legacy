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
package de.bund.bfr.knime.pmm.fittedparameterview;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knime.core.data.DataTable;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.CollectionUtilities;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * <code>NodeDialog</code> for the "FittedParameterView" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class FittedParameterViewNodeDialog extends DataAwareNodeDialogPane
		implements ActionListener {

	private List<String> allConditions;
	private List<String> usedConditions;

	private JPanel panel;
	private JList<String> conditionList;
	private JButton addButton;
	private JButton removeButton;

	/**
	 * New pane for configuring the FittedParameterView node.
	 */
	protected FittedParameterViewNodeDialog() {
		conditionList = new JList<>();
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		removeButton = new JButton("Remove");
		removeButton.addActionListener(this);

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);

		panel = new JPanel();
		panel.setBorder(BorderFactory
				.createTitledBorder("Conditions to use (for x-axis)"));
		panel.setLayout(new BorderLayout());
		panel.add(buttonPanel, BorderLayout.NORTH);
		panel.add(new JScrollPane(conditionList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		try {
			allConditions = getAllMiscParams(input[0]);
		} catch (PmmException e1) {
			e1.printStackTrace();
		}

		try {
			List<String> conditions = CollectionUtilities
					.getStringListFromString(settings
							.getString(FittedParameterViewNodeModel.CFG_USEDCONDITIONS));
			usedConditions = new ArrayList<>();

			for (String cond : conditions) {
				if (allConditions.contains(cond)) {
					usedConditions.add(cond);
				}
			}

			conditionList.setListData(usedConditions.toArray(new String[0]));
		} catch (InvalidSettingsException e) {
			conditionList.setListData(new String[0]);
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (usedConditions.isEmpty()) {
			throw new InvalidSettingsException(
					"List of conditions cannot be empty");
		}

		settings.addString(FittedParameterViewNodeModel.CFG_USEDCONDITIONS,
				CollectionUtilities.getStringFromList(usedConditions));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			Object selection = JOptionPane.showInputDialog(panel,
					"Select Condition", "Input", JOptionPane.QUESTION_MESSAGE,
					null, allConditions.toArray(), allConditions.get(0));

			if (selection != null && !usedConditions.contains(selection)) {
				usedConditions.add((String) selection);
				conditionList
						.setListData(usedConditions.toArray(new String[0]));
			}
		} else if (e.getSource() == removeButton) {
			List<String> selectedConditions = conditionList
					.getSelectedValuesList();

			if (!selectedConditions.isEmpty()) {
				usedConditions.removeAll(selectedConditions);
				conditionList
						.setListData(usedConditions.toArray(new String[0]));
			}
		}
	}

	private List<String> getAllMiscParams(DataTable table) throws PmmException {
		KnimeRelationReader reader = new KnimeRelationReader(
				new TimeSeriesSchema(), table);
		Set<String> paramSet = new LinkedHashSet<String>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				paramSet.add(element.getName());
			}
		}

		return new ArrayList<String>(paramSet);
	}

}
