/**
 * 
 */
package org.sbml.jsbml.ext.parsers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mangosdk.spi.ProviderFor;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.ext.ASTNodePlugin;
import org.sbml.jsbml.ext.SBasePlugin;
import org.sbml.jsbml.ext.pmf.ModelVariable;
import org.sbml.jsbml.ext.pmf.PMFConstants;
import org.sbml.jsbml.ext.pmf.PMFModelPlugin;
import org.sbml.jsbml.xml.parsers.AbstractReaderWriter;
import org.sbml.jsbml.xml.parsers.PackageParser;
import org.sbml.jsbml.xml.parsers.ReadingParser;

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
    // TODO: getListOfSBMLElements

    if (logger.isDebugEnabled()) {
      logger.debug("getListOfSBMLElementsToWrite: " + sbase.getClass().getCanonicalName());
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
    
    // Need to check for every class that may be a parent node of the classes in the extension: Model and ListOf
    
    // Parent=Model -> Child=listOfModelVariables
    if (contextObject instanceof Model) {
      Model model = (Model) contextObject;
      if (elementName.equals(PMFConstants.listOfModelVariables)) {
        PMFModelPlugin plugin = new PMFModelPlugin(model);
        return plugin.getListOfModelVariables();
      }
    }
    
    // Parent=listOfModelVariables -> Child=ModelVariable
    else if (contextObject instanceof ListOf<?>) {
      @SuppressWarnings("unchecked")
      ListOf<SBase> listOf = (ListOf<SBase>) contextObject;
      if (elementName.equals(PMFConstants.modelVariable)) {
        Model model = (Model) listOf.getParentSBMLObject();
        PMFModelPlugin plugin = (PMFModelPlugin) model.getExtension(PMFConstants.shortLabel);
        
        ModelVariable mv = new ModelVariable();
        plugin.addModelVariable(mv);
        
        return mv;
      }
    }

    return contextObject;
  }
  
  /* (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.AbstractReaderWriter#getShortLabel()
   */
  @Override
  public String getShortLabel() {
    return PMFConstants.shortLabel;
  }
  
  /* (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.AbstractReaderWriter#getNamespaceURI
   */
  @Override
  public String getNamespaceURI() {
     return PMFConstants.namespaceURI;
  }
  
  /*
   * (non-Javadoc)
   * 
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
  
  /* (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.ReadingParser#getNamespaces()
   */
  @Override
  public List<String> getNamespaces() {
    return PMFConstants.namespaces;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.sbml.jsbml.xml.parsers.PackageParser#getPackageNamespaces()
   */
  @Override
  public List<String> getPackageNamespaces() {
    return getNamespaces();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.sbml.jsbml.xml.parsers.AbstractReaderWriter#getNamespaceURI()
   */
  @Override
  public String getPackageName() {
    return PMFConstants.shortLabel;
  }
  
  /*
   * (non-Javadoc)
   * 
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
