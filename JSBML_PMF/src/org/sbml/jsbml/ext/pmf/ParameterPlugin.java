/**
 *
 */
package org.sbml.jsbml.ext.pmf;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.ext.AbstractSBasePlugin;
import org.sbml.jsbml.util.StringTools;

/**
 * Extends {@link org.sbml.jsbml.Parameter} with:
 * <ul>
 * <li>p
 * <li>error
 * <li>t
 * <li>description
 * <li>list of {@link Correlation}
 * </ul>
 */
public class ParameterPlugin extends AbstractSBasePlugin {

  private static final long   serialVersionUID = 1945330721734938898L;
  private Double              p;
  private Double              error;
  private Double              t;
  private String              description;
  private Double              min;
  private Double              max;
  private ListOf<Correlation> listOfCorrelations;


  /**
   * Creates a new {@link ParameterPlugin} instance cloned from 'plugin'.
   */
  public ParameterPlugin(ParameterPlugin plugin) {
    super(plugin);
    // We do not clone the pointer to the containing model
    if (plugin.isSetP()) {
      setP(plugin.getP());
    }
    if (plugin.isSetError()) {
      setError(plugin.getError());
    }
    if (plugin.isSetT()) {
      setT(plugin.getT());
    }
    if (plugin.isSetDescription()) {
      setDescription(plugin.getDescription());
    }
    if (plugin.isSetMin()) {
      setMin(plugin.getMin());
    }
    if (plugin.isSetMax()) {
      setMax(plugin.getMax());
    }
    if (plugin.isSetListOfCorrelations()) {
      setListOfCorrelations(plugin.listOfCorrelations.clone());
    }
  }


  /**
   * Creates a new {@link ParameterPlugin} instance from a {@link Parameter}.
   */
  public ParameterPlugin(Parameter parameter) {
    super(parameter);
    setPackageVersion(-1);
  }


  /*
   * (non-Javadoc)
   * @see AbstractSBasePlugin#clone()
   */
  @Override
  public ParameterPlugin clone() {
    return new ParameterPlugin(this);
  }


