/**
 *
 */
package org.sbml.jsbml.ext.pmf;

import java.text.MessageFormat;
import java.util.Collection;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.ext.AbstractSBasePlugin;

/**
 * @author Miguel Alba
 */
public class PMFModelPlugin extends AbstractSBasePlugin {

  private static final long       serialVersionUID = 5078386942266911188L;
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
    throw new IndexOutOfBoundsException(
      MessageFormat.format("Index {0, number, integer} >= {1, number, integer}",
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


  // *** ModelVariable ***
  /**
   * Adds a new element to the listOfModelVariables.
   * listOfModelVariables is initialized if necessary.
   *
   * @return {code true} (as specified by {@link Collection#add})
   */
  public boolean addModelVariable(ModelVariable modelVariable) {
    return getListOfModelVariables().add(modelVariable);
  }


  /**
   * Creates a new instance of {@link ModelVariable} and add it to this
   * {@link PMFModelPlugin}.
   *
   * @param name
   *        the name to be set to the new {@link ModelVariable}.
   * @return the new {@link ModelVariable} instance.
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


  public boolean isSetListOfModelVariables() {
    return this.listOfModelVariables != null;
  }


  // *** Parameter methods ***
  /**
   * Adds a Parameter instance to the listOfParameters of this PMFModelPlugin.
   * listOfCompartments is initialized if necessary.
   *
   * @see org.sbml.jsbml.Model#addParameter()
   * @return {code true} if the {@link #listOfParameters} was changed as a
   *         result of this call.
   */
  public boolean addParameter(final Parameter parameter) {
    return ((Model) this.extendedSBase).getListOfParameters().add(parameter);
  }


  /**
   * Creates a new {@link Parameter} inside this {@link PMFModelPlugin} and
   * returns it.
   * <p>
   * 
   * @return the {@link Parameter} object created
   * @see #addPMFParameter(PMFParameter parameter)
   */
  public Parameter createParameter(final String id) {
    Parameter parameter = new Parameter(id, getLevel(), getVersion());
    addParameter(parameter);
    return parameter;
  }


  // *** UnitDefinition methods ***
  /**
   * Adds an {@link UnitDefinition} instance to the
   * {@link #listOfUnitDefinitions} of this {@link Model}.
   * 
   * @param unitDefinition
   * @return {@code true} if the {@link #listOfUnitDefinitions} was changed
   *         as a result of this call.
   */
  public boolean addUnitDefinition(UnitDefinition unitDefinition) {
    return ((Model) this.extendedSBase).getListOfUnitDefinitions()
                                       .add(unitDefinition);
  }


  /**
   * Checks whether an identical {@link UnitDefinition} like the given
   * {@link UnitDefinition} is already in this {@link Model}'s
   * {@link #listOfUnitDefinitions}. If yes, the identifier of the identical
   * {@link UnitDefinition} will be returned. Otherwise, the given unit is added
   * to the {@link #listOfUnitDefinitions} and its identifier will be returned.
   * In any case, this method returns the identifier of a {@link UnitDefinition}
   * that is part of this {@link Model} at least after calling this method.
   * 
   * @param units
   *        The unit to be checked and added if no identical
   *        {@link UnitDefinition} can be found.
   * @return
   */
  public String addUnitDefinitionOrReturnIdenticalUnit(UnitDefinition units) {
    return ((Model) this.extendedSBase).addUnitDefinitionOrReturnIdenticalUnit(
      units);
  }


  /**
   * Creates a new {@link UnitDefinition} inside this {@link Model} and returns
   * it.
   * <p>
   * 
   * @return the {@link UnitDefinition} object created
   *         <p>
   * @see #addUnitDefinition(UnitDefinition ud)
   */
  public UnitDefinition createUnitDefinition() {
    return createUnitDefinition(null);
  }


  /**
   * Creates a new {@link UnitDefinition} inside this {@link Model} and returns
   * it.
   * 
   * @param id
   *        the id of the new element to create
   * @return the {@link UnitDefinition} object created
   */
  public UnitDefinition createUnitDefinition(String id) {
    UnitDefinition unitDefinition =
      new UnitDefinition(id, getLevel(), getVersion());
    addUnitDefinition(unitDefinition);
    return unitDefinition;
  }
}
