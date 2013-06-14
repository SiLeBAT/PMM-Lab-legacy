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
package de.bund.bfr.knime.pmm.modelanddatajoiner;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.NumberContent;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.Time;

public class PrimaryJoiner implements Joiner {

	private BufferedDataTable modelTable;
	private BufferedDataTable dataTable;

	private Map<Integer, Map<String, JComboBox<String>>> variableBoxes;

	private Map<Integer, String> modelNames;
	private Map<Integer, Map<String, String>> variableCategories;
	private Map<Integer, Map<String, String>> variableUnits;
	private Map<String, String> parameterCategories;

	private List<KnimeTuple> modelTuples;

	public PrimaryJoiner(BufferedDataTable modelTable,
			BufferedDataTable dataTable) {
		this.modelTable = modelTable;
		this.dataTable = dataTable;

		readModelTable();
		readDataTable();
	}

	@Override
	public JComponent createPanel(String assignments) {
		Map<Integer, Map<String, String>> assignmentsMap = XmlConverter
				.xmlToObject(assignments,
						new LinkedHashMap<Integer, Map<String, String>>());

		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel();

		panel.setLayout(new BorderLayout());
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		variableBoxes = new LinkedHashMap<>();

		for (Integer id : modelNames.keySet()) {
			JPanel modelPanel = new JPanel();
			Map<String, JComboBox<String>> boxes = new LinkedHashMap<>();

			modelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			modelPanel.setBorder(BorderFactory.createTitledBorder(modelNames
					.get(id)));

			for (String var : variableCategories.get(id).keySet()) {
				String category = variableCategories.get(id).get(var);
				JComboBox<String> box = new JComboBox<String>(
						getParamsFromCategory(category).toArray(new String[0]));

				if (assignmentsMap.containsKey(id)) {
					box.setSelectedItem(assignmentsMap.get(id).get(var));
				} else {
					box.setSelectedItem(null);
				}

				boxes.put(var, box);
				modelPanel.add(new JLabel(var + ":"));
				modelPanel.add(box);
			}

			variableBoxes.put(id, boxes);
			topPanel.add(modelPanel);
		}

		panel.add(topPanel, BorderLayout.NORTH);

		return panel;
	}

	@Override
	public String getAssignments() {
		Map<Integer, Map<String, String>> assignmentsMap = new LinkedHashMap<>();

		for (int id : variableBoxes.keySet()) {
			assignmentsMap.put(id, new LinkedHashMap<String, String>());

			for (String param : variableBoxes.get(id).keySet()) {
				String assignment = (String) variableBoxes.get(id).get(param)
						.getSelectedItem();

				assignmentsMap.get(id).put(param, assignment);
			}
		}

		return XmlConverter.objectToXml(assignmentsMap);
	}

	@Override
	public BufferedDataTable getOutputTable(String assignments,
			ExecutionContext exec) throws CanceledExecutionException {
		BufferedDataContainer container = exec
				.createDataContainer(SchemaFactory.createM1DataSchema()
						.createSpec());
		Map<Integer, Map<String, String>> assignmentsMap = XmlConverter
				.xmlToObject(assignments,
						new LinkedHashMap<Integer, Map<String, String>>());
		int rowCount = modelTuples.size() * dataTable.getRowCount();
		int index = 0;

		for (int i = 0; i < modelTuples.size(); i++) {
			KnimeTuple modelTuple = modelTuples.get(i);
			PmmXmlDoc modelXml = modelTuple
					.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			int id = ((CatalogModelXml) modelXml.get(0)).getID();
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			PmmXmlDoc depVar = modelTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
			String depVarName = ((DepXml) depVar.get(0)).getName();
			PmmXmlDoc oldIndepVar = modelTuple
					.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc newIndepVar = new PmmXmlDoc();
			Map<String, String> assign = assignmentsMap.get(id);
			List<String> oldVars = new ArrayList<>();

			if (!assign.containsKey(depVarName)) {
				continue;
			}

			oldVars.add(depVarName);
			formula = MathUtilities.replaceVariable(formula, depVarName,
					assign.get(depVarName));
			depVarName = assign.get(depVarName);
			((DepXml) depVar.get(0)).setName(depVarName);

			boolean error = false;

			for (PmmXmlElementConvertable el : oldIndepVar.getElementSet()) {
				IndepXml iv = (IndepXml) el;

				if (!assign.containsKey(iv.getName())) {
					error = true;
					break;
				}

				oldVars.add(iv.getName());
				formula = MathUtilities.replaceVariable(formula, iv.getName(),
						assign.get(iv.getName()));
				iv.setName(assign.get(iv.getName()));
				newIndepVar.add(iv);
			}

			if (error) {
				continue;
			}

			((CatalogModelXml) modelXml.get(0)).setFormula(formula);

			modelTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
			modelTuple.setValue(Model1Schema.ATT_DEPENDENT, depVar);
			modelTuple.setValue(Model1Schema.ATT_INDEPENDENT, newIndepVar);
			modelTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					Model1Schema.NOTWRITABLE);

			KnimeRelationReader reader = new KnimeRelationReader(
					SchemaFactory.createDataSchema(), dataTable);

			while (reader.hasMoreElements()) {
				KnimeTuple dataTuple = reader.nextElement();
				PmmXmlDoc timeSeries = dataTuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				PmmXmlDoc misc = dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				Map<String, String> paramsConvertTo = new LinkedHashMap<>();

				for (String var : oldVars) {
					paramsConvertTo.put(assign.get(var), variableUnits.get(id)
							.get(var));
				}

				String timeUnit = paramsConvertTo.get(AttributeUtilities.TIME);
				String concentrationUnit = paramsConvertTo
						.get(AttributeUtilities.LOGC);

				for (PmmXmlElementConvertable el : timeSeries.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					element.setTime(new Time().convert(element.getTime(),
							element.getTimeUnit(), timeUnit));
					element.setConcentration(new NumberContent()
							.convert(element.getConcentration(),
									element.getConcentrationUnit(),
									concentrationUnit));
					element.setTimeUnit(timeUnit);
					element.setConcentrationUnit(concentrationUnit);
				}

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					if (paramsConvertTo.containsKey(element.getName())) {
						Category cat = Categories.getCategory(element
								.getCategory());
						String unit = paramsConvertTo.get(element.getName());

						element.setValue(cat.convert(element.getValue(),
								element.getUnit(), unit));
						element.setUnit(unit);
					}
				}

				dataTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeries);
				dataTuple.setValue(TimeSeriesSchema.ATT_MISC, misc);

