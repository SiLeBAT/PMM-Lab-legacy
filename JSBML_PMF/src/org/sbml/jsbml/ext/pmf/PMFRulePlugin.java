/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.text.MessageFormat;
import java.util.Collection;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBase;

/**
 * @author Miguel Alba
 */
public class PMFRulePlugin extends PMFSBasePlugin {

  private static final long    serialVersionUID = 3663783888654852712L;
  private FormulaName          formulaName;
  private PmmLabId             pli;
  private ListOf<PMFReference> listOfReferences;


  /** Creates a new {@link PMFRulePlugin} instance cloned from 'plugin'. */
  public PMFRulePlugin(PMFRulePlugin plugin) {
    super(plugin);
    if (plugin.isSetFormulaName()) {
      setFormulaName(plugin.getFormulaName().clone());
    }
    if (plugin.isSetPmmLabId()) {
      setPmmLabId(plugin.getPmmLabId().clone());
    }
    if (plugin.isSetListOfReferences()) {
      setListOfReferences(plugin.getListOfReferences().clone());
    }
    // ...
  }


  /** Creates a new {@link PMFRulePlugin} instance from a {@link Rule}. */
  public PMFRulePlugin(Rule rule) {
    super(rule);
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#clone()
   */
  @Override
  public PMFRulePlugin clone() {
    return new PMFRulePlugin(this);
  }
  // *** plugin methods ***


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractTreeNode#getParent()
   */
  @Override
  public Rule getParent() {
    if (isSetExtendedSBase()) {
      return (Rule) getExtendedSBase();
    }
    return null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#getParentSBMLObject()
   */
  @Override
  public Rule getParentSBMLObject() {
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
    if (isSetFormulaName()) {
      childCount++;
    }
    if (isSetPmmLabId()) {
      childCount++;
    }
    if (isSetListOfReferences()) {
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
    if (isSetFormulaName()) {
      if (pos == childIndex) {
        return getFormulaName();
      }
      pos++;
    }
    if (isSetPmmLabId()) {
      if (pos == childIndex) {
        return getPmmLabId();
      }
      pos++;
    }
    if (isSetListOfReferences()) {
      if (pos == childIndex) {
        return getListOfReferences();
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


  // *** FormulaName ***
  /**
   * Returns the {@link FormulaName} of this {@link PMFRulePlugin}.
   *
   * @return the {@link FormulaName} of this {@link PMFRulePlugin}.
   */
  public FormulaName getFormulaName() {
    return this.formulaName;
  }


  /**
   * Returns whether formulaName is set.
   * 
   * @return whether formulaName is set.
   */
  public boolean isSetFormulaName() {
    return this.formulaName != null;
  }


  /**
   * Sets the {@link FormulaName} of this {@link PMFRulePlugin}.
   *
   * @param formulaName
   */
  public void setFormulaName(FormulaName formulaName) {
    unsetFormulaName();
    this.formulaName = formulaName;
    if (this.extendedSBase != null) {
      this.formulaName.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.formulaName);
    }
  }


  /**
   * Unsets the variable FormulaName.
   *
   * @return {@code true}, if formulaName was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetFormulaName() {
    if (this.formulaName != null) {
      FormulaName oldFormulaName = this.formulaName;
      this.formulaName = null;
      oldFormulaName.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  /**
   * Creates a new {@link FormulaName} inside this {@link PMFRulePlugin}, and
   * returns a pointer to it.
   *
   * @return the new {@link FormulaName} instance.
   */
  public FormulaName createFormulaName(String formulaName) {
    setFormulaName(new FormulaName(formulaName, getLevel(), getVersion()));
    return getFormulaName();
  }


  // *** PmmLabId methods ***
  /**
   * Returns the {@link PmmLabId} of this {@link PMFRulePlugin}.
   * 
   * @return the {@link PmmLabId} of this {@link PMFRulePlugin}.
   */
  public PmmLabId getPmmLabId() {
    return this.pli;
  }


  /**
   * Returns whether pli is set.
   * 
   * @return whether pli is set.
   */
  public boolean isSetPmmLabId() {
    return this.pli != null;
  }


  /**
   * Sets the {@link PmmLabId} of this {@link PMFRulePlugin}.
   * 
   * @param id
   */
  public void setPmmLabId(PmmLabId pli) {
    unsetPmmLabId();
    this.pli = pli;
    if (this.extendedSBase != null) {
      this.pli.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.pli);
    }
  }


  /**
   * Unsets the {@link PmmLabId} of this {@link PMFRulePlugin} and returns a
   * pointer to it.
   * 
   * @return {@code true}, if the {@link PmmLabId} was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetPmmLabId() {
    if (this.pli != null) {
      PmmLabId oldPli = this.pli;
      this.pli = null;
      oldPli.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  // *** listOfReferences ***
  /**
   * Adds a new element to the listOfReferences. listOfReferences is
   * initialized if necessary.
   *
   * @return {@code true} (as specified by {@link Collection#add})
   */
  public boolean addReference(PMFReference reference) {
    return getListOfReferences().add(reference);
  }


  /**
   * Returns the number of {@link PMFReference} of this {@link PMFRulePlugin}.
   *
   * @return the number of {@link PMFReference} of this {@link PMFRulePlugin}.
   */
  public int getReferenceCount() {
    return isSetListOfReferences() ? this.listOfReferences.size() : 0;
  }


  /**
   * Returns the number of {@link PMFReference} of this {@link PMFRulePlugin}.
   *
   * @return the number of {@link PMFReference} of this {@link PMFRulePlugin}.
   * @libsbml.deprecated same as {@link #getReferenceCount()}
   */
  public int getNumReference() {
    return getReferenceCount();
  }


  /**
   * Returns the listOfReferences. If the link {@link ListOf} is not defined,
   * creates an empty one.
   *
   * @return the listOfReferences
   */
  public ListOf<PMFReference> getListOfReferences() {
    if (!isSetListOfReferences()) {
      this.listOfReferences = new ListOf<>();
      this.listOfReferences.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      this.listOfReferences.setPackageName(null);
      this.listOfReferences.setPackageName(PMFConstants.shortLabel);
      this.listOfReferences.setSBaseListType(ListOf.Type.other);
      if (this.extendedSBase != null) {
        this.extendedSBase.registerChild(this.listOfReferences);
      }
    }
    return this.listOfReferences;
  }


  public boolean isSetListOfReferences() {
    return this.listOfReferences != null;
  }


  /**
   * @param listOfReferences
   */
  public void setListOfReferences(ListOf<PMFReference> listOfReferences) {
    unsetListOfReferences();
    this.listOfReferences = listOfReferences;
    if (listOfReferences != null) {
      this.listOfReferences.setPackageVersion(-1);
      // changing the listOf packaga name from 'core' to 'pmf'
      this.listOfReferences.setPackageName(null);
      this.listOfReferences.setPackageName(PMFConstants.shortLabel);
      this.listOfReferences.setSBaseListType(ListOf.Type.other);
      if (this.extendedSBase != null) {
        this.extendedSBase.registerChild(this.listOfReferences);
      }
    }
  }


  /**
   * Removes the {@link #listOfReferences} from this {@link PMFRulePlugin} and
   * notifies all registered instances of
   * {@link org.sbml.jsbml.util.TreeNodeChangeListener}.
   *
   * @return {@code true} if calling this method lead to a change in this data
   *         structure.
   */
  public boolean unsetListOfReferences() {
    if (this.listOfReferences != null) {
      ListOf<PMFReference> oldListOfReferences = this.listOfReferences;
      this.listOfReferences = null;
      oldListOfReferences.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }
}
