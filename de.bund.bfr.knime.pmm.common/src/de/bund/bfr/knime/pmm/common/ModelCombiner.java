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
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class ModelCombiner {

	private ModelCombiner() {
	}

	public static Map<KnimeTuple, List<KnimeTuple>> combine(
			List<KnimeTuple> tuples, KnimeSchema schema,
			boolean discardPrimaryParams, Map<String, String> doNotReplace) {
		KnimeSchema seiSchema = new KnimeSchema(new KnimeSchema(
				new Model1Schema(), new Model2Schema()), new TimeSeriesSchema());
		KnimeSchema peiSchema = new KnimeSchema(new Model1Schema(),
				new TimeSeriesSchema());
		KnimeSchema model12Schema = new KnimeSchema(new Model1Schema(),
				new Model2Schema());
		KnimeSchema model1Schema = new Model1Schema();
		KnimeSchema inSchema = null;
		KnimeSchema outSchema = null;

		if (seiSchema.conforms(schema.createSpec())) {
			inSchema = seiSchema;
			outSchema = peiSchema;
		} else if (model12Schema.conforms(schema.createSpec())) {
			inSchema = model12Schema;
			outSchema = model1Schema;
		}

		if (doNotReplace == null) {
			doNotReplace = new LinkedHashMap<String, String>();
		}

		Map<String, KnimeTuple> newTuples = new LinkedHashMap<String, KnimeTuple>();
		Map<String, List<KnimeTuple>> usedTupleLists = new LinkedHashMap<String, List<KnimeTuple>>();
		Map<String, Set<String>> replacements = new LinkedHashMap<String, Set<String>>();

		for (KnimeTuple tuple : tuples) {
			String id = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID()
					+ "";

			if (inSchema == seiSchema) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			if (!newTuples.containsKey(id)) {
				KnimeTuple newTuple = new KnimeTuple(outSchema);

				for (int i = 0; i < outSchema.size(); i++) {
					String attr = outSchema.getName(i);

					newTuple.setCell(attr, tuple.getCell(attr));
				}

				if (discardPrimaryParams) {
					PmmXmlDoc params = newTuple
							.getPmmXml(Model1Schema.ATT_PARAMETER);

					for (PmmXmlElementConvertable el : params.getElementSet()) {
						ParamXml element = (ParamXml) el;

						element.setValue(null);
					}

					newTuple.setValue(Model1Schema.ATT_PARAMETER, params);
				}

				newTuples.put(id, newTuple);
				usedTupleLists.put(id, new ArrayList<KnimeTuple>());
				replacements.put(id, new LinkedHashSet<String>());
			}

			String depVarSec = ((DepXml) tuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0)).getName();
			String modelID = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID()
					+ "";

			if (!depVarSec.equals(doNotReplace.get(modelID))
					&& replacements.get(id).add(depVarSec)) {
				usedTupleLists.get(id).add(tuple);
			}
		}

		Map<KnimeTuple, List<KnimeTuple>> tupleCombinations = new LinkedHashMap<KnimeTuple, List<KnimeTuple>>();

		for (String id : newTuples.keySet()) {
			KnimeTuple newTuple = newTuples.get(id);
			List<KnimeTuple> usedTuples = usedTupleLists.get(id);

			for (KnimeTuple tuple : usedTuples) {
				String formulaSec = ((CatalogModelXml) tuple.getPmmXml(
						Model2Schema.ATT_MODELCATALOG).get(0)).getFormula();
				String depVarSec = ((DepXml) tuple.getPmmXml(
						Model2Schema.ATT_DEPENDENT).get(0)).getName();
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

			int modelCount = usedTuples.size() + 1;
			int newID = ((CatalogModelXml) newTuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID()
					/ modelCount;

			for (KnimeTuple tuple : usedTuples) {
				newID += ((CatalogModelXml) tuple.getPmmXml(
						Model2Schema.ATT_MODELCATALOG).get(0)).getID()
						/ modelCount;
			}

			Integer newEstID = ((EstModelXml) newTuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID();

			if (!usedTuples.isEmpty()) {
				boolean allParamsReplaced = usedTuples.get(0)
						.getPmmXml(Model1Schema.ATT_PARAMETER).size() == usedTuples
						.size();

				// if all primary parameters were replaced by secondary models,
				// the estID of the primary is not used for the new tertiary
				// estID (catalog id is used instead)
				if (allParamsReplaced) {
					newEstID = ((CatalogModelXml) newTuple.getPmmXml(
							Model1Schema.ATT_MODELCATALOG).get(0)).getID();
				}
			}

			if (newEstID != null) {
				newEstID /= modelCount;

				for (KnimeTuple tuple : usedTuples) {
					Integer estID = ((EstModelXml) tuple.getPmmXml(
							Model2Schema.ATT_ESTMODEL).get(0)).getID();

					if (estID != null) {
						newEstID += estID / modelCount;
					} else {
						newEstID = null;
						break;
					}
				}
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

		return tupleCombinations;
	}
}
