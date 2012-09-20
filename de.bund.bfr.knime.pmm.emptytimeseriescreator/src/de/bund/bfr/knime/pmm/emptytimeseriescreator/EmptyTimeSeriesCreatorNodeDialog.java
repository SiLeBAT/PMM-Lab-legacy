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
package de.bund.bfr.knime.pmm.emptytimeseriescreator;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.PmmConstants;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.IntTextField;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;

/**
 * <code>NodeDialog</code> for the "EmptyTimeSeriesCreator" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class EmptyTimeSeriesCreatorNodeDialog extends NodeDialogPane {

	private StringTextField agentField;
	private StringTextField matrixField;
	private StringTextField commentField;
	private DoubleTextField temperatureField;
	private DoubleTextField phField;
	private DoubleTextField waterActivityField;
	private IntTextField stepNumberField;
	private DoubleTextField stepSizeField;

	/**
	 * New pane for configuring the EmptyTimeSeriesCreator node.
	 */
	protected EmptyTimeSeriesCreatorNodeDialog() {
		JPanel panel = new JPanel();
		JPanel upperPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();

		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftPanel.setLayout(new GridLayout(8, 1, 5, 5));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_AGENTDETAIL) + ":"));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_MATRIXDETAIL) + ":"));
		leftPanel.add(new JLabel(TimeSeriesSchema.ATT_COMMENT + ":"));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_TEMPERATURE) + ":"));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_PH) + ":"));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_WATERACTIVITY) + ":"));
		leftPanel.add(new JLabel("Number of Time Steps:"));
		leftPanel.add(new JLabel("Size of Time Step:"));

		agentField = new StringTextField(true);
		matrixField = new StringTextField(true);
		commentField = new StringTextField(true);
		temperatureField = new DoubleTextField(true);
		phField = new DoubleTextField(PmmConstants.MIN_PH, PmmConstants.MAX_PH,
				true);
		waterActivityField = new DoubleTextField(
				PmmConstants.MIN_WATERACTIVITY, PmmConstants.MAX_WATERACTIVITY,
				true);
		stepNumberField = new IntTextField(
				EmptyTimeSeriesCreatorNodeModel.MIN_TIMESTEPNUMBER,
				EmptyTimeSeriesCreatorNodeModel.MAX_TIMESTEPNUMBER);
		stepNumberField
				.setValue(EmptyTimeSeriesCreatorNodeModel.DEFAULT_TIMESTEPNUMBER);
		stepSizeField = new DoubleTextField(
				EmptyTimeSeriesCreatorNodeModel.MIN_TIMESTEPSIZE,
				EmptyTimeSeriesCreatorNodeModel.MAX_TIMESTEPSIZE);
		stepSizeField
				.setValue(EmptyTimeSeriesCreatorNodeModel.DEFAULT_TIMESTEPSIZE);

		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new GridLayout(8, 1, 5, 5));
		rightPanel.add(agentField);
		rightPanel.add(matrixField);
		rightPanel.add(commentField);
		rightPanel.add(temperatureField);
		rightPanel.add(phField);
		rightPanel.add(waterActivityField);
		rightPanel.add(stepNumberField);
		rightPanel.add(stepSizeField);

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(leftPanel, BorderLayout.WEST);
		upperPanel.add(rightPanel, BorderLayout.CENTER);

		panel.setLayout(new BorderLayout());
		panel.add(upperPanel, BorderLayout.NORTH);
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(final NodeSettingsRO settings,
			final DataTableSpec[] specs) throws NotConfigurableException {
		try {
			agentField.setValue(settings
					.getString(EmptyTimeSeriesCreatorNodeModel.CFGKEY_AGENT));
		} catch (InvalidSettingsException e) {
		}

		try {
			matrixField.setValue(settings
					.getString(EmptyTimeSeriesCreatorNodeModel.CFGKEY_MATRIX));
		} catch (InvalidSettingsException e) {
		}

		try {
			commentField.setValue(settings
					.getString(EmptyTimeSeriesCreatorNodeModel.CFGKEY_COMMENT));
		} catch (InvalidSettingsException e) {
		}

		try {
			temperatureField
					.setValue(settings
							.getDouble(EmptyTimeSeriesCreatorNodeModel.CFGKEY_TEMPERATURE));
		} catch (InvalidSettingsException e) {
		}

		try {
			phField.setValue(settings
					.getDouble(EmptyTimeSeriesCreatorNodeModel.CFGKEY_PH));
		} catch (InvalidSettingsException e) {
		}

		try {
			waterActivityField
					.setValue(settings
							.getDouble(EmptyTimeSeriesCreatorNodeModel.CFGKEY_WATERACTIVITY));
		} catch (InvalidSettingsException e) {
		}

		try {
			stepNumberField
					.setValue(settings
							.getInt(EmptyTimeSeriesCreatorNodeModel.CFGKEY_TIMESTEPNUMBER));
		} catch (InvalidSettingsException e) {
		}

		try {
			stepSizeField
					.setValue(settings
							.getDouble(EmptyTimeSeriesCreatorNodeModel.CFGKEY_TIMESTEPSIZE));
		} catch (InvalidSettingsException e) {
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!temperatureField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (!phField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (!waterActivityField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (!stepNumberField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (!stepSizeField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (agentField.getValue() != null) {
			settings.addString(EmptyTimeSeriesCreatorNodeModel.CFGKEY_AGENT,
					agentField.getValue());
		}

		if (matrixField.getValue() != null) {
			settings.addString(EmptyTimeSeriesCreatorNodeModel.CFGKEY_MATRIX,
					matrixField.getValue());
		}

		if (commentField.getValue() != null) {
			settings.addString(EmptyTimeSeriesCreatorNodeModel.CFGKEY_COMMENT,
					commentField.getValue());
		}

		if (temperatureField.getValue() != null) {
			settings.addDouble(
					EmptyTimeSeriesCreatorNodeModel.CFGKEY_TEMPERATURE,
					temperatureField.getValue());
		}

		if (phField.getValue() != null) {
			settings.addDouble(EmptyTimeSeriesCreatorNodeModel.CFGKEY_PH,
					phField.getValue());
		}

		if (waterActivityField.getValue() != null) {
			settings.addDouble(
					EmptyTimeSeriesCreatorNodeModel.CFGKEY_WATERACTIVITY,
					waterActivityField.getValue());
		}

		if (stepNumberField.getValue() != null) {
			settings.addInt(
					EmptyTimeSeriesCreatorNodeModel.CFGKEY_TIMESTEPNUMBER,
					stepNumberField.getValue());
		}

		if (stepSizeField.getValue() != null) {
			settings.addDouble(
					EmptyTimeSeriesCreatorNodeModel.CFGKEY_TIMESTEPSIZE,
					stepSizeField.getValue());
		}
	}
}
