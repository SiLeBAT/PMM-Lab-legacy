package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

public class TimeSeriesXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_TIMESERIES = "timeseriesxml";

	private String name = null;
	private Double time = null;
	private Double log10C = null;
	private String comment = null;
	private Integer qualityScore = null;
	private Boolean checked = null;
	
	public TimeSeriesXml(String name, Double time, Double log10C) {
		setName(name);
		setTime(time);
		setLog10C(log10C);
	}
	public TimeSeriesXml(Element xmlElement) {
		try {
			setName(xmlElement.getAttribute("name").getValue());
			String strDbl = xmlElement.getAttribute("time").getValue();
			setTime(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("log10c").getValue();
			setLog10C(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			setComment(xmlElement.getAttribute("comment").getValue());
			String strInt = xmlElement.getAttribute("qualityScore").getValue();
			setQualityScore(strInt.trim().isEmpty() ? null : Integer.parseInt(strInt));
			String strBool = xmlElement.getAttribute("checked").getValue();
			setChecked(strBool.trim().isEmpty() ? null : Boolean.parseBoolean(strBool));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getName() {return name;}
	public Double getTime() {return time;}
	public Double getLog10C() {return log10C;}
	public String getComment() {return comment;}
	public Integer getQualityScore() {return qualityScore;}
	public Boolean getChecked() {return checked;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setTime(Double time) {this.time = (time == null) ? null : time;}
	public void setLog10C(Double log10C) {this.log10C = (log10C == null) ? null : log10C;}
	public void setComment(String comment) {this.comment = comment;}
	public void setQualityScore(Integer qualityScore) {this.qualityScore = qualityScore;}
	public void setChecked(Boolean checked) {this.checked = checked;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_TIMESERIES);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("time", "" + (time == null || Double.isNaN(time) ? "" : time));
		modelElement.setAttribute("log10c", "" + (log10C == null || Double.isNaN(log10C) ? "" : log10C));
		modelElement.setAttribute("comment", (comment == null ? "" : comment));
		modelElement.setAttribute("qualityScore", "" + (qualityScore == null ? "" : qualityScore));
		modelElement.setAttribute("checked", "" + (checked == null ? "" : checked));
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Time");
        list.add("Log10C");
        list.add("Comment");
        list.add("QualityScore");
        list.add("Checked");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("time")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("log10c")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("comment")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("qualityscore")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("checked")) {
			return BooleanCell.TYPE;
		}
		return null;
	}
}
