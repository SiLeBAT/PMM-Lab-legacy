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

	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_FORMULA = "formula";
	private static final String ATT_MODEL_CLASS = "modelClass";
	private static final String ATT_DBUUID = "dbuuid";

	private Integer id;
	private String name = null;
	private String formula = null;
	private Integer modelClass = null;
	private String dbuuid = null;

	public CatalogModelXml(Integer id, String name, String formula,
			Integer modelClass) {
		this(id, name, formula, modelClass, null);
	}

	public CatalogModelXml(Integer id, String name, String formula,
			Integer modelClass, String dbuuid) {
		setId(id == null ? MathUtilities.getRandomNegativeInt() : id);
		setName(name);
		setFormula(formula);
		setDbuuid(dbuuid);
	}

	public CatalogModelXml(Element el) {
		this(getInt(el, ATT_ID), getString(el, ATT_NAME), getString(el,
				ATT_FORMULA), getInt(el, ATT_MODEL_CLASS), getString(el,
				ATT_DBUUID));
	}

	private static String getString(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		return el.getAttributeValue(attr);
	}

	private static Integer getInt(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		try {
			return Integer.valueOf(el.getAttributeValue(attr));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	private static String getNonNull(String s) {
		if (s == null) {
			return "";
		}

		return s;
	}

	private static String getNonNull(Integer i) {
		if (i == null) {
			return "";
		}

		return i + "";
	}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_CATALOGMODEL);
		modelElement.setAttribute(ATT_ID, getNonNull(id));
		modelElement.setAttribute(ATT_NAME, getNonNull(name));
		modelElement.setAttribute(ATT_FORMULA, getNonNull(formula));
		modelElement.setAttribute(ATT_MODEL_CLASS, getNonNull(modelClass));
		modelElement.setAttribute(ATT_DBUUID, getNonNull(dbuuid));
		return modelElement;
	}

	public static List<String> getElements() {
		List<String> list = new ArrayList<String>();
		list.add(ATT_ID);
		list.add(ATT_NAME);
		list.add(ATT_FORMULA);
		list.add(ATT_DBUUID);
		return list;
	}

	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase(ATT_ID)) {
			return IntCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_NAME)) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_FORMULA)) {
			return StringCell.TYPE;
		} else if (element.equalsIgnoreCase(ATT_DBUUID)) {
			return StringCell.TYPE;
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Integer getModelClass() {
		return modelClass;
	}

	public void setModelClass(Integer modelClass) {
		this.modelClass = modelClass;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}
}
