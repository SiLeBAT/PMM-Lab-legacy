/**
 * 
 */
package org.sbml.jsbml.xml.parsers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mangosdk.spi.ProviderFor;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.ASTNodePlugin;
import org.sbml.jsbml.ext.SBasePlugin;
import org.sbml.jsbml.ext.pmf.Correlation;
import org.sbml.jsbml.ext.pmf.FormulaName;
import org.sbml.jsbml.ext.pmf.ModelVariable;
import org.sbml.jsbml.ext.pmf.PMFConstants;
import org.sbml.jsbml.ext.pmf.PMFModelPlugin;
import org.sbml.jsbml.ext.pmf.PMFParameterPlugin;
import org.sbml.jsbml.ext.pmf.PMFReference;
import org.sbml.jsbml.ext.pmf.PMFRulePlugin;
import org.sbml.jsbml.ext.pmf.PMFUnitDefinitionPlugin;
import org.sbml.jsbml.ext.pmf.ParamMax;
import org.sbml.jsbml.ext.pmf.ParamMin;
import org.sbml.jsbml.ext.pmf.ParameterDescription;
import org.sbml.jsbml.ext.pmf.ParameterError;
import org.sbml.jsbml.ext.pmf.ParameterP;
import org.sbml.jsbml.ext.pmf.ParameterT;
import org.sbml.jsbml.ext.pmf.PmmLabId;
import org.sbml.jsbml.ext.pmf.UnitTransformation;

/**
 * @author Miguel Alba
 */
@ProviderFor(ReadingParser.class)
public class PMFParser extends AbstractReaderWriter implements PackageParser {

