/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

/**
 * T-statistic.
 * 
 * @see https://en.wikipedia.org/wiki/T-statistic
 * @author Miguel Alba
 */
public class ParameterT extends ParameterCoefficient {
  
  private static final long serialVersionUID = -5158119436630545067L;
  
  /** Creates a {@link ParameterT} instance. */
  public ParameterT() {
    super();
  }
  
  /** Creates a {@link ParameterT} instance from a value. */
  public ParameterT(double value) {
    super(value);
  }
  
  /** Creates a {@link ParameterT} instance from a value, level and version. */
  public ParameterT(double value, int level, int version) {
    super(value, level, version);
  }
  
  /** Clone constructor. */
  public ParameterT(ParameterT t) {
    super(t);
    if (t.isSetValue()) {
      setValue(t.getValue());
    }
  }
  
  /** Clones this class. */
  @Override
  public ParameterT clone() {
    return new ParameterT(this);
  }
  
  @Override
  public String toString() {
    return "T [value=\"" + getValue() + "\"]";
  }
}
