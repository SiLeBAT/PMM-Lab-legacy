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

import java.util.LinkedList;
import java.util.List;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;

import newlib.numl.ConformityMessage;
import newlib.pmf.PMFDocument;

public abstract class SBMLReplacement {

  public static void initLevelAndVersion(SBase sbmlElement) {
    if (!sbmlElement.isSetLevel()) {
      sbmlElement.setLevel(3);
    }
    if (!sbmlElement.isSetVersion()) {
      sbmlElement.setVersion(1);
    }
  }

  public static void replace(SBase oldItem, SBase newItem) {
    newItem.removeFromParent();
    SBase newParent = oldItem.getParentSBMLObject();
    if (newParent instanceof ListOf) {
      // bug SBML does not automatically unregister old elements
      newParent.unregisterChild(oldItem);
      ((ListOf<SBase>) newParent).set(newParent.getIndex(oldItem), newItem);
    }
  }

  public static List<ConformityMessage> getInvalidSettings(SBMLDocument document, String prefix,
      PMFDocument pmf) {
    return new LinkedList<ConformityMessage>();
  }
}
