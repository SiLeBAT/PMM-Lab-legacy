package de.bund.bfr.knime.pmm.file.uri;

import java.net.URI;

/**
 * Creates the corresponding identifier of a COMBINE specification.
 * 
 * @author Miguel de Alba
 * @see <a href="http://co.mbine.org/standards/specifications/">http://co.mbine.
 *      org/standards/specifications/</a>
 */
public interface URIBase {
	public URI createURI();
}
