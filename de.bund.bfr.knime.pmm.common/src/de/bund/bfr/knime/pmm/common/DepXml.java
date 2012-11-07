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
	
	public DepXml(String name) {
		setName(name);
		setOrigName(name);
	}
	public DepXml(Element xmlElement) {
		try {
			setName(xmlElement.getAttribute("name").getValue());
			setOrigName(xmlElement.getAttribute("origname").getValue());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getName() {return name;}
	public String getOrigName() {return origName;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setOrigName(String origName) {this.origName = (origName == null) ? "" : origName;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_DEPENDENT);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("origname", origName);
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Origname");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("origname")) {
			return StringCell.TYPE;
		}
		return null;
	}
}
