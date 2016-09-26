package de.bund.bfr.knime.pmm.fskx;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class FskMetaData implements Serializable {
	
	private static final long serialVersionUID = -625136501840140815L;

	/** Null or empty string if not set. */
	public String modelName;
	
	/** Null or empty string if not set. */
	public String modelId;
	
	/** Null if not set. */
	public URL modelLink;
	
	/** Null or empty string if not set. */
	public String organism;
	
	/** Null or empty string if not set. */
	public String organismDetails;
	
	/** Null or empty string if not set. */
	public String matrix;
	
	/** Null or empty string if not set. */
	public String matrixDetails;
	
	/** Null or empty string if not set. */
	public String creator;
	
	/** Null or empty string if not set. */
	public String familyName;
	
	/** Null or empty string if not set. */
	public String contact;
	
	/** Null or empty string if not set. */
	public String referenceDescription;
	
	/** Null if not set. */
	public URL referenceDescriptionLink;
	
	/** Creation date. Null if not set. */
	public Date createdDate;
	
	/** Last modification date. Null if not set. */
	public Date modifiedDate;
	
	/** Null or empty string if not set. */
	public String rights;
	
	/** Null or empty string if not set. */
	public String notes;
	
	/** <code>false</code> if not set. */
	public boolean curated;
	
	/** Null if not set. */
	public ModelType type;
	
	/** {@link ModelClass#UNKNOWN} if not set. */
	public ModelClass subject = ModelClass.UNKNOWN;
	
	/** Null or empty string if not set. */
	public String foodProcess;
	
	/** Null or empty string if not set. */
	public String dependentVariable;
	
	/** Null or empty string if not set. */
	public String dependentVariableUnit;
	
	/** Double.NaN if not set. */
	public double dependentVariableMin = Double.NaN;
	
	/** Double.NaN if not set. */
	public double dependentVariableMax = Double.NaN;
	
	/** Null if not set. */
	public String[] independentVariables;
	
	/** Null if not set. */
	public String[] independentVariableUnits;
	
	/** Null if not set. */
	public double[] independentVariableMins;
	
	/** Null if not set. */
	public double[] independentVariableMaxs;
	
	/** Null if not set. */
	public double[] independentVariableValues;
	
	/** <code>false</code> if not set */
	public boolean hasData;
}
