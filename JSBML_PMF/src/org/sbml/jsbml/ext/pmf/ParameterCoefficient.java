/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.util.StringTools;

/**
 * @author Miguel Alba
 *
 */
public abstract class ParameterCoefficient extends AbstractSBase {
  
  private static final long serialVersionUID = -1243512328535315089L;
  private Double value;
  
  public ParameterCoefficient() {
    super();
    this.packageName = PMFConstants.shortLabel;
  }
  
  public ParameterCoefficient(double value) {
    super();
    this.value = Double.valueOf(value);
    this.packageName = PMFConstants.shortLabel;
  }
  
  public ParameterCoefficient(double value, int level, int version) {
    super(level, version);
    this.value = Double.valueOf(value);
    this.packageName = PMFConstants.shortLabel;
  }
  
  public ParameterCoefficient(ParameterCoefficient parameterCoefficient) {
    super(parameterCoefficient);
  }
  
  // *** value methods ***

  /**
   * Returns value.
   * 
   * @return value.
   */
  public double getValue() {
    return this.value.doubleValue();
  }


  /**
   * Returns whether value is set.
   * 
   * @return whether value is set.
   */
  public boolean isSetValue() {
    return this.value != null;
  }


  /**
   * Sets value
   * 
   * @param value
   */
  public void setValue(double value) {
    Double oldValue = this.value;
    this.value = Double.valueOf(value);
    firePropertyChange("value", oldValue, this.value);
  }


  /**
   * Unsets the variable value.
   * 
   * @return {@code true}, if value was set before, otherwise {@code false}.
   */
  public boolean unsetValue() {
    if (isSetValue()) {
      Double oldValue = this.value;
      this.value = null;
      firePropertyChange("value", oldValue, this.value);
      return true;
    }
    return false;
  }
  
  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#readAttribute(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix, String value) {
    if (attributeName.equals("value")) {
      setValue(StringTools.parseSBMLDouble(value));
      return true;
    }
    return false;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBase#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
    if (isSetValue()) {
      attributes.put("value", StringTools.toString(Locale.ENGLISH, getValue()));
    }
    return attributes;
  }
}
