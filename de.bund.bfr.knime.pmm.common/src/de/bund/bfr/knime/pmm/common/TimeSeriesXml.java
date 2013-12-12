package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

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

	private String name = null;
	private Double time = null;
	private String timeUnit = null;
	private String origTimeUnit = null;
	private Double concentration = null;
	private String concentrationUnit = null;
	private String concentrationUnitObjectType = null;
	private String origConcentrationUnit = null;
	private Double concentrationStdDev = null;
	private Integer numberOfMeasurements = null;

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
		
		for (String el : getElements()) {
			ret.setAttribute(el, XmlHelper.getNonNull(getValue(el)));
		}		

		return ret;
	}

	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase(ATT_NAME)) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_TIME)) {
			return DoubleCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_TIME_UNIT)) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_ORIG_TIME_UNIT)) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_CONCENTRATION)) {
			return DoubleCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_CONCENTRATION_UNIT)) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_CONCENTRATION_UNIT_OBJECT_TYPE)) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_ORIG_CONCENTRATION_UNIT)) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_CONCENTRATION_STDDEV)) {
			return DoubleCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_NUMBER_OF_MEASUREMENTS)) {
			return IntCell.TYPE;
		}
		return null;
	}

	public static List<String> getElements() {
		List<String> list = new ArrayList<String>();
		list.add(ATT_NAME);
		list.add(ATT_TIME);
		list.add(ATT_TIME_UNIT);
		list.add(ATT_ORIG_TIME_UNIT);
		list.add(ATT_CONCENTRATION);
		list.add(ATT_CONCENTRATION_UNIT);
		list.add(ATT_CONCENTRATION_UNIT_OBJECT_TYPE);
		list.add(ATT_ORIG_CONCENTRATION_UNIT);
		list.add(ATT_CONCENTRATION_STDDEV);
		list.add(ATT_NUMBER_OF_MEASUREMENTS);
		return list;
	}

	public Object getValue(String element) {
		if (element.equalsIgnoreCase(ATT_NAME)) {
			return name;
		} else if (element.equalsIgnoreCase(ATT_TIME)) {
			return time;
		} else if (element.equalsIgnoreCase(ATT_TIME_UNIT)) {
			return timeUnit;
		} else if (element.equalsIgnoreCase(ATT_ORIG_TIME_UNIT)) {
			return origTimeUnit;
		} else if (element.equalsIgnoreCase(ATT_CONCENTRATION)) {
			return concentration;
		} else if (element.equalsIgnoreCase(ATT_CONCENTRATION_UNIT)) {
			return concentrationUnit;
		} else if (element.equalsIgnoreCase(ATT_CONCENTRATION_UNIT_OBJECT_TYPE)) {
			return concentrationUnitObjectType;
		} else if (element.equalsIgnoreCase(ATT_ORIG_CONCENTRATION_UNIT)) {
			return origConcentrationUnit;
		} else if (element.equalsIgnoreCase(ATT_CONCENTRATION_STDDEV)) {
			return concentrationStdDev;
		} else if (element.equalsIgnoreCase(ATT_NUMBER_OF_MEASUREMENTS)) {
			return numberOfMeasurements;
		}
		return null;
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
