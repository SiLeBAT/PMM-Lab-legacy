package de.bund.bfr.knime.pmm.fskx;

import com.google.common.base.Objects;

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
    return name.equals(other.name) && Objects.equal(version, other.version);
  }
}