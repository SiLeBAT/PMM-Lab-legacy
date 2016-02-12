package de.bund.bfr.knime.pmm.fskx;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * R URI.
 * 
 * @author Miguel Alba
 */
public class RUri {

  public URI createURI() {
    URI uri = null;
    try {
      uri = new URI("http://www.r-project.org");
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return uri;
  }
}
