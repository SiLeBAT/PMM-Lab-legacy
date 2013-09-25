/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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
package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class ModelCombiner {

	private ModelCombiner() {
	}

	public static Map<KnimeTuple, List<KnimeTuple>> combine(
			List<KnimeTuple> tuples, boolean containsData,
			Map<String, String> initParams, Map<String, String> lagParams) {
		KnimeSchema outSchema = null;

		if (containsData) {
			outSchema = SchemaFactory.createM1DataSchema();
		} else {
			outSchema = SchemaFactory.createM1Schema();
		}

		if (initParams == null) {
			initParams = new LinkedHashMap<>();
		}

		if (lagParams == null) {
			lagParams = new LinkedHashMap<>();
		}

		Map<String, KnimeTuple> newTuples = new LinkedHashMap<>();
		Map<String, List<KnimeTuple>> usedTupleLists = new LinkedHashMap<>();
		Map<String, Set<String>> replacements = new LinkedHashMap<>();
		Map<Integer, Map<String, Double>> paramValueSums = new LinkedHashMap<>();
		Map<Integer, Map<String, Integer>> paramCounts = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			int modelId = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID();

			if (!paramValueSums.containsKey(modelId)) {
				Map<String, Double> sums = new LinkedHashMap<>();
				Map<String, Integer> counts = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : tuple.getPmmXml(
						Model1Schema.ATT_PARAMETER).getElementSet()) {
					ParamXml param = (ParamXml) el;

					sums.put(param.getName(), 0.0);
					counts.put(param.getName(), 0);
				}

				paramValueSums.put(modelId, sums);
				paramCounts.put(modelId, counts);
			}

			Map<String, Double> sums = paramValueSums.get(modelId);
			Map<String, Integer> counts = paramCounts.get(modelId);

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model1Schema.ATT_PARAMETER).getElementSet()) {
				ParamXml param = (ParamXml) el;

				if (param.getValue() != null) {
					sums.put(param.getName(),
							sums.get(param.getName()) + param.getValue());
					counts.put(param.getName(), counts.get(param.getName()) + 1);
				}
			}

			String id = modelId + "";

			if (containsData) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			if (!newTuples.containsKey(id)) {
				KnimeTuple newTuple = new KnimeTuple(outSchema);

				for (int i = 0; i < outSchema.size(); i++) {
					String attr = outSchema.getName(i);

					newTuple.setCell(attr, tuple.getCell(attr));
				}

				PmmXmlDoc params = newTuple
						.getPmmXml(Model1Schema.ATT_PARAMETER);

				for (PmmXmlElementConvertable el : params.getElementSet()) {
					ParamXml element = (ParamXml) el;

					element.setValue(null);
				}

				newTuple.setValue(Model1Schema.ATT_PARAMETER, params);

				newTuples.put(id, newTuple);
				usedTupleLists.put(id, new ArrayList<KnimeTuple>());
				replacements.put(id, new LinkedHashSet<String>());
			}

			String depVarSec = ((DepXml) tuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0)).getName();

			if (replacements.get(id).add(depVarSec)) {
				usedTupleLists.get(id).add(tuple);
			}
		}

		Map<KnimeTuple, List<KnimeTuple>> tupleCombinations = new LinkedHashMap<>();

		for (String id : newTuples.keySet()) {
			KnimeTuple newTuple = newTuples.get(id);
			List<KnimeTuple> usedTuples = usedTupleLists.get(id);

			for (KnimeTuple tuple : usedTuples) {
				String modelID = ((CatalogModelXml) tuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).getID()
						+ "";
				String depVarSec = ((DepXml) tuple.getPmmXml(
						Model2Schema.ATT_DEPENDENT).get(0)).getName();

				if (depVarSec.equals(initParams.get(modelID))
						|| depVarSec.equals(lagParams.get(modelID))) {
					continue;
				}

				String formulaSec = ((CatalogModelXml) tuple.getPmmXml(
						Model2Schema.ATT_MODELCATALOG).get(0)).getFormula();
				PmmXmlDoc indepVarsSec = tuple
						.getPmmXml(Model2Schema.ATT_INDEPENDENT);
				PmmXmlDoc paramsSec = tuple
						.getPmmXml(Model2Schema.ATT_PARAMETER);

				for (PmmXmlElementConvertable el : paramsSec.getElementSet()) {
					ParamXml element = (ParamXml) el;
					int index = 1;
					String paramName = element.getName();
					String newParamName = paramName;

					while (CellIO.getNameList(
							newTuple.getPmmXml(Model1Schema.ATT_PARAMETER))
							.contains(newParamName)) {
						index++;
						newParamName = paramName + index;
					}

					if (index > 1) {
						formulaSec = MathUtilities.replaceVariable(formulaSec,
								paramName, newParamName);
						element.setName(newParamName);
					}

					element.getAllCorrelations().clear();
				}

				String replacement = "("
						+ formulaSec.replace(depVarSec + "=", "") + ")";
				String formula = ((CatalogModelXml) newTuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).getFormula();
				String newFormula = MathUtilities.replaceVariable(formula,
						depVarSec, replacement);
				PmmXmlDoc newParams = newTuple
						.getPmmXml(Model1Schema.ATT_PARAMETER);
				PmmXmlDoc newIndepVars = newTuple
						.getPmmXml(Model1Schema.ATT_INDEPENDENT);

				newParams.getElementSet().remove(
						CellIO.getNameList(newParams).indexOf(depVarSec));
				newParams.getElementSet().addAll(paramsSec.getElementSet());

				for (PmmXmlElementConvertable el : indepVarsSec.getElementSet()) {
					IndepXml element = (IndepXml) el;

					if (!CellIO.getNameList(newIndepVars).contains(
							element.getName())) {
						newIndepVars.getElementSet().add(element);
					}
				}

				PmmXmlDoc modelXml = tuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG);

				((CatalogModelXml) modelXml.get(0)).setFormula(newFormula);

				newTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
				newTuple.setValue(Model1Schema.ATT_INDEPENDENT, newIndepVars);
				newTuple.setValue(Model1Schema.ATT_PARAMETER, newParams);
			}

			int newID = ((CatalogModelXml) newTuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID();

			for (KnimeTuple tuple : usedTuples) {
				newID += ((CatalogModelXml) tuple.getPmmXml(
						Model2Schema.ATT_MODELCATALOG).get(0)).getID();
			}

			newID = MathUtilities.generateID(newID);

			Integer newEstID = 0;

			for (KnimeTuple tuple : usedTuples) {
				Integer estID = ((EstModelXml) tuple.getPmmXml(
						Model2Schema.ATT_ESTMODEL).get(0)).getID();

				if (estID != null) {
					newEstID += estID;
				} else {
					newEstID = null;
					break;
				}
			}

			if (newEstID != null) {
				newEstID = MathUtilities.generateID(newEstID);
			}

			PmmXmlDoc modelXml = newTuple
					.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			PmmXmlDoc estModelXml = newTuple
					.getPmmXml(Model1Schema.ATT_ESTMODEL);

			((CatalogModelXml) modelXml.get(0)).setID(newID);
			((EstModelXml) estModelXml.get(0)).setID(newEstID);
			((EstModelXml) estModelXml.get(0)).setRMS(null);
			((EstModelXml) estModelXml.get(0)).setR2(null);
			((EstModelXml) estModelXml.get(0)).setAIC(null);
			((EstModelXml) estModelXml.get(0)).setBIC(null);

			newTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
			newTuple.setValue(Model1Schema.ATT_ESTMODEL, estModelXml);
			newTuple.setValue(Model1Schema.ATT_DBUUID, null);
			newTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					Model1Schema.NOTWRITABLE);

			tupleCombinations.put(newTuple, usedTuples);
		}

		for (KnimeTuple tuple : tupleCombinations.keySet()) {
			int id = ((CatalogModelXml) tupleCombinations.get(tuple).get(0)
					.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0)).getID();
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			Map<String, Double> sums = paramValueSums.get(id);
			Map<String, Integer> counts = paramCounts.get(id);

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml param = (ParamXml) el;

				if (param.getValue() == null
						&& counts.get(param.getName()) != null) {
					if (counts.get(param.getName()) != 0) {
						param.setValue(sums.get(param.getName())
								/ counts.get(param.getName()));
					}

					param.getAllCorrelations().clear();
					param.setError(null);
					param.sett(null);
					param.setP(null);
				}
			}

			tuple.setValue(Model1Schema.ATT_PARAMETER, paramXml);
		}

		return tupleCombinations;
	}
}
