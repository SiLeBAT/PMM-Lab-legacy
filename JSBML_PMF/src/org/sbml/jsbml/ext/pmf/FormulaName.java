/**
 *
 */
 package org.sbml.jsbml.ext.pmf;

 import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;

 /**
  * @author Miguel Alba
  */
 public class FormulaName extends AbstractSBase {

  private static final long serialVersionUID = -7804218843540654008L;
 	private String name;

 	/** Creates a {@link FormulaName} instance. */
 	public FormulaName() {
 		super();
 		this.packageName = PMFConstants.shortLabel;
 	}

 	/** Creates a {@link FormulaName} instance from a name. */
 	public FormulaName(String name) {
 		super();
 		this.name = name;
 		this.packageName = PMFConstants.shortLabel;
 	}

 	/** Creates a {@link FormulaName} instance from a name, level and version. */
 	public FormulaName(String name, int level, int version) {
 		super(level, version);
 		this.name = name;
 		this.packageName = PMFConstants.shortLabel;
 	}

 	/** Clone constructor. */
 	public FormulaName(FormulaName formulaName) {
 		super(formulaName);
 	}

 	/** Clones this class. */
 	@Override
 	public FormulaName clone() {
 		return new FormulaName(this);
 	}

 	// *** name methods ***

 	/**
 	 * Returns name.
 	 *
 	 * @return value.
 	 */
 	public String getName() {
 		return this.name;
 	}

 	/**
 	 * Returns whether name is set.
 	 *
 	 * @return whether name is set.
 	 */
 	public boolean isSetName() {
 		return this.name != null;
 	}

 	/**
 	 * Sets name.
 	 *
 	 * @param name.
 	 */
 	public void setName(String name) {
 		String oldName = this.name;
 		this.name = name;
 		firePropertyChange("name", oldName, this.name);
 	}

 	/**
 	 * Unsets the variable name.
 	 *
 	 * @return {@code true}, if name was set before, otherwise {@code false}.
 	 */
 	public boolean unsetName() {
 		if (isSetName()) {
 			String oldName = this.name;
 			this.name = null;
 			firePropertyChange("name", oldName, this.name);
 			return true;
 		}
 		return false;
 	}


 	/*
 	 * (non-Javadoc)
 	 * @see org.sbml.jsbml.SBasePlugin#readAttribute(java.lang.String, java.lang.String, java.lang.String)
 	 */
 	@Override
 	public boolean readAttribute(String attributeName, String prefix, String value) {
 		if (attributeName.equals("value")) {
 			setName(value);
 			return true;
 		}
 		return false;
 	}

 	/*
 	 * (non-Javadoc)
 	 * @see org.sbml.jsbml.AbstractSBase#writeXMLAttributes()
 	 */
 	@Override
 	public Map<String, String> writeXMLAttributes() {
 		Map<String, String> attributes = new TreeMap<>();
 		if (isSetName()) {
 			attributes.put("value", this.name);
 		}
 		return attributes;
 	}

 	@Override
 	public String toString() {
 		return "FormulaName [value=\"" + this.name + "\"]";
 	}
 }