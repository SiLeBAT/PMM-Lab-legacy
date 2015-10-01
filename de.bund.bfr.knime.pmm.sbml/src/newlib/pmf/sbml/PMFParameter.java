package newlib.pmf.sbml;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.CVTerm.Qualifier;
import org.sbml.jsbml.History;
import org.sbml.jsbml.LevelVersionError;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.Unit.Kind;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.SBasePlugin;
import org.sbml.jsbml.util.TreeNodeChangeListener;
import org.sbml.jsbml.util.ValuePair;
import org.sbml.jsbml.util.filters.Filter;
import org.sbml.jsbml.xml.XMLNode;

import newlib.numl.ConformityMessage;
import newlib.pmf.PMFDocument;

public class PMFParameter {
  
  Parameter parameter;
  
  public PMFParameter() {
    parameter = new Parameter();
    SBMLReplacement.initLevelAndVersion(parameter);
  }
  
  public PMFParameter(int level, int version) {
    parameter = new Parameter(level, version);
  }
  
  public PMFParameter(LocalParameter localParameter) {
    parameter = new Parameter(localParameter);
    SBMLReplacement.initLevelAndVersion(parameter);
  }
  
  public PMFParameter(Parameter p) {
    parameter = new Parameter(p);
    SBMLReplacement.initLevelAndVersion(parameter);
  }
  
  public PMFParameter(String id) {
    parameter = new Parameter(id);
    SBMLReplacement.initLevelAndVersion(parameter);
  }
  
  public PMFParameter(String id, int level, int version) {
    parameter = new Parameter(id, level, version);
  }

  public Parameter clone() {
    return parameter.clone();
  }

  public String getPredefinedUnitID() {
    return parameter.getPredefinedUnitID();
  }

  public boolean containsUndeclaredUnits() {
    return parameter.containsUndeclaredUnits();
  }

  public void initDefaults(int level, int version) {
    parameter.initDefaults(level, version);
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners) {
    return parameter.addAllChangeListeners(listeners);
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners,
      boolean recursive) {
    return parameter.addAllChangeListeners(listeners, recursive);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener) {
    parameter.addTreeNodeChangeListener(listener);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    parameter.addTreeNodeChangeListener(listener, recursive);
  }

  public boolean addCVTerm(CVTerm term) {
    return parameter.addCVTerm(term);
  }

  public Enumeration<TreeNode> children() {
    return parameter.children();
  }

  public void addDeclaredNamespace(String prefix, String namespace) {
    parameter.addDeclaredNamespace(prefix, namespace);
  }

  public void addExtension(String nameOrUri, SBasePlugin sbasePlugin) {
    parameter.addExtension(nameOrUri, sbasePlugin);
  }

  public void clearUserObjects() {
    parameter.clearUserObjects();
  }

  public boolean containsUserObjectKey(Object key) {
    return parameter.containsUserObjectKey(key);
  }

  public void addPlugin(String nameOrUri, SBasePlugin sbasePlugin) {
    parameter.addPlugin(nameOrUri, sbasePlugin);
  }

  public void appendAnnotation(String annotation) throws XMLStreamException {
    parameter.appendAnnotation(annotation);
  }

  public void appendAnnotation(XMLNode annotation) {
    parameter.appendAnnotation(annotation);
  }

  public List<? extends TreeNode> filter(Filter filter) {
    return parameter.filter(filter);
  }

