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

  public PMFMetadataNode(final ModelType modelType, final Set<String> masterFiles) {
    this.modelType = modelType;
    this.masterFiles = masterFiles;

    node = new Element("metaParent");
    final Element modelTypeElement = new Element(MODEL_TYPE_TAG);
    modelTypeElement.addContent(modelType.name());
    node.addContent(modelTypeElement);
    for (final String masterFile : masterFiles) {
      final Element masterFileElement = new Element(MASTER_FILE_TAG);
      masterFileElement.addContent(masterFile);
      node.addContent(masterFileElement);
    }
  }

  public PMFMetadataNode(final Element node) {
    this.node = node;

    modelType = ModelType.valueOf(node.getChildText(MODEL_TYPE_TAG));
    final List<Element> masterFileElements = node.getChildren(MASTER_FILE_TAG);
    masterFiles = new HashSet<>(masterFileElements.size());
    for (final Element masterFileElement : masterFileElements) {
      masterFiles.add(masterFileElement.getText());
    }
  }

  public final ModelType getModelType() {
    return modelType;
  }

  public final Set<String> masterFiles() {
    return masterFiles;
  }
}
