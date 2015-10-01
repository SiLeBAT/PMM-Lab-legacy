/*******************************************************************************
  public void replace(SBase sbmlElement) {
    SBMLReplacement.replace(sbmlElement, (SBase) this);
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

import java.io.StringWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.tree.TreeNode;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.dmg.pmml.PredictiveModelQuality;
import org.sbml.jsbml.AlgebraicRule;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.CVTerm.Qualifier;
import org.sbml.jsbml.CallableSBase;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.CompartmentType;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.Delay;
import org.sbml.jsbml.Event;
import org.sbml.jsbml.EventAssignment;
import org.sbml.jsbml.ExplicitRule;
import org.sbml.jsbml.FunctionDefinition;
import org.sbml.jsbml.History;
import org.sbml.jsbml.InitialAssignment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.LevelVersionError;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.NamedSBase;
import org.sbml.jsbml.NamedSBaseWithDerivedUnit;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Quantity;
import org.sbml.jsbml.QuantityWithUnit;
import org.sbml.jsbml.RateRule;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.SimpleSpeciesReference;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.SpeciesType;
import org.sbml.jsbml.Symbol;
import org.sbml.jsbml.Trigger;
import org.sbml.jsbml.UniqueNamedSBase;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.Unit.Kind;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.Variable;
import org.sbml.jsbml.ext.SBasePlugin;
import org.sbml.jsbml.util.TreeNodeChangeListener;
import org.sbml.jsbml.util.ValuePair;
import org.sbml.jsbml.util.filters.Filter;
import org.sbml.jsbml.xml.XMLNode;
import org.xml.sax.InputSource;

import newlib.numl.ConformityMessage;
import newlib.pmf.PMFDocument;
import newlib.pmf.PMFUtil;

/**
 * Wrapper for PMF model.
 * <ul>
 * <li>Specification 5 - PMF namespace.
 * <li>Specification 11 Annotation of metadata.
 * <li>Specification 12 Annotation of uncertainties.
 * </ul>
 * 
 * @author Arvid Heise
 * @author Miguel Alba
 */
public class PMFModel {

  Model model;

  public PMFModel() {
    model = new Model();
    initLevelAndVersion();
  }

  public PMFModel(int level, int version) {
    model = new Model(level, version);
  }

  public PMFModel(Model model) {
    this.model = model;
    SBMLReplacement.initLevelAndVersion(this.model);
  }

  public PMFModel(String id) {
    model = new Model(id);
    SBMLReplacement.initLevelAndVersion(this.model);
  }

  public PMFModel(String id, int level, int version) {
    model = new Model(id, level, version);
  }

  // model methods
  public String getId() {
    return model.getId();
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners) {
    return model.addAllChangeListeners(listeners);
  }

  public boolean isSetId() {
    return model.isSetId();
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners,
      boolean recursive) {
    return model.addAllChangeListeners(listeners, recursive);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener) {
    model.addTreeNodeChangeListener(listener);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    model.addTreeNodeChangeListener(listener, recursive);
  }

  public boolean addCVTerm(CVTerm term) {
    return model.addCVTerm(term);
  }

  public Enumeration<TreeNode> children() {
    return model.children();
  }

  public void addDeclaredNamespace(String prefix, String namespace) {
    model.addDeclaredNamespace(prefix, namespace);
  }

  public boolean accept(SBase sbase) {
    return model.accept(sbase);
  }

  public void addExtension(String nameOrUri, SBasePlugin sbasePlugin) {
    model.addExtension(nameOrUri, sbasePlugin);
  }

  public void clearUserObjects() {
    model.clearUserObjects();
  }

  public boolean addCompartment(Compartment compartment) {
    return model.addCompartment(compartment);
  }

  public boolean containsUserObjectKey(Object key) {
    return model.containsUserObjectKey(key);
  }

  public boolean addCompartmentType(CompartmentType compartmentType) {
    return model.addCompartmentType(compartmentType);
  }

  public boolean addConstraint(Constraint constraint) {
    return model.addConstraint(constraint);
  }

  public void addPlugin(String nameOrUri, SBasePlugin sbasePlugin) {
    model.addPlugin(nameOrUri, sbasePlugin);
  }

  public void appendAnnotation(String annotation) throws XMLStreamException {
    model.appendAnnotation(annotation);
  }

  public boolean addEvent(Event event) {
    return model.addEvent(event);
  }

  public void appendAnnotation(XMLNode annotation) {
    model.appendAnnotation(annotation);
  }

  public List<? extends TreeNode> filter(Filter filter) {
    return model.filter(filter);
  }

  public void appendNotes(String notes) throws XMLStreamException {
    model.appendNotes(notes);
  }

