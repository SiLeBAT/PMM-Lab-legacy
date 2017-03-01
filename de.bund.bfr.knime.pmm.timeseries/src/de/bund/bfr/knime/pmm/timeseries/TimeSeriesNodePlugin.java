package de.bund.bfr.knime.pmm.timeseries;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * This is the eclipse bundle activator.
 * Note: KNIME node developers probably won't have to do anything in here, 
 * as this class is only needed by the eclipse platform/plugin mechanism.
 * If you want to move/rename this file, make sure to change the plugin.xml
 * file in the project root directory accordingly.
 *
 * @author Christian Thoens
 */
public class TimeSeriesNodePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.bund.bfr.knime.pmm.timeseries"; //$NON-NLS-1$

	// The shared instance
	private static TimeSeriesNodePlugin plugin;
	
	/**
	 * The constructor
	 */
	public TimeSeriesNodePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TimeSeriesNodePlugin getDefault() {
		return plugin;
	}

}
