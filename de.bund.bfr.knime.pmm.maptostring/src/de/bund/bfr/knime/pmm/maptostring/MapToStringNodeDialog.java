package de.bund.bfr.knime.pmm.maptostring;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;

/**
 * <code>NodeDialog</code> for the "MapToString" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class MapToStringNodeDialog extends NodeDialogPane {

	private StringTextField nameField;
	private List<StringTextField> keyFields;
	private List<DoubleTextField> valueFields;

	private MapToStringSettings set;

	/**
	 * New pane for configuring the MapToString node.
	 */
	protected MapToStringNodeDialog() {
		set = new MapToStringSettings();
		nameField = new StringTextField(false);
		nameField.setPreferredSize(new Dimension(100, nameField
				.getPreferredSize().height));
		keyFields = new ArrayList<>();
		valueFields = new ArrayList<>();

		JPanel namePanel = new JPanel();

		namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		namePanel.add(new JLabel("Name:"));
		namePanel.add(nameField);

		JPanel valuesPanel = new JPanel();

		valuesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		valuesPanel.setLayout(new GridLayout(5, 4, 5, 5));

		for (int i = 0; i < 5; i++) {
			StringTextField keyField = new StringTextField(true);
			DoubleTextField valueField = new DoubleTextField(true);

			valuesPanel.add(new JLabel("Key " + i + ":"));
			valuesPanel.add(keyField);
			valuesPanel.add(new JLabel("Value " + i + ":"));
			valuesPanel.add(valueField);

			keyFields.add(keyField);
			valueFields.add(valueField);
		}

		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(namePanel);
		panel.add(valuesPanel);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new BorderLayout());
		northPanel.add(panel, BorderLayout.NORTH);

		addTab("Options", northPanel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		set.load(settings);
		nameField.setValue(set.getVariableName());

		for (int i = 0; i < 5; i++) {
			keyFields.get(i).setValue(set.getKeys().get(i));
			valueFields.get(i).setValue(set.getValues().get(i));
		}		
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		for (int i = 0; i < 5; i++) {
			set.getKeys().set(i, keyFields.get(i).getValue());
			set.getValues().set(i, valueFields.get(i).getValue());
		}

		set.setVariableName(nameField.getValue());
		set.save(settings);
	}
}
