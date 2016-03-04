/**
 *
 */
package org.sbml.jsbml.ext.pmf;

import java.text.MessageFormat;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.ext.AbstractSBasePlugin;

/**
 * @author Miguel Alba
 */
public class PMFModelPlugin extends AbstractSBasePlugin {

  private static final long serialVersionUID = 5078386942266911188L;
  protected ListOf<ModelVariable> listOfModelVariables;


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
  }


  /**
   * Creates a new {@link PMFModelPlugin} instance.
   */
  public PMFModelPlugin(Model model) {
    super(model);
    initDefaults();
  }


  /*
   * (non-Javadoc)
   * @see AbstractSBasePlugin#clone()
   */
  @Override
  public PMFModelPlugin clone() {
    return new PMFModelPlugin(this);
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
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#getPrefix()
   */
  @Override
  public String getPrefix() {
    return PMFConstants.shortLabel;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#getURI()
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
   * @see org.sbml.jsbml.ext.SBasePlugin#getChildCount()
   */
  @Override
  public int getChildCount() {
    if (isSetListOfModelVariables()) {
      return 1;
    }
    return 0;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#getChildAt(int)
   */
  @Override
  public SBase getChildAt(int childIndex) {
    if (childIndex < 0 || childIndex >= 1) {
      Integer childCount = Integer.valueOf(Math.min(getChildCount(), 0));
      throw new IndexOutOfBoundsException(MessageFormat.format(
        resourceBundle.getString("IndexExceedsBoundsException"),
        Integer.valueOf(childIndex), childCount));
    }
    return this.listOfModelVariables;
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
   * Adds a new element to the listOfModelVariables.
   * listOfModelVariables is initialized if necessary.
   *
   * @return {code true} (as specified by {link Collection#add})
   */
  public boolean addModelVariable(ModelVariable modelVariable) {
    return getListOfModelVariables().add(modelVariable);
  }


  /**
   * Creates a new instance of {@link ModelVariable} and add it to this
   * {@link PMFModelPlugin}.
   *
   * @param id
   *        the name to be set to the new {@link ModelVariable}.
   * @return the new {@link ModelVariable} instance.
   */
  public ModelVariable createModelVariable(String id) {
    ModelVariable mv = new ModelVariable(id);
    addModelVariable(mv);
    return mv;
  }


  /**
   * Creates a new instance of {@link ModelVariable} and add it to this
   * {@link PMFModelPlugin}.
   *
   * @param id
   *        the name to be set to the new {@link ModelVariable}.
   * @param value
   *        the value to be set to the new {@link ModelVariable}.
   * @return the new {@link ModelVariable} instance.
   */
  public ModelVariable createModelVariable(String id, double value) {
    ModelVariable mv = new ModelVariable(id, value, getLevel(), getVersion());
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


  public void setListOfModelVariables(
    ListOf<ModelVariable> listOfModelVariables) {
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


  public boolean isSetListOfModelVariables() {
    return this.listOfModelVariables != null;
  }
}