  public void appendNotes(String notes) throws XMLStreamException {
    parameter.appendNotes(notes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes) {
    return parameter.filter(filter, retainInternalNodes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes,
      boolean prune) {
    return parameter.filter(filter, retainInternalNodes, prune);
  }

  public void appendNotes(XMLNode notes) {
    parameter.appendNotes(notes);
  }

  public History createHistory() {
    return parameter.createHistory();
  }

  public SBasePlugin createPlugin(String nameOrUri) {
    return parameter.createPlugin(nameOrUri);
  }

  public void disablePackage(String packageURIOrName) {
    parameter.disablePackage(packageURIOrName);
  }

  public void enablePackage(String packageURIOrName) {
    parameter.enablePackage(packageURIOrName);
  }

  public void enablePackage(String packageURIOrName, boolean enabled) {
    parameter.enablePackage(packageURIOrName, enabled);
  }

  public boolean equals(Object o) {
    return parameter.equals(o);
  }

  public List<String> filterCVTerms(Qualifier arg0, boolean arg1, String... arg2) {
    return parameter.filterCVTerms(arg0, arg1, arg2);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern, boolean recursive) {
    return parameter.filterCVTerms(qualifier, pattern, recursive);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern) {
    return parameter.filterCVTerms(qualifier, pattern);
  }

  public List<CVTerm> filterCVTerms(Qualifier qualifier) {
    return parameter.filterCVTerms(qualifier);
  }

  public double getValue() {
    return parameter.getValue();
  }

  public void initDefaults() {
    parameter.initDefaults();
  }

  public boolean readAttribute(String attributeName, String prefix, String value) {
    return parameter.readAttribute(attributeName, prefix, value);
  }

  public UnitDefinition getDerivedUnitDefinition() {
    return parameter.getDerivedUnitDefinition();
  }

  public boolean isIdMandatory() {
    return parameter.isIdMandatory();
  }

  public boolean getConstant() {
    return parameter.getConstant();
  }

  public boolean isSetValue() {
    return parameter.isSetValue();
  }

  public int hashCode() {
    return parameter.hashCode();
  }

  public void setValue(double value) {
    parameter.setValue(value);
  }

  public String getDerivedUnits() {
    return parameter.getDerivedUnits();
  }

  public boolean isConstant() {
    return parameter.isConstant();
  }

  public Map<String, String> writeXMLAttributes() {
    return parameter.writeXMLAttributes();
  }

  public void unsetValue() {
    parameter.unsetValue();
  }

  public String getUnits() {
    return parameter.getUnits();
  }

  public boolean isSetConstant() {
    return parameter.isSetConstant();
  }

  public UnitDefinition getUnitsInstance() {
    return parameter.getUnitsInstance();
  }

  public void setConstant(boolean constant) {
    parameter.setConstant(constant);
  }

  public void unsetConstant() {
    parameter.unsetConstant();
  }

  public String getId() {
    return parameter.getId();
  }

  public boolean isPredefinedUnitsID(String unitsID) {
    return parameter.isPredefinedUnitsID(unitsID);
  }

  public String getName() {
    return parameter.getName();
  }

  public boolean isSetId() {
    return parameter.isSetId();
  }

  public boolean isSetUnits() {
    return parameter.isSetUnits();
  }

  public boolean isSetName() {
    return parameter.isSetName();
  }

  public boolean isSetUnitsInstance() {
    return parameter.isSetUnitsInstance();
  }

  public void setUnits(Kind unitKind) {
    parameter.setUnits(unitKind);
  }

  public void setUnits(String units) {
    parameter.setUnits(units);
  }

  public void setId(String id) {
    parameter.setId(id);
  }

  public void setUnits(Unit unit) {
    parameter.setUnits(unit);
  }

  public void setName(String name) {
    parameter.setName(name);
  }

  public void setUnits(UnitDefinition units) {
    parameter.setUnits(units);
  }

  public String toString() {
    return parameter.toString();
  }

  public void unsetUnits() {
    parameter.unsetUnits();
  }

  public void unsetId() {
    parameter.unsetId();
  }

  public void unsetName() {
    parameter.unsetName();
  }

  public void fireNodeAddedEvent() {
    parameter.fireNodeAddedEvent();
  }

  public int getIndex(TreeNode node) {
    return parameter.getIndex(node);
  }

  public List<TreeNodeChangeListener> getListOfTreeNodeChangeListeners() {
    return parameter.getListOfTreeNodeChangeListeners();
  }

  public int getNumChildren() {
    return parameter.getNumChildren();
  }

  public TreeNode getRoot() {
    return parameter.getRoot();
  }

  public int getTreeNodeChangeListenerCount() {
    return parameter.getTreeNodeChangeListenerCount();
  }

  public Object getUserObject(Object key) {
    return parameter.getUserObject(key);
  }

  public boolean isLeaf() {
    return parameter.isLeaf();
  }

  public boolean isRoot() {
    return parameter.isRoot();
  }

  public boolean isSetParent() {
    return parameter.isSetParent();
  }

  public boolean isSetUserObjects() {
    return parameter.isSetUserObjects();
  }

  public void putUserObject(Object key, Object userObject) {
    parameter.putUserObject(key, userObject);
  }

  public void removeAllTreeNodeChangeListeners() {
    parameter.removeAllTreeNodeChangeListeners();
  }

  public boolean removeFromParent() {
    return parameter.removeFromParent();
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener) {
    parameter.removeTreeNodeChangeListener(listener);
  }

  public void removeAllTreeNodeChangeListeners(boolean recursive) {
    parameter.removeAllTreeNodeChangeListeners(recursive);
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    parameter.removeTreeNodeChangeListener(listener, recursive);
  }

  public Object removeUserObject(Object key) {
    return parameter.removeUserObject(key);
  }

  public void setParent(TreeNode parent) {
    parameter.setParent(parent);
  }

  public Set<Object> userObjectKeySet() {
    return parameter.userObjectKeySet();
  }

  public void fireNodeRemovedEvent() {
    parameter.fireNodeRemovedEvent();
  }

  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    parameter.firePropertyChange(propertyName, oldValue, newValue);
  }

  public boolean getAllowsChildren() {
    return parameter.getAllowsChildren();
  }

  public Annotation getAnnotation() {
    return parameter.getAnnotation();
  }

  public String getAnnotationString() throws XMLStreamException {
    return parameter.getAnnotationString();
  }

  public TreeNode getChildAt(int childIndex) {
    return parameter.getChildAt(childIndex);
  }

  public int getChildCount() {
    return parameter.getChildCount();
  }

  public CVTerm getCVTerm(int index) {
    return parameter.getCVTerm(index);
  }

  public int getCVTermCount() {
    return parameter.getCVTermCount();
  }

  public List<CVTerm> getCVTerms() {
    return parameter.getCVTerms();
  }

  public Map<String, String> getDeclaredNamespaces() {
    return parameter.getDeclaredNamespaces();
  }

  public String getElementName() {
    return parameter.getElementName();
  }

  public SBasePlugin getExtension(String nameOrUri) {
    return parameter.getExtension(nameOrUri);
  }

  public int getExtensionCount() {
    return parameter.getExtensionCount();
  }

  public Map<String, SBasePlugin> getExtensionPackages() {
    return parameter.getExtensionPackages();
  }

  public History getHistory() {
    return parameter.getHistory();
  }

  public int getLevel() {
    return parameter.getLevel();
  }

  public ValuePair<Integer, Integer> getLevelAndVersion() {
    return parameter.getLevelAndVersion();
  }

  public String getMetaId() {
    return parameter.getMetaId();
  }

  public Model getModel() {
    return parameter.getModel();
  }

  public String getNamespace() {
    return parameter.getNamespace();
  }

  public XMLNode getNotes() {
    return parameter.getNotes();
  }

  public String getNotesString() throws XMLStreamException {
    return parameter.getNotesString();
  }

  public int getNumCVTerms() {
    return parameter.getNumCVTerms();
  }

  public int getNumPlugins() {
    return parameter.getNumPlugins();
  }

  public String getPackageName() {
    return parameter.getPackageName();
  }

  public int getPackageVersion() {
    return parameter.getPackageVersion();
  }

  public SBase getParent() {
    return parameter.getParent();
  }

  public SBase getParentSBMLObject() {
    return parameter.getParentSBMLObject();
  }

  public SBasePlugin getPlugin(String nameOrUri) {
    return parameter.getPlugin(nameOrUri);
  }

  public SBMLDocument getSBMLDocument() {
    return parameter.getSBMLDocument();
  }

  public int getSBOTerm() {
    return parameter.getSBOTerm();
  }

  public String getSBOTermID() {
    return parameter.getSBOTermID();
  }

  public String getURI() {
    return parameter.getURI();
  }

  public int getVersion() {
    return parameter.getVersion();
  }

  public boolean hasValidAnnotation() {
    return parameter.hasValidAnnotation();
  }

  public boolean hasValidLevelVersionNamespaceCombination() {
    return parameter.hasValidLevelVersionNamespaceCombination();
  }

  public boolean isExtendedByOtherPackages() {
    return parameter.isExtendedByOtherPackages();
  }

  public boolean isPackageEnabled(String packageURIOrName) {
    return parameter.isPackageEnabled(packageURIOrName);
  }

  public boolean isPackageURIEnabled(String packageURIOrName) {
    return parameter.isPackageURIEnabled(packageURIOrName);
  }

  public boolean isPkgEnabled(String packageURIOrName) {
    return parameter.isPkgEnabled(packageURIOrName);
  }

  public boolean isPkgURIEnabled(String packageURIOrName) {
    return parameter.isPkgURIEnabled(packageURIOrName);
  }

  public boolean isSetAnnotation() {
    return parameter.isSetAnnotation();
  }

  public boolean isSetHistory() {
    return parameter.isSetHistory();
  }

  public boolean isSetLevel() {
    return parameter.isSetLevel();
  }

  public boolean isSetLevelAndVersion() {
    return parameter.isSetLevelAndVersion();
  }

  public boolean isSetMetaId() {
    return parameter.isSetMetaId();
  }

  public boolean isSetNotes() {
    return parameter.isSetNotes();
  }

  public boolean isSetParentSBMLObject() {
    return parameter.isSetParentSBMLObject();
  }

  public boolean isSetPlugin(String nameOrUri) {
    return parameter.isSetPlugin(nameOrUri);
  }

  public boolean isSetSBOTerm() {
    return parameter.isSetSBOTerm();
  }

  public boolean isSetVersion() {
    return parameter.isSetVersion();
  }

  public boolean registerChild(SBase sbase) throws LevelVersionError {
    return parameter.registerChild(sbase);
  }

  public boolean removeCVTerm(CVTerm cvTerm) {
    return parameter.removeCVTerm(cvTerm);
  }

  public CVTerm removeCVTerm(int index) {
    return parameter.removeCVTerm(index);
  }

  public void setAnnotation(Annotation annotation) {
    parameter.setAnnotation(annotation);
  }

  public void setAnnotation(String nonRDFAnnotation) throws XMLStreamException {
    parameter.setAnnotation(nonRDFAnnotation);
  }

  public void setAnnotation(XMLNode nonRDFAnnotation) {
    parameter.setAnnotation(nonRDFAnnotation);
  }

  public void setHistory(History history) {
    parameter.setHistory(history);
  }

  public void setLevel(int level) {
    parameter.setLevel(level);
  }

  public void setMetaId(String metaId) {
    parameter.setMetaId(metaId);
  }

  public void setNamespace(String namespace) {
    parameter.setNamespace(namespace);
  }

  public void setNotes(String notes) throws XMLStreamException {
    parameter.setNotes(notes);
  }

  public void setNotes(XMLNode notes) {
    parameter.setNotes(notes);
  }

  public void setPackageVersion(int packageVersion) {
    parameter.setPackageVersion(packageVersion);
  }

  public void setSBOTerm(int term) {
    parameter.setSBOTerm(term);
  }

  public void setSBOTerm(String sboid) {
    parameter.setSBOTerm(sboid);
  }

  public void setThisAsParentSBMLObject(SBase sbase) throws LevelVersionError {
    parameter.setThisAsParentSBMLObject(sbase);
  }

  public void setVersion(int version) {
    parameter.setVersion(version);
  }

  public void unregisterChild(SBase sbase) {
    parameter.unregisterChild(sbase);
  }

  public void unsetAnnotation() {
    parameter.unsetAnnotation();
  }

  public void unsetCVTerms() {
    parameter.unsetCVTerms();
  }

  public void unsetExtension(String nameOrUri) {
    parameter.unsetExtension(nameOrUri);
  }

  public void unsetHistory() {
    parameter.unsetHistory();
  }

  public void unsetMetaId() {
    parameter.unsetMetaId();
  }

  public void unsetNamespace() {
    parameter.unsetNamespace();
  }

  public void unsetNotes() {
    parameter.unsetNotes();
  }

  public void unsetPlugin(String nameOrUri) {
    parameter.unsetPlugin(nameOrUri);
  }

  public void unsetSBOTerm() {
    parameter.unsetSBOTerm();
  }
  
  // Replacement methods
  public void initLevelAndVersion() {
    SBMLReplacement.initLevelAndVersion(parameter);
  }
  
  public void replace(Parameter parameterToReplace) {
    SBMLReplacement.replace(parameterToReplace, parameter);
  }
  
  public List<ConformityMessage> getInvalidSettings(SBMLDocument document, String prefix, PMFDocument pmf) {
    return SBMLReplacement.getInvalidSettings(document, prefix, pmf);
  }

}

class DoubleRange {

  final double from;
  final double to;
  
  public DoubleRange(double from, double to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(from);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(to);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DoubleRange other = (DoubleRange) obj;
    if (Double.doubleToLongBits(from) != Double.doubleToLongBits(other.from))
      return false;
    if (Double.doubleToLongBits(to) != Double.doubleToLongBits(other.to))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "DoubleRange [from=" + from + ", to=" + to + "]";
  }
}