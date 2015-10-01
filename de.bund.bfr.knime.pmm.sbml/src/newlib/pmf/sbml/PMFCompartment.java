/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
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
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.CompartmentType;
import org.sbml.jsbml.History;
import org.sbml.jsbml.LevelVersionError;
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

/**
 * Wrapper for PMF compartments (Specification 6 SBML component compartments).
 * 
 * @author Arvid Heise
 * @author Miguel de Alba
 */
public class PMFCompartment {

  Compartment compartment;

  public PMFCompartment() {
    compartment = new Compartment();
  }

  public PMFCompartment(Compartment compartment) {
    this.compartment = new Compartment(compartment);
    initCompartment();
  }

  public PMFCompartment(int level, int version) {
    compartment = new Compartment(level, version);
    initCompartment();
  }

  public PMFCompartment(String id) {
    compartment = new Compartment(id);
    initCompartment();
  }

  public PMFCompartment(String id, int level, int version) {
    compartment = new Compartment(id, level, version);
    initCompartment();
  }

  public PMFCompartment(String id, String name, int level, int version) {
    compartment = new Compartment(id, name, level, version);
    initCompartment();
  }
  
  private void initCompartment() {
    initLevelAndVersion();
    this.compartment.setSpatialDimensions(3);
    this.compartment.setUnits(Kind.DIMENSIONLESS);
  }

  // Compartment methods
  public double getValue() {
    return compartment.getValue();
  }

  public boolean isSetValue() {
    return compartment.isSetValue();
  }

  public Compartment clone() {
    return compartment.clone();
  }

  public boolean containsUndeclaredUnits() {
    return compartment.containsUndeclaredUnits();
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners) {
    return compartment.addAllChangeListeners(listeners);
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners,
      boolean recursive) {
    return compartment.addAllChangeListeners(listeners, recursive);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener) {
    compartment.addTreeNodeChangeListener(listener);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    compartment.addTreeNodeChangeListener(listener, recursive);
  }

  public boolean addCVTerm(CVTerm term) {
    return compartment.addCVTerm(term);
  }

  public Enumeration<TreeNode> children() {
    return compartment.children();
  }

  public void addDeclaredNamespace(String prefix, String namespace) {
    compartment.addDeclaredNamespace(prefix, namespace);
  }

  public void addExtension(String nameOrUri, SBasePlugin sbasePlugin) {
    compartment.addExtension(nameOrUri, sbasePlugin);
  }

  public void clearUserObjects() {
    compartment.clearUserObjects();
  }

  public boolean containsUserObjectKey(Object key) {
    return compartment.containsUserObjectKey(key);
  }

  public void addPlugin(String nameOrUri, SBasePlugin sbasePlugin) {
    compartment.addPlugin(nameOrUri, sbasePlugin);
  }

  public void appendAnnotation(String annotation) throws XMLStreamException {
    compartment.appendAnnotation(annotation);
  }

  public void appendAnnotation(XMLNode annotation) {
    compartment.appendAnnotation(annotation);
  }

  public List<? extends TreeNode> filter(Filter filter) {
    return compartment.filter(filter);
  }

