package de.bund.bfr.knime.pmm.fskx.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

import com.sun.jna.Platform;

import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;

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

	/** Utility set to keep count of installed libraries. */
	private final Set<String> installedLibs = new HashSet<>();

	/** Utility RController for running R commands. */
	private final RController controller;

	private String type;

	private LibRegistry() throws IOException, RException {
		// Create directories
		installPath = Files.createTempDirectory("install");
		repoPath = Files.createTempDirectory("repo");
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());

		if (Platform.isWindows()) {
			type = "win.binary";
		} else if (Platform.isMac()) {
			type = "mac.binary";
		} else {
			type = "source";
		}

		controller = new RController();
		controller.eval(RCommandBuilder.library("miniCRAN"));
		controller.eval(RCommandBuilder.makeRepo(repoPath, "http://cran.us.r-project.org", type));
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

		// Gets list of R dependencies of libs: c('dep1', 'dep2', ..., 'depN')
		REXP rexp = controller.eval(RCommandBuilder.pkgDep(libs, type));
		List<String> deps = Arrays.asList(rexp.asStrings());

		// Adds the dependencies to the miniCRAN repository
		controller.eval(RCommandBuilder.addPackage(deps, repoPath, "http://cran.us.r-project.org", type));

		// Gets the paths to the binaries of these dependencies
		rexp = controller.eval(RCommandBuilder.checkVersions(deps, repoPath, type));
		List<String> paths = Arrays.asList(rexp.asStrings());

		// Install binaries
		controller.eval(RCommandBuilder.installPackages(paths, installPath, type));

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
		// Gets list of R dependencies of libs
		REXP rexp = controller.eval(RCommandBuilder.pkgDep(libs, type));
		String[] deps = controller.eval(RCommandBuilder.pkgDep(libs, type)).asStrings();

		// Gets the paths to the binaries of these dependencies
		rexp = controller.eval(RCommandBuilder.checkVersions(Arrays.asList(deps), repoPath, type));

		return Arrays.stream(rexp.asStrings()).map(path -> Paths.get(path)).collect(Collectors.toSet());
	}

	public Path getInstallationPath() {
		return installPath;
	}

	public Path getRepositoryPath() {
		return repoPath;
	}

	/**
	 * Shutdown hook that deletes recursively the directories used to download
	 * and install the R libraries, repository and installation paths
	 * respectively.
	 */
	private class ShutdownHook extends Thread {

		public ShutdownHook() {
			// Does nothing
		}

		@Override
		public void run() {
			// Delete installation path
			try {
				Files.walk(getInstallationPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
						.map(Path::toFile).forEach(File::delete);
			} catch (IOException error) {
				System.err.println("Installation path could not be removed. Remove manually.");
				error.printStackTrace();
			}

			// Delete repository path
			try {
				Files.walk(getRepositoryPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
						.map(Path::toFile).forEach(File::delete);
			} catch (IOException error) {
				System.err.println("Repository path could not be removed. Remove manually.");
				error.printStackTrace();
			}
		}
	}

	private static class RCommandBuilder {

		// Utility method. Should not be used outside of RCommandBuilder.
		static String _pkgList(final List<String> pkgs) {
			return "c(" + pkgs.stream().map(pkg -> "'" + pkg + "'").collect(Collectors.joining(", ")) + ")";
		}

		// Utility method. Should not be used outside of RCommandBuilder.
		static String _path2String(final Path path) {
			return path.toString().replace("\\", "/");
		}

		static String library(final String libraryName) {
			return "library(" + libraryName + ")";
		}

		static String installPackages(final List<String> pkgs, final Path path, final String type) {
			return "install.packages(" + _pkgList(pkgs) + ", repos = NULL, lib = '" + _path2String(path) + "', type = '"
					+ type + "')";
		}

		static String makeRepo(final Path path, final String repos, final String type) {
			return "makeRepo(c(), '" + _path2String(path) + "', repos = '" + repos + "', type = '" + type + "')";
		}

		static String pkgDep(final List<String> pkgs, final String type) {
			return "pkgDep(" + _pkgList(pkgs) + ", availPkgs = cranJuly2014, type = " + type + ")";
		}

		static String addPackage(final List<String> pkgs, final Path path, final String repos, final String type) {
			return "addPackage(" + _pkgList(pkgs) + ", '" + _path2String(path) + "', repos = '" + repos + "', type = '"
					+ type + "')";
		}

		static String checkVersions(final List<String> pkgs, final Path path, final String type) {
			return "checkVersions(" + _pkgList(pkgs) + ", '" + _path2String(path) + "', type = '" + type + "')";
		}
	}
}