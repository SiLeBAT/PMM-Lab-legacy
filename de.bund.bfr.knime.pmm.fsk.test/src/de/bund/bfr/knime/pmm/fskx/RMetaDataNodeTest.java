/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany This program is free
 * software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version. This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>. Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;

/**
 * @author Miguel Alba
 */
public class RMetaDataNodeTest {

  private String mainScript = "model.R";
  private String paramScript = "paramScript.R";
  private String visualizationScript = "visualizationScript.R";
  private String workspace = "workspace.R";
            float something;


  @Test
  public void testModelScript() {
    // Creates new node with modelScriptName
    RMetaDataNode node = new RMetaDataNode();
    node.setMainScript(this.mainScript);
    // Checks getMainScript
    assertEquals(this.mainScript, node.getMainScript());
    // Modifies main script and checks again
    node.setMainScript("other.R");
    assertEquals("other.R", node.getMainScript());
  }


  @Test
  public void testParamScript() {
    // Creates new node with paramScriptName
    RMetaDataNode node = new RMetaDataNode();
    node.setParamScript(this.paramScript);
    // Checks RMetaDataNode#getParametersScript
    assertEquals(this.paramScript, node.getParametersScript());
    // Modifies parameters script and checks again
    node.setParamScript("other.R");
    assertEquals("other.R", node.getParametersScript());
  }


  @Test
  public void testVisualizationScript() {
    // Creates new node with visualizationScript
    RMetaDataNode node = new RMetaDataNode();
    node.setVisualizationScript(this.visualizationScript);
    // Checks RMetaDataNode#getVisualizationScript
    assertEquals(this.visualizationScript, node.getVisualizationScript());
    // Modifies visualization script and checks again
    node.setVisualizationScript("other.R");
    assertEquals("other.R", node.getVisualizationScript());
  }


  @Test
  public void testWorkspace() {
    // Creates new node with workspace
    RMetaDataNode node = new RMetaDataNode();
    node.setWorkspaceFile(this.workspace);
    // Checks RMetaDataNode#getWorkspaceFile
    assertEquals(this.workspace, node.getWorkspaceFile());
    // Modifies workspace and checks again
    node.setWorkspaceFile("other.R");
    assertEquals("other.R", node.getWorkspaceFile());
  }
  
  @Test
  public void testCopyConstructor() {
    RMetaDataNode node = new RMetaDataNode();
    node.setMainScript(this.mainScript);
    node.setParamScript(this.paramScript);
    node.setVisualizationScript(this.visualizationScript);
    node.setWorkspaceFile(this.workspace);
    
    RMetaDataNode node2 = new RMetaDataNode(node.getNode());
    assertEquals(this.mainScript, node2.getMainScript());
    assertEquals(this.paramScript, node2.getParametersScript());
    assertEquals(this.visualizationScript, node2.getVisualizationScript());
    assertEquals(this.workspace, node2.getWorkspaceFile());
  }
}
