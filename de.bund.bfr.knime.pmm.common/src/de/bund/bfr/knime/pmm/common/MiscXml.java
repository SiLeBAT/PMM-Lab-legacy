package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class MiscXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_MISC = "misc";

	private Integer id;
	private String name = null;
	private String description = null;
	private Double value = null;
	private String unit = null;
	
	public MiscXml() {
		id = MathUtilities.getRandomNegativeInt();
	}
	public MiscXml(Integer id, String name, String description, Double value, String unit) {
		setID(id);
		setName(name);
		setDescription(description);
		setValue(value);
		setUnit(unit);
	}
	public MiscXml (Element xmlElement) {
		try {
			setID(Integer.parseInt(xmlElement.getAttribute("id").getValue()));
			setName(xmlElement.getAttribute("name").getValue());
			setDescription(xmlElement.getAttribute("description").getValue());
			String strDbl = xmlElement.getAttribute("value").getValue();
			setValue(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			setUnit(xmlElement.getAttribute("unit").getValue());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	public MiscXml(String xml) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new StringReader(xml));
			Element rootElement = doc.getRootElement();
			if (rootElement.getName().equals(ELEMENT_MISCXMLMODEL)) {
				for (Element el : rootElement.getChildren()) {
					if (el.getName().equals(ELEMENT_MISCXMLMODELENTRY)) {
						setElement(el);
						break;
					}			
				}			
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	public Integer getID() {return id;}
	public String getName() {return name;}
	public String getDescription() {return description;}
	public Double getValue() {return value;}
	public String getUnit() {return unit;}
	
	public void setID(Integer id) {this.id = (id == null) ? MathUtilities.getRandomNegativeInt() : id;}
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setDescription(String description) {this.description = (description == null) ? "" : description;}
	public void setValue(Double value) {this.value = (value == null) ? null : value;;}
	public void setUnit(String unit) {this.unit = (unit == null) ? "" : unit;}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("ID");
        list.add("Name");
        list.add("Description");
        list.add("Value");
        list.add("Unit");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("id")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("description")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("value")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("unit")) {
			return StringCell.TYPE;
		}
		return null;
	}
	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_MISC);
		modelElement.setAttribute("id", id.toString());
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("description", description);
		modelElement.setAttribute("value", "" + (value == null || Double.isNaN(value) ? "" : value));
		modelElement.setAttribute("unit", unit);
		return modelElement;
	}
}
