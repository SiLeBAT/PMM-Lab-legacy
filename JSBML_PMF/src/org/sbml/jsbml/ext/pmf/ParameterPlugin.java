/**
 *
 */
package org.sbml.jsbml.ext.pmf;

import java.text.MessageFormat;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.ext.AbstractSBasePlugin;

/**
 * Extends {@link org.sbml.jsbml.Parameter} with:
 * <ul>
 * <li>list of {@link Correlation}
 * </ul>
 */
public class ParameterPlugin extends AbstractSBasePlugin {

  private static final long     serialVersionUID = 1945330721734938898L;
  private ListOf<Correlation> listOfCorrelations;

  /**
   * Creates a new {@link ParameterPlugin} instance cloned from 'plugin'.
   */
  public ParameterPlugin(ParameterPlugin plugin) {
    super(plugin);
    // We do not clone the pointer to the containing model
    if (plugin.isSetListOfCorrelations()) {
      setListOfCorrelations(plugin.listOfCorrelations.clone());
    }
  }


  /**
   * Creates a new {@link ParameterPlugin} instance from a {@link Parameter}.
   */
  public ParameterPlugin(Parameter parameter) {
    super(parameter);
    initDefaults();
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


  private void initDefaults() {
    setPackageVersion(-1);
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
    // No attribute defined on this plugin
    return false;
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


  // *** listOfCorrelation methods ***
  /**
   * Returns the listOfCorrelations. If the {@link ListOf} is not defined,
   * creates an empty one.
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


  public boolean isSetListOfCorrelations() {
    return this.listOfCorrelations != null;
  }


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
   * Removes the {@linkn #listOfCorrelations} from this {@link ParameterPlugin}
   * and notifies all the registered instance of
   * {@link org.sbml.jsbml.util.TreeNodeChangeListener}.
   * 
   * @return {code true) if calling this method lead to a change in this data
   *         structure.
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
