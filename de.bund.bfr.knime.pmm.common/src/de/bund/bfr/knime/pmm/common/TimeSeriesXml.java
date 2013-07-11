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
	private String origTimeUnit = null;
	private Double concentration = null;
	private String concentrationUnit = null;
	private String origConcentrationUnit = null;
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
			setOrigTimeUnit(xmlElement.getAttribute("origTimeUnit") == null ? null : xmlElement.getAttribute("origTimeUnit").getValue());
			strDbl = xmlElement.getAttribute("concentration").getValue();
			setConcentration(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			setConcentrationUnit(xmlElement.getAttribute("concentrationUnit").getValue());
			setOrigConcentrationUnit(xmlElement.getAttribute("origConcentrationUnit") == null ? null : xmlElement.getAttribute("origConcentrationUnit").getValue());
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
	public String getOrigTimeUnit() {return origTimeUnit;}
	public Double getConcentration() {return concentration;}	
	public String getConcentrationUnit() {return concentrationUnit;}
	public String getOrigConcentrationUnit() {return origConcentrationUnit;}
	public Double getConcentrationStdDev() {return concentrationStdDev;}
	public Integer getNumberOfMeasurements() {return numberOfMeasurements;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setTime(Double time) {this.time = time;}
	public void setTimeUnit(String timeUnit) {this.timeUnit = (timeUnit == null) ? "" : timeUnit;}
	public void setOrigTimeUnit(String origTimeUnit) {this.origTimeUnit = (origTimeUnit == null) ? "" : origTimeUnit;}
	public void setConcentration(Double concentration) {this.concentration = concentration;}
	public void setConcentrationUnit(String concentrationUnit) {this.concentrationUnit = (concentrationUnit == null) ? "" : concentrationUnit;}
	public void setOrigConcentrationUnit(String origConcentrationUnit) {this.origConcentrationUnit = (origConcentrationUnit == null) ? "" : origConcentrationUnit;}
	public void setConcentrationStdDev(Double concentrationStdDev) {this.concentrationStdDev = concentrationStdDev;}
	public void setNumberOfMeasurements(Integer numberOfMeasurements) {this.numberOfMeasurements = numberOfMeasurements;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_TIMESERIES);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("time", "" + (time == null || Double.isNaN(time) ? "" : time));
		modelElement.setAttribute("timeUnit", timeUnit);
		modelElement.setAttribute("origTimeUnit", origTimeUnit == null ? "" : origTimeUnit);
		modelElement.setAttribute("concentration", "" + (concentration == null || Double.isNaN(concentration) ? "" : concentration));
		modelElement.setAttribute("concentrationUnit", concentrationUnit);
		modelElement.setAttribute("origConcentrationUnit", origConcentrationUnit == null ? "" : origConcentrationUnit);
		modelElement.setAttribute("concentrationStdDev", "" + (concentrationStdDev == null || Double.isNaN(concentrationStdDev) ? "" : concentrationStdDev));
		modelElement.setAttribute("numberOfMeasurements", "" + (numberOfMeasurements == null ? "" : numberOfMeasurements));
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Time");
        list.add("TimeUnit");
        list.add("OrigTimeUnit");
        list.add("Concentration");
        list.add("ConcentrationUnit");
        list.add("OrigConcentrationUnit");
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
		else if (element.equalsIgnoreCase("origTimeUnit")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("concentration")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("concentrationUnit")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("origConcentrationUnit")) {
			return StringCell.TYPE;
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
