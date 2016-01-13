package de.bund.bfr.knime.pmm.js.common;

import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.ParamXml;

/**
 * PmmLab parameter. Holds:
 * <ul>
 * <li>name
 * <li>original name
 * <li>value
 * <li>error
 * <li>minimum value
 * <li>maximum value
 * <li>P
 * <li>T
 * <li>minimum guess
 * <li>maximum guess
 * <li>unit
 * <li>description
 * <li>correlation names
 * <li>correlation values
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Param implements ViewValue {

	// Configuration keys
	static final String NAME = "name";
	static final String ORIGNAME = "origname";
	static final String VALUE = "value";
	static final String ERROR = "error";
	static final String MIN = "min";
	static final String MAX = "max";
	static final String P = "P";
	static final String T = "t";
	static final String MINGUESS = "minGuess";
	static final String MAXGUESS = "maxGuess";
	static final String CATEGORY = "category";
	static final String UNIT = "unit";
	static final String DESCRIPTION = "description";
	static final String CORRELATION_NAMES = "correlationNames";
	static final String CORRELATION_VALUES = "correlationValues";

	private String name;
	private String origname;
	private Double value;
	private Double error;
	private Double min;
	private Double max;
	private Double p;
	private Double t;
	private Double minGuess;
	private Double maxGuess;
	private String category;
	private String unit;
	private String description;
	private String[] correlationNames;
	private double[] correlationValues;

	/** Returns the name of this {@link Param}. If not set, returns null. */
	public String getName() {
		return name;
	}

	/**
	 * Returns the original name of this {@link Param}. If not set, returns
	 * null.
	 */
	public String getOrigName() {
		return origname;
	}

	/** Returns the value of this {@link Param}. If not set, returns null. */
	public Double getValue() {
		return value;
	}

	/** Returns the error of this {@link Param}. If not set, returns null. */
	public Double getError() {
		return error;
	}

	/**
	 * Returns the minimum value of this {@link Param}. If not set, returns
	 * null.
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * Returns the maximum value of this {@link Param}. If not set, returns
	 * null.
	 */
	public Double getMax() {
		return max;
	}

	/** Returns the P of this {@link Param}. If not set, returns null. */
	public Double getP() {
		return p;
	}

	/** Returns the T of this {@link Param}. If not set, returns null. */
	public Double getT() {
		return t;
	}

	/**
	 * Returns the minimum guess of this {@link Param}. If not set, returns
	 * null.
	 */
	public Double getMinGuess() {
		return minGuess;
	}

	/**
	 * Returns the maximum guess of this {@link Param}. If not set, returns
	 * null.
	 */
	public Double getMaxGuess() {
		return maxGuess;
	}

	/** Returns the category of this {@link Param}. If not set, returns null. */
	public String getCategory() {
		return category;
	}

	/** Returns the unit of this {@link Param}. If not set, returns null. */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the description of this {@link Param}. If not set, returns null.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the correlation names of this {@lin Param}. If not set, returns
	 * null.
	 */
	public String[] getCorrelationNames() {
		return correlationNames;
	}

	/**
	 * Returns the correlation values of this {@link Param}. If not set, returns
	 * null.
	 */
	public double[] getCorrelationValues() {
		return correlationValues;
	}

	/** Sets the name value with 'name'. Ignores null and empty strings. */
	public void setName(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			this.name = name;
		}
	}

	/**
	 * Sets the original name value with 'origName'. Ignores null and empty
	 * strings.
	 */
	public void setOrigName(String origName) {
		if (!Strings.isNullOrEmpty(origName)) {
			this.origname = origName;
		}
	}

	/** Sets the value with 'value'. */
	public void setValue(Double value) {
		if (value != null) {
			this.value = value;
		}
	}

	/** Sets the error with 'error'. */
	public void setError(Double error) {
		if (error != null) {
			this.error = error;
		}
	}

	/** Sets the minimum value with 'min'. */
	public void setMin(Double min) {
		if (min != null) {
			this.min = min;
		}
	}

	/** Sets the maximum value with 'max'. */
	public void setMax(Double max) {
		if (max != null) {
			this.max = max;
		}
	}

	/** Sets P with 'p'. */
	public void setP(Double p) {
		if (p != null) {
			this.p = p;
		}
	}

	/** Sets T with 't'. */
	public void setT(Double t) {
		if (t != null) {
			this.t = t;
		}
	}

	/** Sets the minimum guess with 'minGuess'. */
	public void setMinGuess(Double minGuess) {
		if (minGuess != null) {
			this.minGuess = minGuess;
		}
	}

	/** Sets the maximum guess with 'maxGuess'. */
	public void setMaxGuess(Double maxGuess) {
		if (maxGuess != null) {
			this.maxGuess = maxGuess;
		}
	}

	/** Sets the category with 'category'. Ignores null and empty strings. */
	public void setCategory(String category) {
		if (!Strings.isNullOrEmpty(category)) {
			this.category = category;
		}
	}

	/** Sets the unit with 'unit'. Ignores null and empty strings. */
	public void setUnit(String unit) {
		if (!Strings.isNullOrEmpty(unit)) {
			this.unit = unit;
		}
	}

	/**
	 * Sets the description with 'description'. Ignores null and empty strings.
	 */
	public void setDescription(String description) {
		if (!Strings.isNullOrEmpty(description)) {
			this.description = description;
		}
	}

	/** Sets the correlation names with 'correlationNames'. */
	public void setCorrelationNames(String[] correlationNames) {
		this.correlationNames = correlationNames;
	}

	/** Sets the correlation values with 'correlationValues'. */
	public void setCorrelationValues(double[] correlationValues) {
		this.correlationValues = correlationValues;
	}

	public void saveToNodeSettings(NodeSettingsWO settings) {
		if (name != null)
			settings.addString(NAME, name);
		if (origname != null)
			settings.addString(ORIGNAME, origname);
		if (value != null)
			settings.addDouble(VALUE, value);
		if (error != null)
			settings.addDouble(ERROR, error);
		if (min != null)
			settings.addDouble(MIN, min);
		if (max != null)
			settings.addDouble(MAX, max);
		if (p != null)
			settings.addDouble(P, p);
		if (t != null)
			settings.addDouble(T, t);
		if (minGuess != null)
			settings.addDouble(MINGUESS, minGuess);
		if (maxGuess != null)
			settings.addDouble(MAXGUESS, maxGuess);
		if (category != null)
			settings.addString(CATEGORY, category);
		if (unit != null)
			settings.addString(UNIT, unit);
		if (description != null)
			settings.addString(DESCRIPTION, description);
		if (correlationNames != null)
			settings.addStringArray(CORRELATION_NAMES, correlationNames);
		if (correlationValues != null)
			settings.addDoubleArray(CORRELATION_VALUES, correlationValues);
	}

	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			name = settings.getString(NAME);
		} catch (InvalidSettingsException e) {
			name = null;
		}
		try {
			origname = settings.getString(ORIGNAME);
		} catch (InvalidSettingsException e) {
			origname = null;
		}
		try {
			value = settings.getDouble(VALUE);
		} catch (InvalidSettingsException e) {
			value = null;
		}
		try {
			error = settings.getDouble(ERROR);
		} catch (InvalidSettingsException e) {
			error = null;
		}
		try {
			min = settings.getDouble(MIN);
		} catch (InvalidSettingsException e) {
			min = null;
		}
		try {
			max = settings.getDouble(MAX);
		} catch (InvalidSettingsException e) {
			max = null;
		}
		try {
			p = settings.getDouble(P);
		} catch (InvalidSettingsException e) {
			p = null;
		}
		try {
			t = settings.getDouble(T);
		} catch (InvalidSettingsException e) {
			t = null;
		}
		try {
			minGuess = settings.getDouble(MINGUESS);
		} catch (InvalidSettingsException e) {
			minGuess = null;
		}
		try {
			maxGuess = settings.getDouble(MAXGUESS);
		} catch (InvalidSettingsException e) {
			maxGuess = null;
		}
		try {
			category = settings.getString(CATEGORY);
		} catch (InvalidSettingsException e) {
			category = null;
		}
		try {
			unit = settings.getString(UNIT);
		} catch (InvalidSettingsException e) {
			unit = null;
		}
		try {
			description = settings.getString(DESCRIPTION);
		} catch (InvalidSettingsException e) {
			description = null;
		}
		try {
			correlationNames = settings.getStringArray(CORRELATION_NAMES);
		} catch (InvalidSettingsException e) {
			correlationNames = null;
		}
		try {
			correlationValues = settings.getDoubleArray(CORRELATION_VALUES);
		} catch (InvalidSettingsException e) {
			correlationValues = null;
		}
	}

	/** Creates an {@link ParamXml} from this {@link Param}. */
	public ParamXml toParamXml() {
		HashMap<String, Double> correlations = new HashMap<>();
		for (int i = 0; i < correlationNames.length; i++) {
			correlations.put(correlationNames[i], correlationValues[i]);
		}
		ParamXml paramXml = new ParamXml(name, origname, value, error, min, max, p, t, minGuess, maxGuess, category,
				unit, description, correlations);

		return paramXml;
	}

	/** Creates an {@link Param} from an {@link ParamXml}. */
	public static Param toParam(ParamXml paramXml) {
		HashMap<String, Double> obtainedCorrelations = paramXml.getAllCorrelations();
		String[] obtainedCorrelationNames = new String[obtainedCorrelations.size()];
		double[] obtainedCorrelationValues = new double[obtainedCorrelations.size()];
		int i = 0;
		for (Map.Entry<String, Double> entry : obtainedCorrelations.entrySet()) {
			obtainedCorrelationNames[i] = entry.getKey();
			obtainedCorrelationValues[i] = entry.getValue();
			i++;
		}

		Param param = new Param();
		param.setName(paramXml.getName());
		param.setOrigName(paramXml.getOrigName());
		param.setValue(paramXml.getValue());
		param.setError(paramXml.getError());
		param.setMin(paramXml.getMin());
		param.setMax(paramXml.getMax());
		param.setP(paramXml.getP());
		param.setT(paramXml.getT());
		param.setMinGuess(paramXml.getMinGuess());
		param.setMaxGuess(paramXml.getMaxGuess());
		param.setCategory(paramXml.getCategory());
		param.setUnit(paramXml.getUnit());
		param.setDescription(paramXml.getDescription());
		param.setCorrelationNames(obtainedCorrelationNames);
		param.setCorrelationValues(obtainedCorrelationValues);

		return param;
	}
}
