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
import javax.swing.JScrollPane;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.NumberContent;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.Time;

public class CombinedJoiner implements Joiner {

	private BufferedDataTable modelTable;
	private BufferedDataTable dataTable;

	private Map<String, Map<String, JComboBox<String>>> primaryVariableBoxes;
	private Map<String, Map<String, JComboBox<String>>> secondaryVariableBoxes;

	private Map<String, String> primaryModelNames;
	private Map<String, String> secondaryModelNames;
	private Map<String, Map<String, String>> primaryVariableCategories;
	private Map<String, Map<String, String>> primaryVariableUnits;
	private Map<String, Map<String, String>> secondaryVariableCategories;
	private Map<String, Map<String, String>> secondaryVariableUnits;
	private Map<String, String> primaryParameterCategories;
	private Map<String, String> secondaryParameterCategories;

	private List<KnimeTuple> modelTuples;

	public CombinedJoiner(BufferedDataTable modelTable,
			BufferedDataTable dataTable) {
		this.modelTable = modelTable;
		this.dataTable = dataTable;

		readDataTable();
		readModelTable();
	}

	@Override
	public JComponent createPanel(String assignments) {
		Map<String, Map<String, String>> assignmentsMap = XmlConverter
				.xmlToObject(assignments,
						new LinkedHashMap<String, Map<String, String>>());
		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel();

		panel.setLayout(new BorderLayout());
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		primaryVariableBoxes = new LinkedHashMap<>();
		secondaryVariableBoxes = new LinkedHashMap<>();

		for (String modelID : primaryModelNames.keySet()) {
			JPanel primaryPanel = new JPanel();
			Map<String, JComboBox<String>> boxes = new LinkedHashMap<>();
			Map<String, String> map = assignmentsMap.get(modelID);

			if (map == null) {
				map = new LinkedHashMap<>();
			}

			primaryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			primaryPanel.setBorder(BorderFactory
					.createTitledBorder(primaryModelNames.get(modelID)));

			for (String var : primaryVariableCategories.get(modelID).keySet()) {
				JComboBox<String> box = new JComboBox<String>(
						getPrimParamsFromCategory(
								primaryVariableCategories.get(modelID).get(var))
								.toArray(new String[0]));

				if (map.containsKey(var)) {
					box.setSelectedItem(map.get(var));
				} else {
					box.setSelectedItem(null);
				}

				boxes.put(var, box);
				primaryPanel.add(new JLabel(var + ":"));
				primaryPanel.add(box);
			}

			topPanel.add(primaryPanel);
			primaryVariableBoxes.put(modelID, boxes);
		}

		for (String modelID : secondaryVariableCategories.keySet()) {
			JPanel secondaryPanel = new JPanel();
			Map<String, JComboBox<String>> boxes = new LinkedHashMap<>();
			Map<String, String> map = assignmentsMap.get(modelID);

			if (map == null) {
				map = new LinkedHashMap<>();
			}

			secondaryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			secondaryPanel.setBorder(BorderFactory
					.createTitledBorder(secondaryModelNames.get(modelID)));

			for (String var : secondaryVariableCategories.get(modelID).keySet()) {
				JComboBox<String> box = new JComboBox<String>(
						getSecParamsFromCategory(
								secondaryVariableCategories.get(modelID).get(
										var)).toArray(new String[0]));

				if (map.containsKey(var)) {
					box.setSelectedItem(map.get(var));
				} else {
					box.setSelectedItem(null);
				}

				boxes.put(var, box);
				secondaryPanel.add(new JLabel(var + ":"));
				secondaryPanel.add(box);
			}

			topPanel.add(secondaryPanel);
			secondaryVariableBoxes.put(modelID, boxes);
		}

		panel.add(topPanel, BorderLayout.NORTH);

		return new JScrollPane(panel);
	}

