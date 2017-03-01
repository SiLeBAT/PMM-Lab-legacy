package de.bund.bfr.knime.pmm.modelcatalog;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ModelCatalogNodePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.bund.bfr.knime.pmm.modelcatalog"; //$NON-NLS-1$

	// The shared instance
	private static ModelCatalogNodePlugin plugin;
	
	/**
	 * The constructor
	 */
	public ModelCatalogNodePlugin() {
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
	public static ModelCatalogNodePlugin getDefault() {
		return plugin;
	}

}
