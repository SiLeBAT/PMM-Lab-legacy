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
package de.bund.bfr.knime.pmm.modelanddatajoiner;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * <code>NodeDialog</code> for the "ModelAndDataJoiner" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class ModelAndDataJoinerNodeDialog extends DataAwareNodeDialogPane
		implements ActionListener {

	private JComboBox<String> joinerBox;
	private JPanel joinerPanel;

	private Joiner joiner;

	private String joinType;
	private String assignments;
	private int joinSameConditions;

	private BufferedDataTable[] input;

	/**
	 * New pane for configuring the ModelAndDataJoiner node.
	 */
	protected ModelAndDataJoinerNodeDialog() {
		JPanel panel = new JPanel();
		JPanel upperPanel = new JPanel();

		joinerBox = new JComboBox<String>(new String[] {
				ModelAndDataJoinerNodeModel.PRIMARY_JOIN,
				ModelAndDataJoinerNodeModel.SECONDARY_JOIN,
				ModelAndDataJoinerNodeModel.COMBINED_JOIN });
		joinerBox.addActionListener(this);
		joinerPanel = new JPanel();
		joinerPanel.setBorder(BorderFactory.createTitledBorder("Join Options"));
		joinerPanel.setLayout(new BorderLayout());

		upperPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		upperPanel.add(joinerBox);

		panel.setLayout(new BorderLayout());
		panel.add(upperPanel, BorderLayout.NORTH);
		panel.add(joinerPanel, BorderLayout.CENTER);
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		this.input = input;

		try {
			joinType = settings
					.getString(ModelAndDataJoinerNodeModel.CFGKEY_JOINTYPE);
		} catch (InvalidSettingsException e) {
			joinType = ModelAndDataJoinerNodeModel.DEFAULT_JOINTYPE;
		}

		try {
			assignments = settings
					.getString(ModelAndDataJoinerNodeModel.CFGKEY_ASSIGNMENTS);
		} catch (InvalidSettingsException e) {
			assignments = "";
		}

		try {
			joinSameConditions = settings
					.getInt(ModelAndDataJoinerNodeModel.CFGKEY_JOINSAMECONDITIONS);
		} catch (InvalidSettingsException e) {
			joinSameConditions = ModelAndDataJoinerNodeModel.DEFAULT_JOINSAMECONDITIONS;
		}

		if (joinType.equals(ModelAndDataJoinerNodeModel.NO_JOIN)) {
			if (SchemaFactory.createM2Schema().conforms(input[0])
					&& SchemaFactory.createM1DataSchema().conforms(input[1])) {
				joinType = ModelAndDataJoinerNodeModel.SECONDARY_JOIN;
			} else if (SchemaFactory.createM12Schema().conforms(input[0])
					&& SchemaFactory.createDataSchema().conforms(input[1])) {
				joinType = ModelAndDataJoinerNodeModel.COMBINED_JOIN;
			} else {
				joinType = ModelAndDataJoinerNodeModel.PRIMARY_JOIN;
			}
		}

		initGUI();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (joiner == null || !joiner.isValid()) {
			throw new InvalidSettingsException("");
		}

		settings.addString(ModelAndDataJoinerNodeModel.CFGKEY_JOINTYPE,
				joinType);
		settings.addString(ModelAndDataJoinerNodeModel.CFGKEY_ASSIGNMENTS,
				joiner.getAssignments());

		if (joinType.equals(ModelAndDataJoinerNodeModel.PRIMARY_JOIN)) {
			if (((PrimaryJoiner) joiner).isJoinSameConditions()) {
				settings.addInt(
						ModelAndDataJoinerNodeModel.CFGKEY_JOINSAMECONDITIONS,
						1);
			} else {
				settings.addInt(
						ModelAndDataJoinerNodeModel.CFGKEY_JOINSAMECONDITIONS,
						0);
			}
		} else {
			settings.addInt(
					ModelAndDataJoinerNodeModel.CFGKEY_JOINSAMECONDITIONS,
					ModelAndDataJoinerNodeModel.DEFAULT_JOINSAMECONDITIONS);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		joinType = (String) joinerBox.getSelectedItem();
		initGUI();
	}

	private void initGUI() {
		joinerBox.removeActionListener(this);
		joinerBox.setSelectedItem(joinType);
		joinerBox.addActionListener(this);
		joiner = null;

		if (joinType.equals(ModelAndDataJoinerNodeModel.PRIMARY_JOIN)) {
			if (SchemaFactory.createM1Schema().conforms(input[0])
					&& SchemaFactory.createDataSchema().conforms(input[1])) {
				joiner = new PrimaryJoiner(input[0], input[1],
						joinSameConditions == 1);
			}
		} else if (joinType.equals(ModelAndDataJoinerNodeModel.SECONDARY_JOIN)) {
			if (SchemaFactory.createM2Schema().conforms(input[0])
					&& SchemaFactory.createM1DataSchema().conforms(input[1])) {
				joiner = new SecondaryJoiner(input[0], input[1]);
			}
		} else if (joinType.equals(ModelAndDataJoinerNodeModel.COMBINED_JOIN)) {
			if (SchemaFactory.createM12Schema().conforms(input[0])
					&& SchemaFactory.createDataSchema().conforms(input[1])) {
				joiner = new CombinedJoiner(input[0], input[1]);
			}
		}

		joinerPanel.removeAll();

		if (joiner != null) {
			joinerPanel.add(joiner.createPanel(assignments),
					BorderLayout.CENTER);
			joinerPanel.revalidate();
		} else {
			if (joinerBox.isValid()) {
				JOptionPane.showMessageDialog(joinerBox,
						"Data is not valid for " + joinType);
			}

			joinerPanel.add(new JLabel(), BorderLayout.CENTER);
		}
	}
}
