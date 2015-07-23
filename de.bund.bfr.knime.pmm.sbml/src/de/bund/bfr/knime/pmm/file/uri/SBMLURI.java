package de.bund.bfr.knime.pmm.file.uri;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Generic SBML URI
 * 
 * @author Miguel de Alba
 * @see <a href="http://co.mbine.org/specifications/sbml">http://co.mbine.org/
 *      specifications/sbml</a>
 */
public class SBMLURI implements URIBase {

	public URI createURI() {
		URI uri = null;
		try {
			uri = new URI("http://identifiers.org/combine/specifications/sbml");
		} catch (URISyntaxException e) {
			// the uri is valid - this catch is never reached
		}
		return uri;
	}
}
