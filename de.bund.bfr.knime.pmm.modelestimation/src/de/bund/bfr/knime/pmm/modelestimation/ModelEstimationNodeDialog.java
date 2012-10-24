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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.ListUtilities;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;

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
public class ModelEstimationNodeDialog extends DataAwareNodeDialogPane {

	private JCheckBox limitsBox;
	private JCheckBox oneStepBox;

	private Map<String, String> modelNames;
	private Map<String, List<String>> parameters;
	private Map<String, Map<String, Double>> minValues;
	private Map<String, Map<String, Double>> maxValues;

	private Map<String, Map<String, DoubleTextField>> minimumFields;
	private Map<String, Map<String, DoubleTextField>> maximumFields;

	/**
	 * New pane for configuring the ModelEstimation node.
	 */
	protected ModelEstimationNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		int enforceLimits;
		int doOneStepFit;
		List<String> parameterGuesses;

		try {
			enforceLimits = settings
					.getInt(ModelEstimationNodeModel.CFGKEY_ENFORCELIMITS);
		} catch (InvalidSettingsException e) {
			enforceLimits = ModelEstimationNodeModel.DEFAULT_ENFORCELIMITS;
		}

		try {
			doOneStepFit = settings
					.getInt(ModelEstimationNodeModel.CFGKEY_ONESTEPMETHOD);
		} catch (InvalidSettingsException e) {
			doOneStepFit = ModelEstimationNodeModel.DEFAULT_ONESTEPMETHOD;
		}

		try {
			parameterGuesses = ListUtilities
					.getStringListFromString(settings
							.getString(ModelEstimationNodeModel.CFGKEY_PARAMETERGUESSES));
		} catch (InvalidSettingsException e) {
			parameterGuesses = new ArrayList<String>();
		}

		try {
			KnimeSchema peiSchema = new KnimeSchema(new Model1Schema(),
					new TimeSeriesSchema());
			KnimeSchema seiSchema = new KnimeSchema(new KnimeSchema(
					new Model1Schema(), new Model2Schema()),
					new TimeSeriesSchema());

			if (seiSchema.conforms(input[0].getDataTableSpec())) {
				readSecondaryTable(input[0]);
			} else if (peiSchema.conforms(input[0].getDataTableSpec())) {
				readPrimaryTable(input[0]);
			}
		} catch (PmmException e) {
			e.printStackTrace();
		}

		JPanel panel = createPanel(enforceLimits == 1, doOneStepFit == 1,
				ModelEstimationNodeModel.getGuessMap(parameterGuesses));

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(panel, BorderLayout.NORTH);
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

		Map<String, Map<String, Point2D.Double>> guessMap = new LinkedHashMap<String, Map<String, Point2D.Double>>();

		for (String modelName : parameters.keySet()) {
			Map<String, Point2D.Double> guesses = new LinkedHashMap<String, Point2D.Double>();

			for (String param : parameters.get(modelName)) {
				Double min = minimumFields.get(modelName).get(param).getValue();
				Double max = maximumFields.get(modelName).get(param).getValue();

				if (min == null) {
					min = Double.NaN;
				}

				if (max == null) {
					max = Double.NaN;
				}

				guesses.put(param, new Point2D.Double(min, max));
			}

			guessMap.put(modelName, guesses);
		}

