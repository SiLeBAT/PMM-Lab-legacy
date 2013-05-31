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
	private String dbuuid = null;
	
	public AgentXml() {
		this(MathUtilities.getRandomNegativeInt(), null, null, null);		
	}
	public AgentXml(Integer id, String name, String detail) {
		this(id, name, detail, null);
	}
	public AgentXml(Integer id, String name, String detail, String dbuuid) {
		setID(id);
		setName(name);
		setDetail(detail);
		setDbuuid(dbuuid);
	}
	public AgentXml(Element xmlElement) {
		try {
			if (xmlElement.getAttribute("id") != null && !xmlElement.getAttribute("id").getValue().equalsIgnoreCase("null")) {
				setID(Integer.parseInt(xmlElement.getAttribute("id").getValue()));				
			}
			setName(xmlElement.getAttribute("name").getValue());
			setDetail(xmlElement.getAttribute("detail").getValue());
			if (xmlElement.getAttribute("dbuuid") != null) {
				setDbuuid(xmlElement.getAttribute("dbuuid").getValue());				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getID() {return id;}
	public String getName() {return name;}
	public String getDetail() {return detail;}
	public String getDbuuid() {return dbuuid;}
	
	public void setID(Integer id) {this.id = id;}
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setDetail(String detail) {this.detail = (detail == null) ? "" : detail;}
	public void setDbuuid(String dbuuid) {this.dbuuid = dbuuid;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_AGENT);
		modelElement.setAttribute("id", "" + id);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("detail", detail);
		modelElement.setAttribute("dbuuid", dbuuid == null ? "" : dbuuid);
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("ID");
        list.add("Name");
        list.add("Detail");
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
		else if (element.equalsIgnoreCase("detail")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("dbuuid")) {
			return StringCell.TYPE;
		}
		return null;
	}
}
