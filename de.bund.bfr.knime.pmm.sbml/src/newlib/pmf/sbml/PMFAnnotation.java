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
import java.util.Set;

import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.CVTerm.Qualifier;
import org.sbml.jsbml.History;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.util.TreeNodeChangeListener;
import org.sbml.jsbml.util.filters.Filter;
import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;

import newlib.numl.ConformityMessage;
import newlib.pmf.PMFDocument;

/**
 * Adds non-rdf nodes to child list.
 * 
 * @author Arvide Heise
 * @author Miguel Alba
 */
public class PMFAnnotation {

  Annotation annotation;

  public PMFAnnotation() {
    annotation = new Annotation();
  }

  public PMFAnnotation(Annotation annotation) {
    annotation = new Annotation(annotation);
  }

  public PMFAnnotation(List<CVTerm> cvTerms) {
    annotation = new Annotation(cvTerms);
  }

  public PMFAnnotation(String annotation) throws XMLStreamException {
    this.annotation = new Annotation(annotation);
  }

  public PMFAnnotation(String annotation, List<CVTerm> cvTerms) throws XMLStreamException {
    this.annotation = new Annotation(annotation, cvTerms);
  }

  // Annotation methods
  public boolean addCVTerm(CVTerm cvTerm) {
    return annotation.addCVTerm(cvTerm);
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners) {
    return annotation.addAllChangeListeners(listeners);
  }

  public void addDeclaredNamespace(String prefix, String uri) {
    annotation.addDeclaredNamespace(prefix, uri);
  }

  public boolean addAllChangeListeners(Collection<TreeNodeChangeListener> listeners,
      boolean recursive) {
    return annotation.addAllChangeListeners(listeners, recursive);
  }

  public void appendNonRDFAnnotation(String annotation) throws XMLStreamException {
    this.annotation.appendNonRDFAnnotation(annotation);
  }

  public void appendNonRDFAnnotation(XMLNode annotationToAppend) {
    annotation.appendNonRDFAnnotation(annotationToAppend);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener) {
    annotation.addTreeNodeChangeListener(listener);
  }

