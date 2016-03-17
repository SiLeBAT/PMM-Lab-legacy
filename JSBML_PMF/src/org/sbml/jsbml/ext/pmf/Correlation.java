/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Locale;
import java.util.Map;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * @author Miguel Alba
 */
public class Correlation extends AbstractSBase {

  private static final long serialVersionUID = -4995244041059209206L;
  private String            name;
  private Double            value;


  /** Creates a {@link Correlation} instance. */
  public Correlation() {
    super();
    this.packageName = PMFConstants.shortLabel;
  }


  /** Creates a {@link Correlation} instance from a name and value */
  public Correlation(String name, double value) {
    super();
    this.name = name;
    this.value = Double.valueOf(value);
    this.packageName = PMFConstants.shortLabel;
  }


  /**
   * Creates a {@link Correlation} instance from a name, value, level and
   * version.
   */
  public Correlation(String name, double value, int level, int version) {
    super(level, version);
    this.name = name;
    this.value = Double.valueOf(value);
    this.packageName = PMFConstants.shortLabel;
  }


  /** Clone constructor. */
  public Correlation(Correlation correlation) {
    super(correlation);
    if (correlation.isSetName()) {
      setName(correlation.getName());
    }
    if (correlation.isSetValue()) {
      setValue(correlation.getValue());
    }
  }


  /** Clones this class. */
  @Override
  public Correlation clone() {
    return new Correlation(this);
  }


  // *** name methods ***
  public String getName() {
    return this.name;
  }


  public boolean isSetName() {
    return this.name != null && !this.name.isEmpty();
  }


  public void setName(String name) {
    String oldName = this.name;
    this.name = name;
    firePropertyChange("name", oldName, this.name);
  }


  public boolean unsetName() {
    if (isSetName()) {
      String oldName = this.name;
      this.name = null;
      firePropertyChange("name", oldName, this.name);
      return true;
    }
    return false;
  }


  // *** value methods ***
  public double getValue() {
    if (isSetValue()) {
      return this.value.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("name", this);
  }


  public boolean isSetValue() {
    return this.value != null;
  }


  public void setValue(double value) {
    Double oldValue = this.value;
    this.value = Double.valueOf(value);
    firePropertyChange("value", oldValue, this.value);
  }


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
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    if (attributeName.equals("name")) {
      setName(value);
      return true;
    }
    if (attributeName.equals("value")) {
      setValue(StringTools.parseSBMLDouble(value));
      return true;
    }
    return false;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractNamedSBase#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = super.writeXMLAttributes();
    if (isSetName()) {
      attributes.put("name", getName());
    }
    if (isSetValue()) {
      attributes.put("value", StringTools.toString(Locale.ENGLISH, getValue()));
    }
    return attributes;
  }


  @Override
  public String toString() {
    return "Correlation [name=\"" + this.name + "\", value=\"" + this.value
      + "\"]";
  }
}
