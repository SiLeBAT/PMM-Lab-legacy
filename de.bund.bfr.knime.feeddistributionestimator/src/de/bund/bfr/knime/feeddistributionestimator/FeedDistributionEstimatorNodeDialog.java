package de.bund.bfr.knime.feeddistributionestimator;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "FeedDistributionEstimator" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class FeedDistributionEstimatorNodeDialog extends NodeDialogPane {

	private JComboBox<String> idBox;
	private JComboBox<String> productionBox;
	private JComboBox<String> consumptionBox;
	private JComboBox<String> xBox;
	private JComboBox<String> yBox;

	/**
	 * New pane for configuring the FeedDistributionEstimator node.
	 */
	protected FeedDistributionEstimatorNodeDialog() {
		idBox = new JComboBox<String>();
		productionBox = new JComboBox<String>();
		consumptionBox = new JComboBox<String>();
		xBox = new JComboBox<String>();
		yBox = new JComboBox<String>();

		JPanel leftTablePanel = new JPanel();

		leftTablePanel.setLayout(new GridLayout(5, 1, 5, 5));
		leftTablePanel.add(new JLabel("Column with IDs:"));
		leftTablePanel.add(new JLabel("Column with Production Values:"));
		leftTablePanel.add(new JLabel("Column with Consumption Values:"));
		leftTablePanel.add(new JLabel("Column with x-Position:"));
		leftTablePanel.add(new JLabel("Column with y-Position:"));

		JPanel rightTablePanel = new JPanel();

		rightTablePanel.setLayout(new GridLayout(5, 1, 5, 5));
		rightTablePanel.add(idBox);
		rightTablePanel.add(productionBox);
		rightTablePanel.add(consumptionBox);
		rightTablePanel.add(xBox);
		rightTablePanel.add(yBox);

		JPanel innerTablePanel = new JPanel();

		innerTablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		innerTablePanel.setLayout(new BorderLayout(5, 5));
		innerTablePanel.add(leftTablePanel, BorderLayout.WEST);
		innerTablePanel.add(rightTablePanel, BorderLayout.CENTER);

		JPanel outerTablePanel = new JPanel();

		outerTablePanel.setBorder(BorderFactory
				.createTitledBorder("Input Table"));
		outerTablePanel.setLayout(new BorderLayout());
		outerTablePanel.add(innerTablePanel, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(outerTablePanel);

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(mainPanel, BorderLayout.NORTH);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		String id;
		String production;
		String consumption;
		String x;
		String y;

		try {
			id = settings.getString(FeedDistributionEstimatorNodeModel.CFG_ID);
		} catch (InvalidSettingsException e) {
			id = "";
		}

		try {
			production = settings
					.getString(FeedDistributionEstimatorNodeModel.CFG_PRODUCTION);
		} catch (InvalidSettingsException e) {
			production = "";
		}

		try {
			consumption = settings
					.getString(FeedDistributionEstimatorNodeModel.CFG_CONSUMPTION);
		} catch (InvalidSettingsException e) {
			consumption = "";
		}

		try {
			x = settings.getString(FeedDistributionEstimatorNodeModel.CFG_X);
		} catch (InvalidSettingsException e) {
			x = "";
		}

		try {
			y = settings.getString(FeedDistributionEstimatorNodeModel.CFG_Y);
		} catch (InvalidSettingsException e) {
			y = "";
		}

		String[] tableColumns = specs[0].getColumnNames();

		idBox.removeAllItems();
		productionBox.removeAllItems();
		consumptionBox.removeAllItems();
		xBox.removeAllItems();
		yBox.removeAllItems();

		for (String column : tableColumns) {
			idBox.addItem(column);
			productionBox.addItem(column);
			consumptionBox.addItem(column);
			xBox.addItem(column);
			yBox.addItem(column);
		}

		idBox.setSelectedItem(id);
		productionBox.setSelectedItem(production);
		consumptionBox.setSelectedItem(consumption);
		xBox.setSelectedItem(x);
		yBox.setSelectedItem(y);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		settings.addString(FeedDistributionEstimatorNodeModel.CFG_ID,
				(String) idBox.getSelectedItem());
		settings.addString(FeedDistributionEstimatorNodeModel.CFG_PRODUCTION,
				(String) productionBox.getSelectedItem());
		settings.addString(FeedDistributionEstimatorNodeModel.CFG_CONSUMPTION,
				(String) consumptionBox.getSelectedItem());
		settings.addString(FeedDistributionEstimatorNodeModel.CFG_X,
				(String) xBox.getSelectedItem());
		settings.addString(FeedDistributionEstimatorNodeModel.CFG_Y,
				(String) yBox.getSelectedItem());
	}
}
