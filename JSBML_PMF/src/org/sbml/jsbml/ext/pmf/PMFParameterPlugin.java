/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.text.MessageFormat;
import java.util.Collection;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBase;

/**
 * @author Miguel Alba
 */
public class PMFParameterPlugin extends PMFSBasePlugin {

  private static final long    serialVersionUID = 4478024757743081671L;
  private ParameterP           p;
  private ParameterT           t;
  private ParameterError       error;
  private ParameterDescription description;
  private ParamMin             min;
  private ParamMax             max;
  private ListOf<Correlation>  listOfCorrelations;


  /** Creates a new {@link PMFParameterPlugin} instance cloned from 'plugin'. */
  public PMFParameterPlugin(PMFParameterPlugin plugin) {
    super(plugin);
    if (plugin.isSetP()) {
      setP(plugin.getP().clone());
    }
    if (plugin.isSetT()) {
      setT(plugin.getT().clone());
    }
    if (plugin.isSetError()) {
      setError(plugin.getError().clone());
    }
    if (plugin.isSetDescription()) {
      setDescription(plugin.getDescription().clone());
    }
    if (plugin.isSetMin()) {
      setMin(plugin.getMin().clone());
    }
    if (plugin.isSetMax()) {
      setMax(plugin.getMax().clone());
    }
    if (plugin.isSetListOfCorrelations()) {
      setListOfCorrelations(plugin.listOfCorrelations.clone());
    }
  }


