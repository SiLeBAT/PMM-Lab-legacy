package de.bund.bfr.knime.pmm.js.modelplotter;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

/**
 * Model Plotter dialog pane. 
 * Shows just one text field for the chart title and one for constant Y0.
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 *
 */
public final class ModelPlotterNodeDialogPane extends NodeDialogPane {

	private static final int TEXT_FIELD_SIZE = 20;
	
	private final JTextField m_chartTitleTextField;
	
	private final SpinnerModel m_y0Model;
	private final JSpinner m_y0;
	
	/**
	 * Constructor of {@code ModelPlotterNodeDialogPane}, creating the dialog pane with the dialog
	 * items.
	 */
	public ModelPlotterNodeDialogPane() {
		m_chartTitleTextField = new JTextField(TEXT_FIELD_SIZE);
		
		m_y0Model = new SpinnerNumberModel(ModelPlotterViewConfig.DEF_Y0,
				ModelPlotterViewConfig.MIN_Y0, ModelPlotterViewConfig.MAX_Y0, 0.1);
		m_y0 = new JSpinner();
		m_y0.setModel(m_y0Model);
		m_y0.setEditor(new JSpinner.NumberEditor(m_y0, "0.0000000000"));
		
		addTab("Options", initDialog());
	}
	
    private Component initDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHWEST;        
        panel.add(new JLabel("Chart title:"), c);
        
        c.gridx = 1;
        c.gridy = 0;
        panel.add(m_chartTitleTextField, c);
        
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Y0 (log10(count/g)):"), c);        

        c.gridx = 1;
        c.gridy = 1;
        panel.add(m_y0, c);
        
        return panel;
    }
	
	/* (non-Javadoc)
	 * @see org.knime.core.node.NodeDialogPane#saveSettingsTo(org.knime.core.node.NodeSettingsWO)
	 */
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		ModelPlotterViewConfig config = new ModelPlotterViewConfig();
		config.setChartTitle(m_chartTitleTextField.getText());
		config.setY0((Double)m_y0Model.getValue());
		
		config.saveSettings(settings);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
            throws NotConfigurableException {
    	ModelPlotterViewConfig config = new ModelPlotterViewConfig();
        config.loadSettingsForDialog(settings);
        m_chartTitleTextField.setText(config.getChartTitle());
        m_y0Model.setValue(config.getY0());
    }
}
