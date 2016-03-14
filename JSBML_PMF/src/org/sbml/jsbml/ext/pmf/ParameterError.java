/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

/**
 * Error.
 * 
 * @author Miguel Alba
 */
public class ParameterError extends ParameterCoefficient {
  
  private static final long serialVersionUID = -1196895092174169582L;
  
  /** Creates a {@link ParameterError} instance. */
  public ParameterError() {
    super();
  }
  
  /** Creates a {@link ParameterError} instance from a value. */
  public ParameterError(double value) {
    super(value);
  }
  
  /** Creates a {@link ParameterError} instance from a value, level and version. */
  public ParameterError(double value, int level, int version) {
    super(value, level, version);
  }
  
  /** Clone constructor. */
  public ParameterError(ParameterError error) {
    super(error);
    if (error.isSetValue()) {
      setValue(error.getValue());
    }
  }
  
  /** Clones this class. */
  @Override
  public ParameterError clone() {
    return new ParameterError(this);
  }

  @Override
  public String toString() {
    return "ParameterError [value=\"" + getValue() + "\"]";
  }
}
