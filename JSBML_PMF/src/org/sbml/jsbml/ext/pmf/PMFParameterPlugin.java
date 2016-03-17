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

  private static final long   serialVersionUID = 4478024757743081671L;

  private ParameterMetadata   parameterMetadata;
  private ListOf<Correlation> listOfCorrelations;


  /** Creates a new {@link PMFParameterPlugin} instance cloned from 'plugin'. */
  public PMFParameterPlugin(PMFParameterPlugin plugin) {
    super(plugin);
    if (plugin.isSetMetadata()) {
      setMetadata(plugin.parameterMetadata.clone());
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
    if (isSetMetadata()) {
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
    if (isSetMetadata()) {
      if (pos == childIndex) {
        return getMetadata();
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


  // *** parameterMetadata ***
  /**
   * Returns the value of {@link #parameterMetadata}.
   *
   * @return the value of {@link #parameterMetadata}.
   */
  public ParameterMetadata getMetadata() {
    return parameterMetadata;
  }


  /**
   * Returns whether {@link #parameterMetadata} is set.
   *
   * @return whether {@link #parameterMetadata} is set.
   */
  public boolean isSetMetadata() {
    return this.parameterMetadata != null;
  }


  /**
   * Sets the value of parameterMetadata
   *
   * @param parameterMetadata
   *        the value of parameterMetadata to be set.
   */
  public void setMetadata(ParameterMetadata parameterMetadata) {
    unsetMetadata();
    this.parameterMetadata = parameterMetadata;
    if (this.extendedSBase != null) {
      this.parameterMetadata.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.parameterMetadata);
    }
  }


  /**
   * Unsets the variable parameterMetadata.
   *
   * @return {@code true} if parameterMetadata was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetMetadata() {
    if (isSetMetadata()) {
      ParameterMetadata oldMetadata = this.parameterMetadata;
      this.parameterMetadata = null;
      firePropertyChange(PMFConstants.paramMetadata, oldMetadata,
        this.parameterMetadata);
      return true;
    }
    return false;
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
   * @param name
   *        the name to be set to the new {@link Correlation}.
   * @param value
   *        the value to be set to the new {@link Correlation}.
   * @return the new {@link Correlation} instance.
   */
  public Correlation createCorrelation(String name, double value) {
    Correlation correlation =
      new Correlation(name, value, getLevel(), getVersion());
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
}
