package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class MdInfoXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_MDINFO = "mdinfoxml";

	private Integer id;
	private String name = null;
	private String comment = null;
	private Integer qualityScore = null;
	private Boolean checked = null;
	
	public MdInfoXml(Integer id, String name, String comment, Integer qualityScore, Boolean checked) {
		setID(id);
		setName(name);
	}
	public MdInfoXml(Element xmlElement) {
		try {
			setID(Integer.parseInt(xmlElement.getAttribute("id").getValue()));
			setName(xmlElement.getAttribute("name").getValue());
			if (xmlElement.getAttribute("comment") != null) {
				setComment(xmlElement.getAttribute("comment").getValue());				
			}
			if (xmlElement.getAttribute("qualityScore") != null) {
				String strInt = xmlElement.getAttribute("qualityScore").getValue();
				setQualityScore(strInt.trim().isEmpty() ? null : Integer.parseInt(strInt));				
			}
			if (xmlElement.getAttribute("checked") != null) {
				String strBool = xmlElement.getAttribute("checked").getValue();
				setChecked(strBool.trim().isEmpty() ? null : Boolean.parseBoolean(strBool));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Integer getID() {return id;}
	public String getName() {return name;}
	public String getComment() {return comment;}
	public Integer getQualityScore() {return qualityScore;}
	public Boolean getChecked() {return checked;}
	
	public void setID(Integer id) {this.id = (id == null) ? MathUtilities.getRandomNegativeInt() : id;}
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setComment(String comment) {this.comment = comment;}
	public void setQualityScore(Integer qualityScore) {this.qualityScore = qualityScore;}
	public void setChecked(Boolean checked) {this.checked = checked;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_MDINFO);
		modelElement.setAttribute("id", id.toString());
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("comment", (comment == null ? "" : comment));
		modelElement.setAttribute("qualityScore", "" + (qualityScore == null ? "" : qualityScore));
		modelElement.setAttribute("checked", "" + (checked == null ? "" : checked));
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("ID");
        list.add("Name");
        list.add("Comment");
        list.add("QualityScore");
        list.add("Checked");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("id")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
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
