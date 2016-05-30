/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.pmm.fskx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.knime.core.util.FileUtil;
import org.osgi.framework.BundleContext;

import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.controller.RController;

public class FSKNodePlugin extends AbstractUIPlugin {

  /** The plug-in ID. */
  public static final String PLUGIN_ID = "fskxNodePlugin";

  // The shared instance.
  private static FSKNodePlugin plugin;

  private static LibRegistry libRegistry;

  /**
   * This method is called upon plug-in activation.
   * 
   * @param context The OSGI bundle context
   * @throws Exception If this plugin could not be started
   */
  @Override
  public void start(final BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
    libRegistry = new LibRegistry();
  }

  /**
   * This method is called when the plug-in is stopped.
   * 
   * @param context The OSGI bundle context
   * @throws Exception If this plugin could not be stopped
   */
  @Override
  public void stop(final BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance.
   * 
   * @return Singleton instance of the Plugin
   */
  public static FSKNodePlugin getDefault() {
    return plugin;
  }

  public Set<Path> getLibs(String libName) throws IOException, RException {
    return libRegistry.get(libName);
  }

  class LibRegistry {

    /** Directory with uncompressed libraries */
    private File installPath;
    
    /** Directory with binary libraries */
    private Path binDir;

    /** Maps libraries names the paths of their zip files (including dependencies) */
    private Map<String, Set<Path>> libMap;

    public LibRegistry() throws IOException {
      libMap = new HashMap<>();
      installPath = FileUtil.createTempDir("installPath");
      binDir = FileUtil.createTempDir("bin").toPath();
    }

    /**
     * Returns the path to a binary library and its dependencies.
     * 
     * If installed returns directly the library path, otherwise the library is installed and then
     * returned.
     * 
     * @return paths to binary libraries
     * @throws RException
     * @throws IOException
     */
    public Set<Path> get(String libName) throws IOException, RException {
      if (libMap.containsKey(libName)) {
        return libMap.get(libName);
      } else {
        Set<Path> libs = installLibrary(libName); // set of new installed libraries (uncompressed)
        Set<Path> binLibs = new HashSet<>(libs.size());  // set of binary libraries (Zip)
        for (Path lib : libs) {
          binLibs.add(createZipFile(lib));
        }
        libMap.put(libName, binLibs);
        return binLibs;
      }
    }

    /**
     * Install a library and its dependencies.
     * 
     * @param libName name of the R library to be installed
     * @return the paths of the installed library and its dependencies
     * @throws IOException
     * @throws RException
     */
    private Set<Path> installLibrary(String libName) throws IOException, RException {
      String installPathString = installPath.getAbsolutePath().replace("\\", "/");
      
      Set<Path> libsBefore = Files.list(installPath.toPath()).filter(p -> Files.isDirectory(p)).collect(Collectors.toSet());
      
      // Builds install command: install.packages("libName")
      String installCmd = "install.packages(\"" + libName + "\", lib=\"" + installPathString
          + "\", repos=\"https://cloud.r-project.org/\")";

      // Install packages into the temporary directory
      try (RController controller = new RController()) {
        controller.eval(installCmd);
      }
      
      Set<Path> libsAfter = Files.list(installPath.toPath()).filter(p -> Files.isDirectory(p) && !libsBefore.contains(p)).collect(Collectors.toSet());
      
      return libsAfter;
    }

    /**
     * Compress an uncompressed R library into a zip file.
     * 
     * @param dir Directory of an uncompressed library
     * @return path of the zip file containing the binary library
     * @throws IOException
     */
    private Path createZipFile(Path dir) throws IOException {

      String pkgName = dir.getFileName().toString();
      Path zip = binDir.resolve(pkgName + ".zip");

      List<Path> paths = Files.walk(dir) // traverse all the files in dir
          .filter(p -> !Files.isDirectory(p)) // ignore directories
          .collect(Collectors.toList()); // collect all the files into a list

      try (ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(zip.toFile()))) {
        for (Path p : paths) {
          // Adds a ZipEntry with the contents of p

          // Creates entry name from the path to string starting with the pkgName.
          // E.g. C:\Temp\knime_randomPath1028\triangle\CITATION => triangle\CITATION
          String entryName = p.toString().substring(p.toString().indexOf(pkgName));

          // Write file
          stream.putNextEntry(new ZipEntry(entryName));
          stream.write(Files.readAllBytes(p));
          stream.closeEntry();
        }
      }

      return zip;
    }
  }
}
