/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * Model variable. Includes a name and a value.
 * 
 * @author Miguel Alba
 */
public class ModelVariable extends AbstractSBase {

  private static final long serialVersionUID = 5419651814539473485L;
  private String            name;
  private Double            value;


  /** Creates a {@link ModelVariable} instance. */
  public ModelVariable() {
    super();
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Creates a {@link ModelVariable} instance from a name. */
  public ModelVariable(String name) {
    super();
    this.name = name;
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Creates a {@link ModelVariable} instance from a name and value. */
  public ModelVariable(String name, double value) {
    super();
    this.name = name;
    this.value = Double.valueOf(value);
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /**
   * Creates a {@link ModelVariable} instance from a name, value, level and
   * version.
   */
  public ModelVariable(String name, double value, int level, int version) {
    super(level, version);
    this.name = name;
    this.value = Double.valueOf(value);
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Clone constructor */
  public ModelVariable(ModelVariable mv) {
    super(mv);
    if (mv.isSetName()) {
      setName(mv.name);
    }
    if (mv.isSetValue()) {
      setValue(mv.value.doubleValue());
    }
  }


  /** Clones this class */
  @Override
  public ModelVariable clone() {
    return new ModelVariable(this);
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
    throw new PropertyUndefinedError("value", this);
  }


  public boolean isSetValue() {
    return this.value != null;
  }


  public void setValue(double value) {
    Double oldValue = this.value;
    this.value = Double.valueOf(value);
    firePropertyChange("value", oldValue, this.value);
  }


  /**
   * Unsets the variable value.
   * 
   * @return {code true}, if value was set before, otherwise {@code false}.
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
   * @see org.sbml.jsbml.AbstractNamedSBase#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = super.writeXMLAttributes();
    if (isSetName()) {
      attributes.remove("name");
      attributes.put("name", getName());
    }
    if (isSetValue()) {
      attributes.remove("value");
      attributes.put("value", this.value.toString());
    }
    return attributes;
  }


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


  @Override
  public String toString() {
    return "ModelVariable [name=" + getName()
      + (isSetValue() ? ", value=\"" + getValue() + "\"" : "") + "]";
  }
}
