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
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
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

public class PMFUnitDefinition {

  UnitDefinition unitDefinition;

  public PMFUnitDefinition() {
    unitDefinition = new UnitDefinition();
  }

  public PMFUnitDefinition(int level, int version) {
    unitDefinition = new UnitDefinition(level, version);
  }

  public PMFUnitDefinition(String id) {
    unitDefinition = new UnitDefinition(id);
    SBMLReplacement.initLevelAndVersion(unitDefinition);
  }

  public PMFUnitDefinition(String id, int level, int version) {
    unitDefinition = new UnitDefinition(id, level, version);
  }

  public PMFUnitDefinition(String id, String name, int level, int version) {
    unitDefinition = new UnitDefinition(id, name, level, version);
  }

  public PMFUnitDefinition(UnitDefinition unitDefinition) {
    unitDefinition = new UnitDefinition(unitDefinition);
    SBMLReplacement.initLevelAndVersion(unitDefinition);
  }

  public int hashCode() {
    return unitDefinition.hashCode();
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners) {
    return unitDefinition.addAllChangeListeners(listeners);
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners,
      boolean recursive) {
    return unitDefinition.addAllChangeListeners(listeners, recursive);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener) {
    unitDefinition.addTreeNodeChangeListener(listener);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    unitDefinition.addTreeNodeChangeListener(listener, recursive);
  }

  public boolean addCVTerm(CVTerm term) {
    return unitDefinition.addCVTerm(term);
  }

  public Enumeration<TreeNode> children() {
    return unitDefinition.children();
  }

  public void addDeclaredNamespace(String prefix, String namespace) {
    unitDefinition.addDeclaredNamespace(prefix, namespace);
  }

  public void addExtension(String nameOrUri, SBasePlugin sbasePlugin) {
    unitDefinition.addExtension(nameOrUri, sbasePlugin);
  }

  public void clearUserObjects() {
    unitDefinition.clearUserObjects();
  }

  public boolean containsUserObjectKey(Object key) {
    return unitDefinition.containsUserObjectKey(key);
  }

  public void addPlugin(String nameOrUri, SBasePlugin sbasePlugin) {
    unitDefinition.addPlugin(nameOrUri, sbasePlugin);
  }

  public void appendAnnotation(String annotation) throws XMLStreamException {
    unitDefinition.appendAnnotation(annotation);
  }

  public void appendAnnotation(XMLNode annotation) {
    unitDefinition.appendAnnotation(annotation);
  }

  public List<? extends TreeNode> filter(Filter filter) {
    return unitDefinition.filter(filter);
  }

