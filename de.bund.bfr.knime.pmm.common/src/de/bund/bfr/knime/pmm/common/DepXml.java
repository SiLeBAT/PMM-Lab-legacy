package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;

public class DepXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_DEPENDENT = "dependent";

	private String name = null;
	private String origName = null;
	private String category = null;
	private String unit = null;
	
	public DepXml(String name) {
		this(name, null, null);
	}
	public DepXml(String name, String category, String unit) {
		setName(name);
		setOrigName(name);
		setCategory(category);
		setUnit(unit);
	}
	public DepXml(Element xmlElement) {
		try {
			setName(xmlElement.getAttribute("name").getValue());
			setOrigName(xmlElement.getAttribute("origname").getValue());
			String strDbl = xmlElement.getAttribute("category").getValue().trim();
			setCategory(strDbl.isEmpty() ? null : strDbl);
			strDbl = xmlElement.getAttribute("unit").getValue().trim();
			setUnit(strDbl.isEmpty() ? null : strDbl);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getName() {return name;}
	public String getOrigName() {return origName;}
	public String getCategory() {return category;}
	public String getUnit() {return unit;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	private void setOrigName(String origName) {this.origName = (origName == null) ? "" : origName;}
	public void setCategory(String category) {this.category = category;}
	public void setUnit(String unit) {this.unit = unit;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_DEPENDENT);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("origname", origName);
		modelElement.setAttribute("category", category == null ? "" : category);
		modelElement.setAttribute("unit", unit == null ? "" : unit);
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Origname");
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
		else if (element.equalsIgnoreCase("category")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("unit")) {
			return StringCell.TYPE;
		}
		return null;
	}
}
