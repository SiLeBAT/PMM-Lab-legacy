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
package de.bund.bfr.knime.pmm.forecaststaticconditions;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;

/**
 * <code>NodeDialog</code> for the "ForecastStaticConditions" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class ForecastStaticConditionsNodeDialog extends DataAwareNodeDialogPane {

	private KnimeSchema schema;
	private List<String> ids;
	private Map<String, String> modelNames;
	private Map<String, String> formulas;
	private Map<String, List<String>> availableParams;
	private DoubleTextField valueField;
	private Map<String, JComboBox> paramBoxes;

	/**
	 * New pane for configuring the ForecastStaticConditions node.
	 */
	protected ForecastStaticConditionsNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);

		try {
			schema = new KnimeSchema(new Model1Schema(), new TimeSeriesSchema());
		} catch (PmmException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		double concentration;
		Map<String, String> concentrationParameters;

		try {
			concentration = settings
					.getDouble(ForecastStaticConditionsNodeModel.CFGKEY_CONCENTRATION);
		} catch (InvalidSettingsException e) {
			concentration = ForecastStaticConditionsNodeModel.DEFAULT_CONCENTRATION;
		}

		try {
			String paramsString = settings
					.getString(ForecastStaticConditionsNodeModel.CFGKEY_CONCENTRATIONPARAMETERS);

			concentrationParameters = new LinkedHashMap<String, String>();

			if (!paramsString.isEmpty()) {
				for (String assign : paramsString.split(",")) {
					String[] elems = assign.split(":");

					concentrationParameters.put(elems[0], elems[1]);
				}
			}
		} catch (InvalidSettingsException e) {
			concentrationParameters = new LinkedHashMap<String, String>();
		}

		try {
			readTable(input[0]);
		} catch (PmmException e) {
			e.printStackTrace();
		}

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createPanel(concentration,
				concentrationParameters));
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!valueField.isValueValid()) {
			throw new InvalidSettingsException("");
		}

		settings.addDouble(
				ForecastStaticConditionsNodeModel.CFGKEY_CONCENTRATION,
				valueField.getValue());

		StringBuilder paramsString = new StringBuilder();

		for (String id : ids) {
			if (!paramBoxes.get(id).getSelectedItem().equals("")) {
				paramsString.append(id);
				paramsString.append(":");
				paramsString.append(paramBoxes.get(id).getSelectedItem());
				paramsString.append(",");
			}
		}

		if (paramsString.length() > 0) {
			paramsString.deleteCharAt(paramsString.length() - 1);
		}

		settings.addString(
				ForecastStaticConditionsNodeModel.CFGKEY_CONCENTRATIONPARAMETERS,
				paramsString.toString());
	}

	private void readTable(BufferedDataTable table) throws PmmException {
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		Set<String> idSet = new LinkedHashSet<String>();

		ids = new ArrayList<String>();
		modelNames = new LinkedHashMap<String, String>();
		formulas = new LinkedHashMap<String, String>();
		availableParams = new LinkedHashMap<String, List<String>>();

		for (KnimeTuple tuple : tuples) {
			String id = tuple.getInt(Model1Schema.ATT_MODELID) + "";

			if (!idSet.add(id)) {
				continue;
			}

			List<String> params = new ArrayList<String>();

			params.add("");
			params.addAll(tuple.getStringList(Model1Schema.ATT_PARAMNAME));

			ids.add(id);
			modelNames.put(id, tuple.getString(Model1Schema.ATT_MODELNAME));
			formulas.put(id, tuple.getString(Model1Schema.ATT_FORMULA));
			availableParams.put(id, params);
		}
	}

	private JPanel createPanel(double value, Map<String, String> params) {
		JPanel panel = new JPanel();
		JPanel upperPanel = new JPanel();
		JPanel valuePanel = new JPanel();
		JPanel parameterPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();

		panel.setLayout(new BorderLayout());
		upperPanel.setLayout(new BorderLayout());

		valuePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		valuePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		valueField = new DoubleTextField();
		valueField.setValue(value);
		valueField.setPreferredSize(new Dimension(150, valueField
				.getPreferredSize().height));
		valuePanel.add(new JLabel("Initial Concentration:"));
		valuePanel.add(valueField);

		parameterPanel.setLayout(new BorderLayout());
		parameterPanel.setBorder(BorderFactory
				.createTitledBorder("Initial Concentration Parameters"));
		leftPanel.setLayout(new GridLayout(ids.size(), 1, 5, 5));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new GridLayout(ids.size(), 1, 5, 5));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		paramBoxes = new LinkedHashMap<String, JComboBox>();

		for (String id : ids) {
			JComboBox box = new JComboBox(availableParams.get(id).toArray());
			JLabel label = new JLabel(modelNames.get(id) + ":");

			if (params.containsKey(id)) {
				box.setSelectedItem(params.get(id));
			}

			box.setPreferredSize(new Dimension(150,
					box.getPreferredSize().height));
			label.setToolTipText(formulas.get(id));
			paramBoxes.put(id, box);
			leftPanel.add(label);
			rightPanel.add(box);
		}

		parameterPanel.add(leftPanel, BorderLayout.WEST);
		parameterPanel.add(rightPanel, BorderLayout.EAST);
		upperPanel.add(valuePanel, BorderLayout.NORTH);
		upperPanel.add(parameterPanel, BorderLayout.CENTER);
		panel.add(upperPanel, BorderLayout.NORTH);

		return panel;
	}

}
