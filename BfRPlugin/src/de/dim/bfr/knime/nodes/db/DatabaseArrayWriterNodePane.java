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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;

public class DatabaseArrayWriterNodePane extends NodeDialogPane {

    //private final JComboBox chosePanel = new JComboBox();
    //private JPanel configPanel;
    
    public static String UPDATE = "Update if primary key is equal";
    public static String INSERT_AS_NEW = "Handle every row as new row";
    
    /**
     * Creates new dialog.
     * @param hasLoginPane true, if a login pane is visible, otherwise false
     */
    DatabaseArrayWriterNodePane() {
        super();
//        configPanel = new JPanel();
//        chosePanel.setEditable(false);
//        
//        chosePanel.addItem(UPDATE);
//        chosePanel.addItem(INSERT_AS_NEW);
//        
//        JPanel dbTablePanel = new JPanel(new BorderLayout());
//        dbTablePanel.setBorder(BorderFactory
//                .createTitledBorder("Handling of existing Rows"));
//        dbTablePanel.add(chosePanel);
//        configPanel.add(dbTablePanel);
//
//        super.addTab("Settings", configPanel);
    }

    /**
     * @return false (default), or true if the option to run the SQL query
     *         without configure should be visible.
     */
    protected boolean runWithoutConfigure() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings,
            final PortObjectSpec[] specs) throws NotConfigurableException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {

//        settings.addString("test",chosePanel.getSelectedItem().toString());
    }
}
