/*******************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.pmm.pmfwriter.fsk;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.pmm.common.ui.DateInputDialog;

class NodeDialog extends DefaultNodeSettingsPane {

  NodeDialog() {
    NodeSettings settings = new NodeSettings();

    DialogComponentFileChooser outComp = new DialogComponentFileChooser(settings.outSettings,
        "Out history", JFileChooser.SAVE_DIALOG, true);
    outComp.setBorderTitle("Output Path");
    DialogComponentString nameComp = new DialogComponentString(settings.modelName, "File name");
    DialogComponentString givenNameComp =
        new DialogComponentString(settings.givenName, "Creator given name");
    DialogComponentString familyNameComp =
        new DialogComponentString(settings.familyName, "Creator family name");
    DialogComponentString creatorContactComp =
        new DialogComponentString(settings.contact, "Creator contact");
    DialogComponentDate createdComp =
        new DialogComponentDate(settings.createdDate, "Created");
    DialogComponentDate modifiedComp =
        new DialogComponentDate(settings.modifiedDate, "Last modified");
    DialogComponentBoolean isSecondaryCheckbox =
        new DialogComponentBoolean(settings.isSecondary, "Is secondary?");
    DialogComponentBoolean overwriteCheckbox =
        new DialogComponentBoolean(settings.overwrite, "Overwrite, ok?");
    DialogComponentBoolean splitCheckbox =
        new DialogComponentBoolean(settings.splitModels, "Split top level models?");
    DialogComponentString referenceLinkComp =
        new DialogComponentString(settings.referenceLink, "Model reference description link");
    DialogComponentString licenseComp = new DialogComponentString(settings.license, "License");
    DialogComponentMultiLineString notesComp =
        new DialogComponentMultiLineString(settings.notes, "Notes");

    addDialogComponent(outComp);
    addDialogComponent(nameComp);
    addDialogComponent(isSecondaryCheckbox);
    addDialogComponent(overwriteCheckbox);
    addDialogComponent(splitCheckbox);
    
    createNewTab("File metadata");
    addDialogComponent(givenNameComp);
    addDialogComponent(familyNameComp);
    addDialogComponent(creatorContactComp);
    addDialogComponent(referenceLinkComp);
    addDialogComponent(licenseComp);
    addDialogComponent(notesComp);
    addDialogComponent(createdComp);
    addDialogComponent(modifiedComp);
  }
  
  /**
   * Customized DialogComponentDate (
   * {@link org.knime.core.node.defaultnodesettings.DialogComponentDate}) that only shows the date.
   * 
   * It currently uses a workaround class, {@link de.bund.bfr.knime.pmm.common.ui.DateInputDialog}
   * until the original class is fixed in KNIME.
   *
   * @author Sebastian Peter, University of Konstanz
   * @since 2.8
   */
  private class DialogComponentDate extends DialogComponent {

    private DateInputDialog m_dialogcomp;

    private SettingsModelDate m_datemodel;

    /**
     * Instantiates a new DateDialogComponent, where the model stores the user input and the label is
     * put as a description on to the dialog. Using this constructor the date is optional.
     *
     * @param model to store the inputed date
     * @param label to place on the dialog
     */
    public DialogComponentDate(final SettingsModelDate model, final String label) {
      this(model, label, true);
    }

    /**
     * Instantiates a new DateDialogComponent, where the model stores the user input and the label is
     * put as a description on to the dialog. Using this constructor the date can be optional or
     * mandatory.
     *
     *
     * @param model to store the inputed date
     * @param label to place on the dialog
     * @param optional specifies whether the date is optional (true) or mandatory (false)
     */
    public DialogComponentDate(final SettingsModelDate model, final String label,
        final boolean optional) {
      super(model);
      m_datemodel = model;
      m_dialogcomp = new DateInputDialog(DateInputDialog.Mode.NOTIME, optional);
      
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), label));
      panel.add(m_dialogcomp);
      getComponentPanel().add(panel);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponent() {
      m_dialogcomp.setDateAndMode(m_datemodel.getTimeInMillis(),
          DateInputDialog.getModeForStatus(m_datemodel.getSelectedFields()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettingsBeforeSave() throws InvalidSettingsException {
      m_datemodel.setTimeInMillis(m_dialogcomp.getSelectedDate().getTime());
      m_datemodel.setSelectedFields(m_dialogcomp.getIntForStatus());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkConfigurabilityBeforeLoad(final PortObjectSpec[] specs)
        throws NotConfigurableException {
      // nothing todo here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setEnabledComponents(final boolean enabled) {
      m_dialogcomp.setEnabled(enabled);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToolTipText(final String text) {
      // todo !?

    }
  }
}
