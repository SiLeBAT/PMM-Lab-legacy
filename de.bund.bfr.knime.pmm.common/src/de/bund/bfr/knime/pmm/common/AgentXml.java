package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class AgentXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_AGENT = "agent";

	private Integer id;
	private String name = null;
	private String detail = null;
	
	public AgentXml() {
		id = MathUtilities.getRandomNegativeInt();
	}
	public AgentXml(Integer id, String name, String detail) {
		setID(id);
		setName(name);
		setDetail(detail);
	}
	public AgentXml(Element xmlElement) {
		try {
			setID(Integer.parseInt(xmlElement.getAttribute("id").getValue()));
			setName(xmlElement.getAttribute("name").getValue());
			setDetail(xmlElement.getAttribute("detail").getValue());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getID() {return id;}
	public String getName() {return name;}
	public String getDetail() {return detail;}
	
	public void setID(Integer id) {this.id = (id == null) ? MathUtilities.getRandomNegativeInt() : id;}
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setDetail(String detail) {this.detail = (detail == null) ? "" : detail;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_AGENT);
		modelElement.setAttribute("id", id.toString());
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("detail", detail);
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("ID");
        list.add("Name");
        list.add("Detail");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("id")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("detail")) {
			return StringCell.TYPE;
		}
		return null;
	}
}
