/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

/**
 * P-value.
 * 
 * @see https://en.wikipedia.org/wiki/P-value
 * @author Miguel Alba
 */
public class ParameterP extends ParameterCoefficient {
  
  private static final long serialVersionUID = 18076252755791808L;
  
  /** Creates a {@link ParameterP} instance. */
  public ParameterP() {
    super();
  }
  
  /** Creates a {@link ParameterP} instance from a value. */
  public ParameterP(double value) {
    super(value);
  }
  
  /** Creates a {@link ParameterP} instance from a value, level and version. */
  public ParameterP(double value, int level, int version) {
    super(value, level, version);
  }
  
  /** Clone constructor. */
  public ParameterP(ParameterP p) {
    super(p);
    if (p.isSetValue()) {
      setValue(p.getValue());
    }
  }
  
  /** Clones this class. */
  @Override
  public ParameterP clone() {
    return new ParameterP(this);
  }


  @Override
  public String toString() {
    return "P [value=\"" + getValue() + "\"]";
  }
}
