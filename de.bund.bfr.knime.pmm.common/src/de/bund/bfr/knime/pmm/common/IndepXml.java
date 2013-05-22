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
	private String origName = null;
	private Double min = null;
	private Double max = null;
	private String category = null;
	private String unit = null;
	
	public IndepXml(String name, Double min, Double max) {
		this(name, min, max, null, null);
	}
	public IndepXml(String name, Double min, Double max, String category, String unit) {
		setName(name);
		setOrigName(name);
		setMin(min);
		setMax(max);
		setCategory(category);
		setUnit(unit);
	}
	public IndepXml(Element xmlElement) {
		try {
			setName(xmlElement.getAttribute("name").getValue());
			setOrigName(xmlElement.getAttribute("origname").getValue());
			String strDbl = xmlElement.getAttribute("min").getValue();
			setMin(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("max").getValue();
			setMax(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			if (xmlElement.getAttribute("category") != null) {
				setCategory(xmlElement.getAttribute("category").getValue());
			}
			if (xmlElement.getAttribute("unit") != null) {
				setUnit(xmlElement.getAttribute("unit").getValue());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getName() {return name;}
	public String getOrigName() {return origName;}
	public Double getMin() {return min;}
	public Double getMax() {return max;}
	public String getCategory() {return category;}
	public String getUnit() {return unit;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	private void setOrigName(String origName) {this.origName = (origName == null) ? "" : origName;}
	public void setMin(Double min) {this.min = (min == null) ? null : min;}
	public void setMax(Double max) {this.max = (max == null) ? null : max;}
	public void setCategory(String category) {this.category = (category == null) ? "" : category;}
	public void setUnit(String unit) {this.unit = (unit == null) ? "" : unit;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_INDEP);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("origname", origName);
		modelElement.setAttribute("min", "" + (min == null || Double.isNaN(min) ? "" : min));
		modelElement.setAttribute("max", "" + (max == null || Double.isNaN(max) ? "" : max));
		modelElement.setAttribute("category", category == null ? "" : category);
		modelElement.setAttribute("unit", unit == null ? "" : unit);
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Origname");
        list.add("Min");
        list.add("Max");
        list.add("Category");
        list.add("Unit");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("origname")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("min")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("max")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("category")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("unit")) {
			return StringCell.TYPE;
		}
		return null;
	}
}
