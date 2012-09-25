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
package de.bund.bfr.knime.pmm.common.combine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bund.bfr.knime.pmm.common.PmmException;
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
			boolean discardPrimaryParams, Map<String, String> doNotReplace)
			throws PmmException {
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
			doNotReplace = new HashMap<String, String>();
		}

		Map<String, KnimeTuple> newTuplesByOldID = new LinkedHashMap<String, KnimeTuple>();
		Map<String, List<KnimeTuple>> usedTuplesByOldID = new HashMap<String, List<KnimeTuple>>();
		Map<String, Set<String>> replacementsByOldID = new HashMap<String, Set<String>>();

		for (KnimeTuple tuple : tuples) {
			String id = null;

			if (inSchema == seiSchema) {
				id = tuple.getInt(Model1Schema.ATT_ESTMODELID) + "("
						+ tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			} else if (inSchema == model12Schema) {
				id = tuple.getInt(Model1Schema.ATT_ESTMODELID) + "";
			}

			if (!newTuplesByOldID.containsKey(id)) {
				KnimeTuple newTuple = new KnimeTuple(outSchema);

				for (int i = 0; i < outSchema.size(); i++) {
					String attr = outSchema.getName(i);

					newTuple.setCell(attr, tuple.getCell(attr));
				}

				if (discardPrimaryParams) {
					newTuple.setValue(Model1Schema.ATT_VALUE, Collections
							.nCopies(
									newTuple.getDoubleList(
											Model1Schema.ATT_VALUE).size(),
									null));
				}

				newTuplesByOldID.put(id, newTuple);
				usedTuplesByOldID.put(id, new ArrayList<KnimeTuple>());
				replacementsByOldID.put(id, new HashSet<String>());
			}

			String depVarSec = tuple.getString(Model2Schema.ATT_DEPVAR);

			if (depVarSec.equals(doNotReplace.get(tuple
					.getInt(Model1Schema.ATT_MODELID) + ""))) {
				continue;
			}

			if (!replacementsByOldID.get(id).add(depVarSec)) {
				continue;
			}

			usedTuplesByOldID.get(id).add(tuple);

			KnimeTuple newTuple = newTuplesByOldID.get(id);
			String formulaSec = tuple.getString(Model2Schema.ATT_FORMULA);
			List<String> indepVarsSec = tuple
					.getStringList(Model2Schema.ATT_INDEPVAR);
			List<String> keysSec = tuple
					.getStringList(Model2Schema.ATT_PARAMNAME);
			List<Double> valuesSec = tuple
					.getDoubleList(Model2Schema.ATT_VALUE);

			for (int i = 0; i < keysSec.size(); i++) {
				int index = 1;
				String newKey = keysSec.get(i);

				while (newTuple.getStringList(Model1Schema.ATT_PARAMNAME)
						.contains(newKey)) {
					index++;
					newKey = keysSec.get(i) + index;
				}

				if (index > 1) {
					formulaSec = MathUtilities.replaceVariable(formulaSec,
							keysSec.get(i), newKey);
					keysSec.set(i, newKey);
				}
			}

			String replacement = "(" + formulaSec.replace(depVarSec + "=", "")
					+ ")";
			String newFormula = MathUtilities.replaceVariable(
					newTuple.getString(Model1Schema.ATT_FORMULA), depVarSec,
					replacement);
			Set<String> newIndepVars = new LinkedHashSet<String>();
			List<String> newKeys = new ArrayList<String>();
			List<Double> newValues = new ArrayList<Double>();

			newIndepVars.addAll(newTuple
					.getStringList(Model1Schema.ATT_INDEPVAR));
			newIndepVars.addAll(indepVarsSec);
			newValues.addAll(newTuple.getDoubleList(Model1Schema.ATT_VALUE));
			newKeys.addAll(newTuple.getStringList(Model1Schema.ATT_PARAMNAME));
			newValues.remove(newKeys.indexOf(depVarSec));
			newKeys.remove(depVarSec);
			newValues.addAll(valuesSec);
			newKeys.addAll(keysSec);

			newTuple.setValue(Model1Schema.ATT_FORMULA, newFormula);
			newTuple.setValue(Model1Schema.ATT_INDEPVAR, new ArrayList<String>(
					newIndepVars));
			newTuple.setValue(Model1Schema.ATT_PARAMNAME, newKeys);
			newTuple.setValue(Model1Schema.ATT_VALUE, newValues);
			newTuplesByOldID.put(id, newTuple);
		}

		Map<KnimeTuple, List<KnimeTuple>> newTuples = new LinkedHashMap<KnimeTuple, List<KnimeTuple>>();

		for (String id : newTuplesByOldID.keySet()) {
			KnimeTuple newTuple = newTuplesByOldID.get(id);
			List<KnimeTuple> usedTuples = usedTuplesByOldID.get(id);
			int modelCount = usedTuples.size() + 1;
			int paramCount = newTuple.getStringList(Model1Schema.ATT_PARAMNAME)
					.size();
			int newID = newTuple.getInt(Model1Schema.ATT_MODELID) / modelCount;

			for (KnimeTuple tuple : usedTuples) {
				newID += tuple.getInt(Model2Schema.ATT_MODELID) / modelCount;
			}

			if (newTuple.getInt(Model1Schema.ATT_ESTMODELID) != null) {
				Integer newEstID = newTuple.getInt(Model1Schema.ATT_ESTMODELID)
						/ modelCount;

				for (KnimeTuple tuple : usedTuples) {
					if (tuple.getInt(Model2Schema.ATT_ESTMODELID) != null) {
						newEstID += tuple.getInt(Model2Schema.ATT_ESTMODELID)
								/ modelCount;
					} else {
						newEstID = null;
						break;
					}
				}

				newTuple.setValue(Model1Schema.ATT_ESTMODELID, newEstID);
			}

			newTuple.setValue(Model1Schema.ATT_MODELID, newID);
			newTuple.setValue(Model1Schema.ATT_DBUUID, null);
			newTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					Model1Schema.NOTWRITABLE);
			newTuple.setValue(Model1Schema.ATT_LITEM, null);
			newTuple.setValue(Model1Schema.ATT_LITIDEM, null);
			newTuple.setValue(Model1Schema.ATT_LITIDM, null);
			newTuple.setValue(Model1Schema.ATT_LITM, null);
			newTuple.setValue(Model1Schema.ATT_RMS, null);
			newTuple.setValue(Model1Schema.ATT_RSQUARED, null);
			newTuple.setValue(Model1Schema.ATT_PARAMERR,
					Collections.nCopies(paramCount, null));
			newTuple.setValue(Model1Schema.ATT_MINVALUE,
					Collections.nCopies(paramCount, null));
			newTuple.setValue(Model1Schema.ATT_MAXVALUE,
					Collections.nCopies(paramCount, null));

			newTuples.put(newTuple, usedTuples);
		}

		return newTuples;
	}
}
