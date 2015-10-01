package newlib.pmf.sbml;

import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.CVTerm.Qualifier;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.History;
import org.sbml.jsbml.LevelVersionError;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.PropertyNotAvailableException;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesType;
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

public class PMFSpecies {
  
  Species species;
  
  public PMFSpecies() {
    species = new Species();
    initSpecies();
  }
  
  public PMFSpecies(int level, int version) {
    species = new Species(level, version);
    initSpecies();
  }
  
  public PMFSpecies(Species species) {
    species = new Species(species);
    initSpecies();
  }
  
  public PMFSpecies(String id, int level, int version) {
    species = new Species(id, level, version);
    initSpecies();
  }
  
  public PMFSpecies(String id, String name, int level, int version) {
    species = new Species(id, name, level, version);
    initSpecies();
  }
  
  private void initSpecies() {
    SBMLReplacement.initLevelAndVersion(species);
    species.setHasOnlySubstanceUnits(false);
  }

  public double getValue() {
    return species.getValue();
  }

  public boolean isSetValue() {
    return species.isSetValue();
  }

  public Species clone() {
    return species.clone();
  }

  public boolean containsUndeclaredUnits() {
    return species.containsUndeclaredUnits();
  }

  public boolean equals(Object object) {
    return species.equals(object);
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners) {
    return species.addAllChangeListeners(listeners);
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners,
      boolean recursive) {
    return species.addAllChangeListeners(listeners, recursive);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener) {
    species.addTreeNodeChangeListener(listener);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    species.addTreeNodeChangeListener(listener, recursive);
  }

  public boolean addCVTerm(CVTerm term) {
    return species.addCVTerm(term);
  }

  public Enumeration<TreeNode> children() {
    return species.children();
  }

  public void addDeclaredNamespace(String prefix, String namespace) {
    species.addDeclaredNamespace(prefix, namespace);
  }

  public void addExtension(String nameOrUri, SBasePlugin sbasePlugin) {
    species.addExtension(nameOrUri, sbasePlugin);
  }

  public void clearUserObjects() {
    species.clearUserObjects();
  }

  public boolean containsUserObjectKey(Object key) {
    return species.containsUserObjectKey(key);
  }

  public void addPlugin(String nameOrUri, SBasePlugin sbasePlugin) {
    species.addPlugin(nameOrUri, sbasePlugin);
  }

  public void appendAnnotation(String annotation) throws XMLStreamException {
    species.appendAnnotation(annotation);
  }

  public void appendAnnotation(XMLNode annotation) {
    species.appendAnnotation(annotation);
  }

  public List<? extends TreeNode> filter(Filter filter) {
    return species.filter(filter);
  }

