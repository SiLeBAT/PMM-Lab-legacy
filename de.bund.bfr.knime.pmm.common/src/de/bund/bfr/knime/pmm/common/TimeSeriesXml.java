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

	private String name = null;
	private Double time = null;
	private String timeUnit = null;
	private Integer timeUnitId = null;
	private String origTimeUnit = null;
	private Integer origTimeUnitId = null;
	private Double concentration = null;
	private String concentrationUnit = null;
	private Integer concentrationUnitId = null;
	private String origConcentrationUnit = null;
	private Integer origConcentrationUnitId = null;
	private Double concentrationStdDev = null;
	private Integer numberOfMeasurements = null;
	
	public TimeSeriesXml(String name, Double time, String timeUnit, Double concentration, String concentrationUnit, Double concentrationStdDev, Integer numberOfMeasurements) {
		setName(name);
		setTime(time);
		setTimeUnit(timeUnit);
		setConcentration(concentration);
		setConcentrationUnit(concentrationUnit);
		setConcentrationStdDev(concentrationStdDev);
		setNumberOfMeasurements(numberOfMeasurements);
	}
	public TimeSeriesXml(Element xmlElement) {
		try {
			setName(xmlElement.getAttribute("name").getValue());
			String strDbl = xmlElement.getAttribute("time").getValue();
			setTime(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			setTimeUnit(xmlElement.getAttribute("timeUnit") == null ? null : xmlElement.getAttribute("timeUnit").getValue());
			if (xmlElement.getAttribute("timeUnitId") != null) {
				strDbl = xmlElement.getAttribute("timeUnitId").getValue();
				setTimeUnitId(strDbl.trim().isEmpty() ? null : Integer.parseInt(strDbl));
			}
			strDbl = xmlElement.getAttribute("concentration").getValue();
			setConcentration(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			setConcentrationUnit(xmlElement.getAttribute("concentrationUnit").getValue());
			if (xmlElement.getAttribute("concentrationUnitId") != null) {
				strDbl = xmlElement.getAttribute("concentrationUnitId").getValue();
				setConcentrationUnitId(strDbl.trim().isEmpty() ? null : Integer.parseInt(strDbl));
			}
			if (xmlElement.getAttribute("concentrationConfInterval") != null) {
				strDbl = xmlElement.getAttribute("concentrationConfInterval").getValue();
				setConcentrationStdDev(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			}
			if (xmlElement.getAttribute("numberOfMeasurements") != null) {
				strDbl = xmlElement.getAttribute("numberOfMeasurements").getValue();
				setNumberOfMeasurements(strDbl.trim().isEmpty() ? null : Integer.parseInt(strDbl));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getName() {return name;}
	public Double getTime() {return time;}
	public String getTimeUnit() {return timeUnit;}
	public Integer getTimeUnitId() {return timeUnitId;}
	public String getOrigTimeUnit() {return origTimeUnit;}
	public Integer getOrigTimeUnitId() {return origTimeUnitId;}
	public Double getConcentration() {return concentration;}	
	public String getConcentrationUnit() {return concentrationUnit;}
	public Integer getConcentrationUnitId() {return concentrationUnitId;}
	public String getOrigConcentrationUnit() {return origConcentrationUnit;}
	public Integer getOrigConcentrationUnitId() {return origConcentrationUnitId;}
	public Double getConcentrationStdDev() {return concentrationStdDev;}
	public Integer getNumberOfMeasurements() {return numberOfMeasurements;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setTime(Double time) {this.time = time;}
	public void setTimeUnit(String timeUnit) {this.timeUnit = (timeUnit == null) ? "" : timeUnit;}
	public void setTimeUnitId(Integer timeUnitId) {this.timeUnitId = timeUnitId;}
	public void setOrigTimeUnit(String origTimeUnit) {this.origTimeUnit = (origTimeUnit == null) ? "" : origTimeUnit;}
	public void setOrigTimeUnitId(Integer origTimeUnitId) {this.origTimeUnitId = origTimeUnitId;}
	public void setConcentration(Double concentration) {this.concentration = concentration;}
	public void setConcentrationUnit(String concentrationUnit) {this.concentrationUnit = (concentrationUnit == null) ? "" : concentrationUnit;}
	public void setConcentrationUnitId(Integer concentrationUnitId) {this.concentrationUnitId = concentrationUnitId;}
	public void setOrigConcentrationUnit(String origConcentrationUnit) {this.origConcentrationUnit = (origConcentrationUnit == null) ? "" : origConcentrationUnit;}
	public void setOrigConcentrationUnitId(Integer origConcentrationUnitId) {this.origConcentrationUnitId = origConcentrationUnitId;}
	public void setConcentrationStdDev(Double concentrationStdDev) {this.concentrationStdDev = concentrationStdDev;}
	public void setNumberOfMeasurements(Integer numberOfMeasurements) {this.numberOfMeasurements = numberOfMeasurements;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_TIMESERIES);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("time", "" + (time == null || Double.isNaN(time) ? "" : time));
		modelElement.setAttribute("timeUnit", timeUnit);
		modelElement.setAttribute("timeUnitId", "" + (timeUnitId == null ? "" : timeUnitId));
		modelElement.setAttribute("origTimeUnit", origTimeUnit);
		modelElement.setAttribute("origTimeUnitId", "" + (origTimeUnitId == null ? "" : origTimeUnitId));
		modelElement.setAttribute("concentration", "" + (concentration == null || Double.isNaN(concentration) ? "" : concentration));
		modelElement.setAttribute("concentrationUnit", concentrationUnit);
		modelElement.setAttribute("concentrationUnitId", "" + (concentrationUnitId == null ? "" : concentrationUnitId));
		modelElement.setAttribute("origConcentrationUnit", origConcentrationUnit);
		modelElement.setAttribute("origConcentrationUnitId", "" + (origConcentrationUnitId == null ? "" : origConcentrationUnitId));
		modelElement.setAttribute("concentrationStdDev", "" + (concentrationStdDev == null || Double.isNaN(concentrationStdDev) ? "" : concentrationStdDev));
		modelElement.setAttribute("numberOfMeasurements", "" + (numberOfMeasurements == null ? "" : numberOfMeasurements));
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Time");
        list.add("TimeUnit");
        list.add("TimeUnitId");
        list.add("OrigTimeUnit");
        list.add("OrigTimeUnitId");
        list.add("Concentration");
        list.add("ConcentrationUnit");
        list.add("ConcentrationUnitId");
        list.add("OrigConcentrationUnit");
        list.add("OrigConcentrationUnitId");
        list.add("ConcentrationStdDev");
        list.add("NumberOfMeasurements");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("time")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("timeUnit")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("timeUnitId")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("origTimeUnit")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("origTimeUnitId")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("concentration")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("concentrationUnit")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("concentrationUnitId")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("origConcentrationUnit")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("origConcentrationUnitId")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("concentrationStdDev")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("numberOfMeasurements")) {
			return IntCell.TYPE;
		}
		return null;
	}
}
