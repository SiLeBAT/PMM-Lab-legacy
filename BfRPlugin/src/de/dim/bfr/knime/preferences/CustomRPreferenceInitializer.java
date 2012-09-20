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
package de.dim.bfr.knime.preferences;


import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 *
 * @author Kilian Thiel, University of Konstanz
 */
public class CustomRPreferenceInitializer extends AbstractPreferenceInitializer {
    

    /**
     * {@inheritDoc}
     */
	@Override
    public void initializeDefaultPreferences() 
    {
		IPreferenceStore store = BfRNodePluginActivator.getDefault().getPreferenceStore();
    	store.setDefault(BfRNodePluginActivator.R_PATH,"");
    	store.setDefault(BfRNodePluginActivator.DB_PATH,"");
    	store.setDefault(BfRNodePluginActivator.USERNAME,"");
    	store.setDefault(BfRNodePluginActivator.PASSWORD,"");
    	
    	store.setDefault(BfRNodePluginActivator.DB_DRIVER,"org.hsqldb.jdbc.JDBCDriver");
    }
}
