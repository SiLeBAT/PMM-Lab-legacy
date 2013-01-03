package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class CatalogModelXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_CATALOGMODEL = "catalogmodelxml";

	private Integer id;
	private String name = null;
	private String formula = null;
	
	public CatalogModelXml(Integer id, String name, String formula) {
		setID(id);
		setName(name);
		setFormula(formula);
	}
	public CatalogModelXml(Element xmlElement) {
		try {
			setID(Integer.parseInt(xmlElement.getAttribute("id").getValue()));
			setName(xmlElement.getAttribute("name").getValue());
			setFormula(xmlElement.getAttribute("formula").getValue());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Integer getID() {return id;}
	public String getName() {return name;}
	public String getFormula() {return formula;}
	
	public void setID(Integer id) {this.id = (id == null) ? MathUtilities.getRandomNegativeInt() : id;}
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setFormula(String formula) {this.formula = (formula == null) ? null : formula;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_CATALOGMODEL);
		modelElement.setAttribute("id", id.toString());
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("formula", formula);
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("ID");
        list.add("Name");
        list.add("Formula");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("id")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("formula")) {
			return StringCell.TYPE;
		}
		return null;
	}
}