  public boolean addFunctionDefinition(FunctionDefinition functionDefinition) {
    return model.addFunctionDefinition(functionDefinition);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes) {
    return model.filter(filter, retainInternalNodes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes,
      boolean prune) {
    return model.filter(filter, retainInternalNodes, prune);
  }

  public void appendNotes(XMLNode notes) {
    model.appendNotes(notes);
  }

  public boolean addInitialAssignment(InitialAssignment initialAssignment) {
    return model.addInitialAssignment(initialAssignment);
  }

  public boolean addParameter(Parameter parameter) {
    return model.addParameter(parameter);
  }

  public List<String> filterCVTerms(Qualifier arg0, boolean arg1, String... arg2) {
    return model.filterCVTerms(arg0, arg1, arg2);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern, boolean recursive) {
    return model.filterCVTerms(qualifier, pattern, recursive);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern) {
    return model.filterCVTerms(qualifier, pattern);
  }

  public List<CVTerm> filterCVTerms(Qualifier qualifier) {
    return model.filterCVTerms(qualifier);
  }

  public String getName() {
    return model.getName();
  }

  public boolean isSetName() {
    return model.isSetName();
  }

  public void setId(String id) {
    model.setId(id);
  }

  public void setName(String name) {
    model.setName(name);
  }

  public String toString() {
    return model.toString();
  }

  public void unsetId() {
    model.unsetId();
  }

  public void unsetName() {
    model.unsetName();
  }

  public void fireNodeAddedEvent() {
    model.fireNodeAddedEvent();
  }

  public int getIndex(TreeNode node) {
    return model.getIndex(node);
  }

  public List<TreeNodeChangeListener> getListOfTreeNodeChangeListeners() {
    return model.getListOfTreeNodeChangeListeners();
  }

  public int getNumChildren() {
    return model.getNumChildren();
  }

  public TreeNode getRoot() {
    return model.getRoot();
  }

  public boolean addReaction(Reaction reaction) {
    return model.addReaction(reaction);
  }

  public int getTreeNodeChangeListenerCount() {
    return model.getTreeNodeChangeListenerCount();
  }

  public boolean addRule(Rule rule) {
    return model.addRule(rule);
  }

  public Object getUserObject(Object key) {
    return model.getUserObject(key);
  }

  public boolean addSpecies(Species spec) {
    return model.addSpecies(spec);
  }

  public boolean addSpeciesType(SpeciesType speciesType) {
    return model.addSpeciesType(speciesType);
  }

  public boolean addUnitDefinition(UnitDefinition unitDefinition) {
    return model.addUnitDefinition(unitDefinition);
  }

  public boolean isLeaf() {
    return model.isLeaf();
  }

  public String addUnitDefinitionOrReturnIdenticalUnit(UnitDefinition units) {
    return model.addUnitDefinitionOrReturnIdenticalUnit(units);
  }

  public boolean isRoot() {
    return model.isRoot();
  }

  public boolean isSetParent() {
    return model.isSetParent();
  }

  public boolean isSetUserObjects() {
    return model.isSetUserObjects();
  }

  public Model clone() {
    return model.clone();
  }

  public void putUserObject(Object key, Object userObject) {
    model.putUserObject(key, userObject);
  }

  public boolean containsCompartment(String id) {
    return model.containsCompartment(id);
  }

  public boolean containsFunctionDefinition(String id) {
    return model.containsFunctionDefinition(id);
  }

  public void removeAllTreeNodeChangeListeners() {
    model.removeAllTreeNodeChangeListeners();
  }

  public boolean containsParameter(String id) {
    return model.containsParameter(id);
  }

  public boolean removeFromParent() {
    return model.removeFromParent();
  }

  public boolean containsQuantity(Quantity quantity) {
    return model.containsQuantity(quantity);
  }

  public boolean containsReaction(String id) {
    return model.containsReaction(id);
  }

  public boolean containsSpecies(String id) {
    return model.containsSpecies(id);
  }

  public boolean containsUniqueNamedSBase(String id) {
    return model.containsUniqueNamedSBase(id);
  }

  public boolean containsUnitDefinition(String units) {
    return model.containsUnitDefinition(units);
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener) {
    model.removeTreeNodeChangeListener(listener);
  }

  public AlgebraicRule createAlgebraicRule() {
    return model.createAlgebraicRule();
  }

  public void removeAllTreeNodeChangeListeners(boolean recursive) {
    model.removeAllTreeNodeChangeListeners(recursive);
  }

  public History createHistory() {
    return model.createHistory();
  }

  public AssignmentRule createAssignmentRule() {
    return model.createAssignmentRule();
  }

  public SBasePlugin createPlugin(String nameOrUri) {
    return model.createPlugin(nameOrUri);
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    model.removeTreeNodeChangeListener(listener, recursive);
  }

  public Compartment createCompartment() {
    return model.createCompartment();
  }

  public Compartment createCompartment(String id) {
    return model.createCompartment(id);
  }

  public void disablePackage(String packageURIOrName) {
    model.disablePackage(packageURIOrName);
  }

  public Object removeUserObject(Object key) {
    return model.removeUserObject(key);
  }

  public CompartmentType createCompartmentType() {
    return model.createCompartmentType();
  }

  public void enablePackage(String packageURIOrName) {
    model.enablePackage(packageURIOrName);
  }

  public void setParent(TreeNode parent) {
    model.setParent(parent);
  }

  public void enablePackage(String packageURIOrName, boolean enabled) {
    model.enablePackage(packageURIOrName, enabled);
  }

  public CompartmentType createCompartmentType(String id) {
    return model.createCompartmentType(id);
  }

  public Set<Object> userObjectKeySet() {
    return model.userObjectKeySet();
  }

  public Constraint createConstraint() {
    return model.createConstraint();
  }

  public Delay createDelay() {
    return model.createDelay();
  }

  public Event createEvent() {
    return model.createEvent();
  }

  public Event createEvent(String id) {
    return model.createEvent(id);
  }

  public EventAssignment createEventAssignment() {
    return model.createEventAssignment();
  }

  public FunctionDefinition createFunctionDefinition() {
    return model.createFunctionDefinition();
  }

  public void fireNodeRemovedEvent() {
    model.fireNodeRemovedEvent();
  }

  public FunctionDefinition createFunctionDefinition(String id) {
    return model.createFunctionDefinition(id);
  }

  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    model.firePropertyChange(propertyName, oldValue, newValue);
  }

  public InitialAssignment createInitialAssignment() {
    return model.createInitialAssignment();
  }

  public KineticLaw createKineticLaw() {
    return model.createKineticLaw();
  }

  public Annotation getAnnotation() {
    return model.getAnnotation();
  }

  public LocalParameter createKineticLawParameter() {
    return model.createKineticLawParameter();
  }

  public String getAnnotationString() throws XMLStreamException {
    return model.getAnnotationString();
  }

  public LocalParameter createKineticParameter(String id) {
    return model.createKineticParameter(id);
  }

  public ModifierSpeciesReference createModifier() {
    return model.createModifier();
  }

  public CVTerm getCVTerm(int index) {
    return model.getCVTerm(index);
  }

  public int getCVTermCount() {
    return model.getCVTermCount();
  }

  public ModifierSpeciesReference createModifier(String id) {
    return model.createModifier(id);
  }

