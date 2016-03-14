/**
 *
 */
package org.sbml.jsbml.ext.pmf;

/**
 * P maximum value.
 *
 * @author Miguel Alba
 */
public class ParamMax extends ParameterCoefficient {

  private static final long serialVersionUID = 6518948806346825454L;

  /** Creates a {@link ParamMax} instance. */
	public ParamMax() {
		super();
	}

	/** Creates a {@link ParamMax} instance from a value. */
	public ParamMax(double value) {
		super(value);
	}

	/** Creates a {@link ParamMax} instance from a value, level and version. */
	public ParamMax(double value, int level, int version) {
		super(value, level, version);
	}

	/** Clone constructor. */
	public ParamMax(ParamMax min) {
		super(min);
		if (min.isSetValue()) {
			setValue(min.getValue());
		}
	}

	/** Clones this class. */
	@Override
	public ParamMax clone() {
		return new ParamMax(this);
	}

	@Override
	public String toString() {
		return "ParamMax [value=\"" + getValue() + "\"]";
	}
}