package de.bund.bfr.pmf.file.uri;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * COMBINE has no official support for NuML, so it has no identifier for NuML.
 * NuML's schema is used instead.
 * 
 * @author Miguel de Alba
 * @see <a href="https://github.com/NuML/NuML">https://github.com/NuML/NuML</a>
 */
public class NuMLURI implements URIBase {

	public URI createURI() {
		URI uri = null;
		try {
			uri = new URI("https://raw.githubusercontent.com/NuML/NuML/master/NUMLSchema.xsd");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return uri;
	}
}
