/**
 *
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * @author Miguel Alba
 */
public class PmmLabId extends AbstractSBase {

  private static final long serialVersionUID = 632352255720929855L;
  private Integer           id;


  /** Creates a {@link PmmLabId} instance. */
  public PmmLabId() {
    super();
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Creates a {@link PmmLabId} instance from an id. */
  public PmmLabId(int id) {
    super();
    this.id = Integer.valueOf(id);
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Creates a {@link PmmLabId} instance from an id, level and version. */
  public PmmLabId(int id, int level, int version) {
    super(level, version);
    this.id = Integer.valueOf(id);
    this.packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Clone constructor. */
  public PmmLabId(PmmLabId pli) {
    super(pli);
    if (pli.isSetId()) {
      setId(pli.getId());
    }
  }


  /** Clones this class. */
  @Override
  public PmmLabId clone() {
    return new PmmLabId(this);
  }


  // *** id methods ***
  /**
   * Returns id.
   * 
   * @return id.
   */
  public int getId() {
    if (isSetId()) {
      return this.id.intValue();
    }
    // This is necessary because we cannot return null here
    throw new PropertyUndefinedError("id", this);
  }


  /**
   * Return whether id is set.
   * 
   * @return whether id is set.
   */
  public boolean isSetId() {
    return this.id != null;
  }


  /**
   * Sets id.
   * 
   * @param id
   */
  public void setId(int id) {
    Integer oldId = this.id;
    this.id = Integer.valueOf(id);
    firePropertyChange("id", oldId, this.id);
  }


  /**
   * Unsets id.
   * 
   * @return {@code true}, if id was set before, otherwise {@code false}.
   */
  public boolean unsetId() {
    if (isSetId()) {
      Integer oldId = this.id;
      this.id = null;
      firePropertyChange("id", oldId, this.id);
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
    if (attributeName.equals("id")) {
      setId(StringTools.parseSBMLInt(value));
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
    if (isSetId()) {
      attributes.put("id", this.id.toString());
    }
    return attributes;
  }


  @Override
  public String toString() {
    return "PmmLabId [id=\"" + this.id + "\"]";
  }
}
