package de.bund.bfr.pmf.sbml;

import java.util.Locale;

/**
 * Limit values of a parameter.
 * 
 * @author Miguel Alba
 */
public class Limits {

	private String var;
	private Double min;
	private Double max;

	/**
	 * Creates new Limits of a variable.
	 * @param var Variable name.
	 * @param min Variable minimum value.
	 * @param max Variable maximum value.
	 */
	public Limits(String var, Double min, Double max) {
		this.var = var;
		this.min = min;
		this.max = max;
	}
	
	public String getVar() { return var; }
	public Double getMin() { return min; }
	public Double getMax() { return max; }

	public String toString() {
		return String.format(Locale.ENGLISH, "Limits [var=%s, min=%.6f, max=%.6f]", var, min, max);
	}
}
