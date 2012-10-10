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
/* @(#)$RCSfile$ 
 * $Revision$ $Date$ $Author$
 *
 */
package de.dim.knime.bfr.internal;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.dim.bfr.external.service.BFRNodeService;

/**
 * This is the eclipse bundle activator.
 * Note: KNIME node developers probably won't have to do anything in here, 
 * as this class is only needed by the eclipse platform/plugin mechanism.
 * If you want to move/rename this file, make sure to change the plugin.xml
 * file in the project root directory accordingly.
 *
 * @author Data In Motion UG 
 */
public class BfRNodePluginActivator extends AbstractUIPlugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "com.dim.knime.bfr"; //$NON-NLS-1$
	
//	public static final String R_PACKAGE_PATH = "R_PACKAGE_PATH";
    public static final String R_PATH = "R_PATH";
    public static final String DB_LOCATION = "DB_LOCATION";
	public static final String DB_PATH = "DB_PATH";
	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String DB_DRIVER = "DRIVER_NAME";
	public static final String CRED_NAME = "CREDENTIALS_NAME";
	public static final String DB_LOADED_DRIVER = "LOADED_DRIVER";

	// The shared instance.
    private static BfRNodePluginActivator plugin;
	private static BFRNodeService service;
	private static ServiceTracker tracker;
	
    private static File rExecutable;
    //private static File rPackageFile;

    /**
     * @return R executable
     */
    public static File getRExecutable() {
        return rExecutable;
    }

    /**
     * The constructor.
     */
    public BfRNodePluginActivator() {
        super();
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation.
     * 
     * @param context The OSGI bundle context
     * @throws Exception If this plugin could not be started
     */
    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        Bundle jpaBundle = Platform.getBundle("de.dim.bfr.jpa");
		if (jpaBundle.getState() < Bundle.STARTING)
			jpaBundle.start();
		tracker = new ServiceTracker(context, BFRNodeService.class.getName(), null);
		tracker.open();
    }
    /**
     * This method is called when the plug-in is stopped.
     * 
     * @param context The OSGI bundle context
     * @throws Exception If this plugin could not be stopped
     */
    @Override
    public void stop(final BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
        service = null;
		tracker.close();
    }

    /**
     * Returns the shared instance.
     * 
     * @return Singleton instance of the Plugin
     */
    public static BfRNodePluginActivator getDefault() {
        return plugin;
    }
    
	public static BFRNodeService getBfRService(){
		if (service == null) {
			service = (BFRNodeService) tracker.getService();
		}
		return service;
	}
	
	/**
	 * Sets the service from the DS framework
	 * @param service the BFR service
	 */
	public static void setBFRService(BFRNodeService service) {
		BfRNodePluginActivator.service = service;
	}
	
	/**
	 * Unsets the service from the DS framework
	 * @param service the BFR service
	 */
	public static void unsetBFRService(BFRNodeService service) {
		BfRNodePluginActivator.service = null;
	}

//    public static File getRPackageFile() {
//        return new File(BfRNodePluginActivator.getDefault().getPreferenceStore().getString(R_PACKAGE_PATH));
//    }
}

