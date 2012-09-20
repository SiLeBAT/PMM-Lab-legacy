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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

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
public class ModelAndDataJoinerNodeDialog extends DataAwareNodeDialogPane {

	private Joiner joiner;

	/**
	 * New pane for configuring the ModelAndDataJoiner node.
	 */
	protected ModelAndDataJoinerNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		List<String> assignments;
		int joinSameConditions;

		try {
			assignments = new ArrayList<String>(
					Arrays.asList(settings
							.getStringArray(ModelAndDataJoinerNodeModel.CFGKEY_ASSIGNMENTS)));
		} catch (InvalidSettingsException e) {
			assignments = new ArrayList<String>();
		}

		try {
			joinSameConditions = settings
					.getInt(ModelAndDataJoinerNodeModel.CFGKEY_JOINSAMECONDITIONS);
		} catch (InvalidSettingsException e) {
			joinSameConditions = ModelAndDataJoinerNodeModel.DEFAULT_JOINSAMECONDITIONS;
		}

		try {
			KnimeSchema model1Schema = new Model1Schema();
			KnimeSchema model2Schema = new Model2Schema();
			KnimeSchema model12Schema = new KnimeSchema(new Model1Schema(),
					new Model2Schema());
			KnimeSchema dataSchema = new TimeSeriesSchema();
			KnimeSchema peiSchema = new KnimeSchema(new Model1Schema(),
					new TimeSeriesSchema());

			if (model2Schema.conforms(input[0].getSpec())
					&& peiSchema.conforms(input[1].getSpec())) {
				joiner = new SecondaryJoiner(input[0], input[1]);
			} else if (model12Schema.conforms(input[0].getSpec())
					&& dataSchema.conforms(input[1].getSpec())) {
				joiner = new CombinedJoiner(input[0], input[1]);
			} else if (model1Schema.conforms(input[0].getSpec())
					&& dataSchema.conforms(input[1].getSpec())) {
				joiner = new PrimaryJoiner(input[0], input[1],
						joinSameConditions == 1);
			}
		} catch (PmmException e) {
			e.printStackTrace();
		}

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(joiner.createPanel(assignments));
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!joiner.isValid()) {
			throw new InvalidSettingsException("");
		}

		List<String> assignments = joiner.getAssignments();

		settings.addStringArray(ModelAndDataJoinerNodeModel.CFGKEY_ASSIGNMENTS,
				assignments.toArray(new String[0]));

		if (joiner instanceof PrimaryJoiner) {
			if (((PrimaryJoiner) joiner).isJoinSameConditions()) {
				settings.addInt(
						ModelAndDataJoinerNodeModel.CFGKEY_JOINSAMECONDITIONS,
						1);
			} else {
				settings.addInt(
						ModelAndDataJoinerNodeModel.CFGKEY_JOINSAMECONDITIONS,
						0);
			}
		}
	}
}
