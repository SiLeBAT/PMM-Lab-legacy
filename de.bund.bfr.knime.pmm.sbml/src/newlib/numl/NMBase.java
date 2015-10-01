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
package newlib.numl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Strings;

import groovy.lang.MetaClass;
import groovy.util.Node;
import groovy.xml.QName;

public abstract class NMBase {

  private static final String NUML_NS = "http://www.numl.org/numl/level1/version1";
  private static final String META_ID = "metaId";

  protected String metaId;

  Node annotation = new Node(null, new QName(NUML_NS, "annotation"));

  Node notes = new Node(null, new QName(NUML_NS, "notes"));

  NMBase parent;

  public NMBase() {
    metaId = null;
  }

  protected NMBase(Element node) {
    metaId = Strings.emptyToNull(node.getAttribute(META_ID));
  }

  public String getMetaId() {
    return metaId;
  }

  public void setMetaId(String metaId) {
    this.metaId = metaId;
  }

  protected abstract Element toNode(Document doc);

  protected void updateNode(Element node) {
    Utils.setAttributeValue(node, META_ID, metaId);
  }

  Map<String, NMBase> getNamedChildren() {
    // TODO: getNamedChildren
    return new HashMap<>();
  }

  List<NMBase> getChildren() {
    return new LinkedList<>(getNamedChildren().values());
  }
  
  List<NMBase> getDocument() {
    return (parent == null) ? null : parent.getDocument();
  }
  
  protected List<String> getIgnoredProperties() {
    return Arrays.asList("document", "parent", "originalNode", "notes", "annotation");
  }
  
  // TODO: ...
//  protected Map<String, Object> getPropertyValue() {
//    
//  }
  
  // TODO: ...
//  protected Map<String, Object> getPropertyValues(MetaClass metaClass) {
//
//  }
}
