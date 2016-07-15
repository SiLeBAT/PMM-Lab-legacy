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
package de.bund.bfr.knime.pmm.sbmlwriter;

import javax.swing.JFileChooser;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentOptionalString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeFactory</code> for the "SBMLWriter" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class SBMLWriterNodeFactory extends NodeFactory<SBMLWriterNodeModel> {

  /**
   * {@inheritDoc}
   */
  @Override
  public SBMLWriterNodeModel createNodeModel() {
    return new SBMLWriterNodeModel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getNrNodeViews() {
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NodeView<SBMLWriterNodeModel> createNodeView(final int viewIndex,
      final SBMLWriterNodeModel nodeModel) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasDialog() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NodeDialogPane createNodeDialogPane() {
    // Create components
    DialogComponentFileChooser outComp =
        new DialogComponentFileChooser(new SettingsModelString(SBMLWriterNodeModel.CFG_OUT_PATH,
            null), "Out History", JFileChooser.SAVE_DIALOG, true);
    outComp.setBorderTitle("Output Path");
    
    DialogComponentBoolean overwriteComp =
        new DialogComponentBoolean(new SettingsModelBoolean(SBMLWriterNodeModel.CFG_OVERWRITE,
            false), "Overwrite OK");
    
    DialogComponentOptionalString varParamComp =
        new DialogComponentOptionalString(new SettingsModelOptionalString(
            SBMLWriterNodeModel.CFG_VARIABLE_PARAM, null, false), "Initial Concentration Parameter");
    
    DialogComponentString givenNameComp =
        new DialogComponentString(new SettingsModelString(
            SBMLWriterNodeModel.CFG_CREATOR_GIVEN_NAME, null), "Creator Given Name");
    
    DialogComponentString familyNameComp =
        new DialogComponentString(new SettingsModelString(
            SBMLWriterNodeModel.CFG_CREATOR_FAMILY_NAME, null), "Creator Family Name");
    
    DialogComponentString creatorContactComp =
        new DialogComponentString(new SettingsModelString(SBMLWriterNodeModel.CFG_CREATOR_CONTACT,
            null), "Creator Contact");
    
    DialogComponentDate createdComp =
        new DialogComponentDate(new SettingsModelDate(SBMLWriterNodeModel.CFG_CREATED_DATE),
            "Created");
    
    DialogComponentDate modifiedComp =
        new DialogComponentDate(new SettingsModelDate(SBMLWriterNodeModel.CFG_LAST_MODIFIED_DATE),
            "Last Modified");
    
    DialogComponentMultiLineString refComp =
        new DialogComponentMultiLineString(new SettingsModelString(
            SBMLWriterNodeModel.CFG_REFERENCE, null), "Reference as XHTML");

    DefaultNodeSettingsPane pane = new DefaultNodeSettingsPane();
    pane.addDialogComponent(outComp);
    pane.addDialogComponent(overwriteComp);
    pane.addDialogComponent(varParamComp);
    pane.addDialogComponent(givenNameComp);
    pane.addDialogComponent(familyNameComp);
    pane.addDialogComponent(creatorContactComp);
    pane.addDialogComponent(createdComp);
    pane.addDialogComponent(modifiedComp);
    pane.addDialogComponent(refComp);
    
    return pane;
  }

}
