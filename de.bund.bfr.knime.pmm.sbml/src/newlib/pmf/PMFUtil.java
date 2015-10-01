package newlib.pmf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.tree.TreeNode;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.pmf.numl.PMFOntologyTerm;
import groovy.util.Node;
import newlib.numl.NMBase;
import newlib.numl.NuMLDocument;
import newlib.numl.ResultComponent;
import newlib.pmf.sbml.PMFAnnotation;
import newlib.pmf.sbml.PMFCompartment;
import newlib.pmf.sbml.PMFModel;
import newlib.pmf.sbml.PMFParameter;
import newlib.pmf.sbml.PMFSpecies;
import newlib.pmf.sbml.PMFUnitDefinition;

public class PMFUtil {

  public static String PMF_NS =
      "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML";
  public static String SBML_NS = "http://www.sbml.org/sbml/level3/version1/core";
  public static String NUML_NS = "http://www.numl.org/numl/level1/version1";
  public static String COMP_NS = "http://www.sbml.org/sbml/level3/version1/comp/version1";
  public static String DC_NS = "http://purl.org/dc/elements/1.1/";
  public static String XLINK_NS = "http://www.w3.org/1999/xlink";
  public static String DCTERMS_NS = "http://purl.org/dc/terms/";
  public static String PMML_NS = "http://www.dmg.org/PMML-4_2";

  public static Map<String, String> standardPrefixes;

  static {
    standardPrefixes = new HashMap<>(8);
    standardPrefixes.put("pmf", PMF_NS);
    standardPrefixes.put("sbml", SBML_NS);
    standardPrefixes.put("numl", NUML_NS);
    standardPrefixes.put("comp", COMP_NS);
    standardPrefixes.put("dc", DC_NS);
    standardPrefixes.put("xlink", XLINK_NS);
    standardPrefixes.put("dcterms", DCTERMS_NS);
    standardPrefixes.put("pmml", PMML_NS);
  }

  public static Map<String, List<String>> BaseAnnotations;

  static {
    List<String> dcTags = Arrays.asList("identifier", "source", "title", "description", "coverage",
        "type", "subject", "creator", "language", "rights", "description", "format");
    List<String> dcTermsTags =
        Arrays.asList("coverage", "references", "created", "modified", "hasVersion");

    BaseAnnotations = new HashMap<>(2);
    BaseAnnotations.put(DC_NS, dcTags);
    BaseAnnotations.put(DCTERMS_NS, dcTermsTags);
  }

  public static Map<String, List<String>> ModelAnnotations;

  static {
    ModelAnnotations = new HashMap<>(BaseAnnotations);
    ModelAnnotations.put(PMML_NS, Arrays.asList("modelquality"));
    ModelAnnotations.put(PMF_NS, Arrays.asList("datasources"));
  }

  Map<String, List<String>> DataSetAnnotations = BaseAnnotations;

  public static void addStandardPrefixes(SBase node) {
    Queue<SBase> queue = new LinkedList<>();

    // Put in the direct children of doc
    for (int i = 0; i < node.getChildCount(); i++) {
      queue.add((SBase) node.getChildAt(i));
    }

    while (!queue.isEmpty()) {
      SBase child = queue.remove();
      if (node instanceof Annotation) {
        addStandardPrefixes(((Annotation) child).getNonRDFannotation());
      }

      // Add children of `child`
      for (int i = 0; i < child.getChildCount(); i++) {
        queue.add((SBase) child.getChildAt(i));
      }
    }
  }

  public static void addStandardPrefixes(XMLNode node) {
    if (!node.getCharacters().isEmpty()) {
      String name = node.getName();
      String namespaceURI = node.getNamespaceURI();
      String prefix = node.getPrefix();

      // Looks for the prefix with tripleNS
      String nsEntry = null;
      for (Map.Entry<String, String> entry : standardPrefixes.entrySet()) {
        if (namespaceURI.equals(entry.getValue())) {
          nsEntry = entry.getValue();
          break;
        }
      }

      if (nsEntry != null && prefix != null) {
        node.setTriple(new XMLTriple(name, namespaceURI, prefix));
      }
    }

    for (int i = 0; i < node.getChildCount(); i++) {
      addStandardPrefixes(node.getChildAt(i));
    }
  }