  public List<CVTerm> getCVTerms() {
    return model.getCVTerms();
  }

  public Map<String, String> getDeclaredNamespaces() {
    return model.getDeclaredNamespaces();
  }

  public Parameter createParameter() {
    return model.createParameter();
  }

  public String getElementName() {
    return model.getElementName();
  }

  public Parameter createParameter(String id) {
    return model.createParameter(id);
  }

  public SBasePlugin getExtension(String nameOrUri) {
    return model.getExtension(nameOrUri);
  }

  public SpeciesReference createProduct() {
    return model.createProduct();
  }

  public int getExtensionCount() {
    return model.getExtensionCount();
  }

  public Map<String, SBasePlugin> getExtensionPackages() {
    return model.getExtensionPackages();
  }

  public SpeciesReference createProduct(String id) {
    return model.createProduct(id);
  }

  public History getHistory() {
    return model.getHistory();
  }

  public RateRule createRateRule() {
    return model.createRateRule();
  }

  public SpeciesReference createReactant() {
    return model.createReactant();
  }

  public SpeciesReference createReactant(String id) {
    return model.createReactant(id);
  }

  public int getLevel() {
    return model.getLevel();
  }

  public ValuePair<Integer, Integer> getLevelAndVersion() {
    return model.getLevelAndVersion();
  }

  public String getMetaId() {
    return model.getMetaId();
  }

  public Model getModel() {
    return model.getModel();
  }

  public Reaction createReaction() {
    return model.createReaction();
  }

  public String getNamespace() {
    return model.getNamespace();
  }

  public Reaction createReaction(String id) {
    return model.createReaction(id);
  }

  public XMLNode getNotes() {
    return model.getNotes();
  }

  public String getNotesString() throws XMLStreamException {
    return model.getNotesString();
  }

  public Species createSpecies() {
    return model.createSpecies();
  }

  public int getNumCVTerms() {
    return model.getNumCVTerms();
  }

  public Species createSpecies(String id) {
    return model.createSpecies(id);
  }

  public int getNumPlugins() {
    return model.getNumPlugins();
  }

  public String getPackageName() {
    return model.getPackageName();
  }

  public Species createSpecies(String id, Compartment c) {
    return model.createSpecies(id, c);
  }

  public int getPackageVersion() {
    return model.getPackageVersion();
  }

  public SBase getParent() {
    return model.getParent();
  }

  public Species createSpecies(String id, String name, Compartment c) {
    return model.createSpecies(id, name, c);
  }

  public SBase getParentSBMLObject() {
    return model.getParentSBMLObject();
  }

  public SBasePlugin getPlugin(String nameOrUri) {
    return model.getPlugin(nameOrUri);
  }

  public SpeciesType createSpeciesType() {
    return model.createSpeciesType();
  }

  public SBMLDocument getSBMLDocument() {
    return model.getSBMLDocument();
  }

  public int getSBOTerm() {
    return model.getSBOTerm();
  }

  public SpeciesType createSpeciesType(String id) {
    return model.createSpeciesType(id);
  }

  public String getSBOTermID() {
    return model.getSBOTermID();
  }

  public String getURI() {
    return model.getURI();
  }

  public int getVersion() {
    return model.getVersion();
  }

  public Trigger createTrigger() {
    return model.createTrigger();
  }

  public Unit createUnit() {
    return model.createUnit();
  }

  public boolean hasValidAnnotation() {
    return model.hasValidAnnotation();
  }

  public Unit createUnit(Kind kind) {
    return model.createUnit(kind);
  }

  public boolean hasValidLevelVersionNamespaceCombination() {
    return model.hasValidLevelVersionNamespaceCombination();
  }

  public UnitDefinition createUnitDefinition() {
    return model.createUnitDefinition();
  }

  public boolean isExtendedByOtherPackages() {
    return model.isExtendedByOtherPackages();
  }

  public boolean isPackageEnabled(String packageURIOrName) {
    return model.isPackageEnabled(packageURIOrName);
  }

  public UnitDefinition createUnitDefinition(String id) {
    return model.createUnitDefinition(id);
  }

  public boolean isPackageURIEnabled(String packageURIOrName) {
    return model.isPackageURIEnabled(packageURIOrName);
  }

  public boolean isPkgEnabled(String packageURIOrName) {
    return model.isPkgEnabled(packageURIOrName);
  }

  public boolean equals(Object object) {
    return model.equals(object);
  }

  public boolean isPkgURIEnabled(String packageURIOrName) {
    return model.isPkgURIEnabled(packageURIOrName);
  }

  public boolean isSetAnnotation() {
    return model.isSetAnnotation();
  }

  public boolean isSetHistory() {
    return model.isSetHistory();
  }

  public boolean isSetLevel() {
    return model.isSetLevel();
  }

  public boolean isSetLevelAndVersion() {
    return model.isSetLevelAndVersion();
  }

  public boolean isSetMetaId() {
    return model.isSetMetaId();
  }

  public boolean isSetNotes() {
    return model.isSetNotes();
  }

  public boolean isSetParentSBMLObject() {
    return model.isSetParentSBMLObject();
  }

  public CallableSBase findCallableSBase(String id) {
    return model.findCallableSBase(id);
  }

  public boolean isSetPlugin(String nameOrUri) {
    return model.isSetPlugin(nameOrUri);
  }

  public UnitDefinition findIdentical(UnitDefinition unitDefinition) {
    return model.findIdentical(unitDefinition);
  }

  public boolean isSetSBOTerm() {
    return model.isSetSBOTerm();
  }

  public boolean isSetVersion() {
    return model.isSetVersion();
  }

  public List<LocalParameter> findLocalParameters(String id) {
    return model.findLocalParameters(id);
  }

  public ModifierSpeciesReference findModifierSpeciesReference(String id) {
    return model.findModifierSpeciesReference(id);
  }

  public boolean registerChild(SBase sbase) throws LevelVersionError {
    return model.registerChild(sbase);
  }

  public NamedSBase findNamedSBase(String id) {
    return model.findNamedSBase(id);
  }

