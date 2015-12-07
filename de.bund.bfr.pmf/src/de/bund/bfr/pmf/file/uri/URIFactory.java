package de.bund.bfr.pmf.file.uri;

import java.net.URI;

public class URIFactory {
	
	public static URI createSBMLURI() {
		return new SBMLURI().createURI();
	}
	
	public static URI createNuMLURI() {
		return new NuMLURI().createURI();
	}
}