				KnimeTuple tuple = new KnimeTuple(
						SchemaFactory.createM1DataSchema(), modelTuple,
						dataTuple);

				container.addRowToTable(tuple);
				exec.checkCanceled();
				exec.setProgress((double) index / (double) rowCount, "");
				index++;
			}
		}

		container.close();

		return container.getTable();
	}

	@Override
	public boolean isValid() {
		for (int id : variableBoxes.keySet()) {
			for (String param : variableBoxes.get(id).keySet()) {
				if (variableBoxes.get(id).get(param).getSelectedItem() == null) {
					return false;
				}
			}
		}

		return true;
	}

	private void readModelTable() {
		Set<Integer> ids = new LinkedHashSet<Integer>();
		Set<Integer> estIDs = new LinkedHashSet<Integer>();
		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createM1Schema(), modelTable);

		modelTuples = new ArrayList<>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			int id = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID();
			Integer estID = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID();

			if (estID != null) {
				if (estIDs.add(estID)) {
					modelTuples.add(tuple);
				}
			} else {
				if (ids.add(id)) {
					modelTuples.add(tuple);
				}
			}
		}

		modelNames = new LinkedHashMap<>();
		variableCategories = new LinkedHashMap<>();
		variableUnits = new LinkedHashMap<>();

		for (KnimeTuple tuple : modelTuples) {
			CatalogModelXml modelXml = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0));
			int id = modelXml.getID();

			if (modelNames.containsKey(id)) {
				continue;
			}

			modelNames.put(id, modelXml.getName());

			Map<String, String> categories = new LinkedHashMap<>();
			Map<String, String> units = new LinkedHashMap<>();

			DepXml dep = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
					.get(0);

			categories.put(dep.getName(), dep.getCategory());
			units.put(dep.getName(), dep.getUnit());

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model1Schema.ATT_INDEPENDENT).getElementSet()) {
				IndepXml indep = (IndepXml) el;

				categories.put(indep.getName(), indep.getCategory());
				units.put(indep.getName(), indep.getUnit());
			}

			variableCategories.put(id, categories);
			variableUnits.put(id, units);
		}
	}

	private void readDataTable() {
		parameterCategories = new LinkedHashMap<>();
		parameterCategories.put(AttributeUtilities.TIME, Categories.TIME);
		parameterCategories.put(AttributeUtilities.LOGC,
				Categories.NUMBER_CONTENT);

		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createDataSchema(), dataTable);

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				parameterCategories.put(element.getName(),
						element.getCategory());
			}
		}
	}

	private List<String> getParamsFromCategory(String category) {
		List<String> params = new ArrayList<>();

		for (String param : parameterCategories.keySet()) {
			if (category == null
					|| parameterCategories.get(param).equals(category)) {
				params.add(param);
			}
		}

		return params;
	}

}