  public NamedSBaseWithDerivedUnit findNamedSBaseWithDerivedUnit(String id) {
    return model.findNamedSBaseWithDerivedUnit(id);
  }

  public Quantity findQuantity(String id) {
    return model.findQuantity(id);
  }

  public QuantityWithUnit findQuantityWithUnit(String idOrName) {
    return model.findQuantityWithUnit(idOrName);
  }

  public boolean removeCVTerm(CVTerm cvTerm) {
    return model.removeCVTerm(cvTerm);
  }

  public SortedSet<String> findReactionsForLocalParameter(String id) {
    return model.findReactionsForLocalParameter(id);
  }

  public CVTerm removeCVTerm(int index) {
    return model.removeCVTerm(index);
  }

  public void setAnnotation(Annotation annotation) {
    model.setAnnotation(annotation);
  }

  public void setAnnotation(String nonRDFAnnotation) throws XMLStreamException {
    model.setAnnotation(nonRDFAnnotation);
  }

  public void setAnnotation(XMLNode nonRDFAnnotation) {
    model.setAnnotation(nonRDFAnnotation);
  }

  public SimpleSpeciesReference findSimpleSpeciesReference(String id) {
    return model.findSimpleSpeciesReference(id);
  }

  public void setHistory(History history) {
    model.setHistory(history);
  }

  public void setLevel(int level) {
    model.setLevel(level);
  }

  public SpeciesReference findSpeciesReference(String id) {
    return model.findSpeciesReference(id);
  }

  public Symbol findSymbol(String id) {
    return model.findSymbol(id);
  }

  public void setMetaId(String metaId) {
    model.setMetaId(metaId);
  }

  public UniqueNamedSBase findUniqueNamedSBase(String id) {
    return model.findUniqueNamedSBase(id);
  }

  public UnitDefinition findUnitDefinition(String id) {
    return model.findUnitDefinition(id);
  }

  public Variable findVariable(String id) {
    return model.findVariable(id);
  }

  public void setNamespace(String namespace) {
    model.setNamespace(namespace);
  }

  public boolean getAllowsChildren() {
    return model.getAllowsChildren();
  }

  public String getAreaUnits() {
    return model.getAreaUnits();
  }

  public UnitDefinition getAreaUnitsInstance() {
    return model.getAreaUnitsInstance();
  }

  public TreeNode getChildAt(int index) {
    return model.getChildAt(index);
  }

  public void setNotes(String notes) throws XMLStreamException {
    model.setNotes(notes);
  }

  public void setNotes(XMLNode notes) {
    model.setNotes(notes);
  }

  public void setPackageVersion(int packageVersion) {
    model.setPackageVersion(packageVersion);
  }

  public int getChildCount() {
    return model.getChildCount();
  }

  public Compartment getCompartment(int n) {
    return model.getCompartment(n);
  }

  public void setSBOTerm(int term) {
    model.setSBOTerm(term);
  }

  public Compartment getCompartment(String id) {
    return model.getCompartment(id);
  }

  public void setSBOTerm(String sboid) {
    model.setSBOTerm(sboid);
  }

  public int getCompartmentCount() {
    return model.getCompartmentCount();
  }

  public void setThisAsParentSBMLObject(SBase sbase) throws LevelVersionError {
    model.setThisAsParentSBMLObject(sbase);
  }

  public void setVersion(int version) {
    model.setVersion(version);
  }

  public CompartmentType getCompartmentType(int n) {
    return model.getCompartmentType(n);
  }

  public CompartmentType getCompartmentType(String id) {
    return model.getCompartmentType(id);
  }

  public void unregisterChild(SBase sbase) {
    model.unregisterChild(sbase);
  }

  public int getCompartmentTypeCount() {
    return model.getCompartmentTypeCount();
  }

  public Constraint getConstraint(int n) {
    return model.getConstraint(n);
  }

  public int getConstraintCount() {
    return model.getConstraintCount();
  }

  public String getConversionFactor() {
    return model.getConversionFactor();
  }

  public Parameter getConversionFactorInstance() {
    return model.getConversionFactorInstance();
  }

  public void unsetAnnotation() {
    model.unsetAnnotation();
  }

  public int getDelayCount() {
    return model.getDelayCount();
  }

  public void unsetCVTerms() {
    model.unsetCVTerms();
  }

  public void unsetExtension(String nameOrUri) {
    model.unsetExtension(nameOrUri);
  }

  public Event getEvent(int n) {
    return model.getEvent(n);
  }

  public Event getEvent(String id) {
    return model.getEvent(id);
  }

  public void unsetHistory() {
    model.unsetHistory();
  }

  public void unsetMetaId() {
    model.unsetMetaId();
  }

  public void unsetNamespace() {
    model.unsetNamespace();
  }

  public int getEventAssignmentCount() {
    return model.getEventAssignmentCount();
  }

  public void unsetNotes() {
    model.unsetNotes();
  }

  public int getEventCount() {
    return model.getEventCount();
  }

  public String getExtentUnits() {
    return model.getExtentUnits();
  }

  public void unsetPlugin(String nameOrUri) {
    model.unsetPlugin(nameOrUri);
  }

  public void unsetSBOTerm() {
    model.unsetSBOTerm();
  }

  public UnitDefinition getExtentUnitsInstance() {
    return model.getExtentUnitsInstance();
  }

  public FunctionDefinition getFunctionDefinition(int n) {
    return model.getFunctionDefinition(n);
  }

  public FunctionDefinition getFunctionDefinition(String id) {
    return model.getFunctionDefinition(id);
  }

  public int getFunctionDefinitionCount() {
    return model.getFunctionDefinitionCount();
  }

  public InitialAssignment getInitialAssignment(int n) {
    return model.getInitialAssignment(n);
  }

  public InitialAssignment getInitialAssignment(String variable) {
    return model.getInitialAssignment(variable);
  }

  public int getInitialAssignmentCount() {
    return model.getInitialAssignmentCount();
  }

  public int getKineticLawCount() {
    return model.getKineticLawCount();
  }

