/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.ArrayList;
import java.util.List;

/**
 * Frequently used constants in the pmf package.
 * 
 * @author Miguel Alba
 */
public class PMFConstants {
  
  /** The name space URI of this parser for SBML level 3, version 1 and package version 1. */
  public static final String namespaceURI_L3V1V1 = "http://www.sbml.org/sbml/level3/version1/pmf/version1";
  
  /** The name space URI of this parser, this value can change between releases. */
  public static final String namespaceURI = namespaceURI_L3V1V1;
  
  public static final String shortLabel = "pmf";
  
  public static final int MIN_SBML_LEVEL = 3;
  
  public static final int MIN_SBML_VERSION = 1;
  
  public static final List<String> namespaces;
  
  static {
    namespaces = new ArrayList<>(1);
    namespaces.add(namespaceURI_L3V1V1);
  }
  
  // Objects defined in this plugin
  public static final String modelVariable = "modelVariable";
  public static final String correlation = "correlation";
  public static final String unitTransformation = "unitTransformation";
  
  public static final String p = "parameterP";
  public static final String t = "parameterT";
  public static final String error = "parameterError";
  public static final String description = "parameterDescription";
  public static final String paramMin = "paramMin";
  public static final String paramMax = "paramMax";
  
  public static final String listOfModelVariables = "listOfModelVariables";
  public static final String listOfCorrelations = "listOfCorrelations";
}