  /**
   * Creates a new {@link PMFParameterPlugin} instance from a {@link Parameter}.
   */
  public PMFParameterPlugin(Parameter parameter) {
    super(parameter);
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#clone()
   */
  @Override
  public PMFParameterPlugin clone() {
    return new PMFParameterPlugin(this);
  }
  // *** plugin methods ***


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractTreeNode#getParent()
   */
  @Override
  public Parameter getParent() {
    if (isSetExtendedSBase()) {
      return (Parameter) getExtendedSBase();
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
   * @see javax.swing.tree.TreeNode#getAllowsChildren()
   */
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
    int childCount = 0;
    if (isSetP()) {
      childCount++;
    }
    if (isSetT()) {
      childCount++;
    }
    if (isSetError()) {
      childCount++;
    }
    if (isSetDescription()) {
      childCount++;
    }
    if (isSetMin()) {
      childCount++;
    }
    if (isSetMax()) {
      childCount++;
    }
    if (isSetListOfCorrelations()) {
      childCount++;
    }
    return childCount;
  }


  /*
   * (non-Javadoc)
   * @see javax.swing.jsbml.SBasePlugin#getChildAt(int)
   */
  @Override
  public SBase getChildAt(int childIndex) {
    if (childIndex < 0) {
      throw new IndexOutOfBoundsException(MessageFormat.format(
        resourceBundle.getString("IndexSurpassesBoundsException"),
        Integer.valueOf(childIndex), Integer.valueOf(0)));
    }
    int pos = 0;
    if (isSetP()) {
      if (pos == childIndex) {
        return getP();
      }
      pos++;
    }
    if (isSetT()) {
      if (pos == childIndex) {
        return getT();
      }
      pos++;
    }
    if (isSetError()) {
      if (pos == childIndex) {
        return getError();
      }
      pos++;
    }
    if (isSetDescription()) {
      if (pos == childIndex) {
        return getDescription();
      }
      pos++;
    }
    if (isSetMin()) {
      if (pos == childIndex) {
        return getMin();
      }
      pos++;
    }
    if (isSetMax()) {
      if (pos == childIndex) {
        return getMax();
      }
      pos++;
    }
    if (isSetListOfCorrelations()) {
      if (pos == childIndex) {
        return getListOfCorrelations();
      }
      pos++;
    }
    throw new IndexOutOfBoundsException(MessageFormat.format(
      resourceBundle.getString("IndexExceedsBoundsException"),
      Integer.valueOf(childIndex), Integer.valueOf(Math.min(pos, 0))));
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#readAttribute(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    // No attribute defined on this plugin
    return false;
  }


  // *** P ***
  /**
   * Returns the {@link ParameterP} of this {@link PMFParameterPlugin}.
   *
   * @return the {@link ParameterP} of this {@link PMFParameterPlugin}.
   */
  public ParameterP getP() {
    return this.p;
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
   * Sets the {@link ParameterP} of this {@link PMFParameterPlugin}.
   *
   * @param p
   */
  public void setP(ParameterP p) {
    unsetP();
    this.p = p;
    if (this.extendedSBase != null) {
      this.p.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.p);
    }
  }


  /**
   * Unsets the variable p.
   *
   * @return {@code true}, if p was set before, otherwise {@code false}.
   */
  public boolean unsetP() {
    if (this.p != null) {
      ParameterP oldP = this.p;
      this.p = null;
      oldP.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  /**
   * Creates a new {@link ParameterP} inside this {@link PMFParameterPlugin},
   * and
   * returns a pointer to it.
   *
   * @return the new {@link ParameterP} instance.
   */
  public ParameterP createP(double p) {
    setP(new ParameterP(p, getLevel(), getVersion()));
    return getP();
  }


  // *** T ***
  /**
   * Returns the {@link ParameterT} of this {@link PMFParameterPlugin}.
   *
   * @return the {@link ParameterT} of this {@link PMFParameterPlugin}.
   */
  public ParameterT getT() {
    return this.t;
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
   * Sets the {@link ParameterT} of this {@link PMFParameterPlugin}.
   *
   * @param t
   */
  public void setT(ParameterT t) {
    unsetT();
    this.t = t;
    if (this.extendedSBase != null) {
      this.t.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.t);
    }
  }


  /**
   * Unsets the variable t.
   *
   * @return {@code true}, if t was set before, otherwise {@code false}.
   */
  public boolean unsetT() {
    if (this.t != null) {
      ParameterT oldT = this.t;
      this.t = null;
      oldT.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  /**
   * Creates a new {@link ParameterT} inside this {@link PMFParameterPlugin},
   * and
   * returns a pointer to it.
   *
   * @return the new {@link ParameterT} instance.
   */
  public ParameterT createT(double t) {
    setT(new ParameterT(t, getLevel(), getVersion()));
    return getT();
  }


  // *** Error ***
  /**
   * Returns the {@link ParameterError} of this {@link PMFParameterPlugin}.
   *
   * @return the {@link ParameterError} of this {@link PMFParameterPlugin}.
   */
  public ParameterError getError() {
    return this.error;
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
   * Sets the {@link ParameterError} of this {@link PMFParameterPlugin}.
   *
   * @param error
   */
  public void setError(ParameterError error) {
    unsetError();
    this.error = error;
    if (this.extendedSBase != null) {
      this.error.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.error);
    }
  }


  /**
   * Unsets the variable error.
   *
   * @return {@code true}, if error was set before, otherwise {@code false}.
   */
  public boolean unsetError() {
    if (this.error != null) {
      ParameterError oldError = this.error;
      this.error = null;
      oldError.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  /**
   * Creates a new {@link ParameterError} inside this {@link PMFParameterPlugin}
   * , and
   * returns a pointer to it.
   *
   * @return the new {@link ParameterError} instance.
   */
  public ParameterError createError(double error) {
    setError(new ParameterError(error, getLevel(), getVersion()));
    return getError();
  }


  // *** Description ***
  /**
   * Returns the {@link ParameterDescription} of this {@link PMFParameterPlugin}
   * .
   *
   * @return the {@link ParameterDescription} of this {@link PMFParameterPlugin}
   *         .
   */
  public ParameterDescription getDescription() {
    return this.description;
  }


  /**
   * Returns whether description is set.
   *
   * @return whether description is set.
   */
  public boolean isSetDescription() {
    return this.description != null;
  }


  /**
   * Sets the {@link ParameterDescription} of this {@link PMFParameterPlugin}.
   *
   * @param description
   */
  public void setDescription(ParameterDescription description) {
    unsetDescription();
    this.description = description;
    if (this.extendedSBase != null) {
      this.description.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.description);
    }
  }


  /**
   * Unsets the variable description.
   *
   * @return {@code true}, if description was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetDescription() {
    if (this.description != null) {
      ParameterDescription oldDescription = this.description;
      this.description = null;
      oldDescription.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  /**
   * Creates a new {@link ParameterDescription} inside this
   * {@link PMFParameterPlugin}, and
   * returns a pointer to it.
   *
   * @return the new {@link ParameterDescription} instance.
   */
  public ParameterDescription createDescription(String description) {
    setDescription(
      new ParameterDescription(description, getLevel(), getVersion()));
    return getDescription();
  }


  // *** Min ***
  /**
   * Returns the {@link ParamMin} of this {@link PMFParameterPlugin}.
   *
   * @return the {@link ParamMin} of this {@link PMFParameterPlugin}.
   */
  public ParamMin getMin() {
    return this.min;
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
   * Sets the {@link ParamMin} of this {@link PMFParameterPlugin}.
   *
   * @param min
   */
  public void setMin(ParamMin min) {
    unsetMin();
    this.min = min;
    if (this.extendedSBase != null) {
      this.min.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.min);
    }
  }


  /**
   * Unsets the variable min.
   *
   * @return {@code true}, if min was set before, otherwise {@code false}.
   */
  public boolean unsetMin() {
    if (this.min != null) {
      ParamMin oldMin = this.min;
      this.min = null;
      oldMin.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  /**
   * Creates a new {@link ParamMin} inside this {@link PMFParameterPlugin},
   * and
   * returns a pointer to it.
   *
   * @return the new {@link ParamMin} instance.
   */
  public ParamMin createMin(double min) {
    setMin(new ParamMin(min, getLevel(), getVersion()));
    return getMin();
  }


  // *** Max ***
  /**
   * Returns the {@link ParamMax} of this {@link PMFParameterPlugin}.
   *
   * @return the {@link ParamMax} of this {@link PMFParameterPlugin}.
   */
  public ParamMax getMax() {
    return this.max;
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
   * Sets the {@link ParamMax} of this {@link PMFParameterPlugin}.
   *
   * @param max
   */
  public void setMax(ParamMax max) {
    unsetMax();
    this.max = max;
    if (this.extendedSBase != null) {
      this.max.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.max);
    }
  }


  /**
   * Unsets the variable max.
   *
   * @return {@code true}, if max was set before, otherwise {@code false}.
   */
  public boolean unsetMax() {
    if (this.max != null) {
      ParamMax oldMax = this.max;
      this.max = null;
      oldMax.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  /**
   * Creates a new {@link ParamMax} inside this {@link PMFParameterPlugin},
   * and
   * returns a pointer to it.
   *
   * @return the new {@link ParamMax} instance.
   */
  public ParamMax createMax(double max) {
    setMax(new ParamMax(max, getLevel(), getVersion()));
    return getMax();
  }


  // *** listOfCorrelations ***

  /**
   * Adds a new element to the listOfCorrelations. listOfCorrelations is
   * initialized if necessary.
   *
   * @return {@code true} (as specified by {@link Collection#add})
   */
  public boolean addCorrelation(Correlation correlation) {
    return getListOfCorrelations().add(correlation);
  }

  /**
   * Creates a new instance of {@link Correlation} and add it to this
   * {@link Correlation}.
   *
   * @param name the name to be set to the new {@link Correlation}.
   * @param value the value to be set to the new {@link Correlation}.
   * @return the new {@link Correlation} instance.
   */
  public Correlation createCorrelation(String name, double value) {
    Correlation correlation = new Correlation(name, value, getLevel(), getVersion());
    addCorrelation(correlation);
    return correlation;
  }


  /**
   * Returns the listOfCorrelations. If the {@link ListOf} is not defined,
   * creates an empty one.
   *
   * @return the listOfCorrelations
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


  public void setListOfCorrelations(ListOf<Correlation> listOfCorrelations) {
    unsetListOfCorrelations();
    this.listOfCorrelations = listOfCorrelations;
    if (listOfCorrelations != null) {
      listOfCorrelations.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      listOfCorrelations.setPackageName(null);
      listOfCorrelations.setPackageName(PMFConstants.shortLabel);
      listOfCorrelations.setSBaseListType(ListOf.Type.other);
      if (isSetExtendedSBase()) {
        this.extendedSBase.registerChild(listOfCorrelations);
      }
    }
  }


  public boolean unsetListOfCorrelations() {
    if (this.listOfCorrelations != null) {
      ListOf<Correlation> oldListOfCorrelations = this.listOfCorrelations;
      this.listOfCorrelations = null;
      oldListOfCorrelations.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  public boolean isSetListOfCorrelations() {
    return this.listOfCorrelations != null;
  }
}
