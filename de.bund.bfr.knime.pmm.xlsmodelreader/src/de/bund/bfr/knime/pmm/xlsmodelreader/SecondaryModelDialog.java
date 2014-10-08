/*******************************************************************************
 * Copyright (c) 2014 Federal Institute for Risk Assessment (BfR), Germany 
 * 
 * Developers and contributors are 
 * Christian Thoens (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
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
package de.bund.bfr.knime.pmm.xlsmodelreader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.ui.UI;
import de.bund.bfr.knime.pmm.common.units.Categories;

public class SecondaryModelDialog extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private List<String> fileColumnList;
	private SettingsHelper set;
	private String param;

	private JButton okButton;
	private JButton cancelButton;
	private JPanel configPanel;

	private KnimeTuple tuple;
	private Map<String, String> mappings;
	private Map<String, String> mins;
	private Map<String, String> maxs;
	private Map<String, String> categories;
	private Map<String, String> units;

	private JButton modelButton;
	private JButton reloadButton;
	private Map<String, JComboBox<String>> modelBoxes;
	private Map<String, JComboBox<String>> minBoxes;
	private Map<String, JComboBox<String>> maxBoxes;
	private Map<String, JComboBox<String>> categoryBoxes;
	private Map<String, JComboBox<String>> unitBoxes;

	public SecondaryModelDialog(Component parent, List<String> fileColumnList,
			SettingsHelper set, String param) {
		super(SwingUtilities.getWindowAncestor(parent), "Secondary Model",
				DEFAULT_MODALITY_TYPE);
		this.fileColumnList = fileColumnList;
		this.set = set;
		this.param = param;

		tuple = set.getSecModelTuples().get(param);
		mappings = new LinkedHashMap<>(set.getSecModelMappings().get(param));
		mins = new LinkedHashMap<>(set.getSecModelIndepMins().get(param));
		maxs = new LinkedHashMap<>(set.getSecModelIndepMaxs().get(param));
		categories = new LinkedHashMap<>(set.getSecModelIndepCategories().get(
				param));
		units = new LinkedHashMap<>(set.getSecModelIndepUnits().get(param));

		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		configPanel = null;

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		setLayout(new BorderLayout());
		add(buttonPanel, BorderLayout.SOUTH);
		updateConfigPanel();
		pack();
		setLocationRelativeTo(parent);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			set.getSecModelTuples().put(param, tuple);
			set.getSecModelMappings().put(param, mappings);
			set.getSecModelIndepMins().put(param, mins);
			set.getSecModelIndepMaxs().put(param, maxs);
			set.getSecModelIndepCategories().put(param, categories);
			set.getSecModelIndepUnits().put(param, units);
			dispose();
		} else if (e.getSource() == cancelButton) {
			dispose();
		} else if (e.getSource() == modelButton) {
			Integer id;

			if (tuple != null) {
				id = DBKernel.openSecModelDBWindow(
						modelButton,
						((CatalogModelXml) tuple.getPmmXml(
								Model2Schema.ATT_MODELCATALOG).get(0)).getId());
			} else {
				id = DBKernel.openSecModelDBWindow(modelButton, null);
			}

			if (id != null) {
				Bfrdb db = new Bfrdb(DBKernel.getLocalConn(true));

				try {
					tuple = db.getSecModelById(id);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				updateConfigPanel();
			}
		} else if (e.getSource() == reloadButton) {
			Integer id = ((CatalogModelXml) tuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0)).getId();

			if (id != null) {
				Bfrdb db = new Bfrdb(DBKernel.getLocalConn(true));

				try {
					set.getSecModelTuples().put(param, db.getSecModelById(id));
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				updateConfigPanel();
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		for (String param2 : modelBoxes.keySet()) {
			if (e.getSource() == modelBoxes.get(param2)) {
				JComboBox<String> box = modelBoxes.get(param2);

				if (box.getSelectedItem().equals(SettingsHelper.DO_NOT_USE)) {
					mappings.put(param2, null);
				} else {
					mappings.put(param2, (String) box.getSelectedItem());
				}

				return;
			}
		}

		for (String param2 : minBoxes.keySet()) {
			if (e.getSource() == minBoxes.get(param2)) {
				mins.put(param2, (String) minBoxes.get(param2)
						.getSelectedItem());
				return;
			}
		}

		for (String param2 : maxBoxes.keySet()) {
			if (e.getSource() == maxBoxes.get(param2)) {
				maxs.put(param2, (String) maxBoxes.get(param2)
						.getSelectedItem());
				return;
			}
		}

		for (String param2 : categoryBoxes.keySet()) {
			if (e.getSource() == categoryBoxes.get(param2)) {
				categories.put(param2, (String) categoryBoxes.get(param2)
						.getSelectedItem());
				return;
			}
		}

		for (String param2 : unitBoxes.keySet()) {
			if (e.getSource() == unitBoxes.get(param2)) {
				units.put(param2, (String) unitBoxes.get(param2)
						.getSelectedItem());
				return;
			}
		}
	}

	private void updateConfigPanel() {
		if (configPanel != null) {
			remove(configPanel);
		}

		configPanel = new JPanel();
		configPanel.setLayout(new GridBagLayout());

		int row = 0;
		List<String> options = new ArrayList<>();

		options.add(SettingsHelper.DO_NOT_USE);
		options.addAll(fileColumnList);

		JPanel secButtonPanel = new JPanel();

		if (tuple != null) {
			modelButton = new JButton(((CatalogModelXml) tuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0)).getName());
			reloadButton = new JButton(SettingsHelper.RELOAD);
			reloadButton.setEnabled(true);
		} else {
			modelButton = new JButton(SettingsHelper.SELECT);
			reloadButton = new JButton(SettingsHelper.RELOAD);
			reloadButton.setEnabled(false);
		}

		secButtonPanel.setLayout(new BorderLayout());
		secButtonPanel.add(modelButton, BorderLayout.WEST);
		secButtonPanel.add(reloadButton, BorderLayout.EAST);

		modelButton.addActionListener(this);
		reloadButton.addActionListener(this);
		configPanel.add(new JLabel(param + ":"), createConstraints(0, row));
		configPanel.add(secButtonPanel, createConstraints(1, row));
		row++;

		modelBoxes = new LinkedHashMap<String, JComboBox<String>>();
		minBoxes = new LinkedHashMap<String, JComboBox<String>>();
		maxBoxes = new LinkedHashMap<String, JComboBox<String>>();
		categoryBoxes = new LinkedHashMap<String, JComboBox<String>>();
		unitBoxes = new LinkedHashMap<String, JComboBox<String>>();

		if (tuple != null) {
			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model2Schema.ATT_PARAMETER).getElementSet()) {
				ParamXml secParam = (ParamXml) el;
				JComboBox<String> box = new JComboBox<>(
						options.toArray(new String[0]));

				if (mappings.get(secParam.getName()) == null) {
					UI.select(box, SettingsHelper.DO_NOT_USE);
				} else {
					UI.select(box, mappings.get(secParam.getName()));
				}

				box.addItemListener(this);
				modelBoxes.put(secParam.getName(), box);

				configPanel.add(new JLabel(secParam.getName() + ":"),
						createConstraints(0, row));
				configPanel.add(box, createConstraints(1, row));
				row++;
			}

			for (PmmXmlElementConvertable el2 : tuple.getPmmXml(
					Model2Schema.ATT_INDEPENDENT).getElementSet()) {
				IndepXml indep = (IndepXml) el2;

				if (indep.getUnit() == null) {
					continue;
				}

				JComboBox<String> minBox = new JComboBox<>(
						options.toArray(new String[0]));
				JComboBox<String> maxBox = new JComboBox<>(
						options.toArray(new String[0]));
				JComboBox<String> categoryBox = new JComboBox<>(Categories
						.getAllCategories().toArray(new String[0]));
				JComboBox<String> unitBox = new JComboBox<>(Categories
						.getCategory(categories.get(indep.getName()))
						.getAllUnits().toArray(new String[0]));

				if (mins.get(indep.getName()) == null) {
					UI.select(minBox, SettingsHelper.DO_NOT_USE);
				} else {
					UI.select(minBox, mins.get(indep.getName()));
				}

				if (maxs.get(indep.getName()) == null) {
					UI.select(maxBox, SettingsHelper.DO_NOT_USE);
				} else {
					UI.select(maxBox, maxs.get(indep.getName()));
				}

				if (categories.get(indep.getName()) == null) {
					UI.select(categoryBox, null);
				} else {
					UI.select(categoryBox, categories.get(indep.getName()));
				}

				if (units.get(indep.getName()) == null) {
					UI.select(unitBox, null);
				} else {
					UI.select(unitBox, units.get(indep.getName()));
				}

				minBox.addItemListener(this);
				maxBox.addItemListener(this);
				categoryBox.addItemListener(this);
				unitBox.addItemListener(this);
				minBoxes.put(indep.getName(), minBox);
				maxBoxes.put(indep.getName(), maxBox);
				categoryBoxes.put(indep.getName(), categoryBox);
				unitBoxes.put(indep.getName(), unitBox);

				configPanel.add(new JLabel("Min " + indep.getName() + ":"),
						createConstraints(0, row));
				configPanel.add(minBox, createConstraints(1, row));
				row++;
				configPanel.add(new JLabel("Max " + indep.getName() + ":"),
						createConstraints(0, row));
				configPanel.add(maxBox, createConstraints(1, row));
				row++;
				configPanel.add(new JLabel(indep.getName() + " Category:"),
						createConstraints(0, row));
				configPanel.add(categoryBox, createConstraints(1, row));
				row++;
				configPanel.add(new JLabel(indep.getName() + " Unit:"),
						createConstraints(0, row));
				configPanel.add(unitBox, createConstraints(1, row));
				row++;
			}
		}

		add(configPanel, BorderLayout.CENTER);
	}

	private GridBagConstraints createConstraints(int x, int y) {
		return new GridBagConstraints(x, y, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
	}
}
