package org.sbml.jsbml.ext.pmf;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * Created by de on 16.03.2016.
 */
public class ParameterMetadata extends AbstractSBase {

  private static final long serialVersionUID = -592329892272706369L;
  
  private Double p;
  private Double t;
  private Double error;
  private String description;
  private Double min;
  private Double max;


  /**
   * Creates an ParameterMetadata instance
   */
  public ParameterMetadata() {
    super();
    initDefaults();
  }


  /**
   * Clone constructor
   */
  public ParameterMetadata(ParameterMetadata obj) {
    super(obj);
    
    if (isSetP()) {
      setP(obj.p);
    }
    if (isSetT()) {
      setT(obj.t);
    }
    if (isSetError()) {
      setError(obj.error);
    }
    if (isSetDescription()) {
      setDescription(obj.description);   
    }
    if (isSetMin()) {
      setMin(obj.getMin());
    }
    if (isSetMax()) {
      setMax(obj.getMax());
    }
  }


  /**
   * clones this class
   */
  @Override
  public ParameterMetadata clone() {
    return new ParameterMetadata(this);
  }


  /**
   * Initializes the default values using the namespace.
   */
  public void initDefaults() {
    setPackageVersion(-1);
    this.packageName = PMFConstants.shortLabel;
  }


  // *** p methods ***
  /**
   * Returns the value of {@link #p}.
   *
   * @return the value of {@link #p}.
   */
  public double getP() {
    if (isSetP()) {
      return p;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("p", this);
  }


  /**
   * Returns whether {@link #p} is set.
   *
   * @return whether {@link #p} is set.
   */
  public boolean isSetP() {
    return this.p != null;
  }


  /**
   * Sets the value of p
   *
   * @param p
   *        the value of p to be set.
   */
  public void setP(double p) {
    Double oldP = this.p;
    this.p = Double.valueOf(p);
    firePropertyChange("p", oldP, this.p);
  }


  /**
   * Unsets the variable p.
   *
   * @return {@code true} if p was set before, otherwise {@code false}.
   */
  public boolean unsetP() {
    if (isSetP()) {
      double oldP = this.p;
      this.p = null;
      firePropertyChange("p", oldP, this.p);
      return true;
    }
    return false;
  }


  // *** t methods ***
  /**
   * Returns the value of {@link #t}.
   *
   * @return the value of {@link #t}.
   */
  public double getT() {
    if (isSetT()) {
      return t;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("t", this);
  }


  /**
   * Returns whether {@link #t} is set.
   *
   * @return whether {@link #t} is set.
   */
  public boolean isSetT() {
    return this.t != null;
  }


  /**
   * Sets the value of t
   *
   * @param t
   *        the value of t to be set.
   */
  public void setT(double t) {
    Double oldT = this.t;
    this.t = Double.valueOf(t);
    firePropertyChange("t", oldT, this.t);
  }


  /**
   * Unsets the variable t.
   *
   * @return {@code true} if t was set before, otherwise {@code false}.
   */
  public boolean unsetT() {
    if (isSetT()) {
      double oldT = this.t;
      this.t = null;
      firePropertyChange("t", oldT, this.t);
      return true;
    }
    return false;
  }


  /**
   * Returns the value of {@link #error}.
   *
   * @return the value of {@link #error}.
   */
  public double getError() {
    if (isSetError()) {
      return error;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("error", this);
  }


  /**
   * Returns whether {@link #error} is set.
   *
   * @return whether {@link #error} is set.
   */
  public boolean isSetError() {
    return this.error != null;
  }


  /**
   * Sets the value of error
   *
   * @param error
   *        the value of error to be set.
   */
  public void setError(double error) {
    Double oldError = this.error;
    this.error = Double.valueOf(error);
    firePropertyChange("error", oldError, this.error);
  }


  /**
   * Unsets the variable error.
   *
   * @return {@code true} if error was set before, otherwise {@code false}.
   */
  public boolean unsetError() {
    if (isSetError()) {
      double oldError = this.error;
      this.error = null;
      firePropertyChange("error", oldError, this.error);
      return true;
    }
    return false;
  }


  // *** description ***
  /**
   * Returns the value of {@link #description}.
   *
   * @return the value of {@link #description}.
   */
  public String getDescription() {
    return description;
  }


  /**
   * Returns whether {@link #description} is set.
   *
   * @return whether {@link #description} is set.
   */
  public boolean isSetDescription() {
    return this.description != null;
  }


  /**
   * Sets the value of description
   *
   * @param description
   *        the value of description to be set.
   */
  public void setDescription(String description) {
    String oldDescription = this.description;
    this.description = description;
    firePropertyChange("description", oldDescription,
      this.description);
  }


  /**
   * Unsets the variable description.
   *
   * @return {@code true} if description was set before, otherwise {@code false}
   *         .
   */
  public boolean unsetDescription() {
    if (isSetDescription()) {
      String oldDescription = this.description;
      this.description = null;
      firePropertyChange("description", oldDescription,
        this.description);
      return true;
    }
    return false;
  }


  // *** min methods ***
  /**
   * Returns the value of {@link #min}.
   *
   * @return the value of {@link #min}.
   */
  public double getMin() {
    if (isSetMin()) {
      return min;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("min", this);
  }


  /**
   * Returns whether {@link #min} is set.
   *
   * @return whether {@link #min} is set.
   */
  public boolean isSetMin() {
    return this.min != null;
  }


  /**
   * Sets the value of min
   *
   * @param min
   *        the value of min to be set.
   */
  public void setMin(double min) {
    Double oldMin = this.min;
    this.min = Double.valueOf(min);
    firePropertyChange("min", oldMin, this.min);
  }


  /**
   * Unsets the variable min.
   *
   * @return {@code true} if min was set before, otherwise {@code false}.
   */
  public boolean unsetMin() {
    if (isSetMin()) {
      double oldMin = this.min;
      this.min = null;
      firePropertyChange("max", oldMin, this.min);
      return true;
    }
    return false;
  }


  // *** max methods ***
  /**
   * Returns the value of {@link #max}.
   *
   * @return the value of {@link #max}.
   */
  public double getMax() {
    if (isSetMax()) {
      return max;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("max", this);
  }


  /**
   * Returns whether {@link #max} is set.
   *
   * @return whether {@link #max} is set.
   */
  public boolean isSetMax() {
    return this.max != null;
  }


  /**
   * Sets the value of max
   *
   * @param max
   *        the value of max to be set.
   */
  public void setMax(double max) {
    Double oldMax = this.max;
    this.max = Double.valueOf(max);
    firePropertyChange("max", oldMax, this.max);
  }


  /**
   * Unsets the variable max.
   *
   * @return {@code true} if max was set before, otherwise {@code false}.
   */
  public boolean unsetMax() {
    if (isSetMax()) {
      double oldMax = this.max;
      this.max = null;
      firePropertyChange("max", oldMax, this.max);
      return true;
    }
    return false;
  }


  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
    if (isSetP()) {
      attributes.put("p", StringTools.toString(Locale.ENGLISH, p));
    }
    if (isSetT()) {
      attributes.put("t", StringTools.toString(Locale.ENGLISH, t));
    }
    if (isSetError()) {
      attributes.put("error", StringTools.toString(Locale.ENGLISH, error));
    }
    if (isSetDescription()) {
      attributes.put("description", description);
    }
    if (isSetMin()) {
      attributes.put("min", StringTools.toString(Locale.ENGLISH, min));
    }
    if (isSetMax()) {
      attributes.put("max", StringTools.toString(Locale.ENGLISH, max));
    }
    return attributes;
  }


  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    switch (attributeName) {
    case "p":
      setP(StringTools.parseSBMLDouble(value));
      return true;
    case "t":
      setT(StringTools.parseSBMLDouble(value));
      return true;
    case "error":
      setError(StringTools.parseSBMLDouble(value));
      return true;
    case "description":
      setDescription(value);
      return true;
    case "min":
      setMin(StringTools.parseSBMLDouble(value));
      return true;
    case "max":
      setMax(StringTools.parseSBMLDouble(value));
      return true;
    default:
      return false;
    }
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBase#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(PMFConstants.paramMetadata + "[");
    if (isSetP()) {
      sb.append(String.format("\"%s\"=%.3f ", "p", p));
    }
    if (isSetT()) {
      sb.append(String.format("\"%s\"=%.3f ", "t", t));
    }
    if (isSetError()) {
      sb.append(String.format("\"%s\"=%.3f ", "error", error));
    }
    if (isSetDescription()) {
      sb.append(String.format("\"%s\"=%s ", "description", description));
    }
    if (isSetMin()) {
      sb.append(String.format("\"%s\"=%.3f ", "min", min));
    }
    if (isSetMax()) {
      sb.append(String.format("\"%s\"=%.3f ", "max", max));
    }
    
    return sb.toString();
  }
}
