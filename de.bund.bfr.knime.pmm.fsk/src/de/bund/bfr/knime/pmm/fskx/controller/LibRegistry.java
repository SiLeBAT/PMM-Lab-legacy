package de.bund.bfr.knime.pmm.fskx.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.FileLocator;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

import com.sun.jna.Platform;

import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.rbin.RBinUtil;
import de.bund.bfr.knime.pmm.fskx.rbin.preferences.RPreferenceInitializer;

/**
 * Singleton!! There can only be one.
 * 
 * @author Miguel Alba
 */
public class LibRegistry {
	
	private static LibRegistry instance;

	/** Installation path. */
	private final Path installPath;

	/** miniCRAN repository path. */
	private final Path repoPath;
	
	/** R path attribute: holds installation path. */
	private final String repoPathAttr;

	/** R repo attribute: holds remote repository. */
	private static final String reposAttr = "repos = 'http://cran.us.r-project.org'";

	/** R type attribute: holds repository attribute. */
	private static final String typeAttr = "type = 'win.binary'";

	/** Utility set to keep count of installed libraries. */
	private final Set<String> installedLibs = new HashSet<>();

	/** Utility RController for running R commands. */
	private final RController controller;

	private LibRegistry() throws IOException, RException {
		// Create directories
		installPath = Files.createTempDirectory("install");
		installPath.toFile().deleteOnExit();
		repoPath = Files.createTempDirectory("repo");
		repoPath.toFile().deleteOnExit();
		
		Properties properties = RBinUtil.retrieveRProperties();
		final String miniCranProp = properties.getProperty("miniCRAN.path");
		if (miniCranProp == null || miniCranProp.isEmpty()) {
			try {
				installMiniCRAN();
			} catch (IOException e) {
				RPreferenceInitializer.invalidateR3PreferenceProviderCache();
				throw new RException("Could not find and install miniCRAN package. "
						+ "Please install it manually in your R installation by running \"install.packages('miniCRAN')\".");
			}
		}

		// ... and initialize installation path attribute
		repoPathAttr = "path = '" + repoPath.toString().replace("\\", "/") + "'";

		controller = new RController();
		controller.eval("library(miniCRAN)");
		controller.eval("makeRepo(c(), " + repoPathAttr + ", " + reposAttr + ", " + typeAttr + ")");
	}
	
	/**
	 * Install miniCRAN just in case the R environment provided by the user does
	 * not have it installed.
	 * 
	 * @throws IOException
	 */
	private void installMiniCRAN() throws IOException {
		if (!Platform.isWindows()) {
			throw new RuntimeException("Non suppported platform, sorry." + System.getProperty("os.name"));
		}

		URL url = getClass().getResource("/de/bund/bfr/knime/pmm/fskx/res/miniCRAN_0.2.5.zip");
		url = FileLocator.toFileURL(url);
		String miniCranPath = url.toString();

		String rBinPath = RPreferenceInitializer.getR3Provider().getRBinPath("R");

		Runtime.getRuntime().exec(rBinPath + " CMD INSTALL " + miniCranPath);
	}
	
	public static LibRegistry instance() throws IOException, RException {
		if (instance == null) {
			instance = new LibRegistry();
		}
		return instance;
	}

	/**
	 * Returns whether an R library is installed.
	 * 
	 * @param libraryName
	 *            name of the R library
	 * @return whether an R library is installed
	 */
	public boolean isInstalled(final String libraryName) {
		return installedLibs.contains(libraryName);
	}

	/**
	 * Install a list of libraries into the repository.
	 * 
	 * @param libs
	 *            list of names of R libraries
	 * @throws RException
	 * @throws REXPMismatchException
	 */
	public void installLibs(final List<String> libs) throws RException, REXPMismatchException {

		final UnaryOperator<String> quoteOperator = astring -> '"' + astring + '"';

		// Creates R package list: c('pkg1', 'pkg2', ..., 'pkgN')
		final List<String> quotedLibs = libs.stream().map(quoteOperator).collect(Collectors.toList());
		final String pkgList = "c(" + String.join(",", quotedLibs) + ")";

		// Gets list of R dependencies of libs: c('dep1', 'dep2', ...,
		// 'depN')
		String cmd = "pkgDep(" + pkgList + ", availPkgs = cranJuly2014, " + typeAttr + ")";
		REXP rexp = controller.eval(cmd);
		List<String> deps = Arrays.asList(rexp.asStrings());
		List<String> quotedDeps = deps.stream().map(quoteOperator).collect(Collectors.toList());
		String depList = "c(" + String.join(",", quotedDeps) + ")";

		// Adds the dependencies to the miniCRAN repository
		controller.eval("addPackage(" + depList + ", " + repoPathAttr + ", " + reposAttr + "," + typeAttr + ")");

		// Gets the paths to the binaries of these dependencies
		rexp = controller.eval("checkVersions(" + depList + ", " + repoPathAttr + ", " + typeAttr + ")");
		List<String> paths = Arrays.stream(rexp.asStrings()).map(quoteOperator).collect(Collectors.toList());
		String fileList = "c(" + String.join(",", paths) + ")";

		// Install binaries
		String cmd2 = "install.packages(" + fileList + ", repos = NULL, lib = '"
				+ installPath.toString().replace("\\", "/") + "', " + typeAttr + ")";
		controller.eval(cmd2);

		// Adds names of installed libraries to utility set
		installedLibs.addAll(deps);
	}

	/**
	 * Gets list of paths to the binaries of the desired libraries.
	 * 
	 * @param libs
	 * @return list of paths to the binaries of the desired libraries
	 * @throws RException
	 * @throws REXPMismatchException
	 */
	public Set<Path> getPaths(List<String> libs) throws RException, REXPMismatchException {

		UnaryOperator<String> quoteOperator = astring -> "'" + astring + "'";

		// Gets list of R dependencies of libs
		List<String> quotedLibs = libs.stream().map(quoteOperator).collect(Collectors.toList());
		String libList = "c(" + String.join(",", quotedLibs) + ")";
		REXP rexp = controller.eval("pkgDep(" + libList + ", availPkgs = cranJuly2014, " + typeAttr + ")");

		// Gets the paths to the binaries of these dependencies
		List<String> deps = Arrays.stream(rexp.asStrings()).map(quoteOperator).collect(Collectors.toList());
		String depList = "c(" + String.join(",", deps) + ")";
		rexp = controller.eval("checkVersions(" + depList + ", " + repoPathAttr + ", " + typeAttr + ")");

		return Arrays.stream(rexp.asStrings()).map(path -> Paths.get(path)).collect(Collectors.toSet());
	}

	public Path getInstallationPath() {
		return installPath;
	}
}