package de.bund.bfr.knime.pmm.fskx;

public class RDependency {
  String name;
  RVersion version;

  @Override
  public String toString() {
    return version == null ? name : name + " (>= " + version + ")";
  }

  @Override
  public boolean equals(Object obj) {
    RDependency other = (RDependency) obj;
    return name.equals(other.name) && version.equals(other.version);
  }
}