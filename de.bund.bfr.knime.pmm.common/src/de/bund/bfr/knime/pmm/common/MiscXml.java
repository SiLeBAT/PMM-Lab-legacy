package de.bund.bfr.knime.pmm.common;

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class MiscXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_MISC = "Misc";

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
			setID(Integer.parseInt(xmlElement.getAttribute("ID").getValue()));
			setName(xmlElement.getAttribute("NAME").getValue());
			setDescription(xmlElement.getAttribute("DESCRIPTION").getValue());
			String strDbl = xmlElement.getAttribute("VALUE").getValue();
			setValue(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
			setUnit(xmlElement.getAttribute("UNIT").getValue());
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
	public void setValue(Double value) {this.value = (value == null) ? Double.NaN : value;;}
	public void setUnit(String unit) {this.unit = (unit == null) ? "" : unit;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_MISC);
		modelElement.setAttribute("ID", id.toString());
		modelElement.setAttribute("NAME", name);
		modelElement.setAttribute("DESCRIPTION", description);
		modelElement.setAttribute("VALUE", "" + (value == null || Double.isNaN(value) ? "" : value));
		modelElement.setAttribute("UNIT", unit);
		return modelElement;
	}
}