  public void appendNotes(String notes) throws XMLStreamException {
    compartment.appendNotes(notes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes) {
    return compartment.filter(filter, retainInternalNodes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes,
      boolean prune) {
    return compartment.filter(filter, retainInternalNodes, prune);
  }

  public void appendNotes(XMLNode notes) {
    compartment.appendNotes(notes);
  }

  public History createHistory() {
    return compartment.createHistory();
  }

  public SBasePlugin createPlugin(String nameOrUri) {
    return compartment.createPlugin(nameOrUri);
  }

  public void disablePackage(String packageURIOrName) {
    compartment.disablePackage(packageURIOrName);
  }

  public void enablePackage(String packageURIOrName) {
    compartment.enablePackage(packageURIOrName);
  }

  public void enablePackage(String packageURIOrName, boolean enabled) {
    compartment.enablePackage(packageURIOrName, enabled);
  }

  public List<String> filterCVTerms(Qualifier arg0, boolean arg1, String... arg2) {
    return compartment.filterCVTerms(arg0, arg1, arg2);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern, boolean recursive) {
    return compartment.filterCVTerms(qualifier, pattern, recursive);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern) {
    return compartment.filterCVTerms(qualifier, pattern);
  }

  public List<CVTerm> filterCVTerms(Qualifier qualifier) {
    return compartment.filterCVTerms(qualifier);
  }

  public boolean isIdMandatory() {
    return compartment.isIdMandatory();
  }

  public boolean getConstant() {
    return compartment.getConstant();
  }

  public String getDerivedUnits() {
    return compartment.getDerivedUnits();
  }

  public boolean isConstant() {
    return compartment.isConstant();
  }

  public void unsetValue() {
    compartment.unsetValue();
  }

  public boolean isSetConstant() {
    return compartment.isSetConstant();
  }

  public void setConstant(boolean constant) {
    compartment.setConstant(constant);
  }

  public void unsetConstant() {
    compartment.unsetConstant();
  }

  public String getId() {
    return compartment.getId();
  }

  public boolean isPredefinedUnitsID(String unitsID) {
    return compartment.isPredefinedUnitsID(unitsID);
  }

  public String getName() {
    return compartment.getName();
  }

  public boolean isSetId() {
    return compartment.isSetId();
  }

  public boolean isSetUnits() {
    return compartment.isSetUnits();
  }

  public boolean isSetName() {
    return compartment.isSetName();
  }

  public boolean isSetUnitsInstance() {
    return compartment.isSetUnitsInstance();
  }

  public String getCompartmentType() {
    return compartment.getCompartmentType();
  }

  public CompartmentType getCompartmentTypeInstance() {
    return compartment.getCompartmentTypeInstance();
  }

  public void setId(String id) {
    compartment.setId(id);
  }

  public UnitDefinition getDerivedUnitDefinition() {
    return compartment.getDerivedUnitDefinition();
  }

  public void setName(String name) {
    compartment.setName(name);
  }

  public String getOutside() {
    return compartment.getOutside();
  }

  public Compartment getOutsideInstance() {
    return compartment.getOutsideInstance();
  }

  public String getPredefinedUnitID() {
    return compartment.getPredefinedUnitID();
  }

  public String toString() {
    return compartment.toString();
  }

  public void unsetUnits() {
    compartment.unsetUnits();
  }

  public void unsetId() {
    compartment.unsetId();
  }

  public double getSize() {
    return compartment.getSize();
  }

  public void unsetName() {
    compartment.unsetName();
  }

  public double getSpatialDimensions() {
    return compartment.getSpatialDimensions();
  }

  public double getSpatialDimensionsAsDouble() {
    return compartment.getSpatialDimensionsAsDouble();
  }

  public String getUnits() {
    return compartment.getUnits();
  }

  public UnitDefinition getUnitsInstance() {
    return compartment.getUnitsInstance();
  }

  public double getVolume() {
    return compartment.getVolume();
  }

  public void fireNodeAddedEvent() {
    compartment.fireNodeAddedEvent();
  }

  public int hashCode() {
    return compartment.hashCode();
  }

  public void initDefaults() {
    compartment.initDefaults();
  }

  public void initDefaults(int level, int version) {
    compartment.initDefaults(level, version);
  }

  public boolean isSetCompartmentType() {
    return compartment.isSetCompartmentType();
  }

  public boolean isSetCompartmentTypeInstance() {
    return compartment.isSetCompartmentTypeInstance();
  }

  public boolean isSetOutside() {
    return compartment.isSetOutside();
  }

  public boolean isSetOutsideInstance() {
    return compartment.isSetOutsideInstance();
  }

  public boolean isSetSize() {
    return compartment.isSetSize();
  }

  public boolean isSetSpatialDimensions() {
    return compartment.isSetSpatialDimensions();
  }

  public int getIndex(TreeNode node) {
    return compartment.getIndex(node);
  }

  public List<TreeNodeChangeListener> getListOfTreeNodeChangeListeners() {
    return compartment.getListOfTreeNodeChangeListeners();
  }

  public boolean isSetVolume() {
    return compartment.isSetVolume();
  }

  public int getNumChildren() {
    return compartment.getNumChildren();
  }

  public TreeNode getRoot() {
    return compartment.getRoot();
  }

  public int getTreeNodeChangeListenerCount() {
    return compartment.getTreeNodeChangeListenerCount();
  }

  public Object getUserObject(Object key) {
    return compartment.getUserObject(key);
  }

  public boolean readAttribute(String attributeName, String prefix, String value) {
    return compartment.readAttribute(attributeName, prefix, value);
  }

  public boolean isLeaf() {
    return compartment.isLeaf();
  }

  public boolean isRoot() {
    return compartment.isRoot();
  }

  public boolean isSetParent() {
    return compartment.isSetParent();
  }

  public boolean isSetUserObjects() {
    return compartment.isSetUserObjects();
  }

  public void setCompartmentType(CompartmentType compartmentType) {
    compartment.setCompartmentType(compartmentType);
  }

  public void setCompartmentType(String compartmentTypeID) {
    compartment.setCompartmentType(compartmentTypeID);
  }

  public void putUserObject(Object key, Object userObject) {
    compartment.putUserObject(key, userObject);
  }

  public void setOutside(Compartment outside) {
    compartment.setOutside(outside);
  }

  public void removeAllTreeNodeChangeListeners() {
    compartment.removeAllTreeNodeChangeListeners();
  }

  public void setOutside(String outside) {
    compartment.setOutside(outside);
  }

  public boolean removeFromParent() {
    return compartment.removeFromParent();
  }

  public void setSize(double size) {
    compartment.setSize(size);
  }

  public void setSpatialDimensions(double spatialDimension) {
    compartment.setSpatialDimensions(spatialDimension);
  }

  public void setSpatialDimensions(int spatialDimensions) {
    compartment.setSpatialDimensions(spatialDimensions);
  }

  public void setSpatialDimensions(short spatialDimensions) {
    compartment.setSpatialDimensions(spatialDimensions);
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener) {
    compartment.removeTreeNodeChangeListener(listener);
  }

  public void setUnits(String units) {
    compartment.setUnits(units);
  }

  public void removeAllTreeNodeChangeListeners(boolean recursive) {
    compartment.removeAllTreeNodeChangeListeners(recursive);
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    compartment.removeTreeNodeChangeListener(listener, recursive);
  }

  public void setUnits(Unit unit) {
    compartment.setUnits(unit);
  }

  public Object removeUserObject(Object key) {
    return compartment.removeUserObject(key);
  }

  public void setParent(TreeNode parent) {
    compartment.setParent(parent);
  }

  public void setUnits(Kind unitKind) {
    compartment.setUnits(unitKind);
  }

  public void setUnits(UnitDefinition unitDefinition) {
    compartment.setUnits(unitDefinition);
  }

  public Set<Object> userObjectKeySet() {
    return compartment.userObjectKeySet();
  }

  public void setValue(double value) {
    compartment.setValue(value);
  }

  public void setVolume(double value) {
    compartment.setVolume(value);
  }

  public void unsetCompartmentType() {
    compartment.unsetCompartmentType();
  }

  public void unsetOutside() {
    compartment.unsetOutside();
  }

  public void unsetSize() {
    compartment.unsetSize();
  }

  public void fireNodeRemovedEvent() {
    compartment.fireNodeRemovedEvent();
  }

  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    compartment.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void unsetSpatialDimensions() {
    compartment.unsetSpatialDimensions();
  }

  public void unsetVolume() {
    compartment.unsetVolume();
  }

  public boolean getAllowsChildren() {
    return compartment.getAllowsChildren();
  }

  public Annotation getAnnotation() {
    return compartment.getAnnotation();
  }

  public String getAnnotationString() throws XMLStreamException {
    return compartment.getAnnotationString();
  }

  public Map<String, String> writeXMLAttributes() {
    return compartment.writeXMLAttributes();
  }

  public TreeNode getChildAt(int childIndex) {
    return compartment.getChildAt(childIndex);
  }

  public int getChildCount() {
    return compartment.getChildCount();
  }

  public CVTerm getCVTerm(int index) {
    return compartment.getCVTerm(index);
  }

  public int getCVTermCount() {
    return compartment.getCVTermCount();
  }

  public List<CVTerm> getCVTerms() {
    return compartment.getCVTerms();
  }

  public Map<String, String> getDeclaredNamespaces() {
    return compartment.getDeclaredNamespaces();
  }

  public String getElementName() {
    return compartment.getElementName();
  }

  public SBasePlugin getExtension(String nameOrUri) {
    return compartment.getExtension(nameOrUri);
  }

  public int getExtensionCount() {
    return compartment.getExtensionCount();
  }

  public Map<String, SBasePlugin> getExtensionPackages() {
    return compartment.getExtensionPackages();
  }

  public History getHistory() {
    return compartment.getHistory();
  }

  public int getLevel() {
    return compartment.getLevel();
  }

  public ValuePair<Integer, Integer> getLevelAndVersion() {
    return compartment.getLevelAndVersion();
  }

  public String getMetaId() {
    return compartment.getMetaId();
  }

  public Model getModel() {
    return compartment.getModel();
  }

  public String getNamespace() {
    return compartment.getNamespace();
  }

  public XMLNode getNotes() {
    return compartment.getNotes();
  }

  public String getNotesString() throws XMLStreamException {
    return compartment.getNotesString();
  }

  public int getNumCVTerms() {
    return compartment.getNumCVTerms();
  }

  public int getNumPlugins() {
    return compartment.getNumPlugins();
  }

  public String getPackageName() {
    return compartment.getPackageName();
  }

  public int getPackageVersion() {
    return compartment.getPackageVersion();
  }

  public SBase getParent() {
    return compartment.getParent();
  }

  public SBase getParentSBMLObject() {
    return compartment.getParentSBMLObject();
  }

  public SBasePlugin getPlugin(String nameOrUri) {
    return compartment.getPlugin(nameOrUri);
  }

  public SBMLDocument getSBMLDocument() {
    return compartment.getSBMLDocument();
  }

  public int getSBOTerm() {
    return compartment.getSBOTerm();
  }

  public String getSBOTermID() {
    return compartment.getSBOTermID();
  }

  public String getURI() {
    return compartment.getURI();
  }

  public int getVersion() {
    return compartment.getVersion();
  }

  public boolean hasValidAnnotation() {
    return compartment.hasValidAnnotation();
  }

  public boolean hasValidLevelVersionNamespaceCombination() {
    return compartment.hasValidLevelVersionNamespaceCombination();
  }

  public boolean isExtendedByOtherPackages() {
    return compartment.isExtendedByOtherPackages();
  }

  public boolean isPackageEnabled(String packageURIOrName) {
    return compartment.isPackageEnabled(packageURIOrName);
  }

  public boolean isPackageURIEnabled(String packageURIOrName) {
    return compartment.isPackageURIEnabled(packageURIOrName);
  }

  public boolean isPkgEnabled(String packageURIOrName) {
    return compartment.isPkgEnabled(packageURIOrName);
  }

  public boolean isPkgURIEnabled(String packageURIOrName) {
    return compartment.isPkgURIEnabled(packageURIOrName);
  }

  public boolean isSetAnnotation() {
    return compartment.isSetAnnotation();
  }

  public boolean isSetHistory() {
    return compartment.isSetHistory();
  }

  public boolean isSetLevel() {
    return compartment.isSetLevel();
  }

  public boolean isSetLevelAndVersion() {
    return compartment.isSetLevelAndVersion();
  }

  public boolean isSetMetaId() {
    return compartment.isSetMetaId();
  }

  public boolean isSetNotes() {
    return compartment.isSetNotes();
  }

  public boolean isSetParentSBMLObject() {
    return compartment.isSetParentSBMLObject();
  }

  public boolean isSetPlugin(String nameOrUri) {
    return compartment.isSetPlugin(nameOrUri);
  }

  public boolean isSetSBOTerm() {
    return compartment.isSetSBOTerm();
  }

  public boolean isSetVersion() {
    return compartment.isSetVersion();
  }

  public boolean registerChild(SBase sbase) throws LevelVersionError {
    return compartment.registerChild(sbase);
  }

  public boolean removeCVTerm(CVTerm cvTerm) {
    return compartment.removeCVTerm(cvTerm);
  }

  public CVTerm removeCVTerm(int index) {
    return compartment.removeCVTerm(index);
  }

  public void setAnnotation(Annotation annotation) {
    compartment.setAnnotation(annotation);
  }

  public void setAnnotation(String nonRDFAnnotation) throws XMLStreamException {
    compartment.setAnnotation(nonRDFAnnotation);
  }

  public void setAnnotation(XMLNode nonRDFAnnotation) {
    compartment.setAnnotation(nonRDFAnnotation);
  }

  public void setHistory(History history) {
    compartment.setHistory(history);
  }

  public void setLevel(int level) {
    compartment.setLevel(level);
  }

  public void setMetaId(String metaId) {
    compartment.setMetaId(metaId);
  }

  public void setNamespace(String namespace) {
    compartment.setNamespace(namespace);
  }

  public void setNotes(String notes) throws XMLStreamException {
    compartment.setNotes(notes);
  }

  public void setNotes(XMLNode notes) {
    compartment.setNotes(notes);
  }

  public void setPackageVersion(int packageVersion) {
    compartment.setPackageVersion(packageVersion);
  }

  public void setSBOTerm(int term) {
    compartment.setSBOTerm(term);
  }

  public void setSBOTerm(String sboid) {
    compartment.setSBOTerm(sboid);
  }

  public void setThisAsParentSBMLObject(SBase sbase) throws LevelVersionError {
    compartment.setThisAsParentSBMLObject(sbase);
  }

  public void setVersion(int version) {
    compartment.setVersion(version);
  }

  public void unregisterChild(SBase sbase) {
    compartment.unregisterChild(sbase);
  }

  public void unsetAnnotation() {
    compartment.unsetAnnotation();
  }

  public void unsetCVTerms() {
    compartment.unsetCVTerms();
  }

  public void unsetExtension(String nameOrUri) {
    compartment.unsetExtension(nameOrUri);
  }

  public void unsetHistory() {
    compartment.unsetHistory();
  }

  public void unsetMetaId() {
    compartment.unsetMetaId();
  }

  public void unsetNamespace() {
    compartment.unsetNamespace();
  }

  public void unsetNotes() {
    compartment.unsetNotes();
  }

  public void unsetPlugin(String nameOrUri) {
    compartment.unsetPlugin(nameOrUri);
  }

  public void unsetSBOTerm() {
    compartment.unsetSBOTerm();
  }

  // replacement methods
  public void initLevelAndVersion() {
    SBMLReplacement.initLevelAndVersion(compartment);
  }

  public void replace(Compartment compartmentToReplace) {
    SBMLReplacement.replace(compartmentToReplace, compartment);
  }

  public List<ConformityMessage> getInvalidSettings(SBMLDocument document, String prefix,
      PMFDocument pmf) {
    return SBMLReplacement.getInvalidSettings(document, prefix, pmf);
  }
}