  public void appendNotes(String notes) throws XMLStreamException {
    unitDefinition.appendNotes(notes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes) {
    return unitDefinition.filter(filter, retainInternalNodes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes,
      boolean prune) {
    return unitDefinition.filter(filter, retainInternalNodes, prune);
  }

  public void appendNotes(XMLNode notes) {
    unitDefinition.appendNotes(notes);
  }

  public void addUnit(String unit) {
    unitDefinition.addUnit(unit);
  }

  public void addUnit(Unit u) {
    unitDefinition.addUnit(u);
  }

  public void addUnit(Kind kind) {
    unitDefinition.addUnit(kind);
  }

  public void clear() {
    unitDefinition.clear();
  }

  public UnitDefinition clone() {
    return unitDefinition.clone();
  }

  public void convertToSIUnits() {
    unitDefinition.convertToSIUnits();
  }

  public Unit createUnit() {
    return unitDefinition.createUnit();
  }

  public Unit createUnit(Kind kind) {
    return unitDefinition.createUnit(kind);
  }

  public UnitDefinition divideBy(UnitDefinition definition) {
    return unitDefinition.divideBy(definition);
  }

  public History createHistory() {
    return unitDefinition.createHistory();
  }

  public SBasePlugin createPlugin(String nameOrUri) {
    return unitDefinition.createPlugin(nameOrUri);
  }

  public void disablePackage(String packageURIOrName) {
    unitDefinition.disablePackage(packageURIOrName);
  }

  public void enablePackage(String packageURIOrName) {
    unitDefinition.enablePackage(packageURIOrName);
  }

  public void enablePackage(String packageURIOrName, boolean enabled) {
    unitDefinition.enablePackage(packageURIOrName, enabled);
  }

  public List<String> filterCVTerms(Qualifier arg0, boolean arg1, String... arg2) {
    return unitDefinition.filterCVTerms(arg0, arg1, arg2);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern, boolean recursive) {
    return unitDefinition.filterCVTerms(qualifier, pattern, recursive);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern) {
    return unitDefinition.filterCVTerms(qualifier, pattern);
  }

  public List<CVTerm> filterCVTerms(Qualifier qualifier) {
    return unitDefinition.filterCVTerms(qualifier);
  }

  public String getId() {
    return unitDefinition.getId();
  }

  public String getName() {
    return unitDefinition.getName();
  }

  public boolean readAttribute(String attributeName, String prefix, String value) {
    return unitDefinition.readAttribute(attributeName, prefix, value);
  }

  public void setName(String name) {
    unitDefinition.setName(name);
  }

  public void unsetId() {
    unitDefinition.unsetId();
  }

  public void unsetName() {
    unitDefinition.unsetName();
  }

  public Map<String, String> writeXMLAttributes() {
    return unitDefinition.writeXMLAttributes();
  }

  public void fireNodeAddedEvent() {
    unitDefinition.fireNodeAddedEvent();
  }

  public int getIndex(TreeNode node) {
    return unitDefinition.getIndex(node);
  }

  public List<TreeNodeChangeListener> getListOfTreeNodeChangeListeners() {
    return unitDefinition.getListOfTreeNodeChangeListeners();
  }

  public int getNumChildren() {
    return unitDefinition.getNumChildren();
  }

  public TreeNode getRoot() {
    return unitDefinition.getRoot();
  }

  public int getTreeNodeChangeListenerCount() {
    return unitDefinition.getTreeNodeChangeListenerCount();
  }

  public Object getUserObject(Object key) {
    return unitDefinition.getUserObject(key);
  }

  public boolean getAllowsChildren() {
    return unitDefinition.getAllowsChildren();
  }

  public TreeNode getChildAt(int index) {
    return unitDefinition.getChildAt(index);
  }

  public void putUserObject(Object key, Object userObject) {
    unitDefinition.putUserObject(key, userObject);
  }

  public int getChildCount() {
    return unitDefinition.getChildCount();
  }

  public ListOf<Unit> getListOfUnits() {
    return unitDefinition.getListOfUnits();
  }

  public int getNumUnits() {
    return unitDefinition.getNumUnits();
  }

  public void removeAllTreeNodeChangeListeners() {
    unitDefinition.removeAllTreeNodeChangeListeners();
  }

  public int getUnitCount() {
    return unitDefinition.getUnitCount();
  }

  public boolean removeFromParent() {
    return unitDefinition.removeFromParent();
  }

  public ListOf<UnitDefinition> getParent() {
    return unitDefinition.getParent();
  }

  public Unit getUnit(int i) {
    return unitDefinition.getUnit(i);
  }

  public boolean isBuiltIn() {
    return unitDefinition.isBuiltIn();
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener) {
    unitDefinition.removeTreeNodeChangeListener(listener);
  }

  public void removeAllTreeNodeChangeListeners(boolean recursive) {
    unitDefinition.removeAllTreeNodeChangeListeners(recursive);
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    unitDefinition.removeTreeNodeChangeListener(listener, recursive);
  }

  public Object removeUserObject(Object key) {
    return unitDefinition.removeUserObject(key);
  }

  public void setParent(TreeNode parent) {
    unitDefinition.setParent(parent);
  }

  public Set<Object> userObjectKeySet() {
    return unitDefinition.userObjectKeySet();
  }

  public UnitDefinition multiplyWith(UnitDefinition definition) {
    return unitDefinition.multiplyWith(definition);
  }

  public void fireNodeRemovedEvent() {
    unitDefinition.fireNodeRemovedEvent();
  }

  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    unitDefinition.firePropertyChange(propertyName, oldValue, newValue);
  }

  public UnitDefinition raiseByThePowerOf(double exponent) {
    return unitDefinition.raiseByThePowerOf(exponent);
  }

  public Unit removeUnit(int i) {
    return unitDefinition.removeUnit(i);
  }

  public Annotation getAnnotation() {
    return unitDefinition.getAnnotation();
  }

  public String getAnnotationString() throws XMLStreamException {
    return unitDefinition.getAnnotationString();
  }

  public void setId(String id) {
    unitDefinition.setId(id);
  }

  public void setListOfUnits(ListOf<Unit> listOfUnits) {
    unitDefinition.setListOfUnits(listOfUnits);
  }

  public UnitDefinition simplify() {
    return unitDefinition.simplify();
  }

  public CVTerm getCVTerm(int index) {
    return unitDefinition.getCVTerm(index);
  }

  public int getCVTermCount() {
    return unitDefinition.getCVTermCount();
  }

  public List<CVTerm> getCVTerms() {
    return unitDefinition.getCVTerms();
  }

  public Map<String, String> getDeclaredNamespaces() {
    return unitDefinition.getDeclaredNamespaces();
  }

  public String getElementName() {
    return unitDefinition.getElementName();
  }

  public SBasePlugin getExtension(String nameOrUri) {
    return unitDefinition.getExtension(nameOrUri);
  }

  public int getExtensionCount() {
    return unitDefinition.getExtensionCount();
  }

  public Map<String, SBasePlugin> getExtensionPackages() {
    return unitDefinition.getExtensionPackages();
  }

  public String toString() {
    return unitDefinition.toString();
  }

  public boolean unsetListOfUnits() {
    return unitDefinition.unsetListOfUnits();
  }

  public History getHistory() {
    return unitDefinition.getHistory();
  }

  public int getLevel() {
    return unitDefinition.getLevel();
  }

  public ValuePair<Integer, Integer> getLevelAndVersion() {
    return unitDefinition.getLevelAndVersion();
  }

  public String getMetaId() {
    return unitDefinition.getMetaId();
  }

  public Model getModel() {
    return unitDefinition.getModel();
  }

  public String getNamespace() {
    return unitDefinition.getNamespace();
  }

  public XMLNode getNotes() {
    return unitDefinition.getNotes();
  }

  public String getNotesString() throws XMLStreamException {
    return unitDefinition.getNotesString();
  }

  public int getNumCVTerms() {
    return unitDefinition.getNumCVTerms();
  }

  public int getNumPlugins() {
    return unitDefinition.getNumPlugins();
  }

  public String getPackageName() {
    return unitDefinition.getPackageName();
  }

  public int getPackageVersion() {
    return unitDefinition.getPackageVersion();
  }

  public SBase getParentSBMLObject() {
    return unitDefinition.getParentSBMLObject();
  }

  public SBasePlugin getPlugin(String nameOrUri) {
    return unitDefinition.getPlugin(nameOrUri);
  }

  public SBMLDocument getSBMLDocument() {
    return unitDefinition.getSBMLDocument();
  }

  public int getSBOTerm() {
    return unitDefinition.getSBOTerm();
  }

  public String getSBOTermID() {
    return unitDefinition.getSBOTermID();
  }

  public String getURI() {
    return unitDefinition.getURI();
  }

  public int getVersion() {
    return unitDefinition.getVersion();
  }

  public boolean hasValidAnnotation() {
    return unitDefinition.hasValidAnnotation();
  }

  public boolean hasValidLevelVersionNamespaceCombination() {
    return unitDefinition.hasValidLevelVersionNamespaceCombination();
  }

  public boolean isExtendedByOtherPackages() {
    return unitDefinition.isExtendedByOtherPackages();
  }

  public boolean registerChild(SBase sbase) throws LevelVersionError {
    return unitDefinition.registerChild(sbase);
  }

  public boolean removeCVTerm(CVTerm cvTerm) {
    return unitDefinition.removeCVTerm(cvTerm);
  }

  public CVTerm removeCVTerm(int index) {
    return unitDefinition.removeCVTerm(index);
  }

  public void setAnnotation(Annotation annotation) {
    unitDefinition.setAnnotation(annotation);
  }

  public void setAnnotation(String nonRDFAnnotation) throws XMLStreamException {
    unitDefinition.setAnnotation(nonRDFAnnotation);
  }

  public void setAnnotation(XMLNode nonRDFAnnotation) {
    unitDefinition.setAnnotation(nonRDFAnnotation);
  }

  public void setHistory(History history) {
    unitDefinition.setHistory(history);
  }

  public void setLevel(int level) {
    unitDefinition.setLevel(level);
  }

  public void setMetaId(String metaId) {
    unitDefinition.setMetaId(metaId);
  }

  public void setNamespace(String namespace) {
    unitDefinition.setNamespace(namespace);
  }

  public void setNotes(String notes) throws XMLStreamException {
    unitDefinition.setNotes(notes);
  }

  public void setNotes(XMLNode notes) {
    unitDefinition.setNotes(notes);
  }

  public void setPackageVersion(int packageVersion) {
    unitDefinition.setPackageVersion(packageVersion);
  }

  public void setSBOTerm(int term) {
    unitDefinition.setSBOTerm(term);
  }

  public void setSBOTerm(String sboid) {
    unitDefinition.setSBOTerm(sboid);
  }

  public void setThisAsParentSBMLObject(SBase sbase) throws LevelVersionError {
    unitDefinition.setThisAsParentSBMLObject(sbase);
  }

  public void setVersion(int version) {
    unitDefinition.setVersion(version);
  }

  public void unregisterChild(SBase sbase) {
    unitDefinition.unregisterChild(sbase);
  }

  public void unsetAnnotation() {
    unitDefinition.unsetAnnotation();
  }

  public void unsetCVTerms() {
    unitDefinition.unsetCVTerms();
  }

  public void unsetExtension(String nameOrUri) {
    unitDefinition.unsetExtension(nameOrUri);
  }

  public void unsetHistory() {
    unitDefinition.unsetHistory();
  }

  public void unsetMetaId() {
    unitDefinition.unsetMetaId();
  }

  public void unsetNamespace() {
    unitDefinition.unsetNamespace();
  }

  public void unsetNotes() {
    unitDefinition.unsetNotes();
  }

  public void unsetPlugin(String nameOrUri) {
    unitDefinition.unsetPlugin(nameOrUri);
  }

  public void unsetSBOTerm() {
    unitDefinition.unsetSBOTerm();
  }

  public boolean equals(Object object) {
    return unitDefinition.equals(object);
  }

  public boolean isLeaf() {
    return unitDefinition.isLeaf();
  }

  public boolean isIdMandatory() {
    return unitDefinition.isIdMandatory();
  }

  public boolean isInvalid() {
    return unitDefinition.isInvalid();
  }

  public boolean isVariantOfSubstance() {
    return unitDefinition.isVariantOfSubstance();
  }

  public boolean isVariantOfSubstancePerLength() {
    return unitDefinition.isVariantOfSubstancePerLength();
  }

  public boolean isVariantOfSubstancePerTime() {
    return unitDefinition.isVariantOfSubstancePerTime();
  }

  public boolean isVariantOfTime() {
    return unitDefinition.isVariantOfTime();
  }

  public boolean isVariantOfVolume() {
    return unitDefinition.isVariantOfVolume();
  }

  public boolean isSetName() {
    return unitDefinition.isSetName();
  }

  public boolean isSetParent() {
    return unitDefinition.isSetParent();
  }

  public boolean isSetUserObjects() {
    return unitDefinition.isSetUserObjects();
  }

  public boolean isUnitKind() {
    return unitDefinition.isUnitKind();
  }

  public boolean isVariantOfLength() {
    return unitDefinition.isVariantOfLength();
  }

  public boolean isVariantOfSubstancePerVolume() {
    return unitDefinition.isVariantOfSubstancePerVolume();
  }

  public boolean isSetHistory() {
    return unitDefinition.isSetHistory();
  }

  public boolean isSetLevel() {
    return unitDefinition.isSetLevel();
  }

  public boolean isSetLevelAndVersion() {
    return unitDefinition.isSetLevelAndVersion();
  }

  public boolean isSetMetaId() {
    return unitDefinition.isSetMetaId();
  }

  public boolean isSetNotes() {
    return unitDefinition.isSetNotes();
  }

  public boolean isSetId() {
    return unitDefinition.isSetId();
  }

  public boolean isRoot() {
    return unitDefinition.isRoot();
  }

  public boolean isPredefined() {
    return unitDefinition.isPredefined();
  }

  public boolean isSetListOfUnits() {
    return unitDefinition.isSetListOfUnits();
  }

  public boolean isVariantOfArea() {
    return unitDefinition.isVariantOfArea();
  }

  public boolean isVariantOfSubstancePerArea() {
    return unitDefinition.isVariantOfSubstancePerArea();
  }

  public boolean isPackageEnabled(String packageURIOrName) {
    return unitDefinition.isPackageEnabled(packageURIOrName);
  }

  public boolean isPackageURIEnabled(String packageURIOrName) {
    return unitDefinition.isPackageURIEnabled(packageURIOrName);
  }

  public boolean isPkgEnabled(String packageURIOrName) {
    return unitDefinition.isPkgEnabled(packageURIOrName);
  }

  public boolean isPkgURIEnabled(String packageURIOrName) {
    return unitDefinition.isPkgURIEnabled(packageURIOrName);
  }

  public boolean isSetAnnotation() {
    return unitDefinition.isSetAnnotation();
  }

  public boolean isSetParentSBMLObject() {
    return unitDefinition.isSetParentSBMLObject();
  }

  public boolean isSetPlugin(String nameOrUri) {
    return unitDefinition.isSetPlugin(nameOrUri);
  }

  public boolean isSetSBOTerm() {
    return unitDefinition.isSetSBOTerm();
  }

  public boolean isSetVersion() {
    return unitDefinition.isSetVersion();
  }

  // replacement methods
  public void initLevelAndVersion() {
    SBMLReplacement.initLevelAndVersion(unitDefinition);
  }

  public void replace(UnitDefinition unitDefinitionToReplace) {
    SBMLReplacement.replace(unitDefinitionToReplace, unitDefinition);
  }

  List<ConformityMessage> getInvalidSettings(SBMLDocument document, String prefix,
      PMFDocument pmf) {
    return SBMLReplacement.getInvalidSettings(document, prefix, pmf);
  }

}
