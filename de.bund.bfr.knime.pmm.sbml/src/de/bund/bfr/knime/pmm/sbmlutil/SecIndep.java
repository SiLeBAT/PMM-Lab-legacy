/**
 * Pmm Lab secondary independent
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.Map;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.annotation.sbml.DescriptionAnnotation;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;

/**
 * Secondary independent. Holds an SBML parameter and its description.
 * 
 * @author Miguel Alba
 */
public class SecIndep {

	Parameter param;
	String desc;

	/**
	 * Builds a SecIndep from an SBML parameter which can be annotated with an
	 * SecIndepAnnotation.
	 * 
	 * @param param
	 *            SBML parameter.
	 */
	public SecIndep(Parameter param) {
		// If param has annotation, processes it
		if (param.getAnnotation().getNonRDFannotation() != null) {
			DescriptionAnnotation annot = new DescriptionAnnotation(param.getAnnotation());
			desc = annot.getDescription();
		}

		// copies parameter
		this.param = param;
	}

	/**
	 * Builds a SecIndep from a Pmm Lab IndepXml.
	 * 
	 * @param indepXml Pmm Lab IndepXml.
	 */
	public SecIndep(IndepXml indepXml) {
		// Initializes param
		param = new Parameter(indepXml.getName());
		param.setConstant(false);

		// If indepXml has a description, saves it within a SecIndepAnnotation
		if (indepXml.getDescription() != null) {
			// Builds and sets non RDF annotation
			param.setAnnotation(new DescriptionAnnotation(indepXml.getDescription()).getAnnotation());
		}
		param.setValue(0.0);
		
		// Adds unit
		if (indepXml.getUnit() == null) {
			param.setUnits(Unit.Kind.DIMENSIONLESS);
		} else {
			param.setUnits(Util.createId(indepXml.getUnit()));
		}
	}

	/** Creates a Pmm Lab IndepXml. */
	public IndepXml toIndepXml(ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {
		IndepXml indepXml = new IndepXml(param.getId(), null, null);
		indepXml.setDescription(desc);
		
		// Adds unit and unit category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			indepXml.setUnit(unitName);
			indepXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
		}
		
		// Adds limits
        if (limits.containsKey(param.getId())) {
        	Limits indepLimits = limits.get(param.getId());
        	indepXml.setMax(indepLimits.getMax());
        	indepXml.setMin(indepLimits.getMin());
		}

		return indepXml;
	}

	// Getters
	public Parameter getParam() {
		return param;
	}

	public String getDescription() {
		return desc;
	}
}