  public void addTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    annotation.addTreeNodeChangeListener(listener, recursive);
  }

  public Enumeration<TreeNode> children() {
    return annotation.children();
  }

  public Annotation clone() {
    return annotation.clone();
  }

  public void clearUserObjects() {
    annotation.clearUserObjects();
  }

  public List<CVTerm> filterCVTerms(Qualifier qualifier) {
    return annotation.filterCVTerms(qualifier);
  }

  public boolean containsUserObjectKey(Object key) {
    return annotation.containsUserObjectKey(key);
  }

  public List<String> filterCVTerms(Qualifier qualifier, String pattern) {
    return annotation.filterCVTerms(qualifier, pattern);
  }

  public String getAbout() {
    return annotation.getAbout();
  }

  public boolean getAllowsChildren() {
    return annotation.getAllowsChildren();
  }

  public XMLNode getAnnotationBuilder() {
    return annotation.getAnnotationBuilder();
  }

  public List<? extends TreeNode> filter(Filter filter) {
    return annotation.filter(filter);
  }

  public TreeNode getChildAt(int childIndex) {
    return annotation.getChildAt(childIndex);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes) {
    return annotation.filter(filter, retainInternalNodes);
  }

  public List<? extends TreeNode> filter(Filter filter, boolean retainInternalNodes,
      boolean prune) {
    return annotation.filter(filter, retainInternalNodes, prune);
  }

  public int getChildCount() {
    return annotation.getChildCount();
  }

  public CVTerm getCVTerm(int i) {
    return annotation.getCVTerm(i);
  }

  public int getCVTermCount() {
    return annotation.getCVTermCount();
  }

  public void fireNodeAddedEvent() {
    annotation.fireNodeAddedEvent();
  }

  public XMLNamespaces getDeclaredNamespaces() {
    return annotation.getDeclaredNamespaces();
  }

  public void fireNodeRemovedEvent() {
    annotation.fireNodeRemovedEvent();
  }

  public XMLNode getFullAnnotation() {
    return annotation.getFullAnnotation();
  }

  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    annotation.firePropertyChange(propertyName, oldValue, newValue);
  }

  public String getFullAnnotationString() {
    return annotation.getFullAnnotationString();
  }

  public History getHistory() {
    return annotation.getHistory();
  }

  public List<CVTerm> getListOfCVTerms() {
    return annotation.getListOfCVTerms();
  }

  public XMLNode getNonRDFannotation() {
    return annotation.getNonRDFannotation();
  }

  public String getNonRDFannotationAsString() {
    return annotation.getNonRDFannotationAsString();
  }

  public int getNumCVTerms() {
    return annotation.getNumCVTerms();
  }

  public XMLNode getXMLNode() {
    return annotation.getXMLNode();
  }

  public void setXMLNode(XMLNode nonRDFannotation) {
    annotation.setXMLNode(nonRDFannotation);
  }

  public int getIndex(TreeNode node) {
    return annotation.getIndex(node);
  }

  public List<TreeNodeChangeListener> getListOfTreeNodeChangeListeners() {
    return annotation.getListOfTreeNodeChangeListeners();
  }

  public boolean isEmpty() {
    return annotation.isEmpty();
  }

  public int getNumChildren() {
    return annotation.getNumChildren();
  }

  public boolean isSetAbout() {
    return annotation.isSetAbout();
  }

  public TreeNode getParent() {
    return annotation.getParent();
  }

  public boolean isSetAnnotation() {
    return annotation.isSetAnnotation();
  }

  public TreeNode getRoot() {
    return annotation.getRoot();
  }

  public int getTreeNodeChangeListenerCount() {
    return annotation.getTreeNodeChangeListenerCount();
  }

  public Object getUserObject(Object key) {
    return annotation.getUserObject(key);
  }

  public boolean isSetHistory() {
    return annotation.isSetHistory();
  }

  public boolean isSetListOfCVTerms() {
    return annotation.isSetListOfCVTerms();
  }

  public boolean isSetNonRDFannotation() {
    return annotation.isSetNonRDFannotation();
  }

  public boolean isSetOtherAnnotationThanRDF() {
    return annotation.isSetOtherAnnotationThanRDF();
  }

  public boolean isLeaf() {
    return annotation.isLeaf();
  }

  public boolean isRoot() {
    return annotation.isRoot();
  }

  public boolean isSetRDFannotation() {
    return annotation.isSetRDFannotation();
  }

  public boolean isSetParent() {
    return annotation.isSetParent();
  }

  public boolean isSetUserObjects() {
    return annotation.isSetUserObjects();
  }

  public boolean readAttribute(String attributeName, String prefix, String value) {
    return annotation.readAttribute(attributeName, prefix, value);
  }

  public void putUserObject(Object key, Object userObject) {
    annotation.putUserObject(key, userObject);
  }

  public boolean removeCVTerm(CVTerm cvTerm) {
    return annotation.removeCVTerm(cvTerm);
  }

  public void removeAllTreeNodeChangeListeners() {
    annotation.removeAllTreeNodeChangeListeners();
  }

  public CVTerm removeCVTerm(int index) {
    return annotation.removeCVTerm(index);
  }

  public boolean removeFromParent() {
    return annotation.removeFromParent();
  }

  public void setAbout(String about) {
    annotation.setAbout(about);
  }

  public void setHistory(History history) {
    annotation.setHistory(history);
  }

  public void setNonRDFAnnotation(String nonRDFAnnotationStr) throws XMLStreamException {
    annotation.setNonRDFAnnotation(nonRDFAnnotationStr);
  }

  public void setNonRDFAnnotation(XMLNode nonRDFAnnotation) {
    annotation.setNonRDFAnnotation(nonRDFAnnotation);
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener) {
    annotation.removeTreeNodeChangeListener(listener);
  }

  public void unsetCVTerms() {
    annotation.unsetCVTerms();
  }

  public void removeAllTreeNodeChangeListeners(boolean recursive) {
    annotation.removeAllTreeNodeChangeListeners(recursive);
  }

  public void unsetHistory() {
    annotation.unsetHistory();
  }

  public void removeTreeNodeChangeListener(TreeNodeChangeListener listener, boolean recursive) {
    annotation.removeTreeNodeChangeListener(listener, recursive);
  }

  public void unsetNonRDFannotation() {
    annotation.unsetNonRDFannotation();
  }

  public Object removeUserObject(Object key) {
    return annotation.removeUserObject(key);
  }

  public void setParent(TreeNode parent) {
    annotation.setParent(parent);
  }

  public String toString() {
    return annotation.toString();
  }

  public Set<Object> userObjectKeySet() {
    return annotation.userObjectKeySet();
  }

  @Override
  public int hashCode() {
    return annotation.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return annotation.equals(obj);
  }

  // replacement methods
  public void replace(Annotation annotation) {
    this.annotation = new Annotation(annotation);
  }

  public List<ConformityMessage> getInvalidSettings(SBMLDocument document, String prefix,
      PMFDocument pmf) {
    return SBMLReplacement.getInvalidSettings(document, prefix, pmf);
  }
}