  public static void addStandardPrefixes(Node node) {

    Map<Object, Object> attributes = node.attributes();
    for (Object obj : attributes.keySet()) {
      if (obj instanceof groovy.xml.QName) {
        groovy.xml.QName key = (groovy.xml.QName) obj;
        if (key.getPrefix() != null) {
          groovy.xml.QName prefixedKey = addStandardPrefixes(key);
          if (prefixedKey.getPrefix() != null) {
            Object value = attributes.get(key);
            attributes.remove(key);
            attributes.put(prefixedKey, value);
          }
        }
      }
    }
    
    for (Object child : node.children()) {
      if (child instanceof Node) {
        addStandardPrefixes((Node) child);
      }
    }
  }

  public static void addStandardPrefixes(NMBase node) {
    addStandardPrefixes(node.getAnnotation());
    for (NMBase child : node.getChildren()) {
      addStandardPrefixes(child);
    }
  }

  public static groovy.xml.QName addStandardPrefixes(groovy.xml.QName name) {
    // Looks for entry
    for (Map.Entry<String, String> entry : standardPrefixes.entrySet()) {
      if (name.getNamespaceURI().equals(entry.getValue())) {
        return new groovy.xml.QName(entry.getValue(), name.getLocalPart(), entry.getKey());
      }
    }
    return name;
  }

  public static SBMLDocument wrap(SBMLDocument doc) {
    Queue<SBase> queue = new LinkedList<>();

    // Put in the direct children of doc
    for (int i = 0; i < doc.getChildCount(); i++) {
      queue.add((SBase) doc.getChildAt(i));
    }

    while (!queue.isEmpty()) {
      TreeNode node = queue.remove();
      if (node instanceof Model) {
        PMFModel newNode = new PMFModel((Model) node);
        newNode.replace((Model) node);
      } else if (node instanceof Compartment) {
        PMFCompartment newNode = new PMFCompartment((Compartment) node);
        newNode.replace((Compartment) node);
      } else if (node instanceof UnitDefinition) {
        PMFUnitDefinition newNode = new PMFUnitDefinition((UnitDefinition) node);
        newNode.replace((UnitDefinition) node);
      } else if (node instanceof Species) {
        PMFSpecies newNode = new PMFSpecies((Species) node);
        newNode.replace((Species) node);
      } else if (node instanceof Parameter) {
        PMFParameter newNode = new PMFParameter((Parameter) node);
        newNode.replace((Parameter) node);
      } else if (node instanceof Annotation) {
        PMFAnnotation newNode = new PMFAnnotation((Annotation) node);
        newNode.replace((Annotation) node);
      }


      // Put in children of `node`
      for (int i = 0; i < node.getChildCount(); i++) {
        queue.add((SBase) node.getChildAt(i));
      }
    }

    return doc;
  }
  
  public static NuMLDocument wrap(NuMLDocument doc) {
    Queue<NMBase> queue = new LinkedList<>();
    
    // Add the direct children of doc
    for (NMBase child : doc.getChildren()) {
      queue.add(child);
    }
    
    while (!queue.isEmpty()) {
      NMBase node = queue.remove();
      if (node instanceof PMFResultComponent) {
        ResultComponent resultComponent = (ResultComponent) node;
        PMFResultComponent newChild = new PMFResultComponent(resultComponent);
        newChild.replace(resultComponent);
      }
      else if (node instanceof PMFOntologyTerm) {
        OntologyTerm ontologyTerm = (OntologyTerm) node;
        PMFOntologyTerm newChild = new PMFOntologyTerm(ontologyTerm);
        newChild.replace(ontologyTerm);
      }
      
      // Add children of `child`
      for (NMBase child : node.getChildren()) {
        queue.add(child);
      }
    }
  }
}