  public String getLengthUnits() {
    return model.getLengthUnits();
  }

  public UnitDefinition getLengthUnitsInstance() {
    return model.getLengthUnitsInstance();
  }

  public ListOf<Compartment> getListOfCompartments() {
    return model.getListOfCompartments();
  }

  public ListOf<CompartmentType> getListOfCompartmentTypes() {
    return model.getListOfCompartmentTypes();
  }

  public ListOf<Constraint> getListOfConstraints() {
    return model.getListOfConstraints();
  }

  public int getListOfCount() {
    return model.getListOfCount();
  }

  public ListOf<Event> getListOfEvents() {
    return model.getListOfEvents();
  }

  public ListOf<FunctionDefinition> getListOfFunctionDefinitions() {
    return model.getListOfFunctionDefinitions();
  }

  public ListOf<InitialAssignment> getListOfInitialAssignments() {
    return model.getListOfInitialAssignments();
  }

  public ListOf<Parameter> getListOfParameters() {
    return model.getListOfParameters();
  }

  public List<UnitDefinition> getListOfPredefinedUnitDefinitions() {
    return model.getListOfPredefinedUnitDefinitions();
  }

  public ListOf<Reaction> getListOfReactions() {
    return model.getListOfReactions();
  }

  public ListOf<Rule> getListOfRules() {
    return model.getListOfRules();
  }

  public ListOf<Species> getListOfSpecies() {
    return model.getListOfSpecies();
  }

  public ListOf<SpeciesType> getListOfSpeciesTypes() {
    return model.getListOfSpeciesTypes();
  }

  public ListOf<UnitDefinition> getListOfUnitDefinitions() {
    return model.getListOfUnitDefinitions();
  }

  public int getLocalParameterCount() {
    return model.getLocalParameterCount();
  }

  public int getMathContainerCount() {
    return model.getMathContainerCount();
  }

  public History getModelHistory() {
    return model.getModelHistory();
  }

  public int getModifierSpeciesReferenceCount() {
    return model.getModifierSpeciesReferenceCount();
  }

  public Set<ModifierSpeciesReference> getModifierSpeciesReferences() {
    return model.getModifierSpeciesReferences();
  }

  public int getNamedSBaseCount() {
    return model.getNamedSBaseCount();
  }

  public int getNamedSBaseWithDerivedUnitCount() {
    return model.getNamedSBaseWithDerivedUnitCount();
  }

  public int getNumCompartments() {
    return model.getNumCompartments();
  }

  public int getNumCompartmentTypes() {
    return model.getNumCompartmentTypes();
  }

  public int getNumConstraints() {
    return model.getNumConstraints();
  }

  public int getNumDelays() {
    return model.getNumDelays();
  }

  public int getNumEventAssignments() {
    return model.getNumEventAssignments();
  }

  public int getNumEvents() {
    return model.getNumEvents();
  }

  public int getNumFunctionDefinitions() {
    return model.getNumFunctionDefinitions();
  }

  public int getNumInitialAssignments() {
    return model.getNumInitialAssignments();
  }

  public int getNumKineticLaws() {
    return model.getNumKineticLaws();
  }

  public int getNumListsOf() {
    return model.getNumListsOf();
  }

  public int getNumLocalParameters() {
    return model.getNumLocalParameters();
  }

  public int getNumMathContainers() {
    return model.getNumMathContainers();
  }

  public int getNumModifierSpeciesReferences() {
    return model.getNumModifierSpeciesReferences();
  }

  public int getNumNamedSBases() {
    return model.getNumNamedSBases();
  }

  public int getNumNamedSBasesWithDerivedUnit() {
    return model.getNumNamedSBasesWithDerivedUnit();
  }

  public int getNumParameters() {
    return model.getNumParameters();
  }

  public int getNumQuantities() {
    return model.getNumQuantities();
  }

  public int getNumQuantitiesWithUnit() {
    return model.getNumQuantitiesWithUnit();
  }

  public int getNumReactions() {
    return model.getNumReactions();
  }

  public int getNumRules() {
    return model.getNumRules();
  }

  public int getNumSBases() {
    return model.getNumSBases();
  }

  public int getNumSBasesWithDerivedUnit() {
    return model.getNumSBasesWithDerivedUnit();
  }

  public int getNumSpecies() {
    return model.getNumSpecies();
  }

  public int getNumSpeciesReferences() {
    return model.getNumSpeciesReferences();
  }

  public int getNumSpeciesTypes() {
    return model.getNumSpeciesTypes();
  }

  public int getNumSpeciesWithBoundaryCondition() {
    return model.getNumSpeciesWithBoundaryCondition();
  }

  public int getNumStoichiometryMath() {
    return model.getNumStoichiometryMath();
  }

  public int getNumSymbols() {
    return model.getNumSymbols();
  }

  public int getNumTriggers() {
    return model.getNumTriggers();
  }

  public int getNumUnitDefinitions() {
    return model.getNumUnitDefinitions();
  }

  public int getNumUnits() {
    return model.getNumUnits();
  }

  public int getNumVariables() {
    return model.getNumVariables();
  }

  public Parameter getParameter(int n) {
    return model.getParameter(n);
  }

  public Parameter getParameter(String id) {
    return model.getParameter(id);
  }

  public int getParameterCount() {
    return model.getParameterCount();
  }

  public UnitDefinition getPredefinedUnitDefinition(String unitKind) {
    return model.getPredefinedUnitDefinition(unitKind);
  }

  public int getQuantityCount() {
    return model.getQuantityCount();
  }

  public int getQuantityWithUnitCount() {
    return model.getQuantityWithUnitCount();
  }

  public Reaction getReaction(int n) {
    return model.getReaction(n);
  }

  public Reaction getReaction(String id) {
    return model.getReaction(id);
  }

  public int getReactionCount() {
    return model.getReactionCount();
  }

  public Rule getRule(int n) {
    return model.getRule(n);
  }

  public ExplicitRule getRule(String variable) {
    return model.getRule(variable);
  }

