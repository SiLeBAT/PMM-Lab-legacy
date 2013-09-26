package de.bund.bfr.knime.pmm.maptostring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class MapToStringSettings {

	protected static final String CFGKEY_VARIABLE_NAME = "VariableName";
	protected static final String CFGKEY_KEY = "Key";
	protected static final String CFGKEY_VALUE = "Value";

	private String variableName;
	private List<String> keys;
	private List<Double> values;

	public MapToStringSettings() {
		variableName = null;
		keys = new ArrayList<String>(Collections.nCopies(5, (String) null));
		values = new ArrayList<Double>(Collections.nCopies(5, (Double) null));
	}

	public void load(NodeSettingsRO settings) {
		try {
			variableName = settings.getString(CFGKEY_VARIABLE_NAME);
		} catch (InvalidSettingsException e) {
			variableName = null;
		}

		keys = new ArrayList<String>(Collections.nCopies(5, (String) null));
		values = new ArrayList<Double>(Collections.nCopies(5, (Double) null));

		for (int i = 0; i < 5; i++) {
			try {
				keys.set(i, settings.getString(CFGKEY_KEY + i));
			} catch (InvalidSettingsException e) {
			}

			try {
				values.set(i, convert(settings.getDouble(CFGKEY_VALUE + i)));
			} catch (InvalidSettingsException e) {
			}
		}
	}

	public void save(NodeSettingsWO settings) {
		settings.addString(CFGKEY_VARIABLE_NAME, variableName);

		for (int i = 0; i < 5; i++) {
			settings.addString(CFGKEY_KEY + i, keys.get(i));
			settings.addDouble(CFGKEY_VALUE + i, convert(values.get(i)));
		}
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}

	private static double convert(Double value) {
		return value != null ? value : Double.NaN;
	}

	private static Double convert(double value) {
		return !Double.isNaN(value) ? value : null;
	}
}
