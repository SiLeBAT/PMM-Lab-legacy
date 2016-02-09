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
package de.bund.bfr.pmf.file;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;

import de.bund.bfr.pmf.ModelType;

/**
 * @author Miguel Alba
 */
public class PMFMetadataNode {

  private static final String MODEL_TYPE_TAG = "modelType";
  private static final String MASTER_FILE_TAG = "masterfile";

  ModelType modelType;
  Set<String> masterFiles;
  Element node;

  public PMFMetadataNode(ModelType modelType, Set<String> masterFiles) {
    this.modelType = modelType;
    this.masterFiles = masterFiles;

    node = new Element("metaParent");
    Element modelTypeElement = new Element(MODEL_TYPE_TAG);
    modelTypeElement.addContent(modelType.name());
    node.addContent(modelTypeElement);
    for (String masterFile : masterFiles) {
      Element masterFileElement = new Element(MASTER_FILE_TAG);
      masterFileElement.addContent(masterFile);
      node.addContent(masterFileElement);
    }
  }

  public PMFMetadataNode(Element node) {
    this.node = node;

    modelType = ModelType.valueOf(node.getChildText(MODEL_TYPE_TAG));
    List<Element> masterFileElements = node.getChildren(MASTER_FILE_TAG);
    masterFiles = new HashSet<>(masterFileElements.size());
    for (Element masterFileElement : masterFileElements) {
      masterFiles.add(masterFileElement.getText());
    }
  }

  public ModelType getModelType() {
    return modelType;
  }

  public Set<String> masterFiles() {
    return masterFiles;
  }
}
