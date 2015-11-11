/** Pmm Lab coefficient.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.annotation.sbml.CoefficientAnnotation;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;

/**
 * Coefficient that extends the SBML parameter with more data: P, error,
 * correlations and a description string.
 * 
 * @author Miguel Alba
 */
public class Coefficient {

	Parameter param;
	Double P;
	Double error;
	Double t;
	Map<String, Double> correlations;
	String desc;

	/**
	 * Builds a Coefficient from existing SBML parameter.
	 * 
	 * @param param
	 *            SBML parameter with CoefficientAnnotation.
	 */
	public Coefficient(Parameter param) {
		if (param.isSetAnnotation()) {
			CoefficientAnnotation annot = new CoefficientAnnotation(param.getAnnotation());
			P = annot.getP();
			error = annot.getError();
			t = annot.getT();
			correlations = annot.getCorrelations();
			desc = annot.getDescription();
		}
		this.param = param;
	}

	/**
	 * Builds a Coefficient from a Pmm Lab ParamXml.
	 * 
	 * @param paramXml
	 *            Pmm Lab ParamXml.
	 */
	public Coefficient(ParamXml paramXml) {
		// Creates SBML parameter with paramXml's name as its id
		param = new Parameter(paramXml.getName());
		param.setConstant(true);

		/*
		 * If paramXml has a value then assign it to the SBML parameter (most
		 * instances of ParamXml have a value)
		 */
		// TODO: An exception or KNIME warning could be thrown here to warn of
		// uninitialized parameters.
		if (paramXml.getValue() != null) {
			param.setValue(paramXml.getValue());
		}

		// If paramXml has not unit then assigns "dimensionless" to SBML
		// parameter
		if (paramXml.getUnit() == null) {
			param.setUnits(Unit.Kind.DIMENSIONLESS);
		} else {
			param.setUnits(Util.createId(paramXml.getUnit()));
		}

		// Saves P, error, t, and correlations
		P = paramXml.getP();
		error = paramXml.getError();
		t = paramXml.getT();
		correlations = paramXml.getAllCorrelations();
		desc = paramXml.getDescription();

		// Builds and sets non RDF annotation
		if (P != null || error != null || t != null || !correlations.isEmpty() || desc != null) {
			param.setAnnotation(new CoefficientAnnotation(P, error, t, correlations, desc).getAnnotation());
		}
	}

	/**
	 * Creates a Pmm Lab ParamXml.
	 */
	public ParamXml toParamXml(ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {
		// Creates ParamXml and adds description
		ParamXml paramXml = new ParamXml(param.getId(), param.getValue(), error, null, null, P, t);
		paramXml.setDescription(desc);

		// Adds correlations
		if (correlations != null) {
			for (Entry<String, Double> entry : correlations.entrySet()) {
				paramXml.addCorrelation(entry.getKey(), entry.getValue());
			}
		}

		// Assigns unit and category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			paramXml.setUnit(unitName);
			paramXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
		}

		// Adds limits
		if (limits.containsKey(param.getId())) {
			Limits constLimits = limits.get(param.getId());
			paramXml.setMax(constLimits.getMax());
			paramXml.setMin(constLimits.getMin());
		}

		return paramXml;
	}

	// Getters
	public Double getP() {
		return P;
	}

	public Double getError() {
		return error;
	}

	public Double getT() {
		return t;
	}

	public Map<String, Double> getCorrelations() {
		return correlations;
	}

	public String getDescription() {
		return desc;
	}

	public Parameter getParameter() {
		return param;
	}
}