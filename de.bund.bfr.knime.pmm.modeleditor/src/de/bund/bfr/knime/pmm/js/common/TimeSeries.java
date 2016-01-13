package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class TimeSeries implements ViewValue {

	// Configuration keys
	static final String NAME = "name";
	static final String TIME = "time";
	static final String TIME_UNIT = "timeUnit";
	static final String ORIG_TIME_UNIT = "origTimeUnit";
	static final String CONCENTRATION = "concentration";
	static final String CONCENTRATION_UNIT = "concentrationUnit";
	static final String CONCENTRATION_UNIT_OBJECT_TYPE = "concentrationUnitObjectType";
	static final String ORIG_CONCENTRATION_UNIT = "origConcentrationUnit";
	static final String CONCENTRATION_STDDEV = "concentrationStdDev";
	static final String NUMBER_OF_MEASUREMENTS = "numberOfMeasurements";

	private String name;
	private Double time;
	private String timeUnit;
	private String origTimeUnit;
	private Double concentration;
	private String concentrationUnit;
	private String concentrationUnitObjectType;
	private String origConcentrationUnit;
	private Double concentrationStdDev;
	private Integer numberOfMeasurements;

	/**
	 * Returns the name of this {@link TimeSeries}. If not set, returns null.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the time of this {@link TimeSeries}. If not set, returns null.
	 */
	public Double getTime() {
		return time;
	}

	/**
	 * Returns the time unit of this {@link TimeSeries}. If not set, returns
	 * null.
	 */
	public String getTimeUnit() {
		return timeUnit;
	}

	/**
	 * Returns the original time unit of this {@link TimeSeries}. If not set,
	 * returns null.
	 */
	public String getOrigTimeUnit() {
		return origTimeUnit;
	}

	/**
	 * Returns the concentration of this {@link TimeSeries}. If not set, returns
	 * null.
	 */
	public Double getConcentration() {
		return concentration;
	}

	/**
	 * Returns the concentration unit of this {@link TimeSeries}. If not set,
	 * returns null.
	 */
	public String getConcentrationUnit() {
		return concentrationUnit;
	}

	/**
	 * Returns the concentration unit object type of this {@link TimeSeries}. If
	 * not set, returns null.
	 */
	public String getConcentrationUnitObjectType() {
		return concentrationUnitObjectType;
	}

	/**
	 * Returns the original concentration unit of this {@link TimeSeries}. If
	 * not set, returns null.
	 */
	public String getOrigConcentrationUnit() {
		return origConcentrationUnit;
	}

	/**
	 * Returns the concentration std dev of this {@link TimeSeries}. If not set,
	 * returns null.
	 */
	public Double getConcentrationStdDev() {
		return concentrationStdDev;
	}

	/**
	 * Returns the number of measurements of this {@link TimeSeries}. If not
	 * set, returns null.
	 */
	public Integer getNumberOfMeasurements() {
		return numberOfMeasurements;
	}

	/** Sets the name value with 'name'. Ignores null and empty. */
	public void setName(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			this.name = name;
		}
	}

	/** Sets the time value with 'time'. */
	public void setTime(Double time) {
		if (time != null) {
			this.time = time;
		}
	}

	/** Sets the time unit with 'timeUnit'. Ignores null and empty. */
	public void setTimeUnit(String timeUnit) {
		if (!Strings.isNullOrEmpty(timeUnit)) {
			this.timeUnit = timeUnit;
		}
	}

	/**
	 * Sets the original time unit with 'origTimeUnit'. Ignores null and empty.
	 */
	public void setOrigTimeUnit(String origTimeUnit) {
		if (!Strings.isNullOrEmpty(origTimeUnit)) {
			this.origTimeUnit = origTimeUnit;
		}
	}

	/** Sets the concentration with 'concentration'. */
	public void setConcentration(Double concentration) {
		if (concentration != null) {
			this.concentration = concentration;
		}
	}

	/**
	 * Sets the concentration unit with 'concentrationUnit'. Ignores null and
	 * empty.
	 */
	public void setConcentrationUnit(String concentrationUnit) {
		if (!Strings.isNullOrEmpty(concentrationUnit)) {
			this.concentrationUnit = concentrationUnit;
		}
	}

	/**
	 * Sets the concentrationUnitObjectType with 'concentrationUnitObjectType'.
	 * Ignores null and empty.
	 */
	public void setConcentrationUnitObjectType(String concentrationUnitObjectType) {
		if (!Strings.isNullOrEmpty(concentrationUnitObjectType)) {
			this.concentrationUnitObjectType = concentrationUnitObjectType;
		}
	}

	/**
	 * Sets the origConcentrationUnit with 'origConcentrationUnit'. Ignores null
	 * and empty.
	 */
	public void setOrigConcentrationUnit(String origConcentrationUnit) {
		if (!Strings.isNullOrEmpty(origConcentrationUnit)) {
			this.origConcentrationUnit = origConcentrationUnit;
		}
	}

	/** Sets the concentration std dev with 'concentrationStdDev'. */
	public void setConcentrationStdDev(Double concentrationStdDev) {
		if (concentrationStdDev != null) {
			this.concentrationStdDev = concentrationStdDev;
		}
	}

	/** Sets the number of measurements with 'numberOfMeasurements'. */
	public void setNumberOfMeasurements(Integer numberOfMeasurements) {
		if (numberOfMeasurements != null) {
			this.numberOfMeasurements = numberOfMeasurements;

		}
	}

	/** Saves time series properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		if (name != null) {
			settings.addString(NAME, name);
		}
		if (time != null) {
			settings.addDouble(TIME, time);
		}
		if (timeUnit != null) {
			settings.addString(TIME_UNIT, timeUnit);
		}
		if (origTimeUnit != null) {
			settings.addString(ORIG_TIME_UNIT, origTimeUnit);
		}
		if (concentration != null) {
			settings.addDouble(CONCENTRATION, concentration);
		}
		if (concentrationUnit != null) {
			settings.addString(CONCENTRATION_UNIT, concentrationUnit);
		}
		if (concentrationUnitObjectType != null) {
			settings.addString(CONCENTRATION_UNIT_OBJECT_TYPE, concentrationUnitObjectType);
		}
		if (origConcentrationUnit != null) {
			settings.addString(ORIG_CONCENTRATION_UNIT, origConcentrationUnit);
		}
		if (concentrationStdDev != null) {
			settings.addDouble(CONCENTRATION_STDDEV, concentrationStdDev);
		}
		if (numberOfMeasurements != null) {
			settings.addInt(NUMBER_OF_MEASUREMENTS, numberOfMeasurements);
		}
	}

	/** Loads time series properties from a {@link NodeSettingsRO}. */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			name = settings.getString(NAME);
		} catch (InvalidSettingsException e) {
			name = null;
		}
		try {
			time = settings.getDouble(TIME);
		} catch (InvalidSettingsException e) {
			time = null;
		}
		try {
			timeUnit = settings.getString(TIME_UNIT);
		} catch (InvalidSettingsException e) {
			timeUnit = null;
		}
		try {
			origTimeUnit = settings.getString(ORIG_TIME_UNIT);
		} catch (InvalidSettingsException e) {
			origTimeUnit = null;
		}
		try {
			concentration = settings.getDouble(CONCENTRATION);
		} catch (InvalidSettingsException e) {
			concentration = null;
		}
		try {
			concentrationUnit = settings.getString(CONCENTRATION_UNIT);
		} catch (InvalidSettingsException e) {
			concentrationUnit = null;
		}
		try {
			concentrationUnitObjectType = settings.getString(CONCENTRATION_UNIT_OBJECT_TYPE);
		} catch (InvalidSettingsException e) {
			concentrationUnitObjectType = null;
		}
		try {
			origConcentrationUnit = settings.getString(ORIG_CONCENTRATION_UNIT);
		} catch (InvalidSettingsException e) {
			origConcentrationUnit = null;
		}
		try {
			concentrationStdDev = settings.getDouble(CONCENTRATION_STDDEV);
		} catch (InvalidSettingsException e) {
			concentrationStdDev = null;
		}
		try {
			numberOfMeasurements = settings.getInt(NUMBER_OF_MEASUREMENTS);
		} catch (InvalidSettingsException e) {
			numberOfMeasurements = null;
		}
	}

	/** Creates a {@link TimeSeriesXml} from this {@link TimeSeries}. */
	public TimeSeriesXml toTimeSeriesXml() {
		return new TimeSeriesXml(name, time, timeUnit, origTimeUnit, concentration, concentrationUnit,
				concentrationUnitObjectType, origConcentrationUnit, concentrationStdDev, numberOfMeasurements);
	}

	/** Creates a {@link TimeSeris} from a {@link TimeSeriesXml}. */
	public static TimeSeries toTimeSeries(TimeSeriesXml timeSeriesXml) {
		TimeSeries timeSeries = new TimeSeries();
		timeSeries.setName(timeSeriesXml.getName());
		timeSeries.setTime(timeSeriesXml.getTime());
		timeSeries.setTimeUnit(timeSeriesXml.getTimeUnit());
		timeSeries.setOrigTimeUnit(timeSeriesXml.getOrigTimeUnit());
		timeSeries.setConcentration(timeSeriesXml.getConcentration());
		timeSeries.setConcentrationUnit(timeSeriesXml.getConcentrationUnit());
		timeSeries.setConcentrationUnitObjectType(timeSeriesXml.getConcentrationUnitObjectType());
		timeSeries.setOrigConcentrationUnit(timeSeriesXml.getOrigConcentrationUnit());
		timeSeries.setConcentrationStdDev(timeSeriesXml.getConcentrationStdDev());
		timeSeries.setNumberOfMeasurements(timeSeriesXml.getNumberOfMeasurements());

		return timeSeries;
	}
}
