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
	private String category = null;
	private String unit = null;
	private String dbuuid = null;
	
	public MiscXml(MiscXml misc) {
		this(misc.getID(),misc.getName(),misc.getDescription(),misc.getValue(),misc.getCategory(),misc.getUnit(),misc.getDbuuid());
	}
	
	public MiscXml(Integer id, String name, String description, Double value, String category, String unit) {
		this(id, name, description, value, category, unit, null);
	}
	public MiscXml() {
		this(MathUtilities.getRandomNegativeInt(), null, null, null, null, null, null);		
	}
	public MiscXml(Integer id, String name, String description, Double value, String category, String unit, String dbuuid) {
		setID(id);
		setName(name);
		setDescription(description);
		setValue(value);
		setCategory(category);
		setUnit(unit);
		setDbuuid(dbuuid);
	}
	public MiscXml (Element xmlElement) {
		try {
			setID(Integer.parseInt(xmlElement.getAttribute("id").getValue()));
			setName(xmlElement.getAttribute("name").getValue());
			setDescription(xmlElement.getAttribute("description").getValue());
			String strDbl = xmlElement.getAttribute("value").getValue();
			setValue(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			setCategory(xmlElement.getAttribute("category") == null ? null : xmlElement.getAttribute("category").getValue());
			setUnit(xmlElement.getAttribute("unit").getValue());
			if (xmlElement.getAttribute("dbuuid") != null) {
				setDbuuid(xmlElement.getAttribute("dbuuid").getValue());				
			}
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
	public String getCategory() {return category;}
	public String getUnit() {return unit;}
	public String getDbuuid() {return dbuuid;}
	
	public void setID(Integer id) {this.id = id;}
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setDescription(String description) {this.description = (description == null) ? "" : description;}
	public void setValue(Double value) {this.value = value;}
	public void setCategory(String category) {this.category = (category == null) ? "" : category;}
	public void setUnit(String unit) {this.unit = (unit == null) ? "" : unit;}
	public void setDbuuid(String dbuuid) {this.dbuuid = dbuuid;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_MISC);
		modelElement.setAttribute("id", "" + id);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("description", description);
		modelElement.setAttribute("value", "" + (value == null || Double.isNaN(value) ? "" : value));
		modelElement.setAttribute("category", category);
		modelElement.setAttribute("unit", unit);
		modelElement.setAttribute("dbuuid", dbuuid == null ? "" : dbuuid);
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("ID");
        list.add("Name");
        list.add("Description");
        list.add("Value");
        list.add("Category");
        list.add("Unit");
        list.add("Dbuuid");
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
		else if (element.equalsIgnoreCase("category")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("unit")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("dbuuid")) {
			return StringCell.TYPE;
		}
		return null;
	}
}
