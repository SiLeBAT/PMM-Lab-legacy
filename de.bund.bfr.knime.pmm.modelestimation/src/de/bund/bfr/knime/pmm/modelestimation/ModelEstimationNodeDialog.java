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
package de.bund.bfr.knime.pmm.modelestimation;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "ModelEstimation" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class ModelEstimationNodeDialog extends NodeDialogPane {

	private JCheckBox limitsBox;
	private JCheckBox oneStepBox;

	/**
	 * New pane for configuring the ModelEstimation node.
	 */
	protected ModelEstimationNodeDialog() {
		JPanel panel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel westPanel = new JPanel();

		limitsBox = new JCheckBox("Enforce Limits",
				ModelEstimationNodeModel.DEFAULT_ENFORCELIMITS == 1);
		oneStepBox = new JCheckBox("Use One Step Method",
				ModelEstimationNodeModel.DEFAULT_ONESTEPMETHOD == 1);

		westPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		westPanel.setLayout(new GridLayout(2, 1, 5, 5));
		westPanel.add(limitsBox);
		westPanel.add(oneStepBox);

		northPanel.setLayout(new BorderLayout());
		northPanel.add(westPanel, BorderLayout.WEST);

		panel.setLayout(new BorderLayout());
		panel.add(northPanel, BorderLayout.NORTH);
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		try {
			limitsBox
					.setSelected(settings
							.getInt(ModelEstimationNodeModel.CFGKEY_ENFORCELIMITS) == 1);
		} catch (InvalidSettingsException e) {
			limitsBox
					.setSelected(ModelEstimationNodeModel.DEFAULT_ENFORCELIMITS == 1);
		}

		try {
			oneStepBox
					.setSelected(settings
							.getInt(ModelEstimationNodeModel.CFGKEY_ONESTEPMETHOD) == 1);
		} catch (InvalidSettingsException e) {
			oneStepBox
					.setSelected(ModelEstimationNodeModel.DEFAULT_ONESTEPMETHOD == 1);
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (limitsBox.isSelected()) {
			settings.addInt(ModelEstimationNodeModel.CFGKEY_ENFORCELIMITS, 1);
		} else {
			settings.addInt(ModelEstimationNodeModel.CFGKEY_ENFORCELIMITS, 0);
		}

		if (oneStepBox.isSelected()) {
			settings.addInt(ModelEstimationNodeModel.CFGKEY_ONESTEPMETHOD, 1);
		} else {
			settings.addInt(ModelEstimationNodeModel.CFGKEY_ONESTEPMETHOD, 0);
		}
	}
}