  public int getRuleCount() {
    return model.getRuleCount();
  }

  public int getSBaseCount() {
    return model.getSBaseCount();
  }

  public int getSBaseWithDerivedUnitCount() {
    return model.getSBaseWithDerivedUnitCount();
  }

  public Species getSpecies(int n) {
    return model.getSpecies(n);
  }

  public Species getSpecies(String id) {
    return model.getSpecies(id);
  }

  public int getSpeciesCount() {
    return model.getSpeciesCount();
  }

  public int getSpeciesReferenceCount() {
    return model.getSpeciesReferenceCount();
  }

  public SpeciesType getSpeciesType(int n) {
    return model.getSpeciesType(n);
  }

  public SpeciesType getSpeciesType(String id) {
    return model.getSpeciesType(id);
  }

  public int getSpeciesTypeCount() {
    return model.getSpeciesTypeCount();
  }

  public int getSpeciesWithBoundaryConditionCount() {
    return model.getSpeciesWithBoundaryConditionCount();
  }

  public int getStoichiometryMathCount() {
    return model.getStoichiometryMathCount();
  }

  public String getSubstanceUnits() {
    return model.getSubstanceUnits();
  }

  public UnitDefinition getSubstanceUnitsInstance() {
    return model.getSubstanceUnitsInstance();
  }

  public int getSymbolCount() {
    return model.getSymbolCount();
  }

  public String getTimeUnits() {
    return model.getTimeUnits();
  }

  public UnitDefinition getTimeUnitsInstance() {
    return model.getTimeUnitsInstance();
  }

  public int getTriggerCount() {
    return model.getTriggerCount();
  }

  public int getUnitCount() {
    return model.getUnitCount();
  }

  public UnitDefinition getUnitDefinition(int n) {
    return model.getUnitDefinition(n);
  }

  public UnitDefinition getUnitDefinition(String id) {
    return model.getUnitDefinition(id);
  }

  public int getUnitDefinitionCount() {
    return model.getUnitDefinitionCount();
  }

  public int getVariableCount() {
    return model.getVariableCount();
  }

  public String getVolumeUnits() {
    return model.getVolumeUnits();
  }

  public UnitDefinition getVolumeUnitsInstance() {
    return model.getVolumeUnitsInstance();
  }

  public int hashCode() {
    return model.hashCode();
  }

  public boolean hasUnit(String id) {
    return model.hasUnit(id);
  }

  public void initDefaults() {
    model.initDefaults();
  }

  public void initDefaults(int level, int version) {
    model.initDefaults(level, version);
  }

  public boolean isIdMandatory() {
    return model.isIdMandatory();
  }

  public boolean isSetAreaUnits() {
    return model.isSetAreaUnits();
  }

  public boolean isSetAreaUnitsInstance() {
    return model.isSetAreaUnitsInstance();
  }

  public boolean isSetConversionFactor() {
    return model.isSetConversionFactor();
  }

  public boolean isSetConversionFactorInstance() {
    return model.isSetConversionFactorInstance();
  }

  public boolean isSetExtentUnits() {
    return model.isSetExtentUnits();
  }

  public boolean isSetExtentUnitsInstance() {
    return model.isSetExtentUnitsInstance();
  }

  public boolean isSetLengthUnits() {
    return model.isSetLengthUnits();
  }

  public boolean isSetLengthUnitsInstance() {
    return model.isSetLengthUnitsInstance();
  }

  public boolean isSetListOfCompartments() {
    return model.isSetListOfCompartments();
  }

  public boolean isSetListOfCompartmentTypes() {
    return model.isSetListOfCompartmentTypes();
  }

  public boolean isSetListOfConstraints() {
    return model.isSetListOfConstraints();
  }

  public boolean isSetListOfEvents() {
    return model.isSetListOfEvents();
  }

  public boolean isSetListOfFunctionDefinitions() {
    return model.isSetListOfFunctionDefinitions();
  }

  public boolean isSetListOfInitialAssignments() {
    return model.isSetListOfInitialAssignments();
  }

  public boolean isSetListOfParameters() {
    return model.isSetListOfParameters();
  }

  public boolean isSetListOfReactions() {
    return model.isSetListOfReactions();
  }

  public boolean isSetListOfRules() {
    return model.isSetListOfRules();
  }

  public boolean isSetListOfSpecies() {
    return model.isSetListOfSpecies();
  }

  public boolean isSetListOfSpeciesTypes() {
    return model.isSetListOfSpeciesTypes();
  }

  public boolean isSetListOfUnitDefinitions() {
    return model.isSetListOfUnitDefinitions();
  }

  public boolean isSetModelHistory() {
    return model.isSetModelHistory();
  }

  public boolean isSetSubstanceUnits() {
    return model.isSetSubstanceUnits();
  }

  public boolean isSetSubstanceUnitsInstance() {
    return model.isSetSubstanceUnitsInstance();
  }

  public boolean isSetTimeUnits() {
    return model.isSetTimeUnits();
  }

  public boolean isSetTimeUnitsInstance() {
    return model.isSetTimeUnitsInstance();
  }

  public boolean isSetVolumeUnits() {
    return model.isSetVolumeUnits();
  }

  public boolean isSetVolumeUnitsInstance() {
    return model.isSetVolumeUnitsInstance();
  }

  public boolean readAttribute(String attributeName, String prefix, String value) {
    return model.readAttribute(attributeName, prefix, value);
  }

  public boolean register(SBase sbase) {
    return model.register(sbase);
  }

  public <T extends UniqueNamedSBase> T remove(String id) {
    return model.remove(id);
  }

  public Compartment removeCompartment(int i) {
    return model.removeCompartment(i);
  }

  public Compartment removeCompartment(String id) {
    return model.removeCompartment(id);
  }

  public CompartmentType removeCompartmentType(int n) {
    return model.removeCompartmentType(n);
  }

  public CompartmentType removeCompartmentType(String id) {
    return model.removeCompartmentType(id);
  }

