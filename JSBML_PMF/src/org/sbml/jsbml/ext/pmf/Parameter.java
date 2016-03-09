/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Locale;
import java.util.Map;

import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * Extends JSBML {@link org.sbml.jsbml.Parameter} with:
 * <ul>
 * <li>P
 * <li>error
 * <li>t
 * <li>description
 * <li>min
 * <li>max
 * </ul>
 * 
 * @author Miguel Alba
 */
public class Parameter extends org.sbml.jsbml.Parameter {

  private static final long serialVersionUID = 2064209737258956780L;
  private Double            p;
  private Double            error;
  private Double            t;
  private String            description;
  private Double            min;
  private Double            max;


  public Parameter() {
    super();
    init();
  }


  public Parameter(int level, int version) {
    super(level, version);
    init();
  }


  public Parameter(LocalParameter localParameter) {
    super(localParameter);
    init();
  }


  public Parameter(Parameter p) {
    super(p);
    if (isSetP()) {
      setP(p.getP());
      setError(p.getError());
      setT(p.getT());
      setDescription(p.getDescription());
      setMin(p.getMin());
      setMax(p.getMax());
    }
    init();
  }


  public Parameter(String id) {
    super(id);
    init();
  }


  public Parameter(String id, int level, int version) {
    super(id, level, version);
    init();
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.Parameter#clone()
   */
  @Override
  public Parameter clone() {
    return new Parameter(this);
  }


  private void init() {
    setPackageVersion(-1);
    this.packageName = PMFConstants.shortLabel;
  }


  // *** p methods ***
  public double getP() {
    if (isSetP()) {
      return this.p.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("p", this);
  }


  public boolean isSetP() {
    return this.p != null;
  }


  public void setP(double p) {
    Double oldP = this.p;
    this.p = Double.valueOf(p);
    firePropertyChange("p", oldP, this.p);
  }


  public boolean unsetP() {
    if (isSetP()) {
      Double oldP = this.p;
      this.p = null;
      firePropertyChange("p", oldP, this.p);
      return true;
    }
    return false;
  }


  // *** error methods ***
  public double getError() {
    if (isSetError()) {
      return this.error.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("error", this);
  }


  public boolean isSetError() {
    return this.error != null;
  }


  public void setError(double error) {
    Double oldError = this.error;
    this.error = Double.valueOf(error);
    firePropertyChange("error", oldError, this.error);
  }


  public boolean unsetError() {
    if (isSetError()) {
      Double oldError = this.error;
      this.error = null;
      firePropertyChange("error", oldError, this.error);
      return true;
    }
    return false;
  }


  // *** t methods ***
  public double getT() {
    if (isSetT()) {
      return this.t.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("t", this);
  }


  public boolean isSetT() {
    return this.t != null;
  }


  public void setT(double t) {
    Double oldT = this.t;
    this.t = Double.valueOf(t);
    firePropertyChange("t", oldT, this.t);
  }


  public boolean unsetT() {
    if (isSetT()) {
      Double oldT = this.t;
      this.t = null;
      firePropertyChange("t", oldT, this.t);
      return true;
    }
    return false;
  }


  // *** description methods ***
  public String getDescription() {
    return this.description;
  }


  public boolean isSetDescription() {
    return this.description != null && !this.description.isEmpty();
  }


  public void setDescription(String description) {
    String oldDescription = this.description;
    this.description = description;
    firePropertyChange("description", oldDescription, this.description);
  }


  public void unsetDescription() {
    if (isSetDescription()) {
      String oldDescription = this.description;
      this.description = null;
      firePropertyChange("description", oldDescription, this.description);
    }
  }


  // *** min methods ***
  public double getMin() {
    if (isSetMin()) {
      return this.min.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("min", this);
  }


  public boolean isSetMin() {
    return this.min != null;
  }


  public void setMin(double min) {
    Double oldMin = this.min;
    this.min = Double.valueOf(min);
    firePropertyChange("min", oldMin, this.min);
  }


  public boolean unsetMin() {
    if (isSetMin()) {
      Double oldMin = this.min;
      this.min = null;
      firePropertyChange("min", oldMin, this.min);
      return true;
    }
    return false;
  }


  // *** max methods ***
  public double getMax() {
    if (isSetMax()) {
      return this.max.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("max", this);
  }


  public boolean isSetMax() {
    return this.max != null;
  }


  public void setMax(double max) {
    Double oldMax = this.max;
    this.max = Double.valueOf(max);
    firePropertyChange("max", oldMax, this.max);
  }


  public boolean unsetMax() {
    if (isSetMax()) {
      Double oldMax = this.max;
      this.max = null;
      firePropertyChange("min", oldMax, this.max);
      return true;
    }
    return false;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.Parameter#readAttribute()
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    // Try to read attribute with base class (Parameter)
    boolean isAttributeRead = super.readAttribute(attributeName, prefix, value);
    if (isAttributeRead)
      return true;
    switch (attributeName) {
    case "p":
      setP(StringTools.parseSBMLDouble(value));
      break;
    case "error":
      setError(StringTools.parseSBMLDouble(value));
      break;
    case "t":
      setT(StringTools.parseSBMLDouble(value));
      break;
    case "description":
      setDescription(value);
      break;
    case "min":
      setMin(StringTools.parseSBMLDouble(value));
      break;
    case "max":
      setMax(StringTools.parseSBMLDouble(value));
      break;
    default:
      return false;  // fails to read the attribute
    }
    return true;
  }


  /**
   * (non-Javadoc)
   * 
   * @see org.sbml.jsbml.Parameter#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    // Writes attributes from base class (Parameter): id, constant, value and
    // units
    Map<String, String> attributes = super.writeXMLAttributes();
    // Writes attributes from PMFParameter: P, error, t and description
    if (isSetP()) {
      attributes.put("p", StringTools.toString(Locale.ENGLISH, getP()));
    }
    if (isSetError()) {
      attributes.put("error", StringTools.toString(Locale.ENGLISH, getError()));
    }
    if (isSetT()) {
      attributes.put("t", StringTools.toString(Locale.ENGLISH, getT()));
    }
    if (isSetDescription()) {
      attributes.put("description", getDescription());
    }
    if (isSetMin()) {
      attributes.put("min", StringTools.toString(Locale.ENGLISH, getMin()));
    }
    if (isSetMax()) {
      attributes.put("max", StringTools.toString(Locale.ENGLISH, getMax()));
    }
    return attributes;
  }
}
