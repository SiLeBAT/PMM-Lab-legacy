package de.bund.bfr.knime.foodprocess.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;

/**
 * <code>NodeDialog</code> for the "View" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class ViewNodeDialog extends NodeDialogPane implements ActionListener {

	private List<String> allParameters;
	private List<String> usedParameters;

	private JPanel panel;
	private JList<String> paramList;
	private JButton addButton;
	private JButton addAllButton;
	private JButton removeButton;

	/**
	 * New pane for configuring the View node.
	 */
	protected ViewNodeDialog() {
		paramList = new JList<>();
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		addAllButton = new JButton("Add All");
		addAllButton.addActionListener(this);
		removeButton = new JButton("Remove");
		removeButton.addActionListener(this);

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(addButton);
		buttonPanel.add(addAllButton);
		buttonPanel.add(removeButton);

		panel = new JPanel();
		panel.setBorder(BorderFactory
				.createTitledBorder("Parameters to use (for y-axis)"));
		panel.setLayout(new BorderLayout());
		panel.add(buttonPanel, BorderLayout.NORTH);
		panel.add(new JScrollPane(paramList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		allParameters = new ArrayList<>();

		for (int i = 0; i < specs[0].getNumColumns(); i++) {
			DataColumnSpec spec = specs[0].getColumnSpec(i);

			if (spec.getType() == DoubleCell.TYPE
					&& !spec.getName().equals(AttributeUtilities.TIME)) {
				allParameters.add(spec.getName());
			}
		}

		try {
			List<String> parameters = XmlConverter.xmlToStringList(settings
					.getString(ViewNodeModel.CFG_USEDPARAMETERS));

			usedParameters = new ArrayList<>();

			for (String cond : parameters) {
				if (allParameters.contains(cond)) {
					usedParameters.add(cond);
				}
			}
		} catch (InvalidSettingsException e) {
			usedParameters = new ArrayList<>();
		}

		paramList.setListData(usedParameters.toArray(new String[0]));
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (usedParameters.isEmpty()) {
			throw new InvalidSettingsException(
					"List of conditions cannot be empty");
		}

		settings.addString(ViewNodeModel.CFG_USEDPARAMETERS,
				XmlConverter.listToXml(usedParameters));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			Object selection = JOptionPane.showInputDialog(panel,
					"Select Condition", "Input", JOptionPane.QUESTION_MESSAGE,
					null, allParameters.toArray(), allParameters.get(0));

			if (selection != null && !usedParameters.contains(selection)) {
				usedParameters.add((String) selection);
				paramList.setListData(usedParameters.toArray(new String[0]));
			}
		} else if (e.getSource() == addAllButton) {
			usedParameters.clear();
			usedParameters.addAll(allParameters);
			paramList.setListData(usedParameters.toArray(new String[0]));
		} else if (e.getSource() == removeButton) {
			List<String> selectedConditions = paramList.getSelectedValuesList();

			if (!selectedConditions.isEmpty()) {
				usedParameters.removeAll(selectedConditions);
				paramList.setListData(usedParameters.toArray(new String[0]));
			}
		}
	}
}
