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
package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.text.parser.FormulaParser;
import org.sbml.jsbml.text.parser.ParseException;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

public class TableReader {

	private Map<String, SBMLDocument> documents;

	public TableReader(List<KnimeTuple> tuples) {
		boolean isTertiaryModel = tuples.get(0).getSchema()
				.conforms(SchemaFactory.createM12Schema());
		Set<Integer> idSet = new LinkedHashSet<Integer>();

		if (isTertiaryModel) {
			tuples = new ArrayList<KnimeTuple>(ModelCombiner.combine(tuples,
					false, null, null).keySet());
		}

		documents = new LinkedHashMap<String, SBMLDocument>();

		for (KnimeTuple tuple : tuples) {
			CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);
			EstModelXml estXml = (EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0);
			DepXml depXml = (DepXml) tuple
					.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);

			Integer id = estXml.getID();

			if (!idSet.add(id)) {
				continue;
			}

			String modelID = "Model_Test" + Math.abs(estXml.getID());
			SBMLDocument doc = new SBMLDocument(2, 4);
			Model model = doc.createModel(modelID);
			Parameter depParam = model.createParameter(depXml.getName());

			depParam.setValue(0.0);
			depParam.setConstant(false);

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model1Schema.ATT_PARAMETER).getElementSet()) {
				ParamXml paramXml = (ParamXml) el;
				Parameter param = model.createParameter(paramXml.getName());

				param.setConstant(true);

				if (paramXml.getValue() != null) {
					param.setValue(paramXml.getValue());
				} else {
					param.setValue(0.0);
				}
			}

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model1Schema.ATT_INDEPENDENT).getElementSet()) {
				IndepXml indepXml = (IndepXml) el;

				if (!indepXml.getName().equals(AttributeUtilities.TIME)) {
					Parameter param = model.createParameter(indepXml.getName());

					param.setValue(0.0);
					param.setConstant(false);
				}
			}

			FormulaParser parser = new FormulaParser(new StringReader(modelXml
					.getFormula().substring(
							modelXml.getFormula().indexOf("=") + 1)));

			try {
				ListOf<Rule> rules = new ListOf<Rule>(2, 4);

				rules.add(new AssignmentRule(depParam, parser.parse()));
				model.setListOfRules(rules);
				documents.put(modelID, doc);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, SBMLDocument> getDocuments() {
		return documents;
	}
}
