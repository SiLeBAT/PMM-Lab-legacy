/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.pmfwriter;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

public class SBMLUtilities {

	/**
	 * Add a unit definition to an existing model.
	 * 
	 * @param unit
	 *            . Unit definition to be added.
	 * @return null
	 */
	public static String toXml(UnitDefinition unit) {
		int unitLevel = unit.getLevel();
		int unitVersion = unit.getVersion();

		SBMLDocument doc = new SBMLDocument(unitLevel, unitVersion);
		Model model = doc.createModel("ID");

		model.addUnitDefinition(unit);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			SBMLWriter.write(doc, out, "test", "1.0", ' ', (short) 0);

			String xml = out.toString(StandardCharsets.UTF_8.name());
			String from = "<listOfUnitDefinitions>";
			String to = "</listOfUnitDefinitions>";

			return xml.substring(xml.indexOf(from) + from.length(),
					xml.indexOf(to)).replace("\n", "");
		} catch (SBMLException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static UnitDefinition addUnitToModel(Model model, UnitDefinition unit) {
		UnitDefinition u = model.createUnitDefinition(unit.getId());

		for (int i = 0; i < unit.getNumUnits(); i++) {
			u.addUnit(new Unit(unit.getUnit(i)));
		}

		return u;
	}

	public static Unit.Kind simplify(UnitDefinition unit) {
		unit = unit.clone().simplify();

		if (unit.isUnitKind()) {
			Unit u = unit.getUnit(0);

			if (u.getScale() == 0 && u.getExponent() == 1
					&& u.getMultiplier() == 1) {
				return unit.getUnit(0).getKind();
			}
		}

		return null;
	}
}
