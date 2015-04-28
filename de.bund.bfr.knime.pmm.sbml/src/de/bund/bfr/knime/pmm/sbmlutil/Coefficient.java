/**
 * Pmm Lab secondary model coefficient
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.Parameter;

import de.bund.bfr.knime.pmm.common.ParamXml;

public abstract class Coefficient {
	protected Parameter param;

	protected Double P;
	protected Double error;
	protected Double t;

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

	public Parameter getParameter() {
		return param;
	}

	public abstract ParamXml toParamXml();
}
