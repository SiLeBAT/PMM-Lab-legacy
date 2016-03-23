package de.bund.bfr.knime.pmm.fskx;

/**
 * Numeric version for a R package.
 * 
 * At least two version numbers, the major and minor numbers. A third optional version number,
 * called patch, is recommended although not mandatory. Version numbers may be separated with dots
 * "." or dashes "-". The use of dashes is discouraged.
 * 
 * @author Miguel Alba
 */
public class RVersion {
  int major;
  int minor;
  Integer patch;

  public static RVersion numericVersion(String string) {
    RVersion version = new RVersion();

    // actual parsing
    String[] tokens = string.split("-|\\.");

    version.major = Integer.valueOf(tokens[0]);
    version.minor = Integer.valueOf(tokens[1]);

    // Patch number
    if (tokens.length == 3) {
      version.patch = Integer.valueOf(tokens[2]);
    }

    return version;
  }

  @Override
  public String toString() {
    String string = major + "." + minor;
    if (patch != null) {
      string += "." + patch;
    }

    return string;
  }
}