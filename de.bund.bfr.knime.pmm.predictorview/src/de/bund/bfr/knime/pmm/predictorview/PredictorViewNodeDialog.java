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
package de.bund.bfr.knime.pmm.predictorview;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * <code>NodeDialog</code> for the "PredictorView" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class PredictorViewNodeDialog extends DataAwareNodeDialogPane {

	private static final String NO_PARAM = "";

	private KnimeSchema schema;
	private List<String> ids;
	private Map<String, String> modelNames;
	private Map<String, String> formulas;
	private Map<String, List<String>> availableParams;
	private Map<String, JComboBox<String>> paramBoxes;

	/**
	 * New pane for configuring the ForecastStaticConditions node.
	 */
	protected PredictorViewNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
		schema = new KnimeSchema(new Model1Schema(), new TimeSeriesSchema());
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		Map<String, String> concentrationParameters;

		try {
			concentrationParameters = XmlConverter
					.xmlToStringMap(settings
							.getString(PredictorViewNodeModel.CFGKEY_CONCENTRATIONPARAMETERS));
		} catch (InvalidSettingsException e) {
			concentrationParameters = new LinkedHashMap<String, String>();
		}

		readTable(input[0]);
		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createPanel(concentrationParameters));
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		Map<String, String> parameterMap = new LinkedHashMap<String, String>();

		for (String id : ids) {
			if (!paramBoxes.get(id).getSelectedItem().equals(NO_PARAM)) {
				parameterMap.put(id, (String) paramBoxes.get(id)
						.getSelectedItem());
			} else {
				parameterMap.put(id, null);
			}
		}

		settings.addString(
				PredictorViewNodeModel.CFGKEY_CONCENTRATIONPARAMETERS,
				XmlConverter.mapToXml(parameterMap));
	}

	private void readTable(BufferedDataTable table) {
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
			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			String id = ((CatalogModelXml) modelXml.get(0)).getID() + "";

			if (!idSet.add(id)) {
				continue;
			}

			List<String> params = new ArrayList<String>();

			params.add(NO_PARAM);
			params.addAll(CellIO.getNameList(tuple
					.getPmmXml(Model1Schema.ATT_PARAMETER)));

			ids.add(id);
			modelNames.put(id, ((CatalogModelXml) modelXml.get(0)).getName());
			formulas.put(id, ((CatalogModelXml) modelXml.get(0)).getFormula());
			availableParams.put(id, params);
		}
	}

	private JPanel createPanel(Map<String, String> params) {
		JPanel panel = new JPanel();
		JPanel parameterPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();

		leftPanel.setLayout(new GridLayout(ids.size(), 1, 5, 5));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new GridLayout(ids.size(), 1, 5, 5));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		paramBoxes = new LinkedHashMap<String, JComboBox<String>>();

		for (String id : ids) {
			JComboBox<String> box = new JComboBox<String>(availableParams.get(
					id).toArray(new String[0]));
			JLabel label = new JLabel(modelNames.get(id) + ":");

			if (params.get(id) != null) {
				box.setSelectedItem(params.get(id));
			}

			box.setPreferredSize(new Dimension(150,
					box.getPreferredSize().height));
			label.setToolTipText(formulas.get(id));
			paramBoxes.put(id, box);
			leftPanel.add(label);
			rightPanel.add(box);
		}

		parameterPanel.setBorder(BorderFactory
				.createTitledBorder("Initial Concentration Parameters"));
		parameterPanel.setLayout(new BorderLayout());
		parameterPanel.add(leftPanel, BorderLayout.WEST);
		parameterPanel.add(rightPanel, BorderLayout.EAST);

		panel.setLayout(new BorderLayout());
		panel.add(parameterPanel, BorderLayout.NORTH);

		return panel;
	}
}