  public Constraint removeConstraint(int n) {
    return model.removeConstraint(n);
  }

  public Event removeEvent(int n) {
    return model.removeEvent(n);
  }

  public Event removeEvent(String id) {
    return model.removeEvent(id);
  }

  public FunctionDefinition removeFunctionDefinition(int n) {
    return model.removeFunctionDefinition(n);
  }

  public FunctionDefinition removeFunctionDefinition(String id) {
    return model.removeFunctionDefinition(id);
  }

  public InitialAssignment removeInitialAssignment(int n) {
    return model.removeInitialAssignment(n);
  }

  public Parameter removeParameter(int n) {
    return model.removeParameter(n);
  }

  public boolean removeParameter(Parameter parameter) {
    return model.removeParameter(parameter);
  }

  public Parameter removeParameter(String id) {
    return model.removeParameter(id);
  }

  public Reaction removeReaction(int n) {
    return model.removeReaction(n);
  }

  public boolean removeReaction(Reaction reac) {
    return model.removeReaction(reac);
  }

  public Reaction removeReaction(String id) {
    return model.removeReaction(id);
  }

  public Rule removeRule(int i) {
    return model.removeRule(i);
  }

  public Rule removeRule(String variableId) {
    return model.removeRule(variableId);
  }

  public boolean removeRule(Rule rule) {
    return model.removeRule(rule);
  }

  public Species removeSpecies(int i) {
    return model.removeSpecies(i);
  }

  public boolean removeSpecies(Species spec) {
    return model.removeSpecies(spec);
  }

  public Species removeSpecies(String id) {
    return model.removeSpecies(id);
  }

  public SpeciesType removeSpeciesType(int n) {
    return model.removeSpeciesType(n);
  }

  public SpeciesType removeSpeciesType(String id) {
    return model.removeSpeciesType(id);
  }

  public UnitDefinition removeUnitDefinition(int n) {
    return model.removeUnitDefinition(n);
  }

  public UnitDefinition removeUnitDefinition(String id) {
    return model.removeUnitDefinition(id);
  }

  public boolean removeUnitDefinition(UnitDefinition unitDefininition) {
    return model.removeUnitDefinition(unitDefininition);
  }

  public void setAreaUnits(String areaUnitsID) {
    model.setAreaUnits(areaUnitsID);
  }

  public void setAreaUnits(Kind kind) {
    model.setAreaUnits(kind);
  }

  public void setAreaUnits(UnitDefinition areaUnits) {
    model.setAreaUnits(areaUnits);
  }

  public void setConversionFactor(Parameter conversionFactor) {
    model.setConversionFactor(conversionFactor);
  }

  public void setConversionFactor(String conversionFactorID) {
    model.setConversionFactor(conversionFactorID);
  }

  public void setExtentUnits(String extentUnitsID) {
    model.setExtentUnits(extentUnitsID);
  }

  public void setExtentUnits(Kind kind) {
    model.setExtentUnits(kind);
  }

  public void setExtentUnits(UnitDefinition extentUnits) {
    model.setExtentUnits(extentUnits);
  }

  public void setLengthUnits(String lengthUnitsID) {
    model.setLengthUnits(lengthUnitsID);
  }

  public void setLengthUnits(Kind kind) {
    model.setLengthUnits(kind);
  }

  public void setLengthUnits(UnitDefinition lengthUnits) {
    model.setLengthUnits(lengthUnits);
  }

  public void setListOfCompartments(ListOf<Compartment> listOfCompartments) {
    model.setListOfCompartments(listOfCompartments);
  }

  public void setListOfCompartmentTypes(ListOf<CompartmentType> listOfCompartmentTypes) {
    model.setListOfCompartmentTypes(listOfCompartmentTypes);
  }

  public void setListOfConstraints(ListOf<Constraint> listOfConstraints) {
    model.setListOfConstraints(listOfConstraints);
  }

  public void setListOfEvents(ListOf<Event> listOfEvents) {
    model.setListOfEvents(listOfEvents);
  }

  public void setListOfFunctionDefinitions(ListOf<FunctionDefinition> listOfFunctionDefinitions) {
    model.setListOfFunctionDefinitions(listOfFunctionDefinitions);
  }

  public void setListOfInitialAssignments(ListOf<InitialAssignment> listOfInitialAssignments) {
    model.setListOfInitialAssignments(listOfInitialAssignments);
  }

  public void setListOfParameters(ListOf<Parameter> listOfParameters) {
    model.setListOfParameters(listOfParameters);
  }

  public void setListOfReactions(ListOf<Reaction> listOfReactions) {
    model.setListOfReactions(listOfReactions);
  }

  public void setListOfRules(ListOf<Rule> listOfRules) {
    model.setListOfRules(listOfRules);
  }

  public void setListOfSpecies(ListOf<Species> listOfSpecies) {
    model.setListOfSpecies(listOfSpecies);
  }

  public void setListOfSpeciesTypes(ListOf<SpeciesType> listOfSpeciesTypes) {
    model.setListOfSpeciesTypes(listOfSpeciesTypes);
  }

  public void setListOfUnitDefinitions(ListOf<UnitDefinition> listOfUnitDefinitions) {
    model.setListOfUnitDefinitions(listOfUnitDefinitions);
  }

  public void setModelHistory(History history) {
    model.setModelHistory(history);
  }

  public void setSubstanceUnits(String substanceUnitsID) {
    model.setSubstanceUnits(substanceUnitsID);
  }

  public void setSubstanceUnits(UnitDefinition substanceUnits) {
    model.setSubstanceUnits(substanceUnits);
  }

  public void setSubstanceUnits(Kind kind) {
    model.setSubstanceUnits(kind);
  }

  public void setTimeUnits(String timeUnitsID) {
    model.setTimeUnits(timeUnitsID);
  }

  public void setTimeUnits(UnitDefinition timeUnits) {
    model.setTimeUnits(timeUnits);
  }

  public void setTimeUnits(Kind kind) {
    model.setTimeUnits(kind);
  }

