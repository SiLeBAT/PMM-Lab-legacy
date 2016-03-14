/**
 *
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;

/**
 * Parameter description
 *
 * @author Miguel Alba
 */
public class ParameterDescription extends AbstractSBase {

  private static final long serialVersionUID = -5807475697965108495L;
  private String value;


  /** Creates a {@link ParameterDescription} instance. */
  public ParameterDescription() {
    super();
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Creates a {@link ParameterDescription} instance from a value. */
  public ParameterDescription(String value) {
    super();
    this.value = value;
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Creates a {@link ParameterDescription} instance from a value, level and version. */
  public ParameterDescription(String value, int level, int version) {
    super(level, version);
    this.value = value;
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Clone constructor. */
  public ParameterDescription(ParameterDescription description) {
    super(description);
    if (description.isSetValue()) {
      setValue(description.getValue());
    }
  }


  /** Clones this class. */
  @Override
  public ParameterDescription clone() {
    return new ParameterDescription(this);
  }


  // *** value methods ***
  public String getValue() {
    return this.value;
  }


  public boolean isSetValue() {
    return this.value != null && !this.value.isEmpty();
  }


  public void setValue(String value) {
    String oldValue = this.value;
    this.value = value;
    firePropertyChange("value", oldValue, this.value);
  }


  public boolean unsetValue() {
    if (isSetValue()) {
      String oldValue = this.value;
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
    Map<String, String> attributes = new TreeMap<>();
    if (isSetValue()) {
      attributes.put("value", this.value);
    }
    return attributes;
  }


  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    if (attributeName.equals("value")) {
      setValue(value);
      return true;
    }
    return false;
  }


  @Override
  public String toString() {
    return "Description [value=\"" + getValue() + "\"]";
  }
}
