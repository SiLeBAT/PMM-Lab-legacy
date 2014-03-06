package de.bund.bfr.knime.pmm.common;

import org.jdom2.Element;

public class TimeSeriesXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_TIMESERIES = "timeseriesxml";

	private static final String ATT_NAME = "name";
	private static final String ATT_TIME = "time";
	private static final String ATT_TIME_UNIT = "timeUnit";
	private static final String ATT_ORIG_TIME_UNIT = "origTimeUnit";
	private static final String ATT_CONCENTRATION = "concentration";
	private static final String ATT_CONCENTRATION_UNIT = "concentrationUnit";
	private static final String ATT_CONCENTRATION_UNIT_OBJECT_TYPE = "concentrationUnitObjectType";
	private static final String ATT_ORIG_CONCENTRATION_UNIT = "origConcentrationUnit";
	private static final String ATT_CONCENTRATION_STDDEV = "concentrationStdDev";
	private static final String ATT_NUMBER_OF_MEASUREMENTS = "numberOfMeasurements";	

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

	public TimeSeriesXml(String name, Double time, String timeUnit,
			String origTimeUnit, Double concentration,
			String concentrationUnit, String concentrationUnitObjectType,
			String origConcentrationUnit, Double concentrationStdDev,
			Integer numberOfMeasurements) {
		this.name = name;
		this.time = time;
		this.timeUnit = timeUnit;
		this.origTimeUnit = origTimeUnit;
		this.concentration = concentration;
		this.concentrationUnit = concentrationUnit;
		this.concentrationUnitObjectType = concentrationUnitObjectType;
		this.origConcentrationUnit = origConcentrationUnit;
		this.concentrationStdDev = concentrationStdDev;
		this.numberOfMeasurements = numberOfMeasurements;
	}

	public TimeSeriesXml(String name, Double time, String timeUnit,
			Double concentration, String concentrationUnit,
			Double concentrationStdDev, Integer numberOfMeasurements) {
		this(name, time, timeUnit, timeUnit, concentration, concentrationUnit,
				null, concentrationUnit, concentrationStdDev,
				numberOfMeasurements);
	}

	public TimeSeriesXml(Element el) {
		this(XmlHelper.getString(el, ATT_NAME), XmlHelper.getDouble(el,
				ATT_TIME), XmlHelper.getString(el, ATT_TIME_UNIT), XmlHelper
				.getString(el, ATT_ORIG_TIME_UNIT), XmlHelper.getDouble(el,
				ATT_CONCENTRATION), XmlHelper.getString(el,
				ATT_CONCENTRATION_UNIT), XmlHelper.getString(el,
				ATT_CONCENTRATION_UNIT_OBJECT_TYPE), XmlHelper.getString(el,
				ATT_ORIG_CONCENTRATION_UNIT), XmlHelper.getDouble(el,
				ATT_CONCENTRATION_STDDEV), XmlHelper.getInt(el,
				ATT_NUMBER_OF_MEASUREMENTS));
	}

	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_TIMESERIES);

		ret.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		ret.setAttribute(ATT_TIME, XmlHelper.getNonNull(time));
		ret.setAttribute(ATT_TIME_UNIT, XmlHelper.getNonNull(timeUnit));
		ret.setAttribute(ATT_ORIG_TIME_UNIT, XmlHelper.getNonNull(origTimeUnit));
		ret.setAttribute(ATT_CONCENTRATION, XmlHelper.getNonNull(concentration));
		ret.setAttribute(ATT_CONCENTRATION_UNIT,
				XmlHelper.getNonNull(concentrationUnit));
		ret.setAttribute(ATT_CONCENTRATION_UNIT_OBJECT_TYPE,
				XmlHelper.getNonNull(concentrationUnitObjectType));
		ret.setAttribute(ATT_ORIG_CONCENTRATION_UNIT,
				XmlHelper.getNonNull(origConcentrationUnit));
		ret.setAttribute(ATT_CONCENTRATION_STDDEV,
				XmlHelper.getNonNull(concentrationStdDev));
		ret.setAttribute(ATT_NUMBER_OF_MEASUREMENTS,
				XmlHelper.getNonNull(numberOfMeasurements));

		return ret;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public String getOrigTimeUnit() {
		return origTimeUnit;
	}

	public void setOrigTimeUnit(String origTimeUnit) {
		this.origTimeUnit = origTimeUnit;
	}

	public Double getConcentration() {
		return concentration;
	}

	public void setConcentration(Double concentration) {
		this.concentration = concentration;
	}

	public String getConcentrationUnit() {
		return concentrationUnit;
	}

	public void setConcentrationUnit(String concentrationUnit) {
		this.concentrationUnit = concentrationUnit;
	}

	public String getConcentrationUnitObjectType() {
		return concentrationUnitObjectType;
	}

	public void setConcentrationUnitObjectType(
			String concentrationUnitObjectType) {
		this.concentrationUnitObjectType = concentrationUnitObjectType;
	}

	public String getOrigConcentrationUnit() {
		return origConcentrationUnit;
	}

	public void setOrigConcentrationUnit(String origConcentrationUnit) {
		this.origConcentrationUnit = origConcentrationUnit;
	}

	public Double getConcentrationStdDev() {
		return concentrationStdDev;
	}

	public void setConcentrationStdDev(Double concentrationStdDev) {
		this.concentrationStdDev = concentrationStdDev;
	}

	public Integer getNumberOfMeasurements() {
		return numberOfMeasurements;
	}

	public void setNumberOfMeasurements(Integer numberOfMeasurements) {
		this.numberOfMeasurements = numberOfMeasurements;
	}
}