  public void setVolumeUnits(String volumeUnitsID) {
    model.setVolumeUnits(volumeUnitsID);
  }

  public void setVolumeUnits(Kind kind) {
    model.setVolumeUnits(kind);
  }

  public void setVolumeUnits(UnitDefinition volumeUnits) {
    model.setVolumeUnits(volumeUnits);
  }

  public boolean unregister(SBase sbase) {
    return model.unregister(sbase);
  }

  public void unsetAreaUnits() {
    model.unsetAreaUnits();
  }

  public void unsetConversionFactor() {
    model.unsetConversionFactor();
  }

  public void unsetExtentUnits() {
    model.unsetExtentUnits();
  }

  public void unsetLengthUnits() {
    model.unsetLengthUnits();
  }

  public boolean unsetListOfCompartments() {
    return model.unsetListOfCompartments();
  }

  public boolean unsetListOfCompartmentTypes() {
    return model.unsetListOfCompartmentTypes();
  }

  public boolean unsetListOfConstraints() {
    return model.unsetListOfConstraints();
  }

  public boolean unsetListOfEvents() {
    return model.unsetListOfEvents();
  }

  public boolean unsetListOfFunctionDefinitions() {
    return model.unsetListOfFunctionDefinitions();
  }

  public boolean unsetListOfInitialAssignments() {
    return model.unsetListOfInitialAssignments();
  }

  public boolean unsetListOfParameters() {
    return model.unsetListOfParameters();
  }

  public boolean unsetListOfReactions() {
    return model.unsetListOfReactions();
  }

  public boolean unsetListOfRules() {
    return model.unsetListOfRules();
  }

  public boolean unsetListOfSpecies() {
    return model.unsetListOfSpecies();
  }

  public boolean unsetListOfSpeciesTypes() {
    return model.unsetListOfSpeciesTypes();
  }

  public boolean unsetListOfUnitDefinitions() {
    return model.unsetListOfUnitDefinitions();
  }

  public void unsetModelHistory() {
    model.unsetModelHistory();
  }

  public void unsetSubstanceUnits() {
    model.unsetSubstanceUnits();
  }

  public void unsetTimeUnits() {
    model.unsetTimeUnits();
  }

  public void unsetVolumeUnits() {
    model.unsetVolumeUnits();
  }

  public Map<String, String> writeXMLAttributes() {
    return model.writeXMLAttributes();
  }

  // Other methods
  PredictiveModelQuality getModelQuality() {
    XMLNode annotation = getAnnotationNode(PMFUtil.PMML_NS, "modelquality");
    if (annotation == null) {
      return null;
    }
    JAXBContext jaxbContent = JAXBContext.newInstance(PredictiveModelQuality.class);
    Unmarshaller unmarshaller = jaxbContent.createUnmarshaller();
    Object obj = unmarshaller.unmarshal(new InputSource(annotation.toXMLString()));
    PredictiveModelQuality pmq = (PredictiveModelQuality) obj;
    return pmq;
  }

  final static String sbmlTemplate =
      "<sbml xmlns='http://www.sbml.org/sbml/level2/version4' level='2' version='1'>\n"
          + "<model id='test'>\n" + "<annotation>%s</annotation>\n" + "</model>\n";

  void setModelQuality(PredictiveModelQuality quality) {
    if (quality == null) {
      throw new NullPointerException("quality must not be null");
    }

    JAXBContext context = null;
    try {
      context = JAXBContext.newInstance(PredictiveModelQuality.class);
    } catch (JAXBException e) {
      e.printStackTrace();
    }

    Marshaller marshaller = null;
    try {
      marshaller = context.createMarshaller();
    } catch (JAXBException e) {
      e.printStackTrace();
    }

    try {
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    } catch (PropertyException e) {
      e.printStackTrace();
    }

    StringWriter stringWriter = new StringWriter();
    try {
      marshaller.marshal(quality, stringWriter);
    } catch (JAXBException e) {
      e.printStackTrace();
    }

    String sbmlMockup = String.format(sbmlTemplate, sbmlMetadata.toXMLString());
    SBMLAdapter reader = new SBMLAdapter(true);

  }

  // TODO: ...
  
  // replacement methods
  public void initLevelAndVersion() {
    SBMLReplacement.initLevelAndVersion(model);
  }
  
  public void replace(Model modelToReplace) {
    SBMLReplacement.replace(modelToReplace, model);
  }
  
  List<ConformityMessage> getInvalidSettings(SBMLDocument document, String prefix, PMFDocument pmf) {
    return SBMLReplacement.getInvalidSettings(document, prefix, pmf);
  }
}


class SourceValue {

  final String sourceId;
  final String xpath;

  public SourceValue(String sourceId, String xpath) {
    this.sourceId = sourceId;
    this.xpath = xpath;

    // validate
    try {
      getXPathExpression();
    } catch (XPathExpressionException e) {
      throw new IllegalArgumentException("Invalid xpath", e);
    }
  }

  XPathExpression getXPathExpression() throws XPathExpressionException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    return xpath.compile(this.xpath);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sourceId == null) ? 0 : sourceId.hashCode());
    result = prime * result + ((xpath == null) ? 0 : xpath.hashCode());
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
    SourceValue other = (SourceValue) obj;
    if (sourceId == null && other.sourceId != null) {
      return false;
    } else if (!sourceId.equals(other.sourceId)) {
      return false;
    }
    if (xpath == null && other.xpath != null) {
      return false;
    } else if (!xpath.equals(other.xpath))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "SourceValue [sourceId=" + sourceId + ", xpath=" + xpath + "]";
  }

  // replacement methods
  public void initLevelAndVersion() {
    SBMLReplacement.initLevelAndVersion((SBase) this);
  }

  public void replace(SBase sbmlElement) {
    SBMLReplacement.replace(sbmlElement, (SBase) this);
  }

  public List<ConformityMessage> getInvalidSettings(SBMLDocument document, String prefix,
      PMFDocument pmf) {
    return SBMLReplacement.getInvalidSettings(document, prefix, pmf);
  }
}
