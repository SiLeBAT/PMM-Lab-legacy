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
import java.util.Arrays;
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
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class CombinedJoiner implements Joiner {

	private static final String PRIMARY = "Primary";

	private KnimeSchema modelSchema;
	private KnimeSchema dataSchema;
	private KnimeSchema seiSchema;

	private BufferedDataTable modelTable;
	private BufferedDataTable dataTable;

	private List<JComboBox<String>> primaryVariableBoxes;
	private Map<String, List<JComboBox<String>>> secondaryVariableBoxes;

	private List<String> primaryVariables;
	private Map<String, List<String>> secondaryVariables;
	private List<String> primaryParameters;
	private List<String> secondaryParameters;

	public CombinedJoiner(BufferedDataTable modelTable,
			BufferedDataTable dataTable) {
		this.modelTable = modelTable;
		this.dataTable = dataTable;

		modelSchema = new KnimeSchema(new Model1Schema(), new Model2Schema());
		dataSchema = new TimeSeriesSchema();
		seiSchema = new KnimeSchema(new KnimeSchema(new Model1Schema(),
				new Model2Schema()), new TimeSeriesSchema());
		readDataTable();
		readModelTable();
	}

	@Override
	public JComponent createPanel(String assignments) {
		Map<String, Map<String, String>> assignmentsMap = XmlConverter
				.xmlToStringMapMap(assignments);

		primaryVariableBoxes = new ArrayList<JComboBox<String>>(
				primaryVariables.size());
		secondaryVariableBoxes = new LinkedHashMap<String, List<JComboBox<String>>>();

		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel();

		panel.setLayout(new BorderLayout());
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		JPanel primaryPanel = new JPanel();

		primaryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		primaryPanel.setBorder(BorderFactory
				.createTitledBorder("Primary Variables"));

		for (String var : primaryVariables) {
			JComboBox<String> box = new JComboBox<String>(
					primaryParameters.toArray(new String[0]));

			if (assignmentsMap.containsKey(PRIMARY)
					&& assignmentsMap.get(PRIMARY).containsKey(var)) {
				box.setSelectedItem(assignmentsMap.get(PRIMARY).get(var));
			} else {
				box.setSelectedItem(null);
			}

			primaryVariableBoxes.add(box);
			primaryPanel.add(new JLabel(var + ":"));
			primaryPanel.add(box);
		}

		topPanel.add(primaryPanel);

		for (String depVarSec : secondaryVariables.keySet()) {
			JPanel secondaryPanel = new JPanel();
			List<JComboBox<String>> boxes = new ArrayList<JComboBox<String>>();

			secondaryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			secondaryPanel.setBorder(BorderFactory.createTitledBorder(depVarSec
					+ "-Model Variables"));

			for (String var : secondaryVariables.get(depVarSec)) {
				JComboBox<String> box = new JComboBox<String>(
						secondaryParameters.toArray(new String[0]));

				if (assignmentsMap.containsKey(depVarSec)
						&& assignmentsMap.get(depVarSec).containsKey(var)) {
					box.setSelectedItem(assignmentsMap.get(depVarSec).get(var));
				} else {
					box.setSelectedItem(null);
				}

				boxes.add(box);
				secondaryPanel.add(new JLabel(var + ":"));
				secondaryPanel.add(box);
			}

			secondaryVariableBoxes.put(depVarSec, boxes);
			topPanel.add(secondaryPanel);
		}

		panel.add(topPanel, BorderLayout.NORTH);

		return new JScrollPane(panel);
	}

	@Override
	public String getAssignments() {
		Map<String, Map<String, String>> assignmentsMap = new LinkedHashMap<>();
		Map<String, String> primaryAssignments = new LinkedHashMap<>();

		for (int i = 0; i < primaryVariables.size(); i++) {
			String replacement = (String) primaryVariableBoxes.get(i)
					.getSelectedItem();

			if (!replacement.equals("")) {
				primaryAssignments.put(primaryVariables.get(i), replacement);
			}
		}

		assignmentsMap.put(PRIMARY, primaryAssignments);

		for (String depVarSec : secondaryVariables.keySet()) {
			Map<String, String> secondaryAssignments = new LinkedHashMap<>();

			for (int i = 0; i < secondaryVariables.get(depVarSec).size(); i++) {
				String replacement = (String) secondaryVariableBoxes
						.get(depVarSec).get(i).getSelectedItem();

				if (!replacement.equals("")) {
					secondaryAssignments.put(secondaryVariables.get(depVarSec)
							.get(i), replacement);
				}
			}

			assignmentsMap.put(depVarSec, secondaryAssignments);
		}

		return XmlConverter.mapToXml(assignmentsMap);
	}

	@Override
	public BufferedDataTable getOutputTable(String assignments,
			ExecutionContext exec) throws CanceledExecutionException {
		BufferedDataContainer container = exec.createDataContainer(seiSchema
				.createSpec());
		Map<String, Map<String, String>> replacements = XmlConverter
				.xmlToStringMapMap(assignments);
		int rowCount = modelTable.getRowCount() * dataTable.getRowCount();
		int index = 0;

		if (!replacements.containsKey(PRIMARY)) {
			container.close();

			return container.getTable();
		}

		KnimeRelationReader modelReader = new KnimeRelationReader(modelSchema,
				modelTable);
		Set<String> ids = new LinkedHashSet<String>();

		while (modelReader.hasMoreElements()) {
			KnimeTuple modelTuple = modelReader.nextElement();
			int modelID = ((CatalogModelXml) modelTuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID();
			String depVarSecName = ((DepXml) modelTuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0)).getName();

			if (!ids.add(modelID + "(" + depVarSecName + ")")) {
				index += dataTable.getRowCount();
				continue;
			}

			PmmXmlDoc modelXml = modelTuple
					.getPmmXml(Model1Schema.ATT_MODELCATALOG);
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
			boolean allVarsReplaced = true;

			if (replacements.get(PRIMARY).containsKey(depVarName)) {
				depVarName = replacements.get(PRIMARY).get(depVarName);
				((DepXml) depVar.get(0)).setName(depVarName);
			} else {
				allVarsReplaced = false;
			}

			for (String var : replacements.get(PRIMARY).keySet()) {
				String newVar = replacements.get(PRIMARY).get(var);

				formula = MathUtilities.replaceVariable(formula, var, newVar);
			}

			for (PmmXmlElementConvertable el : indepVar.getElementSet()) {
				IndepXml iv = (IndepXml) el;

				if (replacements.get(PRIMARY).containsKey(iv.getName())) {
					iv.setName(replacements.get(PRIMARY).get(iv.getName()));
					newIndepVar.add(iv);
				} else {
					allVarsReplaced = false;
					break;
				}
			}

			for (String var : replacements.get(depVarSecName).keySet()) {
				String newVar = replacements.get(depVarSecName).get(var);

				formulaSec = MathUtilities.replaceVariable(formulaSec, var,
						newVar);
			}

			for (PmmXmlElementConvertable el : indepVarSec.getElementSet()) {
				IndepXml iv = (IndepXml) el;

				if (replacements.containsKey(depVarSecName)
						&& replacements.get(depVarSecName).containsKey(
								iv.getName())) {
					iv.setName(replacements.get(depVarSecName)
							.get(iv.getName()));
					newIndepVarSec.add(iv);
				} else {
					allVarsReplaced = false;
					break;
				}
			}

			if (!allVarsReplaced) {
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
					dataSchema, dataTable);

			while (dataReader.hasMoreElements()) {
				KnimeTuple dataTuple = dataReader.nextElement();
				KnimeTuple tuple = new KnimeTuple(seiSchema, modelTuple,
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
		Set<String> secParamSet = new LinkedHashSet<String>();
		KnimeRelationReader reader = new KnimeRelationReader(dataSchema,
				dataTable);

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			secParamSet.addAll(CellIO.getNameList(misc));
		}

		primaryParameters = Arrays.asList(AttributeUtilities.LOGC,
				AttributeUtilities.TIME);
		secondaryParameters = new ArrayList<String>(secParamSet);
	}

	private void readModelTable() {
		KnimeRelationReader reader = new KnimeRelationReader(modelSchema,
				modelTable);
		Set<String> ids = new LinkedHashSet<String>();
		Set<String> primaryVarSet = new LinkedHashSet<String>();

		secondaryVariables = new LinkedHashMap<String, List<String>>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			Integer estModelID = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID();
			String depVar = ((DepXml) tuple.getPmmXml(
					Model1Schema.ATT_DEPENDENT).get(0)).getName();
			String depVarSec = ((DepXml) tuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0)).getName();

			if (estModelID != null
					&& !ids.add(estModelID + "(" + depVarSec + ")")) {
				continue;
			}

			primaryVarSet.add(depVar);
			primaryVarSet.addAll(CellIO.getNameList(tuple
					.getPmmXml(Model1Schema.ATT_INDEPENDENT)));

			if (!secondaryVariables.containsKey(depVarSec)) {
				secondaryVariables.put(depVarSec, CellIO.getNameList(tuple
						.getPmmXml(Model2Schema.ATT_INDEPENDENT)));
			}
		}

		primaryVariables = new ArrayList<String>(primaryVarSet);
	}

}