  /** A {@link Logger} for this class. */
  private static final transient Logger logger =
    Logger.getLogger(PMFParser.class);


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.WritingParser#getListOfSBMLElementsToWrite(Object
   * sbase)
   */
  @Override
  public List<Object> getListOfSBMLElementsToWrite(Object sbase) {
    if (logger.isDebugEnabled()) {
      logger.debug(
        "getListOfSBMLElementsToWrite: " + sbase.getClass().getCanonicalName());
    }
    List<Object> listOfElementsToWrite = new ArrayList<>();
    // test if this treeNode is an extended SBase
    if (sbase instanceof Model) {
      SBasePlugin modelPlugin = ((Model) sbase).getExtension(getNamespaceURI());
      if (modelPlugin != null) {
        listOfElementsToWrite = super.getListOfSBMLElementsToWrite(modelPlugin);
      }
    } else {
      listOfElementsToWrite = super.getListOfSBMLElementsToWrite(sbase);
    }
    return listOfElementsToWrite;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.ReadingParser#processStartElement(String
   * elementName, String prefix,
   * boolean hasAttributes, boolean hasNamespaces, Object contextObject)
   */
  @Override
  public Object processStartElement(String elementName, String uri,
    String prefix, boolean hasAttributes, boolean hasNamespaces,
    Object contextObject) {
    // contextObject is the parent node of the found object
    if (logger.isDebugEnabled()) {
      logger.debug("processStartElement: " + elementName);
    }
    // Need to check for every class that may be a parent node of the classes in
    // the extension: Model and ListOf
    // Parent=Model -> Child=listOfModelVariables
    if (contextObject instanceof Model) {
      Model model = (Model) contextObject;
      if (elementName.equals(PMFConstants.listOfModelVariables)) {
        PMFModelPlugin plugin = new PMFModelPlugin(model);
        model.addExtension(PMFConstants.shortLabel, plugin);
        return plugin.getListOfModelVariables();
      }
    }
    // Parent=listOfModelVariables -> Child=ModelVariable
    else if (contextObject instanceof ListOf<?>) {
      @SuppressWarnings("unchecked")
      ListOf<SBase> listOf = (ListOf<SBase>) contextObject;
      if (elementName.equals(PMFConstants.modelVariable)) {
        Model model = (Model) listOf.getParentSBMLObject();
        PMFModelPlugin plugin =
          (PMFModelPlugin) model.getExtension(PMFConstants.shortLabel);
        ModelVariable mv = new ModelVariable();
        plugin.addModelVariable(mv);
        return mv;
      } else if (elementName.equals(PMFConstants.correlation)) {
        Parameter parameter = (Parameter) listOf.getParentSBMLObject();
        PMFParameterPlugin plugin =
          (PMFParameterPlugin) parameter.getExtension(PMFConstants.shortLabel);
        Correlation correlation = new Correlation();
        plugin.addCorrelation(correlation);
        return correlation;
      } else if (elementName.equals(PMFConstants.pmfReference)) {
        Rule rule = (Rule) listOf.getParentSBMLObject();
        PMFRulePlugin plugin =
          (PMFRulePlugin) rule.getExtension(PMFConstants.shortLabel);
        PMFReference ref = new PMFReference();
        plugin.addReference(ref);
        return ref;
      }
    }
    // Parent=UnitDefinition -> Child=UnitTransformation
    else if (contextObject instanceof UnitDefinition) {
      UnitDefinition unitDefinition = (UnitDefinition) contextObject;
      PMFUnitDefinitionPlugin plugin =
        new PMFUnitDefinitionPlugin(unitDefinition);
      unitDefinition.addExtension(PMFConstants.shortLabel, plugin);
      UnitTransformation unitTransformation = new UnitTransformation();
      plugin.setUnitTransformation(unitTransformation);
      return unitTransformation;
    }
    // Parent=Parameter -> Child=P|T|Error|Description
    else if (contextObject instanceof Parameter) {
      Parameter parameter = (Parameter) contextObject;
      PMFParameterPlugin plugin;
      if (parameter.getExtension(PMFConstants.shortLabel) == null) {
        plugin = new PMFParameterPlugin(parameter);
        parameter.addExtension(PMFConstants.shortLabel, plugin);
      } else {
        plugin =
          (PMFParameterPlugin) parameter.getExtension(PMFConstants.shortLabel);
      }
      if (elementName.equals(PMFConstants.p)) {
        ParameterP p = new ParameterP();
        plugin.setP(p);
        return p;
      } else if (elementName.equals(PMFConstants.t)) {
        ParameterT t = new ParameterT();
        plugin.setT(t);
        return t;
      } else if (elementName.equals(PMFConstants.error)) {
        ParameterError error = new ParameterError();
        plugin.setError(error);
        return error;
      } else if (elementName.equals(PMFConstants.description)) {
        ParameterDescription description = new ParameterDescription();
        plugin.setDescription(description);
        return description;
      } else if (elementName.equals(PMFConstants.paramMin)) {
        ParamMin min = new ParamMin();
        plugin.setMin(min);
        return min;
      } else if (elementName.equals(PMFConstants.paramMax)) {
        ParamMax max = new ParamMax();
        plugin.setMax(max);
        return max;
      } else if (elementName.equals(PMFConstants.listOfCorrelations)) {
        return plugin.getListOfCorrelations();
      }
    }
    // Parent=Rule -> Child=FormulaName|listOfReferences|PmmLabId
    else if (contextObject instanceof Rule) {
      Rule rule = (Rule) contextObject;
      PMFRulePlugin plugin;
      if (rule.getExtension(PMFConstants.shortLabel) == null) {
        plugin = new PMFRulePlugin(rule);
        rule.addExtension(PMFConstants.shortLabel, plugin);
      } else {
        plugin = (PMFRulePlugin) rule.getExtension(PMFConstants.shortLabel);
      }
      
      if (elementName.equals(PMFConstants.formulaName)) {
        FormulaName formulaName = new FormulaName();
        plugin.setFormulaName(formulaName);
        return formulaName;
      } else if (elementName.equals(PMFConstants.listOfReferences)) {
        return plugin.getListOfReferences();
      } else if (elementName.equals(PMFConstants.pmmlabId)) {
        PmmLabId pli = new PmmLabId();
        plugin.setPmmLabId(pli);
        return pli;
      }
    }
    return contextObject;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.AbstractReaderWriter#getShortLabel()
   */
  @Override
  public String getShortLabel() {
    return PMFConstants.shortLabel;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.AbstractReaderWriter#getNamespaceURI
   */
  @Override
  public String getNamespaceURI() {
    return PMFConstants.namespaceURI;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.PackageParser#getNamespaceFor(java.lang.
   * String, java.lang.String, java.lang.String)
   */
  @Override
  public String getNamespaceFor(int level, int version, int packageVersion) {
    if (level == 3 && version == 1 && packageVersion == 1) {
      return PMFConstants.namespaceURI_L3V1V1;
    }
    return null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.ReadingParser#getNamespaces()
   */
  @Override
  public List<String> getNamespaces() {
    return PMFConstants.namespaces;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.PackageParser#getPackageNamespaces()
   */
  @Override
  public List<String> getPackageNamespaces() {
    return getNamespaces();
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.AbstractReaderWriter#getNamespaceURI()
   */
  @Override
  public String getPackageName() {
    return PMFConstants.shortLabel;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.PackageParser#isRequired()
   */
  @Override
  public boolean isRequired() {
    return true;
  }


  @Override
  public SBasePlugin createPluginFor(SBase sbase) {
    if (sbase != null) {
      if (sbase instanceof Model) {
        return new PMFModelPlugin((Model) sbase);
      } else if (sbase instanceof UnitDefinition) {
        return new PMFUnitDefinitionPlugin((UnitDefinition) sbase);
      } else if (sbase instanceof Parameter) {
        return new PMFParameterPlugin((Parameter) sbase);
      } else if (sbase instanceof Rule) {
        return new PMFRulePlugin((Rule) sbase);
      }
    }
    return null;
  }


  @Override
  public ASTNodePlugin createPluginFor(ASTNode astNode) {
    // This package does not extends ASTNode
    return null;
  }
}
