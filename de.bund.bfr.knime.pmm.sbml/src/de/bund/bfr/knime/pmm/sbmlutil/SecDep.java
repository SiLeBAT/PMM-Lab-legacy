package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.Map;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.annotation.sbml.DescriptionAnnotation;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;

/**
 * Secondary dependent variable. Holds an SBML parameter and its description.
 * 
 * @author Miguel de Alba
 */
public class SecDep {

	Parameter param;
	String desc;

	/**
	 * Builds a SecDep from an SBML parameter which can be annotated with an
	 * SecDepAnnotation.
	 * 
	 * @param SBML
	 *            parameter
	 */
	public SecDep(Parameter param) {
		// If param has annotation, processes it
		if (param.getAnnotation().getNonRDFannotation() != null) {
			DescriptionAnnotation annot = new DescriptionAnnotation(param.getAnnotation());
			desc = annot.getDescription();
		}
		// Copies parameter
		this.param = param;
	}

	/**
	 * Builds a SecDep from a PmmLab DepXml.
	 * 
	 * @param depXml
	 *            PmmLab DepXml
	 */
	public SecDep(DepXml depXml) {
		// Initializes param
		param = new Parameter(depXml.getName());
		param.setConstant(false);
		param.setValue(0.0);

		// If depXml has a description, saves it within a SecDepAnnotation
		if (depXml.getDescription() != null) {
			// Builds and sets a non RDF annotation
			param.setAnnotation(new DescriptionAnnotation(depXml.getDescription()).getAnnotation());
		}
		
		// Adds unit
		if (depXml.getUnit() != null) {
			param.setUnits(Util.createId(depXml.getUnit()));
		}
	}

	/**
	 * Creates a PmmLab DepXml
	 */

	public DepXml toDepXml(ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {
		DepXml depXml = new DepXml(param.getId(), null, null);
		depXml.setDescription(desc);	
		if (param.isSetUnits()) {
			// Adds unit
			String unitID = param.getUnits();
			String unitName = unitDefs.get(unitID).getName();
			depXml.setUnit(unitName);
			
			// Adds unit category
			Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();
			if (dbUnits.containsKey(unitName)) {
				UnitsFromDB dbUnit = dbUnits.get(unitName);
				depXml.setCategory(dbUnit.getKind_of_property_quantity());
			}
		}
		
		// Adds limits
        if (limits.containsKey(param.getId())) {
        	Limits depLimits = limits.get(param.getId());
        	depXml.setMax(depLimits.getMax());
        	depXml.setMin(depLimits.getMin());
		}

		return depXml;
	}

	// Getters
	public Parameter getParam() {
		return param;
	}

	public String getDescription() {
		return desc;
	}
}
