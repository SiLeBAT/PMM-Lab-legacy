package de.bund.bfr.knime.pmm.sbmlutil;

/**
 * Limit values of a parameter.
 * 
 * @author malba
 */
public class Limits {

	String var;
	Double min;
	Double max;

	/**
	 * Creates new Limits of a variable.
	 * @param var Variable name.
	 * @param min Variable minimum value.
	 * @param max Varaible maximum value.
	 */
	public Limits(String var, Double min, Double max) {
		this.var = var;
		this.min = min;
		this.max = max;
	}

	// Getters
	public String getVar() {
		return var;
	}

	public Double getMin() {
		return min;
	}

	public Double getMax() {
		return max;
	}
}