  // *** plugin methods ***
  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#getPackageName()
   */
  @Override
  public String getPackageName() {
    return PMFConstants.shortLabel;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbm.AbstractTreeNode#getParent()
   */
  @Override
  public Parameter getParent() {
    if (isSetExtendedSBase()) {
      return (Parameter) getExtendedSBase().getParent();
    }
    return null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#getParentSBMLObject()
   */
  @Override
  public Parameter getParentSBMLObject() {
    return getParent();
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#getPrefix()
   */
  @Override
  public String getPrefix() {
    return PMFConstants.shortLabel;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#getURI()
   */
  @Override
  public String getURI() {
    return getElementNamespace();
  }


  @Override
  public boolean getAllowsChildren() {
    return true;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#getChildCount()
   */
  @Override
  public int getChildCount() {
    if (isSetListOfCorrelations()) {
      return 1;
    }
    return 0;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#getChildAt(int)
   */
  @Override
  public SBase getChildAt(int childIndex) {
    if (childIndex < 0) {
      throw new IndexOutOfBoundsException(MessageFormat.format(
        resourceBundle.getString("IntegerSurpassesBoundsException"),
        Integer.valueOf(childIndex), Integer.valueOf(0)));
    }
    int pos = 0;
    if (isSetListOfCorrelations()) {
      if (pos == childIndex) {
        return getListOfCorrelations();
      }
      pos++;
    }
    throw new IndexOutOfBoundsException(
      MessageFormat.format("Index {0, number, integer} >= {1, number, integer}",
        Integer.valueOf(childIndex), Integer.valueOf(Math.min(pos, 0))));
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#readAttribute(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
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
    default: // fails to read the attribute
      return false;
    }
    return true;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.Parameter#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
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


  // *** Correlation ***
  /**
   * Adds a new element to the listOfCorrelations. listOfCorrelations is
   * initialized if necessary.
   * 
   * @return {code True} (as specified by {@link Collection#add)
   */
  public boolean addCorrelation(Correlation correlation) {
    return getListOfCorrelations().add(correlation);
  }


  /**
   * Creates a new instance of {@link Correlation} and add it to this
   * {@link Correlation}.
   * 
   * @param name
   *        the name to be set to the new {@link Correlation}.
   * @param value
   *        the value to be set to the new {@link Correlation}.
   * @return the new {@link Correlation} instance.
   */
  public Correlation createCorrelation(String name, double value) {
    Correlation correlation = new Correlation(name, value);
    addCorrelation(correlation);
    return correlation;
  }


  /**
   * Returns the number of {@link Correlation} of this {@link ParameterPlugin}.
   * 
   * @return the number of {@link Correlation} of this {@link ParameterPlugin}.
   */
  public int getCorrelationCount() {
    return isSetListOfCorrelations() ? this.listOfCorrelations.size() : 0;
  }


  /**
   * Returns the number of {@link Correlation} of this {@link ParameterPlugin}.
   * 
   * @return the number of {@link Correlation} of this {@link ParameterPlugin}.
   * @libsbml.deprecated same as {@link #getCorrelationCount()}
   */
  public int getNumCorrelations() {
    return getCorrelationCount();
  }


  // *** p methods ***
  /**
   * Returns the value of p.
   * 
   * @return the value of p.
   */
  public double getP() {
    if (isSetP()) {
      return this.p.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("p", this);
  }


  /**
   * Returns whether p is set.
   * 
   * @return whether p is set.
   */
  public boolean isSetP() {
    return this.p != null;
  }


  /**
   * Sets the value of p.
   * 
   * @param p
   */
  public void setP(double p) {
    Double oldP = this.p;
    this.p = Double.valueOf(p);
    firePropertyChange("p", oldP, this.p);
  }


  /**
   * Unsets the variable p.
   * 
   * @return {@code true}, if p was set before, otherwise {@code false}.
   */
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
  /**
   * Returns the value of error.
   * 
   * @return the value of error.
   */
  public double getError() {
    if (isSetError()) {
      return this.error.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("error", this);
  }


  /**
   * Returns whether error is set.
   * 
   * @return whether error is set.
   */
  public boolean isSetError() {
    return this.error != null;
  }


  /**
   * Sets the value of error.
   * 
   * @param error
   */
  public void setError(double error) {
    Double oldError = this.error;
    this.error = Double.valueOf(error);
    firePropertyChange("error", oldError, this.error);
  }


  /**
   * Unsets the variable error.
   * 
   * @return {@code true}, if error was set before, otherwise {@code false}
   */
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
  /**
   * Returns the value of t.
   * 
   * @return the value of t.
   */
  public double getT() {
    if (isSetT()) {
      return this.t.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("t", this);
  }


  /**
   * Returns whether t is set.
   * 
   * @return whether t is set.
   */
  public boolean isSetT() {
    return this.t != null;
  }


  /**
   * Sets the value of t.
   * 
   * @param t
   */
  public void setT(double t) {
    Double oldT = this.t;
    this.t = Double.valueOf(t);
    firePropertyChange("t", oldT, this.t);
  }


  /**
   * Unsets the variable t.
   * 
   * @return {@code true}, if t was set before, otherwise {@code false}.
   */
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
  /**
   * Returns the value of description.
   * 
   * @return the value of description.
   */
  public String getDescription() {
    return this.description;
  }


  /**
   * Returns whether description is set.
   * 
   * @return whether description is set.
   */
  public boolean isSetDescription() {
    return this.description != null && !this.description.isEmpty();
  }


  /**
   * Sets the value of description.
   * 
   * @param description
   */
  public void setDescription(String description) {
    String oldDescription = this.description;
    this.description = description;
    firePropertyChange("description", oldDescription, this.description);
  }


  /**
   * Unsets the variable description.
   * 
   * @return {@code true}, if description was set before, otherwise
   *         {@code false}.
   */
  public void unsetDescription() {
    if (isSetDescription()) {
      String oldDescription = this.description;
      this.description = null;
      firePropertyChange("description", oldDescription, this.description);
    }
  }


  // *** min methods ***
  /**
   * Returns the value of min.
   * 
   * @return the value of min.
   */
  public double getMin() {
    if (isSetMin()) {
      return this.min.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("min", this);
  }


  /**
   * Returns whether min is set.
   * 
   * @return whether min is set.
   */
  public boolean isSetMin() {
    return this.min != null;
  }


  /**
   * Sets the value of min.
   * 
   * @param min
   */
  public void setMin(double min) {
    Double oldMin = this.min;
    this.min = Double.valueOf(min);
    firePropertyChange("min", oldMin, this.min);
  }


  /**
   * Unsets the variable min.
   * 
   * @return {@code true}, if min was set before, otherwise {@code false}.
   */
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
  /**
   * Returns the value of max.
   * 
   * @return the value of max.
   */
  public double getMax() {
    if (isSetMax()) {
      return this.max.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("max", this);
  }


  /**
   * Returns whether max is set.
   * 
   * @return whether max is set.
   */
  public boolean isSetMax() {
    return this.max != null;
  }


  /**
   * Sets the value of max.
   * 
   * @param max
   */
  public void setMax(double max) {
    Double oldMax = this.max;
    this.max = Double.valueOf(max);
    firePropertyChange("max", oldMax, this.max);
  }


  /**
   * Unsets the variable max.
   * 
   * @return {@code true}, if max was set before, otherwise {@code false}.
   */
  public boolean unsetMax() {
    if (isSetMax()) {
      Double oldMax = this.max;
      this.max = null;
      firePropertyChange("min", oldMax, this.max);
      return true;
    }
    return false;
  }


  // *** listOfCorrelation methods ***
  /**
   * Returns the listOfCorrelations. Creates it if it is not already existing.
   * 
   * @return the listOfCorrelations.
   */
  public ListOf<Correlation> getListOfCorrelations() {
    if (!isSetListOfCorrelations()) {
      this.listOfCorrelations = new ListOf<>();
      this.listOfCorrelations.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      this.listOfCorrelations.setPackageName(null);
      this.listOfCorrelations.setPackageName(PMFConstants.shortLabel);
      this.listOfCorrelations.setSBaseListType(ListOf.Type.other);
      if (this.extendedSBase != null) {
        this.extendedSBase.registerChild(this.listOfCorrelations);
      }
    }
    return this.listOfCorrelations;
  }


  /**
   * Returns {@code true}, if listOfCorrelations contains at least one element.
   * 
   * @return {@code true}, if listOfCorrelations contains at least one element,
   *         otherwise {@code false}
   */
  public boolean isSetListOfCorrelations() {
    return this.listOfCorrelations != null;
  }


  /**
   * Sets the optional {@code ListOf<Correlation>}. If listOfCorrelations was
   * defined before and contains some elements, they are all unset.
   * 
   * @param listOfCorrelations
   */
  public void setListOfCorrelations(ListOf<Correlation> listOfCorrelations) {
    unsetListOfCorrelations();
    this.listOfCorrelations = listOfCorrelations;
    if (listOfCorrelations != null) {
      listOfCorrelations.setPackageVersion(-1);
      // changing the ListOf package name from 'core' to 'pmf'
      listOfCorrelations.setPackageName(null);
      listOfCorrelations.setPackageName(PMFConstants.shortLabel);
      if (isSetExtendedSBase()) {
        this.extendedSBase.registerChild(listOfCorrelations);
      }
    }
  }


  /**
   * Returns {@code true}, if listOfCorrelations contain at least one element,
   * otherwise {@code false}
   * 
   * @return {code true), if listOfCorrelations contain at least one element,
   *         otherwise {@code false}
   */
  public boolean unsetListOfCorrelations() {
    if (this.listOfCorrelations != null) {
      ListOf<Correlation> oldListOfCorrelations = this.listOfCorrelations;
      this.listOfCorrelations = null;
      oldListOfCorrelations.fireNodeAddedEvent();
      return true;
    }
    return false;
  }
}