		settings.addString(ModelEstimationNodeModel.CFGKEY_PARAMETERGUESSES,
				ListUtilities.getStringFromList(ModelEstimationNodeModel
						.guessMapToList(guessMap)));
	}

	private void readPrimaryTable(BufferedDataTable table) throws PmmException {
		KnimeRelationReader reader = new KnimeRelationReader(
				new Model1Schema(), table);
		Set<String> parameterSet = new LinkedHashSet<String>();
		Map<String, Double> minValueMap = new LinkedHashMap<String, Double>();
		Map<String, Double> maxValueMap = new LinkedHashMap<String, Double>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			List<String> params = tuple
					.getStringList(Model1Schema.ATT_PARAMNAME);
			List<Double> mins = tuple.getDoubleList(Model1Schema.ATT_MINVALUE);
			List<Double> maxs = tuple.getDoubleList(Model1Schema.ATT_MAXVALUE);

			for (int i = 0; i < params.size(); i++) {
				parameterSet.add(params.get(i));
				minValueMap.put(params.get(i), mins.get(i));
				maxValueMap.put(params.get(i), maxs.get(i));
			}
		}

		modelNames = new LinkedHashMap<String, String>();
		parameters = new LinkedHashMap<String, List<String>>();
		minValues = new LinkedHashMap<String, Map<String, Double>>();
		maxValues = new LinkedHashMap<String, Map<String, Double>>();
		modelNames.put(ModelEstimationNodeModel.PRIMARY,
				ModelEstimationNodeModel.PRIMARY);
		parameters.put(ModelEstimationNodeModel.PRIMARY, new ArrayList<String>(
				parameterSet));
		minValues.put(ModelEstimationNodeModel.PRIMARY, minValueMap);
		maxValues.put(ModelEstimationNodeModel.PRIMARY, maxValueMap);
	}

	private void readSecondaryTable(BufferedDataTable table)
			throws PmmException {
		readPrimaryTable(table);

		KnimeRelationReader reader = new KnimeRelationReader(
				new Model2Schema(), table);
		Map<String, Set<String>> parameterSets = new LinkedHashMap<String, Set<String>>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			String id = tuple.getInt(Model2Schema.ATT_MODELID) + "";
			List<String> params = tuple
					.getStringList(Model2Schema.ATT_PARAMNAME);
			List<Double> mins = tuple.getDoubleList(Model2Schema.ATT_MINVALUE);
			List<Double> maxs = tuple.getDoubleList(Model2Schema.ATT_MAXVALUE);

			if (!parameterSets.containsKey(id)) {
				modelNames.put(id, tuple.getString(Model2Schema.ATT_MODELNAME));
				parameterSets.put(id, new LinkedHashSet<String>());
				minValues.put(id, new LinkedHashMap<String, Double>());
				maxValues.put(id, new LinkedHashMap<String, Double>());
			}

			for (int i = 0; i < params.size(); i++) {
				parameterSets.get(id).add(params.get(i));
				minValues.get(id).put(params.get(i), mins.get(i));
				maxValues.get(id).put(params.get(i), maxs.get(i));
			}
		}

		for (String id : parameterSets.keySet()) {
			parameters.put(id, new ArrayList<String>(parameterSets.get(id)));
		}
	}

	private JPanel createPanel(boolean enforceLimits, boolean doOneStepFit,
			Map<String, Map<String, Point2D.Double>> guessMap) {
		JPanel panel = new JPanel();
		JPanel limitsPanel = new JPanel();
		JPanel oneStepPanel = new JPanel();
		JPanel rangePanel = new JPanel();

		limitsBox = new JCheckBox("Enforce Limits", enforceLimits);
		oneStepBox = new JCheckBox("Use One Step Method", doOneStepFit);
		minimumFields = new LinkedHashMap<String, Map<String, DoubleTextField>>();
		maximumFields = new LinkedHashMap<String, Map<String, DoubleTextField>>();

		limitsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		limitsPanel.setLayout(new BorderLayout());
		limitsPanel.add(limitsBox, BorderLayout.WEST);
		oneStepPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		oneStepPanel.setLayout(new BorderLayout());
		oneStepPanel.add(oneStepBox, BorderLayout.WEST);

		if (parameters.size() == 1
				&& parameters.containsKey(ModelEstimationNodeModel.PRIMARY)) {
			JPanel leftPanel = new JPanel();
			JPanel rightPanel = new JPanel();
			List<String> params = parameters
					.get(ModelEstimationNodeModel.PRIMARY);
			Map<String, DoubleTextField> minFields = new LinkedHashMap<String, DoubleTextField>();
			Map<String, DoubleTextField> maxFields = new LinkedHashMap<String, DoubleTextField>();
			Map<String, Point2D.Double> guesses = guessMap
					.get(ModelEstimationNodeModel.PRIMARY);

			leftPanel.setLayout(new GridLayout(params.size(), 1));
			rightPanel.setLayout(new GridLayout(params.size(), 1));

			if (guesses == null) {
				guesses = new LinkedHashMap<String, Point2D.Double>();
			}

			for (String param : params) {
				JPanel labelPanel = new JPanel();
				JPanel minMaxPanel = new JPanel();
				DoubleTextField minField = new DoubleTextField(true);
				DoubleTextField maxField = new DoubleTextField(true);

				minField.setPreferredSize(new Dimension(100, minField
						.getPreferredSize().height));
				maxField.setPreferredSize(new Dimension(100, maxField
						.getPreferredSize().height));

				if (guesses.containsKey(param)) {
					Point2D.Double range = guesses.get(param);

					if (!Double.isNaN(range.x)) {
						minField.setValue(range.x);
					}

					if (!Double.isNaN(range.y)) {
						maxField.setValue(range.y);
					}
				} else {
					minField.setValue(minValues.get(
							ModelEstimationNodeModel.PRIMARY).get(param));
					maxField.setValue(maxValues.get(
							ModelEstimationNodeModel.PRIMARY).get(param));
				}

				labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				labelPanel.add(new JLabel(param + ":"));

				minMaxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				minMaxPanel.add(minField);
				minMaxPanel.add(new JLabel("to"));
				minMaxPanel.add(maxField);

				minFields.put(param, minField);
				maxFields.put(param, maxField);
				leftPanel.add(labelPanel);
				rightPanel.add(minMaxPanel);
			}

			minimumFields.put(ModelEstimationNodeModel.PRIMARY, minFields);
			maximumFields.put(ModelEstimationNodeModel.PRIMARY, maxFields);

			rangePanel.setLayout(new BorderLayout());
			rangePanel.add(leftPanel, BorderLayout.WEST);
			rangePanel.add(rightPanel, BorderLayout.EAST);
		} else {
			rangePanel.setLayout(new BoxLayout(rangePanel, BoxLayout.Y_AXIS));

			for (String id : modelNames.keySet()) {
				JPanel modelPanel = new JPanel();
				JPanel leftPanel = new JPanel();
				JPanel rightPanel = new JPanel();
				List<String> params = parameters.get(id);
				Map<String, DoubleTextField> minFields = new LinkedHashMap<String, DoubleTextField>();
				Map<String, DoubleTextField> maxFields = new LinkedHashMap<String, DoubleTextField>();
				Map<String, Point2D.Double> guesses = guessMap.get(id);

				leftPanel.setLayout(new GridLayout(params.size(), 1));
				rightPanel.setLayout(new GridLayout(params.size(), 1));

				if (guesses == null) {
					guesses = new LinkedHashMap<String, Point2D.Double>();
				}

				for (String param : params) {
					JPanel labelPanel = new JPanel();
					JPanel minMaxPanel = new JPanel();
					DoubleTextField minField = new DoubleTextField(true);
					DoubleTextField maxField = new DoubleTextField(true);

					minField.setPreferredSize(new Dimension(100, minField
							.getPreferredSize().height));
					maxField.setPreferredSize(new Dimension(100, maxField
							.getPreferredSize().height));

					if (guesses.containsKey(param)) {
						Point2D.Double range = guesses.get(param);

						if (!Double.isNaN(range.x)) {
							minField.setValue(range.x);
						}

						if (!Double.isNaN(range.y)) {
							maxField.setValue(range.y);
						}
					} else {
						minField.setValue(minValues.get(id).get(param));
						maxField.setValue(maxValues.get(id).get(param));
					}

					labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
					labelPanel.add(new JLabel(param + ":"));

					minMaxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
					minMaxPanel.add(minField);
					minMaxPanel.add(new JLabel("to"));
					minMaxPanel.add(maxField);

					minFields.put(param, minField);
					maxFields.put(param, maxField);
					leftPanel.add(labelPanel);
					rightPanel.add(minMaxPanel);
				}

				minimumFields.put(id, minFields);
				maximumFields.put(id, maxFields);

				modelPanel.setBorder(BorderFactory
						.createTitledBorder(modelNames.get(id)));
				modelPanel.setLayout(new BorderLayout());
				modelPanel.add(leftPanel, BorderLayout.WEST);
				modelPanel.add(rightPanel, BorderLayout.EAST);

				rangePanel.add(modelPanel);
			}
		}

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(limitsPanel);
		panel.add(oneStepPanel);
		panel.add(rangePanel);

		return panel;
	}
}
