package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;

public class TimeSeriesXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_TIMESERIES = "timeseriesxml";

	private String name = null;
	private Double time = null;
	private String timeUnit = null;
	private Double concentration = null;
	private String concentrationUnit = null;
	private Double concentrationConfInterval = null;
	
	public TimeSeriesXml(String name, Double time, String timeUnit, Double concentration, String concentrationUnit, Double concentrationConfInterval) {
		setName(name);
		setTime(time);
		setTimeUnit(timeUnit);
		setConcentration(concentration);
		setConcentrationUnit(concentrationUnit);
		setConcentrationConfInterval(concentrationConfInterval);
	}
	public TimeSeriesXml(Element xmlElement) {
		try {
			setName(xmlElement.getAttribute("name").getValue());
			String strDbl = xmlElement.getAttribute("time").getValue();
			setTime(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			setTimeUnit(xmlElement.getAttribute("timeUnit") == null ? null : xmlElement.getAttribute("timeUnit").getValue());
			strDbl = xmlElement.getAttribute("concentration").getValue();
			setConcentration(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			setConcentrationUnit(xmlElement.getAttribute("concentrationUnit").getValue());
			if (xmlElement.getAttribute("concentrationConfInterval") != null) {
				strDbl = xmlElement.getAttribute("concentrationConfInterval").getValue();
				setConcentrationConfInterval(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getName() {return name;}
	public Double getTime() {return time;}
	public String getTimeUnit() {return timeUnit;}
	public Double getConcentration() {return concentration;}	
	public String getConcentrationUnit() {return concentrationUnit;}
	public Double getConcentrationConfInterval() {return concentrationConfInterval;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setTime(Double time) {this.time = time;}
	public void setTimeUnit(String timeUnit) {this.timeUnit = (timeUnit == null) ? "" : timeUnit;}
	public void setConcentration(Double concentration) {this.concentration = concentration;}
	public void setConcentrationUnit(String concentrationUnit) {this.concentrationUnit = (concentrationUnit == null) ? "" : concentrationUnit;}
	public void setConcentrationConfInterval(Double concentrationConfInterval) {this.concentrationConfInterval = concentrationConfInterval;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_TIMESERIES);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("time", "" + (time == null || Double.isNaN(time) ? "" : time));
		modelElement.setAttribute("timeUnit", timeUnit);
		modelElement.setAttribute("concentration", "" + (concentration == null || Double.isNaN(concentration) ? "" : concentration));
		modelElement.setAttribute("concentrationUnit", concentrationUnit);
		modelElement.setAttribute("concentrationConfInterval", "" + (concentrationConfInterval == null || Double.isNaN(concentrationConfInterval) ? "" : concentrationConfInterval));
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Time");
        list.add("TimeUnit");
        list.add("Concentration");
        list.add("ConcentrationUnit");
        list.add("ConcentrationConfInterval");
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
		else if (element.equalsIgnoreCase("concentration")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("concentrationUnit")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("concentrationConfInterval")) {
			return DoubleCell.TYPE;
		}
		return null;
	}
}
