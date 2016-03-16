/**
 *
 */
package org.sbml.jsbml.ext.pmf;

import java.text.MessageFormat;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;

/**
 * @author Miguel Alba
 */
public class PMFModelPlugin extends PMFSBasePlugin {

  private static final long     serialVersionUID = 5078386942266911188L;
  private ListOf<ModelVariable> listOfModelVariables;
  private ListOf<DataSource>    listOfDataSources;


  /**
   * Creates a new {@link PMFModelPlugin} instance cloned from the given
   * parameter.
   */
  public PMFModelPlugin(PMFModelPlugin plugin) {
    super(plugin);
    // We don't clone the pointer to the containing model
    if (plugin.isSetListOfModelVariables()) {
      setListOfModelVariables(plugin.listOfModelVariables.clone());
    }
    if (plugin.isSetListOfDataSources()) {
      setListOfDataSources(plugin.listOfDataSources.clone());
    }
  }


  /**
   * Creates a new {@link PMFModelPlugin} instance.
   */
  public PMFModelPlugin(Model model) {
    super(model);
  }


  /*
   * (non-Javadoc)
   * @see AbstractSBasePlugin#clone()
   */
  @Override
  public PMFModelPlugin clone() {
    return new PMFModelPlugin(this);
  }


  @Override
  public boolean getAllowsChildren() {
    return true;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#getChildCount()
   */
  @Override
  public int getChildCount() {
    int childCount = 0;
    if (isSetListOfModelVariables()) {
      childCount++;
    }
    if (isSetListOfDataSources()) {
      childCount++;
    }
    return childCount;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#getChildAt(int)
   */
  @Override
  public SBase getChildAt(int childIndex) {
    if (childIndex < 0) {
      throw new IndexOutOfBoundsException(MessageFormat.format(
        resourceBundle.getString("IndexSurpassesBoundsException"),
        Integer.valueOf(childIndex), Integer.valueOf(0)));
    }
    int pos = 0;
    if (isSetListOfModelVariables()) {
      if (pos == childIndex) {
        return getListOfModelVariables();
      }
      pos++;
    }
    if (isSetListOfDataSources()) {
      if (pos == childIndex) {
        return getListOfDataSources();
      }
      pos++;
    }
    throw new IndexOutOfBoundsException(
      MessageFormat.format("Index {0, number, integer} >= {1, number, integer}",
        Integer.valueOf(childIndex), Integer.valueOf(Math.min(pos, 0))));
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractTreeNode#getParent()
   */
  @Override
  public SBMLDocument getParent() {
    if (isSetExtendedSBase()) {
      return (SBMLDocument) getExtendedSBase().getParent();
    }
    return null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#getParentSBMLObject()
   */
  @Override
  public SBMLDocument getParentSBMLObject() {
    return getParent();
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


  // *** ModelVariable ***
  /**
   * Adds a new {@link ModelVariable} to the {@link #listOfModelVariables}.
   * <p>
   * The listOfModelVariables is initialized if necessary.
   *
   * @param modelVariable
   *        the element to add to the list
   * @return {code true} (as specified by {@link java.util.Collection#add})
   * @see java.util.Collection#add(Object)
   */
  public boolean addModelVariable(ModelVariable modelVariable) {
    return getListOfModelVariables().add(modelVariable);
  }


  /**
   * Removes an element from the {@link #listOfModelVariables}.
   * 
   * @param modelVariable
   *        the element to be removed from the list.
   * @return {@code true} if the list contained the specified element and it was
   *         removed.
   * @see java.util.List#remove(Object)
   */
  public boolean removeModelVariable(ModelVariable modelVariable) {
    if (isSetListOfModelVariables()) {
      return getListOfModelVariables().remove(modelVariable);
    }
    return false;
  }


  /**
   * Removes an element from the {@link #listOfModelVariables} at the given
   * index.
   * 
   * @param i
   *        the index where to remove the {@link ModelVariable}.
   * @return the specified element if it was successfully found and removed.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or if the index is out of bound (
   *         {@code (i < 0) || (i > listOfModelVariables)}).
   */
  public ModelVariable removeModelVariable(int i) {
    if (!isSetListOfModelVariables()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfModelVariables().remove(i);
  }


  /**
   * Creates a new {@link ModelVariable} element and adds it to this
   * {@link #listOfModelVariables} list.
   *
   * @param src
   * @return the newly created {@link ModelVariable} element, which is the last
   *         element in the {@link #listOfModelVariables}.
   */
  public ModelVariable createModelVariable(String name) {
    ModelVariable mv = new ModelVariable(name);
    addModelVariable(mv);
    return mv;
  }


  /**
   * Creates a new instance of {@link ModelVariable} and add it to this
   * {@link PMFModelPlugin}.
   *
   * @param name
   *        the name to be set to the new {@link ModelVariable}.
   * @param value
   *        the value to be set to the new {@link ModelVariable}.
   * @return the new {@link ModelVariable} instance.
   */
  public ModelVariable createModelVariable(String name, double value) {
    ModelVariable mv = new ModelVariable(name, value, getLevel(), getVersion());
    addModelVariable(mv);
    return mv;
  }


  /**
   * Returns the number of {@link ModelVariable} of this {@link PMFModelPlugin}.
   *
   * @return the number of {@link ModelVariable} of this {@link PMFModelPlugin}.
   */
  public int getModelVariableCount() {
    return isSetListOfModelVariables() ? this.listOfModelVariables.size() : 0;
  }


  /**
   * Returns the number of {@link ModelVariable} of this {@link PMFModelPlugin}.
   * 
   * @return the number of {@link ModelVariable} of this {@link PMFModelPlugin}.
   * @libsbml.deprecated same as {@link #getModelVariableCount()}
   */
  public int getNumModelVariable() {
    return getModelVariableCount();
  }


  // *** listOfModelVariables methods ***
  /**
   * Returns the listOfModelVariables. If the {@link ListOf} is not defined,
   * creates an empty one.
   *
   * @return the listOfModelVariables
   */
  public ListOf<ModelVariable> getListOfModelVariables() {
    if (!isSetListOfModelVariables()) {
      this.listOfModelVariables = new ListOf<>();
      this.listOfModelVariables.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      this.listOfModelVariables.setPackageName(null);
      this.listOfModelVariables.setPackageName(PMFConstants.shortLabel);
      this.listOfModelVariables.setSBaseListType(ListOf.Type.other);
      if (this.extendedSBase != null) {
        this.extendedSBase.registerChild(this.listOfModelVariables);
      }
    }
    return this.listOfModelVariables;
  }


  public boolean isSetListOfModelVariables() {
    return this.listOfModelVariables != null;
  }


  public void setListOfModelVariables(
    final ListOf<ModelVariable> listOfModelVariables) {
    unsetListOfModelVariables();
    this.listOfModelVariables = listOfModelVariables;
    if (listOfModelVariables != null) {
      listOfModelVariables.setPackageVersion(-1);
      // changing the ListOf package name from 'core' to 'pmf'
      listOfModelVariables.setPackageName(null);
      listOfModelVariables.setPackageName(PMFConstants.shortLabel);
      listOfModelVariables.setSBaseListType(ListOf.Type.other);
      if (isSetExtendedSBase()) {
        this.extendedSBase.registerChild(listOfModelVariables);
      }
    }
  }


  /**
   * Removes the {@link #listOfModelVariables} from this {@link Model} and
   * notifies
   * all registered instances of
   * {@link org.sbml.jsbml.util.TreeNodeChangeListener}.
   *
   * @return {code true} if calling this method lead to a change in this data
   *         structure.
   */
  public boolean unsetListOfModelVariables() {
    if (this.listOfModelVariables != null) {
      ListOf<ModelVariable> oldListOfModelVariables = this.listOfModelVariables;
      this.listOfModelVariables = null;
      oldListOfModelVariables.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  // *** listOfDataSources ***
  /**
   * Adds a new {@link DataSource} to the {@link #listOfDataSources}.
   * <p>
   * The listOfDataSources is initialized if necessary.
   *
   * @param dataSource
   *        the element to add to the list
   * @return {@code true} (as specified by {@link java.util.Collection#add})
   * @see java.util.Collection#add(Object)
   */
  public boolean addDataSource(DataSource dataSource) {
    return getListOfDataSources().add(dataSource);
  }


  /**
   * Removes an element from the {@link #listOfDataSources}.
   *
   * @param dataSource
   *        the element to be removed from the list.
   * @return {@code true} if the list contained the specified element and it was
   *         removed.
   * @see java.util.List#remove(Object)
   */
  public boolean removeDataSource(DataSource dataSource) {
    if (isSetListOfDataSources()) {
      return getListOfDataSources().remove(dataSource);
    }
    return false;
  }


  /**
   * Removes an element from the {@link #listOfDataSources} at the given index.
   *
   * @param i
   *        the index where to remove the {@link DataSource}.
   * @return the specified element if it was successfully found and removed.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or if the index is
   *         out of bound ({@code (i < 0) || (i > listOfDataSources)}).
   */
  public DataSource removeDataSource(int i) {
    if (!isSetListOfDataSources()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfDataSources().remove(i);
  }


  /**
   * Creates a new {@link DataSource} element and adds it to the
   * {@link #listOfDataSources} list.
   * 
   * @param src
   * @return the newly created {@link DataSource} element, which is the
   *         last element in the {@link #listOfDataSources}.
   */
  public DataSource createDataSource(String src) {
    DataSource dataSource = new DataSource(src);
    addDataSource(dataSource);
    return dataSource;
  }


  /**
   * Gets an element from the {@link #listOfDataSources} at the given index.
   *
   * @param i
   *        the index of the {@link DataSource} element to get.
   * @return an element from the listOfDataSources at the given index.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or
   *         if the index is out of bound (index < 0 || index > list.size).
   */
  public DataSource getDataSource(int i) {
    if (!isSetListOfDataSources()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfDataSources().get(i);
  }


  /**
   * Returns the number of {@link DataSource}s in this
   * {@link PMFModelPlugin}.
   * 
   * @return the number of {@link DataSource}s in this
   *         {@link PMFModelPlugin}.
   */
  public int getDataSourceCount() {
    return isSetListOfDataSources() ? getListOfDataSources().size() : 0;
  }


  /**
   * Returns the number of {@link DataSource}s in this
   * {@link PMFModelPlugin}.
   * 
   * @return the number of {@link DataSource}s in this
   *         {@link PMFModelPlugin}.
   * @libsbml.deprecated same as {@link #getDataSourceCount()}
   */
  public int getNumDataSources() {
    return getDataSourceCount();
  }


  /**
   * Returns the {@link #listOfDataSources}.
   * Creates it if it does not already exist.
   *
   * @return the {@link #listOfDataSources}.
   */
  public ListOf<DataSource> getListOfDataSources() {
    if (this.listOfDataSources == null) {
      this.listOfDataSources = new ListOf<>();
      this.listOfDataSources.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      this.listOfDataSources.setPackageName(null);
      this.listOfDataSources.setPackageName(PMFConstants.shortLabel);
      this.listOfDataSources.setSBaseListType(ListOf.Type.other);
      if (this.extendedSBase != null) {
        this.extendedSBase.registerChild(listOfDataSources);
      }
    }
    return listOfDataSources;
  }


  /**
   * Returns {@code true} if {@link #listOfDataSources} contains at least
   * one element.
   *
   * @return {@code true} if {@link #listOfDataSources} contains at least
   *         one element, otherwise {@code false}.
   */
  public boolean isSetListOfDataSources() {
    if ((listOfDataSources == null) || listOfDataSources.isEmpty()) {
      return false;
    }
    return true;
  }


  /**
   * Sets the given {@code ListOf<DataSource>}.
   * If {@link #listOfDataSources} was defined before and contains some
   * elements, they are all unset.
   *
   * @param listOfDataSources
   */
  public void setListOfDataSources(ListOf<DataSource> listOfDataSources) {
    unsetListOfDataSources();
    this.listOfDataSources = listOfDataSources;
    if (this.listOfDataSources != null) {
      this.listOfDataSources.setPackageVersion(-1);
      // changing the ListOf package name from 'core' to 'pmf'
      this.listOfDataSources.setPackageName(null);
      this.listOfDataSources.setPackageName(PMFConstants.shortLabel);
      this.listOfDataSources.setSBaseListType(ListOf.Type.other);
      if (this.extendedSBase != null) {
        this.extendedSBase.registerChild(listOfDataSources);
      }
    }
  }


  /**
   * Returns {@code true} if {@link #listOfDataSources} contains at least
   * one element, otherwise {@code false}.
   *
   * @return {@code true} if {@link #listOfDataSources} contains at least
   *         one element, otherwise {@code false}.
   */
  public boolean unsetListOfDataSources() {
    if (this.listOfDataSources != null) {
      ListOf<DataSource> oldDataSources = this.listOfDataSources;
      this.listOfDataSources = null;
      oldDataSources.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }
}
