package de.bund.bfr.knime.pmm.fskx;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Zip URI.
 * 
 * @author Miguel Alba
 */
public class ZipUri {

  public static URI createURI() {
    URI uri = null;
    try {
      uri = new URI("http://www.iana.org/assignments/media-types/application/zip");
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return uri;
  }
}