	@Override
	public String getAssignments() {
		Map<String, Map<String, String>> assignmentsMap = new LinkedHashMap<>();

		for (String modelID : primaryVariableBoxes.keySet()) {
			Map<String, String> primaryAssignments = new LinkedHashMap<>();

			for (String var : primaryVariableBoxes.get(modelID).keySet()) {
				String replacement = (String) primaryVariableBoxes.get(modelID)
						.get(var).getSelectedItem();

				primaryAssignments.put(var, replacement);
			}

			assignmentsMap.put(modelID, primaryAssignments);
		}

		for (String modelID : secondaryVariableBoxes.keySet()) {
			Map<String, String> secondaryAssignments = new LinkedHashMap<>();

			for (String var : secondaryVariableBoxes.get(modelID).keySet()) {
				String replacement = (String) secondaryVariableBoxes
						.get(modelID).get(var).getSelectedItem();

				secondaryAssignments.put(var, replacement);
			}

			assignmentsMap.put(modelID, secondaryAssignments);
		}

		return XmlConverter.objectToXml(assignmentsMap);
	}

	@Override
	public BufferedDataTable getOutputTable(String assignments,
			ExecutionContext exec) throws CanceledExecutionException {
		BufferedDataContainer container = exec
				.createDataContainer(SchemaFactory.createM12DataSchema()
						.createSpec());
		Map<String, Map<String, String>> replacements = XmlConverter
				.xmlToObject(assignments,
						new LinkedHashMap<String, Map<String, String>>());
		int rowCount = modelTable.getRowCount() * dataTable.getRowCount();
		int index = 0;

		for (KnimeTuple modelTuple : modelTuples) {
			PmmXmlDoc modelXml = modelTuple
					.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			String depVarSecName = ((DepXml) modelTuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0)).getName();
			String modelID = ((CatalogModelXml) modelXml.get(0)).getID() + "";
			String modelIDSec = depVarSecName + " (" + modelID + ")";
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			PmmXmlDoc depVar = modelTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
			String depVarName = ((DepXml) depVar.get(0)).getName();
			PmmXmlDoc indepVar = modelTuple
					.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc newIndepVar = new PmmXmlDoc();
			PmmXmlDoc modelXmlSec = modelTuple
					.getPmmXml(Model2Schema.ATT_MODELCATALOG);
			String formulaSec = ((CatalogModelXml) modelXmlSec.get(0))
					.getFormula();
			PmmXmlDoc indepVarSec = modelTuple
					.getPmmXml(Model2Schema.ATT_INDEPENDENT);
			PmmXmlDoc newIndepVarSec = new PmmXmlDoc();
			Map<String, String> primAssign = replacements.get(modelID);
			Map<String, String> secAssign = replacements.get(modelIDSec);
			List<String> oldPrimVars = new ArrayList<>();
			List<String> oldSecVars = new ArrayList<>();
			boolean error = false;

			if (primAssign == null || secAssign == null
					|| !primAssign.containsKey(depVarName)) {
				index += dataTable.getRowCount();
				continue;
			}

			oldPrimVars.add(depVarName);
			formula = MathUtilities.replaceVariable(formula, depVarName,
					primAssign.get(depVarName));
			depVarName = primAssign.get(depVarName);
			((DepXml) depVar.get(0)).setName(depVarName);

			for (PmmXmlElementConvertable el : indepVar.getElementSet()) {
				IndepXml iv = (IndepXml) el;

				if (!primAssign.containsKey(iv.getName())) {
					error = true;
					break;
				}

				oldPrimVars.add(iv.getName());
				formula = MathUtilities.replaceVariable(formula, iv.getName(),
						primAssign.get(iv.getName()));
				iv.setName(primAssign.get(iv.getName()));
				newIndepVar.add(iv);
			}

			if (error) {
				index += dataTable.getRowCount();
				continue;
			}

			for (PmmXmlElementConvertable el : indepVarSec.getElementSet()) {
				IndepXml iv = (IndepXml) el;

				if (!secAssign.containsKey(iv.getName())) {
					error = true;
					break;
				}

				oldSecVars.add(iv.getName());
				formulaSec = MathUtilities.replaceVariable(formulaSec,
						iv.getName(), secAssign.get(iv.getName()));
				iv.setName(secAssign.get(iv.getName()));
				newIndepVarSec.add(iv);
			}

			if (error) {
				index += dataTable.getRowCount();
				continue;
			}

			((CatalogModelXml) modelXml.get(0)).setFormula(formula);
			((CatalogModelXml) modelXmlSec.get(0)).setFormula(formulaSec);

			modelTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
			modelTuple.setValue(Model1Schema.ATT_DEPENDENT, depVar);
			modelTuple.setValue(Model1Schema.ATT_INDEPENDENT, newIndepVar);
			modelTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					Model1Schema.NOTWRITABLE);
			modelTuple.setValue(Model2Schema.ATT_MODELCATALOG, modelXmlSec);
			modelTuple.setValue(Model2Schema.ATT_INDEPENDENT, newIndepVarSec);
			modelTuple.setValue(Model2Schema.ATT_DATABASEWRITABLE,
					Model1Schema.NOTWRITABLE);

