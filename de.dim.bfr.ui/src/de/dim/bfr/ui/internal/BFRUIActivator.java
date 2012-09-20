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
package de.dim.bfr.ui.internal;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.dim.bfr.ui.services.BFRUIService;



/**
 * The activator class controls the plug-in life cycle
 */
public class BFRUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.dim.bfr.ui"; //$NON-NLS-1$

	// The shared instance
	private static BFRUIActivator plugin;
	private static BFRUIService service;
	private static ServiceTracker tracker;
	
	/**
	 * The constructor
	 */
	public BFRUIActivator() {
	}

	/*
	 * (non-Javadoc)  
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Bundle jpaBundle = Platform.getBundle("de.dim.bfr.jpa"); //$NON-NLS-1$
		if (jpaBundle != null && jpaBundle.getState() < Bundle.STARTING)
			jpaBundle.start();
		plugin = this;
		tracker = new ServiceTracker(context, BFRUIService.class.getName(), null);
		tracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		service = null;
		tracker.close();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BFRUIActivator getDefault() {
		return plugin;
	}
	
	/**
	 * Returns the image descriptor of the given path for this plugin
	 * @param path the image path play
	 * @return the image descriptor of the given path for this plugin
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Returns the {@link BFRUIService} instance
	 * @return the {@link BFRUIService} instance
	 */
	public static BFRUIService getBFRService() {
		if (service == null) {
			service = (BFRUIService) tracker.getService();
//			service = new BFRUIServiceImpl();
		}
		return service;
	}
	
	/**
	 * Sets the service from the DS framework
	 * @param service the BFR service
	 */
	public static void setBFRService(BFRUIService service) {
		BFRUIActivator.service = service;
	}
	
	/**
	 * Unsets the service from the DS framework
	 * @param service the BFR service
	 */
	public static void unsetBFRService(BFRUIService service) {
		BFRUIActivator.service = null;
	}

}
