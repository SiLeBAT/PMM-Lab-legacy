/**
 *
 */
package org.sbml.jsbml.ext.pmf;

/**
 * P minimum value.
 *
 * @author Miguel Alba
 */
public class ParamMin extends ParameterCoefficient {

  private static final long serialVersionUID = -191686061265802767L;

  /** Creates a {@link ParamMin} instance. */
	public ParamMin() {
		super();
	}

	/** Creates a {@link ParamMin} instance from a value. */
	public ParamMin(double value) {
		super(value);
	}

	/** Creates a {@link ParamMin} instance from a value, level and version. */
	public ParamMin(double value, int level, int version) {
		super(value, level, version);
	}

	/** Clone constructor. */
	public ParamMin(ParamMin min) {
		super(min);
		if (min.isSetValue()) {
			setValue(min.getValue());
		}
	}

	/** Clones this class. */
	@Override
	public ParamMin clone() {
		return new ParamMin(this);
	}

	@Override
	public String toString() {
		return "ParamMin [value=\"" + getValue() + "\"]";
	}
}