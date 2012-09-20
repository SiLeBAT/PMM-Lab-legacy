/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.knime.nodes.r.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.port.PortObjectSpec;

import de.dim.bfr.knime.util.PluginUtils;

public class CustomRViewNodeDialog extends NodeDialogPane {
	
	public static final String PLOT_MIN_X = "PLOT_MIN_X";
	public static final String PLOT_MAX_X = "PLOT_MAX_X";
	private JPanel spinnerPanel;
	private SpinnerNumberModel minXSpinModel;
	private SpinnerNumberModel maxXSpinModel;
	private JSpinner minXSpinner;
	private JSpinner maxXSpinner;
	private JPanel configPanel;

	protected CustomRViewNodeDialog() 
    {
		super.addTab("Options", configPanel = new JPanel());
		Label minLabel = new Label("Min x-Value: ");
		minLabel.setBackground(configPanel.getBackground());
		Label maxLabel = new Label("Max x-Value: ");
		maxLabel.setBackground(configPanel.getBackground());
		minXSpinModel = new SpinnerNumberModel(0, 0,
				Double.MAX_VALUE, 1);
		maxXSpinModel = new SpinnerNumberModel(20, 0,
				Double.MAX_VALUE, 1);
		
		minXSpinner = new JSpinner(minXSpinModel);
		maxXSpinner = new JSpinner(maxXSpinModel);
		
		spinnerPanel = new JPanel(new GridLayout(2,2));
		spinnerPanel.setPreferredSize(new Dimension(220,75));
		spinnerPanel.setBorder(new TitledBorder("visualisation"));
		
		spinnerPanel.add(minLabel);
		spinnerPanel.add(minXSpinner);
		spinnerPanel.add(maxLabel);
		spinnerPanel.add(maxXSpinner);
		configPanel.add(spinnerPanel);
    }

	public static SettingsModelBoolean createUseSpecifiedFileModel() {
		return null;
	}
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException 
	{
		try {
			if (settings.containsKey(PLOT_MAX_X))
				minXSpinModel.setValue(settings.getDouble(PLOT_MIN_X));
		} catch (InvalidSettingsException e1) {
			e1.printStackTrace();
		}
		try {
			if (settings.containsKey(PLOT_MAX_X))
				maxXSpinModel.setValue(settings.getDouble(PLOT_MAX_X));
		} catch (InvalidSettingsException e) {
			e.printStackTrace();
		}
		if (getAvailableFlowVariables().containsKey(PluginUtils.PLOT_TYPE) && getAvailableFlowVariables().get(PluginUtils.PLOT_TYPE)!=null) {
			enableSpinners();
		} else {
			disableSpinners();
		}
	}	
	
	private void disableSpinners() {
		minXSpinner.setEnabled(false);
		maxXSpinner.setEnabled(false);
	}
	private void enableSpinners() {
		minXSpinner.setEnabled(true);
		maxXSpinner.setEnabled(true);
	}

	public double getMaxX() {
		return Double.parseDouble(maxXSpinner.getValue().toString());
	}

	public double getMinX() {
		return Double.parseDouble(minXSpinner.getValue().toString());
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		settings.addDouble(PLOT_MAX_X, getMaxX());
		settings.addDouble(PLOT_MIN_X, getMinX());
	}
}
