package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.Annotation;

/**
 * Base class for annotations.
 * 
 * @author Miguel de Alba
 */
public abstract class AnnotationBase {
	
	static final String METADATA_TAG = "metadata"; // Metadata tag
	static final String METADATA_NS = "pmf"; // Metadata namespace

	protected Annotation annotation;

	public Annotation getAnnotation() {
		return annotation;
	}
}