/**
 *
 */
package org.sbml.jsbml.ext.pmf;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.ext.AbstractSBasePlugin;
import org.sbml.jsbml.util.StringTools;

/**
 * Extends {@link org.sbml.jsbml.Parameter} with:
 * <ul>
 * <li>p
 * <li>error
 * <li>list of {@link Correlation}
 * </ul>
 */
public class ParameterPlugin extends AbstractSBasePlugin {

  private static final long   serialVersionUID = 1945330721734938898L;
  private ListOf<Correlation> listOfCorrelations;
  private Double              p;
  private Double              error;


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
    if (plugin.isSetListOfCorrelations()) {
      setListOfCorrelations(plugin.listOfCorrelations.clone());
    }
  }


  /**
   * Creates a new {@link ParameterPlugin} instance from a {@link Parameter}.
   * TODO: replace {@link org.sbml.jsbml.ext.pmf.Parameter} with
   * {@link org.sbml.jsbml.Parameter}.
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
    // TODO: so far we only have one attribute. Need to keep updating the size
    // as I keep adding attributes to ParameterPlugin
    Map<String, String> attributes = new HashMap<>(2);
    if (isSetP()) {
      attributes.put("p", StringTools.toString(Locale.ENGLISH, getP()));
    }
    if (isSetError()) {
      attributes.put("error", StringTools.toString(Locale.ENGLISH, getError()));
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