			KnimeRelationReader dataReader = new KnimeRelationReader(
					SchemaFactory.createDataSchema(), dataTable);

			while (dataReader.hasMoreElements()) {
				KnimeTuple dataTuple = dataReader.nextElement();
				PmmXmlDoc timeSeries = dataTuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				PmmXmlDoc misc = dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				Map<String, String> paramsConvertTo = new LinkedHashMap<>();

				for (String var : oldPrimVars) {
					paramsConvertTo.put(primAssign.get(var),
							primaryVariableUnits.get(modelID).get(var));
				}

				for (String var : oldSecVars) {
					paramsConvertTo.put(secAssign.get(var),
							secondaryVariableUnits.get(modelIDSec).get(var));
				}

				String timeUnit = paramsConvertTo.get(AttributeUtilities.TIME);
				String concentrationUnit = paramsConvertTo
						.get(AttributeUtilities.LOGC);

				for (PmmXmlElementConvertable el : timeSeries.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					element.setTime(new Time().convert(element.getTime(),
							element.getTimeUnit(), timeUnit));
					element.setConcentration(new NumberContent().convert(
							element.getConcentration(),
							element.getConcentrationUnit(), concentrationUnit));
					element.setTimeUnit(timeUnit);
					element.setConcentrationUnit(concentrationUnit);
				}

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					if (paramsConvertTo.containsKey(element.getName())) {
						Category cat = Categories.getCategoryByUnit(
								element.getCategories(), element.getUnit());
						String unit = paramsConvertTo.get(element.getName());

						element.setValue(cat.convert(element.getValue(),
								element.getUnit(), unit));
						element.setUnit(unit);
					}
				}

				dataTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeries);
				dataTuple.setValue(TimeSeriesSchema.ATT_MISC, misc);

				KnimeTuple tuple = new KnimeTuple(
						SchemaFactory.createM12DataSchema(), modelTuple,
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
		return true;
	}

	private void readDataTable() {
		primaryParameterCategories = new LinkedHashMap<>();
		secondaryParameterCategories = new LinkedHashMap<>();

		primaryParameterCategories
				.put(AttributeUtilities.TIME, Categories.TIME);
		primaryParameterCategories.put(AttributeUtilities.LOGC,
				Categories.NUMBER_CONTENT);

		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createDataSchema(), dataTable);

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				secondaryParameterCategories.put(
						element.getName(),
						Categories.getCategoryByUnit(element.getCategories(),
								element.getUnit()).getName());
			}
		}
	}

	private void readModelTable() {
		boolean containsData = SchemaFactory.createM12DataSchema().conforms(
				modelTable);
		Map<KnimeTuple, List<KnimeTuple>> tuples;

		if (containsData) {
			tuples = ModelCombiner.combine(
					PmmUtilities.getTuples(modelTable,
							SchemaFactory.createM12DataSchema()), true, false,
					null);
		} else {
			tuples = ModelCombiner.combine(
					PmmUtilities.getTuples(modelTable,
							SchemaFactory.createM12Schema()), false, false,
					null);
		}

		Set<Integer> ids = new LinkedHashSet<Integer>();
		Set<Integer> estIDs = new LinkedHashSet<Integer>();

		modelTuples = new ArrayList<>();

		for (KnimeTuple tuple : tuples.keySet()) {
			int id = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID();
			Integer estID = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID();

			if (estID != null) {
				if (estIDs.add(estID)) {
					modelTuples.addAll(tuples.get(tuple));
				}
			} else {
				if (ids.add(id)) {
					modelTuples.addAll(tuples.get(tuple));
				}
			}
		}

		primaryModelNames = new LinkedHashMap<>();
		secondaryModelNames = new LinkedHashMap<>();
		primaryVariableCategories = new LinkedHashMap<>();
		primaryVariableUnits = new LinkedHashMap<>();
		secondaryVariableCategories = new LinkedHashMap<>();
		secondaryVariableUnits = new LinkedHashMap<>();

		for (KnimeTuple tuple : modelTuples) {
			CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);
			String depVarSec = ((DepXml) tuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0)).getName();
			String modelID = modelXml.getID() + "";
			String modelIDSec = depVarSec + " (" + modelXml.getID() + ")";

			if (!primaryModelNames.containsKey(modelID)) {
				Map<String, String> categories = new LinkedHashMap<>();
				Map<String, String> units = new LinkedHashMap<>();
				DepXml depXml = (DepXml) tuple.getPmmXml(
						Model1Schema.ATT_DEPENDENT).get(0);

				categories.put(depXml.getName(), depXml.getCategory());
				units.put(depXml.getName(), depXml.getUnit());

				for (PmmXmlElementConvertable el : tuple.getPmmXml(
						Model1Schema.ATT_INDEPENDENT).getElementSet()) {
					IndepXml element = (IndepXml) el;

					categories.put(element.getName(), element.getCategory());
					units.put(element.getName(), element.getUnit());
				}

				primaryModelNames.put(modelID, modelXml.getName());
				primaryVariableCategories.put(modelID, categories);
				primaryVariableUnits.put(modelID, units);
			}

			if (!secondaryModelNames.containsKey(modelIDSec)) {
				Map<String, String> categories = new LinkedHashMap<>();
				Map<String, String> units = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : tuple.getPmmXml(
						Model2Schema.ATT_INDEPENDENT).getElementSet()) {
					IndepXml element = (IndepXml) el;

					categories.put(element.getName(), element.getCategory());
					units.put(element.getName(), element.getUnit());
				}

				if (containsData) {
					for (PmmXmlElementConvertable el : tuple.getPmmXml(
							TimeSeriesSchema.ATT_MISC).getElementSet()) {
						MiscXml element = (MiscXml) el;

						if (categories.containsKey(element.getName())
								&& categories.get(element.getName()) == null) {
							categories.put(
									element.getName(),
									Categories.getCategoryByUnit(
											element.getCategories(),
											element.getUnit()).getName());
							units.put(element.getName(), element.getUnit());
						}
					}
				}

				secondaryModelNames.put(modelIDSec,
						depVarSec + " (" + modelXml.getName() + ")");
				secondaryVariableCategories.put(modelIDSec, categories);
				secondaryVariableUnits.put(modelIDSec, units);
			}
		}
	}

	private List<String> getPrimParamsFromCategory(String category) {
		List<String> params = new ArrayList<>();

		for (String param : primaryParameterCategories.keySet()) {
			if (category == null
					|| primaryParameterCategories.get(param).equals(category)) {
				params.add(param);
			}
		}

		return params;
	}

	private List<String> getSecParamsFromCategory(String category) {
		List<String> params = new ArrayList<>();

		for (String param : secondaryParameterCategories.keySet()) {
			if (category == null
					|| secondaryParameterCategories.get(param).equals(category)) {
				params.add(param);
			}
		}

		return params;
	}

}
