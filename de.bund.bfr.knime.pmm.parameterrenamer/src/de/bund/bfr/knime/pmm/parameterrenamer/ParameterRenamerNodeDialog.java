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
package de.bund.bfr.knime.pmm.parameterrenamer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;

/**
 * <code>NodeDialog</code> for the "ParameterRenamer" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class ParameterRenamerNodeDialog extends DataAwareNodeDialogPane
		implements ActionListener {

	private KnimeSchema schema;

	private List<String> parameters;
	private List<JLabel> paramLabels;
	private List<JComboBox> paramBoxes;

	private boolean isValid;

	/**
	 * New pane for configuring the ParameterRenamer node.
	 */
	protected ParameterRenamerNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);

		schema = new Model1Schema();
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		isValid = true;

		try {
			parameters = getParameters(input[0]);
		} catch (PmmException e) {
			e.printStackTrace();
		}

		List<String> assignments;

		try {
			assignments = new ArrayList<String>(
					Arrays.asList(settings
							.getStringArray(ParameterRenamerNodeModel.CFGKEY_ASSIGNMENTS)));
		} catch (InvalidSettingsException e) {
			assignments = new ArrayList<String>();
		}

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createPanel(assignments));
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!isValid) {
			throw new InvalidSettingsException("");
		}

		List<String> assignments = getAssignmentsFromFrame();

		if (assignments != null) {
			settings.addStringArray(
					ParameterRenamerNodeModel.CFGKEY_ASSIGNMENTS,
					assignments.toArray(new String[0]));
		}
	}

	private List<String> getParameters(BufferedDataTable table)
			throws PmmException {
		Set<String> paramSet = new LinkedHashSet<String>();
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);

		while (reader.hasMoreElements()) {
			KnimeTuple row = reader.nextElement();
			List<String> params = row.getStringList(Model1Schema.ATT_PARAMNAME);

			paramSet.addAll(params);
		}

		return new ArrayList<String>(paramSet);
	}

	private JPanel createPanel(List<String> assignments) {
		JPanel centerPanel = new JPanel();
		JPanel parameterPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel okCancelPanel = new JPanel();
		Map<String, String> replacements = new HashMap<String, String>();

		for (String assign : assignments) {
			String[] toks = assign.split("=");

			replacements.put(toks[0], toks[1]);
		}

		centerPanel.setLayout(new BorderLayout());
		parameterPanel.setLayout(new BorderLayout());
		leftPanel.setLayout(new GridLayout(parameters.size(), 1, 5, 5));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new GridLayout(parameters.size(), 1, 5, 5));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		okCancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		paramLabels = new ArrayList<JLabel>();
		paramBoxes = new ArrayList<JComboBox>();

		for (String param : parameters) {
			List<String> choices = new ArrayList<String>();

			choices.add("");
			choices.addAll(parameters);

			JLabel label = new JLabel(param + ":");
			JComboBox box = new JComboBox(choices.toArray(new String[0]));
			String replaceParam = replacements.get(param);

			if (parameters.contains(replaceParam)) {
				box.setSelectedItem(replaceParam);
			}

			box.addActionListener(this);
			paramLabels.add(label);
			paramBoxes.add(box);
			leftPanel.add(label);
			rightPanel.add(box);
		}

		parameterPanel.add(leftPanel, BorderLayout.WEST);
		parameterPanel.add(rightPanel, BorderLayout.EAST);
		centerPanel.add(parameterPanel, BorderLayout.NORTH);

		return centerPanel;
	}

	private List<String> getAssignmentsFromFrame() {
		List<String> replacements = new ArrayList<String>();

		for (int i = 0; i < parameters.size(); i++) {
			if (!paramBoxes.get(i).getSelectedItem().equals("")) {
				replacements.add(parameters.get(i) + "="
						+ paramBoxes.get(i).getSelectedItem());
			}
		}

		return replacements;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		isValid = true;

		for (int i = 0; i < parameters.size(); i++) {
			paramLabels.get(i).setForeground(Color.BLACK);
			paramBoxes.get(i).setForeground(Color.BLACK);
		}

		for (JComboBox box : paramBoxes) {
			String selected = (String) box.getSelectedItem();
			int index = parameters.indexOf(selected);

			if (index != -1
					&& !paramBoxes.get(index).getSelectedItem().equals("")) {
				box.setForeground(Color.RED);
				paramLabels.get(index).setForeground(Color.RED);
				isValid = false;
			}
		}
	}
}
