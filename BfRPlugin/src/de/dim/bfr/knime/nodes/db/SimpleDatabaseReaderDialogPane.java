/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.knime.nodes.db;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;

/**
 *
 * @author Data In Motion
 */
class SimpleDatabaseReaderDialogPane extends NodeDialogPane {

    private final JComboBox databaseTableNameSelector = new JComboBox();
    private JPanel configPanel;
    private static final Logger LOG = Logger.getLogger(SimpleDatabaseReaderDialogPane.class);
    
    /** Config key for the option to run the SQL query during configure. */
    static final String EXECUTE_WITHOUT_CONFIGURE = "execute_without_configure";

    /**
     * Creates new dialog.
     * @param hasLoginPane true, if a login pane is visible, otherwise false
     */
    SimpleDatabaseReaderDialogPane() {
        super();
        configPanel = new JPanel();
        databaseTableNameSelector.setEditable(false);
        
        databaseTableNameSelector.addItem("Agenzien");
        databaseTableNameSelector.addItem("DoubleKennzahlen");
        databaseTableNameSelector.addItem("Einheiten");
        databaseTableNameSelector.addItem("GeschaetzteModelle");
        databaseTableNameSelector.addItem("GeschaetzteParameter");
        databaseTableNameSelector.addItem("GeschaetzteParameterCovCor");
        databaseTableNameSelector.addItem("GeschaetztesModell_Referenz");
        databaseTableNameSelector.addItem("Literatur");
        databaseTableNameSelector.addItem("Matrices");
        databaseTableNameSelector.addItem("Messwerte");
        databaseTableNameSelector.addItem("Modellkatalog");
        databaseTableNameSelector.addItem("ModellkatalogParameter");
        databaseTableNameSelector.addItem("Modell_Referenz");
        databaseTableNameSelector.addItem("Sekundaermodelle_Primaermodelle");
        databaseTableNameSelector.addItem("Versuchsbedingungen");
        
        JPanel dbTablePanel = new JPanel(new BorderLayout());
        dbTablePanel.setBorder(BorderFactory
                .createTitledBorder(" Database Table "));
        dbTablePanel.add(databaseTableNameSelector);
        configPanel.add(dbTablePanel);

        super.addTab("Settings", configPanel);
    }

    /**
     * @return false (default), or true if the option to run the SQL query
     *         without configure should be visible.
     */
    protected boolean runWithoutConfigure() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings,
            final PortObjectSpec[] specs) throws NotConfigurableException {
    	if(settings.containsKey(SimpleDatabaseConnectionSettings.TABLE_INDEX))
			try {
				databaseTableNameSelector.setSelectedIndex(settings.getInt(SimpleDatabaseConnectionSettings.TABLE_INDEX));
			} catch (InvalidSettingsException e) {
				//should not happen
				LOG.error(e.getMessage(), e);
			}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {

        settings.addString(SimpleDatabaseConnectionSettings.CFG_STATEMENT,
               "SELECT * FROM \"PUBLIC\".\""+databaseTableNameSelector.getSelectedItem().toString()+"\"");
        settings.addString(SimpleDatabaseReaderNodeModel.CFG_STATEMENT, 
        		"SELECT * FROM \"PUBLIC\".\""+databaseTableNameSelector.getSelectedItem().toString()+"\"");

        settings.addInt(SimpleDatabaseConnectionSettings.TABLE_INDEX,databaseTableNameSelector.getSelectedIndex());
    }
}
