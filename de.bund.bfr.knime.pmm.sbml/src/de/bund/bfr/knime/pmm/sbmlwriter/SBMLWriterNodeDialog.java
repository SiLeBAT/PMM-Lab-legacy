/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
 ******************************************************************************/
package de.bund.bfr.knime.pmm.sbmlwriter;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentDate;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentOptionalString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "SBMLWriter" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class SBMLWriterNodeDialog extends DefaultNodeSettingsPane {

	private static final String OUT_HISTORY = "Out History";

	/**
	 * New pane for configuring the SBMLWriter node.
	 */
	protected SBMLWriterNodeDialog() {
		DialogComponentFileChooser outComp = new DialogComponentFileChooser(
				new SettingsModelString(SBMLWriterNodeModel.CFG_OUT_PATH, null),
				OUT_HISTORY, JFileChooser.SAVE_DIALOG, true);
		DialogComponentOptionalString varParamComp = new DialogComponentOptionalString(
				new SettingsModelOptionalString(
						SBMLWriterNodeModel.CFG_VARIABLE_PARAM, null, false),
				"Initial Concentration Parameter");
		DialogComponentString nameComp = new DialogComponentString(
				new SettingsModelString(SBMLWriterNodeModel.CFG_MODEL_NAME,
						null), "File Name");
		DialogComponentString givenNameComp = new DialogComponentString(
				new SettingsModelString(
						SBMLWriterNodeModel.CFG_CREATOR_GIVEN_NAME, null),
				"Creator Given Name");
		DialogComponentString familyNameComp = new DialogComponentString(
				new SettingsModelString(
						SBMLWriterNodeModel.CFG_CREATOR_FAMILY_NAME, null),
				"Creator Family Name");
		DialogComponentString creatorContactComp = new DialogComponentString(
				new SettingsModelString(
						SBMLWriterNodeModel.CFG_CREATOR_CONTACT, null),
				"Creator Contact");
		DialogComponentDate createdComp = new DialogComponentDate(
				new SettingsModelDate(SBMLWriterNodeModel.CFG_CREATED_DATE),
				"Created");
		DialogComponentDate modifiedComp = new DialogComponentDate(
				new SettingsModelDate(
						SBMLWriterNodeModel.CFG_LAST_MODIFIED_DATE),
				"Last Modified");

		outComp.setBorderTitle("Output Path");

		addDialogComponent(outComp);
		addDialogComponent(varParamComp);
		addDialogComponent(nameComp);
		addDialogComponent(givenNameComp);
		addDialogComponent(familyNameComp);
		addDialogComponent(creatorContactComp);
		addDialogComponent(createdComp);
		addDialogComponent(modifiedComp);
	}
}
