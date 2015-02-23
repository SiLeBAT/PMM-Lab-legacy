package de.bund.bfr.knime.pmm.sbmlutil;

/**
 * Limits values of a parameter.
 * @author malba
 *
 */
public class Limits {
	private String var;
	private Double min;
	private Double max;
	
	public Limits(String var, Double min, Double max) {
		this.var = var;
		this.min = min;
		this.max = max;
	}
	
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
