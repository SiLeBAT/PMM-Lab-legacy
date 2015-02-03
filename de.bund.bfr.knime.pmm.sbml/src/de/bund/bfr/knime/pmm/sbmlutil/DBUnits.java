package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;

public class DBUnits {

	Map<Integer, UnitDefinition> unitDefs;  // unit definitions
	Map<Integer, UnitsFromDB> units;  // DB units

	public DBUnits() {
		// Get units from DB
		UnitsFromDB ufdb = new UnitsFromDB();
		ufdb.askDB();
		units = (ufdb.getMap());

		// Get unit definitions
		unitDefs = new HashMap<>();
		for (Entry<Integer, UnitsFromDB> entry : units.entrySet()) {
			Integer id = entry.getKey();
			String math = entry.getValue().getMathML_string();
			if (!math.isEmpty()) {
				UnitDefinition ud = SBMLUtil.fromXml(math);
				unitDefs.put(id, ud);
			}
		}
	}

	/*
	 * Get info from the DB of the units used in the list of unit definitions of
	 * a model
	 */
	public Map<String, UnitsFromDB> getUnits(
			ListOf<UnitDefinition> modelUnitDefs) {
		Map<String, UnitsFromDB> modelUnits = new HashMap<>();

		// for each unit definition in the model
		for (UnitDefinition modelUnitDef : modelUnitDefs) {
			// Search unit definition from DB
			for (Entry<Integer, UnitDefinition> entry : unitDefs.entrySet()) {
				UnitDefinition dbUnitDef = entry.getValue(); // Unit def from DB

				// If the unit definition from the model and the one from the
				// DB matches, then retrieve and add the unit from the DB
				if (UnitDefinition.areIdentical(modelUnitDef, dbUnitDef)) {
					UnitsFromDB dbUnit = units.get(entry.getKey());
					modelUnits.put(modelUnitDef.getId(), dbUnit);
				}
			}
		}

		return modelUnits;
	}
}