/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.pmfwriter;

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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.pmm.common.ui.DateInputDialog;

public class PMFWriterNodeDialog extends DefaultNodeSettingsPane {

	private static final String OUT_HISTORY = "Out History";

	/**
	 * New pane for configuring the SBMLWriter node.
	 */
	public PMFWriterNodeDialog() {

		SettingsModelString outSettings = new SettingsModelString(PMFWriterNodeModel.CFG_OUT_PATH, null);
		DialogComponentFileChooser outComp = new DialogComponentFileChooser(outSettings, OUT_HISTORY,
				JFileChooser.SAVE_DIALOG, true);

		SettingsModelString nameSettings = new SettingsModelString(PMFWriterNodeModel.CFG_MODEL_NAME, null);
		DialogComponentString nameComp = new DialogComponentString(nameSettings, "File Name");

		SettingsModelString givenNameSettings = new SettingsModelString(PMFWriterNodeModel.CFG_CREATOR_GIVEN_NAME,
				null);
		DialogComponentString givenNameComp = new DialogComponentString(givenNameSettings, "Creator Given Name");

		SettingsModelString familyNameSettings = new SettingsModelString(PMFWriterNodeModel.CFG_CREATOR_FAMILY_NAME,
				null);
		DialogComponentString familyNameComp = new DialogComponentString(familyNameSettings, "Creator Family Name");

		SettingsModelString creatorContactSettings = new SettingsModelString(PMFWriterNodeModel.CFG_CREATOR_CONTACT,
				null);
		DialogComponentString creatorContactComp = new DialogComponentString(creatorContactSettings, "Creator Contact");

		SettingsModelDate createdDateModel = new SettingsModelDate(PMFWriterNodeModel.CFG_CREATED_DATE);
		DialogComponentDate createdComp = new DialogComponentDate(createdDateModel, "Created");

		SettingsModelDate modifiedDateModel = new SettingsModelDate(PMFWriterNodeModel.CFG_LAST_MODIFIED_DATE);
		DialogComponentDate modifiedComp = new DialogComponentDate(modifiedDateModel, "Last Modified");

		SettingsModelBoolean isSecondary = new SettingsModelBoolean(PMFWriterNodeModel.CFG_ISSECONDARY, false);
		DialogComponentBoolean isSecondaryCheckbox = new DialogComponentBoolean(isSecondary, "Is secondary?");

		SettingsModelBoolean overwrite = new SettingsModelBoolean(PMFWriterNodeModel.CFG_OVERWRITE, true);
		DialogComponentBoolean overwriteCheckbox = new DialogComponentBoolean(overwrite, "Overwrite, ok?");

		SettingsModelBoolean splitModels = new SettingsModelBoolean(PMFWriterNodeModel.CFG_SPLITMODELS, false);
		DialogComponentBoolean splitCheckbox = new DialogComponentBoolean(splitModels, "Split top level models?");

		SettingsModelString referenceLink = new SettingsModelString(PMFWriterNodeModel.CFG_REFERENCE_LINK, null);
		DialogComponentString referenceLinkComp = new DialogComponentString(referenceLink,
				"Model reference description link");

		SettingsModelString license = new SettingsModelString(PMFWriterNodeModel.CFG_LIC, null);
		DialogComponentString licenseComp = new DialogComponentString(license, "License");

		SettingsModelString notes = new SettingsModelString(PMFWriterNodeModel.CFG_NOTES, null);
		DialogComponentMultiLineString notesComp = new DialogComponentMultiLineString(notes, "Notes");

		outComp.setBorderTitle("Output Path");

		addDialogComponent(outComp);
		addDialogComponent(nameComp);
		addDialogComponent(isSecondaryCheckbox);
		addDialogComponent(overwriteCheckbox);
		addDialogComponent(splitCheckbox);
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
