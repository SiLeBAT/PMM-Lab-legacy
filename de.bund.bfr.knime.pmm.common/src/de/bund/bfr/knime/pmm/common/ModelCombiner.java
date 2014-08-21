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
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

public class ModelCombiner {

	private Map<KnimeTuple, List<KnimeTuple>> tupleCombinations;
	private Map<KnimeTuple, Map<KnimeTuple, Map<String, String>>> parameterRenaming;

	public ModelCombiner(List<KnimeTuple> tuples, boolean containsData,
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
		Map<Integer, Set<String>> organisms = new LinkedHashMap<>();
		Map<Integer, Set<String>> matrices = new LinkedHashMap<>();
		Map<Integer, Set<String>> organismDetails = new LinkedHashMap<>();
		Map<Integer, Set<String>> matrixDetails = new LinkedHashMap<>();
		Map<Integer, Set<String>> comments = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			int modelId = -1;

			try {
				modelId = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			} catch (Exception e) {
				continue;
			}

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
				organisms.put(modelId, new LinkedHashSet<String>());
				matrices.put(modelId, new LinkedHashSet<String>());
				organismDetails.put(modelId, new LinkedHashSet<String>());
				matrixDetails.put(modelId, new LinkedHashSet<String>());
				comments.put(modelId, new LinkedHashSet<String>());
			}

			if (containsData) {
				String organism = ((AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0)).getName();
				String matrix = ((MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0)).getName();
				String organismDetail = ((AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0)).getDetail();
				String matrixDetail = ((MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0)).getDetail();
				String comment = ((MdInfoXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MDINFO).get(0)).getComment();

				if (organism != null) {
					organisms.get(modelId).add(organism);
				}

				if (matrix != null) {
					matrices.get(modelId).add(matrix);
				}

				if (organismDetail != null) {
					organismDetails.get(modelId).add(organismDetail);
				}

				if (matrixDetail != null) {
					matrixDetails.get(modelId).add(matrixDetail);
				}

				if (comment != null) {
					comments.get(modelId).add(comment);
				}
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

		tupleCombinations = new LinkedHashMap<>();
		parameterRenaming = new LinkedHashMap<>();

		for (String id : newTuples.keySet()) {
			KnimeTuple newTuple = newTuples.get(id);
			List<KnimeTuple> usedTuples = usedTupleLists.get(id);
			Map<KnimeTuple, Map<String, String>> rename = new LinkedHashMap<>();

			for (KnimeTuple tuple : usedTuples) {
				rename.put(tuple, new LinkedHashMap<String, String>());

				String modelID = ((CatalogModelXml) tuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).getId()
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

					rename.get(tuple).put(paramName, newParamName);

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
					} else {
						IndepXml original = null;

						for (PmmXmlElementConvertable el2 : newIndepVars
								.getElementSet()) {
							if (((IndepXml) el2).getName().equals(
									element.getName())) {
								original = (IndepXml) el2;
								break;
							}
						}

						String fromUnit = original.getUnit();
						String toUnit = element.getUnit();
						Category cat = Categories.getCategoryByUnit(fromUnit);

						if (fromUnit != null && !fromUnit.equals(toUnit)) {
							try {
								String conversion = "("
										+ cat.getConversionString(
												element.getName(), fromUnit,
												toUnit) + ")";

								replacement = MathUtilities.replaceVariable(
										replacement, element.getName(),
										conversion);
							} catch (ConvertException e) {
								e.printStackTrace();
							}
						}
					}
				}

				PmmXmlDoc modelXml = tuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG);

				((CatalogModelXml) modelXml.get(0)).setFormula(MathUtilities
						.replaceVariable(formula, depVarSec, replacement));

				newTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
				newTuple.setValue(Model1Schema.ATT_INDEPENDENT, newIndepVars);
				newTuple.setValue(Model1Schema.ATT_PARAMETER, newParams);
			}

			int newID = ((CatalogModelXml) newTuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getId();

			for (KnimeTuple tuple : usedTuples) {
				newID += ((CatalogModelXml) tuple.getPmmXml(
						Model2Schema.ATT_MODELCATALOG).get(0)).getId();
			}

			newID = MathUtilities.generateID(newID);

			Integer newEstID = ((CatalogModelXml) newTuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getId();

			for (KnimeTuple tuple : usedTuples) {
				Integer estID = ((EstModelXml) tuple.getPmmXml(
						Model2Schema.ATT_ESTMODEL).get(0)).getId();

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

			((CatalogModelXml) modelXml.get(0)).setId(newID);
			((EstModelXml) estModelXml.get(0)).setId(newEstID);
			((EstModelXml) estModelXml.get(0)).setSse(null);
			((EstModelXml) estModelXml.get(0)).setRms(null);
			((EstModelXml) estModelXml.get(0)).setR2(null);
			((EstModelXml) estModelXml.get(0)).setAic(null);			

			newTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
			newTuple.setValue(Model1Schema.ATT_ESTMODEL, estModelXml);
			newTuple.setValue(Model1Schema.ATT_DBUUID, null);
			newTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					Model1Schema.NOTWRITABLE);

			tupleCombinations.put(newTuple, usedTuples);
			parameterRenaming.put(newTuple, rename);
		}

		for (KnimeTuple tuple : tupleCombinations.keySet()) {
			int id = tupleCombinations.get(tuple).get(0)
					.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
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
					param.setT(null);
					param.setP(null);
				}
			}

			tuple.setValue(Model1Schema.ATT_PARAMETER, paramXml);

			if (containsData) {
				String organism = "";
				String matrix = "";
				String organismDetail = "";
				String matrixDetail = "";
				String comment = "";

				for (String o : organisms.get(id)) {
					organism += "," + o;
				}

				if (!organism.isEmpty()) {
					organism = organism.substring(1);
				}

				for (String m : matrices.get(id)) {
					matrix += "," + m;
				}

				if (!matrix.isEmpty()) {
					matrix = matrix.substring(1);
				}

				for (String o : organismDetails.get(id)) {
					organismDetail += "," + o;
				}

				if (!organismDetail.isEmpty()) {
					organismDetail = organismDetail.substring(1);
				}

				for (String m : matrixDetails.get(id)) {
					matrixDetail += "," + m;
				}

				if (!matrixDetail.isEmpty()) {
					matrixDetail = matrixDetail.substring(1);
				}

				for (String c : comments.get(id)) {
					comment += "," + c;
				}

				if (!comment.isEmpty()) {
					comment = comment.substring(1);
				}

				AgentXml organismXml = new AgentXml();
				MatrixXml matrixXml = new MatrixXml();
				MdInfoXml infoXml = new MdInfoXml(null, null, comment, null,
						null);

				organismXml.setName(organism);
				organismXml.setDetail(organismDetail);
				matrixXml.setName(matrix);
				matrixXml.setDetail(matrixDetail);

				tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(
						organismXml));
				tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(
						matrixXml));
				tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(
						infoXml));
			}
		}
	}

	public Map<KnimeTuple, List<KnimeTuple>> getTupleCombinations() {
		return tupleCombinations;
	}

	public Map<KnimeTuple, Map<KnimeTuple, Map<String, String>>> getParameterRenaming() {
		return parameterRenaming;
	}
}
