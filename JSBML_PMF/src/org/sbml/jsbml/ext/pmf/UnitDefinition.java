/**
 * Extended UnitDefinition with a transformation name.
 * 
 * @author Miguel Alba
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;

public class UnitDefinition extends org.sbml.jsbml.UnitDefinition {

  private static final long serialVersionUID = 7380014837661008800L;

  /** Transformation name. **/
	private String transformation;

	/** Creates an UnitDefinition instance. By default, the listOfUnit and transformation are null. */
	public UnitDefinition() {
		super();	
		init();
	}

	public UnitDefinition(int level, int version) {
		super(level, version);
		init();
	}

	public UnitDefinition(String id) {
		super(id);
		init();
	}

	/**
	 * Creates an UnitDefinition instance from an id, level and version. By
	 * default, the listOfUnit and transformation are null.
	 */
	public UnitDefinition(String id, int level, int version) {
		super(id, level, version);
		init();
	}

	/**
	 * Creates an UnitDefinition instance from an id, name, level and version.
	 * By default, the listOfUnit and transformation are null.
	 */
	public UnitDefinition(String id, String name, int level, int version) {
		super(id, name, level, version);
		init();
	}

	/**
	 * Creates an UnitDefinition instance from a given unitDefinition.
	 *
	 * @param unitDefinition
	 */
	public UnitDefinition(UnitDefinition unitDefinition) {
		super(unitDefinition);
		if (unitDefinition.isSetTransformation()) {
			setTransformation(unitDefinition.getTransformation());
		}
		init();
	}


	/* (non-Javadoc)
	 * @see org.sbml.jsbml.element.AbstractSBase#clone()
	 */
	@Override
	public UnitDefinition clone() {
		return new UnitDefinition(this);
	}

	private void init() {
	  setPackageVersion(-1);
	  this.packageName = PMFConstants.shortLabel;
	}

	// *** transformation methods ***
	public String getTransformation() {
		return this.transformation;
	}

	public boolean isSetTransformation() {
		return this.transformation != null;
	}

	public void setTransformation(String transformation) {
		String oldTransformation = this.transformation;
		this.transformation = transformation;
		firePropertyChange("transformation", oldTransformation, this.transformation);
	}

	public boolean unsetTransformation() {
		if (isSetTransformation()) {
			String oldTransformation = this.transformation;
			this.transformation = null;
			firePropertyChange("transformation", oldTransformation, this.transformation);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.sbml.jsbml.UnitDefinition#readAttribute()
	 */
	@Override
	public boolean readAttribute(String attributeName, String prefix, String value) {
		// Try to read attribute with base class (Parameter)
    boolean isAttributeRead = super.readAttribute(attributeName, prefix, value);
		if (!isAttributeRead) {
			if (attributeName.equals("transformation")) {
				setTransformation(value);
				return true;
			}
		}
		return isAttributeRead;
	}

	/* (non-Javadoc)
	 * @see org.sbml.jsbml.UnitDefinition#writeXMLAttributes()
	 */
	@Override
	public Map<String, String> writeXMLAttributes() {
		Map<String, String> attributes = super.writeXMLAttributes();
		if (isSetTransformation()) {
			attributes.put("transformation", getTransformation());
		}
		return attributes;
	}
}