  public void appendNotes(String notes) throws XMLStreamException {
    species.appendNotes(notes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes) {
    return species.filter(filter, retainInternalNodes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes,
      boolean prune) {
    return species.filter(filter, retainInternalNodes, prune);
  }

  public void appendNotes(XMLNode notes) {
    species.appendNotes(notes);
  }

  public History createHistory() {
    return species.createHistory();
  }

  public SBasePlugin createPlugin(String nameOrUri) {
    return species.createPlugin(nameOrUri);
  }

  public void disablePackage(String packageURIOrName) {
    species.disablePackage(packageURIOrName);
  }

  public void enablePackage(String packageURIOrName) {
    species.enablePackage(packageURIOrName);
  }

  public void enablePackage(String packageURIOrName, boolean enabled) {
    species.enablePackage(packageURIOrName, enabled);
  }

  public List<String> filterCVTerms(Qualifier arg0, boolean arg1, String... arg2) {
    return species.filterCVTerms(arg0, arg1, arg2);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern, boolean recursive) {
    return species.filterCVTerms(qualifier, pattern, recursive);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern) {
    return species.filterCVTerms(qualifier, pattern);
  }

  public List<CVTerm> filterCVTerms(Qualifier qualifier) {
    return species.filterCVTerms(qualifier);
  }

  public boolean isIdMandatory() {
    return species.isIdMandatory();
  }

  public boolean getConstant() {
    return species.getConstant();
  }

  public void setValue(double value) {
    species.setValue(value);
  }

  public String getDerivedUnits() {
    return species.getDerivedUnits();
  }

  public boolean isConstant() {
    return species.isConstant();
  }

  public void unsetValue() {
    species.unsetValue();
  }

  public String getUnits() {
    return species.getUnits();
  }

  public boolean isSetConstant() {
    return species.isSetConstant();
  }

  public UnitDefinition getUnitsInstance() {
    return species.getUnitsInstance();
  }

  public void setConstant(boolean constant) {
    species.setConstant(constant);
  }

  public void unsetConstant() {
    species.unsetConstant();
  }

  public String getId() {
    return species.getId();
  }

  public boolean isPredefinedUnitsID(String unitsID) {
    return species.isPredefinedUnitsID(unitsID);
  }

  public String getName() {
    return species.getName();
  }

  public boolean isSetId() {
    return species.isSetId();
  }

  public boolean isSetUnits() {
    return species.isSetUnits();
  }

  public boolean isSetName() {
    return species.isSetName();
  }

  public boolean isSetUnitsInstance() {
    return species.isSetUnitsInstance();
  }

  public void setUnits(Kind unitKind) {
    species.setUnits(unitKind);
  }

  public void setUnits(String units) {
    species.setUnits(units);
  }

  public void setId(String id) {
    species.setId(id);
  }

  public boolean getBoundaryCondition() {
    return species.getBoundaryCondition();
  }

  public int getCharge() {
    return species.getCharge();
  }

  public String getCompartment() {
    return species.getCompartment();
  }

  public Compartment getCompartmentInstance() {
    return species.getCompartmentInstance();
  }

  public void setUnits(Unit unit) {
    species.setUnits(unit);
  }

  public String getConversionFactor() {
    return species.getConversionFactor();
  }

  public void setName(String name) {
    species.setName(name);
  }

  public Parameter getConversionFactorInstance() {
    return species.getConversionFactorInstance();
  }

  public UnitDefinition getDerivedUnitDefinition() {
    return species.getDerivedUnitDefinition();
  }

  public void setUnits(UnitDefinition units) {
    species.setUnits(units);
  }

  public String toString() {
    return species.toString();
  }

  public void unsetUnits() {
    species.unsetUnits();
  }

  public void unsetId() {
    species.unsetId();
  }

  public void unsetName() {
    species.unsetName();
  }

  public String getElementName() {
    return species.getElementName();
  }

  public boolean getHasOnlySubstanceUnits() {
    return species.getHasOnlySubstanceUnits();
  }

  public double getInitialAmount() {
    return species.getInitialAmount();
  }

  public double getInitialConcentration() {
    return species.getInitialConcentration();
  }

  public String getPredefinedUnitID() {
    return species.getPredefinedUnitID();
  }

  public String getSpatialSizeUnits() {
    return species.getSpatialSizeUnits();
  }

  public void fireNodeAddedEvent() {
    species.fireNodeAddedEvent();
  }

  public UnitDefinition getSpatialSizeUnitsInstance() {
    return species.getSpatialSizeUnitsInstance();
  }

  public String getSpeciesType() {
    return species.getSpeciesType();
  }

  public SpeciesType getSpeciesTypeInstance() {
    return species.getSpeciesTypeInstance();
  }

  public String getSubstanceUnits() {
    return species.getSubstanceUnits();
  }

  public UnitDefinition getSubstanceUnitsInstance() {
    return species.getSubstanceUnitsInstance();
  }

  public int hashCode() {
    return species.hashCode();
  }

  public int getIndex(TreeNode node) {
    return species.getIndex(node);
  }

  public boolean hasOnlySubstanceUnits() {
    return species.hasOnlySubstanceUnits();
  }

  public List<TreeNodeChangeListener> getListOfTreeNodeChangeListeners() {
    return species.getListOfTreeNodeChangeListeners();
  }

  public void initDefaults() {
    species.initDefaults();
  }

  public void initDefaults(int level, int version) {
    species.initDefaults(level, version);
  }

  public int getNumChildren() {
    return species.getNumChildren();
  }

  public TreeNode getRoot() {
    return species.getRoot();
  }

  public boolean isBoundaryCondition() {
    return species.isBoundaryCondition();
  }

  public boolean isHasOnlySubstanceUnits() {
    return species.isHasOnlySubstanceUnits();
  }

  public int getTreeNodeChangeListenerCount() {
    return species.getTreeNodeChangeListenerCount();
  }

  public boolean isSetBoundaryCondition() {
    return species.isSetBoundaryCondition();
  }

  public boolean isSetCharge() {
    return species.isSetCharge();
  }

  public Object getUserObject(Object key) {
    return species.getUserObject(key);
  }

  public boolean isSetCompartment() {
    return species.isSetCompartment();
  }

  public boolean isSetCompartmentInstance() {
    return species.isSetCompartmentInstance();
  }

  public boolean isSetConversionFactor() {
    return species.isSetConversionFactor();
  }

  public boolean isSetConversionFactorInstance() {
    return species.isSetConversionFactorInstance();
  }

  public boolean isSetHasOnlySubstanceUnits() {
    return species.isSetHasOnlySubstanceUnits();
  }

  public boolean isLeaf() {
    return species.isLeaf();
  }

  public boolean isRoot() {
    return species.isRoot();
  }

  public boolean isSetInitialAmount() {
    return species.isSetInitialAmount();
  }

  public boolean isSetParent() {
    return species.isSetParent();
  }

  public boolean isSetInitialConcentration() {
    return species.isSetInitialConcentration();
  }

  public boolean isSetUserObjects() {
    return species.isSetUserObjects();
  }

  public boolean isSetSpatialSizeUnits() {
    return species.isSetSpatialSizeUnits();
  }

  public boolean isSetSpatialSizeUnitsInstance() {
    return species.isSetSpatialSizeUnitsInstance();
  }

  public void putUserObject(Object key, Object userObject) {
    species.putUserObject(key, userObject);
  }

  public boolean isSetSpeciesType() {
    return species.isSetSpeciesType();
  }

  public boolean isSetSpeciesTypeInstance() {
    return species.isSetSpeciesTypeInstance();
  }

  public boolean isSetSubstanceUnits() {
    return species.isSetSubstanceUnits();
  }

  public void removeAllTreeNodeChangeListeners() {
    species.removeAllTreeNodeChangeListeners();
  }

  public boolean isSetSubstanceUnitsInstance() {
    return species.isSetSubstanceUnitsInstance();
  }

  public boolean removeFromParent() {
    return species.removeFromParent();
  }

  public boolean readAttribute(String attributeName, String prefix, String value) {
    return species.readAttribute(attributeName, prefix, value);
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener) {
    species.removeTreeNodeChangeListener(listener);
  }

  public void setBoundaryCondition(boolean boundaryCondition) {
    species.setBoundaryCondition(boundaryCondition);
  }

  public void removeAllTreeNodeChangeListeners(boolean recursive) {
    species.removeAllTreeNodeChangeListeners(recursive);
  }

  public void setCharge(int charge) {
    species.setCharge(charge);
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    species.removeTreeNodeChangeListener(listener, recursive);
  }

  public void setCompartment(Compartment compartment) {
    species.setCompartment(compartment);
  }

  public Object removeUserObject(Object key) {
    return species.removeUserObject(key);
  }

  public void setCompartment(String compartment) {
    species.setCompartment(compartment);
  }

  public void setParent(TreeNode parent) {
    species.setParent(parent);
  }

  public Set<Object> userObjectKeySet() {
    return species.userObjectKeySet();
  }

  public void setConversionFactor(Parameter conversionFactor) {
    species.setConversionFactor(conversionFactor);
  }

  public void setConversionFactor(String conversionFactorID) {
    species.setConversionFactor(conversionFactorID);
  }

  public void setHasOnlySubstanceUnits(boolean hasOnlySubstanceUnits) {
    species.setHasOnlySubstanceUnits(hasOnlySubstanceUnits);
  }

  public void setInitialAmount(double initialAmount) {
    species.setInitialAmount(initialAmount);
  }

  public void setInitialConcentration(double initialConcentration) {
    species.setInitialConcentration(initialConcentration);
  }

  public void setSpatialSizeUnits(String spatialSizeUnits)
      throws PropertyNotAvailableException, SBMLException {
    species.setSpatialSizeUnits(spatialSizeUnits);
  }

  public void fireNodeRemovedEvent() {
    species.fireNodeRemovedEvent();
  }

  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    species.firePropertyChange(propertyName, oldValue, newValue);
  }

  public boolean getAllowsChildren() {
    return species.getAllowsChildren();
  }

  public void setSpatialSizeUnits(UnitDefinition spatialSizeUnits)
      throws PropertyNotAvailableException, SBMLException {
    species.setSpatialSizeUnits(spatialSizeUnits);
  }

  public Annotation getAnnotation() {
    return species.getAnnotation();
  }

  public String getAnnotationString() throws XMLStreamException {
    return species.getAnnotationString();
  }

  public void setSpeciesType(SpeciesType speciesType) {
    species.setSpeciesType(speciesType);
  }

  public TreeNode getChildAt(int childIndex) {
    return species.getChildAt(childIndex);
  }

  public void setSpeciesType(String speciesType) {
    species.setSpeciesType(speciesType);
  }

  public void setSubstanceUnits(String unit) {
    species.setSubstanceUnits(unit);
  }

  public void setSubstanceUnits(Unit unit) {
    species.setSubstanceUnits(unit);
  }

  public int getChildCount() {
    return species.getChildCount();
  }

  public void setSubstanceUnits(Kind unitKind) {
    species.setSubstanceUnits(unitKind);
  }

  public void setSubstanceUnits(UnitDefinition units) {
    species.setSubstanceUnits(units);
  }

  public CVTerm getCVTerm(int index) {
    return species.getCVTerm(index);
  }

  public void unsetCharge() {
    species.unsetCharge();
  }

  public void unsetCompartment() {
    species.unsetCompartment();
  }

  public int getCVTermCount() {
    return species.getCVTermCount();
  }

  public void unsetConversionFactor() {
    species.unsetConversionFactor();
  }

  public List<CVTerm> getCVTerms() {
    return species.getCVTerms();
  }

  public void unsetInitialAmount() {
    species.unsetInitialAmount();
  }

  public Map<String, String> getDeclaredNamespaces() {
    return species.getDeclaredNamespaces();
  }

  public void unsetInitialConcentration() {
    species.unsetInitialConcentration();
  }

  public void unsetSpatialSizeUnits() {
    species.unsetSpatialSizeUnits();
  }

  public void unsetSubstanceUnits() {
    species.unsetSubstanceUnits();
  }

  public SBasePlugin getExtension(String nameOrUri) {
    return species.getExtension(nameOrUri);
  }

  public Map<String, String> writeXMLAttributes() {
    return species.writeXMLAttributes();
  }

  public int getExtensionCount() {
    return species.getExtensionCount();
  }

  public Map<String, SBasePlugin> getExtensionPackages() {
    return species.getExtensionPackages();
  }

  public History getHistory() {
    return species.getHistory();
  }

  public int getLevel() {
    return species.getLevel();
  }

  public ValuePair<Integer, Integer> getLevelAndVersion() {
    return species.getLevelAndVersion();
  }

  public String getMetaId() {
    return species.getMetaId();
  }

  public Model getModel() {
    return species.getModel();
  }

  public String getNamespace() {
    return species.getNamespace();
  }

  public XMLNode getNotes() {
    return species.getNotes();
  }

  public String getNotesString() throws XMLStreamException {
    return species.getNotesString();
  }

  public int getNumCVTerms() {
    return species.getNumCVTerms();
  }

  public int getNumPlugins() {
    return species.getNumPlugins();
  }

  public String getPackageName() {
    return species.getPackageName();
  }

  public int getPackageVersion() {
    return species.getPackageVersion();
  }

  public SBase getParent() {
    return species.getParent();
  }

  public SBase getParentSBMLObject() {
    return species.getParentSBMLObject();
  }

  public SBasePlugin getPlugin(String nameOrUri) {
    return species.getPlugin(nameOrUri);
  }

  public SBMLDocument getSBMLDocument() {
    return species.getSBMLDocument();
  }

  public int getSBOTerm() {
    return species.getSBOTerm();
  }

  public String getSBOTermID() {
    return species.getSBOTermID();
  }

  public String getURI() {
    return species.getURI();
  }

  public int getVersion() {
    return species.getVersion();
  }

  public boolean hasValidAnnotation() {
    return species.hasValidAnnotation();
  }

  public boolean hasValidLevelVersionNamespaceCombination() {
    return species.hasValidLevelVersionNamespaceCombination();
  }

  public boolean isExtendedByOtherPackages() {
    return species.isExtendedByOtherPackages();
  }

  public boolean isPackageEnabled(String packageURIOrName) {
    return species.isPackageEnabled(packageURIOrName);
  }

  public boolean isPackageURIEnabled(String packageURIOrName) {
    return species.isPackageURIEnabled(packageURIOrName);
  }

  public boolean isPkgEnabled(String packageURIOrName) {
    return species.isPkgEnabled(packageURIOrName);
  }

  public boolean isPkgURIEnabled(String packageURIOrName) {
    return species.isPkgURIEnabled(packageURIOrName);
  }

  public boolean isSetAnnotation() {
    return species.isSetAnnotation();
  }

  public boolean isSetHistory() {
    return species.isSetHistory();
  }

  public boolean isSetLevel() {
    return species.isSetLevel();
  }

  public boolean isSetLevelAndVersion() {
    return species.isSetLevelAndVersion();
  }

  public boolean isSetMetaId() {
    return species.isSetMetaId();
  }

  public boolean isSetNotes() {
    return species.isSetNotes();
  }

  public boolean isSetParentSBMLObject() {
    return species.isSetParentSBMLObject();
  }

  public boolean isSetPlugin(String nameOrUri) {
    return species.isSetPlugin(nameOrUri);
  }

  public boolean isSetSBOTerm() {
    return species.isSetSBOTerm();
  }

  public boolean isSetVersion() {
    return species.isSetVersion();
  }

  public boolean registerChild(SBase sbase) throws LevelVersionError {
    return species.registerChild(sbase);
  }

  public boolean removeCVTerm(CVTerm cvTerm) {
    return species.removeCVTerm(cvTerm);
  }

  public CVTerm removeCVTerm(int index) {
    return species.removeCVTerm(index);
  }

  public void setAnnotation(Annotation annotation) {
    species.setAnnotation(annotation);
  }

  public void setAnnotation(String nonRDFAnnotation) throws XMLStreamException {
    species.setAnnotation(nonRDFAnnotation);
  }

  public void setAnnotation(XMLNode nonRDFAnnotation) {
    species.setAnnotation(nonRDFAnnotation);
  }

  public void setHistory(History history) {
    species.setHistory(history);
  }

  public void setLevel(int level) {
    species.setLevel(level);
  }

  public void setMetaId(String metaId) {
    species.setMetaId(metaId);
  }

  public void setNamespace(String namespace) {
    species.setNamespace(namespace);
  }

  public void setNotes(String notes) throws XMLStreamException {
    species.setNotes(notes);
  }

  public void setNotes(XMLNode notes) {
    species.setNotes(notes);
  }

  public void setPackageVersion(int packageVersion) {
    species.setPackageVersion(packageVersion);
  }

  public void setSBOTerm(int term) {
    species.setSBOTerm(term);
  }

  public void setSBOTerm(String sboid) {
    species.setSBOTerm(sboid);
  }

  public void setThisAsParentSBMLObject(SBase sbase) throws LevelVersionError {
    species.setThisAsParentSBMLObject(sbase);
  }

  public void setVersion(int version) {
    species.setVersion(version);
  }

  public void unregisterChild(SBase sbase) {
    species.unregisterChild(sbase);
  }

  public void unsetAnnotation() {
    species.unsetAnnotation();
  }

  public void unsetCVTerms() {
    species.unsetCVTerms();
  }

  public void unsetExtension(String nameOrUri) {
    species.unsetExtension(nameOrUri);
  }

  public void unsetHistory() {
    species.unsetHistory();
  }

  public void unsetMetaId() {
    species.unsetMetaId();
  }

  public void unsetNamespace() {
    species.unsetNamespace();
  }

  public void unsetNotes() {
    species.unsetNotes();
  }

  public void unsetPlugin(String nameOrUri) {
    species.unsetPlugin(nameOrUri);
  }

  public void unsetSBOTerm() {
    species.unsetSBOTerm();
  }
  
  // replacement methods
  public void initLevelAndVersion() {
    SBMLReplacement.initLevelAndVersion(species);
  }
  
  public void replace(Species speciesToReplace) {
    SBMLReplacement.replace(speciesToReplace, species);
  }
  
  List<ConformityMessage> getInvalidSettings(SBMLDocument document, String prefix, PMFDocument pmf) {
    return SBMLReplacement.getInvalidSettings(document, prefix, pmf);
  }

}
