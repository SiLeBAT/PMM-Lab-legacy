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
package de.dim.bfr.jpa.internal;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.persistence.config.PersistenceUnitProperties;
//import org.hsh.bfr.db.DBKernel;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.dim.bfr.external.service.BFRNodeService;
import de.dim.bfr.jpa.entities.JPALiteratur;
import de.dim.bfr.jpa.service.BFRNodeServiceImpl;
import de.dim.bfr.jpa.service.BFRUIServiceImpl;
import de.dim.bfr.ui.services.BFRUIService;

public class BFRJPAActivator implements BundleActivator {

	public static final String KNIME_PLUGIN_ID = "de.dim.bfr.knime";
	private static BundleContext context;
	private static BFRJPAActivator instance;
	private BFRUIServiceImpl uiService;
	private BFRNodeServiceImpl nodeService;
	
	private PersistenceProvider provider;
	private EntityManagerFactory emf = null;

	private Map<String, Object> storedDatabasePropeties = null;
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		instance = this;
		BFRJPAActivator.context = bundleContext;
		// simulate auto-start behavior of the launch config
		Bundle bundle = Platform.getBundle("org.eclipse.persistence.jpa");
		if (bundle != null && (bundle.getState() < Bundle.STARTING || nodeService == null || uiService == null)) { // BFRUIActivator.getBFRService() == null ... old - StatUp
			bundle.start();
			// get the persistence service as OSGi service
			ServiceReference serviceReference = context.getServiceReference(PersistenceProvider.class.getName());
			provider = (PersistenceProvider) context.getService(serviceReference);
			
			uiService = new BFRUIServiceImpl();
			uiService.refreshServiceContent();
			nodeService = new BFRNodeServiceImpl();
			context.registerService(BFRUIService.class.getName(), uiService, null);
			context.registerService(BFRNodeService.class.getName(), nodeService, null);
		}
	}

	/**
	 * Reads the properties for the database connection
	 * 
	 * @return
	 */
	private Map<String, Object> readDBProperties() {

		String dbLocation = Platform.getPreferencesService().getString(KNIME_PLUGIN_ID, "DB_PATH", null, null);
		if (dbLocation == null || dbLocation.isEmpty()) {
			return getInternalDBProperties();
		}
		
		HashMap<String,Object> props = new HashMap<String,Object>();
		props.put("javax.persistence.jdbc.driver", Platform.getPreferencesService().getString(KNIME_PLUGIN_ID, "DRIVER_NAME", "org.hsqldb.jdbcDriver", null));
		props.put("javax.persistence.jdbc.url", Platform.getPreferencesService().getString(KNIME_PLUGIN_ID, "DB_LOCATION", "org.hsqldb.jdbcDriver", null) + dbLocation + ";shutdown=true");
		props.put("javax.persistence.jdbc.password", Platform.getPreferencesService().getString(KNIME_PLUGIN_ID, "PASSWORD", "", null));
		props.put("javax.persistence.jdbc.user", Platform.getPreferencesService().getString(KNIME_PLUGIN_ID, "USERNAME", "sa", null));
		
		return props;
	}
	private Map<String, Object> getInternalDBProperties() {
		// Get the bundle this class belongs to.
		String dbFile = null;
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		try {
			// Get the URL to the file
			//URL incURLInternalDBFolder = FileLocator.find(bundle, new Path("/pmmDB"), null); 
			URL incURLfirstDB = bundle.getResource("org/hsh/bfr/db/res/firstDB.tar.gz");
			if (incURLfirstDB == null) { // incURLInternalDBFolder == null || 
				return null;
			}
			// Create a file object from the URL
			// TODO: hier muss noch ein Upgrade bei neuen DB Versionen realisiert werden!!!!
			//String knimeSubPath = "knime" + System.getProperty("file.separator");
			//String internalPath = DBKernel.HSHDB_PATH.endsWith(knimeSubPath) ? DBKernel.HSHDB_PATH : DBKernel.HSHDB_PATH + knimeSubPath;
			String internalPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() +
						System.getProperty("file.separator") + ".pmmlabDB" + System.getProperty("file.separator");
			File incFileInternalDBFolder = new File(internalPath);
			if (!incFileInternalDBFolder.exists()) {
				incFileInternalDBFolder.mkdirs();
			}
			File incFilefirstDB = new File(FileLocator.toFileURL(incURLfirstDB).getPath());
			// folder is empty? Create database!
			if (incFileInternalDBFolder.list().length == 0) {
				try {
					org.hsqldb.lib.tar.DbBackup.main(new String[]{
							"--extract",
							incFilefirstDB.getAbsolutePath(),
							incFileInternalDBFolder.getAbsolutePath()});
				}
				catch (Exception e) {
					throw new IllegalStateException("Creation of internal database not succeeded.", e);
				}
			}
			dbFile = internalPath + "DB";
		}
		catch (IOException e) {
			throw new IllegalStateException("Cannot locate necessary internal database path.", e);
		}
		/*
		if (dbFile == null) {
			return null;
		}
		*/
		HashMap<String,Object> props = new HashMap<String,Object>();
		props.put("javax.persistence.jdbc.driver", "org.hsqldb.jdbcDriver");
		props.put("javax.persistence.jdbc.url", "jdbc:hsqldb:file:" + dbFile + ";shutdown=true");
		//props.put("javax.persistence.jdbc.password", DBKernel.tempSAPass);
		//props.put("javax.persistence.jdbc.user", DBKernel.tempSA);
		
		return props;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		uiService.shutdown();
		nodeService.shutdown();
		if(emf != null) {
			emf.close();
		}
		BFRJPAActivator.context = null;
	}

	public static BFRJPAActivator getDefault() {
		return instance;
	}

	public BFRUIServiceImpl getUIService() {
		return uiService;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		if(emf == null) {
			obtainEntityManagerFactory();
		}
		return emf;
	}

	private void obtainEntityManagerFactory() {

		storedDatabasePropeties = readDBProperties();
		if(storedDatabasePropeties != null){
			Map<String, Object> dbProperies = new HashMap<String, Object>();
			// get entity manager factory from the provider instead of the Persistence class
			// get the bundle's classloader 
			dbProperies.put(PersistenceUnitProperties.CLASSLOADER, JPALiteratur.class.getClassLoader());
			dbProperies.putAll(storedDatabasePropeties);
			emf = provider.createEntityManagerFactory("com.dim.bfr.model.jpa", dbProperies);
		} else {
			//TODO: give some information to the user
		}
	}

	public void refreshEntityManagerFactory() {
		Map<String, Object> newProperties = readDBProperties();
		if(storedDatabasePropeties != null && !storedDatabasePropeties.equals(newProperties)){
			emf.close();
			emf = null;
		}
	}

}
