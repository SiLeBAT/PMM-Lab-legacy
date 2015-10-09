package de.bund.bfr.knime.pmm.openfsmr;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class OpenFSMRConverterNodePlugin extends Plugin {

	// The shared instance
	private static OpenFSMRConverterNodePlugin plugin;
	
	/**
	 * The constructor
	 */
	public OpenFSMRConverterNodePlugin() {
		super();
		plugin = this;
	}
	
	/**
	 * This method is calle upon plug-in activation.
	 * 
	 * @param context The OSFI bundle context
	 * @throws Exception If this plugin could not be started
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
	}
	
	/**
	 * This method is called when the plug-in is stopped.
	 * 
	 * @param context The OSGI bundle context
	 * @throws Exception If this plugin could not bet stopped
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}
	
	/**
	 * Returns the shared instance.
	 * 
	 * @return Singleton instance of the Plugin.
	 */
	public static OpenFSMRConverterNodePlugin getDefault() {
		return plugin;
	}
}
