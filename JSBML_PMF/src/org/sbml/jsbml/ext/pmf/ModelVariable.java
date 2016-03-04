/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;

import org.sbml.jsbml.AbstractNamedSBase;
import org.sbml.jsbml.LevelVersionError;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * Model variable. Includes a name and a value.
 * 
 * @author Miguel Alba
 */
public class ModelVariable extends AbstractNamedSBase {

  private static final long serialVersionUID = 5419651814539473485L;
  private Double            value;
  
  /** Creates a {@link ModelVariable} instance. */
  public ModelVariable() {
    super();
    setPackageVersion(-1);
    this.packageName = PMFConstants.shortLabel;
  }

  /** Creates a {@link ModelVariable} instance with an id and no value. */
  public ModelVariable(String id) {
    super(id);
    setPackageVersion(-1);
    this.packageName = PMFConstants.shortLabel;
  }

  /** Creates a {@link ModelVariable} instance with an id and value. */
  public ModelVariable(String id, double value) {
    super(id);
    this.value = Double.valueOf(value);
    setPackageVersion(-1);
    this.packageName = PMFConstants.shortLabel;
  }


  /**
   * Creates a {@link ModelVariable} instance with an id, value, level and
   * version.
   */
  public ModelVariable(String id, double value, int level, int version)
    throws LevelVersionError {
    super(id, null, level, version);
    this.value = Double.valueOf(value);
    Integer minSBMLLevel = Integer.valueOf(PMFConstants.MIN_SBML_LEVEL);
    Integer minSBMLVersion = Integer.valueOf(PMFConstants.MIN_SBML_VERSION);
    if (getLevelAndVersion().compareTo(minSBMLLevel, minSBMLVersion) < 0) {
      throw new LevelVersionError(getElementName(), level, version);
    }
    this.packageName = PMFConstants.shortLabel;
  }


  /** Clone constructor */
  public ModelVariable(ModelVariable mv) {
    super(mv);
    if (mv.isSetValue()) {
      setValue(mv.value.doubleValue());
    }
  }


  /** Clones this class */
  @Override
  public ModelVariable clone() {
    return new ModelVariable(this);
  }


  @Override
  public boolean isIdMandatory() {
    return true;
  }


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
    if (isSetId()) {
      attributes.remove("id");
      attributes.put("id", getId());
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
    boolean isAttributeRead = super.readAttribute(attributeName, prefix, value);
    if (!isAttributeRead) {
      isAttributeRead = true;
      if (attributeName.equals("value")) {
        setValue(StringTools.parseSBMLDouble(value));
      } else {
        isAttributeRead = false;
      }
    }
    return isAttributeRead;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(PMFConstants.modelVariable + " [id=\"" + getId() + "\"");
    if (isSetValue()) {
      sb.append(", value=\"" + this.value.toString() + "\"");
    }
    sb.append("]");
    return sb.toString();
  }
}
