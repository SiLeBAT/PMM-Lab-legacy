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

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Level;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLError;
import org.sbml.jsbml.SBMLErrorLog;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.util.TreeNodeChangeListener;
import org.sbml.jsbml.util.TreeNodeRemovedEvent;
import org.sbml.jsbml.validator.SBMLValidator;
import org.sbml.jsbml.xml.stax.SBMLReader;
import org.sbml.jsbml.xml.stax.SBMLWriter;

import newlib.numl.ConformityMessage;
import newlib.pmf.PMFUtil;

public class SBMLAdapter {

  SBMLReader reader = new SBMLReader();
  SBMLDocument document;
  boolean validating = false;
  List<ConformityMessage> messages = new LinkedList<>();

  SBMLDocument read(InputStream stream) {
    String text = null;
    try {
      text = IOUtils.toString(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return parseText(text);
  }

  SBMLDocument parseText(String xmlString) {
    messages = new LinkedList<>();
    document = null;
    
    TreeNodeChangeListener noLogging = new TreeNodeChangeListener() {
      
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
      }
      
      @Override
      public void nodeRemoved(TreeNodeRemovedEvent event) {
      }
      
      @Override
      public void nodeAdded(TreeNode node) {
      }
    };
    
    try {
      SBMLDocument document = reader.readSBMLFromString(xmlString, noLogging);
      if (document.getLevel() == -1) {
        document = null;
      }
    } catch (XMLStreamException ex) {
      // ignores exception for now; we use validator for more fine-grain descriptions
      messages.add(new ConformityMessage(ex.getMessage()));
    }
    
    // validates in case of error
    if (validating || document == null) {
      SBMLErrorLog errorLog = SBMLValidator.checkConsistency(xmlString);
      if (errorLog == null) {
        Level level = Level.WARN;
        String msg = "Cannot use validation service. If you are behind a proxy configure it with http://docs.oracle.com/javase/6/docs/technotes/guides/net/proxies.html";
        messages.add(new ConformityMessage(level, msg));
      } else {
        for (SBMLError error : errorLog.getValidationErrors()) {
          Level level = Level.toLevel(error.getSeverity());
          String msg = error.getMessage();
          messages.add(new ConformityMessage(level, msg));
        }
      }
    }
    
    if (!validating) {
      List<ConformityMessage> messages = getParseMessages(Level.ERROR);
      if (!messages.isEmpty()) {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid SBMLDocument ");
        for (ConformityMessage message : messages) {
          sb.append(message + "\n");
        }
        throw new SBMLException(sb.toString());
      }
    }
    
    return document;
  }

  // TODO: toString
  String toString(SBMLDocument document) {
    SBMLDocument nsDocument = PMFUtil.wrap(document.clone());
    for (Map.Entry<String, String> entry : PMFUtil.standardPrefixes.entrySet()) {
      String prefix = entry.getKey();
      String uri = entry.getValue();
      nsDocument.addDeclaredNamespace(prefix, uri);
    }
    PMFUtil.addStandardPrefixes(nsDocument);
    
    String xmlString = null;
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    SBMLWriter writer = new SBMLWriter();
    writer.write(nsDocument, stream);
    xmlString = stream.toString(StandardCharsets.UTF_8.name());

    return xmlString;
  }
  
  List<ConformityMessage> getParseMessages() {
    List<ConformityMessage> relevantMessages = new LinkedList<>();
    for (ConformityMessage message : messages) {
      if (message.getLevel().isGreaterOrEqual(Level.WARN)) {
        relevantMessages.add(message);
      }
    }
    return relevantMessages;
  }
  
  List<ConformityMessage> getParseMessages(Level level) {
    List<ConformityMessage> relevantMessages = new LinkedList<>();
    for (ConformityMessage message : messages) {
      if (message.getLevel().isGreaterOrEqual(level)) {
        relevantMessages.add(message);
      }
    }
    return relevantMessages;
  }

}
