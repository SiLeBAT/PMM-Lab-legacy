package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;

public class IndepXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_INDEP = "indep";

	private String name = null;
	private Double min = null;
	private Double max = null;
	
	public IndepXml() {
		
	}
	public IndepXml(String name, Double min, Double max) {
		setName(name);
		setMin(min);
		setMax(max);
	}
	public IndepXml(Element xmlElement) {
		try {
			setName(xmlElement.getAttribute("name").getValue());
			String strDbl = xmlElement.getAttribute("min").getValue();
			setMin(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("max").getValue();
			setMax(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getName() {return name;}
	public Double getMin() {return min;}
	public Double getMax() {return max;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setMin(Double min) {this.min = (min == null) ? null : min;}
	public void setMax(Double max) {this.max = (max == null) ? null : max;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_INDEP);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("min", "" + (min == null || Double.isNaN(min) ? "" : min));
		modelElement.setAttribute("max", "" + (max == null || Double.isNaN(max) ? "" : max));
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Min");
        list.add("Max");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("min")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("max")) {
			return DoubleCell.TYPE;
		}
		return null;
	}
}
