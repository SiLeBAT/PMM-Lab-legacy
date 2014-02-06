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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

public class TableReader {

	private Map<String, SBMLDocument> documents;

	public TableReader(List<KnimeTuple> tuples) {
		boolean isTertiaryModel = tuples.get(0).getSchema()
				.conforms(SchemaFactory.createM12Schema());
		Set<String> idSet = new LinkedHashSet<String>();

		if (isTertiaryModel) {
			tuples = new ArrayList<KnimeTuple>(ModelCombiner.combine(tuples,
					false, null, null).keySet());
		}

		documents = new LinkedHashMap<String, SBMLDocument>();

		for (KnimeTuple tuple : tuples) {
			String id = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID()
					+ "";

			if (!idSet.add(id)) {
				continue;
			}

			EstModelXml estXml = (EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0);
			String modelID = "Model_Test" + Math.abs(estXml.getID());

			SBMLDocument doc = new SBMLDocument(2, 4);

			doc.createModel(modelID);

			documents.put(modelID, doc);
		}
	}

	public Map<String, SBMLDocument> getDocuments() {
		return documents;
	}
}
