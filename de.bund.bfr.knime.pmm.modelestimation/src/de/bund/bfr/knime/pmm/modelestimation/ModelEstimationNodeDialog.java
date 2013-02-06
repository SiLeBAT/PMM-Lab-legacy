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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.ListUtilities;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.IntTextField;
import de.bund.bfr.knime.pmm.common.ui.SpacePanel;

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
public class ModelEstimationNodeDialog extends DataAwareNodeDialogPane
		implements ActionListener {

	private BufferedDataTable[] input;

	private Map<String, Map<String, Point2D.Double>> guessMap;

	private JComboBox<String> fittingBox;
	private IntTextField nParamSpaceField;
	private IntTextField nLevenbergField;
	private JCheckBox stopWhenSuccessBox;
	private JCheckBox limitsBox;

	private Map<String, String> modelNames;
	private Map<String, List<String>> parameters;
	private Map<String, Map<String, Double>> minValues;
	private Map<String, Map<String, Double>> maxValues;

	private JPanel fittingPanel;
	private JButton modelRangeButton;
	private JButton rangeButton;
	private JButton clearButton;
	private Map<String, Map<String, DoubleTextField>> minimumFields;
	private Map<String, Map<String, DoubleTextField>> maximumFields;

	/**
	 * New pane for configuring the ModelEstimation node.
	 */
	protected ModelEstimationNodeDialog() {
		JPanel panel = new JPanel();
		JPanel upperPanel = new JPanel();
		JPanel fittingTypePanel = new JPanel();
		JPanel regressionPanel = new JPanel();
		JPanel leftRegressionPanel = new JPanel();
		JPanel rightRegressionPanel = new JPanel();

		fittingBox = new JComboBox<String>(new String[] {
				ModelEstimationNodeModel.PRIMARY_FITTING,
				ModelEstimationNodeModel.SECONDARY_FITTING,
				ModelEstimationNodeModel.ONESTEP_FITTING });
		nParamSpaceField = new IntTextField(0, 1000000);
		nParamSpaceField.setPreferredSize(new Dimension(100, nParamSpaceField
				.getPreferredSize().height));
		nLevenbergField = new IntTextField(1, 100);
		nLevenbergField.setPreferredSize(new Dimension(100, nLevenbergField
				.getPreferredSize().height));
		stopWhenSuccessBox = new JCheckBox("Stop When Regression Successful");
		limitsBox = new JCheckBox("Enforce limits of Formula Definition");
		fittingPanel = new JPanel();
		fittingPanel
				.setBorder(BorderFactory
						.createTitledBorder("Specific Start Values for Fitting Procedure - Optional"));
		fittingPanel.setLayout(new BorderLayout());

		fittingTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		fittingTypePanel.add(fittingBox);

		leftRegressionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5,
				5));
		leftRegressionPanel.setLayout(new GridLayout(4, 1, 5, 5));
		leftRegressionPanel.add(new JLabel(
				"Maximal Evaluations to Find Start Values"));
		leftRegressionPanel.add(new JLabel(
				"Maximal Executions of the Levenberg Algorithm"));
		leftRegressionPanel.add(stopWhenSuccessBox);
		leftRegressionPanel.add(limitsBox);

		rightRegressionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5,
				5));
		rightRegressionPanel.setLayout(new GridLayout(4, 1, 5, 5));
		rightRegressionPanel.add(nParamSpaceField);
		rightRegressionPanel.add(nLevenbergField);
		rightRegressionPanel.add(new JLabel());
		rightRegressionPanel.add(new JLabel());

		regressionPanel.setBorder(new TitledBorder(
				"Nonlinear Regression Parameters"));
		regressionPanel.setLayout(new BorderLayout());
		regressionPanel.add(leftRegressionPanel, BorderLayout.WEST);
		regressionPanel.add(rightRegressionPanel, BorderLayout.EAST);

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(fittingTypePanel, BorderLayout.NORTH);
		upperPanel.add(regressionPanel, BorderLayout.CENTER);

		panel.setLayout(new BorderLayout());
		panel.add(upperPanel, BorderLayout.NORTH);
		panel.add(fittingPanel, BorderLayout.CENTER);
		addTab("Options", panel);
	}

	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		this.input = input;

		String fittingType;
		int nParameterSpace;
		int nLevenberg;
		int stopWhenSuccessful;
		int enforceLimits;
		List<String> parameterGuesses;

		try {
			fittingType = settings
					.getString(ModelEstimationNodeModel.CFGKEY_FITTINGTYPE);
		} catch (InvalidSettingsException e) {
			fittingType = ModelEstimationNodeModel.DEFAULT_FITTINGTYPE;
		}

		try {
			enforceLimits = settings
					.getInt(ModelEstimationNodeModel.CFGKEY_ENFORCELIMITS);
		} catch (InvalidSettingsException e) {
			enforceLimits = ModelEstimationNodeModel.DEFAULT_ENFORCELIMITS;
		}

		try {
			nParameterSpace = settings
					.getInt(ModelEstimationNodeModel.CFGKEY_NPARAMETERSPACE);
		} catch (InvalidSettingsException e) {
			nParameterSpace = ModelEstimationNodeModel.DEFAULT_NPARAMETERSPACE;
		}

		try {
			nLevenberg = settings
					.getInt(ModelEstimationNodeModel.CFGKEY_NLEVENBERG);
		} catch (InvalidSettingsException e) {
			nLevenberg = ModelEstimationNodeModel.DEFAULT_NLEVENBERG;
		}

		try {
			stopWhenSuccessful = settings
					.getInt(ModelEstimationNodeModel.CFGKEY_STOPWHENSUCCESSFUL);
		} catch (InvalidSettingsException e) {
			stopWhenSuccessful = ModelEstimationNodeModel.DEFAULT_STOPWHENSUCCESSFUL;
		}

		try {
			parameterGuesses = ListUtilities
					.getStringListFromString(settings
							.getString(ModelEstimationNodeModel.CFGKEY_PARAMETERGUESSES));
		} catch (InvalidSettingsException e) {
			parameterGuesses = new ArrayList<String>();
		}

		if (fittingType.equals(ModelEstimationNodeModel.NO_FITTING)) {
			try {
				KnimeSchema seiSchema = new KnimeSchema(new KnimeSchema(
						new Model1Schema(), new Model2Schema()),
						new TimeSeriesSchema());

				if (seiSchema.conforms(input[0])) {
					fittingType = ModelEstimationNodeModel.SECONDARY_FITTING;
				} else {
					fittingType = ModelEstimationNodeModel.PRIMARY_FITTING;
				}
			} catch (PmmException e) {
				e.printStackTrace();
			}
		}

		fittingBox.setSelectedItem(fittingType);
		fittingBox.addActionListener(this);
		nParamSpaceField.setValue(nParameterSpace);
		nLevenbergField.setValue(nLevenberg);
		stopWhenSuccessBox.setSelected(stopWhenSuccessful == 1);
		limitsBox.setSelected(enforceLimits == 1);
		guessMap = ModelEstimationNodeModel.getGuessMap(parameterGuesses);

		try {
			initGUI();
		} catch (PmmException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!nParamSpaceField.isValueValid() || !nLevenbergField.isValueValid()
				|| minimumFields == null || maximumFields == null) {
			throw new InvalidSettingsException("");
		}

		settings.addString(ModelEstimationNodeModel.CFGKEY_FITTINGTYPE,
				(String) fittingBox.getSelectedItem());
		settings.addInt(ModelEstimationNodeModel.CFGKEY_NPARAMETERSPACE,
				nParamSpaceField.getValue());
		settings.addInt(ModelEstimationNodeModel.CFGKEY_NLEVENBERG,
				nLevenbergField.getValue());

		if (limitsBox.isSelected()) {
			settings.addInt(ModelEstimationNodeModel.CFGKEY_ENFORCELIMITS, 1);
		} else {
			settings.addInt(ModelEstimationNodeModel.CFGKEY_ENFORCELIMITS, 0);
		}

		if (stopWhenSuccessBox.isSelected()) {
			settings.addInt(ModelEstimationNodeModel.CFGKEY_STOPWHENSUCCESSFUL,
					1);
		} else {
			settings.addInt(ModelEstimationNodeModel.CFGKEY_STOPWHENSUCCESSFUL,
					0);
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
		modelNames = new LinkedHashMap<String, String>();
		parameters = new LinkedHashMap<String, List<String>>();
		minValues = new LinkedHashMap<String, Map<String, Double>>();
		maxValues = new LinkedHashMap<String, Map<String, Double>>();

		KnimeRelationReader reader = new KnimeRelationReader(
				new Model1Schema(), table);

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			String id = ModelEstimationNodeModel.PRIMARY
					+ ((CatalogModelXml) modelXml.get(0)).getID();

			if (!modelNames.containsKey(id)) {
				PmmXmlDoc params = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
				List<String> paramNames = new ArrayList<String>();
				Map<String, Double> min = new LinkedHashMap<String, Double>();
				Map<String, Double> max = new LinkedHashMap<String, Double>();

				for (PmmXmlElementConvertable el : params.getElementSet()) {
					ParamXml element = (ParamXml) el;

					paramNames.add(element.getName());
					min.put(element.getName(), element.getMin());
					max.put(element.getName(), element.getMax());
				}

				modelNames.put(id,
						((CatalogModelXml) modelXml.get(0)).getName());
				parameters.put(id, paramNames);
				minValues.put(id, min);
				maxValues.put(id, max);
			}
		}
	}

	private void readSecondaryTable(BufferedDataTable table)
			throws PmmException {
		readPrimaryTable(table);

		KnimeRelationReader reader = new KnimeRelationReader(
				new Model2Schema(), table);

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc modelXml = tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG);
			String id = ModelEstimationNodeModel.SECONDARY
					+ ((CatalogModelXml) modelXml.get(0)).getID();

			if (!modelNames.containsKey(id)) {
				PmmXmlDoc params = tuple.getPmmXml(Model2Schema.ATT_PARAMETER);
				List<String> paramNames = new ArrayList<String>();
				Map<String, Double> min = new LinkedHashMap<String, Double>();
				Map<String, Double> max = new LinkedHashMap<String, Double>();

				for (PmmXmlElementConvertable el : params.getElementSet()) {
					ParamXml element = (ParamXml) el;

					paramNames.add(element.getName());
					min.put(element.getName(), element.getMin());
					max.put(element.getName(), element.getMax());
				}

				modelNames.put(id,
						((CatalogModelXml) modelXml.get(0)).getName());
				parameters.put(id, paramNames);
				minValues.put(id, min);
				maxValues.put(id, max);
			}
		}
	}

	private JComponent createPanel() {
		JPanel panel = new JPanel();
		JPanel rangePanel = new JPanel();

		minimumFields = new LinkedHashMap<String, Map<String, DoubleTextField>>();
		maximumFields = new LinkedHashMap<String, Map<String, DoubleTextField>>();

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
				Double min = minValues.get(id).get(param);
				Double max = maxValues.get(id).get(param);

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
					minField.setValue(min);
					maxField.setValue(max);
				}

				String rangeString;

				if (min != null && max != null) {
					rangeString = " (" + min + " to " + max + "):";
				} else if (min != null) {
					rangeString = " (" + min + " to ):";
				} else if (max != null) {
					rangeString = " ( to " + max + "):";
				} else {
					rangeString = ":";
				}

				labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				labelPanel.add(new JLabel(param + rangeString));

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

			modelPanel.setBorder(BorderFactory.createTitledBorder(modelNames
					.get(id)));
			modelPanel.setLayout(new BorderLayout());
			modelPanel.add(leftPanel, BorderLayout.WEST);
			modelPanel.add(rightPanel, BorderLayout.EAST);

			rangePanel.add(modelPanel);
		}

		panel.setLayout(new BorderLayout());
		panel.add(rangePanel, BorderLayout.NORTH);

		return new JScrollPane(panel);
	}

	private void initGUI() throws PmmException {
		KnimeSchema peiSchema = new KnimeSchema(new Model1Schema(),
				new TimeSeriesSchema());
		KnimeSchema seiSchema = new KnimeSchema(new KnimeSchema(
				new Model1Schema(), new Model2Schema()), new TimeSeriesSchema());

		modelNames = null;
		parameters = null;
		minValues = null;
		maxValues = null;
		minimumFields = null;
		maximumFields = null;
		fittingPanel.removeAll();

		JComponent panel = null;

		if (fittingBox.getSelectedItem().equals(
				ModelEstimationNodeModel.PRIMARY_FITTING)
				&& peiSchema.conforms(input[0])) {
			readPrimaryTable(input[0]);
			panel = createPanel();
		} else if (fittingBox.getSelectedItem().equals(
				ModelEstimationNodeModel.SECONDARY_FITTING)
				&& seiSchema.conforms(input[0])) {
			readSecondaryTable(input[0]);
			panel = createPanel();
		} else if (fittingBox.getSelectedItem().equals(
				ModelEstimationNodeModel.ONESTEP_FITTING)
				&& seiSchema.conforms(input[0])) {
			readSecondaryTable(input[0]);
			panel = createPanel();
		}

		if (panel != null) {
			modelRangeButton = new JButton("Use Range from Formula Definition");
			modelRangeButton.addActionListener(this);
			rangeButton = new JButton("Fill Empty Fields");
			rangeButton.addActionListener(this);
			clearButton = new JButton("Clear");
			clearButton.addActionListener(this);

			JPanel buttonPanel = new JPanel();

			buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			buttonPanel.add(modelRangeButton);
			buttonPanel.add(rangeButton);
			buttonPanel.add(clearButton);

			fittingPanel.add(new SpacePanel(buttonPanel), BorderLayout.NORTH);
			fittingPanel.add(panel, BorderLayout.CENTER);
			fittingPanel.revalidate();
		} else {
			if (fittingBox.isValid()) {
				JOptionPane
						.showMessageDialog(fittingBox, "Data is not valid for "
								+ fittingBox.getSelectedItem());
			}

			fittingPanel.add(new JLabel(), BorderLayout.CENTER);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == fittingBox) {
			try {
				initGUI();
			} catch (PmmException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == modelRangeButton) {
			for (String id : parameters.keySet()) {
				for (String param : parameters.get(id)) {
					minimumFields.get(id).get(param)
							.setValue(minValues.get(id).get(param));
					maximumFields.get(id).get(param)
							.setValue(maxValues.get(id).get(param));
				}
			}
		} else if (e.getSource() == rangeButton) {
			for (String id : parameters.keySet()) {
				for (String param : parameters.get(id)) {
					Double min = minimumFields.get(id).get(param).getValue();
					Double max = maximumFields.get(id).get(param).getValue();

					if (min == null && max == null) {
						minimumFields.get(id).get(param).setValue(-1000000.0);
						maximumFields.get(id).get(param).setValue(1000000.0);
					}
				}
			}
		} else if (e.getSource() == clearButton) {
			for (String id : parameters.keySet()) {
				for (String param : parameters.get(id)) {
					minimumFields.get(id).get(param).setValue(null);
					maximumFields.get(id).get(param).setValue(null);
				}
			}
		}
	}
}
