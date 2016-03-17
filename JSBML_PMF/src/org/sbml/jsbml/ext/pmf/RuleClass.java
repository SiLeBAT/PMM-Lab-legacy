/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;

/**
 * @author Miguel Alba
 */
public class RuleClass extends AbstractSBase {

  /**
   * 
   */
  private static final long serialVersionUID = 7847012598027584096L;

  public static enum ModelClass {
                                 UNKNOWN,
                                 GROWTH,
                                 INACTIVATION,
                                 SURVIVAL,
                                 GROWTH_INACTIVATION,
                                 INACTIVATION_SURVIVAL,
                                 GROWTH_SURVIVAL,
                                 GROWTH_INACTIVATION_SURVIVAL,
                                 T,
                                 PH,
                                 AW,
                                 T_PH,
                                 T_AW,
                                 PH_AW,
                                 T_PH_AW;

    public String getName() {
      switch (this) {
      case UNKNOWN:
        return "unknown";
      case GROWTH:
        return "growth";
      case INACTIVATION:
        return "inactivation";
      case SURVIVAL:
        return "survival";
      case GROWTH_INACTIVATION:
        return "growth/inactivation";
      case INACTIVATION_SURVIVAL:
        return "inactivation/survival";
      case GROWTH_SURVIVAL:
        return "growth/survival";
      case GROWTH_INACTIVATION_SURVIVAL:
        return "growth/inactivation/survival";
      case T:
        return "T";
      case PH:
        return "pH";
      case AW:
        return "aw";
      case T_PH:
        return "T/pH";
      case T_AW:
        return "T/aw";
      case PH_AW:
        return "pH/aw";
      case T_PH_AW:
        return "T/pH/aw";
      default:
        return "unknown";
      }
    }
  }

  private ModelClass modelClass;


  /** Creates a {@link RuleClass} instance. */
  public RuleClass() {
    super();
    this.packageName = PMFConstants.shortLabel;
  }


  /** Creates a {@link RuleClass} instance from a {@link ModelClass}. */
  public RuleClass(ModelClass modelClass) {
    super();
    this.modelClass = modelClass;
    this.packageName = PMFConstants.shortLabel;
  }


  /**
   * Creates a {@link RuleClass} instance from a {@link ModelClass}, level and
   * version.
   */
  public RuleClass(ModelClass modelClass, int level, int version) {
    super(level, version);
    this.modelClass = modelClass;
    this.packageName = PMFConstants.shortLabel;
  }


  /** Clone constructor. */
  public RuleClass(RuleClass ruleClass) {
    super(ruleClass);
  }


  /** Clones this class. */
  @Override
  public RuleClass clone() {
    return new RuleClass(this);
  }


  // *** modelClass methods ***
  /**
   * Returns model class.
   *
   * @return model class.
   */
  public ModelClass getModelClass() {
    return this.modelClass;
  }


  /**
   * Returns whether model class is set.
   *
   * @return whether model class is set.
   */
  public boolean isSetModelClass() {
    return this.modelClass != null;
  }


  /**
   * Sets model class.
   *
   * @param modelClass
   */
  public void setModelClass(ModelClass modelClass) {
    ModelClass oldModelClass = this.modelClass;
    this.modelClass = modelClass;
    firePropertyChange("modelClass", oldModelClass, this.modelClass);
  }


  /**
   * Unsets model class.
   *
   * @return {@code true}, if model class was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetModelClass() {
    if (isSetModelClass()) {
      ModelClass oldModelClass = this.modelClass;
      this.modelClass = null;
      firePropertyChange("modelClass", oldModelClass, this.modelClass);
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
    if (attributeName.equals("modelClass")) {
      switch (value) {
      case "unknown":
        setModelClass(ModelClass.UNKNOWN);
        return true;
      case "growth":
        setModelClass(ModelClass.GROWTH);
        return true;
      case "inactivation":
        setModelClass(ModelClass.INACTIVATION);
        return true;
      case "survival":
        setModelClass(ModelClass.SURVIVAL);
        return true;
      case "growth/inactivation":
        setModelClass(ModelClass.GROWTH_INACTIVATION);
        return true;
      case "inactivation/survival":
        setModelClass(ModelClass.INACTIVATION_SURVIVAL);
        return true;
      case "growth/survival":
        setModelClass(ModelClass.GROWTH_SURVIVAL);
        return true;
      case "growth/inactivation/survival":
        setModelClass(ModelClass.GROWTH_INACTIVATION_SURVIVAL);
        return true;
      case "T":
        setModelClass(ModelClass.T);
        return true;
      case "PH":
        setModelClass(ModelClass.PH);
        return true;
      case "aw":
        setModelClass(ModelClass.AW);
        return true;
      case "T/pH":
        setModelClass(ModelClass.T_PH);
        return true;
      case "T/aw":
        setModelClass(ModelClass.T_AW);
        return true;
      case "pH/aw":
        setModelClass(ModelClass.PH_AW);
        return true;
      case "T/pH/aw":
        setModelClass(ModelClass.T_PH_AW);
        return true;
      default:
        return false;
      }
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
    if (isSetModelClass()) {
      attributes.put("modelClass", this.modelClass.getName());
    }
    return attributes;
  }


  @Override
  public String toString() {
    return "Ruleclass [modelClass=\"" + this.modelClass.getName() + "\"]";
  }
}
